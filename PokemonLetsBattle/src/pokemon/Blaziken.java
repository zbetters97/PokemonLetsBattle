package pokemon;

import java.util.Arrays;
import java.util.Map;

import entity.Entity;
import moves.Move;
import moves.Moves;
import properties.Ability;
import properties.Type;

public class Blaziken extends Pokemon {
	
	public Blaziken(int level, Entity ball) {
		super(257, "Blaziken", level, ball, 80, 120, 70, 110, 70, 80, -1, -1, 209, 3, Growth.MEDIUMSLOW, 45);
		
		id = Pokedex.BLAZIKEN;
		types = Arrays.asList(Type.FIRE, Type.FIGHTING);
		ability = Ability.BLAZE;
								
		mapMoves();
	}
	
	protected void mapMoves() {
		
		moveset.addAll(Arrays.asList(
				new Move(Moves.FIREPUNCH), 
        		new Move(Moves.EMBER), 
        		new Move(Moves.SCRATCH),
        		new Move(Moves.GROWL)
		));
		
		moveLevels = Map.ofEntries(
				Map.entry(13, Moves.EMBER),
				Map.entry(16, Moves.DOUBLEKICK),
				Map.entry(17, Moves.PECK),
				Map.entry(21, Moves.SANDATTACK),
				Map.entry(28, Moves.BULKUP),
				Map.entry(32, Moves.QUICKATTACK),
				Map.entry(36, Moves.BLAZEKICK),
				Map.entry(42, Moves.SLASH),
				Map.entry(49, Moves.BRAVEBIRD),
				Map.entry(59, Moves.SKYUPPERCUT),
				Map.entry(66, Moves.FLAREBLITZ)
		);
	}
}