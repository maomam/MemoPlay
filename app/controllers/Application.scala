package controllers

import play.api._
import play.api.mvc._
import model._

object Application extends Controller {
  import views._
  def index = Action {
    Ok(views.html.index(4, "fruits"))
  }
  
}