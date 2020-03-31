package balance_game
import scala.collection.mutable.Buffer
import scala.Vector
import scala.util.Random

class Game(players: Buffer[Player]) {
  
  var allScales = Buffer(new Scale('a', 5))   //It will be easier to find the scale we need when putting on weights. Also a starting scale. Might change later to be random...
  val allWeights = Buffer[Weight]()
  var weightsLeft = 10
  val allPlayers = players
  var currentPlayer = allPlayers(0) //Player one always starts the game
  var totalPoints = 0
  var turn = 1
  private val randomScale = new Random(30) //568
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
   */
  def checkPlace(scaleCheck: Scale, distanceCheck: Int, checkSide: String): Boolean = {
   val whichOne = if(checkSide == "right") scaleCheck.rightArm else scaleCheck.leftArm
   var result = true
   for(mass <- whichOne(distanceCheck - 1).objects){
     if(mass.isInstanceOf[Scale]) result = false
   }
   var both = (scaleCheck.leftArm ++ scaleCheck.rightArm)
   var i = -1
  /* for(place <- both){
     for(item <- place.objects){
       if(item.isInstanceOf[Scale]){
         if(i < 0 && checkSide == "left"){
           if(item.asInstanceOf[Scale].distance)
         }
         
       }
     }
     if(i < 0) i -= 1 else i += 1
     if(i.abs > scaleCheck.distance) i = 1
   }*/
   result
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
        
        var whichScale = allScales(randomScale.nextInt.abs.toInt % allScales.size) // We need the scale on which we put another scale first so we know its radius.
        var where = (randomScale.nextInt() % whichScale.distance).abs.toInt + 1 //Which place we put the scale on. 0 is not acceptable 
        var counter = 0
        var randomSide = sides(randomScale.nextInt.abs % 2)
        /*
         * Go through the loop until we find a suitable place for a scale. Could be that there is no suitable place
         * and then we just don't put a scale
         */
        while(!this.checkPlace(whichScale, where, randomSide) && (counter < 50001)){
          whichScale = allScales(randomScale.nextInt.abs.toInt % allScales.size)
          where = (randomScale.nextInt() % whichScale.distance).abs.toInt + 1
          randomSide = sides(randomScale.nextInt.abs % 2)
          counter += 1
        }
        
        allPlayers.foreach(_.points = 0)
        playerPoints(allScales(0), 1, true)
        println(counter + "LAKSURIII! " + (turn%allPlayers.size) )
        if(turn % allPlayers.size == 0 && counter <= 50000) {
          addScale( whichScale.name, where, randomSide, (randomScale.nextInt.abs.toInt % 3) + 1, (97 + allScales.size).toChar)
          
          for(scale <- allScales) { //This is just for pure testing. Trying to find, where the new scale is put.
            println(scale.name)
            println("left")
            scale.leftArm.foreach(n => println(n.objects))
            println("right")
            scale.rightArm.foreach(n => println(n.objects))
          }
        }
        println(allScales.size + " ÖÖÖÖ\n")
        nextPlayer
      }
      
      case None => "Scale " + scaleName +  " not found."
    }
  }
}