package controllers.api

@Singleton
class PaymentController @Inject()(val paymentRepo: PaymentRepository, cc: ControllerComponents)(implicit exec: ExecutionContext) extends AbstractController(cc) {

  val logger: Logger = Logger(classOf[PaymentController])

  def getPaymentById(id: Long): Action[AnyContent] = Action.async {
    logger.info(s"getPaymentByName($id)")

    val payment = paymentRepo.getByIdOption(id)
    payment.map {
      case Some(res) => Ok(Json.toJson(res))
      case None => NotFound("payment with given id cannot be found")
    }
  }

  def listPayments(): Action[AnyContent] = Action.async {
    logger.info(s"listPayments()")

    val payments = paymentRepo.list()
    payments.map { payments =>
      Ok(Json.toJson(payments))
    }
  }

  def listPaymentsByUserId(userId: Long): Action[AnyContent] = Action.async {
    logger.info(s"listPaymentsByUserId($userId)")

    val payments = paymentRepo.listByUserId(userId)
    payments.map { payments =>
      Ok(Json.toJson(payments))
    }
  }

  def listPaymentsByCreditCardId(creditCardId: Long): Action[AnyContent] = Action.async {
    logger.info(s"listPaymentsByCreditCardId($creditCardId)")

    val payments = paymentRepo.listByCreditCardId(creditCardId)
    payments.map { payments =>
      Ok(Json.toJson(payments))
    }
  }

  def createPayment(): Action[JsValue] = Action.async(parse.json) { implicit request =>
    logger.info("createPayment()")
    logger.info(s"${request.body}")

    request.body.validate[Payment].map {
      payment =>
        paymentRepo.create(payment.userId, payment.creditCardId, payment.amount).map { res =>
          logger.debug(s"payment created: $res")
          Ok(Json.toJson(res))
        }
    }.getOrElse(Future.successful(BadRequest("incorrect data have been provided")))
  }

  def updatePayment(): Action[JsValue] = Action.async(parse.json) { request =>
    logger.info(s"updatePayment()")
    logger.debug(s"${request.body}")

    request.body.validate[Payment].map {
      payment =>
        paymentRepo.update(payment.id, payment).map { res =>
          logger.debug(s"payments updated count: $res")
          Ok(Json.toJson(res))
        }
    }.getOrElse(Future.successful(BadRequest("invalid json")))
  }

  def deletePayment(id: Long): Action[AnyContent] = Action.async {
    logger.info(s"deletePayment($id)")

    paymentRepo.delete(id).map { res =>
      logger.debug(s"payments deleted count: $res")
      Ok(Json.toJson(res))
    }
  }
}
