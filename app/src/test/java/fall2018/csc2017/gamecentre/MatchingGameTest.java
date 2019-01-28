package fall2018.csc2017.gamecentre;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import fall2018.csc2017.gamecentre.cardmatching.Card;
import fall2018.csc2017.gamecentre.cardmatching.MatchingBoard;
import fall2018.csc2017.gamecentre.cardmatching.MatchingBoardManager;

import static org.junit.Assert.*;

public class MatchingGameTest {
    /**
     * A card for testing
     */
    private Card card;
    /**
     * A MatchingBoard for testing.
     */
    private MatchingBoard board;
    /**
     * A MatchingBoardManager for testing.
     */
    private MatchingBoardManager manager;

    /**
     * Make a set of cards that are in order.
     *
     * @return a set of tiles that are in order
     */
    private List<Card> makeCards() {
        List<Card> cards = new ArrayList<>();
        final int numPairs = 8;
        for (int tileNum = 0; tileNum != numPairs; tileNum++) {
            cards.add(new Card(tileNum, tileNum));
            cards.add(new Card(tileNum, tileNum));
        }

        return cards;
    }

    /**
     * Make an ordered MatchingBoard.
     */
    private void setUpBoard(List<Card> cards) {
        board = new MatchingBoard(cards, 4, 4);
    }

    /*UnitTests for Card Class*/

    /**
     * Test that the card's state is properly changed when flipCard is called.
     */
    @Test
    public void testFlipCard() {
        card = new Card(5, 4);
        assertEquals(1, card.getState());
        card.flipCard();
        assertEquals(0, card.getState());
        card.flipCard();
        assertEquals(1, card.getState());
    }

    @Test
    public void testCardComparison() {
        card = new Card(3, 4);
        Card card2 = new Card(3, 4);
        Card card3 = new Card(4, 4);
        assertEquals(0, card.compareTo(card2));
        assertNotEquals(0, card2.compareTo(card3));
    }

    /**
     * Test that the cardfront is returned when state is 0 and cardback when state is 1.
     */
    @Test
    public void testGetBackground() {
        card = new Card(5, 4);
        assertEquals(R.drawable.tile_0, card.getBackground());
        card.flipCard();
        assertEquals(R.drawable.tile_6, card.getBackground());
    }


    /*UnitTests for MatchingBoard Class*/

    /**
     * Test MatchingBoard constructor properly sets up variables.
     */
    @Test
    public void testBoardSetup() {
        List<Card> cards = makeCards();
        setUpBoard(cards);

        assertEquals(4, board.getNumRows());
        assertEquals(4, board.getNumCols());
        assertEquals(8, board.getNumMatches());
        assertEquals(25, board.getScore());
        board.setScore(26);
        assertEquals(26, board.getScore());
    }

    /**
     * Test getCard returns the correct card.
     */
    @Test
    public void testGetCard() {
        List<Card> cards = makeCards();
        setUpBoard(cards);

        assertEquals(cards.get(0), board.getCard(0, 0));
        assertEquals(cards.get(15), board.getCard(3, 3));

    }

    /**
     * Test CompareCards on a single flipped card.
     */
    @Test
    public void testCompareCardsSingle() {
        List<Card> cards = makeCards();
        setUpBoard(cards);
        board.flipCard(cards.get(0));
        board.checkCards();

        assertEquals(0, board.getCards()[0].getState());
    }

    /**
     * Test CompareCards on a pair of matching cards.
     */
    @Test
    public void testCompareMatchingCards() {
        List<Card> cards = makeCards();
        setUpBoard(cards);
        board.flipCard(cards.get(0));
        board.flipCard(cards.get(1));
        board.checkCards();

        assertEquals(2, board.getCards()[0].getState());
        assertEquals(2, board.getCards()[1].getState());

        board.flipCard(board.getCard(0, 2));
        board.flipCard(board.getCard(1, 0));
        board.checkCards();

        assertEquals(1, board.getCard(0, 2).getState());
        assertEquals(1, board.getCard(1, 0).getState());
    }

    /**
     * Test CompareCards on a pair of non-matching cards.
     */
    @Test
    public void testCompareDifferentCards() {
        List<Card> cards = makeCards();
        setUpBoard(cards);

        board.flipCard(board.getCard(0, 2));
        board.flipCard(board.getCard(1, 0));
        board.checkCards();

        assertEquals(1, board.getCard(0, 2).getState());
        assertEquals(1, board.getCard(1, 0).getState());
    }


    /*UnitTests for MatchingBoardManager Class*/

    /**
     * Test the MatchingBoardManager constructor properly sets up a new manager.
     */
    @Test
    public void testNewManager() {
        manager = new MatchingBoardManager(4, 4);
        assertEquals(4, manager.getBoard().numRows);
        assertEquals(4, manager.getBoard().numCols);
        assertEquals(25, manager.getBoard().score);
        assertEquals(16, manager.getBoard().numTiles());
        assertFalse(manager.puzzleSolved());
        assertTrue(manager.isValidTap(0));
    }
    /**
     * Test gameSolved on an unsolved board and a solved board.
     */
    @Test
    public void testGameSolved() {
        List<Card> cards = makeCards();
        setUpBoard(cards);
        manager = new MatchingBoardManager(board);

        assertFalse(manager.puzzleSolved());
        for (int i = 0; i != 16; i = i + 2) {
            board.flipCard(board.getCards()[i]);
            board.flipCard(board.getCards()[i + 1]);
            board.checkCards();
        }
        assertTrue(manager.puzzleSolved());
    }

    /**
     * Test IsValidTap on a face-down, face-up, and already matched card.
     */
    @Test
    public void testIsValidTap() {
        List<Card> cards = makeCards();
        setUpBoard(cards);
        manager = new MatchingBoardManager(board);

        assertTrue(manager.isValidTap(0));
        board.flipCard(board.getCards()[0]);
        assertFalse(manager.isValidTap(0));
        board.flipCard(board.getCards()[1]);
        board.checkCards();
        assertFalse(manager.isValidTap(1));
    }

    /**
     * Test that TouchFlip flips the card at the specified position if it is valid.
     */
    @Test
    public void testTouchFlip() {
        List<Card> cards = makeCards();
        setUpBoard(cards);
        manager = new MatchingBoardManager(board);

        manager.touchMove(5);
        assertEquals(0, board.getCard(1, 1).getState());
    }

}
