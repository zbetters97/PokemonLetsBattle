package pokemon;

import java.util.Arrays;
import java.util.Map;

import entity.Entity;
import moves.Move;
import moves.Moves;
import properties.Ability;
import properties.Type;

public class Rapidash extends Pokemon {
	
	public Rapidash(int level, Entity ball) {
		super(78, "Rapidash", level, ball, 65, 100, 70, 80, 80, 105, -1, 192, 2, Growth.MEDIUMFAST, 60);
		
		id = Pokedex.RAPIDASH;
		type = Type.FIRE;
		ability = Ability.FLASHFIRE;
								
		mapMoves();
	}
	
	protected void mapMoves() {
		
		moveset.addAll(Arrays.asList(
				new Move(Moves.QUICKATTACK),
        		new Move(Moves.MEGAHORN),
        		new Move(Moves.POISONJAB),
        		new Move(Moves.GROWL)
		));
		
		moveLevels = Map.ofEntries(
				Map.entry(7, Moves.GROWL),
				Map.entry(10, Moves.TAILWHIP),
				Map.entry(16, Moves.EMBER),
				Map.entry(19, Moves.STOMP),
				Map.entry(25, Moves.FIRESPIN),
				Map.entry(28, Moves.TAKEDOWN),
				Map.entry(34, Moves.AGILITY),
				Map.entry(38, Moves.FIREBLAST),
				Map.entry(40, Moves.FURYATTACK),
				Map.entry(49, Moves.BOUNCE),
				Map.entry(58, Moves.FLAREBLITZ)
		);
	}
}