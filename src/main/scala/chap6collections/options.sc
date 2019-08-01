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

// Option supports map & flatMap => can use for comprehensions
// and those for comprehensions will be safe for empty values

/* Exercises */
def addOptions(optA: Option[Int], optB: Option[Int], optC: Option[Int]): Option[Int] =
  for {
    a <- optA
    b <- optB
    c <- optC
  } yield a + b + c

def otherAddOptions(optA: Option[Int], optB: Option[Int], optC: Option[Int]): Option[Int] =
  optA.flatMap(a =>
    optB.flatMap(b =>
      optC.map(c =>
        a + b + c
      )
    )
  )

def divide(a: Int, b: Int): Option[Int] =
  if (b == 0) None else Some(a / b)

def divideOptions(optA: Option[Int], optB: Option[Int]): Option[Int] =
  for {
    a <- optA
    b <- optB
    c <- divide(a, b)
  } yield c

def readInt(s: String): Option[Int] =
  try {
    Some(s.toInt)
  } catch {
    case _: NumberFormatException => None
  }

def calculate(operand1: String, operator: String, operand2: String): Unit = {
  val result = for {
    a <- readInt(operand1)
    b <- readInt(operand2)
    ans <- operator match {
      case "+" => Some(a + b)
      case "-" => Some(a - b)
      case "*" => Some(a * b)
      case "/" => divide(a, b)
      case _ => None
    }
  } yield ans

  result match {
    case Some(v) => println("Answer: " + v)
    case None => println("Error!")
  }
}
