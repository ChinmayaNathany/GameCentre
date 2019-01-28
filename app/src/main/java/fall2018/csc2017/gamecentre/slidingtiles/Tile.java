package fall2018.csc2017.gamecentre.slidingtiles;

import android.support.annotation.NonNull;

import java.io.Serializable;

import fall2018.csc2017.gamecentre.ParentTile;
import fall2018.csc2017.gamecentre.R;

/**
 * A Tile in a sliding tiles puzzle.
 */
public class Tile extends ParentTile {
    public Tile(int id, int background, int size) {
        super(id, background, size);
    }

    /**
     * A tile with a background id in a size x size board; look up and set the id.
     *
     * @param backgroundId the background id.
     * @param size         the size of the board
     */
    public Tile(int backgroundId, int size) {
        super(size);
        id = backgroundId + 1;

        // This looks so ugly.
        switch (backgroundId + 1) {
            case 1:
                background = R.drawable.tile_1;
                break;
            case 2:
                background = R.drawable.tile_2;
                break;
            case 3:
                background = R.drawable.tile_3;
                break;
            case 4:
                background = R.drawable.tile_4;
                break;
            case 5:
                background = R.drawable.tile_5;
                break;
            case 6:
                background = R.drawable.tile_6;
                break;
            case 7:
                background = R.drawable.tile_7;
                break;
            case 8:
                background = R.drawable.tile_8;
                break;
            case 9:
                background = R.drawable.tile_9;
                break;
            case 10:
                background = R.drawable.tile_10;
                break;
            case 11:
                background = R.drawable.tile_11;
                break;
            case 12:
                background = R.drawable.tile_12;
                break;
            case 13:
                background = R.drawable.tile_13;
                break;
            case 14:
                background = R.drawable.tile_14;
                break;
            case 15:
                background = R.drawable.tile_15;
                break;
            case 16:
                background = R.drawable.tile_16;
                break;
            case 17:
                background = R.drawable.tile_17;
                break;
            case 18:
                background = R.drawable.tile_18;
                break;
            case 19:
                background = R.drawable.tile_19;
                break;
            case 20:
                background = R.drawable.tile_20;
                break;
            case 21:
                background = R.drawable.tile_21;
                break;
            case 22:
                background = R.drawable.tile_22;
                break;
            case 23:
                background = R.drawable.tile_23;
                break;
            case 24:
                background = R.drawable.tile_24;
                break;
            case 25:
                background = R.drawable.tile_25;
                break;
            default:
                background = R.drawable.tile_0;
        }
        if (id == size * size)
            background = R.drawable.tile_0;
    }
}
