package controllers

import play.api._
import play.api.mvc._
import model._
import util.Theme
import play.api.libs.json.Json

object Application extends Controller {
  import views._
  var feld: Feld = new Feld(4, Theme.fruits)
   var statusText ="a"
  def index = Action {
    Redirect(routes.Application.showField("fruits",4))
  }
  

 

  def showField(stringTheme: String, newSize: Int) = Action {
    var newTheme :Theme.Value = Theme.fruits
    stringTheme match {
      case "fruits" => newTheme = Theme.fruits
      case "countries" => newTheme = Theme.countries
      case "people" => newTheme = Theme.people
      case "fashion" => newTheme = Theme.fashion
      case _ => newTheme = currentTheme
    }
    
    if(feld.currentTheme == newTheme & newSize == feld.dimension){
       statusText = "Zellen werden zurueckgesetzt"
    }
    if (feld.currentTheme != newTheme & newSize == feld.dimension) {
      feld.setTheme(newTheme)
      statusText = "Thema der Bilder geändert"
    } else { statusText = "Das aktuelle Thema ist schon das geforderte" }
   
    if (newSize != feld.dimension & feld.currentTheme == newTheme) {
      feld.resize(newSize)
      statusText = "Spielgrösse verändert"

    } else { statusText = "Das Spiel ist schon in der geforderten grösse" }
    
    Ok(views.html.index(newSize,stringTheme,statusText)(pictureBindingsJSON))
    
  }

  def selectCell(row: Int, col: Int) = Action {
    val chgCells = feld.tryOpen(row, col) 
    val response = Json.obj(
        "changedCells" -> Json.toJson(chgCells),
        "isGameOver" -> feld.gameOver
    )
    Ok(Json.stringify(response))
   
    
  }

  def reset = Action {
    feld.reset
   Redirect(routes.Application.showField(currentTheme.toString(), fieldSize))
  }
  
  def solve = Action {
    feld.solve
    var statusText = "Spiel geloest"
    val respo = Json.obj(
        "res" -> feld.gameOver,
        "statusText" -> statusText
        
        )
    Ok(Json.stringify(respo))
  }

  //TODO: make this lazy, not to recalculate it every time showField is called
  def pictureBindingsJSON: String = {
    val dimension = fieldSize
    var folder = currentTheme.toString()
    var result = Json.toJson(
        for (i <- 0 to dimension - 1; 
    	       j <- 0 to dimension - 1) yield 
            Json.obj(
                "id" -> ("c" + i + "-" + j),
                "url" -> ("/assets/images/"+ folder + "/"+ pictureNr(i,j) + ".jpg")
            )
    )
    Json.stringify(result)
  }
  
 

  def pictureNr(c: Coordinates): Int = {
    feld(c._1, c._2).pictureNr
  }
  
  
  
  def fieldSize = feld.dimension

  def currentTheme = feld.currentTheme

}