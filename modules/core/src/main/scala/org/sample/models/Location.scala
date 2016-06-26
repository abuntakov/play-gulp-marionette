package org.sample.models

import scalikejdbc._
import java.sql.ResultSet
import org.postgis._

case class Location(lat: Double, lng: Double)

object Location {
	implicit val locationTypeBinder: TypeBinder[Location] = new TypeBinder[Location] {
		override def apply(rs: ResultSet, columnIndex: Int): Location = {
			rowToLocation(rs.getObject(columnIndex).asInstanceOf[PGgeometry])
		}

		override def apply(rs: ResultSet, columnLabel: String): Location = {
			rowToLocation(rs.getObject(columnLabel).asInstanceOf[PGgeometry])
		}

		private def rowToLocation(geo: PGgeometry) = {
			val point = geo.getGeometry.asInstanceOf[Point]
			Location(point.y, point.x)
		}
	}

	def toPoint(location: Location) = new Point(location.lng, location.lat)

	def toGeometry(location: Location) = {
		val point = toPoint(location)
		point.setSrid(4326)
		new PGgeometry(point)
	}
}

object LocationFieldWrapper extends FieldWrapper[Location] {
	def wrap(field: Location) = ParameterBinder[Location](
		value = field,
		binder = (stmt: java.sql.PreparedStatement, idx: Int) => stmt.setObject(idx, Location.toGeometry(field))
	)
}
