package controllers.api

import models.Order
import play.api.Logger
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._
import services.OrderRepository

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class OrderController @Inject()(val orderRepo: OrderRepository, cc: ControllerComponents)(implicit exec: ExecutionContext) extends AbstractController(cc) {

  val logger: Logger = Logger(classOf[OrderController])

  def getOrderById(id: Long): Action[AnyContent] = Action.async {
    logger.info(s"getOrderByName($id)")

    val order = orderRepo.getByIdOption(id)
    order.map {
      case Some(res) => Ok(Json.toJson(res))
      case None => NotFound("order with given id cannot be found")
    }
  }

  def listOrders(): Action[AnyContent] = Action.async {
    logger.info(s"listOrders()")

    val orders = orderRepo.list()
    orders.map { orders =>
      Ok(Json.toJson(orders))
    }
  }

  def listOrdersByUserId(userId: Long): Action[AnyContent] = Action.async {
    logger.info(s"listOrdersByUserId($userId)")

    val orders = orderRepo.listByUserId(userId)
    orders.map { orders =>
      Ok(Json.toJson(orders))
    }
  }

  def listOrdersByPaymentId(paymentId: Long): Action[AnyContent] = Action.async {
    logger.info(s"listOrdersByPaymentId($paymentId)")

    val orders = orderRepo.listByPaymentId(paymentId)
    orders.map { orders =>
      Ok(Json.toJson(orders))
    }
  }

  def listOrdersByVoucherId(voucherId: Long): Action[AnyContent] = Action.async {
    logger.info(s"listOrdersByVoucherId($voucherId)")

    val orders = orderRepo.listByVoucherId(voucherId)
    orders.map { orders =>
      Ok(Json.toJson(orders))
    }
  }

  def createOrder(): Action[JsValue] = Action.async(parse.json) { implicit request =>
    logger.info("createOrder()")
    logger.info(s"${request.body}")

    request.body.validate[Order].map {
      order =>
        orderRepo.create(order.userId, order.paymentId, order.voucherId).map { res =>
          logger.debug(s"order created: $res")
          Ok(Json.toJson(res))
        }
    }.getOrElse(Future.successful(BadRequest("incorrect data have been provided")))
  }

  def updateOrder(): Action[JsValue] = Action.async(parse.json) { request =>
    logger.info(s"updateOrder()")
    logger.debug(s"${request.body}")

    request.body.validate[Order].map {
      order =>
        orderRepo.update(order.id, order).map { res =>
          logger.debug(s"orders updated count: $res")
          Ok(Json.toJson(res))
        }
    }.getOrElse(Future.successful(BadRequest("invalid json")))
  }

  def deleteOrder(id: Long): Action[AnyContent] = Action.async {
    logger.info(s"deleteOrder($id)")

    orderRepo.delete(id).map { res =>
      logger.debug(s"orders deleted count: $res")
      Ok(Json.toJson(res))
    }
  }
}
