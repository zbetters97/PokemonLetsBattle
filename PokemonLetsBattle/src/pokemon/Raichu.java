package pokemon;

import java.util.Arrays;
import java.util.Map;

import entity.Entity;
import moves.Move;
import moves.Moves;
import properties.Ability;
import properties.Type;

public class Raichu extends Pokemon {
	
	public Raichu(int level, Entity ball) {
		super(26, "Raichu", level, ball, 60, 90, 55, 90, 80, 110, -1, 122, 3, Growth.MEDIUMFAST, 75);
		
		id = Pokedex.RAICHU;
		type = Type.ELECTRIC;
		ability = Ability.STATIC;
								
		mapMoves();
	}
	
	protected void mapMoves() {
		
		moveset.addAll(Arrays.asList(
				new Move(Moves.THUNDERBOLT), 
        		new Move(Moves.THUNDERSHOCK), 
        		new Move(Moves.QUICKATTACK), 
        		new Move(Moves.TAILWHIP)
		));
		
		moveLevels = Map.ofEntries(
				
		);
	}
}