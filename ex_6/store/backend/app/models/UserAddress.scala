package models

import models.util.TimestampFormatter
import play.api.libs.json._

import java.sql.Timestamp
import java.time.Instant

case class UserAddress(id: Long = 0, userId: Long, firstname: String, lastname: String, address: String,
                       zipcode: String, city: String, country: String, createdAt: Timestamp = Timestamp.from(Instant.now()), updatedAt: Timestamp = Timestamp.from(Instant.now()))

object UserAddress extends TimestampFormatter {
  implicit val userAddressFormat: OFormat[UserAddress] = Json.using[Json.WithDefaultValues].format[UserAddress]
}
