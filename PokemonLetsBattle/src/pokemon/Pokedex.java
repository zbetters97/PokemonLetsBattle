package pokemon;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import application.GamePanel;
import moves.*;
import properties.*;

/*** POKEDEX ENUM CLASS ***/
public enum Pokedex {	
	/** STAT REFERECE: https://www.serebii.net/pokemon/ **/
	/** EXP & EV REFERENCE: https://bulbapedia.bulbagarden.net/wiki/List_of_Pok%C3%A9mon_by_effort_value_yield_in_Generation_IV **/
	/** XP GROWTH REFERENCE: https://bulbapedia.bulbagarden.net/wiki/List_of_Pok%C3%A9mon_by_experience_type **/	
	/** CATCH RATE REFERENCE: https://bulbapedia.bulbagarden.net/wiki/List_of_Pokémon_by_catch_rate **/
	/** MOVE LEVELS REFERENCE: https://gamefaqs.gamespot.com/gba/563596-pokemon-sapphire-version/faqs/22795 **/
	
	BULBASAUR ("Bulbasaur", 1, Type.GRASS, 45, 49, 49, 65, 65, 45, 16, 64, 3, 1, 45),
	IVYSAUR ("Ivysaur", 2, Arrays.asList(Type.GRASS, Type.POISON), 60, 62, 63, 80, 80, 60, 32, 141, 3, 2, 45),
	VENUSAUR ("Venusaur", 3, Arrays.asList(Type.GRASS, Type.POISON), 80, 82, 83, 100, 100, 80, -1, 208, 3, 3, 45),
	CHARMANDER ("Charmander", 4, Type.FIRE, 39, 52, 43, 60, 50, 65, 16, 65, 3, 1, 45),
	CHARMELEON ("Charmeleon", 5, Type.FIRE, 58, 64, 58, 80, 65, 80, 36, 142, 3, 2, 45),
	CHARIZARD ("Charizard", 6, Arrays.asList(Type.FIRE, Type.FLYING), 78, 84, 78, 109, 85, 100, -1, 209, 3, 3, 45),
	SQUIRTLE ("Squirtle", 7, Type.WATER, 44, 48, 65, 50, 64, 43, 16, 66, 3, 1, 45),
	WARTORTLE ("Wartortle", 8, Type.WATER, 59, 63, 80, 65, 80, 58, 36, 143, 3, 2, 45),
	BLASTOISE ("Blastoise", 9, Type.WATER, 79, 83, 100, 85, 105, 78, -1, 210, 3, 3, 45),	
	PIKACHU ("Pikachu", 25, Type.ELECTRIC, 55, 55, 40, 50, 50, 90, 30, 82, 0, 2, 190),
	RAICHU ("Raichu", 26, Type.ELECTRIC, 60, 90, 55, 90, 80, 110, -1, 122, 0, 3, 75),
	ABRA ("Abra", 63, Type.PSYCHIC, 25, 20, 15, 105, 55, 90, 16, 75, 3, 1, 200),
	KADABRA ("Kadabra", 64, Type.PSYCHIC, 40, 35, 30, 120, 70, 105, 36, 145, 3, 2, 100),
	ALAKAZAM ("Alakazam", 65, Type.PSYCHIC, 55, 50, 45, 135, 95, 120, -1, 186, 3, 3, 50),
	MACHOP ("Machop", 66, Type.FIGHTING, 70, 80, 50, 35, 35, 35, 28, 75, 3, 1, 180),
	MACHOKE ("Machoke", 67, Type.FIGHTING, 80, 100, 70, 50, 60, 45, 40, 146, 3, 2, 90),
	MACHAMP ("Machamp", 68, Type.FIGHTING, 90, 130, 80, 65, 85, 55, -1, 193, 3, 3, 45),
	GEODUDE ("Geodude", 74, Arrays.asList(Type.ROCK, Type.GROUND), 40, 80, 100, 30, 30, 20, 25, 73, 3, 1, 255),
	GRAVELER ("Graveler", 75, Arrays.asList(Type.ROCK, Type.GROUND), 55, 95, 115, 45, 45, 35, 40, 134, 3, 2, 120),
	GOLEM ("Golem", 76, Arrays.asList(Type.ROCK, Type.GROUND), 80, 120, 130, 55, 65, 45, -1, 177, 3, 3, 45),
	GASTLY ("Gastly", 92, Arrays.asList(Type.GHOST, Type.POISON), 30, 35, 30, 100, 35, 80, 25, 95, 3, 1, 190),
	HAUNTER ("Haunter", 93, Arrays.asList(Type.GHOST, Type.POISON), 45, 50, 45, 115, 55, 96, 40, 126, 3, 2, 90),
	GENGAR ("Gengar", 94, Arrays.asList(Type.GHOST, Type.POISON), 60, 65, 60, 130, 75, 110, -1, 190, 3, 3, 45),
	HORSEA ("Horsea", 116, Type.WATER, 30, 40, 70, 70, 25, 60, 32, 83, 0, 1, 225),
	SEADRA ("Seadra", 117, Type.WATER, 55, 65, 95, 95, 45, 85, 45, 155, 0, 2, 75),
	LAPRAS ("Lapras", 131, Arrays.asList(Type.WATER, Type.ICE), 130, 85, 80, 85, 95, 60, -1, 219, 5, 2, 45),
	SNORLAX ("Snorlax", 143, Type.NORMAL, 160, 110, 65, 65, 110, 30, -1, 154, 5, 2, 25),
	KINGDRA ("Kingdra", 230, Arrays.asList(Type.WATER, Type.DRAGON), 75, 95, 95, 95, 95, 85, -1, 207, 0, 3, 45),
	RAIKOU ("Raikou", 243, Type.ELECTRIC, 90, 85, 75, 115, 100, 115, -1, 216, 5, 3, 3),
	ENTEI ("Entei", 244, Type.FIRE, 115, 115, 85, 90, 75, 100, -1, 217, 5, 3, 3),
	SUICUNE ("Suicune", 245, Type.WATER, 100, 75, 115, 90, 115, 85, -1, 215, 5, 3, 3),
	TREECKO ("Treecko", 252, Type.GRASS, 40, 45, 35, 65, 55, 70, 16, 65, 3, 1, 45, 
			Map.ofEntries(
					Map.entry(6, Moves.QUICKATTACK),
					Map.entry(11, Moves.QUICKATTACK),
					Map.entry(31, Moves.AGILITY),
					Map.entry(36, Moves.SLAM),
					Map.entry(46, Moves.GIGADRAIN)
			)),
	GROVYLE ("Grovyle", 253, Type.GRASS, 50, 65, 45, 85, 65, 95, 36, 141, 3, 2, 45, 
			Map.ofEntries(
					Map.entry(29, Moves.LEAFBLADE),
					Map.entry(35, Moves.AGILITY),
					Map.entry(41, Moves.SLAM)
			)),
	SCEPTILE ("Sceptile", 254, Type.GRASS, 70, 85, 65, 105, 85, 120, -1, 208, 3, 3, 45,
			Map.ofEntries(
					Map.entry(29, Moves.LEAFBLADE),
					Map.entry(35, Moves.AGILITY),
					Map.entry(41, Moves.SLAM)
			)),
	TORCHIC ("Torchic", 255, Type.FIRE, 45, 60, 40, 70, 50, 45, 16, 65, 3, 1, 45,
			Map.ofEntries(
					Map.entry(10, Moves.EMBER),
					Map.entry(28, Moves.QUICKATTACK),
					Map.entry(34, Moves.SLASH),
					Map.entry(43, Moves.FLAMETHROWER)
			)),
	COMBUSKEN ("Combusken", 256, Arrays.asList(Type.FIRE, Type.FIGHTING), 60, 85, 60, 85, 60, 55, 36, 142, 3, 2, 45,
			Map.ofEntries(
					Map.entry(16, Moves.DOUBLEKICK),
					Map.entry(28, Moves.QUICKATTACK),
					Map.entry(39, Moves.SLASH)
			)),
	BLAZIKEN ("Blaziken", 257, Arrays.asList(Type.FIRE, Type.FIGHTING), 80, 120, 70, 110, 70, 80, -1, 209, 3, 3, 45, 
			Map.ofEntries(
					Map.entry(36, Moves.BLAZEKICK),
					Map.entry(42, Moves.SLASH),
					Map.entry(59, Moves.SKYUPPERCUT)
			)),
	MUDKIP ("Mudkip", 258, Type.WATER, 50, 70, 50, 50, 50, 40, 16, 65, 3, 1, 45, 
			Map.ofEntries(
					Map.entry(6, Moves.WATERGUN),
					Map.entry(28, Moves.TAKEDOWN),
					Map.entry(42, Moves.HYDROPUMP)
			)),
	MARSHTOMP ("Marshtomp", 259, Arrays.asList(Type.WATER, Type.GROUND), 70, 85, 70, 60, 70, 50, 36, 143, 3, 2, 45,
			Map.ofEntries(
					Map.entry(16, Moves.MUDSHOT),
					Map.entry(31, Moves.TAKEDOWN),
					Map.entry(37, Moves.MUDDYWATER),
					Map.entry(46, Moves.EARTHQUAKE)
			)),
	SWAMPERT ("Swampert", 260, Arrays.asList(Type.WATER, Type.GROUND), 100, 110, 90, 85, 90, 60, -1, 210, 3, 3, 45,
			Map.ofEntries(
					Map.entry(39, Moves.MUDDYWATER),
					Map.entry(52, Moves.EARTHQUAKE)
			)),
	SPHEAL("Spheal", 363, Arrays.asList(Type.ICE, Type.WATER), 70, 40, 50, 55, 50, 25, 32, 75, 3, 1, 255),
	SEALEO("Sealeo", 364, Arrays.asList(Type.ICE, Type.WATER), 90, 60, 70, 75, 70, 45, 44, 128, 3, 2, 120),
	WALREIN("Walrein", 365, Arrays.asList(Type.ICE, Type.WATER), 110, 80, 90, 95, 90, 65, -1, 192, 3, 3, 45),
	KYOGRE ("Kyogre", 382, Type.WATER, 100, 100, 90, 150, 140, 90, -1, 218, 5, 3, 3), 
	GROUDON ("Groudon", 383, Type.GROUND, 100, 150, 140, 100, 90, 90, -1, 218, 5, 3, 3),
	RAYQUAZA ("Rayquaza", 384, Arrays.asList(Type.DRAGON, Type.FLYING), 105, 150, 90, 150, 90, 95, -1, 220, 5, 3, 3);
	/** END INITIALIZE ENUMS **/
				
	/** INITIALIZE VALUES**/
	private final BufferedImage frontSprite, backSprite, menuSprite;
	private final String name;
	private final int index;
	private final Type type;
	private final List<Type> types;
	private int hp, speed, attack, defense, spAttack, spDefense, accuracy, evLevel, ey, growth, ev, catchRate;	
	private Map<Integer, Moves> moveLevels;
	/** END INITIALIZE VALUES **/
		
	/** CONSTRUCTORS **/
	Pokedex(String name, int index, Type type, int hp, int attack, int defense, 
			int spAttack, int spDefense, int speed, int evLevel, int ey, int growth, int ev, int catchRate) {	
		
		this.frontSprite = setup("/pokedexfront/" + name, 48 * 5, 48 * 5); 
		this.backSprite = setup("/pokedexback/" + name, 48 * 5, 48 * 5);
		this.menuSprite = setup("/pokedexmenu/" + name, 48 * 2, 48 * 2);
		
		this.name = name; 
		this.index = index; 
		this.type = type; 
		this.hp = hp;
		this.attack = attack;
		this.defense = defense;
		this.spAttack = spAttack;
		this.spDefense = spDefense;
		this.speed = speed;
		this.ey = ey;
		this.growth = growth;
		this.ev = ev;
		this.evLevel = evLevel; 
		this.catchRate = catchRate;
		
		this.types = null;	
	}
	Pokedex(String name, int index, Type type, int hp, int attack, int defense, 
			int spAttack, int spDefense, int speed, int evLevel, int ey, int growth, int ev, int catchRate, 
			Map<Integer, Moves> moveLevels) {	
		
		this.frontSprite = setup("/pokedexfront/" + name, 48 * 5, 48 * 5); 
		this.backSprite = setup("/pokedexback/" + name, 48 * 5, 48 * 5);
		this.menuSprite = setup("/pokedexmenu/" + name, 48 * 2, 48 * 2);
		
		this.name = name; 
		this.index = index; 
		this.type = type; 
		this.hp = hp;
		this.attack = attack;
		this.defense = defense;
		this.spAttack = spAttack;
		this.spDefense = spDefense;
		this.speed = speed;
		this.ey = ey;
		this.growth = growth;
		this.ev = ev;
		this.evLevel = evLevel; 
		this.catchRate = catchRate;
		
		this.types = null;	
		
		this.moveLevels = moveLevels;
	}
	Pokedex(String name, int index, List<Type> types, int hp, int attack, int defense, 
			int spAttack, int spDefense, int speed, int evLevel, int ey, int growth, int ev, int catchRate) {			
		
		this.frontSprite = setup("/pokedexfront/" + name, 48 * 5, 48 * 5); 
		this.backSprite = setup("/pokedexback/" + name, 48 * 5, 48 * 5);
		this.menuSprite = setup("/pokedexmenu/" + name, 48 * 2, 48 * 2);

		this.name = name; 
		this.index = index; 
		this.types = types; 
		this.hp = hp;
		this.attack = attack;
		this.defense = defense;
		this.spAttack = spAttack;
		this.spDefense = spDefense;
		this.speed = speed;
		this.ey = ey;
		this.growth = growth;
		this.ev = ev;
		this.evLevel = evLevel; 
		this.catchRate = catchRate;
		
		this.type = null;		
	}
	Pokedex(String name, int index, List<Type> types, int hp, int attack, int defense, 
			int spAttack, int spDefense, int speed, int evLevel, int ey, int growth, int ev, int catchRate, 
			Map<Integer, Moves> moveLevels) {			
		
		this.frontSprite = setup("/pokedexfront/" + name, 48 * 5, 48 * 5); 
		this.backSprite = setup("/pokedexback/" + name, 48 * 5, 48 * 5);
		this.menuSprite = setup("/pokedexmenu/" + name, 48 * 2, 48 * 2);

		this.name = name; 
		this.index = index; 
		this.types = types; 
		this.hp = hp;
		this.attack = attack;
		this.defense = defense;
		this.spAttack = spAttack;
		this.spDefense = spDefense;
		this.speed = speed;
		this.ey = ey;
		this.growth = growth;
		this.ev = ev;
		this.evLevel = evLevel; 
		this.catchRate = catchRate;
		
		this.type = null;		
		this.moveLevels = moveLevels;
	}
	/** END CONSTRUCTORS **/
	
	// LIST TO HOLD ALL POKEMON ENUMS
	private static List<Pokedex> PokemonList = Arrays.asList(Pokedex.values());
	
	/** POKEMON MOVES STATIC MAP **/
	private static final Map<Pokedex, List<Move>> moveMap;
	static {		
		moveMap = new HashMap<>();
		moveMap.put(BULBASAUR, Arrays.asList(new Move(Moves.VINEWHIP), new Move(Moves.TACKLE), new Move(Moves.GROWL)));
        moveMap.put(IVYSAUR, Arrays.asList(new Move(Moves.RAZORLEAF), new Move(Moves.VINEWHIP), new Move(Moves.POISONPOWDER),
        		new Move(Moves.TACKLE)));
		moveMap.put(VENUSAUR, Arrays.asList(new Move(Moves.PETALBLIZZARD), new Move(Moves.SOLARBEAM), new Move(Moves.TAKEDOWN), 
				new Move(Moves.DOUBLEEDGE)));
		
        moveMap.put(CHARMANDER, Arrays.asList(new Move(Moves.EMBER), new Move(Moves.SCRATCH), new Move(Moves.GROWL)));
		moveMap.put(CHARMELEON, Arrays.asList(new Move(Moves.FIREFANG), new Move(Moves.EMBER), new Move(Moves.SLASH), 
				new Move(Moves.GROWL)));
        moveMap.put(CHARIZARD, Arrays.asList(new Move(Moves.FLAMETHROWER), new Move(Moves.FLAREBLITZ),new Move(Moves.DRAGONBREATH),
        	new Move(Moves.FLY)));
 
		moveMap.put(SQUIRTLE, Arrays.asList(new Move(Moves.WATERGUN), new Move(Moves.TACKLE), new Move(Moves.TAILWHIP)));
        moveMap.put(WARTORTLE, Arrays.asList(new Move(Moves.WATERPULSE), new Move(Moves.WATERGUN), new Move(Moves.TAILWHIP)));
        moveMap.put(BLASTOISE, Arrays.asList(new Move(Moves.WATERPULSE), new Move(Moves.AQUATAIL), new Move(Moves.HYDROPUMP), 
        		new Move(Moves.FLASHCANNON)));
        
        moveMap.put(PIKACHU, Arrays.asList(new Move(Moves.THUNDERWAVE), new Move(Moves.THUNDERSHOCK),new Move(Moves.TACKLE),
        		new Move(Moves.PLAYNICE)));
        moveMap.put(RAICHU, Arrays.asList(new Move(Moves.THUNDERPUNCH), new Move(Moves.THUNDERBOLT), new Move(Moves.QUICKATTACK), 
        		new Move(Moves.SLAM)));    
      
        moveMap.put(ABRA, Arrays.asList(new Move(Moves.TELEPORT)));
        moveMap.put(KADABRA, Arrays.asList(new Move(Moves.CONFUSION), new Move(Moves.PSYBEAM), new Move(Moves.KINESIS)));
        moveMap.put(ALAKAZAM, Arrays.asList(new Move(Moves.PSYCHIC), new Move(Moves.CONFUSION), new Move(Moves.PSYCHOCUT), 
        		new Move(Moves.CALMMIND)));     
        
        moveMap.put(MACHOP, Arrays.asList(new Move(Moves.LOWKICK), new Move(Moves.LOWSWEEP), new Move(Moves.KNOCKOFF))); 
        moveMap.put(MACHOKE, Arrays.asList(new Move(Moves.LOWKICK), new Move(Moves.LOWSWEEP), new Move(Moves.VITALTHROW),
        		new Move(Moves.SEISMICTOSS))); 
        moveMap.put(MACHAMP, Arrays.asList(new Move(Moves.SEISMICTOSS), new Move(Moves.DYNAMICPUNCH), new Move(Moves.CROSSCHOP), 
        		new Move(Moves.SCARYFACE))); 
        
        moveMap.put(GEODUDE, Arrays.asList(new Move(Moves.ROCKTHROW), new Move(Moves.TACKLE), new Move(Moves.DEFENSECURL)));
        moveMap.put(GRAVELER, Arrays.asList(new Move(Moves.ROCKTHROW), new Move(Moves.ROLLOUT), new Move(Moves.TACKLE),
        		new Move(Moves.DEFENSECURL)));
        moveMap.put(GOLEM, Arrays.asList(new Move(Moves.EARTHQUAKE), new Move(Moves.DIG), new Move(Moves.HEAVYSLAM), 
        		new Move(Moves.DOUBLEEDGE)));
        
        moveMap.put(GASTLY, Arrays.asList(new Move(Moves.LICK), new Move(Moves.PAYBACK), new Move(Moves.HYPNOSIS)));   
        moveMap.put(HAUNTER, Arrays.asList(new Move(Moves.PAYBACK), new Move(Moves.HEX), new Move(Moves.DARKPULSE), 
        		new Move(Moves.CONFUSERAY)));   
        moveMap.put(GENGAR, Arrays.asList(new Move(Moves.SHADOWBALL), new Move(Moves.SHADOWPUNCH),new Move(Moves.HEX), 
        		new Move(Moves.DARKPULSE))); 
        
        moveMap.put(HORSEA, Arrays.asList(new Move(Moves.WATERGUN), new Move(Moves.BUBBLE), new Move(Moves.LEER))); 
        moveMap.put(SEADRA, Arrays.asList(new Move(Moves.WATERGUN), new Move(Moves.TWISTER), new Move(Moves.HYDROPUMP),
        		new Move(Moves.AGILITY)));       
        moveMap.put(KINGDRA, Arrays.asList(new Move(Moves.SURF), new Move(Moves.HYDROPUMP), new Move(Moves.DRAGONPULSE),
        		new Move(Moves.AGILITY))); 
     
        moveMap.put(LAPRAS, Arrays.asList(new Move(Moves.ICEBEAM), new Move(Moves.HYDROPUMP), new Move(Moves.SHEERCOLD), 
        		new Move(Moves.CONFUSERAY)));     
        
        moveMap.put(SNORLAX, Arrays.asList(new Move(Moves.BODYSLAM), new Move(Moves.ROLLOUT), new Move(Moves.CRUNCH), 
        		new Move(Moves.YAWN)));     
        
        moveMap.put(RAIKOU, Arrays.asList(new Move(Moves.THUNDERFANG), new Move(Moves.THUNDER), new Move(Moves.CRUNCH),
        		new Move(Moves.CALMMIND)));
        moveMap.put(ENTEI, Arrays.asList(new Move(Moves.FIREFANG), new Move(Moves.FLAMETHROWER), new Move(Moves.EXTRASENSORY),
        		new Move(Moves.CALMMIND)));
        moveMap.put(SUICUNE, Arrays.asList(new Move(Moves.ICEFANG), new Move(Moves.AURORABEAM), new Move(Moves.HYDROPUMP),
        		new Move(Moves.CALMMIND)));
        
        moveMap.put(TREECKO, Arrays.asList(new Move(Moves.POUND), new Move(Moves.LEER)));      
        moveMap.put(GROVYLE, Arrays.asList(new Move(Moves.ABSORB), new Move(Moves.QUICKATTACK), new Move(Moves.POUND),
        		new Move(Moves.LEER))); 
        moveMap.put(SCEPTILE, Arrays.asList(new Move(Moves.LEAFBLADE), new Move(Moves.QUICKATTACK), new Move(Moves.ABSORB),
        		new Move(Moves.AGILITY))); 
       
        moveMap.put(TORCHIC, Arrays.asList(new Move(Moves.SCRATCH), new Move(Moves.GROWL))); 
        moveMap.put(COMBUSKEN, Arrays.asList(new Move(Moves.EMBER), new Move(Moves.SCRATCH), new Move(Moves.GROWL))); 
        moveMap.put(BLAZIKEN, Arrays.asList(new Move(Moves.FLAREBLITZ), new Move(Moves.QUICKATTACK), new Move(Moves.SCRATCH),
        		new Move(Moves.GROWL)));

        moveMap.put(MUDKIP, Arrays.asList(new Move(Moves.TACKLE), new Move(Moves.GROWL))); 
        moveMap.put(MARSHTOMP, Arrays.asList(new Move(Moves.WATERGUN), new Move(Moves.MUDSHOT), new Move(Moves.TACKLE),
        		new Move(Moves.GROWL)));
        moveMap.put(SWAMPERT, Arrays.asList(new Move(Moves.SURF), new Move(Moves.MUDSHOT), new Move(Moves.TAKEDOWN),
        		new Move(Moves.GROWL)));
        
        moveMap.put(SPHEAL, Arrays.asList(new Move(Moves.WATERGUN), new Move(Moves.DEFENSECURL), new Move(Moves.GROWL))); 
        moveMap.put(SEALEO, Arrays.asList(new Move(Moves.WATERGUN), new Move(Moves.AURORABEAM), new Move(Moves.BODYSLAM), 
        		new Move(Moves.YAWN))); 
        moveMap.put(WALREIN, Arrays.asList(new Move(Moves.AURORABEAM), new Move(Moves.BLIZZARD), new Move(Moves.SHEERCOLD),
        		new Move(Moves.SURF)));
     
        moveMap.put(KYOGRE, Arrays.asList(new Move(Moves.SURF), new Move(Moves.HYDROPUMP), new Move(Moves.THUNDER),
        		new Move(Moves.CALMMIND)));
        moveMap.put(GROUDON, Arrays.asList(new Move(Moves.EARTHQUAKE), new Move(Moves.FIREFANG), new Move(Moves.DRAGONCLAW), 
        		new Move(Moves.SOLARBEAM)));
        moveMap.put(RAYQUAZA, Arrays.asList(new Move(Moves.DRAGONCLAW), new Move(Moves.EXTREMESPEED), new Move(Moves.BLIZZARD), 
        		new Move(Moves.FLY))); 
	}
	/** END POKEMON MOVES STATIC MAP **/
	
	// pokemon can't evolve if evLevel is -1
	protected boolean canEvolve() { return this.getEvLevel() != -1; }
			
	/** GETTERS **/	
	protected BufferedImage getFrontSprite() { return frontSprite; }
	protected BufferedImage getBackSprite() { return backSprite; }
	protected BufferedImage getMenuSprite() { return menuSprite; }
	public String getName() { return name; }
	protected int getIndex() {	return index; }	
	protected Type getType() { return type; }
	protected List<Type> getTypes() { return types; }		
	protected int getHP() { return hp; }
	protected int getSpeed() { return speed; }
	protected int getAttack() { return attack; }
	protected int getDefense() { return defense; }
	protected int getSpAttack() { return spAttack; }
	protected int getSpDefense() {	return spDefense; }
	protected int getAccuracy() { return accuracy; }
	protected int getEXPYeild() { return ey; }
	protected int getGrowth() { return growth; }
	protected int getEvLevel() { return evLevel; }
	protected int getEV() { return ev; }		
	protected int getCatchRate() { return catchRate; }		
	
	protected static List<Pokedex> getPokemonList() { return PokemonList; }
	protected static Map<Pokedex, List<Move>> getMovemap() { return moveMap; }	
	protected Map<Integer, Moves> getMoveLevelMap() { return moveLevels; }	
	/** END GETTERS **/
	
	// IMAGE MANAGERS
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
}
/*** END POKEDEX CLASS ***/