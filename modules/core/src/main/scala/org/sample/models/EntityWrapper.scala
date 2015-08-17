package org.sample.models

import scalikejdbc._

trait FieldWrapper[T] {
  def wrap(field: T): Any
}

object LocationFieldWrapper extends FieldWrapper[Location] {
  def wrap(field: Location) = ParameterBinder[Location](
    value = field,
    binder = (stmt: java.sql.PreparedStatement, idx: Int) => stmt.setObject(idx, toGeometry(field))
    )
}

trait EntityWrapper { this: SQLSyntaxSupport[_] =>
  implicit val fieldWrappers: FieldWrappers = Map(
    classOf[Location] -> LocationFieldWrapper
    )

  type FieldWrappers = Map[Class[_], FieldWrapper[_]] 

  def wrapEntity[T](entity: T, allowedFields: Seq[String])(implicit fieldWrappers: FieldWrappers) = {
    convertToMap(entity).filterKeys(allowedFields.contains).filterNot(isNullable).map { case (fieldName, fieldValue) =>
      column.field(fieldName) -> wrapField(fieldValue)
    }.toArray
  }

  def wrapField[T](field: T)(implicit fieldWrappers: FieldWrappers) = {
    fieldWrappers.get(field.getClass).map(_.asInstanceOf[FieldWrapper[T]].wrap(field)).getOrElse(field)
  }


}
