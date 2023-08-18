package controllers

import javax.inject._
import play.api.mvc._
import play.api.libs.json._
import models.Person

class PeopleController @Inject()(
  cc: ControllerComponents,
) extends AbstractController(cc) {
  implicit val personFormat = Json.format[Person]

  def getAll = Action {
    val persons = Array(
      Person(1, "Capitu", "pitu", "01-01-1995", List("Ruby", "Scala", "Kotlin")),
      Person(2, "Iracema", "cema", "01-01-1992", List("Ruby")),
      Person(3, "Bilico", "bili", "01-01-2003", List("Scala", "Kotlin"))
    ) 
    Ok(Json.toJson(persons))
  }
}
