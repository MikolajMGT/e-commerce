package models

case class Product(id: Long = 0, stockId: Long, categoryId: Long, subcategoryId: Long, name: String, imageUrl: String, description: String,
                   createdAt: Timestamp = Timestamp.from(Instant.now()), updatedAt: Timestamp = Timestamp.from(Instant.now()))

object Product extends TimestampFormatter {
  implicit val productFormat: OFormat[Product] = Json.using[Json.WithDefaultValues].format[Product]
}
