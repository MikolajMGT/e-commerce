package controllers.api.authorization

class SocialAuthController @Inject()(scc: DefaultSilhouetteControllerComponents, addToken: CSRFAddToken)(implicit ex: ExecutionContext) extends SilhouetteController(scc) {

  def authenticate(provider: String): Action[AnyContent] = addToken(Action.async { implicit request: Request[AnyContent] =>
    (socialProviderRegistry.get[SocialProvider](provider) match {
      case Some(p: SocialProvider with CommonSocialProfileBuilder) =>
        p.authenticate().flatMap {
          case Left(result) => Future.successful(result)
          case Right(authInfo) =>
            for {
              profile <- p.retrieveProfile(authInfo)
              res <- userRepository.getByEmailOption(profile.email.getOrElse(""))
              user <- if (res.orNull == null) userRepository.create(profile.loginInfo.providerID, profile.loginInfo.providerKey, profile.email.getOrElse("")) else userRepository.getByEmail(profile.email.getOrElse(""))
              _ <- authInfoRepository.save(profile.loginInfo, authInfo)
              authenticator <- authenticatorService.create(profile.loginInfo)
              value <- authenticatorService.init(authenticator)
              result <- authenticatorService.embed(value, Redirect("http://localhost:3000"))
            } yield {
              val Token(name, value) = CSRF.getToken.get
              result.withCookies(Cookie(name, value, httpOnly = false), Cookie("userId", user.id.toString, httpOnly = false))
            }
        }
      case e =>
        println(e)
        Future.failed(new ProviderException(s"Cannot authenticate with unexpected social provider $provider"))
    }).recover {
      case e: ProviderException =>
        println(e, "XDDDDDDDD")
        Forbidden("Forbidden")
    }
  })
}
