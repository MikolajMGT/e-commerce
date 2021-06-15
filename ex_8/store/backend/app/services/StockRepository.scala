package services

@Singleton
class StockRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  class StockTable(tag: Tag) extends Table[Stock](tag, "stock") {
    def currentWhenInserting = new Timestamp((new Date).getTime)

    def id: Rep[Long] = column[Long]("id", O.PrimaryKey, O.AutoInc)

    def unitPrice: Rep[Int] = column[Int]("unit_price")

    def totalPrice: Rep[Int] = column[Int]("total_price")

    def totalStock: Rep[Int] = column[Int]("total_stock")

    def createdAt: Rep[Timestamp] = column[Timestamp]("created_at", O.Default(currentWhenInserting))

    def updatedAt: Rep[Timestamp] = column[Timestamp]("updated_at", O.Default(currentWhenInserting))

    def * = (id, unitPrice, totalPrice, totalStock, createdAt, updatedAt) <> ((Stock.apply _).tupled, Stock.unapply)
  }

  val stock = TableQuery[StockTable]

  def create(unitPrice: Int, totalPrice: Int, totalStock: Int, createdAt: Timestamp = Timestamp.from(Instant.now()), updatedAt: Timestamp = Timestamp.from(Instant.now())): Future[Stock] = db.run {
    (stock.map(s => (s.unitPrice, s.totalPrice, s.totalStock, s.createdAt, s.updatedAt))
      returning stock.map(_.id)
      into { case ((unitPrice, totalPrice, totalStock, createdAt, updatedAt), id) => Stock(id, unitPrice, totalPrice, totalStock, createdAt, updatedAt) }
      ) += (unitPrice, totalPrice, totalStock, createdAt, updatedAt)
  }

  def getByIdOption(id: Long): Future[Option[Stock]] = db.run {
    stock.filter(_.id === id).result.headOption
  }

  def list(): Future[Seq[Stock]] = db.run {
    stock.result
  }

  def update(id: Long, new_stock: Stock): Future[Int] = {
    val stockToUpdate: Stock = new_stock.copy(id)
    db.run(stock.filter(_.id === id).update(stockToUpdate))
  }

  def delete(id: Long): Future[Int] = db.run(stock.filter(_.id === id).delete)
}
