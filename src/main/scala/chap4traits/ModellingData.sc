/* Modelling Data */

/* has-a and */

// If A has a B and a C, we model using a product type:
// case class A(b: B, c: C)
// or
/*
  trait A {
     def b: B
     def c: C
 }
 */

/* is-a or */

// If A is a B or a C, we model using a sum type:
/*
  sealed trait A
  final case class B() extends A
  final case class C() extends A
 */

// what about the other two types? has-a/is-a, and/or
// turns out we can create them as combinations of is-a or and has-a and

/* is-a and */

/*
  trait B
  trait C
  trait A extends B with C
 */
// often it is better to use type classes (covered later) instead of
// extending multiple interfaces

/* has-a or */

// we have two options:
// 1. A has a D, and D is a B or a C
/*
  trait A {
    def d: D
  }
  sealed trait D
  final case class B() extends D
  final case class C() extends D
 */

// 2. A is a D or an E, and a D has a B while an E has a C
/*
  sealed trait A
  final case class D(b: B) extends A
  final case class E(c: C) extends A
 */

// Exercises
sealed trait TrafficLight
case object RedLight extends TrafficLight
case object GreenLight extends TrafficLight
case object YellowLight extends TrafficLight

sealed trait CalculationResult
final case class Result(result: Int) extends CalculationResult
final case class Error(message: String) extends CalculationResult

sealed trait WaterSource
case object Well extends WaterSource
case object Spring extends WaterSource
case object Tap extends WaterSource

case class BottledWater(size: Int, source: WaterSource, isCarbonated: Boolean)
// structural recursion can use either polymorphism (methods act on members) or
// pattern matching (function receives instance and calculates with its members
// by retrieving them through pattern matching)

// example using a simplified Feline hierarchy from earlier
sealed trait Feline {}
final case class Lion() extends Feline
final case class Tiger() extends Feline
final case class Panther() extends Feline
final case class Cat(favouriteFood: String) extends Feline

sealed trait Food
case object Antelope extends Food
case object TigerFood extends Food
case object Licorice extends Food
final case class CatFood(food: String) extends Food

// we could map felines to their preferred food using:
//   1. polymorphism (a method in each subclass)
//   2. pattern matching in a method in the trait Feline
//   3. pattern matching in an external object (e.g. Dinner below)

/** An object mapping cats to their preferred food. */
object Dinner {

  def dinner(cat: Feline): Food =
    cat match {
      case Lion()         => Antelope
      case Tiger()        => TigerFood
      case Panther()      => Licorice
      case Cat(favourite) => CatFood(favourite)
    }
}

// traffic light exercise
object TrafficLightControls {

  /** Returns the next traffic light state.
    *
    * The sequence is GREEN -> YELLOW -> RED -> GREEN -> ...
    */
  def next(light: TrafficLight): TrafficLight =
    light match {
      case GreenLight  => YellowLight
      case YellowLight => RedLight
      case RedLight    => GreenLight
    }
}

// 4.5.6.2 Calculations
sealed trait Calculation
final case class Success(result: Int) extends Calculation
final case class Failure(message: String) extends Calculation

object Calculator {
  def +(calc: Calculation, x: Int): Calculation =
    calc match {
      case Success(value) => Success(value + x)
      case f: Failure => f
    }
  def -(calc: Calculation, x: Int): Calculation =
    calc match {
      case Success(value) => Success(value - x)
      case f: Failure => f
    }
  def /(calc: Calculation, x: Int): Calculation =
    calc match {
      case Success(value) =>
        x match {
          case 0 => Failure("Division by zero")
          case _ => Success(value / x)
        }
      case f: Failure => f
    }
}

// tests
assert(Calculator.+(Success(1), 1) == Success(2))
assert(Calculator.-(Success(1), 1) == Success(0))
assert(Calculator.+(Failure("Badness"), 1) == Failure("Badness"))

assert(Calculator./(Success(4), 2) == Success(2))
assert(Calculator./(Success(4), 0) == Failure("Division by zero"))
assert(Calculator./(Failure("Badness"), 0) == Failure("Badness"))


/* Recursive Data Types */
sealed trait IntList

case object End extends IntList
final case class IntsPair(head: Int, tail: IntList) extends IntList

// exercise
def sum(list: IntList): Int =
  list match {
    case IntsPair(h, t) => h + sum(t)
    case End => 0  // the base case - returns the identity of the function
  }

// the identity is an element that doesn't change the result:
//   a + 0 = a
//   a * 1 = a
//   a ^ 1 = a
// etc.

val example = IntsPair(1, IntsPair(2, IntsPair(3, End)))
assert(sum(example) == 6)
assert(sum(example.tail) == 5)
assert(sum(End) == 0)

def length(list: IntList): Int =
  list match {
    case End => 0
    case IntsPair(_, tail) => 1 + length(tail)
  }

// length tests
assert(length(example) == 3)
assert(length(example.tail) == 2)
assert(length(End) == 0)

def double(list: IntList): IntList =
  list match {
    case End => End
    case IntsPair(hd, tl) => IntsPair(hd * 2, double(tl))
  }

assert(double(example) == IntsPair(2, IntsPair(4, IntsPair(6, End))))
assert(double(example.tail) == IntsPair(4, IntsPair(6, End)))
assert(double(End) == End)

// Tree exercise
sealed trait IntTree {
  def sum: Int
  def double: IntTree
}
final case class Leaf(value: Int) extends IntTree {
  override def sum: Int = value
  override def double: Leaf = Leaf(value * 2)
}
final case class IntNode(left: IntTree, right: IntTree) extends IntTree {
  override def sum: Int = left.sum + right.sum
  override def double: IntTree = IntNode(left.double, right.double)
}

object TreeOperations {
  // same functionality using pattern matching
  def sum(tree: IntTree): Int =
    tree match {
      case IntNode(left, right) => sum(left) + sum(right)
      case Leaf(value) => value
    }
  def double(tree: IntTree): IntTree =
    tree match {
      case IntNode(left, right) => IntNode(double(left), double(right))
      case Leaf(value) => Leaf(value * 2)
    }
}
