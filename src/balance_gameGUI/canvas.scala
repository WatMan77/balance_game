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
    val bottomScale = new ScaleModel(1600/2 - 50, 735, scales(0).distance)
    
    g.setColor(Color.white)
    g.fillRect(0, 10, 1600, 750)
    bottomScale.paint(g)
    var i = scales(0).distance * (-1)
    
    //We draw everything using recursion starting from the lowest scale.
    for(place <- scales(0).leftArm ++ scales(0).rightArm){
      for(mass <- place.objects){
        if(mass.isInstanceOf[Scale] && i != 0){
          //println("Found scale 1")
          drawScales((1600/2 - 50) + i*35, (735) - 35*i.abs.toInt, mass.asInstanceOf[Scale].distance, mass.asInstanceOf[Scale])
        } else {
         // val weight = new WeightModel( (1600/2 - 50) + i*35, 735 - 50, scales(0).name)
         // weight.paint(g)
        }
        i += 1
      }
    }
    def drawScales(relativeX: Int, relativeY: Int, radius: Int, scaleInspect: Scale): Unit = {
      var i = scaleInspect.distance * (-1)
      for(place <- scaleInspect.leftArm ++ scaleInspect.rightArm){
        for(mass <- place.objects){
          if(mass.isInstanceOf[Scale] && i != 0){
           // println("Found scale 2")
            drawScales(relativeX + i*35, relativeY - 35*i.abs.toInt, place.distance, mass.asInstanceOf[Scale])
          }
          i += 1
        }
      }
       val model = new ScaleModel(relativeX, relativeY, radius)
       model.paint(g)
    }
  }
}