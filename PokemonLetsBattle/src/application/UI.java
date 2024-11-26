package application;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;

import entity.Entity;
import entity.npc.NPC_Nurse;
import moves.Move;
import pokemon.Pokedex;
import pokemon.Pokemon;
import pokemon.Pokemon.Protection;
import properties.Type;

public class UI {
	
	// UI FONTS
	private GamePanel gp;
	private Graphics2D g2;	
	public Font PK_DS;
	
	// COLORS
	private Color dialogue_blue = new Color(115,109,127);
	private Color pause_yellow = new Color(245,170,68);
	private Color pause_orange = new Color(248,192,88);	
	private Color party_green = new Color(81,109,69);
	private Color party_blue = new Color(71,174,181);
	private Color party_red = new Color(245,126,63);
	private Color party_gray = new Color(136,152,168);
	private Color party_faint = new Color(181,87,71);
	private Color hp_green = new Color(105,233,160);
	private Color hp_yellow = new Color(222,202,43);
	private Color hp_red = new Color(200,65,65);	
	private Color battle_white = new Color(234,233,246);
	private Color battle_gray = new Color(135,131,156);
	private Color battle_green = new Color(111,163,161);
	private Color battle_red = new Color(165,71,74);	
	private Color battle_yellow = new Color(250,254,219);
	private Color battle_blue = new Color(62,211,255);
	private Color pc_green = new Color(159,232,143);
	private Color pc_blue = new Color(128,168,176);
	private Color menu_yellow = new Color(255,229,163);
	private Color menu_orange = new Color(255,208,163);
		
	// DIALOGUE HANDLER	
	public Entity npc;
	public String currentDialogue = "";
	public String battleDialogue;
	public String combinedText = "";
	public int dialogueCounter = 0;	
	public int charIndex = 0;	
	public int textSpeed = 2;
	public boolean canSkip = false;
	private BufferedImage dialogue_next;
	public BufferedImage battleIcon;
	
	public int commandNum = 0;
	public int subState = 0;

	// TRANSITION
	public boolean tMoving = false;
	private int tCounter = 0;
	public String tDirection = "";
	
	// PC VALUES 
	private BufferedImage pc_cursor, pc_cursor_up, pc_bkg, pc_data, pc_party, pc_box_1;
	private int boxNum = 3, boxTab = 0; 
	private Pokemon selectedFighter = null;	
	
	// PARTY VALUES
	public String partyDialogue = "";
	private boolean partyMove = false;
	private int partyMoveNum = -1;
	public Entity partyItem;
	public boolean partyItemApply = false;
	private boolean partyItemGive = false;
	private BufferedImage dex_ball;
	
	// TRADE VALUES 
	private Entity selectedItem = null;
	private int selectedAmount = 1;
	
	// BAG VALUES
	private String bag_Subtitle = "";
	private int bagStart = 0;
	public int bagNum = 0;	
	public String bagDialogue = "";
	private BufferedImage bag_menu, bag_Image, bag_Tab;
	private BufferedImage bag_keyItems, bag_items, bag_pokeballs, bag_moves;
	private BufferedImage bag_tab_1, bag_tab_2, bag_tab_3, bag_tab_4;	
	
	// BATTLE VALUES
	private BufferedImage battle_arena, battle_bkg;
	private BufferedImage ball_empty, ball_active, ball_inactive;	
	public int fighterNum = 0;	
	private int attackCounter = 0;
	private int hitCounter = -1;
	private int hpCounter = 0;
	public boolean isFighterCaptured = false;
	public Pokemon evolvePokemon = null;
	
	// FIGHTER X/Y VALUES
	public int fighter_one_X;
	public int fighter_two_X;
	public int fighter_one_Y;
	public int fighter_two_Y;	
	public final int fighter_one_startX;
	public final int fighter_two_startX;
	private final int fighter_one_endX;
	private final int fighter_two_endX;	
	public final int fighter_one_startY;
	public final int fighter_two_startY;
	private final int fighter_one_platform_endX;
	private final int fighter_two_platform_endX;
	private final int fighter_one_platform_Y;
	private final int fighter_two_platform_Y;
	
	// PC STATE
	private int pcState = 0;
	private final int pc_Box = 0;
	private final int pc_Select = 1;
	private final int pc_Party = 2;
	private final int pc_Summary = 3;
	
	// PAUSE STATE
	public int pauseState;
	public final int pause_Main = 0;
	public final int pause_Dex = 1;
	public final int pause_Party = 2;
	public final int pause_Bag = 3;
	public final int pause_Player = 4;
	public final int pause_Save = 5;
	public final int pause_Options = 7;	
	
	// PARTY STATES
	public int partyState;
	public final int party_Main_Select = 1;
	public final int party_Main_Options = 2;
	public final int party_Main_Options_Item = 3;
	public final int party_Main_Dialogue = 4;
	public final int party_Skills = 5;
	public final int party_Moves = 6;
	public final int party_MoveSwap = 7;
	
	// BAG STATE
	public int bagState;
	public final int bag_Main = 0;
	public final int bag_Options = 1;
	public final int bag_Dialogue = 2;
	
	// BAG TABS
	public int bagTab;
	public final int bag_KeyItems = 0;
	public final int bag_Items = 1;
	public final int bag_Pokeballs = 2;
	public final int bag_Moves = 3;
		
	// BATTLE STATE
	public int battleState;
	public final int battle_Encounter = 1;	
	public final int battle_Options = 2;
	public final int battle_Moves = 3;
	public final int battle_Dialogue = 4;
	public final int battle_LevelUp = 5;
	public final int battle_Confirm = 6;
	public final int battle_Evolve = 7;
	public final int battle_End = 8;
	
	public UI(GamePanel gp) {
		this.gp = gp;
		
		dialogue_next = ball_empty = setup("/ui/dialogue/advance", (int) (gp.tileSize * 0.5), (int) (gp.tileSize * 0.5));
		battleIcon = setup("/ui/dialogue/battle_icon", gp.tileSize, gp.tileSize);
		
		battle_arena = setup("/ui/arenas/grass", gp.tileSize * 7, gp.tileSize * 3);
		battle_bkg = setup("/ui/arenas/grass-bkg", gp.screenWidth, gp.screenHeight);
		
		ball_empty = setup("/ui/battle/ball-empty", (int) (gp.tileSize * 0.5), (int) (gp.tileSize * 0.5));
		ball_active = setup("/ui/battle/ball-active", (int) (gp.tileSize * 0.5), (int) (gp.tileSize * 0.5));
		ball_inactive = setup("/ui/battle/ball-inactive", (int) (gp.tileSize * 0.5), (int) (gp.tileSize * 0.5));

		bag_menu = setup("/ui/bag/bag-menu", gp.screenWidth, gp.screenHeight);
		bag_tab_1 = setup("/ui/bag/bag-tab-1", gp.tileSize * 3, gp.tileSize / 3);
		bag_tab_2 = setup("/ui/bag/bag-tab-2", gp.tileSize * 3, gp.tileSize / 3);
		bag_tab_3 = setup("/ui/bag/bag-tab-3", gp.tileSize * 3, gp.tileSize / 3);
		bag_tab_4 = setup("/ui/bag/bag-tab-4", gp.tileSize * 3, gp.tileSize / 3);
//		bag_tab_5 = setup("/ui/bag/bag-tab-5", gp.tileSize * 3, gp.tileSize / 3);
		
		bag_keyItems = setup("/ui/bag/bag-keyitems", gp.tileSize * 5, gp.tileSize * 5);
		bag_items = setup("/ui/bag/bag-items", gp.tileSize * 5, gp.tileSize * 5);
		bag_pokeballs = setup("/ui/bag/bag-pokeballs", gp.tileSize * 5, gp.tileSize * 5);
		bag_moves = setup("/ui/bag/bag-moves", gp.tileSize * 5, gp.tileSize * 5);
		
		dex_ball = setup("/collectables/battle/ball_poke", (int) (gp.tileSize * 0.6), (int) (gp.tileSize * 0.6));	
		
		pc_cursor = setup("/ui/pc/cursor", (int) (gp.tileSize), (int) (gp.tileSize));
		pc_cursor_up = setup("/ui/pc/cursor_up", (int) (gp.tileSize), (int) (gp.tileSize));
		pc_bkg = setup("/ui/pc/bkg", gp.screenWidth, gp.screenHeight);
		pc_data = setup("/ui/pc/box-data", (int) (gp.tileSize * 5.5), (int) (gp.tileSize * 11.5));
		pc_party = setup("/ui/pc/box-party", (int) (gp.tileSize * 5.5), (int) (gp.tileSize * 11.2));
		pc_box_1 = setup("/ui/pc/box-01", (int) (gp.tileSize * 10.37), (int) (gp.tileSize * 10.55));
		
		fighter_one_startX = gp.tileSize * 14;
		fighter_two_startX = 0 - gp.tileSize * 3;
		fighter_one_startY = (int) (gp.tileSize * 3.8);
		fighter_two_startY = 0;	
		
		fighter_one_X = fighter_one_startX;
		fighter_two_X = fighter_two_startX;
		fighter_one_Y = fighter_one_startY;
		fighter_two_Y = fighter_two_startY;			
		
		fighter_one_platform_Y = fighter_one_startY + gp.tileSize * 4;
		fighter_two_platform_Y = (int) (fighter_two_startY + gp.tileSize * 2.3);
		
		fighter_one_endX = gp.tileSize * 2;
		fighter_two_endX = gp.tileSize * 9;
		fighter_one_platform_endX = fighter_one_endX - gp.tileSize;
		fighter_two_platform_endX = fighter_two_endX - gp.tileSize;	
		
		// FONT DECLARATION		
		try {
			InputStream is;
			
			is = getClass().getResourceAsStream("/ui/fonts/pokemon-ds.ttf");
			PK_DS = Font.createFont(Font.TRUETYPE_FONT, is);
		} 
		catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}
	}
	
	public void draw(Graphics2D g2) {		
		this.g2 = g2;
		
		g2.setFont(PK_DS);
			
		if (gp.gameState == gp.playState) {
			drawHUD();
		}
		else if (gp.gameState == gp.pauseState) {
			drawPauseScreen();
		}
		else if (gp.gameState == gp.dialogueState) {
			drawHUD();
			drawDialogueScreen(true);
		}
		else if (gp.gameState == gp.tradeState) {
			drawTradeScreen();
		}
		else if (gp.gameState == gp.hmState) {
			drawHUD();
			drawHMScreen();
		}
		else if (gp.gameState == gp.healState) {
			drawHUD();
			drawHealScreen();
		}
		else if (gp.gameState == gp.battleState) {
			drawBattleScreen();
		}		
		else if (gp.gameState == gp.transitionState) {
			drawTransitionScreen();
		}
		else if (gp.gameState == gp.pcState) {
			drawPCScreen();
		}
	}
	
	private void drawTradeScreen() {	
				
		switch (subState) {
			case 0: trade_select(); break;
			case 1: trade_buy(); break;
//			case 2: trade_sell(); break;
		}
	}
	private void trade_select() {
		
		int x;
		int y;
		int width;
		int height;
		
		npc.dialogueSet = 0;
		drawDialogueScreen(false);
		
		x = (int) (gp.tileSize * 11.1);
		y = (int) (gp.tileSize * 5.4);
		width = (int) (gp.tileSize * 2.9);
		height = (int) (gp.tileSize * 3.4);
		drawSubWindow(x, y, width, height, 25, 10, battle_white, dialogue_blue);
				
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 48F));
		
		x += gp.tileSize * 0.8;		
		y += gp.tileSize;
		drawText("Buy", x, y, Color.BLACK, Color.LIGHT_GRAY);
		if (commandNum == 0) {
			g2.drawString(">", x-24, y);
			if (gp.keyH.aPressed) {
				gp.keyH.aPressed = false;
				gp.keyH.playCursorSE();
				subState = 1;
			}		
		}		
		y += gp.tileSize;
		drawText("Sell", x, y, Color.BLACK, Color.LIGHT_GRAY);
		if (commandNum == 1) {
			g2.drawString(">", x-24, y);
			if (gp.keyH.aPressed) {
				gp.keyH.aPressed = false;
				gp.keyH.playCursorSE();
				subState = 2;
			}			
		}						
		y += gp.tileSize;
		drawText("Leave", x, y, Color.BLACK, Color.LIGHT_GRAY);
		if (commandNum == 2) {
			g2.drawString(">", x-24, y);
			if (gp.keyH.aPressed) {		
				gp.keyH.aPressed = false;
				gp.keyH.playCursorSE();
				subState = 0;
				commandNum = 0;
				canSkip = false;
				charIndex = 0;
				combinedText = "";
				npc.startDialogue(npc, 3);
			}	
		}
				
		if (gp.keyH.upPressed) {
			gp.keyH.upPressed = false;
			if (commandNum > 0) {
				gp.keyH.playCursorSE();
				commandNum--;
			}
		}
		if (gp.keyH.downPressed) {
			gp.keyH.downPressed = false;
			if (commandNum < 2) {
				gp.keyH.playCursorSE();
				commandNum++;
			}
		}
		if (gp.keyH.bPressed) {
			gp.keyH.bPressed = false;
			subState = 0;
			commandNum = 0;
			canSkip = false;
			charIndex = 0;
			combinedText = "";
			npc.startDialogue(npc, 3);
		}
	}
	private void trade_buy() {
		
		int x;
		int y;
		int width;
		int height;
		int textX;
		int slotX;
		int slotY;
		String text;
		
		// MONEY WINDOW
		x = 5;
		y = 5;
		width = gp.tileSize * 5;
		height = gp.tileSize * 2;
		drawSubWindow(x, y, width, height, 10, 10, battle_white, dialogue_blue);
		
		// PLAYER WONEY
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 60F));
		text = "$" + gp.player.money;
		x += gp.tileSize * 4.8;
		x = getXforRightAlignText(text, x);
		y += gp.tileSize * 1.4;		
		drawText(text, x, y, Color.BLACK, Color.LIGHT_GRAY);
		
		// QUANTITY WINDOW
		if (selectedItem != null) {		
			
			x = (int) (gp.tileSize * 2.7);
			y = (int) (gp.tileSize * 5.4);
			width = gp.tileSize * 7;
			height = gp.tileSize * 2;
			drawSubWindow(x, y, width, height, 10, 10, battle_white, dialogue_blue);
			
			text = "x" + selectedAmount;
			x += gp.tileSize * 0.4;
			y += gp.tileSize * 1.4;
			drawText(text, x, y, Color.BLACK, Color.LIGHT_GRAY);
			
			text = "$" + (selectedAmount * selectedItem.value);
			x += gp.tileSize * 5.5;
			x = getXforRightAlignText(text, x);	
			drawText(text, x, y, Color.BLACK, Color.LIGHT_GRAY);
		}
		
		// DESCRIPTION WINDOW
		x = 5;
		y = (int) (gp.tileSize * 7.7);
		width = gp.tileSize * 11;
		height = gp.tileSize * 4;
		drawSubWindow(x, y, width, height, 25, 10, battle_white, dialogue_blue);
		
		// ITEM DESCRIPTION
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 52F));
		x += gp.tileSize * 0.4;
		y += gp.tileSize;
		text = npc.inventory_items.get(commandNum).description;
		for (String line : text.split("\n")) {			
			drawText(line, x, y, Color.BLACK, Color.LIGHT_GRAY);
			y += gp.tileSize;
		} 
						
		// SHOP ITEMS WINDOW
		x = (int) (gp.tileSize * 9);
		y = 5;
		width = (int) (gp.tileSize * 6.9);
		height = (int) (gp.tileSize * 11.6);
		drawSubWindow(x, y, width, height, 10, 10, menu_yellow, menu_orange);
		
		// SHOP ITEMS LIST
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 42F));
		x += gp.tileSize * 0.3;
		y += gp.tileSize * 1.5;
		textX = x;
		
		slotX = (int) (x - gp.tileSize * 0.15);
		slotY = (int) (y - gp.tileSize * 0.7);
		width = (int) (gp.tileSize * 6.65);
		height = (int) (gp.tileSize * 0.85);
		
		for (int i = 0; i < npc.inventory_items.size(); i++) {
			
			text = npc.inventory_items.get(i).name;
			
			if (selectedItem == npc.inventory_items.get(i)) {
				drawText(text, x, y, hp_red, Color.LIGHT_GRAY);			
			}
			else {
				drawText(text, x, y, Color.BLACK, Color.LIGHT_GRAY);		
				
				if (commandNum == i) {
					g2.setColor(battle_red);
					g2.setStroke(new BasicStroke(3));				
					g2.drawRoundRect(slotX, slotY, width, height, 4, 4);
				}
			}
			
			text = "$" + npc.inventory_items.get(i).value;
			textX += gp.tileSize * 6.3;
			textX = getXforRightAlignText(text, textX);
			drawText(text, textX, y, Color.BLACK, Color.LIGHT_GRAY);	
									
			textX = x;
			y += gp.tileSize * 0.9;		
			slotY += gp.tileSize * 0.9;
		}
						
		if (selectedItem == null) {
			if (gp.keyH.upPressed) {
				gp.keyH.upPressed = false;
				if (commandNum > 0) {
					gp.keyH.playCursorSE();
					commandNum--;
				}
			}
			if (gp.keyH.downPressed) {
				gp.keyH.downPressed = false;
				if (commandNum < npc.inventory_items.size() - 1) {
					gp.keyH.playCursorSE();
					commandNum++;
				}
			}
			if (gp.keyH.bPressed) {
				gp.keyH.bPressed = false;
				gp.keyH.playCursorSE();	
				subState = 0;
				commandNum = 0;
				canSkip = false;
				charIndex = 0;
				combinedText = "";
			}		
			if (gp.keyH.aPressed) {
				gp.keyH.aPressed = false;
				gp.keyH.playCursorSE();
				selectedItem = npc.inventory_items.get(commandNum);			
			}
		}
		else {						
			if (gp.keyH.upPressed) {
				gp.keyH.upPressed = false;
				if (selectedAmount < 99) {
					
					Entity item = gp.player.getItem(selectedItem, gp.player);
					if (item == null || item.amount + selectedAmount < 99) {
						gp.keyH.playCursorSE();	
						selectedAmount++;	
					}
				}
			}
			if (gp.keyH.downPressed) {
				gp.keyH.downPressed = false;
				if (selectedAmount > 1) {					
					gp.keyH.playCursorSE();	
					selectedAmount--;					
				}
			}
			if (gp.keyH.bPressed) {
				gp.keyH.bPressed = false;
				gp.keyH.playCursorSE();
				selectedItem = null;
				selectedAmount = 1;
			}		
			if (gp.keyH.aPressed) {
				gp.keyH.aPressed = false;
			
				int cost = selectedItem.value * selectedAmount;
								
				if (gp.player.money >= selectedItem.value * selectedAmount) {
					
					playPurchaseSE();
					
					gp.player.money -= cost;
					gp.player.addItem(selectedItem, gp.player, selectedAmount);
					
					selectedItem = null;
					selectedAmount = 1;
				}
				// NOT ENOUGH MONEY
				else {
					gp.keyH.playErrorSE();
				}
			}
		}
	}
	
	// HUD	
	private void drawHUD() {
		
		// DEBUG
		if (gp.keyH.debug) {				
			HUD_debug();
		}
	}
	private void HUD_debug() {
		
		int x = 10; 
		int y = gp.tileSize * 6; 
		int lineHeight = 20;
		
		String timeOfDay = "";
		switch (gp.eManager.lighting.dayState) {
			case 0: timeOfDay = "DAY"; break;
			case 1: timeOfDay = "DUSK"; break;
			case 2: timeOfDay = "NIGHT"; break;
			case 3: timeOfDay = "DAWN"; break;
		}
		
		g2.setColor(Color.WHITE);
		g2.setFont(new Font("Arial", Font.BOLD, 50));
		g2.drawString(timeOfDay, x, y - gp.tileSize);
		
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 20f));						
		
		g2.drawString("WorldX: " + gp.player.worldX, x , y); 
		y += lineHeight;
		g2.drawString("WorldY: " + gp.player.worldY, x , y); 
		y += lineHeight;
		g2.drawString("Column: " + (gp.player.worldX + gp.player.hitbox.x) / gp.tileSize, x , y);
		y += lineHeight;
		g2.drawString("Row: " + (gp.player.worldY + gp.player.hitbox.y) / gp.tileSize, x , y);
		y += lineHeight;
		g2.drawString("Time Counter: " + gp.eManager.lighting.dayCounter, x, y);
		
		g2.setStroke(new BasicStroke(1));	
		g2.setColor(Color.RED);
		g2.drawRect(gp.player.screenX + gp.player.hitbox.x, gp.player.screenY + gp.player.hitbox.y, 
				gp.player.hitbox.width, gp.player.hitbox.height);
		
		g2.setColor(Color.WHITE);
		g2.setFont(PK_DS);
	}	
	
	// DIALOGUE	
	private void drawDialogueScreen(boolean skip) {
						
		int x = (int) (gp.tileSize * 2);
		int y = gp.tileSize * 9;
		int width =(int) (gp.tileSize * 12);
		int height = (int) (gp.tileSize * 2.5);
		drawSubWindow(x, y, width, height, 25, 10, battle_white, dialogue_blue);
		
		// NPC HAS SOMETHING TO SAY
		if (npc.dialogues[npc.dialogueSet][npc.dialogueIndex] != null) {	
			
			if (dialogueCounter == textSpeed) {
				char characters[] = npc.dialogues[npc.dialogueSet][npc.dialogueIndex].toCharArray();
							
				if (charIndex < characters.length) {					
					// playDialogueSE();
					
					String s = String.valueOf(characters[charIndex]);				
					combinedText += s;
					currentDialogue = combinedText;					
					charIndex++;					
				}
				if (charIndex >= characters.length) {
					canSkip = true;
				}
	
				dialogueCounter = 0;
			}
			else
				dialogueCounter++;
			
		}
		// NPC HAS NO MORE DIALOGUE
		else {			
			npc.dialogueIndex = 0;
			
			// PLAYER HAS DIALOGUE RESPONSE
			if (npc.hasBattle) {
				
				skipDialogue();
				
				gp.btlManager.setup(gp.btlManager.trainerBattle, npc, null, null);
				
				startBattle();
			}
			else if (npc.type == npc.type_obstacle_i) {
				
				Pokemon p = gp.player.pokemonHasHM(npc.hmType);		
				
				if (p != null) gp.gameState = gp.hmState;	
				else gp.gameState = gp.playState;							
			}
			else if (npc.name.equals(NPC_Nurse.npcName) && npc.dialogueSet == 0){			
				gp.gameState = gp.healState;
			}	
			else {
				gp.gameState = gp.playState;
			}
		}				

		x += gp.tileSize * 0.6;
		y += gp.tileSize * 1.1;			
		String lastLine = "";
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 48F));
  		for (String line : currentDialogue.split("\n")) {   			
  			drawText(line, x, y, Color.BLACK, Color.LIGHT_GRAY);
  			lastLine = line;
  			y += 40;
		} 
  		
  		// DRAW ICON BELOW DIALOGUE BOX
  		int nextIndex = npc.dialogueIndex + 1;
  		if (canSkip && npc.dialogues[npc.dialogueSet][nextIndex] != null) {  
 			x += (int)g2.getFontMetrics().getStringBounds(lastLine, g2).getWidth() + 3;
 			y -= gp.tileSize * 1.75;
	  		g2.drawImage(dialogue_next, x, y + 25, null);
  		}
  		
  		if (skip) skipDialogue();
	}
	private void skipDialogue() {		
		
		if (gp.keyH.aPressed && canSkip) {
			gp.keyH.aPressed = false;		
			gp.keyH.playCursorSE();
			
			canSkip = false;
			charIndex = 0;
			combinedText = "";
			currentDialogue = "";
			
			npc.dialogueIndex++;
		}
	}
	
	// HEAL SCREEN
	private void drawHealScreen() {		
		switch (subState) {
			case 0: drawHeal_Dialogue(); break;
		}		
	}
	private void drawHeal_Dialogue() {
		
		int x = (int) (gp.tileSize * 2);
		int y = gp.tileSize * 9;
		int width =(int) (gp.tileSize * 12);
		int height = (int) (gp.tileSize * 2.5);
		drawSubWindow(x, y, width, height, 25, 10, battle_white, dialogue_blue);		
		
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 48F));
		x += gp.tileSize * 0.6;
		y += gp.tileSize * 1.1;		
		String text = "Would you like to heal your\nPokemon team?";
		for (String line : text.split("\n")) {   			
  			drawText(line, x, y, Color.BLACK, Color.LIGHT_GRAY);
  			y += 40;
		} 
		
		x = (int) (gp.tileSize * 11.7);
		y = (int) (gp.tileSize * 6.3);
		width = (int) (gp.tileSize * 2.3);
		height = (int) (gp.tileSize * 2.5);
		drawSubWindow(x, y, width, height, 25, 10, battle_white, dialogue_blue);
				
		x += gp.tileSize * 0.8;
		y += gp.tileSize + 5;
		drawText("YES", x, y, Color.BLACK, Color.LIGHT_GRAY);
		if (commandNum == 0) {
			drawText(">", x-20, y, Color.BLACK, Color.LIGHT_GRAY);	
			if (gp.keyH.aPressed) {		
				gp.keyH.aPressed = false;
				commandNum = 0;
				
				if (gp.player.healPokemonParty()) {
					npc.dialogueSet = 1;
				}
				else {
					npc.dialogueSet = 2;	
				}
				
				gp.gameState = gp.dialogueState;
			}
		}		
		
		y += gp.tileSize;
		drawText("NO", x, y, Color.BLACK, Color.LIGHT_GRAY);
		if (commandNum == 1) {
			drawText(">", x-20, y, Color.BLACK, Color.LIGHT_GRAY);	
			if (gp.keyH.aPressed) {		
				gp.keyH.aPressed = false;
				commandNum = 0;
				
				npc.dialogueSet = 3;
				gp.gameState = gp.dialogueState;
			}
		}		
	}
	
	// HM SCREEN
	private void drawHMScreen() {
		
		int x = (int) (gp.tileSize * 2);
		int y = gp.tileSize * 9;
		int width =(int) (gp.tileSize * 12);
		int height = (int) (gp.tileSize * 2.5);
		drawSubWindow(x, y, width, height, 25, 10, battle_white, dialogue_blue);		
		
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 48F));
		x += gp.tileSize * 0.6;
		y += gp.tileSize * 1.1;		
		String text = "Would you like to use " + npc.hmType + "?";
		drawText(text, x, y, Color.BLACK, Color.LIGHT_GRAY);
		
		x = (int) (gp.tileSize * 11.7);
		y = (int) (gp.tileSize * 6.3);
		width = (int) (gp.tileSize * 2.3);
		height = (int) (gp.tileSize * 2.5);
		drawSubWindow(x, y, width, height, 25, 10, battle_white, dialogue_blue);
				
		x += gp.tileSize * 0.8;						
		y += gp.tileSize + 5;
		drawText("YES", x, y, Color.BLACK, Color.LIGHT_GRAY);
		if (commandNum == 0) {
			drawText(">", x-20, y, Color.BLACK, Color.LIGHT_GRAY);	
			if (gp.keyH.aPressed) {		
				gp.keyH.aPressed = false;
				commandNum = 0;
				
				npc.useHM();
				gp.gameState = gp.playState;
			}
		}		
		
		y += gp.tileSize;
		drawText("NO", x, y, Color.BLACK, Color.LIGHT_GRAY);
		if (commandNum == 1) {
			drawText(">", x-20, y, Color.BLACK, Color.LIGHT_GRAY);	
			if (gp.keyH.aPressed) {		
				gp.keyH.aPressed = false;
				commandNum = 0;
				gp.gameState = gp.playState;
			}
		}		
	}		
			
	/** PAUSE SCREEN **/
	private void drawPauseScreen() {
		
		switch (pauseState) { 
		
			case pause_Main:
				pause_Main();
				break;
			case pause_Dex:
				pause_Dex();
				break;
			case pause_Party:
				pause_Party();
				break;
			case pause_Bag:
				pause_Bag();
				break;
			case pause_Options:
				pause_Options();
				break;			
		}
	}	
	private void pause_Main() {
		
		int x;
		int y;
		int width;
		int height;
		String text;		
		
		width = (int) (gp.tileSize * 4.5);
		height = gp.tileSize * 8;
		x = gp.screenWidth - width - 10;
		y = 10;		
		drawSubWindow(x, y, width, height, 5, 12, battle_white, dialogue_blue);
		
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 55f));
		x += gp.tileSize * 1.1;
		y += gp.tileSize * 1.1;
		text = "POKeDEX";
		drawText(text, x, y, Color.BLACK, Color.LIGHT_GRAY);		
		if (commandNum == 0) {
			drawText(">", x-20, y, Color.BLACK, Color.LIGHT_GRAY);		
			if (gp.keyH.aPressed) {
				gp.keyH.aPressed = false;
				gp.keyH.playCursorSE();
				
				pauseState = pause_Dex;
				bagNum = 0;
				
				commandNum = 0;
			}
		}
		
		y += gp.tileSize * 1.1;
		text = "PARTY";
		drawText(text, x, y, Color.BLACK, Color.LIGHT_GRAY);
		if (commandNum == 1) {
			drawText(">", x-20, y, Color.BLACK, Color.LIGHT_GRAY);	
			if (gp.keyH.aPressed) {				
				gp.keyH.aPressed = false;
				gp.keyH.playCursorSE();				
				
				pauseState = pause_Party;
				partyState = party_Main_Select;
				partyDialogue = "Choose a POKeMON.";
				
				commandNum = 0;
			}
		}
		
		y += gp.tileSize * 1.1;
		text = "BAG";
		drawText(text, x, y, Color.BLACK, Color.LIGHT_GRAY);
		if (commandNum == 2) {
			drawText(">", x-20, y, Color.BLACK, Color.LIGHT_GRAY);
			if (gp.keyH.aPressed) {
				gp.keyH.aPressed = false;
				gp.keyH.playCursorSE();
				
				pauseState = pause_Bag;
				bagState = bag_Main;
				bagTab = bag_KeyItems;
				bagNum = 0;
				
				commandNum = 0;
			}
		}
		
		y += gp.tileSize * 1.1;
		text = gp.player.name.toUpperCase();
		drawText(text, x, y, Color.BLACK, Color.LIGHT_GRAY);
		if (commandNum == 3) {
			drawText(">", x-20, y, Color.BLACK, Color.LIGHT_GRAY);	
			if (gp.keyH.aPressed) {
				gp.keyH.aPressed = false;
				gp.keyH.playCursorSE();
				commandNum = 0;
			}
		}
		
		y += gp.tileSize * 1.1;
		text = "SAVE";
		drawText(text, x, y, Color.BLACK, Color.LIGHT_GRAY);
		if (commandNum == 4) {
			drawText(">", x-20, y, Color.BLACK, Color.LIGHT_GRAY);	
			if (gp.keyH.aPressed) {
				gp.keyH.aPressed = false;
				gp.keyH.playCursorSE();
				commandNum = 0;
			}
		}
		
		y += gp.tileSize * 1.1;
		text = "OPTIONS";
		drawText(text, x, y, Color.BLACK, Color.LIGHT_GRAY);
		if (commandNum == 5) {
			drawText(">", x-20, y, Color.BLACK, Color.LIGHT_GRAY);	
			if (gp.keyH.aPressed) {
				gp.keyH.aPressed = false;
				gp.keyH.playCursorSE();
				pauseState = pause_Options;
				commandNum = 0;
			}
		}
		
		y += gp.tileSize * 1.1;
		text = "EXIT";
		drawText(text, x, y, Color.BLACK, Color.LIGHT_GRAY);
		if (commandNum == 6) {
			drawText(">", x-20, y, Color.BLACK, Color.LIGHT_GRAY);	
			if (gp.keyH.aPressed) {
				gp.keyH.aPressed = false;
				gp.keyH.playCursorSE();
				gp.gameState = gp.playState;
				commandNum = 0;
			}
		}
		
		if (gp.keyH.upPressed) {
			if (commandNum > 0) {
				gp.keyH.playCursorSE();
				commandNum--;
			}
			gp.keyH.upPressed = false;
		}
		if (gp.keyH.downPressed) {
			if (commandNum < 6) {
				gp.keyH.playCursorSE();
				commandNum++;
			}
			gp.keyH.downPressed = false;
		}
		
		if (gp.keyH.startPressed) {
			gp.keyH.startPressed = false;
			gp.keyH.playCloseSE();
			commandNum = 0;
			gp.gameState = gp.playState;			
		}
		
		if (gp.keyH.bPressed) {
			gp.keyH.bPressed = false;
			gp.keyH.playCloseSE();
			commandNum = 0;
			gp.gameState = gp.playState;
		}
	}
	
	/** DEX MENU **/
	private void pause_Dex() {
		
		g2.setColor(party_green);  
		g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);	
		
		int x;
		int y;
		int width;
		int height;
		int textX;
		int textY;
		int slotX;
		int slotY;
		int slotWidth;
		int slotHeight;
		String text;
		Pokedex p;
		
		x = gp.tileSize;
		y = (int) (gp.tileSize * 0.6); 
		width = (int) (gp.tileSize * 7.3);
		height = (int) (gp.tileSize * 1.1);
		g2.setColor(battle_white);
		g2.fillRoundRect(x, y, width, height, 15, 15);
	
		g2.setColor(Color.BLACK);
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 60f));
		text = "POKeDEX";
		x = getXForCenteredTextOnWidth(text, width, x);
		y += gp.tileSize * 0.95;	
		g2.drawString(text, x, y);
		
		x = gp.tileSize;
		y = (int) (gp.tileSize * 4);		
		height = (int) (gp.tileSize * 7.6);		
		g2.setColor(battle_white);
		g2.fillRoundRect(x, y, width, height, 15, 15);			
		g2.setColor(Color.BLACK);
		g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 15, 15);
		
		p = Pokedex.values()[bagNum];
		if (gp.player.personalDex.contains(p)) {
			
			x = (int) (gp.tileSize * 2.3);
			y = (int) (gp.tileSize * 4.8);	
			width = gp.tileSize * 2;
			height = gp.tileSize;
			g2.drawImage(p.getFrontSprite(), x, y, null);	
						
			y = (int) (gp.tileSize * 10.2);
			if (p.getTypes() == null) {
				x = (int) (gp.tileSize * 3.8);	
				drawSubWindow(x, y, width, height, 10, 3, p.getType().getColor(), Color.BLACK);											
				g2.setFont(g2.getFont().deriveFont(Font.BOLD, 30F));			
				text = p.getType().getName();
				textX = getXForCenteredTextOnWidth(text, width, x + 5);
				textY = (int) (y + (gp.tileSize * 0.75));			
				drawText(text, textX, textY, battle_white, Color.BLACK);
			}
			else {
				x = (int) (gp.tileSize * 2.4);	
				for (Type t : p.getTypes()) {					
					drawSubWindow(x, y, width, height, 10, 3, t.getColor(), Color.BLACK);											
					g2.setFont(g2.getFont().deriveFont(Font.BOLD, 30F));			
					text = t.getName();
					textX = getXForCenteredTextOnWidth(text, width, x + 5);
					textY = (int) (y + (gp.tileSize * 0.75));			
					drawText(text, textX, textY, battle_white, Color.BLACK);
					x += gp.tileSize * 2.7;
				}
			}				
		}
		
		x = (int) (gp.tileSize * 8.5);
		y = (int) (gp.tileSize * 0.6); 
		width = (int) (gp.tileSize * 7.3);
		height = (int) (gp.tileSize * 11);				
		drawSubWindow(x, y, width, height, 15, 5, pause_orange, Color.BLACK);
		
		x = (int) (gp.tileSize * 9.5);
		y = (int) (gp.tileSize * 1.9);
		slotX = (int) (gp.tileSize * 8.57);
		slotY = (int) (y - gp.tileSize * 0.8);
		slotWidth = (int) (gp.tileSize * 7.2);
		slotHeight = gp.tileSize;
		for (int i = bagStart; i < bagStart + 11; i++) {
			
			if (i < Pokedex.values().length) {
				
				p = Pokedex.values()[i];
				
				g2.setFont(g2.getFont().deriveFont(Font.BOLD, 35f));
				if (bagNum == i) {
					g2.setColor(battle_white);
					g2.fillRoundRect(slotX, slotY, slotWidth, slotHeight, 15, 15);
				}
				
				int index = p.getIndex();
				if (gp.player.pokeParty.stream().filter(o -> o.getIndex() == index).findFirst().isPresent()) {
					g2.drawImage(dex_ball, x - (int) (gp.tileSize * 0.8), y - (int) (gp.tileSize * 0.6), null);
				}
												
				drawText("No", x, y, Color.BLACK, Color.LIGHT_GRAY);
				
				g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 48f));
				drawText(p.getIndex() + "", x + (int) (gp.tileSize * 0.6), y, Color.BLACK, Color.LIGHT_GRAY);					
				if (gp.player.personalDex.contains(p)) {
					drawText(p.getName(), x + (int) (gp.tileSize * 2.2), y, Color.BLACK, Color.LIGHT_GRAY);	
				}
				else {					
					drawText("---------", x + (int) (gp.tileSize * 2.2), y, Color.BLACK, Color.LIGHT_GRAY);	
				}
				
				y += gp.tileSize * 0.88;
				slotY += gp.tileSize * 0.88;
			}
			else {
				break;
			}	
		}			
				
		if (0 < bagStart) {
			x = (int) (gp.tileSize * 12.5);
			y = (int) (gp.tileSize * 1.6);
			drawText("^", x, y, Color.BLACK, Color.LIGHT_GRAY);
		}
		
		if (bagStart + 11 < Pokedex.values().length) {
			x = (int) (gp.tileSize * 12.5);
			y = (int) (gp.tileSize * 11.4);
			drawText("v", x, y, Color.BLACK, Color.LIGHT_GRAY);
		}
						
		if (gp.keyH.upPressed) {
			gp.keyH.upPressed = false;
			gp.keyH.playCursorSE();
		
			if (bagNum > 0)	bagNum--;
			if (bagStart > 0) bagStart--;
		}
		if (gp.keyH.downPressed) {
			gp.keyH.downPressed = false;
			gp.keyH.playCursorSE();
			
			if (bagNum < Pokedex.values().length - 1) bagNum++;
			if (bagNum >= bagStart + 11) bagStart++;
		}					
		if (gp.keyH.bPressed) {
			gp.keyH.bPressed = false;
			gp.keyH.playCursorSE();		
			
			bagNum = 0;
			bagStart = 0;
			commandNum = 0;
			pauseState = pause_Main;			
		}
		
		if (gp.keyH.xPressed) {
			gp.keyH.xPressed = false;
			if (gp.player.personalDex.contains(p)) {
				gp.playSE(gp.cry_SE, p.toString());				
			}			
		}
	}
	
	/** BAG SCREEN **/
	private void pause_Bag() {
		
		ArrayList<Entity> items = new ArrayList<>();
		
		switch (bagTab) {
			case bag_KeyItems:
				bag_Subtitle = "KEY ITEMS";
				bag_Tab = bag_tab_1;
				bag_Image = bag_keyItems;		
				items = gp.player.inventory_keyItems;
				break;
			case bag_Items:
				bag_Subtitle = "ITEMS";
				bag_Tab = bag_tab_2;
				bag_Image = bag_items;		
				items = gp.player.inventory_items;
				break;
			case bag_Pokeballs:
				bag_Subtitle = "POKe BALLS";
				bag_Tab = bag_tab_3;
				bag_Image = bag_pokeballs;		
				items = gp.player.inventory_pokeballs;	
				break;
			case bag_Moves:
				bag_Subtitle = "TMs & HMs";
				bag_Tab = bag_tab_4;
				bag_Image = bag_moves;		
				items = gp.player.inventory_moves;		
				break;
		}

		switch (bagState) {
			case bag_Main:
				bag_Main(items);
				break;
			case bag_Options:
				bag_Options(items);
				break;
			case bag_Dialogue:
				bag_Dialogue(items);
				break;
		}		
	}	
		
	private void bag_Main(ArrayList<Entity> items) {
		
		int x;
		int y;
		String text;
				
		bag_HUD(items);
		
		if (items.size() > 0) {

			g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 48f));
			x = (int) (gp.tileSize * 0.5);
			y = (int) (gp.tileSize * 8.6);
			text = items.get(bagNum).description;
			
			for (String line : text.split("\n")) {			
				drawText(line, x, y, Color.BLACK, Color.LIGHT_GRAY);
				y += gp.tileSize;
			} 	
		}
		
		if (gp.keyH.upPressed) {
			gp.keyH.upPressed = false;
			gp.keyH.playCursorSE();
		
			if (bagNum > 0)	bagNum--;
			if (bagStart > 0) bagStart--;
		}
		if (gp.keyH.downPressed) {
			gp.keyH.downPressed = false;
			gp.keyH.playCursorSE();
			
			if (bagNum < items.size() - 1) bagNum++;
			if (bagNum >= bagStart + 9) bagStart++;
		}				
		if (gp.keyH.leftPressed) {
			gp.keyH.leftPressed = false;
			
			if (0 < bagTab) {
				gp.keyH.playCursorSE();
				bagTab--;
				bagStart = 0;
				bagNum = 0;
			}
		}
		if (gp.keyH.rightPressed) {	
			gp.keyH.rightPressed = false;
			
			if (bagTab < 3) {
				gp.keyH.playCursorSE();
				bagTab++;
				bagStart = 0;
				bagNum = 0;
			}
		}
		
		if (gp.keyH.aPressed) {
			gp.keyH.aPressed = false;		
			gp.keyH.playCursorSE();
			
			if (partyState == party_Main_Options_Item) {
				
				items.get(bagNum).giveItem(items.get(bagNum), gp.player.pokeParty.get(fighterNum));	
				
				bagNum = 0;
				bagTab = 0;
				bagStart = 0;
				
				pauseState = pause_Party;
				
				commandNum = 0;
			}
			else {
				bagState = bag_Options;	
			}			
		}		
		if (gp.keyH.bPressed) {
			gp.keyH.bPressed = false;
			gp.keyH.playCursorSE();
			
			if (gp.btlManager.active) {
				bagTab = 0;
				bagStart = 0;
				bagNum = 0;
				commandNum = 1;
				gp.gameState = gp.battleState;		
			}
			else if (partyState == party_Main_Options_Item) {
				
				bagNum = 0;
				bagTab = 0;
				bagStart = 0;
				
				pauseState = pause_Party;
				partyState = party_Main_Select;
				partyDialogue = "Choose a POKeMON.";
				
				commandNum = 0;
			}
			else {
				bagTab = 0;
				bagStart = 0;
				bagNum = 0;
				commandNum = 2;
				pauseState = pause_Main;
			}
		}
	}	
	private void bag_Options(ArrayList<Entity> items) {
		
		int x;
		int y;
		int width;
		int height;
		String text;
				
		bag_HUD(items);
						
		x = (int) (gp.tileSize * 0.25);
		y = (int) (gp.tileSize * 4.4);
		width = (int) (gp.tileSize * 6.9);
		height = (int) (gp.tileSize * 3);		
		drawSubWindow(x, y, width, height, 5, 8, battle_white, dialogue_blue);
		
		x += gp.tileSize * 0.5;
		y += gp.tileSize * 1.3;
		height = gp.tileSize;
		
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 65f));	
		g2.setColor(Color.BLACK);
		g2.setStroke(new BasicStroke(4));	
		
		text = "USE";
		g2.drawString(text, x, y);
		if (commandNum == 0) {
			width = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
			g2.setColor(battle_red);
			g2.drawRect(x - 4, y - (int) (gp.tileSize * 0.95), width + 4, height + 2);
			g2.setColor(Color.BLACK);
			
			if (gp.keyH.aPressed) {
				gp.keyH.aPressed = false;
				gp.keyH.playCursorSE();	
				items.get(bagNum).use();
			}
		}

		x += gp.tileSize * 3;
		text = "GIVE";
		g2.drawString(text, x, y);
		if (commandNum == 1) {
			width = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
			g2.setColor(battle_red);
			g2.drawRect(x - 4, y - (int) (gp.tileSize * 0.95), width + 4, height + 2);
			g2.setColor(Color.BLACK);
			
			if (gp.keyH.aPressed) {
				gp.keyH.aPressed = false;
				gp.keyH.playCursorSE();				
				
				partyItem = items.get(bagNum);
				partyItemGive = true;
				
				partyDialogue = "Give to which POKEMON?";				
				partyState = party_Main_Select;
				pauseState = pause_Party;
				
				commandNum = 0;					
			}
		}
		
		x = (int) (gp.tileSize * 0.75);
		y += gp.tileSize * 1.4;
		text = "TOSS";
		g2.drawString(text, x, y);
		if (commandNum == 2) {
			width = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
			g2.setColor(battle_red);
			g2.drawRect(x - 4, y - (int) (gp.tileSize * 0.95), width + 4, height + 2);
			g2.setColor(Color.BLACK);
			
			if (gp.keyH.aPressed) {
				gp.keyH.aPressed = false;
				gp.keyH.playCursorSE();			
				
				if (items.get(bagNum).collectableType == items.get(bagNum).type_keyItem) {
					bagDialogue = "You shouldn't toss this\nitem!";
					bagState = bag_Dialogue;
				}
				else {				
					gp.player.removeItem(items.get(bagNum), gp.player);	
					bagState = bag_Main;
					commandNum = 0;			
				}	
			}
		}
		
		x += gp.tileSize * 3;
		text = "CANCEL";
		g2.drawString(text, x, y);
		if (commandNum == 3) {
			width = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
			g2.setColor(battle_red);
			g2.drawRect(x - 4, y - (int) (gp.tileSize * 0.95), width + 4, height + 2);
			g2.setColor(Color.BLACK);
			
			if (gp.keyH.aPressed) {
				gp.keyH.aPressed = false;
				gp.keyH.playCursorSE();
				bagState = bag_Main;					
				commandNum = 0;				
			}
		}
		
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 58f));
		x = (int) (gp.tileSize * 0.5);
		y = (int) (gp.tileSize * 8.6);
		text = "What would you\nlike to do?";
		
		for (String line : text.split("\n")) {			
			drawText(line, x, y, Color.BLACK, Color.LIGHT_GRAY);
			y += gp.tileSize;
		} 				
		
		if (gp.keyH.upPressed) {	
			gp.keyH.upPressed = false;
			if (commandNum > 1) {
				gp.keyH.playCursorSE();		
				commandNum -= 2;
			}
		}
		if (gp.keyH.downPressed) {	
			gp.keyH.downPressed = false;
			if (commandNum < 2) {
				gp.keyH.playCursorSE();		
				commandNum += 2;
			}
		}
		if (gp.keyH.leftPressed) {	
			gp.keyH.leftPressed = false;
			if (0 < commandNum) {
				gp.keyH.playCursorSE();		
				commandNum--;
			}
		}
		if (gp.keyH.rightPressed) {	
			gp.keyH.rightPressed = false;
			if (commandNum < 3) {
				gp.keyH.playCursorSE();		
				commandNum++;
			}
		}		
		if (gp.keyH.bPressed) {
			gp.keyH.bPressed = false;
			gp.keyH.playCursorSE();		
			bagState = bag_Main;					
			commandNum = 0;		
		}
	}	
	private void bag_Dialogue(ArrayList<Entity> items) {
		
		int x;
		int y;
		int width;
		int height;
				
		bag_HUD(items);
						
		x = (int) (gp.tileSize * 2.2);
		y = (int) (gp.tileSize * 7.85);
		width = (int) (gp.tileSize * 12);
		height = (int) (gp.tileSize * 3.5);		
		drawSubWindow(x, y, width, height, 5, 12, battle_white, dialogue_blue);
		
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 58f));	
		x += gp.tileSize * 0.5;
		y += gp.tileSize * 1.2;
		height = gp.tileSize;		
		
  		for (String line : bagDialogue.split("\n")) {   			
  			drawText(line, x, y, Color.BLACK, Color.LIGHT_GRAY);
  			y += gp.tileSize;
		} 
		
		if (gp.keyH.aPressed) {	
			gp.keyH.aPressed = false;
			gp.keyH.playCursorSE();		
			
			bagState = bag_Main;
			commandNum = 0;
		}
	}	
	private void bag_HUD(ArrayList<Entity> items) {
		
		int x;
		int y;
		int width;
		int height;
		int slotX;
		int slotY;		
		String text;
		
		g2.drawImage(bag_menu, 0, 0, null);
		
		x = (int) (gp.tileSize * 1.8);
		y = gp.tileSize / 2;
		g2.drawImage(bag_Image, x, y, null);		
		
		x = (int) (gp.tileSize * 2.7);
		y = (int) (gp.tileSize * 5.6);
		g2.drawImage(bag_Tab, x, y, null);

		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 65f));
		x = (int) (gp.tileSize * 1.7);
		x = getXForCenteredTextOnWidth(bag_Subtitle, x, (int) (gp.tileSize * 3.5));
		y = gp.tileSize * 7;
		drawText(bag_Subtitle, x, y, Color.BLACK, Color.GRAY);
		
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 55f));			
		x = (int) (gp.tileSize * 7.8);		
		y = (int) (gp.tileSize * 2.1);		
		slotX = (int) (x - gp.tileSize * 0.3);
		slotY = (int) (y - gp.tileSize * 0.9);
		width = gp.tileSize * 8;
		height = (int) (gp.tileSize * 1.1);
								
		for (int i = bagStart; i < bagStart + 9; i++) {
								
			if (i < items.size()) {
												
				text = items.get(i).name;
				drawText(text, x, y, Color.BLACK, Color.LIGHT_GRAY);	
				
				x += gp.tileSize * 6.3;
				text = "x" + String.format("%02d", items.get(i).amount);
				drawText(text, x, y, Color.BLACK, Color.LIGHT_GRAY);	
				
				if (bagNum == i) {
					g2.setColor(battle_red);
					g2.setStroke(new BasicStroke(4));				
					g2.drawRoundRect(slotX, slotY, width, height, 6, 6);
				}
				
				x = (int) (gp.tileSize * 7.8);	
				y += gp.tileSize * 1.08;
				slotY += gp.tileSize * 1.08;		
			}
			else {
				break;
			}	
		}			
				
		if (0 < bagStart) {
			x = (int) (gp.tileSize * 11.4);
			y = (int) (gp.tileSize * 1.5);
			drawText("^", x, y, Color.BLACK, Color.LIGHT_GRAY);
		}
		
		if (bagStart + 9 < items.size()) {
			x = (int) (gp.tileSize * 11.4);
			y = (int) (gp.tileSize * 11.4);
			drawText("v", x, y, Color.BLACK, Color.LIGHT_GRAY);
		}
	}
	/** END BAG SCREEN **/
	
	/** PARTY SCREEN **/
	private void pause_Party() {
		
		switch (partyState) {
		
			case party_Main_Select:
				party_Main_Select();				
				break;
			case party_Main_Options:
				party_Main_Options();
				break;
			case party_Main_Options_Item:
				party_Main_Options_Item();
				break;
			case party_Main_Dialogue:
				party_Main_Dialogue();
				break;
			case party_Skills:				
				party_Skills();
				party_Header(0);
				break;
			case party_Moves:
				party_Moves();				
				party_Header(1);
				break;
			case party_MoveSwap:
				party_MoveSwap();
				break;
		}
	}		
	
	private void party_Main_Select() {
		
		party_Main();
		
		int x;
		int y;
		int width;
		int height;
		String text;
		
		x = (int) (gp.tileSize * 12);
		y = (int) (gp.tileSize * 10.62);
		width = (int) (gp.tileSize * 3.5);
		height = (int) (gp.tileSize * 1);	
				
		if (fighterNum == 6) drawSubWindow(x, y, width, height, 5, 5, party_blue, party_red); 			
		else drawSubWindow(x, y, width, height, 5, 3, party_blue, Color.BLACK); 		
		
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 45F));
		x += gp.tileSize * 0.8;
		y += gp.tileSize * 0.8;
		text = "CANCEL";
		drawText(text, x, y, battle_white, Color.BLACK);
		
		x = (int) (gp.tileSize * 0.3);
		y = (int) (gp.tileSize * 10);
		width = gp.tileSize * 11;
		height = (int) (gp.tileSize * 1.6);
		drawSubWindow(x, y, width, height, 3, 5, battle_white, Color.BLACK);	
		
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 57F));
		x += gp.tileSize * 0.3;
		y += gp.tileSize * 1.15;
		drawText(partyDialogue, x, y, Color.BLACK, Color.LIGHT_GRAY);	
		
		if (gp.keyH.upPressed) {				
			gp.keyH.upPressed = false;
			
			if (fighterNum == 6) {
				gp.keyH.playCursorSE();
				fighterNum = gp.player.pokeParty.size() - 1;
			}
			else if (fighterNum > 0) {
				gp.keyH.playCursorSE();
				fighterNum--;
			}
		}
		if (gp.keyH.downPressed) {				
			gp.keyH.downPressed = false;
			if (fighterNum < gp.player.pokeParty.size() - 1) {
				gp.keyH.playCursorSE();
				fighterNum++;
			}
			else {
				gp.keyH.playCursorSE();
				fighterNum = 6;
			}
		}
		if (gp.keyH.leftPressed) {
			gp.keyH.leftPressed = false;
			gp.keyH.playCursorSE();
			fighterNum = 0;				
		}
		if (gp.keyH.rightPressed) {				
			gp.keyH.rightPressed = false;	
			if (fighterNum == 0 && gp.player.pokeParty.size() > 1) {
				gp.keyH.playCursorSE();
				fighterNum = 1;				
			}
			else if (fighterNum == 0) {
				gp.keyH.playCursorSE();
				fighterNum = 6;
			}
		}
		
		if (gp.keyH.aPressed) {
			gp.keyH.aPressed = false;
			
			if (fighterNum == 6) {
				party_Main_Close();
			}
			else {
				if (partyMove) {
					gp.keyH.playCursorSE();
					
					if (partyMoveNum != fighterNum) {
						Collections.swap(gp.player.pokeParty, fighterNum, partyMoveNum);	
					}	
					
					partyMove = false;
					partyMoveNum = -1;
					partyDialogue = "Choose a POKEMON.";
				}
				else if (partyItem != null) {
					
					Pokemon p = gp.player.pokeParty.get(fighterNum);
					
					if (partyItemApply) {
						partyItem.apply(partyItem, p);	
					}
					else if (partyItemGive) {
						partyItem.giveItem(partyItem, p);		
					}					
				}
				else {
					gp.keyH.playCursorSE();	
					commandNum = 0;
					partyState = party_Main_Options;			
				}						
			}
		}
		
		if (gp.keyH.bPressed) {			
			gp.keyH.bPressed = false;
			
			if (fighterNum == 6) {
				party_Main_Close();			
			}
			else {
				gp.keyH.playCursorSE();
				fighterNum = 6;				
			}
		}
	}
	private void party_Main_Close() {
		
		if (partyMove) {				
			gp.keyH.playCursorSE();
			
			partyMove = false;	
			partyMoveNum = -1;
			partyDialogue = "Choose a POKEMON.";
		}
		else if (partyItem != null) {
			gp.keyH.playCursorSE();		
			partyItem = null;
			partyItemGive = false;
			partyItemApply = false;
			fighterNum = 0;	
			commandNum = 0;
			bagNum = 0;
			bagState = bag_Main;
			pauseState = pause_Bag;
		}
		else {
			if (gp.btlManager.active) {
				
				if (battleState == battle_Options) {
					gp.keyH.playCursorSE();
					
					fighterNum = 0;	
					commandNum = 2;
					gp.gameState = gp.battleState;								
				}
				else if (!gp.btlManager.fighter[0].isAlive()) {							
					gp.keyH.playErrorSE();
				}
				else {						
					gp.keyH.playCursorSE();
					
					fighterNum = 0;	
					commandNum = 0;
					gp.btlManager.running = true;
					new Thread(gp.btlManager).start();	
					gp.gameState = gp.battleState;		
				}					
				
				pauseState = pause_Main;
			}
			else {
				gp.keyH.playCursorSE();
				
				fighterNum = 0;	
				commandNum = 1;
				pauseState = pause_Main;
			}
		}		
	}
	private void party_Main_Options() {
		
		partyDialogue = "Do what with " + gp.player.pokeParty.get(fighterNum).getName() + "?";
		party_Main();
		
		int x;
		int y;
		int width;
		int height;
		String text;
		
		x = (int) (gp.tileSize * 0.3);
		y = (int) (gp.tileSize * 10);
		width = gp.tileSize * 11;
		height = (int) (gp.tileSize * 1.6);
		drawSubWindow(x, y, width, height, 3, 5, battle_white, Color.BLACK);	
		
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 57F));
		x += gp.tileSize * 0.3;
		y += gp.tileSize * 1.15;
		drawText(partyDialogue, x, y, Color.BLACK, Color.LIGHT_GRAY);	
		
		x = (int) (gp.tileSize * 12.35);
		y = (int) (gp.tileSize * 7.95);
		width = (int) (gp.tileSize * 2.8);
		height = (int) (gp.tileSize * 3.72);		
		drawSubWindow(x, y, width, height, 4, 4, battle_white, Color.BLACK);
		
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 40F));	
		x += gp.tileSize * 0.3;
		y += gp.tileSize * 0.72;
		text = "SUMMARY";
		drawText(text, x, y, Color.BLACK, Color.LIGHT_GRAY);
		
		y += gp.tileSize * 0.95;
		if (gp.btlManager.active) text = "SELECT";
		else text = "SWITCH";		
		drawText(text, x, y, Color.BLACK, Color.LIGHT_GRAY);
		
		y += gp.tileSize * 0.95;
		text = "ITEM";		
		drawText(text, x, y, Color.BLACK, Color.LIGHT_GRAY);
		
		y += gp.tileSize * 0.95;
		text = "CANCEL";		
		drawText(text, x, y, Color.BLACK, Color.LIGHT_GRAY);
		
		x -= gp.tileSize * 0.28;
		y -= gp.tileSize * 3.52;
		height = (int) (gp.tileSize * 0.9);	
		
		// SUMMARY
		if (commandNum == 0) {
			g2.setColor(battle_red);
			g2.drawRoundRect(x, y, width, height, 4, 4);
			
			if (gp.keyH.aPressed) {
				gp.keyH.aPressed = false;					
				gp.keyH.playCursorSE();
				partyState = party_Skills;				
				commandNum = 0;			
			}
		}
		
		// SWITCH / SELECT
		y += gp.tileSize * 0.95;
		if (commandNum == 1) {			
			g2.setColor(battle_red);
			g2.drawRoundRect(x, y, width, height, 4, 4);
			
			if (gp.keyH.aPressed) {
				gp.keyH.aPressed = false;
				
				if (gp.btlManager.active) {
					
					fighter_one_Y = fighter_one_startY;
					fighter_two_Y = fighter_two_startY;	
					
					// NEW FIGHTER SELECTED
					if (gp.btlManager.swapPokemon(fighterNum)) {					
						gp.keyH.playCursorSE();	
						
						commandNum = 0;
						fighterNum = 0;	
						partyState = party_Main_Select;
						pauseState = pause_Main;
						
						gp.btlManager.running = true;
						gp.btlManager.fightStage = gp.btlManager.fight_Swap;
						new Thread(gp.btlManager).start();
						
						gp.gameState = gp.battleState;
					}						
					// UNABLE TO SELECT FIGHTER
					else {
						gp.keyH.playErrorSE();
					}				
				}
				else {					
					gp.keyH.playCursorSE();
					
					partyDialogue = "Move to where?";
					partyMoveNum = fighterNum;
					partyMove = true;
					partyState = party_Main_Select;
				}	
			}
		}
		
		// ITEM
		y += gp.tileSize * 0.95;
		if (commandNum == 2) {
			g2.setColor(battle_red);
			g2.drawRoundRect(x, y, width, height, 4, 4);
			
			if (gp.keyH.aPressed) {
				gp.keyH.aPressed = false;	
				gp.keyH.playCursorSE();
				partyState = party_Main_Options_Item;				
				commandNum = 0;			
			}
		}
		
		// CANCEL
		y += gp.tileSize * 0.95;
		if (commandNum == 3) {
			g2.setColor(battle_red);
			g2.drawRoundRect(x, y, width, height, 4, 4);
			
			if (gp.keyH.aPressed) {
				gp.keyH.playCursorSE();
				gp.keyH.aPressed = false;		
				partyDialogue = "Choose a POKEMON.";
				partyState = party_Main_Select;	
				commandNum = 0;	
			}
		}
		
		if (gp.keyH.upPressed) {				
			gp.keyH.upPressed = false;
			if (commandNum > 0) {
				gp.keyH.playCursorSE();
				commandNum--;
			}
		}
		if (gp.keyH.downPressed) {				
			gp.keyH.downPressed = false;
			if (commandNum < 3) {
				gp.keyH.playCursorSE();
				commandNum++;
			}
		}
		
		if (gp.keyH.bPressed) {		
			gp.keyH.playCursorSE();
			gp.keyH.bPressed = false;
			partyDialogue = "Choose a POKEMON.";
			partyState = party_Main_Select;	
			commandNum = 0;	
		}
	}
	private void party_Main_Options_Item() {
		
		partyDialogue = "Do what with " + gp.player.pokeParty.get(fighterNum).getName() + "?";
		party_Main();
		
		int x;
		int y;
		int width;
		int height;
		String text;
		
		x = (int) (gp.tileSize * 0.3);
		y = (int) (gp.tileSize * 10);
		width = gp.tileSize * 11;
		height = (int) (gp.tileSize * 1.6);
		drawSubWindow(x, y, width, height, 3, 5, battle_white, Color.BLACK);	
		
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 57F));
		x += gp.tileSize * 0.3;
		y += gp.tileSize * 1.15;
		drawText(partyDialogue, x, y, Color.BLACK, Color.LIGHT_GRAY);	
		
		x = (int) (gp.tileSize * 12.35);
		y = (int) (gp.tileSize * 8.85);
		width = (int) (gp.tileSize * 2.8);
		height = (int) (gp.tileSize * 2.78);		
		drawSubWindow(x, y, width, height, 4, 4, battle_white, Color.BLACK);
		
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 40F));	
		x += gp.tileSize * 0.3;
		y += gp.tileSize * 0.72;
		text = "GIVE";
		drawText(text, x, y, Color.BLACK, Color.LIGHT_GRAY);
		
		y += gp.tileSize * 0.95;
		text = "TAKE";		
		drawText(text, x, y, Color.BLACK, Color.LIGHT_GRAY);
		
		y += gp.tileSize * 0.95;
		text = "CANCEL";		
		drawText(text, x, y, Color.BLACK, Color.LIGHT_GRAY);
		
		x -= gp.tileSize * 0.28;
		y -= gp.tileSize * 2.58;
		height = (int) (gp.tileSize * 0.9);	
		
		// GIVE ITEM
		if (commandNum == 0) {
			g2.setColor(battle_red);
			g2.drawRoundRect(x, y, width, height, 4, 4);
			
			if (gp.keyH.aPressed) {
				gp.keyH.aPressed = false;					
				
				if (gp.player.pokeParty.get(fighterNum).getHeldItem() == null) {
					gp.keyH.playCursorSE();
					pauseState = pause_Bag;
					bagState = bag_Main;
					bagTab = bag_KeyItems;
					commandNum = 0;
				}
				else {
					gp.keyH.playErrorSE();
				}		
			}
		}
		
		// TAKE ITEM
		y += gp.tileSize * 0.95;
		if (commandNum == 1) {			
			g2.setColor(battle_red);
			g2.drawRoundRect(x, y, width, height, 4, 4);
			
			if (gp.keyH.aPressed) {
				gp.keyH.aPressed = false;		
				
				Pokemon p = gp.player.pokeParty.get(fighterNum);
				Entity item = p.getHeldItem();
				
				if (item != null) {
					gp.keyH.playCursorSE();					
					gp.player.addItem(item, gp.player);
					p.giveItem(null);
					partyDialogue = "Choose a POKEMON.";
					partyState = party_Main_Select;	
					commandNum = 0;
				}
				else {
					gp.keyH.playErrorSE();
				}		
			}
		}
		
		// CANCEL
		y += gp.tileSize * 0.95;
		if (commandNum == 2) {
			g2.setColor(battle_red);
			g2.drawRoundRect(x, y, width, height, 4, 4);
			
			if (gp.keyH.aPressed) {
				gp.keyH.playCursorSE();
				gp.keyH.aPressed = false;		
				partyDialogue = "Choose a POKEMON.";
				partyState = party_Main_Select;	
				commandNum = 0;	
			}
		}
		
		if (gp.keyH.upPressed) {				
			gp.keyH.upPressed = false;
			if (commandNum > 0) {
				gp.keyH.playCursorSE();
				commandNum--;
			}
		}
		if (gp.keyH.downPressed) {				
			gp.keyH.downPressed = false;
			if (commandNum < 2) {
				gp.keyH.playCursorSE();
				commandNum++;
			}
		}
		
		if (gp.keyH.bPressed) {		
			gp.keyH.playCursorSE();
			gp.keyH.bPressed = false;
			partyDialogue = "Choose a POKEMON.";
			partyState = party_Main_Select;	
			commandNum = 0;	
		}
	}
	private void party_Main_Dialogue() {
		
		party_Main();
		
		int x = (int) (gp.tileSize * 2);
		int y = gp.tileSize * 9;
		int width =(int) (gp.tileSize * 12);
		int height = (int) (gp.tileSize * 2.5);
		drawSubWindow(x, y, width, height, 25, 10, battle_white, dialogue_blue);
		
		x += gp.tileSize * 0.4;
		y += gp.tileSize * 1.1;	
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 48F));
  		for (String line : partyDialogue.split("\n")) {   			
  			drawText(line, x, y, Color.BLACK, Color.LIGHT_GRAY);
  			y += 40;
		} 
  		
  		if (gp.keyH.aPressed) {
  			gp.keyH.aPressed = false;
  			gp.keyH.playCursorSE();
  			
  			if (partyItem == null) {
  				partyDialogue = "Choose a POKEMON.";
  				partyState = party_Main_Select;
  			}
  			else {  				
  				partyItem = null;
  				partyItemApply = false;
  				partyItemGive = false;
  				
  				fighterNum = 0;	
  				commandNum = 0;		
  				
  				if (gp.btlManager.active) {  					
					gp.btlManager.running = true;
					gp.btlManager.fightStage = gp.btlManager.fight_Start;
					new Thread(gp.btlManager).start();
					
					bagState = bag_Main;
					battleState = battle_Dialogue;
					gp.gameState = gp.battleState;
  				}
  				else {
  					bagState = bag_Main;
  					pauseState = pause_Bag;
  				}  				
  			}  			
  		}
	}
	
	private void party_Main() {
		
		g2.setColor(party_green);  
		g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
				
		int x;
		int y;
		int width;
		int height;
		Pokemon fighter;
		Color boxColor;
		
		x = (int) (gp.tileSize * 0.5);
		y = (int) (gp.tileSize * 2.8); 
		width = (int) (gp.tileSize * 5.7);
		height = (int) (gp.tileSize * 3.2);
		fighter = gp.player.pokeParty.get(0);
		
		if (fighter.isAlive()) { boxColor = party_blue; }
		else { boxColor = party_faint; }
		
		if (partyMove && partyMoveNum == 0) { drawSubWindow(x, y, width, height, 3, 3, boxColor, hp_yellow); }
		else if (fighterNum == 0) { drawSubWindow(x, y, width, height, 3, 5, boxColor, party_red); }		
		else { drawSubWindow(x, y, width, height, 3, 3, boxColor, Color.BLACK); }
		
		party_Box((int) (x + gp.tileSize * 0.15), (int) (y - gp.tileSize * 0.15), fighter, true);
		
		x = (int) (gp.tileSize * 6.5);
		y = (int) (gp.tileSize * 0.5); 
		width = (int) (gp.tileSize * 9.2);
		height = (int) (gp.tileSize * 1.7);
	
		for (int i = 1; i < 6; i++) {	
			
			if (gp.player.pokeParty.size() > i) {		
				
				fighter = gp.player.pokeParty.get(i);
				
				if (fighter.isAlive()) { boxColor = party_blue; }
				else { boxColor = party_faint; }
				
				if (partyMove && partyMoveNum == i) { drawSubWindow(x, y, width, height, 3, 3, boxColor, hp_yellow); }
				else if (fighterNum == i) { drawSubWindow(x, y, width, height, 3, 5, boxColor, party_red); }
				else { drawSubWindow(x, y, width, height, 3, 3, boxColor, Color.BLACK); }			
				
				party_Box((int) (x + gp.tileSize * 0.15), (int) (y - gp.tileSize * 0.15), fighter, false);				
			}
			else {
				if (fighterNum == i) { drawSubWindow(x, y, width, height, 3, 5, party_blue, party_red); }
				else { drawSubWindow(x, y, width, height, 3, 3, party_blue, Color.BLACK); }
			}
			
			y += gp.tileSize * 1.9;
		}
	}
	private void party_Box(int x, int y, Pokemon fighter, boolean main) {
		
		g2.drawImage(fighter.getMenuSprite(), x, y, null);
		
		if (fighter.getHeldItem() != null) {			
			g2.drawImage(fighter.getHeldItem().image1, x + gp.tileSize, y + (int) (gp.tileSize * 1.2), null);
		}
		
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 35F));	
		x += gp.tileSize * 2.1;
		y += gp.tileSize;
		String text = fighter.getName();
		drawText(text, x, y, battle_white, Color.BLACK);
	
		x += gp.tileSize * 0.9;
		y += gp.tileSize * 0.55;
		text = "Lv" + fighter.getLevel();			
		drawText(text, x, y, battle_white, Color.BLACK);
		
		if (main) {
			x = (int) (gp.tileSize * 1.7);
			y = (int) (gp.tileSize * 4.8); 
		}
		else {
			x += gp.tileSize * 1.65;
			y -= gp.tileSize;	
		}
		
		party_Box_HP(x, y, fighter);
	}
	private void party_Box_HP(int x, int y, Pokemon fighter) {
		
		int width = (int) (gp.tileSize * 4.2);
		int height = (int) (gp.tileSize * 0.5);
		
		g2.setColor(Color.BLACK);
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 25F));
		g2.setStroke(new BasicStroke(2));
		g2.fillRoundRect(x, y, width, height, 10, 10);
		
		g2.setColor(Color.WHITE);	
		x += (int) gp.tileSize * 0.25;
		y += gp.tileSize * 0.42;
		g2.drawString("HP", x, y);		
		
		x += gp.tileSize * 0.55;
		y -= gp.tileSize * 0.3;
		width = (int) (gp.tileSize * 3.3);
		height = (int) (gp.tileSize * 0.28);
		g2.setColor(Color.WHITE);
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 30F));
		g2.setStroke(new BasicStroke(2));
		g2.fillRoundRect(x, y, width, height, 3, 3);
		
		double remainHP = (double)fighter.getHP() / (double)fighter.getBHP();	
		
		if (remainHP >= .50) g2.setColor(hp_green);
		else if (remainHP >= .25) g2.setColor(hp_yellow);
		else g2.setColor(hp_red);
				
		width *= remainHP;	
										
		g2.fillRoundRect(x, y, width, height, 3, 3);			
		
		if (fighter.getStatus() != null) {
			battle_Status((int) (x - (gp.tileSize * 0.6)), (int) (y + (gp.tileSize * 0.45)), fighter, 30F);
			g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 30F));
		}
		
		g2.setColor(Color.WHITE);
		String text = fighter.getHP() + " / " + fighter.getBHP();
		x = getXforRightAlignText(text, (int) (x + gp.tileSize * 3.15));		
		y += gp.tileSize * 0.9;
		g2.drawString(text, x, y);		
	}
	
	private void party_Skills() {
		
		party_Info();
		
		int x;
		int y;
		int width;
		int height;
		String text;
		Pokemon fighter = null;

		if (gp.gameState == gp.pcState) fighter = gp.player.pcParty[boxTab][fighterNum];
		else fighter = gp.player.pokeParty.get(fighterNum);	
		
		// SKILLS BOX
		x = (int) (gp.tileSize * 7.5);
		y = (int) (gp.tileSize * 2.05);
		width = gp.tileSize * 9;
		height = (int) (gp.tileSize * 5.7);
		drawSubWindow(x, y, width, height, 4, 4, party_gray, Color.BLACK);	
		
		int frameX = x;
		int frameY = (int) (gp.tileSize * 2.9);
		
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 32F));
		g2.setColor(Color.BLACK);
		x = frameX + (int) (gp.tileSize * 4.4);
		y = (int) (frameY * 0.9);			
		text = "STAT"; g2.drawString(text, x, y); 
		x += gp.tileSize * 2.5;
		text = "IV"; g2.drawString(text, x, y);
			
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 40F));	
		x = frameX + (int) (gp.tileSize * 0.5);
		y = (int) (frameY + gp.tileSize * 0.6);	
		text = "HP"; drawText(text, x, y, Color.WHITE, Color.BLACK); y += gp.tileSize * 0.8;
		text = "ATTACK"; drawText(text, x, y, Color.WHITE, Color.BLACK); y += gp.tileSize * 0.8;
		text = "DEFENSE"; drawText(text, x, y, Color.WHITE, Color.BLACK); y += gp.tileSize * 0.8;
		text = "SP. ATK"; drawText(text, x, y, Color.WHITE, Color.BLACK); y += gp.tileSize * 0.8;
		text = "SP. DEF"; drawText(text, x, y, Color.WHITE, Color.BLACK); y += gp.tileSize * 0.8;
		text = "SPEED"; drawText(text, x, y, Color.WHITE, Color.BLACK); y += gp.tileSize * 0.8;
				
		x = frameX + (int) (gp.tileSize * 5.5);
		y = (int) (frameY + gp.tileSize * 0.6);		
		text = fighter.getHP() + "/" + fighter.getBHP(); 
		frameX = getXforRightAlignText(text, x);	
		drawText(text, frameX, y, Color.WHITE, Color.BLACK); y += gp.tileSize * 0.8;
		text = Integer.toString((int) fighter.getAttack()); 
		frameX = getXforRightAlignText(text, x);	
		drawText(text, frameX, y, Color.WHITE, Color.BLACK); y += gp.tileSize * 0.8;
		text = Integer.toString((int) fighter.getDefense()); 
		frameX = getXforRightAlignText(text, x);	
		drawText(text, frameX, y, Color.WHITE, Color.BLACK); y += gp.tileSize * 0.8;
		text = Integer.toString((int) fighter.getSpAttack()); 
		frameX = getXforRightAlignText(text, x);	
		drawText(text, frameX, y, Color.WHITE, Color.BLACK); y += gp.tileSize * 0.8;
		text = Integer.toString((int) fighter.getSpDefense()); 
		frameX = getXforRightAlignText(text, x);	
		drawText(text, frameX, y, Color.WHITE, Color.BLACK); y += gp.tileSize * 0.8;
		text = Integer.toString((int) fighter.getSpeed()); 
		frameX = getXforRightAlignText(text, x);	
		drawText(text, frameX, y, Color.WHITE, Color.BLACK); y += gp.tileSize * 0.8;
		
		x = (int) (gp.tileSize * 15);
		y = (int) (frameY + gp.tileSize * 0.6);	
		text = Integer.toString((int) fighter.getHPIV()); 
		frameX = getXforRightAlignText(text, x);	
		drawText(text, frameX, y, Color.WHITE, Color.BLACK); y += gp.tileSize * 0.8;
		text = Integer.toString((int) fighter.getAttackIV()); 
		frameX = getXforRightAlignText(text, x);	
		drawText(text, frameX, y, Color.WHITE, Color.BLACK); y += gp.tileSize * 0.8;
		text = Integer.toString((int) fighter.getDefenseIV()); 
		frameX = getXforRightAlignText(text, x);	
		drawText(text, frameX, y, Color.WHITE, Color.BLACK); y += gp.tileSize * 0.8;
		text = Integer.toString((int) fighter.getSpAttackIV()); 
		frameX = getXforRightAlignText(text, x);	
		drawText(text, frameX, y, Color.WHITE, Color.BLACK); y += gp.tileSize * 0.8;
		text = Integer.toString((int) fighter.getSpDefenseIV()); 
		frameX = getXforRightAlignText(text, x);	
		drawText(text, frameX, y, Color.WHITE, Color.BLACK); y += gp.tileSize * 0.8;
		text = Integer.toString((int) fighter.getSpeedIV()); 
		frameX = getXforRightAlignText(text, x);	
		drawText(text, frameX, y, Color.WHITE, Color.BLACK); y += gp.tileSize * 0.8;
						
		// EXP BAR
		x = -5;	
		y = (int) (gp.tileSize * 9.3);
		width = (int) (gp.tileSize * 7.48);
		height = (int) (gp.tileSize * 0.6);
		g2.setColor(Color.BLACK);
		g2.fillRoundRect(x, y, width, height, 4, 4);
		
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 28F));
		g2.setColor(battle_white);		
		text = "EXP";		
		x += gp.tileSize * 0.25;
		y += gp.tileSize * 0.48;
		g2.drawString(text, x, y);
		
		x += gp.tileSize * 0.88;
		y -= gp.tileSize * 0.41;
		width -= gp.tileSize * 1.3;
		height -= gp.tileSize * 0.12;
		g2.setColor(battle_gray);
		g2.fillRect(x, y, width, height);		
				
		double remainXP = (double) (fighter.getXP() - fighter.getBXP()) / (double) fighter.getNextXP();		
		width *= remainXP;				
		g2.setColor(battle_blue);
		g2.fillRect(x, y, width, height);
		
		// ITEM BOX
		x = -5;
		y = (int) (gp.tileSize * 10);		
		width = (int) (gp.tileSize * 7.45);
		height = (int) (gp.tileSize * 0.8);		
		g2.setColor(party_gray);
		g2.setStroke(new BasicStroke(4));
		g2.fillRoundRect(x, y, width, height, 4, 4);
		
		height += gp.tileSize * 1.8;
		g2.setColor(Color.BLACK);
		g2.drawRoundRect(x, y, width, height, 4, 4);
		
		x += gp.tileSize * 0.3;
		y += gp.tileSize * 0.7;
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 42F));
		text = "ITEM";
		drawText(text, x, y, battle_white, Color.BLACK);	
		
		g2.setColor(Color.BLACK);
		g2.setFont(g2.getFont().deriveFont(Font.BOLD));
		y += gp.tileSize * 0.9;
		if (fighter.getHeldItem() != null) text = fighter.getHeldItem().name;
		else text = "NONE";			
		g2.drawString(text, x, y);
		
		// ABILITY BOX
		x = (int) (gp.tileSize * 7.5);
		y = (int) (gp.tileSize * 7.9);
		width = (int) (gp.tileSize * 2.8);
		height = (int) (gp.tileSize * 0.8);		
		g2.setColor(party_gray);
		g2.fillRoundRect(x, y, width, height, 4, 4);
		
		width = gp.tileSize * 9;
		height = gp.tileSize * 5;
		g2.setColor(Color.BLACK);
		g2.drawRoundRect(x, y, width, height, 4, 4);
		
		x = (int) (gp.tileSize * 7.8);
		y = (int) (gp.tileSize * 8.6);
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 42F));
		text = "ABILITY";
		drawText(text, x, y, battle_white, Color.BLACK);		
		
		x += gp.tileSize * 2.8;
		text = fighter.getAbility().getName();
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 42F));
		g2.setColor(Color.BLACK);
		g2.drawString(text, x, y);
		
		x = (int) (gp.tileSize * 7.8);
		y += gp.tileSize * 0.8;
		g2.setColor(Color.BLACK);
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 38F));
		for (String line : fighter.getAbility().getDescription().split("\n")) {			
			g2.drawString(line, x, y);
			y += gp.tileSize * 0.8;
		} 					
		
		if (gp.keyH.rightPressed) {		
			gp.keyH.playCursorSE();
			partyState = party_Moves;				
			commandNum = 0;
		}
		if (gp.keyH.bPressed) {
			gp.keyH.bPressed = false;
			
			if (gp.gameState == gp.pcState) {
				pcState = pc_Box;
				partyState = 0;
			}
			else {
				partyDialogue = "Choose a POKEMON.";
				partyState = party_Main_Select;	
			}			
			commandNum = 0;
		}
	}
	private void party_Moves() {
		
		party_Info();
		
		int x;
		int y;
		int width;
		int height;
		int textX;
		int textY;
		String text;
		Pokemon fighter = null;

		if (gp.gameState == gp.pcState) fighter = gp.player.pcParty[boxTab][fighterNum];
		else fighter = gp.player.pokeParty.get(fighterNum);	
								
		int slotX = (int) (gp.tileSize * 7.5);
		int slotY;
		int slotWidth = (int) (gp.tileSize * 9.38);
		int slotHeight = gp.tileSize + 8;
		
		int frameX = (int) (gp.tileSize * 7.6);
		int frameY = (int) (gp.tileSize * 2.4);
		
		int tempX = (int) (gp.tileSize * 0.3);
		int tempY = (int) (gp.tileSize * 10.5);
		
		// MOVE STAT BOX
		x = -5;
		y = (int) (gp.tileSize * 9.5);		
		height = gp.tileSize * 3;
		width = (int) (gp.tileSize * 5.5);
		g2.setColor(party_gray);
		g2.fillRoundRect(x, y, width, height, 4, 4);		
		
		width = (int) (gp.tileSize * 7.45);
		g2.setColor(Color.BLACK);
		g2.drawRoundRect(x, y, width, height, 4, 4);
		
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 55F));			
		text = "POWER";
		drawText(text, tempX, tempY, battle_white, Color.BLACK);				
		tempY += gp.tileSize;
		text = "ACCURACY";
		drawText(text, tempX, tempY, battle_white, Color.BLACK);	
		
		g2.setColor(Color.BLACK);
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 55F));			
		tempX += gp.tileSize * 5.35;
		tempY -= gp.tileSize;		
		int power = fighter.getMoveSet().get(commandNum).getPower();
		if (power <= 0 || 999 <= power) { text = "---"; }
		else { text = Integer.toString(power); }
		g2.drawString(text, tempX, tempY);	
		
		tempY += gp.tileSize;		
		if (fighter.getMoveSet().get(commandNum).getAccuracy() <= 0) { text = "---"; }
		else { text = Integer.toString(fighter.getMoveSet().get(commandNum).getAccuracy()); }	
		g2.drawString(text, tempX, tempY);	
		
		// MOVES BOX
		x = (int) (gp.tileSize * 7.5);
		y = (int) (gp.tileSize * 2.05);
		width = gp.tileSize * 9;
		height = (int) (gp.tileSize * 5.7);
		drawSubWindow(x, y, width, height, 4, 4, party_gray, Color.BLACK);			
				
		x = frameX;
		y = frameY;		
		width = (int) (gp.tileSize * 1.8);
		height = (int) (gp.tileSize * 0.8);			
		int i = 0;
		for (Move m : fighter.getMoveSet()) {			
				
			drawSubWindow(x, y + 6, width, height, 10, 3, m.getType().getColor(), Color.BLACK);											
			g2.setFont(g2.getFont().deriveFont(Font.BOLD, 25F));			
			text = m.getType().getName();
			textX = getXForCenteredTextOnWidth(text, width, x + 5);
			textY = (int) (y + (gp.tileSize * 0.75));			
			drawText(text, textX, textY, battle_white, Color.BLACK);
			
			g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 40F));
			text = m.toString();
			textX = (int) (frameX + (gp.tileSize * 2));
			textY = (int) (y + (gp.tileSize * 0.85));			
			drawText(text, textX, textY, battle_white, Color.BLACK);	
			
			g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 45F));						
			text = m.getPP() + "/" + m.getBPP();
			textX = getXforRightAlignText(text, (int) (gp.tileSize * 15.9));
			drawText(text, textX, textY, battle_white, Color.BLACK);
			
			if (partyMove && partyMoveNum == i) {
				slotY = y - 4;
				g2.setColor(hp_yellow);
				g2.setStroke(new BasicStroke(5));
				g2.drawRoundRect(slotX, slotY, slotWidth, slotHeight, 6, 6);
			}
			else if (commandNum == i) {	
				slotY = y - 4;
				g2.setColor(battle_red);
				g2.setStroke(new BasicStroke(5));
				g2.drawRoundRect(slotX, slotY, slotWidth, slotHeight, 6, 6);
			}
						
			y += gp.tileSize * 1.35;
			i++;
		}
		
		if (commandNum == i) {	
			slotY = y - 4;
			g2.setColor(battle_red);
			g2.setStroke(new BasicStroke(5));
			g2.drawRoundRect(slotX, slotY, slotWidth, slotHeight, 6, 6);
		}
		
		// MOVE DESCRIPTION BOX
		x = (int) (gp.tileSize * 7.5);			 
		y = (int) (gp.tileSize * 7.9);
		width = gp.tileSize * 9;
		height = (int) (gp.tileSize * 0.8);		
		g2.setColor(party_gray);
		g2.setStroke(new BasicStroke(4));
		g2.fillRoundRect(x, y, width, height, 4, 4);
		
		height = gp.tileSize * 5;
		g2.setColor(Color.BLACK);
		g2.drawRoundRect(x, y, width, height, 4, 4);
		
		x = (int) (gp.tileSize * 7.8);
		y = (int) (gp.tileSize * 8.6);
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 42F));
		text = "DESCRIPTION";
		drawText(text, x, y, battle_white, Color.BLACK);	
				
		y += gp.tileSize * 0.8;
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 38F));
		g2.setColor(Color.BLACK);
		for (String line : fighter.getMoveSet().get(commandNum).getInfo().split("\n")) {	
		g2.drawString(line, x, y);
			y += gp.tileSize * 0.8;
		} 					
				
		if (gp.keyH.upPressed) {				
			gp.keyH.upPressed = false;
			
			if (commandNum > 0) {
				gp.keyH.playCursorSE();	
				commandNum--;			
			}				
		}
		if (gp.keyH.downPressed) {				
			gp.keyH.downPressed = false;	
			if (commandNum < fighter.getMoveSet().size() - 1) {
				gp.keyH.playCursorSE();	
				commandNum++;		
			}
		}					
		if (gp.keyH.leftPressed && !partyMove) {
			gp.keyH.playCursorSE();
			partyState = party_Skills;				
			commandNum = 0;
		}
		if (gp.keyH.aPressed) {
			gp.keyH.aPressed = false;
			
			if (partyMove) {
				Collections.swap(fighter.getMoveSet(), commandNum, partyMoveNum);		
				partyMove = false;
				partyMoveNum = -1;
			}
			else {
				partyMove = true;
				partyMoveNum = commandNum;	
			}				
		}
		if (gp.keyH.bPressed) {						
			gp.keyH.bPressed = false;
			
			if (partyMove) {
				partyMove = false;
				partyMoveNum = -1;
			}			
			else if (gp.gameState == gp.pcState) {
				pcState = pc_Box;
				partyState = 0;
			}
			else {
				partyDialogue = "Choose a POKEMON.";
				partyState = party_Main_Select;
				commandNum = 0;
			}				
		}		
	}
	private void party_Info() {
		
		g2.setColor(battle_white);  
		g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);			
		
		int x;
		int y;
		int width;
		int height;
		String text;
		Pokemon fighter = null;

		if (gp.gameState == gp.pcState) fighter = gp.player.pcParty[boxTab][fighterNum];
		else fighter = gp.player.pokeParty.get(fighterNum);	
		
		// HEADER 
		x = -10;
		y = -5;
		width = gp.screenWidth + 13;
		height = (int) (gp.tileSize * 2);
		drawSubWindow(x, y, width, height, 10, 4, party_green, Color.BLACK);	
		
		// FIGHTER NAME BOX
		x = -5;
		y = (int) (gp.tileSize * 2.05);
		width = (int) (gp.tileSize * 7.45);
		height = (int) (gp.tileSize * 1.2);
		g2.setColor(party_gray);
		g2.fillRoundRect(x, y, width, height, 8, 8);
		
		height += gp.tileSize * 0.8;
		g2.setColor(Color.BLACK);
		g2.drawRoundRect(x, y, width, height, 4, 4);
				
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 55F));		
		x += gp.tileSize * 0.15;
		y += gp.tileSize * 0.1;
		g2.drawImage(fighter.getBall().image2, x, y, null);
		
		x += gp.tileSize;
		y += gp.tileSize * 0.85;		
		text = fighter.getName();				
		drawText(text, x, y, Color.WHITE, Color.BLACK);
		
		int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();		
		x += length + 3;
		g2.setColor(fighter.getSexColor());	
		g2.drawString("" + fighter.getSex(), x, y);
	
		// FIGHTER LEVEL AND NO.
		x = (int) (gp.tileSize * 0.3);
		y += gp.tileSize * 0.9;
		text = "Lv" + fighter.getLevel();
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 38F));	
		g2.setColor(Color.BLACK);
		g2.drawString(text, x, y);
		
		// FIGHTER STATUS
		if (fighter.getStatus() != null) {
			battle_Status((int) (x + (gp.tileSize * 1.65)), (int) (y - (gp.tileSize * 0.52)), fighter, 30F);
			g2.setFont(g2.getFont().deriveFont(Font.BOLD, 38F));
		}
		
		text = fighter.getNature().getName();
		x = getXforRightAlignText(text, x + (int) (gp.tileSize * 6.9));		
		g2.drawString(text, x, y);
				
		x = (int) (gp.tileSize * 5.8);
		y -= gp.tileSize * 1.2;		
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 32F));	
		text = "No." + String.format("%03d", fighter.getIndex());	
		drawText(text, x, y, Color.WHITE, Color.BLACK);
						
		// FIGHTER SPRITE
		x = (int) (gp.tileSize * 1.3);
		y = (int) (gp.tileSize * 4.2);
		g2.drawImage(fighter.getFrontSprite(), x, y, null);
		
		if (gp.keyH.xPressed) {
			gp.keyH.xPressed = false;
			gp.playSE(gp.cry_SE, fighter.toString());
		}
	}
	private void party_Header(int subState) {
		
		int x;
		int y;
		String text;
		Pokemon fighter;
		
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 50F));	
		
		if (subState == 1) g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3F));
		text = "SKILLS";
		x = (int) (gp.tileSize * 5.2);		
		y = (int) (gp.tileSize * 1.3);					
		drawText(text, x, y, Color.WHITE, Color.BLACK);
		
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1F));
		
		if (subState == 0) text = "->";
		else text = "<-";
		x += gp.tileSize * 2.7;	
		y = (int) (gp.tileSize * 1.3);					
		drawText(text, x, y, Color.WHITE, Color.BLACK);
		
		if (subState == 0) g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3F));
		text = "MOVES";
		x += gp.tileSize;	
		y = (int) (gp.tileSize * 1.3);					
		drawText(text, x, y, Color.WHITE, Color.BLACK);
		
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1F));
	
		x = (int) (gp.tileSize * 0.4);	
		y = (int) (gp.tileSize * 1.3);
		drawText("< " + KeyEvent.getKeyText(gp.btn_L), x, y, battle_white, Color.BLACK);		
		x = (int) (gp.screenWidth - (gp.tileSize * 1.2));
		y = (int) (gp.tileSize * 1.3);
		drawText(KeyEvent.getKeyText(gp.btn_R) + " >", x, y, battle_white, Color.BLACK);
			
		if (gp.gameState == gp.pcState) {		
			
			if (0 < fighterNum && gp.player.pcParty[boxTab][fighterNum - 1] != null) {				
				
				x = (int) (gp.tileSize * 1.4);	
				y = 0;				
				fighter = gp.player.pcParty[boxTab][fighterNum - 1];				
				g2.drawImage(fighter.getMenuSprite(), x, y, null);		
				
				if (gp.keyH.lPressed && !partyMove) {				
					gp.keyH.lPressed = false;
					gp.keyH.playCursorSE();
					fighterNum--;
					commandNum = 0;				 		
				}
			}	
			
			if (fighterNum < 29 && gp.player.pcParty[boxTab][fighterNum + 1] != null) {		
				
				x = (int) (gp.screenWidth - (gp.tileSize * 3.4));
				y = 0;
				fighter = gp.player.pcParty[boxTab][fighterNum + 1];				
				g2.drawImage(fighter.getMenuSprite(), x, y, null);			
				
				if (gp.keyH.rPressed && !partyMove) {				
					gp.keyH.rPressed = false;
					gp.keyH.playCursorSE();
					fighterNum++;
					commandNum = 0;
				}				
			}
		}
		else {			
			if (0 < fighterNum) {
				
				x = (int) (gp.tileSize * 1.4);	
				y = 0;
				fighter = gp.player.pokeParty.get(fighterNum - 1);				
				g2.drawImage(fighter.getMenuSprite(), x, y, null);		
				
				if (gp.keyH.lPressed && !partyMove) {				
					gp.keyH.lPressed = false;
					gp.keyH.playCursorSE();
					fighterNum--;
					commandNum = 0;	
				}
			}		
			
			if (fighterNum + 1 < gp.player.pokeParty.size()) {		
				
				x = (int) (gp.screenWidth - (gp.tileSize * 3.4));
				y = 0;
				fighter = gp.player.pokeParty.get(fighterNum + 1);				
				g2.drawImage(fighter.getMenuSprite(), x, y, null);		
				
				if (gp.keyH.rPressed && !partyMove) {				
					gp.keyH.rPressed = false;
					gp.keyH.playCursorSE();
					fighterNum++;
					commandNum = 0;	
				}		
			}
		}						
	}
	
	private void party_MoveSwap() {
						
		g2.setColor(battle_white);  
		g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);	

		int x;
		int y;
		int width;
		int height;
		String text;
		Pokemon fighter = gp.player.pokeParty.get(fighterNum);
		
		List<Move> moveSet = new ArrayList<>();
		moveSet.addAll(gp.player.pokeParty.get(fighterNum).getMoveSet());
		moveSet.add(gp.btlManager.newMove);
		
		// FIGHTER NAME BOX
		x = -5;
		y = (int) (gp.tileSize * 0.2);
		width = (int) (gp.tileSize * 7.45);
		height = (int) (gp.tileSize * 1.2);
		g2.setColor(party_gray);
		g2.fillRoundRect(x, y, width, height, 8, 8);
		
		height += gp.tileSize * 0.8;
		g2.setColor(Color.BLACK);
		g2.drawRoundRect(x, y, width, height, 4, 4);
				
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 55F));		
		x += gp.tileSize * 0.15;
		y += gp.tileSize * 0.1;
		g2.drawImage(fighter.getBall().image2, x, y, null);
		
		x += gp.tileSize;
		y += gp.tileSize * 0.85;		
		text = fighter.getName();				
		drawText(text, x, y, Color.WHITE, Color.BLACK);
		
		int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();		
		x += length + 3;
		g2.setColor(fighter.getSexColor());	
		g2.drawString("" + fighter.getSex(), x, y);
	
		// FIGHTER LEVEL AND NO.
		x = (int) (gp.tileSize * 0.3);
		y += gp.tileSize * 0.85;
		text = "Lv" + fighter.getLevel();
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 38F));	
		g2.setColor(Color.BLACK);
		g2.drawString(text, x, y);
		
		text = fighter.getNature().getName();
		x = getXforRightAlignText(text, x + (int) (gp.tileSize * 6.9));		
		g2.drawString(text, x, y);
		
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 32F));	
		x = (int) (gp.tileSize * 5.8);
		y -= gp.tileSize * 1.2;
		text = "No." + String.format("%03d", fighter.getIndex());	
		drawText(text, x, y, Color.WHITE, Color.BLACK);
		
		// FIGHTER STATUS
		if (fighter.getStatus() != null) {
			battle_Status((int) (x + (gp.tileSize * 1.5)), (int) (y - (gp.tileSize * 0.53)), fighter, 30F);
			g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 38F));
		}
		
		// FIGHTER SPRITE
		x = (int) (gp.tileSize * 1.3);
		y = (int) (gp.tileSize * 3.1);
		g2.drawImage(fighter.getFrontSprite(), x, y, null);
												
		int tempX = (int) (gp.tileSize * 0.4);
		int tempY = (int) (gp.tileSize * 10.5);		
		
		// MOVE STAT BOX
		x = -5;
		y = (int) (gp.tileSize * 9.5);		
		height = gp.tileSize * 3;
		width = (int) (gp.tileSize * 5.5);
		g2.setColor(party_gray);
		g2.fillRoundRect(x, y, width, height, 4, 4);		
		
		width = (int) (gp.tileSize * 7.45);
		g2.setColor(Color.BLACK);
		g2.drawRoundRect(x, y, width, height, 4, 4);
		
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 55F));			
		text = "POWER";
		drawText(text, tempX, tempY, battle_white, Color.BLACK);				
		tempY += gp.tileSize;
		text = "ACCURACY";
		drawText(text, tempX, tempY, battle_white, Color.BLACK);	
		
		g2.setColor(Color.BLACK);
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 55F));			
		tempX += gp.tileSize * 5.35;
		tempY -= gp.tileSize;		
		if (moveSet.get(commandNum).getPower() == 0) { text = "---"; }
		else { text = Integer.toString(moveSet.get(commandNum).getPower()); }
		g2.drawString(text, tempX, tempY);	
		
		tempY += gp.tileSize;		
		if (moveSet.get(commandNum).getAccuracy() == 0) { text = "---"; }
		else { text = Integer.toString(moveSet.get(commandNum).getAccuracy()); }		
		g2.drawString(text, tempX, tempY);	
				
		// MOVES BOX
		x = (int) (gp.tileSize * 7.5);
		y = (int) (gp.tileSize * 0.2);
		width = gp.tileSize * 9;
		height = (int) (gp.tileSize * 7.5);
		drawSubWindow(x, y, width, height, 4, 4, party_gray, Color.BLACK);			
		
		int i = 0;
		int slotX = (int) (gp.tileSize * 7.5);
		int slotY;
		int slotWidth = (int) (gp.tileSize * 9.38);
		int slotHeight = gp.tileSize + 8;
		
		int frameX = (int) (gp.tileSize * 7.9);
		int frameY = (int) (gp.tileSize * 0.5);
		x = frameX;
		y = frameY;		
		width = (int) (gp.tileSize * 2);
		height = gp.tileSize;	
		int textX;
		int textY;
		for (Move m : moveSet) {			
			
			if (i == moveSet.size() - 1) {
				y = (int) (gp.tileSize * 6.4);
			}
				
			drawSubWindow(x, y, width, height, 10, 3, m.getType().getColor(), Color.BLACK);		
									
			g2.setFont(g2.getFont().deriveFont(Font.BOLD, 30F));			
			text = m.getType().getName();
			textX = getXForCenteredTextOnWidth(text, width, x + 5);
			textY = (int) (y + (gp.tileSize * 0.75));			
			drawText(text, textX, textY, battle_white, Color.BLACK);
			
			g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 40F));
			text = m.toString();
			textX = (int) (frameX + (gp.tileSize * 2.3));
			textY = (int) (y + (gp.tileSize * 0.85));			
			drawText(text, textX, textY, battle_white, Color.BLACK);	
			
			g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 45F));						
			text = m.getPP() + "/" + m.getBPP();
			textX = getXforRightAlignText(text, (int) (gp.tileSize * 15.7));
			drawText(text, textX, textY, battle_white, Color.BLACK);
			
			if (partyMove && partyMoveNum == i) {
				slotY = y - 4;
				g2.setColor(hp_yellow);
				g2.setStroke(new BasicStroke(5));
				g2.drawRoundRect(slotX, slotY, slotWidth, slotHeight, 6, 6);
			}
			else if (commandNum == i) {	
				slotY = y - 4;
				g2.setColor(battle_red);
				g2.setStroke(new BasicStroke(5));
				g2.drawRoundRect(slotX, slotY, slotWidth, slotHeight, 6, 6);
			}
						
			y += gp.tileSize * 1.35;
			i++;
		}
					
		if (commandNum == i) {	
			slotY = y - 4;
			g2.setColor(battle_red);
			g2.setStroke(new BasicStroke(5));
			g2.drawRoundRect(slotX, slotY, slotWidth, slotHeight, 6, 6);
		}
			 
		// MOVE DESCRIPTION BOX
		x = (int) (gp.tileSize * 7.5);			 
		y = (int) (gp.tileSize * 7.9);
		width = gp.tileSize * 9;
		height = (int) (gp.tileSize * 0.8);		
		g2.setColor(party_gray);
		g2.setStroke(new BasicStroke(4));
		g2.fillRoundRect(x, y, width, height, 4, 4);
		
		height = gp.tileSize * 5;
		g2.setColor(Color.BLACK);
		g2.drawRoundRect(x, y, width, height, 4, 4);
		
		x = (int) (gp.tileSize * 7.8);
		y = (int) (gp.tileSize * 8.6);
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 42F));
		text = "DESCRIPTION";
		drawText(text, x, y, battle_white, Color.BLACK);	
				
		y += gp.tileSize * 0.8;
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 38F));
		g2.setColor(Color.BLACK);
		for (String line : moveSet.get(commandNum).getInfo().split("\n")) {			
			g2.drawString(line, x, y);
			y += gp.tileSize * 0.8;
		} 					
				
		if (gp.keyH.upPressed) {				
			gp.keyH.upPressed = false;
			
			if (commandNum > 0) {
				gp.keyH.playCursorSE();	
				commandNum--;				
			}				
		}
		if (gp.keyH.downPressed) {				
			gp.keyH.downPressed = false;	
			if (commandNum < moveSet.size() - 1) {
				gp.keyH.playCursorSE();	
				commandNum++;				
			}
		}		
		if (gp.keyH.aPressed) {			
			gp.keyH.aPressed = false;
			
			if (commandNum != moveSet.size() - 1) {
				gp.keyH.playCursorSE();
				
				gp.btlManager.oldMove = moveSet.get(commandNum);			
				fighter.replaceMove(gp.btlManager.oldMove, gp.btlManager.newMove);	
				
				commandNum = 0;			
				gp.gameState = gp.battleState;	
			}
			else {
				gp.keyH.playErrorSE();
			}
		}
		if (gp.keyH.bPressed) {
			gp.keyH.bPressed = false;	
						
			commandNum = 0;
			gp.gameState = gp.battleState;
		}			
	}
	/** END PARTY SCREEN **/
	
	/** OPTIONS SCREEN **/
	private void pause_Options() {
		
		g2.setColor(party_green);  
		g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
		
		int x;
		int y;
		int width;
		int height;
		
		int slotY;
		int slotWidth;
		int slotHeight;
		
		int volumeWidth;
		
		x = gp.tileSize;
		y = 10;
		width = gp.screenWidth - (gp.tileSize * 2);
		height = gp.tileSize * 2;		
		drawSubWindow(x, y, width, height, 25, 10, battle_white, dialogue_blue);
		
		x += gp.tileSize * 0.8;
		y += gp.tileSize * 1.3;	
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 58F));
  		drawText("OPTIONS", x, y, pause_yellow, Color.GRAY);
  			
  		x = gp.tileSize;
  		y = (int) (gp.tileSize * 2.7);
  		height = gp.tileSize * 9;
  		drawSubWindow(x, y, width, height, 25, 10, battle_white, dialogue_blue); 
  		  		
  		x += gp.tileSize * 0.8;
  		y += gp.tileSize * 1.3;
  		slotY = y;
  		drawText("FULL SCREEN", x, y, pause_yellow, Color.GRAY);
  		if (commandNum == 0) { g2.setColor(Color.BLACK); g2.drawString(">", x - 25, y); }
  		
		y += gp.tileSize * 1.3;	
  		drawText("TEXT SPEED", x, y, pause_yellow, Color.GRAY);
  		if (commandNum == 1) { g2.setColor(Color.BLACK); g2.drawString(">", x - 25, y); }
  		
  		y += gp.tileSize * 1.3;
  		drawText("MUSIC", x, y, pause_yellow, Color.GRAY);
  		if (commandNum == 2) { g2.setColor(Color.BLACK); g2.drawString(">", x - 25, y); }
  		
  		y += gp.tileSize * 1.3;
  		drawText("SOUND EFFECTS", x, y, pause_yellow, Color.GRAY);
  		if (commandNum == 3) { g2.setColor(Color.BLACK); g2.drawString(">", x - 25, y); }
  		
  		y += gp.tileSize * 1.3;
  		drawText("BATTLE STYLE", x, y, pause_yellow, Color.GRAY);
  		if (commandNum == 4) { g2.setColor(Color.BLACK); g2.drawString(">", x - 25, y); }
  		
  		y += gp.tileSize * 2;
  		drawText("EXIT", x, y, pause_yellow, Color.GRAY);
  		if (commandNum == 5) { g2.setColor(Color.BLACK); g2.drawString(">", x - 25, y); }
  		
  		g2.setColor(Color.BLACK);
  		
  		x += gp.tileSize * 6.5;
  		y = slotY;
  		if (gp.fullScreenOn) drawText("ON", x, y, hp_red, Color.LIGHT_GRAY);
  		else drawText("ON", x, y, Color.BLACK, Color.LIGHT_GRAY);
  		
  		y += gp.tileSize * 1.3;
  		if (textSpeed == 4) drawText("SLOW", x, y, hp_red, Color.LIGHT_GRAY);  	
  		else drawText("SLOW", x, y, Color.BLACK, Color.LIGHT_GRAY);  
  		  		
  		// MUSIC SLIDER
  		g2.setStroke(new BasicStroke(5));  	
  		g2.setColor(Color.BLACK);
  		y += gp.tileSize * 1.3;
  		slotWidth = (int) (gp.tileSize * 4);
  		slotHeight = (int) (gp.tileSize * 0.7);  		
		g2.drawRect(x, y - (int) (gp.tileSize * 0.7), slotWidth, slotHeight);		
		volumeWidth = (slotWidth / 5) * gp.music.volumeScale;
		g2.fillRect(x, y - (int) (gp.tileSize * 0.7), volumeWidth, slotHeight); 
		
		// SOUND EFFECTS SLIDER
		y += gp.tileSize * 1.3;		
		g2.drawRect(x, y - (int) (gp.tileSize * 0.7), slotWidth, slotHeight);		
		volumeWidth = (slotWidth / 5) * gp.se.volumeScale;
		g2.fillRect(x, y - (int) (gp.tileSize * 0.7), volumeWidth, slotHeight); 
		
		y += gp.tileSize * 1.3;
		if (!gp.btlManager.set) drawText("SHIFT", x, y, hp_red, Color.LIGHT_GRAY);			
		else drawText("SHIFT", x, y, Color.BLACK, Color.LIGHT_GRAY);
		  		
  		x += gp.tileSize * 2.5;
  		y = slotY;
  		y += gp.tileSize * 1.3;
  		if (textSpeed == 3) drawText("MED", x, y, hp_red, Color.LIGHT_GRAY);  	
  		else drawText("MED", x, y, Color.BLACK, Color.LIGHT_GRAY);  
  		
  		x += gp.tileSize * 2;
  		y = slotY;
  		if (!gp.fullScreenOn) drawText("OFF", x, y, hp_red, Color.LIGHT_GRAY);
  		else drawText("OFF", x, y, Color.BLACK, Color.LIGHT_GRAY);
  		
  		y += gp.tileSize * 1.3;
  		if (textSpeed == 2) drawText("FAST", x, y, hp_red, Color.LIGHT_GRAY);  	
  		else drawText("FAST", x, y, Color.BLACK, Color.LIGHT_GRAY);  
  		
  		y += (gp.tileSize * 1.3) * 3;
  		if (gp.btlManager.set) drawText("SET", x, y, hp_red, Color.LIGHT_GRAY);
  		else drawText("SET", x, y, Color.BLACK, Color.LIGHT_GRAY);

  		if (gp.keyH.upPressed) {
  			
  			if (commandNum > 0) {
  				gp.keyH.playCursorSE();
  				commandNum--;
  			}
  			
  			gp.keyH.upPressed = false;
  		}
  		if (gp.keyH.downPressed) {
  			
  			if (commandNum < 5) {
  				gp.keyH.playCursorSE();
  				commandNum++;
  			}
  			
  			gp.keyH.downPressed = false;
  		}
  		
  		if (gp.keyH.leftPressed) {  
  			
  			if (commandNum == 0) {
  				if (!gp.fullScreenOn) {
  					gp.keyH.playCursorSE();  
  					gp.fullScreenOn = true;
  				}
  			}
  			else if (commandNum == 1) {
  				if (textSpeed < 4) {
  					gp.keyH.playCursorSE();  	
  					textSpeed++;
  				}
  			}  			
  			else if (commandNum == 2) {  			
  				if (gp.music.volumeScale > 0) {
  					gp.keyH.playCursorSE();  	
  					gp.music.volumeScale--;
					gp.music.checkVolume();	
  				}
  			}
  			else if (commandNum == 3) {
  				if (gp.se.volumeScale > 0) {
  					gp.keyH.playCursorSE();  	
  					gp.se.volumeScale--;
  				}
  			}
  			else if (commandNum == 4) {
  				if (gp.btlManager.set) {
  					gp.keyH.playCursorSE();  	
  					gp.btlManager.set = false;  					
  				}
  			}
  			
  			gp.keyH.leftPressed = false;
  		}
  		if (gp.keyH.rightPressed) {
  			
  			if (commandNum == 0) {
  				if (gp.fullScreenOn) {
  					gp.keyH.playCursorSE();  
  					gp.fullScreenOn = false;
  				}
  			}
  			else if (commandNum == 1) {
  				if (textSpeed > 2) {
  					gp.keyH.playCursorSE();  	
  					textSpeed--;
  				}
  			}
  			else if (commandNum == 2) {  			
  				if (gp.music.volumeScale < 5) {
  					gp.keyH.playCursorSE();  	
  					gp.music.volumeScale++;
					gp.music.checkVolume();	
  				}
  			}
  			else if (commandNum == 3) {
  				if (gp.se.volumeScale < 5) {
  					gp.keyH.playCursorSE();  	
  					gp.se.volumeScale++;
  				}
  			}
  			else if (commandNum == 4) {
  				if (!gp.btlManager.set) {
  					gp.keyH.playCursorSE();  	
  					gp.btlManager.set = true;  					
  				}
  			}
  			
  			gp.keyH.rightPressed = false;
  		}
  		
  		if (gp.keyH.aPressed) {
  			if (commandNum == 5) {
  				gp.keyH.playCursorSE();
  				pauseState = pause_Main;
  				commandNum = 5;
  			}
  			gp.keyH.aPressed = false;
  		}
  		if (gp.keyH.bPressed) {
  			gp.keyH.playCursorSE();
  			pauseState = pause_Main;
  			commandNum = 5;  			
  			gp.keyH.bPressed = false;
  		}  		
  		
		gp.config.saveConfig();
	}
	/** END OPTIONS SCREEN **/	
	/** END PAUSE SCREEN **/
			
	/** BATTLE SCREEN **/
	private void drawBattleScreen() {		

		g2.setColor(new Color(234,233,246));  
		g2.drawImage(battle_bkg, 0, 0, null);
		
		switch(battleState) {		
		
			case battle_Encounter:
				animateBattleEntrance();
				battle_Dialogue();
				break;
				
			case battle_Options:				
				battle_Fighters();				
				battle_Options();
				battle_HUD();
				break;
				
			case battle_Moves:
				battle_Fighters();				
				battle_Moves();
				battle_HUD();
				break;
				
			case battle_Dialogue:
				battle_Fighters();
				battle_Dialogue();
				battle_HUD();
				break;		
				
			case battle_LevelUp:
				battle_Fighters();
				battle_Dialogue();
				battle_HUD();
				battle_LevelUp();
				break;		
				
			case battle_Confirm:					
				if (gp.btlManager.fightStage == gp.btlManager.fight_Evolve) {
					battle_Evolve();
					battle_Confirmation();		
					battle_Dialogue();
				}
				else {
					battle_Fighters();	
					battle_Dialogue();
					battle_HUD();
					battle_Confirmation();				
				}
				break;
				
			case battle_Evolve:
				battle_Evolve();
				battle_Dialogue();
				break;
				
			case battle_End:
				animateTrainerDefeat();
				battle_Dialogue();
				break;
		}
	}
		
	private void battle_Dialogue() {
		
		int width = (int) (gp.screenWidth - (gp.tileSize * 0.15));
		int height = (int) (gp.tileSize * 3.5);		
		int x = (int) (gp.tileSize * 0.1);
		int y = (int) (gp.screenHeight - (height * 1.02)); 
		drawSubWindow(x, y, width, height, 12, 10, battle_green, battle_red);
		
		x = gp.tileSize / 2;
		y = gp.screenHeight - gp.tileSize * 2;
		String text = "";
		
		g2.setColor(Color.BLACK);
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 60F));
		
		if (battleDialogue != null) {		
			
			// PRINT OUT DIALOGUE
			for (String line : battleDialogue.split("\n")) {   
	  			text = line;
	  			drawText(text, x, y, battle_white, Color.BLACK);
				y += gp.tileSize;
			} 		
			
			// ADVANCE DIALOGUE ICON
			if (canSkip) {				
	  			x += (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
	  			y -= gp.tileSize * 1.4;
	  			g2.drawImage(dialogue_next, x, y, null);	  			
			}
		}
	}		
	private void animateBattleEntrance() {
		
		int x; 
		int y; 
		
		x = fighter_two_X - gp.tileSize;
		y = (int) (fighter_two_Y + gp.tileSize * 2.3);
		g2.drawImage(battle_arena, x, y, null);	
		
		if (gp.btlManager.battleMode == gp.btlManager.wildBattle) {
			g2.drawImage(gp.btlManager.fighter[1].getFrontSprite(), fighter_two_X, fighter_two_Y, null);
		}
		else {
			g2.drawImage(gp.btlManager.trainer.frontSprite, fighter_two_X + 25, fighter_two_Y + 15, null);
		}
		
		x = fighter_one_X - gp.tileSize;
		y = fighter_one_Y + gp.tileSize * 4;
		g2.drawImage(battle_arena, x, y, null);
		g2.drawImage(gp.player.backSprite, fighter_one_X + 30, fighter_one_Y + 40, null);		
		
		if (fighter_one_X > fighter_one_endX && fighter_two_X < fighter_two_endX) {
			fighter_one_X -= 6;	
			fighter_two_X += 6;	
		}		
	}		
	
	private void battle_Fighters() {	
						
		g2.drawImage(battle_arena, fighter_one_platform_endX, fighter_one_platform_Y, null);		
		g2.drawImage(battle_arena, fighter_two_platform_endX, fighter_two_platform_Y, null);
		
		if (gp.btlManager.fighter[0] != null &&
				gp.btlManager.fighter[0].getProtectedState() == Protection.NONE) {			
			
			if (gp.btlManager.fighter[0].getAttacking()) animateAttack_One();			
			else fighter_one_X = fighter_one_endX;
			
			if (!gp.btlManager.fighter[0].isAlive()) {
				animateFaint_One();			
			}
			
			if (gp.btlManager.fighter[0].getHit()) {
				hitCounter++;
				if (hitCounter > 30) {
					gp.btlManager.fighter[0].setHit(false);
					hitCounter = -1;
				}
				else if (hitCounter % 5 == 0) {
					g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
				}
			}
			
			g2.drawImage(gp.btlManager.fighter[0].getBackSprite(), fighter_one_X, fighter_one_Y, null);		
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));			
		}
		if (gp.btlManager.fighter[1] != null &&
				gp.btlManager.fighter[1].getProtectedState() == Protection.NONE) {				
			
			if (isFighterCaptured) {				
				g2.drawImage(gp.btlManager.ballUsed.image3, fighter_two_X + (int)(gp.tileSize * 2.2), fighter_two_Y + (int)(gp.tileSize * 3.2), null);	
			}
			else {								
				if (gp.btlManager.fighter[1].getAttacking()) animateAttack_Two();				
				else fighter_two_X = fighter_two_endX;					
				
				if (!gp.btlManager.fighter[1].isAlive()) {
					animateFaint_Two();
				}
				else {
					if (gp.btlManager.fighter[1].getHit()) {
						hitCounter++;
						if (hitCounter > 30) {
							gp.btlManager.fighter[1].setHit(false);
							hitCounter = -1;
						}
						else if (hitCounter % 5 == 0) {
							g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
						}
					}
					g2.drawImage(gp.btlManager.fighter[1].getFrontSprite(), fighter_two_X, fighter_two_Y, null);	
					g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));	
				}
			}			
		}		
	}
		
	private void animateFaint_One() {
		if (fighter_one_Y < gp.screenHeight) {
			fighter_one_Y += 16;
		}
	}
	private void animateFaint_Two() {
		if (fighter_two_Y < gp.screenHeight) {
			fighter_two_Y += 16;
		}
		
		g2.drawImage(gp.btlManager.fighter[1].getFrontSprite(), fighter_two_X, fighter_two_Y, null);	
	}		
	private void animateAttack_One() {
		attackCounter++;
		if (attackCounter > 20) fighter_one_X -= 3;
		else fighter_one_X += 3;
		
		if (attackCounter == 41) {
			attackCounter = 0;
			gp.btlManager.fighter[0].setAttacking(false);
		}
	}
	private void animateAttack_Two() {
		attackCounter++;
		if (attackCounter > 20) fighter_two_X += 3;
		else fighter_two_X -= 3;
		
		if (attackCounter == 41) {
			attackCounter = 0;
			gp.btlManager.fighter[1].setAttacking(false);
		}
	}
	private void animateTrainerDefeat() {
		
		fighter_two_Y = fighter_two_startY;	
				
		if (fighter_two_endX < fighter_two_X) {
			fighter_two_X -= 6;
		}
		
		g2.drawImage(battle_arena, fighter_one_platform_endX, fighter_one_platform_Y, null);		
		g2.drawImage(battle_arena, fighter_two_platform_endX, fighter_two_platform_Y, null);

		g2.drawImage(gp.btlManager.trainer.frontSprite, fighter_two_X + 25, fighter_two_Y + 15, null);
		g2.drawImage(gp.btlManager.fighter[0].getBackSprite(), fighter_one_X, fighter_one_Y, null);			
	}
		
	private void battle_HUD() {
		
		int x; 
		int y;
		
		if (gp.btlManager.fighter[0] != null) {
			x = (int) (gp.tileSize * 9.25);
			y = (int) (gp.tileSize * 5.5);
			battle_FighterWindow(x, y, 0);		
			battle_EXP(x, y);
		}
		
		if (gp.btlManager.fighter[1] != null) {
			x = (int) (gp.tileSize * 0.5);
			y = (int) (gp.tileSize * 0.8);
			battle_FighterWindow(x, y, 1);				
		}
	}	
	private void battle_FighterWindow(int x, int y, int num) {
		
		battle_Party(num);
						
		int tempX = x;
		
		String text;
		int length;
		int width = (int) (gp.tileSize * 6.35);
		int height = (int) (gp.tileSize * 2.3);
		
		drawSubWindow(x, y, width, height, 15, 4, battle_yellow, Color.BLACK);
				
		x += gp.tileSize * 0.3;
		y += gp.tileSize * 0.8;
		text = gp.btlManager.fighter[num].getName();
		g2.setColor(Color.BLACK);
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 35F));
		g2.drawString(text, x, y);
		
		length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();		
		x += length + 3;
		g2.setColor(gp.btlManager.fighter[num].getSexColor());	
		g2.drawString("" + gp.btlManager.fighter[num].getSex(), x, y);
		
		g2.setColor(Color.BLACK);		
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 32F));		
		text = "Lv" + gp.btlManager.fighter[num].getLevel();		
		length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();		
		x = (int) (tempX + width - length - gp.tileSize * 0.4);
		g2.drawString(text, x, y);		
		
		if (gp.btlManager.fighter[num].getStatus() != null) {			
			battle_Status((int) (tempX + gp.tileSize * 0.3), (int) (y + gp.tileSize * 0.2), gp.btlManager.fighter[num], 32F);
			g2.setFont(g2.getFont().deriveFont(Font.BOLD, 32F));		
		}
		
		x = (int) (tempX + gp.tileSize * 1.45);
		y += gp.tileSize * 0.22;	
		width = (int) (gp.tileSize * 4.7);
		height = (int) (gp.tileSize * 0.55);
		
		g2.setColor(Color.BLACK);
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 30F));
		g2.setStroke(new BasicStroke(2));
		g2.fillRoundRect(x, y, width, height, 10, 10);
		
		g2.setColor(Color.WHITE);	
		x += (int) gp.tileSize * 0.25;
		y += gp.tileSize * 0.46;
		g2.drawString("HP", x, y);		
		
		x += gp.tileSize * 0.55;
		y -= gp.tileSize * 0.34;
		width = (int) (gp.tileSize * 3.8);
		height = (int) (gp.tileSize * 0.33);
		g2.setColor(Color.WHITE);
		g2.setStroke(new BasicStroke(3));
		g2.fillRoundRect(x, y, width, height, 3, 3);				
			
		double remainHP = (double)gp.btlManager.fighter[num].getHP() / 
				(double)gp.btlManager.fighter[num].getBHP();	
					
		if (remainHP >= .50) g2.setColor(hp_green);
		else if (remainHP >= .25) g2.setColor(hp_yellow);
		else { 
			g2.setColor(hp_red); 
			if (num == 0) {				
				if (remainHP > 0) {
					hpCounter++;
					if (hpCounter == 33) {
						gp.playSE(gp.battle_SE, "hp-low");
						hpCounter = 0;
					}	
				}
				else {
					hpCounter = 0;
				} 
			}
		}
		
		width *= remainHP;							
		g2.fillRoundRect(x, y, width, height, 3, 3);			
		
		g2.setColor(Color.BLACK);
		text = gp.btlManager.fighter[num].getHP() + " / " + gp.btlManager.fighter[num].getBHP();
		x = getXforRightAlignText(text, (int) (x + gp.tileSize * 3.65));		
		y += gp.tileSize * 0.9;
		g2.drawString(text, x, y);	
	}	
	private void battle_Party(int num) {	
		
		if (gp.btlManager.battleMode == gp.btlManager.wildBattle && num != 0) {
			return;
		}		
						
		int startX;
		int x;
		int y;
		
		List<Pokemon> pokeParty = num == 0 ? gp.player.pokeParty : gp.btlManager.trainer.pokeParty;
		
		int partySize = pokeParty.size();
		int activePartySize = 0;
		for (Pokemon p : pokeParty) {
			if (p.isAlive()) activePartySize++;
		}
		
		if (num == 0) {
			
			startX = (int) (gp.tileSize * 12.45);
			x = startX;
			y = (int) (gp.tileSize * 5.0);		
			
			for (int i = 0; i < 6; i++) {
				g2.drawImage(ball_empty, x, y, null);			
				x += gp.tileSize * 0.5;		
			}

			x = startX;
			
			for (int i = 0; i < partySize; i++) {
				g2.drawImage(ball_inactive, x, y, null);		
				x += gp.tileSize * 0.5;	
			}

			for (int i = 0; i < activePartySize; i++) {			
				x -= gp.tileSize * 0.5;	
				g2.drawImage(ball_active, x, y, null);				
			}	
		}
		else {
			
			startX = (int) (gp.tileSize * 0.6);
			x = startX;
			y = (int) (gp.tileSize * 0.3);	
			
			for (int i = 0; i < 6; i++) {
				g2.drawImage(ball_empty, x, y, null);			
				x += gp.tileSize * 0.5;		
			}
			
			for (int i = 0; i < partySize; i++) {
				x -= gp.tileSize * 0.5;	
				g2.drawImage(ball_inactive, x, y, null);				
			}

			for (int i = 0; i < activePartySize; i++) {						
				g2.drawImage(ball_active, x, y, null);	
				x += gp.tileSize * 0.5;
			}
		}	
	}
	private void battle_Status(int x, int y, Pokemon fighter, float fontSize) {	
		
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, fontSize));
		
		int width = gp.tileSize;
		int height = (int) (gp.tileSize * 0.6);
		g2.setColor(fighter.getStatus().getColor());
		g2.fillRoundRect(x, y, width, height, 20, 20);
		
		g2.setColor(Color.BLACK);
		x += gp.tileSize * 0.15;
		y += gp.tileSize * 0.48;
		g2.drawString(fighter.getStatus().getAbreviation(), x, y);
	}	
	private void battle_EXP(int x, int y) {
		
		g2.setColor(Color.BLACK);
		x += gp.tileSize * 0.4;
		y += gp.tileSize * 2.3;
		int width = (int) (gp.tileSize * 5.67);
		int height = (int) (gp.tileSize * 0.28);
		g2.fillRoundRect(x, y, width, height, 3, 3);
		
		g2.setColor(Color.WHITE);
		x += 10;
		y += 2;
		width -= 18;
		height -= 5;
		g2.fillRoundRect(x, y, width, height, 3, 3);		
				
		double remainXP = 
				(double) (gp.btlManager.fighter[0].getXP() - gp.btlManager.fighter[0].getBXP()) / 
				(double) (gp.btlManager.fighter[0].getNextXP());
		
		width *= remainXP;	
		g2.setColor(battle_blue);
		g2.fillRect(x, y, width, height);
	}
	
	private void battle_Options() {	
		
		int width = (int) (gp.screenWidth - (gp.tileSize * 0.15));
		int height = (int) (gp.tileSize * 3.5);		
		int x = (int) (gp.tileSize * 0.1);
		int y = (int) (gp.screenHeight - (height * 1.02)); 
		drawSubWindow(x, y, width, height, 12, 10, battle_green, battle_red);
		
		x = gp.tileSize / 2; 
		y = gp.screenHeight - gp.tileSize * 2;
		String text = "What will\n" + gp.btlManager.fighter[0].getName() + " do?";
		
		g2.setColor(Color.BLACK);
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 60F));
		
		for (String line : text.split("\n")) { 
			drawText(line, x, y, battle_white, Color.BLACK);
			y += gp.tileSize;
		}
				
		width = (int) (gp.tileSize * 7.45);
		height = (int) (gp.tileSize * 3.5);
		x = (int) (gp.tileSize * 8.5);
		y = (int) (gp.screenHeight - (height * 1.02)); 		
		drawSubWindow(x, y, width, height, 12, 10, battle_white, battle_gray);
		
		x += gp.tileSize;
		y += gp.tileSize * 1.4;
		height = gp.tileSize;
		
		g2.setColor(Color.BLACK);
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 60F));
		g2.setStroke(new BasicStroke(4));	
		
		text = "FIGHT";
		g2.drawString(text, x, y);
		if (commandNum == 0) {
			width = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
			g2.setColor(battle_red);
			g2.drawRect(x - 4, y - (int) (gp.tileSize * 0.85), width + 4, height);
			g2.setColor(Color.BLACK);
			
			if (gp.keyH.aPressed) {
				gp.keyH.aPressed = false;
				gp.keyH.playCursorSE();			
				
				battleState = battle_Moves;					
				commandNum = 0;				
			}
		}

		x += gp.tileSize * 4;
		text = "BAG";
		g2.drawString(text, x, y);
		if (commandNum == 1) {
			width = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
			g2.setColor(battle_red);
			g2.drawRect(x - 4, y - (int) (gp.tileSize * 0.85), width + 4, height);
			g2.setColor(Color.BLACK);
			
			if (gp.keyH.aPressed) {
				gp.keyH.aPressed = false;
				gp.keyH.playCursorSE();		
				
				bagTab = bag_KeyItems;
				gp.gameState = gp.pauseState;
				pauseState = pause_Bag;
				commandNum = 0;				
			}
		}
		
		x = gp.tileSize * 9 + gp.tileSize / 2;
		y += gp.tileSize * 1.5;
		text = "POKEMON";
		g2.drawString(text, x, y);
		if (commandNum == 2) {
			width = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
			g2.setColor(battle_red);
			g2.drawRect(x - 4, y - (int) (gp.tileSize * 0.85), width + 4, height);
			g2.setColor(Color.BLACK);
			
			if (gp.keyH.aPressed) {
				gp.keyH.aPressed = false;
				gp.keyH.playCursorSE();		
				
				partyDialogue = "Choose a POKEMON.";				
				gp.gameState = gp.pauseState;
				pauseState = pause_Party;
				partyState = party_Main_Select;						
				commandNum = 0;				
			}
		}
		
		x += gp.tileSize * 4;
		text = "RUN";
		g2.drawString(text, x, y);
		if (commandNum == 3) {
			width = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
			g2.setColor(battle_red);
			g2.drawRect(x - 4, y - (int) (gp.tileSize * 0.85), width + 4, height);
			g2.setColor(Color.BLACK);
			
			if (gp.keyH.aPressed) {
				gp.keyH.aPressed = false;
				gp.keyH.playCursorSE();		
				
				gp.btlManager.fightStage = gp.btlManager.fight_Run;
				gp.btlManager.running = true;						
				new Thread(gp.btlManager).start();	
				
				battleState = battle_Dialogue;					
				commandNum = 0;					
			}
		}
		
		if (gp.keyH.upPressed) {	
			gp.keyH.upPressed = false;
			if (commandNum > 1) {
				gp.keyH.playCursorSE();		
				commandNum -= 2;
			}
		}
		if (gp.keyH.downPressed) {	
			gp.keyH.downPressed = false;
			if (commandNum < 2) {
				gp.keyH.playCursorSE();		
				commandNum += 2;
			}
		}
		if (gp.keyH.leftPressed) {	
			gp.keyH.leftPressed = false;
			if (0 < commandNum) {
				gp.keyH.playCursorSE();		
				commandNum--;
			}
		}
		if (gp.keyH.rightPressed) {	
			gp.keyH.rightPressed = false;
			if (commandNum < 3) {
				gp.keyH.playCursorSE();		
				commandNum++;
			}
		}
	}
	private void battle_Moves() {
		
		battle_MovesDesc();
		
		int width = (int) (gp.screenWidth - (gp.tileSize * 4.2));
		int height = (int) (gp.tileSize * 3.5);
		int x = (int) (gp.tileSize * 0.1);
		int y = (int) (gp.screenHeight - (height * 1.02)); 
		
		drawSubWindow(x, y, width, height, 12, 10, battle_white, battle_gray);
				
		x = gp.tileSize / 2;
		y = (int) (gp.screenHeight - gp.tileSize * 2);		
		height = gp.tileSize;
		String text;
		
		g2.setColor(Color.BLACK);
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 50F));		
		g2.setStroke(new BasicStroke(4));
		
		for (int i = 0; i < 4; i++) {			
						
			if (i == 1) {
				x += gp.tileSize * 5.5;		
			}
			else if (i == 2) {
				x = gp.tileSize / 2;
				y += gp.tileSize * 1.2;
			}
			else if (i == 3) {
				x += gp.tileSize * 5.5;
			}
			
			if (gp.btlManager.fighter[0].getMoveSet().size() > i) {
				
				text = gp.btlManager.fighter[0].getMoveSet().get(i).toString();
				
				if (commandNum == i) {					
					width = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
					g2.setColor(battle_red);
					g2.drawRect(x - 4, y - (int) (gp.tileSize * 0.85), width + 4, height);
					g2.setColor(Color.BLACK);
				}
				
				g2.drawString(text, x, y);	
			}
			else {
				g2.drawString("-", x, y);	
			}			
		}
		
		int maxMoves = gp.btlManager.fighter[0].getMoveSet().size() - 1;		
		
		if (gp.keyH.upPressed) {
			gp.keyH.upPressed = false;
			if (commandNum > 0) {
				gp.keyH.playCursorSE();		
				commandNum -= 2;
				if (commandNum < 0) {
					commandNum = 0;
				}
			}
		}
		if (gp.keyH.downPressed) {				
			gp.keyH.downPressed = false;
			if (commandNum < 2) {
				gp.keyH.playCursorSE();		
				commandNum += 2;
				if (maxMoves < commandNum) {
					commandNum = maxMoves;
				}
			}
		}
		if (gp.keyH.leftPressed) {
			gp.keyH.leftPressed = false;
			if (commandNum > 0) {
				gp.keyH.playCursorSE();	
				commandNum--;
			}
		}
		if (gp.keyH.rightPressed) {				
			gp.keyH.rightPressed = false;	
			if (commandNum < maxMoves) {
				gp.keyH.playCursorSE();		
				commandNum++;
			}
		}
		
		if (gp.keyH.aPressed) {		
			gp.keyH.aPressed = false;
			
			Move pMove = gp.btlManager.getPlayerMove(commandNum);
			
			if (pMove.getPP() <= 0) {
				gp.keyH.playErrorSE();
			}
			else {
				gp.keyH.playCursorSE();
				
				gp.btlManager.running = true;			
				new Thread(gp.btlManager).start();	
				gp.btlManager.fightStage = gp.btlManager.fight_Start;
				
				battleState = battle_Dialogue;				
				commandNum = 0;
			}			
		}
		if (gp.keyH.bPressed) {			
			gp.keyH.aPressed = false;
			gp.keyH.playCursorSE();			
			
			battleState = battle_Options;			
			commandNum = 0;
		}
	}	
	private void battle_MovesDesc() {
		
		String text;
		int width = (int) (gp.tileSize * 3.9);
		int height = (int) (gp.tileSize * 3.5);
		int x = (int) (gp.tileSize * 12.1);
		int y = (int) (gp.screenHeight - (height * 1.02)); 		
		
		drawSubWindow(x, y, width, height, 12, 10, gp.btlManager.fighter[0].getMoveSet().get(commandNum).getType().getColor(), battle_gray);
						
		x += gp.tileSize * 0.3;
		y = (int) (gp.screenHeight - gp.tileSize * 2.2);	
		text = "PP " + gp.btlManager.fighter[0].getMoveSet().get(commandNum).getPP() + "/" + 
				gp.btlManager.fighter[0].getMoveSet().get(commandNum).getBPP();
		
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 57F));	
		drawText(text, x, y, battle_white, Color.BLACK);
		
		y += gp.tileSize * 1.5;
		text = gp.btlManager.fighter[0].getMoveSet().get(commandNum).getType().getName();
		drawText(text, x, y, battle_white, Color.BLACK);
	}		
	
	private void battle_LevelUp() {		
		
		Pokemon p = gp.btlManager.fighter[0];
		
		int x = gp.tileSize * 9;
		int y = gp.tileSize * 3;
		int width = (int) (gp.tileSize * 6.8);
		int height = (int) (gp.tileSize * 5.2);
		int frameX;
		String text;
				
		drawSubWindow(x, y, width, height, 15, 4, battle_white, Color.BLACK);
				
		g2.setColor(Color.BLACK);
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 42F));
		
		x += gp.tileSize * 0.5;
		y += gp.tileSize * 0.9;
		text = "BASE HP"; g2.drawString(text, x, y); y += gp.tileSize * 0.8;
		text = "ATTACK"; g2.drawString(text, x, y); y += gp.tileSize * 0.8;
		text = "DEFENSE"; g2.drawString(text, x, y); y += gp.tileSize * 0.8;
		text = "SP. ATK"; g2.drawString(text, x, y); y += gp.tileSize * 0.8;
		text = "SP. DEF"; g2.drawString(text, x, y); y += gp.tileSize * 0.8;
		text = "SPEED"; g2.drawString(text, x, y); y += gp.tileSize * 0.8;
		
		frameX = (int) (gp.tileSize * 15.5);
		y = (int) (gp.tileSize * 3.9);
		text = Integer.toString(p.getBHP()); 
		x = getXforRightAlignText(text, frameX);
		g2.drawString(text, x, y); y += gp.tileSize * 0.8;
		
		text = Integer.toString((int) p.getAttack()); 
		x = getXforRightAlignText(text, frameX);
		g2.drawString(text, x, y); y += gp.tileSize * 0.8;		
		
		text = Integer.toString((int) p.getDefense()); 
		x = getXforRightAlignText(text, frameX);		
		g2.drawString(text, x, y); y += gp.tileSize * 0.8;
		
		text = Integer.toString((int) p.getSpAttack()); 
		x = getXforRightAlignText(text, frameX);
		g2.drawString(text, x, y); y += gp.tileSize * 0.8;
		text = Integer.toString((int) p.getSpDefense()); 
		x = getXforRightAlignText(text, frameX);
		g2.drawString(text, x, y); y += gp.tileSize * 0.8;
		
		text = Integer.toString((int) p.getSpeed()); 
		x = getXforRightAlignText(text, frameX);
		g2.drawString(text, x, y); y += gp.tileSize * 0.8;
	}
	private void battle_Confirmation() {
		
		int x;
		int y;
		int width;
		int height;
		String text;
		
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 60F));				
		x = (int) (gp.tileSize * 12.4);
		y = (int) (gp.tileSize * 5);
		width = (int) (gp.tileSize * 3.45);
		height = (int) (gp.tileSize * 3.2);		
		drawSubWindow(x, y, width, height, 10, 10, battle_white, battle_gray);
		
		g2.setStroke(new BasicStroke(4));		
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 60F));		
		g2.setColor(Color.BLACK);
		width = (int) (gp.tileSize * 2.5);
		height = gp.tileSize;
		
		x += gp.tileSize * 0.5;
		y += gp.tileSize * 1.3;			
		text = "YES";		
		g2.drawString(text, x, y);	
		if (commandNum == 0) {
			g2.setColor(battle_red);
			g2.drawRect(x - 4, y - (int) (gp.tileSize * 0.85), width + 4, height);
			g2.setColor(Color.BLACK);
			
			if (gp.keyH.downPressed) {
				gp.keyH.downPressed = false;
				gp.keyH.playCursorSE();
				commandNum++;
			}
		}		
		
		y += gp.tileSize * 1.3;		
		text = "NO";		
		g2.drawString(text, x, y);	
		if (commandNum == 1) {
			g2.setColor(battle_red);
			g2.drawRect(x - 4, y - (int) (gp.tileSize * 0.85), width + 4, height);
			g2.setColor(Color.BLACK);	
			
			if (gp.keyH.upPressed) {
				gp.keyH.upPressed = false;
				gp.keyH.playCursorSE();
				commandNum--;
			}
		}	
	}
	
	private void battle_Evolve() {
		
		g2.setColor(new Color(234,233,246));  
		g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
		
		int x = (int) (gp.tileSize * 5.5);
		int y = (int) (gp.tileSize * 2.2);
		
		g2.drawImage(evolvePokemon.getFrontSprite(), x, y, null);
	}
	/** END BATTLE SCREEN **/
			
	/** PC SCREEN **/
	private void drawPCScreen() {	
		
		switch (pcState) {
			case pc_Box:
				pc_Box();
				pc_Data();
				break;
			case pc_Select:
				pc_Box();
				pc_Data();
				pc_Select();
				break;
			case pc_Party:
				pc_Box();
				pc_Party();
				break;
			case pc_Summary:				
				if (partyState == party_Skills) {
					party_Skills();
					party_Header(0);	
				}
				else {
					party_Moves();
					party_Header(1);		
				}				
				break;
		}
	}	
	private void pc_Box() {
		
		int x;
		int y;
		int width;
		int height;
		int textX;
		int textY;
		int slotX;
		int slotY;
		String text;
		
		// BACKGROUND
		g2.drawImage(pc_bkg, 0, 0, null);	
		
		// BOX UI
		x = (int) (gp.tileSize * 5.65);
		y = (int) (gp.tileSize * 1.2);
		g2.drawImage(pc_box_1, x, y, null);
		
		// BOX LABEL
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 65f));
		text = "BOX " + (boxTab + 1);
		textX = getXForCenteredTextOnWidth(text, (int) (gp.tileSize * 10.5), x);
		textY = (int) (gp.tileSize * 2.4);
		drawText(text, textX, textY, battle_white, Color.BLACK);			
		if (boxNum == 2 && pcState == pc_Box) {
			
			slotX = (int) (x + gp.tileSize * 4.8);
			slotY = y + (int) (gp.tileSize * 1.3);		
			if (selectedFighter != null) {
				g2.drawImage(selectedFighter.getMenuSprite(), slotX - 15, slotY - 25, null);
			}
			g2.drawImage(pc_cursor_up, slotX, slotY, null);
			
			if (gp.keyH.downPressed) {
				gp.keyH.downPressed = false;
				gp.keyH.playCursorSE();
				boxNum++;
				fighterNum = 0;				
			}
			if (gp.keyH.upPressed) {
				gp.keyH.upPressed = false;
				gp.keyH.playCursorSE();
				boxNum = 0;				
			}
			
			if (gp.keyH.leftPressed) {
				gp.keyH.leftPressed = false;			
				gp.keyH.playCursorSE();
				boxTab--;
				if (boxTab < 0) boxTab = 9;					
			}
			
			if (gp.keyH.rightPressed) {
				gp.keyH.rightPressed = false;				
				gp.keyH.playCursorSE();
				boxTab++;
				if (boxTab > 9) boxTab = 0;						
			}
		}
		
		// PARTY SELECTION
		x = (int) (gp.tileSize * 5.75);
		y = 1;
		width = (int) (gp.tileSize * 4.7);
		height = (int) (gp.tileSize * 1);
		drawSubWindow(x, y, width, height, 5, 10, pc_green, pc_blue);
		
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 45f));
		text = "PARTY";
		textX = getXForCenteredTextOnWidth(text, width, x);
		textY = (int) (gp.tileSize * 0.85);
		drawText(text, textX, textY, battle_white, Color.GRAY);		
		if (boxNum == 0 && pcState == pc_Box) {
			
			slotX = x + gp.tileSize * 2;
			slotY = y + (int) (gp.tileSize * 0.8);		
			if (selectedFighter != null) {
				g2.drawImage(selectedFighter.getMenuSprite(), slotX - 15, slotY - 25, null);
			}
			g2.drawImage(pc_cursor_up, slotX, slotY, null);
			
			if (gp.keyH.downPressed) {
				gp.keyH.downPressed = false;
				gp.keyH.playCursorSE();
				boxNum = 2;				
			}
			
			if (gp.keyH.rightPressed) {
				gp.keyH.rightPressed = false;
				gp.keyH.playCursorSE();
				boxNum = 1;				
			}
			
			if (gp.keyH.aPressed) {
				gp.keyH.aPressed = false;	
				gp.keyH.playCursorSE();
				commandNum = 0;
				fighterNum = 0;
				pcState = pc_Party;
			}
		}
		
		// CLOSE SELECTION
		x += (int) (gp.tileSize * 5.5);
		drawSubWindow(x, y, width, height, 5, 10, pc_green, pc_blue);
		text = "CLOSE";
		textX = getXForCenteredTextOnWidth(text, width, x);
		textY = (int) (gp.tileSize * 0.85);
		drawText(text, textX, textY, battle_white, Color.GRAY);		
		if (boxNum == 1 && pcState == pc_Box) {
			
			slotX = x + gp.tileSize * 2;
			slotY = y + (int) (gp.tileSize * 0.8);	
			if (selectedFighter != null) {
				g2.drawImage(selectedFighter.getMenuSprite(), slotX - 15, slotY - 25, null);
			}
			g2.drawImage(pc_cursor_up, slotX, slotY, null);
			
			if (gp.keyH.downPressed) {
				gp.keyH.downPressed = false;
				gp.keyH.playCursorSE();
				boxNum = 2;				
			}
			
			if (gp.keyH.leftPressed) {
				gp.keyH.leftPressed = false;	
				gp.keyH.playCursorSE();
				boxNum = 0;				
			}
			
			if (gp.keyH.aPressed) {
				gp.keyH.aPressed = false;	
				
				if (selectedFighter == null) {
					gp.playSE(gp.world_SE, "pc-exit");
					commandNum = 0;
					fighterNum = 0;
					boxNum = 3;
					boxTab = 0;
					pcState = 0;
					gp.gameState = gp.playState;					
				}
				else {
					gp.keyH.playErrorSE();
				}
			}
		}
		
		if (pcState == pc_Box) {
			if (gp.keyH.bPressed) {
				gp.keyH.bPressed = false;
				
				if (selectedFighter == null) {					
					gp.playSE(gp.world_SE, "pc-exit");					
					commandNum = 0;
					fighterNum = 0;
					boxNum = 3;
					pcState = 0;
					boxTab = 0;
					gp.gameState = gp.playState;					
				}
				else {
					gp.keyH.playErrorSE();
				}
			}
		}
		
		pc_Pokemon();
	}
	private void pc_Pokemon() {
						
		int x;
		int y;
		int slotX;
		int slotY;
		Pokemon fighter = null;
		
		x = (int) (gp.tileSize * 5.6); 
		y = (int) (gp.tileSize * 2.9);					
		for (int i = 0; i < 30; i++) {	
			
			if (gp.player.pcParty[boxTab][i] != null) {
				fighter = gp.player.pcParty[boxTab][i];	
				g2.drawImage(fighter.getMenuSprite(), x, y, null);		
			}	
			
			if (fighterNum == i && boxNum > 2) {
				slotX = x + (int) (gp.tileSize * 0.5);
				slotY = y - (int) (gp.tileSize * 0.7);		
				if (selectedFighter != null) {
					g2.drawImage(selectedFighter.getMenuSprite(), slotX - 15, slotY, null);
				}				
				g2.drawImage(pc_cursor, slotX, slotY, null);
			}
			
			x += gp.tileSize * 1.7;
			
			if (i == 5 || i == 11 || i == 17 || i == 23) {
				x = (int) (gp.tileSize * 5.6); 
				y += gp.tileSize * 1.7;
			}
		}				
		if (boxNum > 2 && pcState == pc_Box) {
			if (gp.keyH.upPressed) {
				gp.keyH.upPressed = false;
				gp.keyH.playCursorSE();
				if (fighterNum - 6 >= 0) {
					fighterNum -= 6;
				}
				else {
					fighterNum = 0;
					boxNum = 2;
				}				
			}
			if (gp.keyH.downPressed) {
				gp.keyH.downPressed = false;
				if (fighterNum + 6 < 30) {
					gp.keyH.playCursorSE();
					fighterNum += 6;					
				}
			}
			if (gp.keyH.leftPressed) {
				gp.keyH.leftPressed = false;
				if (fighterNum > 0) {
					gp.keyH.playCursorSE();
					fighterNum--;					
				}				
			}
			if (gp.keyH.rightPressed) {
				gp.keyH.rightPressed = false;
				if (fighterNum < 29) {
					gp.keyH.playCursorSE();
					fighterNum++;					
				}
				
			}
			if (gp.keyH.aPressed) {
				gp.keyH.aPressed = false;
				
				fighter = gp.player.pcParty[boxTab][fighterNum];	
				
				if (selectedFighter == null) {										
					if (fighter != null) {
						gp.keyH.playCursorSE();
						commandNum = 0;						
						pcState = pc_Select;
					}	
					else {
						gp.keyH.playErrorSE();
					}
				}
				else {
					if (fighter == null) {
						gp.player.pcParty[boxTab][fighterNum] = selectedFighter;
						selectedFighter = null;
					}	
					else {				
						gp.player.pcParty[boxTab][fighterNum] = selectedFighter;		
						selectedFighter = fighter;	
					}
				}
			}		
		}		
	}
	private void pc_Data() {
		
		int x;
		int y;
		int width;
		int height;
		int textX;
		int textY;
		String text;
		Pokemon fighter = null;
		
		g2.drawImage(pc_data, 0, (int) (gp.tileSize * 0.25), null);	
		
		x = (int) (gp.tileSize * 0.75); 
		y = (int) (gp.tileSize * 1.6);
		
		if (selectedFighter != null) {
			fighter = selectedFighter;
		}		
		else if (gp.player.pcParty[boxTab][fighterNum] != null && boxNum > 2) {
			fighter = gp.player.pcParty[boxTab][fighterNum];
		}
			
		if (fighter != null) {
			
			g2.drawImage(fighter.getFrontSprite(), x, y, gp.tileSize * 4, gp.tileSize * 4, null);	
			
			x = (int) (gp.tileSize * 0.3); 
			y += gp.tileSize * 6.2;
			g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 62f));
			text = fighter.getName();
			drawText(text, x, y, battle_white, Color.BLACK);
			
			y += gp.tileSize;
			g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 55f));
			text = "Lv" + fighter.getLevel();
			drawText(text, x, y, battle_white, Color.BLACK);
			
			int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();		
			x += length + 3;
			g2.setColor(fighter.getSexColor());	
			g2.drawString("" + fighter.getSex(), x, y);	
			
			x = (int) (gp.tileSize * 0.3); 
			y += (int) (gp.tileSize * 0.7);
			width = (int) (gp.tileSize * 2);
			height = (int) (gp.tileSize * 0.9);
			if (fighter.getTypes() == null) {				
				drawSubWindow(x, y, width, height, 10, 3, fighter.getType().getColor(), Color.BLACK);											
				g2.setFont(g2.getFont().deriveFont(Font.BOLD, 30F));			
				text = fighter.getType().getName();
				textX = getXForCenteredTextOnWidth(text, width, x + 5);
				textY = (int) (y + (gp.tileSize * 0.70));			
				drawText(text, textX, textY, battle_white, Color.BLACK);
			}
			else {
				for (Type t : fighter.getTypes()) {					
					drawSubWindow(x, y, width, height, 10, 3, t.getColor(), Color.BLACK);											
					g2.setFont(g2.getFont().deriveFont(Font.BOLD, 30F));			
					text = t.getName();
					textX = getXForCenteredTextOnWidth(text, width, x + 5);
					textY = (int) (y + (gp.tileSize * 0.70));			
					drawText(text, textX, textY, battle_white, Color.BLACK);
					x += gp.tileSize * 2.7;
				}
			}				
		}
	}		
	private void pc_Select() {
		
		int x;
		int y;
		int width;
		int height;
		String text;
		
		x = (int) (gp.tileSize * 12);
		y = (int) (gp.tileSize * 6.45);
		width = (int) (gp.tileSize * 3.9);
		height = (int) (gp.tileSize * 5.2);
		
		drawSubWindow(x, y, width, height, 5, 5, battle_white, Color.BLACK);
		
		x += gp.tileSize * 0.3;
		y += gp.tileSize * 1.2;
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 60f));		
		text = "MOVE";
		drawText(text, x, y, Color.BLACK, Color.LIGHT_GRAY);
		
		y += gp.tileSize * 1.25;
		text = "SUMMARY";
		drawText(text, x, y, Color.BLACK, Color.LIGHT_GRAY);
		
		y += gp.tileSize * 1.25;
		text = "RELEASE";
		drawText(text, x, y, Color.BLACK, Color.LIGHT_GRAY);
		
		y += gp.tileSize * 1.25;
		text = "CANCEL";		
		drawText(text, x, y, Color.BLACK, Color.LIGHT_GRAY);
		
		// MOVE
		g2.setColor(battle_red);
		x -= gp.tileSize * 0.28;
		y -= gp.tileSize * 4.7;
		height = (int) (gp.tileSize * 1.1);			
		if (commandNum == 0) {
		
			g2.drawRoundRect(x, y, width, height, 4, 4);
			
			if (gp.keyH.aPressed) {
				gp.keyH.aPressed = false;
				gp.keyH.playCursorSE();
				
				selectedFighter = gp.player.pcParty[boxTab][fighterNum];
				gp.player.pcParty[boxTab][fighterNum] = null;
				
				commandNum = 0;
				pcState = pc_Box;
			}
		}
		// SUMMARY
		y += gp.tileSize * 1.25;
		if (commandNum == 1) {
			
			g2.drawRoundRect(x, y, width, height, 4, 4);
			
			if (gp.keyH.aPressed) {
				gp.keyH.aPressed = false;
				gp.keyH.playCursorSE();
				commandNum = 0;				
				partyState = party_Skills;		
				pcState = pc_Summary;						
			}
		}
		// RELEASE
		y += gp.tileSize * 1.25;
		if (commandNum == 2) {
			
			g2.drawRoundRect(x, y, width, height, 4, 4);
			
			if (gp.keyH.aPressed) {
				gp.keyH.aPressed = false;
				gp.keyH.playCursorSE();
				commandNum = 0;				
			}
		}		
		// CANCEL
		y += gp.tileSize * 1.25;
		if (commandNum == 3) {
			
			g2.drawRoundRect(x, y, width, height, 4, 4);
						
			if (gp.keyH.aPressed) {
				gp.keyH.aPressed = false;
				gp.keyH.playCloseSE();				
				commandNum = 0;
				pcState = pc_Box;
			}
		}
		
		if (gp.keyH.downPressed) {
			gp.keyH.downPressed = false;
			if (commandNum < 3) {
				gp.keyH.playCursorSE();
				commandNum++;				
			}
		}
		if (gp.keyH.upPressed) {
			gp.keyH.upPressed = false;
			if (commandNum > 0) {
				gp.keyH.playCursorSE();
				commandNum--;				
			}
		}
		if (gp.keyH.bPressed) {
			gp.keyH.bPressed = false;
			gp.keyH.playCloseSE();
			commandNum = 0;			
			pcState = pc_Box;
		}
	}
	private void pc_Party() {
		
		int x;
		int y;
		int slotX;
		int slotY;
		Pokemon fighter = null;
		
		g2.drawImage(pc_party, 0, 0, null);		
		
		x = (int) (gp.tileSize * 0.4); 
		y = (int) (gp.tileSize * 3.65);		
		g2.drawImage(gp.player.pokeParty.get(0).getMenuSprite(), x, y, null);
		
		if (fighterNum == 0) {			
			slotX = x + (int) (gp.tileSize * 0.5);
			slotY = y - (int) (gp.tileSize * 0.4);		
			if (selectedFighter != null) {
				g2.drawImage(selectedFighter.getMenuSprite(), slotX - 15, slotY, null);
			}				
			g2.drawImage(pc_cursor, slotX, slotY, null);
		}
				
		x = (int) (gp.tileSize * 3.13); 
		y = (int) (gp.tileSize * 0.38);	
		for (int i = 1; i < 6; i++) {
							
			if (gp.player.pokeParty.size() > i && gp.player.pokeParty.get(i) != null) {					
				g2.drawImage(gp.player.pokeParty.get(i).getMenuSprite(), x, y, null);												
			}
			if (fighterNum == i) {					
				slotX = x + (int) (gp.tileSize * 0.5);
				slotY = y - (int) (gp.tileSize * 0.4);		
				if (selectedFighter != null) {
					g2.drawImage(selectedFighter.getMenuSprite(), slotX - 15, slotY, null);
				}				
				g2.drawImage(pc_cursor, slotX, slotY, null);
			}		
			y += gp.tileSize * 1.7;					
		}	
			
		if (fighterNum == 6) {
			slotX = (int) (gp.tileSize * 3.63);
			slotY = (int) (gp.tileSize * 8.8);		
			if (selectedFighter != null) {
				g2.drawImage(selectedFighter.getMenuSprite(), slotX - 15, slotY, null);
			}				
			g2.drawImage(pc_cursor, slotX, slotY, null);	
		}
		
		if (fighterNum == 0) {
			if (gp.keyH.upPressed || gp.keyH.downPressed || gp.keyH.rightPressed) {
				gp.keyH.upPressed = false;
				gp.keyH.downPressed = false;
				gp.keyH.rightPressed = false;
				gp.keyH.playCursorSE();
				fighterNum = 1;							
			}
		}
		else {
			if (gp.keyH.downPressed) {
				gp.keyH.downPressed = false;
				if (fighterNum < 6) {
					gp.keyH.playCursorSE();
					fighterNum++;					
				}
			}
			if (gp.keyH.upPressed) {
				gp.keyH.upPressed = false;
				if (fighterNum > 0) {
					gp.keyH.playCursorSE();
					fighterNum--;					
				}
			}
			if (gp.keyH.leftPressed) {
				gp.keyH.leftPressed = false;
				gp.keyH.playCursorSE();
				fighterNum = 0;						
			}
		}
			
		if (gp.keyH.aPressed) {
			gp.keyH.aPressed = false;
							
			if (fighterNum == 6) {				
				gp.keyH.playCloseSE();
				commandNum = 0;
				fighterNum = 0;		
				pcState = pc_Box;				
			}
			else {	
				if (gp.player.pokeParty.size() > fighterNum) {						
					fighter = gp.player.pokeParty.get(fighterNum);			
				}
				
				if (selectedFighter == null) {										
					if (fighter != null) {
						if (gp.player.pokeParty.size() > 1) {
							selectedFighter = fighter;
							gp.player.pokeParty.remove(fighterNum);	
						}
						else {
							gp.keyH.playErrorSE();
						}						
					}	
					else {
						gp.keyH.playErrorSE();
					}
				}
				else {
					if (fighter == null) {
						if (gp.player.pokeParty.size() <= fighterNum) {
							gp.player.pokeParty.add(selectedFighter);
						}
						else {
							gp.player.pokeParty.set(fighterNum, selectedFighter);	
						}							
						selectedFighter = null;
					}	
					else {	
						gp.player.pokeParty.set(fighterNum, selectedFighter);
						selectedFighter = fighter;					
					}
				}					
			}			
		}
		
		if (gp.keyH.bPressed) {
			gp.keyH.bPressed = false;
			gp.keyH.playCloseSE();
			commandNum = 0;
			fighterNum = 0;				
			pcState = pc_Box;
		}
	}
	/** END PC SCREEN **/
	
	/** TRANSITION SCREEN **/
	private void drawTransitionScreen() {
		
		// DARKEN SCREEN
		tCounter++;
		g2.setColor(new Color(0,0,0, tCounter * 5)); 
		g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
		
		// STOP DARKENING SCREEN
		if (tCounter == 50) {
			tCounter = 0;			
			
			if (gp.btlManager.running) {				
				startBattle();				
			}
			else {
				gp.player.direction = tDirection;
				gp.currentMap = gp.eHandler.tempMap;
				
				gp.player.worldX = gp.tileSize * gp.eHandler.tempCol;
				gp.player.worldY = gp.tileSize * gp.eHandler.tempRow;
				
				gp.player.defaultWorldX = gp.player.worldX;
				gp.player.defaultWorldY = gp.player.worldY;
				
				gp.eHandler.previousEventX = gp.player.worldX;
				gp.eHandler.previousEventY = gp.player.worldY;
				
				gp.changeArea();
				
				if (tMoving) {
					gp.player.moving = true;
					tMoving = false;
				}
				
				gp.gameState = gp.playState;
			}
		}		
	}	
	public void startBattle() {
		battleDialogue = "";
		battleState = battle_Encounter;
		
		fighter_one_X = fighter_one_startX;
		fighter_two_X = fighter_two_startX;
		fighter_one_Y = fighter_one_startY;
		fighter_two_Y = fighter_two_startY;		
		
		new Thread(gp.btlManager).start();	
		
		gp.gameState = gp.battleState;	
	}
	/** END TRANSITION SCREEN **/
	
	// MISC
	public void resetFighterPositions() {
		fighter_one_X = fighter_one_startX;
		fighter_two_X = fighter_two_startX;
		fighter_one_Y = fighter_one_startY;
		fighter_two_Y = fighter_two_startY;		
	}
	public void drawText(String text, int x, int y, Color primary, Color shadow) {
		g2.setColor(shadow);	
		g2.drawString(text, x, y);	
		g2.setColor(primary);
		g2.drawString(text, x-2, y-2);		
	}
	public void drawSubWindow(int x, int y, int width, int height, int curve, int borderStroke, 
			Color fillCollor, Color borderColor) {
		
		g2.setColor(fillCollor);
		g2.fillRoundRect(x, y, width, height, curve, curve);
		
		g2.setColor(borderColor);
		g2.setStroke(new BasicStroke(borderStroke));
		g2.drawRoundRect(x, y, width, height, curve, curve);
	}
	public int getXforRightAlignText(String text, int tailX) {
		int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
		int x = tailX - length;
		return x;
	}	
	private int getXForCenteredTextOnWidth(String text, int width, int x) {		
		FontMetrics fm = g2.getFontMetrics();
		int stringWidth = fm.stringWidth(text);
		int centeredX = (width - stringWidth) / 2;		
		return centeredX + x;
	}
	private BufferedImage setup(String imagePath, int width, int height) {
		
		UtilityTool utility = new UtilityTool();
		BufferedImage image = null;
		
		try {
			image = ImageIO.read(getClass().getResourceAsStream(imagePath + ".png"));
			image = utility.scaleImage(image, width, height);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		return image;
	}	
	
	private void playPurchaseSE() {
		gp.playSE(gp.entity_SE, "purchase");
	}
}