import play.api.libs.json._

package object model {
  type Coordinates = (Int, Int)
  case class ChangedCells(val closedCells: List[Coordinates],
    val guessedCells: List[Coordinates],
    val openedCell: Option[Coordinates])

  implicit val coordsWrites = new Writes[Coordinates] {
    def writes(c: Coordinates): JsValue = {
      Json.obj(
        "row" -> c._1,
        "column" -> c._2
      )
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
        "openedCell" -> openedCell 
      )
    }
  }
}