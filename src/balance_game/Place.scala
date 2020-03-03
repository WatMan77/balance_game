package balance_game
import scala.collection.mutable.Buffer

class Place(place: Int) { //Each place needs it's distance from the center.
  
  var objects = Buffer[Mass]() //Each (Mass) object on this particular 'Place'-object
  val distance = this.place
  
  //Should probably make a second method which simply counts the weights as 1 and not *distance
  //This method now only counts the weight, not the tilt.
  
  def countMassTilt: Int = {
    var total = 0
    for(item <- objects) {
      total += item.weight
    }
    total*distance
  }
  
}