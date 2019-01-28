package fall2018.csc2017.gamecentre.cardmatching;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fall2018.csc2017.gamecentre.ParentManager;

public class MatchingBoardManager extends ParentManager {

    /**
     * Create a new Matching board manager for a new game.
     *
     * @param rows The number of rows for the game.
     * @param cols The number of columns for the game.
     */
    public MatchingBoardManager(int rows, int cols) {
        super(rows, cols);
        List<Card> cards = new ArrayList<>();
        int cardId = 0;
        for (int cardNum = 0; cardNum != numTiles; cardNum = cardNum + 2) {
            cards.add(new Card(cardId, rows));
            cards.add(new Card(cardId, rows));
            cardId++;
        }

        Collections.shuffle(cards);
        this.board = new MatchingBoard(cards, rows, cols);
        saveMove();
    }

    /**
     * Create a new MatchingBoardManager using an existing MatchingBoard.
     *
     * @param board A MatchingBoard.
     */
    public MatchingBoardManager(MatchingBoard board) {
        super(board);
    }

    /**
     * Return true if all matches on the board have been made.
     *
     * @return If the game has been solved.
     */
    public boolean puzzleSolved() {
        MatchingBoard tmp = (MatchingBoard) board;
        return tmp.getNumMatches() == 0;
    }

    /**
     * Return true if the card at position is face-down.
     *
     * @param position The card to checks position.
     * @return If the card can be flipped.
     */
    public boolean isValidTap(int position) {
        int row = position / board.getNumRows();
        int col = position % board.getNumCols();
        //Check if card is face-down.
        Card card = ((MatchingBoard) board).getCard(row, col);
        ArrayList<Integer> flippedCards = new ArrayList<>();

        for (int i = 0; i < ((MatchingBoard) board).getCards().length; i++) {
            if (((MatchingBoard) board).getCards()[i].getState() == 0)
                flippedCards.add(i);
        }
        return card.getState() == 1 && flippedCards.size() < 2;
    }

    /**
     * Flip the card at the specified position if it is a valid move.
     *
     * @param position The cards position
     */
    @Override
    public void touchMove(int position) {
        int row = position / board.getNumRows();
        int col = position % board.getNumCols();
        if (isValidTap(position)) {
            ((MatchingBoard) board).flipCard(((MatchingBoard) board).getCard(row, col));
        }
    }
}
