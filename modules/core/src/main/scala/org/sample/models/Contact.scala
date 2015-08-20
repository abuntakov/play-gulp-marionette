package org.sample.models

import scalikejdbc._

case class Contact(
  id: Long = NoId,
  email: String,
  location: Location,
  boundary: Option[Boundary],
  firstName: Option[String] = None,
  lastName: Option[String] = None)

object Contact extends SQLSyntaxSupport[Contact] with EntityWrapper {
  val c = Contact.syntax("c")

  override val tableName = "contacts"

  val creatableFields = extractFieldNames(classOf[Contact]) diff Seq("id")

  val updatableFields = extractFieldNames(classOf[Contact]) diff Seq("id", "email")

  private def apply(c: SyntaxProvider[Contact])(rs: WrappedResultSet): Contact = apply(c.resultName)(rs)

  private def apply(c: ResultName[Contact])(rs: WrappedResultSet): Contact = autoConstruct(rs, c)

  def create(contact: Contact)(implicit session: DBSession = autoSession): Long = withSQL {
    insert.into(Contact).namedValues( wrapEntity(contact, creatableFields):_ * )
  }.updateAndReturnGeneratedKey().apply()

  def update(contact: Contact, id: Long)(implicit session: DBSession = autoSession) = withSQL {
    QueryDSL.update(Contact).set( wrapEntity(contact, updatableFields):_ *  ).where.eq(column.field("id"), id)
  }.update.apply()

  def find(id: Long)(implicit session: DBSession = autoSession): Option[Contact] = withSQL {
    select.from(Contact as c).where.eq(c.id, id)
  }.map(Contact(c)).single().apply
}

object ContactValidator extends Validator[Contact] {
  val requiredFields = Seq("email", "location")

  val validators = Map.empty[String, Seq[ (String, Any) => Seq[Error] ]]
}
