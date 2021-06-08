package models

import models.util.TimestampFormatter
import play.api.libs.json._

import java.sql.Timestamp
import java.time.Instant

case class Voucher(id: Long = 0, amount: Int, usages: Int, createdAt: Timestamp = Timestamp.from(Instant.now()), updatedAt: Timestamp = Timestamp.from(Instant.now()))

object Voucher extends TimestampFormatter {
  implicit val voucherFormat: OFormat[Voucher] = Json.using[Json.WithDefaultValues].format[Voucher]
}
