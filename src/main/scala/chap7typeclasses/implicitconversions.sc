// it is generally cleaner to use type enhancement, with implicit classes, and
// type classes, with implicit values

// however we do have another tool in our kit:

/* Implicit Conversions */

class Bar {
  def bar = "~wOoOo~ I'm a spooky implicit method"
}

class Foo

implicit def fooToBar(in: Foo): Bar = new Bar()

new Foo().bar

// I have to say I'm not feeling great about this feature

/* Exercises */
// equivalent to an implicit class
class IntOps(n: Int) {
  def talk(): Unit = s"Hi, I'm the number $n"
}

implicit def intToOps(i: Int): IntOps = new IntOps(i)

3.talk()
