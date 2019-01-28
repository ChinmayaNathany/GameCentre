package fall2018.csc2017.gamecentre.simon;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
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
public class SimonGameActivity extends AppCompatActivity implements Observer {

    static final String FILENAME = "/data/data/fall2018.csc2017.slidingtiles/files/AccountActivity.ser";
    /**
     * The board manager.
     */
    private SimonBoardManager boardManager;

    /**
     * The buttons to display.
     */
    private ArrayList<Button> tileButtons;
    /**
     * The main save file.
     */
    static final String SAVE_FILENAME = "save_file.ser";
    /**
     * A temporary save file.
     */
    static final String TEMP_SAVE_FILENAME = "save_file_tmp.ser";

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
    private Map<String, UserAccount> result;
    /**
     * Main handler for delaying tasks.
     */
    private Handler handler = new Handler();

    /**
     * Set up the background image for each button based on the master list
     * of positions, and then call the adapter to set the view.
     */
    // Display
    public void display() {
        updateTileButtons();
        gridView.setAdapter(new CustomAdapter(tileButtons, columnWidth, columnHeight));
        updateScoreView();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        user = (UserAccount) intent.getSerializableExtra(SimonStartingActivity.EXTRA_MESSAGE);
        loadFromFile(SimonStartingActivity.TEMP_SAVE_FILENAME);
        init();
    }

    private void init() {
        createTileButtons(this);
        setContentView(R.layout.activity_simon_main);

        updateScoreView();


        // Add View to activity
        gridView = findViewById(R.id.grid);
        gridView.setNumColumns(boardManager.getBoard().getNumCols());
        gridView.setSimonBoardManager(boardManager);
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

                        columnWidth = displayWidth / 2;
                        columnHeight = displayHeight / 2;

                        display();
                    }
                });
        addSaveButtonListener();
        addUndoButtonListener();
        addSubmitButtonListener();

    }

    @Override
    protected void onResume() {
        super.onResume();
        showPattern(boardManager.getRound());
    }

    /**
     * Create the buttons for displaying the tiles.
     *
     * @param context the context
     */
    private void createTileButtons(Context context) {
        SimonBoard board = (SimonBoard) boardManager.getBoard();
        tileButtons = new ArrayList<>();
        for (int pos = 0; pos != 4; pos++) {
            Button tmp = new Button(context);
            tmp.setBackgroundResource(board.getTile(pos).getBackground());
            this.tileButtons.add(tmp);
        }
    }

    /**
     * Update the backgrounds on the buttons to match the tiles.
     */
    private void updateTileButtons() {
        boardManager.updateTiles();
        SimonBoard board = (SimonBoard) boardManager.getBoard();
        int nextPos = 0;
        for (Button button : tileButtons) {
            button.setBackgroundResource(board.getTile(nextPos).getBackground());
            nextPos++;
        }
    }

    /**
     * Dispatch onPause() to fragments.
     */
    @Override
    protected void onPause() {
        super.onPause();
        saveToFile(SimonStartingActivity.TEMP_SAVE_FILENAME);
    }

    /**
     * Flash the pattern to be memorized.
     *
     * @param size the size of the pattern to be flashed minus one.
     */
    public void showPattern(int size) {
        List<Runnable> list = new ArrayList<>();
        for (int i = 0; i <= size + 1; i++) {
            Runnable run = new Runnable() {
                @Override
                public void run() {
                    display();
                }
            };
            list.add(run);
        }
        for (int i = 0; i <= size + 1; i++) {
            handler.postDelayed(list.get(i), 600 * (i + 1));
        }
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

    /**
     * Add save button listener.
     */
    private void addSaveButtonListener() {
        Button saveButton = findViewById(R.id.SimonSaveButtonGameScreen);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserToFile();
                makeToastSavedText();
            }
        });
    }

    /**
     * Create UndoButton listener.
     */
    private void addUndoButtonListener() {
        Button undoButton = findViewById(R.id.SimonUndoButton);
        undoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int size = boardManager.getUserPattern().size();
                if (size > 0) {
                    boardManager.getUserPattern().remove(size - 1);
                    makeToastSuccessfulUndoText();
                } else {
                    makeToastNoUndoText();
                }
            }
        });
    }

    /**
     * Create SubmitButton listener.
     */
    private void addSubmitButtonListener() {
        Button submitButton = findViewById(R.id.SubmitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!boardManager.getPlayerTurn()) {
                    makeToastNotNowText();
                } else {
                    if (boardManager.UserCurrentPatternCompareLength() <= 0) {
                        if (boardManager.UserCurrentPatternEqual()) {
                            boardManager.getBoard().setScore(boardManager.getBoard().getScore() + 1);
                            boardManager.switchTurn();
                            boardManager.nextRound();
                            boardManager.newRound();
                            showPattern(boardManager.getRound());
                        } else {
                            if (user != null && boardManager.getRound() - 1 > user.getSimonMaxScore()) {
                                user.setSimonMaxScore(boardManager.getRound() - 1);
                            }
                            makeToastGameOverText();
                            saveUserToFile();
                            finish();
                        }
                    } else {
                        makeTooShortText();
                    }
                }
            }
        });
    }

    /**
     * Display that the user cannot undo from the current game state.
     */
    private void makeToastGameOverText() {
        Toast.makeText(this, "Game Over!", Toast.LENGTH_SHORT).show();
    }

    /**
     * Display that the user cannot undo from the current game state.
     */
    private void makeToastNoUndoText() {
        Toast.makeText(this, "Can't Undo", Toast.LENGTH_SHORT).show();
    }

    /**
     * Display that the undo was successful.
     */
    private void makeToastSuccessfulUndoText() {
        Toast.makeText(this, "Successful Undo", Toast.LENGTH_SHORT).show();
    }

    /**
     * Display that the user cannot click on tiles right now.
     */
    private void makeToastNotNowText() {
        Toast.makeText(this, "Not Now!", Toast.LENGTH_SHORT).show();
    }

    /**
     * Display that the user's chosen pattern is too short.
     */
    private void makeTooShortText() {
        String output = String.format("Pattern too short; Choose %d more tile(s)", boardManager.UserCurrentPatternCompareLength());
        Toast.makeText(this, output, Toast.LENGTH_SHORT).show();
    }

    /**
     * Update the score for the user to see.
     */
    void updateScoreView() {
        TextView scoreTextView = findViewById(R.id.SimonScoreText);
        String newScore = "Score: " + boardManager.getBoard().getScore();
        scoreTextView.setText(newScore);
    }

    /**
     * Display that the user has pressed a tile.
     *
     * @param colour the colour of the tile pressed
     */
    private void makeToastPressedText(String colour) {
        String output = String.format("Pressed %s", colour);
        Toast.makeText(this, output, Toast.LENGTH_SHORT).show();
    }

    /**
     * Toast to the screen the move made by the user.
     *
     * @param position the tile touched by user
     */
    public void userTouch(int position) {
        String colour = "BLANK";
        switch (position) {
            case 0:
                colour = "Red";
                break;
            case 1:
                colour = "Green";
                break;
            case 2:
                colour = "Blue";
                break;
            case 3:
                colour = "Yellow";
                break;
        }
        makeToastPressedText(colour);
    }

    /**
     * Save the current game to the user and save the user to AccountActivity file.
     */
    public void saveUserToFile() {
        saveToFile(SAVE_FILENAME);
        saveToFile(TEMP_SAVE_FILENAME);
        if (user != null) {
            user.setSimonSavedGame(boardManager);
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

    @Override
    public void update(Observable o, Object arg) {
        userTouch(boardManager.getUserPattern().get(boardManager.getUserPattern().size() - 1));

        if (boardManager.puzzleSolved() && boardManager.getBoard().getScore() > user.getMaxScore()) {
            user.setMaxScore(boardManager.getBoard().getScore());
            saveToFile(SAVE_FILENAME);
            saveToFile(TEMP_SAVE_FILENAME);
            user.setSimonSavedGame(boardManager);
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
}
