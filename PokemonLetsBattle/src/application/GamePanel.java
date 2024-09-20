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

import javax.swing.JPanel;

import environment.EnvironmentManager;
import person.NPC;
import person.Player;
import tile.TileManager;

public class GamePanel extends JPanel implements Runnable {

	private static final long serialVersionUID = 1L;
	
	// GENERAL CONFIG
	private Graphics2D g2;
	private Thread gameThread;
	private int FPS = 60;

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
	
	public String[] mapFiles = { "worldmap.txt", "indoor01.txt", "dungeon_01_01.txt", "dungeon_01_02.txt" };
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
	public final int battleState = 4;
	public final int partyState = 5;
	
	// AREA STATES
	public int currentArea;
	public int nextArea;	
	public final int outside = 1;
	public final int house = 2;
	public final int shop = 3;
	
	// LOCATION STATES
	public int currentLocation;
	public final int town = 1;
	public final int gym = 2;
	
	public TileManager tileM = new TileManager(this);
	public AssetSetter aSetter = new AssetSetter(this);
	public EnvironmentManager eManager = new EnvironmentManager(this);
	public CollisionChecker cChecker = new CollisionChecker(this);	
	public BattleManager btlManager = new BattleManager(this);	
	
	public ArrayList<NPC> npcList = new ArrayList<>();
	public Player player = new Player(this);	
	public NPC npc[][] = new NPC[maxMap][10]; 
	
/** CONSTRUCTOR **/	
	public GamePanel() {
		this.setPreferredSize(new Dimension(screenWidth, screenHeight)); // screen size
		this.setBackground(Color.black);
		this.setDoubleBuffered(true); // improves rendering performance
		
		this.addKeyListener(keyH);
		this.setFocusable(true); // GamePanel in focus to receive input
	}
	
	protected void setupGame() {	
						
		gameState = playState;
		currentArea = outside;
		currentLocation = town;		
				
		setupMusic();
		
		tileM.loadMap();
		eManager.setup();
		
		player.setDefaultValues();	
		aSetter.setNPC();
		
		// TEMP GAME WINDOW (before drawing to window)
		tempScreen = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
		g2 = (Graphics2D)tempScreen.getGraphics();
		
		if (fullScreenOn) setFullScreen();
		
		gameState = playState;
		
//		btlManager.setBattle(btlManager.trainerBattle);
				
//		ui.battleSubState = ui.battle_Encounter;
//		gameState = battleState;
		
//		ui.partySubState = ui.party_Main;
//		ui.partySubState = ui.party_Stats;
//		gameState = partyState;
	}
	
	public void setupMusic() {		
		if (currentLocation == town) playMusic(0, 0);
	}

	public void playMusic(int category, int record) {		
		music.setFile(category, record);
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
			}		
		}
	}
	
	private void update() {
		
		// GAME PLAYING
		if (gameState == playState) {
			eManager.update();
			player.update();	
			updateNPC();
		}
		// GAME PAUSED
		else if (gameState == pauseState) { 
			
		}	
		// BATTLE STATE
		else if (gameState == battleState) {
			btlManager.update();
		}
	}
	private void updateNPC() {
		for (int i = 0; i < npc[1].length; i++) {
			if (npc[currentMap][i] != null) {
				npc[currentMap][i].update();
			}				
		}
	}	
	
	private void drawToTempScreen() {
		
		// TITLE SCREEN
		if (gameState == titleState) {
			ui.draw(g2);
		}		
		// PLAY STATE
		else if (gameState == playState || gameState == dialogueState) {	
			
			// DRAW TILES
			tileM.draw(g2);	
									
			// DRAW PLAYER
			player.draw(g2);
			
			// DRAW UI
			ui.draw(g2);	
						
			// ALWAYS DRAW DIVING PLAYER FIRST
			npcList.add(player);						
			
			// POPULATE ENTITY LIST
			for (NPC n : npc[currentMap]) { if (n != null) npcList.add(n); }						
			
			// SORT DRAW ORDER BY Y COORD
			Collections.sort(npcList, new Comparator<NPC>() {
				public int compare(NPC e1, NPC e2) {					
					int entityTop = Integer.compare(e1.worldY, e2.worldY);					
					return entityTop;
				}
			});
			
			// DRAW ENTITIES
			for (NPC e : npcList) { e.draw(g2); }
									
			// EMPTY ENTITY LIST
			npcList.clear();			
			
			// DRAW UI
			ui.draw(g2);			
		}		
		// BATTLE STATE
		else if (gameState == battleState) {
							
			// DRAW BATTLE MANAGER
			btlManager.draw(g2);
			
			// DRAW UI
			ui.draw(g2);
		}
		// BATTLE STATE
		else if (gameState == partyState) {
			
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