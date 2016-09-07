package com.example.zing.fragmentbasics;

import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements HeadlinesFragment.OnHeadlineSelectedListener{

    private String[] mUsers;
    public ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (BuildTypeConfig.DEBUGGER) Log.d(BuildTypeConfig.DEBUG_TAG, "MainActivity onCreate()");
        setContentView(R.layout.activity_main);

        // Check whether the activity is using the layout version with
        // the fragment_container FrameLayout. If so, we must add the first fragment
        if (findViewById(R.id.fragment_container) != null) {
            // However, if we're being restored from the previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragment
            if (savedInstanceState != null) {
                return ;
            }

            setMainFragment(new HeadlinesFragment());
        }

    }

    private void setMainFragment(Fragment mainFragment) {
        if (mainFragment != null) {
            if (getSupportFragmentManager().findFragmentById(R.id.fragment_container) == null) {
                // Add the fragment to the 'fragment_container' FrameLayout
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, mainFragment).commit();
            } else {
                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack so the user can navigate back
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, mainFragment)
                        .addToBackStack(null).commit();
            }
        }
    }

    @Override
    public void onUserSelected(User user) {
        setMainFragment(ContentFragment.newInstance(user));
    }
}
