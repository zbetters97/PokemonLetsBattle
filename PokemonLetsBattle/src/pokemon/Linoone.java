package pokemon;

import java.util.Arrays;
import java.util.Map;

import entity.Entity;
import moves.Move;
import moves.Moves;
import properties.Ability;
import properties.Type;

public class Linoone extends Pokemon {
	
	public Linoone(int level, Entity ball) {
		super(261, "Linoone", level, ball, 78, 70, 61, 50, 61, 100, -1, 128, 2, Growth.MEDIUMFAST, 264);
		
		id = Pokedex.LINOONE;
		type = Type.NORMAL;
		ability = Ability.QUICKFEET;
		
		mapMoves();
	}
	
	protected void mapMoves() {
		
		moveset.addAll(Arrays.asList(
				new Move(Moves.TACKLE),
        		new Move(Moves.GROWL),
        		new Move(Moves.TAILWHIP)  
		));
		
		moveLevels = Map.ofEntries(
				Map.entry(5, Moves.TAILWHIP), 
				Map.entry(9, Moves.HEADBUTT),
				Map.entry(13, Moves.SANDATTACK),
				Map.entry(41, Moves.SLASH),
				Map.entry(47, Moves.REST)
//				Map.entry(59, Moves.FLING)
		);
	}
}