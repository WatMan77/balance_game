package balance_gameGUI

import javax.swing.JOptionPane
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import java.io.File
import java.awt.image.BufferedImage
import java.awt.{Graphics2D, Color}

class TestCircle(x: Int, y: Int) {
  
  def paint(g: Graphics2D) = {
    g.fillRoundRect(x, y, 10, 10, 10, 10)
  }
  
}