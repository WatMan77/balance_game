package balance_gameGUI

import scala.collection.mutable.Buffer
import scala.swing._
import scala.swing.event._
import javax.swing.JOptionPane
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import java.io.File
import java.awt.image.BufferedImage
import java.awt.{Graphics2D, Color}

class ScaleModel(startingx: Int, startingy: Int, distance: Int, scaleName: Char){
  
   def paint(g: Graphics2D) = {
    //g.setColor(Color.white)
    //g.fillRect(0, 10, 1600, 750)
    
    g.setColor(Color.black)
    for(time <- 0 to 2) {
      g.setColor(Color.black)
     if(time != 2){
       g.fillRect(startingx, startingy - (32 * time), 25, 25)
     } else {
       g.drawRect(startingx, startingy - (32 * time), 25, 25)
     }
      for(time1 <- 0 to 2) {    //Maybe there should be only one line in the middle?
        g.setColor(Color.black)
        //Draw some lines to make the scale look prettier.
      g.drawLine(startingx + 25/2 * time1, startingy, startingx + 25/2 * time1, startingy - 39)
      }
      
      for(place <- (-1)*distance to distance) {
        if(place != 0) {
          g.drawString(place.abs.toString, startingx + 35*place + 9, startingy - 47 )
          g.setColor(Color.black)
          g.drawRect(startingx + 35*place, startingy - 32*2, 25, 25)
        } else {
          g.drawString(scaleName.toUpper.toString, startingx + 10, startingy - 47)
        }
      }
    }
    
  }
  
}