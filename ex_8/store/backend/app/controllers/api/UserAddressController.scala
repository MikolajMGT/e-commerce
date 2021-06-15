package controllers.api

import models.UserAddress
import play.api.Logger
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._
import services.UserAddressRepository

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserAddressController @Inject()(val userAddressRepo: UserAddressRepository, cc: ControllerComponents)(implicit exec: ExecutionContext) extends AbstractController(cc) {

  val logger: Logger = Logger(classOf[UserAddressController])

  def getUserAddressById(id: Long): Action[AnyContent] = Action.async {
    logger.info(s"getUserAddressByName($id)")

    val userAddress = userAddressRepo.getByIdOption(id)
    userAddress.map {
      case Some(res) => Ok(Json.toJson(res))
      case None => NotFound("userAddress with given id cannot be found")
    }
  }

  def listUserAddresses(): Action[AnyContent] = Action.async {
    logger.info(s"listUserAddresses()")

    val userAddresses = userAddressRepo.list()
    userAddresses.map { userAddresses =>
      Ok(Json.toJson(userAddresses))
    }
  }

  def listUserAddressesByUserId(userId: Long): Action[AnyContent] = Action.async {
    logger.info(s"listUserAddressesByUserId($userId)")

    val userAddresses = userAddressRepo.listByUserId(userId)
    userAddresses.map { userAddresses =>
      Ok(Json.toJson(userAddresses))
    }
  }

  def createUserAddress(): Action[JsValue] = Action.async(parse.json) { implicit request =>
    logger.info("createUserAddress()")
    logger.info(s"${request.body}")

    request.body.validate[UserAddress].map {
      userAddress =>
        userAddressRepo.create(userAddress.userId, userAddress.firstname, userAddress.lastname, userAddress.address, userAddress.zipcode, userAddress.city, userAddress.country).map { res =>
          logger.debug(s"userAddress created: $res")
          Ok(Json.toJson(res))
        }
    }.getOrElse(Future.successful(BadRequest("incorrect data have been provided")))
  }

  def updateUserAddress(): Action[JsValue] = Action.async(parse.json) { request =>
    logger.info(s"updateUserAddress()")
    logger.debug(s"${request.body}")

    request.body.validate[UserAddress].map {
      userAddress =>
        userAddressRepo.update(userAddress.id, userAddress).map { res =>
          logger.debug(s"userAddresses updated count: $res")
          Ok(Json.toJson(res))
        }
    }.getOrElse(Future.successful(BadRequest("invalid json")))
  }

  def deleteUserAddress(id: Long): Action[AnyContent] = Action.async {
    logger.info(s"deleteUserAddress($id)")

    userAddressRepo.delete(id).map { res =>
      logger.debug(s"userAddresses deleted count: $res")
      Ok(Json.toJson(res))
    }
  }
}
