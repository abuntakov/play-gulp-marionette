package org.sample.models

import play.api.libs.json.JsValue
import scalikejdbc._
import org.sample.helpers.ClassHelper._

case class Contact(
	id: Long = NoId,
	email: String,
	location: Location,
	boundary: Option[Boundary] = None,
	firstName: Option[String] = None,
	lastName: Option[String] = None,
	numbers: List[Int] = List.empty[Int],
	categories: Option[JsValue] = None
)

object Contact extends SQLSyntaxSupport[Contact] with EntityWrapper[Contact] {

	implicit val wrappers: FieldWrappers = Map(
		"location" -> LocationFieldWrapper,
		"boundary" -> BoundaryFieldWrapper,
		"numbers" -> IntListFieldWrapper,
		"categories" -> JsonFieldWrapper
	)

	val resultName = Contact.syntax("c")

	def tableAlias = Contact as resultName

	override val tableName = "contacts"

	val entityFields = extractFieldNames(classOf[Contact])

	val creatableFields = entityFields diff Seq("id")

	val updatableFields = entityFields diff Seq("id", "email")

	protected def apply(c: ResultName[Contact])(rs: WrappedResultSet): Contact = autoConstruct(rs, c)

}

object ContactValidator extends Validator[Contact] {
	import Validator._

	val requiredFields = Seq("email", "location")

	val FirstNameMinLength = 5



	val validators = Map[ String, Seq[(String, Any) => Errors] ](
		"firstName" -> Seq( minLength(FirstNameMinLength) _ ),
		"email" -> Seq( unique(Contact) _ )
	)


}
