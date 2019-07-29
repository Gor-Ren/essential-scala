import java.util.Optional

import chap5genericsfunctions.{End, Leaf, LinkedList, Node, Tree}

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

// a flatmap is used when we have some F[A] and a function A => F[B] and we
//    want a result F[B]. This could be to "flatten" out sublists in a
//    List[List[A]] but equally it could be because we don't want an
//    Either[Either[A]]

// map and flatMap are immediately usable in collections, but there is a bigger
//    picture use of sequencing computations, e.g. a sequence of computations
//    which may fail. If we flatMap each of them, then a failure at any step
//    fails the whole computation.

// A type with a map and flatMap implementation is known as a monad.
//    (there is more to it than this, but covered in a later course).
// The general idea is that a monad represents a value in some context. We use
//    an appropriate monad for the context, e.g. optional values when retrieving
//    something with IO, or a sum type if we may succeed or fail.

/* Variance */
// we want to allow a generic A[B] to be able to use subclasses of B.
// This would allow us to define a case object Empty[Nothing] which
// can be used for all Maybe[B]s.container

// A Foo[T] is invariant in terms of T; Foo[A] and Foo[B] are unrelated
// regardless of the association between A and B.

// Type Foo[+T] is covariant in terms of T, meaning that Foo[A] is a supertype
// of Foo[B] if A is a supertype of B. (Most Scala collection classes are
// covariant).

// Type Foo[-T] is contravariant in terms of T: Foo[A] is a subtype of Foo[B] if
// A is a supertype of B. (It gets used in function arguments).
