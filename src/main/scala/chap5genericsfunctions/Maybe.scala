package chap5genericsfunctions

sealed trait Maybe[+A] {

  def fold[B](empty: B)(full: A => B): B =
    this match {
      case Empty       => empty
      case Full(value) => full(value)
    }

  def map[B](fn: A => B): Maybe[B] =
    this match {
      case Empty       => Empty
      case Full(value) => Full(fn(value))
    }

  def flatMap[B](fn: A => Maybe[B]): Maybe[B] =
    this match {
      case Empty       => Empty
      case Full(value) => fn(value)
    }

  /* For bonus points, implement map in terms of flatMap */
  def mapUsingFlatMap[B](fn: A => B): Maybe[B] = {
    this.flatMap(x => Full(fn(x)))
  }

  def mapUsingFold[B](fn: A => B): Maybe[B] = {
    fold(Empty: Maybe[B])(v => Full(fn(v)))
  }
}

case object Empty extends Maybe[Nothing]
final case class Full[A](value: A) extends Maybe[A]
