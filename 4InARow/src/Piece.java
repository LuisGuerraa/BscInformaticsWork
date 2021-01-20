/**
 * Created by David on 16/12/2016.
 */
import isel.leic.pg.Console;

import java.awt.event.KeyEvent;

public class Piece {
    public static final int        // Piece dimensions (3x3 ; 2x2 ; 3x2 ... )
            HEIGHT = 3, WIDTH = 3;
    public static final int     // Colors of pieces for each player
            PLAYER1_COLOR = Console.BLUE, PLAYER2_COLOR = Console.RED;
    private static final int FALL_STEP = 35; // 75 milliseconds second by step
    public static int[] positions = new int[Board.DIM_COLS];        // Array to returns available spaces on the column
    public static int[][] grid = new int[Board.DIM_LINES][Board.DIM_COLS];      // 2D array to store the corresponding piece of the player on determined coordinates
    public static int startAsWinner = 0;          // Start by the last winner

    // The piece state
    private final boolean player;     // Current player (false-1 , true-2) [can not be changed]
    private int line;                 // Line in Board coordinates (-1 above the board)
    private int column;               // Column in Board coordinates

    public static Piece last;                       // Reference to the last piece placed in board

    // Only current piece may be falling
    private static boolean falling;                 // If is moving in the column
    private static long nextStepTime;               // Time to next step in piece falling

    /**
     * Build a piece for a player in the indicated position.
     *
     * @param lin    The line of start position (0 .. Board.DIM_LINES-1) or -1 if is in the top
     * @param col    The column of start position (0 .. Board.DIM_COLS-1)
     * @param player The player (false-1, true-2)
     */
    public Piece(int lin, int col, boolean player) {
        this.player = player;       // this.player refers the class instantiate and player refers the parameter of the method Piece
        line = lin;
        column = col;
        falling = false;
    }

    /**
     * Build a piece for a player in the top of the board aligned with the indicated column.
     *
     * @param player The player (false-1, true-2)
     * @param col    The column of start position (0 .. Board.DIM_COLS-1)
     */
    public Piece(boolean player, int col) {
        this(-1, col, player);      // "this" refers the constructor above described
        falling = false;         // not falling
        show();                  // show the piece in the top of board
    }

    /**
     * Returns the current line.
     *
     * @return current line (0 .. Board.DIM_LINES-1)
     */
    public int getLine() {
        return line;
    }

    /**
     * Returns the current column.
     *
     * @return current column (0 .. Board.DIM_COLS-1)
     */
    public int getColumn() {
        return column;
    }

    /**
     * Returns true if it is falling
     *
     * @return
     */
    public boolean isFalling() {
        return Piece.falling;
    }

    /**
     * Shows the piece in its current position.<br/>
     * The interior is filled with 'V' if the piece is on the top or with '*' if it was last placed on the grill.
     */
    public void show() { // Show the piece in the current position
        char inside = (line == -1) ? 'V' : (this == last) ? '*' : ' ';
        int viewL = Board.toViewLin(line);
        int viewC = Board.toViewCol(column);
        Console.color(Console.BLACK, player ? PLAYER2_COLOR : PLAYER1_COLOR);
        for (int l = 0; l < HEIGHT; l++, viewL++) {
            Console.cursor(viewL, viewC);
            for (int c = 0; c < WIDTH; c++) Console.print(inside);
        }
    }

    /**
     * Hide the piece in its current position.
     */
    public void hide() {  // Hide the pice in the current position
        int viewL = Board.toViewLin(line);
        int viewC = Board.toViewCol(column);
        Console.setBackground(Console.BLACK);
        for (int l = 0; l < HEIGHT; l++, viewL++) {
            Console.cursor(viewL, viewC);
            for (int c = 0; c < WIDTH; c++) Console.print(' ');
        }
    }

    /**
     * Move the piece to the left column, if possible
     * Hides it in the previous position and shows it in the new position
     */
    public void jumpLeftColumn() {
        if (!falling && column > 0) move(0, -1);
    }

    /**
     * Move the piece to the right column, if possible
     * Hides it in the previous position and shows it in the new position
     */
    public void jumpRightColumn() {
        if (!falling && column < Board.DIM_COLS - 1) move(0, +1);
    }

    /**
     * Mark the piece as falling.<br/>
     * NOTE: Method stepFall() must be called repeatedly during fall.
     *
     * @see #stepFall()
     */
    public void startFall() {
        if (falling) return;
        if(positions[getColumn()]==Board.DIM_LINES+1)falling = false;
        else falling = true;
        nextStepTime = System.currentTimeMillis() + FALL_STEP;  // Sets the next move down time
    }

    /**
     * Make a step of the movement of the fall, if the right time is reached.<br/>
     * Returns false if the current piece is falling but can not fall any more.
     *
     * @return true if the method should continue to be called.
     */
    public boolean stepFall() {
        if (!falling) return true;          // Prevents a possible call without the piece to be falling.
        if (line >= (Board.DIM_LINES - positions[getColumn()])) {        // Condition to stop fall  [TO CHANGE]
            if (positions[getColumn()] <= Board.DIM_LINES) {
                if (!player) {                       // Write Pieces on Board
                    writeNumberPieces(1);       // When player 1 makes the move, program will write on the Board +1 piece moved from player 1
                } else {
                    writeNumberPieces(2);       // When player 2 makes the move, program will write on the Board +1 piece moved from player 2
                }
                positions[getColumn()]++;       // This array is the positions of the pieces. After one of the players drops the piece on determinated column, the next play of that column is going to be less 1
            }
            if (!checkHorizontal(grid,1)){      // Program will check if player 1 made N in line (horizontally)
                playerVictory(1, "horizontal");     // If Player 1 made it means he won and the program will write a message saying "Player 1 won in horizontal" and add 1 victory of Player 1 to scoreboard
            } else if (!checkHorizontal(grid,2)){
                playerVictory(2, "horizontal");
            } else if (!checkVertical(grid,1)){     // Program will check if player 1 made N in column (vertically)
                playerVictory(1, "vertical  ");     // If Player 1 made it means he won and the program will write a message saying "Player 1 won in vertical" and add 1 victory of Player 1 to scoreboard
            } else if (!checkVertical(grid,2)){
                playerVictory(2, "vertical  ");
            } else if (!checkDiagonalBack(grid,1)){     // Program will check if player 1 made N in back diagonal
                playerVictory(1, "diagonal");       // If Player 1 made it means he won and the program will write a message saying "Player 1 won in diagonal" and add 1 victory of Player 1 to scoreboard
            } else if (!checkDiagonalBack(grid,2)){
                playerVictory(2, "diagonal");
            } else if (!checkDiagonalForward(grid,1)){      // Program will check if player 1 made N in forward diagonal
                playerVictory(1, "diagonal");
            } else if (!checkDiagonalForward(grid,2)){
                playerVictory(2, "diagonal");
            } else if (!checkTied(grid)){       // Program will check if the board is completely full and if it is it means it's a tie.
                Board.writeOnBoard(Board.VIEW_LINES-8,Board.VIEW_COLS+2,5, 11, "     Tied Game      ");
                Board.pressEnter();         // Program is waiting for the user presses Enter bottom
            }
            return false;
        }
        if (System.currentTimeMillis() >= nextStepTime) { // The right time?
            move(1, 0);// Move to next line
            nextStepTime += FALL_STEP;      // Set next time to move the piece
        }
        return true;
    }

    /**
     * writeNumberPiece method stores the numbers of pieces that each player played.
     * @param player is used to distinguish each player the program will write on board.
     */

    private void writeNumberPieces(int player) {
        fix();          // When program writes +1 move of the that player, the piece he played is going to be fixed and that means the interior of that piece will be full of "*"
        Console.color(Console.YELLOW, Console.DARK_GRAY);
        if (player == 1) {
            Console.cursor(HEIGHT + 2, Board.VIEW_COLS + 9);
            InLine.CountB++;        // When player 1 plays, the pieces that he played will be incremented on the board.
            if (InLine.CountB < 10)     // If the pieces that he played is less than 10, the board will adjust the numbers in he center
                Console.print(" " + InLine.CountB + " ");
            else if (InLine.CountB >= 10)        // If the pieces that he played is more than 10, the board will adjust the numbers in the spaces
                Console.print(InLine.CountB + " ");
            insertPiece(1, line, column);       // The coordinates of the piece that player 1 made will be inserted on 2D array that will be used afterwards.
        } else if (player == 2) {
            Console.cursor(HEIGHT + 2, Board.VIEW_COLS + 12);
            InLine.CountR++;
            if (InLine.CountR < 10)
                Console.print(" " + InLine.CountR + " ");
            else if (InLine.CountB >= 10 && InLine.CountB < 100)
                Console.print(InLine.CountR + " ");
            insertPiece(2, line, column);
        }
    }

    /**
     * playerVictory method is used when one of the players won the game and so will increment his number of victories and write it on the board.
     * @param player is used to distinguish which player the program will write on board.
     * @param text is used to indicate the way that player won (Vertically, Horizontally or Diagonally).
     */

    private void playerVictory(int player, String text) {
        if (player == 1){
            Board.PWon(false,text);     // After program detects that one of the players won the game, PWon() method is called to write on board saying the player X won the game
            InLine.WinB++;      // The variable victories of player 1 (Blue) will be incremented and afterwards printed on the board.
            startAsWinner=1;        // The next game will start as Player 1
            Board.writeOnBoard(HEIGHT+1,Board.VIEW_COLS+9, 5, 11, " " + InLine.WinB + " "); // Write the victories that player 1 has
        } else if (player == 2){
            Board.PWon(true,text);
            InLine.WinR++;
            startAsWinner=2;
            Board.writeOnBoard(HEIGHT + 1, Board.VIEW_COLS+12, 5, 11, " " + InLine.WinR + " ");
        }
    }

    /**
     * checkTied method is going to verify if the board is completely full and if it is, it's a draw.
     * @param grid is a 2D array that verifies on line=0 if the columns are being used by the players.
     * @return flag is going to return back if it's a tie (false in this case) or not (true in this case).
     */

    private boolean checkTied(int grid[][]) {
        boolean flag = true;
        int count = 0;
        for (int cols = 0; cols < Board.DIM_COLS; cols++) {
            if (grid[0][cols]!=0)
                count++;
            else if (grid[0][cols]==0)
                count=0;
        }
        if (count >= Board.DIM_COLS)
            flag = false;
        return flag;
    }

    /**
     * Stops the falling movement and shows the piece as the last one placed on the grid.
     */
    public void fix() {
        falling = false;                    // Mark as stop.
        Piece previous = last;              // The previous last piece
        last = this;                        // The actual last piece
        if (previous != null)
            previous.show();                // Show previous as normal piece
        if (getLine() != -1) {
            last.show();                        // Show current as the last
        }
    }

    private void move(int dLin, int dCol) {
        hide();
        column += dCol;
        line += dLin;
        show();
    }

    public void insertPiece(int player, int line, int column) {
        grid[line][column] = player;        // When player makes the move it will be stored on determined line and column
    }

    /**
     * checkHorizontal method is going to be called to check if one of the players won horizontally.
     * @param grid is a 2D array that is being used to compare the current piece to the neighborhood pieces.
     * @param player is used to compare which player this method is going to compare.
     * @return flag is going to return back if the player won in horizontal (false in this case) or not (true in this case).
     */

    public boolean checkHorizontal(int grid[][], int player) {
        boolean flag = true;        // The flag starts as true and when program detects that one of the players won the game it automatically changes to false.
        int count = 1;      // Variable count starts as 1 because of the current piece placed on the board
        int colsleft = ((getColumn() - InLine.N + 1)>=0) ? (getColumn() - InLine.N + 1) : 0; // Variable indicating whether the leftmost piece exceeds the grid (if N=4 then the leftmost piece is the current piece - 3). If it exceeds the grid the variable starts as 0
        int colsright = ((getColumn() + InLine.N - 1)>Board.DIM_COLS-1) ? Board.DIM_COLS-1 : (getColumn() + InLine.N - 1);      // Variable indicating whether the rightmost piece exceeds the grid (if N=4 then the rightmost piece is the current piece + 3). If it exceeds the grid the variable starts as Board_DIM_COLS-1
        if (player == grid[getLine()][getColumn()]){       // Checks if the player that needs to be checked is the same as that found in the 2D array in the current positions
            //Check Behind current Piece
                for (int col = getColumn()-1; col >= colsleft; col--) {
                    if (grid[getLine()][getColumn()] == grid[getLine()][col])       // If the current player is the same as the pieces on the left it increments the counter.
                        count++;
                    else
                        break;
                }
                if (count >= InLine.N) {    // When counter reaches N it means that player won the game
                    flag = false;
                }
            //Check After current Piece
                for (int col = getColumn() + 1; col <= colsright; col++) {
                    if (grid[getLine()][getColumn()] == grid[getLine()][col])       // If the current player is the same as the pieces on the right it increments the counter.
                        count++;
                    else
                        break;
                }
                if (count >= InLine.N) {
                    flag = false;
                }
        }
        return flag;
    }

    /**
     * checkVertical method is going to be called to check if one of the players won vertically.
     * @param grid is a 2D array that is being used to compare the current piece to the neighborhood pieces.
     * @param player is used to compare which player this method is going to compare.
     * @return flag is going to return back if the player won in horizontal (false in this case) or not (true in this case).
     */

    public boolean checkVertical(int grid[][], int player) {
        boolean flag = true;
        int count = 1, lin;     // Variable lin gets the line of next element of the grid to compare to the current piece
        if (player == grid[getLine()][getColumn()]){
            for (int i = InLine.N-1; i > 0; i--) {
                lin = getLine()+InLine.N-i;
                if (lin < Board.DIM_LINES)      // If the variable lin exceeds the grid of the board, the array cannot compare to the next line
                    if (grid[getLine()][getColumn()] == grid[lin][getColumn()])
                        count++;
                if (count >= InLine.N) {        // When counter reaches N, the flag return as false and player won the game.
                    flag = false;
                    break;
                }
            }
        }
        return flag;
    }

    /**
     * checkDiagonalBack method is going to be called to check if one of the players won on diagonal "\".
     * @param grid is a 2D array that is being used to compare the current piece to the neighborhood pieces.
     * @param player is used to compare which player this method is going to compare.
     * @return flag is going to return back if the player won in horizontal (false in this case) or not (true in this case).
     */

    public boolean checkDiagonalBack(int grid[][], int player){
        boolean flag = true;
        int count = 1, col, lin;        // Variable col and lin gets the column and the line of next element of the grid to compare to the current piece
        if (player == grid[getLine()][getColumn()]){
            //Check Behind the current piece
                for (int i = InLine.N-1; i > 0; i--) {
                    col = getColumn()-InLine.N+i;       // col assumes the value of column and runs to the left of the current piece from minus N-1 to 1
                    lin = getLine()-InLine.N+i;         // lin assumes the value of line and runs to the left of the current piece from minus N-1 to 1
                    if (col >= 0 && lin >= 0)
                        if (grid[getLine()][getColumn()] == grid[lin][col])
                            count++;
                    if (count >= InLine.N) {
                        flag = false;
                        break;
                    }
                }
            //Check After the current piece
                for (int i = InLine.N-1; i > 0; i--) {
                    col = getColumn()+InLine.N-i;       // col assumes the value of column and runs to the right of the current piece from minus N-1 to 1
                    lin = getLine()+InLine.N-i;     // lin assumes the value of line and runs to the right of the current piece from minus N-1 to 1
                    if (col < Board.DIM_COLS && lin < Board.DIM_LINES)
                        if (grid[getLine()][getColumn()] == grid[lin][col])
                            count++;
                    if (count >= InLine.N) {
                        flag = false;
                        break;
                    }
                }
        }
        return flag;
    }

    /**
     * checkDiagonalForward method is going to be called to check if one of the players won on diagonal "/".
     * @param grid is a 2D array that is being used to compare the current piece to the neighborhood pieces.
     * @param player is used to compare which player this method is going to compare.
     * @return flag is going to return back if the player won in horizontal (false in this case) or not (true in this case).
     */

    public boolean checkDiagonalForward(int grid[][], int player) {
        boolean flag = true;
        int count = 1, col, lin;
        if (player == grid[getLine()][getColumn()]) {
            //Check Behind current piece
                for (int i = InLine.N - 1; i > 0; i--) {
                    col = getColumn() - InLine.N + i;
                    lin = getLine() + InLine.N - i;
                    if (col >= 0 && lin < Board.DIM_LINES)
                        if (grid[getLine()][getColumn()] == grid[lin][col])
                            count++;
                    if (count >= InLine.N) {
                        flag = false;
                        break;
                    }
                }
            //Check After current piece
                for (int i = InLine.N - 1; i > 0; i--) {
                    col = getColumn() + InLine.N - i;
                    lin = getLine() - InLine.N + i;
                    if (col < Board.DIM_COLS && lin >= 0)
                        if (grid[getLine()][getColumn()] == grid[lin][col])
                            count++;
                    if (count >= InLine.N) {
                        flag = false;
                        break;
                    }
                }
        }
        return flag;
    }
}