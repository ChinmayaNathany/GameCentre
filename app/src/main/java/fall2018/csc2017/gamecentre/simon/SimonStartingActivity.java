package fall2018.csc2017.gamecentre.simon;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
public class SimonStartingActivity extends AppCompatActivity {

    /**
     * The main save file.
     */
    static final String SAVE_FILENAME = "simon_save_file.ser";
    /**
     * A temporary save file.
     */
    static final String TEMP_SAVE_FILENAME = "simon_save_file_tmp.ser";
    /**
     * The board manager.
     */
    private SimonBoardManager boardManager;
    /**
     * The current logged in user.
     */
    private UserAccount user;
    /**
     * Unique tag required for the intent extra.
     */
    static final String EXTRA_MESSAGE = "fall2018.csc2017.gamecentre.extra.message";
    /**
     * Key-value pairs of username's and the corresponding UserAccount object.
     */
    private Map<String, UserAccount> result;
    /**
     * Location of the account activity data.
     */
    static final String FILENAME = "/data/data/fall2018.csc2017.slidingtiles/files/AccountActivity.ser";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simon_starting);

        boardManager = new SimonBoardManager(2, 2);
        SimonBoard board = (SimonBoard) boardManager.getBoard();
        saveToFile(TEMP_SAVE_FILENAME);
        Intent intent = getIntent();
        user = (UserAccount) intent.getSerializableExtra(ChooseGameActivity.EXTRA_MESSAGE);

        setContentView(R.layout.activity_simon_starting);
        addStartButtonListener();
        addLoadButtonListener();
        addSaveButtonListener();
    }

    /**
     * Called when the user taps the scoreboard button.
     */
    public void scoreboardPressed(View view) {
        if (user != null) {
            Intent intent = new Intent(this, SimonScoreBoardActivity.class);
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
     * Activate the start button.
     */
    private void addStartButtonListener() {
        Button startButton = findViewById(R.id.SimonStartButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boardManager = new SimonBoardManager(2, 2);
                switchToGame();
            }
        });
    }

    /**
     * Activate the load button.
     */
    private void addLoadButtonListener() {
        Button loadButton = findViewById(R.id.SimonLoadButton);
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
                    if (user.getSimonSavedGame() == null) {
                        boardManager = new SimonBoardManager(2, 2);
                    } else {
                        boardManager = user.getSimonSavedGame();
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
        Button saveButton = findViewById(R.id.SimonSaveButton);
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
     * Switch to the SimonGameActivity view to play the game.
     */
    private void switchToGame() {
        Intent tmp = new Intent(this, SimonGameActivity.class);
        saveToFile(SimonStartingActivity.TEMP_SAVE_FILENAME);
        tmp.putExtra(EXTRA_MESSAGE, user);
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
                boardManager = (SimonBoardManager) input.readObject();
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
