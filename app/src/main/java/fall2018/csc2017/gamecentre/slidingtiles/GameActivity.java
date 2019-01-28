package fall2018.csc2017.gamecentre.slidingtiles;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import fall2018.csc2017.gamecentre.CreateAccountActivity;
import fall2018.csc2017.gamecentre.CustomAdapter;
import fall2018.csc2017.gamecentre.GestureDetectGridView;
import fall2018.csc2017.gamecentre.R;
import fall2018.csc2017.gamecentre.UserAccount;

/**
 * The game activity.
 */
public class GameActivity extends AppCompatActivity implements Observer {

    private static final String FILENAME = "/data/data/fall2018.csc2017.slidingtiles/files/AccountActivity.ser";
    /**
     * The board manager.
     */
    private BoardManager boardManager;

    /**
     * The buttons to display.
     */
    private ArrayList<Button> tileButtons;
    /**
     * The main save file.
     */
    public static final String SAVE_FILENAME = "save_file.ser";
    /**
     * A temporary save file.
     */
    public static final String TEMP_SAVE_FILENAME = "save_file_tmp.ser";

    /**
     * Constants for swiping directions. Should be an enum, probably.
     */
    public static final int UP = 1;
    public static final int DOWN = 2;
    public static final int LEFT = 3;
    public static final int RIGHT = 4;

    // Grid View and calculated column height and width based on device size
    private GestureDetectGridView gridView;
    private static int columnWidth, columnHeight;
    /**
     * The currently logged in user.
     */
    private UserAccount user;
    /**
     * The hashmap of all users.
     */
    Map<String, UserAccount> result;

    /**
     * Set up the background image for each button based on the master list
     * of positions, and then call the adapter to set the view.
     */
    // Display
    public void display() {
        updateTileButtons();
        gridView.setAdapter(new CustomAdapter(tileButtons, columnWidth, columnHeight));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        user = (UserAccount) intent.getSerializableExtra("user");
        loadFromFile(StartingActivity.TEMP_SAVE_FILENAME);
        init();
    }

    /**
     * Initializes the screen to display the current board.
     */
    void init() {
        createTileButtons(this);
        setContentView(R.layout.activity_main);

        TextView scoreTextView = findViewById(R.id.textView9);
        String newScore = "Score: " + (boardManager.getBoard().getScore());
        scoreTextView.setText(newScore);

        // Add View to activity
        gridView = findViewById(R.id.grid);
        gridView.setNumColumns(boardManager.getBoard().getNumCols());
        gridView.setBoardManager(boardManager);
        boardManager.getBoard().addObserver(this);
        // Observer sets up desired dimensions as well as calls our display function
        gridView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        gridView.getViewTreeObserver().removeOnGlobalLayoutListener(
                                this);
                        int displayWidth = gridView.getMeasuredWidth();
                        int displayHeight = gridView.getMeasuredHeight();

                        columnWidth = displayWidth / boardManager.getBoard().getNumCols();
                        columnHeight = displayHeight / boardManager.getBoard().getNumRows();

                        display();
                    }
                });
        addSaveButtonListener();
        addUndoButtonListener();
    }

    /**
     * Create the buttons for displaying the tiles.
     *
     * @param context the context
     */
    private void createTileButtons(Context context) {
        Board board = (Board) boardManager.getBoard();
        tileButtons = new ArrayList<>();
        for (int row = 0; row != board.getNumRows(); row++) {
            for (int col = 0; col != board.getNumCols(); col++) {
                Button tmp = new Button(context);
                tmp.setBackgroundResource(board.getTile(row, col).getBackground());
                this.tileButtons.add(tmp);
            }
        }
    }

    /**
     * Update the backgrounds on the buttons to match the tiles.
     */
    private void updateTileButtons() {
        Board board = (Board) boardManager.getBoard();
        int nextPos = 0;
        for (Button b : tileButtons) {
            int row = nextPos / board.getNumRows();
            int col = nextPos % board.getNumCols();
            b.setBackgroundResource(board.getTile(row, col).getBackground());
            nextPos++;
        }
    }

    /**
     * Dispatch onPause() to fragments.
     */
    @Override
    protected void onPause() {
        super.onPause();
        saveToFile(StartingActivity.TEMP_SAVE_FILENAME);
    }

    /**
     * Load the board manager from fileName.
     *
     * @param fileName the name of the file
     */
    private void loadFromFile(String fileName) {

        try {
            InputStream inputStream = this.openFileInput(fileName);
            if (inputStream != null) {
                ObjectInputStream input = new ObjectInputStream(inputStream);
                boardManager = (BoardManager) input.readObject();
                inputStream.close();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        } catch (ClassNotFoundException e) {
            Log.e("login activity", "File contained unexpected data type: " + e.toString());
        }
    }

    /**
     * Save the board manager to fileName.
     *
     * @param fileName the name of the file
     */
    public void saveToFile(String fileName) {
        try {
            FileOutputStream fos = openFileOutput(fileName, MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(boardManager);
            oos.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    /**
     * Display that a game was saved successfully.
     */
    private void makeToastSavedText() {
        Toast.makeText(this, "Game Saved", Toast.LENGTH_SHORT).show();
    }

    private void addSaveButtonListener() {
        Button saveButton = findViewById(R.id.SaveButtonGameScreen);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToFile(SAVE_FILENAME);
                saveToFile(TEMP_SAVE_FILENAME);
                user.setTilesSavedGame(boardManager);
                try {
                    // read object from file
                    FileInputStream fis = new FileInputStream(FILENAME);
                    ObjectInputStream ois = new ObjectInputStream(fis);
                    result = (Map<String, UserAccount>) ois.readObject();
                    ois.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                result.put(user.getUsername(), user);
                try {
                    // write object to file
                    FileOutputStream fos = openFileOutput("AccountActivity.ser", MODE_APPEND);
                    ObjectOutputStream oos = new ObjectOutputStream(fos);
                    oos.writeObject(result);
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                saveUserToFile();
                makeToastSavedText();
            }
        });
    }

    /**
     * Display that the user cannot undo from the current game state.
     */
    private void makeToastNoUndoText() {
        Toast.makeText(this, "Can't Undo", Toast.LENGTH_SHORT).show();
    }

    private void addUndoButtonListener() {
        Button undoButton = findViewById(R.id.UndoButton);
        final GameActivity gameActivity = this;
        undoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Undo undo = new Undo(gameActivity.boardManager);
                if (!undo.canUndo()) {
                    makeToastNoUndoText();
                } else if (undo.canUndo()) {
                    undo.performUndo();
                    saveToFile(TEMP_SAVE_FILENAME);
                    init();
                }
                //undo.boards.remove(undo.boards.size()-1);
                try {
                    FileOutputStream outputStream = new FileOutputStream("/data/data/fall2018.csc2017.slidingtiles/files/AutoSave.ser");
                    BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(bufferedOutputStream);
                    objectOutputStream.writeObject(undo.boards);
                    objectOutputStream.close();
                } catch (IOException e) {
                    Log.e("Exception", "File write failed: " + e.toString());
                }
            }
        });
    }

    @Override
    public void update(Observable o, Object arg) {
        display();
        TextView scoreTextView = findViewById(R.id.textView9);
        String newScore = "Score: " + boardManager.getBoard().getScore();
        scoreTextView.setText(newScore);

        if (boardManager.puzzleSolved() && boardManager.getBoard().getScore() > user.getMaxScore()) {
            user.setMaxScore(boardManager.getBoard().getScore());
            saveToFile(SAVE_FILENAME);
            saveToFile(TEMP_SAVE_FILENAME);
            user.setTilesSavedGame(boardManager);
            try {
                // read object from file
                FileInputStream fis = new FileInputStream(FILENAME);
                ObjectInputStream ois = new ObjectInputStream(fis);
                result = (Map<String, UserAccount>) ois.readObject();
                ois.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            result.put(user.getUsername(), user);
            try {
                // write object to file
                FileOutputStream fos = openFileOutput("AccountActivity.ser", MODE_APPEND);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(result);
                oos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Save the current game to the user and save the user to AccountActivity file.
     */
    public void saveUserToFile() {
        saveToFile(SAVE_FILENAME);
        saveToFile(TEMP_SAVE_FILENAME);
        if (user != null) {
            user.setTilesSavedGame(boardManager);
        }
        try {
            // read object from file
            FileInputStream fis = new FileInputStream(FILENAME);
            ObjectInputStream ois = new ObjectInputStream(fis);
            result = (Map<String, UserAccount>) ois.readObject();
            ois.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (user != null) {
            result.put(user.getUsername(), user);
        }
        try {
            // write object to file
            FileOutputStream fos = openFileOutput("AccountActivity.ser", MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(result);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
