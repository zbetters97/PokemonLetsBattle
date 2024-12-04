package pokemon;

import java.util.Arrays;
import java.util.Map;

import entity.Entity;
import moves.Move;
import moves.Moves;
import properties.Ability;
import properties.Type;

public class Spheal extends Pokemon {
	
	public Spheal(int level, Entity ball) {
		super(363, "Spheal", level, ball, 70, 40, 50, 55, 50, 25, 32, 75, 1, Growth.MEDIUMSLOW, 255);
		
		id = Pokedex.SPHEAL;
		types = Arrays.asList(Type.ICE, Type.WATER);
		ability = Ability.THICKFAT;
								
		mapMoves();
	}
	
	protected void mapMoves() {
		
		moveset.addAll(Arrays.asList(
				new Move(Moves.WATERGUN), 
        		new Move(Moves.DEFENSECURL),
        		new Move(Moves.GROWL)
		));
		
		moveLevels = Map.ofEntries(
				Map.entry(19, Moves.BODYSLAM),
				Map.entry(25, Moves.AURORABEAM),
				Map.entry(31, Moves.HAIL),
				Map.entry(37, Moves.REST),
				Map.entry(38, Moves.SNORE),
				Map.entry(43, Moves.BLIZZARD),
				Map.entry(49, Moves.SHEERCOLD)
		);
	}
	
	public Pokemon evolve() {
		Pokemon evolvedForm = null;
		
		evolvedForm = new Sealeo(level, ball);				
		evolvedForm.create(this);
		
		return evolvedForm;
	}
}