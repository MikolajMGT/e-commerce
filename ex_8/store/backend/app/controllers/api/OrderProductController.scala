package controllers.api

@Singleton
class OrderProductController @Inject()(val orderProductRepo: OrderProductRepository, cc: ControllerComponents)(implicit exec: ExecutionContext) extends AbstractController(cc) {

  val logger: Logger = Logger(classOf[OrderProductController])

  def getOrderProductById(id: Long): Action[AnyContent] = Action.async {
    logger.info(s"getOrderProductByName($id)")

    val orderProduct = orderProductRepo.getByIdOption(id)
    orderProduct.map {
      case Some(res) => Ok(Json.toJson(res))
      case None => NotFound("orderProduct with given id cannot be found")
    }
  }

  def listOrderProducts(): Action[AnyContent] = Action.async {
    logger.info(s"listOrderProducts()")

    val orderProducts = orderProductRepo.list()
    orderProducts.map { orderProducts =>
      Ok(Json.toJson(orderProducts))
    }
  }

  def listProductsByOrderId(orderId: Long): Action[AnyContent] = Action.async {
    logger.info(s"listProductsByOrderId($orderId)")

    val orderProducts = orderProductRepo.listProductsByOrderId(orderId)
    orderProducts.map { orderProducts =>
      Ok(Json.toJson(orderProducts))
    }
  }

  def listOrdersByProductId(productId: Long): Action[AnyContent] = Action.async {
    logger.info(s"listOrdersByProductId($productId)")

    val orderProducts = orderProductRepo.listOrdersByProductId(productId)
    orderProducts.map { orderProducts =>
      Ok(Json.toJson(orderProducts))
    }
  }

  def createOrderProduct(): Action[JsValue] = Action.async(parse.json) { implicit request =>
    logger.info("createOrderProduct()")
    logger.info(s"${request.body}")

    request.body.validate[OrderProduct].map {
      orderProduct =>
        orderProductRepo.create(orderProduct.orderId, orderProduct.productId, orderProduct.amount).map { res =>
          logger.debug(s"orderProduct created: $res")
          Ok(Json.toJson(res))
        }
    }.getOrElse(Future.successful(BadRequest("incorrect data have been provided")))
  }

  def updateOrderProduct(): Action[JsValue] = Action.async(parse.json) { request =>
    logger.info(s"updateOrderProduct()")
    logger.debug(s"${request.body}")

    request.body.validate[OrderProduct].map {
      orderProduct =>
        orderProductRepo.update(orderProduct.id, orderProduct).map { res =>
          logger.debug(s"orderProducts updated count: $res")
          Ok(Json.toJson(res))
        }
    }.getOrElse(Future.successful(BadRequest("invalid json")))
  }

  def deleteOrderProduct(id: Long): Action[AnyContent] = Action.async {
    logger.info(s"deleteOrderProduct($id)")

    orderProductRepo.delete(id).map { res =>
      logger.debug(s"orderProducts deleted count: $res")
      Ok(Json.toJson(res))
    }
  }
}
