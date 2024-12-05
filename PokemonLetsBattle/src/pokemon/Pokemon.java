package pokemon;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.imageio.ImageIO;

import application.GamePanel;
import entity.Entity;
import moves.Move;
import moves.Moves;
import properties.*;

public class Pokemon {
	/** STAT, ABILITY, AND MOVE REFERECE: https://www.serebii.net/pokemon/ **/
	/** EXP & EV REFERENCE: https://bulbapedia.bulbagarden.net/wiki/List_of_Pok%C3%A9mon_by_effort_value_yield_in_Generation_IV **/
	/** XP GROWTH REFERENCE: https://bulbapedia.bulbagarden.net/wiki/List_of_Pok%C3%A9mon_by_experience_type **/	
	/** CATCH RATE REFERENCE: https://bulbapedia.bulbagarden.net/wiki/List_of_Pokémon_by_catch_rate **/	
		
	public static enum Pokedex {
		BULBUSAUR, IVYSAUR, VENUSAUR, 
		CHARMANDER, CHARMELEON, CHARIZARD,
		SQUIRTLE, WARTORTLE, BLASTOISE,
		PIKACHU, RAICHU,
		ZUBAT, GOLBAT,
		GROWLITHE, ARCANINE,		
		ABRA, KADABRA, ALAKAZAM,
		MACHOP, MACHOKE, MACHAMP,
		GEODUDE, GRAVELER, GOLEM,
		PONYTA, RAPIDASH,
		GASTLY, HAUNTER, GENGAR,
		HITMONLEE, HITMONCHAN,		
		HORSEA, SEADRA, 
		MAGIKARP, GYARADOS,
		LAPRAS, SNORLAX,
		ARTICUNO, ZAPDOS, MOLTRES, 
		DRATINI, DRAGONAIR, DRAGONITE,
		MEWTWO, MEW,
		CHIKORITA, BAYLEEF, MEGANIUM,
		CYNDAQUIL, QUILAVA, TYPHLOSION,
		TOTODILE, CROCONAW, FERALIGATR,		
		CROBAT, 
		KINGDRA,
		RAIKOU, ENTEI, SUICUNE, 
		LUGIA, HOOH, CELEBI,		
		TREECKO, GROVYLE, SCEPTILE,
		TORCHIC, COMBUSKEN, BLAZIKEN,
		MUDKIP, MARSHTOMP, SWAMPERT,
		POOCHYENA, MIGHTYENA,
		ZIGZAGOON, LINOONE,
		RALTS, KIRLIA, GARDEVOIR,
		NINCADA, NINJASK, SHEDINJA,
		WHISMUR, LOUDRED, EXPLOUD,
		SPHEAL, SEALEO, WALREIN,
		BAGON, SHELGON, SALAMENCE,
		BELDUM, METANG, METAGROSS,
		KYOGRE, GROUDON, RAYQUAZA,
		JIRACHI, DEOXYS	
	}
	
	protected enum Growth {
		MEDIUMFAST, ERATIC, FLUCTUATING, MEDIUMSLOW, FAST, SLOW
	}	
	public enum Protection {
		NONE, BOUNCE, DIG, DIVE, FLY, PHANTOMFORCE, SHADOWFORCE, SKYDROP
	}
	
	private String uniqueID;
	protected Pokedex id;
	protected int index;
	protected String name, nickname;
	protected BufferedImage frontSprite, backSprite, menuSprite;
	protected char sex;
	protected Type type = null;
	protected List<Type> types = null;
	protected Nature nature;
	protected Ability ability;
	protected Growth growth;
	protected Status status = null;
	protected Protection protection = Protection.NONE;
	protected Entity ball = null, item = null;
	
	protected int level, hp, chp, ev, xp, cxp, nxp, xpYield, evolveLevel, catchRate;
	protected int hpIV, attackIV, defenseIV, spAttackIV, spDefenseIV, speedIV;
	protected int baseHP, baseAttack, baseDefense, baseSpAttack, baseSpDefense, baseSpeed;
	protected double attack, defense, spAttack, spDefense, speed, accuracy, evasion;
	protected double cAttack, cDefense, cSpAttack, cSpDefense, cSpeed;
	protected int speedStg, attackStg, defenseStg, spAttackStg, spDefenseStg, accuracyStg, evasionStg;
	
	protected int statusCounter, statusLimit;
	protected boolean isAlive = true, attacking = false, hit = false;
	
	protected List<Move> moveset, activeMoves;
	protected Map<Integer, Moves> moveLevels;
			
	/** CONSTRUCTORS **/
	public Pokemon(int index, String name, int level, Entity ball,
			int hp, int attack, int defense, int spAttack, int spDefense, int speed,
			int evolveLevel, int xpYield, int ev, Growth growth, int catchRate) {		
				
		uniqueID = UUID.randomUUID().toString();
		
		this.index = index;
		this.name = name;
		
		frontSprite = setup("/pokedex/front/" + name, 48 * 5, 48 * 5); 
		backSprite = setup("/pokedex/back/" + name, 48 * 5, 48 * 5);
		menuSprite = setup("/pokedex/menu/" + name, 48 * 2, 48 * 2);
		
		sex = Math.random() > 0.5 ? '♂' : '♀';
		
		this.level = level;
		this.growth = growth;
		this.ball = ball;
		
		xp = getXP(level);
		cxp = xp;
		nxp = getNextXP();
		
		this.hp = (int)(Math.floor(((2 * hp + hpIV + Math.floor(0.25 * ev)) * level) / 100) + level + 10);
		chp = this.hp;
		
		baseHP = hp;
		baseAttack = attack;
		baseDefense = defense;
		baseSpAttack = spAttack;
		baseSpDefense = spDefense;
		baseSpeed = speed;
		this.evolveLevel = evolveLevel;
		this.xpYield = xpYield; 
		this.ev = ev;		
		this.catchRate = catchRate;
		
		hpIV = 1 + (int)(Math.random() * ((31 - 1) + 1));			
		attackIV = 1 + (int)(Math.random() * ((31 - 1) + 1));
		defenseIV = 1 + (int)(Math.random() * ((31 - 1) + 1));
		spAttackIV = 1 + (int)(Math.random() * ((31 - 1) + 1));
		spDefenseIV = 1 + (int)(Math.random() * ((31 - 1) + 1));
		speedIV = 1 + (int)(Math.random() * ((31 - 1) + 1));
		
		this.attack = getStat(baseAttack, attackIV); 
		this.defense = getStat(baseDefense, defenseIV);		
		this.spAttack = getStat(baseSpAttack, spAttackIV); 
		this.spDefense = getStat(baseSpDefense, spDefenseIV);
		this.speed = getStat(baseSpeed, speedIV);
		this.accuracy = 1;
		this.evasion = 1;
		
		// random Nature selection
		int num = 0 + (int)(Math.random() * ((Nature.getNatures().size() - 0) + 0));
		nature = Nature.getNatures().get(num);
		setNature();
		
		cAttack = this.attack;
		cDefense = this.defense;
		cSpAttack = this.spAttack;
		cSpDefense = this.spDefense;
		cSpeed = this.speed;
								
		status = null;
		statusCounter = 0;
		statusLimit = 0;
		
		resetStats();
		resetStatStages();
		
		moveset = new ArrayList<>();
		activeMoves = new ArrayList<>();
		
		isAlive = true;
	}	
	/** END CONSTRUCTORS **/
		
	/** CHILD METHODS **/
	protected void mapMoves() { }
	public Pokemon evolve() { return null; }
	/** END CHILD METHODS **/
			
	/** LEVEL UP METHODS **/
	protected int getXP(int level) {		
		/*** XP CALULCATOR REFERENCE https://bulbapedia.bulbagarden.net/wiki/Experience#Experience_at_each_level ***/
		
		double xp = 0;		
		
		double n = level;
		double n2 = Math.pow(level, 2);
		double n3 = Math.pow(level, 3);
		
		switch (growth) {
		
			case MEDIUMFAST:
				xp = n3;
				break;
				
			case ERATIC:				
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
				
			case FLUCTUATING:
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
				
			case MEDIUMSLOW:				
				xp = ((6.0 / 5.0) * n3) + (-15 * n2) + (100 * n) - 140;				
				break;
				
			case FAST:
				xp = Math.floor( (4.0 * n3) / 5.0 );
				break;
				
			case SLOW:
				xp = Math.floor( (5.0 * n3) / 4.0 );
				break;
				
			default: 
					break;
		}
		
		if (xp < 0 || level == 1) xp = 0.0;		
		
		return (int) Math.floor(xp);		
	}
	protected int getNextXP() {		
		int nextXP = getXP(level + 1) - getXP(level);		
		if (nextXP < 0) nextXP = 0;
		return nextXP;		
	}
				
	public boolean checkLevelUp(int gainedXP) {		
		return nxp <= xp + gainedXP;		
	}	
	public void levelUp() { 	
		
		level++;
		
		this.xp = getXP(level);
		
		int oldBHP = hp;
		hp = (int)(Math.floor(((2 * baseHP + hpIV + Math.floor(ev / 4)) * level) / 100) + level + 10);
		
		chp += Math.ceil((hp - oldBHP));
		if (chp > hp) chp = hp;
		
		attack = getStat(baseAttack, attackIV); 
		defense = getStat(baseDefense, defenseIV);		
		spAttack = getStat(baseSpAttack, spAttackIV); 
		spDefense = getStat(baseSpDefense, spDefenseIV);
		speed = getStat(baseSpeed, speedIV);
		
		setNature();
		
		cAttack = attack;
		cDefense = defense;
		cSpAttack = spAttack;
		cSpDefense = spDefense;
		cSpeed = speed;
	}
	public boolean canEvolve() {
		return (evolveLevel != -1) && (level >= evolveLevel);
	}
	protected void create(Pokemon old) {
		
		nickname = old.nickname;
				
		sex = old.sex;
		
		level = old.level;
				
		xp = getXP(level);
		cxp = xp;
		nxp = getNextXP();
		
		hpIV = old.hpIV;		
		attackIV = old.attackIV;
		defenseIV = old.defenseIV;
		spAttackIV = old.spAttackIV;
		spDefenseIV = old.spDefenseIV;
		speedIV = old.speedIV;		
						
		hp = (int)(Math.floor(((2 * baseHP + hpIV + Math.floor(0.25 * ev)) * level) / 100) + level + 10);
		chp = hp;
		
		attack = getStat(baseAttack, attackIV); 
		defense = getStat(baseDefense, defenseIV);		
		spAttack = getStat(baseSpAttack, spAttackIV); 
		spDefense = getStat(baseSpDefense, spDefenseIV);
		speed = getStat(baseSpeed, speedIV);
		accuracy = old.accuracy;
		evasion = old.evasion;
		
		nature = old.nature;
		setNature();
		
		cAttack = attack;
		cDefense = defense;
		cSpAttack = spAttack;
		cSpDefense = spDefense;
		cSpeed = speed;
						
		resetStats();
		resetStatStages();
				
		status = old.status;
		statusCounter = 0;
		statusLimit = 0;
				
		moveset = old.moveset;
		activeMoves = new ArrayList<>();		
		protection = Protection.NONE;
		
		item = old.item;
		ball = old.ball;		
		isAlive = old.isAlive;		
	}
	public void create(char sex, int level, int cxp, int ev,
			int hpIV, int attackIV, int defenseIV, int spAttackIV, int spDefenseIV, int speedIV, 
			Nature nature, Status status, List<Move> moveset, Entity item, Entity ball, boolean isAlive) {
		
		this.sex = sex;
		this.level = level;
		
		xp = getXP(level);
		this.cxp = cxp;
		nxp = getNXP();
		
		this.ev = ev;		
		
		this.hpIV = hpIV;
		this.attackIV = attackIV;
		this.defenseIV = defenseIV;
		this.spAttackIV = spAttackIV;
		this.spDefenseIV = spDefenseIV;
		this.speedIV = speedIV;
		
		hp = (int)(Math.floor(((2 * baseHP + hpIV + Math.floor(0.25 * ev)) * level) / 100) + level + 10);
		chp = hp;
		
		attack = getStat(baseAttack, attackIV); 
		defense = getStat(baseDefense, defenseIV);		
		spAttack = getStat(baseSpAttack, spAttackIV); 
		spDefense = getStat(baseSpDefense, spDefenseIV);
		speed = getStat(baseSpeed, speedIV);
		accuracy = 1;
		evasion = 1;
		
		this.nature = nature;
		setNature();
		
		cAttack = attack;
		cDefense = defense;
		cSpAttack = spAttack;
		cSpDefense = spDefense;
		cSpeed = speed;
		
		this.status = status;
		statusCounter = 0;
		statusLimit = 0;
		
		this.moveset = new ArrayList<Move>(moveset);
		activeMoves = new ArrayList<>();		
		protection = Protection.NONE;
		
		this.item = item;
		this.ball = ball;
		this.isAlive = isAlive;
	}
	/** END LEVEL UP METHODS **/
			
	/** MOVE METHODS **/
	public Move getNewMove() {
		
		Move newMove = null;
		
		if (moveLevels != null) {
			for (Integer lvl : moveLevels.keySet()) {
				if (level == lvl) {
					newMove = new Move(moveLevels.get(lvl));
					break;
				}
			}	
		}
		
		return newMove;
	}	
	public void addMove(Moves move) {
		if (moveset.size() < 4) {
			moveset.add(new Move(move));
		}
	}
	public void addMoves(List<Moves> moves) {			
		moveset.clear();
		for (Moves move : moves) {
			if (moveset.size() < 4) {
				moveset.add(new Move(move));
			}
		}
	}
	public boolean learnMove(Move move) { 
		
		if (moveset.size() == 4) {
			return false;
		}
		else {
			moveset.add(move);
			return true;
		}
	}
	public boolean forgetMove(Move move) {
		
		for (int i = 0; i < moveset.size(); i++) {
			if (moveset.get(i).getName().equals(move.getName())) {
				moveset.remove(i);
				return true;
			}
		}
		
		return false;
	}
	public boolean replaceMove(Move oldMove, Move newMove) {
		
		for (int i = 0; i < moveset.size(); i++) {
			if (moveset.get(i).getName().equals(oldMove.getName())) {
				moveset.remove(i);
				moveset.add(i, newMove);
				return true;
			}
		}
		
		return false;
	}
	public void resetMoves() {		
		for (Move m : moveset) {
			m.resetPP();
			m.resetMoveTurns();
		}
		for (Move m : activeMoves) {
			m.setTurnCount(m.getTurns());
			activeMoves.remove(m);
		}
	}
	public void resetMoveTurns() {
		for (Move m : moveset) {
			m.resetMoveTurns();
		}
	}
	/** END MOVE METHODS **/
		
	/** NATURE METHODS **/
	protected void setNature() {
		
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
	/** END MOVE METHODS **/
		
	/** GET POKEMON METHODS **/
	public static Pokemon get(Pokedex id, int level, Entity ball) {
		
		Pokemon pokemon = null;
		
		switch(id) {
			case BULBUSAUR: pokemon = new Bulbasaur(level, ball); break;			
			case IVYSAUR: pokemon = new Ivysaur(level, ball); break;			
			case VENUSAUR: pokemon = new Venusaur(level, ball); break;			
			case CHARMANDER: pokemon = new Charmander(level, ball); break;	
			case CHARMELEON: pokemon = new Charmeleon(level, ball); break;		
			case CHARIZARD: pokemon = new Charizard(level, ball); break;	
			case SQUIRTLE: pokemon = new Squirtle(level, ball); break;	
			case WARTORTLE: pokemon = new Wartortle(level, ball); break;		
			case BLASTOISE: pokemon = new Blastoise(level, ball); break;	
			case PIKACHU: pokemon = new Pikachu(level, ball); break;		
			case RAICHU: pokemon = new Raichu(level, ball); break;		
			case ZUBAT: pokemon = new Zubat(level, ball); break;		
			case GOLBAT: pokemon = new Golbat(level, ball); break;		
			case GROWLITHE: pokemon = new Growlithe(level, ball); break;
			case ARCANINE: pokemon = new Arcanine(level, ball); break;			
			case ABRA: pokemon = new Abra(level, ball); break;			
			case KADABRA: pokemon = new Kadabra(level, ball); break;			
			case ALAKAZAM: pokemon = new Alakazam(level, ball); break;	
			case MACHOP: pokemon = new Machop(level, ball); break;			
			case MACHOKE: pokemon = new Machoke(level, ball); break;			
			case MACHAMP: pokemon = new Machamp(level, ball); break;	
			case GEODUDE: pokemon = new Geodude(level, ball); break;			
			case GRAVELER: pokemon = new Graveler(level, ball); break;			
			case GOLEM: pokemon = new Golem(level, ball); break;	
			case PONYTA: pokemon = new Ponyta(level, ball); break;
			case RAPIDASH: pokemon = new Rapidash(level, ball); break;
			case GASTLY: pokemon = new Gastly(level, ball); break;
			case HAUNTER: pokemon = new Haunter(level, ball); break;
			case GENGAR: pokemon = new Gengar(level, ball); break;
			case HITMONLEE: pokemon = new Hitmonlee(level, ball); break;
			case HITMONCHAN: pokemon = new Hitmonchan(level, ball); break;
			case HORSEA: pokemon = new Horsea(level, ball); break;
			case SEADRA: pokemon = new Seadra(level, ball); break;
			case MAGIKARP: pokemon = new Magikarp(level, ball); break;
			case GYARADOS: pokemon = new Gyarados(level, ball); break;
			case LAPRAS: pokemon = new Lapras(level, ball); break;
			case SNORLAX: pokemon = new Snorlax(level, ball); break;			
			case ARTICUNO: pokemon = new Articuno(level, ball); break;
			case ZAPDOS: pokemon = new Zapdos(level, ball); break;
			case MOLTRES: pokemon = new Moltres(level, ball); break;
			case DRATINI: pokemon = new Dratini(level, ball); break;
			case DRAGONAIR: pokemon = new Dragonair(level, ball); break;
			case DRAGONITE: pokemon = new Dragonite(level, ball); break;
			case MEWTWO: pokemon = new Mewtwo(level, ball); break;
			case MEW: pokemon = new Mew(level, ball); break;
			case CHIKORITA: pokemon = new Chikorita(level, ball); break;
			case BAYLEEF: pokemon = new Bayleef(level, ball); break;
			case MEGANIUM: pokemon = new Meganium(level, ball); break;
			case CYNDAQUIL: pokemon = new Cyndaquil(level, ball); break;
			case QUILAVA: pokemon = new Quilava(level, ball); break;
			case TYPHLOSION: pokemon = new Typhlosion(level, ball); break;
			case TOTODILE: pokemon = new Totodile(level, ball); break;
			case CROCONAW: pokemon = new Croconaw(level, ball); break;
			case FERALIGATR: pokemon = new Feraligatr(level, ball); break;			
			case CROBAT: pokemon = new Crobat(level, ball); break;
			case KINGDRA: pokemon = new Kingdra(level, ball); break;
			case RAIKOU: pokemon = new Raikou(level, ball); break;
			case ENTEI: pokemon = new Entei(level, ball); break;
			case SUICUNE: pokemon = new Suicune(level, ball); break;
			case LUGIA: pokemon = new Lugia(level, ball); break;
			case HOOH: pokemon = new Hooh(level, ball); break;
			case CELEBI: pokemon = new Celebi(level, ball); break;			
			case TREECKO: pokemon = new Treecko(level, ball); break;			
			case GROVYLE: pokemon = new Grovyle(level, ball); break;			
			case SCEPTILE: pokemon = new Sceptile(level, ball); break;	
			case TORCHIC: pokemon = new Torchic(level, ball); break;			
			case COMBUSKEN: pokemon = new Combusken(level, ball); break;			
			case BLAZIKEN: pokemon = new Blaziken(level, ball); break;	
			case MUDKIP: pokemon = new Mudkip(level, ball); break;			
			case MARSHTOMP: pokemon = new Marshtomp(level, ball); break;			
			case SWAMPERT: pokemon = new Swampert(level, ball); break;	
			case POOCHYENA: pokemon = new Poochyena(level, ball); break;	
			case MIGHTYENA: pokemon = new Mightyena(level, ball); break;	
			case ZIGZAGOON: pokemon = new Zigzagoon(level, ball); break;	
			case LINOONE: pokemon = new Linoone(level, ball); break;	
			case RALTS: pokemon = new Ralts(level, ball); break;	
			case KIRLIA: pokemon = new Kirlia(level, ball); break;	
			case GARDEVOIR: pokemon = new Gardevoir(level, ball); break;	
			case NINCADA: pokemon = new Nincada(level, ball); break;	
			case NINJASK: pokemon = new Ninjask(level, ball); break;	
			case SHEDINJA: pokemon = new Shedinja(level, ball); break;	
			case WHISMUR: pokemon = new Whismur(level, ball); break;	
			case LOUDRED: pokemon = new Loudred(level, ball); break;
			case EXPLOUD: pokemon = new Exploud(level, ball); break;	
			case SPHEAL: pokemon = new Spheal(level, ball); break;	
			case SEALEO: pokemon = new Sealeo(level, ball); break;	
			case WALREIN: pokemon = new Walrein(level, ball); break;	
			case BAGON: pokemon = new Bagon(level, ball); break;	
			case SHELGON: pokemon = new Shelgon(level, ball); break;	
			case SALAMENCE: pokemon = new Salamence(level, ball); break;	
			case BELDUM: pokemon = new Beldum(level, ball); break;	
			case METANG: pokemon = new Metang(level, ball); break;	
			case METAGROSS: pokemon = new Metagross(level, ball); break;	
			case KYOGRE: pokemon = new Kyogre(level, ball); break;	
			case GROUDON: pokemon = new Groudon(level, ball); break;	
			case RAYQUAZA: pokemon = new Rayquaza(level, ball); break;	
			case JIRACHI: pokemon = new Jirachi(level, ball); break;	
			case DEOXYS: pokemon = new Deoxys(level, ball); break;	

			default: break;
		}
		
		return pokemon;
	}
	/** END GET POKEMON METHODS **/
				
	/** GETTERS AND SETTERS **/
	public String getNickname() { return nickname; }
	public void setNickname(String nickname) { this.nickname = nickname; }
	
	public int getHP() { return chp; }
	public void setHP(int chp) { this.chp = chp; }
	public void addHP(int hp) { 
		this.chp += hp; 
		if (this.chp > this.hp) 
			this.chp = this.hp; 
	}	
	public int getBXP() { return xp; }
	public void setBXP(int bxp) { this.xp = bxp; }
	public int getXP() { return cxp; }
	public void setXP(int cxp) { this.cxp = cxp; }	
	
	public double getBAttack() { return attack; }
	public void setBAttack(int attack) {	this.attack = attack; }
	public double getBDefense() { return defense; }
	public void setBDefense(int defense) { this.defense = defense; }	
	public double getBSpAttack() { return spAttack; }
	public void setBSpAttack(int spAttack) { this.spAttack = spAttack; }
	public double getBSpDefense() {	return spDefense; }
	public void setBSpDefense(int spDefense) { this.spDefense = spDefense; }	
	public double getBSpeed() { return speed; }
	public void setBSpeed(int speed) { this.speed = speed; }
	public double getAccuracy() { return accuracy; }
	public void setAccuracy(int accuracy) { this.accuracy = accuracy; }	
	public double getEvasion() { return evasion; }
	public void setEvasion(int evasion) { this.evasion = evasion; }
	
	public double getAttack() { return cAttack; }
	public void setAttack(int cAttack) {	this.cAttack = cAttack; }
	public double getDefense() { return cDefense; }
	public void setDefense(int cDefense) { this.cDefense = cDefense; }	
	public double getSpAttack() { return cSpAttack; }
	public void setSpAttack(int cSpAttack) { this.cSpAttack = cSpAttack; }
	public double getSpDefense() {	return cSpDefense; }
	public void setSpDefense(int cSpDefense) { this.cSpDefense = cSpDefense; }	
	public double getSpeed() { return cSpeed; }
	public void setSpeed(int cSpeed) { this.cSpeed = cSpeed; }
			
	public int getAttackStg() { return attackStg; }
	public void setAttackStg(int attackStg) { this.attackStg = attackStg; }
	public int getDefenseStg() { return defenseStg; }
	public void setDefenseStg(int defenseStg) { this.defenseStg = defenseStg; }
	public int getSpAttackStg() { return spAttackStg; }
	public void setSpAttackStg(int spAttackStg) { this.spAttackStg = spAttackStg; }
	public int getSpDefenseStg() { return spDefenseStg; }
	public void setSpDefenseStg(int spDefenseStg) { this.spDefenseStg = spDefenseStg; }
	public int getSpeedStg() { return speedStg; }
	public void setSpeedStg(int speedStg) { this.speedStg = speedStg; }
	public int getAccuracyStg() { return accuracyStg; }
	public void setAccuracyStg(int accuracyStg) { this.accuracyStg = accuracyStg; }
	public int getEvasionStg() { return evasionStg; }
	public void setEvasionStg(int evasionStg) { this.evasionStg = evasionStg; }
	
	public Status getStatus() { return status; }
	public void setStatus(Status status) { this.status = status; }	
	public boolean hasStatus(Status status) { return (this.status != null && this.status == status);}
	public int getStatusCounter() { return statusCounter; }
	public void setStatusCounter(int statusCounter) { this.statusCounter = statusCounter; }	
	public int getStatusLimit() { return statusLimit; }
	public void setStatusLimit(int statusLimit) { this.statusLimit = statusLimit; }
	public void removeStatus() {
		status = null; 
		statusLimit = 0;
		statusCounter = 0;
	}
	
	public List<Move> getMoveSet() { return moveset; }
	public void setMoveSet(ArrayList<Move> moveSet) { this.moveset = moveSet; }
	public List<Move> getActiveMoves() { return activeMoves; }
	public void setActiveMoves(List<Move> activeMoves) { this.activeMoves = activeMoves; }	
	public Protection getProtection() { return protection; }
	public void setProtection(Protection protection) { this.protection = protection; }
	public void clearProtection() { protection = Protection.NONE; }
	
	public boolean getAttacking() { return attacking; }
	public void setAttacking(boolean attacking) { this.attacking = attacking; }
	public boolean getHit() { return hit; }
	public void setHit(boolean hit) { this.hit = hit; }
	
	public boolean isAlive() { return isAlive; }
	public void setAlive(boolean isAlive) {	
		this.isAlive = isAlive; 
		chp = 0;		
		removeStatus();
		getAbility().setActive(false);
		resetMoves();
		resetStats();
		resetStatStages();
		clearProtection();
	}
	
	public Entity getBall() { return ball; }
	public void setBall(Entity ball) { this.ball = ball; }
	public Entity getItem() { return item; }
	public void giveItem(Entity item) { this.item = item; }
	/** END GETTERS AND SETTERS **/
	
	/** GETTERS **/
	public String toString() { return name; }
	public String getUniqueID() { return uniqueID; }
	public Pokedex getID() { return id; }
	public int getIndex() { return index; }
	public String getName() { 
		if (nickname != null) return nickname;		
		else return name.toUpperCase();		
	}
	
	public BufferedImage getFrontSprite() { return frontSprite; }
	public BufferedImage getBackSprite() { return backSprite; }
	public BufferedImage getMenuSprite() { return menuSprite; }
	
	public char getSex() { return sex; }
	public Color getSexColor() {
		if (sex == '♂') return Color.BLUE;		
		else return Color.RED;		
	}
	public int getLevel() { return level; }
	public int getBHP() { return hp; }	
	public int getNXP() { return nxp; }
	public Type getType() { return type; }
	public List<Type> getTypes() { return types; }	
	public boolean checkType(Type type) {
		
		boolean isType = false;
		
		if (this.types != null) {
			if (this.types.contains(type)) {
				isType = true;
			}
		}
		else if (this.type == type) {
			isType = true;
		}
		
		return isType;		
	}
	
	public Nature getNature() { return nature; }	
	public Ability getAbility() { return ability; }		
	public Growth getGrowth() { return growth; }
			
	public int getBaseHP() { return baseHP; }
	public int getBaseAttack() { return baseAttack; }
	public int getBaseDefense() { return baseDefense; }
	public int getBaseSpAttack() { return baseSpAttack; }
	public int getBaseSpDefense() { return baseSpDefense; }
	public int getBaseSpeed() { return baseSpeed; }
	
	public int getHPIV() { return hpIV; }
	public int getAttackIV() { return attackIV; }
	public int getDefenseIV() { return defenseIV; }
	public int getSpAttackIV() { return spAttackIV; }
	public int getSpDefenseIV() { return spDefenseIV; }
	public int getSpeedIV() { return speedIV; }
		
	public int getXPYield() { return xpYield; }
	public int getEV() { return ev; }
	public int getEvolveLevel() { return evolveLevel; }
	public int getCatchRate() { return catchRate; }		
	/** END GETTERS **/
		
	/** MISC METHODS **/
	protected int getStat(double base, int IV) {
		return (int)(Math.floor(0.01 * (2 * base + IV + Math.floor(0.25 * ev)) * level)) + 5;
	};	
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
						cAttack = Math.floor(attack * ((2.0 + attackStg) / 2.0)); 
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
						cAttack = Math.floor(attack * (2.0 / (2.0 - attackStg))); 
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
						cDefense = Math.floor(defense * ((2.0 + defenseStg) / 2.0)); 
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
						cDefense = Math.floor(defense * (2.0 / (2.0 - defenseStg))); 
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
						cSpAttack = Math.floor(spAttack * ((2.0 + spAttackStg) / 2.0)); 
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
						cSpAttack = Math.floor(spAttack * (2.0 / (2.0 - spAttackStg))); 
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
						cSpDefense = Math.floor(spDefense * ((2.0 + spDefenseStg) / 2.0)); 
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
						cSpDefense = Math.floor(spDefense * (2.0 / (2.0 - spDefenseStg))); 
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
						cSpeed = Math.floor(speed * ((2.0 + speedStg) / 2.0)); 
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
						cSpeed = Math.floor(speed * (2.0 / (2.0 - speedStg))); 
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
						accuracy = Math.floor(1 * ((3.0 + accuracyStg) / 3.0)); 
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
						accuracy = Math.floor(1 * (3.0 / (3.0 - accuracyStg))); 
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
						evasion = Math.floor(1 * ((3.0 + evasionStg) / 3.0)); 
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
						evasion = Math.floor(1 * (3.0 / (3.0 - evasionStg))); 
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
		cAttack = attack;
		cDefense = defense;
		cSpAttack = spAttack;
		cSpDefense = defense;
		cSpeed = speed;
		accuracy = 1;
		evasion = 1;
	}
	public void resetStatStages() {		
		attackStg = 0;
		defenseStg = 0;
		spAttackStg = 0;
		spDefenseStg = 0;
		speedStg = 0;
		accuracyStg = 0;
		evasionStg = 0;
	}
	
	protected BufferedImage setup(String imagePath, int width, int height) {
		
		BufferedImage image = null;
		
		try {
			image = ImageIO.read(getClass().getResourceAsStream(imagePath + ".png"));
			image = GamePanel.utility.scaleImage(image, width, height);
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		return image;
	}
	/** END MISC METHODS **/
}