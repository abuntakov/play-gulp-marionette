package controllers

import play.api.mvc.{Action, Controller}

object Application extends Controller {
  val index = Action { implicit request => 
    Ok("Hello world!")
  }
}
