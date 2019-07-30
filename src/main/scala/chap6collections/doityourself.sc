/* Implement collection methods*/

// min element of Seq[Int]
def min(xs: Seq[Int]): Int =
  if (xs.isEmpty)
    throw new UnsupportedOperationException("min of empty sequence")
  else
    xs.foldLeft(Int.MinValue)(math.min)

// unique elements
def unique(xs: Seq[Int]): Seq[Int] =
  xs.foldLeft(Seq.empty[Int])((seq, i) =>
    if (seq.contains(i))
      seq
    else
      i +: seq
  )

// investigating the order of results
def otherUnique(xs: Seq[Int]): Seq[Int] =
  xs.foldRight(Seq.empty[Int])((i, seq) =>
    if (seq.contains(i))
      seq
    else
      i +: seq
  )

val test = unique(Seq(1, 1, 2, 4, 3, 4))
val othertest = otherUnique(Seq(1, 1, 2, 4, 3, 4))
assert(test.length == 4)

def reverseUsingFold(xs: Seq[Int]): Seq[Int] =
  xs.foldLeft(Seq.empty[Int])((seq, i) => i +: seq)

reverseUsingFold(test)

def mapUsingFoldRight[A, B](xs: Seq[A])(fn: A => B): Seq[B] =
  xs.foldRight(Seq.empty[B])((el, result) => fn(el) +: result)

mapUsingFoldRight(test)(_.toString)

def foldLeftUsingForEach[A, B](xs: Seq[A])(base: B)(f: (B, A) => B): B = {
  var result: B = base
  xs.foreach { x => result = f(result, x) }
  result
}
