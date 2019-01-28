package fall2018.csc2017.gamecentre;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;

import fall2018.csc2017.gamecentre.simon.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class SimonTest {

    /**
     * The board manager for testing.
     */
    private SimonBoardManager boardManager = new SimonBoardManager(2, 2);

    /* UnitTests for SimonBoardManager */

    /**
     * Create a new 2 by 2 board
     *
     * @return a 2 by 2 board of tiles for Simon
     */
    private SimonBoard create2by2Board() {
        Tile[] tiles = new Tile[4];
        tiles[0] = new Tile(0, 4);
        tiles[1] = new Tile(1, 4);
        tiles[2] = new Tile(2, 4);
        tiles[3] = new Tile(3, 4);
        return new SimonBoard(tiles);
    }

    private boolean contains(int srchItem, int[] list) {
        for (int item : list)
            if (item == srchItem)
                return true;
        return false;
    }

    /**
     * Test the set-up of SimonBoardManager.
     */
    @Test
    public void testSimonBoardManager() {
        SimonBoard tmpBoard = create2by2Board();
        boardManager.setBoard(tmpBoard);
        assertEquals(tmpBoard, boardManager.getBoard());

        SimonBoardManager testSimonBoardManager = new SimonBoardManager(tmpBoard);
        assertEquals(tmpBoard, testSimonBoardManager.getBoard());
    }

    /**
     * Test the set-up of SimonBoardManager without provided board.
     */
    @Test
    public void testSimonBoardManagerNoBoard() {
        SimonBoardManager testSimonBoardManager = new SimonBoardManager(2, 2);
        assertEquals(4, ((SimonBoard) testSimonBoardManager.getBoard()).tiles.length);
    }

    /**
     * Test whether a new pattern of size 1 generated is valid
     */
    @Test
    public void testNewCurrentPatternSize1() {
        SimonBoardManager boardMan = new SimonBoardManager(create2by2Board());
        boardMan.newCurrentPattern(1);
        ArrayList<Integer> pattern = boardMan.getCurrentPattern();
        Set<Integer> set = new HashSet<>(pattern);

        assertTrue("Duplicates Exist", set.size() == pattern.size() && pattern.size() == 1);
    }

    /**
     * Test whether a new pattern of size 3 generated is valid
     */
    @Test
    public void testNewCurrentPatternSize3() {
        SimonBoardManager boardMan = new SimonBoardManager(create2by2Board());
        boardMan.newCurrentPattern(3);
        ArrayList<Integer> pattern = boardMan.getCurrentPattern();
        boolean twoInARow = false;
        for (int i = 0; i != 2; i++) {
            if (pattern.get(i).equals(pattern.get(i + 1))) {
                twoInARow = true;
            }
        }
        assertFalse("Same color occurs twice in a row (NOT ALLOWED)", twoInARow);
    }

    /**
     * Test whether a the new round method is working correctly
     */
    @Test
    public void testNextRound() {
        SimonBoardManager boardMan = new SimonBoardManager(create2by2Board());
        boardMan.nextRound();
        boardMan.nextRound();
        assertEquals(2, boardMan.getRound());
    }

    /**
     * Test whether a the user's tap is valid
     */
    @Test
    public void testIsValidTap() {
        SimonBoardManager boardMan = new SimonBoardManager(create2by2Board());
        assertFalse("Not a Valid Tap", boardMan.isValidTap(0));
    }

    /**
     * Test whether the tiles are getting updated correctly when round is 0
     */
    @Test
    public void testUpdateTilesRound0() {
        SimonBoardManager boardMan = new SimonBoardManager(create2by2Board());
        boardMan.updateTiles();
        assertEquals(1, boardMan.getRound());
    }

    /**
     * Test whether the tiles are getting updated correctly in every situation except when round is 0
     */
    @Test
    public void testUpdateTiles() {
        SimonBoardManager boardMan = new SimonBoardManager(create2by2Board());
        boardMan.nextRound();
        boardMan.newRound();
        boardMan.touchMove(1);
        boardMan.updateTiles();
        assertFalse(boardMan.getNewRound());
    }

    /**
     * Test whether the tiles are correctly activated
     */
    @Test
    public void testActivateTiles() {
        ParentManager boardMan = new SimonBoardManager(create2by2Board());
        SimonBoard board = (SimonBoard) boardMan.getBoard();
        for (int i = 0; i <= 3; i++)
            board.activateTile(i, true);
        Tile[] tiles = board.getArrayTiles();
        boolean result = true;
        int[] temp = {R.drawable.on_1, R.drawable.on_2, R.drawable.on_3, R.drawable.on_4};
        for (int i = 0; i <= 3; i++) {
            if (!contains(tiles[i].getBackground(), temp))
                result = false;
        }
        assertTrue(result);

    }

    /**
     * Test whether the tiles are correctly deactivated
     */
    @Test
    public void testDeactivateTiles() {
        ParentManager boardMan = new SimonBoardManager(create2by2Board());
        for (int i = 0; i <= 3; i++)
            ((SimonBoard) boardMan.getBoard()).activateTile(i, true);
        ((SimonBoardManager) boardMan).deactivateTiles();
        SimonBoard board = (SimonBoard) boardMan.getBoard();
        Tile[] tiles = board.getArrayTiles();
        boolean result = true;
        int[] temp = {R.drawable.on_1, R.drawable.on_2, R.drawable.on_3, R.drawable.on_4};
        for (int i = 0; i <= 3; i++) {
            if (contains(tiles[i].getBackground(), temp))
                result = false;
        }
        assertTrue(result);
    }

    /**
     * Test whether the turn is correctly switched
     */
    @Test
    public void testSwitchTurn() {
        ParentManager boardMan = new SimonBoardManager(create2by2Board());
        ((SimonBoardManager) boardMan).switchTurn();
        assertTrue(((SimonBoardManager) boardMan).getPlayerTurn());
    }

    /**
     * Test whether the length of the patterns are being compared correctly
     */
    @Test
    public void testCurrentPatternCompareLength() {
        ParentManager boardMan = new SimonBoardManager(create2by2Board());
        ArrayList<Integer> listUser = new ArrayList<>();
        listUser.add(0);
        ArrayList<Integer> listCurr = new ArrayList<>();
        listCurr.add(0);
        listCurr.add(1);
        ((SimonBoardManager) boardMan).setUserPattern(listUser);
        ((SimonBoardManager) boardMan).setCurrentPattern(listCurr);
        assertEquals(1, ((SimonBoardManager) boardMan).UserCurrentPatternCompareLength());
    }

    /**
     * Test whether the pattern size equality tests are working correctly for unequal patterns
     */
    @Test
    public void testCurrentPatternNotEqualLength() {
        ParentManager boardMan = new SimonBoardManager(create2by2Board());
        ArrayList<Integer> listUser = new ArrayList<>();
        listUser.add(0);
        ArrayList<Integer> listCurr = new ArrayList<>();
        listCurr.add(0);
        listCurr.add(1);
        ((SimonBoardManager) boardMan).setUserPattern(listUser);
        ((SimonBoardManager) boardMan).setCurrentPattern(listCurr);
        assertFalse(((SimonBoardManager) boardMan).UserCurrentPatternEqual());
    }

    /**
     * Test whether the pattern size equality tests are working correctly for unequal patterns (same length)
     */
    @Test
    public void testCurrentPatternNotEqual() {
        ParentManager boardMan = new SimonBoardManager(create2by2Board());
        ArrayList<Integer> listUser = new ArrayList<>();
        listUser.add(0);
        listUser.add(5);
        ArrayList<Integer> listCurr = new ArrayList<>();
        listCurr.add(0);
        listCurr.add(1);
        ((SimonBoardManager) boardMan).setUserPattern(listUser);
        ((SimonBoardManager) boardMan).setCurrentPattern(listCurr);
        assertFalse(((SimonBoardManager) boardMan).UserCurrentPatternEqual());
    }

    /**
     * Test whether the pattern size equality tests are working correctly for equal patterns
     */
    @Test
    public void testCurrentPatternEqualLength() {
        ParentManager boardMan = new SimonBoardManager(create2by2Board());
        ArrayList<Integer> listUser = new ArrayList<>();
        listUser.add(0);
        listUser.add(1);
        ArrayList<Integer> listCurr = new ArrayList<>();
        listCurr.add(0);
        listCurr.add(1);
        ((SimonBoardManager) boardMan).setUserPattern(listUser);
        ((SimonBoardManager) boardMan).setCurrentPattern(listCurr);
        assertTrue(((SimonBoardManager) boardMan).UserCurrentPatternEqual());
    }

    /**
     * Test whether the touch move function works correctly
     */
    @Test
    public void testTouchMove() {
        ParentManager boardMan = new SimonBoardManager(create2by2Board());
        ((SimonBoardManager) boardMan).updateTiles();
        ((SimonBoardManager) boardMan).switchTurn();
        ((SimonBoardManager) boardMan).setUserPattern(new ArrayList<Integer>());
        boardMan.touchMove(1);
        boardMan.touchMove(2);
        assertEquals(2, ((SimonBoardManager) boardMan).getUserPattern().size());
    }


    /* UnitTests for SimonBoard */

    /**
     * Test getTile function in SimonBoard.
     */
    @Test
    public void testSimonBoardGetTile() {
        SimonBoard tmpBoard = create2by2Board();
        int id0 = tmpBoard.getTile(0).getId();
        int id1 = tmpBoard.getTile(1).getId();
        int id2 = tmpBoard.getTile(2).getId();
        assertEquals(0, id0);
        assertEquals(1, id1);
        assertEquals(2, id2);
    }

    @Test
    public void testSimonBoardIterator() {
        SimonBoard tmpBoard = create2by2Board();
        Iterator iter = tmpBoard.iterator();
        assertTrue(iter.hasNext());
        assertEquals(tmpBoard.getTile(0), iter.next());
        assertEquals(tmpBoard.getTile(1), iter.next());
        iter.next();
        iter.next();
        assertFalse(iter.hasNext());
    }
}
