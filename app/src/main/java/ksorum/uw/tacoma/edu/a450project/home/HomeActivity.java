package ksorum.uw.tacoma.edu.a450project.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import ksorum.uw.tacoma.edu.a450project.R;
import ksorum.uw.tacoma.edu.a450project.inventory.LandingPageActivity;
import ksorum.uw.tacoma.edu.a450project.shoppinglist.ShoppingListActivity;


/**
 * The main activity which hosts the fragments which allow a user to login or sign up.
 *
 * @author Kaitlyn Kinerk, Jasmine Dacones
 * @version 1.0
 */
public class HomeActivity extends AppCompatActivity implements LoginFragment.OnLoginUser,
        SignUpFragment.OnAddUser,
        MainFragment.OnMainFragmentInteractionListener {

    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mSharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.LOGIN_PREFS)
                , Context.MODE_PRIVATE);

        if (!mSharedPreferences.getBoolean(getString(R.string.LOGGEDIN), false)) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, new MainFragment())
                    .commit();
        } else {
            Intent i = new Intent(this, ShoppingListActivity.class);
            startActivity(i);
            finish();
        }

    }

    /**
     * Launches the login fragment.
     *
     * @param v the View object
     */
    public void launchLogin(View v) {

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new LoginFragment())
                .addToBackStack(null)
                .commit();
    }


    /**
     * Launches the sign up fragment.
     *
     * @param v the View object
     */
    public void launchSignUp(View v) {

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new SignUpFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void addUser(String url) {
        UserTask task = new UserTask();
        task.execute(new String[]{url.toString()});
    }

    @Override
    public void loginUser(String url, String email) {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            UserTask task = new UserTask();
            task.execute(new String[]{url.toString()});
        }
        else {
            Toast.makeText(this, "No network connection available. Cannot authenticate user",
                    Toast.LENGTH_SHORT) .show();
            return;
        }

        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(getString(R.string.LOGGEDIN), true);
        editor.putString("user", email);
        editor.commit();
    }


    @Override
    public void onMainFragmentInteraction(Uri uri) {
    }

    /**
     * Launches AsyncTask to execute the webservice to add a user or log in a user.
     */
    private class UserTask extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... urls) {
            String response = "";

            HttpURLConnection urlConnection = null;
            for (String url : urls) {
                try {
                    URL urlObject = new URL(url);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();

                    InputStream content = urlConnection.getInputStream();

                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }

                } catch (Exception e) {
                    response = "Unable to perform action, Reason: "
                            + e.getMessage();
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            return response;
        }


        /**
         * Checks to see if there was a problem with the URL(Network) which is when an
         * exception is caught. Tries to call the parse Method and checks to see if it was successful.
         * If not, displays the exception.
         *
         * @param result the returned JSON formatted as a String
         */
        @Override
        protected void onPostExecute(String result) {
            // Something wrong with the network or the URL.
            try {
                JSONObject jsonObject = new JSONObject(result);
                String status = (String) jsonObject.get("result");
                if (status.equals("success")) {
                    Intent intent = new Intent(HomeActivity.this, LandingPageActivity.class);
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(getApplicationContext(), "Failed to complete user task: "
                                    + jsonObject.get("error")
                            , Toast.LENGTH_LONG)
                            .show();
                    getSupportFragmentManager().popBackStackImmediate();
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Please enter valid data " +
                        e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

    }
}
