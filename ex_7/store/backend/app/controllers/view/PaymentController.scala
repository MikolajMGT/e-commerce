package controllers.view

@Singleton
class PaymentController @Inject()(paymentRepo: PaymentRepository, userRepo: UserRepository, creditCardRepo: CreditCardRepository, cc: MessagesControllerComponents)(implicit ec: ExecutionContext) extends MessagesAbstractController(cc) {

  var userList: Seq[User] = Seq[User]()
  var creditCardList: Seq[CreditCard] = Seq[CreditCard]()

  // fill above lists
  fetchData()

  def listPayments: Action[AnyContent] = Action.async { implicit request =>
    paymentRepo.list().map(payments => Ok(views.html.payment_list(payments)))
  }

  def createPayment(): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val users = Await.result(userRepo.list(), 1.second)
    val creditCards = creditCardRepo.list()

    creditCards.map(creditCards => Ok(views.html.payment_create(paymentForm, users, creditCards)))
  }

  def createPaymentHandle(): Action[AnyContent] = Action.async { implicit request =>
    paymentForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.payment_create(errorForm, userList, creditCardList))
        )
      },
      payment => {
        paymentRepo.create(payment.userId, payment.creditCardId, payment.amount).map { _ =>
          Redirect("/form/payment/list")
        }
      }
    )
  }

  def updatePayment(id: Long): Action[AnyContent] = Action.async { implicit request: MessagesRequest[AnyContent] =>
    val payment = paymentRepo.getByIdOption(id)
    payment.map(payment => {
      val prodForm = updatePaymentForm.fill(UpdatePaymentForm(payment.get.id, payment.get.userId, payment.get.creditCardId, payment.get.amount))
      Ok(views.html.payment_update(prodForm, userList, creditCardList))
    })
  }

  def updatePaymentHandle(): Action[AnyContent] = Action.async { implicit request =>
    updatePaymentForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(
          BadRequest(views.html.payment_update(errorForm, userList, creditCardList))
        )
      },
      payment => {
        paymentRepo.update(payment.id, Payment(payment.id, payment.userId, payment.creditCardId, payment.amount)).map { _ =>
          Redirect("/form/payment/list")
        }
      }
    )
  }

  def deletePayment(id: Long): Action[AnyContent] = Action {
    paymentRepo.delete(id)
    Redirect("/form/payment/list")
  }

  // utilities

  val paymentForm: Form[CreatePaymentForm] = Form {
    mapping(
      "userId" -> longNumber,
      "creditCardId" -> longNumber,
      "amount" -> number,
    )(CreatePaymentForm.apply)(CreatePaymentForm.unapply)
  }

  val updatePaymentForm: Form[UpdatePaymentForm] = Form {
    mapping(
      "id" -> longNumber,
      "userId" -> longNumber,
      "creditCardId" -> longNumber,
      "amount" -> number,
    )(UpdatePaymentForm.apply)(UpdatePaymentForm.unapply)
  }

  def fetchData(): Unit = {

    userRepo.list().onComplete {
      case Success(users) => userList = users
      case Failure(e) => print("error while listing users", e)
    }

    creditCardRepo.list().onComplete {
      case Success(creditCards) => creditCardList = creditCards
      case Failure(e) => print("error while listing creditCards", e)
    }
  }
}

case class CreatePaymentForm(userId: Long, creditCardId: Long, amount: Int)

case class UpdatePaymentForm(id: Long, userId: Long, creditCardId: Long, amount: Int)
