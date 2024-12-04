package pokemon;

import java.util.Arrays;
import java.util.Map;

import entity.Entity;
import moves.Move;
import moves.Moves;
import properties.Ability;
import properties.Type;

public class Feraligatr extends Pokemon {
	
	public Feraligatr(int level, Entity ball) {
		super(160, "Feraligatr", level, ball, 85, 105, 100, 79, 83, 78, -1, 210, 3, Growth.MEDIUMSLOW, 45);
		
		id = Pokedex.FERALIGATR;
		type = Type.WATER;
		ability = Ability.TORRENT;
								
		mapMoves();
	}
	
	protected void mapMoves() {
		
		moveset.addAll(Arrays.asList(
				new Move(Moves.SCRATCH), 
        		new Move(Moves.LEER),
        		new Move(Moves.WATERGUN),
        		new Move(Moves.RAGE)
		));
		
		moveLevels = Map.ofEntries(
				Map.entry(6, Moves.WATERGUN),
				Map.entry(8, Moves.RAGE),
				Map.entry(13, Moves.BITE),
				Map.entry(15, Moves.SCARYFACE),
				Map.entry(21, Moves.ICEFANG),
				Map.entry(24, Moves.THRASH),
				Map.entry(30, Moves.AGILITY),
				Map.entry(32, Moves.CRUNCH),
				Map.entry(37, Moves.SLASH),
				Map.entry(45, Moves.SCREECH),
				Map.entry(50, Moves.AQUATAIL),
//				Map.entry(58, Moves.SUPERPOWER),
				Map.entry(63, Moves.HYDROPUMP)
		);
	}
}