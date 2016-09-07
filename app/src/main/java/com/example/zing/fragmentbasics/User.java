package com.example.zing.fragmentbasics;

import org.json.JSONException;
import org.json.JSONObject;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Zing on 2016/8/27.
 */

public class User implements Parcelable {
    private String mName;
    private String mEmail;
    private String mHome;
    private String mMobile;
    private String mImageUrl;

    public User(String name, String email, String home, String mobile, String imageUrl) {
        this.mName = name;
        this.mEmail = email;
        this.mHome = home;
        this.mMobile = mobile;
        this.mImageUrl = imageUrl;
    }

    public static User newInstance(JSONObject jsonObject) throws JSONException {
        String name = jsonObject.optString("name");
        String email = jsonObject.optString("email");
        JSONObject phone = jsonObject.getJSONObject("phone");
        String home = phone.optString("home");
        String mobile = phone.optString("mobile");
        String imageUrl = jsonObject.optString("imageUrl");
        return new User(name, email, home, mobile, imageUrl);
    }

    public String getName() {
        return this.mName;
    }

    public String getEmail() {
        return this.mEmail;
    }

    public String getHome() {
        return this.mHome;
    }

    public String getMobile() {
        return this.mMobile;
    }

    public String getimageUrl() { return this.mImageUrl; }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeString(mEmail);
        dest.writeString(mHome);
        dest.writeString(mMobile);
        dest.writeString(mImageUrl);
    }

    protected User(Parcel in) {
        mName = in.readString();
        mEmail = in.readString();
        mHome = in.readString();
        mMobile = in.readString();
        mImageUrl = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

}
