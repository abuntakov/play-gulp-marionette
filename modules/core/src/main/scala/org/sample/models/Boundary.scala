package org.sample.models

import scalikejdbc._
import java.sql.ResultSet
import org.postgis._

case class Boundary(locations: Location*) {
	def toRing = locations :+ locations.head
}

object Boundary {
	implicit val boundaryTypeBinder: TypeBinder[Boundary] = new TypeBinder[Boundary] {
		override def apply(rs: ResultSet, columnIndex: Int): Boundary = {
			rowToLocation(rs.getObject(columnIndex).asInstanceOf[PGgeometry])
		}

		override def apply(rs: ResultSet, columnLabel: String): Boundary = {
			rowToLocation(rs.getObject(columnLabel).asInstanceOf[PGgeometry])
		}

		private def rowToLocation(geo: PGgeometry) = {
			val points = geo.getGeometry.asInstanceOf[Polygon].getRing(0).getPoints.map { p =>
				Location(p.y, p.x)
			}.dropRight(1)
			Boundary(points:_ *)
		}
	}

	def toGeometry(boundary: Boundary) = {
		val linearRing = new LinearRing( boundary.toRing.map(Location.toPoint).toArray )
		val polygon = new Polygon( Array(linearRing) )
		new PGgeometry(polygon)
	}
}

object BoundaryFieldWrapper extends FieldWrapper[Boundary] {
	def wrap(field: Boundary) = ParameterBinder[Boundary](
		value = field,
		binder = (stmt: java.sql.PreparedStatement, idx: Int) => stmt.setObject(idx, Boundary.toGeometry(field))
	)
}
