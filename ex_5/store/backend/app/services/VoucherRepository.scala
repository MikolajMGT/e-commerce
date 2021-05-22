package services

import models.Voucher
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import java.sql.Timestamp
import java.time.Instant
import java.util.Date
import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class VoucherRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  class VoucherTable(tag: Tag) extends Table[Voucher](tag, "voucher") {
    def currentWhenInserting = new Timestamp((new Date).getTime)

    def id: Rep[Long] = column[Long]("id", O.PrimaryKey, O.AutoInc)

    def amount: Rep[Int] = column[Int]("amount")

    def usages: Rep[Int] = column[Int]("usages")

    def createdAt: Rep[Timestamp] = column[Timestamp]("created_at", O.Default(currentWhenInserting))

    def updatedAt: Rep[Timestamp] = column[Timestamp]("updated_at", O.Default(currentWhenInserting))

    def * = (id, amount, usages, createdAt, updatedAt) <> ((Voucher.apply _).tupled, Voucher.unapply)
  }

  val voucher = TableQuery[VoucherTable]

  def create(amount: Int, usages: Int, createdAt: Timestamp = Timestamp.from(Instant.now()), updatedAt: Timestamp = Timestamp.from(Instant.now())): Future[Voucher] = db.run {
    (voucher.map(v => (v.amount, v.usages, v.createdAt, v.updatedAt))
      returning voucher.map(_.id)
      into { case ((amount, usages, createdAt, updatedAt), id) => Voucher(id, amount, usages, createdAt, updatedAt) }
      ) += (amount, usages, createdAt, updatedAt)
  }

  def getByIdOption(id: Long): Future[Option[Voucher]] = db.run {
    voucher.filter(_.id === id).result.headOption
  }

  def list(): Future[Seq[Voucher]] = db.run {
    voucher.result
  }

  def update(id: Long, new_voucher: Voucher): Future[Int] = {
    val voucherToUpdate: Voucher = new_voucher.copy(id)
    db.run(voucher.filter(_.id === id).update(voucherToUpdate))
  }

  def delete(id: Long): Future[Int] = db.run(voucher.filter(_.id === id).delete)
}
