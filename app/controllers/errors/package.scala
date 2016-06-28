package controllers

import org.sample.models.Errors
import play.api.i18n.Messages
import play.api.libs.json.Json
import play.api.mvc.Result
import play.api.mvc.Results.UnprocessableEntity

package object errors {
	object ErrorStatus extends Enumeration {
		type ErrorStatus = Value
		val
		NOT_FOUND, VALIDATION_ERROR = Value
	}

	def createResponseError(errors: Errors)(implicit msgApi: Messages): Result = {
		val error = ResponseError(
			ErrorStatus.VALIDATION_ERROR,
			Option(Messages("response.error.message.validation.error")),
			validationErrors = Option(errors)
		)

		UnprocessableEntity(Json.toJson(error))
	}

	def createResponseError(status: ErrorStatus.ErrorStatus, message: String, args: Any*)(implicit msgApi: Messages): Result = {
		createResponseError(status, Some(message), args: _*)
	}

	def createResponseError(status: ErrorStatus.ErrorStatus, message: Option[String], args: Any*)(implicit msgApi: Messages): Result = {
		val error = ResponseError(status, message.map(m => Messages(m, args: _*)))
		UnprocessableEntity(Json.toJson(error))
	}
}
