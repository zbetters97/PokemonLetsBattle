package pokemon;

import java.util.Arrays;
import java.util.Map;

import entity.Entity;
import moves.Move;
import moves.Moves;
import properties.Ability;
import properties.Type;

public class Charizard extends Pokemon {
	
	public Charizard(int level, Entity ball) {
		super(6, "Charizard", level, ball, 78, 84, 78, 109, 85, 100, -1, 209, 3, Growth.MEDIUMSLOW, 45);
				
		id = Pokedex.CHARIZARD;
		types = Arrays.asList(Type.FIRE, Type.FLYING);
		ability = Ability.BLAZE;
								
		mapMoves();
	}
	
	protected void mapMoves() {
		
		moveset.addAll(Arrays.asList(
				new Move(Moves.DRAGONCLAW), 
        		new Move(Moves.EMBER), 
        		new Move(Moves.SCRATCH),
        		new Move(Moves.GROWL)
		));
		
		moveLevels = Map.ofEntries(
				Map.entry(7, Moves.EMBER),
				Map.entry(10, Moves.SMOKESCREEN),
				Map.entry(17, Moves.DRAGONRAGE),
				Map.entry(21, Moves.SCARYFACE),
				Map.entry(28, Moves.FIREFANG),
				Map.entry(32, Moves.SLASH),
				Map.entry(36, Moves.WINGATTACK),
				Map.entry(42, Moves.FLAMETHROWER),
				Map.entry(49, Moves.FIRESPIN),
				Map.entry(59, Moves.HEATWAVE),
				Map.entry(66, Moves.FLAREBLITZ)
		);
	}
}