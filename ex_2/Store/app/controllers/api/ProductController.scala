package controllers.api

import akka.actor.ActorSystem
import model.Sample
import play.api.libs.json.Json
import play.api.mvc._

import javax.inject._
import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future, Promise}

@Singleton
class ProductController @Inject()(cc: ControllerComponents, actorSystem: ActorSystem)(implicit exec: ExecutionContext) extends AbstractController(cc) {

  private def performSampleAction(delayTime: FiniteDuration, name: String = "sample_name"): Future[Sample] = {
    val sample = Sample("sample_id", name)
    val promise: Promise[Sample] = Promise[Sample]()
    actorSystem.scheduler.scheduleOnce(delayTime) {
      promise.success(sample)
    }(actorSystem.dispatcher)
    promise.future
  }

  def getProduct(id: String): Action[AnyContent] = Action.async {
    println("getProduct:", id)
    performSampleAction(10.millisecond).map { msg => Ok(Json.toJson(msg)) }
  }

  def listProducts(name: String): Action[AnyContent] = Action.async {
    println("listProducts:", name)
    performSampleAction(10.millisecond).map { msg => Ok(Json.toJson(List(msg))) }
  }

  def createProduct(): Action[AnyContent] = Action.async { implicit request =>
    println("createProduct:", request.body)
    performSampleAction(10.millisecond, request.body.asJson.get("name").as[String]).map { msg => Ok(Json.toJson(msg)) }
  }

  def updateProduct(): Action[AnyContent] = Action.async { implicit request =>
    println("updateProduct:", request.body)
    performSampleAction(10.millisecond, request.body.asJson.get("name").as[String]).map { msg => Ok(Json.toJson(msg)) }
  }

  def deleteProduct(id: String): Action[AnyContent] = Action.async {
    println("deleteProduct:", id)
    performSampleAction(10.millisecond).map { msg => Ok(Json.toJson(msg)) }
  }
}
