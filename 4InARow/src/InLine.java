/**
 * Created by David on 16/12/2016.
 */
import isel.leic.pg.Console;

import java.awt.event.KeyEvent;

public class InLine {
    public static final int N = 4;           // Number of pieces on a winning line. (3,4,5...)
    public static int CountB = 0, CountR = 0;   // Count the pieces falling
    public static int WinB = 0, WinR = 0;       // Count the victories
    private static final int                 // Console dimensions
            VIEW_LINES = Board.VIEW_LINES + Piece.HEIGHT + 1 , VIEW_COLS = Board.VIEW_COLS + 23;
    // The game state
    private static boolean player = false;    // Current player (false- 1 , true- 2)
    public static Piece piece;               // Current piece (not placed on the board)

    /**
     * Start method of game
     * @param args Not used.
     */
    public static void main(String[] args) {
        init();
        run();        // Loop of the game
        terminate();
    }

    private static void init() {
        Console.open("PG In Line ("+N+')', VIEW_LINES, VIEW_COLS);
        //Console.exit(true);				 // Uncomment to enable exit closing the console
        startGame();
    }

    public static void startGame() {
        Board.drawGrid();				    // Draw initial board
        if(Piece.startAsWinner!=0){return;}     // After one of the players won the game, the program remains with the last player and does not changes to the other player
        piece = new Piece(player, Board.DIM_COLS/2);  // Create first piece
    }

    private static final String GAME_OVER_TXT = "GAME OVER";

    private static void terminate() {
        try {                               // After clicking Escape and Y to exit, the program will show an error "indexOutOfBounds" so the instruction try catch will ommit the error
            int key;
            do {
                Board.exiting();            // Show message to confirm if you really want to exit
                key = Console.getKeyPressed();      // Gets the next key after clicking on Escape
                if (key == KeyEvent.VK_Y) {
                    Board.clean();              // If the key is Y, clean() method will clean the confirmation message (exiting())
                    Console.cursor(VIEW_LINES / 2, (VIEW_COLS - GAME_OVER_TXT.length()) / 2);
                    Console.color(Console.RED, Console.YELLOW);
                    Console.print(GAME_OVER_TXT);        // Message GAME OVER
                    while (Console.isKeyPressed()) ;    // Wait if any key is pressed
                    Console.waitKeyPressed(5000);        // Wait 5 seconds for any key
                    Console.close();                    // Close Console window
                }
                if (key == KeyEvent.VK_N) {
                    Board.clean();          // If the key is N, the program is going to clean the confirmation message (exiting()) and run program again
                    run();
                }
            } while (key != KeyEvent.VK_Y || key != KeyEvent.VK_N);     // If user clicks any key besides Y or N, the program will enter on loop mode until user clicks Y or N
        } catch (NullPointerException e){}
    }

    private static void run() {
        int key;           // Current key
        do {
            key = Console.getKeyPressed();  // Read a key
            if (!piece.stepFall())          // Try move the piece down
                stopFall();                 // Fix the piece
            if (key!=Console.NO_KEY)        // A key was pressed ?
                action(key);	            // Do action for the key
        } while (key != KeyEvent.VK_ESCAPE);  // Key to abort game.
    }

    private static void stopFall() {
        if(Piece.startAsWinner==1){ piece = new Piece(false, Board.DIM_COLS/2); Piece.startAsWinner=0; return;} // After player 1 won the game, the program is going to start a new game with the same player at line = -1 and col at the half of the board and will reset the variable "startAsWinner"
        if(Piece.startAsWinner==2){ piece = new Piece(true, Board.DIM_COLS/2); Piece.startAsWinner=0; return;} // Create other piece
        piece.fix();                        // Fix current piece
        player = !player;                   // Swap player
        piece = new Piece(player, piece.getColumn()); // Create other piece
    }

    private static void action(int key) {
        switch (key) {
            case KeyEvent.VK_LEFT:
                piece.jumpLeftColumn();     // In case of user clicks Left key, the program will jump the piece to the left when line = -1 (line above the Board)
                break;
            case KeyEvent.VK_RIGHT:
                piece.jumpRightColumn();       // In case of user clicks Right key, the program will jump the piece to the right
                break;
            case KeyEvent.VK_SPACE:
            case KeyEvent.VK_DOWN:
                piece.startFall();      // In case of user clicks Space or Down key, the program will make the piece start falling
                break;
        }
        while (Console.isKeyPressed(key)) ;  // Wait to release key
    }
}