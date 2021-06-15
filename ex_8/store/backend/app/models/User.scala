package models

case class User(id: Long, loginInfo: LoginInfo, email: String) extends Identity

object User {
  implicit val loginInfoFormat: OFormat[LoginInfo] = Json.using[Json.WithDefaultValues].format[LoginInfo]
  implicit val userFormat: OFormat[User] = Json.using[Json.WithDefaultValues].format[User]
}