package pokemon;

import java.util.Arrays;
import java.util.Map;

import entity.Entity;
import moves.Move;
import moves.Moves;
import properties.Ability;
import properties.Type;

public class Squirtle extends Pokemon {
	
	public Squirtle(int level, Entity ball) {
		super(7, "Squirtle", level, ball,  44, 48, 65, 50, 64, 43, 16, 8, 66, 1, Growth.MEDIUMSLOW, 45);
		
		id = Pokedex.SQUIRTLE;
		type = Type.WATER;
		ability = Ability.TORRENT;
								
		mapMoves();
	}
	
	protected void mapMoves() {
		
		moveset.addAll(Arrays.asList(
				new Move(Moves.TACKLE)
		));
		
		moveLevels = Map.ofEntries(
				Map.entry(4, Moves.TAILWHIP),
				Map.entry(7, Moves.BUBBLE),
				Map.entry(10, Moves.WITHDRAW),
				Map.entry(13, Moves.WATERGUN),
				Map.entry(16, Moves.BITE),
				Map.entry(19, Moves.RAPIDSPIN),
				Map.entry(22, Moves.PROTECT),
				Map.entry(25, Moves.WATERPULSE),
				Map.entry(28, Moves.AQUATAIL),
				Map.entry(31, Moves.SKULLBASH),
				Map.entry(34, Moves.RAINDANCE),
				Map.entry(37, Moves.HYDROPUMP)
		);
	}
}