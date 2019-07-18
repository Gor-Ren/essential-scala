class Person(first: String, last: String) {
  val firstName = first
  val lastName = last

  def name = s"$firstName $lastName"
}

// Any is the supertype of everything

// Null and Nothing are subtypes of everything
//    Null type has only the value null,
//    Nothing type occurs when we throw an exception

// Thus, an expression can return, say, an Int, OR throw, and the compiler
// picks their lease common supertype (Int) as the expression return type

class Cat(val name: String, val colour: String, val food: String) {
}

val oswald = new Cat("Oswald", "Black", "Milk")
val henderson = new Cat("Henderson", "Ginger", "Chips")
val quentin = new Cat("Quentin", "Tabby and White", "Curry")

object ChipShop {
  val SERVES: String = "chips"

  def willServe(cat: Cat) = cat.food.equals(SERVES)
}