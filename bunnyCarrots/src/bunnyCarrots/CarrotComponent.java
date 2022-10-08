package bunnyCarrots;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import javax.imageio.*;
 
/*
* Implements the main component for the carrot game.
*/

public class CarrotComponent extends  JComponent  {
	public static final int SIZE = 500; // initial size
	public static final int PIXELS = 50; // square size per image
	public static final int MOVE = 20; // keyboard move
	public static final int GRAVITY = 2; // gravity move
	public static final int CARROTS = 20; // number of carrots
	private ArrayList myPoints; // x,y upper left of each carrot
	
	private int myX1; // upper left of head (white bunny) x
	private int myY1; // upper left of head (white bunny) y
	
	private int score1;  //  white bunny Score
	
	private int myX2; // upper left of tail (Brown bunny) x
	private int myY2; // upper left of tail (Brown bunny) y
	
	private int score2; // Brown bunny Score
	
	private int myDy; // change in y for gravity
	
	private Image carrotImage;
	private Image headImage;  // this is the first bunny
	private Image tailImage;  // this is the second bunny
	

	
	public CarrotComponent( ) {
		setPreferredSize(new  Dimension(SIZE,  SIZE));
		// getScaledInstance( ) gives us re-sized version of the image --
		// speeds up the drawImage( ) if the image is already the right size
		// See paintComponent( )
		headImage = readImage("bunny.jpg");
		headImage = headImage.getScaledInstance(PIXELS,  PIXELS, 
		Image.SCALE_SMOOTH);
		
		
		// add image to the second bunny
		tailImage = readImage("bunny2.jpg");
		tailImage = tailImage.getScaledInstance(PIXELS,  PIXELS, 
		Image.SCALE_SMOOTH);
		
		carrotImage = readImage("carrot.gif");
		carrotImage = carrotImage.getScaledInstance(PIXELS, PIXELS,
		Image.SCALE_SMOOTH);
		myPoints = new ArrayList( );
	}
	
	// Utility -- create a random point within the window
	// leaving PIXELS space at the right and bottom
	private Point randomPoint( ) {
		int width = getWidth();
		int height = getHeight();
		Point p = new Point( (int) (Math.random( ) * (width- PIXELS )),(int) (Math.random( ) * (height- PIXELS)));
		return(p); }
	
	// Reset things for the start of a game
	public void reset( ) {
		myPoints.clear( ); // removes all the points
		for (int i=0; i < CARROTS; i++) {
			myPoints.add( randomPoint( ) );
		}
		
		// white bunny  coordinates
		myX1 = getWidth( ) / 2;
		myY1 = 0;
		
		
		// reset white bunny score
		score1 = 0;
		
		
		// brown bunny coordinates 
		myX2 = (getWidth( ) / 2) - PIXELS;
		myY2 = 0;
		
		
		// reset brown bunny score
		score2 = 0;
		
		
		myDy = 0;
		repaint( );
	}
	
	// Advance things by one tick -- do gravity, check collisions
	
	// gravity adj to white bunny
	public void tick1( ) {
		myDy = myDy + GRAVITY; // increase dy
		myY1 += myDy; // figure new y
		// figure new y
		// check if hit bottom
		if (myY1 + PIXELS >= getHeight( )) {
			// back y up
			myY1 -= myDy;
			// reverse  direction of dy (i.e. bounce), but with 98% efficiency
			myDy = (int) (0.98 * -myDy);
		}
		
		checkCollisions1();

		repaint( );
	}
	
	// gravity adj to brown bunny
	public void tick2( ) {
		myDy = myDy + GRAVITY; // increase dy
		myY2 += myDy; // figure new y
		// figure new y
		// check if hit bottom
		if (myY2 + PIXELS >= getHeight( )) {
			// back y up
			myY2 -= myDy;
			// reverse  direction of dy (i.e. bounce), but with 98% efficiency
			myDy = (int) (0.98 * -myDy);
		}
		checkCollisions2();

		repaint( );
	}
	
	// Check if white bunny hit carrots
	public void checkCollisions1(  ) {
		for (int i=0; i < myPoints.size( ); i++) {
			Point point = (Point) myPoints.get(i);
			// if we overlap a carrot, remove it
			if (Math.abs(point.getX( ) - myX1) <= PIXELS
			&& Math.abs(point.getY( ) - myY1) <= PIXELS) {
				myPoints.remove(i); // removes the ith elem from an ArrayList
				i--; // tricky:
				
				score1 += 5 ;
				// back i up, since we removed the ith element
				repaint( );
			}
		}
		if (myPoints.size( ) == 0) {
			reset( );                            // new game
		}
	}
	
	
	// check if brown bunny hit carrots
	public void checkCollisions2(  ) {
		for (int i=0; i < myPoints.size( ); i++) {
			Point point = (Point) myPoints.get(i);
			// if we overlap a carrot, remove it
			if (Math.abs(point.getX( ) - myX2) <= PIXELS
			&& Math.abs(point.getY( ) - myY2) <= PIXELS) {
				myPoints.remove(i); // removes the ith elem from an ArrayList
				i--; // tricky:
				score2 += 5 ;
				// back i up, since we removed the ith element
				repaint( );
			}
		}
		if (myPoints.size( ) == 0) {
			reset( );                            // new game
		}
	}
	
	
	
	// Process one key click -- up, down, left, right
	public void key1(int code) {
		if (code == KeyEvent.VK_UP) {
			myY1 += -MOVE;
		}
			else if (code == KeyEvent.VK_DOWN) {
			myY1 += MOVE;
		}
			else if (code == KeyEvent.VK_LEFT) {
			myX1 += -MOVE;
		}
			else if (code == KeyEvent.VK_RIGHT) {
			myX1 += MOVE;
		}
		checkCollisions1(  );
		repaint( );
	}
	
	public void key2(int code) {
		if (code == KeyEvent.VK_W) {
			myY2 += -MOVE;
		}
			else if (code == KeyEvent.VK_S) {
			myY2 += MOVE;
		}
			else if (code == KeyEvent.VK_A) {
			myX2 += -MOVE;
		}
			else if (code == KeyEvent.VK_D) {
			myX2 += MOVE;
		}
		checkCollisions2(  );
		repaint( );
	}


	
	// Utility to read in an Image object
	// If image cannot load, prints error output and returns null.
	private Image readImage(String filename) {
		Image image = null;
		try {
			image = ImageIO.read(new  File(filename));
		}
			catch (IOException e) {
			System.out.println("Failed to load image '" + filename + "'");
			e.printStackTrace( );
		}
	return(image);
	}
	
	
	// Draws the bunnies carrots and scores
	public void paintComponent(Graphics g) {
		
		
		g.drawImage(headImage, myX1, myY1, PIXELS, PIXELS, null);
		g.drawImage(tailImage, myX2, myY2, PIXELS, PIXELS, null);
		g.drawString("White Bunny Score",5,15);
		g.drawString("Brown Bunny Score",getWidth()-130,15);
		g.drawString(String.valueOf(score1),30,30);
		g.drawString(String.valueOf(score2),getWidth()-100,30);
		// Draw all the carrots
		for (int i=0; i < myPoints.size( ); i++) {
			Point p = (Point) myPoints.get(i);
			// point.getX( ) returns a double, so we must cast to int
			g.drawImage(carrotImage,
				(int) (p.getX( )), (int) (p.getY( )),
				PIXELS, PIXELS,
				null);
		}
	}
}
