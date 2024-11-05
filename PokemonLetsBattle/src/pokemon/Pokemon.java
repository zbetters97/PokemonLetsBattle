package pokemon;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import entity.Entity;
import moves.Move;
import moves.Moves;
import properties.Nature;
import properties.Status;
import properties.Type;
import properties.Ability;

/*** MOVE CLASS ***/
public class Pokemon {
	
	public enum Protection {
		NONE, BOUNCE, DIG, DIVE, FLY, PHANTOMFORCE, SHADOWFORCE, SKYDROP
	}
	
	/** INITIALIZE VALUES FOR UNIQUE MOVES **/
	private Pokedex pokemon;
	private String nickname = null;
	private char sex;
	private Nature nature;
	private int level, bhp, hp, xp, bxp, nxp;
	private int hpIV, attackIV, defenseIV, spAttackIV, spDefenseIV, speedIV;
	private double speed, attack, defense, spAttack, spDefense, accuracy, evasion;	
	private double speedBase, attackBase, defenseBase, spAttackBase, spDefenseBase, accuracyBase, evasionBase;
	private int speedStg, attackStg, defenseStg, spAttackStg, spDefenseStg, accuracyStg, evasionStg;
	private Status status;
	private boolean isAlive = true, attacking = false, hit = false;
	private int statusCounter, statusLimit;
	private List<Move> moveSet, activeMoves;
	private Protection protectedState;
	private Entity item, capturedBall;	
	/** END INITIALIZE VALUES **/
	
	/** CONSTRUCTORS **/
	public Pokemon(Pokedex p, int lvl, Entity capturedBall) {	
		// STAT FORMULA REFERENCE: https://pokemon.fandom.com/wiki/Statistics
		
		pokemon = p;
		level = lvl;	
		this.capturedBall = capturedBall;
		
		bxp = getXP(level);
		xp = bxp;
		nxp = getNextXP();
		
		// coin flip for Pokemon gender
		sex = Math.random() > 0.5 ? '♂' : '♀';
		
		hpIV = 1 + (int)(Math.random() * ((31 - 1) + 1));			
		attackIV = 1 + (int)(Math.random() * ((31 - 1) + 1));
		defenseIV = 1 + (int)(Math.random() * ((31 - 1) + 1));
		spAttackIV = 1 + (int)(Math.random() * ((31 - 1) + 1));
		spDefenseIV = 1 + (int)(Math.random() * ((31 - 1) + 1));
		speedIV = 1 + (int)(Math.random() * ((31 - 1) + 1));			
		
		hp = (int)(Math.floor(((2 * pokemon.getHP() + hpIV + Math.floor(0.25 * pokemon.getEV())) * level) / 100) + level + 10);
		bhp = hp;
		
		Calculate getStat = (base, IV, EV, lev) -> {
			return (int)(Math.floor(0.01 * (2 * base + IV + Math.floor(0.25 * EV)) * lev)) + 5;
		};		
		
		attackBase = getStat.compute(pokemon.getAttack(), attackIV, pokemon.getEV(), level); 
		defenseBase = getStat.compute(pokemon.getDefense(), defenseIV, pokemon.getEV(), level);		
		spAttackBase = getStat.compute(pokemon.getSpAttack(), spAttackIV, pokemon.getEV(), level); 
		spDefenseBase = getStat.compute(pokemon.getSpDefense(), spDefenseIV, pokemon.getEV(), level);
		speedBase = getStat.compute(pokemon.getSpeed(), speedIV, pokemon.getEV(), level);
		accuracyBase = 1;
		evasionBase = 1;
		
		resetStats();
		resetStatStages();
		
		// random Nature selection
		int num = 0 + (int)(Math.random() * ((Nature.getNatures().size() - 0) + 0));
		nature = Nature.getNatures().get(num);
		
		setNature();
		
		status = null;
		statusCounter = 0;
		statusLimit = 0;
		
		isAlive = true;	
		
		moveSet = new ArrayList<>();
		activeMoves = new ArrayList<>();
		
		protectedState = Protection.NONE;
	}	
	public Pokemon(Pokemon old, Pokedex p) {	
		// STAT FORMULA REFERENCE: https://pokemon.fandom.com/wiki/Statistics
		
		pokemon = p;
		level = old.level;	
		capturedBall = old.capturedBall;
		
		bxp = old.bxp;
		xp = bxp;
		nxp = getNextXP();
		sex = old.sex;
		
		hpIV = old.hpIV;		
		attackIV = old.attackIV;
		defenseIV = old.defenseIV;
		spAttackIV = old.spAttackIV;
		spDefenseIV = old.spDefenseIV;
		speedIV = old.speedIV;		
		
		hp = (int)(Math.floor(((2 * pokemon.getHP() + hpIV + Math.floor(0.25 * pokemon.getEV())) * level) / 100) + level + 10);
		bhp = hp;
		
		Calculate getStat = (base, IV, EV, lev) -> {
			return (int)(Math.floor(0.01 * (2 * base + IV + Math.floor(0.25 * EV)) * lev)) + 5;
		};		
		
		attackBase = getStat.compute(pokemon.getAttack(), attackIV, pokemon.getEV(), level); 
		defenseBase = getStat.compute(pokemon.getDefense(), defenseIV, pokemon.getEV(), level);		
		spAttackBase = getStat.compute(pokemon.getSpAttack(), spAttackIV, pokemon.getEV(), level); 
		spDefenseBase = getStat.compute(pokemon.getSpDefense(), spDefenseIV, pokemon.getEV(), level);
		speedBase = getStat.compute(pokemon.getSpeed(), speedIV, pokemon.getEV(), level);
		accuracyBase = 1;	
		evasionBase = 1;
		
		resetStats();
		resetStatStages();
		
		nature = old.getNature();		
		setNature();
				
		status = old.getStatus();
		statusCounter = 0;
		statusLimit = 0;
		
		isAlive = old.isAlive;		
		
		moveSet = old.getMoveSet();
		activeMoves = new ArrayList<>();
		item = old.item;
		
		protectedState = Protection.NONE;
	}		
	public Pokemon(Pokedex p, Entity capturedBall, char sex, int level, int bxp, int xp, int nxp,
			int hpIV, int attackIV, int defenseIV, int spAttackIV, int spDefenseIV, int speedIV,
			int hp, int bhp, int attack, int defense, int spAttack, int spDefense, int speed, 
			Nature nature, Status status, boolean isAlive, ArrayList<Move> moveSet) {	
		
		pokemon = p;
		this.capturedBall = capturedBall;
		this.sex = sex;		
		this.level = level;			
		this.bxp = xp;
		this.xp = xp;
		
		this.hpIV = hpIV;
		this.attackIV = attackIV;
		this.defenseIV = defenseIV;
		this.spAttackIV = spAttackIV;
		this.spDefenseIV = spDefenseIV;
		this.speedIV = speedIV;
		
		this.hp = hp;
		this.bhp = bhp;
		
		this.attackBase = attack;
		this.defenseBase = defense;
		this.spAttackBase = spAttack;
		this.spDefenseBase = spDefense;
		this.speedBase = speed;
		accuracyBase = 1;
		evasionBase = 1;
		
		resetStats();
		resetStatStages();
		
		this.nature = nature;
		
		this.status = status;		
		statusCounter = 0;
		statusLimit = 0;
						
		this.isAlive = isAlive;	
		
		this.moveSet = moveSet;		
		activeMoves = new ArrayList<>();
		
		protectedState = Protection.NONE;
	}	
	/** END CONSTRUCTORS **/
			
	/** NATURE METHODS **/
	private void setNature() {
								
		// find which values to increase/decrease
		int increase = nature.increase();
		int decrease = nature.decrease();
		
		// increase by 10%
		switch (increase) {
			case (1): attack = Math.rint((double) attack * 1.10); break;
			case (2): defense = Math.rint((double) attack * 1.10); break;
			case (3): spAttack = Math.rint((double) spAttack * 1.10); break;
			case (4): spDefense = Math.rint((double) spDefense * 1.10); break;
			case (5): speed = Math.rint((double) speed * 1.10); break;
		}		
		// decrease by 10%
		switch (decrease) {
			case (1): attack = Math.rint((double) attack * .90); break;
			case (2): defense = Math.rint((double) attack * .90); break;
			case (3): spAttack = Math.rint((double) spAttack * .90); break;
			case (4): spDefense = Math.rint((double) spDefense * .90); break;
			case (5): speed = Math.rint((double) speed * .90); break;
		}	
	}
	/** END NATURE METHODS **/
	
	/** EXP METHODS **/
	private int getXP(int level) {		
		/*** XP CALULCATOR REFERENCE https://bulbapedia.bulbagarden.net/wiki/Experience#Experience_at_each_level ***/
		
		double xp = 0;		
		
		int growth = pokemon.getGrowth();
		
		double n = level;
		double n2 = Math.pow(level, 2);
		double n3 = Math.pow(level, 3);
		
		switch (growth) {
		
			// MEDIUM FAST
			case 0:
				xp = n3;
				break;
				
			// ERRATIC
			case 1:				
				if (1 < level && level < 50) {
					xp = Math.floor( (n3 * (100.0 - n)) / 50.0 );
				}
				else if (50 <= level && level < 68) {
					xp = Math.floor( (n3 * (150.0 - level)) / 100.0 );
				}
				else if (68 <= level && level < 98) {
					xp = n3 * (Math.floor(1911 - (10 * n)) / 3);
				}
				else if (98 <= level && level <= 100) {
					xp = Math.floor( (n3 * (160.0 - level)) / 100.0 );
				}
				break;
				
			// FLUCTUATING
			case 2:
				if (1 < level && level < 15) {
					xp = Math.floor( (n3 * (Math.floor((n + 1.0) / 3.0 ) + 24.0)) / 50.0 );
				}
				else if (15 <= level && level < 36) {
					xp = Math.floor( (n3 * (n + 14.0)) / 50.0 );
				}
				else if (36 <= level && level <= 100) {
					xp = Math.floor( (n3 * (Math.floor(n / 2.0) + 32.0)) / 50.0 );
				}				
				break;
				
			// MEDIUM SLOW
			case 3:				
				xp = ((6.0 / 5.0) * n3) + (-15 * n2) + (100 * n) - 140;				
				break;
				
			// FAST
			case 4:
				xp = Math.floor( (4.0 * n3) / 5.0 );
				break;
				
			// SLOW
			case 5:
				xp = Math.floor( (5.0 * n3) / 4.0 );
				break;
		}
		
		if (xp < 0 || level == 1) xp = 0.0;		
		
		return (int) Math.floor(xp);		
	}
	public int getNextXP() {		
		int nextXP = getXP(level + 1) - getXP(level);		
		if (nextXP < 0) nextXP = 0;
		return nextXP;		
	}
	public boolean checkLevelUp(int gainedXP) {
		
		boolean leveledUp = false;
				
		if (getNextXP() <= xp + gainedXP) {			
			leveledUp = true;
		}
		
		return leveledUp;		
	}
	public void levelUp() { 	
		
		level++;
		int oldBHP = bhp;
		bhp = (int)(Math.floor(((2 * pokemon.getHP() + hpIV + Math.floor(pokemon.getEV() / 4)) * level) / 100) + level + 10);
		
		hp += Math.ceil((bhp - oldBHP));
		if (hp > bhp) hp = bhp;
		
		Calculate getStat = (stat, IV, EV, lev) -> {
			return (int)(Math.floor(0.01 * (2 * stat + IV + Math.floor(EV / 4)) * lev)) + 5;
		};		
		
		attack = getStat.compute(pokemon.getAttack(), attackIV, pokemon.getEV(), level); 
		defense = getStat.compute(pokemon.getDefense(), defenseIV, pokemon.getEV(), level);		
		spAttack = getStat.compute(pokemon.getSpAttack(), spAttackIV, pokemon.getEV(), level); 
		spDefense = getStat.compute(pokemon.getSpDefense(), spDefenseIV, pokemon.getEV(), level);
		speed = getStat.compute(pokemon.getSpeed(), speedIV, pokemon.getEV(), level);
		
		setNature();	
		
		this.bxp = getXP(level);
	}
	public Move getNewMove() {
		
		Move newMove = null;
		
		if (pokemon.getMoveLevelMap() != null) {
			for (Integer lvl : pokemon.getMoveLevelMap().keySet()) {
				if (level == lvl) {
					newMove = new Move(pokemon.getMoveLevelMap().get(lvl));
				}
			}	
		}
		
		return newMove;
	}
	public boolean canEvolve() {
		
		boolean canEvolve = false;
		
		if (pokemon.canEvolve() && pokemon.getEvLevel() <= level) {
			canEvolve = true;
		}
		else {
			canEvolve = false;
		}
		
		return canEvolve;
	}
	public static Pokemon evolve(Pokemon oldPokemon) {
		
		Pokemon evolvedForm = null;
		
		int nextIndex = oldPokemon.getIndex() + 1;
		
		for (Pokedex base : Pokedex.values()) {
			if (base.getIndex() == nextIndex) {
				evolvedForm = new Pokemon(oldPokemon, base);				
				break;
			}
		}		
		
		return evolvedForm;
	}
	/** END EXP METHODS **/

	/** MOVE METHODS **/
	private static void mapMoves(Pokemon p) {
		
		// if pokemon not already mapped to moveset
        if (p.getMoveSet().isEmpty()) {
        	
        	// add each move to passed in pokemon object
            for (int i = 0; i < Pokedex.getMovemap().get(p.pokemon).size(); i++) {        	
            	p.learnMove(Pokedex.getMovemap().get(p.pokemon).get(i));
            } 	
        }
	}
	public void addMove(Moves move) {
		if (this.getMoveSet().size() < 4) {
			this.getMoveSet().add(new Move(move));
		}
	}
	public void addMoves(List<Moves> moves) {			
		this.getMoveSet().clear();
		for (Moves move : moves) {
			if (this.getMoveSet().size() < 4) {
				this.getMoveSet().add(new Move(move));
			}
		}
	}
	public boolean learnMove(Move move) { 
		
		if (this.getMoveSet().size() == 4) {
			return false;
		}
		else {
			this.getMoveSet().add(move);
			return true;
		}
	}
	public boolean forgetMove(Move move) {
		
		for (int i = 0; i < moveSet.size(); i++) {
			if (moveSet.get(i).getName().equals(move.getName())) {
				moveSet.remove(i);
				return true;
			}
		}
		
		return false;
	}
	public boolean replaceMove(Move oldMove, Move newMove) {
		
		for (int i = 0; i < moveSet.size(); i++) {
			if (moveSet.get(i).getName().equals(oldMove.getName())) {
				moveSet.remove(i);
				moveSet.add(i, newMove);
				return true;
			}
		}
		
		return false;
	}
	
	public void resetMoves() {		
		for (Move m : moveSet) {
			m.resetPP();
			m.resetMoveTurns();
		}
		for (Move m : activeMoves) {
			m.setTurnCount(m.getTurns());
			activeMoves.remove(m);
		}
	}
	public void resetMoveTurns() {
		for (Move m : moveSet) {
			m.resetMoveTurns();
		}
	}
	
	/** ACTIVE MOVES METHODS **/
	public void addActiveMove(Moves move) {
		activeMoves.add(new Move(move));
	}	
	public boolean hasActiveMove(Moves move) {		
		return activeMoves.stream().anyMatch(m -> m.getMove().equals(move));
	}
	public Move getActiveMove(Moves move) {		
		return activeMoves.stream().filter(m -> m.getMove().equals(move)).findAny().orElse(null);
	}
	public void removeActiveMove(Move move) {
		activeMoves.remove(move);		
	}
	public void removeActiveMove(Moves move) {
		activeMoves.removeIf(m -> m.getMove() == move);
		
	}
	public void clearActiveMoves() {
		activeMoves.clear();		
	}
	/** END ADD NEW MOVE METHOD **/
	
	/** GET POKEMON METHODS **/	
	public static Pokemon getPokemon(Pokedex poke, int level, Entity capturedBall) {
		
		Pokemon pokemon = null;
		
		for (Pokedex p : Pokedex.values()) {
			if (p == poke) {
				pokemon = new Pokemon(p, level, capturedBall);
				break;
			}
		}
		
		mapMoves(pokemon);		
		
		return pokemon;		
	}	
	public static Pokemon getPokemonByIndex(int index, int level, Entity capturedBall) {
		
		Pokemon pokemon = null;
		
		for (Pokedex p : Pokedex.values()) {
			if (p.getIndex() == index) {
				pokemon = new Pokemon(p, level, capturedBall);
				break;
			}
		}
		
		mapMoves(pokemon);		
		
		return pokemon;		
	}	
	/** END GET POKEMON METHODS **/
	
	/** GETTERS AND SETTERS **/	
	public String getNickname() { return nickname; }	
	public void setNickname(String nickname) { this.nickname = nickname; }	
	public char getSex() { return sex; }
	public void setSex(char sex) { this.sex = sex; }	
	public Color getSexColor() {
		if (sex == '♂') return Color.BLUE;		
		else return Color.RED;		
	}
	
	public int getLevel() {	return level; }	
	
	public int getBXP() { return bxp; }
	public void setBXP(int bxp) {	this.bxp = bxp; }
	public int getXP() { return xp; }
	public void setXP(int xp) {	this.xp = xp; }
	public int getNXP() { return nxp; }

	public int getHP() { return hp; }
	public void addHP(int hp) { 
		this.hp += hp; 
		if (this.hp > bhp) 
			this.hp = bhp; 
	}
	public void setHP(int hp) {	this.hp = hp; }	
	public int getBHP() { return bhp; }
	public void setBHP(int bhp) {	this.bhp = bhp; }

	public double getSpeed() { return speed; }
	public void setSpeed(int speed) { this.speed = speed; }
	public double getAttack() { return attack; }
	public void setAttack(int attack) {	this.attack = attack; }
	public double getDefense() { return defense; }
	public void setDefense(int defense) { this.defense = defense; }
	
	public double getSpAttack() { return spAttack; }
	public void setSpAttack(int spAttack) { this.spAttack = spAttack; }
	public double getSpDefense() {	return spDefense; }
	public void setSpDefense(int spDefense) { this.spDefense = spDefense; }	
	public double getAccuracy() { return accuracy; }
	public void setAccuracy(int accuracy) { this.accuracy = accuracy; }	
	public double getEvasion() { return evasion; }
	public void setEvasion(int evasion) { this.evasion = evasion; }
	
	public int getSpeedStg() { return speedStg; }
	public void setSpeedStg(int speedStg) { this.speedStg = speedStg; }
	public int getAttackStg() { return attackStg; }
	public void setAttackStg(int attackStg) { this.attackStg = attackStg; }
	public int getDefenseStg() { return defenseStg; }
	public void setDefenseStg(int defenseStg) { this.defenseStg = defenseStg; }
	public int getSpAttackStg() { return spAttackStg; }
	public void setSpAttackStg(int spAttackStg) { this.spAttackStg = spAttackStg; }
	public int getSpDefenseStg() { return spDefenseStg; }
	public void setSpDefenseStg(int spDefenseStg) { this.spDefenseStg = spDefenseStg; }
	public int getAccuracyStg() { return accuracyStg; }
	public void setAccuracyStg(int accuracyStg) { this.accuracyStg = accuracyStg; }
	public int getEvasionStg() { return evasionStg; }
	public void setEvasionStg(int evasionStg) { this.evasionStg = evasionStg; }
		
	public int getEXPYeild() { return pokemon.getEXPYeild(); }
	public int getEV() { return pokemon.getEV(); }
	
	public int getHPIV() { return hpIV; }
	public int getAttackIV() { return attackIV; }
	public int getDefenseIV() { return defenseIV; }
	public int getSpAttackIV() { return spAttackIV; }
	public int getSpDefenseIV() { return spDefenseIV; }
	public int getSpeedIV() { return speedIV; }

	public Status getStatus() { return status; }
	public void setStatus(Status status) { this.status = status; }	
	public boolean hasStatus(Status status) { return (this.status != null && this.status == status);}
	public int getStatusCounter() { return statusCounter; }
	public void setStatusCounter(int statusCounter) { this.statusCounter = statusCounter; }	
	public int getStatusLimit() { return statusLimit; }
	public void setStatusLimit(int statusLimit) { this.statusLimit = statusLimit; }
	
	public boolean isAlive() { return isAlive; }
	public void setAlive(boolean isAlive) {	
		this.isAlive = isAlive; 
		hp = 0;		
		status = null; 
		statusLimit = 0;
		statusCounter = 0;
		getAbility().setActive(false);
		resetMoves();
		resetStats();
		resetStatStages();
		clearProtection();
	}
	
	public boolean getAttacking() { return attacking; }
	public void setAttacking(boolean attacking) { this.attacking = attacking; }
	
	public boolean getHit() { return hit; }
	public void setHit(boolean hit) { this.hit = hit; }
		
	public List<Move> getMoveSet() { return moveSet; }
	public void setMoveSet(ArrayList<Move> moveSet) { this.moveSet = moveSet; }
	
	public Entity getHeldItem() { return item; }
	public void giveItem(Entity item) { this.item = item; }
	
	public List<Move> getActiveMoves() { return activeMoves; }
	public void setActiveMoves(List<Move> activeMoves) { this.activeMoves = activeMoves; }
	
	public Protection getProtectedState() { return protectedState; }
	public void setProtectedState(Protection state) { this.protectedState = state; }
	public void clearProtection() { protectedState = Protection.NONE; }
		
	public Entity getBall() { return capturedBall; }
	public void setBall(Entity capturedBall) { this.capturedBall = capturedBall; }
	
	public Pokedex getPokemon() { return pokemon; }
	/** END GETTERS AND SETTERS **/
	
	/** GETTERS **/
	public String toString() { return pokemon.getName(); }
	public String getName() { 
		if (nickname != null) return nickname;		
		else return pokemon.getName().toUpperCase();		
	}
	public int getIndex() {	return pokemon.getIndex(); }
	public Type getType() { return pokemon.getType(); }
	public List<Type> getTypes() { return pokemon.getTypes(); }	
	public boolean checkType(Type type) {
		
		boolean isType = false;
		
		if (pokemon.getTypes() != null) {
			if (pokemon.getTypes().contains(type)) {
				isType = true;
			}
		}
		else if (pokemon.getType() == type) {
			isType = true;
		}
		
		return isType;		
	}
	public Ability getAbility() { return pokemon.getAbility(); }		
	public Nature getNature() { return this.nature; }	
	
	public int getCatchRate() { return pokemon.getCatchRate(); }
	
	public BufferedImage getFrontSprite() { return pokemon.getFrontSprite(); }
	public BufferedImage getBackSprite() { return pokemon.getBackSprite(); }
	public BufferedImage getMenuSprite() { return pokemon.getMenuSprite(); }	
	/** END GETTERS **/
		
	public String changeStat(String stat, int level) {	
		
		String output = "";
		
		int difference = 0;
		int change = 0;				
		
		switch (stat) {		
			case "attack":							
				
				difference = attackStg + level;
				
				if (level > 0) {
					while (attackStg < difference && attackStg < 6) { 
						attackStg++; change++; 
					}
					if (change == 0) { 
						output = getName() + "'s " + stat + "\nwon't go any higher!";
					}
					else { 
						attack = Math.floor(attackBase * ((2.0 + attackStg) / 2.0)); 
						output = outputChange(stat, change); 
					}
				}
				else if (level < 0) {
					while (attackStg > difference && attackStg > -6) { 
						attackStg--; change--; 
					}
					if (change == 0) { 
						output = getName() + "'s " + stat + "\nwon't go any lower!";
					}
					else { 
						attack = Math.floor(attackBase * (2.0 / (2.0 - attackStg))); 
						output = outputChange(stat, change); 
					}
				}
				
				break;			
				
			case "sp. attack":	
				
				difference = spAttackStg + level;
				
				if (level > 0) {
					while (spAttackStg < difference && spAttackStg < 6) { 
						spAttackStg++; change++; 
					}
					if (change == 0) { 
						output = getName() + "'s " + stat + "\nwon't go any higher!";
					}
					else { 
						spAttack = Math.floor(spAttackBase * ((2.0 + spAttackStg) / 2.0)); 
						output = outputChange(stat, change); 
					}
				}
				else if (level < 0) {
					while (spAttackStg > difference && spAttackStg > -6) { 
						spAttackStg--; change--; 
					}
					if (change == 0) { 
						output = getName() + "'s " + stat + "\nwon't go any lower!";
					}
					else { 
						spAttack = Math.floor(spAttackBase * (2.0 / (2.0 - spAttackStg))); 
						output = outputChange(stat, change); 
					}
				}				
				break;
				
			case "defense":
				
				difference = defenseStg + level;
				
				if (level > 0) {
					while (defenseStg < difference && defenseStg < 6) { 
						defenseStg++; change++; 
					}
					if (change == 0) { 
						output = getName() + "'s " + stat + "\nwon't go any higher!";
					}
					else { 
						defense = Math.floor(defenseBase * ((2.0 + defenseStg) / 2.0)); 
						output = outputChange(stat, change); 
					}
				}
				else if (level < 0) {
					while (defenseStg > difference && defenseStg > -6) { 
						defenseStg--; change--; 
					}
					if (change == 0) { 
						output = getName() + "'s " + stat + "\nwon't go any lower!";
					}
					else { 
						defense = Math.floor(defenseBase * (2.0 / (2.0 - defenseStg))); 
						output = outputChange(stat, change); 
					}
				}
				break;
				
			case "sp. defense":
				
				difference = spDefenseStg + level;
				
				if (level > 0) {
					while (spDefenseStg < difference && spDefenseStg < 6) { 
						spDefenseStg++; change++; 
					}
					if (change == 0) { 
						output = getName() + "'s " + stat + "\nwon't go any higher!";
					}
					else { 
						spDefense = Math.floor(spDefenseBase * ((2.0 + spDefenseStg) / 2.0)); 
						output = outputChange(stat, change); 
					}
				}
				else if (level < 0) {
					while (spDefenseStg > difference && spDefenseStg > -6) { 
						spDefenseStg--; change--; 
					}
					if (change == 0) { 
						output = getName() + "'s " + stat + "\nwon't go any lower!";
					}
					else { 
						spDefense = Math.floor(spDefenseBase * (2.0 / (2.0 - spDefenseStg))); 
						output = outputChange(stat, change); 
					}
				}
				break;
				
			case "speed":
				
				difference = speedStg + level;
				
				if (level > 0) {
					while (speedStg < difference && speedStg < 6) { 
						speedStg++; change++; 
					}
					if (change == 0) { 
						output = getName() + "'s " + stat + "\nwon't go any higher!";
					}
					else { 
						speed = Math.floor(speedBase * ((2.0 + speedStg) / 2.0)); 
						output = outputChange(stat, change); 
					}
				}
				else if (level < 0) {
					while (speedStg > difference && speedStg > -6) { 
						speedStg--; change--; 
					}
					if (change == 0) { 
						output = getName() + "'s " + stat + "\nwon't go any lower!";
					}
					else { 
						speed = Math.floor(speedBase * (2.0 / (2.0 - speedStg))); 
						output = outputChange(stat, change); 
					}
				}
				break;
				
			case "accuracy":
				
				difference = accuracyStg + level;
				
				if (level > 0) {
					while (accuracyStg < difference && accuracyStg < 6) { 
						accuracyStg++; change++; 
					}
					if (change == 0) { 
						output = getName() + "'s " + stat + "\nwon't go any higher!";
					}
					else { 
						accuracy = Math.floor(accuracyBase * ((3.0 + accuracyStg) / 3.0)); 
						output = outputChange(stat, change); 
					}
				}
				else if (level < 0) {
					while (accuracyStg > difference && accuracyStg > -6) { 
						accuracyStg--; change--; 
					}
					if (change == 0) { 
						output = getName() + "'s " + stat + "\nwon't go any lower!";
					}
					else { 
						accuracy = Math.floor(accuracyBase * (3.0 / (3.0 - accuracyStg))); 
						output = outputChange(stat, change); 
					}
				}				
				break;
				
			case "evasion":
				
				difference = attackStg + level;
				
				if (level > 0) {
					while (evasionStg < difference && evasionStg < 6) { 
						evasionStg++; change++; 
					}
					if (change == 0) { 
						output = getName() + "'s " + stat + "\nwon't go any higher!";
					}
					else { 
						evasion = Math.floor(evasionBase * ((3.0 + evasionStg) / 3.0)); 
						output = outputChange(stat, change); 
					}
				}
				else if (level < 0) {
					while (evasionStg > difference && evasionStg > -6) { 
						evasionStg--; change--; 
					}
					if (change == 0) { 
						output = getName() + "'s " + stat + "\nwon't go any lower!";
					}
					else { 
						evasion = Math.floor(evasionBase * (3.0 / (3.0 - evasionStg))); 
						output = outputChange(stat, change); 
					}
				}				
				break;
		}
		
		return output;
	}
	private String outputChange(String stat, int level) {
		
		String output = "";
		
		if (level == 1) output = getName() + "'s " + stat + "\nrose!";
		else if (level == 2) output = getName() + "'s " + stat + "\ngreatly rose!";
		else if (level >= 3) output = getName() + "'s " + stat + "\ndrastically rose!";
		else if (level == -1) output = getName() + "'s " + stat + "\nfell!";
		else if (level == -2) output = getName() + "'s " + stat + "\ngreatly fell!";
		else if (level <= -3) output = getName() + "'s " + stat + "\nseverely fell!";
		
		return output;
	}
	
	public void resetStats() {
		attack = attackBase;
		defense = defenseBase;
		spAttack = spAttackBase;
		spDefense = defenseBase;
		speed = speedBase;
		accuracy = accuracyBase;
		evasion = evasionBase;
	}
	public void resetStatStages() {		
		speedStg = 0;
		attackStg = 0;
		defenseStg = 0;
		spAttackStg= 0;
		spDefenseStg = 0;
		accuracyStg = 0;
		evasionStg = 0;
	}
}
/*** END MOVE CLASS ***/

@FunctionalInterface
interface Calculate {
	public int compute(int j, int k, int l, int m);
}