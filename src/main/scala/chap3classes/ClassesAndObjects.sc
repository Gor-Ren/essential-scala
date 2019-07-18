case class Person(firstName: String, lastName: String) {
  def name = s"$firstName $lastName"
}

// Any is the supertype of everything

// Null and Nothing are subtypes of everything
//    Null type has only the value null,
//    Nothing type occurs when we throw an exception

// Thus, an expression can return, say, an Int, OR throw, and the compiler
// picks their lease common supertype (Int) as the expression return type

case class Cat(name: String, colour: String, food: String) {
}

val oswald = Cat("Oswald", "Black", "Milk")
val henderson = Cat("Henderson", "Ginger", "Chips")
val quentin = Cat("Quentin", "Tabby and White", "Curry")

object ChipShop {
  val SERVES: String = "chips"

  def willServe(cat: Cat) = {
    cat match {
      case Cat (_, _, "Chips") => true
      case _ => false
    }
  }
}

// I've skipped writing down a lot of content because it became apparent the
// Udemy course I started had cribbed from this book

// Scala has two namespaces: type names, and value names
// this is how a companion class and object can co-exist with the same name!

// case classes implement Serializable and Product

/* Pattern Matching */
// we use the match keyword to perform pattern matching:
object Stormtrooper {
  def inspect(p: Person): String = {
    p match {
      case Person("Luke", "Skywalker") => "Stop, rebel scum!"
      case Person("Han", "Solo") => "Stop, rebel scum!"
      case Person(first, _) => s"Move along, $first"
    }
  }
}

Stormtrooper.inspect(Person("Luna", "Bug"))
Stormtrooper.inspect(Person("Han", "Solo"))


case class Director(firstName: String, lastName: String, yearOfBirth: Int) {
  def name: String = s"$firstName $lastName"
}

object Director {
  def older(director1: Director, director2: Director): Director =
    if (director1.yearOfBirth < director2.yearOfBirth) director1 else
      director2
}

case class Film(name: String,
                yearOfRelease: Int,
                imdbRating: Double,
                director: Director) {
  def directorsAge: Int = yearOfRelease - director.yearOfBirth
  def isDirectedBy(director: Director): Boolean = this.director == director
}

object Film {
  def newer(film1: Film, film2: Film): Film =if (film1.yearOfRelease < film2.yearOfRelease) film1 else film2
  def highestRating(film1: Film, film2: Film): Double = {
    val rating1 = film1.imdbRating
    val rating2 = film2.imdbRating
    if (rating1 > rating2)
      rating1
    else
      rating2
  }

  def oldestDirectorAtTheTime(film1: Film, film2: Film): Director =
    if (film1.directorsAge > film2.directorsAge) film1.director else
      film2.director
}

object DadCritic {
  def rate(film: Film): Double ={
    film match {
      case Film(_, _, _, Director("Clint", "Eastwood", _)) => 10.0
      case Film(_, _, _, Director("John", "McTiernan", _)) => 7.0
      case _ => 3.0
      // later the course will cover constant patterns, to reduce verbosity
    }
  }
}
