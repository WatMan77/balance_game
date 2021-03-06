package balance_game


class Weight(var owner: Player) extends Mass {
  //Owner of the weight can change
  private val _weight = 1
  def weight = this._weight
  def changeOwner(player: Player): Unit = {
    owner = player
  }
  
  override def toString = "I am a weight!" // This is used in recursion to identify, whethear we are counting a weight or a scale. Maybe Checking type would be better?
}