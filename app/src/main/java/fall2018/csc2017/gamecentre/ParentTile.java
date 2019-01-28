package fall2018.csc2017.gamecentre;

import android.support.annotation.NonNull;

import java.io.Serializable;

public class ParentTile implements Comparable<ParentTile>, Serializable {
    /**
     * The number of rows/columns.
     */
    protected int size;

    /**
     * The background id to find the tile image.
     */
    protected int background;

    /**
     * The unique id.
     */
    protected int id;

    /**
     * Create a new ParentTile with id, background, and size.
     *
     * @param id         The ParentTiles id.
     * @param background The ParentTiles assigned background.
     * @param size       The size of the intended board.
     */
    public ParentTile(int id, int background, int size) {
        this.id = id;
        this.background = background;
        this.size = size;
    }

    /**
     * Create a new ParentTile with id, background, and size.
     *
     * @param size The ParentTiles size
     */
    public ParentTile(int size) {
        this.size = size;
    }


    /**
     * Return the background id.
     *
     * @return the background id
     */
    public int getBackground() {
        return background;
    }

    /**
     * Return the tile id.
     *
     * @return the tile id
     */
    public int getId() {
        return id;
    }

    /**
     * Compare the ids and return the difference
     *
     * @return the difference between the ids
     */
    @Override
    public int compareTo(@NonNull ParentTile o) {
        return o.id - this.id;
    }
}
