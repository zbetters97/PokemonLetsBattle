package properties.abilities;


import moves.Move;
import pokemon.Pokemon;
import properties.Type;

public class ThickFat extends Ability {

	public ThickFat() {
		super("Tick Fat", Category.ATTACK, 0.5, "Fire and Ice-type moves\ndeal 50% damage.");
	}
	
	public boolean isValid(Pokemon attacker, Pokemon target, Move move) {
		
		if (move.getType() == Type.FIRE || move.getType() == Type.ICE) {
			return true;				
		}
		else {
			return false;
		}
	}
}