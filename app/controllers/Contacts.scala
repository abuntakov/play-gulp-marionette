package controllers

import javax.inject.Inject

import controllers.converters.Model
import org.sample.models.{Contact, ContactValidator}
import converters.ContactJson.contactWithFieldsReader
import play.api.mvc.{Action, Controller}
import play.api.Logger
import errors.createResponseError
import play.api.i18n.{I18nSupport, MessagesApi}

class Contacts @Inject()(val messagesApi: MessagesApi) extends Controller with I18nSupport {
	val logger = Logger(this.getClass)

	def update(id: Long) = Action(parse.json) { implicit request =>
		val (contact, fields) = request.body.as[Model[Contact]]

		ContactValidator.validate(contact, fields) match {
			case Nil =>
				Contact.update(contact, id, fields)
				Ok
			case errors => createResponseError(errors)
		}
	}
}
