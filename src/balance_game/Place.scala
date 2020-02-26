package balance_game
import scala.collection.mutable.Buffer

class Place(distance: Int) { //Each place needs it's distance from the center.
  
  var objects = Buffer[Mass]() //Each (Mass) object on this particular 'Place'-object
  
  def countMass: Int = {
    var weight = 0
    for(item <- objects) {
      if(item.toString == "I am a weight!"){
       weight += item.weight*distance
       
      } else {
       weight += item.totalWeight*distance
      }
    }
    weight
  }
  
  
}