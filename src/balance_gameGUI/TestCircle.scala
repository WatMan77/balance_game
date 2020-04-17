package balance_gameGUI

import javax.swing.JOptionPane
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import java.io.File
import java.awt.image.BufferedImage
import java.awt.{Graphics2D, Color}

/*
 * This whole class was made to find out the coordinates of the scales. It has no other purpose
 * and is not really used in the program.
 */
class TestCircle(x: Int, y: Int) {
  
  def paint(g: Graphics2D) = {
    g.setColor(Color.BLUE)
    g.fillRoundRect(x, y, 10, 10, 10, 10)
  }
  
}