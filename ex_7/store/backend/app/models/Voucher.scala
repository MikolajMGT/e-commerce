package models

case class Voucher(id: Long = 0, code: String, amount: Int, usages: Int, createdAt: Timestamp = Timestamp.from(Instant.now()), updatedAt: Timestamp = Timestamp.from(Instant.now()))

object Voucher extends TimestampFormatter {
  implicit val voucherFormat: OFormat[Voucher] = Json.using[Json.WithDefaultValues].format[Voucher]
}
