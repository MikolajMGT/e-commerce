package controllers.api

import akka.actor.ActorSystem
import model.Sample
import play.api.libs.json.Json
import play.api.mvc._

import javax.inject._
import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future, Promise}

@Singleton
class CreditCardController @Inject()(cc: ControllerComponents, actorSystem: ActorSystem)(implicit exec: ExecutionContext) extends AbstractController(cc) {

  private def performSampleAction(delayTime: FiniteDuration, name: String = "sample_name"): Future[Sample] = {
    val sample = Sample("sample_id", name)
    val promise: Promise[Sample] = Promise[Sample]()
    actorSystem.scheduler.scheduleOnce(delayTime) {
      promise.success(sample)
    }(actorSystem.dispatcher)
    promise.future
  }

  def getCreditCard(id: String): Action[AnyContent] = Action.async {
    println("getCreditCard:", id)
    performSampleAction(10.millisecond).map { msg => Ok(Json.toJson(msg)) }
  }

  def listCreditCards(name: String): Action[AnyContent] = Action.async {
    println("listCreditCards:", name)
    performSampleAction(10.millisecond).map { msg => Ok(Json.toJson(List(msg))) }
  }

  def createCreditCard(): Action[AnyContent] = Action.async { implicit request =>
    println("createCreditCard:", request.body)
    performSampleAction(10.millisecond, request.body.asJson.get("name").as[String]).map { msg => Ok(Json.toJson(msg)) }
  }

  def updateCreditCard(): Action[AnyContent] = Action.async { implicit request =>
    println("updateCreditCard:", request.body)
    performSampleAction(10.millisecond, request.body.asJson.get("name").as[String]).map { msg => Ok(Json.toJson(msg)) }
  }

  def deleteCreditCard(id: String): Action[AnyContent] = Action.async {
    println("deleteCreditCard:", id)
    performSampleAction(10.millisecond).map { msg => Ok(Json.toJson(msg)) }
  }
}
