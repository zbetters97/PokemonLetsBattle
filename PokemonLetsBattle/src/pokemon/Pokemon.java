package pokemon;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import moves.Move;
import properties.Nature;
import properties.Status;
import properties.Type;

/*** MOVE CLASS ***/
public class Pokemon {
	
	/** INITIALIZE VALUES FOR UNIQUE MOVES **/
	private PokemonBase pokemon;
	private String nickname = null;
	private char sex;
	private Nature nature;
	private int level, bhp, hp, xp, nxp;
	private int hpIV, attackIV, defenseIV, spAttackIV, spDefenseIV, speedIV;
	private double speed, attack, defense, spAttack, spDefense, accuracy;	
	private int speedStg, attackStg, defenseStg, spAttackStg, spDefenseStg, accuracyStg;
	private Status status;
	private boolean isAlive;
	public boolean isHit = false;
	private int statusCounter, statusLimit;
	private List<Move> moveSet;
	/** END INITIALIZE VALUES **/
	
	/** CONSTRUCTORS **/
	public Pokemon(PokemonBase pokemon, int level) {	
		// STAT FORMULA REFERENCE: https://pokemon.fandom.com/wiki/Statistics
		
		this.pokemon = pokemon;
		this.level = level;	
		this.xp = getXP(level);
		this.nxp = getNextXP();
		
		// coin flip for Pokemon gender
		this.sex = Math.random() > 0.5 ? '♂' : '♀';
		
		hpIV = 1 + (int)(Math.random() * ((31 - 1) + 1));			
		attackIV = 1 + (int)(Math.random() * ((31 - 1) + 1));
		defenseIV = 1 + (int)(Math.random() * ((31 - 1) + 1));
		spAttackIV = 1 + (int)(Math.random() * ((31 - 1) + 1));
		spDefenseIV = 1 + (int)(Math.random() * ((31 - 1) + 1));
		speedIV = 1 + (int)(Math.random() * ((31 - 1) + 1));			
		
		this.hp = (int)(Math.floor(((2 * pokemon.getHP() + hpIV + Math.floor(0.25 * pokemon.getEV())) * level) / 100) + level + 10);
		this.bhp = this.hp;
		
		Calculate getStat = (base, IV, EV, lev) -> {
			return (int)(Math.floor(0.01 * (2 * base + IV + Math.floor(0.25 * EV)) * lev)) + 5;
		};		
		
		this.attack = getStat.compute(pokemon.getAttack(), attackIV, pokemon.getEV(), level); 
		this.defense = getStat.compute(pokemon.getDefense(), defenseIV, pokemon.getEV(), level);		
		this.spAttack = getStat.compute(pokemon.getSpAttack(), spAttackIV, pokemon.getEV(), level); 
		this.spDefense = getStat.compute(pokemon.getSpDefense(), spDefenseIV, pokemon.getEV(), level);
		this.speed = getStat.compute(pokemon.getSpeed(), speedIV, pokemon.getEV(), level);
		this.accuracy = 1;	
		
		// random Nature selection
		int num = 0 + (int)(Math.random() * ((Nature.getNatures().size() - 0) + 0));
		nature = Nature.getNatures().get(num);
		
		setNature();
				
		this.speedStg = 0;
		this.attackStg = 0;
		this.defenseStg = 0;
		this.spAttackStg= 0;
		this.spDefenseStg = 0;
		this.accuracyStg = 0;
		
		this.status = null;
		this.isAlive = true;	
		
		this.statusCounter = 0;
		this.statusLimit = 0;
		
		moveSet = new ArrayList<>();
	}		
	/** END CONSTRUCTORS **/
			
	/** NATURE METHODS **/
	public void setNature() {
								
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
	public Pokemon evolve() {
		
		Pokemon newPokemon = null;
		
		newPokemon = getPokemonByIndex(pokemon.getIndex() + 1, level);
		
		return newPokemon;		
	}
	/** END EXP METHODS **/

	/** ADD NEW MOVE METHOD **/
	public boolean addMove(Move move) { 
		
		if (this.getMoveSet().size() == 4) {
			return false;
		}
		else {
			this.getMoveSet().add(move);
			return true;
		}
	}
	/** END ADD NEW MOVE METHOD **/
	
	/** GET POKEMON METHODS **/	
	public static Pokemon getPokemon(String name, int level) {
		
		Pokemon pokemon = null;
		
		for (PokemonBase p : PokemonBase.getPOKEDEX()) {
			if (p.getName().equals(name)) {
				pokemon = new Pokemon(p, level);
				break;
			}
		}
		
		mapMoves(pokemon);		
		
		return pokemon;		
	}	
	public static Pokemon getPokemonByIndex(int index, int level) {
		
		Pokemon pokemon = null;
		
		for (PokemonBase p : PokemonBase.getPOKEDEX()) {
			if (p.getIndex() == index) {
				pokemon = new Pokemon(p, level);
				break;
			}
		}
		
		mapMoves(pokemon);		
		
		return pokemon;		
	}	
	/** END GET POKEMON METHODS **/
	
	/** MAP MOVES METHOD **/
	private static void mapMoves(Pokemon p) {
		
		// if pokemon not already mapped to moveset
        if (p.getMoveSet().isEmpty()) {
        	
        	// add each move to passed in pokemon object
            for (int i = 0; i < PokemonBase.getMovemap().get(p.pokemon).size(); i++) {        	
            	p.addMove(PokemonBase.getMovemap().get(p.pokemon).get(i));
            } 	
        }
	}
	/** END MAP MOVES METHOD **/
	
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
	public void levelUp() { 	
		
		level++;
		
		this.bhp = (int)(Math.floor(((2 * pokemon.getHP() + hpIV + Math.floor(pokemon.getEV() / 4)) * level) / 100) + level + 10);
		
		Calculate getStat = (stat, IV, EV, lev) -> {
			return (int)(Math.floor(0.01 * (2 * stat + IV + Math.floor(EV / 4)) * lev)) + 5;
		};		
		
		attack = getStat.compute(pokemon.getAttack(), attackIV, pokemon.getEV(), level); 
		defense = getStat.compute(pokemon.getDefense(), defenseIV, pokemon.getEV(), level);		
		spAttack = getStat.compute(pokemon.getSpAttack(), spAttackIV, pokemon.getEV(), level); 
		spDefense = getStat.compute(pokemon.getSpDefense(), spDefenseIV, pokemon.getEV(), level);
		speed = getStat.compute(pokemon.getSpeed(), speedIV, pokemon.getEV(), level);
		
		setNature();	
	}
	
	public int getXP() { return xp; }
	public void setXP(int xp) {	this.xp = xp; }
	public int getNXP() { return nxp; }

	public int getHP() { return hp; }
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
	public void setAlive(boolean isAlive) {	this.isAlive = isAlive; this.setStatus(null); }
	
	public boolean isHit() { return isHit; }
	public void setHit(boolean isHit) { this.isHit = isHit; }
	
	public List<Move> getMoveSet() { return moveSet; }
	public void setMoveSet(ArrayList<Move> moveSet) { this.moveSet = moveSet; }
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
	public Nature getNature() { return this.nature; }		
	
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