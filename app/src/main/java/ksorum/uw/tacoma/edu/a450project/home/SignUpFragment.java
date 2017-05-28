package ksorum.uw.tacoma.edu.a450project.home;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.net.URLEncoder;

import ksorum.uw.tacoma.edu.a450project.R;


/**
 * The fragment from which a user can sign up to be a registered user of the app.
 * <p>
 * Activities that contain this fragment must implement the
 * {@link SignUpFragment.OnAddUser} interface
 * to handle interaction events.
 * Use the {@link SignUpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUpFragment extends Fragment {

    private final static String COURSE_ADD_URL
            = "http://cssgate.insttech.washington.edu/~ksorum/adduser.php?";


    private EditText mUserEmail;
    private EditText mUserPassword;
    private EditText mUserConfirmPassword;

    private OnAddUser mListener;

    public SignUpFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SignUpFragment.
     */
    public static SignUpFragment newInstance() {
        SignUpFragment fragment = new SignUpFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sign_up, container, false);

        // get user data from EditText views
        mUserEmail = (EditText) v.findViewById(R.id.email_signup);
        mUserPassword = (EditText) v.findViewById(R.id.password_signup);
        mUserConfirmPassword = (EditText) v.findViewById(R.id.password_confirm);

        // add functionality to the create account button
        Button addUserButton = (Button) v.findViewById(R.id.create_account_button);
        addUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = buildCourseURL(v);
                mListener.addUser(url, mUserEmail.getText().toString());
            }
        });


        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAddUser) {
            mListener = (OnAddUser) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnMainFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * Builds the url which will be used by the webservice to register a user.
     *
     * @param v the View object
     * @return the url to be used by the register user webservice
     */
    private String buildCourseURL(View v) {

        StringBuilder sb = new StringBuilder(COURSE_ADD_URL);

        try {

            String email = mUserEmail.getText().toString();
            sb.append("email=");
            sb.append(URLEncoder.encode(email, "UTF-8"));


            String password = mUserPassword.getText().toString();
            sb.append("&password=");
            sb.append(URLEncoder.encode(password, "UTF-8"));


            String confirmPassword = mUserConfirmPassword.getText().toString();
            sb.append("&confirm_password=");
            sb.append(URLEncoder.encode(confirmPassword, "UTF-8"));

            Log.i("SignUpFragment", sb.toString());

        } catch (Exception e) {

        }
        return sb.toString();
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnAddUser {
        void addUser(String url, String email);
    }
}