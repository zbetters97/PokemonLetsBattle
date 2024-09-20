package environment;

import java.awt.Graphics2D;

import application.GamePanel;

public class EnvironmentManager {

	private GamePanel gp;
	public Lighting lighting;
	
	public EnvironmentManager(GamePanel gp) {
		this.gp = gp;
	}
	
	public void update() {
		lighting.update();
	}
	
	public void setup() {
		lighting = new Lighting(gp); 
	}
	
	public void draw(Graphics2D g2) {
		lighting.draw(g2);
	}
}