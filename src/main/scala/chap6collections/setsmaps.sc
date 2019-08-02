/* Maps */

// as in Java, collection of key-value pairs that are typically unordered
val aMap: Map[String, Int] =
  Map("a" -> 1, "b" -> 2, "c" -> 3)

// its constructor accepts a collection of Tuple2s
// the -> operator is a function which generates a Tuple2:
"d" -> 42  // res0: (String, Int) = (d,42)

// retrieving elements:
//    1. apply (unsafe, throws NoSuchElementException)
  aMap("a")
//    2. get() - safe, returns an Option
  aMap.get("b")
//    3. getOrElse() - safe, returns else value if no such key
  aMap.getOrElse("d", 4)

// test membership
aMap.contains("qwerty")

// add k-v pair
aMap + ("g" -> 5) + ("h" -> 99)
aMap.+("g" -> 5, "h" -> 88)

// remove a key
aMap - "a"
aMap.-("a", "b")

// union
aMap ++ Map("teh" -> 55, "a" -> 999)  // N.B. right overwrites left's keys

/* Mutable Maps */
val mutMap = scala.collection.mutable.Map("x" -> 10, "y" -> 11)
mutMap += ("z" -> 0)
mutMap  // mutated in place

mutMap -= ("x", "y")

mutMap.update("w", 42)  // returns unit
mutMap
// below is equivalent to above; very Python-y
mutMap("w") = 43
mutMap

/* Ordered Maps */
// a ListMap is available which guarantees keeping insertion order

// there are performance trade-offs; see:
// https://docs.scala-lang.org/overviews/collections/performance-characteristics.html

/* Map's map, flatMap */
// Maps implement trait Traversable[Tuple2[K,V]] so we can map and flatMap over Maps.
// if we return sequences of Tuple2s, these are converted to a Map
// for any other sequence type, the implementation is smart enough to give back an iterator

/* Exercises */
val people = Set(
  "Alice",
  "Bob",
  "Charlie",
  "Derek",
  "Edith",
  "Fred")

val ages = Map(
  "Alice" -> 20,
  "Bob" -> 30,
  "Charlie" -> 50,
  "Derek" -> 40,
  "Edith" -> 10,
  "Fred" -> 60)

val favoriteColors = Map(
  "Bob" -> "green",
  "Derek" -> "magenta",
  "Fred" -> "yellow")

val favoriteLolcats = Map(
  "Alice" -> "Long Cat",
  "Charlie" -> "Ceiling Cat",
  "Edith" -> "Cloud Cat")

def favouriteColour(name: String): String =
  favoriteColors.getOrElse(name, "beige")

def printColours(): Unit =
  favoriteColors.foreach( x =>
    println(s"${x._1}'s favourite colour is ${x._2}!"))

def printColours2(): Unit =
  // uses the list 'people'
  for {
    person <- people
    colour <- favouriteColour(person)
  } println(s"$person's favourite colour is $colour!")

def lookup[V](name: String, map: Map[String, V]): Option[V] = map.get(name)

// calculate the fav color of oldest person
val elder: Option[String] =
  people.foldLeft(Option.empty[String])((oldest, person) =>
    if (ages.getOrElse(person, 0) > oldest.flatMap(ages.get).getOrElse(0))
      Some(person)
    else
      oldest
  )
elder.flatMap(name => favoriteColors.get(name))

/* DIY collection methods */
def union[A](a: Set[A], b: Set[A]): Set[A] =
  b.foldLeft(a)((set, elt) => set + elt)

def addUnion[K](a: Map[K, Int], b: Map[K, Int]): Map[K, Int] =
  b.foldLeft(a)((map, pair) => {
      val (key, value) = pair
      val oldValue = map.getOrElse(key, 0)
      map + (key -> (value + oldValue))
    }
  )
