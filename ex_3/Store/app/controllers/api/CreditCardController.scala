package controllers.api

import models.CreditCard
import play.api.Logger
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._
import services.CreditCardRepository

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class CreditCardController @Inject()(val creditCardRepo: CreditCardRepository, cc: ControllerComponents)(implicit exec: ExecutionContext) extends AbstractController(cc) {

  val logger: Logger = Logger(classOf[CreditCardController])

  def getCreditCardById(id: String): Action[AnyContent] = Action.async {
    logger.info(s"getCreditCardByName($id)")

    val creditCard = creditCardRepo.getByIdOption(id.toLong)
    creditCard.map {
      case Some(res) => Ok(Json.toJson(res))
      case None => NotFound("creditCard with given id cannot be found")
    }
  }

  def listCreditCards(): Action[AnyContent] = Action.async {
    logger.info(s"listCreditCards()")

    val creditCards = creditCardRepo.list()
    creditCards.map { creditCards =>
      Ok(Json.toJson(creditCards))
    }
  }

  def listCreditCardsByUserId(userId: String): Action[AnyContent] = Action.async {
    logger.info(s"listCreditCardsByUserId($userId)")

    val creditCards = creditCardRepo.listByUserId(userId.toLong)
    creditCards.map { creditCards =>
      Ok(Json.toJson(creditCards))
    }
  }

  def createCreditCard(): Action[JsValue] = Action.async(parse.json) { implicit request =>
    logger.info("createCreditCard()")
    logger.info(s"${request.body}")

    request.body.validate[CreditCard].map {
      creditCard =>
        creditCardRepo.create(creditCard.userId, creditCard.cardholderName, creditCard.number, creditCard.expDate, creditCard.cvcCode).map { res =>
          logger.debug(s"creditCard created: $res")
          Ok(Json.toJson(res))
        }
    }.getOrElse(Future.successful(BadRequest("incorrect data have been provided")))
  }

  def updateCreditCard(): Action[JsValue] = Action.async(parse.json) { request =>
    logger.info(s"updateCreditCard()")
    logger.debug(s"${request.body}")

    request.body.validate[CreditCard].map {
      creditCard =>
        creditCardRepo.update(creditCard.id, creditCard).map { res =>
          logger.debug(s"creditCards updated count: $res")
          Ok(Json.toJson(res))
        }
    }.getOrElse(Future.successful(BadRequest("invalid json")))
  }

  def deleteCreditCard(id: String): Action[AnyContent] = Action.async {
    logger.info(s"deleteCreditCard($id)")

    creditCardRepo.delete(id.toLong).map { res =>
      logger.debug(s"creditCards deleted count: $res")
      Ok(Json.toJson(res))
    }
  }
}
