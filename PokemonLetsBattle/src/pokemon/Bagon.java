package pokemon;

import java.util.Arrays;
import java.util.Map;

import entity.Entity;
import moves.Move;
import moves.Moves;
import properties.Ability;
import properties.Type;

public class Bagon extends Pokemon {
	
	public Bagon(int level, Entity ball) {
		super(371, "Bagon", level, ball, 45, 75, 60, 40, 30, 50, 35, 89, 1, Growth.SLOW, 45);
		
		id = Pokedex.BAGON;
		type = Type.DRAGON;
		ability = Ability.ROCKHEAD;
								
		mapMoves();
	}
	
	protected void mapMoves() {
		
		moveset.addAll(Arrays.asList(
				new Move(Moves.RAGE)
		));
		
		moveLevels = Map.ofEntries(
				Map.entry(5, Moves.BITE),
				Map.entry(10, Moves.LEER),
				Map.entry(16, Moves.HEADBUTT),
//				Map.entry(20, Moves.FOCUSENERGY),
				Map.entry(25, Moves.EMBER),
				Map.entry(31, Moves.DRAGONBREATH),
				Map.entry(35, Moves.ZENHEADBUTT),
				Map.entry(40, Moves.SCARYFACE),
				Map.entry(46, Moves.CRUNCH),
				Map.entry(50, Moves.DRAGONCLAW),
				Map.entry(55, Moves.DOUBLEEDGE)
		);
	}
	
	public Pokemon evolve() {
		Pokemon evolvedForm = null;
		
		evolvedForm = new Shelgon(level, ball);				
		evolvedForm.create(this);
		
		return evolvedForm;
	}
}