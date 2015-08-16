package org.sample

package object models {
  val NoId = -1L

  val isNullable: PartialFunction[(_, Any), Boolean] = { case (_, value) => 
    value == null
  }

  def extractFieldNames(clazz: Class[_]): Seq[String] = clazz.getDeclaredFields.filterNot(_.isSynthetic).map(_.getName)

  def convertToMap[T](obj: T) = (Map[String, Any]() /: obj.getClass.getDeclaredFields.filterNot(_.isSynthetic)) { (result, field) => 
    field.setAccessible(true)
    result + (field.getName -> field.get(obj))
  }

}
