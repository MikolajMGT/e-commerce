package controllers.api

import models.Subcategory
import play.api.Logger
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._
import services.SubcategoryRepository

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class SubcategoryController @Inject()(val subcategoryRepo: SubcategoryRepository, cc: ControllerComponents)(implicit exec: ExecutionContext) extends AbstractController(cc) {

  val logger: Logger = Logger(classOf[SubcategoryController])

  /**
   * FORM
   */


  /**
   * REST
   */

  def getSubcategoryById(id: Long): Action[AnyContent] = Action.async {
    logger.info(s"getSubcategoryByName($id)")

    val subcategory = subcategoryRepo.getByIdOption(id)
    subcategory.map {
      case Some(res) => Ok(Json.toJson(res))
      case None => NotFound("subcategory with given id cannot be found")
    }
  }

  def getSubcategoryByName(name: String): Action[AnyContent] = Action.async {
    logger.info(s"getSubcategoryByName($name)")

    val subcategory = subcategoryRepo.getByNameOption(name)
    subcategory.map {
      case Some(res) => Ok(Json.toJson(res))
      case None => NotFound("subcategory with given name cannot be found")
    }
  }

  def listSubcategories(): Action[AnyContent] = Action.async {
    logger.info(s"listSubcategories()")

    val subcategories = subcategoryRepo.list()
    subcategories.map { subcategories =>
      Ok(Json.toJson(subcategories))
    }
  }

  def listSubcategoriesByCategoryId(categoryId: Long): Action[AnyContent] = Action.async {
    logger.info(s"listSubcategoriesByCategoryId($categoryId)")

    val subcategories = subcategoryRepo.listByCategoryId(categoryId)
    subcategories.map { subcategories =>
      Ok(Json.toJson(subcategories))
    }
  }

  def createSubcategory(): Action[JsValue] = Action.async(parse.json) { implicit request =>
    logger.info("createSubcategory()")
    logger.info(s"${request.body}")

    request.body.validate[Subcategory].map {
      subcategory =>
        subcategoryRepo.create(subcategory.name, subcategory.categoryId).map { res =>
          logger.debug(s"subcategory created: $res")
          Ok(Json.toJson(res))
        }
    }.getOrElse(Future.successful(BadRequest("incorrect data have been provided")))
  }

  def updateSubcategory(): Action[JsValue] = Action.async(parse.json) { request =>
    logger.info(s"updateSubcategory()")
    logger.debug(s"${request.body}")

    request.body.validate[Subcategory].map {
      subcategory =>
        subcategoryRepo.update(subcategory.id, subcategory).map { res =>
          logger.debug(s"subcategories updated count: $res")
          Ok(Json.toJson(res))
        }
    }.getOrElse(Future.successful(BadRequest("invalid json")))
  }

  def deleteSubcategory(id: Long): Action[AnyContent] = Action.async {
    logger.info(s"deleteSubcategory($id)")

    subcategoryRepo.delete(id).map { res =>
      logger.debug(s"subcategories deleted count: $res")
      Ok(Json.toJson(res))
    }
  }
}
