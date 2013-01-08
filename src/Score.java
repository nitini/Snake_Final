import java.awt.*;

import javax.swing.JPanel;

/* A Jpanel to handle displaying scoring and the message when a player loses. */
@SuppressWarnings("serial")
public class Score extends JPanel {
	private static final Font FONT_LARGE = new Font("Arial", Font.BOLD, 25); 
	private static final Font FONT_NORM = new Font("Arial", Font.BOLD, 15);
	private int scoreBoxWidth = SnakeGrid.WIDTH * SnakeGrid.GRID_BOX_L;
	private int scoreBoxHeight = SnakeGrid.HEIGHT*SnakeGrid.GRID_BOX_L*2;
	private boolean gameOver;
	private int score;
	private SnakeGrid grid;
	
	/* Constructs a score, which is necessary to provide access to the grid */
	public Score (int score, SnakeGrid grid){
		this.score = score;
		this.grid = grid;
	}
	
	/* This is where the threading of the snake length comes into play, having
	 * access to the grid gives the score a way to find out the length of the
	 * snake. For every length of snake that is there, a player gets 10 points,
	 * so the player starts off with 10 points, or it could be set up so that
	 * the player always has 10 less than his length, meaning the head does
	 * not count.
	 */
	public void keepScore(){
		this.score = grid.getSnakeLength() * 10;
	}
	
	/* If the game is over, then the End message needs to be displayed, this 
	 * method is called every interval, in order to continually update the
	 * gameOver boolean
	 */
	public void report(boolean gameOver){
		this.gameOver = gameOver;
		repaint();
	}
	
	/* If the gameOver boolean is true, then in large font the end message is
	 * displayed, if not the normal score display is updated with the current
	 * score.
	 */
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		if (gameOver){
			g.setFont(FONT_LARGE);
			String endMessage = "YOU LOSE!!!!!";
			g.drawString(endMessage, this.getWidth() / 2 - 
					(g.getFontMetrics().stringWidth(endMessage) / 2),
					scoreBoxHeight - scoreBoxHeight/3);
		}
		g.setFont(FONT_NORM);
		String scoreDisplay = " Your Score: "+ score;
		g.drawString(scoreDisplay, SnakeGrid.GRID_BOX_L *2, 
				SnakeGrid.GRID_BOX_L *2);	
	}
	
	/* Resets the score to zero */
	public void reset(){
		this.score = 0;
	}
	
	/* Sets the dimensions of the score window. */
	@Override
	public Dimension getPreferredSize(){
		return new Dimension(scoreBoxWidth, scoreBoxHeight);	
	}
	

}
