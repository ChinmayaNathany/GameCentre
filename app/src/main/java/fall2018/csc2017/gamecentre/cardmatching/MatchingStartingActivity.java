package fall2018.csc2017.gamecentre.cardmatching;

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
import fall2018.csc2017.gamecentre.CreateAccountActivity;
import fall2018.csc2017.gamecentre.R;
import fall2018.csc2017.gamecentre.UserAccount;

/**
 * The Initial activity for the matching cards game.
 */
public class MatchingStartingActivity extends AppCompatActivity {

    /**
     * The main save file.
     */
    public static final String SAVE_FILENAME = "matching_save_file.ser";
    /**
     * A temporary save file.
     */
    public static final String TEMP_SAVE_FILENAME = "matching_save_file_tmp.ser";
    /**
     * The board manager.
     */
    private MatchingBoardManager manager;
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
    private UserAccount user;
    /**
     * Unique tag required for the intent extra.
     */
    public static final String EXTRA_MESSAGE = "fall2018.csc2017.gamecentre.extra.message";
    /**
     * Key-value pairs of username's and the corresponding UserAccount object.
     */
    private Map<String, UserAccount> result;
    /**
     * Location of the account activity data.
     */
    public static final String FILENAME = "/data/data/fall2018.csc2017.slidingtiles/files/AccountActivity.ser";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matching_starting);

        manager = new MatchingBoardManager(gameComplexity, gameComplexity);
        MatchingBoard board = (MatchingBoard) manager.getBoard();
        saveToFile(TEMP_SAVE_FILENAME);
        Intent intent = getIntent();
        user = (UserAccount) intent.getSerializableExtra(ChooseGameActivity.EXTRA_MESSAGE);

        setContentView(R.layout.activity_matching_starting);
        addStartButtonListener();
        addLoadButtonListener();
        addSaveButtonListener();
    }

    /**
     * Called when the user taps the scoreboard button.
     */
    public void scoreboardPressed(View view) {
        if (user != null) {
            Intent intent = new Intent(this, MatchingScoreBoardActivity.class);
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
     * Save the current MatchingBoardManager when save button is clicked.
     */
    private void addSaveButtonListener() {
        Button button = findViewById(R.id.MatchingSaveButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveToFile(SAVE_FILENAME);
                saveToFile(TEMP_SAVE_FILENAME);
                makeToastSavedText();
            }
        });
    }

    /**
     * Display save message Toast on successful save.
     */
    private void makeToastSavedText() {
        Toast.makeText(this, "Game Saved", Toast.LENGTH_SHORT).show();
    }

    /**
     * Load the last saved game when load button is clicked.
     */
    private void addLoadButtonListener() {
        Button loadButton = findViewById(R.id.MatchingLoadButton);
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
                    if (user.getMatchingSavedGame() == null) {
                        manager = new MatchingBoardManager(gameComplexity, gameComplexity);
                    } else {
                        manager = user.getMatchingSavedGame();
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
     * Display loaded message on successful load.
     */
    private void makeToastLoadedText() {
        Toast.makeText(this, "Loaded Game", Toast.LENGTH_SHORT).show();
    }

    /**
     * Load the MatchingBoardManager from saveFilename.
     *
     * @param saveFilename The file to load from.
     */
    private void loadFromFile(String saveFilename) {
        try {
            InputStream inputStream = this.openFileInput(saveFilename);
            if (inputStream != null) {
                ObjectInputStream input = new ObjectInputStream(inputStream);
                manager = (MatchingBoardManager) input.readObject();
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
     * Create and switch to a new game when new game button is clicked.
     */
    private void addStartButtonListener() {
        Button button = findViewById(R.id.MatchingStartButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager = new MatchingBoardManager(gameComplexity, gameComplexity);
                MatchingBoard matchingBoard = (MatchingBoard) manager.getBoard();
                switchToGame();
            }
        });
    }

    /**
     * Switch the view to the game screen.
     */
    private void switchToGame() {
        Intent tmp = new Intent(this, MatchingMainActivity.class);
        saveToFile(MatchingStartingActivity.TEMP_SAVE_FILENAME);
        tmp.putExtra(EXTRA_MESSAGE, user);
        startActivity(tmp);
    }

    /**
     * Save the current game to filename
     *
     * @param fileName The name of the file to save to.
     */
    private void saveToFile(String fileName) {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(
                    this.openFileOutput(fileName, MODE_PRIVATE));
            outputStream.writeObject(manager);
            outputStream.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
}
