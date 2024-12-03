package pokemon;

import java.util.Arrays;
import java.util.Map;

import entity.Entity;
import moves.Move;
import moves.Moves;
import properties.Ability;
import properties.Type;

public class Pikachu extends Pokemon {
	
	public Pikachu(int level, Entity ball) {
		super(25, "Pikachu", level, ball, 55, 55, 40, 50, 50, 90, 30, 26, 82, 2, Growth.MEDIUMFAST, 190);
		
		id = Pokedex.PIKACHU;
		type = Type.ELECTRIC;
		ability = Ability.STATIC;
								
		mapMoves();
	}
	
	protected void mapMoves() {
		
		moveset.addAll(Arrays.asList(
				new Move(Moves.THUNDERSHOCK),
        		new Move(Moves.GROWL)
		));
		
		moveLevels = Map.ofEntries(
				Map.entry(5, Moves.TAILWHIP),
				Map.entry(10, Moves.THUNDERWAVE),
				Map.entry(13, Moves.QUICKATTACK),
				Map.entry(18, Moves.DOUBLETEAM),
				Map.entry(21, Moves.SLAM),
				Map.entry(26, Moves.THUNDERBOLT),
				Map.entry(34, Moves.AGILITY),
				Map.entry(37, Moves.DISCHARGE),
				Map.entry(45, Moves.THUNDER)
		);
	}
}