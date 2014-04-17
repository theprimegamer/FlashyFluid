import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JApplet;

public class TheApplet extends JApplet implements MouseListener, Runnable {
	
	private static final long serialVersionUID = 0L;
	
	//App variables
	private static final int WIDTH = 600;
	private static final int HEIGHT = 300;
	
	//Used to make the applet run
	private static Thread t = null;
	
	public void start() {
		//Create the app window
		this.setSize(new Dimension(WIDTH, HEIGHT));
		this.addMouseListener(this);
		
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
	}
	
	public void update() {
		//Advect Fluid
	}
	
	public void run() {
		Thread myThread = Thread.currentThread();
        while (t == myThread) {
        	update();
            repaint();
            try {
                Thread.sleep(33);
            } catch (InterruptedException e) {
            	e.printStackTrace();
            }
        }
	}

	@Override
	public void mouseClicked(MouseEvent e) {

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
}
