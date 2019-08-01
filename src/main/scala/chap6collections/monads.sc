import scala.util.Try
// A class with a map and a flatMap (and some other technical details) is a Monad

// Monads allow us to sequence computations while abstracting away some technicality:
// e.g. that a value may not be present (Option), that there may be zero or many values (Seq), or
// that the operation to get a value is asynchronous (Future).

/* Example */
val opt1 = Some(1)
val opt2 = Some(2)
val opt3 = Some(3)

val seq1 = Seq(1)
val seq2 = Seq(2)
val seq3 = Seq(3)

val try1 = Try(1)
val try2 = Try(2)
val try3 = Try(3)

for {
  a <- opt1
  b <- opt2
  c <- opt3
} yield a + b + c

for {
  a <- seq1
  b <- seq2
  c <- seq3
} yield a + b + c

for {
  a <- try1
  b <- try2
  c <- try3
} yield a + b + c
