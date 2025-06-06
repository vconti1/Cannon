import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;

import javax.swing.JPanel;


public class CannonSimulator extends JPanel implements ActionListener, KeyListener {

	final int boardWidth = 1080;
	final int boardHeight = 720;
	final int cannonWidth = 150;
	final int cannonHeight = 40;
	final int pivotX = 0;
	final int pivotY = (boardHeight - cannonHeight);
	
	//Hardcoded for now, but the user should be able to change angle of the cannon
	double angle = Math.toRadians(-30); 
	
	CannonSimulator(){
		setPreferredSize(new Dimension(boardWidth,boardHeight));
		setBackground(Color.black);
		setFocusable(true);
		addKeyListener(this);
	}
	
	//transform with AffineTransformations
	public void draw(Graphics g) {
		AffineTransform tx = new AffineTransform();
		Graphics2D cannon = (Graphics2D) g;
		
		
		tx.rotate(angle, pivotX, pivotY);
		cannon.setTransform(tx);
		cannon.setPaint(Color.white);
		cannon.fillRect(0, boardHeight - cannonHeight, cannonWidth, cannonHeight);
		
		
	}
	
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	
	
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

}
