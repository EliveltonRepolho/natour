package com.repolho.natour.user;

import android.content.Context;
import android.databinding.ObservableField;
import android.widget.ImageView;

import com.repolho.natour.BaseViewModel;
import com.repolho.natour.firebase.FirebaseManager;
import com.repolho.natour.model.Author;
import com.repolho.natour.util.GlideUtil;

public class UserViewModel extends BaseViewModel {
    private static String TAG = "UserViewModel";
    private UserNavigator mNavigator;

    public final ObservableField<String> userNameLabel = new ObservableField<>();

    public UserViewModel(Context context) {
        super(context);
    }

    void setNavigator(UserNavigator navigator) {
        mNavigator = navigator;
    }

    void onActivityDestroyed() {
        // Clear references to avoid potential memory leaks.
        mNavigator = null;
    }

    public void start(ImageView userPhoto) {
        Author profile = FirebaseManager.getAuthor();
        GlideUtil.loadProfileIcon(profile.getProfilePicture(), userPhoto);
        userNameLabel.set(profile.getFullName());
    }

    /**
     * Can be called by the Data Binding Library
     */
    public void logoutClicked() {
        if (mNavigator != null) {
            mNavigator.onLogout();
        }
    }

    public void restartIntroClicked() {
        if (mNavigator != null) {
            mNavigator.onRestartIntro();
        }
    }
}
