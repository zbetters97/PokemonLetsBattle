package pokemon;

import java.util.Arrays;
import java.util.Map;

import entity.Entity;
import moves.Move;
import moves.Moves;
import properties.Ability;
import properties.Type;

public class Blastoise extends Pokemon {
	
	public Blastoise(int level, Entity ball) {
		super(9, "Blastoise", level, ball, 79, 83, 100, 85, 105, 78, -1, 210, 3, Growth.MEDIUMSLOW, 45);
		
		id = Pokedex.WARTORTLE;
		type = Type.WATER;
		ability = Ability.TORRENT;
								
		mapMoves();
	}
	
	protected void mapMoves() {
		
		moveset.addAll(Arrays.asList(
				new Move(Moves.FLASHCANNON), 
        		new Move(Moves.BUBBLE), 
        		new Move(Moves.TACKLE), 
        		new Move(Moves.TAILWHIP)
		));
		
		moveLevels = Map.ofEntries(
				Map.entry(4, Moves.TAILWHIP),
				Map.entry(7, Moves.BUBBLE),
				Map.entry(10, Moves.WITHDRAW),
				Map.entry(13, Moves.WATERGUN),
				Map.entry(16, Moves.BITE),
				Map.entry(20, Moves.RAPIDSPIN),
				Map.entry(24, Moves.PROTECT),
				Map.entry(28, Moves.WATERPULSE),
				Map.entry(32, Moves.AQUATAIL),
				Map.entry(39, Moves.SKULLBASH),
				Map.entry(46, Moves.RAINDANCE),
				Map.entry(53, Moves.HYDROPUMP)
		);
	}
}