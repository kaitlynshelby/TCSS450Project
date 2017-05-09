package ksorum.uw.tacoma.edu.a450project;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URLEncoder;


/**
 * A simple {@link Fragment} subclass.
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
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignUpFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignUpFragment newInstance(String param1, String param2) {
        SignUpFragment fragment = new SignUpFragment();
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
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sign_up, container, false);

        mUserEmail = (EditText) v.findViewById(R.id.email_signup);
        mUserPassword = (EditText) v.findViewById(R.id.password_signup);
        mUserConfirmPassword = (EditText) v.findViewById(R.id.password_confirm);

        Button addUserButton = (Button) v.findViewById(R.id.create_account_button);
        addUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = buildCourseURL(v);
                mListener.addUser(url);
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
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

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

        }
        catch(Exception e) {
            Toast.makeText(v.getContext(), "Something wrong with the url" + e.getMessage(), Toast.LENGTH_LONG)
                    .show();
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
        void addUser(String url);
    }
}
