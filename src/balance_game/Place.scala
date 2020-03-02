package balance_game
import scala.collection.mutable.Buffer

class Place(place: Int) { //Each place needs it's distance from the center.
  
  var objects = Buffer[Mass]() //Each (Mass) object on this particular 'Place'-object
  val distance = this.place
  
  //Should probably make a second method which simply counts the weights as 1 and not *distance
  //This method now only counts the weight, not the tilt.
  
  def countMassTilt: Int = {
    var weight = 0
    for(item <- objects) {
      if(item.toString == "I am a weight!"){
       weight += item.weight*distance
       
      } else {
       weight += countWeight(item.asInstanceOf[Scale])*distance
      }
    }
    weight
  }
  
  def countWeight(scale: Scale): Int = {
    var left = 0
    var right = 0
    for(place <- scale.leftArm) {
      for(item <- place.objects) {
        if(item.toString() == "I am a weight!") {
          left += item.weight
        } else {
          left += countWeight(item.asInstanceOf[Scale])
        }
      }
    }
    
    for(place <- scale.rightArm) {
      for(item <- place.objects) {
        if(item.toString() == "I am a weight!") {
          right += item.weight
        } else {
          right += countWeight(item.asInstanceOf[Scale])
        }
      }
    }
    
    
    left + right
  }
  
}