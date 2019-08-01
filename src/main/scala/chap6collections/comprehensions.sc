/** For Comprehensions */

// provides a more readable syntax for complex ops
// can be performed on types implementing map and flatmap
Seq(1, 2, 3).map(_ * 2)

// equivalent with a for comprehension
for {
  x <- Seq(1, 2, 3)  // the "generator"
} yield x * 2  // results in a sequence of same type as first generator

// a more complicated example to demonstrate
val data = Seq(Seq(1), Seq(2, 3), Seq(4, 5, 6))

// want to double values and flatten out
data.flatMap(_.map(_ * 2))

// or
for {
  subseq <- data
  el <- subseq
} yield el * 2

// a for comprehension can be replicated by a series of flatMaps and maps

// we can make the expression type "Unit" by omitting the yield keyword

for {
  subseq <- data
  el <- subseq
} print(el)  // type Unit, side effects only

// we can use parens instead of braces after the 'for'. but need to separate
// generators with semicolons in that case
for (
  subseq <- data;
  el <- subseq
) yield {  // we can also use a braced block for the yield
  el * 2
}

/* More features */

// filtering
for {
  x <- Seq(1, 2, 3, 4) if x > 2
  // (converts to a withFilter or filter call)
} println(x)

// parallel iteration, with pattern matching
for {
  (x, y) <- Seq(1, 2, 3).zip(Seq(4, 5, 6))
} println(s"x=$x, y=$y")

// enumerating elements
for {
  (x, y) <- Seq(1, 2, 3).zipWithIndex
} println(s"x=$x, y=$y")

// assigning intermediate values
for {
  x <- 1 to 5  // a way to define a range
  interValue = math.min(2, x)  // intermediate val
  y <- Some(2)
} println(s"${interValue + y}")
