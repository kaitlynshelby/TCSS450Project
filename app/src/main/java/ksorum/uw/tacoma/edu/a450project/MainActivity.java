package ksorum.uw.tacoma.edu.a450project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * The onClick method for opening the Login screen.
     * @param view
     */
    public void openLoginActivity(View view) {
        Intent login_intent = new Intent(this, LoginActivity.class);
        startActivity(login_intent);
    }

    /**
     * The onClick method for opening the Sign Up screen.
     * @param view
     */
    public void openSignUpActivity(View view) {
        Intent signup_intent = new Intent(this, SignUpActivity.class);
        startActivity(signup_intent);
    }
}
