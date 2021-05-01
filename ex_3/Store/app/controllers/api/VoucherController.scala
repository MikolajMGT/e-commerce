package controllers.api

import models.Voucher
import play.api.Logger
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._
import services.VoucherRepository

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class VoucherController @Inject()(val voucherRepo: VoucherRepository, cc: ControllerComponents)(implicit exec: ExecutionContext) extends AbstractController(cc) {

  val logger: Logger = Logger(classOf[VoucherController])

  def getVoucherById(id: String): Action[AnyContent] = Action.async {
    logger.info(s"getVoucherByName($id)")

    val voucher = voucherRepo.getByIdOption(id.toLong)
    voucher.map {
      case Some(res) => Ok(Json.toJson(res))
      case None => NotFound("voucher with given id cannot be found")
    }
  }

  def listVouchers(): Action[AnyContent] = Action.async {
    logger.info(s"listVouchers()")

    val vouchers = voucherRepo.list()
    vouchers.map { vouchers =>
      Ok(Json.toJson(vouchers))
    }
  }

  def createVoucher(): Action[JsValue] = Action.async(parse.json) { implicit request =>
    logger.info("createVoucher()")
    logger.info(s"${request.body}")

    request.body.validate[Voucher].map {
      voucher =>
        voucherRepo.create(voucher.amount, voucher.usages).map { res =>
          logger.debug(s"voucher created: $res")
          Ok(Json.toJson(res))
        }
    }.getOrElse(Future.successful(BadRequest("incorrect data have been provided")))
  }

  def updateVoucher(): Action[JsValue] = Action.async(parse.json) { request =>
    logger.info(s"updateVoucher()")
    logger.debug(s"${request.body}")

    request.body.validate[Voucher].map {
      voucher =>
        voucherRepo.update(voucher.id, voucher).map { res =>
          logger.debug(s"vouchers updated count: $res")
          Ok(Json.toJson(res))
        }
    }.getOrElse(Future.successful(BadRequest("invalid json")))
  }

  def deleteVoucher(id: String): Action[AnyContent] = Action.async {
    logger.info(s"deleteVoucher($id)")

    voucherRepo.delete(id.toLong).map { res =>
      logger.debug(s"vouchers deleted count: $res")
      Ok(Json.toJson(res))
    }
  }
}
