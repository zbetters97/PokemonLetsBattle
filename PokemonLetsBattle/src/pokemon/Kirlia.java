package pokemon;

import java.util.Arrays;
import java.util.Map;

import entity.Entity;
import moves.Move;
import moves.Moves;
import properties.Ability;
import properties.Type;

public class Kirlia extends Pokemon {
	
	public Kirlia(int level, Entity ball) {
		super(281, "Kirlia", level, ball, 38, 35, 35, 65, 55, 50, 40, 35, 2, Growth.SLOW, 120);
		
		id = Pokedex.KIRLIA;
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
				Map.entry(31, Moves.PSYCHIC),
//				Map.entry(36, Moves.IMPRISON),
				Map.entry(39, Moves.FUTURESIGHT),
				Map.entry(45, Moves.CHARM),
				Map.entry(50, Moves.HYPNOSIS),
				Map.entry(53, Moves.DREAMEATER)
		);
	}
	
	public Pokemon evolve() {
		Pokemon evolvedForm = null;
		
		evolvedForm = new Gardevoir(level, ball);				
		evolvedForm.create(this);
		
		return evolvedForm;
	}
}