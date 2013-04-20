package controllers

import play.api._
import play.api.mvc._
import model._
import util.Theme

object Application extends Controller {
  import views._
  var feld: Feld = new Feld(4, Theme.fruits)
  
  def index = Action {
    Ok(views.html.index(4, Theme.people))
  }
  
   def redo (size: Int, theme : String) = Action {
     var newTheme = Theme.people
      theme match {
      case "fruits" => newTheme = Theme.fruits
      case "countries" => newTheme = Theme.countries
      case "people" => newTheme = Theme.people
      case "fashion" => newTheme = Theme.fashion

    }
    Ok(views.html.index(size, newTheme))
  }

  var statusText = "Spiel angefangen"

  def solve = Action {
    feld.solve; statusText = "Spiel gelöst";
    Ok(statusText)
  }

  def changeTheme(stringTheme: String) = Action {
    
    var newTheme :Theme.Value = Theme.fruits
    stringTheme match {
      case "fruits" => newTheme = Theme.fruits
      case "countries" => newTheme = Theme.countries
      case "people" => newTheme = Theme.people
      case "fashion" => newTheme = Theme.fashion

    }
    if (feld.currentTheme != newTheme) {
      feld.setTheme(newTheme)
      statusText = "Thema der Bilder geändert"

    } else { statusText = "Das aktuelle Thema ist schon das geforderte" }
    Ok("Theme (" + stringTheme +  ") was pressed")
    
  }

  def selectCell(row: Int, col: Int) = Action {
    Ok("Button with coordinates (" + row + "," + col + ") was pressed")
    /*request =>
   Ok("Got request [" + request + "]")
   val chgCells = feld.tryOpen(cellCoords._1, cellCoords._2) 
   
   if(!chgCells.closedCells.isEmpty) 
   if(!chgCells.guessedCells.isEmpty) 
   chgCells.openedCell match {
     case Some(coords) => Action{}
     case None => 
   }
   if(feld.gameOver) { statusText ="Spiel ist beendet"
     }*/
  }

  def reset = Action {
    feld.reset
    statusText = "Zellen werden zurückgesetzt"
    Ok(statusText)

  }

  def resize(newSize: Int) = Action {
    if (newSize != feld.dimension) {
      feld.resize(newSize)
      statusText = "Spielgrösse verändert"

    } else { statusText = "Das Spiel ist schon in der geforderten grösse" }
     Ok("Size (" + newSize +  ") was pressed")
  }

  def pictureNr(c: Coordinates): Int = {
    feld(c._1, c._2).pictureNr
  }
  
  
  
  def fieldSize = feld.dimension

  def currentTheme = feld.currentTheme

}