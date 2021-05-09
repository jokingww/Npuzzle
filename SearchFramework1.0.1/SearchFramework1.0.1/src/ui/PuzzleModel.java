package ui;

import java.util.Random;

import ui.Point;
import ui.PuzzleFieldStyle;

public class PuzzleModel {
	protected PuzzleFieldStyle[][] board;
	private int boardSize;
	private Point emptyPoint;
	
	private int[] indexX = { 0, -1, 1, 0 };
    private int[] indexY = { 1, 0, 0, -1 };
	
	public PuzzleModel(int size) {
        board = new PuzzleFieldStyle[size][size];
        boardSize = board.length;
        init();
    }
	
	private void init() {
        int colorCounter = 1;
        for (int x = 0; x < boardSize; x++) {
            for (int y = 0; y < boardSize; y++) {
                PuzzleFieldStyle style = new PuzzleFieldStyle(colorCounter, "puzzle-field-style-no"+colorCounter);
                colorCounter++;
                board[x][y] = style;
            }
        }
 
        // One field is empty
        PuzzleFieldStyle style = new PuzzleFieldStyle(0, "puzzle-field-style-no16");
        board[boardSize-1][boardSize-1] = style;
        emptyPoint = new Point(boardSize - 1, boardSize - 1);
 
        //shuffleBoard();
    }
	
	protected void shuffleBoard() {
        // Per definition this is the empty field in the initial state
        // see colorSet4: Last value is null
        //Point emptyFieldPos = new Point(boardSize - 1, boardSize - 1);
 
        Random r = new Random(System.currentTimeMillis());
 
        for (int i = 0; i < 1000; i++) {
            int fieldToMove = r.nextInt(indexX.length);
            if (emptyPoint.getX() + indexX[fieldToMove] >= 0 && emptyPoint.getY() + indexY[fieldToMove] >= 0 && emptyPoint.getX() + indexX[fieldToMove] < boardSize && emptyPoint.getY() + indexY[fieldToMove] < boardSize) {
                Point colorFieldPos = new Point(emptyPoint.getX() + indexX[fieldToMove], emptyPoint.getY() + indexY[fieldToMove]);
                emptyPoint = switchField(colorFieldPos, emptyPoint);
            }
        }
    }
	
	private Point switchField(Point colorFieldPos, Point emptyFieldPos) {
        // Switch with one temp variable was possible, too. But this is
        // better to understand.
        PuzzleFieldStyle coloredField = board[colorFieldPos.getX()][colorFieldPos.getY()];
        PuzzleFieldStyle emptyWhiteField = board[emptyFieldPos.getX()][emptyFieldPos.getY()];
        board[emptyFieldPos.getX()][emptyFieldPos.getY()] = coloredField;
        board[colorFieldPos.getX()][colorFieldPos.getY()] = emptyWhiteField;
        return new Point(colorFieldPos.getX(), colorFieldPos.getY());
    }
	
	public PuzzleFieldStyle getColorAt(int x, int y) {
        if (x < boardSize && x >= 0 && y < boardSize && y >= 0) {
            return board[x][y];
        }
        return null;
    }

	public Point getEmptyPoint() {
		return emptyPoint;
	}
	
	public void setBoard(String[] numbers) {
		int count = 0;
		boardSize=(int) Math.sqrt(numbers.length);
		for(int i=0;i<boardSize;i++) {
			for(int j=0;j<boardSize;j++) {
				int number = Integer.parseInt(numbers[count]);
				PuzzleFieldStyle style;
				if(number==0) {
					style = new PuzzleFieldStyle(number,"puzzle-field-style-no16");
					emptyPoint = new Point(i,j);
				}
				else {
					style = new PuzzleFieldStyle(number, "puzzle-field-style-no"+number);
				}
				
				board[i][j] = style;
				count++;
			}
		}
	}

	public void setEmptyPoint(Point emptyPoint) {
		this.emptyPoint = emptyPoint;
	}
	
	public void printBoard() {
		for(int i=0;i<boardSize;i++) {
			for(int j=0;j<boardSize;j++) {
				System.out.print(board[i][j].getNumber());
			}
			System.out.println();
		}
		System.out.println("emptyPoint x:"+emptyPoint.getX()+" y:"+emptyPoint.getY());
	}
}
