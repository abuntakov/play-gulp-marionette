package org.sample

import scalikejdbc.TypeBinder
import java.sql.ResultSet
import org.postgis.{Point, PGgeometry}

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

  case class Location(lat: Double, lng: Double)

  def toGeometry(location: Location) = {
    val point = new Point(location.lng, location.lat)
    point.setSrid(4326)
    new PGgeometry(point)
  }

  implicit val locationTypeBinder: TypeBinder[Location] = new TypeBinder[Location] {
    override def apply(rs: ResultSet, columnIndex: Int): Location = rowToLocation(rs.getObject(columnIndex).asInstanceOf[PGgeometry])

    override def apply(rs: ResultSet, columnLabel: String): Location = rowToLocation(rs.getObject(columnLabel).asInstanceOf[PGgeometry])

    private def rowToLocation(geo: PGgeometry) = {
      val point = geo.getGeometry.asInstanceOf[Point]
      Location(point.y, point.x)
    }
  }


}
