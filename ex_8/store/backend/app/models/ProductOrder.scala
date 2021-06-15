package models

case class ProductOrder(id: Long = 0, product: Long, stock: Long, createdAt: Timestamp = Timestamp.from(Instant.now()), updatedAt: Timestamp = Timestamp.from(Instant.now()))

object ProductOrder extends TimestampFormatter {
  implicit val productOrderFormat: OFormat[ProductOrder] = Json.using[Json.WithDefaultValues].format[ProductOrder]
}
