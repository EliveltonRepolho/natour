package com.repolho.natour.firebase;

import com.google.firebase.database.DataSnapshot;

/**
 * Created by saksham on 26/6/17.
 */

public interface FirebaseCallBacks {
    void onNewMessage(DataSnapshot dataSnapshot);
}