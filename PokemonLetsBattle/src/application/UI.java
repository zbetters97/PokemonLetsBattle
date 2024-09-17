package application;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import moves.Move;
import pokemon.Pokemon;

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
	
	// DIALOGUE HANDLER	
	public String currentDialogue = "";
	private ArrayList<String> battleDialogue;
	public String combinedText = "";
	public int dialogueCounter = 0;	
	private int dialogueIndex = 0;
	public int charIndex = 0;	
	public int textSpeed = 2;
	private int dialogueTimer = 0;
	public int dialogueTimerMax = 60;
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
	public final int party_Stats = 2;
	public final int party_Moves = 3;
	
	public int battleSubState;
	public final int battle_Encounter = 1;
	public final int battle_Dialogue = 2;
	public final int battle_Options = 3;
	public final int battle_Moves = 4;	
	public final int battle_KO = 5;
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
		g2.setColor(Color.RED);
		g2.drawRect(gp.player.screenX + gp.player.hitbox.x, gp.player.screenY + gp.player.hitbox.y, 
				gp.player.hitbox.width, gp.player.hitbox.height);
	}
	
	// PARTY SCREEN
	private void drawPartyScreen() {
		
		switch(partySubState) {
			case party_Main:
				drawParty_Main();
				break;
			case party_Stats:				
				drawParty_Skills();
				drawHeader("STATS");
				break;
			case party_Moves:
				drawParty_Moves();
				drawHeader("MOVES");
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
		
		x = (int) (gp.tileSize * 0.5);
		y = (int) (gp.tileSize * 2.8); 
		width = (int) (gp.tileSize * 5.5);
		height = (int) (gp.tileSize * 3.2);
		fighter = gp.btlManager.trainer[0].pokeParty.get(0);
		
		if (commandNum == 0) {
			drawSubWindow(x, y, width, height, 3, 5, party_blue, party_red);
		}
		else {
			drawSubWindow(x, y, width, height, 3, 3, party_blue, Color.BLACK);
		}
		
		drawPartyBox(x, y, fighter, true);
		
		x = (int) (gp.tileSize * 6.5);
		y = (int) (gp.tileSize * 0.5); 
		width = gp.tileSize * 9;
		height = (int) (gp.tileSize * 1.7);
	
		for (int i = 1; i < 6; i++) {		
			
			if (commandNum == i) {
				drawSubWindow(x, y, width, height, 3, 5, party_blue, party_red);	
			}
			else {
				drawSubWindow(x, y, width, height, 3, 3, party_blue, Color.BLACK);
			}
			
			if (gp.btlManager.trainer[0].pokeParty.size() > i) {					
				fighter = gp.btlManager.trainer[0].pokeParty.get(i);
				drawPartyBox((int) (x + gp.tileSize * 0.15), (int) (y - gp.tileSize * 0.15), fighter, false);				
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
		drawText(x, y, text, Color.BLACK, Color.LIGHT_GRAY);
		
		x += width + (gp.tileSize * 0.7);
		y = (int) (gp.tileSize * 10.5);
		width = gp.tileSize * 3;
		height = (int) (gp.tileSize * 1);			
		
		if (commandNum == 6) {
			drawSubWindow(x, y, width, height, 25, 6, party_blue, party_red);			
		}
		else {
			drawSubWindow(x, y, width, height, 25, 4, party_blue, Color.BLACK);	
		}
		
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 45F));
		x += gp.tileSize * 0.55;
		y += gp.tileSize * 0.8;
		text = "CANCEL";
		drawText(x, y, text, battle_white, Color.BLACK);
		
		if (gp.keyH.aPressed) {
			fighterNum = commandNum;
			commandNum = 0;
			partySubState = party_Stats;
		}
		if (gp.keyH.bPressed) {			
			commandNum = 6;	
			gp.keyH.bPressed = false;
		}
	}
	private void drawPartyBox(int x, int y, Pokemon fighter, boolean main) {
		
		g2.drawImage(fighter.getMenuSprite(), x, y, null);
		
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 35F));	
		x += gp.tileSize * 2;
		y += gp.tileSize;
		String text = fighter.name();
		drawText(x, y, text, battle_white, Color.BLACK);
	
		x += gp.tileSize * 0.9;
		y += gp.tileSize * 0.55;
		text = "Lv" + fighter.getLevel();			
		drawText(x, y, text, battle_white, Color.BLACK);
		
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
		
		int frameX = (int) (gp.tileSize * 7.5);
		int frameY = gp.tileSize * 2;
		int width = gp.tileSize * 9;
		int height = gp.tileSize * 11;
		drawSubWindow(frameX, frameY, width, height, 10, 4, party_gray, Color.BLACK);	
		
		frameX += gp.tileSize * 0.8;
		frameY += gp.tileSize;
						
		x = frameX;
		y = frameY;		
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 45F));	

		text = "HP"; drawText(x, y, text, Color.WHITE, Color.BLACK); y += gp.tileSize;
		text = "ATTACK"; drawText(x, y, text, Color.WHITE, Color.BLACK); y += gp.tileSize;
		text = "DEFENSE"; drawText(x, y, text, Color.WHITE, Color.BLACK); y += gp.tileSize;
		text = "SP. ATK"; drawText(x, y, text, Color.WHITE, Color.BLACK); y += gp.tileSize;
		text = "SP. DEF"; drawText(x, y, text, Color.WHITE, Color.BLACK); y += gp.tileSize;
		text = "SPEED"; drawText(x, y, text, Color.WHITE, Color.BLACK); y += gp.tileSize;
		
		Pokemon fighter = gp.btlManager.trainer[0].pokeParty.get(fighterNum);
		
		x = frameX += gp.tileSize * 5;
		y = frameY;		
		text = fighter.getHP() + "/" + fighter.getBHP(); 
		frameX = getXforRightAlignText(text, x);	
		drawText(frameX, y, text, Color.WHITE, Color.BLACK); y += gp.tileSize;
		text = Integer.toString((int) fighter.getAttack()); 
		frameX = getXforRightAlignText(text, x);	
		drawText(frameX, y, text, Color.WHITE, Color.BLACK); y += gp.tileSize;
		text = Integer.toString((int) fighter.getDefense()); 
		frameX = getXforRightAlignText(text, x);	
		drawText(frameX, y, text, Color.WHITE, Color.BLACK); y += gp.tileSize;
		text = Integer.toString((int) fighter.getSpAttack()); 
		frameX = getXforRightAlignText(text, x);	
		drawText(frameX, y, text, Color.WHITE, Color.BLACK); y += gp.tileSize;
		text = Integer.toString((int) fighter.getSpDefense()); 
		frameX = getXforRightAlignText(text, x);	
		drawText(frameX, y, text, Color.WHITE, Color.BLACK); y += gp.tileSize;
		text = Integer.toString((int) fighter.getSpeed()); 
		frameX = getXforRightAlignText(text, x);	
		drawText(frameX, y, text, Color.WHITE, Color.BLACK); y += gp.tileSize;
		
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
		int textX;
		int textY;
		String text;
								
		int frameX = (int) (gp.tileSize * 7.5);
		int frameY = gp.tileSize * 2;
		int width = (int) (gp.tileSize * 8.4);
		int height = (int) (gp.tileSize * 5.8);
		drawSubWindow(frameX, frameY, width, height, 10, 4, party_gray, Color.BLACK);	
		
		
		int slotX = frameX + 3;
		int slotY;
		int slotWidth = width - 7;
		int slotHeight = gp.tileSize + 7;
		
		frameX += gp.tileSize * 0.4;
		frameY += gp.tileSize * 0.4;
					
		int i = 0;
		x = frameX;
		y = frameY;		
		width = gp.tileSize * 2;
		height = gp.tileSize;
		Pokemon fighter = gp.btlManager.trainer[0].pokeParty.get(fighterNum);
		
		for (Move m : fighter.getMoveSet()) {			
				
			drawSubWindow(x, y, width, height, 10, 3, m.getType().getColor(), Color.BLACK);		
			
			if (commandNum == i) {			
				slotY = y - 3;
				g2.setColor(battle_red);
				g2.setStroke(new BasicStroke(4));
				g2.drawRect(slotX, slotY, slotWidth, slotHeight);
			}
			
			g2.setFont(g2.getFont().deriveFont(Font.BOLD, 35F));			
			text = m.getType().getName();
			textX = getXForCenteredTextOnWidth(text, width, x + 5);
			textY = (int) (y + (gp.tileSize * 0.75));			
			drawText(textX, textY, text, battle_white, Color.BLACK);
			
			g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 50F));
			text = m.getName();
			textX = (int) (frameX + (gp.tileSize * 2.5));
			textY = (int) (y + (gp.tileSize * 0.85));			
			drawText(textX, textY, text, battle_white, Color.BLACK);		
						
			y += gp.tileSize * 1.35;
			i++;
		}
		
		frameX = (int) (gp.tileSize * 7.5);
		frameY = (int) (gp.tileSize * 7.9);
		width = (int) (gp.tileSize * 8.4);
		height = gp.tileSize * 4;
		drawSubWindow(frameX, frameY, width, height, 10, 4, battle_white, Color.BLACK);	
		
		y = (int) (frameY + (gp.tileSize * 0.8));
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 38F));
		for (String line : fighter.getMoveSet().get(commandNum).getInfo().split("\n")) {   
			drawText(x, y, line, Color.BLACK, Color.WHITE);
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
		Pokemon fighter = gp.btlManager.trainer[0].pokeParty.get(fighterNum);
		
		x = -10;
		y = (int) (gp.tileSize * 0.5);
		width = gp.screenWidth + 20;
		height = (int) (gp.tileSize * 1);
		drawSubWindow(x, y, width, height, 10, 4, party_green, Color.BLACK);	
				
		x = 5;
		y = gp.tileSize * 2;
		width = gp.tileSize * 7;
		height = gp.tileSize * 2;
		drawSubWindow(x, y, width, height, 10, 4, party_gray, Color.BLACK);	
		
		x += gp.tileSize * 0.3;
		y += gp.tileSize;
		String text = fighter.getName();		
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 55F));	
		drawText(x, y, text, Color.WHITE, Color.BLACK);
		
		int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();		
		x += length + 3;
		g2.setColor(fighter.getSexColor());	
		g2.drawString("" + fighter.getSex(), x, y);
		
		x = (int) (gp.tileSize * 0.7) - 10;
		y += gp.tileSize * 0.7;
		text = "Lv" + fighter.getLevel();
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 38F));	
		drawText(x, y, text, Color.WHITE, Color.BLACK);
		
		x = gp.tileSize;
		y = gp.tileSize * 5;
		g2.drawImage(fighter.getFrontSprite(), x, y, null);
	}
	private void drawHeader(String text) {		
		int x = gp.tileSize;
		int y = (int) (gp.tileSize * 1.3);		
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 45F));		
		drawText(x, y, text, Color.WHITE, Color.BLACK);
	}
	
	// BATTLE SCREEN
	private void drawBattleScreen() {
		
		switch(battleSubState) {
		
			case battle_Encounter:
				drawBattleDialogueWindow();
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
			case battle_KO:
				drawFighterWindows();
				drawBattleDialogue();
				break;
			case battle_Swap:
				drawFighterWindows();
				drawSwapOptionsWindow();
				break;
		}
	}
	private void drawFighterWindows() {
		int x = (int) (gp.tileSize * 9.25);
		int y = (int) (gp.tileSize * 5.85);
		drawFighterWindow(x, y, 0);	
		
		x = (int) (gp.tileSize * 0.5);
		y = (int) (gp.tileSize * 0.8);
		drawFighterWindow(x, y, 1);	
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
		
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 30F));
		
		if (gp.btlManager.fighter[num].getStatus() != null) {
			drawFighterStatus((int) (tempX + gp.tileSize * 0.3), (int) (y + gp.tileSize * 0.2), num);
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
	private void drawFighterStatus(int x, int y, int num) {	
		
		int width = gp.tileSize;
		int height = (int) (gp.tileSize * 0.6);
		g2.setColor(gp.btlManager.fighter[num].getStatus().getColor());
		g2.fillRoundRect(x, y, width, height, 20, 20);
		
		g2.setColor(Color.BLACK);
		x += gp.tileSize * 0.15;
		y += gp.tileSize * 0.48;
		g2.drawString(gp.btlManager.fighter[num].getStatus().getAbreviation(), x, y);
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
			if (hpCounter == tempHPspeed) {
				tempHP--;	
				hpCounter = 0;
			}
			else {
				hpCounter++;			
			}	
		}
		else if (tempHP < gp.btlManager.fighter[num].getHP()) {			
			if (hpCounter == tempHPspeed) {
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
			drawText(x, y, line, battle_white, Color.BLACK);
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
				advanceDialogue();	
				battleSubState = battle_Moves;		
			}
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
				
				if (commandNum == i) {
					width = (int)g2.getFontMetrics().getStringBounds(gp.btlManager.fighter[0].getMoveSet().get(i).getName(), g2).getWidth();
					g2.setColor(battle_red);
					g2.drawRect(x - 4, y - (int) (gp.tileSize * 0.85), width + 4, height);
					g2.setColor(Color.BLACK);
				}
				
				g2.drawString(gp.btlManager.fighter[0].getMoveSet().get(i).getName(), x, y);	
			}
			else {
				g2.drawString("-", x, y);	
			}			
		}
		
		if (gp.keyH.aPressed) {		
			gp.btlManager.setPlayerMove(commandNum);
			advanceDialogue();		
			battleSubState = battle_Dialogue;	
		}
		else if (gp.keyH.bPressed) {			
			advanceDialogue();
			battleSubState = battle_Options;			
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
		drawText(x, y, text, battle_white, Color.BLACK);
		
		y += gp.tileSize * 1.5;
		text = gp.btlManager.fighter[0].getMoveSet().get(commandNum).getType().getName();
		drawText(x, y, text, battle_white, Color.BLACK);
	}		
	
	private void drawSwapOptionsWindow() {
		
		drawBattleDialogueWindow();
		
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 60F));		
		int x = gp.tileSize / 2;
		int y = gp.screenHeight - gp.tileSize * 2;
		String text = "Will " + gp.btlManager.trainer[0].name + " swap\nPokemon?";
		
		for (String line : text.split("\n")) { 
			drawText(x, y, line, battle_white, Color.BLACK);
			y += gp.tileSize;
		}
		
		x = (int) (gp.tileSize * 12.5);
		y = (int) (gp.tileSize * 5.2);
		int width = (int) (gp.tileSize * 3.45);
		int height = (int) (gp.tileSize * 3.2);
		
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
						
			}
			else {
				battleSubState = battle_Options;
			}
			
			advanceDialogue();	
		}
	}
	
	private void drawBattleDialogue() {
		
		drawBattleDialogueWindow();	
		
		int x = gp.tileSize / 2;
		int y = gp.screenHeight - gp.tileSize * 2;
		String text = "";
		
		g2.setColor(Color.BLACK);
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 60F));
				
		if (battleDialogue.size() > dialogueIndex && battleDialogue.get(dialogueIndex) != null) {

			if (dialogueCounter == textSpeed) {
				
				char characters[] = battleDialogue.get(dialogueIndex).toCharArray();
							
				if (charIndex < characters.length) {
					
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
			else {
				dialogueCounter++;		
			}			
		}
		else {			
			dialogueIndex = 0;
			battleDialogue.clear();	
			gp.btlManager.ready = true;
		}
		
  		for (String line : currentDialogue.split("\n")) {   
  			drawText(x, y, line, battle_white, Color.BLACK);
			y += gp.tileSize;
		} 	
  		
  		dialogueTimer++;  		
  		if (seTimer.size() > 0) {
  			canSkip = false;
  			if (dialogueTimer == seTimer.get(0)) playBattleSE();
  		}
  		
  		if (canSkip) {
  			x += (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
  			y -= gp.tileSize * 1.4;
  			g2.drawImage(dialogue_next, x, y, null);

  			if (gp.keyH.aPressed) {
  				gp.keyH.playCursorSE();		
  				advanceDialogue();			
  			}
  		}
  		else {
  			if (dialogueTimer >= dialogueTimerMax) {    			
  	  			advanceDialogue();  			
  	  		}
  		}
	}		
	public void advanceDialogue() {
		gp.keyH.aPressed = false;
		canSkip = false;
		charIndex = 0;
		combinedText = "";	
		currentDialogue = "";
		commandNum = 0;
		dialogueTimer = 0;
		dialogueIndex++;
		if (seTimer.size() > 0) {
			seTimer.remove(0);	
		}
	}
	private void playBattleSE() {	
		if (category_SE.size() == seTimer.size()) {
			gp.playSE(category_SE.get(0), record_SE.get(0));  				
			category_SE.remove(0);	
			record_SE.remove(0);	
		}			
	}
	private void drawText(int x, int y, String text, Color primary, Color shadow) {
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
	public void addBattleDialogue(String text) {
		battleDialogue.add(text);
	}
	
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