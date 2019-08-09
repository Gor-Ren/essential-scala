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

// the problem here is that we might need to write a lot of code to use these
// objects.

// therefore we turn to implicit parameters:
object HtmlUtil {

  def writeToHtml[A](data: A)(implicit writer: HtmlWriter[A]): String = {
    // N.B.: `implicit` applies to whole param list! => separate into own param
    // group
    writer.toHtml(data)
  }
}

implicit val writer = new MyDataWriter

HtmlUtil.writeToHtml(MyData())  // implicit passed in

// we can write a companion object with an apply function to concisely get
// type class instances:
object HtmlWriter {
  def apply[A](implicit writer: HtmlWriter[A]): HtmlWriter[A] = writer

  // compare this to the alternative, with more indirection:
  // def write[A](in: A)(implicit writer: HtmlWriter[A]): String =
  //   writer.write(in)
}

// then we can pass in the class as an arg, and using the no-param apply method:
HtmlWriter[MyData].toHtml(MyData())

/* Exercises */
object Eq {
  def apply[A](v1: A, v2: A)(implicit eq: Equal[A]): Boolean =
    eq.isEqual(v1, v2)
}

val p = Person("tom", "tom@myspace.com")
// if we don't declare this implicit...:
//implicit val pEq = NameAndEmailEqual
// ...we get error: Error:(104, 11) could not find implicit value for parameter
// eq: Equal[Person] Eq(p, p)

// alternatively we can import our implicit:
object NameAndEmailEqualImplicit {
  implicit object NameAndEmailEquals extends Equal[Person] {
    override def isEqual(v1: Person, v2: Person) =
      v1.name == v2.name && v1.email == v2.email
  }
}

import NameAndEmailEqualImplicit.NameAndEmailEquals

Eq(p, p)

// we can use `type enrichment` to expand the functionality of classes

implicit class ExtraStringMethods(str: String) {
  val vowels = Seq('a', 'e', 'i', 'o', 'u')

  def numberOfVowels: Int = str.filter(vowels contains _).length

}

new ExtraStringMethods("testyuiop").numberOfVowels

// it's a bit unwieldy to make the constructor call
// instead, we can make the class implicit and Scala will insert for us:
"hurrdurr".numberOfVowels

// this works well when combined with type classes:
implicit class HtmlOps[T](data: T) {
  def toHtml(implicit writer: HtmlWriter[T]) = writer.toHtml(data)
}

MyData().toHtml  // new method call!

/* Exercises */
trait Describer[A] {
  def describe(value: A)(implicit describer: Describer[A]): Unit
}

object IntYeahDescriber extends Describer[Int] {
  override def describe(i: Int)(implicit describer: Describer[Int]): Unit =
    (0 until i).foreach(_ => println("Oh yeah!"));
}

implicit class ExtraIntMethods(n: Int) {
  def yeah()(implicit describer: Describer[Int]): Unit =
    describer.describe(n)

  // note: for single implementation, we don't need the type class! e.g.:
  def times(f: Int => Unit): Unit =
    for {
      i <- 0 until n
    } f(i)
  // defined in terms of `times`
  def otherYeah(): Unit = times(_ => println("Oh yeah!"))
}

implicit val intDescriber = IntYeahDescriber
1.yeah()
(-1).yeah()
3.yeah()

2.otherYeah()
3.times(i => println(s"Look - it's the number $i!"))

/* Equality exercise */
object StringEqualIgnoringCase extends Equal[String] {
  override def isEqual(v1: String, v2: String): Boolean =
    v1.equalsIgnoreCase(v2)
}

implicit val strEqualIgnoreCase = StringEqualIgnoringCase

implicit class ExtraStringOps(str: String) {
  def ===(other: String)(implicit isEq: Equal[String]): Boolean =
    isEq.isEqual(str, other)
}

"abcd".===("ABCD")
"abcd" === "ABCD"

/* Context Bounds */
// a more concise notation:
def pageTemplate[A : HtmlWriter](body: A): String = {
  val renderedBody = body.toHtml
  s"<html><head>hi</head><body>$renderedBody</body></html>"
}

// this is syntactic sugar for:
// def pageTemplate[A](body: A)(implicit writer: HtmlWriter[A]) ...

// we don't have a variable bound to the HtmlWriter, but this is fine since we
// use it implicitly and simply require it in scope

// to find an implicit value (useful when debugging) we can use `implicitly[A]`
