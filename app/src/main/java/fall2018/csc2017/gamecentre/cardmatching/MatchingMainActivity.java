package fall2018.csc2017.gamecentre.cardmatching;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import fall2018.csc2017.gamecentre.CreateAccountActivity;
import fall2018.csc2017.gamecentre.CustomAdapter;
import fall2018.csc2017.gamecentre.GestureDetectGridView;
import fall2018.csc2017.gamecentre.R;
import fall2018.csc2017.gamecentre.UserAccount;

public class MatchingMainActivity extends AppCompatActivity implements Observer {

    /**
     * The file storing the created accounts.
     */
    private static final String FILENAME = "/data/data/fall2018.csc2017.slidingtiles/files/AccountActivity.ser";
    /**
     * The manager for the game being played.
     */
    private MatchingBoardManager manager;
    /**
     * The buttons to display on the screen.
     */
    private ArrayList<Button> cardButtons;
    /**
     * The main save file.
     */
    public static final String SAVE_FILENAME = "matching_save_file.ser";
    /**
     * A temporary save file.
     */
    public static final String TEMP_SAVE_FILENAME = "matching_save_file_tmp.ser";
    /**
     * The gridview
     */
    private GestureDetectGridView gridView;
    /**
     * The calculated width and height for the screen size.
     */
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.activity_matching_main);
        user = (UserAccount) intent.getSerializableExtra(MatchingStartingActivity.EXTRA_MESSAGE);
        loadFromFile(MatchingStartingActivity.TEMP_SAVE_FILENAME);
        init();
    }

    /**
     * Initialize and display the buttons for each card.
     */
    private void init() {
        createCardButtons(this);
        setContentView(R.layout.activity_matching_main);

        TextView scoreTextView = (TextView) findViewById(R.id.MatchingScoreText);
        String newScore = "Score: " + (manager.getBoard().getScore());
        scoreTextView.setText(newScore);

        // Add View to activity
        gridView = findViewById(R.id.matchingGrid);
        gridView.setNumColumns(manager.getBoard().getNumCols());
        gridView.setMatchingBoardManager(manager);
        manager.getBoard().addObserver(this);
        // Observer sets up desired dimensions as well as calls our display function
        gridView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        gridView.getViewTreeObserver().removeOnGlobalLayoutListener(
                                this);
                        int displayWidth = gridView.getMeasuredWidth();
                        int displayHeight = gridView.getMeasuredHeight();

                        columnWidth = displayWidth / manager.getBoard().getNumCols();
                        columnHeight = displayHeight / manager.getBoard().getNumRows();

                        display();
                    }
                });
        addSaveButtonListener();
    }

    /**
     * Create the buttons for displaying the cards.
     *
     * @param context The context.
     */
    private void createCardButtons(Context context) {
        MatchingBoard board = (MatchingBoard) manager.getBoard();
        cardButtons = new ArrayList<>();
        for (int row = 0; row != board.getNumRows(); row++) {
            for (int col = 0; col != board.getNumCols(); col++) {
                Button tmp = new Button(context);
                tmp.setBackgroundResource(board.getCard(row, col).getBackground());
                this.cardButtons.add(tmp);
            }
        }
    }

    /**
     * Set up the background image for each button based on the master list
     * of positions, and then call the adapter to set the view.
     */
    public void display() {
        updateTileButtons();
        gridView.setAdapter(new CustomAdapter(cardButtons, columnWidth, columnHeight));
    }

    /**
     * Update the backgrounds of the card buttons to match the cards.
     */
    private void updateTileButtons() {
        MatchingBoard board = (MatchingBoard) manager.getBoard();
        int nextPos = 0;
        for (Button b : cardButtons) {
            int row = nextPos / board.getNumRows();
            int col = nextPos % board.getNumCols();
            b.setBackgroundResource(board.getCard(row, col).getBackground());
            nextPos++;
        }
    }

    /**
     * Dispatch onPause() to fragments.
     */
    @Override
    protected void onPause() {
        super.onPause();
        saveToFile(MatchingStartingActivity.TEMP_SAVE_FILENAME);
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
     * Save the board manager to fileName.
     *
     * @param fileName the name of the file
     */
    public void saveToFile(String fileName) {
        try {
            FileOutputStream fos = openFileOutput(fileName, MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(manager);
            oos.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    /**
     * Save the current game to the user and save the user to AccountActivity file.
     */
    public void saveUserToFile() {
        saveToFile(SAVE_FILENAME);
        saveToFile(TEMP_SAVE_FILENAME);
        if (user != null) {
            user.setMatchingSavedGame(manager);
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

    /**
     * Display that a game was saved successfully.
     */
    private void makeToastSavedText() {
        Toast.makeText(this, "Game Saved", Toast.LENGTH_SHORT).show();
    }

    /**
     * Create the save button and save when pressed.
     */
    private void addSaveButtonListener() {
        Button saveButton = findViewById(R.id.matchingSaveButtonGameScreen);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserToFile();
                makeToastSavedText();
            }
        });
    }

    @Override
    public void update(Observable o, Object arg) {
        final MatchingMainActivity game = this;
        display();
        TextView scoreTextView = findViewById(R.id.MatchingScoreText);
        String newScore = "Score: " + manager.getBoard().getScore();
        scoreTextView.setText(newScore);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                ((MatchingBoard) manager.getBoard()).checkCards();
                if (manager.puzzleSolved()) {
                    if (manager.getBoard().getScore() > user.getMatchingMaxScore())
                        user.setMatchingMaxScore(manager.getBoard().getScore());
                    Toast.makeText(game, "You Win!!", Toast.LENGTH_SHORT).show();
                }
                saveUserToFile();
            }
        };
        handler.postDelayed(runnable, 500);


    }
}
