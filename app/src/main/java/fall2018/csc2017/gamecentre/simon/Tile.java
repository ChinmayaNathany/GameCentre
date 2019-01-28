package fall2018.csc2017.gamecentre.simon;

import fall2018.csc2017.gamecentre.ParentTile;
import fall2018.csc2017.gamecentre.R;

/**
 * A Tile in a sliding tiles puzzle.
 */
public class
Tile extends ParentTile {
    /**
     * Set a new background.
     *
     * @param backgroundId the background id
     */
    public void setBackground(int backgroundId) {
        backgroundSwitch(backgroundId);
    }

    public Tile(int id, int background, int size) {
        super(id, background, size);
    }

    /**
     * A tile with a background id in a size x size board; look up and set the id.
     *
     * @param backgroundId the background id.
     */
    public Tile(int backgroundId, int size) {
        super(size);
        id = backgroundId;
        backgroundSwitch(backgroundId);
    }

    public void backgroundSwitch(int backgroundId) {
        switch (backgroundId) {
            case 0:
                background = R.drawable.off_1;
                break;
            case 1:
                background = R.drawable.off_2;
                break;
            case 2:
                background = R.drawable.off_3;
                break;
            case 3:
                background = R.drawable.off_4;
                break;
            case 4:
                background = R.drawable.on_1;
                break;
            case 5:
                background = R.drawable.on_2;
                break;
            case 6:
                background = R.drawable.on_3;
                break;
            case 7:
                background = R.drawable.on_4;
                break;
        }
    }
}
