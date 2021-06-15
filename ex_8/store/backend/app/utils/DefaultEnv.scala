package utils

trait DefaultEnv extends Env {
  type I = User
  type A = CookieAuthenticator
}