package balance_gameGUI

import scala.collection.mutable.Buffer
import scala.swing._
import scala.swing.event._
import balance_game._
import javax.swing.JOptionPane
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import java.io.File
import java.awt.image.BufferedImage
import java.awt.{Graphics2D, Color}


object BalanceGameGUI extends SimpleSwingApplication {
  
  
  val players = Buffer[Player]()
  val playerAmount = JOptionPane.showInputDialog("How many players?", "").trim.toInt
  for(times <-1 to playerAmount){
    //Let's make sure the player enters the name in a correct format.
    var correctFormat = false
      while(!correctFormat){
      val player = JOptionPane.showInputDialog("Player " + times, "").trim.filter(_ != ' ').split(",")
        if(player.size == 2){
          players += new Player(player(0), player(1)(0))
          correctFormat = true
        }
    }
  }
  val weights = JOptionPane.showInputDialog("How many Weights?", "").trim.toInt
  
  val game = new Game(players, weights)
  
  def top = new MainFrame{
    title = "Balance Game"
    
    
   private def updateInfo = { //Helper function to update all the info like player points, weights left (to be implemented)
      var i = 0
      for(component <- gameInfo.contents) {
        component.asInstanceOf[Label].text = game.allPlayers(i).name + " " + game.allPlayers(i).emblem + " " + game.allPlayers(i).points + " "
        i += 1
      }
        middleComponent.contents(0).asInstanceOf[Label].text = "      Weights Left: " + game.weightsLeft.toString
        middleComponent.contents(1).asInstanceOf[Label].text = "    Current Player: " + game.currentPlayer.name
      
    }
    
   private def announceWinners = {
      //Sort the points and reverse it so the person with most points is first.
      val sortedPoints = game.allPlayers.sortBy(_.points).reverse
      val winners = sortedPoints.takeWhile(_.points == sortedPoints(0).points)
      if(winners.size == 1){
        Dialog.showMessage(top, "The winner is " + winners(0).name + " with " + winners(0).points + " points!", "Game over!")
      } else {
        var message = ""
        for(person <- winners){
          message += person.name + " with " + person.points + " points, "
        }
        //The dropping is done to get rid of the ',' and replace it with a dot '.'.
        Dialog.showMessage(top, "We have a tie! The winners are: " + message.dropRight(2) + '.', "Game Over!")
      }
    }
    
    //Buttons, ComboBoxes etc...
    val weightButton    = new Button("Add")
    var sideChoices     = new ComboBox(List("left", "right")){maximumSize = new Dimension(50, 25)}
    var scaleChoices    = new ComboBox(game.allScales.map(_.name.toString)){maximumSize = new Dimension(50, 25)} //The names for some reason cannot be in Char
    var distanceChoices = new ComboBox( List.tabulate(game.allScales.find(_.name == scaleChoices.item(0)).get.distance)(x => x + 1) ){maximumSize = new Dimension(50, 25)}
    
    listenTo(weightButton)
    listenTo(scaleChoices)
    this.reactions += {
      case button: ButtonClicked => 
        if(game.weightsLeft <= 0){
          announceWinners
        } else {
        val whichScale    = scaleChoices.item(0) //Here we take the name of the Scale.
        val whichSide     = sideChoices.item
        val whichDistance = distanceChoices.item //The player cannot put a weight "under" a scale cause that doesn't make sense.
        val scaleChecking = if(whichSide == "right"){
          game.allScales.find(_.name == whichScale).get.rightArm
        } else {
          game.allScales.find(_.name == whichScale).get.leftArm
        }
        
        scaleChecking(whichDistance - 1).objects.find(_.toString == "I am a scale!") match{
          case Some(scale) => 
            Dialog.showMessage(top, "You cannot put a weight under a scale", "Error")
        
          case None => game.addWeight(whichScale, whichDistance, whichSide)
            scaleChoices.peer.setModel( ComboBox.newConstantModel(game.allScales.map(_.name.toString)) )
            updateInfo      
            repaint()
            if(game.weightsLeft <= 0) announceWinners
        }
       }
        
      case scaleChanged: FocusLost   => //We change the distances in the combobox when choosing a scale. This way, we don't have to check, wether the player gave incorrect inputs.
        distanceChoices.peer.setModel(ComboBox.newConstantModel(List.tabulate(game.allScales.find(_.name == scaleChoices.item(0)).get.distance)(x => x + 1)))
        
    }
    
    
    val choiceBoxes = new BoxPanel(Orientation.Horizontal){
      contents += scaleChoices
      contents += distanceChoices
      contents += sideChoices
      contents += weightButton
      minimumSize = new Dimension(100, 100)
    }
    
    val gameInfo = new BoxPanel(Orientation. Horizontal) {
      for(player <- game.allPlayers){
        contents += new Label(player.name + " " + player.emblem + " " +  player.points + " ")
      }
    }
    
    //For some reason putting a component in the Center positions the labels next to the west component.
    //That's why there are spaces to separate 
   private val middleComponent = new BoxPanel(Orientation.Horizontal){
      contents += new Label("      Weights Left: " + game.weightsLeft.toString)
      contents += new Label("    Current Player: " + game.currentPlayer.name)
    }
    
   private val bottomComponents = new BorderPanel {
      add(choiceBoxes, BorderPanel.Position.East)
      add(gameInfo, BorderPanel.Position.West)    //Center or West don't make a difference for some reason.
      add(middleComponent, BorderPanel.Position.Center)
    }
    
   private val bottom = new BorderPanel {
      layout += bottomComponents -> BorderPanel.Position.South
      layout += new Canvas -> BorderPanel.Position.Center
      //layout += new ScaleModel(1600/2 - 50, 735) -> BorderPanel.Position.Center
      maximumSize = new Dimension(5,5)
    }
    //println(game.scaleCoordinates) 
    contents = bottom
    size = new Dimension(1600, 840)
  }
}