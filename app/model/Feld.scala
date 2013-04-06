package model

import model._
import util.Theme



class Feld(var dimension: Int, var currentTheme: Theme.Value) {
  require(List(2, 6, 4, 8).contains(dimension))

  var anzahlZellen = dimension * dimension - 1
  var gameIsOver = false
  var counterGuessed = 0
  val tempOpenCellsSet = scala.collection.mutable.Set[Coordinates]()

  var zellen = { generateCells(dimension) }

  def generateCells(size: Int): Array[Array[Zelle]] = {
    val temp = Array.ofDim[Zelle](size, size)
    for (
      i <- 0 to size - 1;
      j <- 0 to size - 1
    ) temp(i)(j) = new Zelle(i, j)
    temp
  }

  setPictures(dimension)

  def setPictures(size: Int): Unit = {
    var isThisPictureSet = false
    for (
      pairNr <- 1 to (size * size) / 2;
      pairMember <- 1 to 2
    ) {
      while (isThisPictureSet == false) {
        var row = scala.util.Random.nextInt(size)
        var column = scala.util.Random.nextInt(size)
        val Zelle = zellen(row)(column)
        if (Zelle.pictureNr == 0) {
          isThisPictureSet = true
          Zelle.pictureNr = pairNr

        } else {
          isThisPictureSet = false
        }
      }
      isThisPictureSet = false
    }
  }

  def setTheme(newTheme: Theme.Value) = {
    reset
    currentTheme = newTheme
  }

  def apply(coords: Coordinates) = zellen(coords._1)(coords._2)

  def reset: Unit = {
    //TODO: merge this and resize() into one method with default parameter
    tempOpenCellsSet.clear
    counterGuessed = 0
    gameIsOver = false
    for (i <- 0 to dimension - 1) {
      for (j <- 0 to dimension - 1) {
        zellen(i)(j).pictureNr = 0
        zellen(i)(j).guessed = false
        //Zelle.open set in previous line
      }
    }
    setPictures(dimension)
  }

  def resize(newFieldSize: Int) = {
    tempOpenCellsSet.clear
    counterGuessed = 0
    gameIsOver = false
    dimension = newFieldSize
    zellen = generateCells(newFieldSize)
    setPictures(newFieldSize)
    
  }

  def solve: Unit = {
    for (i <- 0 to dimension - 1) {
      for (j <- 0 to dimension - 1) {
        zellen(i)(j).guessed = true
      }
    }
    tempOpenCellsSet.clear
    gameIsOver = true
    counterGuessed = 0
  }

  def isMatch(coords1: (Int, Int), coords2: (Int, Int)): Boolean =
    !this(coords1).guessed && !this(coords2).guessed &&
      (this(coords1).pictureNr == this(coords2).pictureNr)

  /*def openCellsToString(s: scala.collection.mutable.Set[(Int, Int)]) = {
    s.foreach(x => println(x))
  }*/

  def closeOpenCells(): Unit = {
    tempOpenCellsSet.foreach(coords => this(coords).open = false)
    tempOpenCellsSet.clear
  }

  def openCell(c: Coordinates): Unit = {
    tempOpenCellsSet.add(c)
    this(c).open = true
  }

  def tryOpen(row: Int, col: Int): ChangedCells = {
    if (!tempOpenCellsSet.contains((row, col)) && !this(row, col).guessed) {
      tempOpenCellsSet.size match {
        case 2 => {
          val toClose = tempOpenCellsSet.toList
          closeOpenCells()
          openCell((row, col))
          ChangedCells(closedCells = toClose,
            guessedCells = Nil,
            openedCell = Some((row, col)))
        }
        case 1 => {
          val openedCell = tempOpenCellsSet.head
          if (isMatch(openedCell, (row, col))) {
            this(row, col).guessed = true
            this(openedCell).guessed = true
            counterGuessed = counterGuessed + 1
            tempOpenCellsSet.clear
            ChangedCells(closedCells = Nil,
              guessedCells = (openedCell :: List((row, col))),
              openedCell = Some((row, col)))
          } else {
            openCell((row, col))
            ChangedCells(closedCells = Nil,
              guessedCells = Nil,
              openedCell = Some((row, col)))
          }
        }
        case 0 => {
          openCell((row, col))
          ChangedCells(closedCells = Nil,
            guessedCells = Nil,
            openedCell = Some((row, col)))
        }
      }
    } else ChangedCells(closedCells = Nil,
      guessedCells = Nil,
      openedCell = None)
  }

  def gameOver: Boolean = {
    if (counterGuessed == (dimension * dimension) / 2) {
      gameIsOver = true
    }
    gameIsOver
  }

}