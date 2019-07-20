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
