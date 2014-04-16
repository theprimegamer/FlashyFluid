import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JApplet;

public class TheApplet extends JApplet implements MouseListener, Runnable {
	
	private static final long serialVersionUID = 0L;
	
	//App variables
	private static final int WIDTH = 600;
	private static final int HEIGHT = 300;
	
	//Fluid sim variables
	private static ArrayList<Particle> parts;
	private static final int GRAVITY = 1;
	private static final float FRICTION = 1;
	
	//Used to make the applet run
	private static Thread t = null;
	
	public void start() {
		//Create the app window
		this.setSize(new Dimension(WIDTH, HEIGHT));
		this.addMouseListener(this);
		
		//Initialize the particle array
		this.parts = new ArrayList<Particle>();
		
		//My test case
		tylerTest();
		
		//Create the Thread if it doesn't exist
		if (t == null) {
			t = new Thread(this);
			t.start();
		}
	}
	
	public void paint(Graphics gg) {
		//Better graphics
		Graphics2D g = (Graphics2D) gg;
		
		//Clear the scene
		g.setBackground(Color.lightGray);
		g.clearRect(0, 0, WIDTH, HEIGHT);
		
		//Draw particles
		g.setColor(Color.blue.brighter());
		for (Particle p : this.parts) {
			p.draw(g);
		}
		g.drawString("Particles: " + parts.size(), 0, 12);
	}
	
	public void update() {
		//Move each particle
		for (Particle p : this.parts) {
			applyGravity(p);
			updateVelocity(p);
			applyVelocity(p);
			checkBounds(p);
		}
	}
	
	public void applyGravity(Particle p) {
		//Gravity is changed using the constant
		p.vy += GRAVITY;
	}
	
	public void updateVelocity(Particle p) {
		//This is where the grid comes in
	}
	
	public void applyVelocity(Particle p) {
		//This shouldn't need to be touched
		p.x += p.vx;
		p.y += p.vy;
	}
	
	public void checkBounds(Particle p) {
		//check lower
		if (p.y > HEIGHT) {
			p.y = HEIGHT;
			p.vy = -p.vy + FRICTION;
		}
		
		//check upper
		
		//check right
		
		//check left
	}
	
	public void run() {
		Thread myThread = Thread.currentThread();
        while (t == myThread) {
        	update();
            repaint();
            try {
                Thread.sleep(333);
            } catch (InterruptedException e) {
            	e.printStackTrace();
            }
        }
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		Particle p = new Particle(e.getX(), e.getY(), 0, 0);
		parts.add(p);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public void tylerTest() {
		this.parts.add(new Particle(100, 100, 0, 0));
	}
}
