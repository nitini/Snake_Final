import java.awt.Point;
import java.util.LinkedList;

/* The enemy class, a hybrid between food and a snake, it's purpose is to
 * delete a part of the snake when it intersects with the snake.
 */
public class Enemy {
	private SnakeGrid grid;
	private SnakeBody snake;
	private LinkedList<Point> enemyBody;
	private GridBoxType enemyHeadType;
	private Point enemyHeadLocation;
	boolean reachedEnd = false;
	private SnakeDirection enemyDirection;
	private int randomX = 30;
	private int randomY = 10;
	double randEnemyDirNum = 0;
	private Point head;
	
	/* Constructs a new enemy, which mainly involves setting the point at
	 * which to spawn the enemy, and assigning the necessary internal state.
	 */
	public Enemy(SnakeGrid grid, SnakeBody snake){
		this.grid = grid;		
		this.snake = snake;
		enemyDirection = randEnemyDirection();
		this.enemyBody = new LinkedList<Point>();
		randomX = (int) (Math.random() * SnakeGrid.GRID_WIDTH);
		randomY = (int) (Math.random() * SnakeGrid.GRID_HEIGHT);
		head = generateRandomPoint();
		enemyBody.add(head);
	}
	
	/* This is the work horse of the class, it is only periodically called
	 * as explained in the SnakeGrid class. if this function is called, then
	 * it creates what is basically a 1 length snake and moves it in a randomly
	 * designated direction. The interesting part is that if the method is 
	 * called and the enemy is at the edge of a tile, it changes its internal
	 * state to reflect this, so a new point is chosen to spawn an enemy.
	 */
	public boolean generateEnemy(){
		head = enemyBody.getFirst();
			/* Handles spawning new enemy if current one has reached edge */
			if (reachedEnd){
				enemyDirection = randEnemyDirection();
				enemyBody.addFirst(generateRandomPoint());
				if (snake.getBodyList().contains(enemyBody.getFirst())){
					enemyBody.removeFirst();
					enemyBody.addFirst(generateRandomPoint());
				}
				enemyBody.removeLast();
				reachedEnd = false;
			}
		/* Updates the movement of the enemy */
		head = enemyBody.getFirst();
			if (enemyDirection == SnakeDirection.UP){
				if (head.y <= 0){
					reachedEnd = true;
					grid.setGridBoxType(head.x, head.y, GridBoxType.EMPTY);
					return false;}
				enemyBody.addFirst(new Point(head.x, head.y - 1));
			} else if (enemyDirection == SnakeDirection.DOWN){
				if (head.y >= SnakeGrid.GRID_HEIGHT -1){ 
					reachedEnd = true;
					grid.setGridBoxType(head.x, head.y, GridBoxType.EMPTY);
					return false;}
				enemyBody.addFirst(new Point(head.x, head.y + 1));
			} else if (enemyDirection == SnakeDirection.RIGHT){
				if (head.x >= SnakeGrid.GRID_WIDTH - 1){ 
					reachedEnd = true;
					grid.setGridBoxType(head.x, head.y, GridBoxType.EMPTY);
					return false;}
				enemyBody.addFirst(new Point(head.x + 1, head.y));
			} else {
				if (head.x <= 0){
					reachedEnd = true;
					grid.setGridBoxType(head.x, head.y, GridBoxType.EMPTY);
					return false;}
				enemyBody.addFirst(new Point(head.x - 1, head.y));
			}
			
			/* Handling the drawing part of the tile that was just
			 *  crossed, making the old one back to empty.
			 */
			enemyHeadLocation = enemyBody.getFirst();
			enemyHeadType = this.grid.getGridBoxType(enemyHeadLocation.x,
					enemyHeadLocation.y);
			Point end = enemyBody.removeLast();
			grid.setGridBoxType(end.x, end.y, GridBoxType.EMPTY);
			
			Point snakeLocation = new Point (snake.getBodyList().getFirst().x, 
					snake.getBodyList().getFirst().y);
			
			/* Handles if the enemy has intersected the snake, makes a call
			 * to the setBodyList function, passing off the index of the snake
			 * where the intersection happened.
			 */
			if (enemyHeadType == GridBoxType.SNAKE){
				int cutPoint = snake.getBodyList().indexOf(enemyHeadLocation);
				if (snakeLocation.x == enemyHeadLocation.x 
						&& snakeLocation.y == enemyHeadLocation.y){
					cutPoint++;
				}
				snake.setBodyList(cutPoint);
			}
			
			/* Make the tile now on into type Enemy */
			grid.setGridBoxType(enemyHeadLocation.x, enemyHeadLocation.y, 
					GridBoxType.ENEMY);
			return true;
	}
	
	/* Getter method for the head of the enemy. */
	public LinkedList<Point> getEnemyHeadLocation() {
		return enemyBody;
	}

	/* Used in the food class to check where the enemy head is, in order to
	 * generate more food if those two types of tiles intersect */
	public GridBoxType getEnemyHeadType (){
		return enemyHeadType;
	}
	
	/* Generates a random direction for the enemy to move */
	public SnakeDirection randEnemyDirection() {
		randEnemyDirNum = (Math.random() * 4);
		if (randEnemyDirNum < 1.0){
			return SnakeDirection.UP;
		} else if (randEnemyDirNum >= 1.0 && randEnemyDirNum < 2.0){
			return SnakeDirection.DOWN;
		} else if(randEnemyDirNum >= 2.0 && randEnemyDirNum < 3.0){
			return SnakeDirection.RIGHT;
		} else {
			return SnakeDirection.LEFT;
		}
	}
	
	/* Generates a random point to spawn an enemy at */
	public Point generateRandomPoint() {
		randomX = (int) (Math.random() * SnakeGrid.GRID_WIDTH);
		randomY = (int) (Math.random() * SnakeGrid.GRID_HEIGHT);
		return new Point(randomX, randomY);
	}
	
}
	



