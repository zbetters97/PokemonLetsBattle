package application;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

import ai.PathFinder;
import data.SaveLoad;
import entity.Entity;
import entity.Player;
import environment.EnvironmentManager;
import event.EventHandler;
import pokemon.Pokemon.Pokedex;
import tile.TileManager;
import tile.tile_interactive.InteractiveTile;

public class GamePanel extends JPanel implements Runnable {

	private static final long serialVersionUID = 1L;
	
	public enum Weather {
		/** WEATHER BATTLE REFERENCE: https://www.ign.com/wikis/pokemon-heartgold-soulsilver-version/Weather **/
		
		CLEAR, SUNLIGHT, RAIN, HAIL, SANDSTORM
	}
	
	// GENERAL CONFIGaaaa
	private Graphics2D g2;
	private Thread gameThread;
	private int FPS = 60;
	public long playtime = 0;

	public static UtilityTool utility = new UtilityTool();
	public ConfigManager config = new ConfigManager(this);
	public KeyHandler keyH = new KeyHandler(this);
	public UI ui = new UI(this);
	public SoundCard music = new SoundCard();
	public SoundCard se = new SoundCard();	
	
	// BUTTON MAPPING
	public int btn_UP = KeyEvent.VK_UP;
	public int btn_DOWN = KeyEvent.VK_DOWN;
	public int btn_LEFT = KeyEvent.VK_LEFT;
	public int btn_RIGHT = KeyEvent.VK_RIGHT;
	
	public int btn_DUP = KeyEvent.VK_1;
	public int btn_DDOWN = KeyEvent.VK_2;
	
	public int btn_A = KeyEvent.VK_A;		
	public int btn_B = KeyEvent.VK_S;
	public int btn_X = KeyEvent.VK_D;
	public int btn_Y = KeyEvent.VK_F;
	
	public int btn_L = KeyEvent.VK_W;	
	public int btn_R = KeyEvent.VK_E;	
	public int btn_Z = KeyEvent.VK_R;		
	
	public int btn_START = KeyEvent.VK_SPACE;
	public int btn_SELECT = KeyEvent.VK_SHIFT;
	
	public int btn_DEBUG = KeyEvent.VK_ESCAPE;
	
	// SCREEN SETTINGS
	private final int originalTileSize = 16; // 16x16 tile
	private final int scale = 3; // scale rate to accommodate for large screen
	public final int tileSize = originalTileSize * scale; // scaled tile (16*3 = 48px)	
	public final int maxScreenCol = 16; // columns (width)
	public final int maxScreenRow = 12; // rows (height)
	public final int screenWidth = tileSize * maxScreenCol; // screen width (in tiles) 768px
	public final int screenHeight = tileSize * maxScreenRow; // screen height (in tiles) 768px
		
	// WORLD SIZE (assigned in Tile Manager)
	public int maxWorldCol;
	public int maxWorldRow;
	public int worldWidth;
	public int worldHeight;
	
	public String[] mapFiles = { "petalburg.txt", "pokecenter.txt", "pokemart.txt"};
	public final int maxMap = mapFiles.length;
	public int currentMap = 0;
	
	// FULL SCREEN SETTINGS
	public boolean fullScreenOn = false;
	private int screenWidth2 = screenWidth;
	private int screenHeight2 = screenHeight;
	private BufferedImage tempScreen;	
	
	// GAME STATES
	public int gameState;
	public final int titleState = 0;
	public final int playState = 1;
	public final int pauseState = 2;	
	public final int dialogueState = 3;	
	public final int tradeState = 4;
	public final int healState = 5;
	public final int hmState = 6;
	public final int transitionState = 7;	
	public final int battleState = 8;
	public final int pcState = 9;
			
	// AREA STATES
	public int currentArea;
	public int nextArea;	
	public final int town = 1;
	public final int house = 2;
	public final int shop = 3;
	public final int gym = 4;
	
	// LOCATION STATES
	public final int petalburg = 0;		
	public final int pokecenter = 1;
	public final int pokemart = 2;

	// SOUND LIBRARIES
	public final int menu_SE = 2;
	public final int cry_SE = 3;
	public final int faint_SE = 4;
	public final int moves_SE = 5;
	public final int battle_SE = 6;
	public final int world_SE = 7;
	public final int entity_SE = 8;
	
	public TileManager tileM = new TileManager(this);
	public AssetSetter aSetter = new AssetSetter(this);
	public EnvironmentManager eManager = new EnvironmentManager(this);
	public CollisionChecker cChecker = new CollisionChecker(this);	
	public EventHandler eHandler = new EventHandler(this);	
	public EntityGenerator eGenerator = new EntityGenerator(this);
	public iTileGenerator iGenerator = new iTileGenerator(this);
	public BattleManager btlManager = new BattleManager(this);
	public PathFinder pFinder = new PathFinder(this);
	
	public ArrayList<Entity> entities = new ArrayList<>();
	public Player player = new Player(this);	
	public Entity npc[][] = new Entity[maxMap][10]; 
	public Entity obj[][] = new Entity[maxMap][20];
	public Entity obj_i[][] = new Entity[maxMap][20];
	public InteractiveTile iTile[][] = new InteractiveTile[maxMap][100];
	public ArrayList<Entity> particleList = new ArrayList<>();
	
	public Map<Integer, Map<Pokedex, Integer>> wildEncounters_Grass = new HashMap<>();
	public Map<Integer, Integer> wildLevels_Grass = new HashMap<>();	
	public Map<Integer, Map<Integer, Map<Pokedex, Integer>>> wildEncounters_Fishing = new HashMap<>();
	
	// SAVE LOAD MANAGER
	public SaveLoad saveLoad = new SaveLoad(this);
	
/** CONSTRUCTOR **/	
	public GamePanel() {
		this.setPreferredSize(new Dimension(screenWidth, screenHeight)); // screen size
		this.setBackground(Color.black);
		this.setDoubleBuffered(true); // improves rendering performance
		
		this.addKeyListener(keyH);
		this.setFocusable(true); // GamePanel in focus to receive input
	}
	
	protected void setupGame() {	
		
		currentMap = 0;
		
		gameState = playState;
		currentArea = town;	
								
		setupMusic();
		
		tileM.loadMap();
		eManager.setup();
		
		player.setDefaultValues();	
		aSetter.setNPC();
		aSetter.setObject();
		aSetter.setInteractiveObjects();
		
		setupWildPokemon();
		
		// TEMP GAME WINDOW (before drawing to window)
		tempScreen = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
		g2 = (Graphics2D)tempScreen.getGraphics();
		
		if (fullScreenOn) setFullScreen();
		
		gameState = playState;
	}
	
	private void setupWildPokemon() {
		
		wildEncounters_Grass.put( 
				petalburg, Map.ofEntries(
						Map.entry(Pokedex.ZIGZAGOON, 65), 
						Map.entry(Pokedex.POOCHYENA, 30),
						Map.entry(Pokedex.PIKACHU, 5)
				)
		);
		wildLevels_Grass.put(petalburg, 4);
		
		wildEncounters_Fishing.put( 
				petalburg, Map.ofEntries(
						Map.entry(0, Map.ofEntries(
										Map.entry(Pokedex.MAGIKARP, 100)
						)),
						Map.entry(1, Map.ofEntries(
										Map.entry(Pokedex.HORSEA, 60),
										Map.entry(Pokedex.MAGIKARP, 25)	,								
										Map.entry(Pokedex.SPHEAL, 15)
						)),
						Map.entry(2, Map.ofEntries(										
										Map.entry(Pokedex.SEADRA, 50),
										Map.entry(Pokedex.HORSEA, 30),									
										Map.entry(Pokedex.SPHEAL, 12),
										Map.entry(Pokedex.GYARADOS, 7),
										Map.entry(Pokedex.KYOGRE, 1)
						))
				)
		);
	}
	
	public void setupMusic() {					
		if (currentMap == petalburg) startMusic(0, "littleroot");
		else if (currentMap == pokecenter) startMusic(0, "pokecenter");		
		else if (currentMap == pokemart) startMusic(0, "pokemart");		
	}

	public void startMusic(int category, int record) {		
		music.setFile(category, record);
		music.play();
		music.loop();
	}
	public void startMusic(int category, String file) {		
		music.setFile(category, se.getFile(category, file));
		music.play();
		music.loop();
	}
	public void pauseMusic() {
		music.stop();
	}
	public void playMusic() {
		music.play();
		music.loop();
	}
	public void stopMusic() {
		music.stop();
	}
	public void playSE(int category, int record) {
		se.setFile(category, record);
		se.play();
	}	
	public void playSE (int category, String file) {
		se.setFile(category, se.getFile(category, file));
		se.play();
	}
	
	private void setFullScreen() {
		
		// GET SYSTEM SCREEN
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();
		gd.setFullScreenWindow(Driver.window);
		
		// GET FULL SCREEN WIDTH AND HEIGHT
		screenWidth2 = Driver.window.getWidth();
		screenHeight2 = Driver.window.getHeight();
	}
	
	protected void startGameThread() {		
		gameThread = new Thread(this); // new Thread with GamePanel class
		gameThread.start(); // calls run() method
	}
	
	@Override
	public void run() {		
		
		long currentTime;
		long lastTime = System.nanoTime();
		double drawInterval = 1000000000 / FPS; // 1/60th of a second
		double delta = 0;
		int drawCount = 0;
		
		// UPDATE AND REPAINT gameThread
		while (gameThread != null) {
			
			currentTime = System.nanoTime();			
			delta += (currentTime - lastTime) / drawInterval; // time passed (1/60th second)
			lastTime = currentTime;
			
			if (delta >= 1) {
				
				// UPDATE GAME INFORMATION
				update();
				
				// DRAW TEMP SCREEN WITH NEW INFORMATION
				drawToTempScreen();
				
				// SEND TEMP SCREEN TO MONITOR
				drawToScreen();
				
				delta = 0;		
				
				drawCount++;
				if (drawCount >= 60) {
					playtime++;
					drawCount = 0;		    
				}
			}		
		}
	}
	
	private void update() {
		
		// GAME PLAYING
		if (gameState == playState) {
			eManager.update();
			player.update();	
			updateNPC();
			updateOBJ();
			updateOBJ_I();
			updateParticles();
		}
		// GAME PAUSED
		else if (gameState == pauseState) { 
			
		}	
		// BATTLE STATE
		else if (gameState == battleState) {
//			btlManager.update();
		}
	}
	private void updateNPC() {
		for (int i = 0; i < npc[1].length; i++) {
			if (npc[currentMap][i] != null) {
				npc[currentMap][i].update();
			}				
		}
	}	
	private void updateOBJ() {
		for (int i = 0; i < obj[1].length; i++) {
			if (obj[currentMap][i] != null) {
				if (!obj[currentMap][i].alive)
					obj[currentMap][i] = null;
				else 
					obj[currentMap][i].update();
			}				
		}
	}	
	private void updateOBJ_I() {
		for (int i = 0; i < obj_i[1].length; i++) {
			if (obj_i[currentMap][i] != null) {			
				if (!obj_i[currentMap][i].alive)
					obj_i[currentMap][i] = null;
				else 
					obj_i[currentMap][i].update();
			}				
		}
	}	
	private void updateParticles() {
		for (int i = 0; i < particleList.size(); i++) {
			if (particleList.get(i) != null) {
				particleList.get(i).update();	
				if (!particleList.get(i).alive) 
					particleList.remove(i);						
			}
		}
	}
	
	public void changeArea() {
		
		player.resetValues();		
		particleList.clear();
		
		tileM.loadMap();	
		
		if (nextArea != currentArea) {
			stopMusic();			
			setupMusic();
		}					
			
		aSetter.setInteractiveObjects();
		
		currentArea = nextArea;
	}
	
	public void resetGame() {
		stopMusic();
		
		removeTempEntity(true);
		
		player.alive = true;	
		player.pokeParty.clear();
		player.inventory_keyItems.clear();
		player.inventory_items.clear();
		player.inventory_pokeballs.clear();
		player.restoreStatus();
		player.setDefaultValues();
		player.setDefaultPosition();	
		player.resetCounter();		
				
		aSetter.setNPC();	
		aSetter.setObject();
		aSetter.setInteractiveObjects();
		aSetter.setInteractiveTiles(true);
		
		eManager.lighting.resetDay();	
	}
	public void removeTempEntity(boolean reset) {		

		for (int mapNum = 0; mapNum < maxMap; mapNum++) {
			
			for (int i = 0; i < npc[1].length; i++) {
				if (npc[mapNum][i] != null && npc[mapNum][i].temp) {
					npc[mapNum][i] = null;
				}
			}	
			for (int i = 0; i < obj[1].length; i++) {
				if (obj[mapNum][i] != null && obj[mapNum][i].temp) {					
					obj[mapNum][i] = null;						
				}
			}	
			for (int i = 0; i < obj_i[1].length; i++) {
				if (obj_i[mapNum][i] != null && obj_i[mapNum][i].temp) {
					obj_i[mapNum][i] = null;
				}
			}	
		}
	}
	
	private void drawToTempScreen() {
		
		// TITLE SCREEN
		if (gameState == titleState) {
			ui.draw(g2);
		}		
		// BATTLE STATE
		else if (gameState == battleState) {
							
			// DRAW UI
			ui.draw(g2);
		}
		// PC STATE
		else if (gameState == pcState) {
			
			// DRAW UI
			ui.draw(g2);
		}
		// PLAY STATE
		else {	
			
			// DRAW TILES
			tileM.draw(g2);	
									
			// DRAW PLAYER
			player.draw(g2);
			
			// DRAW UI
			ui.draw(g2);	
												
			// DON'T DRAW JUMPING PLAYER FIRST
			if (!player.jumping) { entities.add(player); }
			
			// POPULATE ENTITY LIST
			for (Entity n : npc[currentMap]) { if (n != null) entities.add(n); }			
			for (Entity o : obj[currentMap]) { if (o != null) entities.add(o); }			
			for (Entity oi : obj_i[currentMap]) { if (oi != null) entities.add(oi); }	
			for (Entity pa : particleList) { if (pa != null) entities.add(pa); }			
			
			// SORT DRAW ORDER BY Y COORD
			Collections.sort(entities, new Comparator<Entity>() {
				public int compare(Entity e1, Entity e2) {					
					int entityTop = Integer.compare(e1.worldY, e2.worldY);					
					return entityTop;
				}
			});
			
			// DRAW ENTITIES
			for (Entity e : entities) { e.draw(g2); }
							
			// DRAW JUMPING PLAYER OVER ENTITIES
			if (player.jumping) { player.draw(g2); }
			
			// EMPTY ENTITY LIST
			entities.clear();			
			
			// DRAW UI
			ui.draw(g2);			
		}		
	}
	private void drawToScreen() {		
		Graphics g = getGraphics();
		g.drawImage(tempScreen, 0, 0, screenWidth2, screenHeight2, null);	
		g.dispose();
	}	
}