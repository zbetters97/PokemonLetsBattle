package pokemon;

import java.util.Arrays;
import java.util.Map;

import entity.Entity;
import moves.Move;
import moves.Moves;
import properties.Ability;
import properties.Type;

public class Swampert extends Pokemon {
	
	public Swampert(int level, Entity ball) {
		super(260, "Swampert", level, ball, 100, 110, 90, 85, 90, 60, -1, 210, 3, Growth.MEDIUMSLOW, 45);
		
		id = Pokedex.SWAMPERT;
		types = Arrays.asList(Type.WATER, Type.GROUND);
		ability = Ability.TORRENT;
								
		mapMoves();
	}
	
	protected void mapMoves() {
		
		moveset.addAll(Arrays.asList(
				new Move(Moves.WATERGUN),
        		new Move(Moves.TACKLE),
        		new Move(Moves.GROWL)
		));
		
		moveLevels = Map.ofEntries(
				Map.entry(6, Moves.MUDSLAP),
				Map.entry(10, Moves.WATERGUN),
				Map.entry(16, Moves.MUDSHOT),
				Map.entry(25, Moves.MUDBOMB),
				Map.entry(31, Moves.TAKEDOWN),					
				Map.entry(39, Moves.MUDDYWATER),
				Map.entry(46, Moves.PROTECT),
				Map.entry(52, Moves.EARTHQUAKE),
				Map.entry(61, Moves.ENDEAVOR),
				Map.entry(69, Moves.HAMMERARM)
		);
	}
}