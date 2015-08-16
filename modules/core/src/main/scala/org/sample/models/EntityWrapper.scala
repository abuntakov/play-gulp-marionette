package org.sample.models

import scalikejdbc._

trait FieldWrapper[T] {
  def wrap(field: T): Any
}

trait EntityWrapper { this: SQLSyntaxSupport[_] =>
  implicit val fieldWrappers: FieldWrappers = Map.empty

  type FieldWrappers = Map[Class[_], FieldWrapper[_]] 

  def extractFieldNames(clazz: Class[_]): Seq[String] = clazz.getDeclaredFields.filterNot(_.isSynthetic).map(_.getName)

  def convertToMap[T](obj: T) = (Map[String, Any]() /: obj.getClass.getDeclaredFields.filterNot(_.isSynthetic)) { (result, field) => 
    field.setAccessible(true)
    result + (field.getName -> field.get(obj))
  }

  def wrapEntity[T](entity: T, allowedFields: Seq[String])(implicit fieldWrappers: FieldWrappers) = {
    convertToMap(entity).toArray.filter { case (fieldName, _) =>
      allowedFields.contains(fieldName)
    } map { case (fieldName, fieldValue) =>
      column.field(fieldName) -> wrapField(fieldValue)
    }
  }

  def wrapField[T](field: T)(implicit fieldWrappers: FieldWrappers) = {
    fieldWrappers.get(field.getClass).map(_.asInstanceOf[FieldWrapper[T]].wrap(field)).getOrElse(field)
  }


}
