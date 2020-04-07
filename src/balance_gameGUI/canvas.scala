package balance_gameGUI

import balance_game._
import scala.collection.mutable.Buffer
import scala.swing._
import scala.swing.event._
import javax.swing.JOptionPane
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import java.io.File
import java.awt.image.BufferedImage
import java.awt.{Graphics2D, Color}

class canvas extends Panel {
  
  //From which point do we draw the scale? What is the radius of the new scale?
  //We can try going from the very first scale (meaning we draw the first scale separately)

  
  override def paintComponent(g: Graphics2D) = {
    val scales = BalanceGameGUI.game.allScales
    val scaleModels = Buffer[ScaleModel]()
    val bottomScale = new ScaleModel(1600/2 - 50, 735, scales(0).distance, scales(0).name)
    
    g.setColor(Color.white)
    g.fillRect(0, 10, 1600, 750)
    bottomScale.paint(g)
    var i = -1
    
    //We draw everything using recursion starting from the lowest scale.
    //We empty the Coordinates Buffer because it is easier than checking whether it a scale is already in the list.
    BalanceGameGUI.game.scaleCoordinates = Buffer[(Int, Int, Char, Int)]()
    BalanceGameGUI.game.scaleCoordinates += ((1600/2 - 50, 735, scales(0).name, scales(0).distance))
    for(place <- scales(0).leftArm ++ scales(0).rightArm){
      var itemsFound = 0
      for(mass <- place.objects){
        if(mass.isInstanceOf[Scale] && i != 0){
          drawScale((1600/2 - 50) + i*35, (735) - 91 - 12*itemsFound, mass.asInstanceOf[Scale].distance, mass.asInstanceOf[Scale])
        } else {
          val weight = new WeightModel( (1600/2 - 50) + i*35 , 735 - 72 - 12*itemsFound, mass.asInstanceOf[Weight].owner.emblem)
          itemsFound += 1
          weight.paint(g)
        }  
      }
      //If we are on the left arm, i need to be negative.
      if(i < 0) i -= 1 else i += 1
      if(i.abs > scales(0).distance) i = 1
    }
    //The parameters are the ones we use to draw the scale.
    def drawScale(relativeX: Int, relativeY: Int, scaleDistance: Int, scaleInspect: Scale): Unit = {
      var j = -1
      for(place <- scaleInspect.leftArm ++ scaleInspect.rightArm){
        var itemsFound = 0
        for(mass <- place.objects){
          if(mass.isInstanceOf[Scale] && j != 0){
            //When found a scale on a scale, it just draws it one "step" above so the change in relativeY is linear.
            //println(j + " " + scaleInspect.name)
            drawScale(relativeX + j*35, relativeY - 90 - 12*itemsFound, mass.asInstanceOf[Scale].distance, mass.asInstanceOf[Scale])
          } else {
            val weight = new WeightModel(relativeX + j*35, relativeY - 72 - 12*itemsFound, mass.asInstanceOf[Weight].owner.emblem)
            itemsFound += 1
            weight.paint(g)
          }
        }
        if(j < 0) j -= 1 else j += 1
        if(j.abs > scaleDistance) j = 1
      }
       val model = new ScaleModel(relativeX, relativeY, scaleInspect.distance, scaleInspect.name)
       model.paint(g)
       BalanceGameGUI.game.scaleCoordinates += ((relativeX, relativeY, scaleInspect.name, scaleInspect.distance))
       BalanceGameGUI.game.scaleCoordinates.foreach((new TestCircle(relativeX, relativeY).paint(g))
    }
  }
}