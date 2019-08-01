/* Maps */

// as in Java, collection of key-value pairs that are typically unordered
val aMap: Map[String, Int] = Map("a" -> 1, "b" -> 2, "c" -> 3)

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
