package pokemon;

import java.util.Arrays;
import java.util.Map;

import entity.Entity;
import moves.Move;
import moves.Moves;
import properties.Ability;
import properties.Type;

public class Moltres extends Pokemon {
	
	public Moltres(int level, Entity ball) {
		super(146, "Moltres", level, ball, 90, 100, 90, 125, 85, 90, -1, 217, 3, Growth.SLOW, 3);
		
		id = Pokedex.MOLTRES;
		types = Arrays.asList(Type.FLYING, Type.FIRE);
		ability = Ability.PRESSURE;
		
		mapMoves();
	}
	
	protected void mapMoves() {
		
		moveset.addAll(Arrays.asList(
				new Move(Moves.WINGATTACK),
        		new Move(Moves.EMBER)
		));
		
		moveLevels = Map.ofEntries(
				Map.entry(8, Moves.FIRESPIN),
				Map.entry(15, Moves.AGILITY),
//				Map.entry(22, Moves.ENDURE),
				Map.entry(29, Moves.ANCIENTPOWER),
				Map.entry(36, Moves.FLAMETHROWER),
				Map.entry(43, Moves.SAFEGUARD),
				Map.entry(50, Moves.AIRSLASH),
				Map.entry(57, Moves.ROOST),
				Map.entry(64, Moves.HEATWAVE),
				Map.entry(71, Moves.SOLARBEAM),
				Map.entry(78, Moves.SKYATTACK),
				Map.entry(85, Moves.SUNNYDAY)
		);
	}
}