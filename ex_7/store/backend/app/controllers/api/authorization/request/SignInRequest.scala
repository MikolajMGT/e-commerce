package controllers.api.authorization.request

case class SignInRequest(email: String, password: String)

object SignInRequest {
  implicit val signInRequestForm: OFormat[SignInRequest] = Json.using[Json.WithDefaultValues].format[SignInRequest]
}
