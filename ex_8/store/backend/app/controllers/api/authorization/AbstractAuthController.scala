package controllers.api.authorization

abstract class AbstractAuthController(scc: DefaultSilhouetteControllerComponents)(implicit ex: ExecutionContext) extends SilhouetteController(scc) {

  protected def authenticateUser(user: User)(implicit request: RequestHeader): Future[AuthenticatorResult] = {
    authenticatorService.create(user.loginInfo)
      .flatMap { authenticator =>
        authenticatorService.init(authenticator).flatMap { v =>
          authenticatorService.embed(v, Ok(Json.toJson(user)))
        }
      }
  }
}
