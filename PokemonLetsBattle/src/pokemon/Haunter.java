package pokemon;

import java.util.Arrays;
import java.util.Map;

import entity.Entity;
import moves.Move;
import moves.Moves;
import properties.Ability;
import properties.Type;

public class Haunter extends Pokemon {
	
	public Haunter(int level, Entity ball) {
		super(93, "Haunter", level, ball, 45, 50, 45, 115, 55, 96, 40, 94, 126, 2, Growth.MEDIUMSLOW, 90);
		
		id = Pokedex.HAUNTER;
		types = Arrays.asList(Type.GHOST, Type.POISON);
		ability = Ability.LEVITATE;
		
		mapMoves();
	}
	
	protected void mapMoves() {
		
		moveset.addAll(Arrays.asList(
				new Move(Moves.LICK), 
        		new Move(Moves.HYPNOSIS)
		));
		
		moveLevels = Map.ofEntries(
				Map.entry(15, Moves.NIGHTSHADE),
				Map.entry(19, Moves.CONFUSERAY),
				Map.entry(22, Moves.SUCKERPUNCH),
				Map.entry(25, Moves.SHADOWPUNCH),
				Map.entry(28, Moves.PAYBACK),
				Map.entry(33, Moves.SHADOWBALL),
				Map.entry(39, Moves.DREAMEATER),
				Map.entry(44, Moves.DARKPULSE)
		);
	}
}