package application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import application.GamePanel.Weather;
import entity.Entity;
import entity.collectables.items.ITM_EXP_Share;
import moves.Move;
import moves.Moves;
import pokemon.Pokemon;
import properties.Status;
import properties.Type;
import properties.abilities.*;

public class BattleManager extends Thread {
			
	private GamePanel gp;
	public boolean active = false;
	public boolean running = false;
	
	// GENERAL VALUES		
	public Entity trainer;
	
	public Pokemon[] fighter = new Pokemon[2];
	private Pokemon[] newFighter = new Pokemon[2];
	private ArrayList<Pokemon> otherFighters = new ArrayList<>();
	
	private Move playerMove, cpuMove;
	public Move newMove = null;
	public Move oldMove = null;
	
	private Weather weather = Weather.CLEAR;
	private int weatherDays = -1;
	private int winner = -1, loser = -1;	
	private int escapeAttempts = 0;
		
	public Entity ballUsed;
			
	// TURN VALUES
	private int currentTurn;
	private int nextTurn;
	private final int playerTurn = 0;
	private final int cpuTurn = 1;	
					
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
	public final int fight_Encounter = 1;
	public final int fight_Swap = 2;
	public final int fight_Start = 3;
	public final int fight_Capture = 4;
	public final int fight_Run = 5;
	public final int fight_Evolve = 6;
	
	// CONSTRUCTOR
	public BattleManager(GamePanel gp) {
		this.gp = gp;
	}
	public void setup(int currentBattle, Entity trainer, Pokemon pokemon, String condition) {
		
		battleMode = currentBattle;
		
		if (condition == null) weather = Weather.CLEAR;
		else weather = Weather.valueOf(condition);
		
		weatherDays = -1;
					
		if (trainer != null) this.trainer = trainer;	
		else if (pokemon != null) fighter[1] = pokemon;

		active = true;
		running = true;		
		fightStage = fight_Encounter;
	}
			
	/** RUN METHOD **/
	public void run() {		
		
		while (running) {
			
			switch (fightStage) {	
			
				case fight_Encounter:
					try { setBattle(); } 
					catch (InterruptedException e) { e.printStackTrace(); }					
					break;
			
				case fight_Swap:
					try { swapFighters(); } 
					catch (InterruptedException e) { e.printStackTrace(); }
					break;
			
				case fight_Start:					
					try { runBattle(); } 
					catch (InterruptedException e) { e.printStackTrace(); }
					break;	
					
				case fight_Capture:
					try { throwPokeball(); } 
					catch (InterruptedException e) { e.printStackTrace(); }				
					break;
					
				case fight_Run:
					try { escapeBattle(); } 
					catch (InterruptedException e) { e.printStackTrace(); }				
					break;
					
				case fight_Evolve:
					try { checkEvolve(); } 
					catch (InterruptedException e) { e.printStackTrace(); }				
					break;
			}		
		}		
	}
		
	// SETUP BATTLE METHOD
 	private void setBattle() throws InterruptedException {		
		gp.stopMusic();	
				
		switch(battleMode) {
			case wildBattle:
				
				gp.startMusic(1, 1);				
				pause(1400);
				
				gp.playSE(cry_SE, fighter[1].toString());	
				typeDialogue("A wild " + fighter[1].getName() + "\nappeared!", true);	
				
				gp.ui.battleState = gp.ui.battle_Dialogue;
				
				break;
			case trainerBattle: 
				
				gp.startMusic(1, 4);				
				pause(1400);		
				
				typeDialogue("Trainer " + trainer.name + "\nwould like to battle!", true);	
				
				gp.ui.battleState = gp.ui.battle_Dialogue;
				
				fighter[1] = trainer.pokeParty.get(0);
				
				gp.playSE(cry_SE, fighter[1].toString());	
				typeDialogue("Trainer " + trainer.name + "\nsent out " + fighter[1].getName() + "!");				
				pause(100);
				
				break;			
		}
		
		fighter[0] = gp.player.pokeParty.get(0);
		getOtherFighters();
		
		gp.playSE(cry_SE, fighter[0].toString());	
		typeDialogue("Go, " + fighter[0].getName() + "!");					
		pause(100);
		
		getWeatherAbility();	
		checkWeatherCondition();
		
		running = false;		
		
		gp.ui.battleState = gp.ui.battle_Options;
	}	
	private void getOtherFighters() {
		
		for (Pokemon p : gp.player.pokeParty) {
			if (!otherFighters.contains(p) &&
					p.getHeldItem() != null && 
					p.getHeldItem().name.equals(ITM_EXP_Share.colName)) {
				otherFighters.add(p);
			}
		}
	}
		
	// CHANGE WEATHER BASED ON FIGHTER ABILITY
	private void getWeatherAbility() {
			
		if (fighter[0].getAbility().getCategory() == Ability.Category.WEATHER &&
				fighter[1].getAbility().getCategory() == Ability.Category.WEATHER) {
			
			// if fighter 1 is faster
			if (fighter[0].getSpeed() > fighter[1].getSpeed()) {
				weather = Weather.valueOf(fighter[1].getAbility().getAttribute());
			}
			// if fighter 2 is faster
			else if (fighter[0].getSpeed() < fighter[1].getSpeed()) {
				weather = Weather.valueOf(fighter[0].getAbility().getAttribute());
			}
			// if both fighters have equal speed, coin flip decides
			else {
				Random r = new Random();					
				if (r.nextFloat() <= ((float) 1 / 2)) {
					weather = Weather.valueOf(fighter[1].getAbility().getAttribute());
				}
				else {
					weather = Weather.valueOf(fighter[0].getAbility().getAttribute());
				}
			}					
		}
		else if (fighter[0].getAbility().getCategory() == Ability.Category.WEATHER) {									
			weather = Weather.valueOf(fighter[0].getAbility().getAttribute());
		}
		else if (fighter[1].getAbility().getCategory() == Ability.Category.WEATHER) {									
			weather = Weather.valueOf(fighter[1].getAbility().getAttribute());
		}
		
	}
	
	// SWAP POKEMON METHODS
	private void swapFighters() throws InterruptedException {
				
		// WINNER NOT YET DECIDED
		if (winner == -1) {
			
			if (!fighter[0].isAlive()) fighter[0] = null;
			if (!fighter[1].isAlive()) fighter[1] = null;
									
			gp.ui.battleState = gp.ui.battle_Dialogue;
			
			// CPU SWAP OUT
			if (fighter[1] == null) {
				
				// PLAYER SWAP OUT
				if (newFighter[0] != null) {
					
					if (otherFighters.contains(newFighter[0])) {
						otherFighters.remove(newFighter[0]);
					}
					
					fighter[0] = newFighter[0];		
					
					gp.playSE(cry_SE, fighter[0].toString());	
					typeDialogue("Go, " + fighter[0].getName() + "!");					
					pause(100);
				}
				
				fighter[1] = newFighter[1];
				
				gp.playSE(cry_SE, fighter[1].toString());	
				typeDialogue("Trainer " + trainer.name + "\nsent out " + fighter[1].getName() + "!");				
				pause(100);
				
				newFighter[0] = null;
				newFighter[1] = null;
				
				running = false;				
				gp.ui.battleState = gp.ui.battle_Options;				
			}
			// PLAYER FORCE SWAP OUT
			else if (fighter[0] == null) {
				
				if (otherFighters.contains(newFighter[0])) {
					otherFighters.remove(newFighter[0]);
				}
				
				fighter[0] = newFighter[0];		
				
				gp.playSE(cry_SE, fighter[0].toString());	
				typeDialogue("Go, " + fighter[0].getName() + "!");					
				pause(100);
				
				newFighter[0] = null;
				newFighter[1] = null;
				
				running = false;				
				gp.ui.battleState = gp.ui.battle_Options;	
			}
			// MID BATTLE SWAP OUT
			else {			
				
				if (otherFighters.contains(newFighter[0])) {
					otherFighters.remove(newFighter[0]);
				}
				
				if (!otherFighters.contains(fighter[0])) {
					otherFighters.add(fighter[0]);	
				}

				fighter[0] = newFighter[0];		
				
				gp.playSE(cry_SE, fighter[0].toString());
				typeDialogue("Go, " + fighter[0].getName() + "!");	
				pause(100);
				
				newFighter[0] = null;
				newFighter[1] = null;
				
				fightStage = fight_Start;
			}
		}				
		// TRAINER HAS WON
		else {							
			getWinningTrainer();
		}
	}
	
	// SWAP FIGHTER METHODS
	public boolean swapPokemon(int partySlot) {
		
		if (fighter[0] == gp.player.pokeParty.get(partySlot)) {
			return false;
		}
		
		if (gp.player.pokeParty.get(partySlot).isAlive()) {
			
			newFighter[0] = gp.player.pokeParty.get(partySlot);
						
			if (gp.player.pokeParty.size() > 1) {
				Collections.swap(gp.player.pokeParty, 0, partySlot);	
			}
						
			return true;
		}
		else {
			return false;
		}		
	}
	
	private Pokemon cpuSelectNextFighter() {
		
		if (fighter[0] == null) return trainer.pokeParty.get(0);
		
		int index = trainer.pokeParty.indexOf(fighter[1]);
		if (index < 0 || index + 1 == trainer.pokeParty.size()) {
			return null;
		}
		else {
			 return trainer.pokeParty.get(index + 1);
		}
	}
					
	// RUN BATTLE METHOD
	private void runBattle() throws InterruptedException {
		
		getCPUMove();
		setRotation();	
		
		startTurn();
		
		if (hasWinningPokemon()) {
			getWinningPokemon();
		}		
		else {
			checkStatusDamage();	
			checkWeatherDamage();
			
			if (hasWinningPokemon()) {
				getWinningPokemon();
			}
			else {
				turnReset();
			}
		}
	}
	
	// GET MOVES METHOD
	public Move getPlayerMove(int selection) {
		
		playerMove = fighter[0].getMoveSet().get(selection);
		
		boolean struggle = true;
		for (Move m : fighter[0].getMoveSet()) {
			if (m.getpp() > 0) {
				struggle = false;
				break;
			}
		}
		
		if (struggle) {
			playerMove = new Move(Moves.STRUGGLE);
		}
		
		return playerMove;
	}
	private void getCPUMove() throws InterruptedException {
		
		// GET CPU MOVE IF NO CPU DELAY
		int delay = BattleUtility.getDelay(playerMove, cpuMove);
		
		if (delay == 0 || delay == 1) {			
			cpuMove = BattleUtility.getBestMove(fighter[1], fighter[0], weather);
		}
	}
	
	// SET ROTATION METHODS
	private void setRotation() {		
		
		if (fighter[0].isAlive() && fighter[1].isAlive()) {
			
			// 1 if player moves first
			// 2 if cpu moves first
			// 3 if only cpu
			// 4 if only player
			// 5 if neither			
			int firstTurn = BattleUtility.getFirstTurn(fighter[0], fighter[1], playerMove, cpuMove);	
			
			if (firstTurn == 1) { 
				currentTurn = playerTurn;
				nextTurn = cpuTurn;
			}	
			else if (firstTurn == 2) { 
				currentTurn = cpuTurn;
				nextTurn = playerTurn;
			}				
			else if (firstTurn == 3) { 
				currentTurn = cpuTurn;
				nextTurn = -1;
			}				
			else if (firstTurn == 4) { 
				currentTurn = playerTurn;
				nextTurn = -1;
			}
			else if (firstTurn == 5) {
				currentTurn = -1;
				nextTurn = -1;
			}
			else {
				currentTurn = -1;
				nextTurn = -1;
			}
		}
	}
		
	// START TURN METHOD
	private void startTurn() throws InterruptedException {	
		
		if (currentTurn != -1) {		

			if (canMove()) {					
				move();					
			}
			else {
				currentTurn = nextTurn;
				nextTurn = -1;
			}
			
			startTurn();
		}	
	}		
	
	// STATUS CONDITION METHODS
	private boolean canMove() throws InterruptedException {
		
		boolean canMove = true;
		Pokemon pkm = fighter[currentTurn];
		
		// if attacker has status effect
		if (pkm.getStatus() != null) {
		
			// check which status
			switch (fighter[currentTurn].getStatus().getAbreviation()) {			
				case "PAR":	canMove = paralyzed(pkm); break;					
				case "FRZ": canMove = frozen(pkm); break;			
				case "SLP": canMove = asleep(pkm); break;
				case "CNF": canMove = confused(pkm); break;
			}
		}	
		
		return canMove;
	}
	private boolean paralyzed(Pokemon pkm) throws InterruptedException {
		
		// 1/4 chance can't move due to PAR
		int val = 1 + (int)(Math.random() * 4);
		if (val == 1) {		
			pkm.getStatus().printStatus(gp, pkm.getName());
			return false;
		} 
		else {
			return true;
		}
	}
	private boolean frozen(Pokemon pkm) throws InterruptedException {
		
		// 1/4 chance attacker can thaw from ice
		int val = 1 + (int)(Math.random() * 4);
		if (val == 1) {
			Status status = pkm.getStatus();
			pkm.setStatus(null);
			typeDialogue(pkm.getName() + "" + status.printRecover());			
			return true;
		}
		else {	
			pkm.getStatus().printStatus(gp, pkm.getName());
			return false;		
		}
	}
	private boolean asleep(Pokemon pkm) throws InterruptedException {
				
		// if number of moves under status hit limit, remove status
		if (recoverStatus(pkm)) {			
			return true;
		}
		// pokemon still under status status
		else {
			
			// increase counter
			pkm.setStatusCounter(pkm.getStatusCounter() + 1);
			pkm.getStatus().printStatus(gp, pkm.getName());
			
			return false;
		}
	}
	private boolean confused(Pokemon pkm) throws InterruptedException {
		
		gp.playSE(battle_SE, pkm.getStatus().getStatus());
		typeDialogue(pkm.getName() + " is\n" + pkm.getStatus().getStatus() + "...");
		
		// if number of moves under status hit limit, remove status
		if (recoverStatus(pkm)) {			
			return true;
		}
		// pokemon still under status status
		else {
			
			// increase counter
			pkm.setStatusCounter(pkm.getStatusCounter() + 1);
									
			// if pokemon hurt itself in confusion
			if (confusionDamage(pkm)) {
				return false;
			}
			else { 
				return true;
			}			
		}
	}
	private boolean confusionDamage(Pokemon pkm) throws InterruptedException {

		// 1/2 chance of hurting self
		int val = 1 + (int)(Math.random() * 2);	
				
		if (val == 1) {					
			
			double level = pkm.getLevel();
			double power = 1.0;
			double A = pkm.getAttack();
			double D = pkm.getDefense();
					
			// confusion damage reference: https://fighterlp.fandom.com/wiki/Confusion_(status)
			int damage = (int)((Math.floor(((((Math.floor((2 * level) / 5)) + 2) * 
				power * (A / D)) / 50)) + 2));
		
			int hp = pkm.getHP() - damage;
			
			// pokemon defeated itself in confusion damage
			if (hp <= 0) {
				hp = 0;					
			}									
			
			pkm.setHit(true);
			gp.playSE(battle_SE, "hit-normal");	
			decreaseHP(pkm, hp, damage);	
			pkm.getStatus().printStatus(gp, pkm.getName());
			
			return true;	
		}
		else {
			return false;
		}
	}
	private boolean recoverStatus(Pokemon pkm) throws InterruptedException {
		
		// if first move under status, set number of moves until free (1-5)
		if (pkm.getStatusLimit() == 0) {
			pkm.setStatusLimit((int)(Math.random() * 5));	
		}
		
		// if number of moves under status hit limit, remove status
		if (pkm.getStatusCounter() >= pkm.getStatusLimit()) {									
			
			Status status = pkm.getStatus();
			pkm.setStatusCounter(0); 
			pkm.setStatusLimit(0);			
			pkm.setStatus(null);
			typeDialogue(pkm.getName() + status.printRecover());				
			
			return true;
		}
		else {
			return false;
		}
	}
	
	// MOVE METHOD
	private void move() throws InterruptedException {		
				
		Pokemon atk = currentTurn == playerTurn ? fighter[0] : fighter[1];
		Pokemon trg = currentTurn == playerTurn ? fighter[1] : fighter[0];		
		Move move = currentTurn == playerTurn ? playerMove : cpuMove;
		
		getWeatherMoveDelay(move);
		if (move.getTurns() == 0) {				
			
			typeDialogue(atk.getName() + " used\n" + move.toString() + "!", false); 
			
			atk.setAttacking(true);
			playSE(moves_SE, move.getName());
			
			move.setTurns(move.getNumTurns());
			
			// decrease move pp
			if (trg.getAbility().getCategory() == Ability.Category.PP) {
				move.setpp(move.getpp() - (int) trg.getAbility().getFactor());
			}
			else {				
				move.setpp(move.getpp() - 1);
			}						
			
			attack(atk, trg, move);				
		}			
		else {						
			typeDialogue(atk.getName() + " used\n" + move.toString() + "!");
			typeDialogue(move.getDelay(atk.getName()));		
			
			move.setTurns(move.getTurns() - 1);
						
			currentTurn = nextTurn;	
			nextTurn = -1;							
		}	
	}		
	private void getWeatherMoveDelay(Move move) {
		
		switch (weather) {		
			case SUNLIGHT:
				if (move.getMove() == Moves.SOLARBEAM) {
					move.setTurns(1);
				}
				break;
			case RAIN:
				break;
			case HAIL:
				break;
			case SANDSTORM:
				break;
			case CLEAR:
				break;
		}		
	}
	
	// ATTACK METHOD
	public void attack(Pokemon atk, Pokemon trg, Move move) throws InterruptedException {
			
		if (BattleUtility.hit(atk, trg, move, weather)) {
			
			switch (move.getMType()) {
			
				case STATUS:
					statusMove(trg, move);					
					break;
					
				case ATTRIBUTE:
					attributeMove(atk, trg, move);					
					break;
					
				case WEATHER:
					weatherMove(move);					
					break;
					
				case OTHER:
					otherMove(atk, trg, move);					
					break;
					
				default:
					damageMove(atk, trg, move);
					break;
			}		
		}
		else {
			typeDialogue("The attack missed!");
			currentTurn = nextTurn;	
			nextTurn = -1;
		}			
	}		
	
	// STATUS MOVE
	private void statusMove(Pokemon pkm, Move move) throws InterruptedException {
		
		setStatus(pkm, move.getEffect());
		
		currentTurn = nextTurn;	
		nextTurn = -1;
	}
	private void setStatus(Pokemon pkm, Status status) throws InterruptedException {
		
		if (pkm.getHP() > 0) {
			
			if (pkm.getStatus() == null) {
				
				pkm.setStatus(status);		
				
				gp.playSE(battle_SE, status.getStatus());
				typeDialogue(pkm.getName() + status.printCondition());
			}
			else {				
				typeDialogue(pkm.getName() + " is\nalready " + 
						pkm.getStatus().getStatus() + "!");
			}			
		}
	}

	// ATTRIBUTE MOVE
	private void attributeMove(Pokemon atk, Pokemon trg, Move move) throws InterruptedException {
		
		// if move changes self attributes
		if (move.isToSelf()) {
			
			if (move.getMove() == Moves.REST) {
				
				int gainedHP = atk.getBHP() - atk.getHP();
				int result = atk.getHP() + gainedHP;
				increaseHP(atk, result, gainedHP);
				
				setStatus(atk, Status.SLEEP);		
			}
			else {				
				setAttribute(atk, move.getStats(), move.getLevel());		
			}
		}
		// if move changes target attributes
		else {
			setAttribute(trg, move.getStats(), move.getLevel());
		}			
		
		currentTurn = nextTurn;	
		nextTurn = -1;
	}
	private void setAttribute(Pokemon pkm, List<String> stats, int level) throws InterruptedException {
		
		// loop through each specified attribute to be changed
		for (String stat : stats) {				
			
			// attributes raised
			if (level > 0) {
				gp.playSE(battle_SE, "stat-up");
			}
			// attributes lowered
			else {
				gp.playSE(battle_SE, "stat-down");
			}
			
			typeDialogue(pkm.changeStat(stat, level));	
		}			
	}
	
	// WEATHER MOVE
	private void weatherMove(Move move) throws InterruptedException {		
		
		weather = Weather.valueOf(move.getWeather());
		checkWeatherCondition();
		weatherDays = move.getNumTurns();
		
		currentTurn = nextTurn;	
		nextTurn = -1;
	}
	private void checkWeatherCondition() throws InterruptedException {
		
		switch (weather) {	
			case CLEAR:
				break;
			case SUNLIGHT:
				gp.playSE(battle_SE, "sunlight");
				typeDialogue("The sun shines intensely!");	
				break;
			case RAIN:
				gp.playSE(battle_SE, "rain");
				typeDialogue("Rain started to fall!");	
				break;
			case HAIL:
				gp.playSE(battle_SE, "hail");
				typeDialogue("Hail started to fall!");	
				break;
			case SANDSTORM:
				gp.playSE(battle_SE, "sandstorm");
				typeDialogue("A sandstorm started to\n rage!");	
				break;
		}		
	}
	
	// OTHER MOVE
	private void otherMove(Pokemon atk, Pokemon trg, Move move) {
		if (move.getMove() == Moves.TELEPORT) {
			
		}
	}
	
	// DAMAGE MOVE
	private void damageMove(Pokemon atk, Pokemon trg, Move move) throws InterruptedException {
		
		setDamage(atk, trg, move);
		
		// BOTH POKEMON ALIVE
		if (trg.getHP() > 0 && atk.getHP() > 0) {
			
			applyEffect(atk, trg, move);
									
			if (nextTurn != -1 && flinched(trg, move)) {
				currentTurn = -1;	
				nextTurn = -1;					
			}
			else {
				currentTurn = nextTurn;	
				nextTurn = -1;	
			}			
		}
		// BOTH POKEMON FAINTED
		else if (trg.getHP() <= 0 && atk.getHP() <= 0) {
			
			currentTurn = -1;
			nextTurn = -1;
		}
		// TARGET FAINTED
		else if (trg.getHP() <= 0) {
			applyEffect(atk, trg, move);
			
			currentTurn = -1;
			nextTurn = -1;
		}
		// ATTACKER FAINTED
		else if (atk.getHP() <= 0) {
			applyEffect(atk, trg, move);
			
			currentTurn = -1;
			nextTurn = -1;
		}	
	}	
	private void setDamage(Pokemon atk, Pokemon trg, Move move) throws InterruptedException {
		
		String hitSE;
		
		if (trg.getTypes() != null) {
			hitSE = getHitSE(BattleUtility.effectiveness(trg.getTypes().get(0), move.getType()));	
		}
		else {
			hitSE = getHitSE(BattleUtility.effectiveness(trg.getType(), move.getType()));		
		}		
		
		gp.playSE(battle_SE, hitSE);
		trg.setHit(true);
					
		double crit = getCritical(atk, trg, move);
		int damage = 1;
		
		if (move.getPower() == -1) {
			 damage = (int) (atk.getLevel() * crit);
		}
		else {
			damage = (int) (BattleUtility.calculateDamage(atk, trg, move, weather) * crit);	
		}
		
		if (damage <= 0) {
			typeDialogue("It had no effect!");
			currentTurn = nextTurn;	
			nextTurn = -1;	
		}
		else {
			
			if (damage > trg.getHP()) {
				damage = trg.getHP();
			}
			
			decreaseHP(trg, trg.getHP() - damage, damage);		
			
			if (crit == 1.5) {
				typeDialogue("A critical hit!");
			}
			
			if (hitSE.equals("hit-super")) {
				typeDialogue("It's super effective!");
			}
			else if (hitSE.equals("hit-weak")) {
				typeDialogue("It's not very effective...");
			}	
			
			typeDialogue(trg.getName() + " took\n" + damage + " damage!");		
		}
		
		absorbHP(atk, trg, move, damage);
		getRecoil(atk, move, damage);		
	}
	
	private String getHitSE(double effectiveness) throws InterruptedException {
		
		String hit = "";
		
		switch (Double.toString(effectiveness)) {
			case "0.25": hit = "hit-weak"; break;			
			case "0.5": hit = "hit-weak"; break;
			case "1.0": hit = "hit-normal"; break;
			case "1.5": hit = "hit-super"; break;
			case "2.25": hit = "hit-super"; break;			
			default: hit = "hit-normal"; break;
		}
		
		return hit;
	}	
	private double getCritical(Pokemon atk, Pokemon trg, Move move) {			
		/** CRITICAL HIT REFERENCE: https://www.serebii.net/games/criticalhits.shtml (GEN II-V) **/
		
		int chance = 2;
		double impact  = 1.5;
						
		if (move.getCrit() == 1) {
			chance = 4;
		}
		
		if (trg.getAbility().getCategory() == Ability.Category.CRITICAL) {
			chance *= trg.getAbility().getFactor();
		}
		
		if (atk.getAbility().getCategory() == Ability.Category.CRITICAL) {
			impact = atk.getAbility().getFactor();
		}
		
		Random r = new Random();		
		return (r.nextFloat() <= ((float) chance / 25)) ? impact : 1.0;					
	}
	
	private void absorbHP(Pokemon atk, Pokemon trg, Move move, int damage) throws InterruptedException {
		
		if (move.getName().equals(Moves.ABSORB.getName()) || 
				move.getName().equals(Moves.GIGADRAIN.getName())) {
			
			int gainedHP = (damage / 2);
			int result = atk.getHP() + gainedHP;
			
			// if attacker not at full health
			if (atk.getHP() != atk.getBHP()) {
				
				// if gained hp is greater than total hp
				if (gainedHP + atk.getHP() > atk.getBHP()) {
					
					// gained hp is set to amount need to hit hp limit									
					gainedHP = atk.getBHP() - atk.getHP();					
					
					increaseHP(atk, atk.getBHP(), gainedHP);					
				}
				else {		
					increaseHP(atk, result, gainedHP);
				}
				
				typeDialogue(atk.getName() + "\nabsorbed " + gainedHP + " HP!");
			}
		}
	}
	private void getRecoil(Pokemon pkm, Move move, int damage) throws InterruptedException {
		
		if (move.getSelfInflict() != 0.0) {	
			
			int recoilDamage = (int)(Math.ceil(damage * move.getSelfInflict()));	

			// subtract damage dealt from total HP
			int result = pkm.getHP() - recoilDamage;		
			
			if (pkm.getAbility().getCategory() == Ability.Category.RECOIL) {
				result *= pkm.getAbility().getFactor();
			}

			if (result <= 0) {
				result = 0;
			}
			else {
				decreaseHP(pkm, result, recoilDamage);
				typeDialogue(pkm.getName() + " was hit\nwith recoil damage!");		
			}			
		}
	}
	private void applyEffect(Pokemon atk, Pokemon trg, Move move) throws InterruptedException {

		// move causes attribute or status effect
		if (move.getProbability() != 0.0) {								
										
			// chance for effect to apply
			if (new Random().nextDouble() <= move.getProbability()) {
				
				if (move.getStats() != null) {
					attributeMove(atk, trg, move);
				}
				else {			
					setStatus(trg, move.getEffect());						
				}
			}							
		}	
		else if (trg.getAbility().getCategory() == Ability.Category.STATUS) {				
			setStatus(atk, trg.getAbility().getEffect());
		}			
	}
	private boolean flinched(Pokemon pkm, Move move) throws InterruptedException {
		
		boolean flinched = false;
		
		if (move.getFlinch() != 0.0 && 
				pkm.getAbility().getCategory() != Ability.Category.FLINCH) {
			
			if (new Random().nextDouble() <= move.getFlinch()) {
				
				typeDialogue(pkm.getName() + " flinched!");
				flinched = true;
			}
		}		
		
		return flinched;
	}
	
	// STATUS METHODS
	private void checkStatusDamage() throws InterruptedException {					
		if (fighter[0].isAlive()) {
			getStatusDamage(fighter[0]);
		}	
		if (fighter[1].isAlive()) {
			getStatusDamage(fighter[1]);
		}	
	}
	private void getStatusDamage(Pokemon pkm) throws InterruptedException {
		// status effects reference: https://pokemon.fandom.com/wiki/Status_Effects		
		
		if (pkm.getStatus() != null) {				
			
			pause(500);
			
			if (pkm.getStatus().getAbreviation().equals("PSN") || 
					pkm.getStatus().getAbreviation().equals("BRN")) {
									
				int damage = (int) Math.ceil((pkm.getHP() * 0.16));
				int newHP = pkm.getHP() - damage;		
				
				if (newHP <= 0) {
					newHP = 0;
				}
				
				gp.playSE(battle_SE, pkm.getStatus().getStatus());
				pkm.setHit(true);
				decreaseHP(pkm, newHP, damage);
				
				pkm.getStatus().printStatus(gp, pkm.getName());
			}		
		}	
	}
	
	// WEATHER METHODS
	private void checkWeatherDamage() throws InterruptedException {
					
		if (weatherDays != -1) {
						
			if (weatherDays == 0) {
												
				switch (weather) {		
					case CLEAR:
						break;
					case SUNLIGHT:
						typeDialogue("The sunlight grew gentle!");	
						break;
					case RAIN:
						typeDialogue("The rain stopped falling!");	
						break;
					case HAIL:
						typeDialogue("The hail stopped falling!");					
						break;
					case SANDSTORM:
						typeDialogue("The sandstorm subsided!");					
						break;
				}
				
				weather = Weather.CLEAR;
				weatherDays = -1;
			}
			else {
				weatherDays--;
			}
		}
		
		switch (weather) {		
			case CLEAR:
				break;
			case SUNLIGHT:
				gp.playSE(battle_SE, "sunlight");
				typeDialogue("The sunlight continues\nto shine intensely!");	
				break;
			case RAIN:
				gp.playSE(battle_SE, "rain");
				typeDialogue("Rain continues to fall!");	
				break;
			case HAIL:
				gp.playSE(battle_SE, "hail");
				typeDialogue("Hail continues to fall!");	
				
				if (fighter[0].isAlive() &&
						!fighter[0].checkType(Type.ICE)) {
					getWeatherDamage(fighter[0]);
				}
				if (fighter[1].isAlive() &&
						!fighter[1].checkType(Type.ICE)) {
					getWeatherDamage(fighter[1]);
				}	
				
				break;
			case SANDSTORM:
				gp.playSE(battle_SE, "sandstorm");
				typeDialogue("The sandstorm continues to rage!");	
				
				if (fighter[0].isAlive() &&
						!fighter[0].checkType(Type.ROCK) && 
						!fighter[0].checkType(Type.STEEL) &&
						!fighter[0].checkType(Type.GROUND)) {
					getWeatherDamage(fighter[0]);
				}
				if (fighter[1].isAlive() &&
						!fighter[1].checkType(Type.ROCK) && 
						!fighter[1].checkType(Type.STEEL) &&
						!fighter[1].checkType(Type.GROUND)) {
					getWeatherDamage(fighter[1]);
				}
				
				break;
		}
	}
	private void getWeatherDamage(Pokemon pkm) throws InterruptedException {
		
		int damage = (int) Math.ceil((pkm.getHP() * 0.0625));
		int newHP = pkm.getHP() - damage;		
		
		if (newHP <= 0) {
			newHP = 0;
		}
		
		gp.playSE(battle_SE, "hit-normal");
		pkm.setHit(true);
		decreaseHP(pkm, newHP, damage);
		
		typeDialogue(pkm.getName() + " was hurt\nby the " + weather.toString().toLowerCase() + "!");
	}
	
	// GET WINNING POKEMON METHODS
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
			otherFighters.remove(fighter[0]);
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
	public void getWinningPokemon() throws InterruptedException {
		
		// TRAINER 1 WINNER
		if (winner == 0) {			
			
			gp.playSE(faint_SE, fighter[1].toString());
			typeDialogue(fighter[1].getName() + " fainted!");	
			
			gainXP();
									
			fightStage = fight_Swap;
			gp.ui.battleState = gp.ui.battle_Dialogue;
		}
		// TRAINER 2 WINNER
		else if (winner == 1) {			
			
			gp.playSE(faint_SE, fighter[0].toString());			
			typeDialogue(fighter[0].getName() + " fainted!");	

			getWinningTrainer();
		}
		// TIE GAME
		else if (winner == 2) {
		
			gp.playSE(faint_SE, fighter[0].toString());
			typeDialogue(fighter[0].getName() + " fainted!");
			
			gp.playSE(faint_SE, fighter[1].toString());
			typeDialogue(fighter[1].getName() + " fainted!");

			getWinningTrainer();
		}
	}		
	private void gainXP() throws InterruptedException {
		
		int gainedXP = BattleUtility.calculateEXPGain(fighter[loser], battleMode == trainerBattle);
		
		if (otherFighters.size() > 0) {
			
			gainedXP = (int) Math.ceil(gainedXP / (otherFighters.size() + 1));
			
			typeDialogue(fighter[0].getName() + " gained\n" + gainedXP + " Exp. Points!", false);	
			
			int xp = fighter[0].getXP() + gainedXP;
			int expTimer = (int) Math.ceil(2500.0 / (double) gainedXP);
			increaseXP(fighter[0], xp, expTimer);	
			
			for (Pokemon p : otherFighters) {
								
				typeDialogue(p.getName() + " gained\n" + gainedXP + " Exp. Points!");
				
				xp = p.getXP() + gainedXP;				
				increaseXP(p, xp, 0);
			}
			
			otherFighters.clear();
		}
		else {
			typeDialogue(fighter[0].getName() + " gained\n" + gainedXP + " Exp. Points!", false);	
			
			int xp = fighter[0].getXP() + gainedXP;
			int expTimer = (int) Math.ceil(2500.0 / (double) gainedXP);
			increaseXP(fighter[0], xp, expTimer);	
		}
		
		getOtherFighters();	
		pause(500);
	}
	private void increaseXP(Pokemon p, int xp, int timer) throws InterruptedException {
		
		while (p.getXP() < xp) {
			
			p.setXP(p.getXP() + 1);
			
			pause(timer);
												
			// FIGHTER LEVELED UP
			if (p.getXP() >= p.getBXP() + p.getNextXP()) {			
				
				gp.pauseMusic();
				gp.playSE(battle_SE, "level-up");
				
				p.levelUp();					
				gp.ui.battleState = gp.ui.battle_LevelUp;
			
				gp.playSE(battle_SE, "upgrade");
				typeDialogue(p.getName() + " grew to\nLv. " + 
						(p.getLevel()) + "!", true);
				
				gp.ui.battleState = gp.ui.battle_Dialogue;
								
				checkNewMove(p);
				
				gp.playMusic();
			}		
		}				
	}
	private void checkNewMove(Pokemon pokemon) throws InterruptedException {
		
		newMove = pokemon.getNewMove();				
		if (newMove != null) {
			
			if (pokemon.learnMove(newMove)) {
				
				gp.playSE(battle_SE, "upgrade");
				typeDialogue(pokemon.getName() + " learned\n" + 
						newMove.getName() + "!", true);
				
				newMove = null;
			}
			else {
				gp.playMusic();
				
				typeDialogue(pokemon.getName() + " is trying to\nlearn " + newMove.getName() + ".", true);	
				typeDialogue("But, " + pokemon.getName() + " can't learn\nmore than four moves.", true);	
				typeDialogue("Delete a move to make\nroom for " + newMove.getName() + "?", false);
				
				gp.ui.battleState = gp.ui.battle_Confirm;					
				waitForKeyPress();
				getMoveAnswer(pokemon);
			}					
		}
	}
	private void getMoveAnswer(Pokemon pokemon) throws InterruptedException {
		
		if (gp.keyH.aPressed) {
			gp.keyH.aPressed = false;	
			
			if (fightStage == fight_Evolve) {
				gp.ui.battleState = gp.ui.battle_Evolve;	
			}
			else {
				fightStage = fight_Swap;	
				gp.ui.battleState = gp.ui.battle_Dialogue;	
			}
			
			if (gp.ui.commandNum == 0) {	
				
				for (int i = 0; i < gp.player.pokeParty.size(); i++) {
					if (gp.player.pokeParty.get(i) == pokemon) {						
						gp.ui.fighterNum = i;
						break;
					}
				}
				
				gp.ui.partyState = gp.ui.party_MoveSwap;
				gp.gameState = gp.partyState;	
				
				while (oldMove == null && !gp.keyH.bPressed) {
					pause(5);
				}
				
				if (oldMove != null) {					
					typeDialogue("1, 2, and.. .. ..\nPoof!", true);
					typeDialogue(pokemon.getName() + " forgot\n" + oldMove.getName() + ".", true);	
					typeDialogue("And...", true);
					
					gp.pauseMusic();
					gp.playSE(battle_SE, "upgrade");
					
					typeDialogue(pokemon.getName() + " learned\n" + newMove.getName() + "!", true);	
					
					gp.playMusic();
				}
				else {
					typeDialogue(pokemon.getName() + " did not learn\n" + newMove.getName() + ".", true);
				}
			}
			else {
				typeDialogue(pokemon.getName() + " did not learn\n" + newMove.getName() + ".", true);
			}
			
			newMove = null;
			oldMove = null;
			gp.ui.commandNum = 0;			
		}			
		else if (gp.keyH.bPressed) {
			gp.keyH.bPressed = false;
			
			if (fightStage == fight_Evolve) {
				gp.ui.battleState = gp.ui.battle_Evolve;	
			}
			else {
				fightStage = fight_Swap;	
				gp.ui.battleState = gp.ui.battle_Dialogue;	
			}
			
			typeDialogue(pokemon.getName() + " did not learn\n" + newMove.getName() + ".", true);
			
			newMove = null;
			gp.ui.commandNum = 0;	
		}
	}
	
	// GET WINNING TRAINER METHODS
	private void getWinningTrainer() throws InterruptedException {
		
		// WILD BATTLE
		if (battleMode == wildBattle) {
			
			// TRAINER 1 WINNER
			if (winner == 0) {		
				pause(1000);
				fightStage = fight_Evolve;
			}
			// WILD POKEMON WINNER
			else if (winner == 1) {			
				
				// TRAINER 1 HAS MORE POKEMON
				if (gp.player.hasPokemon()) {
					
					winner = -1;							
					running = false;
					fightStage = fight_Swap;
					
					gp.ui.partyState = gp.ui.party_Main_Select;
					gp.gameState = gp.partyState;
				}
				// TRAINER 1 OUT OF POKEMON
				else {					
					announceWinner();
				}			
			}			
		}
		// TRAINER BATTLE
		else {			
			gp.ui.battleState = gp.ui.battle_Dialogue;
			
			// TRAINER 1 WINNER
			if (winner == 0) {	
				
				// TRAINER 2 HAS MORE POKEMON
				if (trainer.hasPokemon()) {
					
					winner = -1;
					
					newFighter[1] = cpuSelectNextFighter();	
					
					if (gp.player.getAvailablePokemon() > 1 && playerMove.isReady()) {
						
						typeDialogue("Trainer " + trainer.name + " is about\nto sent out " + 
								newFighter[1].getName() + "!", true);
						
						typeDialogue("Will " + gp.player.name + " swap\nPokemon?", false);										
						
						gp.ui.battleState = gp.ui.battle_Confirm;					
						waitForKeyPress();
						getSwapAnswer();
					}
					else {
						gp.ui.resetFighterPositions();	
					}
				}
				// TRAINER 2 OUT OF POKEMON
				else {								
					announceWinner();
				}					
			}
			// TRAINER 2 WINNER
			else if (winner == 1) {
				
				// TRAINER 1 HAS MORE POKEMON
				if (gp.player.hasPokemon()) {
					
					winner = -1;							
					running = false;
					fightStage = fight_Swap;
					
					gp.ui.partyState = gp.ui.party_Main_Select;
					gp.gameState = gp.partyState;
				}
				// TRAINER 1 OUT OF POKEMON
				else {				
					announceWinner();
				}							
			}		
			// TIE GAME
			else if (winner == 2) {
				
				// TRAINER 1 AND 2 HAVE MORE POKEMON
				if (gp.player.hasPokemon() && trainer.hasPokemon()) {
					
					// GET NEW FIGHTER
					newFighter[1] = cpuSelectNextFighter();	
					
					winner = -1;							
					running = false;
					fightStage = fight_Swap;
					
					gp.ui.partyState = gp.ui.party_Main_Select;
					gp.gameState = gp.partyState;
				}
				// ONLY TRAINER 1 HAS MORE POKEMON
				else if (gp.player.hasPokemon()) {
					winner = 0;
					announceWinner();
				}
				// ONLY TRAINER 2 HAS MORE POKEMON
				else if (trainer.hasPokemon()) {
					winner = 1;
					announceWinner();					
				}
				// BOTH TRAINERS DEFEATED
				else {
					pause(500);
					endBattle();
				}
			}
		}
	}
	private void getSwapAnswer() {
		
		if (gp.keyH.aPressed) {
			gp.keyH.aPressed = false;
			
			gp.ui.resetFighterPositions();
			
			fightStage = fight_Swap;
			gp.ui.battleState = gp.ui.battle_Dialogue;						
			
			if (gp.ui.commandNum == 0) {		
				running = false;
				
				gp.ui.partyState = gp.ui.party_Main_Select;
				gp.gameState = gp.partyState;								
			}
			
			gp.ui.commandNum = 0;
		}			
	}
	private void announceWinner() throws InterruptedException {
		
		// TRAINER 1 VICTORY
		if (winner == 0) {
			
			gp.ui.fighter_two_X = gp.screenWidth + gp.tileSize;
			
			gp.ui.battleState = gp.ui.battle_End;
			
			gp.stopMusic();
			gp.startMusic(1, 5);					
			
			typeDialogue("Player defeated\nTrainer " + trainer.name + "!", true);
			
			trainer.isDefeated = true;
			trainer.hasBattle = false;
			
			int dialogueSet = trainer.dialogueSet + 1;
			typeDialogue(trainer.dialogues[dialogueSet][0], true);
			
			int moneyEarned = BattleUtility.calculatePay(trainer);
			gp.player.money += moneyEarned;
			typeDialogue(gp.player.name + " got $" + moneyEarned + "\nfor winning!", true);	
			
			fightStage = fight_Evolve;
		}
		// TRAINER 2 VICTORY
		else {
			typeDialogue(gp.player.name + " is out\nof Pokemon!", true);			
			pause(1000);
			
			endBattle();
		}		
	}	
		
	private void turnReset() {
		
		// RESET NON-DELAYED MOVES
		int delay = BattleUtility.getDelay(playerMove, cpuMove);	
		
		if (delay == 0) { 
			playerMove = null; 
			cpuMove = null; 
		}
		else if (delay == 1) {
			cpuMove = null;			
		}
		else if (delay == 2) {
			playerMove = null;
		}
		
		if (delay == 1 || delay == 3) {		
			fightStage = fight_Start;
		}
		else {				
			running = false;
			fightStage = fight_Start;
			gp.ui.battleState = gp.ui.battle_Options;
		}
	}
	
	// CAPTURE POKEMON METHODS
	private void throwPokeball() throws InterruptedException {
		
		if (battleMode == wildBattle) {			
			
			typeDialogue(gp.player.name + " used a\n" + ballUsed.name + "!", false);
			playSE(battle_SE, "ball-throw");
			gp.ui.isFighterCaptured = true;
			playSE(battle_SE, "ball-open");
			playSE(battle_SE, "ball-bounce");
			
			for (int i = 0; i < 3; i++) {
				playSE(battle_SE, "ball-shake");
				Thread.sleep(800);	
			}	
			
			if (BattleUtility.isCaptured(fighter[1], ballUsed)) {				
				capturePokemon();
			}
			else {
				gp.playSE(battle_SE, "ball-open");
				gp.ui.isFighterCaptured = false;		
				gp.playSE(battle_SE, "ball-open");
				typeDialogue("Oh no!\n" + fighter[1].getName() + " broke free!", true);
				fightStage = fight_Start;			
			}	
		}
		else if (battleMode == trainerBattle) {
			typeDialogue("You can't use\nthat here!", true);
			gp.ui.battleState = gp.ui.battle_Options;
			running = false;
		}
		
		ballUsed = null;
	}
	private void capturePokemon() throws InterruptedException {
		
		gp.stopMusic();
		gp.startMusic(1, 3);										
		typeDialogue("Gotcha!\n" + fighter[1].getName() + " was caught!", true);
		
		if (gp.player.pokeParty.size() < 6) {
			fighter[1].resetMovesPP();
			fighter[1].setBall(ballUsed);
			gp.player.pokeParty.add(fighter[1]);
			typeDialogue(fighter[1].getName() + " was added\nto your party!", true);
		}
		else {
			
		}
		
		endBattle();
	}
	
	// RUN AWAY METHOD
	public void escapeBattle() throws InterruptedException {
		/** ESCAPE FORMULA REFERENCE: https://bulbapedia.bulbagarden.net/wiki/Escape **/
		
		boolean escape = false;
		
		if (battleMode == wildBattle) {
			double playerSpeed = fighter[0].getSpeed();
			double wildSpeed = fighter[1].getSpeed();
			
			if (playerSpeed > wildSpeed) {
				escape = true;
			}
			else {
				escapeAttempts++;
				double attempts = (double) escapeAttempts;				
				double escapeOdds = ((((playerSpeed * 128) / wildSpeed) + 30) * attempts);
				
				Random r = new Random();
				int roll = (int) (r.nextInt(255 - 0 + 1) + 0);
				
				if (roll <= escapeOdds) {
					escape = true;
				}
			}	
			
			if (escape) {
				gp.playSE(battle_SE, "run");
				typeDialogue("Got away safely!", true);
				endBattle();
			}
			else {				
				typeDialogue("Oh no!\nYou can't escape!", true);
				fightStage = fight_Start;
			}
		}
		else if (battleMode == trainerBattle) {
			typeDialogue("You can't flee\na trainer battle!", true);
			gp.ui.battleState = gp.ui.battle_Options;
			running = false;
		}
	}
	
	// EVOLVE METHODS
	private void checkEvolve() throws InterruptedException {
		
		gp.stopMusic();
		
		for (int i = 0; i < gp.player.pokeParty.size(); i++) {					
			if (gp.player.pokeParty.get(i).canEvolve()) {
				
				Pokemon oldEvolve = gp.player.pokeParty.get(i);
				Pokemon newEvolve = Pokemon.evolvePokemon(oldEvolve);
				
				gp.ui.evolvePokemon = oldEvolve;
				gp.ui.battleState = gp.ui.battle_Evolve;
				
				gp.playSE(3, gp.se.getFile(3, oldEvolve.toString()));
				
				typeDialogue("What?\n" + oldEvolve.getName() + " is evolving?");
												
				startEvolve(oldEvolve, newEvolve, i);
				
				break;
			}					
		}
		
		endBattle();			
	}
	private void startEvolve(Pokemon oldEvolve, Pokemon newEvolve, int index) throws InterruptedException {
		
		gp.startMusic(1, 0);	
		pause(1000);
		
		for (int i = 0; i < 30; i++) {
									
			if (i % 2 == 0) {
				gp.ui.evolvePokemon = oldEvolve;
			}
			else {
				gp.ui.evolvePokemon = newEvolve;
			}
			
			pause(500);
			
			if (gp.keyH.bPressed) {				
				gp.ui.evolvePokemon = oldEvolve;
				gp.stopMusic();				
				typeDialogue("Oh?\n" + oldEvolve.getName() + " stopped evolving...", true);
				return;
			}
		}
		gp.stopMusic();	
		playSE(3, newEvolve.toString());
		
		gp.player.pokeParty.set(index, newEvolve);				
		gp.ui.evolvePokemon = newEvolve;		
		
		gp.startMusic(1, 2);
		typeDialogue("Congratulations! Your " +  oldEvolve.getName() + 
				"\nevolved into " + newEvolve.getName() + "!", true);
		
		checkNewMove(newEvolve);
		
		return;
	}
	
	// BATTLE END METHOD
	public void endBattle() {
		gp.stopMusic();
		gp.setupMusic();
		
		gp.ui.isFighterCaptured = false;
		gp.ui.commandNum = 0;
		
		gp.particleList.clear();
		gp.gameState = gp.playState;
		
		fightStage = fight_Encounter;
		
		active = false;
		running = false;
		
		for (Pokemon p : gp.player.pokeParty) {
			for (Move m : p.getMoveSet()) {
				m.setTurns(m.getTurns());
			}
		}
				
		currentTurn = -1;
		nextTurn = -1;
		
		trainer = null;
		fighter[0] = null;
		fighter[1] = null;
		newFighter[0] = null;
		newFighter[1] = null;
		otherFighters.clear();
		
		playerMove = null;
		cpuMove = null;		
		newMove = null;
		ballUsed = null;		
				
		weather = Weather.CLEAR;
		weatherDays = -1;
		
		winner = -1;
		loser = -1;
		
		escapeAttempts = 0;
	}

	// MISC METHODS	
	public void typeDialogue(String dialogue) throws InterruptedException {
		
		gp.ui.battleDialogue = "";
		
		for (char letter : dialogue.toCharArray()) {
			gp.ui.battleDialogue += letter;
			pause(35);
		}		
		
		pause(1000);
	}
	public void typeDialogue(String dialogue, boolean canSkip) throws InterruptedException {
		
		gp.ui.battleDialogue = "";
		
		for (char letter : dialogue.toCharArray()) {
			gp.ui.battleDialogue += letter;
			pause(35);
		}		
		
		if (canSkip) {
			gp.ui.canSkip = true;
			waitForKeyPress();
			gp.ui.canSkip = false;
			gp.keyH.aPressed = false;
		}
	}
	
	private void waitForKeyPress() throws InterruptedException {
		while (!gp.keyH.aPressed) {
			pause(5);
		}
	}	
	
	private void increaseHP(Pokemon pkm, int newHP, int damage) throws InterruptedException {
		
		int hpTimer = getHPTimer(damage);
		
		while (pkm.getHP() < newHP) {			
			pkm.setHP(pkm.getHP() + 1);
			pause(hpTimer);
		}	 
	}
	private void decreaseHP(Pokemon pkm, int newHP, int damage) throws InterruptedException {
		
		int hpTimer = getHPTimer(damage);
		
		while (newHP < pkm.getHP()) {			
			pkm.setHP(pkm.getHP() - 1);
			pause(hpTimer);
		}	 
	}
	private int getHPTimer(int damage) {
		
		int hpTimer = 1;
		
		if (damage < 25) hpTimer = 70;
		else if (25 <= damage && damage < 50) hpTimer = 55;
		else if (50 <= damage && damage < 100) hpTimer = 40;
		else if (100 <= damage && damage < 200) hpTimer = 25;
		else if (200 <= damage && damage < 300) hpTimer = 15;
		else if (300 <= damage) hpTimer = 10;
						
		return hpTimer;		
	}
	
	private void pause(int time) throws InterruptedException {
		Thread.sleep(time); 
	}
	public void playSE(int cat, String name) throws InterruptedException {
		
		gp.playSE(cat, name);
		
		double soundDuration = (double)gp.se.getSoundDuration(cat, gp.se.getFile(cat, name));	
		soundDuration /= 0.06;	
		
		Thread.sleep((int) soundDuration);
	}
}