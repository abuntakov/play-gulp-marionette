package org.sample.models

import org.sample.UnitSpec
import org.sample.helpers.ClassHelper.extractFieldNames

class PackageSpec extends UnitSpec {

	case class Parent(id: Long, name: String, phone: Option[String])

	//class Child(id: Long, name: String, phone: Option[String], age: Option[Long]) extends Parent(id, name, phone)

	val fieldNames = Seq("id", "phone", "name")

	it should "contains all field names for class" in {
		extractFieldNames(classOf[Parent]) should contain theSameElementsAs fieldNames
	}

	it should "contains all field names for instance" in {
		val instance = new Parent(1L, "Some name", None)
		extractFieldNames(instance.getClass) should contain theSameElementsAs fieldNames
	}
}
