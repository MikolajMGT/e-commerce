package controllers.api

import play.api.Logger
import play.api.libs.json.Json
import play.api.mvc._
import services.UserRepository

import javax.inject._
import scala.concurrent.ExecutionContext

@Singleton
class UserController @Inject()(val userRepo: UserRepository, cc: ControllerComponents)(implicit exec: ExecutionContext) extends AbstractController(cc) {

  val logger: Logger = Logger(classOf[UserController])

  def getUserById(id: Long): Action[AnyContent] = Action.async {
    logger.info(s"getUserByName($id)")

    val user = userRepo.getByIdOption(id)
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
}
