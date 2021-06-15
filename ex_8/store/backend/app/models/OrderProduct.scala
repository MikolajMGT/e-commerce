package models

case class OrderProduct(id: Long = 0, orderId: Long, productId: Long, amount: Int, createdAt: Timestamp = Timestamp.from(Instant.now()), updatedAt: Timestamp = Timestamp.from(Instant.now()))

object OrderProduct extends TimestampFormatter {
  implicit val orderProductFormat: OFormat[OrderProduct] = Json.using[Json.WithDefaultValues].format[OrderProduct]
}
