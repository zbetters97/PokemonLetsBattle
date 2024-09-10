package application;

import java.util.Random;

import battle.BattleEngine;
import moves.Move;
import pokemon.Pokemon;

public class BattleManager {
	
	GamePanel gp;
	public Pokemon[] fighter = new Pokemon[2];
	public Move[] move = new Move[2];
	public BattleEngine battleEngine;	
	public boolean canMove;
	
	public int damageDealt;
	
	public int fighterTurn;
	public final int playerTurn = 1;
	public final int cpuTurn = 2;
	
	private final int faint_SE = 4;
	private final int moves_SE = 5;
	private final int battle_SE = 6;

	// BATTLE STATES
	public int battleMode;
	public final int wildBattle = 1;
	public final int trainerBattle = 2;
	public final int gymBattle = 3;
	public final int rivalBattle = 4;
	public final int eliteBattle = 5;
	public final int championBattle = 6;
	public final int legendaryBattle = 7;
			
	public BattleManager(GamePanel gp) {
		this.gp = gp;
	}
	
	public void update() {		
		
	}
	
	public void setupBattle(int currentBattle) {		
		
		fighter[0] = Pokemon.getPokemon(0);
		fighter[1] = Pokemon.getPokemon(1);
		
		battleMode = currentBattle;
		
		battleEngine = new BattleEngine(fighter[0], fighter[1], gp);
		
		if (battleMode == wildBattle) {
			gp.ui.addBattleDialogue("A wild " + fighter[1].toString() + " appeared!");
			gp.ui.nextSubState = gp.ui.subState_Options;
		}
		
		gp.stopMusic();
//		setupMusic();
	}

	private void setupMusic() {
		if (battleMode == wildBattle) gp.playMusic(1, 0);		
		else if (battleMode == trainerBattle) gp.playMusic(1, 1);
		else if (battleMode == gymBattle) gp.playMusic(1, 2);
		else if (battleMode == rivalBattle) gp.playMusic(1, 3);
		else if (battleMode == eliteBattle) gp.playMusic(1, 4);
		else if (battleMode == championBattle) gp.playMusic(1, 5);
		else if (battleMode == legendaryBattle) gp.playMusic(1, 6);		
	}
	
	public void playerSelectMove(int selection) {
		move[0] = fighter[0].getMoveSet().get(selection);
	}
	
	public void cpuSelectMove() {
		move[1] = battleEngine.cpuSelectMove();
	}
	
	public void startTurn(int atk, int trg) {
		turn(atk, trg);
	}
	
	private void turn(int atk, int trg) {
		
		gp.ui.addBattleDialogue(fighter[atk].toString() + " used\n" + move[atk].toString() + "!"); 	
						
		// if not delayed move or delayed move is ready
		if (1 >= move[atk].getTurns()) {	
			
			gp.playSE(moves_SE, move[atk].getName());
			
			int soundFile = gp.se.getFile(moves_SE, move[atk].getName());
			int soundDuration = gp.se.getSoundDuration(moves_SE, soundFile);			
			gp.ui.dialogueTimerMax = 30 + (soundDuration * 60);
						
			// reset turns to wait
			move[atk].setTurns(move[atk].getNumTurns());
		}
		// delayed move is used for first time
		else if (move[atk].getTurns() == move[atk].getNumTurns()) {
						
			gp.ui.addBattleDialogue(move[atk].getDelay(fighter[atk].getName()));	
			gp.ui.dialogueTimerMax = 120;
			
			// reduce number of turns to wait
			move[atk].setTurns(move[atk].getTurns() - 1);	
		}
		
		// decrease move pp
		move[atk].setpp(move[atk].getpp() - 1);
		
		runMove(atk, trg);
	}
	
	private void runMove(int atk, int trg) {
		
		// move has a status affect
		if (move[atk].getMType().equals("Status")) {
			statusMove(trg, move[atk]);	
		}		
		// move has an attribute affect
		else if (move[atk].getMType().equals("Attribute")) 	{
			attributeMove(atk, trg, move[atk]);
		}		
		// move is in other category
		else if (move[atk].getMType().equals("Other")) {
			
			switch (move[atk].getName()) {
				case "TELEPORT":						
					break;						
				default:
					break;
			}		
		}		
		// move deals damage
		else {
			if (damageMove(atk, trg)) {
				defeated(atk, trg);
			}
		}
		
		if (trg == 1) gp.ui.nextSubState = gp.ui.subState_CPU;			
		else gp.ui.nextSubState = gp.ui.subState_Options;
		
		gp.ui.dialogueTimerMax = 120;
		gp.ui.battleSubState = gp.ui.subState_Dialogue;
	}
	
	private void statusMove(int trg, Move move) {
		
		// if pokemon does not already have status affect
		if (fighter[trg].getStatus() == null) {
			
			fighter[trg].setStatus(move.getEffect());	
			
			gp.ui.addBattleDialogue(fighter[trg].getName() + " is\n" + 
					fighter[trg].getStatus().getCondition() + "!");
		}
		// pokemon already has status affect
		else {
			gp.ui.addBattleDialogue(fighter[trg].getName() + " is already\n" + 
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
//			gp.playSE(battle_SE, "stat-up");
		}
		// attributes lowered
		else  {
//			gp.playSE(battle_SE, "stat-down");
		}
	}
	
	private boolean damageMove(int atk, int trg) {
		
		boolean defeated = false;
		
		// get critical damage (1 or 1.5)
		double crit = isCritical(move[atk]);
		int damage = 1;
		
		// logic for seismic toss
		if (move[atk].getPower() == -1) {
			 damage = fighter[atk].getLevel();
		}
		else {
			damage = battleEngine.calculateDamage(atk, trg, move[atk], crit, false);
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
		
			gp.ui.addBattleDialogue(fighter[trg].getName() + "\ntook " + damage + " damage!");	
			
			absorbHP(atk, trg, move[atk], damage);
									
			// if damage is fatal
			if (damage >= fighter[trg].getHP())	{
				battleEngine.dealDamage(trg, damage);
				isRecoil(atk, trg, move[atk], damage);
				defeated = true;
			}						
			// fighter survives hit
			else {							
				battleEngine.dealDamage(trg, damage);							
				applyEffect(atk, trg, move[atk]);							
				isRecoil(atk, trg, move[atk], damage);
			}
		}		
		
		return defeated;
	}
	
	private void defeated(int win, int lsr) {
		
		fighter[lsr].setAlive(false);
		
		int xp = calculateXP(lsr);
		fighter[win].setXP(fighter[win].getBXP() + xp);
		
		gp.playSE(faint_SE, fighter[lsr].getName());
		
		gp.ui.addBattleDialogue(fighter[lsr].getName() + "\nfainted!");			
		gp.ui.addBattleDialogue(fighter[win].getName() + " gained\n" + xp + " Exp. Points!");
		
		return;
	}
	
	private int calculateXP(int lsr) {
		
		// exp formula reference (GEN I-IV): https://bulbapedia.bulbagarden.net/wiki/Experience		
		int exp = (int) (((( fighter[lsr].getXP() * fighter[lsr].getLevel() ) / 7)) * 1.5);		
		return exp;
	}
	
	private void absorbHP(int atk, int trg, Move move, int damage) {
		
		if (move.getName() == "ABSORB" || move.getName() == "GIGA DRAIN") {
			
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
	
	private void isRecoil(int atk, int trg, Move move, int damage) {
		
		if (move.getSelfInflict() != null) {								
			damage = (int)(damage * move.getSelfInflict());	
			
			gp.ui.addBattleDialogue(fighter[atk].getName() + "\nwas hit with " + damage + " recoil damage!");		
			
			// damage is fatal to attacker
			if (damage >= fighter[atk].getHP()) {
				battleEngine.dealDamage(atk, damage);
//				defeated(trg, atk, damage);
			}					
			else
				battleEngine.dealDamage(atk, damage);
		}
	}
	
	private void applyEffect(int atk, int trg, Move move) {
		
		// move causes attribute or status effect
		if (move.getProbability() != null) {								
										
			// chance for effect to apply
			if (new Random().nextDouble() <= move.getProbability()) {
				
				if (move.getStats() != null) {
					attributeMove(atk, trg, move);
				}
				else {			
					// if not already affected by a status effect
					if (fighter[trg].getStatus() == null) {
						fighter[trg].setStatus(move.getEffect());
						
						gp.ui.addBattleDialogue(fighter[trg].getName() + " \nis " + 
							fighter[trg].getStatus().getCondition() + "!");
					}
				}
			}							
		}
	}
	
	private double isCritical(Move move) {			
		/** CRITICAL HIT REFERENCE: https://www.serebii.net/games/criticalhits.shtml (GEN II-V) **/
		
		int chance = 2;
		if (move.getCrit() == 1) 
			chance = 4;
		
		Random r = new Random();		
		return (r.nextFloat() <= ((float) chance / 25)) ? 1.5 : 1;
	}
}