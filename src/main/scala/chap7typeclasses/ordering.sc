// we start with an example of type class usage before describing them
val maxOrdering = Ordering.fromLessThan[Int](_ > _)
val testList = List(3, 1, 2)

testList.sorted(maxOrdering)

/* Implicits */
// we cab use implicit vals to tell the compiler to pass it for us
implicit val ordering: Ordering[Int] = Ordering.fromLessThan[Int](_ < _)

testList.sorted  // uses the implicit ordering
// this works because the ordering param on sorted was declared as implicit

// N.B. the compiler will emit an error if there is ambiguity about which
// implicit to use - there must be exactly one

/* Exercises */
val absOrdering = Ordering.fromLessThan[Int](math.abs(_) < math.abs(_))

assert(List(-4, -1, 0, 2, 3).sorted(absOrdering) == List(0, -1, 2, 3, -4))
assert(List(-4, -3, -2, -1).sorted(absOrdering) == List(-1, -2, -3, -4))

// rational orderings
final case class Rational(numerator: Int, denominator: Int) {
  def toDouble: Double = numerator.toDouble / denominator.toDouble
}

object Rational {
  implicit val rationalOrd: Ordering[Rational] = Ordering.fromLessThan((x, y) =>
    x.toDouble < y.toDouble
  )
}

assert(List(Rational(1, 2), Rational(3, 4), Rational(1, 3)).sorted ==
  List(Rational(1, 3), Rational(1, 2), Rational(3, 4)))

/* Implicit scope */
// Local scope: the compiler first looks in the local scope:
// local values, within an enclosing class/object/trait, and imports.

// With lower priority, it looks in the companion objects of the type class and
// of the type parameter.

// The compiler only emits an ambiguity error if there are two possible
// implicit values with the same priority. E.g. we can override a companion's
// ordering by defining/importing our own in the local scope.

// Prefer to put type class instances (such as Ordering) in the companion class
// if possible. It is possible when:
//  - there is a single type class instance for the type
//  - you can edit the source code for the type that the instance is being
//    defined for
// Then users may override this sensible default using local scope if required.

/* Exercises */
final case class Order(units: Int, unitPrice: Double) {
  val totalPrice: Double = units * unitPrice
}

// we will package 3 orderings in their own objects to allow specific imports
object Order {
  // ordering by total price of order seems like a reasonable default -> goes
  // in companion
  implicit val totalPriceOrdering: Ordering[Order] =
    Ordering.fromLessThan(_.totalPrice < _.totalPrice)
}

// other orderings in separate objects to allow importing
object OrderUnitsOrdering {
  implicit val unitsOrdering: Ordering[Order] = Ordering.fromLessThan(_.units < _.units)
}

object OrderUnitPriceOrdering {
  implicit val unitPriceOrdering: Ordering[Order] = Ordering.fromLessThan(_.unitPrice < _.unitPrice)
}
