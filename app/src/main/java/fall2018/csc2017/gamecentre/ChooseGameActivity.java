package fall2018.csc2017.gamecentre;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import fall2018.csc2017.gamecentre.cardmatching.MatchingStartingActivity;
import fall2018.csc2017.gamecentre.simon.SimonStartingActivity;
import fall2018.csc2017.gamecentre.slidingtiles.StartingActivity;

/**
 * Choose which game you want to play.
 */
public class ChooseGameActivity extends AppCompatActivity {
    /**
     * The current user.
     */
    UserAccount user;
    /**
     * Unique tag required for the intent extra.
     */
    public static final String EXTRA_MESSAGE = "fall2018.csc2017.gamecentre.extra.message";

    /**
     * Create the activity and dispatch to fragments
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_game);

        Intent intent = getIntent();
        user = (UserAccount) intent.getSerializableExtra(CreateAccountActivity.EXTRA_MESSAGE);
        TextView name;
        if (user != null) {
            name = findViewById(R.id.name);
            name.setText(String.format("Welcome, %s", user.getUsername()));
        }
        addSlidingTilesButtonListener();
        addMatchingCardsButtonListener();
        addSimonButtonListener();
    }

    /**
     * Activate the MatchingCards button.
     */
    private void addMatchingCardsButtonListener() {
        Button cardButton = findViewById(R.id.matchingCardsButton);
        cardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToMatchingCards();
            }
        });
    }

    /**
     * Switch to the Matching Cards Starting Activity view.
     */
    private void switchToMatchingCards() {
        Intent tmp = new Intent(this, MatchingStartingActivity.class);
        tmp.putExtra(EXTRA_MESSAGE, user);
        startActivity(tmp);
    }

    /**
     * Activate the SlidingTiles button.
     */
    private void addSlidingTilesButtonListener() {
        Button guestButton = findViewById(R.id.slidingTilesButton);
        guestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToSlidingTiles();
            }
        });
    }

    /**
     * Switch to the Sliding Tiles starting activity view.
     */
    private void switchToSlidingTiles() {
        Intent tmp = new Intent(this, StartingActivity.class);
        tmp.putExtra(EXTRA_MESSAGE, user);
        startActivity(tmp);
    }

    /**
     * Activate the Simon button.
     */
    private void addSimonButtonListener() {
        Button cardButton = findViewById(R.id.simonButton);
        cardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToSimon();
            }
        });
    }

    /**
     * Switch to the Simon Starting Activity view.
     */
    private void switchToSimon() {
        Intent tmp = new Intent(this, SimonStartingActivity.class);
        tmp.putExtra(EXTRA_MESSAGE, user);
        startActivity(tmp);
    }
}
