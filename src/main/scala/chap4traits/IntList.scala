package chap4traits

sealed trait IntList {

  def fold[A](end: A)(f: (Int, A) => A): A =
    this match {
      case IntEnd          => end
      case IntPair(hd, tl) => f(hd, tl.fold(end)(f))
    }

  def sum: Int = fold(0)((hd, tl) => hd + tl)
  def product: Int = fold(1)((hd, tl) => hd * tl)
  def length: Int = fold(0)((_, tl) => 1 + tl)

  def double: IntList = fold(IntEnd: IntList)((hd, tl) => IntPair(2 * hd, tl))
}

case object IntEnd extends IntList
final case class IntPair(head: Int, tail: IntList) extends IntList
