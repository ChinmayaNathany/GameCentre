package fall2018.csc2017.gamecentre;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * The initial activity for the sliding puzzle tile app.
 */

public class AccountActivity extends AppCompatActivity {

    /**
     * The main save file with all user information.
     */
    public static String FILENAME;
    /**
     * Used when writing changes to user information save file.
     */
    FileOutputStream fos;

    /**
     * Dispatch OnCreate() to fragments
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        FILENAME = getFilesDir().getPath() + "/AccountActivity.ser";

        Map<String, UserAccount> users = new HashMap<>();
        // adding example users + their scores
        ArrayList<String> examples = new ArrayList();
        examples.add("chinmaya");
        examples.add("thomas");
        examples.add("rayah");
        examples.add("magomed");
        for (int i = 0; i != examples.size() - 1; i++) {
            String name = examples.get(i);
            UserAccount user = new UserAccount(name, name);
            user.setMatchingMaxScore(3 * i + 1);
            user.setSimonMaxScore(i + 1);
            users.put(name, user);
        }
        try {
            // read object from file
            FileInputStream fis = new FileInputStream(FILENAME);
            ObjectInputStream ois = new ObjectInputStream(fis);
            users = (Map<String, UserAccount>) ois.readObject();
            ois.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(
                    this.openFileOutput("AccountActivity.ser", MODE_PRIVATE));
            outputStream.writeObject(users);
            outputStream.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
        addNewAccountButtonListener();
        addLoginButtonListener();
        addGuestButtonListener();
    }

    /**
     * Activate the Guest button.
     */
    private void addGuestButtonListener() {
        Button guestButton = findViewById(R.id.guestButton);
        guestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToGuest();
            }
        });
    }

    /**
     * Activate the login button.
     */
    private void addLoginButtonListener() {
        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToLogin();
            }
        });
    }

    /**
     * Activate the New Account button.
     */
    private void addNewAccountButtonListener() {
        Button newAccountButton = findViewById(R.id.newAccountButton);
        newAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToCreateAccount();
            }
        });
    }

    /**
     * Switch to the CreateAccountActivity view to create an account.
     */
    private void switchToCreateAccount() {
        Intent tmp = new Intent(this, CreateAccountActivity.class);
        startActivityForResult(tmp, 0);
    }

    /**
     * Switch to the LoginActivity view to login.
     */
    private void switchToLogin() {
        Intent tmp = new Intent(this, LoginActivity.class);
        startActivityForResult(tmp, 0);
    }

    /**
     * Switch to the GuestActivity view to play as a guest.
     */
    private void switchToGuest() {
        Intent tmp = new Intent(this, ChooseGameActivity.class);
        startActivity(tmp);
    }

    /**
     * Handles the data in the return intent from any latter activities.
     *
     * @param requestCode the request code
     * @param resultCode  the result code
     * @param data        the intent data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            String message = "Logged Out";
            Toast toast = Toast.makeText(getApplicationContext(),
                    message,
                    Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
