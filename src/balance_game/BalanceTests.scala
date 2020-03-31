package balance_game

import scala.collection.mutable.Buffer
import org.junit.Test
import org.junit.Assert._
import scala.util.Random

class BalanceTests {
  
  def tellNumber(which: Int): Int = {
    val a = new Random(568)
    for(time <- 1 until which) {
      a.nextInt
    }
    a.nextInt
  }
 /* 
 @Test def TestA = {
    val scale1 = new Scale('a', 3)
    val david = new Player("David", 'D')
    val mark = new Player("Mark", 'M')
    scale1.rightArm(0).objects += new Weight(david)
    
    assertEquals("Scale1 should have a moment of -1", -1, scale1.totalMoment)
    assertEquals('a', scale1.name)
    assertEquals(3, scale1.distance)
  }
 
 @Test def TestB = {
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
 /*
 @Test def TestGame = {
   val game = new Game( new Player("David", 'D'), new Player("Mark", 'M'))
   
   game.allScales += new Scale('a', 3)
   
   game.addWeight('a', 1, "right")
   
   assertEquals(1, game.allScales.size)
   assertEquals(-1, game.allScales(0).totalMoment)
   
   
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
   
   game.addWeight('a', 1, "left") //Jarmo puts a weight
   game.addWeight('a', 1, "right") // Irmeli puts a weight
   
   assertEquals(0, game.allScales(0).totalMoment)
   
   game.addScale('a', 2, "right", 3, 'x') //Jarmo puts a scale
   game.addWeight('x', 2, "right")        //Irmeli puts a weight
   
   assertEquals(2, game.allPlayers.find(n => n.name == "Irmeli").get.points)
   assertEquals(-2, game.allScales(0).totalMoment)
   
   game.addWeight('x', 1, "right") //Jarmo puts a weight which causes an imbalance, so the weight is not put. ie nothing should change
   
   assertEquals(-2, game.allScales(0).totalMoment)
   assertEquals(2, game.allPlayers.find(n => n.name == "Irmeli").get.points)
   assertEquals(1, game.allPlayers.find(n => n.name == "Jarmo").get.points)
   
   game.addWeight('a', 1, "left") //Irmeli puts a weight on Jarmos weight.
   
   assertEquals(0, game.allPlayers.find(n => n.name == "Jarmo").get.points)
   assertEquals(4, game.allPlayers.find(n => n.name == "Irmeli").get.points)
 }
 
 @Test def ComplicatedGame = {
   val game = new Game(new Player("Tervo", 'T'), new Player("Sami", 'S'))
   
   game.allScales += new Scale('a', 4)
   
   game.addScale('a', 3, "left", 3, 'b') // Tervo puts a scale
   game.addScale('b', 2, "right", 3, 'c') //Sami puts a scale
   game.addScale('a', 4, "right", 2, 'd') //Tervo puts a scale
   
   game.addWeight('c', 2, "right") //Sami puts a weight
   game.addWeight('d', 1, "left") //Tervo puts a weight
   game.addWeight('a', 1,"left") //Sami puts a weight
   game.addWeight('b', 2, "left") //Tervo puts a weight
   
  assertEquals(4,game.allScales.find(n => n.name == 'a').get.weight)  //Weight of the bottom scale.
  assertEquals(2, game.allScales.find(n => n.name == 'b').get.weight)
  assertEquals(1, game.allScales.find(_.name == 'c').get.weight)
  assertEquals(1, game.allScales.find(_.name == 'd').get.weight)
  assertEquals(3, game.allScales.find( _.name == 'a').get.totalMoment)
  assertEquals(0, game.allScales.find(_.name == 'b').get.totalMoment)
  
  assertEquals(2,game.allPlayers.find(_.name == "Sami").get.points)
  assertEquals(2,game.allPlayers.find(_.name == "Tervo").get.points)
  
  game.addWeight('a', 1, "right") //Sami adds a weight.  Moment of 'a' 7-5
  game.addScale('c', 2, "right", 3, 'e') //Scale is put on Sami's weight so the weight should now be Tervo's
  assertEquals(3, game.allPlayers.find(_.name == "Tervo").get.points)
  assertEquals(2, game.allPlayers.find(_.name == "Sami").get.points)
  
   
   
 }
  */
  
  @Test def TestNewScale = {
    val players = Buffer(new Player("Tervo", 'T'), new Player("Sami", 'S'))
   val game = new Game(players)  
   //game.allScales += new Scale('a', 4) //A scale at the beginning of the game.
   
   game.addWeight('a', 2, "right") //Tervo puts a weight
   assertEquals(1,game.allScales.size)
   game.addWeight('a', 2, "left") //Sami puts a weight. A new Scale should appear.
   assertEquals(2, game.allScales.size)
   assertEquals(2, game.allPlayers(0).points)
   assertEquals(2, game.allPlayers(1).points)
   
   game.addWeight('d', 3, "right")   //Tervo puts a weight
   assertEquals(4, game.allPlayers(0).points)
   
   game.addWeight('d', 2, "left") //Sami puts a weight
   assertEquals(4, game.allPlayers(0).points) //Tervo's points
   game.addWeight('d', 3, "right") //Tervo puts a weight and the scale becomes unbalanced. Nothing should change
   assertEquals(4, game.allPlayers(0).points)
   assertEquals(4, game.allPlayers(1).points)
   game.addWeight('a', 2, "right") //Sami puts his weight on Tervo's weight and thus making them both Sami's. But he also puts it "under" a scale....
   
   assertEquals(2, game.allPlayers(0).points)
   assertEquals(8, game.allPlayers(1).points)
  }
  
}