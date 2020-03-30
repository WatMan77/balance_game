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
  
  val firstPlayer  = JOptionPane.showInputDialog("Player 1", "").trim.filter(_ != ' ').split(",")
  val secondPlayer = JOptionPane.showInputDialog("Player 2", "").trim.filter(_ != ' ').split(",")
  
  
  val game = new Game(new Player(firstPlayer(0), firstPlayer(1)(0)), new Player(secondPlayer(0), secondPlayer(1)(0)) )
  
  def top = new MainFrame{
    title = "Balance Game"
    
    
    def updateInfo = { //Helper function to update all the info like player points, weights left (to be implemented)
      var i = 0
      for(component <- gameInfo.contents) {
        component.asInstanceOf[Label].text = game.allPlayers(i).name + " " + game.allPlayers(i).emblem + " " + game.allPlayers(i).points
        i += 1
      }
      for(component <- middleComponent.contents) {
        component.asInstanceOf[Label].text = "      Weights Left: " + game.weightsLeft.toString
      }
    }
    
    def announceWinners = {
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
        
      case scaleChanged: FocusLost          => //We change the distances in when choosing a scale. This way, we don't have to check, wether the player gave incorrect inputs.
        distanceChoices.peer.setModel(ComboBox.newConstantModel(List.tabulate(game.allScales.find(_.name == scaleChoices.item(0)).get.distance)(x => x + 1)))
        
     // case e => println(e) For testing to see the reactions.
    }
    
    
    val choiceBoxes = new BoxPanel(Orientation.Horizontal){
      contents += scaleChoices
      contents += distanceChoices
      contents += sideChoices
      contents += weightButton
      minimumSize = new Dimension(100, 100)
    }
    
    val gameInfo = new BoxPanel(Orientation. Vertical) {
      for(player <- game.allPlayers){
        contents += new Label(player.name + " " + player.emblem + " " +  player.points)
      }
    }
    
    val middleComponent = new BoxPanel(Orientation.Vertical){
      contents += new Label("      Weights Left: " + game.weightsLeft.toString)
    }
    
    val bottomComponents = new BorderPanel {
      layout += choiceBoxes -> BorderPanel.Position.East
      layout += gameInfo -> BorderPanel.Position.West    //Center or West don't make a difference for some reason.
      layout += middleComponent -> BorderPanel.Position.Center
    }
    
    val bottom = new BorderPanel {
      layout += bottomComponents -> BorderPanel.Position.South
      layout += new canvas -> BorderPanel.Position.Center
      //layout += new ScaleModel(1600/2 - 50, 735) -> BorderPanel.Position.Center
      maximumSize = new Dimension(5,5)
    }
      
    contents = bottom
    size = new Dimension(1600, 840)
  }
}