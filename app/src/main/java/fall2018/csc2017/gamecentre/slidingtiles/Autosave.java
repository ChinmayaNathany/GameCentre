package fall2018.csc2017.gamecentre.slidingtiles;

import android.app.Activity;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import fall2018.csc2017.gamecentre.ParentBoard;

/**
 * Implements Autosave functionality.
 */
public class Autosave extends Activity {

    /**
     * Keeps track of previous moves.
     */
    private ArrayList<ParentBoard> movesList;
    /**
     * Location of the auto save data.
     */
    private static final String FILENAME = "/data/data/fall2018.csc2017.slidingtiles/files/AutoSave.ser";

    /**
     * Auto save a movesList that has been passed in.
     */
    public Autosave(ArrayList<ParentBoard> moves) {
        this.movesList = moves;
        autosaveToFile();
    }

    /**
     * Auto save a new empty movesList if a movesList hasn't been passed in.
     */
    public Autosave() {
        this.movesList = new ArrayList<>();
        autosaveToFile();
    }

    /**
     * Returns the file path of the save location
     */
    public String getFilename() {
        return FILENAME;
    }

    /**
     * Saves the list of all previous moves made to a Serializable File
     */
    public void autosaveToFile() {
        try {
            FileOutputStream outputStream = new FileOutputStream(FILENAME);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(bufferedOutputStream);
            objectOutputStream.writeObject(this.movesList);
            objectOutputStream.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    /**
     * Loads the list of all previous moves made from the Serializable File
     */
    public ArrayList<ParentBoard> autoloadFromFile() {
        try {
            FileInputStream var2 = new FileInputStream(FILENAME);
            BufferedInputStream var3 = new BufferedInputStream(var2);
            ObjectInputStream var4 = new ObjectInputStream(var3);
            this.movesList = (ArrayList<ParentBoard>) var4.readObject();
            var4.close();
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        } catch (ClassNotFoundException e) {
            Log.e("login activity", "File contained unexpected data type: " + e.toString());
        }
        return movesList;
    }
}
