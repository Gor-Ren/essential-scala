// in java we might write:
// for (i = 0; i < 100; i++)

// in Scala we use a Range
(0 until 5).foreach(println)
Range(0, 5).foreach(println)

// to go backwards, need to specify negative step
(5 until 0 by -1).foreach(println)
Range(5, 0, -1).foreach(println)

// we can concatenate them as well:
(1 to 10) ++ (21 to 30)

