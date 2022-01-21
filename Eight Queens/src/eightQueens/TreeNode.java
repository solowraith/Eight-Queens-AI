package eightQueens;

public class TreeNode 
{
	protected TreeNode higherBranch;
	protected TreeNode[] lowerBranch;
	protected Board gameBoard;
	private int openSpaces;
	
	
	/**
	 * Constructor for starting node
	 */
	public TreeNode(Board gB)
	{
		gameBoard = new Board(gB);
		openSpaces = gB.getOpenspaces();
		higherBranch = null;
		lowerBranch = new TreeNode[openSpaces];
		generateLower();
	}
	
	/**
	 * Constructor for lower level nodes
	 * @param higher
	 * @param gB
	 */
	public TreeNode(TreeNode higher, Board gB)
	{
		higherBranch = higher;
		gameBoard = new Board(gB);
		openSpaces = gB.getOpenspaces();
		lowerBranch = new TreeNode[openSpaces];
	}
	
	/**
	 * Creates the lower level branches for the node
	 */
	public void generateLower()
	{
		Position[] temp = gameBoard.getOpenPositions();
		
		for(int i = 0; i < openSpaces; i++)
		{
			int[] coordinates = temp[i].getCoordinate();
			lowerBranch[i] = new TreeNode(this, gameBoard);
			lowerBranch[i].placement(coordinates[0], coordinates[1]);
		}
	}
	
	/**
	 * Attempts to place a piece at the given x,y coordinates
	 * @param x
	 * @param y
	 */
	private void placement(int x, int y)
	{
		gameBoard.placePiece(x, y, true);
		openSpaces = gameBoard.getOpenspaces();
	}

	/**
	 * Returns the amount of open spaces on the game board
	 * @return
	 */
	public int getOpenSpaces()
	{
		return openSpaces;
	}
}
