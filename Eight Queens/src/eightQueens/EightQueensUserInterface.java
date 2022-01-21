package eightQueens;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class EightQueensUserInterface 
{
	private Board gameBoard;
	private JFrame Frame;
	private JButton[] buttons;
	private GameTree tree;
	private int boardLocation;
	
	/**
	 * Creates a menu that allows the user to select who plays
	 * The choice is then sent to UIBuilder and the current menu is disposed
	 * @param gameBoard
	 */
	public EightQueensUserInterface(Board gameBoard)
	{	
		JFrame tempFrame = new JFrame();
		tempFrame.setDefaultCloseOperation(tempFrame.EXIT_ON_CLOSE);
		
		JButton tempB1,tempB2;
		
		tempB1 = new JButton("I want to play!");
		tempB2 = new JButton("Let the AI play!");

		tempB1.setBounds(0, 0, 300, 50);
		tempB2.setBounds(0, 50, 300, 50);
		
		tempFrame.add(tempB1);
		tempFrame.add(tempB2);
		
		tempB1.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				tempFrame.dispose();
				UIBuilder(gameBoard, true);
			}
		});
		
		tempB2.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				tempFrame.dispose();
				UIBuilder(gameBoard, false);
			}
		});
		
		tempFrame.setSize(300,140);  
		tempFrame.setLayout(null);  
		tempFrame.setVisible(true); 
	}
	
	/**
	 * Creates the frame for the gameBoard and if applicable generates the tree for the AI
	 * It will then call initializeButtons() to create the buttons as well as call updateUI to ensure the text on each button is correct
	 * @param gameBoard
	 * @param isHuman
	 */
	private void UIBuilder(Board gameBoard, boolean isHuman)
	{
		Frame = new JFrame();
		
		Frame.setDefaultCloseOperation(Frame.EXIT_ON_CLOSE);
		Frame.setSize(495,520);  
		Frame.setLayout(null);  
		Frame.setVisible(true);  
		//Configures the frame's size and behavior
		
		if(isHuman)
		{
			this.gameBoard = gameBoard;
			buttons = new JButton[64];
			
			initializeButtons(true);   
			updateUI();
		}
		else
		{	
			
			tree = new GameTree(gameBoard);
			//this.gameBoard = tree.search();
			
			this.gameBoard = tree.search();
						
			buttons = new JButton[64];
			
			initializeButtons(true); 					
			updateUI();
		}
	}
	
	/**
	 * Creates each button in the buttons array as well as defines the action when a button is clicked
	 * @param isHuman
	 */
	private void initializeButtons(boolean isHuman)
	{
		int countX = 0;
		int countY = 0;
		for(int i = 0; i < 64; i++)
		{
			buttons[i] = new JButton("" + countX + "," + countY);
			
			buttons[i].setBounds(0+countX*60,0+countY*60,60,60);
			buttons[i].setBackground(Color.WHITE);
			if((countX%2 == 1 && countY%2 == 0) || (countX%2 == 0 && countY%2 == 1))
				buttons[i].setBackground(Color.BLACK);
			buttons[i].setForeground(Color.RED);
			//Configures the button's size/color, and its texts' contents/color
			
			buttons[i].addActionListener(new ActionListener() 
			{
		         public void actionPerformed(ActionEvent ae) 
		         {
		        	JButton temp = (JButton) ae.getSource();
					if(!temp.getText().equals("-") && !temp.getText().equals("X"))
					{
					     int x,y;
					     String contents = temp.getText();
					     
					     x = Integer.parseInt("" + contents.charAt(0));
					     y = Integer.parseInt("" + contents.charAt(2));
					     
					     gameBoard.placePiece(x, y, true);
					     updateUI();
					}
					else
					{
						JOptionPane.showConfirmDialog(Frame, "Cannot place at location", null, JOptionPane.OK_OPTION ,JOptionPane.ERROR_MESSAGE);
					}
		         }
			});
			//Defines the action such that when a button is clicked a piece is placed and the board is updated
			
			Frame.add(buttons[i]);
			
			countX++;
			if(countX > 7)
			{
				countX = 0;
				countY++;
			}
			//Increments x/y and wraps x back to 0 to keep it within the bounds of the gameBoard's array
		}
	}
	
	/**
	 * Goes through the current gameBoard and substitutes the text on each button with the current state of it's
	 * position on the gameBoard
	 * 
	 * Also checks if the game is finished and prompts the user if they want to replay
	 */
	private void updateUI()
	{
		for(int i = 0; i < 64; i++)
		{
			if(!(buttons[i].getText().equals("X") || buttons[i].getText().equals("-")))
			{
				int x,y;
				String contents = buttons[i].getText();
	             
	            x = Integer.parseInt("" + contents.charAt(0));
	            y = Integer.parseInt("" + contents.charAt(2));
	            	            
	            if(gameBoard.gameBoard[y][x].getState() != 'O')
	            	buttons[i].setText("" + gameBoard.gameBoard[y][x].getState());
	         
			}
			//Visits each button, if it's text is still an X,Y coordinate it will check that position on the gameBoard
			//If that location's state on the gameBoard is anything but 'O' then the text will be changed to the position's state
		}
		if(tree == null)
		{
			if(gameBoard.getStatus())
			{
				String text;
				if(gameBoard.getScore() < 11)
					text = gameBoard.getScore() + " queens were placed successfully. Would you like to play again";
				else
					text = "All queens have been placed successfully! Would you like to play again?";
				
				int choice = JOptionPane.showConfirmDialog(Frame, text, null, JOptionPane.YES_NO_OPTION);
	
				if(choice == 0)
				{
					Frame.dispose();
					
					GameRunner.main(null);
				}
				else
				{
					Frame.dispose();
				}
			}
		}
		else
		{
			if(gameBoard.getStatus())
			{
				String text;
				//If there are multiple locations for the last queen
				if(tree.finalBoards.length > 1 && gameBoard.getScore() != 8)
				{
					text = gameBoard.getScore() + " queens placed with " + tree.finalBoards.length + " more locations for last queen. Would you like to see?";
					
					int choice = JOptionPane.showConfirmDialog(Frame, text, null, JOptionPane.YES_NO_OPTION);
					
					if(choice == 0)
					{
						int continueBoards = 0;
						utilityFrame popUp;
						do
						{
							tree.finalBoards[boardLocation].displayBoard();						
							
							popUp = new utilityFrame(tree.finalBoards[boardLocation]);
							
							boardLocation++;
							if(boardLocation >= tree.finalBoards.length)
								boardLocation = 0;
							
							JOptionPane oP = new JOptionPane("", JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION);
														
							JDialog dia = oP.createDialog(null, "");
							dia.setLocation(550, 250);
							dia.setModalityType(JDialog.ModalityType.MODELESS);
							dia.setVisible(false);
							
							
							continueBoards = JOptionPane.showConfirmDialog(dia, "Keep displaying?", null, JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
						}while(continueBoards == 0);
						
						Frame.dispose();
					}
					else
						Frame.dispose();
				}
				else
				{
					if(gameBoard.getScore() < 11)
						text = gameBoard.getScore() + " queens were placed successfully. Would you like to play again";
					else
						text = "All queens have been placed successfully! Would you like to play again?";
					
					int choice = JOptionPane.showConfirmDialog(Frame, text, null, JOptionPane.YES_NO_OPTION);
		
					if(choice == 0)
					{
						Frame.dispose();
						
						GameRunner.main(null);
					}
					else
					{
						Frame.dispose();
					}
				}
			}
		}
	}
}

class utilityFrame
{
	private JButton[] buttons;
	private JFrame Frame;
	private Board board;
	
	public utilityFrame(Board gB)
	{
		board = new Board(gB);
		buttons = new JButton[64];
		
		Frame = new JFrame();
		
		Frame.setDefaultCloseOperation(Frame.EXIT_ON_CLOSE);
		Frame.setSize(495,520);  
		Frame.setLayout(null);  
		Frame.setVisible(true);
		
		initializeButtons();
	}
	private void initializeButtons()
	{
		int countX, countY;
		
		countX = 0;
		countY = 0;
		
		for(int i = 0; i < 64; i++)
		{
			buttons[i] = new JButton("" + board.gameBoard[countY][countX].getState());
			
			buttons[i].setBounds(0+countX*60,0+countY*60,60,60);
			buttons[i].setBackground(Color.WHITE);
			if((countX%2 == 1 && countY%2 == 0) || (countX%2 == 0 && countY%2 == 1))
				buttons[i].setBackground(Color.BLACK);
			buttons[i].setForeground(Color.RED);
			
			Frame.add(buttons[i]);
			
			countX++;
			if(countX>=8)
			{
				countX=0;
				countY++;
				if(countY>=8)
					break;
			}
		}
	}
}
