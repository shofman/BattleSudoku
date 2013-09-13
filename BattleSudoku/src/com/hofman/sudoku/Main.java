package com.hofman.sudoku;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Main {
	
	private static Box[][] board = new Box[9][9];

	public static void main(String[] args) {
		fillBoard();
		
		//printBox(0,0, board);
		//updateBoxPoss(3,3, board);
		String originalBoard = "", newBoard = "";
		do {
			originalBoard = boardToString(board);
			updateAll(board);
			setSingleValues(board);
			newBoard = boardToString(board);
		} while(!(originalBoard.equals(newBoard)));
		displayBoard();
		findSingleRow(0, board);
		//displayBoard();
	}
	
	public static void fillBoard() {
		int boardChoice = 1;
		for(int i=0; i<9; i++) {
			for (int j=0; j<9; j++) {
				board[i][j] = new Box();
			}
		}
		switch(boardChoice) {
		case 1:	
			board[0][3].setValue(5);
			board[0][5].setValue(4);
			board[0][6].setValue(8);
			
			board[1][0].setValue(7);
			board[1][5].setValue(6);
			board[1][7].setValue(1);
			
			board[2][2].setValue(4);
			board[2][6].setValue(3);
			board[2][7].setValue(9);
			
			board[3][1].setValue(4);
			board[3][2].setValue(3);
			board[3][3].setValue(6);
			board[3][4].setValue(5);
			
			board[4][4].setValue(8);
			board[4][8].setValue(2);
			
			board[5][1].setValue(2);
			board[5][3].setValue(3);
			board[5][8].setValue(1);
			
			board[6][0].setValue(9);
			
			board[7][1].setValue(3);
			board[7][2].setValue(1);
			board[7][6].setValue(6);
			
			board[8][0].setValue(8);
			board[8][5].setValue(7);
		default:
			
		}
	}	
	
	public static void displayBoard() {
		for(int i=0; i<9; i++) {
			for (int j=0; j<9; j++) {
				if (j%3 == 0 && j!= 0) System.out.print("|");
				String answer = "";
				if (board[i][j].getValue() == 0) {
					answer += " ";
				}
				else {
					answer += "" + board[i][j].getValue();
				}
				System.out.print(" "+answer + " ");
			}
			if (i%3==2 && i!= 8) {
				System.out.print("\n");
				System.out.print("----------------------------");
			}
			System.out.println();
		}
		System.out.println();
	}
	
	public static int getBoxCenter(int value) {
		int mid = 0;
		
		//Check columns, return middle value
		if (value < 3) 		mid = 1;
		else if (value<6)	mid = 4;
		else 				mid = 7;

		return mid;
	}
	
	public static void updateAll(Box[][] overBoard) {
		updateSquares(overBoard);
		updateColsRows(overBoard);
	}
	
	public static void setSingleValues(Box[][] overBoard) {
		for (int i=0; i<9; i++) {
			for (int j=0; j<9; j++) {
				if(overBoard[i][j].possSet.size() == 1 && overBoard[i][j].getValue()==0) {
					overBoard[i][j].setValue((int) overBoard[i][j].possSet.toArray()[0]);
				}
			}
		}
	}
	
	public static void updateSquares(Box[][] overBoard) {
		//Go through each of the 9 squares, and update their possibility sets
		for(int i=0; i<9; i+=3) {
			for (int j=0; j<9; j+=3) {
				int rowMid = getBoxCenter(i);
				int colMid = getBoxCenter(j);
				updateBoxPoss(rowMid, colMid, overBoard);
			}
		}
	}
	
	public static void updateColsRows(Box[][] overBoard) {
		//Go through each column and update possibility sets
		for (int i=0; i<9; i++) {
			updateColPoss(i, overBoard);
			updateRowPoss(i, overBoard);
		}
	}
	
	public static void updateColPoss(int col, Box[][] overBoard) {
		//Find the current values that cannot be within the box (already found). Add to set
		Set<Integer> foundCol = new HashSet<Integer>();
		for (int i=0; i<9; i++) {
			int boxValue = overBoard[i][col].getValue();
			if (boxValue != 0) {
				foundCol.add(boxValue);
			}
		}
		
		//Remove these found values from the possibility set
		for (int i=0; i<9; i++) {
			updatePossSet(i, col, overBoard, foundCol);
		}
	}
	
	public static void updateRowPoss(int row, Box[][] overBoard) {
		Set<Integer> foundRow = new HashSet<Integer>();
		for(int i=0; i<9; i++) {
			int boxValue = overBoard[row][i].getValue();
			if (boxValue !=0) {
				foundRow.add(boxValue);
			}
		}
		
		for(int i=0; i<9; i++) {
			updatePossSet(row, i, overBoard, foundRow);
		}
	}
	
	public static void updateBoxPoss(int rowMid, int colMid, Box[][] overBoard) {
		//Find the current values that cannot be within the box (already found). Add to set
		Set<Integer> foundSquare = new HashSet<Integer>();
		for (int i=rowMid-1; i<rowMid+2; i++) {
			for (int j=colMid-1; j<colMid+2; j++) {
				int boxValue = overBoard[i][j].getValue();
				if (boxValue != 0) {
					foundSquare.add(boxValue);
				}
			}
		}
		
		//Remove these values from the possibility set
		for (int i=rowMid-1; i<rowMid+2; i++) {
			for (int j=colMid-1; j<colMid+2; j++) {
				updatePossSet(i, j, overBoard, foundSquare);
			}
		}
		
	}
	
	public static void updatePossSet(int row, int col, Box[][] overBoard, Set<Integer> found) {
		int boxValue = overBoard[row][col].getValue();
		if (boxValue == 0) 	
			overBoard[row][col].possSet.removeAll(found);
		
		//Otherwise, use intersection to remove all the possibilities (done for cleanliness)
		else {
			Set<Integer> alreadyFound = new HashSet<Integer>();
			alreadyFound.add(boxValue);
			overBoard[row][col].possSet.retainAll(alreadyFound);
		}
	}
	
	public static void findSingleCol(int row, Box[][] overBoard) {
		List<Integer> colEmpty = new ArrayList<Integer>();
		for(int i=0; i<9; i++) {
			if(overBoard[row][i].getValue() == 0) {
				colEmpty.add(i);
			}
		}
		
		for (int i=0; i<colEmpty.size();i++) {
			Set<Integer> compareSet = new HashSet<Integer>();
			Set<Integer> unionSet = new HashSet<Integer>();
			
			//Create new set of possibility elements for the first empty column
			Iterator<Integer> possSetIterator = overBoard[row][colEmpty.get(i)].possSet.iterator();
			while(possSetIterator.hasNext()) {
				compareSet.add((Integer) possSetIterator.next());
			}
			
			for (int j=0; j<colEmpty.size();j++) {
				if (j!=i) {
					unionSet.addAll(overBoard[row][colEmpty.get(j)].possSet);
				}
			}
			
			compareSet.removeAll(unionSet);
		}
	}
	
	public static void findSingleRow(int col, Box[][] overBoard) {
		List<Integer> rowEmpty = new ArrayList<Integer>();
		for(int i=0; i<9; i++) {
			if(overBoard[i][col].getValue() == 0) {
				rowEmpty.add(i);
			}
		}
		
		for (int i=0; i<rowEmpty.size();i++) {
			Set<Integer> compareSet = new HashSet<Integer>();
			Set<Integer> unionSet = new HashSet<Integer>();
			
			//Create new set of possibility elements for the first empty column
			Iterator<Integer> possSetIterator = overBoard[rowEmpty.get(i)][col].possSet.iterator();
			while(possSetIterator.hasNext()) {
				compareSet.add((Integer) possSetIterator.next());
			}
			
			for (int j=0; j<rowEmpty.size();j++) {
				if (j!=i) {
					unionSet.addAll(overBoard[rowEmpty.get(j)][col].possSet);
				}
			}
			compareSet.removeAll(unionSet);
		}
	}
	
	public static void printBox(int row, int col, Box[][] overBoard) {
		int rowMid = getBoxCenter(row);
		int colMid = getBoxCenter(col);
		
		for (int i=rowMid-1; i<rowMid+2; i++) {
			for (int j=colMid-1; j<colMid+2; j++) {
				System.out.print(" " + overBoard[i][j].getValue());
			}
			System.out.println();
		}
		
		
	}

	public static String boardToString(Box[][] overBoard) {
		String output = "";
		for (int i=0; i<9; i++) {
			for (int j=0; j<9; j++) {
				output += overBoard[i][j].getValue();
			}
		}
		return output;
	}

}
