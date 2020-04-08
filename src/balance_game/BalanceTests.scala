package balance_game

import scala.collection.mutable.Buffer
import org.junit.Test
import org.junit.Assert._
import scala.util.Random
import balance_gameGUI.BalanceGameGUI

class BalanceTests {
  
  //This test was made using the seed 568. Making the seed any
  @Test def TestNewScale = {
   val players = Buffer(new Player("Tervo", 'T'), new Player("Sami", 'S'))
   var newGame = BalanceGameGUI
   println("HEII")
   //players.foreach(x => newGame.allPlayers += x)
   newGame.game.addWeight('a', 3, "left") //Tervo puts a weight
   newGame.game.addWeight('a', 4, "right")
   assertEquals(newGame.game.allPlayers(0).points, 3)
   assertEquals(newGame.game.allPlayers(0).points, 4)
  }
  
}