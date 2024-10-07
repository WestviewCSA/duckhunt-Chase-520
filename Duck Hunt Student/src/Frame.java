import java.awt.Color;
import java.awt.AlphaComposite;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import java.util.ArrayList;
import java.util.List;

import java.math.*;
public class Frame extends JPanel implements ActionListener, MouseListener, KeyListener {
	//creating objects
	Font newFont = new Font("Serif", Font.BOLD, 40);
	Duck duck = new Duck();
	Background bg = new Background();
	Enemy enemy = new Enemy();
	Badge badge1 = new Badge();
	attorney cbt = new attorney();
	Dog zx = new Dog();
	//GameMusic music = new GameMusic("/Duck Hunt Student/src/audio/Title.wav",true);
	
	//score variables
	int roundTimer;
	int score;
	long time;
	int num_of_duck;
	int wait_black;
	int final_animation;
	
	//duck properties
	int duck_x = 100;
	int duck_y = 100;
	
	//enemy properties;
	int enemy_x = 0;
	int enemy_y = 0;
	
	//
	int badge_x;
	int badge_y;
	int badge_interval;
	
	//bg variables
	float trans;
	
	// booleans
	boolean start;
	boolean end;
	boolean InAnimation;
	boolean InTransition;
	boolean shaking;
	//add a method
	/*
	 * init an variables, objects etc for the start of the game
	 */
	public void init() {
		//timer & score
		roundTimer = 1;
		score = 0;
		time = 0;
		trans=0.5f;
		wait_black = 0;
		num_of_duck = (int)Math.random()*10;
		final_animation = 0;
		
		//duck setups
		duck.setScale(0.3, 0.3);
		duck.setWidth(400);
		duck.setHeight(300);
		duck.setVx(0);
		duck.setVy(10);
		//duck.setXY(duck_x, duck_y);
		
		// cbt setup
		cbt.setXY(-1500, 100);
		
		// zx setup
		zx.setScale(-0.4, 0.4);
		zx.setXY(800, 650);
		zx.toggleHitBox();
		
		//enemy setups
		enemy.setXY(50, 100);
		enemy.toggleHitBox();

		//background setup
		bg.setScale(1.1, 1.1);
		//bg.changePicture("/imgs/doorBG.jpg");
		
		// 
		StdAudio.stoploop(false);
		StdAudio.loopInBackground("/audio/Title.wav");
		



		//booleans
		start = false;
		end = false;
		InAnimation =false;
		InTransition = false;
		shaking = false;
	}
	/*
	 * resetting for multiple rounds etc
	 */
	
	public void reset() {
		init();
	}

	public void transition() {

		if(trans>=0.95f) {
			InTransition = false;
			trans = 0.95f;
		}
		else{
			InTransition = true;
			trans += 0.05f;
		}
	}
	
	public void win_animation() {
		wait_black += 1;
		shaking = !shaking;
		if (cbt.getX()<-500){
			cbt.setVx(70);
		}
		else {
			cbt.setVx(0);
			cbt.changePicture("/imgs/cbt1-异议-慢动作.gif");
			if(wait_black >=20) {
				final_animation += 1;
				trans = 0.0f;
				//bg.setTrans(0.0f);
				bg.changePicture("/imgs/pw_scrolling_prosecution.gif");
				bg.setScale(10, 20);
				enemy.shaking(shaking);
			if(final_animation>=60) {
				end =true;
			}
			}
			
		}
		
	}
	
	
	
	public void paint(Graphics g) {
		super.paintComponent(g);
		//System.out.println(t.toString());
		if(start && InTransition==false){
			// if the game end
			
			if (end) {
				reset();
			}
			
			time += 16; // update time
			if(time%16==0) {
				
				if(time%960==0) { //has been 1 second
					roundTimer -= 1;
				}
				if(roundTimer <= 0 && InTransition) {
					//what do you do after one complete round
					transition();
				}
				if(roundTimer<-1 && score>=0) {
					InAnimation = true;
					t.stop();
					win_animation();
				}
			}
			
			// change zx img
			if(score<=10 && roundTimer<=10) {
				zx.changePicture("/imgs/zx2-严肃-1.gif");
			}
			else if(score>=30 && roundTimer <=5) {
				zx.changePicture("/imgs/zx2-前倾-1.gif");
			}
			else if(score>=20) {
				zx.changePicture("/imgs/zx2-通常-1.gif");
			}
			
			//change enemy img
			if(this.score <= 10) {
				enemy.changePicture("/imgs/yj1-抱胸-1.gif");
			}
			if(this.score>=20) {
				enemy.changePicture("/imgs/red_loser.gif");
			}
			else if(this.score==10) {
				enemy.changePicture("/imgs/yj1-崩坏.gif");
			}
			
			
			
			
			
			//layer your objects as you want them to layer
			if(end!=true && InAnimation!=true) {
				trans = 0.0f;
				bg.setScale(1.5, 1.5);
				bg.changePicture("/imgs/court-new.jpg");
				bg.setTrans(trans);
				bg.paint(g);
				enemy.paint(g);
				cbt.paint(g);
				zx.paint(g);
				duck.paint(g);
			}
			else if(InAnimation == true) {
				bg.setTrans(trans);
				bg.paint(g);
				enemy.paint(g);
				cbt.paint(g);
			}
			
			
			//update position
			duck_x += 1;
			duck_y += 1;
			
			// right score
			int start_score_x = 1520;
			int start_score_y = 750;
			int score_width = 200;
			int score_height = 100;
			int score_edge = 10;
			g.setColor(new Color(141, 8, 1));
			g.fillRect(start_score_x, start_score_y, score_width, score_height);
			g.setColor(new Color(255, 255, 255));
			g.fillRect(start_score_x+score_edge, start_score_y+score_edge, score_width-2*score_edge, score_height-2*score_edge);
			
			String score_text = "Score";
			g.setFont(newFont);
			g.setColor(new Color(141, 8, 1));
			g.drawString(score_text, start_score_x+4*score_edge+10, start_score_y+4*score_edge);
			g.drawString(""+score, start_score_x+85, start_score_y+75);
			
			//center life box
			int life_x = 600;
			int life_y = 750;
			int life_width = 700;
			int life_height = 200;
			int life_edge = 20;
			g.setColor(new Color(3, 63, 99));
			g.fillRect(life_x, life_y, life_width, life_height);
			g.setColor(new Color(255, 255, 255));
			g.fillRect(life_x+life_edge, life_y+life_edge, life_width-2*life_edge, life_height-2*life_edge);
			
			// badge=life display
			int badge_y = 800;
			int badge_x = 700;
			badge1.toggleHitBox();
			badge1.setScale(0.2, 0.2);
			badge1.setXY(badge_x, badge_y);
			badge1.paint(g);
			
			
			//Timer display
			g.drawString(""+this.roundTimer,500, 500);
			
			
			//logic for resetting dog position
			if(duck.getY()>=400) {
				//int ran = (int)(Math.random()*400);
				//System.out.println("ran: "+ ran);
				duck.setVy(-1*duck.getVy());
	
			}
			else if(duck.getY()<=0) {
				duck.setVy(-1*duck.getVy());
			}
		}
		else{
			//music.run();
			bg.setTrans(trans);
			bg.paint(g);
			if(start) {
				time += 16;
				if(time%16 ==0) {
					transition();
				}

				
			}
			
			g.setColor(new Color(255, 255, 255));
			Font titleFont = new Font("Serif", Font.BOLD, 80);
			g.setFont(titleFont);
			g.drawString("Press space bar to start", 450, 450);
			
			

			

		}
		
	}
	
	public static void main(String[] arg) {
		Frame f = new Frame();

	}
	
	public Frame() {
		JFrame f = new JFrame("Duck Hunt");
		f.setSize(new Dimension(1920, 1080));
		f.setBackground(Color.blue);
		f.add(this);
		f.setResizable(false);
		f.setLayout(new GridLayout(1,2));
		f.addMouseListener(this);
		f.addKeyListener(this);
		
		init(); //call init to give properties to the objects

		t.start();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}
	
	Timer t = new Timer(16, this);
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println("mouse entered: "+ arg0.getPoint());
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
		Rectangle rMouse = new Rectangle(arg0.getX()-15, arg0.getY()-15,30, 30); //guess and check mouse display
		
		Rectangle rMain = new Rectangle(
				duck.getX(),
				duck.getY(),
				duck.getWidth(),
				duck.getHeight()
				);
		
		if(rMouse.intersects(rMain)) {
			System.out.println("shooting");
			
			StdAudio.playInBackground("/audio/hit.wav");
			this.score += 1;
		}
		
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		repaint();
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println(arg0.getKeyCode());
		if(arg0.getKeyCode()==32){
			
			StdAudio.loopInBackground("/audio/Examiniation Moderate 2001(Av955216805,P5).wav");
			start = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
