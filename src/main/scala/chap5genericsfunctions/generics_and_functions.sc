import java.util.Optional

import chap5genericsfunctions.{Leaf, Node, Tree}

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

/* Shorthand function notation*/

(_: Int) * 2  // equivalent to (x: Int) => x * 2

// convert method call to a function
// Optional.of  // doesn't compile with 'missing args' error
Optional.of _  // creates function from method

// in places where the compiler can infer a function is required, it may be
// possible to drop the underscore

// Scala allows multiple parameter lists to be defined
def testy(x: Int)(y: String) = x.toString + y

// they are called using separate arg parens
testy(1)("hi")

// multiple param lists can improve readability
//    but they also improve Scala's type inference: it can infer the type of an
//    arg(s) in one param list, and use that solution to help inference.
//
//    With a single param list, the compiler is unable to use inference from one
//    param to help other params; this leads to more explicit type declarations
