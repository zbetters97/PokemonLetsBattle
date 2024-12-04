package pokemon;

import java.util.Arrays;
import java.util.Map;

import entity.Entity;
import moves.Move;
import moves.Moves;
import properties.Ability;
import properties.Type;

public class Dragonite extends Pokemon {
	
	public Dragonite(int level, Entity ball) {
		super(149, "Dragonite", level, ball, 91, 134, 95, 100, 100, 80, -1, 218, 3, Growth.SLOW, 45);
		
		id = Pokedex.DRAGONITE;
		types = Arrays.asList(Type.DRAGON, Type.FLYING);
		ability = Ability.INNERFOCUS;
		
		mapMoves();
	}
	
	protected void mapMoves() {
		
		moveset.addAll(Arrays.asList(
				new Move(Moves.FIREPUNCH),
        		new Move(Moves.THUNDERPUNCH),
        		new Move(Moves.ROOST),
        		new Move(Moves.TWISTER)
		));
		
		moveLevels = Map.ofEntries(
				Map.entry(5, Moves.THUNDERWAVE),
				Map.entry(11, Moves.TWISTER),
				Map.entry(15, Moves.DRAGONRAGE),
				Map.entry(21, Moves.SLAM),
				Map.entry(25, Moves.AGILITY),
				Map.entry(33, Moves.AQUATAIL),
				Map.entry(39, Moves.DRAGONRUSH),
				Map.entry(47, Moves.SAFEGUARD),
				Map.entry(53, Moves.DRAGONDANCE),
				Map.entry(55, Moves.WINGATTACK),
				Map.entry(64, Moves.OUTRAGE),
				Map.entry(73, Moves.HYPERBEAM)
		);
	}
}