package fall2018.csc2017.gamecentre.slidingtiles;

import android.support.v7.app.AppCompatActivity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fall2018.csc2017.gamecentre.ParentBoard;
import fall2018.csc2017.gamecentre.ParentManager;

/**
 * Manage a board, including swapping tiles, checking for a win, and managing taps.
 */
public class BoardManager extends ParentManager {

    /**
     * Manage a board that has been pre-populated.
     *
     * @param board the board
     */
    public BoardManager(Board board) {
        super(board);
    }

    /**
     * Manage a new shuffled board.
     *
     * @param rows the number of rows of the board
     * @param cols the number of cols of the board
     */
    public BoardManager(int rows, int cols) {

        //final int numTiles = rows * cols;
        super(rows, cols);
        List<Tile> tiles = new ArrayList<>();
        for (int tileNum = 0; tileNum != numTiles; tileNum++) {
            tiles.add(new Tile(tileNum, rows));
        }

        Collections.shuffle(tiles);
        this.board = new Board(tiles, rows, cols);
        saveMove();
    }

    /**
     * Return whether the tiles are in row-major order.
     *
     * @return whether the tiles are in row-major order
     */
    public boolean puzzleSolved() {
        boolean solved = true;
        int correct = 1;
        for (Tile curr : (Board) getBoard()) {
            if (curr.getId() != correct)
                solved = false;
            correct++;
        }
        return solved;
    }

    /**
     * Return whether any of the four surrounding tiles is the blank tile.
     *
     * @param position the tile to check
     * @return whether the tile at position is surrounded by a blank tile
     */
    public boolean isValidTap(int position) {

        int row = position / board.getNumRows();
        int col = position % board.getNumCols();
        int blankId = board.numTiles();
        // Are any of the 4 the blank tile?
        Tile above = row == 0 ? null : ((Board) board).getTile(row - 1, col);
        Tile below = row == board.getNumRows() - 1 ? null : ((Board) board).getTile(row + 1, col);
        Tile left = col == 0 ? null : ((Board) board).getTile(row, col - 1);
        Tile right = col == board.getNumCols() - 1 ? null : ((Board) board).getTile(row, col + 1);
        return (below != null && below.getId() == blankId)
                || (above != null && above.getId() == blankId)
                || (left != null && left.getId() == blankId)
                || (right != null && right.getId() == blankId);
    }

    /**
     * Process a touch at position in the board, swapping tiles as appropriate.
     *
     * @param position the position
     */
    public void touchMove(int position) {

        int row = position / board.getNumRows();
        int col = position % board.getNumCols();
        int blankId = board.numTiles();

        if (isValidTap(position)) {
            int above = (row == 0) ? -1 : ((Board) board).getTile(row - 1, col).getId();
            int below = (row == (board.getNumRows() - 1)) ? -1 : ((Board) board).getTile(row + 1, col).getId();
            int left = (col == 0) ? -1 : ((Board) board).getTile(row, col - 1).getId();
            int right = (col == (board.getNumCols() - 1)) ? -1 : ((Board) board).getTile(row, col + 1).getId();
            int[] iter = {above, below, left, right};
            for (int i = 0; i < 4; i++)
                if (iter[i] == blankId) {
                    int new_row = (i < 2) ? (row - 1) + (2 * i) : row;
                    int new_col = (i >= 2) ? (col - 1) + (2 * (i % 2)) : col;
                    ((Board) board).swapTiles(row, col, new_row, new_col);
                    saveMove();
                    new Autosave(moves_list);

                }
        }

    }
}