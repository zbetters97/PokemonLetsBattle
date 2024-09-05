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
	
	// DIALOGUE HANDLER	
	public String currentDialogue = "";
	public String combinedText = "";
	public int dialogueCounter = 0;	
	public int charIndex = 0;	
	public int textSpeed = 3;
	public boolean canSkip = false;
	public int commandNum = 0;
	
	// FIGHTER VALUES
	public Pokemon fighter_one, fighter_two;	
	private final int fighter_one_startX;
	private final int fighter_two_startX;
	private final int fighter_one_endX;
	private final int fighter_two_endX;
	private int fighter_one_X;
	private int fighter_two_X;
	private final int fighter_one_Y;
	private final int fighter_two_Y;	
	
	private boolean fighterReady = false;	
	
	public String battleDialogue;
	private ArrayList<String> battleOptions;
	
	public int battleSubState;
	public final int subStateEncounter = 1;
	public final int subStateOptions = 2;
	public final int subStateMoves = 3;
	public final int subStateAttack = 4;
	public final int subStateRun = 5;
	
	public UI(GamePanel gp) {
		this.gp = gp;
		
		battleOptions = new ArrayList<String>();
		battleOptions.addAll(Arrays.asList("FIGHT", "BAG", "POKEMON", "RUN"));
		
		fighter_one_startX = gp.tileSize * 14;
		fighter_two_startX = 0 - gp.tileSize * 3;
		fighter_one_endX = gp.tileSize * 2;
		fighter_two_endX = gp.tileSize * 9;
		
		fighter_one_X = fighter_one_startX;
		fighter_two_X = fighter_two_startX;
		fighter_one_Y = gp.tileSize * 3;
		fighter_two_Y = 0;
		
		// FONT DECLARATION		
		try {
			InputStream is;
			
			is = getClass().getResourceAsStream("/fonts/pokemon-ds.ttf");
			PK_DS = Font.createFont(Font.TRUETYPE_FONT, is);
		} 
		catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}
	}
	
	public void draw(Graphics2D g2) {
		
		this.g2 = g2;
		
		g2.setFont(PK_DS);
		g2.setColor(Color.white);
		
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
		
		g2.setColor(battle_white);  
		g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
		
		if (battleSubState == subStateEncounter) {
			animateFighterOneSprite(); animateFighterTwoSprite();
			drawBattleDialogueWindow();		
			if (fighterReady) {
				drawBattleDialogue();
				if (gp.keyH.aPressed && canSkip) {
					gp.keyH.playCursorSE();	
					fighterReady = false;
					startBattle();		
				}
			}
		}
		else if (battleSubState == subStateOptions) {
			drawFighterOne(); drawFighterTwo();
			drawBattleDialogueWindow();		
			drawBattleDialogue();
			drawBattleOptionsWindow();			
		}
		else if (battleSubState == subStateMoves) {
			drawFighterOne(); drawFighterTwo();
			drawBattleMovesetWindow();
			drawBattleMoveDescriptionWindow();
		}
		else if (battleSubState == subStateAttack) {
			drawFighterOne(); drawFighterTwo();
			drawBattleDialogueWindow();		
			drawBattleDialogue();
		}
		else if (battleSubState == subStateRun) {
			drawFighterOne(); drawFighterTwo();
			drawBattleDialogueWindow();		
			drawBattleDialogue();
		}
	}
	
	public void startBattle() {		
		advanceDialogue();
		battleDialogue = "What will\n" + fighter_one.getName() + " do?";
		battleSubState = subStateOptions;			
	}
	
	private void animateFighterOneSprite() {
		
		if (fighter_one_endX < fighter_one_X) fighter_one_X -= 5;		
		else fighterReady = true;		
				
		g2.drawImage(fighter_one.getBackSprite(), fighter_one_X, fighter_one_Y, null);
	}
	private void animateFighterTwoSprite() {
		
		if (fighter_two_endX > fighter_two_X) fighter_two_X += 5;		
		else fighterReady = true;		
		
		g2.drawImage(fighter_two.getFrontSprite(), fighter_two_X, fighter_two_Y, null);
	}
	
	private void drawFighterOne() {				
		g2.drawImage(fighter_one.getBackSprite(), fighter_one_X, fighter_one_Y, null);
		drawFighterOneWindow();
	}
	private void drawFighterTwo() {				
		g2.drawImage(fighter_two.getFrontSprite(), fighter_two_X, fighter_two_Y, null);
		drawFighterTwoWindow();
	}
	private void drawFighterOneWindow() {		
		
		int x = (int) (gp.tileSize * 9.3);
		int y = (int) (gp.tileSize * 5.3);
		int width = gp.tileSize * 6;
		int height = (int) (gp.tileSize * 2.5);
		
		drawSubWindow(x, y, width, height, 15, 6, battle_yellow, Color.BLACK);
		
		x += gp.tileSize * 0.3;
		y += gp.tileSize * 0.8;
		g2.setColor(Color.BLACK);
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 40F));
		g2.drawString(fighter_one.getName(), x, y);
		
		x += gp.tileSize * 4;
		g2.drawString("Lv:" + fighter_one.getLevel(), x, y);
		
		x = (int) (gp.tileSize * 10.3);
		y += gp.tileSize * 0.7;
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 30F));
		g2.drawString("HP", x, y);
		
		x += gp.tileSize * 0.6;
		y -= gp.tileSize * 0.35;
		width = gp.tileSize * 4;
		height = (int) (gp.tileSize * 0.4);
		g2.setStroke(new BasicStroke(2));
		g2.drawRoundRect(x, y, width, height, 15, 15);
				
		double remainHP = (double)fighter_one.getHP() / (double)fighter_one.getBHP();			
		if (remainHP >= .50) g2.setColor(Color.GREEN);
		else if (remainHP >= .25) g2.setColor(Color.YELLOW);
		else g2.setColor(Color.RED);	
		
		width *= remainHP;
		
		g2.fillRoundRect(x + 1, y + 1, width - 2, height - 2, 15, 15);
				
		x += gp.tileSize * 2.4;
		y += gp.tileSize;
		g2.setColor(Color.BLACK);
		g2.drawString(fighter_one.getHP() + " / " + fighter_one.getBHP(), x, y);
	}
	private void drawFighterTwoWindow() {
		
		int x = gp.tileSize;
		int y = gp.tileSize;
		int width = gp.tileSize * 6;
		int height = gp.tileSize * 2;
		
		drawSubWindow(x, y, width, height, 15, 6, battle_yellow, Color.BLACK);
		
		x += gp.tileSize * 0.3;
		y += gp.tileSize * 0.8;
		g2.setColor(Color.BLACK);
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 40F));
		g2.drawString(fighter_two.getName(), x, y);
		
		x += gp.tileSize * 4;
		g2.drawString("Lv:" + fighter_one.getLevel(), x, y);
		
		x = (int) (gp.tileSize * 2);
		y += gp.tileSize * 0.7;
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 30F));
		g2.drawString("HP", x, y);
		
		x += gp.tileSize * 0.6;
		y -= gp.tileSize * 0.35;
		width = gp.tileSize * 4;
		height = (int) (gp.tileSize * 0.4);
		g2.setStroke(new BasicStroke(2));
		g2.drawRoundRect(x, y, width, height, 15, 15);
		
		double remainHP = (double)fighter_two.getHP() / (double)fighter_two.getBHP();			
		if (remainHP >= .50) g2.setColor(Color.GREEN);
		else if (remainHP >= .25) g2.setColor(Color.YELLOW);
		else g2.setColor(Color.RED);	
		
		width *= remainHP;
		
		g2.fillRoundRect(x, y + 1, width, height - 2, 15, 15);
	}
	
	private void drawBattleDialogueWindow() {
		
		int x = 0;
		int y = gp.screenHeight - gp.tileSize * 4; 
		int width = gp.screenWidth;
		int height = gp.tileSize * 4;
		
		drawSubWindow(x, y, width, height, 12, 10, battle_green, battle_red);
	}	
	private void drawBattleDialogue() {
						
		int x = gp.tileSize / 2;
		int y = (int) (gp.screenHeight - gp.tileSize * 2.2);
		
		g2.setColor(Color.BLACK);
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 60F));
		
		if (dialogueCounter == textSpeed) {
			char characters[] = battleDialogue.toCharArray();
						
			if (charIndex < characters.length) {
				
				// PLAY DIALOGUE SE
				
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
		
  		for (String line : currentDialogue.split("\n")) { 
  			g2.setColor(Color.BLACK);
			g2.drawString(line, x, y);	
			g2.setColor(battle_white);
			g2.drawString(line, x-2, y-2);
			y += gp.tileSize;
		} 
	}
	private void advanceDialogue() {		
		canSkip = false;
		charIndex = 0;
		combinedText = "";
		gp.keyH.aPressed = false;			
	}
	
	private void drawBattleOptionsWindow() {
		
		int x = gp.tileSize * 9;
		int y = gp.screenHeight - gp.tileSize * 4; 
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
		
		for (int i = 0; i < fighter_one.getMoveSet().size(); i++) {			
						
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
				width = (int)g2.getFontMetrics().getStringBounds(fighter_one.getMoveSet().get(i).getName(), g2).getWidth();
				g2.setColor(battle_red);
				g2.drawRect(x - 4, y - (int) (gp.tileSize * 0.85), width + 4, height);
				g2.setColor(Color.BLACK);
			}
			
			g2.drawString(fighter_one.getMoveSet().get(i).getName(), x, y);
		}
	}	
	private void drawBattleMoveDescriptionWindow() {
		
		int x = gp.tileSize * 12;
		int y = gp.screenHeight - gp.tileSize * 4; 
		int width = gp.tileSize * 4;
		int height = gp.tileSize * 4;
		
		drawSubWindow(x, y, width, height, 12, 10, battle_white, battle_gray);
						
		x += gp.tileSize * 0.3;
		y = (int) (gp.screenHeight - gp.tileSize * 2.5);	
		String pp = "PP " + fighter_one.getMoveSet().get(commandNum).getpp() + "/" + 
				fighter_one.getMoveSet().get(commandNum).getbpp();
		g2.setColor(Color.BLACK);
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 57F));	
		g2.drawString(pp, x, y);	
		
		y += gp.tileSize * 1.5;
		String type = fighter_one.getMoveSet().get(commandNum).getType().getName();
		g2.drawString(type, x, y);	
	}	
	
	private void drawSubWindow(int x, int y, int width, int height, int curve, int borderStroke, 
			Color fillCollor, Color borderColor) {
		
		g2.setColor(fillCollor);
		g2.fillRoundRect(x, y, width, height, curve, curve);
		
		g2.setColor(borderColor);
		g2.setStroke(new BasicStroke(borderStroke));
		g2.drawRoundRect(x + 4, y + 4, width - 8, height - 8, curve, curve);		
	}
}