package ksorum.uw.tacoma.edu.a450project;

import android.content.Intent;
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


/**
 * The main activity which hosts the fragments which allow a user to login or sign up.
 *
 * @author Kaitlyn Kinerk, Jasmine Dacones
 * @version 1.0
 */
public class HomeActivity extends AppCompatActivity implements LoginFragment.OnLoginUser,
        SignUpFragment.OnAddUser,
        MainFragment.OnMainFragmentInteractionListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new MainFragment())
                .addToBackStack(null)
                .commit();
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
<<<<<<< HEAD

    @Override
    public void addUser(String url) {
=======

    @Override
    public void addUser(String url) {
        UserTask task = new UserTask();
        task.execute(new String[]{url.toString()});
    }

    @Override
    public void loginUser(String url) {
>>>>>>> 4f77dd5aaa771b1bac3a442d362ac5be8bf65ff9
        UserTask task = new UserTask();
        task.execute(new String[]{url.toString()});
    }


    @Override
<<<<<<< HEAD
    public void loginUser(String url) {
        UserTask task = new UserTask();
        task.execute(new String[]{url.toString()});
    }
=======
    public void onMainFragmentInteraction(Uri uri) {
>>>>>>> 4f77dd5aaa771b1bac3a442d362ac5be8bf65ff9

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
                } else {
                    Toast.makeText(getApplicationContext(), "Failed to complete user task: "
                                    + jsonObject.get("error")
                            , Toast.LENGTH_LONG)
                            .show();
                    getSupportFragmentManager().popBackStackImmediate();
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Please enter valid data" +
                        e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

    }
}
