package fall2018.csc2017.gamecentre.simon;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Map;

import fall2018.csc2017.gamecentre.R;
import fall2018.csc2017.gamecentre.UserAccount;

public class SimonScoreBoardActivity extends AppCompatActivity {

    /**
     * The file containing all registered users.
     */
    public static String FILENAME;
    /**
     * List of all UserAccounts.
     */
    private Map<String, UserAccount> result;
    /**
     * A list of the top 5 scores across all users.
     */
    private int[] maxScores = new int[5];
    /**
     * A list of corresponding usernames.
     */
    private String[] maxUsers = new String[5];
    /**
     * The currently logged in user.
     */
    private UserAccount currentUser;

    /**
     * Dispatch onCreate() to fragments.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simon_score_board);

        FILENAME = getFilesDir().getPath() + "/AccountActivity.ser";

        Intent intent = getIntent();
        currentUser = (UserAccount) intent.getSerializableExtra("currentUser");

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

        currentUser = result.get(currentUser.getUsername());
        getMaxScores(result);
        setScoreboardTable();

        TextView personalBest = (TextView) findViewById(R.id.simonPersonalBest);
        String scoreText = "Personal Best: " + currentUser.getSimonMaxScore();
        personalBest.setText(scoreText);
    }

    /**
     * Return the best scores across all accounts
     *
     * @param userAccounts map containing all users and their data
     */
    public void getMaxScores(Map<String, UserAccount> userAccounts) {
        for (Map.Entry<String, UserAccount> x : userAccounts.entrySet()
                ) {
            String name = x.getValue().getUsername();
            int score = x.getValue().getSimonMaxScore();
            boolean scoreHigher = false;
            int i = 0;

            while (i < 5 && !scoreHigher) {
                if (maxUsers[i] == null | score > maxScores[i]) {
                    for (int j = 3; j >= i; j--) {
                        maxUsers[j + 1] = maxUsers[j];
                        maxScores[j + 1] = maxScores[j];
                    }
                    maxScores[i] = score;
                    maxUsers[i] = name;
                    scoreHigher = true;
                }
                i++;
            }
        }
    }

    /**
     * Create the scoreboard table.
     */
    public void setScoreboardTable() {
        TableLayout scoreboardTable = (TableLayout) findViewById(R.id.simonScoreboardTable);

        for (int i = 0, j = scoreboardTable.getChildCount(); i < j; i++) {
            TableRow row = (TableRow) scoreboardTable.getChildAt(i);
            TextView username = (TextView) row.getChildAt(0);
            TextView score = (TextView) row.getChildAt(1);

            username.setText(maxUsers[i]);
            if (maxScores[i] != 0)
                score.setText(String.valueOf(maxScores[i]));
        }
    }
}
