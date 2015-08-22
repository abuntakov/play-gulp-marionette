package org.sample.models

import org.sample.UnitSpec

class ValidatorMinLengthSpec extends UnitSpec {
  import Validator._

  it should "pass with string more or equals 5" in {
    val validator = minLength(5) _
    validator("field", "12345") shouldBe NoErrors
    validator("field", "123456") shouldBe NoErrors
  }

  it should "not pass with string less then 5" in {
    val validator = minLength(5) _
    val errors = validator("field", "1234")
    errors should have size 1
    errors.head should matchPattern { case Error("field", "minLength", _) => }
  }

}
