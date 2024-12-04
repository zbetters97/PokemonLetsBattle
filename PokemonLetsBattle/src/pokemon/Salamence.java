package pokemon;

import java.util.Arrays;
import java.util.Map;

import entity.Entity;
import moves.Move;
import moves.Moves;
import properties.Ability;
import properties.Type;

public class Salamence extends Pokemon {
	
	public Salamence(int level, Entity ball) {
		super(373, "Salamence", level, ball, 95, 135, 80, 110, 80, 100, -1, 218, 3, Growth.SLOW, 45);
		
		id = Pokedex.SALAMENCE;
		types = Arrays.asList(Type.DRAGON, Type.FLYING);
		ability = Ability.INTIMIDATE;
								
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
				Map.entry(50, Moves.FLY),
				Map.entry(53, Moves.CRUNCH),
				Map.entry(61, Moves.DRAGONCLAW),
				Map.entry(70, Moves.DOUBLEEDGE)
		);
	}
}