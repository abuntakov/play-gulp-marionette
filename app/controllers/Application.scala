package controllers

import play.api.mvc.{Action, Controller}

class Application extends Controller {
	val index = Action { implicit request =>
		Ok(views.html.main())
	}
}
