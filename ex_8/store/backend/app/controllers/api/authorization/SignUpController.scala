package controllers.api.authorization

class SignUpController @Inject()(components: DefaultSilhouetteControllerComponents)(implicit ex: ExecutionContext) extends SilhouetteController(components) {

  def signUp: Action[AnyContent] = unsecuredAction.async { implicit request: Request[AnyContent] =>
    val json = request.body.asJson.get
    val signUpRequest = json.as[SignUpRequest]
    val loginInfo = LoginInfo(CredentialsProvider.ID, signUpRequest.email)

    userRepository.retrieve(loginInfo).flatMap {
      case Some(_) =>
        Future.successful(Forbidden("User already exists"))
      case None =>
        val authInfo = passwordHasherRegistry.current.hash(signUpRequest.password)
        userRepository.create(
          CredentialsProvider.ID,
          signUpRequest.email,
          signUpRequest.email
        ).flatMap { user =>
          authInfoRepository.add(loginInfo, authInfo)
            .map(_ => user)
        }.flatMap { user =>
          authTokenRepository.create(user.id)
            .map(_ => user)
        }.map { user =>
          Json.toJson(user)
        }.map { json =>
          Created(json)
        }
    }
  }
}
