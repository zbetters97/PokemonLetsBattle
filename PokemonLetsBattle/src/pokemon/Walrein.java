package pokemon;

import java.util.Arrays;
import java.util.Map;

import entity.Entity;
import moves.Move;
import moves.Moves;
import properties.Ability;
import properties.Type;

public class Walrein extends Pokemon {
	
	public Walrein(int level, Entity ball) {
		super(365, "Walrein", level, ball, 110, 80, 90, 95, 90, 65, -1, 192, 3, Growth.MEDIUMSLOW, 45);
		
		id = Pokedex.WALREIN;
		types = Arrays.asList(Type.ICE, Type.WATER);
		ability = Ability.THICKFAT;
								
		mapMoves();
	}
	
	protected void mapMoves() {
		
		moveset.addAll(Arrays.asList(
				new Move(Moves.CRUNCH),
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
				Map.entry(44, Moves.ICEFANG),
				Map.entry(52, Moves.BLIZZARD),
				Map.entry(65, Moves.SHEERCOLD)
		);
	}
}