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

import javax.imageio.ImageIO;

import entity.Entity;
import moves.Move;
import pokemon.Pokemon;
import properties.Type;

public class UI {
	
	// UI FONTS
	private GamePanel gp;
	private Graphics2D g2;	
	public Font PK_DS;
	
	// COLORS
	private Color battle_white = new Color(234,233,246);
	private Color battle_gray = new Color(135,131,156);
	private Color battle_green = new Color(111,163,161);
	private Color battle_red = new Color(165,71,74);	
	private Color battle_yellow = new Color(250,254,219);
	private Color hp_green = new Color(105,233,160);
	private Color hp_yellow = new Color(222,202,43);
	private Color hp_red = new Color(200,65,65);
	private Color party_green = new Color(81,109,69);
	private Color party_blue = new Color(71,174,181);
	private Color party_red = new Color(245,126,63);
	private Color party_gray = new Color(136,152,168);
	private Color party_faint = new Color(181,87,71);
		
	// DIALOGUE HANDLER	
	public Entity npc;
	public String currentDialogue = "";
	private ArrayList<String> battleDialogue;
	public String combinedText = "";
	public int dialogueCounter = 0;	
	private int dialogueIndex = 0;
	public int charIndex = 0;	
	public int textSpeed = 2;
	public int battleTextSpeed = 1;
	private int dialogueTimer = 0;
	public int dialogueTimerMax = 90;
	private boolean canSkip = false;
	private BufferedImage dialogue_next;
	
	public ArrayList<Integer> seTimer;
	private ArrayList<Integer> category_SE;
	private ArrayList<Integer> record_SE;
	
	// FIGHTER HP
	public int fighter_one_HP;
	public int fighter_two_HP;
	private int hpCounter = 0;
	private BufferedImage ball_empty, ball_active, ball_inactive;
	
	public int hpSpeed_one = 1;
	public int hpSpeed_two = 1;
	
	public boolean fighterReady = false;	
	
	public int commandNum = 0;
	public int fighterNum = 0;
	
	public int partySubState;
	public final int party_Main = 1;
	public final int party_Main_Select = 2;
	public final int party_Stats = 3;
	public final int party_Moves = 4;
	
	public int battleSubState;
	public final int battle_Encounter = 1;
	public final int battle_Start = 2;
	public final int battle_Dialogue = 3;
	public final int battle_Options = 4;
	public final int battle_Moves = 5;	
	public final int battle_Swap = 6;
	
	public UI(GamePanel gp) {
		this.gp = gp;
		
		battleDialogue = new ArrayList<String>();
		dialogue_next = ball_empty = setup("/ui/dialogue/advance", (int) (gp.tileSize * 0.5), (int) (gp.tileSize * 0.5));
		
		seTimer = new ArrayList<Integer>();
		category_SE = new ArrayList<Integer>();
		record_SE = new ArrayList<Integer>();
		
		ball_empty = setup("/ui/battle/ball-empty", (int) (gp.tileSize * 0.5), (int) (gp.tileSize * 0.5));
		ball_active = setup("/ui/battle/ball-active", (int) (gp.tileSize * 0.5), (int) (gp.tileSize * 0.5));
		ball_inactive = setup("/ui/battle/ball-inactive", (int) (gp.tileSize * 0.5), (int) (gp.tileSize * 0.5));
		
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
		else if (gp.gameState == gp.dialogueState) {
			drawHUD();
			drawDialogueScreen();
		}
		else if (gp.gameState == gp.battleState) {
			drawBattleScreen();
		}
		else if (gp.gameState == gp.partyState) {
			drawPartyScreen();
		}
	}
	
	// HUD	
	private void drawHUD() {
		
		// DEBUG
		if (gp.keyH.debug) {				
			drawDebug();
		}
	}
	private void drawDebug() {
		
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
	public void drawDialogueScreen() {
						
		int x = (int) (gp.tileSize * 2);
		int y = gp.tileSize * 9;
		int width =(int) (gp.tileSize * 12);
		int height = (int) (gp.tileSize * 2.5);
		drawSubWindow(x, y, width, height, 25, 10, battle_white, party_green);
		
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
				gp.btlManager.trainer[1] = npc;
				gp.btlManager.setBattle(gp.btlManager.trainerBattle);
				
				skipDialogue();
				
				gp.gameState = gp.battleState;
			}
			else {
				// playDialogueFinishSE();				
				gp.gameState = gp.playState;
			}	
		}				

		x += gp.tileSize * 0.8;
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
  		
  		skipDialogue();
	}
	private void skipDialogue() {		
		if (gp.keyH.aPressed && canSkip) {
			gp.keyH.playCursorSE();
			
			canSkip = false;
			charIndex = 0;
			combinedText = "";
			currentDialogue = "";
			
			npc.dialogueIndex++;
			gp.keyH.aPressed = false;			
		}
	}
	public void resetDialogue() {	
		dialogueCounter = 0;
		charIndex = 0;		
		combinedText = "";
		currentDialogue = "";
		npc = null;		
		canSkip = false;		
	}
	
	// PARTY SCREEN
	private void drawPartyScreen() {
		
		switch(partySubState) {
			case party_Main:
				drawParty_Main();
				break;
			case party_Main_Select:
				drawParty_Main();
				drawParty_Main_Select();
				break;
			case party_Stats:				
				drawParty_Skills();
				drawHeader(0);
				break;
			case party_Moves:
				drawParty_Moves();
				drawHeader(1);
				break;
		}
	}			
	
	private void drawParty_Main() {
		
		g2.setColor(party_green);  
		g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
				
		int x;
		int y;
		int width;
		int height;
		String text;
		Pokemon fighter;
		Color boxColor;
		
		x = (int) (gp.tileSize * 0.5);
		y = (int) (gp.tileSize * 2.8); 
		width = (int) (gp.tileSize * 5.5);
		height = (int) (gp.tileSize * 3.2);
		fighter = gp.btlManager.trainer[0].pokeParty.get(0);
		
		if (fighter.isAlive()) { boxColor = party_blue; }
		else { boxColor = party_faint; }
		
		if (fighterNum == 0) { drawSubWindow(x, y, width, height, 3, 5, boxColor, party_red); }
		else { drawSubWindow(x, y, width, height, 3, 3, boxColor, Color.BLACK); }
		
		drawPartyBox(x, y, fighter, true);
		
		x = (int) (gp.tileSize * 6.5);
		y = (int) (gp.tileSize * 0.5); 
		width = gp.tileSize * 9;
		height = (int) (gp.tileSize * 1.7);
	
		for (int i = 1; i < 6; i++) {	
			
			if (gp.btlManager.trainer[0].pokeParty.size() > i) {		
				
				fighter = gp.btlManager.trainer[0].pokeParty.get(i);
				
				if (fighter.isAlive()) { boxColor = party_blue; }
				else { boxColor = party_faint; }
				
				if (fighterNum == i) { drawSubWindow(x, y, width, height, 3, 5, boxColor, party_red); }
				else { drawSubWindow(x, y, width, height, 3, 3, boxColor, Color.BLACK);	}				
				
				drawPartyBox((int) (x + gp.tileSize * 0.15), (int) (y - gp.tileSize * 0.15), fighter, false);				
			}
			else {
				if (fighterNum == i) { drawSubWindow(x, y, width, height, 3, 5, party_blue, party_red); }
				else { drawSubWindow(x, y, width, height, 3, 3, party_blue, Color.BLACK); }
			}
			
			y += gp.tileSize * 1.9;
		}
		
		x = (int) (gp.tileSize * 0.3);
		y = (int) (gp.tileSize * 10);
		width = gp.tileSize * 11;
		height = (int) (gp.tileSize * 1.6);
		drawSubWindow(x, y, width, height, 3, 5, battle_white, Color.BLACK);	
		
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 57F));
		x += gp.tileSize * 0.3;
		y += gp.tileSize * 1.15;
		text = "CHOOSE A POKEMON";	
		drawText(text, x, y, Color.BLACK, Color.LIGHT_GRAY);
		
		x += width + (gp.tileSize * 0.4);
		y = (int) (gp.tileSize * 10.62);
		width = (int) (gp.tileSize * 3.5);
		height = (int) (gp.tileSize * 1);			
		
		if (fighterNum == 6) { drawSubWindow(x, y, width, height, 5, 5, party_blue, party_red); }
		else { drawSubWindow(x, y, width, height, 5, 3, party_blue, Color.BLACK); }
		
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 45F));
		x += gp.tileSize * 0.8;
		y += gp.tileSize * 0.8;
		text = "CANCEL";
		drawText(text, x, y, battle_white, Color.BLACK);
		
		if (partySubState == party_Main) {			
			if (gp.keyH.aPressed) {
				gp.keyH.aPressed = false;
				
				if (fighterNum == 6) {					
					gp.gameState = gp.battleState;
					battleSubState = battle_Options;
					fighterNum = 0;
				}
				else if (gp.btlManager.trainer[0].pokeParty.size() > fighterNum){							
					partySubState = party_Main_Select;					
				}	
			}
			if (gp.keyH.bPressed) {			
				gp.keyH.bPressed = false;
				fighterNum = 6;				
			}
		}
	}
	private void drawParty_Main_Select() {
		
		int x;
		int y;
		int width;
		int height;
		
		x = (int) (gp.tileSize * 12.35);
		y = (int) (gp.tileSize * 8.85);
		width = (int) (gp.tileSize * 2.8);
		height = (int) (gp.tileSize * 2.78);		
		drawSubWindow(x, y, width, height, 4, 4, battle_white, Color.BLACK);
		
		x += gp.tileSize * 0.4;
		y += gp.tileSize * 0.72;
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 40F));	
		drawText("SELECT", x, y, Color.BLACK, Color.LIGHT_GRAY);
		
		y += gp.tileSize * 0.95;
		drawText("INFO", x, y, Color.BLACK, Color.LIGHT_GRAY);
		
		y += gp.tileSize * 0.95;
		drawText("CANCEL", x, y, Color.BLACK, Color.LIGHT_GRAY);
		
		x -= gp.tileSize * 0.38;
		y -= gp.tileSize * 2.58;
		height = (int) (gp.tileSize * 0.9);		
		if (commandNum == 0) {
			g2.setColor(battle_red);
			g2.drawRoundRect(x, y, width, height, 4, 4);
			
			if (gp.keyH.aPressed) {
				gp.keyH.aPressed = false;
				
				// NEW FIGHTER SELECTED
				if (gp.btlManager.swapPokemon(fighterNum)) {
					
					if (gp.btlManager.fighter[0].isAlive()) {
						gp.btlManager.fighter[0] = gp.btlManager.newFighter[0];								
						gp.btlManager.fightStage = gp.btlManager.fightStage_Swap;
					}
					
					gp.gameState = gp.battleState;
					battleSubState = battle_Dialogue;
					commandNum = 0;
					fighterNum = 0;					
				}	
				
				// UNABLE TO SELECT FIGHTER
				else {
					
				}				
			}
		}
		
		y += gp.tileSize * 0.95;
		if (commandNum == 1) {
			g2.setColor(battle_red);
			g2.drawRoundRect(x, y, width, height, 4, 4);
			
			if (gp.keyH.aPressed) {
				gp.keyH.aPressed = false;					
				partySubState = party_Stats;	
				gp.playSE(3, gp.btlManager.trainer[0].pokeParty.get(fighterNum).toString());  
				commandNum = 0;			
			}
		}
		
		y += gp.tileSize * 0.95;
		if (commandNum == 2) {
			g2.setColor(battle_red);
			g2.drawRoundRect(x, y, width, height, 4, 4);
			
			if (gp.keyH.aPressed) {
				gp.keyH.aPressed = false;					
				partySubState = party_Main;	
				commandNum = 0;	
			}
		}
		
		if (gp.keyH.bPressed) {			
			gp.keyH.bPressed = false;
			partySubState = party_Main;
			commandNum = 0;	
		}
	}
	private void drawPartyBox(int x, int y, Pokemon fighter, boolean main) {
		
		g2.drawImage(fighter.getMenuSprite(), x, y, null);
		
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 35F));	
		x += gp.tileSize * 2;
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
		
		drawFighterHP_Party(x, y, fighter);
	}
	private void drawFighterHP_Party(int x, int y, Pokemon fighter) {
		
		int width = (int) (gp.tileSize * 4.2);
		int height = (int) (gp.tileSize * 0.5);
		
		g2.setColor(Color.BLACK);
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 25F));
		g2.setStroke(new BasicStroke(2));
		g2.fillRoundRect(x, y, width, height, 30, 30);
		
		g2.setColor(Color.WHITE);	
		x += (int) gp.tileSize * 0.25;
		y += gp.tileSize * 0.42;
		g2.drawString("HP", x, y);		
				
		double remainHP = (double)fighter.getHP() / (double)fighter.getBHP();	
		
		if (remainHP >= .50) g2.setColor(hp_green);
		else if (remainHP >= .25) g2.setColor(hp_yellow);
		else g2.setColor(hp_red);
		
		x += gp.tileSize * 0.55;
		y -= gp.tileSize * 0.3;
		width = (int) (gp.tileSize * 3.3);
		width *= remainHP;	
		height = (int) (gp.tileSize * 0.28);								
		g2.fillRoundRect(x, y + 1, width, height, 15, 15);			
		
		width = (int) (gp.tileSize * 3.3);
		g2.setColor(Color.WHITE);
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 30F));
		g2.setStroke(new BasicStroke(2));
		g2.drawRoundRect(x, y, width, height, 15, 15);	
		
		if (fighter.getStatus() != null) {
			drawFighterStatus((int) (x - (gp.tileSize * 0.6)), (int) (y + (gp.tileSize * 0.45)), fighter, 30F);
			g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 30F));
		}
		
		g2.setColor(Color.WHITE);
		String text = fighter.getHP() + " / " + fighter.getBHP();
		x = getXforRightAlignText(text, (int) (x + gp.tileSize * 3.15));		
		y += gp.tileSize * 0.9;
		g2.drawString(text, x, y);		
	}
	
	private void drawParty_Skills() {
		
		drawParty_Info();
		
		int x;
		int y;
		String text;
		
		int frameX = (int) (gp.tileSize * 7.9);
		int frameY = (int) (gp.tileSize * 2.9);
						
		x = frameX;
		y = frameY;		
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 40F));	

		text = "HP"; drawText(text, x, y, Color.WHITE, Color.BLACK); y += gp.tileSize * 0.9;
		text = "ATTACK"; drawText(text, x, y, Color.WHITE, Color.BLACK); y += gp.tileSize * 0.9;
		text = "DEFENSE"; drawText(text, x, y, Color.WHITE, Color.BLACK); y += gp.tileSize * 0.9;
		text = "SP. ATK"; drawText(text, x, y, Color.WHITE, Color.BLACK); y += gp.tileSize * 0.9;
		text = "SP. DEF"; drawText(text, x, y, Color.WHITE, Color.BLACK); y += gp.tileSize * 0.9;
		text = "SPEED"; drawText(text, x, y, Color.WHITE, Color.BLACK); y += gp.tileSize * 0.9;
		
		Pokemon fighter = gp.btlManager.trainer[0].pokeParty.get(fighterNum);
		
		x = frameX += gp.tileSize * 5;
		y = frameY;		
		text = fighter.getHP() + "/" + fighter.getBHP(); 
		frameX = getXforRightAlignText(text, x);	
		drawText(text, frameX, y, Color.WHITE, Color.BLACK); y += gp.tileSize * 0.9;
		text = Integer.toString((int) fighter.getAttack()); 
		frameX = getXforRightAlignText(text, x);	
		drawText(text, frameX, y, Color.WHITE, Color.BLACK); y += gp.tileSize * 0.9;
		text = Integer.toString((int) fighter.getDefense()); 
		frameX = getXforRightAlignText(text, x);	
		drawText(text, frameX, y, Color.WHITE, Color.BLACK); y += gp.tileSize * 0.9;
		text = Integer.toString((int) fighter.getSpAttack()); 
		frameX = getXforRightAlignText(text, x);	
		drawText(text, frameX, y, Color.WHITE, Color.BLACK); y += gp.tileSize * 0.9;
		text = Integer.toString((int) fighter.getSpDefense()); 
		frameX = getXforRightAlignText(text, x);	
		drawText(text, frameX, y, Color.WHITE, Color.BLACK); y += gp.tileSize * 0.9;
		text = Integer.toString((int) fighter.getSpeed()); 
		frameX = getXforRightAlignText(text, x);	
		drawText(text, frameX, y, Color.WHITE, Color.BLACK); y += gp.tileSize * 0.9;
				
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 38F));	
		int textX;
		int textY = (int) (y + (gp.tileSize * 2.95));	
		int width = (int) (gp.tileSize * 2.7);
		int height = gp.tileSize;		
		
		if (fighter.getTypes() != null) {			
			x = (int) (gp.tileSize * 0.85);
			y = (int) (gp.tileSize * 10.5);			
			for (Type t : fighter.getTypes()) {				
				drawSubWindow(x, y, width, height, 10, 3, t.getColor(), Color.BLACK);		
										
				text = t.getName();
				textX = getXForCenteredTextOnWidth(text, width, x + 5);						
				drawText(text, textX, textY, battle_white, Color.BLACK);
				
				x += gp.tileSize * 3.1;
			}			
		}
		else {			
			x = (int) (gp.tileSize * 2.4);
			y = (int) (gp.tileSize * 10.5);			
			drawSubWindow(x, y, width, height, 10, 3, fighter.getType().getColor(), Color.BLACK);		
						
			text = fighter.getType().getName();
			textX = getXForCenteredTextOnWidth(text, width, x + 5);
			drawText(text, textX, textY, battle_white, Color.BLACK);	
		}
		
		x = (int) (gp.tileSize * 7.8);
		y = (int) (gp.tileSize * 8.6);
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 42F));
		text = "ABILITY";
		drawText(text, x, y, battle_white, Color.BLACK);	
		
		if (gp.keyH.rightPressed) {
			commandNum = 0;
			partySubState = party_Moves;				
		}
		if (gp.keyH.bPressed) {
			commandNum = 0;
			partySubState = party_Main;
			gp.keyH.bPressed = false;
		}
	}
	private void drawParty_Moves() {
		
		drawParty_Info();
		
		int x;
		int y;
		int width;
		int height;
		int textX;
		int textY;
		String text;
		Pokemon fighter = gp.btlManager.trainer[0].pokeParty.get(fighterNum);
								
		int slotX = (int) (gp.tileSize * 7.5);
		int slotY;
		int slotWidth = (int) (gp.tileSize * 8.38);
		int slotHeight = gp.tileSize + 8;
		
		int frameX = (int) (gp.tileSize * 7.9);
		int frameY = (int) (gp.tileSize * 2.4);
		
		int tempX = (int) (gp.tileSize * 0.4);
		int tempY = (int) (gp.tileSize * 10.5);
		
		x = 5;
		y = (int) (gp.tileSize * 9.5);		
		height = (int) (gp.tileSize * 2.4);
		width = (int) (gp.tileSize * 5.5);
		g2.setColor(party_gray);
		g2.fillRoundRect(x, y, width, height, 4, 4);
		
		width = (int) (gp.tileSize * 7.3);
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
		tempX += gp.tileSize * 5.45;
		tempY -= gp.tileSize;		
		if (fighter.getMoveSet().get(commandNum).getPower() == 0) { text = "---"; }
		else { text = Integer.toString(fighter.getMoveSet().get(commandNum).getPower()); }
		g2.drawString(text, tempX, tempY);	
		
		tempY += gp.tileSize;		
		if (fighter.getMoveSet().get(commandNum).getAccuracy() == 0) { text = "---"; }
		else { text = Integer.toString(fighter.getMoveSet().get(commandNum).getAccuracy()); }		
		g2.drawString(text, tempX, tempY);	
		
		int i = 0;
		x = frameX;
		y = frameY;		
		width = (int) (gp.tileSize * 2.3);
		height = gp.tileSize;
		
		for (Move m : fighter.getMoveSet()) {			
				
			drawSubWindow(x, y, width, height, 10, 3, m.getType().getColor(), Color.BLACK);		
									
			g2.setFont(g2.getFont().deriveFont(Font.BOLD, 35F));			
			text = m.getType().getName();
			textX = getXForCenteredTextOnWidth(text, width, x + 5);
			textY = (int) (y + (gp.tileSize * 0.75));			
			drawText(text, textX, textY, battle_white, Color.BLACK);
			
			g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 50F));
			text = m.toString();
			textX = (int) (frameX + (gp.tileSize * 2.5));
			textY = (int) (y + (gp.tileSize * 0.85));			
			drawText(text, textX, textY, battle_white, Color.BLACK);	
			
			if (commandNum == i) {	
				slotY = y - 4;
				g2.setColor(battle_red);
				g2.setStroke(new BasicStroke(5));
				g2.drawRoundRect(slotX, slotY, slotWidth, slotHeight, 6, 6);
			}
						
			y += gp.tileSize * 1.35;
			i++;
		}
		
		x = (int) (gp.tileSize * 7.8);
		y = (int) (gp.tileSize * 8.6);
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 42F));
		text = "DESCRIPTION";
		drawText(text, x, y, battle_white, Color.BLACK);	
		
		y += gp.tileSize * 0.7;
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 38F));
		g2.setColor(Color.BLACK);
		for (String line : fighter.getMoveSet().get(commandNum).getInfo().split("\n")) {			
			g2.drawString(line, x, y);
			y += gp.tileSize * 0.8;
		} 			
		
		if (gp.keyH.leftPressed) {
			commandNum = 0;
			partySubState = party_Stats;			
		}
		if (gp.keyH.bPressed) {			
			commandNum = 0;
			partySubState = party_Main;
			gp.keyH.bPressed = false;
		}
	}
	private void drawParty_Info() {
		
		g2.setColor(battle_white);  
		g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);			
		
		int x;
		int y;
		int width;
		int height;
		String text;
		Pokemon fighter = gp.btlManager.trainer[0].pokeParty.get(fighterNum);
		
		x = -10;
		y = -5;
		width = gp.screenWidth + 13;
		height = (int) (gp.tileSize * 2);
		drawSubWindow(x, y, width, height, 10, 4, party_green, Color.BLACK);	
		
		x = 5;
		y = gp.tileSize * 2;
		width = (int) (gp.tileSize * 7.3);
		height = (int) (gp.tileSize * 1.2);
		g2.setColor(party_gray);
		g2.fillRoundRect(x, y, width, height, 8, 8);
		
		height += gp.tileSize;
		g2.setColor(Color.BLACK);
		g2.drawRoundRect(x, y, width, height, 4, 4);
		
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 55F));		
		x += gp.tileSize * 0.3;
		y += gp.tileSize * 0.95;
		text = fighter.getName();				
		drawText(text, x, y, Color.WHITE, Color.BLACK);
		
		int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();		
		x += length + 3;
		g2.setColor(fighter.getSexColor());	
		g2.drawString("" + fighter.getSex(), x, y);
	
		x = (int) (gp.tileSize * 0.7) - 10;
		y += gp.tileSize * 0.95;
		text = "Lv" + fighter.getLevel();
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 38F));	
		g2.setColor(Color.BLACK);
		g2.drawString(text, x, y);
		
		text = fighter.getNature().getName();
		x = getXforRightAlignText(text, x + (int) (gp.tileSize * 6.8));		
		g2.drawString(text, x, y);
		
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 32F));	
		x = (int) (gp.tileSize * 5.8);
		y -= gp.tileSize * 1.2;
		text = "No." + String.format("%03d", fighter.getIndex());	
		drawText(text, x, y, Color.WHITE, Color.BLACK);
		
		if (fighter.getStatus() != null) {
			drawFighterStatus((int) (x + (gp.tileSize * 1.5)), (int) (y - (gp.tileSize * 0.53)), fighter, 30F);
			g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 38F));
		}
		
		x = (int) (gp.tileSize * 1.2);
		y = (int) (gp.tileSize * 4.3);
		g2.drawImage(fighter.getFrontSprite(), x, y, null);
		
		x = (int) (gp.tileSize * 7.5);
		y = gp.tileSize * 2;
		width = (int) (gp.tileSize * 8.4);
		height = (int) (gp.tileSize * 5.8);
		drawSubWindow(x, y, width, height, 10, 4, party_gray, Color.BLACK);	
		
		y = (int) (gp.tileSize * 7.9);
		height = (int) (gp.tileSize * 0.8);		
		g2.setColor(party_gray);
		g2.fillRoundRect(x, y, width, height, 4, 4);
		
		height = gp.tileSize * 4;
		g2.setColor(Color.BLACK);
		g2.drawRoundRect(x, y, width, height, 4, 4);
	}
	private void drawHeader(int subState) {
		
		int x;
		int y;
		String text;
		
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
		
		if (0 < fighterNum) {
			x = (int) (gp.tileSize * 1.2);	
			y = 0;
			g2.drawImage(gp.btlManager.trainer[0].pokeParty.get(fighterNum - 1).getMenuSprite(), x, y, null);			
		}
		
		if (fighterNum + 1 < gp.btlManager.trainer[0].pokeParty.size()) {
			
			x = (int) (gp.screenWidth - (gp.tileSize * 3.1));
			y = 0;
			g2.drawImage(gp.btlManager.trainer[0].pokeParty.get(fighterNum + 1).getMenuSprite(), x, y, null);				
		}
	}
	
	// BATTLE SCREEN
	private void drawBattleScreen() {
		
		switch(battleSubState) {		
			case battle_Encounter:
				drawBattleDialogueWindow();	
				break;
			case battle_Start:
				drawBattleDialogue();
				break;
			case battle_Dialogue:	
				drawFighterWindows();
				drawBattleDialogue();
				break;
			case battle_Options:
				drawFighterWindows();
				drawBattleOptionsWindow();	
				break;
			case battle_Moves:
				drawFighterWindows();
				drawBattleMovesetWindow();
				drawBattleMoveDescriptionWindow();
				break;
			case battle_Swap:
				drawFighterWindows();
				drawSwapOptionsWindow();
				break;
		}
	}
	
	private void drawFighterWindows() {
		
		int x; 
		int y;
		
		if (gp.btlManager.fighter[0] != null) {
			x = (int) (gp.tileSize * 9.25);
			y = (int) (gp.tileSize * 5.85);
			drawFighterWindow(x, y, 0);		
		}
		
		if (gp.btlManager.fighter[1] != null) {
			x = (int) (gp.tileSize * 0.5);
			y = (int) (gp.tileSize * 0.8);
			drawFighterWindow(x, y, 1);		
		}
	}
	private void drawFighterWindow(int x, int y, int num) {
		
		if (num == 0) {
			drawPartyCount(num);
		}
		else { 
			if (gp.btlManager.battleMode != gp.btlManager.wildBattle) {
				drawPartyCount(num);
			}
		}
		
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
			drawFighterStatus((int) (tempX + gp.tileSize * 0.3), (int) (y + gp.tileSize * 0.2), gp.btlManager.fighter[num], 32F);
			g2.setFont(g2.getFont().deriveFont(Font.BOLD, 32F));		
		}
		
		x = (int) (tempX + gp.tileSize * 1.45);
		y += gp.tileSize * 0.22;		
		drawFighterHP_Battle(x, y, num);
	}	
	private void drawPartyCount(int num) {	
		
		int startX;
		int x;
		int y;
		
		int partySize = gp.btlManager.trainer[num].pokeParty.size();
		int activePartySize = 0;
		for (Pokemon p : gp.btlManager.trainer[num].pokeParty) {
			if (p.isAlive()) activePartySize++;
		}
		
		if (num == 0) {
			
			startX = (int) (gp.tileSize * 12.45);
			x = startX;
			y = (int) (gp.tileSize * 5.35);		
			
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
	private void drawFighterStatus(int x, int y, Pokemon fighter, float fontSize) {	
		
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
	private void drawFighterHP_Battle(int x, int y, int num) {
		
		int width = (int) (gp.tileSize * 4.7);
		int height = (int) (gp.tileSize * 0.55);
		
		g2.setColor(Color.BLACK);
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 30F));
		g2.setStroke(new BasicStroke(2));
		g2.fillRoundRect(x, y, width, height, 30, 30);
		
		g2.setColor(Color.WHITE);	
		x += (int) gp.tileSize * 0.25;
		y += gp.tileSize * 0.46;
		g2.drawString("HP", x, y);		
		
		x += gp.tileSize * 0.55;
		y -= gp.tileSize * 0.34;
		width = (int) (gp.tileSize * 3.8);
		height = (int) (gp.tileSize * 0.33);
		
		int tempHP = 0;
		int tempHPspeed = 0;
		
		if (num == 0) { tempHP = fighter_one_HP; tempHPspeed = hpSpeed_one; }	
		else { tempHP = fighter_two_HP; tempHPspeed = hpSpeed_two; }	
							
		if (tempHP > gp.btlManager.fighter[num].getHP()) {			
			if (hpCounter >= tempHPspeed) {
				tempHP--;	
				hpCounter = 0;
			}
			else {
				hpCounter++;			
			}	
		}
		else if (tempHP < gp.btlManager.fighter[num].getHP()) {			
			if (hpCounter >= tempHPspeed) {
				tempHP++;
				hpCounter = 0;
			}
			else {
				hpCounter++;			
			}	
		}
		
		double remainHP = (double)tempHP / (double)gp.btlManager.fighter[num].getBHP();	
		
		if (remainHP >= .50) g2.setColor(hp_green);
		else if (remainHP >= .25) g2.setColor(hp_yellow);
		else { 
			g2.setColor(hp_red);
			if (num == 0) playLowHPSE(remainHP);
		}
		
		width *= remainHP;							
		g2.fillRoundRect(x, y + 1, width, height, 15, 15);	
		
		if (num == 0) fighter_one_HP = tempHP;
		else fighter_two_HP = tempHP;		

		width = (int) (gp.tileSize * 3.8);
		g2.setColor(Color.WHITE);
		g2.setStroke(new BasicStroke(3));
		g2.drawRoundRect(x, y, width, height, 15, 15);		
		
		g2.setColor(Color.BLACK);
		String text = gp.btlManager.fighter[num].getHP() + " / " + gp.btlManager.fighter[num].getBHP();
		x = getXforRightAlignText(text, (int) (x + gp.tileSize * 3.65));		
		y += gp.tileSize * 0.9;
		g2.drawString(text, x, y);	
	}	
	private void playLowHPSE(double remainHP) {
		
		if (remainHP > 0) {
			hpCounter++;
			if (hpCounter == 33) {
				gp.playSE(6, 8);
				hpCounter = 0;
			}	
		}
		else {
			hpCounter = 0;
		}
	}
	
	private void drawBattleDialogueWindow() {
		
		int width = (int) (gp.screenWidth - (gp.tileSize * 0.15));
		int height = (int) (gp.tileSize * 3.5);
		int x = (int) (gp.tileSize * 0.1);
		int y = (int) (gp.screenHeight - (height * 1.02)); 
		
		drawSubWindow(x, y, width, height, 12, 10, battle_green, battle_red);
	}	
	private void drawBattleOptionsWindow() {	
		
		drawBattleDialogueWindow();
		
		int x = gp.tileSize / 2; 
		int y = gp.screenHeight - gp.tileSize * 2;
		String text = "What will\n" + gp.btlManager.fighter[0].getName() + " do?";
		
		g2.setColor(Color.BLACK);
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 60F));
		
		for (String line : text.split("\n")) { 
			drawText(line, x, y, battle_white, Color.BLACK);
			y += gp.tileSize;
		}
		
		
		int width = (int) (gp.tileSize * 7.45);
		int height = (int) (gp.tileSize * 3.5);
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
		}

		x += gp.tileSize * 4;
		text = "BAG";
		g2.drawString(text, x, y);
		if (commandNum == 1) {
			width = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
			g2.setColor(battle_red);
			g2.drawRect(x - 4, y - (int) (gp.tileSize * 0.85), width + 4, height);
			g2.setColor(Color.BLACK);
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
		}
		
		x += gp.tileSize * 4;
		text = "RUN";
		g2.drawString(text, x, y);
		if (commandNum == 3) {
			width = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
			g2.setColor(battle_red);
			g2.drawRect(x - 4, y - (int) (gp.tileSize * 0.85), width + 4, height);
			g2.setColor(Color.BLACK);
		}
						
		if (gp.keyH.aPressed) {
			gp.keyH.aPressed = false;
			
			if (commandNum == 0) {
				skipBattleDialogue();	
				battleSubState = battle_Moves;		
			}
			else if (commandNum == 2) {
				skipBattleDialogue();
				gp.gameState = gp.partyState;
				partySubState = party_Main;				
			}
			commandNum = 0;	
		}
	}
	private void drawBattleMovesetWindow() {
		
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
		
		if (gp.keyH.aPressed) {		
			gp.btlManager.setPlayerMove(commandNum);
			skipBattleDialogue();		
			battleSubState = battle_Dialogue;	
			commandNum = 0;	
		}
		else if (gp.keyH.bPressed) {			
			skipBattleDialogue();
			battleSubState = battle_Options;	
			commandNum = 0;	
		}
	}	
	private void drawBattleMoveDescriptionWindow() {
		
		String text;
		int width = (int) (gp.tileSize * 3.9);
		int height = (int) (gp.tileSize * 3.5);
		int x = (int) (gp.tileSize * 12.1);
		int y = (int) (gp.screenHeight - (height * 1.02)); 		
		
		drawSubWindow(x, y, width, height, 12, 10, gp.btlManager.fighter[0].getMoveSet().get(commandNum).getType().getColor(), battle_gray);
						
		x += gp.tileSize * 0.3;
		y = (int) (gp.screenHeight - gp.tileSize * 2.2);	
		text = "PP " + gp.btlManager.fighter[0].getMoveSet().get(commandNum).getpp() + "/" + 
				gp.btlManager.fighter[0].getMoveSet().get(commandNum).getbpp();
		
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 57F));	
		drawText(text, x, y, battle_white, Color.BLACK);
		
		y += gp.tileSize * 1.5;
		text = gp.btlManager.fighter[0].getMoveSet().get(commandNum).getType().getName();
		drawText(text, x, y, battle_white, Color.BLACK);
	}		
	
	private void drawSwapOptionsWindow() {
		
		drawBattleDialogueWindow();
		
		int x;
		int y;
		int width;
		int height;
		String text;
		
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 60F));		
		x = gp.tileSize / 2;
		y = gp.screenHeight - gp.tileSize * 2;				
		text = "Will " + gp.btlManager.trainer[0].name + " swap\nPokemon?";
		
		for (String line : text.split("\n")) { 
			drawText(line, x, y, battle_white, Color.BLACK);
			y += gp.tileSize;
		}
		
		x = (int) (gp.tileSize * 12.5);
		y = (int) (gp.tileSize * 5.2);
		width = (int) (gp.tileSize * 3.45);
		height = (int) (gp.tileSize * 3.2);
		
		drawSubWindow(x, y, width, height, 10, 10, battle_white, battle_gray);
		
		width = (int) (gp.tileSize * 2.5);
		height = gp.tileSize;
		g2.setStroke(new BasicStroke(4));
		
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 60F));
		
		g2.setColor(Color.BLACK);
		x += gp.tileSize * 0.5;
		y += gp.tileSize * 1.3;	
		text = "YES";		
		g2.drawString(text, x, y);	
		if (commandNum == 0) {
			g2.setColor(battle_red);
			g2.drawRect(x - 4, y - (int) (gp.tileSize * 0.85), width + 4, height);
			g2.setColor(Color.BLACK);
		}		
		
		y += gp.tileSize * 1.3;		
		text = "NO";		
		g2.drawString(text, x, y);	
		if (commandNum == 1) {
			g2.setColor(battle_red);
			g2.drawRect(x - 4, y - (int) (gp.tileSize * 0.85), width + 4, height);
			g2.setColor(Color.BLACK);		
		}			
		
		if (gp.keyH.aPressed) {
			gp.keyH.aPressed = false;
			
			if (commandNum == 0) {		
				gp.gameState = gp.partyState;
				partySubState = party_Main;
			}
			else {
				gp.btlManager.fightStage = gp.btlManager.fightStage_Swap;
				battleSubState = battle_Dialogue;
			}
			
			commandNum = 0;	
			skipBattleDialogue();	
		}
	}
	
	private void drawBattleDialogue() {
		
		drawBattleDialogueWindow();	
		
		int x = gp.tileSize / 2;
		int y = gp.screenHeight - gp.tileSize * 2;
		String text = "";
		
		g2.setColor(Color.BLACK);
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 60F));
		
		// DIALOGUE TO PRINT
		if (battleDialogue.size() > dialogueIndex && battleDialogue.get(dialogueIndex) != null) {

			if (dialogueCounter >= battleTextSpeed) {
				
				char characters[] = battleDialogue.get(dialogueIndex).toCharArray();
										
				if (charIndex < characters.length) {					
					String s = String.valueOf(characters[charIndex]);				
					combinedText += s;
					currentDialogue = combinedText;					
					charIndex++;					
				}				
				
				// ALL CHARACTERS PRINTED
				if (charIndex >= characters.length) {
					canSkip = true;
				}
				
				dialogueCounter = 0;
			}
			else {
				dialogueCounter++;		
			}			
		}
		// NO MORE DIALOGUE TO PRINT
		else {			
			dialogueIndex = 0;
			battleDialogue.clear();	
			gp.btlManager.ready = true;
		}
		
		// PRINT OUT DIALOGUE
		for (String line : currentDialogue.split("\n")) {   
  			text = line;
  			drawText(text, x, y, battle_white, Color.BLACK);
			y += gp.tileSize;
		} 	
		
		dialogueTimer++;  		
  		if (seTimer.size() > 0) {
  			if (dialogueTimer == seTimer.get(0)) playBattleSE();
  		}
		
		// IF NO SE AND BATTLE TAKING PLACE
		if (seTimer.size() == 0 &&
				(gp.btlManager.fightStage != gp.btlManager.fightStage_Move &&
				gp.btlManager.fightStage != gp.btlManager.fightStage_Attack)) {
			
			// PLAYER CAN ADVANCE DIALOGUE
			if (canSkip) {
	  			x += (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
	  			y -= gp.tileSize * 1.4;
	  			g2.drawImage(dialogue_next, x, y, null);
	
	  			if (gp.keyH.aPressed) {		  				
	  				gp.keyH.playCursorSE();
	  				skipBattleDialogue();		
	  			}
			}
  		}
		else if (dialogueTimer >= dialogueTimerMax) {  
			skipBattleDialogue();  			
	  	}
	}		
	public void skipBattleDialogue() {
		gp.keyH.aPressed = false;		
		
		dialogueIndex++;
		charIndex = 0;
		combinedText = "";	
		currentDialogue = "";
				
		dialogueTimer = 0;
		dialogueTimerMax = 90;
		
		canSkip = false;
		
		if (seTimer.size() > 0) seTimer.remove(0);			
	}
	private void playBattleSE() {	
		if (category_SE.size() == seTimer.size()) {
			
			if (category_SE.get(0) > -1 && record_SE.get(0) > -1) {
				gp.playSE(category_SE.get(0), record_SE.get(0));  			
			}
					
			category_SE.remove(0);	
			record_SE.remove(0);	
		}			
	}
	private void drawText(String text, int x, int y, Color primary, Color shadow) {
		g2.setColor(shadow);	
		g2.drawString(text, x, y);	
		g2.setColor(primary);
		g2.drawString(text, x-2, y-2);		
	}
	
	public void setSoundFile(int cat, String soundFile, int timer) {		
		category_SE.add(cat);		
		record_SE.add(gp.se.getFile(cat, soundFile));
		
		int soundDuration = gp.se.getSoundDuration(cat, gp.se.getFile(cat, soundFile));		
		dialogueTimerMax = 30 + soundDuration;		
		
		seTimer.add(timer);
	}
	public void setSoundFile(int cat, String soundFile, int timer, int duration) {		
		category_SE.add(cat);		
		record_SE.add(gp.se.getFile(cat, soundFile));
		
		dialogueTimerMax = duration;				
		seTimer.add(timer);
	}
	public void setDummyFile() {		
		category_SE.add(-1);		
		record_SE.add(-1);
		seTimer.add(5);
	}	
	public void addBattleDialogue(String text) {
		battleDialogue.add(text);
	}
	
	// SUB WINDOW
	private void drawSubWindow(int x, int y, int width, int height, int curve, int borderStroke, 
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
}