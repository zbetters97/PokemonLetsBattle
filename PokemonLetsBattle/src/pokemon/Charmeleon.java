package pokemon;

import java.util.Arrays;
import java.util.Map;

import entity.Entity;
import moves.Move;
import moves.Moves;
import properties.Ability;
import properties.Type;

public class Charmeleon extends Pokemon {
	
	public Charmeleon(int level, Entity ball) {
		super(5, "Charmeleon", level, ball, 58, 64, 58, 80, 65,  80, 36, 6, 142, 2, Growth.MEDIUMSLOW, 45);
		
		id = Pokedex.CHARMELEON;
		type = Type.FIRE;
		ability = Ability.BLAZE;
		
		mapMoves();
	}
	
	protected void mapMoves() {
		
		moveset.addAll(Arrays.asList(
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
				Map.entry(39, Moves.FLAMETHROWER),
				Map.entry(43, Moves.FIRESPIN)
		);
	}
}