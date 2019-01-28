package fall2018.csc2017.gamecentre;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import fall2018.csc2017.gamecentre.slidingtiles.Autosave;
import fall2018.csc2017.gamecentre.slidingtiles.Board;
import fall2018.csc2017.gamecentre.slidingtiles.BoardManager;
import fall2018.csc2017.gamecentre.slidingtiles.Tile;
import fall2018.csc2017.gamecentre.slidingtiles.Undo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;

public class SlidingTilesTest {

    /**
     * The board manager for testing.
     */
    private BoardManager boardManager = new BoardManager(4, 4);

    /* UnitTests for BoardManager */

    /**
     * Create a new 4 by 4 board that has a specific combination of tiles.
     *
     * @return a 4 by 4 board of tiles
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

    /**
     * Test the set-up of BoardManager.
     */
    @Test
    public void testBoardManagerSetUp() {
        Board tmpBoard = create4by4Board();
        boardManager.setBoard(tmpBoard);
        assertEquals(tmpBoard, boardManager.getBoard());

        BoardManager testBoardManager = new BoardManager(tmpBoard);
        assertEquals(tmpBoard, testBoardManager.getBoard());
    }

    /**
     * Test whether removing a move from BoardManager works.
     */
    @Test
    public void testRemoveMove() {
        Board board = (Board) boardManager.getBoard();
        boardManager.removeMove();
        assertEquals(board, boardManager.getBoard());

        ((Board) boardManager.getBoard()).swapTiles(0, 0, 0, 1);
        Board tmpBoard = (Board) boardManager.getBoard();
        ((Board) boardManager.getBoard()).swapTiles(1, 1, 1, 2);
        boardManager.removeMove();
        assertEquals(tmpBoard, boardManager.getBoard());
    }

    /**
     * Test whether the board manager used for testing is shuffled and not solved.
     */
    @Test
    public void testShuffledBoardManager() {
        assertFalse(this.boardManager.puzzleSolved());
    }

    /* UnitTests for Undo */

    /**
     * Test whether undo works for a board with no moves made.
     */
    @Test
    public void testCantUndo() {
        BoardManager boardMan = new BoardManager(create4by4Board());
        Undo undoObj = new Undo(boardMan);
        assertFalse("Returns True for a Can't Undo case", undoObj.canUndo());
    }

    /**
     * Test whether undo works for a board with moves made.
     */
    @Test
    public void testCanUndo() {
        BoardManager boardMan = new BoardManager(create4by4Board());
        boardMan.touchMove(0);
        Undo undoObj = new Undo(boardMan);
        assertTrue("Returns False for a Can Undo case", undoObj.canUndo());
    }

    /**
     * Test whether the undo function works for one move.
     */
    @Test
    public void testOneUndo() {
        BoardManager boardMan = new BoardManager(create4by4Board());
        boardMan.touchMove(0);
        Undo undoObj = new Undo(boardMan);
        if (undoObj.canUndo())
            undoObj.performUndo();
        assertEquals(16, ((Board) boardMan.getBoard()).getTile(0, 1).getId());
    }

    /**
     * Test whether the undo function works for two moves.
     */
    @Test
    public void testTwoUndos() {
        BoardManager boardMan = new BoardManager(create4by4Board());
        boardMan.touchMove(0);
        boardMan.touchMove(4);
        Undo undoObj = new Undo(boardMan);
        if (undoObj.canUndo())
            undoObj.performUndo();
        if (undoObj.canUndo())
            undoObj.performUndo();

        assertEquals(16, ((Board) boardMan.getBoard()).getTile(0, 1).getId());
    }

    /**
     * Test whether the undo function works for three moves.
     */
    @Test
    public void testThreeUndos() {
        BoardManager boardMan = new BoardManager(create4by4Board());
        boardMan.touchMove(0);
        boardMan.touchMove(4);
        boardMan.touchMove(8);
        Undo undoObj = new Undo(boardMan);
        if (undoObj.canUndo())
            undoObj.performUndo();
        if (undoObj.canUndo())
            undoObj.performUndo();
        if (undoObj.canUndo())
            undoObj.performUndo();
        assertEquals(16, ((Board) boardMan.getBoard()).getTile(0, 1).getId());
    }

    /**
     * Test whether getBoards() returns the correct ArrayList of boards.
     */
    @Test
    public void testGetBoards() {
        BoardManager boardMan = new BoardManager(create4by4Board());
        boardMan.touchMove(0);
        boardMan.touchMove(4);
        boardMan.touchMove(8);
        Undo undo = new Undo(boardMan);
        undo.performUndo();
        Board currBoard = (Board) undo.getBoards().get(undo.getBoards().size() - 1);
        assertEquals(16, currBoard.getTile(1, 0).getId());
    }

    /**
     * Test whether getBoardManager() returns the correctly stored Board
     * in it's BoardManager.
     */
    @Test
    public void testGetBoardManager() {
        BoardManager boardMan = new BoardManager(create4by4Board());
        boardMan.touchMove(0);
        boardMan.touchMove(4);
        boardMan.touchMove(8);
        Undo undo = new Undo(boardMan);
        undo.performUndo();
        BoardManager currBoardManager = undo.getBoardManager();
        Board currBoard = (Board) currBoardManager.getBoard();
        assertEquals(16, currBoard.getTile(1, 0).getId());
    }

    /* UnitTests for the game's score */

    /**
     * Test whether the score updates after a move is made.
     */
    @Test
    public void testMoveScore() {
        BoardManager boardMan = new BoardManager(create4by4Board());
        boardMan.touchMove(0);
        boardMan.touchMove(4);
        assertEquals(248, boardMan.getBoard().getScore());
    }

    /**
     * Test whether the score updates after an undo is made.
     */
    @Test
    public void testUndoScore() {
        BoardManager boardMan = new BoardManager(create4by4Board());
        boardMan.touchMove(0);
        Undo undoObj = new Undo(boardMan);
        if (undoObj.canUndo())
            undoObj.performUndo();
        assertEquals(250, boardMan.getBoard().getScore());
    }

    /* UnitTests for AutoSave */

    /**
     * Test whether the game is auto-saved after a move.
     */
    @Test
    public void testMoveAutoSave() {
        BoardManager boardMan = new BoardManager(create4by4Board());
        boardMan.touchMove(0);
        boardMan.touchMove(4);
        boardMan.touchMove(8);
        Autosave saveObj = new Autosave(boardMan.getMovesList());
        ArrayList<ParentBoard> movesList = saveObj.autoloadFromFile();
        Board currBoardLoaded = (Board) movesList.get(movesList.size() - 1);
        assertEquals(16, currBoardLoaded.getTile(2, 0).getId());
    }

    /**
     * Test whether the game is auto-saved after an undo.
     */
    @Test
    public void testUndoAutoSave() {
        Autosave autoSave1 = new Autosave(boardManager.getMovesList());
        ArrayList<ParentBoard> autoSaveList1 = autoSave1.autoloadFromFile();
        ((Board) boardManager.getBoard()).swapTiles(0, 0, 0, 1);
        Undo undo = new Undo(boardManager);
        assertEquals(autoSaveList1, undo.getBoards());
    }

    /**
     * Test whether the game is auto-saved initially,
     * if auto save is called before a game is started.
     */
    @Test
    public void testNewAutoSave() {

        Autosave autoSave1 = new Autosave();
        ArrayList<ParentBoard> autoSaveList1 = autoSave1.autoloadFromFile();
        assertNotSame(autoSaveList1, new ArrayList<>());
    }
}
