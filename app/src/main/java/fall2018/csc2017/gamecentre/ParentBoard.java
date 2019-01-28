package fall2018.csc2017.gamecentre;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;

import fall2018.csc2017.gamecentre.slidingtiles.Score;

public class ParentBoard extends Observable implements Serializable, Score {
    /**
     * The number of rows.
     */
    protected int numRows;

    /**
     * The number of rows.
     */
    protected int numCols;

    /**
     * The players score.
     */
    protected int score = 25;

    /**
     * Initialize a new Parent Board using a given score, rows and columns
     *
     * @param score   The current score
     * @param rows    The number of rows for the board.
     * @param columns The number of columns for the board.
     */
    public ParentBoard(int score, int rows, int columns) {
        numRows = rows;
        numCols = columns;
        this.score = score;
    }

    /**
     * Return the number of tiles on the board.
     *
     * @return the number of tiles on the board
     */
    public int numTiles() {
        return numCols * numRows;
    }

    /**
     * Return the number of rows on the board.
     *
     * @return The number of rows.
     */
    public int getNumRows() {
        return this.numRows;
    }

    /**
     * Return the number of columns on the board.
     *
     * @return The number of columns.
     */
    public int getNumCols() {
        return this.numCols;
    }

    /**
     * Return the current score.
     *
     * @return The current score
     */
    @Override
    public int getScore() {
        return this.score;
    }

    /**
     * Set the score.
     *
     * @param score The current score
     */
    @Override
    public void setScore(int score) {
        this.score = score;
    }
}
