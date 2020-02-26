package balance_game
import scala.collection.mutable.Buffer
import scala.Vector

class game {
  
  val allScales = Buffer[Scale]()   //It will be easier to find the scale we need when putting on weights
  val allPlayers = Vector[Player]() //This might be wrong since we need the players from the very beginning.
  var currentPlayer = allPlayers(0) //Player one always starts the game
  
  def addWeight(scale: Char, distance: Int) = {
    allScales.find(n => n.name == scale)
  }
}