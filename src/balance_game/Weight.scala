package balance_game


class Weight(owner: Player) extends Mass {
  
  val weight = 1
  def totalWeight = this.weight
  
  override def toString = "I am a weight!" // This is used in recursion to identify, whethear we are counting a weight or a scale
}