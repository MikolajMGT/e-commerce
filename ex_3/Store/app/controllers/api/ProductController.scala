package controllers.api

import models.Product
import play.api.Logger
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._
import services.ProductRepository

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ProductController @Inject()(val productRepo: ProductRepository, cc: ControllerComponents)(implicit exec: ExecutionContext) extends AbstractController(cc) {

  val logger: Logger = Logger(classOf[ProductController])

  def getProductById(id: String): Action[AnyContent] = Action.async {
    logger.info(s"getProductByName($id)")

    val product = productRepo.getByIdOption(id.toLong)
    product.map {
      case Some(res) => Ok(Json.toJson(res))
      case None => NotFound("product with given id cannot be found")
    }
  }

  def listProducts(): Action[AnyContent] = Action.async {
    logger.info(s"listProducts()")

    val products = productRepo.list()
    products.map { products =>
      Ok(Json.toJson(products))
    }
  }

  def listProductsByStockId(stockId: String): Action[AnyContent] = Action.async {
    logger.info(s"listProductsByStockId($stockId)")

    val products = productRepo.listByStockId(stockId.toLong)
    products.map { products =>
      Ok(Json.toJson(products))
    }
  }

  def listProductsByCategoryId(categoryId: String): Action[AnyContent] = Action.async {
    logger.info(s"listProductsByCategoryId($categoryId)")

    val products = productRepo.listByCategoryId(categoryId.toLong)
    products.map { products =>
      Ok(Json.toJson(products))
    }
  }

  def listProductsBySubcategoryId(subcategoryId: String): Action[AnyContent] = Action.async {
    logger.info(s"listProductsBySubcategoryId($subcategoryId)")

    val products = productRepo.listBySubcategoryId(subcategoryId.toLong)
    products.map { products =>
      Ok(Json.toJson(products))
    }
  }

  def createProduct(): Action[JsValue] = Action.async(parse.json) { implicit request =>
    logger.info("createProduct()")
    logger.info(s"${request.body}")

    request.body.validate[Product].map {
      product =>
        productRepo.create(product.stockId, product.categoryId, product.subcategoryId, product.name, product.description).map { res =>
          logger.debug(s"product created: $res")
          Ok(Json.toJson(res))
        }
    }.getOrElse(Future.successful(BadRequest("incorrect data have been provided")))
  }

  def updateProduct(): Action[JsValue] = Action.async(parse.json) { request =>
    logger.info(s"updateProduct()")
    logger.debug(s"${request.body}")

    request.body.validate[Product].map {
      product =>
        productRepo.update(product.id, product).map { res =>
          logger.debug(s"products updated count: $res")
          Ok(Json.toJson(res))
        }
    }.getOrElse(Future.successful(BadRequest("invalid json")))
  }

  def deleteProduct(id: String): Action[AnyContent] = Action.async {
    logger.info(s"deleteProduct($id)")

    productRepo.delete(id.toLong).map { res =>
      logger.debug(s"products deleted count: $res")
      Ok(Json.toJson(res))
    }
  }
}
