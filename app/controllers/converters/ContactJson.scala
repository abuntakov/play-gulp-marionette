package controllers.converters

import org.sample.models.{Location, Boundary, Contact}
import play.api.libs.json.{Json, Reads, JsPath}
import play.api.libs.functional.syntax._

object ContactJson {

  implicit val contactReader: Reads[Contact] = (
    ((JsPath \ "id").read[Long] or Reads.pure(null.asInstanceOf[Long])) and
    ((JsPath \ "email").read[String] or Reads.pure(null.asInstanceOf[String])) and
    ((JsPath \ "location").read[Location] or Reads.pure(null.asInstanceOf[Location])) and
    ((JsPath \ "boundary").read(Reads.optionNoError[Boundary]) or Reads.pure(null.asInstanceOf[Option[Boundary]])) and
    ((JsPath \ "firstName").read(Reads.optionNoError[String]) or Reads.pure(null.asInstanceOf[Option[String]])) and
    ((JsPath \ "lastName").read(Reads.optionNoError[String]) or Reads.pure(null.asInstanceOf[Option[String]]))
  )(Contact.apply _)
}
