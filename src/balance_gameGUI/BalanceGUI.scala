package balance_gameGUI

import scala.collection.mutable.Buffer
import scala.swing._
import scala.swing.event._
import balance_game._

object BalanceGameGUI extends SimpleSwingApplication {
  
  val game = new Game(new Player("Sami", 'S'), new Player("Tervo", 'T'))
  
  
  def top = new MainFrame{
    title = "Balance Game"
    
    def updateInfo = { //Helper function to update all the info like player points, weights left (to be implemented)
      var i = 0
      for(component <- gameInfo.contents) {
        component.asInstanceOf[Label].text = game.allPlayers(i).name + " " + game.allPlayers(i).points
        i += 1
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
        val whichScale    = scaleChoices.item(0) //Here we take the name of the Scale.
        val whichSide     = sideChoices.item
        val whichDistance = distanceChoices.item
        game.addWeight(whichScale, whichDistance, whichSide)
        scaleChoices.peer.setModel( ComboBox.newConstantModel(game.allScales.map(_.name.toString)) )
        updateInfo
        
        
        
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
        contents += new Label(player.name + " " + player.points)
      }
    }
    
    
    val bottomComponents = new BorderPanel {
      layout += choiceBoxes -> BorderPanel.Position.East
      layout += gameInfo -> BorderPanel.Position.Center    //Center on West don't make a difference for some reason.
    }
    
    val bottom = new BorderPanel {
      layout += bottomComponents -> BorderPanel.Position.South
      maximumSize = new Dimension(5,5)
    }
      
    contents = bottom
    size = new Dimension(1500, 800)
  }
  
}