package models

import models.util.TimestampFormatter
import play.api.libs.json._

import java.sql.Timestamp
import java.time.Instant

case class Stock(id: Long = 0, unitPrice: Int, totalPrice: Int, totalStock: Int, createdAt: Timestamp = Timestamp.from(Instant.now()), updatedAt: Timestamp = Timestamp.from(Instant.now()))

object Stock extends TimestampFormatter {
  implicit val stockFormat: OFormat[Stock] = Json.using[Json.WithDefaultValues].format[Stock]
}
