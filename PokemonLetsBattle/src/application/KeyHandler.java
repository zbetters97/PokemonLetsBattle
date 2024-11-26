package application;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

	private GamePanel gp;
	public boolean lock = true;
	public boolean upPressed, downPressed, leftPressed, rightPressed,					
					aPressed, bPressed, xPressed, yPressed, 
					lPressed, rPressed, zPressed,
					dupPressed, ddownPressed, 
					startPressed, selectPressed;
	public boolean debug = false;
	public boolean capital = true;
	
	public KeyHandler(GamePanel gp) {
		this.gp = gp;
	}
	
	@Override
	public void keyTyped(KeyEvent e) { } // not used

	@Override
	public void keyPressed(KeyEvent e) {
		
		int code = e.getKeyCode(); 
		
		if (gp.gameState == gp.playState) {
			playState(code);
		}
		else if (gp.gameState == gp.pauseState) {
			pauseState(code);
		}
		else if (gp.gameState == gp.dialogueState) {
			dialogueState(code);
		}	
		else if (gp.gameState == gp.tradeState) {
			tradeState(code);
		}
		else if (gp.gameState == gp.hmState || gp.gameState == gp.healState) {
			hmState(code);
		}	
		else if (gp.gameState == gp.battleState) {
			battleState(code);
		}
		else if (gp.gameState == gp.pcState) {
			pcState(code);
		}
	}
					
	// PLAY
	private void playState(int code) { 	

		if (code == gp.btn_UP) upPressed = true;
		if (code == gp.btn_DOWN) downPressed = true;
		if (code == gp.btn_LEFT) leftPressed = true;
		if (code == gp.btn_RIGHT) rightPressed = true;		
						
		if (code == gp.btn_A && lock) { aPressed = true; lock = false; }
		if (code == gp.btn_B && lock) { bPressed = true; lock = false; }
		if (code == gp.btn_X && lock) { xPressed = true; lock = false; }
		if (code == gp.btn_Y && lock) { yPressed = true; lock = false; }
		
		if (code == gp.btn_L && lock) { lPressed = true; lock = false; }
		if (code == gp.btn_R && lock) { rPressed = true; lock = false; }
		if (code == gp.btn_Z) { zPressed = true; }
		
		if (code == gp.btn_START && lock) { startPressed = true; lock = false; }			
		
		if (code == gp.btn_DEBUG) { if (debug) debug = false; else debug = true; }
	}
	
	// PAUSE
	private void pauseState(int code) { 		

		if (code == gp.btn_UP) { upPressed = true; }
		if (code == gp.btn_DOWN) { downPressed = true; }
		if (code == gp.btn_LEFT) { leftPressed = true; }
		if (code == gp.btn_RIGHT) { rightPressed = true; }
		
		if (code == gp.btn_A && lock) { aPressed = true; lock = false; }
		if (code == gp.btn_B && lock) { bPressed = true; lock = false; }
		if (code == gp.btn_X && lock) { xPressed = true; lock = false; }
		if (code == gp.btn_Y && lock) { yPressed = true; lock = false; }
		
		if (code == gp.btn_L && lock) { lPressed = true; lock = false; }
		if (code == gp.btn_R && lock) { rPressed = true; lock = false; }
		if (code == gp.btn_Z) { zPressed = true; }
		
		if (code == gp.btn_START && lock) { startPressed = true; lock = false; }			
		
		if (code == gp.btn_DEBUG) { if (debug) debug = false; else debug = true; }
	}
	
	// DIALOGUE
	private void dialogueState(int code) { 
		if (code == gp.btn_A && lock) {	aPressed = true; lock = false; }	
	}	
	
	// PAUSE
	private void tradeState(int code) { 		

		if (code == gp.btn_UP) { upPressed = true; }
		if (code == gp.btn_DOWN) { downPressed = true; }
		if (code == gp.btn_LEFT) { leftPressed = true; }
		if (code == gp.btn_RIGHT) { rightPressed = true; }
		
		if (code == gp.btn_A && lock) { aPressed = true; lock = false; }
		if (code == gp.btn_B && lock) { bPressed = true; lock = false; }
	}
	
	// HM
	private void hmState(int code) {
		
		if (code == gp.btn_A && lock) {	aPressed = true; lock = false; }
		
		if (code == gp.btn_UP) { 				
			gp.ui.commandNum--;
			if (gp.ui.commandNum < 0) gp.ui.commandNum = 0;
			else playCursorSE(); 
		}
		if (code == gp.btn_DOWN) { 
			gp.ui.commandNum++;
			if (gp.ui.commandNum > 1) gp.ui.commandNum = 1;
			else playCursorSE(); 
		}
	}
	
	// BATTLE
	private void battleState(int code) { 
		
		if (code == gp.btn_UP) { upPressed = true; }
		if (code == gp.btn_DOWN) { downPressed = true; }
		if (code == gp.btn_LEFT) { leftPressed = true; }
		if (code == gp.btn_RIGHT) {	rightPressed = true; }		

		if (code == gp.btn_A && lock) { aPressed = true; lock = false; }
		if (code == gp.btn_B && lock) { bPressed = true; lock = false; }
	}
	
	// PC
	private void pcState(int code) { 	

		if (code == gp.btn_UP) upPressed = true;
		if (code == gp.btn_DOWN) downPressed = true;
		if (code == gp.btn_LEFT) leftPressed = true;
		if (code == gp.btn_RIGHT) rightPressed = true;		
						
		if (code == gp.btn_A && lock) { aPressed = true; lock = false; }
		if (code == gp.btn_B && lock) { bPressed = true; lock = false; }
		if (code == gp.btn_X && lock) { xPressed = true; lock = false; }
		if (code == gp.btn_Y && lock) { yPressed = true; lock = false; }
		
		if (code == gp.btn_L && lock) { lPressed = true; lock = false; }
		if (code == gp.btn_R && lock) { rPressed = true; lock = false; }
		if (code == gp.btn_Z) { zPressed = true; }
		
		if (code == gp.btn_START && lock) { startPressed = true; lock = false; }		
	}
		
	// KEY RELEASED
	@Override
	public void keyReleased(KeyEvent e) {	
		int code = e.getKeyCode();		
		
		if (code == gp.btn_UP) { upPressed = false; lock = true; }
		if (code == gp.btn_DOWN) { downPressed = false; lock = true; }
		if (code == gp.btn_LEFT) { leftPressed = false; lock = true; }
		if (code == gp.btn_RIGHT) { rightPressed = false; lock = true; }
		
		if (code == gp.btn_A) { aPressed = false; lock = true; }
		if (code == gp.btn_B) { bPressed = false; lock = true; }
		if (code == gp.btn_X) { xPressed = false; lock = true; }
		if (code == gp.btn_Y) { yPressed = false; lock = true; }
		
		if (code == gp.btn_L) { lPressed = false; lock = true; }
		if (code == gp.btn_R) { rPressed = false; lock = true; }
		if (code == gp.btn_Z) { zPressed = false; lock = true; }
		
		if (code == gp.btn_DUP) { lock = true; }
		if (code == gp.btn_DDOWN) { lock = true; }
		
		if (code == gp.btn_START) { startPressed = false; lock = true; }	
		if (code == gp.btn_SELECT) { selectPressed = false; lock = true; }			
	}
	
	// SOUND EFFECTS
	public void playCursorSE() {
		gp.playSE(gp.menu_SE, "select");
	}
	public void playErrorSE() {
		gp.playSE(gp.menu_SE, "error");
	}
	public void playMenuOpenSE() {
		gp.playSE(gp.menu_SE, "menu-open");
	}
	public void playCloseSE() {
		gp.playSE(gp.menu_SE, "back");
	}
}