package controllers.api

@Singleton
class ProductController @Inject()(val productRepo: ProductRepository, val categoryRepo: CategoryRepository, cc: ControllerComponents)(implicit exec: ExecutionContext) extends AbstractController(cc) {

  val logger: Logger = Logger(classOf[ProductController])

  def getProductById(id: Long): Action[AnyContent] = Action.async {
    logger.info(s"getProductByName($id)")

    val product = productRepo.getByIdOption(id)
    product.map {
      case Some(res) => Ok(Json.toJson(res))
      case None => NotFound("product_create with given id cannot be found")
    }
  }

  def listProducts(): Action[AnyContent] = Action.async {
    logger.info(s"listProducts()")

    val products = productRepo.list()
    products.map { products =>
      Ok(Json.toJson(products))
    }
  }

  def listProductsByStockId(stockId: Long): Action[AnyContent] = Action.async {
    logger.info(s"listProductsByStockId($stockId)")

    val products = productRepo.listByStockId(stockId)
    products.map { products =>
      Ok(Json.toJson(products))
    }
  }

  def listProductsByCategoryId(categoryId: Long): Action[AnyContent] = Action.async {
    logger.info(s"listProductsByCategoryId($categoryId)")

    val products = productRepo.listByCategoryId(categoryId)
    products.map { products =>
      Ok(Json.toJson(products))
    }
  }

  def listProductsBySubcategoryId(subcategoryId: Long): Action[AnyContent] = Action.async {
    logger.info(s"listProductsBySubcategoryId($subcategoryId)")

    val products = productRepo.listBySubcategoryId(subcategoryId)
    products.map { products =>
      Ok(Json.toJson(products))
    }
  }

  def createProduct(): Action[JsValue] = Action.async(parse.json) { implicit request =>
    logger.info("createProduct()")
    logger.info(s"${request.body}")

    request.body.validate[Product].map {
      product =>
        productRepo.create(product.stockId, product.categoryId, product.subcategoryId, product.name, product.imageUrl, product.description).map { res =>
          logger.debug(s"product_create created: $res")
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
          logger.debug(s"product_create updated count: $res")
          Ok(Json.toJson(res))
        }
    }.getOrElse(Future.successful(BadRequest("invalid json")))
  }

  def deleteProduct(id: Long): Action[AnyContent] = Action.async {
    logger.info(s"deleteProduct($id)")

    productRepo.delete(id).map { res =>
      logger.debug(s"product_create deleted count: $res")
      Ok(Json.toJson(res))
    }
  }
}
