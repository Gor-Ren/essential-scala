/* JSON Serialisation Exercise */

// provided code
sealed trait JsValue {
  def stringify: String
}
final case class JsObject(values: Map[String, JsValue]) extends JsValue {

  def stringify =
    values
      .map { case (name, value) => "\"" + name + "\":" + value.stringify }
      .mkString("{", ",", "}")
}

final case class JsString(value: String) extends JsValue {

  def stringify: String =
    "\"" + value.replaceAll("\\|\"", "\\\\$1") + "\""
}

import java.util.Date

sealed trait Visitor {
  def id: String
  def createdAt: Date
  def age: Long = new Date().getTime() - createdAt.getTime()
}

final case class Anonymous(id: String, createdAt: Date = new Date())
    extends Visitor

final case class User(id: String, email: String, createdAt: Date = new Date())
    extends Visitor

// begin
trait JsWriter[A] {
  def write(value: A): JsValue
}

implicit class JsUtil[A](value: A) {
  def toJson(implicit writer: JsWriter[A]): JsValue =
    writer.write(value)
}

implicit object AnonymousJsWriter extends JsWriter[Anonymous] {
  override def write(value: Anonymous): JsValue =
    JsObject(
      Map(
        "id" -> JsString(value.id),
        "createdAt" -> JsString(value.createdAt.toString)
      )
    )
}

implicit object UserJsWriter extends JsWriter[User] {
  override def write(value: User) =
    JsObject(
      Map(
        "id" -> JsString(value.id),
        "createdAt" -> JsString(value.createdAt.toString),
        "email" -> JsString(value.email)
      )
    )
}

implicit object VisitorWriter extends JsWriter[Visitor] {
  override def write(value: Visitor): JsValue =
    value match {
      case anon: Anonymous => anon.toJson
      case user: User      => user.toJson
    }
}

// test
val visitors: Seq[Visitor] = Seq(
  Anonymous("001", new Date),
  User("003", "dave@xample.com", new Date)
)

Anonymous("001", new Date).toJson

visitors.map(_.toJson)
