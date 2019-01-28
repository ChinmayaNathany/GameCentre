package fall2018.csc2017.gamecentre;

import android.content.Context;
import android.widget.Toast;

import fall2018.csc2017.gamecentre.cardmatching.MatchingBoardManager;
import fall2018.csc2017.gamecentre.simon.SimonBoardManager;
import fall2018.csc2017.gamecentre.slidingtiles.BoardManager;


public class MovementController {
    /**
     * The Sliding Tiles BoardManager.
     */
    private BoardManager boardManager = null;
    /**
     * The MatchingBoardManager.
     */
    private MatchingBoardManager matchingBoardManager = null;
    /**
     * The SimonBoardManager.
     */
    private SimonBoardManager simonBoardManager = null;

    public MovementController() {
    }

    public void setBoardManager(BoardManager boardManager) {
        this.boardManager = boardManager;
    }

    /**
     * Set the matchingBoardManager to manager.
     *
     * @param manager the new MatchingBoardManager.
     */
    public void setMatchingBoardManager(MatchingBoardManager manager) {
        this.matchingBoardManager = manager;
    }

    /**
     * Set the SimonBoardManager to manager.
     *
     * @param manager the new SimonBoardManager.
     */
    public void setSimonBoardManager(SimonBoardManager manager) {
        this.simonBoardManager = manager;
    }

    /**
     * Process Sliding Tiles Tap Movement
     *
     * @param context  the Context
     * @param position the position of the tap
     * @param display  boolean display
     */
    public void processTapMovement(Context context, int position, boolean display) {
        if (boardManager.isValidTap(position)) {
            boardManager.touchMove(position);
            if (boardManager.puzzleSolved()) {
                Toast.makeText(context, "YOU WIN!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "Invalid Tap", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Process Simon Tap Movement
     *
     * @param context  the Context
     * @param position the position of the tap
     * @param display  boolean display
     */
    public void processSimonTapMovement(Context context, int position, boolean display) {
        if (simonBoardManager.isValidTap(position)) {
            simonBoardManager.touchMove(position);
            if (simonBoardManager.puzzleSolved()) {
                Toast.makeText(context, "YOU WIN!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "Invalid Tap", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Process Matching Cards Tap Movement
     *
     * @param context  the Context
     * @param position the position of the tap
     * @param display  boolean display
     */
    public void processMatchingTapMovement(Context context, int position, boolean display) {
        if (matchingBoardManager.isValidTap(position)) {
            matchingBoardManager.touchMove(position);
        } else {
            Toast.makeText(context, "Invalid Tap", Toast.LENGTH_SHORT).show();
        }
    }
}
