package environment;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.image.BufferedImage;

import application.GamePanel;

public class Lighting {

	private GamePanel gp;
	BufferedImage darknessFilter;
	
	// DAY AND NIGHT CYCLE
	public int dayCounter;
	private final int dayLength = 5400; // 90 SECONDS (1/60 * 5400)
	public final int day = 0;
	private final int dusk = 1;
	private final int night = 2;
	private final int dawn = 3;
	public int dayState = day;
	public float filterAlpha = 0f;
	
	public Lighting(GamePanel gp) {
		this.gp = gp;
		setLightSource();
	}
	
	public void update() {
				
		// ONLY PASS TIME WHEN OUTSIDE
		if (gp.currentArea == gp.outside) {
		
			// DAY
			if (dayState == day) {
				dayCounter++;
				if (dayCounter > dayLength) { // 100 SEC (60/1 SEC)
					dayState = dusk;
					dayCounter = 0;			
					setLightSource();
				}
			}
			// DUSK
			else if (dayState == dusk) {				
									
				filterAlpha += 0.001f; // 16 SEC ([1/0.001] / 60)
				if (filterAlpha > 1f) { // NO OPACITY (DARK)
					
					dayState = night;
					filterAlpha = 1f;	
				}
			}
			// NIGHT
			else if (dayState == night) {	
				dayCounter+=2;
				if (dayCounter > dayLength) {
					dayState = dawn;
					dayCounter = 0;				
				}
			}
			// DAWN
			else if (dayState == dawn) {				
				filterAlpha -= 0.001f;
				if (filterAlpha < 0f) { // FULL OPACITY (LIGHT)
					dayState = day;
					filterAlpha = 0f;
				}
			}
		}
	}
	
	public void setLightSource() {
		
		darknessFilter = new BufferedImage(gp.screenWidth, gp.screenHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = (Graphics2D)darknessFilter.getGraphics();

		// LIGHTING CIRCLE CENTER POINT ON PLAYER
		int centerX = gp.player.screenX + gp.tileSize / 2;
		int centerY = gp.player.screenY + gp.tileSize / 2;
		
		// GRADIENT AFFECT
		Color color[] = new Color[12];	
		float fraction[] = new float[12];
			
		
		// BLUE OR RED MOON FILTER
		float red = 0;
		float blue = 0.1f;			
		
		// OPACITY OF GRADIENT (1 = DARK, 0 = LIGHT)
		color[0] = new Color(red,0,blue,0.1f);
		color[1] = new Color(red,0,blue,0.42f);
		color[2] = new Color(red,0,blue,0.52f);
		color[3] = new Color(red,0,blue,0.61f);
		color[4] = new Color(red,0,blue,0.69f);
		color[5] = new Color(red,0,blue,0.76f);
		color[6] = new Color(red,0,blue,0.82f);
		color[7] = new Color(red,0,blue,0.87f);
		color[8] = new Color(red,0,blue,0.91f);
		color[9] = new Color(red,0,blue,0.92f);
		color[10] = new Color(red,0,blue,0.93f);
		color[11] = new Color(red,0,blue,0.94f);
		
		// DISTANCE FROM CIRCLE (1 = EDGE, 0 = CENTER)
		fraction[0] = 0f;
		fraction[1] = 0.4f;
		fraction[2] = 0.5f;
		fraction[3] = 0.6f;
		fraction[4] = 0.65f;
		fraction[5] = 0.7f;
		fraction[6] = 0.75f;
		fraction[7] = 0.8f;
		fraction[8] = 0.85f;
		fraction[9] = 0.9f;
		fraction[10] = 0.95f;
		fraction[11] = 1f;	
						
		// DRAW GRAIDENT ON LIGHT CIRCLE
		RadialGradientPaint gPaint = new RadialGradientPaint(
				centerX, centerY, 150, fraction, color
		);
		g2.setPaint(gPaint);
		
		// DRAW DARKNESS RECTANGLE
		g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight);
		
		g2.dispose();
	}
	
	public void resetDay() {		
		dayState = day;
		filterAlpha = 0f;
	}
	
	public void draw(Graphics2D g2) {
		
		// ONLY CHANGE LIGHTING WHEN OUTSIDE
		if (gp.currentArea == gp.outside) {
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, filterAlpha));
			g2.drawImage(darknessFilter, 0, 0, null);
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
		}
	}
}