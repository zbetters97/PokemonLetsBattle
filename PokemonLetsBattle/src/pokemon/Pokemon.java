package pokemon;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import application.GamePanel;
import moves.*;
import properties.*;

/*** POKEDEX ENUM CLASS ***/
public enum Pokemon {
	
	/*** STAT REFERECE https://www.serebii.net/pokemon/ ***/
	/*** EXP / EV REFERENCE https://bulbapedia.bulbagarden.net/wiki/List_of_Pok%C3%A9mon_by_effort_value_yield ***/
	
	BULBASAUR ("Bulbasaur", 1, Type.GRASS, 5, 45, 49, 49, 65, 65, 45, 16, 64, 1),
	CHARMANDER ("Charmander", 4, Type.FIRE, 5, 39, 52, 43, 60, 50, 65, 16, 65, 1),
	
//	IVYSAUR ("Ivysaur", 2, Arrays.asList(Type.GRASS, Type.POISON), 16, 60, 62, 63, 80, 80, 60, 32, 141, 2),
	VENUSAUR ("Venusaur", 3, Arrays.asList(Type.GRASS, Type.POISON), 36, 80, 82, 83, 100, 100, 80, -1, 208, 3),
//	CHARMANDER ("Charmander", 4, Type.FIRE, 5, 39, 52, 43, 60, 50, 65, 16, 65, 1),
//	CHARMELEON ("Charmeleon", 5, Type.FIRE, 16, 58, 64, 58, 80, 65, 80, 36, 142, 2),
	CHARIZARD ("Charizard", 6, Arrays.asList(Type.FIRE, Type.FLYING), 36, 78, 84, 78, 109, 85, 100, -1, 209, 3);
/*
	SQUIRTLE ("Squirtle", 7, Type.WATER, 5, 44, 48, 65, 50, 64, 43, 16, 66, 1),
	WARTORTLE ("Wartortle", 8, Type.WATER, 16, 59, 63, 80, 65, 80, 58, 36, 143, 2),
	BLASTOISE ("Blastoise", 9, Type.WATER, 36, 79, 83, 100, 85, 105, 78, -1, 210, 3),
	PIKACHU ("Pikachu", 25, Type.ELECTRIC, 5, 55, 55, 40, 50, 50, 90, 30, 82, 2),
	RAICHU ("Raichu", 26, Type.ELECTRIC, 30, 60, 90, 55, 90, 80, 110, -1, 122, 3),
	ABRA ("Abra", 63, Type.PSYCHIC, 5, 25, 20, 15, 105, 55, 90, 16, 62, 1),
	KADABRA ("Kadabra", 64, Type.PSYCHIC, 16, 40, 35, 30, 120, 70, 105, 36, 140, 2),
	ALAKAZAM ("Alakazam", 65, Type.PSYCHIC, 36, 55, 50, 45, 135, 95, 120, -1, 250, 3),
	MACHOP ("Machop", 66, Type.FIGHTING, 5, 70, 80, 50, 35, 35, 35, 28, 61, 1),
	MACHOKE ("Machoke", 67, Type.FIGHTING, 28, 80, 100, 70, 50, 60, 45, 40, 142, 2),
	MACHAMP ("Machamp", 68, Type.FIGHTING, 40, 90, 130, 80, 65, 85, 55, -1, 253, 3),
	GEODUDE ("Geodude", 74, Arrays.asList(Type.ROCK, Type.GROUND), 5, 40, 80, 100, 30, 30, 20, 25, 60, 1),
	GRAVELER ("Graveler", 75, Arrays.asList(Type.ROCK, Type.GROUND), 25, 55, 95, 115, 45, 45, 35, 40, 137, 2),
	GOLEM ("Golem", 76, Arrays.asList(Type.ROCK, Type.GROUND), 40, 80, 120, 130, 55, 65, 45, -1, 248, 3),
	GASTLY ("Gastly", 92, Arrays.asList(Type.GHOST, Type.POISON), 5, 30, 35, 30, 100, 35, 80, 25, 62, 1),
	HAUNTER ("Haunter", 93, Arrays.asList(Type.GHOST, Type.POISON), 25, 45, 50, 45, 115, 55, 96, 40, 142, 2),
	GENGAR ("Gengar", 94, Arrays.asList(Type.GHOST, Type.POISON), 40, 60, 65, 60, 130, 75, 110, -1, 250, 3),
	HORSEA ("Horsea", 116, Type.WATER, 5, 30, 40, 70, 70, 25, 60, 32, 59, 1),
	SEADRA ("Seadra", 117, Type.WATER, 32, 55, 65, 95, 95, 45, 85, 45, 154, 2),
	LAPRAS ("Lapras", 131, Arrays.asList(Type.WATER, Type.ICE), 40, 130, 85, 80, 85, 95, 60, -1, 187, 2),
	SNORLAX ("Snorlax", 143, Type.NORMAL, 45, 160, 110, 65, 65, 110, 30, -1, 189, 2),
	KINGDRA ("Kingdra", 230, Arrays.asList(Type.WATER, Type.DRAGON), 40, 75, 95, 95, 95, 95, 85, -1, 270, 3),
	RAIKOU ("Raikou", 243, Type.ELECTRIC, 40, 90, 85, 75, 115, 100, 115, -1, 290, 3),
	ENTEI ("Entei", 244, Type.FIRE, 40, 115, 115, 85, 90, 75, 100, -1, 290, 3),
	SUICUNE ("Suicune", 245, Type.WATER, 40, 100, 75, 115, 90, 115, 85, -1, 290, 3),
	TREECKO ("Treecko", 252, Type.GRASS, 5, 40, 45, 35, 65, 55, 70, 16, 62, 1),
	GROVYLE ("Grovyle", 253, Type.GRASS, 16, 50, 65, 45, 85, 65, 95, 36, 142, 2),
	SCEPTILE ("Sceptile", 254, Type.GRASS, 36, 70, 85, 65, 105, 85, 120, -1, 265, 3),
	TORCHIC ("Torchic", 255, Type.FIRE, 5, 45, 60, 40, 70, 50, 45, 16, 62, 1),
	COMBUSKEN ("Combusken", 256, Arrays.asList(Type.FIRE, Type.FIGHTING), 16, 60, 85, 60, 85, 60, 55, 36, 142, 2),
	BLAZIKEN ("Blaziken", 257, Arrays.asList(Type.FIRE, Type.FIGHTING), 36, 80, 120, 70, 110, 70, 80, -1, 265, 3),
	MUDKIP ("Mudkip", 258, Type.WATER, 5, 50, 70, 50, 50, 50, 40, 16, 62, 1),
	MARSHTOMP ("Marshtomp", 259, Arrays.asList(Type.WATER, Type.GROUND), 16, 70, 85, 70, 60, 70, 50, 36, 142, 2),
	SWAMPERT ("Swampert", 260, Arrays.asList(Type.WATER, Type.GROUND), 36, 100, 110, 90, 85, 90, 60, -1, 268, 3),
	KYOGRE ("Kyogre", 382, Type.WATER, 50, 100, 100, 90, 150, 140, 90, -1, 335, 3), 
	GROUDON ("Groudon", 383, Type.GROUND, 50, 100, 150, 140, 100, 90, 90, -1, 335, 3),
	RAYQUAZA ("Rayquaza", 384, Arrays.asList(Type.DRAGON, Type.FLYING), 50, 105, 150, 90, 150, 90, 95, -1, 340, 3);
	*/
	/** END INITIALIZE ENUMS **/
		
	/** INITIALIZE VALUES**/
	private BufferedImage frontSprite, backSprite;
	private String name;
	private int index;
	private char sex;
	private Type type;
	private Nature nature;
	private List<Type> types;
	private int level, bhp, hp, evLevel, xp, bxp, ev, iv;
	private double speed, attack, defense, spAttack, spDefense, accuracy;	
	private int speedStg, attackStg, defenseStg, spAttackStg, spDefenseStg, accuracyStg;
	private Status status;
	private boolean isAlive;
	
	private int statusCounter, statusLimit;
	/** END INITIALIZE VALUES **/
	
	// initialize list to hold moves of given pokemon
	private List<Move> moveSet;
	
	// initialize list to hold all enums in pokemon class
	private static List<Pokemon> POKEDEX = Arrays.asList(Pokemon.values());

	/** CONSTRUCTORS **/
	Pokemon(String name, int index, Type type, int level, int hp, int attack, int defense, 
			int spAttack, int spDefense, int speed, int evLevel, int xp, int ev) {	
		
		this.frontSprite = setup("/pokedexfront/" + name, 48 * 5, 48 * 5); 
		this.backSprite = setup("/pokedexback/" + name, 48 * 5, 48 * 5);
		
		// hp calculation reference (GEN IV): https://pokemon.fandom.com/wiki/Individual_Values
		this.name = name; this.index = index; 
		
		// coin flip for Pokemon gender
		this.sex = Math.random() > 0.5 ? '♂' : '♀';
		this.type = type; this.level = level;
		
		this.iv = 1 + (int)(Math.random() * ((31 - 1) + 1));							
		this.hp = (int)(Math.floor(((2 * hp + iv + Math.floor(ev / 4)) * level) / 100) + level + 10);
		this.bhp = this.hp;
		
		Calculate getStat = (stat, IV, EV, lev) -> {
			return (int)(Math.floor(0.01 * (2 * stat + IV + Math.floor(EV / 4)) * lev)) + 5;
		};
		
		this.speed = getStat.compute(speed, 1 + (int)(Math.random() * ((31 - 1) + 1)), ev, level);
		this.attack = getStat.compute(attack, 1 + (int)(Math.random() * ((31 - 1) + 1)), ev, level); 
		this.defense = getStat.compute(defense, 1 + (int)(Math.random() * ((31 - 1) + 1)), ev, level);		
		this.spAttack = getStat.compute(spAttack, 1 + (int)(Math.random() * ((31 - 1) + 1)), ev, level); 
		this.spDefense = getStat.compute(spDefense, 1 + (int)(Math.random() * ((31 - 1) + 1)), ev, level);
		this.accuracy = 1;	
		
		setNature();
				
		this.speedStg = 0;
		this.attackStg = 0;
		this.defenseStg = 0;
		this.spAttackStg= 0;
		this.spDefenseStg = 0;
		this.accuracyStg = 0;
		
		this.evLevel = evLevel; this.xp = xp; this.bxp = xp; this.ev = ev; this.types = null;	
		this.status = null;
		this.isAlive = true;	
		
		this.statusCounter = 0;
		this.statusLimit = 0;
		
		moveSet = new ArrayList<>();
	}
	Pokemon(String name, int index, List<Type> types, int level, int hp, int attack, int defense, 
			int spAttack, int spDefense, int speed, int evLevel, int xp, int ev) {			
		
		this.frontSprite = setup("/pokedexfront/" + name, 48 * 5, 48 * 5); 
		this.backSprite = setup("/pokedexback/" + name, 48 * 5, 48 * 5);

		this.name = name; this.index = index; 
		
		// coin flip for Pokemon gender
		this.sex = Math.random() > 0.5 ? '♂' : '♀';		
		this.types = types; this.level = level;
		
		this.iv = 1 + (int)(Math.random() * ((31 - 1) + 1));							
		this.hp = (int)(Math.floor(((2 * hp + iv + Math.floor(ev / 4)) * level) / 100) + level + 10);
		this.bhp = this.hp;
		
		Calculate getStat = (stat, IV, EV, lev) -> {
			return (int)(Math.floor(0.01 * (2 * stat + IV + Math.floor(EV / 4)) * lev)) + 5;
		};
		
		this.speed = getStat.compute(speed, iv, ev, level);
		this.attack = getStat.compute(attack, iv, ev, level); 
		this.defense = getStat.compute(defense, iv, ev, level);		
		this.spAttack = getStat.compute(spAttack, iv, ev, level); 
		this.spDefense = getStat.compute(spDefense, iv, ev, level);
		this.accuracy = 1;
		
		setNature();
		
		this.speedStg = 0;
		this.attackStg = 0;
		this.defenseStg = 0;
		this.spAttackStg= 0;
		this.spDefenseStg = 0;
		this.accuracyStg = 0;
		
		this.evLevel = evLevel; this.xp = xp; this.bxp = xp; this.ev = ev; this.type = null;
		this.status = null;
		this.isAlive = true;	
		
		this.statusCounter = 0;
		this.statusLimit = 0;
		
		moveSet = new ArrayList<>();		
	}
	/** END CONSTRUCTORS **/
	
	/** MAP MOVES POKEMON METHOD **/
	private static Pokemon mapMoves(Pokemon pokemon) {
		
		// map of pokemon and corresponding move set
		Map<Pokemon, List<Move>> pokeMap = new HashMap<>();
		
		// set default moves for each pokemon
 //       pokeMap.put(BULBASAUR, Arrays.asList(new Move(Moves.VINEWHIP), new Move(Moves.TACKLE), new Move(Moves.GROWL)));
        pokeMap.put(BULBASAUR, Arrays.asList(new Move(Moves.RAZORLEAF), new Move(Moves.VINEWHIP), new Move(Moves.POISONPOWDER),
        		new Move(Moves.TACKLE)));
        pokeMap.put(CHARMANDER, Arrays.asList(new Move(Moves.EMBER), new Move(Moves.SCRATCH), new Move(Moves.GROWL)));
        
    
 //       pokeMap.put(IVYSAUR, Arrays.asList(new Move(Moves.RAZORLEAF), new Move(Moves.VINEWHIP), new Move(Moves.POISONPOWDER),
//        		new Move(Moves.TACKLE)));
		pokeMap.put(VENUSAUR, Arrays.asList(new Move(Moves.PETALBLIZZARD), new Move(Moves.SOLARBEAM, 2), new Move(Moves.TAKEDOWN), 
				new Move(Moves.DOUBLEEDGE)));
		
 //       pokeMap.put(CHARMANDER, Arrays.asList(new Move(Moves.EMBER), new Move(Moves.SCRATCH), new Move(Moves.GROWL)));
//		pokeMap.put(CHARMELEON, Arrays.asList(new Move(Moves.FIREFANG), new Move(Moves.EMBER), new Move(Moves.SLASH), 
//				new Move(Moves.GROWL)));
        pokeMap.put(CHARIZARD, Arrays.asList(new Move(Moves.FLAMETHROWER), new Move(Moves.FLAREBLITZ),new Move(Moves.DRAGONBREATH),
        	new Move(Moves.FLY, 2)));
  /*      
		pokeMap.put(SQUIRTLE, Arrays.asList(new Move(Moves.WATERGUN), new Move(Moves.TACKLE), new Move(Moves.TAILWHIP)));
        pokeMap.put(WARTORTLE, Arrays.asList(new Move(Moves.WATERPULSE), new Move(Moves.WATERGUN), new Move(Moves.TAILWHIP)));
        pokeMap.put(BLASTOISE, Arrays.asList(new Move(Moves.WATERPULSE), new Move(Moves.AQUATAIL), new Move(Moves.HYDROPUMP), 
        		new Move(Moves.FLASHCANNON)));
        
        pokeMap.put(PIKACHU, Arrays.asList(new Move(Moves.THUNDERWAVE), new Move(Moves.THUNDERSHOCK),new Move(Moves.TACKLE),
        		new Move(Moves.PLAYNICE)));
        pokeMap.put(RAICHU, Arrays.asList(new Move(Moves.THUNDERPUNCH), new Move(Moves.THUNDERBOLT), new Move(Moves.QUICKATTACK), 
        		new Move(Moves.SLAM)));    
        
        pokeMap.put(ABRA, Arrays.asList(new Move(Moves.TELEPORT)));
        pokeMap.put(KADABRA, Arrays.asList(new Move(Moves.CONFUSION), new Move(Moves.PSYBEAM), new Move(Moves.KINESIS)));
        pokeMap.put(ALAKAZAM, Arrays.asList(new Move(Moves.PSYCHIC), new Move(Moves.CONFUSION), new Move(Moves.PSYCHOCUT), 
        		new Move(Moves.CALMMIND)));     
        
        pokeMap.put(MACHOP, Arrays.asList(new Move(Moves.LOWKICK), new Move(Moves.LOWSWEEP), new Move(Moves.KNOCKOFF))); 
        pokeMap.put(MACHOKE, Arrays.asList(new Move(Moves.LOWKICK), new Move(Moves.LOWSWEEP), new Move(Moves.VITALTHROW),
        		new Move(Moves.SEISMICTOSS))); 
        pokeMap.put(MACHAMP, Arrays.asList(new Move(Moves.SEISMICTOSS), new Move(Moves.DYNAMICPUNCH), new Move(Moves.CROSSCHOP), 
        		new Move(Moves.SCARYFACE))); 
        
        pokeMap.put(GEODUDE, Arrays.asList(new Move(Moves.ROCKTHROW), new Move(Moves.TACKLE), new Move(Moves.DEFENSECURL)));
        pokeMap.put(GRAVELER, Arrays.asList(new Move(Moves.ROCKTHROW), new Move(Moves.ROLLOUT), new Move(Moves.TACKLE),
        		new Move(Moves.DEFENSECURL)));
        pokeMap.put(GOLEM, Arrays.asList(new Move(Moves.EARTHQUAKE), new Move(Moves.DIG, 2), new Move(Moves.HEAVYSLAM), 
        		new Move(Moves.DOUBLEEDGE)));
        
        pokeMap.put(GASTLY, Arrays.asList(new Move(Moves.LICK), new Move(Moves.PAYBACK), new Move(Moves.HYPNOSIS)));   
        pokeMap.put(HAUNTER, Arrays.asList(new Move(Moves.PAYBACK), new Move(Moves.HEX), new Move(Moves.DARKPULSE), 
        		new Move(Moves.CONFUSERAY)));   
        pokeMap.put(GENGAR, Arrays.asList(new Move(Moves.SHADOWBALL), new Move(Moves.SHADOWPUNCH),new Move(Moves.HEX), 
        		new Move(Moves.DARKPULSE))); 
        
        pokeMap.put(HORSEA, Arrays.asList(new Move(Moves.WATERGUN), new Move(Moves.BUBBLE), new Move(Moves.LEER))); 
        pokeMap.put(SEADRA, Arrays.asList(new Move(Moves.WATERGUN), new Move(Moves.TWISTER), new Move(Moves.HYDROPUMP),
        		new Move(Moves.AGILITY))); 
        pokeMap.put(KINGDRA, Arrays.asList(new Move(Moves.SURF), new Move(Moves.HYDROPUMP), new Move(Moves.DRAGONPULSE),
        		new Move(Moves.AGILITY))); 
        
        pokeMap.put(LAPRAS, Arrays.asList(new Move(Moves.ICEBEAM), new Move(Moves.HYDROPUMP), new Move(Moves.SHEERCOLD), 
        		new Move(Moves.CONFUSERAY)));     
        
        pokeMap.put(SNORLAX, Arrays.asList(new Move(Moves.BODYSLAM), new Move(Moves.ROLLOUT), new Move(Moves.CRUNCH), 
        		new Move(Moves.YAWN)));     
        
        pokeMap.put(RAIKOU, Arrays.asList(new Move(Moves.THUNDERFANG), new Move(Moves.THUNDER), new Move(Moves.CRUNCH),
        		new Move(Moves.CALMMIND)));
        pokeMap.put(ENTEI, Arrays.asList(new Move(Moves.FIREFANG), new Move(Moves.FLAMETHROWER), new Move(Moves.EXTRASENSORY),
        		new Move(Moves.CALMMIND)));
        pokeMap.put(SUICUNE, Arrays.asList(new Move(Moves.ICEFANG), new Move(Moves.AURORABEAM), new Move(Moves.HYDROPUMP),
        		new Move(Moves.CALMMIND)));
        
        pokeMap.put(TREECKO, Arrays.asList(new Move(Moves.ABSORB), new Move(Moves.QUICKATTACK), new Move(Moves.LEER))); 
        pokeMap.put(GROVYLE, Arrays.asList(new Move(Moves.LEAFBLADE), new Move(Moves.ABSORB), new Move(Moves.QUICKATTACK),
        		new Move(Moves.AGILITY))); 
        pokeMap.put(SCEPTILE, Arrays.asList(new Move(Moves.LEAFBLADE), new Move(Moves.LEAFSTORM), new Move(Moves.GIGADRAIN),
        		new Move(Moves.AGILITY))); 
        
        pokeMap.put(TORCHIC, Arrays.asList(new Move(Moves.EMBER), new Move(Moves.SCRATCH), new Move(Moves.GROWL))); 
        pokeMap.put(COMBUSKEN, Arrays.asList(new Move(Moves.EMBER), new Move(Moves.DOUBLEKICK), new Move(Moves.SLASH), 
        		new Move(Moves.GROWL))); 
        pokeMap.put(BLAZIKEN, Arrays.asList(new Move(Moves.FIREPUNCH), new Move(Moves.BLAZEKICK), new Move(Moves.FLAREBLITZ),
        		new Move(Moves.SKYUPPERCUT))); 
        
        pokeMap.put(MUDKIP, Arrays.asList(new Move(Moves.WATERGUN), new Move(Moves.TACKLE), new Move(Moves.GROWL))); 
        pokeMap.put(MARSHTOMP, Arrays.asList(new Move(Moves.WATERGUN), new Move(Moves.MUDSHOT), new Move(Moves.MUDSLAP), 
        		new Move(Moves.TACKLE))); 
        pokeMap.put(SWAMPERT, Arrays.asList(new Move(Moves.SURF), new Move(Moves.MUDDYWATER), new Move(Moves.MUDBOMB),
        		new Move(Moves.EARTHQUAKE)));
        
        pokeMap.put(KYOGRE, Arrays.asList(new Move(Moves.SURF), new Move(Moves.HYDROPUMP), new Move(Moves.THUNDER),
        		new Move(Moves.CALMMIND)));
        pokeMap.put(GROUDON, Arrays.asList(new Move(Moves.EARTHQUAKE), new Move(Moves.FIREFANG), new Move(Moves.DRAGONCLAW), 
        		new Move(Moves.SOLARBEAM, 2)));
        pokeMap.put(RAYQUAZA, Arrays.asList(new Move(Moves.DRAGONCLAW), new Move(Moves.EXTREMESPEED), new Move(Moves.BLIZZARD), 
        		new Move(Moves.FLY, 2))); 
        		
        */
        
        // if pokemon not already mapped to moveset
        if (pokemon.getMoveSet().isEmpty()) {
        	// add each move to passed in pokemon object
            for (int i = 0; i < pokeMap.get(pokemon).size(); i++) {        	
            	pokemon.addMove(pokeMap.get(pokemon).get(i));
            } 	
        }
        
        return pokemon;
	}
	/** END MAP MOVES METHOD **/
		
	public static Pokemon getPokemon(int index) { 		
		Pokemon pokemon = mapMoves(POKEDEX.get(index));
		return pokemon; 
	}	
	
	/** POKEDEX ARRAYLIST GETTERS **/
	public static Pokemon getPokemon(String name) {
		
		for (Pokemon pokemon : POKEDEX) {
			if (pokemon.getName().equals(name)) {
				return pokemon;
			}
		}
		return null;
	}
	public static List<Pokemon> getPokedex() {
		return POKEDEX; 
	}
	public static int getPokedexSize() { 
		return POKEDEX.size(); 
	}	
	/** END POKEDEX ARRAYLIST GETTERS **/
	
	/** CAN EVOLVE METHOD **/
	public boolean canEvolve() {		
		
		// pokemon can't evolve if evLevel is -1
		return this.getEvLevel() != -1;
	}
	/** END CAN EVOLVE METHOD **/
	
	/** EVOLVE POKEMON  METHOD **/
	public Pokemon evolve() {
		
		// check if pekemon can evolve to avoid errors
		if (this.canEvolve()) {
			
			// find current position of calling object in pokedex
			int nextID = POKEDEX.indexOf(this);
			
			// evolved form is next position in pokedex
			Pokemon evolvedPokemon = Pokemon.getPokemon(nextID + 1);
			
			return evolvedPokemon;
		}
		// pokemon can't evolve
		else {
			return null;	
		}
	}
	/** END EVOLVE POKEMON METHOD **/
	
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
	
	/** LIST MOVE SET FOR GIVEN POKEMON METHOD **/
	public void listMoves() {			
		
		System.out.println("MOVESET FOR " + this.name + ":\n");
		
		for (Move move : moveSet) {
			System.out.println(move.getName() + " : (TYPE: " + move.getType()  + "), (PP: " + move.getpp() + 
					"), (PWR: " + move.getPower() + ")"	+ ", (ACC: " + move.getAccuracy() + ")");
		}
	}
	/** END LIST MOVE SET METHOD **/
	
	/** SET NATURE METHOD **/
	public void setNature() {
				
		// random Nature selection
		int num = 0 + (int)(Math.random() * ((Nature.getNatures().size() - 0) + 0));
		nature = Nature.getNatures().get(num);
				
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
	/** END SET NATURE METHOD **/
	
	/** GETTERS AND SETTERS **/	
	public String toString() {
		return name.toUpperCase();
	}
	
	public BufferedImage getFrontSprite() { return frontSprite; }
	public BufferedImage getBackSprite() { return backSprite; }
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }	
	public int getIndex() {	return index; }
	public void setIndex(int index) { this.index = index; }
	public char getSex() { return sex; }
	public String printSex() { 			
		return sex + ""; 
	}
	public Color getSexColor() {
		if (sex == '♂') return Color.BLUE;		
		else return Color.RED;		
	}
	public void setSex(char sex) { this.sex = sex; }

	public Type getType() { return type; }
	public void setType(Type type) { this.type = type; }	
	public List<Type> getTypes() { return types; }	
	public void setTypes(List<Type> types) { this.types = types; }	
	public String printTypes() {
		
		String s = "";		
		int i = types.size() - 1;
		for (Type t : types) {
			 s += t.printType();		
			 if (i-- != 0)
				 s += " / ";
		}
		return s;
	}
	public String printTypesShort() {
		String s = "";		
		int i = types.size() - 1;
		for (Type t : types) {
			 s += t.toString().charAt(0);
			 s += t.toString().toLowerCase().charAt(1);
			 if (i-- != 0)
				 s += "/";
		}
		return s;
	}
	
	public int getLevel() {	return level; }	
	public void setLevel(int level) { 	
		
		// don't initiate twice on same pokemon
		if (level != this.level && level != -1) {
			this.level = level;			
			this.hp = (int)(Math.floor(((2 * this.hp + this.iv + Math.floor(ev / 4)) * level) / 100) + level + 10);
			this.bhp = hp;
		}
	}
	
	public Nature getNature() { return nature; }
	public void setNature(Nature nature) { this.nature = nature; }
	
	public int getXP() { return xp; }
	public void setXP(int xp) {	this.xp = xp; }
	public int getBXP() { return bxp; }
	public void setBXP(int bxp) {	this.bxp = bxp; }

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
	
	public int getEvLevel() { return evLevel; }
	public void setEvLevel(int evLevel) { this.evLevel = evLevel; }	
	public int getEV() { return ev; }
	public void setEV(int ev) { this.ev = ev; }	
	public int getIV() { return iv; }
	public void setIV(int iv) { this.iv = iv; }

	public Status getStatus() { return status; }
	public void setStatus(Status status) { this.status = status; }	
	public int getStatusCounter() { return statusCounter; }
	public void setStatusCounter(int statusCounter) { this.statusCounter = statusCounter; }	
	public int getStatusLimit() { return statusLimit; }
	public void setStatusLimit(int statusLimit) { this.statusLimit = statusLimit; }
	
	public List<Move> getMoveSet() { return moveSet; }
	public void setMoveSet(ArrayList<Move> moveSet) { this.moveSet = moveSet; }
	
	public boolean isAlive() { return isAlive; }
	public void setAlive(boolean isAlive) {	this.isAlive = isAlive; }
	/** END GETTERS AND SETTERS **/
	
	public void changeStat(String stat, int level) {	
		
		switch (stat) {
			case "attack":
				if (this.attackStg + level > 6 || this.attackStg + level < -6) {
					if (level >= 1) 
						System.out.println(this.name + "'s attack won't go any higher!");
					else if (level <= -1) 
						System.out.println(this.name + "'s attack won't go any lower!");
					return;
				}
				else {	
					this.attackStg += level;
					this.attack *= Math.max(2, 2 + (double) this.attackStg) / Math.max(2, 2 - (double) this.attackStg);	
		
					outputChange(stat, level);
				}	
				break;
			case "sp. attack":
				if (this.spAttackStg + level > 6 || this.spAttackStg + level < -6) {
					if (level >= 1) 
						System.out.println(this.name + "'s sp. attack won't go any higher!");
					else if (level <= -1) 
						System.out.println(this.name + "'s sp. attack won't go any lower!");
					return;
				}
				else {	
					this.spAttackStg += level;
					this.spAttack *= Math.max(2, 2 + (double) this.spAttackStg) / Math.max(2, 2 - (double) this.spAttackStg);	
					
					outputChange(stat, level);
				}
				break;
			case "defense":
				if (this.defenseStg + level > 6 || this.defenseStg + level < -6) {
					if (level >= 1) 
						System.out.println(this.name + "'s defense won't go any higher!");
					else if (level <= -1) 
						System.out.println(this.name + "'s defense won't go any lower!");
					return;
				}
				else {	
					this.defenseStg += level;
					this.defense *= Math.max(2, 2 + (double) this.defenseStg) / Math.max(2, 2 - (double) this.defenseStg);
					
					outputChange(stat, level);
				}	
				break;
			case "sp. defense":
				if (this.spDefenseStg + level > 6 || this.spDefenseStg  + level < -6) {
					if (level >= 1) 
						System.out.println(this.name + "'s sp. defense won't go any higher!");
					else if (level <= -1) 
						System.out.println(this.name + "'s sp. defense won't go any lower!");
					return;
				}
				else {	
					this.spDefenseStg  += level;
					this.spDefense *= Math.max(2, 2 + (double) this.spDefenseStg ) / Math.max(2, 2 - (double) this.spDefenseStg);	
					
					outputChange(stat, level);
				}	
				break;
			case "speed":
				if (this.speedStg + level > 6 || this.speedStg + level < -6) {
					if (level >= 1) 
						System.out.println(this.name + "'s speed won't go any higher!");
					else if (level <= -1) 
						System.out.println(this.name + "'s speed won't go any lower!");
					return;
				}
				else {	
					this.speedStg += level;					
					this.speed *= Math.max(2, 2 + (double) this.speedStg) / Math.max(2, 2 - (double) this.speedStg);	
					
					outputChange(stat, level);
				}	
				break;
			case "accuracy":
				if (this.accuracyStg + level > 6 || this.accuracyStg + level < -6) {
					if (level >= 1) 
						System.out.println(this.name + "'s accuracy won't go any higher!");
					else if (level <= -1) 
						System.out.println(this.name + "'s accuracy won't go any lower!");
					return;
				}
				else {	
					this.accuracyStg += level;
					this.accuracy = Math.max(3, 3 + (double) this.accuracyStg) / Math.max(3, 3 - (double) this.accuracyStg);	
					
					outputChange(stat, level);
				}	
				break;
		}
	}
	private void outputChange(String stat, int level) {
		
		if (level == 1)
			System.out.println(this.name + "'s " + stat + " rose!");
		else if (level == 2) 
			System.out.println(this.name + "'s " + stat + " greatly rose!");
		else if (level >= 3)
			System.out.println(this.name + "'s " + stat + " drastically rose!");
		else if (level == -1)
			System.out.println(this.name + "'s " + stat + " fell!");
		else if (level == -2)
			System.out.println(this.name + "'s " + stat + " greatly fell!");
		else if (level <= -3)
			System.out.println(this.name + "'s " + stat + " severely fell!");
	}
	
	// IMAGE MANAGERS
	public BufferedImage setup(String imagePath, int width, int height) {
		
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
}
/*** END POKEDEX CLASS ***/

@FunctionalInterface
interface Calculate {
	public int compute(int j, int k, int l, int m);
}