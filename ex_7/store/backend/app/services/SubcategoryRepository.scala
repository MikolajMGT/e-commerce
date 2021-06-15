package services

@Singleton
class SubcategoryRepository @Inject()(dbConfigProvider: DatabaseConfigProvider, val categoryRepository: CategoryRepository)(implicit ec: ExecutionContext) {
  val dbConfig = dbConfigProvider.get[JdbcProfile]

  class SubcategoryTable(tag: Tag) extends Table[Subcategory](tag, "subcategory") {
    def currentWhenInserting = new Timestamp((new Date).getTime)

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

    def categoryId = column[Long]("category_id")

    def category_fk = foreignKey("category_fk", categoryId, category_)(_.id)

    def name = column[String]("name")

    def createdAt: Rep[Timestamp] = column[Timestamp]("created_at", O.Default(currentWhenInserting))

    def updatedAt: Rep[Timestamp] = column[Timestamp]("updated_at", O.Default(currentWhenInserting))

    def * = (id, categoryId, name, createdAt, updatedAt) <> ((Subcategory.apply _).tupled, Subcategory.unapply)
  }

  val subcategory = TableQuery[SubcategoryTable]
  val category_ = TableQuery[CategoryTable]

  def create(name: String, categoryId: Long, createdAt: Timestamp = Timestamp.from(Instant.now()), updatedAt: Timestamp = Timestamp.from(Instant.now())): Future[Subcategory] = db.run {
    (subcategory.map(s => (s.name, s.categoryId, s.createdAt, s.updatedAt))
      returning subcategory.map(_.id)
      into { case ((name, categoryId, createdAt, updatedAt), id) => Subcategory(id, categoryId, name, createdAt, updatedAt) }
      ) += (name, categoryId, createdAt, updatedAt)
  }

  def getByIdOption(id: Long): Future[Option[Subcategory]] = db.run {
    subcategory.filter(_.id === id).result.headOption
  }

  def getByNameOption(name: String): Future[Option[Subcategory]] = db.run {
    subcategory.filter(_.name === name).result.headOption
  }

  def list(): Future[Seq[Subcategory]] = db.run {
    subcategory.result
  }

  def listByCategoryId(categoryId: Long): Future[Seq[Subcategory]] = db.run {
    subcategory.filter(_.categoryId === categoryId).result
  }

  def update(id: Long, new_subcategory: Subcategory): Future[Int] = {
    val subcategoryToUpdate: Subcategory = new_subcategory.copy(id)
    db.run(subcategory.filter(_.id === id).update(subcategoryToUpdate))
  }

  def delete(id: Long): Future[Int] = db.run(subcategory.filter(_.id === id).delete)
}
