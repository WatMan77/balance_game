package balance_game
import scala.collection.mutable.Buffer

class Place(place: Int) { //Each place needs it's distance from the center.
  
  var objects = Buffer[Mass]() //Each (Mass) object on this particular 'Place'-object
  val distance = this.place
  
  
  def countMassTilt: Int = {
    var total = 0
    for(item <- objects) {
      total += item.weight
    }
    total*distance
  }
  
}