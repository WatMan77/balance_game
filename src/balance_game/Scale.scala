package balance_game

import scala.collection.mutable.Buffer

class Scale(emblem: Char, radius: Int) extends Mass {
  
  val name = this.emblem
  val distance = radius
  val leftArm = Buffer.tabulate(radius)(n => new Place(n + 1))
  val rightArm = Buffer.tabulate(radius)(n => new Place(n + 1))
 // var isImbalanced = false
  
  val weight = 0 // The weight of a scale is 0 and thus doesn't tilt another scale if it is empty.
  
  def sideWeight(items: Buffer[Place]) = {
    var mass = 0
    for(place <- items) {
      mass += place.countMass
    }
    mass
  }
  
  def leftWeight: Int = sideWeight(leftArm) //Might be wise to save the weights into variables.
  
  def rightWeight: Int = sideWeight(rightArm)
  
  def isImbalanced = if( math.abs(leftWeight - rightWeight) >= radius) true else false
  
  def totalWeight = leftWeight + rightWeight
  
  override def toString = "I am a scale!"
}