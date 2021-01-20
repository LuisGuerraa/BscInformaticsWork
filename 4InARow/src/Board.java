import isel.leic.pg.Console;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Board {
	public static final int  // Board dimensions (N=4 -> 6x7 ; N=3 -> 5x5 ; ...)
		    DIM_LINES = InLine.N*2-InLine.N/2, DIM_COLS = InLine.N*2-1;
	public static final int  // Visual dimensions in console coordinates
            VIEW_LINES = DIM_LINES * (Piece.HEIGHT+1) +1,
            VIEW_COLS = DIM_COLS * (Piece.WIDTH+1) +1;
	private static final int  // Visual positions in console coordinates
			VIEW_TOP = Piece.HEIGHT, VIEW_LEFT = 1;
	private static final int
			GRID_COLOR = Console.BROWN;

	/**
	 * Draws the entire grid without pieces
	 */
	public static void drawGrid() {
		Console.cursor(VIEW_TOP,VIEW_LEFT);
		drawDivHorizontal();					// First horizontal division. Top of the first row of holes.
		for (int l = 0; l < DIM_LINES; l++) {
			int h;
			for (h = 0; h < Piece.HEIGHT; h++) {// Each line of holes
				Console.cursor(VIEW_TOP+l*(Piece.HEIGHT+1)+h+1, VIEW_LEFT);
				drawDivVertical();
			}
			Console.cursor(VIEW_TOP+l*(Piece.HEIGHT+1)+h+1, VIEW_LEFT);
			drawDivHorizontal();				// Base of each row of holes
		}
		for (int i = 0; i < DIM_COLS; i++) {
			Piece.positions[i]=1;
		}
        for (int i = 0; i < DIM_LINES; i++) {
			for (int j = 0; j < DIM_COLS; j++) {
				Piece.grid[i][j]=0;
			}
		}
		// Write Player
		writeOnBoard(Piece.HEIGHT-1,VIEW_COLS+9, 1, 0, "Player");
		// Write Player 1 and 2
		writeOnBoard(Piece.HEIGHT,VIEW_COLS+9, 1, 4, " 1 ");
		writeOnBoard(Piece.HEIGHT,VIEW_COLS+12, 1, 2, " 2 ");
		// Write Games
		writeOnBoard(Piece.HEIGHT+1,VIEW_COLS+3, 1, 0, "Games:");
		writeOnBoard(Piece.HEIGHT+1,VIEW_COLS+9, 5, 11, " " + InLine.WinB + " ");
		writeOnBoard(Piece.HEIGHT+1,VIEW_COLS+12, 5, 11, " " + InLine.WinR + " ");
		// Write Pieces
		writeOnBoard(Piece.HEIGHT+2,VIEW_COLS+2, 1, 0, "Pieces:");
		writeOnBoard(Piece.HEIGHT+2,VIEW_COLS+9, 5, 11, " " + InLine.CountB + " ");
		writeOnBoard(Piece.HEIGHT+2,VIEW_COLS+12, 5, 11, " " + InLine.CountR + " ");
		// Instructions
		writeOnBoard(VIEW_LINES-4,VIEW_COLS+2, 1, 11, " Left- Move to left ");
		writeOnBoard(VIEW_LINES-3,VIEW_COLS+2, 1, 11, "Right- Move to right");
		writeOnBoard(VIEW_LINES-2,VIEW_COLS+2, 1, 11, " Down- Drop piece   ");
		writeOnBoard(VIEW_LINES-1,VIEW_COLS+2, 1, 11, "Space- Drop piece   ");
		writeOnBoard(VIEW_LINES,VIEW_COLS+2, 1, 11, "  Esc- Terminate    ");
		writeOnBoard(VIEW_LINES+1,VIEW_COLS+2, 1, 11, "   Up- Undo last    ");
		writeOnBoard(VIEW_LINES+2,VIEW_COLS+2, 1, 11, "   F2- Auto 2 level ");
		// Write PG Grupo 6
		writeOnBoard(VIEW_LINES+3,VIEW_COLS+2, 1, 0, "          PG Grupo 6");
	}

	public static void writeOnBoard(int line, int column, int corF, int corB, String text) {
		Console.cursor(line,column);
		Console.color(corF,corB);
		Console.print(text);
	}

	public static void exiting() {
		writeOnBoard(VIEW_LINES-8,VIEW_COLS+2,5, 11, "     Exit Game      ");
		writeOnBoard(VIEW_LINES-6,VIEW_COLS+2,1, 11, "Confirm (Y/N)? ");
	}

    /**
     * clean method is called when it is necessary to clear the information given on the board.
     */

	public static void clean() {
		Console.cursor(VIEW_LINES-8,VIEW_COLS+2);
		Console.setBackground(Console.BLACK);
		for (int i = 0; i < 20; i++) Console.print(' ');
        Console.cursor(VIEW_LINES-7,VIEW_COLS+2);
        for (int i = 0; i < 21; i++) Console.print(' ');
		Console.cursor(VIEW_LINES-6,VIEW_COLS+2);
		for (int i = 0; i < 15; i++) Console.print(' ');
	}

	public static void PWon(boolean player, String text) {
		if (!player) {
			writeOnBoard(VIEW_LINES - 8, VIEW_COLS + 2, 5, 11, "     Player 1 won   ");
		}else {
			writeOnBoard(VIEW_LINES - 8, VIEW_COLS + 2, 5, 11, "     Player 2 won   ");
		}
		writeOnBoard(VIEW_LINES-7,VIEW_COLS+2,5, 11, "    in " + text + "   ");
		pressEnter();
	}

	public static void pressEnter(){
		writeOnBoard(VIEW_LINES-6,VIEW_COLS+2,1, 11, "Press enter");
		Console.cursor(VIEW_LINES-6,VIEW_COLS+13);
		Console.cursor(true);           // Cursor is going to blink waiting for response of the user
		char ans = Console.waitChar(30);
		while (ans != KeyEvent.VK_ENTER){
			Console.cursor(true);       // If user doesn't click on Enter bottom, the cursor keeps blinking until he clicks.
			char ans2 = Console.waitChar(9000000);
			if (ans2 == KeyEvent.VK_ENTER){break;}
		}
		clean();
		Console.cursor(false);
		Piece.last = null;      // After player won the game the program will reset the last piece placed on board.
		InLine.CountB = 0;      // The counter of the pieces placed on the board by which player will reset too.
		InLine.CountR = 0;
		InLine.startGame();     // The program will start from the beginning again.
	}

	private static void drawDivHorizontal() {
		Console.color(Console.WHITE,GRID_COLOR);
		Console.print('o');
		for (int c = 0; c < DIM_COLS; c++) {
			for (int w = 0; w < Piece.WIDTH; w++) Console.print('-');
			Console.print('o');
		}
	}

	private static void drawDivVertical() {
		Console.color(Console.WHITE,GRID_COLOR);
		Console.print('|');
		for (int c = 0; c < DIM_COLS; c++) {
			Console.color(Console.WHITE,Console.BLACK);
			for (int w = 0; w < Piece.WIDTH; w++) Console.print(' ');
			Console.color(Console.WHITE,GRID_COLOR);
			Console.print('|');
		}
	}

	/**
	 * Converts the column number in the grid to coordinates in the console where the column begins.
	 * @param col The column number (0..DIM_COLS-1)
	 * @return The Console column.
	 */
    public static int toViewCol(int col) {
        return VIEW_LEFT+col*(Piece.WIDTH+1)+1;
    }

	/**
	 * Converts the line number in the grid to coordinates in the console where the line begins.
	 * @param lin The line number (0..DIM_LINES-1)
	 * @return The Console line.
	 */
	public static int toViewLin(int lin) {
        return VIEW_TOP+lin*(Piece.HEIGHT+1)+1;
    }
}