package org.sample

import scalikejdbc._

package object models {
  val NoId = -1L

  //------------------------ Helpers ------------------------- 

  /** without inheritable fields */
  def extractFieldNames(clazz: Class[_]): Seq[String] = {
    clazz.getDeclaredFields.filterNot(_.isSynthetic).map(_.getName)
  } 

  /** without inheritable fields */
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

  trait EntityWrapper[T] { this: SQLSyntaxSupport[_] =>
    def tableAlias: TableAsAliasSQLSyntax

    val resultName: SyntaxProvider[T]

    val entityFields: Seq[String]

    val creatableFields: Seq[String]

    val updatableFields: Seq[String]


    implicit val wrappers: FieldWrappers = Map(
      classOf[Location] -> LocationFieldWrapper,
      classOf[Boundary] -> BoundaryFieldWrapper
      )

    type FieldWrappers = Map[Class[_], FieldWrapper[_]] 

    def wrapEntity(entity: T, allowedFields: Seq[String])(implicit wrappers: FieldWrappers) = {
      convertToMap(entity).filterKeys(allowedFields.contains).map {
        case(fieldName, Some(fieldValue)) => column.field(fieldName) -> wrapField(fieldValue)
        case(fieldName, None) => column.field(fieldName) -> None
        case(fieldName, fieldValue) => column.field(fieldName) -> wrapField(fieldValue)
      }.toArray
    }

    def wrapField[F](field: F)(implicit wrappers: FieldWrappers): Any = {
      wrappers.get(field.getClass).map { fieldValue => 
        fieldValue.asInstanceOf[FieldWrapper[F]].wrap(field)
      }.getOrElse(field)
    }

    protected def apply(c: SyntaxProvider[T])(rs: WrappedResultSet): T = apply(c.resultName)(rs)

    protected def apply(c: ResultName[T])(rs: WrappedResultSet): T

    def create(entity: T, definedFields: Seq[String] = entityFields)(implicit session: DBSession = autoSession): Long = withSQL {
      insert.into(this).namedValues( wrapEntity(entity, definedFields intersect creatableFields):_ * )
    }.updateAndReturnGeneratedKey().apply()

    def update(entity: T, id: Long, definedFields: Seq[String] = entityFields)(implicit session: DBSession = autoSession) = withSQL {
      QueryDSL.update(this).set( wrapEntity(entity, definedFields intersect updatableFields):_ *  ).where.eq(column.field("id"), id)
    }.update().apply()

    def count()(implicit session: DBSession = ReadOnlyAutoSession): Long = withSQL {
      select(sqls.count).from(tableAlias)
    }.map(rs => rs.long(1)).single().apply().get

    def countBy(where: SQLSyntax)(implicit session: DBSession = ReadOnlyAutoSession): Long = withSQL {
      select(sqls.count).from(tableAlias).where.append(sqls"${where}")
    }.map(rs => rs.long(1)).single().apply().get

    def find(id: Long)(implicit session: DBSession = ReadOnlyAutoSession): Option[T] = withSQL {
      select.from(tableAlias).where.eq(column.id, id)
    }.map(this(resultName)).single().apply

    def findBy(where: SQLSyntax)(implicit session: DBSession = ReadOnlyAutoSession): List[T] = withSQL {
      select.from(tableAlias).where.append(sqls"${where}")
    }.map(this(resultName)).list().apply()


  }

  //-------------------------- Validator --------------------------

  case class Error(fieldName: String, validatorName: String, message: String)

  type Errors = Seq[Error]
  
  def NoErrors: Errors = Seq.empty[Error]

  object Validator {

    def minLength(length: Int)(fieldName: String, fieldValue: Any): Errors = {
      if(fieldValue.asInstanceOf[String].length < length) 
        Seq(Error(fieldName, "minLength", s"Field should be least at $length")) 
      else 
        NoErrors
    }

    def maxLength(length: Int)(fieldName: String, fieldValue: Any): Errors = {
      if(fieldValue.asInstanceOf[String].length > length) 
        Seq(Error(fieldName, "maxLength", s"Field should be max $length")) 
      else 
        NoErrors
    }

    def regexp(pattern: scala.util.matching.Regex)(fieldName: String, fieldValue: Any): Errors = {
      fieldValue.asInstanceOf[String] match {
        case pattern(_) => NoErrors
        case _ => Seq(Error(fieldName, "regexp", "No match with pattern"))
      }
    }

    def email: (String, Any) => Errors = {
      val pattern = """^([a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,4})$""".r
      (fieldName: String, fieldValue: Any) => regexp(pattern)(fieldName, fieldValue).map { error => 
        Error(fieldName, "email", "Email has invalid format")
      }
    }
    def unique[T](entityWrapper: EntityWrapper[T])(fieldName: String, fieldValue:Any): Errors = DB readOnly { implicit session =>
      entityWrapper.countBy(sqls.eq(entityWrapper.resultName.field(fieldName), fieldValue)) match {
        case 0 => NoErrors
        case _ => Seq( Error(fieldName, "unique", "This field should be unique") )
      }
    }



  }

  trait Validator[T] {

    val requiredFields: Seq[String]

    val validators: Map[String, Seq[ (String, Any) => Errors ]]

    def validate(entity: T, definedFields: Seq[String], checkForRequire: Boolean = false): Errors = {
      val requireErrors = if(checkForRequire)
        (requiredFields diff definedFields).map( Error(_, "required", "This field is required") )
      else
        NoErrors

      convertToMap(entity).filterKeys(definedFields.contains).flatMap {
        case (fieldName, None) => NoErrors
        case (fieldName, Some(fieldValue)) => validate(fieldName,fieldValue)
        case (fieldName, fieldValue) => validate(fieldName,fieldValue)
      }.toSeq ++ requireErrors
    }

    protected def validate[V](fieldName:String, fieldValue: V): Errors = {
      validators.get(fieldName).map { checkers =>
        checkers.flatMap(_(fieldName, fieldValue))
      }.getOrElse(NoErrors)
    }
  }
}
