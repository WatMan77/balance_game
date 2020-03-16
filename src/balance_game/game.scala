package balance_game
import scala.collection.mutable.Buffer
import scala.Vector
import scala.util.Random

class Game(player1: Player, player2: Player) {
  
  var allScales = Buffer(new Scale('a', 4))   //It will be easier to find the scale we need when putting on weights. Also a starting scale. Might change later to be random...
  val allWeights = Buffer[Weight]()
  val allPlayers = Vector(player1, player2) //This might be wrong since we need the players from the very beginning.
  var currentPlayer = allPlayers(0) //Player one always starts the game
  var totalPoints = 0
  var turn = 1
  private val randomScale = new Random(568)
  private val sides = Array("left", "right")
  
  def nextPlayer: Unit = {
    currentPlayer = allPlayers((allPlayers.indexOf(currentPlayer) + 1) % 2)
    turn += 1
  }
  
  def playerPoints(startingPoint: Scale, aDistance: Int, isLowest: Boolean) {
    var howFar = aDistance
    
    for(place <- startingPoint.leftArm ++ startingPoint.rightArm) {
      for(item <- place.objects) {
        
        if(item.toString == "I am a weight!"){
          item.asInstanceOf[Weight].owner.points += 1*howFar //1 is useless but helps understand, what is actualy going on.
        } else {
          playerPoints(item.asInstanceOf[Scale], howFar, false) 
        }
      }
      
      if(isLowest) howFar += 1 //When we are in a different Scale, the multiplayer is constant.
      if(howFar > startingPoint.distance && isLowest) howFar = 1
    }
  }
  
 private def addScale(scaleName: Char, distance: Int, side: String, newRadius: Int, name: Char ) = {
    allScales.find(n => n.name == scaleName) match {
      
      case Some(scale) => { //Scale might've been found but do other parameters match with the scale?
        
        if(distance > scale.distance || distance <= 0) {
          "Invalid distance. Try again" 
          
        } else {
          val item =  new Scale(name, newRadius)
          allScales += item
          if(side == "right") {
            scale.rightArm(distance - 1).objects += item
          } else {
            scale.leftArm(distance - 1).objects += item
          }
        }
        allPlayers.foreach(_.points = 0)
        playerPoints(allScales(0), 1, true)
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
         weightsBeneath.foreach(_.owner = currentPlayer)
        }
        
        val whichScale = allScales(randomScale.nextInt.abs.toInt % allScales.size) // We need the scale on which we put another scale first so we know its radius.
        val where = (randomScale.nextInt() % whichScale.distance).abs.toInt + 1 //Which place we put the scale on. 0 is not acceptable       
        
        allPlayers.foreach(_.points = 0)
        playerPoints(allScales(0), 1, true)
        if(turn % allPlayers.size == 0) {
          addScale( whichScale.name, where, sides(randomScale.nextInt % 2), (randomScale.nextInt.abs.toInt % 4) + 1, (98 + turn).toChar)
          for(scale <- allScales) { //This is just for pure testing. Trying to find, where the new scale is put.
            println(scale.name)
            println("left")
            scale.leftArm.foreach(n => println(n.objects))
            println("right")
            scale.rightArm.foreach(n => println(n.objects))
          }
        }
        nextPlayer
      }
      
      case None => "Scale " + scaleName +  " not found."
    }
  }
}