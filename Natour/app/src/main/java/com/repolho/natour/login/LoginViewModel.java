package com.repolho.natour.login;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.ObservableBoolean;

public class LoginViewModel extends BaseObservable {
    private static String TAG = "LoginViewModel";
    private Context mContext;
    private LoginNavigator mNavigator;

    final ObservableBoolean error = new ObservableBoolean(false);

    public LoginViewModel(Context context) {
        mContext = context;
    }

    void setNavigator(LoginNavigator navigator) {
        mNavigator = navigator;
    }

    void onActivityDestroyed() {
        // Clear references to avoid potential memory leaks.
        mNavigator = null;
    }

    /**
     * Can be called by the Data Binding Library
     */
    public void loginClicked() {
        if (mNavigator != null) {
            mNavigator.makeLoginWithGoogle();
        }
    }
}
