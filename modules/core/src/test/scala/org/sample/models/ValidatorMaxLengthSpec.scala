package org.sample.models

import org.sample.UnitSpec

class ValidatorMaxLengthSpec extends UnitSpec {
	import Validator._

	it should "pass with string less or equals then 5" in {
		val validator = maxLength(5) _
		validator("field", "12345") shouldBe NoErrors
		validator("field", "123") shouldBe NoErrors
	}

	it should "not pass with string more then 5" in {
		val validator = maxLength(5) _
		val errors = validator("field", "123456")
		errors should have size 1
		errors.head should matchPattern { case Error("field", "maxLength", _) => }
	}

	it should "throw ClassCastException if applied to non String" in {
		intercept[ClassCastException] { maxLength(5)("field", 12345) }
	}
}
