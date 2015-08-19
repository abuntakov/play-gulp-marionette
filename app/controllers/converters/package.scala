package controllers

import org.sample.models.{Location, Boundary}
import play.api.libs.json.Json

package object converters {
  implicit val locationFormat = Json.format[Location]

  implicit val boundaryFormat = Json.format[Boundary]

}
