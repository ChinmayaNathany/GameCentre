package fall2018.csc2017.gamecentre;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

import fall2018.csc2017.gamecentre.slidingtiles.Board;
import fall2018.csc2017.gamecentre.slidingtiles.BoardManager;
import fall2018.csc2017.gamecentre.slidingtiles.Tile;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class BoardAndTileTest {

    /**
     * The board manager for testing.
     */
    private BoardManager boardManager = new BoardManager(4, 4);

    /**
     * Make a set of tiles that are in order.
     *
     * @return a set of tiles that are in order
     */
    private List<Tile> makeTiles() {
        List<Tile> tiles = new ArrayList<>();
        Board board = (Board) boardManager.getBoard();
        final int numTiles = board.getNumRows() * board.getNumCols();
        for (int tileNum = 0; tileNum != numTiles; tileNum++) {
            tiles.add(new Tile(tileNum + 1, tileNum, 4));
        }

        return tiles;
    }

    /**
     * Make a solved Board.
     */
    private void setUpCorrect() {
        List<Tile> tiles = makeTiles();
        Board board = new Board(tiles, 4, 4);
        boardManager = new BoardManager(board);
    }

    /**
     * Shuffle a few tiles.
     */
    private void swapFirstTwoTiles() {
        ((Board) boardManager.getBoard()).swapTiles(0, 0, 0, 1);
    }

    /**
     * Test whether swapping two tiles makes a solved board unsolved.
     */
    @Test
    public void testIsSolved() {
        setUpCorrect();
        assertTrue(boardManager.puzzleSolved());
        swapFirstTwoTiles();
        assertFalse(boardManager.puzzleSolved());
    }

    /**
     * Test whether swapping the first two tiles works.
     */
    @Test
    public void testSwapFirstTwo() {
        setUpCorrect();
        assertEquals(1, ((Board) boardManager.getBoard()).getTile(0, 0).getId());
        assertEquals(2, ((Board) boardManager.getBoard()).getTile(0, 1).getId());
        ((Board) boardManager.getBoard()).swapTiles(0, 0, 0, 1);
        assertEquals(2, ((Board) boardManager.getBoard()).getTile(0, 0).getId());
        assertEquals(1, ((Board) boardManager.getBoard()).getTile(0, 1).getId());
    }

    /**
     * Test whether swapping the last two tiles works.
     */
    @Test
    public void testSwapLastTwo() {
        setUpCorrect();
        assertEquals(15, ((Board) boardManager.getBoard()).getTile(3, 2).getId());
        assertEquals(16, ((Board) boardManager.getBoard()).getTile(3, 3).getId());
        ((Board) boardManager.getBoard()).swapTiles(3, 3, 3, 2);
        assertEquals(16, ((Board) boardManager.getBoard()).getTile(3, 2).getId());
        assertEquals(15, ((Board) boardManager.getBoard()).getTile(3, 3).getId());
    }

    /**
     * Test whether isValidHelp works.
     */
    @Test
    public void testIsValidTap() {
        setUpCorrect();
        assertTrue(boardManager.isValidTap(11));
        assertTrue(boardManager.isValidTap(14));
        assertFalse(boardManager.isValidTap(10));
    }

    /**
     * Test whether touchMove works.
     */
    @Test
    public void testTouchMove() {
        setUpCorrect();
        boardManager.touchMove(2);
        assertTrue(boardManager.isValidTap(11));
        assertTrue(boardManager.isValidTap(14));
        assertFalse(boardManager.isValidTap(10));
    }

    /**
     * Create a new 3 by 3 board that has a specific combination of tiles
     * This is important when it comes to testing for the getLocation and getInversion methods
     */
    private Board create3by3Board() {
        Tile[][] tiles;
        Tile t1 = new Tile(0, 3);
        Tile t2 = new Tile(1, 3);
        Tile t3 = new Tile(2, 3);
        Tile t4 = new Tile(3, 3);
        Tile t5 = new Tile(4, 3);
        Tile t6 = new Tile(5, 3);
        Tile t7 = new Tile(6, 3);
        Tile t8 = new Tile(7, 3);
        Tile t9 = new Tile(8, 3);
        tiles = new Tile[3][3];
        tiles[0][0] = t7;
        tiles[0][1] = t6;
        tiles[0][2] = t2;
        tiles[1][0] = t5;
        tiles[1][1] = t9;
        tiles[1][2] = t3;
        tiles[2][0] = t1;
        tiles[2][1] = t8;
        tiles[2][2] = t4;
        return new Board(tiles);
    }

    private Board board3by3 = create3by3Board();

    /**
     * Create a new 4 by 4 board that has a specific combination of tiles
     * This is important when it comes to testing for the getLocation and getInversion methods
     */
    private Board create4by4Board() {
        Tile[][] tiles;
        Tile t1 = new Tile(0, 4);
        Tile t2 = new Tile(1, 4);
        Tile t3 = new Tile(2, 4);
        Tile t4 = new Tile(3, 4);
        Tile t5 = new Tile(4, 4);
        Tile t6 = new Tile(5, 4);
        Tile t7 = new Tile(6, 4);
        Tile t8 = new Tile(7, 4);
        Tile t9 = new Tile(8, 4);
        Tile t10 = new Tile(9, 4);
        Tile t11 = new Tile(10, 4);
        Tile t12 = new Tile(11, 4);
        Tile t13 = new Tile(12, 4);
        Tile t14 = new Tile(13, 4);
        Tile t15 = new Tile(14, 4);
        Tile t16 = new Tile(15, 4);
        tiles = new Tile[4][4];
        tiles[0][0] = t5;
        tiles[0][1] = t16;
        tiles[0][2] = t1;
        tiles[0][3] = t3;
        tiles[1][0] = t13;
        tiles[1][1] = t15;
        tiles[1][2] = t9;
        tiles[1][3] = t12;
        tiles[2][0] = t2;
        tiles[2][1] = t6;
        tiles[2][2] = t7;
        tiles[2][3] = t14;
        tiles[3][0] = t10;
        tiles[3][1] = t4;
        tiles[3][2] = t8;
        tiles[3][3] = t11;
        return new Board(tiles);
    }

    private Board board4by4 = create4by4Board();

    /**
     * Create a new 5 by 5 board that has a specific combination of tiles
     * This is important when it comes to testing for the getLocation and getInversion methods
     */
    private Board create5by5Board() {
        Tile[][] tiles;
        Tile t1 = new Tile(0, 5);
        Tile t2 = new Tile(1, 5);
        Tile t3 = new Tile(2, 5);
        Tile t4 = new Tile(3, 5);
        Tile t5 = new Tile(4, 5);
        Tile t6 = new Tile(5, 5);
        Tile t7 = new Tile(6, 5);
        Tile t8 = new Tile(7, 5);
        Tile t9 = new Tile(8, 5);
        Tile t10 = new Tile(9, 5);
        Tile t11 = new Tile(10, 5);
        Tile t12 = new Tile(11, 5);
        Tile t13 = new Tile(12, 5);
        Tile t14 = new Tile(13, 5);
        Tile t15 = new Tile(14, 5);
        Tile t16 = new Tile(15, 5);
        Tile t17 = new Tile(16, 5);
        Tile t18 = new Tile(17, 5);
        Tile t19 = new Tile(18, 5);
        Tile t20 = new Tile(19, 5);
        Tile t21 = new Tile(20, 5);
        Tile t22 = new Tile(21, 5);
        Tile t23 = new Tile(22, 5);
        Tile t24 = new Tile(23, 5);
        Tile t25 = new Tile(24, 5);
        tiles = new Tile[5][5];
        tiles[0][0] = t7;
        tiles[0][1] = t6;
        tiles[0][2] = t2;
        tiles[0][3] = t5;
        tiles[0][4] = t9;
        tiles[1][0] = t3;
        tiles[1][1] = t1;
        tiles[1][2] = t8;
        tiles[1][3] = t4;
        tiles[1][4] = t11;
        tiles[2][0] = t17;
        tiles[2][1] = t18;
        tiles[2][2] = t10;
        tiles[2][3] = t25;
        tiles[2][4] = t20;
        tiles[3][0] = t23;
        tiles[3][1] = t21;
        tiles[3][2] = t12;
        tiles[3][3] = t15;
        tiles[3][4] = t13;
        tiles[4][0] = t22;
        tiles[4][1] = t24;
        tiles[4][2] = t14;
        tiles[4][3] = t16;
        tiles[4][4] = t19;
        return new Board(tiles);
    }

    private Board board5by5 = create5by5Board();

    /**
     * Test whether getInversions returns the total number of inversions on a 3x3 board
     */
    @Test
    public void testGetInversions3by3() {
        int inversions = board3by3.getInversions();
        assertEquals(17, inversions);
    }

    /**
     * Test whether getInversions returns the total number of inversions on a 4x4 board
     */
    @Test
    public void testGetInversions4by4() {
        int inversions = board4by4.getInversions();
        assertEquals(44, inversions);
    }

    /**
     * Test whether getInversions returns the total number of inversions on a 4x4 board
     */
    @Test
    public void testGetInversions5by5() {
        int inversions = board5by5.getInversions();
        assertEquals(62, inversions);
    }

    /**
     * Test whether getTile returns the tile at the index of the 2d array contained by some board
     * by comparing ids
     */
    @Test
    public void testGetTile() {
        assertEquals(7, board3by3.getTile(0, 0).getId());
    }

    /**
     * Test whether getLocation returns the location of a tile in the 2d array list
     * contained by some board
     */
    @Test
    public void testGetLocation() {
        int[] location = board3by3.getLocation(1);
        int[] result = new int[2];
        result[0] = 2;
        result[1] = 0;
        boolean equiv = Arrays.equals(result, location);
        assertTrue(equiv);
    }

    /**
     * Test whether getScore returns the score of a board
     */
    @Test
    public void testGetScore() {
        assertEquals(250, board3by3.getScore());
    }

    /**
     * Test whether compareAfter returns the number of inversions for a particular tile
     */
    @Test
    public void testCompareAfter() {
        Tile t7 = new Tile(6, 3);
        assertEquals(6, board3by3.compareAfter(t7));
    }

}