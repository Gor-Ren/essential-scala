
case class Film(name: String, yearOfRelease: Int, imdbRating: Double)

case class Director(firstName: String,
                    lastName: String,
                    yearOfBirth: Int,
                    films: Seq[Film])

val memento = Film("Memento", 2000, 8.5)
val darkKnight = Film("Dark Knight", 2008, 9.0)
val inception = Film("Inception", 2010, 8.8)
val highPlainsDrifter = Film("High Plains Drifter", 1973, 7.7)
val outlawJoseyWales = Film("The Outlaw Josey Wales", 1976, 7.9)
val unforgiven = Film("Unforgiven", 1992, 8.3)
val granTorino = Film("Gran Torino", 2008, 8.2)
val invictus = Film("Invictus", 2009, 7.4)
val predator = Film("Predator", 1987, 7.9)
val dieHard = Film("Die Hard", 1988, 8.3)
val huntForRedOctober = Film("The Hunt for Red October", 1990, 7.6)
val thomasCrownAffair = Film("The Thomas Crown Affair", 1999, 6.8)

val eastwood = Director(
    "Clint",
    "Eastwood",
    1930,
    Seq(highPlainsDrifter, outlawJoseyWales, unforgiven, granTorino, invictus)
  )
val mcTiernan = Director(
    "John",
    "McTiernan",
    1951,
    Seq(predator, dieHard, huntForRedOctober, thomasCrownAffair)
  )
val nolan = Director("Christopher",
                           "Nolan",
                           1970,
                           Seq(memento, darkKnight, inception))
val someGuy = Director("Just", "Some Guy", 1990, Seq())

val directors = Seq(eastwood, mcTiernan, nolan, someGuy)

/* Exercises 6.1 */
def findDirectorsWithMoreThanNFilms(n: Int): Seq[Director] = directors.filter(_.films.length > n)

def findOneDirectorBornBefore(yearAd: Int): Option[Director] = directors.find(_.yearOfBirth < yearAd)

def findDirectorsWho(nFilms: Int, bornBeforeYear: Int): Seq[Director] =
  directors.filter(d => d.yearOfBirth < bornBeforeYear && d.films.length > nFilms)

def sortDirectorsByAge(ascending: Boolean = true): Seq[Director] = {
  val result = directors.sortWith(_.yearOfBirth < _.yearOfBirth)
  if (ascending) result else result.reverse
}

/* Exercises 6.2 */
val nolanFilmNames: Seq[String] = nolan.films.map(_.name)

val allFilmNames: Seq[String] = directors.flatMap(
  director => director.films.map(
    film => film.name)
)

val earliestMcTiernan: Film = mcTiernan
  .films
  .sortWith(_.yearOfRelease < _.yearOfRelease)
  .head
// alternatively, mcTiernan.films.map(_.yearOfRelease).min

val filmsByImbdRatingDesc: Seq[Film] = directors
  .flatMap(_.films)
  .sortWith(_.imdbRating > _.imdbRating)

val allFilms = directors.flatMap(director => director.films)
val allFilmsImdbAverage: Double = allFilms
  .foldLeft(0.0D)((total, film) => total + film.imdbRating)
  ./(allFilms.length)

directors.foreach(director =>
  director.films.foreach(film =>
    println(s"Tonight only! ${film.name.toUpperCase} by ${director.lastName.toUpperCase}")
  )
)

val earliestFilm: Film = directors
  .flatMap(_.films)
  .sortWith((f1, f2) => f1.yearOfRelease < f2.yearOfRelease)
  .head

/* Exercises Repeated: with for comprehensions */
// nolan films
for (film <- nolan.films)
  yield film.name

// all films
for {
  director <- directors
  film <- director.films
} yield film.name

// films in order of IMDB rating
val films = for {
  director <- directors
  film <- director.films
} yield film

films.sortWith(_.imdbRating > _.imdbRating)

// print listing
for {
  director <- directors
  film <- director.films
} println(s"Tonight only! ${film.name.toUpperCase} by ${director.lastName.toUpperCase}")
