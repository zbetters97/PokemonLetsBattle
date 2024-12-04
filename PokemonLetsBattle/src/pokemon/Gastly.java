package pokemon;

import java.util.Arrays;
import java.util.Map;

import entity.Entity;
import moves.Move;
import moves.Moves;
import properties.Ability;
import properties.Type;

public class Gastly extends Pokemon {
	
	public Gastly(int level, Entity ball) {
		super(92, "Gastly", level, ball, 30, 35, 30, 100, 35, 80, 25, 95, 1, Growth.MEDIUMSLOW, 190);
		
		id = Pokedex.GASTLY;
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
	
	public Pokemon evolve() {
		Pokemon evolvedForm = null;
		
		evolvedForm = new Haunter(level, ball);				
		evolvedForm.create(this);
		
		return evolvedForm;
	}
}