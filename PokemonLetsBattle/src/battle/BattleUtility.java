package battle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import application.GamePanel;
import application.GamePanel.Weather;
import entity.Entity;
import moves.Move;
import moves.Moves;
import moves.Move.MoveType;
import pokemon.Pokemon;
import properties.Type;
import properties.abilities.Ability;

public final class BattleUtility {
	
	private BattleUtility(GamePanel gp) { }
	
	public static Move getBestMove(Pokemon attacker, Pokemon target, Weather weather) {
		
		Move bestMove;
		
		// holds Map of Move and Damage Points
		Map<Move, Integer> moves = new HashMap<>();
		
		// for each move in attacker's move set
		for (Move move : attacker.getMoveSet()) {
			
			if (move.getPower() > 0 && move.getpp() != 0) {
				
				// find damage value of each move (no crit is assumed)
				int damage = calculateDamage(attacker, target, move, weather);
				
				// add move and corresponding damage value to k/v list
				moves.put(move, damage);	
			}		
		}
		
		// find max value in moves list based on value
		if (!moves.isEmpty()) {
			
			bestMove = Collections.max(moves.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey(); 
			
			// if best move does not cause KO
			int damage = calculateDamage(attacker, target, bestMove, weather);
			if (damage < target.getHP()) {
				
				// 33% chance CPU selects random move instead of most powerful			
				int val = 1 + (int)(Math.random() * 4);
				if (val == 1) {				
					int ranMove = (int)(Math.random() * (attacker.getMoveSet().size()));				
					bestMove = attacker.getMoveSet().get(ranMove);
				}	
			}			
		}
		// if list is empty, select random move
		else {
			int ranMove = (int)(Math.random() * (attacker.getMoveSet().size()));				
			bestMove = attacker.getMoveSet().get(ranMove);
		}
		
		return bestMove;
	}
	
	public static int getFirstTurn(Pokemon fighter1, Pokemon fighter2, Move move1, Move move2) {
		
		int first = 1;
		
		if (move1 == null && move2 == null) {
			first = 5;
		}
		else if (move1 == null) {
			first = 3;
		}
		else if (move2 == null) {
			first = 4;
		}
		else {				
			
			double speed1 = fighter1.getSpeed();
			double speed2 = fighter2.getSpeed();
			
			if (fighter1.getAbility().getCategory() == Ability.Category.ATTRIBUTE &&
					fighter1.getAbility().isValid(fighter1)) {
				
				speed1 *= fighter1.getAbility().getFactor();
			}
			if (fighter2.getAbility().getCategory() == Ability.Category.ATTRIBUTE &&
					fighter2.getAbility().isValid(fighter2)) {
				
				speed2 *= fighter2.getAbility().getFactor();
			}
			
			// if both moves go first (EX: Quick Attack)
			if (move1.getGoFirst() && move2.getGoFirst()) {			
				
				// if fighter_one is faster (fighter_one has advantage if equal)
				if (speed1 >= speed2) { 
					first = 1;
				}
				else { 
					first = 2; 
				}
			}
			// if only playerMove goes first (EX: Quick Attack)
			else if (move1.getGoFirst()) {
				first = 1;
			}
			// if only cpuMove goes first (EX: Quick Attack)
			else if (move2.getGoFirst()) {
				first = 2;
			}
			else {
				// if fighter 1 is faster
				if (speed1 > speed2) {
					first = 1;
				}
				// if fighter 2 is faster
				else if (speed1 < speed2) {
					first = 2;
				}
				// if both fighters have equal speed, coin flip decides
				else {
					Random r = new Random();					
					first = (r.nextFloat() <= ((float) 1 / 2)) ? 1 : 2;
				}
			}
		}
		
		return first;
	}
	
	public static int getDelay(Move move1, Move move2) {		
		
		int delay = 0;
		
		// BOTH MOVES ARE ACTIVE
		if (move1 != null && move2 != null) {
			
			// both fighters are waiting
			if (move1.isWaiting() && move2.isWaiting()) {
				delay = 3;	
			}
			// fighter 2 is waiting;
			else if (move2.isWaiting()) {
				delay = 2;	
			}
			// fighter 1 is waiting
			else if (move1.isWaiting()) {
				delay = 1;
			}	
		}
		// CPU MOVE IS ACTIVE AND WAITING
		else if (move2 != null && move2.isWaiting()) {
			delay = 2;
		}
		// PLAYER MOVE IS ACTIVE AND WAITING
		else if (move1 != null && move1.isWaiting()) {
			delay = 1;
		}
				
		return delay;
	}	
	
	public static boolean hit(Pokemon attacker, Pokemon target, Move move, Weather weather) {
		
		boolean hit;
		
		if (target.isProtected()) {
			hit = false;
		}
		else {
			// if move never misses, return true
			if (move.getAccuracy() == -1) {
				hit = true; 
			}
			else {
				double accuracy = BattleUtility.getAccuracy(move, weather) * attacker.getAccuracy();
			
				Random r = new Random();
				float chance = r.nextFloat();
				
				// chance of missing is accuracy / 100
				hit = (chance <= ((float) accuracy / 100)) ? true : false;	
			}
		}
		
		return hit;
	}
	
 	public static int calculateDamage(Pokemon attacker, Pokemon target, Move move, Weather weather) {
		// DAMAGE FORMULA REFERENCE: https://bulbapedia.bulbagarden.net/wiki/Damage (GEN IV)
		
		int damage = 0;
		
		double level = attacker.getLevel();		
		double power = getPower(move, level, weather);
		double A = getAttack(attacker, move, weather);
		double D = getDefense(target, move, weather);
		double STAB = attacker.checkType(move.getType()) ? 1.5 : 1.0;
		double type = getEffectiveness(target, move.getType());
								
		Random r = new Random();
		double random = (double) (r.nextInt(100 - 85 + 1) + 85) / 100.0;
		
		if (attacker.getAbility().getCategory() == Ability.Category.ATTACK &&
				attacker.getAbility().isValid(attacker, target, move)) {
			
			A *= attacker.getAbility().getFactor();
		}
		
		damage = (int)((Math.floor(((((Math.floor((2 * level) / 5)) + 2) * 
			power * (A / D)) / 50)) + 2) * STAB * type * random);
		
		if (target.getAbility().getCategory() == Ability.Category.DEFENSE &&
				target.getAbility().isValid(attacker, target, move)) {
			
			damage *= target.getAbility().getFactor();
		}
						
		return damage;
	}
		
 	public static double getAccuracy(Move move, Weather weather) {
		
		double accuracy = move.getAccuracy();
		
		switch (weather) {
			case CLEAR:
				break;
			case SUNLIGHT:
				if (move.getMove() == Moves.THUNDER) {
					accuracy *= 0.5;
				}
				break;
			case RAIN:
				break;
			case HAIL:
				if (move.getMove() == Moves.BLIZZARD) {
					accuracy = 100;
				}
				break;
			case SANDSTORM:
				if (move.getMove() == Moves.THUNDER) {
					accuracy *= 0.5;
				}
				break;
		}
		
		return accuracy;
	}
	private static double getPower(Move move, double level, Weather weather) {
		
		double power = 1.0; 
		
		power = (move.getPower() == -1) ? level : move.getPower();	
		
		switch (weather) {	
			case CLEAR:
				break;
			case SUNLIGHT:
				if (move.getType() == Type.FIRE) {
					power *= 1.5;
				}
				else if (move.getType() == Type.WATER) {
					power *= 0.5;
				}
				break;
			case RAIN:
				if (move.getType() == Type.WATER) {
					power *= 1.5;
				}
				else if (move.getType() == Type.FIRE) {
					power *= 0.5;
				}
				if (move.getMove() == Moves.SOLARBEAM) {
					power *= 0.5;
				}
				break;
			case HAIL:
				if (move.getMove() == Moves.SOLARBEAM) {
					power *= 0.5;
				}
				break;
			case SANDSTORM:
				if (move.getMove() == Moves.SOLARBEAM) {
					power *= 0.5;
				}
				break;
		}		
		
		return power;
	}	
	private static double getAttack(Pokemon pokemon, Move move, Weather weather) {
		
		double attack = 1.0;
		
		if (move.getMType().equals(MoveType.SPECIAL)) {
			attack = pokemon.getSpAttack();
		}
		else if (move.getMType().equals(MoveType.PHYSICAL)) {
			attack = pokemon.getAttack();
		}
		
		switch (weather) {
			case CLEAR:
				break;
			case SUNLIGHT:					
				break;
			case RAIN:					
				break;
			case HAIL:					
				break;
			case SANDSTORM:
				break;
		}				
		
		return attack;
	}
	private static double getDefense(Pokemon pokemon, Move move, Weather weather) {
		
		double defense = 1.0;
		
		if (move.getMType().equals(MoveType.SPECIAL)) {
			defense = pokemon.getSpDefense();
		}
		else if (move.getMType().equals(MoveType.PHYSICAL)) {
			defense = pokemon.getDefense();
		}
		
		switch (weather) {
			case CLEAR:
				break;
			case SUNLIGHT:					
				break;
			case RAIN:					
				break;
			case HAIL:					
				break;
			case SANDSTORM:
				if (pokemon.getType() == Type.ROCK &&
						move.getMType().equals(MoveType.SPECIAL)) {
					defense *= 1.5;
				}
				break;
		}			
		
		return defense;
	}
	public static double getEffectiveness(Pokemon pokemon, Type type) {
		
		double effect = 1.0;
		
		// if target is single type
		if (pokemon.getTypes() == null) {
			
			// if vulnerable, retrieve and return vulnerable value		
			for (Type vulnType : pokemon.getType().getVulnerability().keySet()) {		
				if (vulnType.getName().equals(type.getName())) {
					effect = pokemon.getType().getVulnerability().get(vulnType);
					return effect;
				}
			}			
			// if resistant, retrieve and return resistance value
			for (Type resType : pokemon.getType().getResistance().keySet()) {			
				if (resType.getName().equals(type.getName())) {
					effect = pokemon.getType().getResistance().get(resType);
					return effect;
				}			
			}		
		}
		// if target is multi type
		else {
			
			// for each type in target
			for (Type targetType : pokemon.getTypes()) {		
				
				// for each vulnerability			
				vulnerabilityLoop:
				for (Type vulnType : targetType.getVulnerability().keySet()) {		
					
					// if found, multiply by effect and move to next loop
					if (vulnType.getName().equals(type.getName())) {						
						effect *= targetType.getVulnerability().get(vulnType);		
						break vulnerabilityLoop;
					}
				}	
				
				// for each resistance
				resistanceLoop:
				for (Type resType : targetType.getResistance().keySet()) {		
					
					// if found, multiply by effect and move to next loop
					if (resType.getName().equals(type.getName())) {
						effect *= targetType.getResistance().get(resType);
						break resistanceLoop;
					}
				}
			}			
			
			// vulnerable and resistant cancel out
			if (effect == 0.75)	{
				effect = 1.0;
			}
		}			
						
		return effect;
	}
	
	public static Pokemon getNextCPUFighter(Pokemon pokemon, Entity trainer) {
		
		if (pokemon == null) {
			return trainer.pokeParty.get(0);
		}
		
		int index = trainer.pokeParty.indexOf(pokemon);
		if (index < 0 || index + 1 == trainer.pokeParty.size()) {
			return null;
		}
		else {
			 return trainer.pokeParty.get(index + 1);
		}
	}
 	public static Pokemon getBestCPUFighter(ArrayList<Pokemon> pokeParty, Pokemon target) {
		
		int available = 0;
		
		// list to hold all candidates based on type effectiveness
		Map<Pokemon, Integer> pokemonList = new HashMap<>();
		
		for (Pokemon p : pokeParty) {
			if (p.isAlive()) {
				available++;
			}
		}
		
		// if more than 1 pokemon in CPU party
		if (available > 1) {
			
			// loop through each pokemon in party
			for (Pokemon p : pokeParty) {
				
				if (p.isAlive()) {
				
					// if party is single type
					if (p.getTypes() == null) {
												
						// if target is single type
						if (target.getTypes() == null) {	
							
							// loop through each type in target pokemon
							for (Type vulnType : target.getType().getVulnerability().keySet()) {	
								
								// if type matches target's vulnerability
								if (vulnType.getName().equals(p.getType().getName()))
									pokemonList.put(p, p.getLevel());
							}
						}					
						// if target is multi type
						else {			
							
							// for each type in target
							for (Type type : target.getTypes()) {
								
								// loop through each vulnerability in type
								for (Type vuln : type.getVulnerability().keySet()) {									
	
									// if type matches target's vulnerability
									if (vuln.getName().equals(p.getType().getName()))
										pokemonList.put(p, p.getLevel());
								}
							}						
						}					
					}				
					// if party is multi type
					else { 
											
						// if target is single type
						if (target.getTypes() == null) {	
							
							// for each type in party
							for (Type type : p.getTypes()) {
								
								// loop through each vulnerability in target pokemon
								for (Type vulnType : target.getType().getVulnerability().keySet()) {	
								
									// if type matches target's vulnerability
									if (vulnType.getName().equals(type.getName()))
										pokemonList.put(p, p.getLevel());
								}
							}
							
							
						}					
						// if target is multi type
						else {			
							
							// for each type in party
							for (Type parType : p.getTypes()) {
	
								// for each type in target
								for (Type tarType : target.getTypes()) {
									
									// loop through each vulnerability in type
									for (Type vuln : tarType.getVulnerability().keySet()) {									
		
										// if type matches target's vulnerability
										if (vuln.getName().equals(parType.getName()))
											pokemonList.put(p, p.getLevel());
									}
								}		
							}
						}
					}
				}				
			}
		}
		// if 1 pokemon remaining in party
		else if (available == 1) {
			for (Pokemon p : pokeParty) {				
				if (p.isAlive()) {
					return p;
				}
			}
		}
		
		// if 0 pokemon remaining in party
		else {
			return null;
		}
			
		Pokemon bestPokemon;
		
		// find best pokemon based on max level
		if (!pokemonList.isEmpty()) {
			bestPokemon = Collections.max(pokemonList.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey();
			return bestPokemon;
		}
		else {			
			// loop through party and find highest level pokemon
			for (Pokemon p : pokeParty) {
				pokemonList.put(p, p.getLevel());
			}
			
			bestPokemon = Collections.max(pokemonList.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey();
			return bestPokemon;
		}
	}
 	
 	public static boolean isCaptured(Pokemon pokemon, Entity ball) {
		/** CATCH RATE FOMRULA REFERENCE (GEN IV): https://bulbapedia.bulbagarden.net/wiki/Catch_rate#Capture_method_(Generation_III-IV) **/
			
		boolean isCaptured = false;
		
		double catchOdds;
		double maxHP = 1.0;
		double hp = 1.0;
		double catchRate = 1.0;
		double statusBonus = 1.0;
		
		maxHP = pokemon.getBHP(); if (maxHP == 0.0) maxHP = 1.0; 		
		hp = pokemon.getHP(); if (hp == 0.0) hp = 1.0;
		catchRate = pokemon.getCatchRate();
		
		if (pokemon.getStatus() != null) {			
			switch(pokemon.getStatus().getAbreviation()) {			
				case "PAR": statusBonus = 1.5; break;
				case "PSN": statusBonus = 1.5; break;
				case "CNF": statusBonus = 1.0; break;
				case "BRN": statusBonus = 1.5; break;
				case "FRZ": statusBonus = 2.0; break;
				case "SLP": statusBonus = 2.0; break;
			}			
		}
		
		catchOdds = ( (((3 * maxHP) - (2 * hp)) / (3 * maxHP) ) * catchRate * statusBonus);
		
		Random r = new Random();
		int roll = (int) (r.nextInt(ball.catchProbability - 0 + 1) + 0);
		
		if (roll <= catchOdds) {
			isCaptured = true;
		}
		
		return isCaptured;
		
	}
	
	public static int calculateEXPGain(Pokemon target, boolean trainerBattle) {
		// EXP FORMULA REFERENCE (GEN I-IV): https://bulbapedia.bulbagarden.net/wiki/Experience		
		
		double b = target.getEXPYeild();
		double L = target.getLevel();
		double s = 1.0;
		double e = 1.0;
		double a = trainerBattle ? 1.5 : 1.0;
		double t = 1.0;
		
		double exp = Math.floor( (b * L) / 7 ) * Math.floor(1 / s) * e * a * t;
						
		return (int) exp;
	}
	
	public static int calculatePay(Entity trainer) {
		// MONEY EARNED FORMULA REFERENCE (ALL GEN): https://bulbapedia.bulbagarden.net/wiki/Prize_money
		
		int payout  = 0;
		
		int level = trainer.pokeParty.get(trainer.pokeParty.size() - 1).getLevel();
		int base = trainer.trainerClass;		
		payout = base * level;
		
		return payout;
	}
}