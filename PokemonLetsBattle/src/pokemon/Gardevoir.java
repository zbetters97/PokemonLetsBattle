package pokemon;

import java.util.Arrays;
import java.util.Map;

import entity.Entity;
import moves.Move;
import moves.Moves;
import properties.Ability;
import properties.Type;

public class Gardevoir extends Pokemon {
	
	public Gardevoir(int level, Entity ball) {
		super(282, "Gardevoir", level, ball, 68, 65, 65, 125, 115, 80, 208, -1, 3, Growth.SLOW, 45);
		
		id = Pokedex.GARDEVOIR;
		type = Type.PSYCHIC;
		ability = Ability.SYNCHRONIZE;
		
		mapMoves();
	}
	
	protected void mapMoves() {
		
		moveset.addAll(Arrays.asList(
				new Move(Moves.GROWL),	
        		new Move(Moves.CONFUSION),	
        		new Move(Moves.DOUBLETEAM),	
        		new Move(Moves.TELEPORT)
		));
		
		moveLevels = Map.ofEntries(
				Map.entry(6, Moves.CONFUSION),
				Map.entry(10, Moves.DOUBLETEAM),
				Map.entry(12, Moves.TELEPORT),
//				Map.entry(17, Moves.LUCKYCHANT),
				Map.entry(22, Moves.MAGICALLEAF),
				Map.entry(25, Moves.CALMMIND),
				Map.entry(33, Moves.PSYCHIC),
//				Map.entry(40, Moves.IMPRISON),
				Map.entry(45, Moves.FUTURESIGHT),
				Map.entry(53, Moves.CAPTIVATE),
				Map.entry(60, Moves.HYPNOSIS),
				Map.entry(65, Moves.DREAMEATER)
		);
	}
}