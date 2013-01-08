import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import javax.swing.*;

public class Game implements Runnable {
   private boolean gameIsOver = false;
   SnakeGrid grid = new SnakeGrid();
   Food food;
   Score score = new Score(0, grid);
   private Timer timer;
   private int interval = 10;
   private int pace = 0;
   
   /* Constructs an instance of a new game, performed only once. Sets up the
    * timer that basically runs the game for the duration. In every interval
    * three mehods are called, each one handles a some different functionality
    * of the game, which will be explained later.
    */
   public Game() {
	   timer = new Timer(interval, new ActionListener() {
		   public void actionPerformed(ActionEvent e) {
				grid.playGame();
				maintainGameStatus();
				score.keepScore();
			}});
		timer.start();

		
   }
   /*The method given in the original hw files, here it has mainly been
    * edited to include an optionPane, and then added in specific functionality
    * for resetting this game.
    */
   public void run() {
	  final JFrame frame = new JFrame("Snake");
      frame.setLocation(300, 300);
      frame.setResizable(false);
      frame.setBackground(Color.GRAY);
      frame.add(grid, BorderLayout.CENTER);
      
      final JPanel panel = new JPanel();
      frame.add(panel, BorderLayout.SOUTH);
      final JButton reset = new JButton("Start Over!!!");
      reset.addActionListener(new ActionListener() {
    	  public void actionPerformed(ActionEvent e) {
    		  resetWholeGame();
          }
      });
      
      panel.add(reset);
      
      //Instructions for the Game
      final JButton instructions = new JButton("Click for Instructions");
      instructions.addActionListener(new ActionListener() {
    	  public void actionPerformed(ActionEvent e) {
    		  resetWholeGame();
    		  JOptionPane.showMessageDialog(frame,
        			    "\n Welcome to the game of Snake.\n " +
        			    "You control the snake with the arrow " +
        			    "keys.\n The objective is to get the most amount of " +
        			    "points without dying.\n You die if you hit the " +
        			    "edge of the playing area,\n or if you run into your " +
        			    "own body. You have to eat food \n to grow "+
        			    "longer and gain points.\n" +
        			    "\n (Feature 1) There are 5 " +
        			    "different types of food.\n Only two allow " +
        			    "you to grow. Normal food gives you 1 extra " +
        			    "length \n and is red. Super food gives you 5 extra " +
        			    " length and is orange. \n The other three " +
        			    "types change your speed.\n Yellow is go "+
        			    "normal speed, pink is go slow, and cyan is go fast. " +
        			    " \n \n (Feature 2) Periodically a mongoose (Blue)" +
        			    " will cross the screen,\n if the mongoose " +
        			    "intersects your snake, then your snake loses " +
        			    "\n all of its length from that point on and your " +
        			    "score decreases.\n Your score is always a " +
        			    "reflection of how many lengths \n your snake has. " +
        			    " \n \n Good luck and have fun!");
   
          }
      });
      
      panel.add(instructions);
      
      frame.add(score, BorderLayout.NORTH);
      frame.pack();
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setVisible(true);  
    }
   
   	/* This method is called at each interval, and is mainly in charge of 
   	 * checking whether the player is dead, it is also in charge of handling 
   	 * game speed, for the different types of food. The way speed was 
   	 * implemented was through sleeping the thread based on a pace value
   	 * that is set by which type of food was most recently eaten. This method
   	 * also makes a call to the report method of score, which handles displaying
   	 * the message when the player has lost.
   	 */
    public void maintainGameStatus(){
    	if (gameIsOver){
    		return;
    	}
    	if((grid.snakeHeadType == null)
    			|| grid.snakeHeadType == GridBoxType.SNAKE){
    		gameIsOver = true;
    		timer.stop();
    	} else if(grid.snakeHeadType == GridBoxType.AVERAGE_FOOD){
    		pace = 0;
    	} else if(grid.snakeHeadType == GridBoxType.SLOW_FOOD){
    		pace = -1;
    	} else if(grid.snakeHeadType == GridBoxType.FAST_FOOD){
    		pace = 1;
    	}
    	if (pace == -1){
    		try {Thread.sleep(65);} catch (InterruptedException e) {}
    	} else if (pace == 0) {
    		try {Thread.sleep(40);} catch (InterruptedException e) {}
    	} else if (pace == 1){
        	try {Thread.sleep(15);} catch (InterruptedException e) {}
    	}
    	score.report(gameIsOver);	
    }
    
    
    /*This method resets the game, allowing for a new game of snake to be played
     * You only need to instantiate a grid once, but within the grid many
     * objects are re-instaniated, hence the presence of a grid resetGame method
     * as well.
     */

    public void resetWholeGame() {
    	timer.start();
    	gameIsOver = false;
    	grid.resetGame();
    	score.reset();
    	pace = 0;
    	grid.setFocusable(true);
    	grid.requestFocusInWindow();	
    }
    
   /* The part that gets the game actually running */
   public static void main(String[] args) {
       SwingUtilities.invokeLater(new Game());
   }
   
}