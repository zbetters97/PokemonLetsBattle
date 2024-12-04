package pokemon;

import java.util.Arrays;
import java.util.Map;

import entity.Entity;
import moves.Move;
import moves.Moves;
import properties.Ability;
import properties.Type;

public class Zapdos extends Pokemon {
	
	public Zapdos(int level, Entity ball) {
		super(145, "Zapdos", level, ball, 90, 90, 85, 125, 90, 100, -1, 216, 3, Growth.SLOW, 3);
		
		id = Pokedex.ZAPDOS;
		types = Arrays.asList(Type.FLYING, Type.ELECTRIC);
		ability = Ability.PRESSURE;
		
		mapMoves();
	}
	
	protected void mapMoves() {
		
		moveset.addAll(Arrays.asList(
				new Move(Moves.PECK),
        		new Move(Moves.THUNDERSHOCK)
		));
		
		moveLevels = Map.ofEntries(
				Map.entry(8, Moves.THUNDERWAVE),
				Map.entry(15, Moves.DETECT),
				Map.entry(22, Moves.PLUCK),
				Map.entry(29, Moves.ANCIENTPOWER),
//				Map.entry(36, Moves.CHARGE),
				Map.entry(43, Moves.AGILITY),
				Map.entry(50, Moves.DISCHARGE),
				Map.entry(57, Moves.ROOST),
				Map.entry(64, Moves.LIGHTSCREEN),
				Map.entry(71, Moves.DRILLPECK),
				Map.entry(78, Moves.THUNDER),
				Map.entry(85, Moves.RAINDANCE)
		);
	}
}