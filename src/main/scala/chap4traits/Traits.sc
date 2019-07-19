import java.time.{Duration, Instant}

// Traits are templates for creating classes, analogous to classes being
// templates for creating objects.

// Traits express that two or more classes implement the same operations
// i.e. that they share a common supertype

// Traits are similar to Java 8's interfaces with default methods

trait Visitor {
  def id: String
  def createdAt: Instant

  def age: Duration = Duration.between(createdAt, Instant.now())
}

case class Anonymous(id: String, createdAt: Instant = Instant.now())
  extends Visitor

case class User(id: String, email: String, createdAt: Instant = Instant.now())
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

// Exercise 4.1.4.1
trait Feline {
  def colour: String
  def sound: String
}

case class Panther(colour: String) extends Feline {
  val sound: String = "roar"
}

case class Cat(colour: String, favouriteFood: String) extends Feline {
  val sound: String = "meow"
}

case class Lion(colour: String, maneSize: Int) extends Feline {
  val sound: String = "roar"
}

// we could have defined a default value for sound in Feline, but this is bad
// practice when the value is not suitable for all subtypes

