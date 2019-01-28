package fall2018.csc2017.gamecentre.slidingtiles;

import java.util.ArrayList;

import fall2018.csc2017.gamecentre.ParentBoard;


public class Undo {
    // The Undo functionality that allows the user to undo their last n moves.

    /**
     * The board of the last move. If there were no prior moves, lastBoard is null.
     */
    private Board lastBoard = null;

    /**
     * The current board.
     */
    private Board currentBoard = null;

    /**
     * ArrayList of the serialized boards.
     */
    ArrayList<ParentBoard> boards;
    /**
     * The BoardManager.
     */
    private BoardManager boardManager;

    public static final String FILENAME = "/data/data/fall2018.csc2017.slidingtiles/files/AutoSave.ser";

    public Undo(BoardManager game) {
        boardManager = game;
        boards = game.getMovesList();
    }

    public void performUndo() {
        if (canUndo()) {
            /*Board newBoard = new Board(lastBoard.getArrayTiles(), lastBoard.getNUM_ROWS(), lastBoard.getNUM_COLS());
            BoardManager boardManager = new BoardManager(newBoard);
            boardManager.setMovesList(boards);
            Tile[][] lastMove = lastBoard.getArrayTiles();
            boardManager.removeMove();
            boardManager.getBoard().swapTileList(lastMove);*/
            store_last_move();
            Tile[][] lastMove = lastBoard.getArrayTiles();
            boardManager.setBoard(lastBoard);
            ((Board) boardManager.getBoard()).swapTileList(lastMove);
//            ArrayList<Board> movesList = boardManager.getMovesList();
//            movesList.remove(movesList.size() - 1);
//            boardManager.setMovesList(movesList);
            boardManager.removeMove();
        }
    }

    /**
     * Store the last move that was made.
     */
    private void store_last_move() {
//        try {
//            FileInputStream var2 = new FileInputStream(FILENAME);
//            BufferedInputStream var3 = new BufferedInputStream(var2);
//            ObjectInputStream var4 = new ObjectInputStream(var3);
//            boards = (ArrayList<Board>) var4.readObject();
//            var4.close();
//            System.out.println("Loading");
//        } catch (FileNotFoundException e) {
//            Log.e("login activity", "File not found: " + e.toString());
//        } catch (IOException e) {
//            Log.e("login activity", "Can not read file: " + e.toString());
//        } catch (ClassNotFoundException e) {
//            Log.e("login activity", "File contained unexpected data type: " + e.toString());
//        }
        currentBoard = (Board) boards.get(boards.size() - 1);
        lastBoard = (Board) boards.get(boards.size() - 2);
    }

//    /**
//     * Get the current board that is in play.
//     * @return the current board
//     */
//    public Board getCurrentBoard() { return this.currentBoard; }

    /**
     * Get the board manager that is in play.
     *
     * @return the current board
     */
    public BoardManager getBoardManager() {
        return this.boardManager;
    }

    /**
     * Get the serialized boards of previous moves.
     *
     * @return ArrayList of boards
     */
    public ArrayList<ParentBoard> getBoards() {
        return this.boards;
    }

    /**
     * Return if there is a valid undo available.
     *
     * @return boolean
     */
    public boolean canUndo() {
        return this.boards != null && this.boards.size() >= 2;
    }

}