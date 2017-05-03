package ksorum.uw.tacoma.edu.a450project;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity
        implements MainFragment.OnFragmentInteractionListener, LoginFragment.OnFragmentInteractionListener,
        SignUpFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager manager = getSupportFragmentManager();
        final FragmentTransaction transaction = manager.beginTransaction();

        if (savedInstanceState == null) {
            manager.beginTransaction()
                    .add(R.id.fragment_container, new MainFragment())
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }
}
