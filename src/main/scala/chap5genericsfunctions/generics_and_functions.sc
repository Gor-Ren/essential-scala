import chap5genericsfunctions.End

// generics can be declared with the class, in which case they are available throughout body
case class MyType[A](value: A) {}

// or in the method/function signature, meaning only available in method
def getValue[A]: A = ???

// we can therefore improve our Calculation types from earlier:
sealed trait Result[A]
final case class Success[A](result: A) extends Result[A]
final case class Failure[A](message: String) extends Result[A]

// implemented fold in IntList.scala

// woah: fold is a general concept for converting any algebraic datatype A to B
// it requires:
//    one function arg per case in the datatype
//    each function has input params corresponding to the case's fields
//    if A is recursive, any function parameters for this field are of type B
//      instead

// in the common case of a List[E] fold, we have
//    case Nil, with no fields, so function arg is:
//      () => B, or simplified to B
//    case Pair(E, A), which is recursive, so function arg is:
//      (E, B) => B
