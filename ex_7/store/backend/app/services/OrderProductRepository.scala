package services

@Singleton
class OrderProductRepository @Inject()(dbConfigProvider: DatabaseConfigProvider, val orderRepository: OrderRepository, val productRepository: ProductRepository)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  class OrderProductTable(tag: Tag) extends Table[OrderProduct](tag, "order_product") {
    def currentWhenInserting = new Timestamp((new Date).getTime)

    def id: Rep[Long] = column[Long]("id", O.PrimaryKey, O.AutoInc)

    def orderId = column[Long]("order_id")

    def order_fk = foreignKey("order_fk", orderId, order_)(_.id)

    def productId = column[Long]("product_id")

    def product_fk = foreignKey("product_id_fk", productId, product_)(_.id)

    def amount: Rep[Int] = column[Int]("amount")

    def createdAt: Rep[Timestamp] = column[Timestamp]("created_at", O.Default(currentWhenInserting))

    def updatedAt: Rep[Timestamp] = column[Timestamp]("updated_at", O.Default(currentWhenInserting))

    def * = (id, orderId, productId, amount, createdAt, updatedAt) <> ((OrderProduct.apply _).tupled, OrderProduct.unapply)
  }

  val orderProduct = TableQuery[OrderProductTable]
  val order_ = TableQuery[OrderTable]
  val product_ = TableQuery[ProductTable]

  def create(orderId: Long, productId: Long, amount: Int, createdAt: Timestamp = Timestamp.from(Instant.now()), updatedAt: Timestamp = Timestamp.from(Instant.now())): Future[OrderProduct] = db.run {
    (orderProduct.map(p => (p.orderId, p.productId, p.amount, p.createdAt, p.updatedAt))
      returning orderProduct.map(_.id)
      into { case ((orderId, productId, amount, createdAt, updatedAt), id) => OrderProduct(id, orderId, productId, amount, createdAt, updatedAt) }
      ) += (orderId, productId, amount, createdAt, updatedAt)
  }

  def getByIdOption(id: Long): Future[Option[OrderProduct]] = db.run {
    orderProduct.filter(_.id === id).result.headOption
  }

  def list(): Future[Seq[OrderProduct]] = db.run {
    orderProduct.result
  }

  def listByOrderId(orderId: Long): Future[Seq[OrderProduct]] = db.run {
    orderProduct.filter(_.orderId === orderId).result
  }

  def listByProductId(productId: Long): Future[Seq[OrderProduct]] = db.run {
    orderProduct.filter(_.productId === productId).result
  }

  def listOrdersByProductId(productId: Long): Future[Seq[Order]] = {
    val orderProducts = Await.result(listByProductId(productId), 10.second)
    val ids = orderProducts.map { orderProduct => orderProduct.orderId }
    orderRepository.listByIds(ids)
  }

  def listProductsByOrderId(orderId: Long): Future[Seq[Product]] = {
    val orderProducts = Await.result(listByOrderId(orderId), 10.second)
    val ids = orderProducts.map { orderProduct => orderProduct.productId }
    productRepository.listByIds(ids)
  }

  def update(id: Long, new_orderProduct: OrderProduct): Future[Int] = {
    val orderProductToUpdate: OrderProduct = new_orderProduct.copy(id)
    db.run(orderProduct.filter(_.id === id).update(orderProductToUpdate))
  }

  def delete(id: Long): Future[Int] = db.run(orderProduct.filter(_.id === id).delete)
}
