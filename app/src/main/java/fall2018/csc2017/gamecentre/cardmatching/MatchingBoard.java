package fall2018.csc2017.gamecentre.cardmatching;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;

import fall2018.csc2017.gamecentre.ParentBoard;
import fall2018.csc2017.gamecentre.slidingtiles.Board;
import fall2018.csc2017.gamecentre.slidingtiles.Score;
import fall2018.csc2017.gamecentre.slidingtiles.Tile;

public class MatchingBoard extends ParentBoard {

    /**
     * The tiles on the board in row-major order.
     */
    private Card[] cards;

    /**
     * The number of possible matches remaining in the game.
     */
    private int numMatches;

    /**
     * Initialize a new Matching Board using a given array of cards.
     *
     * @param cards   The array of cards to place on the board.
     * @param rows    The number of rows for the board.
     * @param columns The number of columns for the board.
     */
    public MatchingBoard(List<Card> cards, int rows, int columns) {
        super(25, rows, columns);
        this.numMatches = (rows * columns) / 2;
        this.cards = new Card[numRows * numCols];
        Iterator<Card> iter = cards.iterator();

        for (int size = 0; size != numRows * numCols; size++)
            this.cards[size] = iter.next();
    }

    /**
     * Return the Card at the position specified by row and column
     *
     * @param row the Cards row position.
     * @param col The Cards column position.
     * @return The wanted card.
     */
    public Card getCard(int row, int col) {
        return cards[((row) * this.numCols) + col];
    }

    /**
     * Return the list of cards on the board
     *
     * @return A list of cards.
     */
    public Card[] getCards() {
        return this.cards;
    }

    /**
     * Return the number of remaining matches on the board.
     *
     * @return The number of matches remaining.
     */
    public int getNumMatches() {
        return this.numMatches;
    }

    /**
     * Flip the card at the specified position to its opposite side.
     *
     * @param card The card to flip.
     */
    public void flipCard(Card card) {
        card.flipCard();

        setChanged();
        notifyObservers();
    }

    /**
     * Go through the cards to check if two newly flipped cards match.
     */
    public void checkCards() {
        ArrayList<Integer> flippedCards = new ArrayList<>();

        for (int i = 0; i < cards.length; i++) {
            if (cards[i].getState() == 0)
                flippedCards.add(i);
        }

        //If there are two face up cards, compare them.
        if (flippedCards.size() == 2)
            compareCards(flippedCards);
    }

    /**
     * Compare two given cards to see if they have the same value, update their state if they match.
     *
     * @param flippedCards The index of the two cards to compare in this.cards.
     */
    private void compareCards(ArrayList<Integer> flippedCards) {
        int newState = 0;
        Card card1 = cards[flippedCards.get(0)];
        Card card2 = cards[flippedCards.get(1)];

        if (card1.getId() == card2.getId()) {
            newState = 2;
            numMatches--;
        } else if (this.score > 1) {
            this.score--;
        }

        for (int i = 0; i < flippedCards.size(); i++) {
            Card tmp = cards[flippedCards.get(i)];
            tmp.setState(newState);
            if (tmp.getState() != 2) {
                this.flipCard(tmp);
            }
        }
    }
}
