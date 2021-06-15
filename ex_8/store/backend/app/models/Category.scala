package models

import models.util.TimestampFormatter
import play.api.libs.json._

import java.sql.Timestamp
import java.time.Instant

case class Category(id: Long = 0, name: String, createdAt: Timestamp = Timestamp.from(Instant.now()), updatedAt: Timestamp = Timestamp.from(Instant.now()))

object Category extends TimestampFormatter {
  implicit val categoryFormat: OFormat[Category] = Json.using[Json.WithDefaultValues].format[Category]
}
