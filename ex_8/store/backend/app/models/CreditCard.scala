package models

import models.util.TimestampFormatter
import play.api.libs.json._

import java.sql.Timestamp
import java.time.Instant

case class CreditCard(id: Long = 0, userId: Long, cardholderName: String, number: String, expDate: String,
                      cvcCode: String, createdAt: Timestamp = Timestamp.from(Instant.now()), updatedAt: Timestamp = Timestamp.from(Instant.now()))

object CreditCard extends TimestampFormatter {
  implicit val creditCardFormat: OFormat[CreditCard] = Json.using[Json.WithDefaultValues].format[CreditCard]
}
