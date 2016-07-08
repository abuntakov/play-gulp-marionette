package controllers

import javax.inject.Inject

import com.thedeanda.lorem.LoremIpsum
import controllers.converters.Model
import org.sample.models.{Contact, ContactValidator, Location}
import converters.ContactJson.contactWithFieldsReader
import play.api.mvc.{Action, Controller}
import play.api.Logger
import errors.createResponseError
import org.apache.commons.lang3.RandomUtils
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.libs.json.Json

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

	def list = Action { implicit request =>
		import converters.ContactJson.contactWriter
		val contacts = Contact.findAll

		Ok(Json.toJson(contacts))
	}

	def generate = Action { implicit request =>
		val lorem = LoremIpsum.getInstance

		val contact = Contact(
			email = lorem.getEmail,
			location = Location(58.0 + RandomUtils.nextDouble(0.0, 2.0), 29.0 +  RandomUtils.nextDouble(0.0, 2.0)),
			firstName = Some(lorem.getFirstName),
			lastName = Some(lorem.getLastName)
		)

		Contact.create(contact, Contact.creatableFields)

		Ok
	}
}
