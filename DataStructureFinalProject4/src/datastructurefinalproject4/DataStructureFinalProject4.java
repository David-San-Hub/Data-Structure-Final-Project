package datastructurefinalproject4;

//this is where we all imports stores
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList; // <-----------THIS IS WHY WE MADE THIS PROGRAM JUST FOR THIS ARRAYLIST AND WHAT DOES IT DO
import java.util.Random;

import javax.swing.JFrame; //import JFrame, this is to create window on screen
import javax.swing.Timer; //import Timer, enables threads to schedule tasks for later execution

//THIS IS DATASTRUCTUREMAIN CLASS
public class DataStructureFinalProject4 implements ActionListener, MouseListener, KeyListener {
    
    public static DataStructureFinalProject4 FlappyBird; //create static instance 
    
    public final int WIDTH = 800, HEIGHT = 800; //this is to create the window size
    
    public Renderer renderer; //this is instance for renderer
    
    public Rectangle bird; //create object for bird
    
    public ArrayList<Rectangle> columns; //ArrayList contains rectangles for pipes
    
    public int ticks, yMotion, score; //to create motion of the bird
    
    public boolean gameOver, started; //to make game over
    
    public Random rand; //create random objects 
    
    //ADD CONSTRUCTOR TO CLASS
    public DataStructureFinalProject4() { 
    
        JFrame jframe = new JFrame(); //create instance Jframe
        Timer timer = new Timer(20, this); //call timer
        
        renderer = new Renderer(); //make it not NULL when run
        rand = new Random();
        
        jframe.add(renderer); //call renderer
        jframe.setTitle("Flappy Bird"); //set title for the window
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //this is for exiting the window (the X mark on top right)
        jframe.setSize(WIDTH, HEIGHT); //this calls the WIDTH and HEIGHT of the window and make the size fixed
        jframe.addMouseListener(this); //this calls mouse Listener
        jframe.addKeyListener(this); //this calls key Listener
        jframe.setResizable(false); //to disable sizing the window
        jframe.setVisible(true); //so you can see it, self explanatory 
        
        bird = new Rectangle(WIDTH / 2 - 10, HEIGHT / 2 - 10, 20, 20); //this makes the bird object
        columns = new ArrayList<Rectangle>(); //ArrayList YAY :3

	addColumn(true);
	addColumn(true);
	addColumn(true);
	addColumn(true);

	timer.start(); //to schedule a thread to be executed at certain time in future
    }
    
    //THIS METHOD IS FOR THE HITBOX OF THE GREEN COLUMNS/PIPES
    public void addColumn(boolean start) { 
        
	int space = 300; //creates space between pipes
	int width = 100; //width of the pipes
	int height = 50 + rand.nextInt(300); //height of the pipes

	if (start) {
            
            columns.add(new Rectangle(WIDTH + width + columns.size() * 300, HEIGHT - height - 120, width, height)); //hitbox size for pipes 1
            columns.add(new Rectangle(WIDTH + width + (columns.size() - 1) * 300, 0, width, HEIGHT - height - space)); //hitbox size for pipes 2
            
	}else {
            
            columns.add(new Rectangle(columns.get(columns.size() - 1).x + 600, HEIGHT - height - 120, width, height)); //hitbox size for pipes 3
            columns.add(new Rectangle(columns.get(columns.size() - 1).x, 0, width, HEIGHT - height - space)); //hitbox size for pipes 4
	}
    }
    
    //THIS METHOD IS FOR GREEN PIPES/COLUMNS
    public void paintColumn(Graphics g, Rectangle column) {
        
	g.setColor(Color.green.darker()); //color for green pipes
	g.fillRect(column.x, column.y, column.width, column.height); //color size for the pipes
        
    }

    //THIS IS FOR JUMP MECHANIC
    public void jump() {
        
	if (gameOver) { //if game over, start the game
            
            bird = new Rectangle(WIDTH / 2 - 10, HEIGHT / 2 - 10, 20, 20);
            columns.clear();
            yMotion = 0;
            score = 0;

            addColumn(true);
            addColumn(true);
            addColumn(true);
            addColumn(true);

            gameOver = false;
	}

	if (!started) { //not started then begin game
            
            started = true;
            
	}else if (!gameOver) {  //game not over when start and restarted
            
            if (yMotion > 0) {
                
                yMotion = 0;
            }

            yMotion -= 10;
	}
    }   
    
    //METHOD THAT IMPLEMENTS ACTIONLISTENER 
    @Override 
    public void actionPerformed(ActionEvent e) { //this method is for the bird's motion, movement, and game over + the pipes moving from right to left
        
        int speed = 10;
        
        ticks++;
        
        if (started) {
            int i = 0;
            while (i < columns.size()) {
                Rectangle column = columns.get(i);
                column.x -= speed;

                if (column.x + column.width < 0) {
                    columns.remove(column);

                    if (column.y == 0) {
                        addColumn(false);
                    }
                } else {
                    i++;
                }
            }

            if (ticks % 2 == 0 && yMotion < 15) {
                yMotion += 2;
            }
            bird.y += yMotion;
        
            for (Rectangle column : columns) { //check collision for loop
            
                if (column.y == 0 && bird.x + bird.width / 2 > column.x + column.width / 2 - 10 && bird.x + bird.width / 2 < column.x + column.width / 2 + 10) { //this is for score, when bird go further the score set higher
					
                    score++;
                }

                if (column.intersects(bird)) { //this is for bird don't no clip through ground when game over
                
                    gameOver = true;

                    if (bird.x <= column.x) { //bird does not go through top of pipes/column when game over
                    
                        bird.x = column.x - bird.width;
                    
                    }else {
                    
                        if (column.y != 0) { // make top column intersecting to bird
                        
                            bird.y = column.y - bird.height;
                        
                        } else if (bird.y < column.height) {
                        
                            bird.y = column.height;
                        }
                    }
                }
            }
            
            if (bird.y > HEIGHT - 120 || bird.y < 0) { //game over when bird hit
                
		gameOver = true;
            }

            if (bird.y + yMotion >= HEIGHT - 120) { //when game over, bird fall with physics
                
                bird.y = HEIGHT - 120 - bird.height;
		gameOver = true;
            }
        }            
        renderer.repaint(); //performs a request to erase and perform redraw of the component after a small delay in time
    }

    //THIS IS FOR COLORS OF THE OBJECTS
    public void repaint(Graphics g) { 
        
        g.setColor(Color.cyan); //color for background
        g.fillRect(0, 0, WIDTH, HEIGHT); //fill cyan for background
        
        g.setColor(Color.orange); //color for ground
	g.fillRect(0, HEIGHT - 120, WIDTH, 120); //fill bottom window 

	g.setColor(Color.green); //color for grass
	g.fillRect(0, HEIGHT - 120, WIDTH, 20); //fill the grass color above ground

        g.setColor(Color.red); //color for bird
        g.fillRect(bird.x, bird.y, bird.width, bird.height); //color size for the bird
        
	for (Rectangle column : columns) { //create iterator for the columns/pipes
            
            paintColumn(g, column);
	}
        
        g.setColor(Color.white); //this is font color for words on screen
	g.setFont(new Font("Arial", 1, 100)); //font size for words on screen

	if (!started) { //this is to create the start font
            
            g.drawString("Click to start!", 75, HEIGHT / 2 - 50);
	}

	if (gameOver) { //this is to create game over font
            
            g.drawString("Game Over!", 100, HEIGHT / 2 - 50);
	}

	if (!gameOver && started) { //shows score when die and remove before start
            
            g.drawString(String.valueOf(score), WIDTH / 2 - 25, 100);
	}
    }

    //TO DEFINE MAIN METHOD
    public static void main(String[] args) { 
        
        FlappyBird = new DataStructureFinalProject4();
        
      // returns the current value of the system timer, in nanoseconds
      System.out.print("time in nanoseconds = ");
      System.out.println(System.nanoTime());

      // returns the current value of the system timer, in milliseconds
      System.out.print("time in milliseconds = ");
      System.out.println(System.currentTimeMillis());
    }

    //ALL OF THESE ARE METHODS FOR KEYBOARDS AND MOUSE
    @Override
    public void mouseClicked(MouseEvent e) {
        
        jump();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        
    }

    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        
    }

    @Override
    public void keyReleased(KeyEvent e) {
        
        if (e.getKeyCode() == KeyEvent.VK_SPACE) { //press space to jump
            
            jump();
	}
    }
}