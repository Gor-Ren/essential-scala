package chap4traits.extendedexercises

import chap5genericsfunctions.{Fail, Failure, Pass, Result, Success, Sum}

sealed trait Expression {

  // we will learn some tools next chapter to simplify all this logic
  def eval: Result[Double] =
    this match {
      case Number(x) => Success(x)
      case Addition(l, r) =>
        (l.eval, r.eval) match {
          case (Failure(m), _)            => Failure(m)
          case (_, Failure(m))            => Failure(m)
          case (Success(v1), Success(v2)) => Success(v1 + v2)
        }
      case Subtraction(l, r) =>
        (l.eval, r.eval) match {
          case (Failure(m), _)            => Failure(m)
          case (_, Failure(m))            => Failure(m)
          case (Success(v1), Success(v2)) => Success(v1 - v2)
        }
      case Division(l, r) =>
        (l.eval, r.eval) match {
          case (Failure(m), _) => Failure(m)
          case (_, Failure(m)) => Failure(m)
          case (Success(0), Success(0)) =>
            Failure("0 div 0 is undefined")
          case (Success(_), Success(0)) =>
            Failure("division by 0")
          case (Success(v1), Success(v2)) => Success(v1 / v2)
        }
      case SquareRoot(x) =>
        x.eval match {
          case Failure(m) => Failure(m)
          case Success(v) =>
            if (v >= 0)
              Success(math.sqrt(v))
            else
              Failure("square root arg must be non-negative")
        }
    }

  // an eval implementation for chap 5 using Sum type
  def eval5: Sum[String, Double] =
    this match {
      case Number(x) => Pass(x)
      case SquareRoot(v) =>
        v.eval5.flatMap(
          x =>
            if (x < 0)
              Fail("square root of negative number")
            else
              Pass(math.sqrt(x))
        )
      case Addition(l, r)    => failFastBinaryOp(l, r, (l, r) => Pass(l + r))
      case Subtraction(l, r) => failFastBinaryOp(l, r, (l, r) => Pass(l - r))
      case Division(l, r) =>
        failFastBinaryOp(l, r, (l, r) => {
          (l, r) match {
            case (0, 0)          => Fail("Zero divided by zero is undefined")
            case (_, 0)          => Fail("Divide by zero")
            case (vLeft, vRight) => Pass(vLeft / vRight)
          }
        })
    }

  /** Evaluates `fn` applied to `l` and `r`, preserving any failures.
    *
    * If `l` or `r` are a [[Fail]], returns that [[Fail]] (with `l` given priority)
    * else returns `fn(l, r)`.
    *
    * @param fn the function to be applied
    * @param l the left expression
    * @param r the right expression
    * @return a failure from left, a failure from right, or `fn(l, r)` in that
    *         order of priority
    */
  def failFastBinaryOp(
      l: Expression,
      r: Expression,
      fn: (Double, Double) => Sum[String, Double]
  ): Sum[String, Double] = {
    l.eval5.flatMap(
      left =>
        r.eval5.flatMap(
          right => fn(left, right)
        )
    )
  }
}

/** Adds the left expression to the right. */
final case class Addition(left: Expression, right: Expression)
    extends Expression

/** Subtracts right from left */
final case class Subtraction(left: Expression, right: Expression)
    extends Expression

/** Represents a numerical value as a [[Double]] */
final case class Number(value: Double) extends Expression

/** Divides left by right. */
final case class Division(left: Expression, right: Expression)
    extends Expression

/** Represents a square root operation on input value. */
final case class SquareRoot(x: Expression) extends Expression

object Tests extends App {
  // tests
  assert(
    Addition(SquareRoot(Number(-1.0)), Number(2.0)).eval ==
      Failure("square root arg must be non-negative")
  )
  assert(
    Addition(SquareRoot(Number(4.0)), Number(2.0)).eval == Success(4.0)
  )
  assert(Division(Number(4), Number(0)).eval == Failure("division by 0"))
}
