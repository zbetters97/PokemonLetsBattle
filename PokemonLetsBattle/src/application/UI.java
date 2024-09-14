package application;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;

import java.awt.Graphics2D;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

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
	
	// DIALOGUE HANDLER	
	public String currentDialogue = "";
	private ArrayList<String> battleDialogue;
	
	public String combinedText = "";
	public int dialogueCounter = 0;	
	private int dialogueIndex = 0;
	public int charIndex = 0;	
	public int textSpeed = 2;
	public int commandNum = 0;
	private int dialogueTimer = 0;
	public int dialogueTimerMax = 60;
	
	public ArrayList<Integer> seTimer;
	private ArrayList<Integer> category_SE;
	private ArrayList<Integer> record_SE;
	
	// FIGHTER HP
	public int fighter_one_HP;
	public int fighter_two_HP;
	private int hpSpeed = 2;
	private int hpCounter = 0;
	
	public boolean fighterReady = false;	
	
	private ArrayList<String> battleOptions;
	
	public int battleSubState;
	public final int subState_Encounter = 1;
	public final int subState_Dialogue = 2;
	public final int subState_Options = 3;
	public final int subState_Moves = 4;
	public final int subState_KO = 5;
	
	public UI(GamePanel gp) {
		this.gp = gp;
		
		seTimer = new ArrayList<Integer>();
		category_SE = new ArrayList<Integer>();
		record_SE = new ArrayList<Integer>();
		
		battleDialogue = new ArrayList<String>();
		battleOptions = new ArrayList<String>();
		battleOptions.addAll(Arrays.asList("FIGHT", "BAG", "POKEMON", "RUN"));
			
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
	
	// BATTLE SCREEN
	private void drawBattleScreen() {
	
		if (battleSubState == subState_Encounter) {		
			drawBattleDialogueWindow();	
		}		
		else if (battleSubState == subState_Dialogue) {			
			drawFighterWindows();
			drawBattleDialogue();
		}
		else if (battleSubState == subState_Options) {
			drawFighterWindows();
			drawBattleOptionsWindow();			
		}
		else if (battleSubState == subState_Moves) {
			drawFighterWindows();
			drawBattleMovesetWindow();
			drawBattleMoveDescriptionWindow();
		}
		else if (battleSubState == subState_KO) {	
			drawFighterWindows();
			drawBattleDialogue();
		}
	}
	
	private void drawFighterWindows() {
		int x = (int) (gp.tileSize * 9.1);
		int y = (int) (gp.tileSize * 5.4);
		drawFighterWindow(x, y, 0);	
		
		x = (int) (gp.tileSize * 0.4);
		y = (int) (gp.tileSize * 0.4);
		drawFighterWindow(x, y, 1);	
	}
	private void drawFighterWindow(int x, int y, int num) {
		
		int tempX = x;
		
		String text;
		int length;
		int width = (int) (gp.tileSize * 6.5);
		int height = (int) (gp.tileSize * 2.3);
		
		drawSubWindow(x, y, width, height, 15, 6, battle_yellow, Color.BLACK);
		
		x += gp.tileSize * 0.3;
		y += gp.tileSize * 0.8;
		text = gp.btlManager.fighter[num].getName();
		g2.setColor(Color.BLACK);
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 35F));
		g2.drawString(text, x, y);
		
		length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();		
		x += length;
		g2.setColor(gp.btlManager.fighter[num].getSexColor());	
		g2.drawString("" + gp.btlManager.fighter[num].getSex(), x, y);
		
		g2.setColor(Color.BLACK);		
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 32F));		
		text = "Lv" + gp.btlManager.fighter[num].getLevel();		
		length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();		
		x = (int) (tempX + width - length - gp.tileSize * 0.45);
		g2.drawString(text, x, y);
		
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 30F));
		
		if (gp.btlManager.fighter[num].getStatus() != null) {
			drawFighterStatus((int) (tempX + gp.tileSize * 0.3), (int) (y + gp.tileSize * 0.2), num);
		}
		
		x = (int) (tempX + gp.tileSize * 1.7);
		y += gp.tileSize * 0.7;
		g2.drawString("HP", x, y);
		
		x += gp.tileSize * 0.6;
		y -= gp.tileSize * 0.35;
		width = (int) (gp.tileSize * 3.8);
		height = (int) (gp.tileSize * 0.4);
		g2.setColor(Color.BLACK);
		g2.setStroke(new BasicStroke(3));
		g2.fillRoundRect(x, y, width, height, 15, 15);
				
		height -= 2;
		drawFighterHealthBar(x, y, width, height, num);
		
		g2.setColor(Color.BLACK);
		text = gp.btlManager.fighter[num].getHP() + " / " + gp.btlManager.fighter[num].getBHP();
		x = getXforRightAlignText(text, (int) (x + gp.tileSize * 3.8));		
		y += gp.tileSize * 0.9;
		g2.drawString(text, x, y);	
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
	private void drawFighterHealthBar(int x, int y, int width, int height, int num) {
		
		int tempHP = 0;
		
		if (num == 0) tempHP = fighter_one_HP;		
		else tempHP = fighter_two_HP;
					
		if (tempHP > gp.btlManager.fighter[num].getHP()) {
			if (hpCounter == hpSpeed) {
				tempHP--;	
				hpCounter = 0;
			}
			else {
				hpCounter++;			
			}					
		}
		else if (tempHP < gp.btlManager.fighter[num].getHP()) {
			
			if (hpCounter == hpSpeed) {
				tempHP++;
				hpCounter = 0;
			}
			else {
				hpCounter++;			
			}			
		}
		
		double remainHP = (double)tempHP / (double)gp.btlManager.fighter[num].getBHP();				
		if (remainHP >= .50) g2.setColor(Color.GREEN);
		else if (remainHP >= .25) g2.setColor(Color.YELLOW);
		else g2.setColor(Color.RED);	
		
		width *= remainHP;
		
		g2.fillRoundRect(x, y + 1, width, height, 15, 15);	
		
		if (num == 0) fighter_one_HP = tempHP;
		else fighter_two_HP = tempHP;	
	}
	
	private void drawBattleOptionsWindow() {	
		
		drawBattleDialogueWindow();
		
		int x = gp.tileSize / 2;
		int y = (int) (gp.screenHeight - gp.tileSize * 2.2);
		String text = "What will\n" + gp.btlManager.fighter[0].getName() + " do?";
		
		g2.setColor(Color.BLACK);
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 60F));
		
		for (String line : text.split("\n")) { 
			g2.setColor(Color.BLACK);
			g2.drawString(line, x, y);	
			g2.setColor(battle_white);
			g2.drawString(line, x-2, y-2);
			y += gp.tileSize;
		}
		
		x = gp.tileSize * 9;
		y = gp.screenHeight - gp.tileSize * 4; 
		int width = gp.tileSize * 7;
		int height = gp.tileSize * 4;
		
		drawSubWindow(x, y, width, height, 12, 10, battle_white, battle_gray);
		
		x += gp.tileSize / 2;
		y += gp.tileSize * 1.5;
		height = gp.tileSize;
		
		g2.setColor(Color.BLACK);
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 60F));
		g2.setStroke(new BasicStroke(4));		
		
		for (int i = 0; i < battleOptions.size(); i++) {			
			
			if (i == 1) {
				x += gp.tileSize * 4;
			}
			else if (i == 2) {
				x = gp.tileSize * 9 + gp.tileSize / 2;
				y += gp.tileSize * 1.5;
			}
			else if (i == 3) {
				x += gp.tileSize * 4;
			}
			
			if (commandNum == i) {
				width = (int)g2.getFontMetrics().getStringBounds(battleOptions.get(i), g2).getWidth();
				g2.setColor(battle_red);
				g2.drawRect(x - 4, y - (int) (gp.tileSize * 0.85), width + 4, height);
				g2.setColor(Color.BLACK);
			}
			
			g2.drawString(battleOptions.get(i), x, y);
		}
		
		if (gp.keyH.aPressed) {
			gp.keyH.aPressed = false;
			
			if (commandNum == 0) {
				advanceDialogue();	
				battleSubState = subState_Moves;		
			}
		}
	}
	private void drawBattleMovesetWindow() {
		
		int x = 0;
		int y = gp.screenHeight - gp.tileSize * 4; 
		int width = gp.screenWidth - gp.tileSize * 4;
		int height = gp.tileSize * 4;
		
		drawSubWindow(x, y, width, height, 12, 10, battle_white, battle_gray);
		
		x = gp.tileSize / 2;
		y = (int) (gp.screenHeight - gp.tileSize * 2.5);		
		height = gp.tileSize;
		
		g2.setColor(Color.BLACK);
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 57F));		
		g2.setStroke(new BasicStroke(4));
		
		for (int i = 0; i < gp.btlManager.fighter[0].getMoveSet().size(); i++) {			
						
			if (i == 1) {
				x += gp.tileSize * 6;		
			}
			else if (i == 2) {
				x = gp.tileSize / 2;
				y += gp.tileSize * 1.5;
			}
			else if (i == 3) {
				x += gp.tileSize * 6;
			}
			
			if (commandNum == i) {
				width = (int)g2.getFontMetrics().getStringBounds(gp.btlManager.fighter[0].getMoveSet().get(i).getName(), g2).getWidth();
				g2.setColor(battle_red);
				g2.drawRect(x - 4, y - (int) (gp.tileSize * 0.85), width + 4, height);
				g2.setColor(Color.BLACK);
			}
			
			g2.drawString(gp.btlManager.fighter[0].getMoveSet().get(i).getName(), x, y);
		}
		
		if (gp.keyH.aPressed) {		
			gp.btlManager.setPlayerMove(commandNum);
			advanceDialogue();		
			battleSubState = subState_Dialogue;	
		}
		else if (gp.keyH.bPressed) {			
			advanceDialogue();
			battleSubState = subState_Options;			
		}
	}	
	private void drawBattleMoveDescriptionWindow() {
		
		String text;
		int x = gp.tileSize * 12;
		int y = gp.screenHeight - gp.tileSize * 4; 
		int width = gp.tileSize * 4;
		int height = gp.tileSize * 4;
		
		drawSubWindow(x, y, width, height, 12, 10, gp.btlManager.fighter[0].getMoveSet().get(commandNum).getType().getColor(), battle_gray);
						
		x += gp.tileSize * 0.3;
		y = (int) (gp.screenHeight - gp.tileSize * 2.5);	
		text = "PP " + gp.btlManager.fighter[0].getMoveSet().get(commandNum).getpp() + "/" + 
				gp.btlManager.fighter[0].getMoveSet().get(commandNum).getbpp();
		
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 57F));	
		g2.setColor(Color.BLACK);
		g2.drawString(text, x, y);	
		g2.setColor(battle_white);
		g2.drawString(text, x-2, y-2);
		
		y += gp.tileSize * 1.5;
		text = gp.btlManager.fighter[0].getMoveSet().get(commandNum).getType().getName();
		g2.setColor(Color.BLACK);
		g2.drawString(text, x, y);	
		g2.setColor(battle_white);
		g2.drawString(text, x-2, y-2);
	}		
	private void drawBattleDialogueWindow() {
		
		int x = 0;
		int y = gp.screenHeight - gp.tileSize * 4; 
		int width = gp.screenWidth;
		int height = gp.tileSize * 4;
		
		drawSubWindow(x, y, width, height, 12, 10, battle_green, battle_red);
	}	
	
	private void drawBattleDialogue() {
		
		drawBattleDialogueWindow();	
		
		int x = gp.tileSize / 2;
		int y = (int) (gp.screenHeight - gp.tileSize * 2.2);
		
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
  			g2.setColor(Color.BLACK);
			g2.drawString(line, x, y);	
			g2.setColor(battle_white);
			g2.drawString(line, x-2, y-2);
			y += gp.tileSize;
		} 	
  		
  		dialogueTimer++;
  		if (dialogueTimer >= dialogueTimerMax) {  	
  			dialogueIndex++;
  			dialogueTimerMax = 2 * 45;
  			advanceDialogue();
  			
  		}
  		
  		if (seTimer.size() > 0 && dialogueTimer == seTimer.get(0)) {
  			playBattleSE();
  		}
	}		
	public void advanceDialogue() {
		gp.keyH.aPressed = false;
		charIndex = 0;
		combinedText = "";	
		currentDialogue = "";
		commandNum = 0;
		dialogueTimer = 0;
	}
	private void playBattleSE() {	

		if (category_SE.size() == seTimer.size()) {
			gp.playSE(category_SE.get(0), record_SE.get(0));  				
			category_SE.remove(0);	
			record_SE.remove(0);	
		}			
			
		seTimer.remove(0);	
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
		g2.drawRoundRect(x + 2, y + 2, width - 4, height - 4, curve, curve);		
	}
	public int getXforRightAlignText(String text, int tailX) {
		int length = (int)g2.getFontMetrics().getStringBounds(text, g2).getWidth();
		int x = tailX - length;
		return x;
	}	
}