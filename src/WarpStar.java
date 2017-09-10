/**
 * The WarpStar class has its own methods which are called in the level
 * class. These methods include the constructor which constructs the graphic
 * image of the Warpstar and implements its abilities in the game.
 * 
 * Bugs: (None that I'm aware of!) 
 * 
 * @author Susie C. and Krishna P. 
 */
public class WarpStar {

	private Graphic graphic;	// initializes graphic object

	/**
	 * Initializes a new WarpStar object to show up at the specified position.
	 *
	 * @param x is the horizontal position of the newly created warp star.
	 * @param y is the vertical position of the newly created warp star.
	 */
	public WarpStar(float x, float y) { 
		graphic = new Graphic("WARP_STAR"); // creates Warpstar object/image
		graphic.setPosition(x,y);
	}

	/**
	 * Draws this WarpStar object at its current position.
	 *
	 * @param args is not used by this program
	 * @return void
	 */
	public void update() {
		graphic.draw();
	}	

	/**
	 * This method detects whether both 1) the player's taxi has fuel, 
	 * and 2) the player is clicking on this WarpStar object. When both 
	 * are detected this method sets the taxi to travel at warp speed 
	 * toward this WarpStar.
	 *
	 * @param taxi takes in the taxi object to handle the taxi's navigation
	 * @return void
	 */
	public void handleNavigation(Taxi taxi) {
		if (GameEngine.isKeyPressed("MOUSE") && graphic.isCoveringPosition
				(GameEngine.getMouseX(), GameEngine.getMouseY()) ) {
			if( taxi.getFuel() > 0) {
				taxi.setWarp(graphic.getX(), graphic.getY());
			}
		}
	}
}



