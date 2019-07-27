package chap5genericsfunctions

/** A re-implementation of Scala's [[Either]] type */
sealed trait Sum[A, B] {

  def fold[C](fail: A => C)(pass: B => C): C =
    this match {
      case Fail(value) => fail(value)
      case Pass(value) => pass(value)
    }

  /** Transforms a pass and leaves a fail unaffected */
  def map[C](fn: B => C): Sum[A, C] =
    this match {
      case Fail(v) => Fail(v)
      case Pass(v) => Pass(fn(v))
    }

  /** Transforms a pass and leaves a fail unaffected */
  def flatMap[C](fn: B => Sum[A, C]): Sum[A, C] =
    this match {
      case Fail(v) => Fail(v)
      case Pass(v) => fn(v)
    }
}

final case class Fail[A, B](value: A) extends Sum[A, B]
final case class Pass[A, B](value: B) extends Sum[A, B]
