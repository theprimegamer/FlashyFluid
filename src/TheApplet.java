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
	
	public void start() {
		//Create the app window
		this.setSize(new Dimension(WIDTH, HEIGHT));
		this.addMouseListener(this);
		
		//Initialize the particle array
		this.parts = new ArrayList<Particle>();
		
		//My test case
		tylerTest();
		
		Thread t = new Thread(this);
		t.start();
	}
	
	public void paint(Graphics gg) {
		System.out.println("Painting");
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
	}
	
	public void applyGravity(Particle p) {
		p.vy += GRAVITY;
	}
	
	public void applyVelocity(Particle p) {
		p.x += p.vx;
		p.y += p.vy;
	}
	
	public void run() {
		repaint();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
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
