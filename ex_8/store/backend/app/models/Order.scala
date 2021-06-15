package models

case class Order(id: Long = 0, userId: Long, addressId: Long, paymentId: Long, voucherId: Long = 0, createdAt: Timestamp = Timestamp.from(Instant.now()), updatedAt: Timestamp = Timestamp.from(Instant.now()))

object Order extends TimestampFormatter {
  implicit val orderFormat: OFormat[Order] = Json.using[Json.WithDefaultValues].format[Order]
}
