package com.example.zing.fragmentbasics;

import android.media.Image;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.BundleCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.example.zing.fragmentbasics.BuildTypeConfig;
import com.example.zing.fragmentbasics.User;

/**
 * Created by Zing on 2016/8/29.
 */

public class ContentFragment extends Fragment {
    private static final String ARG_USER = "ARG_USER";
    private ImageLoader mImageLoader;
    private ImageLoader.ImageContainer mContainer;
    private User mUser;

    public void ContentFragment() {
    }

    // If Android decides to recreate the fragment later,
    // it's gonna call no-argument constructor so overloading the constructor is not solution
    // The way to pass arguments to the recreated fragment is to pass a bundle object to the "setArgument" method
    public static ContentFragment newInstance(User user) {
        ContentFragment fragment = new ContentFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_USER, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (BuildTypeConfig.DEBUGGER) Log.d(BuildTypeConfig.DEBUG_TAG, "ContentFragment onCreate()");
        mImageLoader = AppController.getImageLoader();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (BuildTypeConfig.DEBUGGER) Log.d(BuildTypeConfig.DEBUG_TAG, "ContentFragment onCreateView()");

        // If activity recreated (such as from screen rotate), restore
        // the previous user selection set by onSaveInstanceState().
        // This is primarily necessary when in the two-pane layout.
        if (savedInstanceState != null) {
            mUser = savedInstanceState.getParcelable(ARG_USER);
        }


        return inflater.inflate(R.layout.fragment_content, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        // During startup, check if there are arguments passed to the fragment.
        // onStart is a good place to do this because the layout has already been
        // applied to the fragment at this point so we can safely call the method
        // below that sets the article text.
        Bundle args = getArguments();
        if (args != null) {
            // Set article based on argument passed in
            updateUserView((User) args.getParcelable(ARG_USER));
        } else if (mUser != null) {
            // Set article based on saved instance state defined during onCreateView
            updateUserView(mUser);
        }
    }

    public void updateUserView(User user) {
        ImageView image = (ImageView) getActivity().findViewById(R.id.image);
        TextView name = (TextView) getActivity().findViewById(R.id.name);
        TextView email = (TextView) getActivity().findViewById(R.id.email);
        TextView home = (TextView) getActivity().findViewById(R.id.home);
        TextView mobile = (TextView) getActivity().findViewById(R.id.mobile);

        if (mContainer != null) {
            mContainer.cancelRequest();
            mContainer = null;
        }

        mContainer = mImageLoader.get(user.getimageUrl(), ImageLoader.getImageListener(image, 0, 0));
        name.setText(user.getName());
        email.setText(user.getEmail());
        home.setText(user.getHome());
        mobile.setText(user.getMobile());
        mUser = user;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the current user selection in case we need to recreate the fragment
        outState.putParcelable(ARG_USER, mUser);
    }
}
