package battle;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import application.GamePanel;

import java.lang.Math;

import moves.Move;
import pokemon.Pokemon;
import properties.Type;

/*** BATTLE ENGINE CLASS ***/
public class BattleEngine {
	
	private Pokemon winningPokemon;
	private Pokemon[] pokemon;
	private int[] cpuItems;
	public boolean swap = false;
	private GamePanel gp;
	
	private int faint_SE = 4;
	private int moves_SE = 5;
	private int battle_SE = 6;
	
	private String dialogue;
	
	/** GETTERS AND SETTERS **/
	public Pokemon getWinningPokemon() { return winningPokemon; }
	public void setWinningPokemon(Pokemon winningPokemon) { this.winningPokemon = winningPokemon; }
	/** END GETTERS AND SETTERS **/
	
	/** CONSTRUCTOR 
	 * @param trainer 1 pokemon, trainer 2 pokemon
	 **/
	public BattleEngine(Pokemon fighter_one, Pokemon fighter_two, GamePanel gp) {
				
		// array to hold fighting pokemon
		pokemon = new Pokemon[12];
		
		pokemon[0] = fighter_one;
		pokemon[1] = fighter_two;
		
		this.cpuItems = new int[]{3, 2, 1, 1};
		
		this.gp = gp;
	}
	/** END CONSTRUCTOR **/

	/** SWAP POKEMON METHOD
	 * Swap in battle pokemon with new pokemon
	 * @param new pokemon to swap in, int spot
	 **/
	public void swapPokemon(Pokemon newPokemon, int player) {
						
		int slot = 0;
		
		// loop through list of battle pokemon and find open slot
		while (pokemon[slot] != null) {
			
			// if chosen pokemon exists in list
			if (pokemon[slot].getName() == newPokemon.getName()) {
				
				// re-assign to fighter slot
				pokemon[player] = pokemon[slot];
				
				// remove original slot
				pokemon[slot] = null;
				
				return;
			}
			slot ++;
			
			// if reached end of list, break loop
			if (slot == pokemon.length)
				break;
		}
		
		// if slot is open, assign new fighter
		pokemon[slot] = newPokemon;			
		Pokemon temp = pokemon[player];
		
		// swap fighter to old slot
		pokemon[player] = pokemon[slot];
		pokemon[slot] = temp;
	}
	/** END SWAP POKEMON METHOD **/
	
	/** CPU SELECT MOVE METHOD 
	 * Find which move will cause most damage
	 * @return most powerful move
	 **/
	public Move cpuSelectMove() {
				
		// if cpu used potion, no move is selected
		if (usePotion())
			return null;
		
		if (useHeal()) 
			return null;
		
		// holds Map of Move and Damage Points
		Map<Move, Integer> moves = new HashMap<>();
		
		// for each move in attacker's move set
		for (Move move : pokemon[1].getMoveSet()) {
			
			if (!move.isToSelf() && move.getpp() != 0) {
				
				// find damage value of each move (no crit is assumed)
				int damage = calculateDamage(1, 0, move, 1.0, true);
				
				// add move and corresponding damage value to k/v list
				moves.put(move, damage);	
			}		
		}
		
		Move bestMove;
		
		// find max value in moves list based on value
		if (!moves.isEmpty()) {
			
			// 33% chance CPU selects random move instead of most powerful			
			int val = 1 + (int)(Math.random() * 4);
			if (val == 1) {				
				int ranMove = (int)(Math.random() * (pokemon[1].getMoveSet().size()));				
				bestMove = pokemon[1].getMoveSet().get(ranMove);
			}
			else
				bestMove = Collections.max(moves.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey(); 	
		}
		// if list is empty, select random move
		else {
			int ranMove = (int)(Math.random() * (pokemon[1].getMoveSet().size()));				
			bestMove = pokemon[1].getMoveSet().get(ranMove);
		}
		
		return bestMove;
	}
	/** END CPU SELECT MOVE METHOD **/
	
	/** IS CPU HEALTH LOW METHOD 
	 * Check if CPU should use a potion
	 * @return true if potion used, false if not
	 **/
	private boolean usePotion() {
		
		// if cpu health is low
		boolean isHPLow = ((double) pokemon[1].getHP() / pokemon[1].getBHP() < 0.20);
		if (isHPLow) {
							
			// if super potion fully heals
			if (pokemon[1].getHP() + 60 >= pokemon[1].getBHP()) {
				
				// try super potion first
				if (healCPU(60, 0)) return true;
				if (healCPU(120, 1)) return true;
				if (healCPU(999, 2)) return true;
			}		
			// if hyper potion fully heals
			else if (pokemon[1].getHP() + 120 >= pokemon[1].getBHP()) {
				// try hyper potion first
				if (healCPU(120, 1)) return true;
				if (healCPU(999, 2)) return true;
				if (healCPU(60, 0)) return true;
			}
			// try full restore first
			else {
				if (healCPU(999, 2)) return true;
				if (healCPU(120, 1)) return true;
				if (healCPU(60, 0)) return true;
			}
		}
		
		return false;
	}
	/** END USE POTION METHOD **/
	
	/** HEAL CPU METHOD 
	 * Restore health of CPU Pokemon
	 * @param potion healing hp
	 * @return true if item is available, false if not
	 **/
	private boolean healCPU(int newHP, int item) {
		
		// if cpu has items
		if (cpuItems[item] != 0) {
		
			int hp = pokemon[1].getHP() + newHP;
			cpuItems[item] -= 1;
			
			if (hp > pokemon[1].getBHP()) 
				hp = pokemon[1].getBHP();	
			
			pokemon[1].setHP(hp);
			
			gp.playSE(battle_SE, "heal");
			
			if (newHP == 60) {				
				System.out.println("'s POTION restored " + 
					pokemon[1].getName() + "'s health!");
			}
			else if (newHP == 120) {				
				System.out.println("'s HYPER POTION restored " + 
					pokemon[1].getName() + "'s health!");
			}
			else if (newHP == 999) {				
				System.out.println("'s FULL RESTORE restored " + 
					pokemon[1].getName() + "'s health!");
			}
			
			return true;
		}
		else 
			return false;
	}
	/** END HEAL CPU METHOD **/
	
	/** USE HEAL METHOD 
	 * Check if CPU should use healing item, apply if so
	 * @return true if heal is used, false if not
	 **/
	private boolean useHeal() {
		
		// if cpu has status
		if (pokemon[1].getStatus() != null && cpuItems[3] != 0) {
			
			// if cpu has healing item
			if (cpuItems[3] != 0) {
				
				cpuItems[3] -= 1;
				
				String status = pokemon[1].getStatus().getAbreviation();
				
				// only apply item for asleep or frozen
				if (status.equals("SLP") || status.equals("FRZ")) {
					
					System.out.println(" used a FULL HEAL!");
					System.out.println(pokemon[1].getName() + pokemon[1].getStatus().printRecover());
					
					pokemon[1].setStatusCounter(0); pokemon[1].setStatusLimit(0);
					pokemon[1].setStatus(null);
					
					// healing item used
					return true;
				}
			}
		}		
		
		return false;
	}
	/** END USE POTION METHOD **/
	
	/** GET DELAYED MOVE METHOD
	 * Check if a move is waiting and return which player 
	 * @param trainer 1 move, trainer 2 move
	 * @return 3 if both, 1 if player 1, 2 if player 2, 0 if neither
	 **/
	public int getDelayedMove(Move move1, Move move2) {		
		
		// if both moves are active
		if (move1 != null && move2 != null) {	
			
			//both players are waiting
			if (move1.getTurns() != move1.getNumTurns() && move2.getTurns() != move2.getNumTurns())
				return 3;
			
			else if (move1.getTurns() != move1.getNumTurns())
				return 1;
			else if (move2.getTurns() != move2.getNumTurns())
				return 2;
		}
		// if only player 1 is active
		else if (move1 != null) {
			if (move1.getTurns() != move1.getNumTurns())
				return 1;
		}
		// if only player 2 is active
		else if (move2 != null) {
			if (move2.getTurns() != move2.getNumTurns())
				return 2;
		}
		
		return 0;
	}
	/** END GET DELAYED MOVE METHOD **/	
	
	/** MOVE METHOD 
	 * Find which trainer moves first and initiate turn
	 * @param trainer 1 move, trainer 2 move
	 **/
	public void move(Move move1, Move move2) {
		
		// if both pokemon are alive
		if (pokemon[0].isAlive() && pokemon[1].isAlive()) {
			
			int numTurn = 0;			
			
			// 1 if trainer 1 moves first, 2 if trainer 2 moves first
			// 3 if only trainer 2, 4 if only trainer 1, 5 if neither
			if (move1 == null && move2 == null)
				numTurn = 5;
			else if (move1 == null)
				numTurn = 3;
			else if (move2 == null)
				numTurn = 4;
			else
				numTurn = getTurn(move1, move2);
			
			// do nothing if numTurn == 5
			if (numTurn == 1) 
				turn(0, 1, move1, move2);	
			else if (numTurn == 2)
				turn(1, 0, move2, move1);	
			else if (numTurn == 3)
				turn(1, 0, move2, null);
			else if (numTurn == 4)
				turn(0, 1, move1, null);
		
			// check if either fighter has status damage
			statusDamage();
		}
	}
	/** END MOVE METHOD **/
	
	/** GET TURN METHOD 
	 * Calculate which trainer moves first
	 * @param trainer 1 move, trainer 2 move
	 * @return 1 if trainer 1 moves first, 2 if trainer 2 moves first
	 **/
	private int getTurn(Move move1, Move move2) {		
		
		// if both moves go first (EX: Quick Attack)
		if (move1.getGoFirst() && move2.getGoFirst()) {			
			
			// if fighter_one is faster (fighter_one has advantage if equal)
			if (pokemon[0].getSpeed() >= pokemon[1].getSpeed()) 
				return 1;
			else 
				return 2;
		}
		// if only move1 goes first (EX: Quick Attack)
		else if (move1.getGoFirst()) 
			return 1;
		// if only move2 goes first (EX: Quick Attack)
		else if (move2.getGoFirst()) 
			return 2;
		else {
			// if fighter_one is faster
			if (pokemon[0].getSpeed() > pokemon[1].getSpeed())
				return 1;
			// if both pokemon have equal speed, coin flip decides
			else if (pokemon[0].getSpeed() == pokemon[1].getSpeed()) {
				Random r = new Random();
				return (r.nextFloat() <= ((float) 1 / 2)) ? 1 : 2;
			}
			else
				return 2;
		}
	}
	/** END GET TURN METHOD **/
	
	/** TURN METHOD 
	 * Dictate which trainer's turn it is
	 * @param trainer 1 pokemon, trainer 1 move, trainer 2 move
	 **/
	private void turn(int pk1, int pk2, Move move1, Move move2) {

		// if attacker can fight and has fighting move
		if (canTurn(pk1) && move1 != null)
			useTurn(pk1, pk2, move1, move2);
		
		// target becomes attacker if battle not over
		if (pokemon[pk1].isAlive() && pokemon[pk2].isAlive()) {
					
			// if target can fight and has fighting move
			if (canTurn(pk2) && move2 != null)
				useTurn(pk2, pk1, move2, move1);
		}
	}
	/** END TURN METHOD **/	
	
	/** CAN TURN METHOD 
	 * Check status condition to check if pokemon is able to turn 
	 * @param int attacking pokemon
	 * @return true if can turn, false if can't
	 **/ 
	private boolean canTurn(int atk) {
		
		int val = 0;
		
		// if attacker has status effect
		if (pokemon[atk].getStatus() != null) {
			
			// check which status
			switch (pokemon[atk].getStatus().getAbreviation()) {
			
				case "PAR":		
					
					// 1/4 chance can't move due to PAR
					val = 1 + (int)(Math.random() * 4);
					if (val == 1) {			
						
						gp.playSE(battle_SE, "paralyzed");
						
						System.out.println(pokemon[atk].getName() + pokemon[atk].getStatus().printStatus());
						
						return false;
					} 
					else 
						return true;			
					
				case "FRZ":
					
					// 1/4 chance attacker can thaw from ice
					val = 1 + (int)(Math.random() * 4);
					if (val == 1) {
						System.out.println(pokemon[atk].getName() + pokemon[atk].getStatus().printRecover());
						pokemon[atk].setStatus(null);
						return true;
					}
					else {	
						
						gp.playSE(battle_SE, "frozen");
						
						System.out.println(pokemon[atk].getName() + pokemon[atk].getStatus().printStatus());
						
						return false;						
					}
					
				case "SLP": return getEffect(atk);
				case "CNF": return getEffect(atk);
				default: return true;
			}
		}	
		else 
			return true;
	}
	/** END CAN TURN METHOD **/	
	
	/**  GET EFFECT METHOD 
	 * Check if pokemon is asleep or confused
	 * @param int pokemon
	 * @return true if can't turn, false if can turn
	 **/
	private boolean getEffect(int pkm) {
		
		String status = pokemon[pkm].getStatus().getAbreviation();		
		
		// if first move under condition, set number of moves until free (1-5)
		if (pokemon[pkm].getStatusLimit() == 0) 
			pokemon[pkm].setStatusLimit((int)(Math.random() * 5));	
		
		// if number of moves under condition hit limit, remove condition
		if (pokemon[pkm].getStatusCounter() >= pokemon[pkm].getStatusLimit()) {
									
			if (status.equals("SLP") || status.equals("CNF")) 
				System.out.println(pokemon[pkm].getName() + pokemon[pkm].getStatus().printRecover());	
		
			pokemon[pkm].setStatusCounter(0); pokemon[pkm].setStatusLimit(0);
			pokemon[pkm].setStatus(null);
			
			return true;
		}
		// pokemon still under status condition
		else {
			
			// increase counter
			pokemon[pkm].setStatusCounter(pokemon[pkm].getStatusCounter() + 1);
			
			if (status.equals("SLP")) {	
				
				gp.playSE(battle_SE, "asleep");
				
				System.out.println(pokemon[pkm].getName() + pokemon[pkm].getStatus().printStatus());
				
				return false;
			}
			else if (status.equals("CNF")) {		
				
				gp.playSE(battle_SE, "confused");
				
				System.out.println(pokemon[pkm].getName() + pokemon[pkm].getStatus().printStatus());
				
				// if pokemon hurt itself in confusion
				if (confusionDamage(pkm)) 
					return false;
				else 
					return true;				
			}
			// default return value
			return false;
		}
	}
	/**  END GET EFFECT METHOD **/
	
	/** STATUS DAMAGE METHOD 
	 * Check if either pokemon has status condition and apply damage
	 **/
	private void statusDamage() {
		
		StatusEffect condition = (Pokemon p) -> {
			
			if (p.getStatus() != null) {				
				
				if (p.getStatus().getAbreviation().equals("PSN") || p.getStatus().getAbreviation().equals("BRN")) {
					
					// status effects reference: https://pokemon.fandom.com/wiki/Status_Effects			
					int damage = (int) (p.getHP() * 0.16);
					int newHP = p.getHP() - damage;		
					
					if (newHP <= 0) {
						newHP = 0;
						p.setAlive(false);
					}
					
					p.setHP(newHP);			
					
					gp.playSE(battle_SE, p.getStatus().getName());
					
					System.out.println(p.getName() + p.getStatus().printStatus());
										
					return damage;
				}							
			}
			return 0;
		};
		
		if (pokemon[0].isAlive()) {
			int damage = condition.dealDamage(pokemon[0]);

			if (!pokemon[0].isAlive())
				defeated(1, 0, damage);
		}
		
		if (pokemon[1].isAlive()) {
			int damage = condition.dealDamage(pokemon[1]);

			if (!pokemon[1].isAlive())
				defeated(0, 1, damage);
		}		
	}
	/** END STATUS DAMAGE METHOD **/
	
	/** CALCULATE CONFUSION DAMAGE DEALT METHOD 
	 * Check if pokemon takes confusion damage
	 * @param number of attacking pokemon
	 * @return true if pokemon hurts self, false if not
	 **/
	private boolean confusionDamage(int atk) {
		
		int trg = (atk == 1) ? 0 : 1;
		
		// 50% chance of hurting self
		int val = 1 + (int)(Math.random() * 2);	
		if (val == 1) {					
			
			double level = pokemon[atk].getLevel();
			//double power = Move.CONFUSE.getPower();
			double power = 1.0;
			double A = pokemon[atk].getAttack();
			double D = pokemon[atk].getDefense();
					
			// confusion damage reference: https://pokemonlp.fandom.com/wiki/Confusion_(status)
			int damage = (int)((Math.floor(((((Math.floor((2 * level) / 5)) + 2) * 
				power * (A / D)) / 50)) + 2));
		
			gp.playSE(battle_SE, "hit-normal");
			
			System.out.println(pokemon[atk].getName() + pokemon[atk].getStatus().printStatus());
			
			int hp = pokemon[atk].getHP() - damage;
			
			// pokemon defeated itself in confusion damage
			if (hp <= 0) {
				hp = 0;			
				pokemon[atk].setHP(hp);
				pokemon[atk].setAlive(false);
				defeated(trg, atk, damage);				
				return true;
			}
			// apply confusion damage
			else {
				pokemon[atk].setHP(hp);				
				return true;	
			}
		}
		else
			return false;
	}
	/** END CALCULATE CONFUSION DAMAGE DEALT METHOD **/	
	
	/** USE TURN METHOD 
	 * Initiate attack with available moves
	 * @param int attacker, int target, Moves attacker move, Moves target move
	 **/	
	private void useTurn(int atk, int trg, Move atkMove, Move trgMove) {
						
		dialogue = pokemon[atk].toString() + " used\n" + atkMove.toString() + "!"; 	
		gp.ui.dialogueTimerMax = 120;
		gp.ui.battleSubState = gp.ui.subStateDialogue;
						
		// if not delayed move or delayed move is ready
		if (1 >= atkMove.getTurns()) {	
			
			gp.playSE(moves_SE, atkMove.getName());
			
			// reset turns to wait
			atkMove.setTurns(atkMove.getNumTurns());
		}
		// delayed move is used for first time
		else if (atkMove.getTurns() == atkMove.getNumTurns()) {
						
			System.out.println(atkMove.getDelay(pokemon[atk].getName()));	
			
			// reduce number of turns to wait
			atkMove.setTurns(atkMove.getTurns() - 1);	
			
			return;
		}
		
		// decrease move pp
		atkMove.setpp(atkMove.getpp() - 1);	
		
		attack(atk, trg, atkMove, trgMove);
	}		
	/** END USE TURN METHOD **/
	
	/** ATTACK METHOD 
	 * Handle selected move, calculate damage, and apply it to target
	 * @param number of attacker, number of target, move of attacker
	 **/
	private void attack(int atk, int trg, Move move, Move trgMove) {
				
        // if attack lands
		if (isHit(atk, move, trgMove)) {
			
			// move has a status affect
			if (move.getMType().equals("Status"))
				statusMove(trg, move);				
			
			// move has an attribute affect
			else if (move.getMType().equals("Attribute")) 	
				attributeMove(atk, trg, move);
			
			// move is in other category
			else if (move.getMType().equals("Other")) {
				
				switch (move.getName()) {
					case "TELEPORT":	
						System.out.println(pokemon[atk].getName() + " teleported away!");
						System.exit(0);
						break;						
					default:
						break;
				}		
			}
			// move is damage-dealing
			else {				
				
				// get critical damage (1 or 1.5)
				double crit = isCritical(move);
				int damage = 1;
				
				// logic for seismic toss
				if (move.getPower() == -1)
					 damage = pokemon[atk].getLevel();
				else
					damage = calculateDamage(atk, trg, move, crit, false);
						
				// no damage dealt
				if (damage == 0) {
					System.out.println("It had no effect!");
				}
				else {				
					
					// if critical hit
					if (crit == 1.5) {
						System.out.println("A critical hit!");
					}
				
					System.out.println(pokemon[trg].getName() + " took " + damage + " damage!");	
					
					absorbHP(atk, trg, move, damage);
											
					// if damage is fatal
					if (damage >= pokemon[trg].getHP())	{
						dealDamage(trg, damage);
						isRecoil(atk, trg, move, damage);
						defeated(atk, trg, damage);
					}						
					// fighter survives hit
					else {							
						dealDamage(trg, damage);							
						applyEffect(atk, trg, move);							
						isRecoil(atk, trg, move, damage);
					}
				}	
			}				
		}
		// attack missed
		else {
			System.out.println("The attack missed!");
		}				
	}		
	/** END ATTACK METHOD **/
	
	/** IS HIT METHOD 
	 * Calculates if move lands on target
	 * @param number of attacker, selected move
	 * @return true if attack lands, false if not
	 **/
	private boolean isHit(int atk, Move move, Move trgMove) {
		
		if (trgMove == null)
			return true;
		
		// if target used delayed move and delayed move protects target		
		if (trgMove.getTurns() == 1 && !trgMove.getCanHit())
			return false;
				
		// if move never misses, return true
		if (move.getAccuracy() == -1) 
			return true; 
		
		double accuracy = move.getAccuracy() * pokemon[atk].getAccuracy();
		
		Random r = new Random();
		float chance = r.nextFloat();
		
		// chance of missing is accuracy value / 100
		return (chance <= ((float) accuracy / 100)) ? true : false;
	}
	/** END IS HIT METHOD **/
	
	/** STATUS MOVE METHOD 
	 * Adds status effect to pokemon if not already
	 * @param number of target, selected move
	 **/
	private void statusMove(int trg, Move move) {
		
		// if pokemon does not already have status affect
		if (pokemon[trg].getStatus() == null) {
			
			pokemon[trg].setStatus(move.getEffect());	
			
			System.out.println(pokemon[trg].getName() + " is " + 
					pokemon[trg].getStatus().getCondition() + "!");
		}
		// pokemon already has status affect
		else {
			System.out.println(pokemon[trg].getName() + " is already " + 
					pokemon[trg].getStatus().getCondition() + "!");
		}
	}
	/** END STATUS MOVE METHOD **/
	
	/** ATTRIBUTE MOVE METHOD 
	 * Raise or lower stat of target or to self
	 * @param number of attacker, number of target, selected move
	 **/
	private void attributeMove(int atk, int trg, Move move) {
		
		// if move changes self attributes
		if (move.isToSelf()) {
			
			// loop through each specified attribute to be changed
			for (String stat : move.getStats()) 
				pokemon[atk].changeStat(stat, move.getLevel());	
		}
		// if move changes target attributes
		else {
			// loop through each specified attribute to be changed
			for (String stat : move.getStats()) 
				pokemon[trg].changeStat(stat, move.getLevel());	
		}
		
		// attributes raised
		if (move.getLevel() > 0) {
			gp.playSE(battle_SE, "stat-up");
		}
		// attributes lowered
		else  {
			gp.playSE(battle_SE, "stat-down");
		}
	}
	/** END ATTRIBUTE MOVE METHOD **/
	
	/** IS CRITICAL METHOD 
	 * Returns value of critical
	 * @return critical value
	 **/
	private double isCritical(Move move) {			
		/** CRITICAL HIT REFERENCE: https://www.serebii.net/games/criticalhits.shtml (GEN II-V) **/
		
		int chance = 2;
		if (move.getCrit() == 1) 
			chance = 4;
		
		Random r = new Random();		
		return (r.nextFloat() <= ((float) chance / 25)) ? 1.5 : 1;
	}
	/** END IS CRITICAL METHOD **/
	
	/** ABSORB HP METHOD 
	 * Increases attackers hp by damage dealt if move calls for it
	 * @param number of attacker, number of target, selected move, int damage
	 **/
	private void absorbHP(int atk, int trg, Move move, int damage) {
		
		if (move.getName() == "ABSORB" || move.getName() == "GIGA DRAIN") {
			
			int gainedHP = (damage / 2);
			
			// if attacker not at full health
			if (pokemon[atk].getHP() != pokemon[atk].getBHP()) {
				
				// if gained hp is greater than total hp
				if (gainedHP + pokemon[atk].getHP() > pokemon[atk].getBHP()) {
					
					// gained hp is set to amount need to hit hp limit									
					gainedHP = pokemon[atk].getBHP() - pokemon[atk].getHP();
					
					// refill hp to limit
					pokemon[atk].setHP(pokemon[atk].getBHP());
				}
				else 
					pokemon[atk].setHP(gainedHP + pokemon[atk].getHP()); 
				
				System.out.println(pokemon[atk].getName() + " absorbed " + gainedHP + " HP!");
			}
		}
	}
	/** END ABSORB HP METHOD **/
	
	/** APPLY EFFECT METHOD 
	 * Applys attribute or status effect if move causes it
	 * @param number of attacker, number of target, selected move
	 **/
	private void applyEffect(int atk, int trg, Move move) {
		
		// move causes attribute or status effect
		if (move.getProbability() != null) {								
										
			// chance for effect to apply
			if (new Random().nextDouble() <= move.getProbability()) {
				
				if (move.getStats() != null) 
					attributeMove(atk, trg, move);
				else {			
					// if not already affected by a status effect
					if (pokemon[trg].getStatus() == null) {
						pokemon[trg].setStatus(move.getEffect());
						
						System.out.println(pokemon[trg].getName() + " is " + 
							pokemon[trg].getStatus().getCondition() + "!");
					}
				}
			}							
		}
	}
	/** END APPLY EFFECT METHOD **/
	
	/** IS RECOIL METHOD 
	 * Inflicts damage on attacker if move inflicts recoil
	 * @param number of attacker, number of target, selected move, damage dealt
	 **/
	private void isRecoil(int atk, int trg, Move move, int damage) {
		
		if (move.getSelfInflict() != null) {								
			damage = (int)(damage * move.getSelfInflict());	
			
			System.out.println(pokemon[atk].getName() + " was hit with " + damage + " recoil damage!");		
			
			// damage is fatal to attacker
			if (damage >= pokemon[atk].getHP()) {
				dealDamage(atk, damage);
				defeated(trg, atk, damage);
			}					
			else
				dealDamage(atk, damage);
		}
	}
	/** END IS RECOIL METHOD **/
	
	/** CALCULATE DAMAGE DEALT METHOD
	 * Calculates the damage dealt to the attacker 
	 * @param number of attacker, number of target, selected move, critical damage, cpu true/false
	 * @return damage calculated
	 **/
	private int calculateDamage(int atk, int trg, Move move, double crit, boolean cpu) {
		
		double level = pokemon[atk].getLevel();		
		double power = (move.getPower() == -1) ? level : move.getPower();		
		double A = 1.0, D = 1.0, STAB = 1.0, type = 1.0;

		if (move.getMType().equals("Special")) {
			A = pokemon[atk].getSpAttack();
			D = pokemon[trg].getSpDefense();
		}
		else if (move.getMType().equals("Physical")) {
			A = pokemon[atk].getAttack();
			D = pokemon[trg].getDefense();
		}
		
		// if attacker has more than 1 type
		if (pokemon[atk].getTypes() != null) {	
			
			// cycle through each type of attacker
			for (Type t : pokemon[atk].getTypes()) {
				
				// if same type move
				if (move.getType() == t) {
					STAB = 1.5;
					break;
				}
			}
		}
		else
			STAB = move.getType() == pokemon[atk].getType() ? 1.5 : 1.0;

		type = effectiveness(trg, move.getType());	

		// damage formula reference: https://bulbapedia.bulbagarden.net/wiki/Damage (GEN IV)
		int damageDealt = (int)((Math.floor(((((Math.floor((2 * level) / 5)) + 2) * 
			power * (A / D)) / 50)) + 2) * crit * STAB * type);

		// keep damage dealt less than or equal to remaining HP
		if (damageDealt > pokemon[trg].getHP())
			damageDealt = pokemon[trg].getHP();
		
		// don't play sound if cpu is calling method
		if (!cpu) {
			// PLAY MOVE HIT SE
		}
		
		return damageDealt;
	}
	/** END CALCULATE DAMAGE DEALT METHOD **/	
	
	/** EFFECTIVENESS METHOD 
	 * Calculates effectiveness of moves type against target
	 * @param number of target, type of selected move
	 * @return effectiveness
	 **/
	private double effectiveness(int trg, Type type) {
		
		// default value
		double effect = 1.0;
		
		// if target is single types
		if (pokemon[trg].getTypes() == null) {
			
			// if vulnerable, retrieve and return vulnerable value		
			for (Type vulnType : pokemon[trg].getType().getVulnerability().keySet()) {		
				if (vulnType.getName().equals(type.getName())) {
					effect = pokemon[trg].getType().getVulnerability().get(vulnType);
					return effect;
				}
			}			
			// if resistant, retrieve and return resistance value
			for (Type resType : pokemon[trg].getType().getResistance().keySet()) {			
				if (resType.getName().equals(type.getName())) {
					effect = pokemon[trg].getType().getResistance().get(resType);
					return effect;
				}			
			}		
		}
		// if target is multi type
		else {
			
			// for each type in target
			for (Type targetType : pokemon[trg].getTypes()) {		
				
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
			if (effect == 0.75)	
				effect = 1;
		}			
						
		return effect;
	}
	/** END EFFECTIVENESS METHOD **/
	
	/** DEAL DAMAGE METHOD 
	 * Applies damage dealt to target's total HP
	 * @param number of target, damage dealt
	 **/
	private void dealDamage(int trg, int damage) {		
		
		// subtract damage dealt from total hp
		int result = pokemon[trg].getHP() - (int)damage;		
		
		// set HP to 0 if below 0
		if (result < 0)
			result = 0;
		
		pokemon[trg].setHP(result);
	}
	/** END DEAL DAMAGE METHOD **/
	
	/** POKEMON DEFEATED METHOD 
	 * Assigns xp gained and announces winner and loser
	 * @param winning pokemon, losing pokemon, damage dealt
	 **/
	private void defeated(int win, int lsr, int damageDealt) {
				
		pokemon[lsr].setAlive(false);
		this.setWinningPokemon(pokemon[win]);
		
		int xp = calculateXP(lsr);
		pokemon[win].setXP(pokemon[win].getBXP() + xp);
		
		gp.playSE(faint_SE, pokemon[lsr].getName());
		
		System.out.println(pokemon[lsr].getName() + " fainted!");			
		System.out.println(pokemon[win].getName() + " gained " + xp + " Exp. Points!");
		
		return;
	}
	/** END DEFEATED METHOD **/
	
	/** CALCULATE XP METHOD 
	 * Calculates xp earned based on level and existing xp
	 * @param number of loser
	 * @return gained xp
	 **/
	private int calculateXP(int lsr) {
		
		// exp formula reference (GEN I-IV): https://bulbapedia.bulbagarden.net/wiki/Experience		
		int exp = (int) (((( pokemon[lsr].getXP() * pokemon[lsr].getLevel() ) / 7)) * 1.5);		
		return exp;
	}
	/** END CALCULATE XP METHOD **/
	
	/** GET WINNER METHOD 
	 * Asks if a winner has been decided
	 * @return true if winner exists, false if not
	 **/
	public boolean hasWinner() {
		if (winningPokemon != null) return true;
		else return false;
	}
	/** END GET WINNER METHOD **/	
	
	/** GET MONEY METHOD 
	 * Calculates money earned (12 * level of loser)
	 * @param number of loser
	 * @return money earned
	 **/
	public int getMoney(int lsr) {		
		int money = 12 * pokemon[lsr].getLevel();		
		return money;
	}
	/** END GET MONEY METHOD **/
	
	public Pokemon getPokemon(int slot) {
		return pokemon[slot];
	}
	
	public String getDialogue() {
		return dialogue;
	}
	public void setDialogue(String dialogue) {
		this.dialogue = dialogue;
	}
}
/*** END BATTLE ENGINE CLASS ***/

@FunctionalInterface
interface StatusEffect {
	public int dealDamage(Pokemon pokemon);
}

@FunctionalInterface
interface Sleep {
	public boolean trackSleep(int counter, int limit);
}