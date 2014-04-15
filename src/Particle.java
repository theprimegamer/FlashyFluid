import java.awt.Graphics2D;

/**
 * This class will store the particle that the fluid is made of.
 * It will have position and velocity.
 * @author tyler
 *
 */
public class Particle {
		
	//Instance vars
	public float x, y; 		//Positiion
	public float vx, vy;	//Velocity
	
	//Constants
	private static final int DIAMETER = 10;
		
	public Particle(float x, float y, float vx, float vy) {
		this.x = x;
		this.y = y;
		this.vx = vx;
		this.vy = vy;
	}
	
	public void draw(Graphics2D g) {
		g.fillOval((int) (this.x - DIAMETER), (int) (this.y - DIAMETER), DIAMETER, DIAMETER);
	}
}
