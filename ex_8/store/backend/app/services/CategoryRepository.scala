package services

import models.Category
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import java.sql.Timestamp
import java.time.Instant
import java.util.Date
import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class CategoryRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  class CategoryTable(tag: Tag) extends Table[Category](tag, "category") {
    def currentWhenInserting = new Timestamp((new Date).getTime)

    def id: Rep[Long] = column[Long]("id", O.PrimaryKey, O.AutoInc)

    def name: Rep[String] = column[String]("name")

    def createdAt: Rep[Timestamp] = column[Timestamp]("created_at", O.Default(currentWhenInserting))

    def updatedAt: Rep[Timestamp] = column[Timestamp]("updated_at", O.Default(currentWhenInserting))

    def * = (id, name, createdAt, updatedAt) <> ((Category.apply _).tupled, Category.unapply)
  }

  val category = TableQuery[CategoryTable]

  def create(name: String, createdAt: Timestamp = Timestamp.from(Instant.now()), updatedAt: Timestamp = Timestamp.from(Instant.now())): Future[Category] = db.run {
    (category.map(c => (c.name, c.createdAt, c.updatedAt))
      returning category.map(_.id)
      into { case ((name, description, category), id) => Category(id, name, description, category) }
      ) += (name, createdAt, updatedAt)
  }

  def getByIdOption(id: Long): Future[Option[Category]] = db.run {
    category.filter(_.id === id).result.headOption
  }

  def getByNameOption(name: String): Future[Option[Category]] = db.run {
    category.filter(_.name === name).result.headOption
  }

  def list(): Future[Seq[Category]] = db.run {
    category.result
  }

  def update(id: Long, new_category: Category): Future[Int] = {
    val categoryToUpdate: Category = new_category.copy(id)
    db.run(category.filter(_.id === id).update(categoryToUpdate))
  }

  def delete(id: Long): Future[Int] = db.run(category.filter(_.id === id).delete)
}
