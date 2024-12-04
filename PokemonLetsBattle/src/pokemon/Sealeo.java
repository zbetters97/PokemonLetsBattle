package pokemon;

import java.util.Arrays;
import java.util.Map;

import entity.Entity;
import moves.Move;
import moves.Moves;
import properties.Ability;
import properties.Type;

public class Sealeo extends Pokemon {
	
	public Sealeo(int level, Entity ball) {
		super(364, "Sealeo", level, ball, 90, 60, 70, 75, 70, 45, 44, 128, 2, Growth.MEDIUMSLOW, 120);
		
		id = Pokedex.SEALEO;
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
				Map.entry(32, Moves.SWAGGER),
				Map.entry(39, Moves.REST),
				Map.entry(40, Moves.SNORE),
				Map.entry(47, Moves.BLIZZARD),
				Map.entry(55, Moves.SHEERCOLD)
		);
	}
	
	public Pokemon evolve() {
		Pokemon evolvedForm = null;
		
		evolvedForm = new Walrein(level, ball);				
		evolvedForm.create(this);
		
		return evolvedForm;
	}
}