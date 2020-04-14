package balance_game

import scala.collection.mutable.Buffer
import org.junit.Test
import org.junit.Assert._
import scala.util.Random
import balance_gameGUI.BalanceGameGUI

class BalanceTests {
  
  //This test was made using the seed 568. Making the seed any
  @Test def TestSmallBasics = {
    val players = Buffer(new Player("Sami", 'S'), new Player("Tervo", 'T'))
    val game = new Game(players, 10)
    game.addWeight('a', 2, "right") //Sami puts a weight on
    game.addWeight('a', 2, "left") //Tervo puts a weight
    assertEquals(2, game.allPlayers(0).points) //Both players should have 2 points
    assertEquals(2, game.allPlayers(1).points)
    assertEquals(2, game.allScales.size)        //A new Scale should appear. Appears on a, 2, left
    
    game.addWeight('b', 2, "right") //Sami puts a weight on scale b. b should be tilted right while 'a' to the left 
    assertEquals(1, game.allScales(0).totalMoment) //a is tilted by only 1 while b is tilted by -2
    assertEquals(-2, game.allScales(1).totalMoment) 
    
  }
  
  //Testing a few smaller mechanics on max 3 scales.
  @Test def TestBasics = {
    val players = Buffer(new Player("Sami", 'S'), new Player("Tervo", 'T'))
    val game = new Game(players, 10)
    game.addWeight('a', 3, "right") //Sami puts a weight
    game.addWeight('a', 3, "right") // Tervo puts a weight on Sami's weight but causes an imbalance and thus the weight is lost.
    assertEquals(3, game.allPlayers(0).points)
    assertEquals(0, game.allPlayers(1).points)
    
    game.addWeight('b', 1, "left")
    game.addWeight('b', 1, "left") //Tervo again puts a weight on Samis weight and gains it to himself
    assertEquals(3, game.allPlayers(0).points)
    assertEquals(2, game.allPlayers(1).points)
    assertEquals(6, game.weightsLeft)
  }
  
  //Because the scales appear differently in the tests, we preset some Scales and check the points.
  @Test def TestPoints = {
    val players = Buffer(new Player("Sami", 'S'), new Player("Tervo", 'T'))
    val game = new Game(players, 10)
    game.addScale('a', 2, "right", 2, 'b')
    game.addScale('b', 1, "left", 3, 'c')
    game.addScale('c', 3, "right", 1, 'd')
    game.addWeight('d', 1, "right")
    game.addWeight('c', 1, "left")
    assertEquals(2, game.allPlayers(0).points)
    assertEquals(2, game.allPlayers(1).points)
    //Even though the players put their weights on different scales,
    //they both get 2 points because the scales they put the weights on are both on a,2,right
    
    //Now we check whether the TotalMoment of each scale is correct
    assertEquals(-4, game.allScales(0).totalMoment) //a
    assertEquals(2, game.allScales(1).totalMoment)  //b
    assertEquals(-2, game.allScales(2).totalMoment) //c
    assertEquals(-1, game.allScales(3).totalMoment) //d
  }
}