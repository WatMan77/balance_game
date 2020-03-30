package balance_gameGUI

import javax.swing.JOptionPane
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import java.io.File
import java.awt.image.BufferedImage
import java.awt.{Graphics2D, Color}

class WeightModel(positionX: Int, positionY: Int, emblem: Char) {
  
   def paint(g: Graphics2D) = {
     /*
      * The positions the recatngle and the string have been manually discovered trought trial and error
      * and thus don't have any real algorithm behind them.
      */
     g.drawRect(positionX + 6, positionY - 5, 12, 12)
     g.drawString(emblem.toString, positionX + 9, positionY + 5)
   }
}