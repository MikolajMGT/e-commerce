package controllers.api.authorization.request

case class SignUpRequest(email: String, password: String)

object SignUpRequest {
  implicit val signUpRequestForm: OFormat[SignUpRequest] = Json.using[Json.WithDefaultValues].format[SignUpRequest]
}
