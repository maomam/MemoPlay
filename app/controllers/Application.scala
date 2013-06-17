package controllers

import play.api._
import play.api.mvc._
import util.Theme
import play.api.libs.json._
import model._
import memogame._

object utils {
    implicit val coordsWrites = new Writes[Coordinates] {
      def writes(c: Coordinates): JsValue = {
        Json.obj(
          "row" -> c._1,
          "column" -> c._2)
      }
    }

    implicit val changedCellsWrites = new Writes[ChangedCells] {
      def writes(c: ChangedCells): JsValue = {
        val openedCell = c.openedCell match {
          case Some(coords) => Json.toJson(coords)
          case None => JsNull
        }
        Json.obj(
          "closedCells" -> c.closedCells,
          "guessedCells" -> c.guessedCells,
          "openedCell" -> openedCell)
      }
    }
  }

object Application extends Controller {
import views._
import utils._
import memogame.Memo

  def index = Action {
    Redirect(routes.Application.showField("fruits", 4))
  }

  def showField(stringTheme: String, newSize: Int) = Action {
    var newTheme: Theme.Value = Theme.fruits
    stringTheme match {
      case "fruits" => newTheme = Theme.fruits
      case "countries" => newTheme = Theme.countries
      case "people" => newTheme = Theme.people
      case "fashion" => newTheme = Theme.fashion
      case _ => newTheme = Memo.controller.currentTheme
    }

    if (newTheme != Memo.controller.currentTheme & newSize == Memo.controller.fieldSize) {
      Memo.controller.changeTheme(newTheme)
    }

    if (newSize != Memo.controller.fieldSize & newTheme == Memo.controller.currentTheme) {
      Memo.controller.resize(newSize)

    }
    Ok(views.html.index(newSize, stringTheme, Memo.controller.statusText)(pictureBindingsJSON))
  }

  def selectCell(row: Int, col: Int) = Action {
    val chgCells = Memo.controller.field.tryOpen(row, col)
    val response = Json.obj(
      "changedCells" -> Json.toJson(chgCells),
      "isGameOver" -> Memo.controller.field.gameOver)
    Ok(Json.stringify(response))

  }

  def reset = Action {
    Memo.controller.reset
    Redirect(routes.Application.showField(Memo.controller.currentTheme.toString(), Memo.controller.fieldSize))
  }

  def solve = Action {
    Memo.controller.solve
    val respo = Json.obj(
      "res" -> Memo.controller.field.gameOver,
      "statusText" -> Memo.controller.statusText)
    Ok(Json.stringify(respo))
  }

  //TODO: make this lazy, not to recalculate it every time showField is called
  def pictureBindingsJSON: String = {
    val dimension = Memo.controller.fieldSize
    var folder = Memo.controller.currentTheme.toString()
    var result = Json.toJson(
      for (
        i <- 0 to dimension - 1;
        j <- 0 to dimension - 1
      ) yield Json.obj(
        "id" -> ("c" + i + "-" + j),
        "url" -> ("/assets/images/" + folder + "/" + Memo.controller.pictureNr(i, j) + ".jpg")))
    Json.stringify(result)
  }

}