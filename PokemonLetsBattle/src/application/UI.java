package application;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.ImageIO;

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
	
	private BufferedImage current_arena;
	
	// DIALOGUE HANDLER	
	public String currentDialogue = "";
	public String combinedText = "";
	public int dialogueCounter = 0;	
	public int charIndex = 0;	
	public int textSpeed = 3;
	public boolean canSkip = false;
	public boolean skippable = false;
	public int commandNum = 0;
	private int dialogueTimer = 0;
	public int dialogueTimerMax = 60;
	
	// FIGHTER VALUES
	public Pokemon[] fighter = new Pokemon[2];
	private final int fighter_one_startX;
	private final int fighter_two_startX;
	private final int fighter_one_endX;
	private final int fighter_two_endX;
	private int fighter_one_X;
	private int fighter_two_X;
	private final int fighter_one_Y;
	private final int fighter_two_Y;	
	
	private int fighter_one_HP;
	private int fighter_two_HP;
	private int hpSpeed = 3;
	private int hpCounter = 0;
	
	private boolean fighterReady = false;	
	
	public String battleDialogue;
	private ArrayList<String> battleOptions;
	
	public int battleSubState;
	public int nextSubState;
	public final int subStateEncounter = 1;
	public final int subStateDialogue = 2;
	public final int subStateOptions = 3;
	public final int subStateMoves = 4;
	public final int subStateAttack = 5;
	public final int subStateRun = 6;	
	
	public UI(GamePanel gp) {
		this.gp = gp;
		
		battleOptions = new ArrayList<String>();
		battleOptions.addAll(Arrays.asList("FIGHT", "BAG", "POKEMON", "RUN"));
		
		current_arena = setup("/ui/arenas/grass", gp.tileSize * 7, gp.tileSize * 3);
		
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
			drawFighterPlatforms();
			animateFighterOneEntrance(); animateFighterTwoEntrance();
			drawBattleDialogueWindow();		
			if (fighterReady) {
				fighter_one_HP = fighter[0].getHP();
				fighter_two_HP = fighter[1].getHP();
				
				gp.playSE(3, fighter[1].getName());
				battleSubState = subStateDialogue;				
			}
		}		
		else if (battleSubState == subStateOptions) {
			gp.battleEngine.setDialogue("What will\n" + fighter[0].toString() + " do?");
			skippable = true;
			drawFighters();
			drawBattleDialogueWindow();	
			drawBattleOptionsWindow();			
		}
		else if (battleSubState == subStateMoves) {
			skippable = true;
			drawFighters();
			drawBattleMovesetWindow();
			drawBattleMoveDescriptionWindow();
		}
		else if (battleSubState == subStateAttack) {
			drawFighters();
			drawBattleDialogueWindow();	
		}
		else if (battleSubState == subStateRun) {
			skippable = true;
			drawFighters();
			drawBattleDialogueWindow();	
		}
		else if (battleSubState == subStateDialogue) {			
			drawFighters();
			drawBattleDialogueWindow();				
		}
	}
	
	private void animateFighterOneEntrance() {
		
		if (fighter_one_endX < fighter_one_X) fighter_one_X -= 5;		
		else fighterReady = true;		
				
		g2.drawImage(fighter[0].getBackSprite(), fighter_one_X, fighter_one_Y, null);
	}	
	private void animateFighterTwoEntrance() {
		
		if (fighter_two_endX > fighter_two_X) fighter_two_X += 5;		
		else fighterReady = true;		
		
		g2.drawImage(fighter[1].getFrontSprite(), fighter_two_X, fighter_two_Y, null);
	}
	
	private void drawFighters() {	
		
		drawFighterPlatforms();
		
		int x;
		int y;		
		
		g2.drawImage(fighter[0].getBackSprite(), fighter_one_X, fighter_one_Y, null);
		x = (int) (gp.tileSize * 9.6);
		y = (int) (gp.tileSize * 5.3);
		drawFighterWindow(x, y, 0);				
				
		g2.drawImage(fighter[1].getFrontSprite(), fighter_two_X, fighter_two_Y, null);
		x = (int) (gp.tileSize * 0.4);
		y = (int) (gp.tileSize * 0.4);
		drawFighterWindow(x, y, 1);		
	}
	private void drawFighterPlatforms() {
		
		int x;
		int y;
		
		x = fighter_one_X - gp.tileSize;
		y = fighter_one_Y + gp.tileSize * 4;
		g2.drawImage(current_arena, x, y, null);
		
		x = fighter_two_X - gp.tileSize;
		y = (int) (fighter_two_Y + gp.tileSize * 2.3);
		g2.drawImage(current_arena, x, y, null);
	}
	private void drawFighterWindow(int x, int y, int num) {
		
		int tempX = x + gp.tileSize;
		
		int width = gp.tileSize * 6;
		int height = (int) (gp.tileSize * 2.5);
		
		drawSubWindow(x, y, width, height, 15, 6, battle_yellow, Color.BLACK);
		
		x += gp.tileSize * 0.3;
		y += gp.tileSize * 0.8;
		g2.setColor(Color.BLACK);
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 40F));
		g2.drawString(fighter[num].getName(), x, y);
		
		x += gp.tileSize * 4.3;
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 27F));
		g2.drawString("Lv", x, y);
		
		x += gp.tileSize * 0.55;
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 40F));
		g2.drawString("" + fighter[num].getLevel(), x, y);
		
		x = tempX;
		y += gp.tileSize * 0.7;
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, 30F));
		g2.drawString("HP", x, y);
		
		x += gp.tileSize * 0.6;
		y -= gp.tileSize * 0.35;
		width = gp.tileSize * 4;
		height = (int) (gp.tileSize * 0.4);
		g2.setStroke(new BasicStroke(2));
		g2.drawRoundRect(x, y, width, height, 15, 15);
				
		height -= 2;
		drawFighterHealthBar(x, y, width, height, num);
		
		x += gp.tileSize * 2.4;
		y += gp.tileSize;
		g2.setColor(Color.BLACK);
		g2.drawString(fighter[num].getHP() + " / " + fighter[num].getBHP(), x, y);	
	}
	private void drawFighterHealthBar(int x, int y, int width, int height, int num) {
		
		int tempHP = 0;
		
		if (num == 0) tempHP = fighter_one_HP;
		else tempHP = fighter_two_HP;		
		
		if (hpCounter == hpSpeed) {
			if (tempHP > fighter[num].getHP()) tempHP--;			
			else if (tempHP < fighter[num].getHP()) tempHP++;
			
			hpCounter = 0;
		}
		else {
			hpCounter++;			
		}
		
		double remainHP = (double)tempHP / (double)fighter[num].getBHP();				
		if (remainHP >= .50) g2.setColor(Color.GREEN);
		else if (remainHP >= .25) g2.setColor(Color.YELLOW);
		else g2.setColor(Color.RED);	
		
		width *= remainHP;
		
		g2.fillRoundRect(x, y + 1, width, height, 15, 15);	
		
		if (num == 0) fighter_one_HP = tempHP;
		else fighter_two_HP = tempHP;	
	}
	
	private void drawBattleDialogueWindow() {
		
		int x = 0;
		int y = gp.screenHeight - gp.tileSize * 4; 
		int width = gp.screenWidth;
		int height = gp.tileSize * 4;
		
		drawSubWindow(x, y, width, height, 12, 10, battle_green, battle_red);
		
		if (fighterReady) drawBattleDialogue();
	}	
	private void drawBattleDialogue() {
		
		int x = gp.tileSize / 2;
		int y = (int) (gp.screenHeight - gp.tileSize * 2.2);
		
		g2.setColor(Color.BLACK);
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 60F));
		
		if (dialogueCounter == textSpeed) {
			
			char characters[] = gp.battleEngine.getDialogue().toCharArray();
						
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
  		
  		if (canSkip) {
  			dialogueTimer++;
  		}
  		
  		if (!skippable && dialogueTimer >= 60) {
  			advanceDialogue();
  		}
	}	
	private void advanceDialogue() {		
		gp.keyH.aPressed = false;
		dialogueTimer = 0;
		dialogueTimerMax = 0;
		skippable = false;
		canSkip = false;
		charIndex = 0;
		combinedText = "";	
		battleDialogue = "";	
		currentDialogue = "";
		
		battleSubState = nextSubState;
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
		
		if (gp.keyH.aPressed) {
			gp.keyH.aPressed = false;
			
			if (commandNum == 0) {
				advanceDialogue();	
				battleSubState = gp.ui.subStateMoves;		
			}
			else if (commandNum == 3) {
				advanceDialogue();	
				battleDialogue = "Got away safely!";
				battleSubState = gp.ui.subStateRun;
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
		
		for (int i = 0; i < fighter[0].getMoveSet().size(); i++) {			
						
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
				width = (int)g2.getFontMetrics().getStringBounds(fighter[0].getMoveSet().get(i).getName(), g2).getWidth();
				g2.setColor(battle_red);
				g2.drawRect(x - 4, y - (int) (gp.tileSize * 0.85), width + 4, height);
				g2.setColor(Color.BLACK);
			}
			
			g2.drawString(fighter[0].getMoveSet().get(i).getName(), x, y);
		}
		
		if (gp.keyH.aPressed) {
			
			advanceDialogue();	
			gp.keyH.aPressed = false;			
			
			gp.battleEngine.move(fighter[0].getMoveSet().get(commandNum), gp.battleEngine.cpuSelectMove());		
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
		String pp = "PP " + fighter[0].getMoveSet().get(commandNum).getpp() + "/" + 
				fighter[0].getMoveSet().get(commandNum).getbpp();
		g2.setColor(Color.BLACK);
		g2.setFont(g2.getFont().deriveFont(Font.PLAIN, 57F));	
		g2.drawString(pp, x, y);	
		
		y += gp.tileSize * 1.5;
		String type = fighter[0].getMoveSet().get(commandNum).getType().getName();
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