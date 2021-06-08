package models

import models.util.TimestampFormatter
import play.api.libs.json._

import java.sql.Timestamp
import java.time.Instant

case class User(id: Long = 0, email: String, nickname: String, password: String, createdAt: Timestamp = Timestamp.from(Instant.now()), updatedAt: Timestamp = Timestamp.from(Instant.now()))

object User extends TimestampFormatter {
  implicit val userFormat: OFormat[User] = Json.using[Json.WithDefaultValues].format[User]
}
