package fall2018.csc2017.gamecentre.simon;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;

import fall2018.csc2017.gamecentre.ParentBoard;
import fall2018.csc2017.gamecentre.slidingtiles.Score;

/**
 * The sliding tiles board.
 */
public class SimonBoard extends ParentBoard implements Iterable<Tile> {

    /**
     * The tiles on the board in row-major order.
     */
    public Tile[] tiles;

    /**
     * A new board of tiles in row-major order.
     * Precondition: len(tiles) == NUM_ROWS * NUM_COLS
     *
     * @param tiles the tiles for the board
     */
    public SimonBoard(List<Tile> tiles) {
        super(0, 2, 2);
        this.tiles = new Tile[4];
        Iterator<Tile> iter = tiles.iterator();

        for (int pos = 0; pos != 4; pos++) {
            this.tiles[pos] = iter.next();
        }
    }

    /**
     * A new board of tiles in row-major order.
     * Precondition: len(tiles) == NUM_ROWS * NUM_COLS
     *
     * @param tiles the tiles for the board
     */
    public SimonBoard(Tile[] tiles) {
        super(0, 2, 2);
        this.tiles = tiles;
    }

    /**
     * Return the tile at pos
     *
     * @param pos the tile position
     * @return the tile at (row, col)
     */
    public Tile getTile(int pos) {
        return tiles[pos];
    }

    /**
     * Notify observers on user touch.
     */
    void userTouchNotify() {
        setChanged();
        notifyObservers();
    }

    /**
     * Flash (activate) the tile at given position.
     *
     * @param pos the position of the tile to be activated
     */
    public void activateTile(int pos, boolean playerTurn) {
        tiles[pos].setBackground(pos + 4);
        if (playerTurn) {
            setChanged();
            notifyObservers();
        }
    }

    /**
     * Deactivate the tile at given position.
     *
     * @param pos the position of the tile to be deactivated
     */
    void deactivateTile(int pos) {
        tiles[pos].setBackground(pos);
    }

    /**
     * Return the array of Tile tiles.
     *
     * @return the array of Tile tiles.
     */
    public Tile[] getArrayTiles() {
        return tiles;
    }

    /**
     * Return a string representation of tiles in row-major order
     *
     * @return a string representation of tiles in row-major order
     */
    @Override
    public String toString() {
        return "SimonBoard{" +
                "tiles=" + Arrays.toString(tiles) +
                '}';
    }

    /**
     * Returns an iterator over elements of type Tile.
     *
     * @return an Iterator.
     */
    @NonNull
    @Override
    public Iterator<Tile> iterator() {
        return new BoardIterator();
    }

    /**
     * An iterator over a board in row-major order.
     */
    private class BoardIterator implements Iterator<Tile> {
        /**
         * Tracks the value of the next index.
         */
        int nextIndex = 0;

        /**
         * Returns true if the next index is not the equal the number of tiles
         *
         * @return true if iteration has more elements
         */
        @Override
        public boolean hasNext() {
            return nextIndex != numTiles();
        }

        /**
         * Returns the next tile in row-major order
         *
         * @return the next tile in row-major order
         */
        @Override
        public Tile next() {
            Tile result = SimonBoard.this.getTile(nextIndex);
            nextIndex++;
            return result;
        }
    }
}