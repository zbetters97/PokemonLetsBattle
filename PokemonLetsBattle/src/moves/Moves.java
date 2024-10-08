package moves;

import java.util.Arrays;
import java.util.List;

import moves.Move.MoveType;
import properties.*;

/*** MOVES ENUM ***/
public enum Moves {
	// ATTACK DESCRIPTION REFERENCE (GEN IV): https://www.serebii.net/attackdex-dp/
	
	CONFUSE("Confuse", MoveType.PHYSICAL, Type.NORMAL, 1, 40, -1, ""),
	ABSORB ("Absorb", MoveType.SPECIAL, Type.GRASS, 20, 20, 100, 
			"A nutrient-draining attack.\nThe user's HP is restored\nby half the damage taken\nby the target."),
	AGILITY ("Agility", MoveType.ATTRIBUTE, Type.PSYCHIC, true, 30, -1, 1, Arrays.asList("speed"), 
			"The user relaxes and\nlightens its body to move\nfaster. It sharply boosts\nthe speed stat."),
	AQUATAIL ("Aqua Tail", MoveType.PHYSICAL, Type.WATER, 10, 135, 90, 
			"The user attacks by swinging its tail as if it were a vicious wave in a raging storm."),
	AURORABEAM ("Aurora Beam", MoveType.SPECIAL, Type.ICE, 20, 65, 100, 
			"The foe is hit with a\nrainbow-colored beam."),
	BLAZEKICK ("Blaze Kick", MoveType.PHYSICAL, Type.FIRE, Status.BURN, 0.10, 10, 85, 90, 1, 
			"The user launches a kick\nwith a high critical-hit\nratio. It may also leave\nthe target with a burn."),
	BLIZZARD ("Blizzard", MoveType.SPECIAL, Type.ICE, Status.FREEZE, 0.10, 5, 120, 70, 
			"A howling blizzard is summoned to strike the foe. It may also freeze the target solid."),
	BODYSLAM ("Body Slam", MoveType.PHYSICAL, Type.NORMAL, Status.PARALYZE, 0.10, 15, 85, 100, 
			"The user drops onto the foe with its full body weight. It may leave the foe paralyzed."),
	BUBBLE ("Bubble", MoveType.SPECIAL, Type.WATER, 30, 20, 100, 
			"A spray of countless bubbles is jetted at the foe."),
	CALMMIND ("Calm Mind", MoveType.ATTRIBUTE, Type.PSYCHIC, true, 20, -1, 1, Arrays.asList("sp. attack", "sp. defense"), 
			"The user quietly focuses\nits mind and calms its spirit\nto raise its Sp. Atk and\nSp. Def stats."),
	CONFUSERAY ("Confuse Ray", MoveType.STATUS, Type.GHOST, Status.CONFUSE, 10, 100, 
			"The foe is exposed to a sinister ray that triggers confusion."),
	CONFUSION ("Confusion", MoveType.SPECIAL, Type.PSYCHIC, 25, 75, 100, 
			"The foe is hit by a weak telekinetic force. It may also leave the foe confused."),
	CROSSCHOP ("Cross Chop", MoveType.PHYSICAL, Type.FIGHTING, 5, 150, 80, 1, 
			"The user delivers a double chop with its forearms crossed. It has a high critical-hit ratio."),
	CRUNCH ("Crunch", MoveType.PHYSICAL, Type.DARK, 15, 80, 100, 
			"The user crunches up the\nfoe with sharp fangs."),
	CUT ("Cut", MoveType.PHYSICAL, Type.NORMAL, 30, 50, 95, 
			"The foe is cut with a scythe\nor a claw. It can also be\nused to cut down thin\ntrees."),
	DARKPULSE ("Dark Pulse", MoveType.SPECIAL, Type.DARK, 0.20, 15, 80, 100, 
			"The user releases a horrible aura imbued with dark thoughts. It may also make the target flinch."),
	DEFENSECURL ("Defense Curl", MoveType.ATTRIBUTE, Type.NORMAL, true, 40, -1, 1, Arrays.asList("defense"), 
			"The user curls up to conceal weak spots and raise its Defense stat."),
	DIG ("Dig", MoveType.PHYSICAL, Type.GROUND, 10, 80, 100, 2, false, "dug\ninto the ground!", 
			"The user burrows, then attacks on the second turn. It can also be used to exit dungeons."),
	DOUBLEEDGE ("Double Edge", MoveType.PHYSICAL, Type.STEEL, 10, 80, 100, 0.25, 
			"A reckless, life- risking tackle. It also damages the user by a fairly large amount, however."),
	DOUBLEKICK ("Double Kick", MoveType.PHYSICAL, Type.FIGHTING, 35, 60, 100, 
			"The foe is quickly kicked\ntwice in succession using\nboth feet."),
	DRAGONBREATH ("Dragon Breath", MoveType.SPECIAL, Type.DRAGON, Status.PARALYZE, 0.10, 20, 60, 100, 
			"The user exhales a mighty\ngust that inflicts damage.\nIt may also paralyze the\ntarget."),
	DRAGONCLAW ("Dragon Claw", MoveType.PHYSICAL, Type.DRAGON, 15, 80, 100, 
			"The user slashes the foe with huge, sharp claws."),
	DRAGONPULSE ("Dragon Pulse", MoveType.SPECIAL, Type.DRAGON, 10, 90, 100, 
			"The foe is attacked with a shock wave generated by the user's gaping mouth."),
	DYNAMICPUNCH ("Dynamic Punch", MoveType.PHYSICAL, Type.FIGHTING, Status.CONFUSE, 1.0, 5, 150, 50, 
			"The foe is punched with the user's full, concentrated power. It confuses the foe if it hits."),
	EARTHQUAKE ("Earthquake", MoveType.PHYSICAL, Type.GROUND, 10, 150, 100, 
			"The user sets off an\nearthquake that hits all\nthe Pokémon in the battle."),
	EMBER ("Ember", MoveType.SPECIAL, Type.FIRE, Status.BURN, 0.10, 25, 60, 100, 
			"The foe is attacked with\nsmall flames.\nThe target may also be left\nwith a burn."),
	EXTRASENSORY ("Extrasensory", MoveType.SPECIAL, Type.PSYCHIC, 30, 80, 100, 
			"The user attacks with an\nodd, unseeable power."),
	EXTREMESPEED ("Extreme Speed", MoveType.PHYSICAL, Type.NORMAL, 5, 80, 100, 
			"The user charges the foe at blinding speed. This attack always goes before any other move."),
	FIREFANG ("Fire Fang", MoveType.PHYSICAL, Type.FIRE, Status.BURN, 0.10, 0.10, 15, 95, 95, 
			"The user bites with\nflame-cloaked fangs.\nIt may also make the foe\nflinch or sustain a burn."),
	FIREPUNCH ("Fire Punch", MoveType.PHYSICAL, Type.FIRE, Status.BURN, 0.10, 15, 75, 100, 
			"The foe is punched with a\nfiery fist.\nIt may leave the target\nwith a burn."),
	FLAMETHROWER ("Flamethrower", MoveType.SPECIAL, Type.FIRE, Status.BURN, 0.10, 15, 135, 100, 
			"The foe is scorched with\nan intense blast of fire.\nThe target may also be\nleft with a burn."),
	FLAREBLITZ ("Flare Blitz", MoveType.PHYSICAL, Type.FIRE, Status.BURN, 0.10, 15, 120, 100, 
			"The foe is scorched with\nan intense blast of fire.\nThe target may also be\nleft with a burn."),
	FLASHCANNON ("Flash Cannon", MoveType.SPECIAL, Type.STEEL, 10, 80, 100, 
			"The user gathers all its light energy and releases it at once."),
	FLY ("Fly", MoveType.PHYSICAL, Type.FLYING, 15, 90, 95, 2, false, "took\nflight!", 
			"The user soars, then\nstrikes on the second turn.\nIt can also be used for\nflying to any familiar town."),
	GIGADRAIN ("Giga Drain", MoveType.SPECIAL, Type.GRASS, 10, 60, 100, 
			"A nutrient-draining attack. The user's HP is restored by half the damage taken by the target."),
	GROWL ("Growl", MoveType.ATTRIBUTE, Type.NORMAL, false, 40, 100, -1, Arrays.asList("attack"), 
			"The user growls, making the\nfoe less wary.\nThe target's Attack stat\nis lowered."),
	HEAVYSLAM ("Heavy Slam", MoveType.PHYSICAL, Type.NORMAL, 20, 80, 75,
			"The user slams into the target with its heavy body."),
	HEX ("Hex", MoveType.SPECIAL, Type.GHOST, 10, 95, 100, 
			"This relentless attack does massive damage to a target affected by status conditions."),
	HYDROPUMP ("Hydro Pump", MoveType.SPECIAL, Type.WATER, 5, 165, 80, 
			"The foe is blasted by a huge\nvolume of water launched\nunder great pressure."),
	HYPNOSIS ("Hypnosis", MoveType.STATUS, Type.PSYCHIC, Status.SLEEP, 20, 60, 
			"The user employs hypnotic suggestion to make the target fall into a deep sleep."),
	ICEBEAM ("Ice Beam", MoveType.SPECIAL, Type.ICE, Status.FREEZE, 0.10, 10, 95, 100, 
			"The foe is struck with an icy-cold beam of energy. It may also freeze the target solid."),
	ICEFANG ("Ice Fang", MoveType.PHYSICAL, Type.ICE, Status.FREEZE, 0.10, 15, 65, 95, 
			"The user bites with\ncold-infused fangs.\nIt may also make the foe\nfreeze."),
	KINESIS ("Kinesis", MoveType.ATTRIBUTE, Type.PSYCHIC, false, 15, 80, -1, Arrays.asList("accuracy"), 
			"The user distracts the foe by bending a spoon. It may lower the target's accuracy."),
	KNOCKOFF ("Knock Off", MoveType.PHYSICAL, Type.DARK, 20, 65, 100, 
			"The user slaps down the target's held item, preventing that item from being used in the battle."),
	LEAFBLADE ("Leaf Blade", MoveType.PHYSICAL, Type.GRASS, 15, 90, 100, 1, 
			"The user handles a sharp\nleaf like a sword and\nattacks by cutting its\ntarget."),
	LEAFSTORM ("Leaf Storm", MoveType.SPECIAL, Type.GRASS, 5, 140, 90, 
			"A storm of sharp leaves is whipped up."),
	LEER ("Leer", MoveType.ATTRIBUTE, Type.NORMAL, false, 30, 100, -1, Arrays.asList("defense"), 
			"The foe is given an intimidating leer with sharp eyes. The target's Defense stat is reduced."),
	LICK ("Lick", MoveType.PHYSICAL, Type.GHOST, Status.PARALYZE, 0.10, 30, 45, 100, 
			"The foe is licked with a long tongue, causing damage. It may also paralyze the target."),
	LOWKICK ("Low Kick", MoveType.PHYSICAL, Type.FIGHTING, 20, 40, 100, 
			"A powerful low kick that makes the foe fall over. It inflicts greater damage on heavier foes."),
	LOWSWEEP ("Low Sweep", MoveType.PHYSICAL, Type.FIGHTING, 20, 95, 100, 
			"The user makes a swift attack on the target's legs."),
	MUDBOMB ("Mud Bomb", MoveType.SPECIAL, Type.GROUND, false, 0.30, 10, 65, 85, -1, Arrays.asList("accuracy"), 
			"The user launches a hard-\npacked mud ball to attack.\nIt may also lower the\ntarget's accuracy."),
	MUDSHOT ("Mud Shot", MoveType.SPECIAL, Type.GROUND, false, 1.0, 15, 55, 95, -1, Arrays.asList("speed"), 
			"The user attacks by hurling\na blob of mud at the foe.\nIt also reduces the\ntarget's Speed."),
	MUDSLAP ("Mud Slap", MoveType.SPECIAL, Type.GROUND, false, 1.0, 10, 20, 100, -1, Arrays.asList("accuracy"),
			"The user hurls mud in the\nfoe's face to inflict damage\nand lower its accuracy."),
	MUDDYWATER ("Muddy Water", MoveType.SPECIAL, Type.WATER, false, 0.30, 10, 95, 85, -1, Arrays.asList("accuracy"), 
			"The user attacks by\nshooting out muddy water.\nIt may also lower the foe's\naccuracy."),
	PAYBACK ("Payback", MoveType.PHYSICAL, Type.DARK, 10, 50, 100, 
			"If the user can use this attack after the foe attacks, its power is doubled."),
	PETALBLIZZARD ("Petal Blizzard", MoveType.PHYSICAL, Type.GRASS, 15, 135, 100, 
			"The user stirs up a violent petal blizzard and attacks everything around it."),
	PLAYNICE ("Play Nice", MoveType.ATTRIBUTE, Type.NORMAL, false, 20, -1, -1, Arrays.asList("attack"), 
			"The user and the target become friends, and the target loses its will to fight. This lowers the target's Attack stat."),
	POISONPOWDER ("Poison Powder", MoveType.STATUS, Type.POISON, Status.POISON, 45, 75, 
			"A cloud of poisonous dust is scattered on the foe. It may poison the target."),
	POUND ("Pound", MoveType.PHYSICAL, Type.NORMAL, 35, 40, 100, 
			"The foe is physically pounded with a long tail or a foreleg, etc."),
	PSYBEAM ("Psybeam", MoveType.SPECIAL, Type.PSYCHIC, Status.CONFUSE, 1.0, 20, 95, 100, 
			"The foe is attacked with a peculiar ray. It may also leave the target confused."),
	PSYCHIC ("Psychic", MoveType.SPECIAL, Type.PSYCHIC, 10, 135, 100, 
			"The foe is hit by a strong telekinetic force."),
	PSYCHOCUT ("Psycho Cut", MoveType.PHYSICAL, Type.PSYCHIC, 20, 105, 100, 1, 
			"The user tears at the foe with blades formed by psychic power. Critical hits land more easily."),
	QUICKATTACK ("Quick Attack", MoveType.PHYSICAL, Type.NORMAL, 30, 40, 100, true, 
			"The user lunges at the foe at a speed that makes it almost invisible. It is sure to strike first."),
	RAZORLEAF ("Razor Leaf", MoveType.PHYSICAL, Type.GRASS, 25, 80, 95, 1, 
			"Sharp-edged leaves are launched to slash at the foe. It has a high critical-hit ratio."),
	ROCKTHROW ("Rock Throw", MoveType.PHYSICAL, Type.ROCK, 15, 75, 90, 
			"The user picks up and throws a small ROCK at the foe to attack."),
	ROLLOUT ("Rollout", MoveType.PHYSICAL, Type.ROCK, 20, 45, 90, 
			"The user continually rolls into the foe over five turns."),
	SCARYFACE ("Scary Face", MoveType.ATTRIBUTE, Type.NORMAL, false, 10, 90, -2, Arrays.asList("speed"), 
			"The user frightens the foe with a scary face to sharply reduce its Speed stat."),
	SCRATCH ("Scratch", MoveType.PHYSICAL, Type.NORMAL, 35, 40, 100, 
			"Hard, pointed, and sharp\nclaws rake the foe to\ninflict damage."),
	SEISMICTOSS ("Seismic Toss", MoveType.PHYSICAL, Type.FIGHTING, 20, -1, 100, 
			"The foe is thrown using the power of gravity. It inflicts damage equal to the user's level."),
	SHADOWBALL ("Shadow Ball", MoveType.SPECIAL, Type.GHOST, 15, 120, 100, 
			"The user hurls a shadowy blob at the foe."),
	SHADOWPUNCH ("Shadow Punch", MoveType.PHYSICAL, Type.GHOST, 20, 90, -1, 
			"The user throws a punch at the foe from the shadows. The punch lands without fail."),
	SHEERCOLD ("Sheer Cold", MoveType.SPECIAL, Type.ICE, 5, 1000, 30, 
			"The foe is attacked with a blast of absolute-zero cold. The foe instantly faints if it hits."),
	SKYUPPERCUT ("Sky Uppercut", MoveType.PHYSICAL, Type.FIGHTING, 15, 120, 100, 
			"The user attacks the foe\nwith an uppercut thrown\nskyward with force."),
	SLAM ("Slam", MoveType.PHYSICAL, Type.NORMAL, 20, 80, 75, 
			"The foe is slammed with a long tail, vines, etc., to inflict damage."),
	SLASH ("Slash", MoveType.PHYSICAL, Type.NORMAL, 20, 70, 100, 1, 
			"The foe is attacked with a\nslash of claws, etc.\nIt has a high critical-hit\nratio."),
	SOLARBEAM ("Solar Beam", MoveType.SPECIAL, Type.GRASS, 10, 180, 100, 2, true, 
			"is\ncharging a light beam...", "A two-turn attack. The user gathers light, then blasts a bundled beam on the second turn."),
	STRUGGLE ("Struggle", MoveType.PHYSICAL, Type.NORMAL, 10, 50, 100, 0.25, "Struggles"),
	SURF ("Surf", MoveType.SPECIAL, Type.WATER, 15, 95, 100, 
			"It swamps the entire\nbattlefield with a giant\nwave. It can also be used\nfor crossing water."),
	TACKLE ("Tackle", MoveType.PHYSICAL, Type.NORMAL, 35, 40, 95, 
			"A physical attack in which\nthe user charges and slams\ninto the foe with its whole\nbody."),
	TAILWHIP ("Tail Whip", MoveType.ATTRIBUTE, Type.NORMAL, false, 30, 100, -1, Arrays.asList("defense"), 
			"The user wags its tail cutely, making the foe less wary. The target's Defense stat is lowered."),
	TAKEDOWN ("Take Down", MoveType.PHYSICAL, Type.NORMAL, 20, 90, 85, 0.25, 
			"A reckless, full-body charge attack for slamming into the foe. It also damages the user a little."),
	TELEPORT ("Teleport", MoveType.OTHER, Type.PSYCHIC, 20, 0, -1, 
			"Use it to flee from any wild Pokémon."),
	THUNDER ("Thunder", MoveType.SPECIAL, Type.ELECTRIC, Status.PARALYZE, 0.10, 10, 120, 70, 
			"A wicked thunderbolt is\ndropped on the foe to\ninflict damage. It may also\nleave the target paralyzed."),
	THUNDERBOLT ("Thunder Bolt", MoveType.SPECIAL, Type.ELECTRIC, Status.PARALYZE, 0.10, 15, 135, 100, 
			"A strong ELECTRIC blast is loosed at the foe. It may also leave the foe paralyzed."),
	THUNDERFANG ("Thunder Fang", MoveType.PHYSICAL, Type.ELECTRIC, Status.PARALYZE, 0.10, 15, 65, 95, 
			"The user bites with\nelectrified fangs.\nIt may also make the\nfoe paralyzed."),
	THUNDERPUNCH ("Thunder Punch", MoveType.PHYSICAL, Type.ELECTRIC, Status.PARALYZE, 0.10, 15, 110, 100, 
			"The foe is punched with an electrified fist. It may leave the target with paralysis."),
	THUNDERSHOCK ("Thunder Shock", MoveType.SPECIAL, Type.ELECTRIC, Status.PARALYZE, 0.10, 40, 60, 100, 
			"A jolt of electric is hurled at the foe to inflict damage. It may also leave the foe paralyzed."),
	THUNDERWAVE ("Thunder Wave", MoveType.STATUS, Type.ELECTRIC, Status.PARALYZE, 20, 90, 
			"A weak eletric charge is launched at the foe. It causes paralysis if it hits."),
	TWISTER ("Twister", MoveType.SPECIAL, Type.DRAGON, 20, 40, 100, 
			"The user whips up a vicious tornado to tear at the foe."),
	VINEWHIP ("Vine Whip", MoveType.PHYSICAL, Type.GRASS, 25, 65, 100, 
			"The foe is struck with slender, whiplike vines to inflict damage."),
	VITALTHROW ("Vital Throw", MoveType.PHYSICAL, Type.FIGHTING, 10, 105, -1, 
			"The user allows the foe to attack first. In return, this throw move is guaranteed not to miss."),
	WATERGUN ("Water Gun", MoveType.SPECIAL, Type.WATER, 25, 60, 100, 
			"The foe is blasted with a\nforceful shot of water."),
	WATERPULSE ("Water Pulse", MoveType.SPECIAL, Type.WATER, 20, 90, 100, 
			"The user attacks the foe with a pulsing blast of WATER. It may also confuse the foe."),
	XSCISSOR ("X-scissor", MoveType.PHYSICAL, Type.BUG, 15, 80, 100, 
			"The user slashes at the foe by crossing its scythes or claws as if they were a pair of scissors."),
	YAWN ("Yawn", MoveType.STATUS, Type.NORMAL, Status.SLEEP, 10, 100, 
			"The user lets loose a huge yawn that lulls the foe into falling asleep.");
	/** END INITIALIZE ENUMS **/
	
	/** INITIALIZE VALUES **/
	private String name, delay, info;
	private MoveType mType;
	private Type type;
	private Status effect;
	private int pp, power, accuracy, level, crit, numTurns;
	private boolean goFirst, toSelf, isProtected;	
	private double probability, damageToSelf, flinch;		
	private List<String> stats;
	/** END INITIALIZE VALUES **/	
		
	/** CONSTRUCTORS **/
	Moves (String name, MoveType mType, Type type, 
			Status effect, double probability,
			List<String> stats, boolean toSelf,  
			int pp, int power, int accuracy, int level, int crit, int numTurns,
			double damageToSelf, double flinch,		
			boolean goFirst, boolean isProtected,
			String delay, String info) {
		this.name = name;
		this.mType = mType;
		
		this.type = type;
		this.effect = effect;		
		this.probability = probability;
		
		this.stats = stats;
		this.toSelf = toSelf;
		
		this.pp = pp;
		this.power = power;
		this.accuracy = accuracy;
		this.level = level;
		this.crit = crit;
		this.numTurns = numTurns;
		
		this.goFirst = goFirst;
		this.isProtected = isProtected;
		
		this.damageToSelf = damageToSelf;
		this.flinch = flinch;
		
		this.delay = delay;
		this.info = info;
	}	
	Moves (String name, MoveType mType, Type type, int pp, int power, int accuracy, String info) {

		this(name, mType, type, 
				null, 0.0, 
				null, false, 
				pp, power, accuracy, 0, 0, 0,
				0.0, 0.0, 
				false, false, 
				"", info);
	}	
	Moves (String name, MoveType mType, Type type, double flinch, int pp, int power, int accuracy, String info) {

		this(name, mType, type, 
				null, 0.0, 
				null, false, 
				pp, power, accuracy, 0, 0, 0,
				0.0, flinch, 
				false, false, 
				"", info);
	}	
	Moves (String name, MoveType mType, Type type, int pp, int power, int accuracy, double damageToSelf, String info) {
		this(name, mType, type, 
				null, 0.0, 
				null, false, 
				pp, power, accuracy, 0, 0, 0,
				damageToSelf, 0.0, 
				false, false, 
				"", info);
	}	
	Moves (String name, MoveType mType, Type type, int pp, int power, int accuracy, int crit, String info) {
		this(name, mType, type, 
				null, 0.0, 
				null, false, 
				pp, power, accuracy, 0, crit, 0,
				0.0, 0.0, 
				false, false, 
				"", info);
	}	
	Moves (String name, MoveType mType, Type type, int pp, int power, int accuracy, boolean goFirst, String info) {
		this(name, mType, type, 
				null, 0.0, 
				null, false, 
				pp, power, accuracy, 0, 0, 0,
				0.0, 0.0, 
				goFirst, false, 
				"", info);
	}	
	Moves (String name, MoveType mType, Type type, int pp, int power, int accuracy, int numTurns, boolean isProtected, String delay, String info) {
		this(name, mType, type, 
				null, 0.0, 
				null, false, 
				pp, power, accuracy, 0, 0, numTurns,
				0.0, 0.0, 
				false, isProtected, 
				delay, info);
	}	
	Moves (String name, MoveType mType, Type type, Status effect, int pp, int accuracy, String info) {
		this(name, mType, type, 
				effect, 0.0, 
				null, false, 
				pp, 0, accuracy, 0, 0, 0,
				0.0, 0.0, 
				false, false, 
				"", info);
	}	
	Moves (String name, MoveType mType, Type type, Status effect, double probability, int pp, int power, int accuracy, String info) {
		this(name, mType, type, 
				effect, probability, 
				null, false, 
				pp, power, accuracy, 0, 0, 0,
				0.0, 0.0, 
				false, false, 
				"", info);
	}	
	Moves (String name, MoveType mType, Type type, Status effect, double probability, double flinch, int pp, int power, int accuracy, String info) {
		this(name, mType, type, 
				effect, probability, 
				null, false, 
				pp, power, accuracy, 0, 0, 0,
				0.0, flinch, 
				false, false, 
				"", info);
	}	
	Moves (String name, MoveType mType, Type type, Status effect, double probability, int pp, int power, int accuracy, int crit, String info) {
		this(name, mType, type, 
				effect, probability, 
				null, false, 
				pp, power, accuracy, 0, crit, 0,
				0.0, 0.0, 
				false, false, 
				"", info);
	}	
	Moves (String name, MoveType mType, Type type, boolean toSelf, int pp, int accuracy, int level, List<String> stats, String info) {
		this(name, mType, type, 
				null, 0.0, 
				stats, toSelf, 
				pp, 0, accuracy, level, 0, 0,
				0.0, 0.0, 
				false, false, 
				"", info);
	}	
	Moves (String name, MoveType mType, Type type, boolean toSelf, double probability, int pp, int power, int accuracy, int level, List<String> stats, String info) {
		this(name, mType, type, 
				null, probability, 
				stats, toSelf, 
				pp, power, accuracy, level, 0, 0,
				0.0, 0.0, 
				false, false, 
				"", info);
	}
	/** END CONSTRUCTORS **/
	
	/** GETTERS **/
	public String getName() { return name; }
	public MoveType getMType() { return mType; }	
	public Type getType() { return type; }
	public int getpp() { return pp; }
	public Status getEffect() { return effect; }	
	public double getProbability() { return probability; }	
	public double getFlinch() { return flinch; }	
	public double getSelfInflict() { return damageToSelf; }	
	public boolean isToSelf() { return toSelf; }	
	public int getAccuracy() { 
		if (accuracy == -1) return 100;
		else return accuracy; 
	}
	public int getPower() {	return power; }	
	public int getNumTurns() { return numTurns; }
	public boolean getGoFirst() { return goFirst; }	
	public boolean getIsProtected() { return isProtected; }	
	public String getDelay(String name) { return name + " " + delay; }	
	public String getInfo() {	return info; }	
	public int getCrit() { return crit; }	
	public int getLevel() { return level; }	
	public List<String> getStats() { return stats; }
	/** END GETTERS **/
}
/*** END MOVES ENUM ***/