package fall2018.csc2017.gamecentre.slidingtiles;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;

import fall2018.csc2017.gamecentre.ChooseGameActivity;
import fall2018.csc2017.gamecentre.R;
import fall2018.csc2017.gamecentre.UserAccount;

/**
 * The initial activity for the sliding puzzle tile game.
 */
public class StartingActivity extends AppCompatActivity {

    /**
     * The main save file.
     */
    public static final String SAVE_FILENAME = "save_file.ser";
    /**
     * A temporary save file.
     */
    public static final String TEMP_SAVE_FILENAME = "save_file_tmp.ser";
    /**
     * The board manager.
     */
    private BoardManager boardManager;
    /**
     * The game complexity.
     */
    private int gameComplexity = 4;
    /**
     * Unique tag for settings intent reply.
     */
    public static final int COMPLEXITY_REQUEST = 1;
    /**
     * The current logged in user.
     */
    UserAccount user;
    /**
     * Unique tag required for the intent extra.
     */
    public static final String EXTRA_MESSAGE = "fall2018.csc2017.slidingtiles.extra.message";
    /**
     * Key-value pairs of username's and the corresponding UserAccount object.
     */
    Map<String, UserAccount> result;
    /**
     * Location of the account activity data.
     */
    public static final String FILENAME = "/data/data/fall2018.csc2017.slidingtiles/files/AccountActivity.ser";

    /**
     * Get the game complexity.
     */
    public int getGameComplexity() {
        return gameComplexity;
    }

    /**
     * Set the game complexity.
     */
    public void setGameComplexity(int gameComplexity) {
        this.gameComplexity = gameComplexity;
    }

    /**
     * Return a BoardManager that is solvable given the location of the blank tile, the total
     * number of inversions, and an existing BoardManager to replace if the BoardManager does
     * not have a solvable board
     */
    private BoardManager createSolvableBoard(BoardManager boardManager,
                                             int blankLocation, int totalInversions) {
        BoardManager result = boardManager;
        Board board = (Board) boardManager.getBoard();
        if (getGameComplexity() % 2 == 1) {
            while (totalInversions % 2 == 1) {
                result = new BoardManager(getGameComplexity(), getGameComplexity());
                board = (Board) result.getBoard();
                totalInversions = board.getInversions();
            }
        } else {
            while (blankLocation % 2 == totalInversions % 2) {
                result = new BoardManager(getGameComplexity(), getGameComplexity());
                board = (Board) result.getBoard();
                blankLocation = board.getLocation(getGameComplexity() * getGameComplexity())[0];
                totalInversions = board.getInversions();
            }
        }
        return result;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starting_);

        boardManager = new BoardManager(getGameComplexity(), getGameComplexity());
        Board board = (Board) boardManager.getBoard();
        int blankLocation = board.getLocation(getGameComplexity() * getGameComplexity())[0];
        int totalInversions = board.getInversions();
        boardManager = createSolvableBoard(boardManager, blankLocation, totalInversions);
        saveToFile(TEMP_SAVE_FILENAME);
        Intent intent = getIntent();
        user = (UserAccount) intent.getSerializableExtra(ChooseGameActivity.EXTRA_MESSAGE);

        setContentView(R.layout.activity_starting_);
        addStartButtonListener();
        addLoadButtonListener();
        addSaveButtonListener();
    }

    /**
     * Called when the user taps the scoreboard button.
     */
    public void scoreboardPressed(View view) {
        if (user != null) {
            Intent intent = new Intent(this, ScoreBoardActivity.class);
            intent.putExtra("currentUser", user);
            startActivity(intent);
        } else {
            makeToastLogin();
        }
    }

    /**
     * Display "Login to view Scoreboard".
     */
    private void makeToastLogin() {
        Toast.makeText(this, "Login to view Scoreboard", Toast.LENGTH_SHORT).show();
    }

    /**
     * Called when the user taps the scoreboard button.
     */
    public void settingsPressed(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivityForResult(intent, COMPLEXITY_REQUEST);
    }

    /**
     * Handles the data in the return intent from SettingsActivity.
     *
     * @param requestCode Code for the SecondActivity request.
     * @param resultCode  Code that comes back from SecondActivity.
     * @param data        Intent data sent back from SecondActivity.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == COMPLEXITY_REQUEST && resultCode == RESULT_OK) {
            setGameComplexity(Integer.parseInt(data.getStringExtra(SettingsActivity.EXTRA_REPLY)));
        }

    }

    /**
     * Activate the start button.
     */
    private void addStartButtonListener() {
        Button startButton = findViewById(R.id.StartButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boardManager = new BoardManager(getGameComplexity(), getGameComplexity());
                Board board = (Board) boardManager.getBoard();
                int blankLocation = board.getLocation(getGameComplexity() * getGameComplexity())[0];
                int totalInversions = board.getInversions();
                boardManager = createSolvableBoard(boardManager, blankLocation, totalInversions);
                switchToGame();
            }
        });
    }

    /**
     * Activate the load button.
     */
    private void addLoadButtonListener() {
        Button loadButton = findViewById(R.id.LoadButton);
        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user == null) {
                    loadFromFile(SAVE_FILENAME);
                    saveToFile(TEMP_SAVE_FILENAME);
                    makeToastLoadedText();
                } else {
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
                    user = result.get(user.getUsername());
                    if (user.getTilesSavedGame() == null) {
                        boardManager = new BoardManager(getGameComplexity(), getGameComplexity());
                    } else {
                        boardManager = user.getTilesSavedGame();
                        saveToFile(SAVE_FILENAME);
                        saveToFile(TEMP_SAVE_FILENAME);
                        makeToastLoadedText();
                    }
                }
                switchToGame();
            }
        });
    }

    /**
     * Display that a game was loaded successfully.
     */
    private void makeToastLoadedText() {
        Toast.makeText(this, "Loaded Game", Toast.LENGTH_SHORT).show();
    }

    /**
     * Activate the save button.
     */
    private void addSaveButtonListener() {
        Button saveButton = findViewById(R.id.SaveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToFile(SAVE_FILENAME);
                saveToFile(TEMP_SAVE_FILENAME);
                makeToastSavedText();
            }
        });
    }

    /**
     * Display that a game was saved successfully.
     */
    private void makeToastSavedText() {
        Toast.makeText(this, "Game Saved", Toast.LENGTH_SHORT).show();
    }

    /**
     * Read the temporary board from disk.
     */
    @Override
    protected void onResume() {
        super.onResume();
        loadFromFile(TEMP_SAVE_FILENAME);
    }

    /**
     * Switch to the GameActivity view to play the game.
     */
    private void switchToGame() {
        Intent tmp = new Intent(this, GameActivity.class);
        saveToFile(StartingActivity.TEMP_SAVE_FILENAME);
        tmp.putExtra("user", user);
        startActivity(tmp);
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
            ObjectOutputStream outputStream = new ObjectOutputStream(
                    this.openFileOutput(fileName, MODE_PRIVATE));
            outputStream.writeObject(boardManager);
            outputStream.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
}
