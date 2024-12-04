package pokemon;

import java.util.Arrays;
import java.util.Map;

import entity.Entity;
import moves.Move;
import moves.Moves;
import properties.Ability;
import properties.Type;

public class Rayquaza extends Pokemon {
	
	public Rayquaza(int level, Entity ball) {
		super(384, "Rayquaza", level, ball, 105, 150, 90, 150, 90, 95, -1, 220, 3, Growth.SLOW, 3);
		
		id = Pokedex.RAYQUAZA;
		types = Arrays.asList(Type.DRAGON, Type.FLYING);
		ability = Ability.AIRLOCK;
		
		mapMoves();
	}
	
	protected void mapMoves() {
		
		moveset.addAll(Arrays.asList(
				new Move(Moves.TWISTER)
		));
		
		moveLevels = Map.ofEntries(
				Map.entry(5, Moves.SCARYFACE),
				Map.entry(15, Moves.ANCIENTPOWER),
				Map.entry(20, Moves.DRAGONCLAW),
				Map.entry(30, Moves.DRAGONDANCE),
				Map.entry(35, Moves.CRUNCH),
				Map.entry(45, Moves.FLY),
				Map.entry(50, Moves.REST),
				Map.entry(60, Moves.EXTREMESPEED),
				Map.entry(65, Moves.HYPERBEAM),
				Map.entry(75, Moves.DRAGONPULSE),
				Map.entry(80, Moves.OUTRAGE)
		);
	}
}