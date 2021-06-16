package services

import models.CreditCard
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import java.sql.Timestamp
import java.time.Instant
import java.util.Date
import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class CreditCardRepository @Inject()(dbConfigProvider: DatabaseConfigProvider, val userRepository: UserRepository)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  class CreditCardTable(tag: Tag) extends Table[CreditCard](tag, "credit_card") {
    def currentWhenInserting = new Timestamp((new Date).getTime)

    def id: Rep[Long] = column[Long]("id", O.PrimaryKey, O.AutoInc)

    def userId = column[Long]("user_id")

    def userFk = foreignKey("user_fk", userId, user_)(_.id)

    def cardholderName: Rep[String] = column[String]("cardholder_name")

    def number: Rep[String] = column[String]("number")

    def expDate: Rep[String] = column[String]("exp_date")

    def cvcCode: Rep[String] = column[String]("cvc_code")

    def createdAt: Rep[Timestamp] = column[Timestamp]("created_at", O.Default(currentWhenInserting))

    def updatedAt: Rep[Timestamp] = column[Timestamp]("updated_at", O.Default(currentWhenInserting))

    def * = (id, userId, cardholderName, number, expDate, cvcCode, createdAt, updatedAt) <> ((CreditCard.apply _).tupled, CreditCard.unapply)
  }

  import userRepository.UserTable

  val creditCard = TableQuery[CreditCardTable]
  val user_ = TableQuery[UserTable]

  def create(userId: Long, cardholderName: String, number: String, expDate: String, cvcCode: String, createdAt: Timestamp = Timestamp.from(Instant.now()), updatedAt: Timestamp = Timestamp.from(Instant.now())): Future[CreditCard] = db.run {
    (creditCard.map(c => (c.userId, c.cardholderName, c.number, c.expDate, c.cvcCode, c.createdAt, c.updatedAt))
      returning creditCard.map(_.id)
      into { case ((userId, cardholderName, number, expDate, cvcCode, createdAt, updatedAt), id) => CreditCard(id, userId, cardholderName, number, expDate, cvcCode, createdAt, updatedAt) }
      ) += (userId, cardholderName, number, expDate, cvcCode, createdAt, updatedAt)
  }

  def getByIdOption(id: Long): Future[Option[CreditCard]] = db.run {
    creditCard.filter(_.id === id).result.headOption
  }

  def list(): Future[Seq[CreditCard]] = db.run {
    creditCard.result
  }

  def listByUserId(userId: Long): Future[Seq[CreditCard]] = db.run {
    creditCard.filter(_.userId === userId).result
  }

  def update(id: Long, newCreditCard: CreditCard): Future[Int] = {
    val creditCardToUpdate: CreditCard = newCreditCard.copy(id)
    db.run(creditCard.filter(_.id === id).update(creditCardToUpdate))
  }

  def delete(id: Long): Future[Int] = db.run(creditCard.filter(_.id === id).delete)
}
