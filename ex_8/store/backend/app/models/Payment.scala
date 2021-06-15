package models

import models.util.TimestampFormatter
import play.api.libs.json._

import java.sql.Timestamp
import java.time.Instant

case class Payment(id: Long = 0, userId: Long, creditCardId: Long, amount: Int, createdAt: Timestamp = Timestamp.from(Instant.now()), updatedAt: Timestamp = Timestamp.from(Instant.now()))

object Payment extends TimestampFormatter {
  implicit val paymentFormat: OFormat[Payment] = Json.using[Json.WithDefaultValues].format[Payment]
}
