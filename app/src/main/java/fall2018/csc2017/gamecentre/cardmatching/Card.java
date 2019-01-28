package fall2018.csc2017.gamecentre.cardmatching;

import fall2018.csc2017.gamecentre.R;
import fall2018.csc2017.gamecentre.slidingtiles.Tile;

public class Card extends Tile {
    /**
     * The image of the cards backside.
     */
    private int cardBack;
    /**
     * The image of the cards frontside.
     */
    private int cardFront;
    /**
     * The cards current state. 1 if facedown, 0 if faceup.
     */
    private int state = 1;

    /**
     * A card with a background id in a size x size board; look up and set the id.
     *
     * @param backgroundId The background Id for setting the background image.
     * @param size         The size of the intended board.
     */
    public Card(int backgroundId, int size) {
        super(backgroundId, size);
        cardBack = R.drawable.tile_0;
        this.cardFront = super.background;
    }

    /**
     * Flip the card to the opposite side.
     */
    public void flipCard() {
        if (state == 0)
            state = 1;
        else if (state == 1)
            state = 0;
    }

    /**
     * Return which side of the card should be facing upwards.
     *
     * @return the background
     */
    public int getBackground() {
        if (state == 1)
            return this.cardBack;
        else
            return this.cardFront;
    }

    /**
     * Return the value of the card.
     *
     * @return the id
     */
    public int getId() {
        return this.id;
    }

    /**
     * Return if this card is faceup or facedown.
     *
     * @return the cards state.
     */
    public int getState() {
        return this.state;
    }

    /**
     * Change the state of the card to newState.
     *
     * @param newState The new state for the card.
     */
    public void setState(int newState) {
        this.state = newState;
    }
}
