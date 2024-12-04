package pokemon;

import java.util.Arrays;
import java.util.Map;

import entity.Entity;
import moves.Move;
import moves.Moves;
import properties.Ability;
import properties.Type;

public class Loudred extends Pokemon {
	
	public Loudred(int level, Entity ball) {
		super(294, "Loudred", level, ball, 84, 71, 43, 71, 43, 48, 40, 126, 2, Growth.MEDIUMSLOW, 120);
		
		id = Pokedex.LOUDRED;
		type = Type.NORMAL;
		ability = Ability.SOUNDPROOF;
								
		mapMoves();
	}
	
	protected void mapMoves() {
		
		moveset.addAll(Arrays.asList(
				new Move(Moves.POUND),
        		new Move(Moves.ASTONISH),
        		new Move(Moves.HOWL)        
		));
		
		moveLevels = Map.ofEntries(
				Map.entry(11, Moves.ASTONISH),
				Map.entry(15, Moves.HOWL),
				Map.entry(20, Moves.BITE),
				Map.entry(23, Moves.SUPERSONIC),
				Map.entry(29, Moves.STOMP),
				Map.entry(37, Moves.SCREECH),
				Map.entry(51, Moves.REST),
				Map.entry(52, Moves.SLEEPTALK),
				Map.entry(57, Moves.HYPERVOICE)
		);
	}
	
	public Pokemon evolve() {
		Pokemon evolvedForm = null;
		
		evolvedForm = new Exploud(level, ball);				
		evolvedForm.create(this);
		
		return evolvedForm;
	}
}