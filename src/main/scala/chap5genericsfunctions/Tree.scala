package chap5genericsfunctions

sealed trait Tree[A] {

  def fold[B](leafF: A => B)(nodeF: (B, B) => B): B =
    this match {
      case Leaf(value)       => leafF(value)
      case Node(left, right) => nodeF(left, right)
    }
}

final case class Leaf[A](value: A) extends Tree[A]
final case class Node[A](left: Tree[A], right: Tree[A]) extends Tree[A]
