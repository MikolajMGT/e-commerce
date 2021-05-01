package services

import models.Payment
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import java.sql.Timestamp
import java.time.Instant
import java.util.Date
import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class PaymentRepository @Inject()(dbConfigProvider: DatabaseConfigProvider, val userRepository: UserRepository, val creditCardRepository: CreditCardRepository)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  class PaymentTable(tag: Tag) extends Table[Payment](tag, "payment") {
    def currentWhenInserting = new Timestamp((new Date).getTime)

    def id: Rep[Long] = column[Long]("id", O.PrimaryKey, O.AutoInc)

    def userId = column[Long]("user_id")
    def user_fk = foreignKey("user_fk", userId, user_)(_.id)

    def creditCardId = column[Long]("credit_card_id")
    def creditCard_fk = foreignKey("credit_card_id_fk", creditCardId, creditCard_)(_.id)
    
    def amount: Rep[Int] = column[Int]("amount")
    def createdAt: Rep[Timestamp] = column[Timestamp]("created_at", O.Default(currentWhenInserting))
    def updatedAt: Rep[Timestamp] = column[Timestamp]("updated_at", O.Default(currentWhenInserting))

    def * = (id, userId, creditCardId, amount, createdAt, updatedAt) <> ((Payment.apply _).tupled, Payment.unapply)
  }

  import userRepository.UserTable
  import creditCardRepository.CreditCardTable

  val payment = TableQuery[PaymentTable]
  val user_ = TableQuery[UserTable]
  val creditCard_ = TableQuery[CreditCardTable]

  def create(userId: Long, creditCardId: Long, amount: Int, createdAt: Timestamp = Timestamp.from(Instant.now()), updatedAt: Timestamp = Timestamp.from(Instant.now())): Future[Payment] = db.run {
    (payment.map(c => (c.userId, c.creditCardId, c.amount, c.createdAt, c.updatedAt))
      returning payment.map(_.id)
      into {case ((userId, creditCardId, amount, createdAt, updatedAt), id) => Payment(id, userId, creditCardId, amount, createdAt, updatedAt)}
      ) += (userId, creditCardId, amount, createdAt, updatedAt)
  }

  def getByIdOption(id: Long): Future[Option[Payment]] = db.run {
    payment.filter(_.id === id).result.headOption
  }

  def list(): Future[Seq[Payment]] = db.run {
    payment.result
  }

  def listByUserId(userId: Long): Future[Seq[Payment]] = db.run {
    payment.filter(_.userId === userId).result
  }

  def listByCreditCardId(creditCardId: Long): Future[Seq[Payment]] = db.run {
    payment.filter(_.creditCardId === creditCardId).result
  }

  def update(id: Long, new_payment: Payment): Future[Int] = {
    val paymentToUpdate: Payment = new_payment.copy(id)
    db.run(payment.filter(_.id === id).update(paymentToUpdate))
  }

  def delete(id: Long): Future[Int] = db.run(payment.filter(_.id === id).delete)
}
