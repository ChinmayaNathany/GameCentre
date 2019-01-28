package fall2018.csc2017.gamecentre;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Log into an already existing account.
 */
public class LoginActivity extends AccountManagementActivity {
    /**
     * The Sign In button.
     */
    Button signIn;

    /**
     * Create the activity and dispatch to fragments
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Intent replyIntent = new Intent();
        replyIntent.putExtra(EXTRA_REPLY, "temp");
        setResult(RESULT_CANCELED, replyIntent);

        addSignInButtonListener();
    }

    /**
     * Activate the SignInActivity button.
     */
    private void addSignInButtonListener() {
        signIn = findViewById(R.id.signIn);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usernameInput = findViewById(R.id.usernameText);
                passwordInput = findViewById(R.id.passwordText);
                username = usernameInput.getText().toString();
                password = passwordInput.getText().toString();

                if (!(result.containsKey(username) && result.get(username).checkPassword(password))) {
                    String message = "Incorrect username or password";
                    Toast toast = Toast.makeText(getApplicationContext(),
                            message,
                            Toast.LENGTH_SHORT);
                    toast.show();
                }
                if (result.containsKey(username) && result.get(username).checkPassword(password)) {
                    Intent replyIntent = new Intent();
                    replyIntent.putExtra(EXTRA_REPLY, "temp");
                    setResult(RESULT_OK, replyIntent);

                    user = result.get(username);
                    String message = "Sign In Successful";
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
