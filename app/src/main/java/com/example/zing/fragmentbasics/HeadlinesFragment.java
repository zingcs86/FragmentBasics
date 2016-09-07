package com.example.zing.fragmentbasics;

import android.app.Activity;
import android.os.Build;
import android.support.v4.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Zing on 2016/8/27.
 */

public class HeadlinesFragment extends ListFragment {
    private final String dataUrl = "http://api.androidhive.info/volley/person_array.json";
    private String imageUrl = "http://i.imgur.com/7spzG.png";
    OnHeadlineSelectedListener mCallback;
    private HeadlinesListAdapter mAdapter;
    private static final String ARG_USER_LIST = "ARG_USER_LIST";
    private ArrayList<User> mUserArrayList;

    public HeadlinesFragment() {

    }

    // The container Activity must implement this interface so the fragment can deliver messages
    public interface OnHeadlineSelectedListener {
        public void onUserSelected(User user);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (BuildTypeConfig.DEBUGGER)
            Log.d(BuildTypeConfig.DEBUG_TAG, "HeadlinesFragment onCreate()");

        mAdapter = mAdapter == null ? new HeadlinesListAdapter(mCallback) : mAdapter;
        // Create an adapter for the list view, using headlines array
        setListAdapter(mAdapter);

        // We don't need do jsonRequest again when rotating the screen
        if (savedInstanceState != null) {
            mUserArrayList = savedInstanceState.getParcelableArrayList(ARG_USER_LIST);
            mAdapter.swapData(mUserArrayList);
        } else {
            mUserArrayList = new ArrayList<>();
            makeJsonRequest();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (BuildTypeConfig.DEBUGGER) Log.d(BuildTypeConfig.DEBUG_TAG, "HeadlinesFragment onAttach()");
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception.
        try {
            mCallback = (OnHeadlineSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // Save the previous state
        outState.putParcelableArrayList(ARG_USER_LIST, mUserArrayList);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (BuildTypeConfig.DEBUGGER) Log.d(BuildTypeConfig.DEBUG_TAG, "HeadlinesFragment onDetach()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (BuildTypeConfig.DEBUGGER) Log.d(BuildTypeConfig.DEBUG_TAG, "HeadlinesFragment onDestroy()");
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // Notify the parent activity of selected item
        mCallback.onUserSelected(mUserArrayList.get(position));

        getListView().setItemChecked(position, true);
    }

    private void makeJsonRequest() {
        if (BuildTypeConfig.DEBUGGER) Log.d(BuildTypeConfig.DEBUG_TAG, "makeJsonRequest()");
        JsonArrayRequest jsonRequest = new JsonArrayRequest(Request.Method.GET, dataUrl, (String) null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int index = 0; index < response.length(); index++) {
                        response.getJSONObject(index).put("imageUrl", imageUrl);
                        User user = User.newInstance(response.getJSONObject(index));
                        mUserArrayList.add(user);
                    }

                    // After finishing jsonRequest, we can swap list into adapter.
                    mAdapter.swapData(mUserArrayList);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Volley" ,"Error: " + error.getMessage());
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
        AppController.getInstance().addToRequestQueue(jsonRequest);
    }
}
