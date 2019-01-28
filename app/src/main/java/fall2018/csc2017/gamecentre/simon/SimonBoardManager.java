package fall2018.csc2017.gamecentre.simon;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import fall2018.csc2017.gamecentre.ParentBoard;
import fall2018.csc2017.gamecentre.ParentManager;

/**
 * Manage a board, including swapping tiles, checking for a win, and managing taps.
 */
public class SimonBoardManager extends ParentManager {

    /**
     * Tracks whether it is the players turn.
     */
    private boolean playerTurn = false;
    /**
     * Tracks whether the player has lost.
     */
    private boolean hasLost = false;
    /**
     * Tracks whether a new round is begining.
     */
    private boolean newRound = false;
    /**
     * Tracks the current index within the pattern.
     */
    private Integer currentIndex = 0;
    /**
     * The current pattern to be remembered by the user.
     */
    private ArrayList<Integer> currentPattern;
    /**
     * The current round of play.
     */
    private Integer round = 0;
    /**
     * The current pattern input by the user.
     */
    private ArrayList<Integer> userPattern;


    /**
     * Increment round by one.
     */
    public void nextRound() {
        round++;
    }

    /**
     * Get the current round
     */
    public boolean getNewRound() {
        return this.newRound;
    }

    /**
     * Get the current round
     */
    public int getRound() {
        return this.round;
    }


    /**
     * Return the value for 'new round'
     */
    public void newRound() {
        newRound = true;
    }

    /**
     * Return the user's current pattern.
     *
     * @return the user's current pattern
     */
    public ArrayList<Integer> getUserPattern() {
        return userPattern;
    }

    /**
     * Set the user's current pattern.
     */
    public void setUserPattern(ArrayList<Integer> userPat) {
        userPattern = userPat;
    }

    /**
     * Set the current pattern.
     */
    public void setCurrentPattern(ArrayList<Integer> currPat) {
        currentPattern = currPat;
    }

    /**
     * Return whether it is the player's turn.
     *
     * @return whether it is the player's turn
     */
    public boolean getPlayerTurn() {
        return playerTurn;
    }

    /**
     * Return the current pattern to be mimicked
     *
     * @return the current pattern to be mimicked
     */
    public ArrayList<Integer> getCurrentPattern() {
        return currentPattern;
    }

    /**
     * Manage a board that has been pre-populated.
     *
     * @param board the board for Simon
     */
    public SimonBoardManager(SimonBoard board) {
        super(board);
    }

    /**
     * Manage a new board.
     */
    public SimonBoardManager(int rows, int cols) {
        super(rows, cols);
        List<Tile> tiles = new ArrayList<>();
        for (int tileNum = 0; tileNum != 4; tileNum++) {
            tiles.add(new Tile(tileNum, 4));
        }
        newCurrentPattern(1);

        this.board = new SimonBoard(tiles);
        saveMove();
    }

    /**
     * Create and set new currentPattern.
     *
     * @param size the length of the pattern.
     */
    public void newCurrentPattern(int size) {
        Random random = new Random();
        currentPattern = new ArrayList<Integer>();
        for (int i = 0; i != size; i++) {
            currentPattern.add(random.nextInt(4));
            while (i > 0 && currentPattern.get(i) == currentPattern.get(i - 1)) {
                currentPattern.remove(i);
                currentPattern.add(random.nextInt(4));
            }
        }
    }

    /**
     * Return whether the tiles are in row-major order.
     *
     * @return whether the tiles are in row-major order
     */
    public boolean puzzleSolved() {
        return hasLost;
    }

    /**
     * Return whether the user's tap is valid.
     *
     * @return whether the user's tap is valid.
     */
    public boolean isValidTap(int position) {
        return playerTurn;
    }

    /**
     * Update the tiles.
     */
    public void updateTiles() {
        if (round == 0)
            round++;
        else if (!playerTurn)
            ifNotPlayerTurnUpdateTiles();
    }

    /**
     * Helper method for activating/deactivating tiles when it is not the players turn.
     */
    public void ifNotPlayerTurnUpdateTiles() {
        if (newRound) {
            newCurrentPattern(round);
            newRound = false;
            currentIndex = 0;
        }

        deactivateTiles();

        if (currentIndex.equals(round)) {
            switchTurn();
            currentIndex = 0;
            userPattern = new ArrayList<>();
        }
        if (!playerTurn) {
            ArrayList<Integer> pattern = getCurrentPattern();
            int index = currentIndex;
            ((SimonBoard) board).activateTile(pattern.get(index), playerTurn);
            currentIndex++;
        }
    }

    /**
     * Deactivate the tiles.
     */
    public void deactivateTiles() {
        for (int i = 0; i != 4; i++) {
            ((SimonBoard) board).deactivateTile(i);
        }
    }

    /**
     * Switch turns.
     */
    public void switchTurn() {
        playerTurn = !playerTurn;
    }

    /**
     * Compare the length of the given pattern and the user's submission.
     */
    public int UserCurrentPatternCompareLength() {
        return currentPattern.size() - userPattern.size();
    }

    /**
     * Check equality of given pattern and user's submission.
     */
    public boolean UserCurrentPatternEqual() {
        return currentPattern.equals(userPattern);
    }

    /**
     * Process a touch at position in the board, swapping tiles as appropriate.
     *
     * @param position the position
     */
    public void touchMove(int position) {
        if (playerTurn) {
            userPattern.add(position);
            ((SimonBoard) board).userTouchNotify();
            //moves_list.add(board);
        }

    }
}