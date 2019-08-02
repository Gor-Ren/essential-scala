/* 6.10 Generating Random Data */

// 6.10.1 random text
val subjects = List("Noel", "The cat", "The dog")
val verbs = List("wrote", "chased", "slept on")
val objects = List("the book", "the ball", "the bed")

def allSentences(): Seq[String] =
  for {
    subject <- subjects
    verb <- verbs
    obj <- objects
  } yield s"$subject $verb $obj"

allSentences()

def verbsFor(subject: String): List[String] =
  subject match {
    case "Noel" => List("wrote", "chased", "slept on")
    case "The cat" => List("meowed at", "chased", "slept on")
    case "The dog" => List("barked at", "chased", "slept on")
  }

def objectsFor(verb: String): List[String] =
  verb match {
    case "wrote" => List("the book", "the letter", "the code")
    case "chased" => List("the ball", "the dog", "the cat")
    case "slept on" => List("the bed", "the mat", "the train")
    case "meowed at" => List("Noel", "the door", "the food cupboard")
    case "barked at" => List("the postman", "the car", "the cat")
  }

def allSentenceConditional(): List[(String, String, String)] =
  for {
    subject <- subjects
    verb <- verbsFor(subject)
    obj <- objectsFor(verb)
  } yield (subject, verb, obj)

allSentenceConditional()

// 6.10.2 probabilities
final case class Distribution[+A](events: List[(A, Double)]) {
  def map[B](f: A => B): Distribution[B] =
    Distribution(events.map { case (e, p) => (f(e), p) })

  def flatMap[B](f: A => Distribution[B]): Distribution[B] =
    Distribution(events.flatMap { case (e1, p1) =>
      f(e1).events.map { case (e2, p2) =>
        (e2, p1 * p2)
      }
    }).compact.normalize

  def normalize: Distribution[A] = {
    val totalWeight = (events map { case (_, p) => p }).sum
    Distribution(events map { case (a, p) => a -> (p / totalWeight) })
  }

  def compact: Distribution[A] = {
    val distinct = (events map { case (a, p) => a }).distinct
    def prob(a: A): Double =
      (events filter { case (x, p) => x == a } map { case (a, p) => p
      }).sum
    Distribution(distinct map { a => a -> prob(a) })
  }
}

object Distribution {
  def uniform[A](events: List[A]): Distribution[A] = {
    Distribution(events.map(e => (e, 1.toDouble / events.size)))
  }
}

sealed trait Coin
case object Heads extends Coin
case object Tails extends Coin

val fairCoin: Distribution[Coin] = Distribution.uniform(List(Heads, Tails))

val threeFlips =
  fairCoin
    .flatMap(c1 =>
      fairCoin
        .flatMap(c2 =>
          fairCoin
            .map(c3 => (c1, c2, c3))
        )
    )
