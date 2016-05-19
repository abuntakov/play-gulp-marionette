package org.sample

import org.slf4j.LoggerFactory
import org.sample.models._
import play.api.libs.json.{JsValue, Json}
import scalikejdbc._
import scalikejdbc.config._

object Application extends App {
  val logger = LoggerFactory.getLogger(this.getClass)

  logger.info("It works!")
  logger.info("Creatable fields: " + Contact.creatableFields.mkString(","))
  logger.info("Updatable fields: " + Contact.updatableFields.mkString(","))

  DBs.setupAll()

  val boundary = Boundary( Location(59.862700, 30.308542), Location(59.862786, 30.354719), Location(59.839421, 30.320387), Location(59.858907, 30.283480) )

  val contact = Contact(
    email = "test@email.com",
    location = Location(59.8944, 30.2641),
    boundary = Some(boundary),
    firstName = Some("alex"),
    lastName = null,
    numbers = List(1,2,4),
    categories = Some( Json.obj("foo" -> "bar", "x" -> 5) )
  )

  val id = Contact.create( contact, Contact.entityFields diff Seq("lastName") )

  logger.info("Saved with id=" + id)

  val savedContact = Contact.find(id).get

  logger.info(s"Found contact: $savedContact")
  
  val updateResult = Contact.update(savedContact.copy(email = "ERROR", firstName = Some("Jack"), lastName = Some("Smith")), id)

  
  logger.info(s"Update result: $updateResult")

  val updatedContact = Contact.find(id)

  logger.info(s"Updated contact: $updatedContact")
  

  {
    val reqErrors = ContactValidator.validate(Contact(NoId, null, null, None), Contact.entityFields diff Seq("id", "email", "location"), checkForRequire = true)
    logger.info(s"Errors: $reqErrors")
  }

  {
    val reqErrors = ContactValidator.validate(Contact(NoId, null, null, None, Some("shor")), Contact.entityFields diff Seq("id", "email", "location"), checkForRequire = true)
    logger.info(s"Errors: $reqErrors")
  }

  {
    val reqErrors = ContactValidator.validate(Contact.apply(NoId, "test@email.com", null, None, Some("shor")), Contact.entityFields diff Seq("id", "location"), checkForRequire = true)
    logger.info(s"Errors: $reqErrors")
  }




  DBs.closeAll()
}
