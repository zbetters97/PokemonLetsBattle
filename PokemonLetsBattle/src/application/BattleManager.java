package application;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;

import moves.Move;
import moves.Moves;
import person.NPC;
import moves.Move.MoveType;
import pokemon.Pokemon;
import properties.Type;

public class BattleManager {
		
	// GENERAL VALUES
	private GamePanel gp;
	public NPC[] trainer = new NPC[2];
	public Pokemon[] fighter = new Pokemon[2];
	public Pokemon[] newFighter = new Pokemon[2];
	public Move move1, move2;
	public int winner = -1;
	public int loser = -1;
	private int hitCounter = -1;
	private BufferedImage current_arena;
		
	// TURN VALUES
	public boolean ready = false;
	public int currentTurn;
	private int nextTurn;
	private final int playerTurn = 0;
	private final int cpuTurn = 1;	
				
	// FIGHTER X/Y VALUES
	public int fighter_one_X;
	public int fighter_two_X;
	public int fighter_one_Y;
	public int fighter_two_Y;	
	private final int fighter_one_startX;
	private final int fighter_two_startX;
	private final int fighter_one_endX;
	private final int fighter_two_endX;	
	private final int fighter_one_startY;
	private final int fighter_two_startY;
	private final int fighter_one_platform_endX;
	private final int fighter_two_platform_endX;
	private final int fighter_one_platform_Y;
	private final int fighter_two_platform_Y;
	
	// SOUND LIBRARIES
	private final int cry_SE = 3;
	private final int faint_SE = 4;
	private final int moves_SE = 5;
	private final int battle_SE = 6;
	
	// BATTLE STATES
	public int battleMode;
	public final int wildBattle = 1;
	public final int trainerBattle = 2;
	public final int rivalBattle = 3;
	public final int gymBattle = 4;	
	public final int eliteBattle = 5;
	public final int championBattle = 6;
	public final int legendaryBattle = 7;
	
	// FIGHT STAGES
	public int fightStage;
	public final int fightStage_Encounter = 1;	
	public final int fightStage_Swap = 2;
	public final int fightStage_SwapOut = 3;
	public final int fightStage_Start = 4;
	public final int fightStage_Move = 5;
	public final int fightStage_Attack = 6;
	public final int fightStage_End = 7;
	public final int fightStage_KO = 8;		
	public final int fightStage_Defeat = 9;
	public final int fightStage_Victory = 10;
	public final int fightStage_Close = 11;
			
	// CONSTRUCTOR
	public BattleManager(GamePanel gp) {
		this.gp = gp;
		
		fighter_one_startX = gp.tileSize * 14;
		fighter_two_startX = 0 - gp.tileSize * 3;
		fighter_one_startY = (int) (gp.tileSize * 3.8);
		fighter_two_startY = 0;
		
		fighter_one_X = fighter_one_startX;
		fighter_two_X = fighter_two_startX;
		fighter_one_Y = fighter_one_startY;
		fighter_two_Y = fighter_two_startY;
		
		fighter_one_platform_Y = fighter_one_Y + gp.tileSize * 4;
		fighter_two_platform_Y = (int) (fighter_two_Y + gp.tileSize * 2.3);
		
		fighter_one_endX = gp.tileSize * 2;
		fighter_two_endX = gp.tileSize * 9;
		fighter_one_platform_endX = fighter_one_endX - gp.tileSize;
		fighter_two_platform_endX = fighter_two_endX - gp.tileSize;		
		
		current_arena = setup("/ui/arenas/grass", gp.tileSize * 7, gp.tileSize * 3);
	}
	
	// SETUP METHODS
	public void setBattle(int currentBattle) {
		
		/*		
		trainer[0] = new NPC(gp);
		trainer[0].name = "ASH";
		trainer[0].pokeParty.add(Pokemon.getPokemon(3));
		trainer[0].pokeParty.add(Pokemon.getPokemon(4));
		trainer[0].pokeParty.add(Pokemon.getPokemon(39));
		*/
						
		trainer[0] = gp.player;
		newFighter[0] = trainer[0].pokeParty.get(0);
			
		/*
		trainer[1] = new NPC(gp);
		trainer[1].name = "RED";
		trainer[1].pokeParty.add(Pokemon.getPokemon(37));
		trainer[1].pokeParty.add(Pokemon.getPokemon(38));
		trainer[1].pokeParty.add(Pokemon.getPokemon(5));
		*/
				
		battleMode = currentBattle;
		fightStage = fightStage_Encounter;
		
		gp.stopMusic();		
		getBattleMode();
	}
	private void getBattleMode() {
		
		if (battleMode == wildBattle) {
			
			fighter[1] = trainer[1].pokeParty.get(0);	
			gp.ui.addBattleDialogue("A wild " + fighter[1].getName() + "\nappeared!");

			gp.ui.setSoundFile(cry_SE, fighter[1].getName(), 30, 160);		
			gp.playMusic(1, 0);					
		}
		else if (battleMode == trainerBattle) {		

			gp.ui.addBattleDialogue("Trainer " + trainer[1].name + "\nwould like to battle!");				
			newFighter[1] = trainer[1].pokeParty.get(0);
			
			gp.playMusic(1, 1);
		}
		else if (battleMode == rivalBattle) {					
			
		}
		else if (battleMode == gymBattle) {					
			
		}
		else if (battleMode == eliteBattle) {			
			
		}
		else if (battleMode == championBattle) {			
			
		}
		else if (battleMode == legendaryBattle) {
			
		}
	}
	private void setHP() {
		
		gp.ui.fighter_one_HP = fighter[0].getHP();
		gp.ui.fighter_two_HP = fighter[1].getHP();
		
		if (fighter[0].getHP() > 100) gp.ui.hpSpeed_one = 1;
		else if (fighter[0].getHP() > 50) gp.ui.hpSpeed_one = 2;
		else gp.ui.hpSpeed_one = 3;
		
		if (fighter[1].getHP() > 100) gp.ui.hpSpeed_two = 1;
		else if (fighter[1].getHP() > 50) gp.ui.hpSpeed_two = 2;
		else gp.ui.hpSpeed_two = 3;
	}
	
	// UPDATE METHOD
	public void update() {	
		
		if (ready) {
			
			if (fightStage == fightStage_Encounter) {
				gp.ui.battleSubState = gp.ui.battle_Dialogue;
				fightStage = fightStage_Swap;
			}	
			else if (fightStage == fightStage_Swap) {
				swapFighters();
			}
			else if (fightStage == fightStage_SwapOut) {				
				setSwap();					
			}
			else if (fightStage == fightStage_Start) {
				checkTurn();
			}
			else if (fightStage == fightStage_Move) {
				startMove();				
			}
			else if (fightStage == fightStage_Attack) {
				attack();
			}
			else if (fightStage == fightStage_End) {
				checkStatusDamage();								
			}
			else if (fightStage == fightStage_Victory) {
				setVictory();
			}
			else if (fightStage == fightStage_Close) {
				gp.stopMusic();
				gp.setupMusic();
				fightStage = 0;
				gp.gameState = gp.playState;
			}
			
		}
		
		ready = false;
	}
	
	// SWAP POKEMON METHODS
	private void swapFighters() {
		
		// WINNER NOT YET DECIDED
		if (winner == -1) {
			
			// TRAINER 2 FORCE SWAP OUT
			if (fighter[1] == null || !fighter[1].isAlive()) {		
												
				fighter[1] = newFighter[1];
				
				gp.ui.addBattleDialogue("Trainer " + trainer[1].name + "\nsent out " + fighter[1].getName() + "!");
				gp.ui.setSoundFile(cry_SE, fighter[1].getName(), 15, 120);	
				
				fightStage = fightStage_SwapOut;
			}		
			// TRAINER 1 FORCE SWAP OUT
			if ((fighter[0] == null || !fighter[0].isAlive()) ||
					(newFighter[0] != null && newFighter[1] != null)) {	
				
				fighter[0] = newFighter[0];
				
				gp.ui.addBattleDialogue("GO, " + fighter[0].getName() + "!");
				gp.ui.setSoundFile(cry_SE, fighter[0].getName(), 15, 120);				
								
				fightStage = fightStage_SwapOut;
			}	
			// TRAINER 1 SWAP OUT (SAME TRAINER 2 FIGHTER)
			else if (newFighter[0] != null && newFighter[1] == null) {
				
				fighter[0] = newFighter[0];
				
				gp.ui.addBattleDialogue("GO, " + fighter[0].getName() + "!");
				gp.ui.setSoundFile(cry_SE, fighter[0].getName(), 30, 120);		
				
				fightStage = fightStage_Start;
			}			

			newFighter[0] = null;
			newFighter[1] = null;
			
			setHP();	
			
			fighter_one_Y = fighter_one_startY;
			fighter_two_Y = fighter_two_startY;			
		}		
		// TRAINER HAS WON
		else {					
			getWinningTrainer();
		}
	}
	private void getWinningTrainer() {
		
		if (winner == 0) {
			
			// GET NEW FIGHTER
			newFighter[1] = cpuSelectNextFighter();	
			
			// TRAINER 2 HAS MORE POKEMON
			if (newFighter[1] != null) {
				if (trainer[0].getAvailablePokemon() > 1) {
					gp.ui.addBattleDialogue("Trainer " + trainer[1].name + " is about\nto sent out " + newFighter[1].getName() + "!");
				}
				fightStage = fightStage_SwapOut;	
			}
			// TRAINER 2 OUT OF POKEMON
			else {				
				fighter_two_X = gp.screenWidth + gp.tileSize;
				fighter_two_Y = fighter_two_startY;
				
				gp.ui.addBattleDialogue("Player defeated\nTrainer " + trainer[1].name + "!");
				
				gp.stopMusic();
				gp.playMusic(1, 0);
				
				fightStage = fightStage_Defeat;
			}	
		}
		else if (winner == 1) {
			
			// TRAINER 1 HAS MORE POKEMON
			if (trainer[0].hasPokemon()) {				
				winner = -1;
				fightStage = fightStage_Swap;
				gp.gameState = gp.partyState;
				gp.ui.partySubState = gp.ui.party_Main;
			}
			// TRAINER 1 OUT OF POKEMON
			else {
				fightStage = fightStage_Victory;
			}		
		}				
	}
	public boolean swapPokemon(int partySlot) {
		
		Pokemon p = trainer[0].pokeParty.get(partySlot);
		
		if (fighter[0] == p) {
			return false;
		}
		
		if (p.isAlive()) {
			
			newFighter[0] = p;
						
			if (trainer[0].pokeParty.size() > 1) {
				Collections.swap(trainer[0].pokeParty, 0, partySlot);	
			}
						
			return true;
		}
		else {
			return false;
		}		
	}
	private Pokemon cpuSelectNextFighter() {
		
		if (fighter[0] == null) return trainer[1].pokeParty.get(0);
		
		int available = 0;
		
		// list to hold all candidates based on type effectiveness
		Map<Pokemon, Integer> pokemonList = new HashMap<>();
		
		for (Pokemon p : trainer[1].pokeParty) {
			if (p.isAlive()) {
				available++;
			}
		}
		
		// if more than 1 pokemon in CPU party
		if (available > 1) {
			
			// loop through each pokemon in party
			for (Pokemon p : trainer[1].pokeParty) {
				
				if (p.isAlive()) {
				
					// if party is single type
					if (p.getTypes() == null) {
												
						// if target is single type
						if (fighter[0].getTypes() == null) {	
							
							// loop through each type in target pokemon
							for (Type vulnType : fighter[0].getType().getVulnerability().keySet()) {	
								
								// if type matches target's vulnerability
								if (vulnType.getName().equals(p.getType().getName()))
									pokemonList.put(p, p.getLevel());
							}
						}					
						// if target is multi type
						else {			
							
							// for each type in target
							for (Type type : fighter[0].getTypes()) {
								
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
						if (fighter[0].getTypes() == null) {	
							
							// for each type in party
							for (Type type : p.getTypes()) {
								
								// loop through each vulnerability in target pokemon
								for (Type vulnType : fighter[0].getType().getVulnerability().keySet()) {	
								
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
								for (Type tarType : fighter[0].getTypes()) {
									
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
			for (Pokemon p : trainer[1].pokeParty) {
				
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
			for (Pokemon p : trainer[1].pokeParty) {
				pokemonList.put(p, p.getLevel());
			}
			
			bestPokemon = Collections.max(pokemonList.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey();
			return bestPokemon;
		}
	}
	private void setSwap() {
		
		// POKEMON ALREADY SWAPPED OUT
		if (winner == -1) {			
			fightStage = fightStage_Start;
			gp.ui.battleSubState = gp.ui.battle_Options;	
		}
		// INITIATE SWAP OUT
		else {								
			fightStage = fightStage_Swap;
			
			if (trainer[0].getAvailablePokemon() > 1) {
				gp.ui.battleSubState = gp.ui.battle_Swap;	
			}
			
			winner = -1;
		}		
	}
	
	// SELECT MOVE METHODS
	public void setPlayerMove(int selection) {
		move1 = fighter[0].getMoveSet().get(selection);
	}
	private Move cpuSelectMove() {
		
		// holds Map of Move and Damage Points
		Map<Move, Integer> moves = new HashMap<>();
		
		// for each move in attacker's move set
		for (Move move : fighter[1].getMoveSet()) {
			
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
				int ranMove = (int)(Math.random() * (fighter[1].getMoveSet().size()));				
				bestMove = fighter[1].getMoveSet().get(ranMove);
			}
			else
				bestMove = Collections.max(moves.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey(); 	
		}
		// if list is empty, select random move
		else {
			int ranMove = (int)(Math.random() * (fighter[1].getMoveSet().size()));				
			bestMove = fighter[1].getMoveSet().get(ranMove);
		}
		
		return bestMove;	
	}
			
	// CHECK TURN METHODS
	private void checkTurn() {
						
		// GET CPU MOVE IF NO CPU DELAY
		int delay = getDelayedMove();
		
		if (delay == 0 || delay == 1) {
			move2 = cpuSelectMove();
		}
		
		// SET ORDER OF TURNS
		setRotation();
		
		// INITIATE MOVE
		if (canMove()) {
			fightStage = fightStage_Move;
		}
		// SKIP CURRENT TURN
		else {
			currentTurn = nextTurn;
			nextTurn = -1;
			fightStage = fightStage_Move;
		}	
	}	
	private void setRotation() {		
		
		// if both pokemon are alive
		if (fighter[0].isAlive() && fighter[1].isAlive()) {
			
			// 1 if trainer 1 moves first, 2 if trainer 2 moves first
			// 3 if only trainer 2, 4 if only trainer 1, 5 if neither
			int numTurn = getFirst();	

			if (numTurn == 1) { 
				currentTurn = playerTurn;
				nextTurn = cpuTurn;
			}	
			else if (numTurn == 2) { 
				currentTurn = cpuTurn;
				nextTurn = playerTurn;
			}				
			else if (numTurn == 3) { 
				currentTurn = cpuTurn;
				nextTurn = -1;
			}				
			else if (numTurn == 4) { 
				currentTurn = playerTurn;
				nextTurn = -1;
			}
			else if (numTurn == 5) {
				currentTurn = -1;
				nextTurn = -1;
			}
		}
	}
	private int getFirst() {
		
		if (move1 == null && move2 == null) {
			return 5;
		}
		else if (move1 == null) {
			return 3;
		}
		else if (move2 == null) {
			return 4;
		}
		else {		
			// if both moves go first (EX: Quick Attack)
			if (move1.getGoFirst() && move2.getGoFirst()) {			
				
				// if fighter_one is faster (fighter_one has advantage if equal)
				if (fighter[0].getSpeed() >= fighter[1].getSpeed()) 
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
				if (fighter[0].getSpeed() > fighter[1].getSpeed())
					return 1;
				// if both pokemon have equal speed, coin flip decides
				else if (fighter[0].getSpeed() == fighter[1].getSpeed()) {
					Random r = new Random();
					return (r.nextFloat() <= ((float) 1 / 2)) ? 1 : 2;
				}
				else
					return 2;
			}
		}
	}
	public int getDelayedMove() {		
		
		// 3 if both, 1 if player 1, 2 if player 2, 0 if neither
		
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
	private boolean canMove() {
		
		boolean canMove = true;
		
		// if attacker has status effect
		if (fighter[currentTurn].getStatus() != null) {
		
			// check which status
			switch (fighter[currentTurn].getStatus().getAbreviation()) {			
				case "PAR":	canMove = paralyzed(currentTurn); break;					
				case "FRZ": canMove = frozen(currentTurn); break;			
				case "SLP": canMove = asleep(currentTurn); break;
				case "CNF": canMove = confused(currentTurn); break;
			}
		}	
		
		return canMove;
	}
	private boolean paralyzed(int atk) {
		
		// 1/4 chance can't move due to PAR
		int val = 1 + (int)(Math.random() * 4);
		if (val == 1) {		
			fighter[atk].getStatus().printStatus(gp, fighter[atk].getName());
			return false;
		} 
		else {
			return true;
		}
	}
	private boolean frozen(int atk) {
		
		// 1/4 chance attacker can thaw from ice
		int val = 1 + (int)(Math.random() * 4);
		if (val == 1) {
			gp.ui.addBattleDialogue(fighter[atk].getName() + "" + fighter[atk].getStatus().printRecover());
			fighter[atk].setStatus(null);
			return true;
		}
		else {	
			fighter[atk].getStatus().printStatus(gp, fighter[atk].getName());
			return false;		
		}
	}
	private boolean asleep(int atk) {
		
		// if number of moves under condition hit limit, remove condition
		if (recoverCondition(atk)) {			
			return true;
		}
		// pokemon still under status condition
		else {
			
			// increase counter
			fighter[atk].setStatusCounter(fighter[atk].getStatusCounter() + 1);
			fighter[atk].getStatus().printStatus(gp, fighter[atk].getName());
			
			return false;
		}
	}
	private boolean confused(int atk) {
		
		// if number of moves under condition hit limit, remove condition
		if (recoverCondition(atk)) {			
			return true;
		}
		// pokemon still under status condition
		else {
			
			// increase counter
			fighter[atk].setStatusCounter(fighter[atk].getStatusCounter() + 1);
									
			// if pokemon hurt itself in confusion
			if (confusionDamage(atk)) {
				return false;
			}
			else { 
				return true;
			}			
		}
	}
	private boolean confusionDamage(int atk) {

		// 1/2 chance of hurting self
		int val = 1 + (int)(Math.random() * 2);	
				
		if (val == 1) {					
			
			double level = fighter[atk].getLevel();
			double power = 1.0;
			double A = fighter[atk].getAttack();
			double D = fighter[atk].getDefense();
					
			// confusion damage reference: https://fighterlp.fandom.com/wiki/Confusion_(status)
			int damage = (int)((Math.floor(((((Math.floor((2 * level) / 5)) + 2) * 
				power * (A / D)) / 50)) + 2));
		
			int hp = fighter[atk].getHP() - damage;
			
			// pokemon defeated itself in confusion damage
			if (hp <= 0) {
				hp = 0;						
			}
			
			fighter[atk].setHP(hp);					
			fighter[atk].getStatus().printStatus(gp, fighter[atk].getName());
			
			return true;	
		}
		else {
			return false;
		}
	}
	private boolean recoverCondition(int atk) {
		
		// if first move under condition, set number of moves until free (1-5)
		if (fighter[atk].getStatusLimit() == 0) {
			fighter[atk].setStatusLimit((int)(Math.random() * 5));	
		}
		
		// if number of moves under condition hit limit, remove condition
		if (fighter[atk].getStatusCounter() >= fighter[atk].getStatusLimit()) {
									
			gp.ui.addBattleDialogue(fighter[atk].getName() + fighter[atk].getStatus().printRecover());	
			gp.ui.setDummyFile();
		
			fighter[atk].setStatusCounter(0); fighter[atk].setStatusLimit(0);
			fighter[atk].setStatus(null);
			
			return true;
		}
		else {
			return false;
		}
	}
	
	// START MOVE METHODS
	private void startMove() {	

		if (currentTurn != -1) {		

			if (canMove()) {					
				if (currentTurn == playerTurn) {
					useMove(0, 1, move1, move2);	
				}
				else if (currentTurn == cpuTurn) {
					useMove(1, 0, move2, move1);
				}					
			}
			else {
				currentTurn = nextTurn;
				nextTurn = -1;
			}
		}
		else {		
			
			// RESET NON-DELAYED MOVES
			int delay = getDelayedMove();			
			if (delay == 0) { move1 = null; move2 = null; }
			else if (delay == 1) move2 = null;			
			else if (delay == 2) move1 = null;					
			
			fightStage = fightStage_End;				
		}			
	}	
	private void useMove(int atk, int trg, Move atkMove, Move trgMove) {		
				
		gp.ui.addBattleDialogue(fighter[atk].toString() + " used\n" + atkMove.toString() + "!"); 		
		
		// if not delayed move or delayed move is ready
		if (1 >= atkMove.getTurns()) {	
			
			gp.ui.setSoundFile(moves_SE, atkMove.getName(), 45);
			
			// reset turns to wait
			atkMove.setTurns(atkMove.getNumTurns());
			
			// decrease move pp
			atkMove.setpp(atkMove.getpp() - 1);
			
			fightStage = fightStage_Attack;
		}
		// delayed move is used for first time
		else if (atkMove.getTurns() == atkMove.getNumTurns()) {
						
			gp.ui.addBattleDialogue(atkMove.getDelay(fighter[atk].getName()));	
									
			// reduce number of turns to wait
			atkMove.setTurns(atkMove.getTurns() - 1);	
			
			currentTurn = nextTurn;	
			nextTurn = -1;
			fightStage = fightStage_Move;
		}		
	}		
	
	// ATTACK METHOD
	public void attack() {
		
		int atk = 0;
		int trg = 0;
		
		Move move = null;
		Move trgMove = null;
		
		if (currentTurn == playerTurn) {
			atk = 0;
			trg = 1;
			move = move1;
			trgMove = move2;
		}
		else if (currentTurn == cpuTurn){
			atk = 1;
			trg = 0;
			move = move2;
			trgMove = move1;
		}
		
        // if attack lands
		if (hit(atk, move, trgMove)) {
			
			// move has a status affect
			if (move.getMType().equals(MoveType.STATUS)) {
				statusMove(trg, move);		
				currentTurn = nextTurn;	
				nextTurn = -1;
			}
			
			// move has an attribute affect
			else if (move.getMType().equals(MoveType.ATTRIBUTE)) {
				attributeMove(atk, trg, move);
				currentTurn = nextTurn;	
				nextTurn = -1;
			}
			
			// move is in other category
			else if (move.getMType().equals(MoveType.OTHER)) {
				
				switch (move.getName()) {
					case "Teleport":
						break;						
					default:
						break;
				}		
			}
			// move is damage-dealing
			else {								
				damageMove(atk, trg, move);
			}				
		}
		// attack missed
		else {
			gp.ui.addBattleDialogue("The attack missed!");
			currentTurn = nextTurn;	
			nextTurn = -1;
		}			
		
		fightStage = fightStage_Move;
	}		
	private boolean hit(int atk, Move move, Move trgMove) {
				
		if (trgMove == null)
			return true;
		
		// if target used delayed move and delayed move protects target		
		if (trgMove.getTurns() == 1 && !trgMove.getIsProtected())
			return false;
				
		// if move never misses, return true
		if (move.getAccuracy() == -1) 
			return true; 
		
		double accuracy = move.getAccuracy() * fighter[atk].getAccuracy();
		
		Random r = new Random();
		float chance = r.nextFloat();
		
		// chance of missing is accuracy value / 100
		return (chance <= ((float) accuracy / 100)) ? true : false;
	}
	
	// MOVE TYPE METHODS
	private void statusMove(int trg, Move move) {
		
		// if pokemon does not already have status affect
		if (fighter[trg].getStatus() == null) {
			
			fighter[trg].setStatus(move.getEffect());	
			
			gp.ui.setSoundFile(battle_SE, fighter[trg].getStatus().getCondition(), 5);
			gp.ui.addBattleDialogue(fighter[trg].getName() + " is\n" + 
					fighter[trg].getStatus().getCondition() + "!");
		}
		// pokemon already has status affect
		else {
			gp.ui.addBattleDialogue(fighter[trg].getName() + " is\nalready " + 
					fighter[trg].getStatus().getCondition() + "!");
		}
	}
	private void attributeMove(int atk, int trg, Move move) {
		
		// if move changes self attributes
		if (move.isToSelf()) {
			
			// loop through each specified attribute to be changed
			for (String stat : move.getStats()) 
				gp.ui.addBattleDialogue(fighter[atk].changeStat(stat, move.getLevel()));	
		}
		// if move changes target attributes
		else {
			// loop through each specified attribute to be changed
			for (String stat : move.getStats()) 
				gp.ui.addBattleDialogue(fighter[trg].changeStat(stat, move.getLevel()));	
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
	private void damageMove(int atk, int trg, Move move) {
		
		// get critical damage (1 or 1.5)
		double crit = isCritical(move);
		int damage = 1;
		
		// logic for seismic toss
		if (move.getPower() == -1) {
			 damage = fighter[atk].getLevel();
		}
		else {
			damage = calculateDamage(atk, trg, move, crit, false);
		}
	
		// no damage dealt
		if (damage == 0) {
			gp.ui.addBattleDialogue("It had no effect!");
		}
		else {				
			
			// if critical hit
			if (crit == 1.5) {
				gp.ui.addBattleDialogue("A critical hit!");
			}
		
			gp.ui.addBattleDialogue(fighter[trg].getName() + " took\n" + damage + " damage!");	
			fighter[trg].isHit = true;			
			
			absorbHP(atk, trg, move, damage);
			getRecoil(atk, trg, move, damage);
			dealDamage(atk, trg, move, damage);
		}	
	}	
	
	// CALCULATION METHODS
	private double isCritical(Move move) {			
		/** CRITICAL HIT REFERENCE: https://www.serebii.net/games/criticalhits.shtml (GEN II-V) **/
		
		int chance = 2;
		if (move.getCrit() == 1) 
			chance = 4;
		
		Random r = new Random();		
		return (r.nextFloat() <= ((float) chance / 25)) ? 1.5 : 1;
	}
	private int calculateDamage(int atk, int trg, Move move, double crit, boolean cpu) {
		
		double level = fighter[atk].getLevel();		
		double power = (move.getPower() == -1) ? level : move.getPower();		
		double A = 1.0, D = 1.0, STAB = 1.0, type = 1.0;

		if (move.getMType().equals(MoveType.SPECIAL)) {
			A = fighter[atk].getSpAttack();
			D = fighter[trg].getSpDefense();
		}
		else if (move.getMType().equals(MoveType.PHYSICAL)) {
			A = fighter[atk].getAttack();
			D = fighter[trg].getDefense();
		}
		
		// if attacker has more than 1 type
		if (fighter[atk].getTypes() != null) {	
			
			// cycle through each type of attacker
			for (Type t : fighter[atk].getTypes()) {
				
				// if same type move
				if (move.getType() == t) {
					STAB = 1.5;
					break;
				}
			}
		}
		else
			STAB = move.getType() == fighter[atk].getType() ? 1.5 : 1.0;

		type = effectiveness(trg, move.getType());	

		// damage formula reference: https://bulbapedia.bulbagarden.net/wiki/Damage (GEN IV)
		int damageDealt = (int)((Math.floor(((((Math.floor((2 * level) / 5)) + 2) * 
			power * (A / D)) / 50)) + 2) * crit * STAB * type);

		// keep damage dealt less than or equal to remaining HP
		if (damageDealt > fighter[trg].getHP())
			damageDealt = fighter[trg].getHP();
		
		// don't play sound if cpu is calling method
		if (!cpu) {
			gp.ui.setSoundFile(battle_SE, getHitSE(type), 5, 90);
		}
		
		return damageDealt;
	}
	private double effectiveness(int trg, Type type) {
		
		// default value
		double effect = 1.0;
		
		// if target is single types
		if (fighter[trg].getTypes() == null) {
			
			// if vulnerable, retrieve and return vulnerable value		
			for (Type vulnType : fighter[trg].getType().getVulnerability().keySet()) {		
				if (vulnType.getName().equals(type.getName())) {
					effect = fighter[trg].getType().getVulnerability().get(vulnType);
					return effect;
				}
			}			
			// if resistant, retrieve and return resistance value
			for (Type resType : fighter[trg].getType().getResistance().keySet()) {			
				if (resType.getName().equals(type.getName())) {
					effect = fighter[trg].getType().getResistance().get(resType);
					return effect;
				}			
			}		
		}
		// if target is multi type
		else {
			
			// for each type in target
			for (Type targetType : fighter[trg].getTypes()) {		
				
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
	private String getHitSE(double effectiveness) {
		
		String hit = "";
		
		switch (Double.toString(effectiveness)) {
			case "0.25": hit = "hit-weak"; break;			
			case "0.5": hit = "hit-weak"; break;
			case "1.0": hit = "hit-normal"; break;
			case "1.5": hit = "hit-super"; break;
			case "2.25": hit = "hit-super"; break;			
			default: hit = "hit-normal"; break;
		}
		
		if (effectiveness == 1.5 || effectiveness == 2.25) 
			gp.ui.addBattleDialogue("It's super effective!");
		else if (effectiveness == 0.25 || effectiveness == 0.5)
			gp.ui.addBattleDialogue("It's not very effective...");
		else if (effectiveness == 0) 
			gp.ui.addBattleDialogue("It has no effect!");
		
		return hit;
	}
	
	// POST MOVE METHODS
	private void absorbHP(int atk, int trg, Move move, int damage) {
		
		if (move.getName().equals(Moves.ABSORB.getName()) || 
				move.getName().equals(Moves.GIGADRAIN.getName())) {
			
			int gainedHP = (damage / 2);
			
			// if attacker not at full health
			if (fighter[atk].getHP() != fighter[atk].getBHP()) {
				
				// if gained hp is greater than total hp
				if (gainedHP + fighter[atk].getHP() > fighter[atk].getBHP()) {
					
					// gained hp is set to amount need to hit hp limit									
					gainedHP = fighter[atk].getBHP() - fighter[atk].getHP();
					
					// refill hp to limit
					fighter[atk].setHP(fighter[atk].getBHP());
				}
				else 
					fighter[atk].setHP(gainedHP + fighter[atk].getHP()); 
				
				gp.ui.addBattleDialogue(fighter[atk].getName() + "\nabsorbed " + gainedHP + " HP!");
			}
		}
	}
	private void getRecoil(int atk, int trg, Move move, int damage) {
		
		if (move.getSelfInflict() != 0.0) {	
			
			damage = (int)(Math.ceil(damage * move.getSelfInflict()));	

			// subtract damage dealt from total HP
			int result = fighter[atk].getHP() - (int)damage;		

			// set HP to 0 if below 0
			if (result <= 0) {
				result = 0;
				currentTurn = -1;
				nextTurn = -1;		
			}
			
			fighter[atk].setHP(result);
			gp.ui.addBattleDialogue(fighter[atk].getName() + " was hit\nwith recoil damage!");	
		}
	}
	private void dealDamage(int atk, int trg, Move move, int damage) {		
		
		// subtract damage dealt from total hp
		int result = fighter[trg].getHP() - (int)damage;		
		
		// set HP to 0 if below 0
		if (result <= 0) {
			result = 0;
			currentTurn = -1;
			nextTurn = -1;		
		}
		else {
			applyEffect(atk, trg, move);
			if (nextTurn != -1) {
				getFlinch(trg, move);
			}
			else {
				currentTurn = nextTurn;	
				nextTurn = -1;	
			}			
		}
		
		fighter[trg].setHP(result);
	}
	private void applyEffect(int atk, int trg, Move move) {
		
		// move causes attribute or status effect
		if (move.getProbability() != 0.0) {								
										
			// chance for effect to apply
			if (new Random().nextDouble() <= move.getProbability()) {
				
				if (move.getStats() != null) {
					attributeMove(atk, trg, move);
				}
				else {			
					// if not already affected by a status effect
					if (fighter[trg].getStatus() == null) {
						fighter[trg].setStatus(move.getEffect());
						
						gp.ui.addBattleDialogue(fighter[trg].getName() + " is\n" + 
							fighter[trg].getStatus().getCondition() + "!");
					}
				}
			}							
		}
	}
	private void getFlinch(int trg, Move move) {
		
		if (move.getFlinch() != 0.0) {
			
			// chance for move to cause flinch
			if (new Random().nextDouble() <= move.getFlinch()) {
				gp.ui.addBattleDialogue(fighter[trg].getName() + " flinched!");
				
				currentTurn = -1;	
				nextTurn = -1;	
				
				return;
			}
		}
	
		currentTurn = nextTurn;	
		nextTurn = -1;		
	}
	
	// STATUS METHODS
	private void checkStatusDamage() {
						
		// STATUS CONDITIONS CHECKED		
		if (nextTurn == 1) {	
			
			nextTurn = -1;
			
			if (getDelayedMove() == 1 || getDelayedMove() == 3) {				
				fightStage = fightStage_Start;
			}
			else {
				fightStage = fightStage_Start;
				gp.ui.battleSubState = gp.ui.battle_Options;		
			}
		}
		// CHECK BOTH STATUS CONDITIONS
		else {		
			nextTurn++;
			
			if (hasWinningPokemon()) {
				nextTurn = -1;
				getWinningPokemon();	
			}
			else {
				if (fighter[nextTurn].isAlive()) {
					getStatusDamage(nextTurn);
				}	
				if (hasWinningPokemon()) {
					nextTurn = -1;
					getWinningPokemon();	
				}
				else {
					fightStage = fightStage_End;
				}
			}
		}
	}
	private void getStatusDamage(int atk) {
		
		if (fighter[atk].getStatus() != null) {				
			
			if (fighter[atk].getStatus().getAbreviation().equals("PSN") || 
					fighter[atk].getStatus().getAbreviation().equals("BRN")) {
				
				// status effects reference: https://pokemon.fandom.com/wiki/Status_Effects			
				int damage = (int) Math.ceil((fighter[atk].getHP() * 0.16));
				int newHP = fighter[atk].getHP() - damage;		
				
				if (newHP <= 0) {
					newHP = 0;
				}
				
				fighter[atk].getStatus().printStatus(gp, fighter[atk].getName());				
				fighter[atk].setHP(newHP);	
			}		
		}	
	}
	
	// GET WINNER METHODS
	private boolean hasWinningPokemon() {
		
		for (int i = 0; i <= 1; i++) {
			if (fighter[i].getHP() <= 0) {
				fighter[i].setHP(0);
				fighter[i].setAlive(false);
			}
		}	
		
		// TIE
		if (!fighter[0].isAlive() && !fighter[1].isAlive()) {
			winner = 2;
			loser = 2;
			return true;
		}			
		// PLAYER 2 WINS
		else if (!fighter[0].isAlive()) {	
			winner = 1;
			loser = 0;
			return true;
		}
		// PLAYER 1 WINS
		else if (!fighter[1].isAlive()) {
			winner = 0;
			loser = 1;
			return true;
		}
		else {
			return false;
		}
	}
	public void getWinningPokemon() {
		
		// TIE GAME
		if (winner == 2) {
			gp.ui.addBattleDialogue(fighter[0].getName() + " fainted!");
			gp.ui.addBattleDialogue(fighter[1].getName() + " fainted!");
			gp.ui.addBattleDialogue("It's a draw!");

			fightStage = fightStage_Swap;
		}
		else {
			int xp = calculateXP(loser);
			fighter[winner].setXP(fighter[winner].getBXP() + xp);
			
			gp.ui.setSoundFile(faint_SE, fighter[loser].getName(), 5);
			
			gp.ui.addBattleDialogue(fighter[loser].getName() + " fainted!");			
			gp.ui.addBattleDialogue(fighter[winner].getName() + " gained\n" + xp + " Exp. Points!");	
			
			fightStage = fightStage_KO;			
		}
	}
	private int calculateXP(int lsr) {
		// exp formula reference (GEN I-IV): https://bulbapedia.bulbagarden.net/wiki/Experience		
		
		int exp = (int) (((( fighter[lsr].getXP() * fighter[lsr].getLevel() ) / 7)) * 1.5);	
		
		return exp;
	}	
	
	private void setVictory() {
		
		// TRAINER 1 WINNER
		if (winner == 0) {
			
			// WILD BATTLE
			if (battleMode == wildBattle || battleMode == legendaryBattle) {
				fightStage = 0;
				gp.gameState = gp.playState;
			}			
			// TRAINER BATTLE
			else {			
				trainer[loser].isDefeated = true;
				trainer[loser].hasBattle = false;
				int dialogueSet = trainer[loser].dialogueSet + 1;
				gp.ui.addBattleDialogue(trainer[loser].dialogues[dialogueSet][0]);
				gp.ui.addBattleDialogue(trainer[winner].name + " got $" + getMoney() + "\nfor winning!" );								
				
				fightStage = fightStage_Close;
			}	
		}
		// TRAINER 2 WINNER
		else {
			// WILD BATTLE
			if (battleMode == wildBattle || battleMode == legendaryBattle) {				
			}			
			// TRAINER BATTLE
			else {							
			}	
		}
	}
	private int getMoney() {
		// money earned formula reference (ALL GEN): https://bulbapedia.bulbagarden.net/wiki/Prize_money
		
		int level = trainer[loser].pokeParty.get(trainer[loser].pokeParty.size() - 1).getLevel();
		int base = trainer[loser].trainerClass;		
		int payout = base * level;
		
		return payout;
	}
	
	// DRAW METHODS
	public void draw(Graphics2D g2) {
		
		g2.setColor(new Color(234,233,246));  
		g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
		
		if (fightStage == fightStage_Encounter) {
			animateEntrance(g2);
		}
		else if (fightStage == fightStage_KO) {			
			animateFighterDefeat();
			drawFighters(g2);		
		}
		else if (fightStage == fightStage_Defeat || fightStage == fightStage_Victory) {
			animateTrainerDefeat(g2);
		}
		else if (fightStage == fightStage_Close) {
			drawTrainerDefeat(g2);
		}
		else {
			drawFighters(g2);	
		}		
	}	
	private void animateEntrance(Graphics2D g2) {
		
		int x; 
		int y; 
		
		x = fighter_two_X - gp.tileSize;
		y = (int) (fighter_two_Y + gp.tileSize * 2.3);
		g2.drawImage(current_arena, x, y, null);	
		
		if (battleMode == wildBattle || battleMode == legendaryBattle) {
			g2.drawImage(fighter[1].getFrontSprite(), fighter_two_X, fighter_two_Y + 20, null);
		}
		else {
			g2.drawImage(trainer[1].frontSprite, fighter_two_X + 25, fighter_two_Y, null);
		}
		
		x = fighter_one_X - gp.tileSize;
		y = fighter_one_Y + gp.tileSize * 4;
		g2.drawImage(current_arena, x, y, null);
		g2.drawImage(trainer[0].backSprite, fighter_one_X + 30, fighter_one_Y + 40, null);		
		
		if (fighter_one_X > fighter_one_endX && fighter_two_X < fighter_two_endX) {
			fighter_one_X -= 6;	
			fighter_two_X += 6;	
		}
		else {
			gp.ui.battleSubState = gp.ui.battle_Start;
		}
	}	
	private void drawFighters(Graphics2D g2) {					
		g2.drawImage(current_arena, fighter_one_platform_endX, fighter_one_platform_Y, null);		
		g2.drawImage(current_arena, fighter_two_platform_endX, fighter_two_platform_Y, null);
		
		if (fighter[0] != null) {			
			if (fighter[0].isHit) animateHit(0, g2);						
			g2.drawImage(fighter[0].getBackSprite(), fighter_one_X, fighter_one_Y, null);			
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
		}
		if (fighter[1] != null) {
			if (fighter[1].isHit) animateHit(1, g2);				
			g2.drawImage(fighter[1].getFrontSprite(), fighter_two_X, fighter_two_Y, null);			
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
		}				
	}
	private void animateHit(int num, Graphics2D g2) {
		hitCounter++;
		if (hitCounter > 30) {
			fighter[num].isHit = false;
			hitCounter = -1;
		}
		else if (hitCounter % 5 == 0) {
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
		}
	}
	private void animateFighterDefeat() {		
		if (loser == 0) {
			if (fighter_one_Y < gp.screenHeight) {
				fighter_one_Y += 16;
			}
			else {
				fightStage = fightStage_Swap;
			}
		}
		else if (loser == 1) {
			if (fighter_two_Y < gp.screenHeight) {
				fighter_two_Y += 16;
			}
			else {
				fightStage = fightStage_Swap;
			}
		}		
	}
	private void animateTrainerDefeat(Graphics2D g2) {
				
		if (loser == 0) {
			
		}
		else if (loser == 1) {
			
			if (fighter_two_endX < fighter_two_X) {
				fighter_two_X -= 6;
			}
			else {
				fightStage = fightStage_Victory;
			}
		}		
		
		g2.drawImage(current_arena, fighter_one_platform_endX, fighter_one_platform_Y, null);		
		g2.drawImage(current_arena, fighter_two_platform_endX, fighter_two_platform_Y, null);

		g2.drawImage(trainer[1].frontSprite, fighter_two_X + 25, fighter_two_Y, null);
		g2.drawImage(fighter[0].getBackSprite(), fighter_one_X, fighter_one_Y, null);			
	}
	private void drawTrainerDefeat(Graphics2D g2) {
		g2.drawImage(current_arena, fighter_one_platform_endX, fighter_one_platform_Y, null);		
		g2.drawImage(current_arena, fighter_two_platform_endX, fighter_two_platform_Y, null);
		
		g2.drawImage(fighter[0].getBackSprite(), fighter_one_X, fighter_one_Y, null);				
		g2.drawImage(trainer[1].frontSprite, fighter_two_endX + 25, fighter_two_Y, null);	
	}
	
	// MISC
	private BufferedImage setup(String imagePath, int width, int height) {
		
		UtilityTool utility = new UtilityTool();
		BufferedImage image = null;
		
		try {
			image = ImageIO.read(getClass().getResourceAsStream(imagePath + ".png"));
			image = utility.scaleImage(image, width, height);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		return image;
	}	
	public void changeAlpha(Graphics2D g2, float alphaValue) {		
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaValue));
	}
}