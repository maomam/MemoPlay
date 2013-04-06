package object model
{
  type Coordinates = (Int,Int)
  case class ChangedCells (val closedCells: List[Coordinates], 
      val guessedCells: List[Coordinates], 
      val openedCell: Option[Coordinates])
}