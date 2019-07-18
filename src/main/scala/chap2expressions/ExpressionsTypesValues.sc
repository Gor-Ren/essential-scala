"Hello world!"

"Hello world!".toUpperCase

// Exercises
1 + 2
(1 + 2).getClass

"3".toInt

// Infix Operator Notation
// Any expression written a.b(c) can also be written a b c

// But N.B. a b c d e == a.b(c).d(e), which is the shortest valid
// interpretation

42 // Int
42.0 // Double
42.0f // Float
42L // Long

'a' // Char

// at runtime Scala classes are equivalent to Java's primitives
// e.g. 42 Int is indistinguishable to Java's 42 int

null
// null has type Null; it's use is strongly discouraged

()
// Unit; equivalent to Java's void


// 2.4.3
object Test {
  // method vs field
  // a field gives a name to a value
  // a method gives a name to a computation that produces a value
  val simpleField = {
    println("eval simpleField")
    42
  }
  def simpleMethod = {
    // can be referenced just like simpleField due to no params
    println("eval simpleMethod")
    42
  }
}

// definition of Test object doesn't cause any prints!
// because of lazy loading
Test
// the field is not re-evaluated again once a value is calculated
Test.simpleField  // no print

// calls to simpleMethod will always re-compute the value
Test.simpleMethod
Test.simpleMethod  // prints again

// 2.4.5 Exercises
object Calc {
  def square(n: Double): Double = n * n

  def cube(n: Double): Double = square(n) * n
}

// we can't assign a field to a parameterised method
// although there is some stuff not covered yet that lets us convert method to a function:
val x = Calc.square(_)

// in summary: a method is not an expression, but a call to a method is.

// tests (without any external test library)
assert(Calc.square(2.0) == 4.0)
assert(Calc.square(3.0) == 9.0)
assert(Calc.square(-2.0) == 4.0)

// skipped some simple stuff

// cool: blocks can be used for intermediate vals when assigning
val name: String = {
  val first = "Gor"
  val family = "Ren"
  first + " " + family
}