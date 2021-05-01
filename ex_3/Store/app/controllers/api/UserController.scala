package controllers.api

import models.User
import play.api.Logger
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._
import services.UserRepository

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserController @Inject()(val userRepo: UserRepository, cc: ControllerComponents)(implicit exec: ExecutionContext) extends AbstractController(cc) {

  val logger: Logger = Logger(classOf[UserController])

  def getUserById(id: String): Action[AnyContent] = Action.async {
    logger.info(s"getUserByName($id)")

    val user = userRepo.getByIdOption(id.toLong)
    user.map {
      case Some(res) => Ok(Json.toJson(res))
      case None => NotFound("user with given id cannot be found")
    }
  }

  def listUsers(): Action[AnyContent] = Action.async {
    logger.info(s"listUsers()")

    val users = userRepo.list()
    users.map { users =>
      Ok(Json.toJson(users))
    }
  }

  def createUser(): Action[JsValue] = Action.async(parse.json) { implicit request =>
    logger.info("createUser()")
    logger.info(s"${request.body}")

    request.body.validate[User].map {
      user =>
        userRepo.create(user.email, user.nickname, user.password).map { res =>
          logger.debug(s"user created: $res")
          Ok(Json.toJson(res))
        }
    }.getOrElse(Future.successful(BadRequest("incorrect data have been provided")))
  }

  def updateUser(): Action[JsValue] = Action.async(parse.json) { request =>
    logger.info(s"updateUser()")
    logger.debug(s"${request.body}")

    request.body.validate[User].map {
      user =>
        userRepo.update(user.id, user).map { res =>
          logger.debug(s"users updated count: $res")
          Ok(Json.toJson(res))
        }
    }.getOrElse(Future.successful(BadRequest("invalid json")))
  }

  def deleteUser(id: String): Action[AnyContent] = Action.async {
    logger.info(s"deleteUser($id)")

    userRepo.delete(id.toLong).map { res =>
      logger.debug(s"users deleted count: $res")
      Ok(Json.toJson(res))
    }
  }
}
