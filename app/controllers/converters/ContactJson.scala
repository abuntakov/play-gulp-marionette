package controllers.converters

import org.sample.models.{Location, Boundary, Contact}
import play.api.data.validation.ValidationError
import play.api.libs.json._
import play.api.libs.functional.syntax._

object ContactJson {

	implicit val contactWriter: Writes[Contact] = Json.writes[Contact]

	implicit val contactReader: Reads[Contact] = (
		((JsPath \ "id").read[Long] or Reads.pure(null.asInstanceOf[Long])) and
		((JsPath \ "email").read[String] or Reads.pure(null.asInstanceOf[String])) and
		((JsPath \ "location").read[Location] or Reads.pure(null.asInstanceOf[Location])) and
		((JsPath \ "boundary").read(Reads.optionNoError[Boundary]) or Reads.pure(null.asInstanceOf[Option[Boundary]])) and
		((JsPath \ "firstName").read(Reads.optionNoError[String]) or Reads.pure(null.asInstanceOf[Option[String]])) and
		((JsPath \ "lastName").read(Reads.optionNoError[String]) or Reads.pure(null.asInstanceOf[Option[String]])) and
		((JsPath \ "numbers").read[List[Int]] or Reads.pure(null.asInstanceOf[List[Int]])) and
		((JsPath \ "categories").read(Reads.optionNoError[JsValue]) or Reads.pure(null.asInstanceOf[Option[JsValue]]))
	)(Contact.apply _)

	implicit val contactWithFieldsReader: Reads[Model[Contact]] = new Reads[Model[Contact]] {
		override def reads(json: JsValue): JsResult[Model[Contact]] = {
			json.validate[Contact].map { contact =>
				(contact, json.asInstanceOf[JsObject].keys.toSeq)
			}
		}
	}
}
