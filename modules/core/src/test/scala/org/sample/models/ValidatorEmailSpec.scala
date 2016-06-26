package org.sample.models

import org.sample.UnitSpec

class ValidatorEmailSpec extends UnitSpec {
	import Validator._

	it should "pass with email john@gmail.com" in {
		val validator = email
		validator("field", "john@gmail.com") shouldBe NoErrors
	}

	it should "pass with email john+smith@gmail.com" in {
		val validator = email
		validator("field", "john+smith@gmail.com") shouldBe NoErrors
	}

	it should "not pass with john@gmail" in {
		val validator = email
		val errors = validator("field", "john@gmail")
		errors should have size 1
		errors.head should matchPattern { case Error("field", "email", _) => }
	}

	it should "throw ClassCastException if applied to non String" in {
		intercept[ClassCastException] { email("field", 12345) }
	}

}
