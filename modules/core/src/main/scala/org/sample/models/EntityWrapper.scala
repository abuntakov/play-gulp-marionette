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

object BoundaryFieldWrapper extends FieldWrapper[Boundary] {
  def wrap(field: Boundary) = ParameterBinder[Boundary](
    value = field,
    binder = (stmt: java.sql.PreparedStatement, idx: Int) => stmt.setObject(idx, toGeometry(field))
    )
}

trait EntityWrapper { this: SQLSyntaxSupport[_] =>
  implicit val fieldWrappers: FieldWrappers = Map(
    classOf[Location] -> LocationFieldWrapper,
    classOf[Boundary] -> BoundaryFieldWrapper
    )

  type FieldWrappers = Map[Class[_], FieldWrapper[_]] 

  def wrapEntity[T](entity: T, allowedFields: Seq[String])(implicit fieldWrappers: FieldWrappers) = {
    convertToMap(entity).filterKeys(allowedFields.contains).filterNot(isNullable).map {
      case (fieldName, fieldValue) =>
        val wrappedField = if(fieldValue.isInstanceOf[Option[_]]) 
          fieldValue.asInstanceOf[Option[_]].map(wrapField) 
        else 
          wrapField(fieldValue)
        
        column.field(fieldName) -> wrappedField
    }.toArray
  }

  def wrapField[T](field: T)(implicit fieldWrappers: FieldWrappers): Any = {
    fieldWrappers.get(field.getClass).map(_.asInstanceOf[FieldWrapper[T]].wrap(field)).getOrElse(field)
  }


}
