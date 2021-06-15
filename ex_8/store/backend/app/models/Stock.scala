package models

case class Stock(id: Long = 0, unitPrice: Int, totalPrice: Int, totalStock: Int, createdAt: Timestamp = Timestamp.from(Instant.now()), updatedAt: Timestamp = Timestamp.from(Instant.now()))

object Stock extends TimestampFormatter {
  implicit val stockFormat: OFormat[Stock] = Json.using[Json.WithDefaultValues].format[Stock]
}
