package org.sample

import scalikejdbc.TypeBinder
import java.sql.ResultSet
import org.postgis._

package object models {
  val NoId = -1L

  val isNullable: PartialFunction[(_, Any), Boolean] = { case (_, value) => 
    value == null
  }

  def extractFieldNames(clazz: Class[_]): Seq[String] = {
    clazz.getDeclaredFields.filterNot(_.isSynthetic).map(_.getName)
  } 

  def convertToMap[T](obj: T) = (Map[String, Any]() /: obj.getClass.getDeclaredFields.filterNot(_.isSynthetic)) { (result, field) => 
    field.setAccessible(true)
    result + (field.getName -> field.get(obj))
  }

  case class Location(lat: Double, lng: Double)

  case class Boundary(locations: Location*) {
    def toRing = locations :+ locations.head
  }

  def toPoint(location: Location) = new Point(location.lng, location.lat)

  def toGeometry(location: Location) = {
    val point = toPoint(location)
    point.setSrid(4326)
    new PGgeometry(point)
  }
  
  def toGeometry(boundary: Boundary) = {
    val linearRing = new LinearRing( boundary.toRing.map(toPoint).toArray )
    val polygon = new Polygon( Array(linearRing) )
    new PGgeometry(polygon)
  }

  implicit val locationTypeBinder: TypeBinder[Location] = new TypeBinder[Location] {
    override def apply(rs: ResultSet, columnIndex: Int): Location = rowToLocation(rs.getObject(columnIndex).asInstanceOf[PGgeometry])

    override def apply(rs: ResultSet, columnLabel: String): Location = rowToLocation(rs.getObject(columnLabel).asInstanceOf[PGgeometry])

    private def rowToLocation(geo: PGgeometry) = {
      val point = geo.getGeometry.asInstanceOf[Point]
      Location(point.y, point.x)
    }
  }

  implicit val boundaryTypeBinder: TypeBinder[Boundary] = new TypeBinder[Boundary] {
    override def apply(rs: ResultSet, columnIndex: Int): Boundary = rowToLocation(rs.getObject(columnIndex).asInstanceOf[PGgeometry])

    override def apply(rs: ResultSet, columnLabel: String): Boundary = rowToLocation(rs.getObject(columnLabel).asInstanceOf[PGgeometry])

    private def rowToLocation(geo: PGgeometry) = {
      val points = geo.getGeometry.asInstanceOf[Polygon].getRing(0).getPoints.map { p =>
        Location(p.y, p.x)
      }.dropRight(1)
      Boundary(points:_ *)
    }
  }

}
