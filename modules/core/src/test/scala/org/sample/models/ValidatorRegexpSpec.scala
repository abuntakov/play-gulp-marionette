package org.sample.models

import org.sample.UnitSpec

class ValidatorRegexpSpec extends UnitSpec {
  import Validator._

  it should "pass with string with [a-z]+ " in {
   val validator = regexp("[a-z]+".r.pattern) _ 
   validator("field", "helloworld") shouldBe NoErrors
  }

  it should "not pass with string [a-z]+" in {
    val validator = regexp("[a-z]+".r.pattern) _
    val errors = validator("field", "hello world")
    errors should have size 1
    errors.head should matchPattern { case Error("field", "regexp", _) => }
  }

  it should "throw ClassCastException if applied to non String" in {
    intercept[ClassCastException] { regexp("[0-9]+".r.pattern)("field", 12345) }
  }

}
