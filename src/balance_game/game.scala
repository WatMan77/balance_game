package balance_game
import scala.collection.mutable.Buffer
import scala.Vector
import scala.util.Random

class Game(player1: Player, player2: Player) {
  
  var allScales = Buffer[Scale]()   //It will be easier to find the scale we need when putting on weights
  val allWeights = Buffer[Weight]()
  val allPlayers = Vector(player1, player2) //This might be wrong since we need the players from the very beginning.
  var currentPlayer = allPlayers(0) //Player one always starts the game
  var totalPoints = 0
  
  def nextPlayer = {
    currentPlayer = allPlayers((allPlayers.indexOf(currentPlayer) + 1) % 2)
  }
  
  def addScale(scaleName: Char, distance: Int, side: String, radius: Int, name: Char ) = {
    allScales.find(n => n.name == scaleName) match {
      
      case Some(scale) => { //Scale might've been found but do other parameters match with the scale?
        
        if(distance > scale.distance || distance <= 0) {
          "Invalid distance. Try again" 
          
        } else {
          val item =  new Scale(name, radius)
          allScales += item
          if(side == "right") {
            scale.rightArm(distance - 1).objects += item
          } else {
            scale.leftArm(distance - 1).objects += item
          }
        }    
        //The turn should advance i.e the other player 
        nextPlayer
      }
      
      case None => "Scale '" + scaleName + "' not found. Try again."
    }
  }
  
  def addWeight(scaleName: Char, distance: Int, side: String) {
    var foundImbalance = false
    allScales.find(n => n.name == scaleName) match {
      case Some(scale) => {
        
        val thePlace = {
          if(side == "right") {
            scale.rightArm(distance - 1).objects
          } else {
            scale.leftArm(distance - 1).objects
          }
        }
        
        if(distance > scale.distance || distance <= 0) {
          "Invalid distance. Try again" 
          
        } else {
          val item = new Weight(currentPlayer)
          allWeights += item
          thePlace += item
          
          if(allScales.forall(!_.isImbalanced) == false) {
            foundImbalance = true
            thePlace -= item
            allWeights -= item
          }
        }
        
        //Because a weight is always put on top, it quite easy to change all the weights below it to the current player.
        if(foundImbalance == false){
         val weightsBeneath = thePlace.filter(_.isInstanceOf[Weight]).map(_.asInstanceOf[Weight])
         if(!weightsBeneath.forall(_.owner == currentPlayer)) currentPlayer.points += weightsBeneath.size - 1
        }
        nextPlayer
      }
      
      case None => "Scale " + scaleName +  " not found."
    }
  }
}