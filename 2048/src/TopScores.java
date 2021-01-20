import java.io.*;
import java.util.Scanner;

import static sun.misc.Version.print;

/**
 * Class that manages the table of the best scores.
 * Each instance object of this class is a distinct table.
 * <p>
 * The getRow, canAdd, and addRow methods are marked to be implemented.
 * <p>
 * The saveToFile and restoreFromFile methods are already implemented to write to file
 * and restore from the file the best scores table.
 * Just uncomment the lines marked and implement the Score class.
 */
public class TopScores {
    // File name to save and restore table.
    private static final String FILE_NAME = "top2048.txt";

    // Maximum rows of the table.
    public static final int MAX_SCORES = 5;


    // The table. Each row of the table is an object of Score class.
    private Score[] table = new Score[MAX_SCORES];
    // Number of rows currently used in table.
    private int rows = 0;

    /**
     * The constructor read the table from file.
     */
    public TopScores() {
        restoreFromFile();
    }


    /**
     * Returns one row of the table.
     *
     * @param idx Index of the row ( 0 .. getNumOfRows() )
     * @return The row. One object of the class Score
     */
    public Score getRow(int idx) { //ja ta
        return table[idx];
    } //linha da tabela

    /**
     * Returns the number of rows currently used in table.
     *
     * @return a value between 0 and MAX_SCORES
     */
    public int getNumOfRows() {
        return rows;
    }

    /**
     * Check the possibility to add the score to the record table.
     *
     * @param score The score to try add
     * @return true if the table is not full or the score is greater than the last stored
     */
    public boolean canAdd(int score) { //condiçoes de adesão á tabela de resultados
        return rows < MAX_SCORES || table[MAX_SCORES - 1].getPoints() < score;
    }

    /**
     * Inserts one more row in the table.
     * The table should be sorted in descending order of the score.
     *
     * @param name  of the player
     * @param score
     * @return true if is inserted.
     * pre- condition
     */
    public boolean addRow(String name, int score) { //INSERTION SORT ja ta
        if(!canAdd(score)) return false;
        if (rows < MAX_SCORES) ++rows;
        int i = rows - 1;
        for (; i > 0 && table[i - 1].getPoints() < score; --i) {
            table[i] = table[i - 1];
        }
        table[i] = new Score(name, score);
        return true;
    }


    /**
     * Load the table of best scores from a text file.
     */

    private void restoreFromFile() {
        try { // Read table from file
            Scanner in = new Scanner(new FileReader(FILE_NAME));
            for (; in.hasNextLine(); ++rows) {
                int points = in.nextInt();
                String name = in.nextLine().trim();
                table[rows] = new Score(name, points);  //ja ta TODO: UNCOMMENT to use Score constructor
            }
            in.close();
        } catch (Exception e) {
            System.out.println("Error reading file " + FILE_NAME);
        }
    }

    /**
     * Save the table of best scores in a text file.
     */
    public void saveToFile() {
        try {
            PrintWriter out = new PrintWriter(new FileWriter(FILE_NAME));
            for (int i = 0; i < rows; i++) {
                out.println(table[i].getPoints() + " " + table[i].getName());  // ja ta TODO: UNCOMMENT to use fields of Score
            }
            out.close();
        } catch (Exception e) {
            System.out.println("Error writing file " + FILE_NAME);
        }
    }
}