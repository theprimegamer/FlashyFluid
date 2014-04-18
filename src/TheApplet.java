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
	private static final int GRID_WIDTH = 100;
	private static final int GRID_HEIGHT = 40;
	private static final int GRID_UNIT = 8;
	private static final int WIDTH = GRID_UNIT*GRID_WIDTH;
	private static final int HEIGHT = GRID_UNIT*GRID_HEIGHT;
	
	//Used to make the applet run
	private static Thread t = null;
	
	//Grid Variables
	private static float[][] values;
	private static float[][] xConvect;
	private static float[][] yConvect;
	private static float[][] corners;
	
	public void start() {
		//Create the app window
		this.setSize(new Dimension(WIDTH, HEIGHT));
		this.addMouseListener(this);
		
		//Create the Thread if it doesn't exist
		if (t == null) {
			t = new Thread(this);
			t.start();
		}
		
		//Grid setup
		values = new float[GRID_HEIGHT][GRID_WIDTH];
		xConvect = new float[GRID_HEIGHT][GRID_WIDTH-1];
		yConvect = new float[GRID_HEIGHT-1][GRID_WIDTH];
		corners = new float[GRID_HEIGHT+1][GRID_WIDTH+1];
		
		//Initialize the grid to have some fluid in it. (2000 units)
		for (int r = 0; r < values.length; r++) {
			for (int c = 0; c < values[r].length; c++) {
				values[r][c] = 1;
			}
		}
	}
	
	public void paint(Graphics gg) {
		//Better graphics
		Graphics2D g = (Graphics2D) gg;
		
		//Clear the scene
		g.setBackground(Color.lightGray);
		g.clearRect(0, 0, WIDTH, HEIGHT);
		
		//Marching Squares
	}
	
	public void update() {
		System.out.println("update");
		moveFluid();
		calculateNewConvections();
	}
	
	public void moveFluid() {
		//Checking the xMovements
		for (int r = 0; r < xConvect.length; r++) {
			for (int c = 0; c < xConvect[r].length; c++) {
				//If moving right
				if (xConvect[r][c] > 0) {
					//Enough fluid to move
					if (values[r][c] >= xConvect[r][c]) {
						values[r][c] -= xConvect[r][c];
						values[r][c+1] += xConvect[r][c];
						//move all the fluid
					} else {
						values[r][c+1] += values[r][c];
						values[r][c] = 0;
					}
				//If moving left
				} else {
					//Flip sign
					float temp = -xConvect[r][c];
					//Enough fluid to move
					if (values[r][c+1] >= temp) {
						values[r][c+1] -= temp;
						values[r][c] += temp;
					//move all fluid
					} else {
						values[r][c] += values[r][c+1];
						values[r][c+1] = 0;
					}
				}
			}
		}
		
		//Check the yMovement
		for (int r = 0; r < yConvect.length; r++) {
			for (int c = 0; c < yConvect[r].length; c++) {
				//If moving down
				if (yConvect[r][c] > 0) {
					//Enough Fluid
					if (values[r][c] >= yConvect[r][c]) {
						values[r][c] -= yConvect[r][c];
						values[r+1][c] += yConvect[r][c];
					//not enough
					} else {
						values[r+1][c] += values[r][c];
						values[r][c] = 0;
					}
				//Moving up
				} else {
					//Flip sign
					float temp = -yConvect[r][c];
					//Enough fluid
					if (values[r+1][c] >= temp) {
						values[r+1][c] -= temp;
						values[r][c] += temp;
					//Not enough
					} else {
						values[r][c] += values[r+1][c];
						values[r+1][c] = 0;
					}
				}
			}
		}
	}
	
	public void calculateNewConvections() {
		
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
