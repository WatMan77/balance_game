package balance_game

import scala.collection.mutable.Buffer
import org.junit.Test
import org.junit.Assert._

class BalanceTests {
  
 @Test def TestA = {
    val scale1 = new Scale('a', 3)
    val david = new Player("David", 'D')
    val mark = new Player("Mark", 'M')
    scale1.rightArm(0).objects += new Weight(david)
    
    assertEquals("Scale1 should have a moment of -1", -1, scale1.totalMoment)
    assertEquals('a', scale1.name)
    assertEquals(3, scale1.distance)
  }
 
/* @Test def TestB = {
   val scale1 = new Scale('a', 3)
   val david = new Player("David", 'D')
   val mark = new Player("Mark", 'M')
   scale1.rightArm(0).objects += new Weight(david)
   scale1.rightArm(1).objects += new Weight(mark)
   
   assertEquals(3, scale1.totalWeight) //No tilt has been taken into calculations.
   assertEquals(true, scale1.isImbalanced)
   
   scale1.leftArm(2).objects += new Weight(mark)
   
   assertEquals(6, scale1.totalWeight)
   assertEquals(false, scale1.isImbalanced)
 }*/
 
 @Test def TestGame = {
   val game = new Game( new Player("David", 'D'), new Player("Mark", 'M'))
   
   game.allScales += new Scale('a', 3)
   
   game.addWeight('a', 1, "right")
   
   assertEquals(1, game.allScales.size)
   assertEquals(-1, game.allScales(0).totalMoment)
   
   game.addScale('a', 2, "right", 2, 'x')
   
   assertEquals(2, game.allScales.size)
   assertEquals(-1, game.allScales(0).totalMoment)
   
   game.addWeight('x', 1, "left")
   
   //After adding the weight, a scale becomes imblanaced, so the weight is not put on the scale
   assertEquals(1, game.allWeights.size)
   assertEquals(-1, game.allScales(0).totalMoment)
 }
 
 @Test def TestPoints = {
   val game = new Game(new Player("Jarmo", 'J'), new Player("Irmeli", 'I'))
   
   game.allScales += new Scale('a', 3)
   
   game.addWeight('a', 1, "left")
   game.addWeight('a', 1, "right")
   
   assertEquals(0, game.allScales(0).totalMoment)
   
   game.addScale('a', 2, "right", 3, 'x')
   game.addWeight('x', 2, "right")
   
   assertEquals(-2, game.allScales(0).totalMoment)
 }
}