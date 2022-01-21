package eightQueens;

public class GameRunner 
{
	public static void main(String[] args) 
	{	
		Board game = new Board(true);

		EightQueensUserInterface ui = new EightQueensUserInterface(game);
	}
}