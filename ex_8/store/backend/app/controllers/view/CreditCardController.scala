package controllers.view

import models.{CreditCard, User}
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import services.{CreditCardRepository, UserRepository}

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

@Singleton
class CreditCardController @Inject()(creditCardRepo: CreditCardRepository, userRepo: UserRepository, cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  val REDIRECT_URL = "/form/credit-card/list"

  var userList: Seq[User] = Seq[User]()

  // fill above lists
  fetchData()

  def listCreditCards: Action[AnyContent] = Action.async { implicit request =>
    creditCardRepo.list().map(creditCards => Ok(views.html.credit_card_list(creditCards)))
  }

  def createCreditCard(): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val users = userRepo.list()

    users.map(users => Ok(views.html.credit_card_create(creditCardForm, users)))
  }

  def createCreditCardHandle(): Action[AnyContent] = Action.async { implicit request =>
    creditCardForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.credit_card_create(errorForm, userList))
        )
      },
      creditCard => {
        creditCardRepo.create(creditCard.userId, creditCard.cardholderName, creditCard.number, creditCard.expDate, creditCard.cvcCode).map { _ =>
          Redirect(REDIRECT_URL)
        }
      }
    )
  }

  def updateCreditCard(id: Long): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val creditCard = creditCardRepo.getByIdOption(id)
    creditCard.map(creditCard => {
      val prodForm = updateCreditCardForm.fill(UpdateCreditCardForm(creditCard.get.id, creditCard.get.userId, creditCard.get.cardholderName, creditCard.get.number, creditCard.get.expDate, creditCard.get.cvcCode))
      Ok(views.html.credit_card_update(prodForm, userList))
    })
  }

  def updateCreditCardHandle(): Action[AnyContent] = Action.async { implicit request =>
    updateCreditCardForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.credit_card_update(errorForm, userList))
        )
      },
      creditCard => {
        creditCardRepo.update(creditCard.id, CreditCard(creditCard.id, creditCard.userId, creditCard.cardholderName, creditCard.number, creditCard.expDate, creditCard.cvcCode)).map { _ =>
          Redirect(REDIRECT_URL)
        }
      }
    )
  }

  def deleteCreditCard(id: Long): Action[AnyContent] = Action {
    creditCardRepo.delete(id)
    Redirect(REDIRECT_URL)
  }

  // utilities

  val creditCardForm: Form[CreateCreditCardForm] = Form {
    mapping(
      "userId" -> longNumber,
      "cardholderName" -> nonEmptyText,
      "number" -> nonEmptyText,
      "expDate" -> nonEmptyText,
      "cvcCode" -> nonEmptyText,
    )(CreateCreditCardForm.apply)(CreateCreditCardForm.unapply)
  }

  val updateCreditCardForm: Form[UpdateCreditCardForm] = Form {
    mapping(
      "id" -> longNumber,
      "userId" -> longNumber,
      "cardholderName" -> nonEmptyText,
      "number" -> nonEmptyText,
      "expDate" -> nonEmptyText,
      "cvcCode" -> nonEmptyText,
    )(UpdateCreditCardForm.apply)(UpdateCreditCardForm.unapply)
  }

  def fetchData(): Unit = {
    userRepo.list().onComplete {
      case Success(user) => userList = user
      case Failure(e) => print("error while listing users", e)
    }
  }
}

case class CreateCreditCardForm(userId: Long, cardholderName: String, number: String, expDate: String, cvcCode: String)

case class UpdateCreditCardForm(id: Long, userId: Long, cardholderName: String, number: String, expDate: String, cvcCode: String)
