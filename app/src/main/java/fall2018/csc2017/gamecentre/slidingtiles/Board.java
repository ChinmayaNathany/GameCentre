package fall2018.csc2017.gamecentre.slidingtiles;

import android.support.annotation.NonNull;

import java.util.Observable;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import fall2018.csc2017.gamecentre.ParentBoard;

/**
 * The sliding tiles board.
 */
public class Board extends ParentBoard implements Iterable<Tile> {

    /**
     * The tiles on the board in row-major order.
     */
    private Tile[][] tiles;

    /**
     * A new board of tiles in row-major order.
     * Precondition: len(tiles) == NUM_ROWS * NUM_COLS
     *
     * @param tiles the tiles for the board
     */
    public Board(List<Tile> tiles, int rows, int columns) {
        super(250, rows, columns);
        this.tiles = new Tile[numRows][numCols];
        Iterator<Tile> iter = tiles.iterator();

        for (int row = 0; row != numRows; row++) {
            for (int col = 0; col != numCols; col++) {
                this.tiles[row][col] = iter.next();
            }
        }
    }

    public Board(Tile[][] tiles) {
        super(250, tiles.length, tiles.length);
        this.tiles = tiles;
    }

    /**
     * Return the tile at (row, col)
     *
     * @param row the tile row
     * @param col the tile column
     * @return the tile at (row, col)
     */
    public Tile getTile(int row, int col) {
        return tiles[row][col];
    }

    /**
     * return the location of a particular tile given its id number
     */
    public int[] getLocation(int id) {
        int[] coordinate;
        coordinate = new int[2];
        for (int row = 0; row != numRows; row++) {
            for (int col = 0; col != numCols; col++) {
                if (tiles[row][col].getId() == id) {
                    coordinate[0] = row;
                    coordinate[1] = col;
                }
            }
        }
        return coordinate;
    }

    /**
     * return the total number of inversions
     */
    public int getInversions() {

        int sum = 0;
        for (int row = 0; row != numRows; row++) {
            for (int col = 0; col != numCols; col++) {
                if (tiles[row][col].getId() != numRows * numCols) {
                    sum += compareAfter(tiles[row][col]);
                }
            }
        }
        return sum;
    }

    /**
     * return the number of inversions for a particular tile
     */
    public int compareAfter(Tile t) {

        int inversion = 0;
        for (int row = this.getLocation(t.getId())[0]; row != numRows; row++) {
            if (row == this.getLocation(t.getId())[0]) {
                for (int col = this.getLocation(t.getId())[1]; col != numCols; col++) {
                    if (t.getId() > tiles[row][col].getId()
                            && tiles[row][col].getId() != numRows * numCols) {
                        inversion += 1;
                    }
                }
            } else {
                for (int col = 0; col != numCols; col++) {
                    if (t.getId() > tiles[row][col].getId()
                            && tiles[row][col].getId() != numRows * numCols) {
                        inversion += 1;
                    }
                }
            }
        }
        return inversion;
    }

    /**
     * Swap the tiles at (row1, col1) and (row2, col2)
     *
     * @param row1 the first tile row
     * @param col1 the first tile col
     * @param row2 the second tile row
     * @param col2 the second tile col
     */
    public void swapTiles(int row1, int col1, int row2, int col2) {
        Tile temp = getTile(row1, col1);
        tiles[row1][col1] = getTile(row2, col2);
        tiles[row2][col2] = temp;

        if (this.score > 1) {
            this.score--;
        }

        setChanged();
        notifyObservers();
    }

    /**
     * Swap tiles list with newTiles for the undo functionality.
     *
     * @param newTiles the new array of tiles to swap
     */
    void swapTileList(Tile[][] newTiles) {
        this.tiles = newTiles;

        setChanged();
        notifyObservers();
    }

    /**
     * Return the array of Tile tiles.
     *
     * @return the array of Tile tiles.
     */
    Tile[][] getArrayTiles() {
        return tiles;
    }

    /**
     * Return a string representation of tiles in row-major order
     *
     * @return a string representation of tiles in row-major order
     */
    @Override
    public String toString() {
        return "Board{" +
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
            Tile result = Board.this.getTile(nextIndex / numRows, nextIndex % numCols);
            nextIndex++;
            return result;
        }
    }
}