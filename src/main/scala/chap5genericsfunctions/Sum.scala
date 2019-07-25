package chap5genericsfunctions

/** A re-implementation of Scala's [[Either]] type */
sealed trait Sum[A, B] {

  def fold[C](left: A => C)(right: B => C): C =
    this match {
      case Left(value)  => left(value)
      case Right(value) => right(value)
    }
}

final case class Left[A, B](value: A) extends Sum[A, B]
final case class Right[A, B](value: B) extends Sum[A, B]
