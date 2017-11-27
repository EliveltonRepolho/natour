package com.repolho.natour.user;

import android.content.Context;
import android.databinding.ObservableField;
import android.text.TextUtils;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
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

    public void answerOnlineForm() {
        if (mNavigator != null) {
            FirebaseManager.getWebPageResponsesRef().addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    String url = String.valueOf(snapshot.getValue());
                    if(!TextUtils.isEmpty(url) && !"null".equals(url)){
                        mNavigator.answerOnlineForm(url);
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
    }
}
