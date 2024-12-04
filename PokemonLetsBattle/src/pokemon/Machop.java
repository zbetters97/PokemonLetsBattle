package pokemon;

import java.util.Arrays;
import java.util.Map;

import entity.Entity;
import moves.Move;
import moves.Moves;
import properties.Ability;
import properties.Type;

public class Machop extends Pokemon {
	
	public Machop(int level, Entity ball) {
		super(66, "Machop", level, ball, 70, 80, 50, 35, 35, 35, 28, 75, 1, Growth.MEDIUMSLOW, 180);
		
		id = Pokedex.MACHOP;
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
				Map.entry(31, Moves.SUBMISSION),
				Map.entry(34, Moves.WAKEUPSLAP),
				Map.entry(37, Moves.CROSSCHOP),
				Map.entry(43, Moves.SCARYFACE),
				Map.entry(46, Moves.DYNAMICPUNCH)
		);
	}
	
	public Pokemon evolve() {
		Pokemon evolvedForm = null;
		
		evolvedForm = new Machoke(level, ball);				
		evolvedForm.create(this);
		
		return evolvedForm;
	}
}