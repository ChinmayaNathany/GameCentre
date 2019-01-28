package fall2018.csc2017.gamecentre;

import java.io.Serializable;

import fall2018.csc2017.gamecentre.simon.SimonBoardManager;
import fall2018.csc2017.gamecentre.cardmatching.MatchingBoardManager;
import fall2018.csc2017.gamecentre.slidingtiles.BoardManager;

/**
 * Represent a single user and their information.
 */
public class UserAccount implements Serializable {
    /**
     * The user's username.
     */
    private String username;
    /**
     * The user's password.
     */
    private String password;
    /**
     * The users highest Sliding Tiles score achieved.
     */
    private int maxScore;
    /**
     * The users highest Matching game score achieved.
     */
    private int matchingMaxScore;
    /**
     * The users highest Simon game score achieved.
     */
    private int simonMaxScore;

    /**
     * Return the user's last saved game.
     *
     * @return last saved game.
     */
    public BoardManager getTilesSavedGame() {
        return tilesSavedGame;
    }

    /**
     * Update the user's lsat saved game.
     *
     * @param savedGame the game to be saved
     */
    public void setTilesSavedGame(BoardManager savedGame) {
        this.tilesSavedGame = savedGame;
    }

    /**
     * The user's last sliding tiles saved game.
     */
    private BoardManager tilesSavedGame;
    /**
     * The user's last matching cards saved game.
     */
    private MatchingBoardManager matchingSavedGame;
    /**
     * The user's last matching cards saved game.
     */
    private SimonBoardManager simonSavedGame;

    /**
     * Return the user's last matching cards saved game.
     *
     * @return The manager for the users last matching cards saved game.
     */
    public MatchingBoardManager getMatchingSavedGame() {
        return matchingSavedGame;
    }

    /**
     * Set the users last matching cards saved game.
     *
     * @param manager the manager for the last matching cards saved game.
     */
    public void setMatchingSavedGame(MatchingBoardManager manager) {
        this.matchingSavedGame = manager;
    }

    /**
     * Return the user's last matching cards saved game.
     *
     * @return The manager for the users last matching cards saved game.
     */
    public SimonBoardManager getSimonSavedGame() {
        return simonSavedGame;
    }

    /**
     * Set the users last simon saved game.
     *
     * @param manager the manager for the last simon saved game.
     */
    public void setSimonSavedGame(SimonBoardManager manager) {
        this.simonSavedGame = manager;
    }

    UserAccount(String username, String password) {
        this.username = username;
        this.password = password;
        this.tilesSavedGame = null;
        this.matchingSavedGame = null;
        this.simonSavedGame = null;
    }

    /**
     * Return if the password input matches the user's password.
     *
     * @param input password attempt.
     */
    public boolean checkPassword(String input) {
        return this.password.equals(input);
    }

    /**
     * Return the user's username.
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Get the users max score.
     *
     * @return the max score.
     */
    public int getMaxScore() {
        return this.maxScore;
    }

    /**
     * Set the users max score to score.
     *
     * @param score The new maxScore.
     */
    public void setMaxScore(int score) {
        this.maxScore = score;
    }

    /**
     * Get the users max score.
     *
     * @return the max score.
     */
    public int getMatchingMaxScore() {
        return this.matchingMaxScore;
    }

    /**
     * Set the users max score to score.
     *
     * @param score The new maxScore.
     */
    public void setMatchingMaxScore(int score) {
        this.matchingMaxScore = score;
    }

    /**
     * Get the users max score.
     *
     * @return the max score.
     */
    public int getSimonMaxScore() {
        return this.simonMaxScore;
    }

    /**
     * Set the users max score to score.
     *
     * @param score The new maxScore.
     */
    public void setSimonMaxScore(int score) {
        this.simonMaxScore = score;
    }


}
