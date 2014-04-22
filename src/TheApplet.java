import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JApplet;

public class TheApplet extends JApplet implements MouseListener, Runnable {
	
	private static final long serialVersionUID = 0L;
	
	//App variables
	private static final int GRID_WIDTH = 100;
	private static final int GRID_HEIGHT = 40;
	private static final int GRID_UNIT = 8;
	private static final int BUFFER_DEPTH = 5;
	private static final int WIDTH = GRID_UNIT*GRID_WIDTH;
	private static final int HEIGHT = GRID_UNIT*(GRID_HEIGHT + BUFFER_DEPTH);
	private static final float GRAVITY = 1;
	private static final float THRESHHOLD = 1;
	private static final float SQRT_TWO = (float)Math.sqrt(2);
	
	//Used to make the applet run
	private static Thread t = null;
	private static long prevTime = System.currentTimeMillis();
	
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
				if (r > 36)
					values[r][c] = 1;
			}
		}
		//yConvect[36][12] = -20;
	}
	
	public void paint(Graphics gg) {
		//Better graphics
		Graphics2D g = (Graphics2D) gg;
		
		//Clear the scene
		g.setBackground(Color.lightGray);
		g.clearRect(0, 0, WIDTH, HEIGHT);
		
		g.setColor(Color.blue);
		
		corners = new float[GRID_HEIGHT+1][GRID_WIDTH+1];
		
		float leftMod = 1, rightMod = 1, topMod = 1, botMod = 1;
		
		for (int r = 0; r < values.length; r++) {
			if (r == 0)
				topMod *= SQRT_TWO;
			if (r == values.length - 1)
				botMod *= SQRT_TWO;
			for (int c = 0; c < values.length; c++) {
				if (c == 0)
					leftMod *= SQRT_TWO;
				if (c == values[r].length - 1)
					rightMod *= SQRT_TWO;
				float val = values[r][c];
				corners[r][c] += .25f * val * botMod * leftMod;
				corners[r+1][c] += .25f * val * topMod * leftMod;
				corners[r][c+1] += .25f * val * botMod * rightMod;
				corners[r+1][c+1] += .25f * val * topMod * rightMod;
			}
		}
		
		corners[0][0] *= 2;
		corners[0][GRID_WIDTH] *= 2;
		corners[GRID_HEIGHT][0] *= 2;
		corners[GRID_HEIGHT][GRID_WIDTH] *= 2;
		
		for (int r = 0; r < values.length; r++) {
			for (int c = 0; c < values.length; c++) {
				int mask = 0x0;
				if (corners[r+1][c] >= THRESHHOLD)
					mask |= 0x1;
				if (corners[r+1][c+1] >= THRESHHOLD)
					mask |= 0x2;
				if (corners[r][c+1] >= THRESHHOLD)
					mask |= 0x4;
				if (corners[r][c] >= THRESHHOLD)
					mask |= 0x8;
				switch(mask) {
					//TODO - Brian
					//Look on wikipedia for the marching squares cases
					//Our numbers line up exactly (ie if mask == 1, then the image for 1 will be the one to draw)
					//You may find my previous drawing method helpful (The one UNcommented at the bottom) to find the square in which you'll draw
					//You'll need to draw triangles n stuff though.  In this case using GRID_UNIT/2 might be nice to find endpoints of triangles
				}
//				if (c == values.length - 1)
//					System.out.println("Mask: " + mask);
				
			}
		}
		
		//Set the first row
		/*for (int c = 0; c < corners[0].length; c++) {
			//left edge case
			if (c == 0)
				corners[0][c] = values[0][0];
			//normal case
			else if (c < GRID_WIDTH)
				corners[0][c] = .5f * values[0][c-1] + .5f * values[0][c];
			//right edge case
			else 
				corners[0][c] = values[0][c-1];
		}
		
		//We can now assume that every row we examine has the top corners already computed
		for (int r = 0; r < values.length; r++) {
			//every other row
			if (r == values.length) {
				for (int c = 0; c < values[r].length; c++) {
					//Edge cases
					if (c == 0) {
						corners[r+1][c] = .5f * values[r][c] + .5f * values[r+1][c];
						corners[r+1][c+1] = .25f * values[r][c] + .25f * values[r][c + 1] + .25f * values[r+1][c] + .25f * values[r+1][c + 1];
					} else if (c == values[r].length - 1) {
						corners[r+1][c] = .25f * values[r][c-1] + .25f * values[r][c] + .25f * values[r+1][c-1] + .25f * values[r+1][c];
						corners[r+1][c+1] = .5f * values[r][c] + .5f * values[r+1][c];
					//normals
					} else {
						corners[r+1][c] = .25f * values[r][c-1] + .25f * values[r][c] + .25f * values[r+1][c-1] + .25f * values[r+1][c];
						corners[r+1][c+1] = .25f * values[r][c] + .25f * values[r][c + 1] + .25f * values[r+1][c] + .25f * values[r+1][c + 1];
					}
					//All the corners are found
					int mask = 0x0;
					if (corners[r+1][c] >= THRESHHOLD)
						mask |= 0x1;
					if (corners[r+1][c+1] >= THRESHHOLD)
						mask |= 0x2;
					if (corners[r][c+1] >= THRESHHOLD)
						mask |= 0x4;
					if (corners[r][c] >= THRESHHOLD)
						mask |= 0x8;
					switch(mask) {
						
					}
					System.out.println();
				}
				
			//last row
			} else {
				for (int c = 0; c < values[r].length; c++) {
					//Edge cases
					if (c == 0) {
						corners[r+1][c] = values[r][c];
						corners[r+1][c+1] = .5f * values[r][c] + .5f * values[r][c + 1];
					} else if (c == values[r].length - 1) {
						corners[r+1][c] = .5f * values[r][c-1] + .5f * values[r][c];
						corners[r+1][c+1] = values[r][c];
					//normals
					} else {
						corners[r+1][c] = .5f * values[r][c-1] + .5f * values[r][c];
						corners[r+1][c+1] = .5f * values[r][c] + .5f * values[r][c+1];
					}
					//All the corners are found
					int mask = 0x0;
					if (corners[r+1][c] >= THRESHHOLD)
						mask |= 0x1;
					if (corners[r+1][c+1] >= THRESHHOLD)
						mask |= 0x2;
					if (corners[r][c+1] >= THRESHHOLD)
						mask |= 0x4;
					if (corners[r][c] >= THRESHHOLD)
						mask |= 0X8;
					switch(mask) {
						
					}
				}
			}
		}*/
		for (int r = 0; r < values.length; r++) {
			for (int c = 0; c < values[r].length; c++) {
				if (values[r][c] > 0)
					g.fillRect(c*GRID_UNIT, r*GRID_UNIT, GRID_UNIT, GRID_UNIT);
			}
		}
		g.fillRect(0, GRID_HEIGHT*GRID_UNIT, GRID_WIDTH*GRID_UNIT, GRID_UNIT*BUFFER_DEPTH);
		//Marching Squares
	}
	
	public void update(float dt) {
		//Could merge these methods for better runtime.
		//This is just to make sure the algorithms are correct
		//Adjust millisecs to secs
		System.out.println(dt);
		dt /= 1000;
		applyForces(dt);
		moveFluid(dt);
		calculateNewConvections(dt);
	}
	
	public void applyForces(float dt) {
		//TODO - Brian
		//This is where you'll integrate the object that Monika and Rob are working on
		//It'll take in a timestep (dt - maybe in millis) and return a force and frequency
		//You'll just need to translate these two things into the yConvect matrix
		//It'll probably be like yConvect[39][some_freq_transform] = -force*some_constant
	}
	
	public void moveFluid(float dt) {
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
	
	//Doing backward advection
	public void calculateNewConvections(float dt) {
		float[][] tempXCon = new float[xConvect.length][xConvect[0].length];
		float[][] tempYCon = new float[yConvect.length][yConvect[0].length];
		/*for (int r = 0; r < xConvect.length; r++) {
			for (int c = 0; c < xConvect[r].length; c++) {
				//find -dx
				float prev = -xConvect[r][c]*dt;
				//left xConvect
				float left;
				if (r + Math.floor(prev) >= 0 && r + Math.floor(prev) < xConvect[r].length)
					left = xConvect[(int)(r + Math.floor(prev))][c];
				//out of bounds.  Just make left number 0
				else
					left = 0;
				
				float right;
				if (r + Math.ceil(prev) >= 0 && r + Math.ceil(prev) < xConvect[r].length)
					right = xConvect[(int)(r + Math.ceil(prev))][c];
				//out of bounds.  Just make left number 0
				else
					right = 0;
				
				//Find the distance the "particle" is from left and right
				float ratio = (float)(prev - Math.floor(prev));
				
				float newCon = ratio*right + (1-ratio) * left;
				tempXCon[r][c] = newCon;
				//System.out.print(newCon);
			}
		}
		//System.out.println();
		
		for (int r = 0; r < yConvect.length; r++) {
			for (int c = 0; c < yConvect[r].length; c++) {
				
			}
		}*/
		//Loop over X's (c is important)
		for (int r = 0; r < xConvect.length; r++) {
			for (int c = 0; c < xConvect[r].length; c++) {
				float left = 0;
				if (c > 0)
					left = xConvect[r][c-1];
				float right = 0;
				if (c < xConvect[r].length-1)
					right = xConvect[r][c+1];
				tempXCon[r][c] = (left+right)/2.0f;
			}
		}
		//Loop over Y's (r is important
		for (int r = 0; r < yConvect.length; r++) {
			for (int c = 0; c < yConvect[r].length; c++) {
				float top = 0;
				if (r > 0)
					top = yConvect[r-1][c];
				float bot = 0;
				if (r < yConvect.length - 1)
					bot = yConvect[r+1][c];
				tempYCon[r][c] = (top+bot)/2.0f + GRAVITY*dt;
			}
		}
		
		xConvect = tempXCon.clone();
		yConvect = tempYCon.clone();
	}
	
	public void run() {
		Thread myThread = Thread.currentThread();
        while (t == myThread) {
        	//Pass in delta time to the update function
        	update(System.currentTimeMillis() - prevTime);
        	prevTime = Calendar.getInstance().getTimeInMillis();
            repaint();
            try {
                Thread.sleep(1000/64);
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
		int c = (int) Math.floor(e.getX()/GRID_UNIT);
		yConvect[38][c] -= 20;	
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
