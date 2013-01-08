import java.awt.Point;
import java.util.LinkedList;

/* This is the data structure implementation for the snakeBody, it contains all
 * the information about the snake, mainly where the snake's head is, and 
 * what tiles in the grid should be of type snake.
 */
public class SnakeBody {
	
	private SnakeDirection currentDirection = SnakeDirection.NONE;
	private SnakeDirection tempDirection = SnakeDirection.NONE;
	private SnakeGrid grid;
	private LinkedList<Point> body;
	private Point headLocation;
	private GridBoxType headLocationType;
	private int superFoodCount = -1;
	static final int SUPER_FOOD_BONUS = 16;
	
	/* Constructor for the snake, sets up the grid, and creates a new Linked
	 * list the chosen data structure for the snake. Then sets the starting
	 * point for the snake, currently at (1,1), and adds that point to the list.
	 */
	public SnakeBody(SnakeGrid grid) {
		this.grid = grid;
		this.body = new LinkedList<Point>();
		Point head = new Point (1, 1);
		body.add(head);
	}
	
	/* This function works in tandem with the key entry part in the SnakeGrid,
	 * it sets the temporary direction of the snake but and does not allow for
	 * the snake to move backwards, or against itself.
	 */
	public void setDirection(SnakeDirection direction) {
		if(direction.equals(SnakeDirection.UP) 
				&& currentDirection.equals(SnakeDirection.DOWN)) {
			return;
		} else if(direction.equals(SnakeDirection.DOWN) 
				&& currentDirection.equals(SnakeDirection.UP)) {
			return;
		} else if(direction.equals(SnakeDirection.LEFT) 
				&& currentDirection.equals(SnakeDirection.RIGHT)) {
			return;
		} else if(direction.equals(SnakeDirection.RIGHT) 
				&& currentDirection.equals(SnakeDirection.LEFT)) {
			return;
		}
		this.tempDirection = direction;
	}
	
	/* The major component of the class, this updates the snake list, and
	 * then depending on where the snake's head has just landed, it either
	 * grows, or does not grow. Death is handled here by returning null if
	 * the snake has moved off the screen, this null is handled in the 
	 * SnakeGrid class and the game ends then.
	 */
	public GridBoxType updateBody(){
		currentDirection = tempDirection;
		Point head = body.getFirst();
		
		switch(currentDirection){
		case UP:
			if (head.y <= 0){ 
				grid.setGridBoxType( head.x, head.y, GridBoxType.SNAKE);
				return null;
			}
			body.addFirst(new Point(head.x, head.y - 1));
			break;
			
		case DOWN:
			if (head.y >= SnakeGrid.GRID_HEIGHT - 1){ 
				grid.setGridBoxType( head.x, head.y, GridBoxType.SNAKE);
				return null;
			}
			body.addFirst(new Point(head.x, head.y +1));
			break;
			
		case LEFT:
			if (head.x <= 0){ 
				grid.setGridBoxType( head.x, head.y, GridBoxType.SNAKE);
				return null;}
			body.addFirst(new Point(head.x -1, head.y));
			break;
			
		case RIGHT:
			if (head.x >= SnakeGrid.GRID_WIDTH - 1){ 
				grid.setGridBoxType( head.x, head.y, GridBoxType.SNAKE);
				return null;
			}
			body.addFirst(new Point(head.x +1, head.y));
			break;
			
		case NONE:
			grid.setGridBoxType( head.x, head.y, GridBoxType.SNAKE);
			return GridBoxType.EMPTY;
		}
		
		headLocation = body.getFirst();
		headLocationType = this.grid.getGridBoxType(headLocation.x,
				headLocation.y);
		
		/* If the tile is not of a food type, then the last node is deleted */
		if((!headLocationType.equals(GridBoxType.NORM_FOOD) && 
				!headLocationType.equals(GridBoxType.SUPER_FOOD)) 
				&& superFoodCount < 0){
			Point end = body.removeLast();
			grid.setGridBoxType(end.x, end.y, GridBoxType.EMPTY);
		}
		
		superFoodCount -= 2;
		
		/* If the food type is Super, then more growth is necessary,
		 * so a counter is set to a specific value, and until it decrements to
		 * the point where the above if statement is true, the snake keeps 
		 * growing
		 */
		if (headLocationType.equals(GridBoxType.SUPER_FOOD)){
			superFoodCount = SUPER_FOOD_BONUS;
		}
		
		grid.setGridBoxType( headLocation.x, headLocation.y, 
				GridBoxType.SNAKE);
		
		return headLocationType;
	}
	
	/* This method is only used by the enemy class, basically when an enemy
	 * intersects the snake, that point on must be deleted. So to handle this
	 * a for loop is used and every point after the intersection is removed from
	 * the body list and the tile types are set back to empty.
	 */
	public void setBodyList(int index) {
		int bodySize = body.size();
		for (int i = index; i < bodySize; i++){
			Point removal = body.removeLast();
			grid.setGridBoxType(removal.x, removal.y, GridBoxType.EMPTY);
		}
	}
	
	/* This method provides the linkedList of the snake for other methods to use
	 * in order to see if any collisions have happened 
	 */
	public LinkedList<Point> getBodyList(){
		return body;
	}
	
	/* Provides the snake's head location, mainly used in the food class to
	 * see if food needs to be generated
	 */
	public Point getSnakeHeadLocation() {
		return headLocation;
	}
	
	/* Gets the type of the tile at the snake's head, also used for food
	 * generation purposes
	 */
	public GridBoxType getSnakeHeadType() {
		return headLocationType;
	}
	
	/* Provides the direction of the snake */
	public SnakeDirection getDirection(){
		return currentDirection;
	}
	
	/* Gets the length of the body of the snake */
	public int getBodyLength() {
		return body.size();
	}

	/* Resets the snake, used for resetting the game */
	public void resetSnakeBody() {
		this.currentDirection = SnakeDirection.NONE;
		this.tempDirection = SnakeDirection.NONE;
		Point head = new Point (40, 40);
		body.clear();
		body.add(head);
	}

}
