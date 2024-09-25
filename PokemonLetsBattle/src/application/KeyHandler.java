package application;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

	private GamePanel gp;
	private boolean lock = true;
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
		
		int code = e.getKeyCode(); // key pressed by user
		
		if (gp.gameState == gp.playState) {
			playState(code);
		}
		else if (gp.gameState == gp.pauseState) {
			pauseState(code);
		}
		else if (gp.gameState == gp.dialogueState) {
			dialogueState(code);
		}	
		else if (gp.gameState == gp.hmState) {
			hmState(code);
		}	
		else if (gp.gameState == gp.battleState) {
			battleState(code);
		}
		else if (gp.gameState == gp.partyState) {
			partyState(code);
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
		
		if (code == gp.btn_START && lock) { 
			playMenuOpenSE(); 
			gp.gameState = gp.pauseState; 
			lock = false;		
		}			
		
		if (code == gp.btn_DEBUG) { if (debug) debug = false; else debug = true; }
	}
	
	// PAUSE
	private void pauseState(int code) { 		

		if (code == gp.btn_UP) {
			if (gp.ui.commandNum != 0) {
				playCursorSE();
				gp.ui.commandNum--;
			}
		}
		if (code == gp.btn_DOWN) {
			if (gp.ui.commandNum != 6) {
				playCursorSE();
				gp.ui.commandNum++;
			}
		}
		if (code == gp.btn_LEFT) {		
		}
		if (code == gp.btn_RIGHT) {			
		}						
		if (code == gp.btn_A && lock) { aPressed = true; lock = false; }
		if (code == gp.btn_B && lock) { bPressed = true; lock = false; }
		if (code == gp.btn_X && lock) { xPressed = true; lock = false; }
		if (code == gp.btn_Y && lock) { yPressed = true; lock = false; }
		
		if (code == gp.btn_L && lock) { lPressed = true; lock = false; }
		if (code == gp.btn_R && lock) { rPressed = true; lock = false; }
		if (code == gp.btn_Z) { zPressed = true; }
		
		if (code == gp.btn_START && lock) { 
			gp.ui.commandNum = 0;
			gp.gameState = gp.playState;
			lock = false;
		}			
		
		if (code == gp.btn_DEBUG) { if (debug) debug = false; else debug = true; }
	}
	
	// DIALOGUE
	private void dialogueState(int code) { 
		if (code == gp.btn_A && lock) {	aPressed = true; lock = false; }		
	}	
	
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
		
		if (gp.ui.battleSubState == gp.ui.battle_Dialogue ||
				gp.ui.battleSubState == gp.ui.battle_Start) {				
			if (code == gp.btn_A && lock) { 
				aPressed = true; 
				lock = false; 
			}
		}
		else if (gp.ui.battleSubState == gp.ui.battle_Options) {			
			
			if (code == gp.btn_A && lock) { 					
				aPressed = true; lock = false; 
			}
			
			if (code == gp.btn_UP) {				
				upPressed = true;
				if (gp.ui.commandNum > 1) {
					playCursorSE();		
					gp.ui.commandNum -= 2;
				}
			}
			if (code == gp.btn_DOWN) {				
				downPressed = true;
				if (gp.ui.commandNum < 2) {
					playCursorSE();		
					gp.ui.commandNum += 2;
				}
			}
			if (code == gp.btn_LEFT) {
				leftPressed = true;
				if (gp.ui.commandNum > 0) {
					playCursorSE();		
					gp.ui.commandNum--;
				}
			}
			if (code == gp.btn_RIGHT) {				
				rightPressed = true;	
				if (gp.ui.commandNum < 3) {
					playCursorSE();		
					gp.ui.commandNum++;
				}
			}
		}
		else if (gp.ui.battleSubState == gp.ui.battle_Moves) {
			
			int maxMoves = gp.btlManager.fighter[0].getMoveSet().size() - 1;
			
			if (code == gp.btn_A && lock) { 	
				playCursorSE();					
				aPressed = true; lock = false;										
			}			
			if (code == gp.btn_B && lock) { 
				playCursorSE();						
				bPressed = true; lock = false; 	
			}			
			
			if (code == gp.btn_UP) {
				upPressed = true;
				if (gp.ui.commandNum > 1) {
					playCursorSE();		
					gp.ui.commandNum -= 2;
				}
			}
			if (code == gp.btn_DOWN) {				
				downPressed = true;
				if (gp.ui.commandNum < 2) {
					playCursorSE();		
					gp.ui.commandNum += 2;
					if (gp.ui.commandNum > maxMoves) gp.ui.commandNum = maxMoves;
				}
			}
			if (code == gp.btn_LEFT) {
				leftPressed = true;
				if (gp.ui.commandNum > 0) {
					playCursorSE();	
					gp.ui.commandNum--;
				}
			}
			if (code == gp.btn_RIGHT) {				
				rightPressed = true;	
				if (gp.ui.commandNum < maxMoves) {
					playCursorSE();		
					gp.ui.commandNum++;
				}
			}
		}		
		else if (gp.ui.battleSubState == gp.ui.battle_Swap) {			
			
			if (code == gp.btn_A && lock) { 
				playCursorSE();						
				aPressed = true; lock = false; 
			}
			
			if (code == gp.btn_UP) {				
				upPressed = true;
				if (gp.ui.commandNum != 0) {
					playCursorSE();		
					gp.ui.commandNum--;
				}
			}
			if (code == gp.btn_DOWN) {				
				downPressed = true;
				if (gp.ui.commandNum != 1) {
					playCursorSE();		
					gp.ui.commandNum++;
				}
			}			
		}
	}
	
	// PARTY
	private void partyState(int code) { 
		
		if (gp.ui.partySubState == gp.ui.party_Main) {			
			
			if (code == gp.btn_A && lock) { 
				playCursorSE();						
				aPressed = true; lock = false; 
			}
			if (code == gp.btn_B && lock) { 
				playCursorSE();						
				bPressed = true; lock = false; 	
			}
			
			if (code == gp.btn_UP) {				
				upPressed = true;
				if (gp.ui.fighterNum > 0) {
					playCursorSE();		
					gp.ui.fighterNum--;
				}
			}
			if (code == gp.btn_DOWN) {				
				downPressed = true;
				if (gp.ui.fighterNum < 6) {
					playCursorSE();		
					gp.ui.fighterNum++;
				}
			}
			if (code == gp.btn_LEFT) {
				leftPressed = true;
				playCursorSE();		
				gp.ui.fighterNum = 0;				
			}
			if (code == gp.btn_RIGHT) {				
				rightPressed = true;	
				if (gp.ui.fighterNum < 6) {
					playCursorSE();		
					gp.ui.fighterNum++;
				}
			}
		}
		if (gp.ui.partySubState == gp.ui.party_Main_Select) {			
			
			if (code == gp.btn_A && lock) { 	
				aPressed = true; lock = false; 
			}
			if (code == gp.btn_B && lock) { 
				playCursorSE();						
				bPressed = true; lock = false; 	
			}
			
			if (code == gp.btn_UP) {				
				upPressed = true;
				if (gp.ui.commandNum > 0) {
					playCursorSE();		
					gp.ui.commandNum--;
				}
			}
			if (code == gp.btn_DOWN) {				
				downPressed = true;
				if (gp.ui.commandNum < 2) {
					playCursorSE();		
					gp.ui.commandNum++;
				}
			}
		}
		else if (gp.ui.partySubState == gp.ui.party_Skills) {	
			
			int maxParty = gp.player.pokeParty.size() - 1;
			
			if (code == gp.btn_B && lock) { 
				playCursorSE();						
				bPressed = true; lock = false; 
			}			
			if (code == gp.btn_LEFT) {
				leftPressed = true;
				playCursorSE();					
			}
			if (code == gp.btn_RIGHT) {
				rightPressed = true;
				playCursorSE();	
			}			
			if (code == gp.btn_L) {				
				lPressed = true;
				if (0 < gp.ui.fighterNum) {
					gp.ui.fighterNum--;
					gp.ui.commandNum = 0;
					gp.playSE(3, gp.player.pokeParty.get(gp.ui.fighterNum).toString());  		
				}
			}
			if (code == gp.btn_R) {				
				rPressed = true;
				if (gp.ui.fighterNum < maxParty) {
					gp.ui.fighterNum++;
					gp.ui.commandNum = 0;
					gp.playSE(3, gp.player.pokeParty.get(gp.ui.fighterNum).toString());  
				}
			}
		}
		else if (gp.ui.partySubState == gp.ui.party_Moves) {			
			
			int maxParty = gp.player.pokeParty.size() - 1;
			int maxMoves = gp.player.pokeParty.get(gp.ui.fighterNum).getMoveSet().size() - 1;

			if (code == gp.btn_B && lock) { 
				playCursorSE();						
				bPressed = true; lock = false; 	
			}	
			
			if (code == gp.btn_UP) {				
				upPressed = true;
				
				if (gp.ui.commandNum > 0) {
					gp.ui.commandNum--;
					playCursorSE();	
				}				
			}
			if (code == gp.btn_DOWN) {				
				downPressed = true;	
				if (gp.ui.commandNum < maxMoves) {
					gp.ui.commandNum++;
					playCursorSE();	
				}
			}			
			if (code == gp.btn_LEFT) {
				leftPressed = true;
				playCursorSE();					
			}
			if (code == gp.btn_RIGHT) {
				rightPressed = true;
				playCursorSE();	
			}
			if (code == gp.btn_L) {				
				lPressed = true;
				if (0 < gp.ui.fighterNum) {
					gp.ui.fighterNum--;
					gp.ui.commandNum = 0;
					gp.playSE(3, gp.player.pokeParty.get(gp.ui.fighterNum).toString());  		
				}
			}
			if (code == gp.btn_R) {				
				rPressed = true;
				if (gp.ui.fighterNum < maxParty) {
					gp.ui.fighterNum++;
					gp.ui.commandNum = 0;
					gp.playSE(3, gp.player.pokeParty.get(gp.ui.fighterNum).toString());  		
				}
			}
		}
	}
	
	// KEY RELEASED
	@Override
	public void keyReleased(KeyEvent e) {	
		int code = e.getKeyCode();		
		
		if (code == gp.btn_UP) upPressed = false; 
		if (code == gp.btn_DOWN) downPressed = false;
		if (code == gp.btn_LEFT) leftPressed = false;
		if (code == gp.btn_RIGHT) rightPressed = false;
		
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
		gp.playSE(2, 0);
	}
	public void playErrorSE() {
		gp.playSE(2, 1);
	}
	public void playMenuOpenSE() {
		
	}
	public void playMenuCloseSE() {
		
	}
}