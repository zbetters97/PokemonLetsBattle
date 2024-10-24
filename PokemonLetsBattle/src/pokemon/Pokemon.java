package pokemon;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import entity.Entity;
import moves.Move;
import properties.Nature;
import properties.Status;
import properties.Type;
import properties.abilities.Ability;

/*** MOVE CLASS ***/
public class Pokemon {
	
	/** INITIALIZE VALUES FOR UNIQUE MOVES **/
	private Pokedex pokemon;
	private String nickname = null;
	private char sex;
	private Nature nature;
	private int level, bhp, hp, xp, bxp, nxp;
	private int hpIV, attackIV, defenseIV, spAttackIV, spDefenseIV, speedIV;
	private double speed, attack, defense, spAttack, spDefense, accuracy;	
	private int speedStg, attackStg, defenseStg, spAttackStg, spDefenseStg, accuracyStg;
	private Status status;
	private boolean isAlive;
	private boolean attacking = false;
	private boolean hit = false;	
	private boolean waiting = false, isProtected = false;
	private int statusCounter, statusLimit;
	private List<Move> moveSet;
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
		
		attack = getStat.compute(pokemon.getAttack(), attackIV, pokemon.getEV(), level); 
		defense = getStat.compute(pokemon.getDefense(), defenseIV, pokemon.getEV(), level);		
		spAttack = getStat.compute(pokemon.getSpAttack(), spAttackIV, pokemon.getEV(), level); 
		spDefense = getStat.compute(pokemon.getSpDefense(), spDefenseIV, pokemon.getEV(), level);
		speed = getStat.compute(pokemon.getSpeed(), speedIV, pokemon.getEV(), level);
		accuracy = 1;	
		
		// random Nature selection
		int num = 0 + (int)(Math.random() * ((Nature.getNatures().size() - 0) + 0));
		nature = Nature.getNatures().get(num);
		
		setNature();
				
		speedStg = 0;
		attackStg = 0;
		defenseStg = 0;
		spAttackStg= 0;
		spDefenseStg = 0;
		accuracyStg = 0;
		
		status = null;
		statusCounter = 0;
		statusLimit = 0;
		
		isAlive = true;	
		
		moveSet = new ArrayList<>();
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
		
		attack = getStat.compute(pokemon.getAttack(), attackIV, pokemon.getEV(), level); 
		defense = getStat.compute(pokemon.getDefense(), defenseIV, pokemon.getEV(), level);		
		spAttack = getStat.compute(pokemon.getSpAttack(), spAttackIV, pokemon.getEV(), level); 
		spDefense = getStat.compute(pokemon.getSpDefense(), spDefenseIV, pokemon.getEV(), level);
		speed = getStat.compute(pokemon.getSpeed(), speedIV, pokemon.getEV(), level);
		accuracy = 1;	
		
		nature = old.getNature();		
		setNature();
				
		speedStg = 0;
		attackStg = 0;
		defenseStg = 0;
		spAttackStg= 0;
		spDefenseStg = 0;
		accuracyStg = 0;
		
		status = old.getStatus();
		statusCounter = 0;
		statusLimit = 0;
		
		isAlive = old.isAlive;		
		
		moveSet = old.getMoveSet();
		item = old.item;
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
	public static Pokemon evolvePokemon(Pokemon oldPokemon) {
		
		Pokemon evolvedForm = null;
		
		int nextIndex = oldPokemon.getIndex() + 1;
		
		for (Pokedex base : Pokedex.getPokemonList()) {
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
	
	public void resetMovesPP() {		
		for (int i = 0; i < moveSet.size(); i++) {			
			moveSet.get(i).resetpp();			
		}		
	}
	/** END ADD NEW MOVE METHOD **/
	
	/** GET POKEMON METHODS **/	
	public static Pokemon getPokemon(Pokedex poke, int level, Entity capturedBall) {
		
		Pokemon pokemon = null;
		
		for (Pokedex p : Pokedex.getPokemonList()) {
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
		
		for (Pokedex p : Pokedex.getPokemonList()) {
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
	public int getStatusCounter() { return statusCounter; }
	public void setStatusCounter(int statusCounter) { this.statusCounter = statusCounter; }	
	public int getStatusLimit() { return statusLimit; }
	public void setStatusLimit(int statusLimit) { this.statusLimit = statusLimit; }
	
	public boolean isAlive() { return isAlive; }
	public void setAlive(boolean isAlive) {	
		this.isAlive = isAlive; 
		this.status = null; 
		this.waiting = false; 
		this.isProtected = false;
	}
	
	public boolean getAttacking() { return attacking; }
	public void setAttacking(boolean attacking) { this.attacking = attacking; }
	
	public boolean getHit() { return hit; }
	public void setHit(boolean hit) { this.hit = hit; }
	
	public List<Move> getMoveSet() { return moveSet; }
	public void setMoveSet(ArrayList<Move> moveSet) { this.moveSet = moveSet; }
	
	public Entity getHeldItem() { return item; }
	public void giveItem(Entity item) { this.item = item; }
	
	public Entity getBall() { return capturedBall; }
	public void setBall(Entity capturedBall) { this.capturedBall = capturedBall; }
	
	public boolean isWatiing() { return waiting; }
	public void setWaiting(boolean waiting) { this.waiting = waiting; }
	
	public boolean isProtected() { return isProtected; }
	public void setProtected(boolean isProtected) { this.isProtected = isProtected; }
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
		
		switch (stat) {
			case "attack":
				if (this.attackStg + level > 6 || this.attackStg + level < -6) {
					if (level >= 1) 
						output = getName() + "'s attack won't go any higher!";
					else if (level <= -1) 
						output = getName() + "'s attack won't go any lower!";
				}
				else {	
					this.attackStg += level;
					this.attack *= Math.max(2, 2 + (double) this.attackStg) / Math.max(2, 2 - (double) this.attackStg);	
		
					output = outputChange(stat, level);
				}	
				break;
			case "sp. attack":
				if (this.spAttackStg + level > 6 || this.spAttackStg + level < -6) {
					if (level >= 1) 
						output = getName() + "'s sp. attack won't go any higher!";
					else if (level <= -1) 
						output = getName() + "'s sp. attack won't go any lower!";					
				}
				else {	
					this.spAttackStg += level;
					this.spAttack *= Math.max(2, 2 + (double) this.spAttackStg) / Math.max(2, 2 - (double) this.spAttackStg);	
					
					output = outputChange(stat, level);
				}
				break;
			case "defense":
				if (this.defenseStg + level > 6 || this.defenseStg + level < -6) {
					if (level >= 1) 
						output = getName() + "'s defense won't go any higher!";
					else if (level <= -1) 
						output = getName() + "'s defense won't go any lower!";
				}
				else {	
					this.defenseStg += level;
					this.defense *= Math.max(2, 2 + (double) this.defenseStg) / Math.max(2, 2 - (double) this.defenseStg);
					
					output = outputChange(stat, level);
				}	
				break;
			case "sp. defense":
				if (this.spDefenseStg + level > 6 || this.spDefenseStg  + level < -6) {
					if (level >= 1) 
						output = getName() + "'s sp. defense won't go any higher!";
					else if (level <= -1) 
						output = getName() + "'s sp. defense won't go any lower!";
				}
				else {	
					this.spDefenseStg  += level;
					this.spDefense *= Math.max(2, 2 + (double) this.spDefenseStg ) / Math.max(2, 2 - (double) this.spDefenseStg);	
					
					output = outputChange(stat, level);
				}	
				break;
			case "speed":
				if (this.speedStg + level > 6 || this.speedStg + level < -6) {
					if (level >= 1) 
						output = getName() + "'s speed won't go any higher!";
					else if (level <= -1) 
						output = getName() + "'s speed won't go any lower!";
				}
				else {	
					this.speedStg += level;					
					this.speed *= Math.max(2, 2 + (double) this.speedStg) / Math.max(2, 2 - (double) this.speedStg);	
					
					output = outputChange(stat, level);
				}	
				break;
			case "accuracy":
				if (this.accuracyStg + level > 6 || this.accuracyStg + level < -6) {
					if (level >= 1) 
						output = getName() + "'s accuracy won't go any higher!";
					else if (level <= -1) 
						output = getName() + "'s accuracy won't go any lower!";
				}
				else {	
					this.accuracyStg += level;
					this.accuracy = Math.max(3, 3 + (double) this.accuracyStg) / Math.max(3, 3 - (double) this.accuracyStg);	
					
					output = outputChange(stat, level);
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
}
/*** END MOVE CLASS ***/

@FunctionalInterface
interface Calculate {
	public int compute(int j, int k, int l, int m);
}