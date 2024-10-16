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
	Duck objection = new Duck();
	Background bg = new Background();
	Enemy enemy = new Enemy();
	Badge badge1 = new Badge();
	Badge badge2 = new Badge();
	attorney cbt = new attorney();
	Dog zx = new Dog();
	//GameMusic music = new GameMusic("/Duck Hunt Student/src/audio/Title.wav",true);
	
	//score variables
	int roundTimer;
	int score;
	long time;
	int duck_speed;
	int wait_black;
	int final_animation;
	int dog_speed;
	int final_hit;
	
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
	boolean catch_duck;
	boolean go_to_score;
	//add a method
	/*
	 * init an variables, objects etc for the start of the game
	 */
	public void init() {
		//timer & score
		roundTimer = 30;
		score = 0;
		time = 0;
		trans=0.5f;
		wait_black = 0;
		duck_speed = (int)(Math.random()*30);
		final_animation = 0;
		dog_speed = 25;
		final_hit = 0;
		
		//duck setups
		duck.setScale(0.3, 0.3);
		duck.setWidth(150);
		duck.setHeight(100);
		duck.setVx(duck_speed);
		duck.setVy(duck_speed);
		
		objection.setScale(1.3, 1.3);
		objection.changePicture("/imgs/objection_word.gif");
		objection.toggleHitBox();
		//duck.setXY(duck_x, duck_y);
		
		// cbt setup
		cbt.setXY(-1500, 0);
		cbt.toggleHitBox();
		
		// zx setup
		zx.setScale(0.4, 0.4);
		zx.setXY(800, 650);
		zx.setVx(dog_speed);
		zx.setVy(0);
//		zx.setWidth(500);
//		zx.setHeight(300);
		zx.toggleHitBox();
		
		//enemy setups
		enemy.setXY(50, 100);
		enemy.toggleHitBox();

		//background setup
		bg.setScale(1.1, 1.1);
		//bg.changePicture("/imgs/doorBG.jpg");
		
		// 
		StdAudio.stopInBackground();
		StdAudio.playInBackground("/audio/Title.wav");
		//StdAudio.loopInBackground("/audio/Title.wav");
		
		// badges
		badge1.toggleHitBox();



		//booleans
		start = false;
		end = false;
		InAnimation =false;
		InTransition = true;
		shaking = false;
		catch_duck = false;
		go_to_score = false;
	}
	/*
	 * resetting for multiple rounds etc
	 */
	
	public void reset() {
		init();
		cbt.toggleHitBox();
		enemy.setVx(0);
		enemy.setVy(0);
		enemy.toggleHitBox();
		zx.toggleHitBox();
		badge1.toggleHitBox();
	}
	
	public void reset_duck(Duck myduck) {
		go_to_score = false;
		catch_duck = false;
		int ran = (int)(Math.random()*(400));
		int newx = (int)(Math.random()*(1400));
		int newy = ran;
		myduck.setXY(newx, newy);
		
		int speed = (int)(Math.random()*15);
		System.out.println(speed);
		myduck.setVx(15+speed);
		myduck.setVy(12+speed);
	}

	public void duck_score(Duck myduck,int score_x, int score_y) {
		int deltax = score_x - myduck.getX();
		int deltay = score_y - myduck.getY();
		myduck.setVx(deltax/60*2); 
		myduck.setVy(deltay/60*2);
		double descale = (0.3)/60*3;
		myduck.setScale(myduck.getScale()-descale, myduck.getScale()-descale);
		if(!go_to_score) {
			if (myduck.getY()>=400) {
				myduck.setVx(0);
				myduck.setVy(0);
				reset_duck(duck);
				go_to_score = true;
				
			}
		}
		
	}
	
	public void transition(boolean reverse) {
		if(!reverse) {
			if(trans>=0.985f) {
				InTransition = false;
				trans = 0.985f;
			}
			else{
				InTransition = true;
				trans += 0.005f;
			}
		}
		else {
			if(trans<=0.005f) {
				InTransition = false;
				trans = 0.005f;
			}
			else{
				InTransition = true;
				trans -= 0.005f;
			}
		}
	}
	
	public void win_animation() {
		wait_black += 1;
		shaking = !shaking;
		if (wait_black==1) {
			StdAudio.stopInBackground();
			StdAudio.playInBackground("/audio/Phoenix Wright Objection! 2001(Av955216805,P7).wav");
		}
		if (cbt.getX()<-500){
			cbt.setScale(1.0, 1.0);
			objection.setXY(-1000, -1000);
			cbt.changePicture("/imgs/cbt1-集中.png");
			cbt.setVx(70);
		}
		else {
			cbt.setVx(0);
			//bg.setTrans(0.0f);
			bg.changePicture("/imgs/pw_scrolling_prosecution.gif");
			bg.setScale(10, 20);
			if(wait_black >=(int)60*1.5) {
				if (wait_black==(int)60*1.5) {
					StdAudio.playInBackground("/audio/phoenix-objection.wav");
				}
				cbt.setScale(1.5, 1.5);
				cbt.setXY(-500, 50);
				cbt.changePicture("/imgs/cbt1-异议.png");
				cbt.toggleHitBox();
				objection.setXY(600, 200);
				enemy.setVx(2);
				enemy.changePicture("/imgs/yj1-崩坏.gif");
				final_animation += 1;
				trans = 0.0f;		
				enemy.shaking(shaking);
				
			if(final_animation>=60*4) {
				end =true;
				StdAudio.stopInBackground();

			}
			}
			
		}
		
	}
	
	
	
	public void paint(Graphics g) {
		super.paintComponent(g);
		//System.out.println(t.toString());
		
		
		if(start && InTransition==false){
			if(time==0) {
				trans = 0.0f;
				StdAudio.stopInBackground();
				StdAudio.playInBackground("/audio/Examiniation Moderate 2001(Av955216805,P5).wav");
			}
			time += 16; // update time
			if(time%16==0) {
				
				if(time%960==0) { //has been 1 second
					roundTimer -= 1;
				}
				if(roundTimer<=0 && score>=10) {
					InAnimation = true;
					//t.stop();
					win_animation();
				}
				else if(roundTimer<=0&& score<10) {
					StdAudio.stopInBackground();
					StdAudio.playInBackground("/audio/fails.wav");
					//transition(false);
					end = true;
					if (!InTransition) {
						System.out.println("set end to true");
						end = true;

					}
				}
			}
			
			// change zx img
//			if(score<=10 && roundTimer<=10) {
//				zx.changePicture("/imgs/zx2-严肃-1.gif");
//			}
//			else if(score>=30 && roundTimer <=5) {
//				zx.changePicture("/imgs/zx2-前倾-1.gif");
//			}
//			else if(score>=20) {
//				zx.changePicture("/imgs/zx2-通常-1.gif");
//			}
			
			//change enemy img
			if(InAnimation!=true) {
				if(this.score>=18) {
					enemy.changePicture("/imgs/red_loser.gif");
				}
				else if(this.score==14) {
					enemy.changePicture("/imgs/yj1-扶桌-拍桌.gif");
				}
				else if(this.score>10) {
					enemy.changePicture("/imgs/yj1-扶桌.png");
				}
				else if(this.score==10) {
					enemy.changePicture("/imgs/yj1-崩坏.gif");
				}
				else if(this.score <= 10) {
					enemy.changePicture("/imgs/yj1-抱胸-1.gif");
				}
			}
			
			
			
			
			
			
			
			//layer your objects as you want them to layer
			if(end!=true && InAnimation!=true) {
				bg.setScale(1.5, 1.5);
				bg.changePicture("/imgs/court-new.jpg");
				bg.setTrans(trans);
				bg.paint(g);
				enemy.paint(g);
				//cbt.paint(g);
				//zx.paint(g);
				duck.paint(g);
			}
			else if(InAnimation == true) {
				bg.setTrans(trans);
				bg.paint(g);
				enemy.paint(g);
				cbt.paint(g);
				objection.paint(g);
			}
			
			
			//update position
			duck_x += 1;
			duck_y += 1;
			
			
			
//			//center life box
//			int life_x = 600;
//			int life_y = 750;
//			int life_width = 700;
			int life_height = 200;
			int life_edge = 20;
//			g.setColor(new Color(3, 63, 99));
//			g.fillRect(life_x, life_y, life_width, life_height);
//			g.setColor(new Color(255, 255, 255));
//			g.fillRect(life_x+life_edge, life_y+life_edge, life_width-2*life_edge, life_height-2*life_edge);
//			
//			// badge=life display
//			int badge_y = 800;
//			int badge_x = 700;
//			
//			badge1.setScale(0.2, 0.2);
//			badge1.setXY(badge_x, badge_y);
//			badge1.paint(g);
			
			
			
			if(!InAnimation) {
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
				
				
				//Time frame
				int timebar_x = 200;
				int timebar_y = 750;
				int time_width = 300;
				int time_height = 200;
				int time_edge = 20;
				g.setColor(new Color(141, 8, 1));
				g.fillRect(timebar_x, timebar_y, time_width, time_height);
				g.setColor(new Color(255, 255, 255));
				g.fillRect(timebar_x+life_edge, timebar_y+time_edge, time_width-2*time_edge, life_height-2*time_edge);
				
				//Timer display
				g.setColor(new Color(141, 8, 1));
				g.drawString("Your time: ",timebar_x+time_edge+40, timebar_y+time_edge+50);
				g.drawString(""+this.roundTimer,timebar_x+time_edge+120, timebar_y+time_edge+120);
				
			}
			
			
			//paint the dog
			if(end!=true && InAnimation!=true) {
				zx.paint(g);
			}
			
			//logic for resetting duck position
			if(duck.getY()>=400) {
				//int ran = (int)(Math.random()*400);
				//System.out.println("ran: "+ ran);
				duck.setVy(-1*duck.getVy());
	
			}
			else if(duck.getY()<=0) {
				duck.setVy(-1*duck.getVy());
			}
			
			if(duck.getX()<=0) {
				duck.setVx(-1*duck.getVx());
			}
			else if(duck.getX()>=1550) {
				duck.setVx(-1*duck.getVx());
			}
			
			//dog moving
			//zx.setVx(dog_speed);
			// bouncing
//			System.out.println("zx location:"+zx.getX()+" "+zx.getY());
//			System.out.println("current speed: "+ zx.getVx());
			if(zx.getX()>1700) {
				zx.setVx(-1*zx.getVx());
			}
			else if(zx.getX()<=-400) {
				zx.setVx(-1*zx.getVx());
			}
			
			// catching
			Rectangle rectdog = new Rectangle(zx.getX()+200,zx.getY()+200,zx.getWidth(),zx.getHeight());
			Rectangle rectduck = new Rectangle(duck.getX(),duck.getY(),duck.getWidth(),duck.getHeight());
			
			if (rectdog.intersects(rectduck)) {
				zx.setVx(0);
				zx.setVy(0);
				zx.changePicture("/imgs/zx1-元气-点头.gif");
				catch_duck =true;
			}
			
			//reset dog
			if(catch_duck) {
				zx.setVy(10);
				//duck_score(duck,start_score_x+85,start_score_y+75);
				if(zx.getY()>=600) {
					zx.setVy(0);
					zx.setVx(10);
					zx.changePicture("/imgs/zx1-元气.png");
					catch_duck = false;
					reset_duck(duck);
				}
			}
			
			//reset duck
			if(catch_duck) {
				duck.setScale(0.3, 0.3);
				duck.changePicture("/imgs/objection_word.gif");
			}
			else {
				duck.setScale(0.5, 0.5);
				duck.changePicture("/imgs/whiteObjection.png");
			}
			
			
			//dubug area:
			System.out.println("\n");
			System.out.println("You are in the painting loop");
			System.out.println("duck x,y: "+ duck.getX()+" "+duck.getY());
			System.out.println("duck vx, vy: "+ duck.getVx()+ " "+ duck.getVy());
			System.out.println("InTransition: "+ InTransition);
			System.out.println("Trans: "+ trans);
			
			
			if (end) {
				System.out.println("Restart the game");
				reset();
			}
		}
		else{
			//music.run();
			bg.changePicture("/imgs/Title background.jpg");
			bg.setTrans(trans);
			bg.paint(g);
			if(start) {
				time += 16;
				if(time%16 ==0) {
					transition(false);
				}
			}
			
			g.setColor(new Color(255, 255, 255));
			Font titleFont = new Font("Serif", Font.BOLD, 80);
			g.setFont(titleFont);
			g.drawString("Press space bar to start", 450, 450);
			
			time=0;
			//debug
			System.out.println("\n");
			System.out.println("You have't start the game");
			System.out.println("trans: "+trans);
			System.out.println("time: "+time);
			System.out.println("start: "+start);
			System.out.println("end: "+end);
			System.out.println("InTransition: "+ InTransition);

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
		System.out.println("shooting");
		StdAudio.playInBackground("/audio/hit.wav");
		Rectangle rMouse = new Rectangle(arg0.getX()-15, arg0.getY()-15,30, 30); //guess and check mouse display
		
		Rectangle rMain = new Rectangle(
				duck.getX(),
				duck.getY(),
				duck.getWidth(),
				duck.getHeight()
				);
		
		if(rMouse.intersects(rMain)) {
			duck.setVx(0);
			duck.setVy(0);
			if(!catch_duck && (duck.getVx()<=5&&duck.getVy()<=5)) {
				if(zx.getVy()==0) {
					this.score += 1;
				}
				System.out.println("You are in socre +1");
				zx.setVx((duck.getX()-zx.getX()-200)/60*3);
				zx.setVy(-1*Math.abs(duck.getY()-zx.getY()-200)/60*3);
			}
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
		if(arg0.getKeyCode()==32&&InAnimation!=true){		
			//StdAudio.stoploop(false);
			StdAudio.stopInBackground();
			StdAudio.playInBackground("/audio/ends.wav");
			start = true;
		}
		else if (arg0.getKeyCode()==32&& InAnimation==true) {
			final_hit+= 1;
			enemy.changePicture("/imgs/yj1-崩坏.gif");
		}
		if(arg0.getKeyCode()==81) {
			reset();
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
