package fall2018.csc2017.gamecentre;

import android.support.v7.app.AppCompatActivity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class ParentManager extends AppCompatActivity implements Serializable {
    /**
     * The board being managed.
     */
    protected ParentBoard board;
    /**
     * Storing the moves made.
     */
    protected ArrayList<ParentBoard> moves_list = new ArrayList<>();
    /**
     * The number of tiles on the board.
     */
    protected int numTiles;

    /**
     * Constructor to create a new instance of ParentManager
     *
     * @param rows the number of rows
     * @param cols the number of columns
     */
    public ParentManager(int rows, int cols) {
        numTiles = rows * cols;
    }

    /**
     * Constructor to create a new instance of ParentManager given a board
     *
     * @param board the board to bet set
     */
    public ParentManager(ParentBoard board) {
        this.board = board;
        saveMove();
    }

    /**
     * Save the move made
     */
    public void saveMove() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(this.board);
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            moves_list.add((ParentBoard) ois.readObject());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Return if the puzzle has been solved.
     *
     * @return True if puzzle is solved.
     */
    protected abstract boolean puzzleSolved();

    /**
     * Return whether any of the four surrounding tiles is the blank tile.
     *
     * @param position the tile to check
     * @return whether the tile at position is surrounded by a blank tile
     */
    protected abstract boolean isValidTap(int position);

    /**
     * Remove the current board from moves_list.
     * Consequently, change board to the last board.
     */
    public void removeMove() {
        if (moves_list.size() != 1) {
            moves_list.remove(moves_list.size() - 1);
            int index = moves_list.size();
            this.board = this.moves_list.get(index - 1);
        }
    }

    /**
     * Process a touch at position in the board, swapping tiles as appropriate.
     *
     * @param position the position
     */
    protected abstract void touchMove(int position);

    /**
     * Return the current board.
     */
    public ParentBoard getBoard() {
        return this.board;
    }

    /**
     * Set the current board
     *
     * @param board The new board.
     */
    public void setBoard(ParentBoard board) {
        this.board = board;
    }

    /**
     * Return the list of boards in moves_list.
     *
     * @return moves_list
     */
    public ArrayList<ParentBoard> getMovesList() {
        return moves_list;
    }
}
