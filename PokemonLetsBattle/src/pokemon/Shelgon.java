package pokemon;

import java.util.Arrays;
import java.util.Map;

import entity.Entity;
import moves.Move;
import moves.Moves;
import properties.Ability;
import properties.Type;

public class Shelgon extends Pokemon {
	
	public Shelgon(int level, Entity ball) {
		super(372, "Shelgon", level, ball, 65, 95, 100, 60, 50, 50, 50, 144, 2, Growth.SLOW, 45);
		
		id = Pokedex.SHELGON;
		type = Type.DRAGON;
		ability = Ability.ROCKHEAD;
								
		mapMoves();
	}
	
	protected void mapMoves() {
		
		moveset.addAll(Arrays.asList(
				new Move(Moves.RAGE),
        		new Move(Moves.BITE),
        		new Move(Moves.LEER),
        		new Move(Moves.HEADBUTT)
		));
		
		moveLevels = Map.ofEntries(
				Map.entry(5, Moves.BITE),
				Map.entry(10, Moves.LEER),
				Map.entry(16, Moves.HEADBUTT),
//				Map.entry(20, Moves.FOCUSENERGY),
				Map.entry(25, Moves.EMBER),
				Map.entry(30, Moves.PROTECT),
				Map.entry(32, Moves.DRAGONBREATH),
				Map.entry(37, Moves.ZENHEADBUTT),
				Map.entry(43, Moves.SCARYFACE),
				Map.entry(50, Moves.CRUNCH),
				Map.entry(55, Moves.DRAGONCLAW),
				Map.entry(61, Moves.DOUBLEEDGE)
		);
	}
	
	public Pokemon evolve() {
		Pokemon evolvedForm = null;
		
		evolvedForm = new Salamence(level, ball);				
		evolvedForm.create(this);
		
		return evolvedForm;
	}
}