package eightQueens;
import java.util.Random;

public class Board 
{
	private Random generator = new Random();
	protected Position[][] gameBoard = new Position[8][8];
	private boolean isFull;
	private int openSpaces, score;
	//Score is increased by 1 for each piece placed with an additional 2 points if the game is won
	
	//Above is deprecated, score now represents how many pieces were successfully placed
	
	/*
	 * Constructor for initializing the gameboard
	 */
	public Board(boolean silenceDisplay)
	{
		score = 0;
		openSpaces = 63;
		isFull= false;

		int x = generator.nextInt(6)+1;
		int y = generator.nextInt(6)+1;
		//Generates a random coordinate pair between (0-7,0-7)

		for( int i = 0; i < 8; i++)
			for(int j = 0; j < 8; j++)
			{
				gameBoard[i][j] = new Position(j,i);
			}
		//Initializes all positions on the board to default settings
		
		placePiece(x,y, silenceDisplay);
	}
	
	/**
	 * Constructor for creating a copy of a gameboard
	 * @param gB
	 */
	public Board(Board gB)
	{
		gameBoard = gB.getGameBoard();
		isFull = gB.getStatus();
		openSpaces = gB.getOpenspaces();
		score = gB.getScore();
	}
	
	/**
	 * Checks the position to ensure it is open then places a piece at the given x,y coordinates then goes to evalAttack
	 * @param x
	 * @param y
	 */
	public void placePiece(int x, int y, boolean silenceDisplay)
	{
		if(gameBoard[y][x].getState() == 'O')
		{
			gameBoard[y][x].place(y, x, gameBoard);
			evalAttack(y,x, silenceDisplay);			
			score++;
		}
		else
			System.err.println("Cannot place piece, position is " + gameBoard[y][x].getState());
		//Checks the state of the position to ensure it is open, if not print to error stream.
	}

	/*
	 * Changes all spaces relevant to the given position to attacked, then goes to evalBoard
	 */
	private void evalAttack(int x, int y, boolean silenceDisplay)
	{
		for(int i = 0; i < 8; i++)
		{
			gameBoard[x][i].isAttacked();
			gameBoard[i][y].isAttacked();
		}
		//Marks all positions in the same X or same Y as attacked
		
		int incrementX = x;
		int incrementY = y;
		while(incrementX <= 7 && incrementY <= 7)
		{
			gameBoard[incrementX][incrementY].isAttacked();
			incrementX++;
			incrementY++;
		}
		//Evaluates bottom right diagonal
		
		incrementX = x;
		incrementY = y;
		while(incrementX >= 0 && incrementY <= 7)
		{
			gameBoard[incrementX][incrementY].isAttacked();
			incrementX--;
			incrementY++;
		}
		//Evaluates top right diagonal
		
		incrementX = x;
		incrementY = y;
		while(incrementX >= 0 && incrementY >= 0)
		{
			gameBoard[incrementX][incrementY].isAttacked();
			incrementX--;
			incrementY--;
		}
		//Evaluates top left diagonal
		
		incrementX = x;
		incrementY = y;
		while(incrementX <= 7 && incrementY >= 0)
		{
			gameBoard[incrementX][incrementY].isAttacked();
			incrementX++;
			incrementY--;
		}
		//Evaluates bottom left diagonal
		
		evalBoard(silenceDisplay);
	}
	
	/**
	 * Checks each position of the board to see if there are any valid moves left to be made then displays the board
	 */
	private void evalBoard(boolean silenceDisplay)
	{
		boolean boardFull = true;
		int count = 0;
		
		for( int i = 0; i < 8; i++)
			for(int j = 0; j < 8; j++)
			{
				if(gameBoard[i][j].getState() == 'O')
				{
					boardFull = false;
					count++;
				}
			}
		openSpaces = count;
/*		
		if(openSpaces == 0 && score == 7)
			score = 10;
		else if(openSpaces == 0 && score <7)
			score += 2;
		/*
		 * This signifies if either all queens were placed then score=10
		 * If there are no moves left to play then the score will be # of queens placed +2
		 */
	
		if(boardFull)
			isFull = true;
		if(!silenceDisplay)
			displayBoard();	
	}
	
	/**
	 * Outputs to the console a graphical representation of the current state of the board as a whole
	 * 
	 * Deprecated due to creation of UI
	 */
	public void displayBoard()
	{
		int count = 0;
		
		for( int i = 0; i < 8; i++)
			for(int j = 0; j < 8; j++)
			{
				System.out.print(gameBoard[i][j].getState() +  " ");
				count++;
				if(count%8 == 0)
					System.out.println();
			}
		System.out.println();
	}
	
	/**
	 * Returns the count of current open spaces
	 * @return
	 */
	public int getOpenspaces()
	{	
		return openSpaces; 
	}
	
	/**
	 * Returns the current score
	 * @return
	 */
	public int getScore()
	{
		return score;
	}
	
	/**
	 * Returns isFull
	 * @return
	 */
	public boolean getStatus()
	{
		return isFull;
	}
	
	/**
	 * Returns a copy of the current gameBoard
	 * @return
	 */
	public Position[][] getGameBoard()
	{
		Position[][] temp = new Position[8][8];

		for( int i = 0; i < 8; i++)
			for(int j = 0; j < 8; j++)
			{
				temp[i][j] = new Position(j,i);
				temp[i][j].copyIn(gameBoard[i][j]);
			}		
		return temp;
	}
	
	/**
	 * Returns an array of positions of every open space on the gameboard
	 * @return
	 */
	public Position[] getOpenPositions()
	{
		Position[] locations = new Position[openSpaces];
		
		for(int i = 0; i < locations.length; i++)
			locations[i] = new Position(0,0);
		
		int count = 0;
		for(Position[] temp1: gameBoard)
			for(Position temp2: temp1)
				if(temp2.getState() == 'O')
				{
					locations[count].copyIn(temp2);
					count++;
				}
		
		return locations;
	}
}
