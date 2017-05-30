package ksorum.uw.tacoma.edu.a450project.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.net.URLEncoder;

import ksorum.uw.tacoma.edu.a450project.R;


/**
 *
 * A fragment which allows a user to log in to the app.
 *
 * Activities that contain this fragment must implement the
 * {@link LoginFragment.OnLoginUser} interface
 * to handle interaction events.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class LoginFragment extends Fragment {

    /** URL for location of user login credentials */
    public final static String LOGIN_URL
            = "http://cssgate.insttech.washington.edu/~ksorum/login.php?";

    /** Field to enter in user email address */
    private EditText mUserEmail;
    /** Field to enter in user password */
    private EditText mUserPassword;
    /** Listener to log user into their account */
    private OnLoginUser mListener;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LoginFragment.
     */
    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_login, container, false);

        // Get user-entered data from EditText views
        mUserEmail = (EditText) v.findViewById(R.id.email_login);
        mUserPassword = (EditText) v.findViewById(R.id.pwd_login);

        // Add functionality to login button
        Button loginUserButton = (Button) v.findViewById(R.id.login_button);
        loginUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = buildCourseURL(v);
                mListener.loginUser(url, mUserEmail.getText().toString());
            }
        });

        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnLoginUser) {
            mListener = (OnLoginUser) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnLoginUser");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    /**
     * Build the url which will be used by the webservice.
     *
     * @param v the View object
     * @return the url to be used by the webservice
     */
    private String buildCourseURL(View v) {

        StringBuilder sb = new StringBuilder(LOGIN_URL);

        try {

            // encode user data
            String email = mUserEmail.getText().toString();
            sb.append("email=");
            sb.append(URLEncoder.encode(email, "UTF-8"));


            String password = mUserPassword.getText().toString();
            sb.append("&password=");
            sb.append(URLEncoder.encode(password, "UTF-8"));



            Log.i("SignUpFragment", sb.toString());

        }
        catch(Exception e) {

        }
        return sb.toString();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     */
    public interface OnLoginUser {
        void loginUser(String url, String email);
    }
}
