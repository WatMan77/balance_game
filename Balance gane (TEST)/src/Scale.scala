import scala.collection.mutable.Buffer

class Scale(name: Char, radius: Int) extends Mass {
  
  
  var leftArm = Buffer[Place]()
  var rightArm = Buffer[Place]()
  
  val weight = 0 // The weight of a scale is 0 and thus doesn't tilt another scale if it is empty.
  def totalWeight = {
   
  }
  
  override def toString = "I am a scale!"
}