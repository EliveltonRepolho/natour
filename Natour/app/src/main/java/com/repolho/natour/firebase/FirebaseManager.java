package com.repolho.natour.firebase;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.repolho.natour.R;
import com.repolho.natour.model.Author;
import com.repolho.natour.model.Image;
import com.repolho.natour.model.Point;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by saksham on 26/6/17.
 */

public class FirebaseManager {
    private static final String TAG = "FirebaseManager";

    private volatile static FirebaseManager sFirebaseManager;
    private Context mContext;

    public static synchronized FirebaseManager getInstance(Context context) {
        if(sFirebaseManager == null) {
            synchronized (FirebaseManager.class) {
                sFirebaseManager = new FirebaseManager(context);
            }
        }
        return sFirebaseManager;
    }

    private FirebaseManager(Context context){
        this.mContext =context;
    }

    public static DatabaseReference getBaseRef() {
        return FirebaseDatabase.getInstance().getReference();
    }

    public static String getCurrentUserId() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            return user.getUid();
        }
        return null;
    }
    public static Author getAuthor() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return null;
        return new Author(user.getDisplayName(), user.getPhotoUrl().toString(), user.getUid());
    }


    public static DatabaseReference getCurrentUserRef() {
        String uid = getCurrentUserId();
        if (uid != null) {
            return getBaseRef().child("users").child(getCurrentUserId());
        }
        return null;
    }

    public static String getUsersPath() {
        return "users/";
    }

    public static DatabaseReference getPointsRef() {
        return getBaseRef().child("point");
    }

    public static DatabaseReference getWebPageResponsesRef() {
        return getBaseRef().child("webforms");
    }

    public static String getPointsPath() {
        return "point/";
    }

    public void uploadPointImage(Bitmap fullSize, String fileName, Point point, Map<String, String> result){
        FirebaseStorage storageRef = FirebaseStorage.getInstance();
        StorageReference photoRef = storageRef.getReferenceFromUrl("gs://" + mContext.getString(R.string.google_storage_bucket));
        Long timestamp = System.currentTimeMillis();
        final StorageReference fullSizeRef = photoRef.child(FirebaseManager.getCurrentUserId()).child("full").child(timestamp.toString()).child(fileName + ".jpg");
        Log.d(TAG, fullSizeRef.toString());

        ByteArrayOutputStream fullSizeStream = new ByteArrayOutputStream();
        fullSize.compress(Bitmap.CompressFormat.JPEG, 90, fullSizeStream);
        byte[] bytes = fullSizeStream.toByteArray();
        fullSizeRef.putBytes(bytes).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                final Uri fullSizeUrl = taskSnapshot.getDownloadUrl();
                point.setImage(new Image(fullSizeUrl.toString()));
                writeNewPoint(point, result);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                FirebaseCrash.logcat(Log.ERROR, TAG, "Failed to upload post to database.");
                FirebaseCrash.report(e);
                result.put("error", mContext.getString(R.string.error_upload_task_create));
            }
        });
    }

    public void writeNewPoint(final Point point, final Map<String, String> result){
        final DatabaseReference ref = FirebaseManager.getBaseRef();
        DatabaseReference pointsRef = FirebaseManager.getPointsRef();
        final String newPointKey = pointsRef.push().getKey();

        Author author = FirebaseManager.getAuthor();
        if (author == null) {
            FirebaseCrash.logcat(Log.ERROR, TAG, "Couldn't upload post: Couldn't get signed in user.");
            result.put("error", mContext.getString(R.string.error_user_not_signed_in));
            return;
        }
        point.setAuthor(author);
        point.setTimestamp(ServerValue.TIMESTAMP);

        Map<String, Object> updatedUserData = new HashMap<>();
        updatedUserData.put(FirebaseManager.getUsersPath() + author.getUid() + "/" + FirebaseManager.getPointsPath()
                + newPointKey, true);
        updatedUserData.put(FirebaseManager.getPointsPath() + newPointKey,
                new ObjectMapper().convertValue(point, Map.class));
        ref.updateChildren(updatedUserData, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError firebaseError, DatabaseReference databaseReference) {
                if (firebaseError != null) {
                    Log.e(TAG, "Unable to create new post: " + firebaseError.getMessage());
                    FirebaseCrash.report(firebaseError.toException());
                    result.put("error", mContext.getString(R.string.error_upload_task_create));
                }
            }
        });
    }

    public void destroy() {
        sFirebaseManager=null;
    }
}
