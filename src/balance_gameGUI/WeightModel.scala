package balance_gameGUI

import javax.swing.JOptionPane
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import java.io.File
import java.awt.image.BufferedImage
import java.awt.{Graphics2D, Color}

class WeightModel(positionX: Int, positionY: Int, emblem: Char) {
  
   def paint(g: Graphics2D) = {
     
     g.drawRect(positionX, positionY - 5, 12, 12)
     g.drawString(emblem.toString, positionX, positionY)
   }
}