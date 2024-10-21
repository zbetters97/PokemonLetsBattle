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
	/** MOVE LEVELS REFERENCE: https://www.serebii.net/pokemon/ **/
	
	BULBASAUR ("Bulbasaur", 1, Type.GRASS, 45, 49, 49, 65, 65, 45, 16, 64, 3, 1, 45,
			Map.ofEntries(
					Map.entry(9, Moves.VINEWHIP),
					Map.entry(12, Moves.POISONPOWDER),
					Map.entry(15, Moves.TAKEDOWN),
					Map.entry(19, Moves.RAZORLEAF),
					Map.entry(27, Moves.DOUBLEEDGE)
			)),
	IVYSAUR ("Ivysaur", 2, Arrays.asList(Type.GRASS, Type.POISON), 60, 62, 63, 80, 80, 60, 32, 141, 3, 2, 45, 
			Map.ofEntries(					
					Map.entry(20, Moves.RAZORLEAF),
					Map.entry(31, Moves.DOUBLEEDGE),
					Map.entry(44, Moves.SOLARBEAM)
			)),
	VENUSAUR ("Venusaur", 3, Arrays.asList(Type.GRASS, Type.POISON), 80, 82, 83, 100, 100, 80, -1, 208, 3, 3, 45,
			Map.ofEntries(					
					Map.entry(53, Moves.SOLARBEAM)
			)),
	CHARMANDER ("Charmander", 4, Type.FIRE, 39, 52, 43, 60, 50, 65, 16, 65, 3, 1, 45,
			Map.ofEntries(
					Map.entry(7, Moves.EMBER),
					Map.entry(25, Moves.FIREFANG),
					Map.entry(28, Moves.SLASH),
					Map.entry(34, Moves.FLAMETHROWER)
			)),
	CHARMELEON ("Charmeleon", 5, Type.FIRE, 58, 64, 58, 80, 65, 80, 36, 142, 3, 2, 45,
			Map.ofEntries(
					Map.entry(28, Moves.FIREFANG),
					Map.entry(32, Moves.SLASH),
					Map.entry(39, Moves.FLAMETHROWER)
			)),
	CHARIZARD ("Charizard", 6, Arrays.asList(Type.FIRE, Type.FLYING), 78, 84, 78, 109, 85, 100, -1, 209, 3, 3, 45,
			Map.ofEntries(
					Map.entry(42, Moves.FLAMETHROWER),
					Map.entry(66, Moves.FLAREBLITZ)
			)),
	SQUIRTLE ("Squirtle", 7, Type.WATER, 44, 48, 65, 50, 64, 43, 16, 66, 3, 1, 45,
			Map.ofEntries(
					Map.entry(7, Moves.BUBBLE),
					Map.entry(13, Moves.WATERGUN),
					Map.entry(25, Moves.WATERPULSE),
					Map.entry(28, Moves.AQUATAIL),
					Map.entry(37, Moves.HYDROPUMP)
			)),
	WARTORTLE ("Wartortle", 8, Type.WATER, 59, 63, 80, 65, 80, 58, 36, 143, 3, 2, 45,
			Map.ofEntries(
					Map.entry(28, Moves.WATERPULSE),
					Map.entry(32, Moves.AQUATAIL),
					Map.entry(44, Moves.HYDROPUMP)
			)),
	BLASTOISE ("Blastoise", 9, Type.WATER, 79, 83, 100, 85, 105, 78, -1, 210, 3, 3, 45,
			Map.ofEntries(
					Map.entry(53, Moves.HYDROPUMP)
			)),
	PIKACHU ("Pikachu", 25, Type.ELECTRIC, 55, 55, 40, 50, 50, 90, 30, 82, 0, 2, 190,
			Map.ofEntries(
					Map.entry(5, Moves.TAILWHIP),
					Map.entry(10, Moves.THUNDERWAVE),
					Map.entry(13, Moves.QUICKATTACK),
					Map.entry(21, Moves.SLAM),
					Map.entry(26, Moves.THUNDERBOLT),
					Map.entry(34, Moves.AGILITY),
					Map.entry(45, Moves.THUNDER)
			)),
	RAICHU ("Raichu", 26, Type.ELECTRIC, 60, 90, 55, 90, 80, 110, -1, 122, 0, 3, 75, null),
	ABRA ("Abra", 63, Type.PSYCHIC, 25, 20, 15, 105, 55, 90, 16, 75, 3, 1, 200, null),
	KADABRA ("Kadabra", 64, Type.PSYCHIC, 40, 35, 30, 120, 70, 105, 36, 145, 3, 2, 100,
			Map.ofEntries(
					Map.entry(16, Moves.CONFUSION),
					Map.entry(24, Moves.PSYBEAM),
					Map.entry(34, Moves.PSYCHOCUT),
					Map.entry(40, Moves.PSYCHIC)
			)),
	ALAKAZAM ("Alakazam", 65, Type.PSYCHIC, 55, 50, 45, 135, 95, 120, -1, 186, 3, 3, 50,
			Map.ofEntries(
					Map.entry(36, Moves.CALMMIND),
					Map.entry(40, Moves.PSYCHIC)
			)),
	MACHOP ("Machop", 66, Type.FIGHTING, 70, 80, 50, 35, 35, 35, 28, 75, 3, 1, 180,
			Map.ofEntries(
					Map.entry(19, Moves.SEISMICTOSS),
					Map.entry(25, Moves.VITALTHROW),
					Map.entry(37, Moves.CROSSCHOP),
					Map.entry(46, Moves.DYNAMICPUNCH)
			)),
	MACHOKE ("Machoke", 67, Type.FIGHTING, 80, 100, 70, 50, 60, 45, 40, 146, 3, 2, 90,
			Map.ofEntries(
					Map.entry(19, Moves.SEISMICTOSS),
					Map.entry(25, Moves.VITALTHROW),
					Map.entry(40, Moves.CROSSCHOP),
					Map.entry(51, Moves.DYNAMICPUNCH)
			)),
	MACHAMP ("Machamp", 68, Type.FIGHTING, 90, 130, 80, 65, 85, 55, -1, 193, 3, 3, 45,
			Map.ofEntries(
					Map.entry(40, Moves.CROSSCHOP),
					Map.entry(51, Moves.DYNAMICPUNCH)
			)),
	GEODUDE ("Geodude", 74, Arrays.asList(Type.ROCK, Type.GROUND), 40, 80, 100, 30, 30, 20, 25, 73, 3, 1, 255,
			Map.ofEntries(
					Map.entry(11, Moves.ROCKTHROW),
					Map.entry(22, Moves.ROLLOUT),
					Map.entry(29, Moves.EARTHQUAKE),
					Map.entry(36, Moves.DOUBLEEDGE)
			)),
	GRAVELER ("Graveler", 75, Arrays.asList(Type.ROCK, Type.GROUND), 55, 95, 115, 45, 45, 35, 40, 134, 3, 2, 120,
			Map.ofEntries(
					Map.entry(11, Moves.ROCKTHROW),
					Map.entry(22, Moves.ROLLOUT),
					Map.entry(33, Moves.EARTHQUAKE),
					Map.entry(36, Moves.DOUBLEEDGE)
			)),
	GOLEM ("Golem", 76, Arrays.asList(Type.ROCK, Type.GROUND), 80, 120, 130, 55, 65, 45, -1, 177, 3, 3, 45,
			Map.ofEntries(
					Map.entry(11, Moves.ROCKTHROW),
					Map.entry(22, Moves.ROLLOUT),
					Map.entry(33, Moves.EARTHQUAKE),
					Map.entry(44, Moves.DOUBLEEDGE)
			)),
	GASTLY ("Gastly", 92, Arrays.asList(Type.GHOST, Type.POISON), 30, 35, 30, 100, 35, 80, 25, 95, 3, 1, 190,
			Map.ofEntries(
					Map.entry(19, Moves.CONFUSERAY),
					Map.entry(26, Moves.PAYBACK),
					Map.entry(29, Moves.SHADOWBALL),
					Map.entry(36, Moves.DARKPULSE)
			)),
	HAUNTER ("Haunter", 93, Arrays.asList(Type.GHOST, Type.POISON), 45, 50, 45, 115, 55, 96, 40, 126, 3, 2, 90,
			Map.ofEntries(
					Map.entry(19, Moves.CONFUSERAY),
					Map.entry(25, Moves.SHADOWPUNCH),
					Map.entry(28, Moves.PAYBACK),
					Map.entry(33, Moves.SHADOWBALL),
					Map.entry(44, Moves.DARKPULSE)
			)),
	GENGAR ("Gengar", 94, Arrays.asList(Type.GHOST, Type.POISON), 60, 65, 60, 130, 75, 110, -1, 190, 3, 3, 45,
			Map.ofEntries(
					Map.entry(19, Moves.CONFUSERAY),
					Map.entry(25, Moves.SHADOWPUNCH),
					Map.entry(28, Moves.PAYBACK),
					Map.entry(33, Moves.SHADOWBALL),
					Map.entry(44, Moves.DARKPULSE)
			)),
	HORSEA ("Horsea", 116, Type.WATER, 30, 40, 70, 70, 25, 60, 32, 83, 0, 1, 225,
			Map.ofEntries(
					Map.entry(8, Moves.LEER),
					Map.entry(11, Moves.WATERGUN),
					Map.entry(23, Moves.AGILITY),
					Map.entry(26, Moves.TWISTER),
					Map.entry(35, Moves.HYDROPUMP),
					Map.entry(42, Moves.DRAGONPULSE)
			)),
	SEADRA ("Seadra", 117, Type.WATER, 55, 65, 95, 95, 45, 85, 45, 155, 0, 2, 75,
			Map.ofEntries(
					Map.entry(8, Moves.LEER),
					Map.entry(11, Moves.WATERGUN),
					Map.entry(23, Moves.AGILITY),
					Map.entry(26, Moves.TWISTER),
					Map.entry(40, Moves.HYDROPUMP),
					Map.entry(57, Moves.DRAGONPULSE)
			)),
	LAPRAS ("Lapras", 131, Arrays.asList(Type.WATER, Type.ICE), 130, 85, 80, 85, 95, 60, -1, 219, 5, 2, 45,
			Map.ofEntries(
					Map.entry(7, Moves.CONFUSERAY),
					Map.entry(14, Moves.WATERPULSE),
					Map.entry(18, Moves.BODYSLAM),
					Map.entry(32, Moves.ICEBEAM),
					Map.entry(49, Moves.HYDROPUMP),
					Map.entry(55, Moves.SHEERCOLD)
			)),
	SNORLAX ("Snorlax", 143, Type.NORMAL, 160, 110, 65, 65, 110, 30, -1, 154, 5, 2, 25,
			Map.ofEntries(
					Map.entry(4, Moves.DEFENSECURL),
					Map.entry(12, Moves.LICK),
					Map.entry(20, Moves.YAWN),
					Map.entry(33, Moves.BODYSLAM),
					Map.entry(41, Moves.ROLLOUT),
					Map.entry(44, Moves.CRUNCH)
			)),
	KINGDRA ("Kingdra", 230, Arrays.asList(Type.WATER, Type.DRAGON), 75, 95, 95, 95, 95, 85, -1, 207, 0, 3, 45,
			Map.ofEntries(
					Map.entry(8, Moves.LEER),
					Map.entry(11, Moves.WATERGUN),
					Map.entry(23, Moves.AGILITY),
					Map.entry(26, Moves.TWISTER),
					Map.entry(40, Moves.HYDROPUMP),
					Map.entry(57, Moves.DRAGONPULSE)
			)),
	RAIKOU ("Raikou", 243, Type.ELECTRIC, 90, 85, 75, 115, 100, 115, -1, 216, 5, 3, 3,
			Map.ofEntries(
					Map.entry(8, Moves.THUNDERSHOCK),
					Map.entry(22, Moves.QUICKATTACK),
					Map.entry(43, Moves.CRUNCH),
					Map.entry(50, Moves.THUNDERFANG),
					Map.entry(64, Moves.EXTRASENSORY),
					Map.entry(71, Moves.THUNDER),
					Map.entry(78, Moves.CALMMIND)
			)),
	ENTEI ("Entei", 244, Type.FIRE, 115, 115, 85, 90, 75, 100, -1, 217, 5, 3, 3,
			Map.ofEntries(
					Map.entry(8, Moves.EMBER),
					Map.entry(36, Moves.FLAMETHROWER),
					Map.entry(50, Moves.FIREFANG),
					Map.entry(64, Moves.EXTRASENSORY),
					Map.entry(78, Moves.CALMMIND)
			)),
	SUICUNE ("Suicune", 245, Type.WATER, 100, 75, 115, 90, 115, 85, -1, 215, 5, 3, 3,
			Map.ofEntries(
					Map.entry(29, Moves.AURORABEAM),
					Map.entry(50, Moves.ICEFANG),
					Map.entry(64, Moves.EXTRASENSORY),
					Map.entry(71, Moves.HYDROPUMP),
					Map.entry(78, Moves.CALMMIND)
			)),
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
	SPHEAL("Spheal", 363, Arrays.asList(Type.ICE, Type.WATER), 70, 40, 50, 55, 50, 25, 32, 75, 3, 1, 255,
			Map.ofEntries(
					Map.entry(19, Moves.BODYSLAM),
					Map.entry(25, Moves.AURORABEAM),
					Map.entry(43, Moves.BLIZZARD),
					Map.entry(49, Moves.SHEERCOLD)
			)),
	SEALEO("Sealeo", 364, Arrays.asList(Type.ICE, Type.WATER), 90, 60, 70, 75, 70, 45, 44, 128, 3, 2, 120, 
			Map.ofEntries(
					Map.entry(19, Moves.BODYSLAM),
					Map.entry(25, Moves.AURORABEAM),
					Map.entry(47, Moves.BLIZZARD),
					Map.entry(55, Moves.SHEERCOLD)
			)),
	WALREIN("Walrein", 365, Arrays.asList(Type.ICE, Type.WATER), 110, 80, 90, 95, 90, 65, -1, 192, 3, 3, 45, 
			Map.ofEntries(
					Map.entry(50, Moves.BLIZZARD),
					Map.entry(61, Moves.SHEERCOLD)
			)),
	KYOGRE ("Kyogre", 382, Type.WATER, 100, 100, 90, 150, 140, 90, -1, 218, 5, 3, 3,
			Map.ofEntries(
					Map.entry(60, Moves.SHEERCOLD),
					Map.entry(65, Moves.DOUBLEEDGE)
			)), 
	GROUDON ("Groudon", 383, Type.GROUND, 100, 150, 140, 100, 90, 90, -1, 218, 5, 3, 3,
			Map.ofEntries(
					Map.entry(65, Moves.SOLARBEAM)
			)),
	RAYQUAZA ("Rayquaza", 384, Arrays.asList(Type.DRAGON, Type.FLYING), 105, 150, 90, 150, 90, 95, -1, 220, 5, 3, 3,
			Map.ofEntries(
					Map.entry(60, Moves.EXTREMESPEED)
			));
	/** END INITIALIZE ENUMS **/
				
	/** INITIALIZE VALUES**/
	private final BufferedImage frontSprite, backSprite, image1;
	private final String name;
	private final int index;
	private final Type type;
	private final List<Type> types;
	private int hp, speed, attack, defense, spAttack, spDefense, accuracy, evLevel, ey, growth, ev, catchRate;	
	private Map<Integer, Moves> moveLevels;
	/** END INITIALIZE VALUES **/
		
	/** CONSTRUCTORS **/
	
	Pokedex(String name, int index, Type type, int hp, int attack, int defense, 
			int spAttack, int spDefense, int speed, int evLevel, int ey, int growth, int ev, int catchRate, 
			Map<Integer, Moves> moveLevels) {	
		
		this.frontSprite = setup("/pokedexfront/" + name, 48 * 5, 48 * 5); 
		this.backSprite = setup("/pokedexback/" + name, 48 * 5, 48 * 5);
		this.image1 = setup("/pokedexmenu/" + name, 48 * 2, 48 * 2);
		
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
			int spAttack, int spDefense, int speed, int evLevel, int ey, int growth, int ev, int catchRate, 
			Map<Integer, Moves> moveLevels) {			
		
		this.frontSprite = setup("/pokedexfront/" + name, 48 * 5, 48 * 5); 
		this.backSprite = setup("/pokedexback/" + name, 48 * 5, 48 * 5);
		this.image1 = setup("/pokedexmenu/" + name, 48 * 2, 48 * 2);

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
		
		moveMap.put(BULBASAUR, Arrays.asList(
				new Move(Moves.TACKLE), 
				new Move(Moves.GROWL)
		));
        moveMap.put(IVYSAUR, Arrays.asList(
        		new Move(Moves.TACKLE), 
        		new Move(Moves.GROWL)
        ));
		moveMap.put(VENUSAUR, Arrays.asList(
				new Move(Moves.VINEWHIP), 
				new Move(Moves.TACKLE), 
				new Move(Moves.GROWL)
		));			
        moveMap.put(CHARMANDER, Arrays.asList(
        		new Move(Moves.SCRATCH), 
        		new Move(Moves.GROWL)
        ));
		moveMap.put(CHARMELEON, Arrays.asList(
				new Move(Moves.EMBER), 
				new Move(Moves.SCRATCH), 
				new Move(Moves.GROWL)
		));
        moveMap.put(CHARIZARD, Arrays.asList(
        		new Move(Moves.DRAGONCLAW), 
        		new Move(Moves.EMBER), 
        		new Move(Moves.SCRATCH),
        		new Move(Moves.GROWL)
        ));         
		moveMap.put(SQUIRTLE, Arrays.asList(
				new Move(Moves.TACKLE)
		));
        moveMap.put(WARTORTLE, Arrays.asList(
        		new Move(Moves.BUBBLE), 
        		new Move(Moves.TACKLE), 
        		new Move(Moves.TAILWHIP)
        ));
        moveMap.put(BLASTOISE, Arrays.asList(
        		new Move(Moves.FLASHCANNON), 
        		new Move(Moves.BUBBLE), 
        		new Move(Moves.TACKLE), 
        		new Move(Moves.TAILWHIP)
        ));         
        moveMap.put(PIKACHU, Arrays.asList(
        		new Move(Moves.THUNDERSHOCK),
        		new Move(Moves.GROWL)
        ));
        moveMap.put(RAICHU, Arrays.asList(
        		new Move(Moves.THUNDERBOLT), 
        		new Move(Moves.THUNDERSHOCK), 
        		new Move(Moves.QUICKATTACK), 
        		new Move(Moves.TAILWHIP)
        ));          
        moveMap.put(ABRA, Arrays.asList(
        		new Move(Moves.TELEPORT)
        ));
        moveMap.put(KADABRA, Arrays.asList(
        		new Move(Moves.CONFUSION),
        		new Move(Moves.KINESIS), 
        		new Move(Moves.TELEPORT)
        ));
        moveMap.put(ALAKAZAM, Arrays.asList(
        		new Move(Moves.CONFUSION), 
        		new Move(Moves.KINESIS), 
        		new Move(Moves.TELEPORT)
        ));         
        moveMap.put(MACHOP, Arrays.asList(
        		new Move(Moves.LOWKICK), 
        		new Move(Moves.LEER)
        )); 
        moveMap.put(MACHOKE, Arrays.asList(
        		new Move(Moves.LOWKICK), 
        		new Move(Moves.LEER)
        )); 
        moveMap.put(MACHAMP, Arrays.asList(
        		new Move(Moves.LOWKICK), 
        		new Move(Moves.LEER)
        ));            
        moveMap.put(GEODUDE, Arrays.asList(
        		new Move(Moves.TACKLE), 
        		new Move(Moves.DEFENSECURL)
        ));
        moveMap.put(GRAVELER, Arrays.asList(
        		new Move(Moves.TACKLE), 
        		new Move(Moves.DEFENSECURL)
        ));
        moveMap.put(GOLEM, Arrays.asList(
        		new Move(Moves.TACKLE), 
        		new Move(Moves.DEFENSECURL)
        ));          
        moveMap.put(GASTLY, Arrays.asList(
        		new Move(Moves.LICK), 
        		new Move(Moves.HYPNOSIS)
        ));   
        moveMap.put(HAUNTER, Arrays.asList(
        		new Move(Moves.LICK), 
        		new Move(Moves.HYPNOSIS)
        ));     
        moveMap.put(GENGAR, Arrays.asList(
        		new Move(Moves.LICK),
        		new Move(Moves.HYPNOSIS)
        ));           
        moveMap.put(HORSEA, Arrays.asList(
        		new Move(Moves.BUBBLE)
        )); 
        moveMap.put(SEADRA, Arrays.asList(
        		new Move(Moves.WATERGUN), 
        		new Move(Moves.BUBBLE), 
        		new Move(Moves.LEER)
        ));       
        moveMap.put(KINGDRA, Arrays.asList(
        		new Move(Moves.WATERGUN), 
        		new Move(Moves.BUBBLE), 
        		new Move(Moves.YAWN),
        		new Move(Moves.LEER)
        ));            
        moveMap.put(LAPRAS, Arrays.asList(
        		new Move(Moves.WATERGUN), 
        		new Move(Moves.GROWL)
        ));           
        moveMap.put(SNORLAX, Arrays.asList(
        		new Move(Moves.TACKLE)
        ));         
        moveMap.put(RAIKOU, Arrays.asList(
        		new Move(Moves.LEER)
        ));
        moveMap.put(ENTEI, Arrays.asList(
        		new Move(Moves.LEER)
        ));
        moveMap.put(SUICUNE, Arrays.asList(
        		new Move(Moves.LEER)
        ));        
        moveMap.put(TREECKO, Arrays.asList(
        		new Move(Moves.POUND), 
        		new Move(Moves.LEER)
        ));      
        moveMap.put(GROVYLE, Arrays.asList(
        		new Move(Moves.POUND),
        		new Move(Moves.ABSORB), 
        		new Move(Moves.QUICKATTACK),
        		new Move(Moves.LEER)
        )); 
        moveMap.put(SCEPTILE, Arrays.asList(
        		new Move(Moves.POUND),
        		new Move(Moves.ABSORB), 
        		new Move(Moves.QUICKATTACK),
        		new Move(Moves.LEER)
        ));        
        moveMap.put(TORCHIC, Arrays.asList(
        		new Move(Moves.SCRATCH), 
        		new Move(Moves.GROWL)
        )); 
        moveMap.put(COMBUSKEN, Arrays.asList(
        		new Move(Moves.EMBER), 
        		new Move(Moves.SCRATCH), 
        		new Move(Moves.GROWL)
        )); 
        moveMap.put(BLAZIKEN, Arrays.asList(
        		new Move(Moves.FIREPUNCH), 
        		new Move(Moves.EMBER), 
        		new Move(Moves.SCRATCH),
        		new Move(Moves.GROWL)
        ));
        moveMap.put(MUDKIP, Arrays.asList(
        		new Move(Moves.TACKLE), 
        		new Move(Moves.GROWL)
        )); 
        moveMap.put(MARSHTOMP, Arrays.asList(
        		new Move(Moves.WATERGUN),
        		new Move(Moves.TACKLE),
        		new Move(Moves.GROWL)
        ));
        moveMap.put(SWAMPERT, Arrays.asList(
        		new Move(Moves.WATERGUN),
        		new Move(Moves.TACKLE),
        		new Move(Moves.GROWL)
        ));        
        moveMap.put(SPHEAL, Arrays.asList(
        		new Move(Moves.WATERGUN), 
        		new Move(Moves.DEFENSECURL),
        		new Move(Moves.GROWL)
        )); 
        moveMap.put(SEALEO, Arrays.asList(
        		new Move(Moves.WATERGUN), 
        		new Move(Moves.DEFENSECURL),
        		new Move(Moves.GROWL)
        )); 
        moveMap.put(WALREIN, Arrays.asList(
        		new Move(Moves.CRUNCH),
        		new Move(Moves.WATERGUN), 
        		new Move(Moves.DEFENSECURL),
        		new Move(Moves.GROWL)
        ));     
        moveMap.put(KYOGRE, Arrays.asList(
        		new Move(Moves.WATERPULSE)
        ));
        moveMap.put(GROUDON, Arrays.asList(
        		new Move(Moves.MUDSHOT)
        ));
        moveMap.put(RAYQUAZA, Arrays.asList(
        		new Move(Moves.TWISTER)
        )); 
	}
	/** END POKEMON MOVES STATIC MAP **/
	
	// pokemon can't evolve if evLevel is -1
	protected boolean canEvolve() { return this.getEvLevel() != -1; }
			
	/** GETTERS **/	
	protected BufferedImage getFrontSprite() { return frontSprite; }
	protected BufferedImage getBackSprite() { return backSprite; }
	protected BufferedImage getMenuSprite() { return image1; }
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