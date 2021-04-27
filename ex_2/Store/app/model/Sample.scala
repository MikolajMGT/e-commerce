package model

import play.api.libs.json._

case class Sample(id: String, name: String)

object Sample {
  implicit val sampleFormat: OFormat[Sample] = Json.format[Sample]
}
