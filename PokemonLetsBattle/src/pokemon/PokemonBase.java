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
public enum PokemonBase {
	
	/*** STAT REFERECE https://www.serebii.net/pokemon/ ***/
	/*** EXP & EV REFERENCE https://bulbapedia.bulbagarden.net/wiki/List_of_Pok%C3%A9mon_by_effort_value_yield_in_Generation_IV ***/
	/*** XP GROWTH REFERENCE https://bulbapedia.bulbagarden.net/wiki/List_of_Pok%C3%A9mon_by_experience_type ***/
	
	BULBASAUR ("Bulbasaur", 1, Type.GRASS, 45, 49, 49, 65, 65, 45, 16, 64, 3, 1),
	IVYSAUR ("Ivysaur", 2, Arrays.asList(Type.GRASS, Type.POISON), 60, 62, 63, 80, 80, 60, 32, 141, 3, 2),
	VENUSAUR ("Venusaur", 3, Arrays.asList(Type.GRASS, Type.POISON), 80, 82, 83, 100, 100, 80, -1, 208, 3, 3),
	CHARMANDER ("Charmander", 4, Type.FIRE, 39, 52, 43, 60, 50, 65, 16, 65, 3, 1),
	CHARMELEON ("Charmeleon", 5, Type.FIRE, 58, 64, 58, 80, 65, 80, 36, 142, 3, 2),
	CHARIZARD ("Charizard", 6, Arrays.asList(Type.FIRE, Type.FLYING), 78, 84, 78, 109, 85, 100, -1, 209, 3, 3),
	SQUIRTLE ("Squirtle", 7, Type.WATER, 44, 48, 65, 50, 64, 43, 16, 66, 3, 1),
	WARTORTLE ("Wartortle", 8, Type.WATER, 59, 63, 80, 65, 80, 58, 36, 143, 3, 2),
	BLASTOISE ("Blastoise", 9, Type.WATER, 79, 83, 100, 85, 105, 78, -1, 210, 3, 3),	
	PIKACHU ("Pikachu", 25, Type.ELECTRIC, 55, 55, 40, 50, 50, 90, 30, 82, 0, 2),
	RAICHU ("Raichu", 26, Type.ELECTRIC, 60, 90, 55, 90, 80, 110, -1, 122, 0, 3),
	ABRA ("Abra", 63, Type.PSYCHIC, 25, 20, 15, 105, 55, 90, 16, 75, 3, 1),
	KADABRA ("Kadabra", 64, Type.PSYCHIC, 40, 35, 30, 120, 70, 105, 36, 145, 3, 2),
	ALAKAZAM ("Alakazam", 65, Type.PSYCHIC, 55, 50, 45, 135, 95, 120, -1, 186, 3, 3),
	MACHOP ("Machop", 66, Type.FIGHTING, 70, 80, 50, 35, 35, 35, 28, 75, 3, 1),
	MACHOKE ("Machoke", 67, Type.FIGHTING, 80, 100, 70, 50, 60, 45, 40, 146, 3, 2),
	MACHAMP ("Machamp", 68, Type.FIGHTING, 90, 130, 80, 65, 85, 55, -1, 193, 3, 3),
	GEODUDE ("Geodude", 74, Arrays.asList(Type.ROCK, Type.GROUND), 40, 80, 100, 30, 30, 20, 25, 73, 3, 1),
	GRAVELER ("Graveler", 75, Arrays.asList(Type.ROCK, Type.GROUND), 55, 95, 115, 45, 45, 35, 40, 134, 3, 2),
	GOLEM ("Golem", 76, Arrays.asList(Type.ROCK, Type.GROUND), 80, 120, 130, 55, 65, 45, -1, 177, 3, 3),
	GASTLY ("Gastly", 92, Arrays.asList(Type.GHOST, Type.POISON), 30, 35, 30, 100, 35, 80, 25, 95, 3, 1),
	HAUNTER ("Haunter", 93, Arrays.asList(Type.GHOST, Type.POISON), 45, 50, 45, 115, 55, 96, 40, 126, 3, 2),
	GENGAR ("Gengar", 94, Arrays.asList(Type.GHOST, Type.POISON), 60, 65, 60, 130, 75, 110, -1, 190, 3, 3),
	HORSEA ("Horsea", 116, Type.WATER, 30, 40, 70, 70, 25, 60, 32, 83, 0, 1),
	SEADRA ("Seadra", 117, Type.WATER, 55, 65, 95, 95, 45, 85, 45, 155, 0, 2),
	LAPRAS ("Lapras", 131, Arrays.asList(Type.WATER, Type.ICE), 130, 85, 80, 85, 95, 60, -1, 219, 5, 2),
	SNORLAX ("Snorlax", 143, Type.NORMAL, 160, 110, 65, 65, 110, 30, -1, 154, 5, 2),
	KINGDRA ("Kingdra", 230, Arrays.asList(Type.WATER, Type.DRAGON), 75, 95, 95, 95, 95, 85, -1, 207, 0, 3),
	RAIKOU ("Raikou", 243, Type.ELECTRIC, 90, 85, 75, 115, 100, 115, -1, 216, 5, 3),
	ENTEI ("Entei", 244, Type.FIRE, 115, 115, 85, 90, 75, 100, -1, 217, 5, 3),
	SUICUNE ("Suicune", 245, Type.WATER, 100, 75, 115, 90, 115, 85, -1, 215, 5, 3),
	TREECKO ("Treecko", 252, Type.GRASS, 40, 45, 35, 65, 55, 70, 16, 65, 3, 1),
	GROVYLE ("Grovyle", 253, Type.GRASS, 50, 65, 45, 85, 65, 95, 36, 141, 3, 2),
	SCEPTILE ("Sceptile", 254, Type.GRASS, 70, 85, 65, 105, 85, 120, -1, 208, 3, 3),
	TORCHIC ("Torchic", 255, Type.FIRE, 45, 60, 40, 70, 50, 45, 16, 65, 3, 1),
	COMBUSKEN ("Combusken", 256, Arrays.asList(Type.FIRE, Type.FIGHTING), 60, 85, 60, 85, 60, 55, 36, 142, 3, 2),
	BLAZIKEN ("Blaziken", 257, Arrays.asList(Type.FIRE, Type.FIGHTING), 80, 120, 70, 110, 70, 80, -1, 209, 3, 3),
	MUDKIP ("Mudkip", 258, Type.WATER, 50, 70, 50, 50, 50, 40, 16, 65, 3, 1),
	MARSHTOMP ("Marshtomp", 259, Arrays.asList(Type.WATER, Type.GROUND), 70, 85, 70, 60, 70, 50, 36, 143, 3, 2),
	SWAMPERT ("Swampert", 260, Arrays.asList(Type.WATER, Type.GROUND), 100, 110, 90, 85, 90, 60, -1, 210, 3, 3),
	KYOGRE ("Kyogre", 382, Type.WATER, 100, 100, 90, 150, 140, 90, -1, 218, 5, 3), 
	GROUDON ("Groudon", 383, Type.GROUND, 100, 150, 140, 100, 90, 90, -1, 218, 5, 3),
	RAYQUAZA ("Rayquaza", 384, Arrays.asList(Type.DRAGON, Type.FLYING), 105, 150, 90, 150, 90, 95, -1, 220, 5, 3);
	/** END INITIALIZE ENUMS **/
		
	/** INITIALIZE VALUES**/
	private final BufferedImage frontSprite, backSprite, menuSprite;
	private final String name;
	private final int index;
	private final Type type;
	private final List<Type> types;
	private int hp, speed, attack, defense, spAttack, spDefense, accuracy, evLevel, ey, growth, ev;	
	/** END INITIALIZE VALUES **/
		
	// initialize list to hold all enums in pokemon class
	private static List<PokemonBase> POKEDEX = Arrays.asList(PokemonBase.values());

	/** CONSTRUCTORS **/
	PokemonBase(String name, int index, Type type, int hp, int attack, int defense, 
			int spAttack, int spDefense, int speed, int evLevel, int ey, int growth, int ev) {	
		
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
		
		this.types = null;	
	}
	PokemonBase(String name, int index, List<Type> types, int hp, int attack, int defense, 
			int spAttack, int spDefense, int speed, int evLevel, int ey, int growth, int ev) {			
		
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
		
		this.type = null;		
	}
	/** END CONSTRUCTORS **/
	

	/** POKEMON MOVES STATIC MAP **/
	private static final Map<PokemonBase, List<Move>> moveMap;
	static {
		moveMap = new HashMap<>();
		getMovemap().put(BULBASAUR, Arrays.asList(new Move(Moves.VINEWHIP), new Move(Moves.TACKLE), new Move(Moves.GROWL)));
        getMovemap().put(IVYSAUR, Arrays.asList(new Move(Moves.RAZORLEAF), new Move(Moves.VINEWHIP), new Move(Moves.POISONPOWDER),
        		new Move(Moves.TACKLE)));
		getMovemap().put(VENUSAUR, Arrays.asList(new Move(Moves.PETALBLIZZARD), new Move(Moves.SOLARBEAM, 2), new Move(Moves.TAKEDOWN), 
				new Move(Moves.DOUBLEEDGE)));
		
        getMovemap().put(CHARMANDER, Arrays.asList(new Move(Moves.EMBER), new Move(Moves.SCRATCH), new Move(Moves.GROWL)));
		getMovemap().put(CHARMELEON, Arrays.asList(new Move(Moves.FIREFANG), new Move(Moves.EMBER), new Move(Moves.SLASH), 
				new Move(Moves.GROWL)));
        getMovemap().put(CHARIZARD, Arrays.asList(new Move(Moves.FLAMETHROWER), new Move(Moves.FLAREBLITZ),new Move(Moves.DRAGONBREATH),
        	new Move(Moves.FLY, 2)));
 
		getMovemap().put(SQUIRTLE, Arrays.asList(new Move(Moves.WATERGUN), new Move(Moves.TACKLE), new Move(Moves.TAILWHIP)));
        getMovemap().put(WARTORTLE, Arrays.asList(new Move(Moves.WATERPULSE), new Move(Moves.WATERGUN), new Move(Moves.TAILWHIP)));
        getMovemap().put(BLASTOISE, Arrays.asList(new Move(Moves.WATERPULSE), new Move(Moves.AQUATAIL), new Move(Moves.HYDROPUMP), 
        		new Move(Moves.FLASHCANNON)));
        
        getMovemap().put(PIKACHU, Arrays.asList(new Move(Moves.THUNDERWAVE), new Move(Moves.THUNDERSHOCK),new Move(Moves.TACKLE),
        		new Move(Moves.PLAYNICE)));
        getMovemap().put(RAICHU, Arrays.asList(new Move(Moves.THUNDERPUNCH), new Move(Moves.THUNDERBOLT), new Move(Moves.QUICKATTACK), 
        		new Move(Moves.SLAM)));    
      
        getMovemap().put(ABRA, Arrays.asList(new Move(Moves.TELEPORT)));
        getMovemap().put(KADABRA, Arrays.asList(new Move(Moves.CONFUSION), new Move(Moves.PSYBEAM), new Move(Moves.KINESIS)));
        getMovemap().put(ALAKAZAM, Arrays.asList(new Move(Moves.PSYCHIC), new Move(Moves.CONFUSION), new Move(Moves.PSYCHOCUT), 
        		new Move(Moves.CALMMIND)));     
        
        getMovemap().put(MACHOP, Arrays.asList(new Move(Moves.LOWKICK), new Move(Moves.LOWSWEEP), new Move(Moves.KNOCKOFF))); 
        getMovemap().put(MACHOKE, Arrays.asList(new Move(Moves.LOWKICK), new Move(Moves.LOWSWEEP), new Move(Moves.VITALTHROW),
        		new Move(Moves.SEISMICTOSS))); 
        getMovemap().put(MACHAMP, Arrays.asList(new Move(Moves.SEISMICTOSS), new Move(Moves.DYNAMICPUNCH), new Move(Moves.CROSSCHOP), 
        		new Move(Moves.SCARYFACE))); 
        
        getMovemap().put(GEODUDE, Arrays.asList(new Move(Moves.ROCKTHROW), new Move(Moves.TACKLE), new Move(Moves.DEFENSECURL)));
        getMovemap().put(GRAVELER, Arrays.asList(new Move(Moves.ROCKTHROW), new Move(Moves.ROLLOUT), new Move(Moves.TACKLE),
        		new Move(Moves.DEFENSECURL)));
        getMovemap().put(GOLEM, Arrays.asList(new Move(Moves.EARTHQUAKE), new Move(Moves.DIG, 2), new Move(Moves.HEAVYSLAM), 
        		new Move(Moves.DOUBLEEDGE)));
        
        getMovemap().put(GASTLY, Arrays.asList(new Move(Moves.LICK), new Move(Moves.PAYBACK), new Move(Moves.HYPNOSIS)));   
        getMovemap().put(HAUNTER, Arrays.asList(new Move(Moves.PAYBACK), new Move(Moves.HEX), new Move(Moves.DARKPULSE), 
        		new Move(Moves.CONFUSERAY)));   
        getMovemap().put(GENGAR, Arrays.asList(new Move(Moves.SHADOWBALL), new Move(Moves.SHADOWPUNCH),new Move(Moves.HEX), 
        		new Move(Moves.DARKPULSE))); 
        
        getMovemap().put(HORSEA, Arrays.asList(new Move(Moves.WATERGUN), new Move(Moves.BUBBLE), new Move(Moves.LEER))); 
        getMovemap().put(SEADRA, Arrays.asList(new Move(Moves.WATERGUN), new Move(Moves.TWISTER), new Move(Moves.HYDROPUMP),
        		new Move(Moves.AGILITY)));       
        getMovemap().put(KINGDRA, Arrays.asList(new Move(Moves.SURF), new Move(Moves.HYDROPUMP), new Move(Moves.DRAGONPULSE),
        		new Move(Moves.AGILITY))); 
     
        getMovemap().put(LAPRAS, Arrays.asList(new Move(Moves.ICEBEAM), new Move(Moves.HYDROPUMP), new Move(Moves.SHEERCOLD), 
        		new Move(Moves.CONFUSERAY)));     
        
        getMovemap().put(SNORLAX, Arrays.asList(new Move(Moves.BODYSLAM), new Move(Moves.ROLLOUT), new Move(Moves.CRUNCH), 
        		new Move(Moves.YAWN)));     
        
        getMovemap().put(RAIKOU, Arrays.asList(new Move(Moves.THUNDERFANG), new Move(Moves.THUNDER), new Move(Moves.CRUNCH),
        		new Move(Moves.CALMMIND)));
        getMovemap().put(ENTEI, Arrays.asList(new Move(Moves.FIREFANG), new Move(Moves.FLAMETHROWER), new Move(Moves.EXTRASENSORY),
        		new Move(Moves.CALMMIND)));
        getMovemap().put(SUICUNE, Arrays.asList(new Move(Moves.ICEFANG), new Move(Moves.AURORABEAM), new Move(Moves.HYDROPUMP),
        		new Move(Moves.CALMMIND)));
        
        getMovemap().put(TREECKO, Arrays.asList(new Move(Moves.ABSORB), new Move(Moves.QUICKATTACK), new Move(Moves.LEER))); 
        getMovemap().put(GROVYLE, Arrays.asList(new Move(Moves.LEAFBLADE), new Move(Moves.ABSORB), new Move(Moves.CUT),
        		new Move(Moves.AGILITY))); 
        getMovemap().put(SCEPTILE, Arrays.asList(new Move(Moves.LEAFBLADE), new Move(Moves.LEAFSTORM), new Move(Moves.GIGADRAIN),
        		new Move(Moves.AGILITY))); 
       
        getMovemap().put(TORCHIC, Arrays.asList(new Move(Moves.EMBER), new Move(Moves.SCRATCH), new Move(Moves.GROWL))); 
        getMovemap().put(COMBUSKEN, Arrays.asList(new Move(Moves.EMBER), new Move(Moves.DOUBLEKICK), new Move(Moves.SLASH), 
        		new Move(Moves.GROWL))); 
        getMovemap().put(BLAZIKEN, Arrays.asList(new Move(Moves.FIREPUNCH), new Move(Moves.BLAZEKICK), new Move(Moves.FLAREBLITZ),
        		new Move(Moves.SKYUPPERCUT))); 

        getMovemap().put(MUDKIP, Arrays.asList(new Move(Moves.WATERGUN), new Move(Moves.TACKLE), new Move(Moves.GROWL))); 
        getMovemap().put(MARSHTOMP, Arrays.asList(new Move(Moves.WATERGUN), new Move(Moves.MUDSHOT), new Move(Moves.MUDSLAP), 
        		new Move(Moves.TACKLE))); 
        getMovemap().put(SWAMPERT, Arrays.asList(new Move(Moves.SURF), new Move(Moves.MUDDYWATER), new Move(Moves.MUDBOMB),
        		new Move(Moves.EARTHQUAKE)));
     
        getMovemap().put(KYOGRE, Arrays.asList(new Move(Moves.SURF), new Move(Moves.HYDROPUMP), new Move(Moves.THUNDER),
        		new Move(Moves.CALMMIND)));
        getMovemap().put(GROUDON, Arrays.asList(new Move(Moves.EARTHQUAKE), new Move(Moves.FIREFANG), new Move(Moves.DRAGONCLAW), 
        		new Move(Moves.SOLARBEAM, 2)));
        getMovemap().put(RAYQUAZA, Arrays.asList(new Move(Moves.DRAGONCLAW), new Move(Moves.EXTREMESPEED), new Move(Moves.BLIZZARD), 
        		new Move(Moves.FLY, 2))); 
	}
	/** END POKEMON MOVES STATIC MAP **/
	
	// pokemon can't evolve if evLevel is -1
	public boolean canEvolve() { return this.getEvLevel() != -1; }
			
	/** GETTERS **/	
	public BufferedImage getFrontSprite() { return frontSprite; }
	public BufferedImage getBackSprite() { return backSprite; }
	public BufferedImage getMenuSprite() { return menuSprite; }
	public String getName() { return name; }
	public int getIndex() {	return index; }	
	public Type getType() { return type; }
	public List<Type> getTypes() { return types; }		
	public int getHP() { return hp; }
	public int getSpeed() { return speed; }
	public int getAttack() { return attack; }
	public int getDefense() { return defense; }
	public int getSpAttack() { return spAttack; }
	public int getSpDefense() {	return spDefense; }
	public int getAccuracy() { return accuracy; }
	public int getEXPYeild() { return ey; }
	public int getGrowth() { return growth; }
	public int getEvLevel() { return evLevel; }
	public int getEV() { return ev; }		
	
	public static List<PokemonBase> getPOKEDEX() { return POKEDEX; }
	public static Map<PokemonBase, List<Move>> getMovemap() { return moveMap; }	
	/** END GETTERS **/
	
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