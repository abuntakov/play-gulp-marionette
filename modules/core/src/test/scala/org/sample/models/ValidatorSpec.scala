package org.sample.models

import org.sample.UnitSpec
import org.sample.helpers.ClassHelper.extractFieldNames

class ValidatorSpec extends UnitSpec {
	import Validator._

	case class Sample(id: Long, email: String, name: Option[String])

	object Sample {
		val entityFields = extractFieldNames(classOf[Sample])
	}

	object SampleValidator extends Validator[Sample] {
		val requiredFields = Seq("id", "email")

		val validators = Map(
			"name" -> Seq(minLength(9) _, maxLength(12) _),
			"email" -> Seq(email, minLength(8) _, maxLength(16) _)
		)
	}

	it should "pass without required fields" in {
		val sample = Sample(1L, null, None)
		val definedFields = Sample.entityFields diff Seq("email")
		SampleValidator.validate(sample, definedFields, checkForRequire = false) shouldBe NoErrors
	}

	it should "not pass without required fields" in {
		val sample = Sample(1L, null, None)
		val definedFields = Sample.entityFields diff Seq("email")
		val errors = SampleValidator.validate(sample, definedFields, checkForRequire = true)
		errors should have size 1
		errors.head should matchPattern { case Error("email", "required", _) => }
	}

	it should "validate option field" in {
		val sample = Sample(1L, null, Some("short"))
		val definedFields = Sample.entityFields diff Seq("email")
		val errors = SampleValidator.validate(sample, definedFields, checkForRequire = false)
		errors should have size 1
		errors.head should matchPattern { case Error("name", "minLength", _) => }
	}

	it should "return several errors for one field" in {
		val sample = Sample(1L, "short", None)
		val definedFields = Sample.entityFields
		val errors = SampleValidator.validate(sample, definedFields, checkForRequire = false)
		val expected = Seq(Error("email", "email", ""), Error("email", "minLength", ""))
		errors should have size 2
		errors.map(_.copy(message = "")) should contain theSameElementsAs expected
	}

	it should "return several errors for more then one fields" in {
		val sample = Sample(NoId, "john@smith", Some("short"))
		val definedFields = Sample.entityFields diff Seq("id")
		val errors = SampleValidator.validate(sample, definedFields, checkForRequire = true)
		val expected = Seq( Error("email", "email", ""), Error("id", "required", ""), Error("name", "minLength", "") )
		errors should have size 3
		errors.map(_.copy(message = "")) should contain theSameElementsAs expected
	}

}
