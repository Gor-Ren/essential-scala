// generics can be declared with the class, in which case they are available throughout body
case class MyType[A](value: A) {}

// or in the method/function signature, meaning only available in method
def getValue[A](): A = ???

// we can therefore improve our Calculation types from earlier:
sealed trait Result[A]
final case class Success[A](result: A) extends Result[A]
final case class Failure[A](message: String) extends Result[A]

