package pokemon;

import java.util.Arrays;
import java.util.Map;

import entity.Entity;
import moves.Move;
import moves.Moves;
import properties.Ability;
import properties.Type;

public class Golbat extends Pokemon {
	
	public Golbat(int level, Entity ball) {
		super(42, "Golbat", level, ball, 75, 80, 70, 65, 75, 90, 40, 171, 2, Growth.MEDIUMFAST, 90);
		
		id = Pokedex.GOLBAT;
		types = Arrays.asList(Type.FLYING, Type.POISON);
		ability = Ability.INNERFOCUS;
		
		mapMoves();
	}
	
	protected void mapMoves() {
		
		moveset.addAll(Arrays.asList(
				new Move(Moves.SCREECH),
        		new Move(Moves.LEECHLIFE),
        		new Move(Moves.SUPERSONIC),
        		new Move(Moves.ASTONISH)
		));
		
		moveLevels = Map.ofEntries(
				Map.entry(5, Moves.SUPERSONIC),
				Map.entry(9, Moves.ASTONISH),
				Map.entry(13, Moves.BITE),
				Map.entry(17, Moves.WINGATTACK),
				Map.entry(21, Moves.CONFUSERAY),
				Map.entry(27, Moves.AIRCUTTER),
//				Map.entry(33, Moves.MEANLOOK),
				Map.entry(39, Moves.POISONFANG),
				Map.entry(45, Moves.HAZE),
				Map.entry(51, Moves.AIRSLASH)
		);
	}
	
	public Pokemon evolve() {
		Pokemon evolvedForm = null;
		
		evolvedForm = new Crobat(level, ball);				
		evolvedForm.create(this);
		
		return evolvedForm;
	}
}