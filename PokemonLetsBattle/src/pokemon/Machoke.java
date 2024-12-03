package pokemon;

import java.util.Arrays;
import java.util.Map;

import entity.Entity;
import moves.Move;
import moves.Moves;
import properties.Ability;
import properties.Type;

public class Machoke extends Pokemon {
	
	public Machoke(int level, Entity ball) {
		super(67, "Machoke", level, ball, 80, 100, 70, 50, 60, 45, 40, 68, 146, 2, Growth.MEDIUMSLOW, 90);
		
		id = Pokedex.MACHOKE;
		type = Type.FIGHTING;
		ability = Ability.GUTS;
		
		mapMoves();
	}
	
	protected void mapMoves() {
		
		moveset.addAll(Arrays.asList(
				new Move(Moves.LOWKICK), 
        		new Move(Moves.LEER)
		));
		
		moveLevels = Map.ofEntries(
				Map.entry(10, Moves.KARATECHOP),
				Map.entry(19, Moves.SEISMICTOSS),
//				Map.entry(22, Moves.REVENGE),
				Map.entry(25, Moves.VITALTHROW),
				Map.entry(32, Moves.SUBMISSION),
				Map.entry(36, Moves.WAKEUPSLAP),
				Map.entry(40, Moves.CROSSCHOP),
				Map.entry(44, Moves.SCARYFACE),
				Map.entry(51, Moves.DYNAMICPUNCH)
		);
	}
}