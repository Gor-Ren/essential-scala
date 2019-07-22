package chap4traits.extendedexercises

sealed trait Expression {

  // we will learn some tools next chapter to simplify all this logic
  def eval: Result =
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
          case (Failure(m), _)            => Failure(m)
          case (_, Failure(m))            => Failure(m)
          case (Success(0), Success(0))   => Failure("0 div 0 is undefined")
          case (Success(_), Success(0))   => Failure("division by 0")
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

sealed trait Result
final case class Failure(message: String) extends Result
final case class Success(result: Double) extends Result

object Tests extends App {
  // tests
  assert(
    Addition(SquareRoot(Number(-1.0)), Number(2.0)).eval ==
      Failure("square root arg must be non-negative")
  )
  assert(Addition(SquareRoot(Number(4.0)), Number(2.0)).eval == Success(4.0))
  assert(Division(Number(4), Number(0)).eval == Failure("division by 0"))
}
