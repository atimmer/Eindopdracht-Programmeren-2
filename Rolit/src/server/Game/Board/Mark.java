package server.Game.Board;


/**
 * This class models a Mark on the rolit board, it can be Green, Blue, Red and Yellow
 * @author Anton Timmermans & Marcel Boersma
 *
 */
public class Mark {

	public static final Mark EMPTY = new Mark(0);
	
	/**
	 * A constant for a RED marker
	 */
	public static final Mark RED = new Mark(1);
	
	/**
	 * A constant for a YELLOW marker
	 */
	public static final Mark YELLOW = new Mark(2);
	
	/**
	 * A constant for a GREEN marker
	 */
	public static final Mark GREEN = new Mark(3);
	
	/**
	 * A constant for a BLUE marker
	 */
	public static final Mark BLUE = new Mark(4);
	
	/**
	 * Stores the color of this Mark
	 * color >= 0 && color <= 4
	 */
	private int color;
	
	/**
	 * Makes a Mark without a color (empty)
	 * @ensure this.color == EMPTY
	 */
	public Mark() {
		this.color = 0;
	}
	
	/**
	 * Makes a Mark with the specified color
	 * @param color The color to make a Mark with
	 * @require color >= 0 && color <= 4
	 */
	public Mark(int color) {
		this.color = color;
	}
	
	/**
	 * Gives a nice representation of the color (ea Red, Green, Blue, Yellow)
	 */
	@Override
	public String toString() {
		String result;
		switch (this.color) {
		default:
		case 0:
			result = "-";
			break;
		case 1:
			result = "R";
			break;
		case 2:
			result = "Y";
			break;
		case 3:
			result = "G";
			break;
		case 4:
			result = "B";
			break;
		}
		return result;
	}
}
