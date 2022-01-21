package eightQueens;

public class GameTree 
{
	private TreeNode origin;
	private Board board;
	protected Board[] finalBoards;
	
	/**
	 * Constructor for initializing the game tree
	 * @param gB
	 */
	public GameTree(Board gB)
	{
		board = gB;
		origin = new TreeNode(gB);
	}
	
	/**
	 * Does up to 7 passes over the gameBoard to find the solution with the least amount of open spaces
	 * In the event of a tie the most recently searched branch is chosen
	 * 
	 * Finally returns the final gameBoard
	 * @return
	 */
	public Board search()
	{		
		int[] openSpaces = new int[7];
		int[] passes = {-1,-1,-1,-1,-1,-1,-1};
		TreeNode primaryTemp, secondaryTemp;
		/*
		 *Each pass checks each lower branch for whichever has the most open spaces
		 *The location of the branch with the most open spaces is then stored into passes
		 *The amount of open spaces of this branch is also stored into openSpaces to be used in the next pass
		 */
		
		//Pass 1
		primaryTemp = origin;
		for(int i = 0; i < primaryTemp.lowerBranch.length; i++)
		{
			if(primaryTemp.lowerBranch[i].getOpenSpaces() > openSpaces[0])
			{
				passes[0] = i;
				openSpaces[0] = primaryTemp.lowerBranch[i].getOpenSpaces();
			}
		}
				
		
		//Pass 2
		secondaryTemp = primaryTemp.lowerBranch[passes[0]];
		secondaryTemp.generateLower();
		for(int i = 0; i < openSpaces[0]; i++)
		{
			if(secondaryTemp.lowerBranch[i].getOpenSpaces() > openSpaces[1])
			{
				passes[1] = i;
				openSpaces[1] = secondaryTemp.lowerBranch[i].getOpenSpaces();
			}
		}
		
		//Pass 3
		primaryTemp = secondaryTemp.lowerBranch[passes[1]];
		primaryTemp.generateLower();
		for(int i = 0; i < openSpaces[1]; i++)
		{
			if(primaryTemp.lowerBranch[i].getOpenSpaces() > openSpaces[2])
			{
				passes[2] = i;
				openSpaces[2] = primaryTemp.lowerBranch[i].getOpenSpaces();
			}
		}
		
		//Pass 4
		secondaryTemp = primaryTemp.lowerBranch[passes[2]];
		secondaryTemp.generateLower();
		for(int i = 0; i < openSpaces[2]; i++)
		{
			if(secondaryTemp.lowerBranch[i].getOpenSpaces() > openSpaces[3])
			{
				passes[3] = i;
				openSpaces[3] = secondaryTemp.lowerBranch[i].getOpenSpaces();
			}
		}
		
		//subsequent passes are special because the game can be ended with a minimum of 5 queens
		//Pass 5
		if(passes[3] != -1)
		{
			primaryTemp = secondaryTemp.lowerBranch[passes[3]];
			if(!primaryTemp.gameBoard.getStatus())
			{
				primaryTemp.generateLower();
				for(int i = 0; i < openSpaces[3]; i++)
				{
					if(primaryTemp.lowerBranch[i].getOpenSpaces() >= openSpaces[4])
					{
						passes[4] = i;
						openSpaces[4] = primaryTemp.lowerBranch[i].getOpenSpaces();
					}
				}
			}
		}
		
		//Pass 6
		if(passes[4] != -1)
		{	
			secondaryTemp = primaryTemp.lowerBranch[passes[4]];
			if(!secondaryTemp.gameBoard.getStatus())
			{
				secondaryTemp.generateLower();
				for(int i = 0; i < openSpaces[4]; i++)
				{
					if(secondaryTemp.lowerBranch[i].getOpenSpaces() >= openSpaces[5])
					{
						passes[5] = i;
						openSpaces[5] = secondaryTemp.lowerBranch[i].getOpenSpaces();
					}
				}
			}
		}
		
		//Pass 7
		if(passes[5] != -1)
		{
			primaryTemp = secondaryTemp.lowerBranch[passes[5]];
			if(!primaryTemp.gameBoard.getStatus())
			{
				primaryTemp.generateLower();
				for(int i = 0; i < openSpaces[5]; i++)
				{
					if(primaryTemp.lowerBranch[i].getOpenSpaces() >= openSpaces[6])
					{
						passes[6] = i;
						openSpaces[6] = primaryTemp.lowerBranch[i].getOpenSpaces();
					}
				}
			}
		}
		
		int index = -1;
		for(int x: passes)
		{
			if(x == -1)
				break;
			else
				index++;
		}
		
		TreeNode temp;
		
		if(index%2 == 1)
			temp = primaryTemp.higherBranch;
		else
			temp = secondaryTemp.higherBranch;
		
		int[][] results = new int[temp.gameBoard.getOpenspaces()][passes.length];
		
		for(int i = 0; i < temp.gameBoard.getOpenspaces(); i++)
			for(int j = 0; j < passes.length; j++)
				results[i][j] = passes[j];
				
		deeperSearch(results, index);
		
		return parseResult(passes); // changed from passes -> results
	}
	
	/**
	 * Backtracks one node to display all possible locations for final queen
	 * @param results
	 * @param index
	 * @return
	 */
	private void deeperSearch(int[][] results, int index)
	{
		TreeNode copy = new TreeNode(board);
		finalBoards = new Board[results.length];
		
		for(int i = 0; i < index; i++)
		{
			if(results[0][i] == -1)
				break;
			else
			{
				copy = copy.lowerBranch[results[0][i]];
				copy.generateLower();
			}
		}
				
		for(int i = 0; i < results.length; i++)
			results[i][index] = i;
		/*
		try
		{
		for(int i = 0; i < results.length; i++)
			finalBoards[i] = copy.lowerBranch[i].gameBoard;		
		}
		catch(NullPointerException e)
		{
			for(int[] x: results)
			{
				for(int y: x)
					System.out.print(y + " ");
				System.out.print("\n");
			}
			
			copy.higherBranch.higherBranch.gameBoard.displayBoard();
			
			System.exit(1);
		}
		*/
		if(index != results[0].length-1)
			for(int i = 0; i < results.length; i++)
				finalBoards[i] = copy.lowerBranch[i].gameBoard;
	}
	
	/**
	 * Takes the list of indexes found from search() and returns the final gameBoard
	 * @param index
	 */
	private Board parseResult(int[] index)
	{
		TreeNode finalResult = origin;
		
		
		
		//Navigates along the branches indicated given by the passes variable from search()
		for(int x: index)
		{
			if(x == -1)
				break;
			else
				finalResult = finalResult.lowerBranch[x];
		}
		 
		return finalResult.gameBoard;
		
	}
}
