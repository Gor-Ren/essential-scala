package chap5genericsfunctions

/* Generic linked list */
sealed trait LinkedList[A] {

  def length: Int =
    this match {
      case End()         => 0
      case Pair(_, tail) => 1 + tail.length
    }

  def contains(element: A): Boolean =
    this match {
      case End()        => false
      case Pair(hd, tl) => (element == hd) || tl.contains(element)
    }

  // unsafe for neg numbers
  def apply(i: Int): Result[A] =
    (i, this) match {
      case (_, End())       => Failure("Index out of bounds!")
      case (0, Pair(hd, _)) => Success(hd)
      case (_, Pair(_, tl)) => tl(i - 1)
    }

  // abstracting over sum, length and product
  def fold[B](end: B, f: (A, B) => B): B =
    this match {
      case End()        => end
      case Pair(hd, tl) => f(hd, tl.fold(end, f))
    }

  def map[B](fn: A => B): LinkedList[B] =
    this match {
      case End()        => End()
      case Pair(hd, tl) => Pair(fn(hd), tl.map(fn))
    }
}

final case class End[A]() extends LinkedList[A]

final case class Pair[A](head: A, tail: LinkedList[A]) extends LinkedList[A]

sealed trait Result[A]
final case class Success[A](result: A) extends Result[A]
final case class Failure[A](message: String) extends Result[A]

// tests
object LinkedListTest extends App {
  // length tests
  val example = Pair(1, Pair(2, Pair(3, End())))
  assert(example.length == 3)
  assert(example.tail.length == 2)
  assert(End().length == 0)

  // contains tests
  val example2 = Pair(1, Pair(2, Pair(3, End())))
  assert(example2.contains(3))
  assert(!example2.contains(4))
  assert(!End().contains(0))

  // index apply tests
  assert(example(0) == Success(1))
  assert(example(1) == Success(2))
  assert(example(2) == Success(3))
  assert(example(3) == Failure("Index out of bounds!"))
}
