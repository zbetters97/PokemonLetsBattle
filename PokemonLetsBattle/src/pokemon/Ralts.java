package pokemon;

import java.util.Arrays;
import java.util.Map;

import entity.Entity;
import moves.Move;
import moves.Moves;
import properties.Ability;
import properties.Type;

public class Ralts extends Pokemon {
	
	public Ralts(int level, Entity ball) {
		super(280, "Ralts", level, ball, 28, 25, 25, 45, 45, 40, 20, 70, 1, Growth.SLOW, 235);
		
		id = Pokedex.RALTS;
		type = Type.PSYCHIC;
		ability = Ability.SYNCHRONIZE;
		
		mapMoves();
	}
	
	protected void mapMoves() {
		
		moveset.addAll(Arrays.asList(
				new Move(Moves.GROWL)
		));
		
		moveLevels = Map.ofEntries(
				Map.entry(6, Moves.CONFUSION),
				Map.entry(10, Moves.DOUBLETEAM),
				Map.entry(12, Moves.TELEPORT),
//				Map.entry(17, Moves.LUCKYCHANT),
				Map.entry(21, Moves.MAGICALLEAF),
				Map.entry(23, Moves.CALMMIND),
				Map.entry(28, Moves.PSYCHIC),
//				Map.entry(32, Moves.IMPRISON),
				Map.entry(34, Moves.FUTURESIGHT),
				Map.entry(39, Moves.CHARM),
				Map.entry(43, Moves.HYPNOSIS),
				Map.entry(45, Moves.DREAMEATER)
		);
	}
	
	public Pokemon evolve() {
		Pokemon evolvedForm = null;
		
		evolvedForm = new Kirlia(level, ball);				
		evolvedForm.create(this);
		
		return evolvedForm;
	}
}