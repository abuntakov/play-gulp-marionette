package org.sample

// import java.sql.ResultSet
import scalikejdbc._

package object models {
  val NoId = -1L

  //------------------------ Helpers ------------------------- 

  val isNullable: PartialFunction[(_, Any), Boolean] = { case (_, value) => 
    value == null
  }

  def extractFieldNames(clazz: Class[_]): Seq[String] = {
    clazz.getDeclaredFields.filterNot(_.isSynthetic).map(_.getName)
  } 

  def convertToMap[T](obj: T) = {
    val fields = obj.getClass.getDeclaredFields.filterNot(_.isSynthetic)

    (Map[String, Any]() /: fields) { (result, field) => 
      field.setAccessible(true)
      result + (field.getName -> field.get(obj))
    }
  }

  //-------------------------- Wrapper --------------------------

  trait FieldWrapper[T] {
    def wrap(field: T): Any
  }

  trait EntityWrapper { this: SQLSyntaxSupport[_] =>
    implicit val wrappers: FieldWrappers = Map(
      classOf[Location] -> LocationFieldWrapper,
      classOf[Boundary] -> BoundaryFieldWrapper
      )

    type FieldWrappers = Map[Class[_], FieldWrapper[_]] 

    def wrapEntity[T](entity: T, allowedFields: Seq[String])(implicit wrappers: FieldWrappers) = {
      convertToMap(entity).filterKeys(allowedFields.contains).filterNot(isNullable).map {
        case(fieldName, Some(fieldValue)) => column.field(fieldName) -> wrapField(fieldValue)
        case(fieldName, None) => column.field(fieldName) -> None
        case(fieldName, fieldValue) => column.field(fieldName) -> wrapField(fieldValue)
      }.toArray
    }

    def wrapField[T](field: T)(implicit wrappers: FieldWrappers): Any = {
      wrappers.get(field.getClass).map { fieldValue => 
        fieldValue.asInstanceOf[FieldWrapper[T]].wrap(field)
      }.getOrElse(field)
    }
  }

  //-------------------------- Validator --------------------------

  case class Error(fieldName: String, validatorName: String, message: String)

  trait Validator[T] {
    val requiredFields: Seq[String]

    val validators:Map[String, Seq[ (String, Any) => Seq[Error] ]]

    def validate(entity: T, checkForRequire: Boolean = false): Seq[Error] = {
      convertToMap(entity).flatMap { 
        case (fieldName, null) if checkForRequire && requiredFields.contains(fieldName) => 
          Seq[Error](Error(fieldName, "required", "This field is required"))
        case (fieldName, null) => Seq.empty[Error]
        case (fieldName, None) => Seq.empty[Error]
        case (fieldName, Some(fieldValue)) => validate(fieldName,fieldValue)
        case (fieldName, fieldValue) => validate(fieldName,fieldValue)
      }.toSeq
    }

    protected def validate[V](fieldName:String, fieldValue: V): Seq[Error] = {
      validators.get(fieldName).map { checkers =>
        checkers.flatMap(_(fieldName, fieldValue))
      }.getOrElse(Seq.empty[Error])
    }
  }
}
