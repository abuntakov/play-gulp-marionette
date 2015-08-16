package org.sample.models

import scalikejdbc._

case class Contact(id: Long = NoId, email: String, firstName: Option[String] = None, lastName: Option[String] = None)

object Contact extends SQLSyntaxSupport[Contact] with EntityWrapper {
  val c = Contact.syntax("c")

  override val tableName = "contacts"

  val creatableFields = extractFieldNames(classOf[Contact]) diff Seq("id")

  val updatableFields = extractFieldNames(classOf[Contact]) diff Seq("id", "email")

  def apply(c: SyntaxProvider[Contact])(rs: WrappedResultSet): Contact = apply(c.resultName)(rs)

  def apply(c: ResultName[Contact])(rs: WrappedResultSet): Contact = Contact(
    id = rs.get[Long](c.id),
    email = rs.get[String](c.email),
    firstName = rs.get[Option[String]](c.firstName),
    lastName = rs.get[Option[String]](c.lastName))

  def create(contact: Contact)(implicit session: DBSession = autoSession): Long = withSQL {
    insert.into(Contact).namedValues( wrapEntity(contact, creatableFields):_ * )
  }.updateAndReturnGeneratedKey().apply()

}
