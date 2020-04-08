package balance_game
import scala.collection.mutable.Buffer
import scala.Vector
import scala.util.Random

class Game(players: Buffer[Player], weights: Int) {
  
  var allScales = Buffer(new Scale('a', 5))   //It will be easier to find the scale we need when putting on weights. Also a starting scale. Might change later to be random...
  var scaleCoordinates = Buffer[(Int, Int, Char, Int)]()
  val allWeights = Buffer[Weight]()
  var weightsLeft = weights
  val allPlayers = players
  var currentPlayer = allPlayers(0) //Player one always starts the game
  var totalPoints = 0
  var turn = 1
  private val randomScale = new Random(568) //568
  private val sides = Array("left", "right")
  
  def nextPlayer: Unit = {
    currentPlayer = allPlayers((allPlayers.indexOf(currentPlayer) + 1) % allPlayers.size)
    turn += 1
  }
  
  /*
   * This method is used to check, wether the place we are trying to put a scale on
   * doesn't already have a scale. It messes up the GUI.
   * We can also use it to see, wether it is a good idea to put a scale on it. For example,
   * putting two scales on two places next to each messes up the GUI as well, because scales will overlap each other.
   * Unfortunately it also limits where a scale can be put.
   * A scale that has y-coordinate of 104 or lower (calculating from it's bottom rectangle) cannot be <= 104
   * if we want to put a scale on it.
   */
  def checkPlace(scaleCheck: Scale, distanceCheck: Int, checkSide: String, newRadius: Int): Boolean = {
    //We need the coordinates of the scale we want to put the new scale on.
   val inspectiveScale = scaleCoordinates.find(_._3 == scaleCheck.name).get
     if(inspectiveScale._2 <= 104){
       println("false Y")
       return false
     }
    //We have to find the scales that are around on the same level as the one we want to put.
    //From there we check if two scales would intertwine 
   val sameLevelScales = scaleCoordinates.filter(scale => scale._2 <= inspectiveScale._2 - 70 && scale._2 >= inspectiveScale._2 - 90 - 3*12 )
   val i = if(checkSide == "right") 1 else -1
   if(!sameLevelScales.isEmpty){
     for(scale <- sameLevelScales) {
       //If the sum of the sides (one from each scale) is greater than the distance between the points,
       //the scale cannot be put.
       if((scale._4*35 + newRadius*35).abs >= (scale._1 - (inspectiveScale._1 + i*distanceCheck*35)).abs){
         println("false distances")
         return false
       }
     }
   }
   true 
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
      if(howFar > startingPoint.distance && isLowest) howFar = 1 //Now when we "change" the arm, howFar becomes 1 again.
    }
  }
  
  def addScale(scaleName: Char, distance: Int, side: String, newRadius: Int, name: Char ) = {
    allScales.find(n => n.name == scaleName) match {
      
      case Some(scale) => { //Scale might've been found but do other parameters match with the scale?
        //println("LISÄTTIIN")
        if(distance > scale.distance || distance <= 0) {
          println("INVALID")
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
      //This should never happen since we give the players a list of scales.
      //Should a bug happen, the game at least won't crash.
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
          weightsLeft -= 1
          
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
        
       
        
        allPlayers.foreach(_.points = 0)
        playerPoints(allScales(0), 1, true)
        if(turn % allPlayers.size == 0) {
          
        var whichScale = allScales(randomScale.nextInt.abs.toInt % allScales.size) // We need the scale on which we put another scale first so we know its radius.
        var where = (randomScale.nextInt() % whichScale.distance).abs.toInt + 1 //Which place we put the scale on. 0 is not acceptable 
        var counter = 0
        var randomSide = sides(randomScale.nextInt.abs % 2)
        var newRadius = (randomScale.nextInt.abs.toInt % 3) + 1
        /*
         * Go through the loop until we find a suitable place for a scale. Could be that there is no suitable place
         * and then we just don't put a scale
         */
        while(!this.checkPlace(whichScale, where, randomSide, newRadius) && (counter < 50001)){
          whichScale = allScales(randomScale.nextInt.abs.toInt % allScales.size)
          where = (randomScale.nextInt() % whichScale.distance).abs.toInt + 1
          randomSide = sides(randomScale.nextInt.abs % 2)
          newRadius = (randomScale.nextInt.abs.toInt % 3) + 1
          counter += 1
        }
         if(counter < 50000) addScale( whichScale.name, where, randomSide, newRadius, (97 + allScales.size).toChar)
          
         /* for(scale <- allScales) { //This is just for pure testing. Trying to find, where the new scale is put.
            println(scale.name)
            println("left")
            scale.leftArm.foreach(n => println(n.objects))
            println("right")
            scale.rightArm.foreach(n => println(n.objects))
          }*/

        }
        println(allScales.size + " size\n")
        nextPlayer
      }
      
      case None => "Scale " + scaleName +  " not found."
    }
  }
}