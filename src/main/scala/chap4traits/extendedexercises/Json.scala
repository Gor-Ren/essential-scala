package chap4traits.extendedexercises

sealed trait JsonElement {
  override def toString: String =
    this match {
      case JsonString(v)  => '"' + v + '"'
      case JsonInteger(v) => v.toString
      case JsonDouble(v)  => v.toString
      case JsonBoolean(v) => v.toString
      case JsonArray(es) =>
        "[ " + JsonElement.jsonArrayElementsToString(es) + "]"
      case JsonObject(ms) =>
        "{ " + JsonElement.jsonObjectMembersToString(ms) + "}"
      case JsonNothing => ""
    }

}

object JsonElement {

  def jsonArrayElementsToString(list: List[JsonElement]): String =
    list match {
      case Nil => ""
      case head :: tail =>
        head.toString + ", " + jsonArrayElementsToString(tail)
    }

  def jsonObjectMembersToString(
      members: List[(JsonString, JsonElement)]
  ): String =
    members match {
      case Nil => ""
      case (name, value) :: tail =>
        s"$name: $value, " + jsonObjectMembersToString(tail)
    }
}

final case class JsonString(value: String) extends JsonElement
final case class JsonInteger(value: Int) extends JsonElement
final case class JsonDouble(value: Double) extends JsonElement
final case class JsonBoolean(value: Boolean) extends JsonElement
final case class JsonArray(values: List[JsonElement]) extends JsonElement
final case class JsonObject(members: List[(JsonString, JsonElement)])
    extends JsonElement
case object JsonNothing extends JsonElement

object JsonTest extends App {
  println(
    JsonArray(
      List(JsonString("a string"), JsonDouble(1.0), JsonBoolean(true))
    )
  )

  println(
    JsonObject(
      List(
        (JsonString("a"), JsonString("a value")),
        (JsonString("b"), JsonArray(List(JsonBoolean(false), JsonNothing))),
        (JsonString("c"), JsonInteger(42))
      )
    )
  )
}
