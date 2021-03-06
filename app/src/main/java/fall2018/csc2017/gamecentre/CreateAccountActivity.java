package fall2018.csc2017.gamecentre;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * Create a new account.
 */
public class CreateAccountActivity extends AccountManagementActivity {
    /**
     * The Create Account button.
     */
    Button createAccount;

    /**
     * Create the activity and dispatch to fragments
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        Intent replyIntent = new Intent();
        replyIntent.putExtra(EXTRA_REPLY, "temp");
        setResult(RESULT_CANCELED, replyIntent);

        addCreateAccountActivityButtonListener();
    }

    /**
     * Activate the CreateAccountActivity button.
     */
    private void addCreateAccountActivityButtonListener() {
        createAccount = findViewById(R.id.createAccount);
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usernameInput = findViewById(R.id.usernameText);
                passwordInput = findViewById(R.id.passwordText);
                username = usernameInput.getText().toString();
                password = passwordInput.getText().toString();
                UserAccount newUser = new UserAccount(username, password);

                if (result.containsKey(username)) {
                    String message = "Username not available";
                    Toast toast = Toast.makeText(getApplicationContext(),
                            message,
                            Toast.LENGTH_SHORT);
                    toast.show();
                }
                if (!(result.containsKey(username))) {
                    Intent replyIntent = new Intent();
                    replyIntent.putExtra(EXTRA_REPLY, "temp");
                    setResult(RESULT_OK, replyIntent);

                    result.put(username, newUser);
                    user = newUser;
                    try {
                        // write object to file
                        FileOutputStream fos = new FileOutputStream(FILENAME);
                        ObjectOutputStream oos = new ObjectOutputStream(fos);
                        oos.writeObject(result);
                        oos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String message = "Account Creation Successful";
                    Toast toast = Toast.makeText(getApplicationContext(),
                            message,
                            Toast.LENGTH_SHORT);
                    toast.show();
                    finish();
                    switchToChooseGameActivity();
                }
            }
        });
    }
}
