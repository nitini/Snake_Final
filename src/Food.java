import java.awt.Point;


/* The food class, primarily used to generate food when neccessary, as actual
 * food types are just an enum or a type of tile to be put into the grid.
 */
public class Food {
	
	private SnakeGrid grid;
	private SnakeBody snake;
	private Enemy enemy;
	private int randomX = 10;
	private int randomY = 10;
	private Point randomFood;
	int food_frequency;
	
	/* Constructs a new food object, which basically means that a random point
	 * is generated and the newly made ojbects are assigned to their respective
	 * variables
	 */
	public Food (SnakeGrid grid, SnakeBody snake, Enemy enemy){
		this.grid = grid;
		this.snake = snake;
		this.enemy = enemy;
		randomFood = generateRandomPoint();
	}
	
	/* The main work horse of the class. This function is called at every
	 * interval, but food is only needed when it has just been eaten, so while
	 * this method is called every time, it only fires when the snake, or the
	 * enemy is on a food tile. If the snake or enemy are null then nothing
	 * happens.
	 */
	public void generateFood(){
		if (grid.getSnakeHeadType() == null || 
				enemy.getEnemyHeadType() == null){
			return;
		}
		
		if ((!grid.getSnakeHeadType().equals(GridBoxType.SNAKE) &&
				!grid.getSnakeHeadType().equals(GridBoxType.EMPTY))
				|| (!enemy.getEnemyHeadType().equals(GridBoxType.ENEMY) &&
				!enemy.getEnemyHeadType().equals(GridBoxType.EMPTY) &&
				!enemy.getEnemyHeadType().equals(GridBoxType.SNAKE))){
			
			if(snake.getBodyList().getFirst().x != 1 && 
					snake.getBodyList().getFirst().y != 1){
				grid.setGridBoxType(snake.getSnakeHeadLocation().x, 
				snake.getSnakeHeadLocation().y, GridBoxType.SNAKE);
			}
			/* Makes sure we don't generate food on a snake Tile */
			while (snake.getBodyList().contains(randomFood) 
					|| enemy.getEnemyHeadLocation().contains(randomFood)){
				randomX = (int) (Math.random() * SnakeGrid.GRID_WIDTH);
				randomY = (int) (Math.random() * SnakeGrid.GRID_HEIGHT);
				randomFood = new Point (randomX, randomY);
			}
			
			/* This handles the frequency of each type of food, a random
			 * number from 0 to 100 is generated, and depending on what range
			 * the number is in determines what type of food we choose. The
			 * most common occurrence is normal food, and the speed food is
			 * generated less frequently.
			 */
			food_frequency =  (int) (Math.random() * 100);
			if (food_frequency < 20) {
				grid.setGridBoxType(randomFood.x, randomFood.y, 
					GridBoxType.NORM_FOOD);
			} else if (food_frequency >= 20 && food_frequency < 80) {
				grid.setGridBoxType(randomFood.x, randomFood.y, 
					GridBoxType.SUPER_FOOD);
			} else if (food_frequency >= 80 && food_frequency < 90){
				grid.setGridBoxType(randomFood.x, randomFood.y, 
					GridBoxType.AVERAGE_FOOD);
			} else if (food_frequency >= 90 && food_frequency < 98){
				grid.setGridBoxType(randomFood.x, randomFood.y, 
					GridBoxType.SLOW_FOOD);
			} else if (food_frequency >= 98 && food_frequency < 100){
				grid.setGridBoxType(randomFood.x, randomFood.y, 
					GridBoxType.FAST_FOOD);
			}
		
		}
	}
	
	/* Generates a random point to place the food when it is generated */
	public Point generateRandomPoint() {
		randomX = (int) (Math.random() * SnakeGrid.GRID_WIDTH);
		randomY = (int) (Math.random() * SnakeGrid.GRID_HEIGHT);
		return new Point(randomX, randomY);
	}
}
