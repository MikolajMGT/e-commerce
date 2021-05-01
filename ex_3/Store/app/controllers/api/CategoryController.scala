package controllers.api

import models.Category
import play.api.Logger
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._
import services.CategoryRepository

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class CategoryController @Inject()(val categoryRepo: CategoryRepository, cc: ControllerComponents)(implicit exec: ExecutionContext) extends AbstractController(cc) {

  val logger: Logger = Logger(classOf[CategoryController])

  def getCategoryById(id: String): Action[AnyContent] = Action.async {
    logger.info(s"getCategoryByName($id)")

    val category = categoryRepo.getByIdOption(id.toLong)
    category.map {
      case Some(res) => Ok(Json.toJson(res))
      case None => NotFound("category with given id cannot be found")
    }
  }

  def getCategoryByName(name: String): Action[AnyContent] = Action.async {
    logger.info(s"getCategoryByName($name)")

    val category = categoryRepo.getByNameOption(name)
    category.map {
      case Some(res) => Ok(Json.toJson(res))
      case None => NotFound("category with given name cannot be found")
    }
  }

  def listCategories(): Action[AnyContent] = Action.async {
    logger.info(s"listCategories()")

    val categories = categoryRepo.list()
    categories.map { categories =>
      Ok(Json.toJson(categories))
    }
  }

  def createCategory(): Action[JsValue] = Action.async(parse.json) { implicit request =>
    logger.info("createCategory()")
    logger.info(s"${request.body}")

    request.body.validate[Category].map {
      category =>
        categoryRepo.create(category.name).map { res =>
          logger.debug(s"category created: $res")
          Ok(Json.toJson(res))
        }
    }.getOrElse(Future.successful(BadRequest("incorrect data have been provided")))
  }

  def updateCategory(): Action[JsValue] = Action.async(parse.json) { request =>
    logger.info(s"updateCategory()")
    logger.debug(s"${request.body}")

    request.body.validate[Category].map {
      category =>
        categoryRepo.update(category.id, category).map { res =>
          logger.debug(s"categories updated count: $res")
          Ok(Json.toJson(res))
        }
    }.getOrElse(Future.successful(BadRequest("invalid json")))
  }

  def deleteCategory(id: String): Action[AnyContent] = Action.async {
    logger.info(s"deleteCategory($id)")

    categoryRepo.delete(id.toLong).map { res =>
      logger.debug(s"categories deleted count: $res")
      Ok(Json.toJson(res))
    }
  }
}
