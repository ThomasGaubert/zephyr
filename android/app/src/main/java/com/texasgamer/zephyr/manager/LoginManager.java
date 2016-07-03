package com.texasgamer.zephyr.manager;

import android.content.Context;
import android.preference.PreferenceManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.texasgamer.zephyr.Constants;
import com.texasgamer.zephyr.R;

public class LoginManager {

    private Context mContext;

    public LoginManager(Context context) {
        mContext = context;
    }

    public boolean shouldShowLoginCard() {
        return !isLoginCardHidden() && !isLoggedIn();
    }

    public boolean isLoggedIn() {
        if (!Constants.FIREBASE_LOGIN_ENABLED) {
            return false;
        }

        FirebaseAuth auth = FirebaseAuth.getInstance();
        return auth.getCurrentUser() != null;
    }

    public FirebaseUser getUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public boolean isLoginCardHidden() {
        if (!Constants.FIREBASE_LOGIN_ENABLED) {
            return true;
        }

        return PreferenceManager.getDefaultSharedPreferences(mContext)
                .getBoolean(mContext.getString(R.string.pref_hide_login_card), false);
    }

    public void setLoginCardHidden(boolean hideLoginCard) {
        PreferenceManager.getDefaultSharedPreferences(mContext)
                .edit()
                .putBoolean(mContext.getString(R.string.pref_hide_login_card), hideLoginCard)
                .apply();
    }
}
