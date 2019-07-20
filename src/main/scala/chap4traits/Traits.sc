import java.time.{Duration, Instant}

// Traits are templates for creating classes, analogous to classes being
// templates for creating objects.

// Traits express that two or more classes implement the same operations
// i.e. that they share a common supertype

// Traits are similar to Java 8's interfaces with default methods

sealed trait Visitor {
  def id: String
  def createdAt: Instant

  def age: Duration = Duration.between(createdAt, Instant.now())
}

final case class Anonymous(id: String, createdAt: Instant = Instant.now())
  extends Visitor

final case class User(id: String, email: String, createdAt: Instant = Instant.now())
  extends Visitor

// Traits differ from classes in that:
//    traits cannot be instantiated
//    traits can define abstract methods
def older(v1: Visitor, v2: Visitor): Boolean = {
  v1.createdAt.isBefore(v2.createdAt)
}

// a trait which declares an abstract, paramaterless def can be implemented
// with a sublcasses' val because of Scala's uniform access principle

// you should always use def over val in a trait to give implementors the
// choice of using a val or def

// we can mark a trait as `sealed` to improve safety, enforced by the compiler
// we must define all subclasses of a sealed trait in that file
// the compiler will emit warnings for any unhandled matches

// we should mark sealed trait subclasses as final to avoid them being
// subclassed elsewhere, compromising our compile-time safety


// Exercise 4.1.4.1
trait Feline {
  def colour: String
  def sound: String
}

abstract class BigCat extends Feline {
  override val sound: String = "roar"
}

case class Panther(colour: String) extends BigCat {
}

case class Lion(colour: String, maneSize: Int) extends BigCat {
}

case class Tiger(colour: String) extends BigCat

case class Cat(colour: String, favouriteFood: String) extends Feline {
  override val sound: String = "meow"
}

// we could have defined a default value for sound in Feline, but this is bad
// practice when the value is not suitable for all subtypes.
// instead we made a common supertype BigCat

// exercise 4.2.2.2
sealed trait Colour {
  val MAX_VALUE: Int = 255

  def red: Int
  def green: Int
  def blue: Int

  /** Returns true if this colour is light, else false.
    *
    * A light colour is defined as one having at least one third of the maximum
    * possible brightness of its three component colours.
    */
  def isLight: Boolean = ((red + green + blue) / 3) > (MAX_VALUE / 3)
}

sealed abstract class BaseColour(val red: Int, val green: Int, val blue: Int)
  extends Colour {}

case object Red extends BaseColour(255, 0, 0)
case object Yellow extends BaseColour(255, 255, 0)
case object Pink extends BaseColour(255, 192, 203)

final case class RgbColour(override val red: Int,
                     override val green: Int,
                     override val blue: Int)
  extends BaseColour(red, green, blue)


sealed trait Shape {
  def sides: Int
  def perimeter: Double
  def area: Double
  def colour: Colour
}

final case class Circle(radius: Double, colour: Colour) extends Shape {
  override val sides: Int = 1
  override def perimeter: Double = math.Pi * radius * 2
  override def area: Double = math.Pi * math.pow(radius, 2)
}

sealed trait Rectangular extends Shape {
  def length: Double
  def width: Double

  override val sides: Int = 4
  override def perimeter: Double = length*2 + width*2
  override def area: Double = length * width
}

final case class Rectangle(length: Double, width: Double, colour: Colour)
  extends Rectangular {}

final case class Square(size: Double, colour: Colour) extends Rectangular {
  override val width: Double = size
  override val length: Double = size
}


// pattern matching against sealed trait
object Draw {
  def apply(s: Shape): String = {
    val shapeDetails: String = s match {
        case Circle(r, _) => s"circle of radius $r units"
        case Square(l, _) => s"square with sides of length $l units"
        case Rectangle(l, w, _) => s"rectangle with sides of length $l units " +
          s"and width $w units"
    }
    s"A ${Draw(s.colour)} $shapeDetails"
  }

  def apply(c: Colour): String = c match {
    case Red => "red"
    case Yellow => "yellow"
    case Pink => "pink"
    case _ => if(c.isLight) "light" else "dark"
  }
}

Draw(Circle(3.2, Pink))
Draw(Rectangle(1.0, 2.0, RgbColour(0, 0, 0)))
Draw(Square(9.9, RgbColour(255, 255, 255)))

// exercise 4.2.2.4 Division
sealed trait DivisionResult

final case class Finite(x: Double) extends DivisionResult
case object Infinite extends DivisionResult
case object Undefined extends DivisionResult

object Divide {
  /** Divides the first arg by the second arg.
    *
    * @param x the dividend
    * @param d the divisor
    * @return a [[Finite]] containing the result, [[Undefined]] if both args
    *         are zero, or [[Infinite]] if the second arg only is zero
    */
  def apply(x: Int, d: Int): DivisionResult = (x, d) match {
    case (0, 0) => Undefined
    case (_, 0) => Infinite
    case (a, b) => Finite(a.toDouble / b.toDouble)
  }
}

Divide(0, 0)
Divide(10, 0)
Divide(13, 3)
