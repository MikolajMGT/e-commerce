package models

case class Subcategory(id: Long = 0, categoryId: Long, name: String, createdAt: Timestamp = Timestamp.from(Instant.now()), updatedAt: Timestamp = Timestamp.from(Instant.now()))

object Subcategory extends TimestampFormatter {
  implicit val subcategoryFormat: OFormat[Subcategory] = Json.using[Json.WithDefaultValues].format[Subcategory]
}
