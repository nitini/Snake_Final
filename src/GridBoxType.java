import java.awt.Color;


/* This defines what types the gameGrid can be, there are snake, enemy, food
 * and the empty type tiles. Depending on what type a tile is determines what 
 * is drawn on the tile.
 */
public enum GridBoxType {
	
	SNAKE(Color.GREEN),
	
	NORM_FOOD(Color.RED),
	
	SUPER_FOOD(Color.ORANGE),
	
	FAST_FOOD(Color.CYAN),
	
	SLOW_FOOD(Color.PINK),
	
	AVERAGE_FOOD(Color.YELLOW),
	
	EMPTY(null),
	
	ENEMY(Color.BLUE);
	
	private Color gridBoxColor;
	
	private GridBoxType (Color color) {
		this.gridBoxColor = color;
	}
	
	public Color getColor() {
		return gridBoxColor;
	}
}
