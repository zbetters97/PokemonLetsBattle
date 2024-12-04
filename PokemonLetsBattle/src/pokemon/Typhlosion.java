package pokemon;

import java.util.Arrays;
import java.util.Map;

import entity.Entity;
import moves.Move;
import moves.Moves;
import properties.Ability;
import properties.Type;

public class Typhlosion extends Pokemon {
	
	public Typhlosion(int level, Entity ball) {
		super(157, "Typhlosion", level, ball, 78, 84, 78, 109, 85, 100, -1, 209, 3, Growth.MEDIUMSLOW, 45);
		
		id = Pokedex.TYPHLOSION;
		type = Type.FIRE;
		ability = Ability.BLAZE;
								
		mapMoves();
	}
	
	protected void mapMoves() {
		
		moveset.addAll(Arrays.asList(
				new Move(Moves.TACKLE), 
        		new Move(Moves.LEER),
        		new Move(Moves.SMOKESCREEN),
        		new Move(Moves.EMBER)
		));
		
		moveLevels = Map.ofEntries(
				Map.entry(4, Moves.SMOKESCREEN),
				Map.entry(10, Moves.EMBER),
				Map.entry(13, Moves.QUICKATTACK),
				Map.entry(20, Moves.FLAMEWHEEL),
				Map.entry(24, Moves.DEFENSECURL),
				Map.entry(31, Moves.SWIFT),
				Map.entry(35, Moves.LAVAPLUME),
				Map.entry(42, Moves.FLAMETHROWER),
				Map.entry(46, Moves.ROLLOUT),
				Map.entry(53, Moves.DOUBLEEDGE),
				Map.entry(57, Moves.ERUPTION)		
		);
	}
}