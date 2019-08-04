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

implicit val rationalOrd: Ordering[Rational] = Ordering.fromLessThan((x, y) =>
  x.toDouble < y.toDouble
)

assert(List(Rational(1, 2), Rational(3, 4), Rational(1, 3)).sorted ==
  List(Rational(1, 3), Rational(1, 2), Rational(3, 4)))
