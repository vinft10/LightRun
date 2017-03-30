package dot;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.io.*;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.Timer;


public class Dot extends JFrame implements ActionListener, KeyListener, MouseListener{
	
	private static final long serialVersionUID = 1L;

	public static Dot dot;
	//size of Jframe
	public final int WIDTH = 800, HEIGHT = 800;
	public CheckScore checkScore;
	public Renderer renderer;
	//used to detect collision with player
	public Rectangle playerColli;
	//icon and jlabel for player gif
	ImageIcon logoIcon;
	JLabel logo;
	ImageIcon playerIcon;
	JLabel player;
	ImageIcon enemyIcon;
	JLabel enemyI;
	ImageIcon backGround;
	JLabel backG;
	//limits "press space to start" prompt
	int tries = 0;
	int lightType;
	//mX and mY are mouse coordinates   
	//ticks is used for timing and keeping score
	public int ticks, mX, mY,temp,highScore = 0;
	public int backY = 0;
	CheckScore scoreCheck;
	//mod is the intervals of difficulty in the game 15 max and 3-4 min
	//time is used to keep track of when to decrement mod
	int mod = 3;
	int decr = 75;
	//starts the game and keeps it on a timer
	Timer timer;
	//if started is true the game is running
	//if gameOver is true the game is not running
	public boolean gameOver, started;	
	public ArrayList <Rectangle> enemy;
	public ArrayList <Rectangle> light = new ArrayList<Rectangle>(); 
	public Random rand;
	
	public Dot(){
		JFrame jframe = new JFrame();
		scoreCheck = new CheckScore();
		try {
			scoreCheck.check(ticks);
			highScore = scoreCheck.checkFinal;
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		renderer = new Renderer();
		rand = new Random();
		timer = new Timer(10, this);
		
		jframe.add(renderer);
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.setSize(WIDTH, HEIGHT);
		jframe.setFocusable(true);
		jframe.requestFocus();
		jframe.requestFocusInWindow();
		jframe.setResizable(false);
		jframe.setTitle("LightRun");
		jframe.addKeyListener(this);
		 		
        enemy = new ArrayList<Rectangle>(); 
		//makes cursor invisible
		BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
		Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "blank cursor");
		jframe.getContentPane().setCursor(blankCursor);
		//creates player icon
		URL backImage = null;
		try{
			backImage = new URL("file:///Users/Vinny1/Documents/workspace/Dot/backGround.png");
		}catch (MalformedURLException e) {
	        e.printStackTrace();
	    }
		backGround = new ImageIcon(backImage);
		backG = new JLabel(backGround);
		add(backG);
		URL url = null;
		try{
			 url = new URL("file:///Users/Vinny1/Documents/workspace/Dot/player.gif");
		}catch (MalformedURLException e) {
	        e.printStackTrace();
	    }
		playerIcon = new ImageIcon(url);
		player = new JLabel(playerIcon);
		add(player);
		URL logoUrl = null;
		try{
			 logoUrl = new URL("file:///Users/Vinny1/Documents/workspace/Dot/Logo.gif");
		}catch (MalformedURLException e) {
	        e.printStackTrace();
	    }
		logoIcon = new ImageIcon(logoUrl);
		Image temp = logoIcon.getImage();
		Image temp2 = temp.getScaledInstance(200, 200, Image.SCALE_DEFAULT);
		logoIcon = new ImageIcon(temp2);
		logo = new JLabel(logoIcon);
		add(logo);
 		playerColli = new Rectangle();
		timer.restart();
		timer.start();
		jframe.setVisible(true);
	}
	public void addEnemy(boolean start)
	{
		if(start)
		{
			enemy.add(new Rectangle(WIDTH / 2, -20 , 20, 20));	
		}
		else{
			if((ticks/5) % mod == 0 ){
			enemy.add(new Rectangle( 0 + rand.nextInt(780), 0 - rand.nextInt(500), 20, 20));
			}
			if(ticks/5 == decr && mod != 3){
				mod = mod - 1;
				decr = decr + 50;
			}
		}
	}
	public void lightBurst(){
    	//add a light somewhere above the jframe randomly
    	if(ticks/5 % 100 == 0 && ticks/5 != 0 && light.size() == 0){
    	light.add(new Rectangle( 0 + rand.nextInt(780),-50, 20, 20));
    	lightType = 1 + rand.nextInt(3);
    	}
    	//sets speed of every new light to 15
    	int speed = 15;
    	int i;
    	for(i = 0; i < light.size() ; i++)
    	{
    		Rectangle lights = light.get(i);
    		lights.y += speed;
    	}
    	//if a light falls past the jframe remove it
    	for(i = 0; i < light.size(); i ++)
    	{
    		Rectangle lightTemp = light.get(i);
    		if(lightTemp.y > 800)
    			{
    				light.remove(i);
    		}
    		//if a light hits the player clear the enemies
    		for(Rectangle alight : light)
    		{
    			if(alight.intersects(playerColli))
    			{
    				if(lightType == 1){
        				enemy.clear();
    				}
    				if(lightType == 2){
        				enemy.clear();
    				}
    				if(lightType == 3){
        				enemy.clear();
    				}
    			}
		
    		}
		
    	}
    	
    }
	public void actionPerformed(ActionEvent e) 
	{
		renderer.repaint();
		if(started)
 		{
		ticks++;
 		playerColli.setSize(20,20);
 		playerColli.x = mX;
 		playerColli.y = mY;
 		PointerInfo a = MouseInfo.getPointerInfo();
		Point b = a.getLocation();
		mY = (int) b.getY();
		if(b.getX() <= 0){
			mX = 0;
		}else if(b.getX() >= 800){
			mX = 780;
		}else{
			mX = (int) b.getX();
		}
		
		if(b.getY() <= 0){
			mY = 0;
		}else if(b.getY() >= 800){
			mY = 800;
		}else{
			mY = (int) b.getY();
		}
		
		//enemy speed
		int speed = 8;
		for(int i = 0; i < enemy.size() ; i++)
		{
			Rectangle enemys = enemy.get(i);
			enemys.y += speed;
		}
				
		for(int i = 0; i < enemy.size(); i ++)
		{
			Rectangle enemys = enemy.get(i);
			if(enemys.y > HEIGHT)
			{
				addEnemy(false);
				enemy.remove(i);
			}
			for(Rectangle aenemy : enemy)
			{
				if(aenemy.intersects(playerColli))
				{
					gameOver = true;
					started = false;
						try {
							scoreCheck.check(ticks);
							highScore = scoreCheck.checkFinal;
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
				}
			
			}
		}
		addEnemy(false);
 		lightBurst();
	 		//System.out.println(lightDone + " ticks " + ticks/5);
 		}
 		
	}
	
	
	public void repaint(Graphics g) {
		
		g.setColor(Color.white);
		g.fillRect(0, 0, 800, 800);
		backGround.paintIcon(this, g, 0, backY);
		if(gameOver){
			g.setColor(Color.black);
			g.fillRect(0, 0, 800, 800);
		}
		
		g.setColor(Color.gray);
		g.setFont(new Font("Cooper Black", 1 , 50));
		if(started){	
			if(ticks/5 % 1 == 0 && backY != 0){
			backGround.paintIcon(this, g, 0, backY);
			backY ++;
			}else{
				backGround.paintIcon(this, g, 0, 0);
			}
			g.drawString("Score: " + ticks/5 + " mod " + mod, 0 , 750);
			playerIcon.paintIcon(this, g, playerColli.x - 7, playerColli.y - 5);
		}
		
	
		if(!started && tries == 0){
			g.setColor(Color.white);
			g.setFont(new Font("Cooper Black", 1 , 20));
			logoIcon.paintIcon(this, g, 315, 100);
			g.drawString("Space to Start!", 325 , 250);
			g.drawString("High", 0 , 730);
			g.drawString("Score: " + highScore, 0 , 750);
		}
		
				
		if(gameOver)
		{	
			g.drawString("Game Over!", 250 , 300);
			if(highScore > ticks/5){
			g.drawString("High Score: " + highScore , 275 , 750);
			g.drawString("Score: " + ticks/5, 0 , 750);
			}else{
			g.drawString("NEW", 0 , 650);
			g.drawString("HIGH", 0 , 700);
			g.drawString("SCORE: " + ticks/5, 0 , 750);
			}
			g.drawString("Space to try again.", 170 , 350);
		}
		
		g.setColor(Color.black);
		g.drawLine(10, 10,10 , 10);
		
		for(Rectangle lights : light){
			
			paintLight( g, lights);
			
		}
		for(Rectangle enemys : enemy){
			
			paintEnemy( g, enemys);
			
		}
	}
	public void start(int c){
		
		if(c == KeyEvent.VK_SPACE){
			if(!started){
			enemy.clear();
			started = true;
			gameOver = false;
			mod = 9;
			decr = 75;
			ticks = 0;
			addEnemy(true);
			tries = 1;
			}
		}
		
	}
	public void paintEnemy(Graphics g, Rectangle enemy)
	{
		g.setColor(Color.black);
		g.fillRect(enemy.x, enemy.y, enemy.width, enemy.height);	
	}
	public void paintLight(Graphics g,Rectangle light){
		g.setColor(Color.yellow);
		g.fillRect(light.x, light.y, light.width,light.height);
	}
	public static void main(String[] args) throws IOException {	
		dot = new Dot();
		}
	
	@Override
	public void keyTyped(KeyEvent e) {
		//not used
}
	@Override
	public void keyPressed(KeyEvent e) {
		int c = e.getKeyCode();
		start(c);
		}
	@Override
	public void keyReleased(KeyEvent e) {
		
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
		
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	
		
}
