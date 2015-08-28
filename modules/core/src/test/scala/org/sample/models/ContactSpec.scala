package org.sample.models

import org.sample.DbUnitSpec
import scalikejdbc._
import scalikejdbc.config._
import scalikejdbc.scalatest.AutoRollback

class ContactSpec extends DbUnitSpec with AutoRollback {
  import org.sample.models._

  DBs.setupAll()

  override def fixture(implicit session: DBSession) {
    sql"""
      insert into contacts values 
      (1, ${"john@gmail.com"}, ${"john"}, ${"smith"}, St_GeomFromText(${"POINT(30.12 59.23)"}, 4326), NULL)
    """.update().apply()
  }

  behavior of "Contact"

  it should "create/get a new record" in { implicit session =>
    val before = Contact.count()
    val contact = Contact(
      email = "jack@email.com",
      location = Location(59.2392, 32.2525),
      boundary = Some(Boundary( Location(59.862700, 30.308542), Location(59.862786, 30.354719), Location(59.839421, 30.320387), Location(59.858907, 30.283480) )),
      firstName = Some("Jack"),
      lastName = Some("Black")
      )

    val id: Long = Contact.create(contact)
    Contact.count() should equal(before + 1)

    val savedContact = Contact.find(id).get
    savedContact.id shouldBe id
    savedContact.location shouldBe contact.location
    savedContact.boundary shouldBe contact.boundary
    savedContact shouldBe contact.copy(id = id)
  }

  it should "not update id and email field" in { implicit session => 
    val contact = Contact.find(1L).get
    val changedContact = contact.copy(
      id = 300L,
      email = "newemail@gamil.com",
      firstName = Some("new firstName"),
      lastName = Some("new lastName")
      )

    Contact.update(changedContact, 1L)

    Contact.find(changedContact.id) shouldBe None

    val savedContact = Contact.find(1L).get
    savedContact.email shouldBe "john@gmail.com"
    savedContact.firstName shouldBe changedContact.firstName
    savedContact.lastName shouldBe changedContact.lastName
  }

  it should "not update firstName field" in { implicit session => 
    val contact = Contact.find(1L).get
    val changedContact = contact.copy(
      firstName = Some("new firstName"),
      lastName = Some("new lastName")
      )

    Contact.update(changedContact, 1L, Seq("lastName"))

    val savedContact = Contact.find(1L).get
    savedContact.firstName shouldBe Some("john")
    savedContact.lastName shouldBe changedContact.lastName
  }

  it should "return one record" in { implicit session =>
    val contacts = Contact.findBy(sqls.eq(Contact.column.email, "john@gmail.com")) 
    contacts should have size 1
  }

  it should "not pass with already existing email" in { implicit session =>
    val contact = Contact(
      email = "john@gmail.com",
      location = Location(59.2392, 32.2525),
      boundary = None
      )

    val errors = ContactValidator.validate(contact, Seq("email", "location", "boundary"))
    errors should have size 1
    errors.head should matchPattern { case Error("email", "unique", _) => }
  }
}
