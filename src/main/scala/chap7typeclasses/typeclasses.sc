// if we want to add behaviour to classes, we could use a trait and have those
// classes extend the trait
trait Foo {
  def myBehaviour: Unit
}

final case class MyData() extends Foo {
  override def myBehaviour: Unit = {}
}

// but this has two drawbacks:
//  - we are limited to only one behaviour implementation per class
//  - we can only do this when we can edit the class' source code

// Okay, how about by pattern matching on the class?
object FooMaker {

  def myBehaviour(in: Any): Unit =
    in match {
      case MyData() => println("my data")
      case in: Int  => println("an int!")
      case _        => ()
    }
}

// Ugh, bad: lose type safety, have to modify this method for every
// implementation

// To solve these issues, we use a type class:
trait HtmlWriter[A] {
  def toHtml(in: A): String
}

class MyDataWriter extends HtmlWriter[MyData] {
  override def toHtml(in: MyData) = "<myData/>"
}

class RedactedMyDataWriter extends HtmlWriter[MyData] {
  override def toHtml(in: MyData) = "<redacted/>" // hides my secret data >:)
}

class IntWriter extends HtmlWriter[Int] {
  override def toHtml(in: Int) = s"<int>$in</int>"
}

// In summary: a type class is a trait with at least one type variable.

/* Exercises */
trait Equal[A] {
  def isEqual(v1: A, v2: A): Boolean
}

case class Person(name: String, email: String)

object EmailEqual extends Equal[Person] {
  override def isEqual(v1: Person, v2: Person) = v1.email == v2.email
  // side-note to self: scala's == operator is null-safe and defers to the
  // object's .equals() method - so we can use it on Strings! Whereas in Java
  // it would check identity.
}

object NameAndEmailEqual extends Equal[Person] {
  override def isEqual(v1: Person, v2: Person) =
    v1.name == v2.name && v1.email == v2.email
}
