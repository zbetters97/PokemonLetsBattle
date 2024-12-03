package pokemon;

import java.util.Arrays;
import java.util.Map;

import entity.Entity;
import moves.Move;
import moves.Moves;
import properties.Ability;
import properties.Type;

public class Gengar extends Pokemon {
	
	public Gengar(int level, Entity ball) {
		super(94, "Gengar", level, ball, 60, 65, 60, 130, 75, 110, -1, -1, 190, 3, Growth.MEDIUMSLOW, 45);
		
		id = Pokedex.GENGAR;
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
				Map.entry(26, Moves.PAYBACK),
				Map.entry(29, Moves.SHADOWBALL),
				Map.entry(33, Moves.DREAMEATER),
				Map.entry(36, Moves.DARKPULSE)
		);
	}
}