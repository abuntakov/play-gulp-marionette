package org.sample

import org.slf4j.LoggerFactory
import org.sample.models.Contact
import scalikejdbc._
import scalikejdbc.config._

object Application extends App {
  val logger = LoggerFactory.getLogger(this.getClass)

  logger.info("It works!")
  logger.info("Creatable fields: " + Contact.creatableFields.mkString(","))
  logger.info("Updatable fields: " + Contact.updatableFields.mkString(","))

  DBs.setupAll()

  val contact = Contact(email = "test@email.com", firstName = Some("alex"), lastName = null)
  val id = Contact.create( contact )

  logger.info("Saved with id=" + id)

  val savedContact = Contact.find(id)

  logger.info(s"Found contact: $savedContact")

  DBs.closeAll()
}
