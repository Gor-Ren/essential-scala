// options can be thought of as sequences with 0 or 1 elements
// they support sequence-ey operations like folds, map, flatMap, filter and find

// we can use options to compose safe sequences of computations, e.g.
val optionA: Option[Int] = Some(10)
val optionB: Option[Int] = None
// safely add them
optionA.flatMap(a =>
  optionB.map(b => a + b)
)
// safely fails with None if either options are empty

// we can also use flatmap to select non-empty elements in a seq:
Seq(Some(1), None, Some(3)).flatMap(x => x)
// my IDE was shouting at me because apparently this is equiv:
Seq(Some(1), None, Some(3)).flatten
