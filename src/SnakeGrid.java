import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JComponent;

@SuppressWarnings("serial")

/* This class implements the grid or playing area of the game. It handles all
 * of the drawing functions of the game, handles key input, and most importantly
 * contains the method that is called by the Game class that gets everything 
 * going.
 */
public class SnakeGrid extends JComponent {

	public static final int GRID_BOX_L = 10;
	public static final int GRID_HEIGHT = 40;
	public static final int GRID_WIDTH = 60;
	public SnakeBody snake;
	private Food food;
	private Enemy enemy;
	boolean running = true;
	private int randGen;
	private int randomX;
	private int randomY;
	private GridBoxType[][] gameGrid;
	public GridBoxType snakeHeadType;
	
	/* Constructor makes a new gameGrid, the actual array for the snake. It also
	 * makes a new snake, new food, and new enemy. The grid is then reset so
	 * that all the tiles are empty, and then the first food tile is set. Also
	 * the constructor has within it the basic code for key events, here the
	 * game uses the arrow keys
	 */
	public SnakeGrid(){
		gameGrid = new GridBoxType[GRID_WIDTH][GRID_HEIGHT];
		snake = new SnakeBody(this);
		enemy = new Enemy(this, snake);
		food = new Food(this, snake, enemy);
		resetGrid();
		Point originFood = generateRandomPoint();
		gameGrid[originFood.x][originFood.y] = GridBoxType.NORM_FOOD;
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
		setFocusable(true);

		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_LEFT)
					snake.setDirection(SnakeDirection.LEFT);
				else if (e.getKeyCode() == KeyEvent.VK_RIGHT)
					snake.setDirection(SnakeDirection.RIGHT);
				else if (e.getKeyCode() == KeyEvent.VK_UP)
					snake.setDirection(SnakeDirection.UP);
				else if (e.getKeyCode() == KeyEvent.VK_DOWN)
					snake.setDirection(SnakeDirection.DOWN);
			}
		});
	}
	
	/* This is the most crucial method of the game it makes calls to 
	 * generateFood, and to updateSnakeBody, which basically advances the game.
	 * The way in which enemy Generation is handled is quite interesting.
	 * In order to not constantly generate enemies a random number is generated
	 * and then if that number is less than three an enemy is generated and the
	 * function is called until the boolean running hits false. This way as
	 * long as both booleans are false, no enemies are generated, but if one
	 * is true, then the method call is made. Finally this method calls the 
	 * repaint method which indirectly calls paintComponent.
	 */
	public void playGame(){
		food.generateFood();
		snakeHeadType = snake.updateBody();
		randGen = (int)(Math.random() * 100);
		if(running || randGen < 5){
			running = enemy.generateEnemy();
		}
		repaint();
	}
	
	/* Resets the grid by making every tile of type Empty. */
	public void resetGrid() {
		for (int i = 0; i < GRID_WIDTH; i++){
			for (int j = 0; j < GRID_HEIGHT; j++){
				gameGrid[i][j] = GridBoxType.EMPTY;
			}	
		}	
	}
	
	/* This method resets the game, by which it remakes snake, food, and enemy
	 * objects, resets the grid, and then regenerates new food. This is done
	 * here and not in the game class because the grid has access to these
	 * specific objects, while the game does not.
	 */
	public void resetGame(){
		snake = new SnakeBody(this);
		enemy = new Enemy(this, snake);
		food = new Food(this, snake, enemy);
		resetGrid();
		Point originFood = generateRandomPoint();
		gameGrid[originFood.x][originFood.y] = GridBoxType.NORM_FOOD;
		requestFocusInWindow();
	}
	
	/* Setter method for the gameGrid, can set a location to a type of tile */
	public void setGridBoxType(int x, int y, GridBoxType type){
		this.gameGrid[x][y] = type;
		if(x == 0 && y == 0){
		}
	}
	
	/* Getter method for the gameGrid, can get the type of a specific tile. */
	public GridBoxType getGridBoxType(int x, int y){
		return this.gameGrid[x][y];
	}
	
	/* The following methods are there due to a specific design choice to make
	 * the grid construct a snake. Some of the other method calls within the
	 * game require information about the snake, so that information has to be
	 * threaded from the snake to the grid, from which other methods can get
	 * access to snake information.
	 */
	public GridBoxType getSnakeHeadType() {
		return snakeHeadType;
	}
	
	public int getSnakeLength() {
		return snake.getBodyLength();
	}
	
	/* Generates a random point used to spawn new food and enemies */
	public Point generateRandomPoint() {
		randomX = (int) (Math.random() * SnakeGrid.GRID_WIDTH);
		randomY = (int) (Math.random() * SnakeGrid.GRID_HEIGHT);
		return new Point(randomX, randomY);
	}
	
	/* The actual drawing of the game, basically depending on the tile type
	 * a shape is drawn in the tile, or nothing. The method is made efficient by
	 * only filling in the tiles that are not empty, as opposed to filling in
	 * each and every tile, including the empty ones.
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(GridBoxType.SNAKE.getColor());
		for (int i = 0; i < GRID_WIDTH; i++){
			for(int j = 0; j < GRID_HEIGHT; j++){
				if (gameGrid[i][j].equals(GridBoxType.EMPTY)){
					continue;
				} else if (gameGrid[i][j].equals(GridBoxType.NORM_FOOD)){
					g.setColor(GridBoxType.NORM_FOOD.getColor());
					g.fillOval(i*GRID_BOX_L, j*GRID_BOX_L, GRID_BOX_L -2,
							GRID_BOX_L -2);
					g.setColor(GridBoxType.SNAKE.getColor());
				} else if (gameGrid[i][j].equals(GridBoxType.SUPER_FOOD)) {
					g.setColor(GridBoxType.SUPER_FOOD.getColor());
					g.fillOval(i*GRID_BOX_L, j*GRID_BOX_L, GRID_BOX_L -2,
							GRID_BOX_L -2);
					g.setColor(GridBoxType.SNAKE.getColor());
				} else if (gameGrid[i][j].equals(GridBoxType.AVERAGE_FOOD)){
					g.setColor(GridBoxType.AVERAGE_FOOD.getColor());
					g.fillOval(i*GRID_BOX_L, j*GRID_BOX_L, GRID_BOX_L -2,
							GRID_BOX_L -2);
					g.setColor(GridBoxType.SNAKE.getColor());
				} else if (gameGrid[i][j].equals(GridBoxType.SLOW_FOOD)){
					g.setColor(GridBoxType.SLOW_FOOD.getColor());
					g.fillOval(i*GRID_BOX_L, j*GRID_BOX_L, GRID_BOX_L -2,
							GRID_BOX_L -2);
					g.setColor(GridBoxType.SNAKE.getColor());
				} else if (gameGrid[i][j].equals(GridBoxType.FAST_FOOD)){
					g.setColor(GridBoxType.FAST_FOOD.getColor());
					g.fillOval(i*GRID_BOX_L, j*GRID_BOX_L, GRID_BOX_L -2,
							GRID_BOX_L -2);
					g.setColor(GridBoxType.SNAKE.getColor());
				} else if (gameGrid[i][j].equals(GridBoxType.ENEMY)){
					g.setColor(GridBoxType.ENEMY.getColor());
					g.fillRect(i*GRID_BOX_L, j*GRID_BOX_L, GRID_BOX_L -2,
							GRID_BOX_L -2);
					g.setColor(GridBoxType.SNAKE.getColor());
				} else {
					g.fillRect(i * GRID_BOX_L, j * GRID_BOX_L, 
							GRID_BOX_L -1 , GRID_BOX_L -1);
				}
			}
		}
	}
	
	/* Set the size of the game you want to play */
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(GRID_BOX_L * GRID_WIDTH, 
				GRID_BOX_L * GRID_HEIGHT );
	}
	
}
