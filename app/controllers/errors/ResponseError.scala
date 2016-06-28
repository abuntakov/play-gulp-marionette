package controllers.errors

import play.api.libs.json.Json
import org.sample.models.{Error, Errors}

case class ResponseError(status: ErrorStatus.ErrorStatus, message: Option[String], validationErrors: Option[Errors] = None)

object ResponseError {
	implicit val validationErrorWriter = Json.writes[Error]
	implicit val errorWriter = Json.writes[ResponseError]
}
