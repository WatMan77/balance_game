package balance_game

import scala.collection.mutable.Buffer

class Scale(emblem: Char, radius: Int) extends Mass {
  
  val name = this.emblem
  val distance = radius
  val leftArm = Buffer.tabulate(radius)(n => new Place(n + 1))
  val rightArm = Buffer.tabulate(radius)(n => new Place(n + 1))
 // var isImbalanced = false
  
  
  def weight: Int = {
    
    var total = 0
    for(place <- leftArm) {
      for(item <- place.objects) {
       total += item.weight
      }
    }
    
    for(place <- rightArm) {
      for(item <- place.objects) {
        total += item.weight
      }
    }
      
    total
  }
  
  def sideTilt(items: Buffer[Place]) = {
    var mass = 0
    for(place <- items) {
      mass += place.countMassTilt
    }
    mass
  }
  
  def leftTilt: Int = sideTilt(leftArm) //Might be wise to save the weights into variables.
  
  def rightTilt: Int = sideTilt(rightArm)
  
  def isImbalanced = if( math.abs(leftTilt - rightTilt) >= radius) true else false
  
  def totalMoment = leftTilt - rightTilt //This is with distance!
  
  override def toString = "I am a scale!"
}