package models

import models.util.TimestampFormatter
import play.api.libs.json._

import java.sql.Timestamp
import java.time.Instant

case class Product(id: Long = 0, stockId: Long, categoryId: Long, subcategoryId: Long, name: String, imageUrl: String, description: String,
                   createdAt: Timestamp = Timestamp.from(Instant.now()))

object Product extends TimestampFormatter {
  implicit val productFormat: OFormat[Product] = Json.using[Json.WithDefaultValues].format[Product]
}
