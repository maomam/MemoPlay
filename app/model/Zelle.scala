package model



class Zelle (val row: Int, val col: Int){
  var pictureNr = 0
  var open = false
  private var _guessed = false
    
  def guessed_= (v: Boolean) = {
    _guessed = v
    open = v
  }
  
  def guessed = _guessed
}