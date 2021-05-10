package controllers.api

import models.Stock
import play.api.Logger
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._
import services.StockRepository

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class StockController @Inject()(val stockRepo: StockRepository, cc: ControllerComponents)(implicit exec: ExecutionContext) extends AbstractController(cc) {

  val logger: Logger = Logger(classOf[StockController])

  def getStockById(id: Long): Action[AnyContent] = Action.async {
    logger.info(s"getStockByName($id)")

    val stock = stockRepo.getByIdOption(id)
    stock.map {
      case Some(res) => Ok(Json.toJson(res))
      case None => NotFound("stock with given id cannot be found")
    }
  }

  def listStocks(): Action[AnyContent] = Action.async {
    logger.info(s"listStocks()")

    val stocks = stockRepo.list()
    stocks.map { stocks =>
      Ok(Json.toJson(stocks))
    }
  }

  def createStock(): Action[JsValue] = Action.async(parse.json) { implicit request =>
    logger.info("createStock()")
    logger.info(s"${request.body}")

    request.body.validate[Stock].map {
      stock =>
        stockRepo.create(stock.unitPrice, stock.totalPrice, stock.totalStock).map { res =>
          logger.debug(s"stock created: $res")
          Ok(Json.toJson(res))
        }
    }.getOrElse(Future.successful(BadRequest("incorrect data have been provided")))
  }

  def updateStock(): Action[JsValue] = Action.async(parse.json) { request =>
    logger.info(s"updateStock()")
    logger.debug(s"${request.body}")

    request.body.validate[Stock].map {
      stock =>
        stockRepo.update(stock.id, stock).map { res =>
          logger.debug(s"stocks updated count: $res")
          Ok(Json.toJson(res))
        }
    }.getOrElse(Future.successful(BadRequest("invalid json")))
  }

  def deleteStock(id: Long): Action[AnyContent] = Action.async {
    logger.info(s"deleteStock($id)")

    stockRepo.delete(id).map { res =>
      logger.debug(s"stocks deleted count: $res")
      Ok(Json.toJson(res))
    }
  }
}
