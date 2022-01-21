package eightQueens;

public class Position 
{
	private boolean isPlaced; //default is false
	private int coordinateX, coordinateY;
	private char state; //default is 'O'	
	public final char[] possibleStates = {'O','X','-'};
	//State can only be O for open, X for placed, or - for attacked
	
	/**
	 * Initializes the global variables to default values
	 */
	public Position(int x, int y)
	{
		isPlaced = false;
		state = possibleStates[0];
		coordinateX = x;
		coordinateY = y;
	}
	
	/**
	 * Checks the state to make sure it is 'O' then changes the state of the position on the board to 'X' 
	 * @param x
	 * @param y
	 * @param gameBoard
	 */
	public void place(int x, int y, Position[][] gameBoard)
	{
		if(gameBoard[x][y].getState() == possibleStates[0])
		{
			isPlaced = true;
			coordinateX = x;
			coordinateY = y;
			state = possibleStates[1];
		}
		else
		{
			System.err.println("Cannot place piece, current state of position is " + gameBoard[x][y].getState() + " at ("+coordinateX+","+coordinateY+")");
		}
	}
	
	/**
	 * Checks the position to make sure it's not attacked and changes the state of the position to attacked
	 */
	public void isAttacked()
	{
		if(state != possibleStates[1])
			state = possibleStates[2];	
	}
	
	public void isOpen()
	{
		state = possibleStates[0];
	}
	
	/**
	 * Returns an array containing the position of the piece in an array with format (X,Y)
	 * @return
	 */
	public int[] getCoordinate()
	{	
		int[] coordinateArray = {coordinateX, coordinateY}; 
		return coordinateArray;
	}

	/**
	 * Returns the current state of the Position
	 * @return
	 */
	public char getState()
	{
		return state;
	}
	
	/**
	 * Returns a String in format Current state: state, (coordinateX,coordinateY)
	 * @return
	 */
	@Override
	public String toString()
	{
		return "Current state: " + state + " at ("+coordinateX+","+coordinateY+")";
	}
	
	/**
	 * Returns a string containing all relevant information for the position
	 * @return
	 */
	private String[] copyOut()
	{
		String[] output =
			{
				""+state,
				""+isPlaced,
				""+coordinateX,
				""+coordinateY
			};
		
		return output;
	}
	
	/**
	 * Takes a Position as input and turns the current position into a copy of the parameter
	 * @param temp
	 */
	public void copyIn(Position temp)
	{
		String[] input = temp.copyOut();
		state = input[0].charAt(0);
		isPlaced = Boolean.parseBoolean(input[1]);
		coordinateX = Integer.parseInt(input[2]);
		coordinateY = Integer.parseInt(input[3]);
	}
}
