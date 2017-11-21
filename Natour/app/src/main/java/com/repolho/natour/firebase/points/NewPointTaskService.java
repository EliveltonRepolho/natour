package com.repolho.natour.firebase.points;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.repolho.natour.R;
import com.repolho.natour.firebase.FirebaseManager;
import com.repolho.natour.model.Author;
import com.repolho.natour.model.Image;
import com.repolho.natour.model.Point;
import com.repolho.natour.firebase.points.NewPointListener;

import java.io.ByteArrayOutputStream;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * Abstract Picture Uploading Service.
 *
 */
public abstract class NewPointTaskService {

    /**
     * Tag for the {@link Log}.
     */
    private static final String TAG = "NewPointTaskService";
    private Context mContext;

    public void uploadPoint(Bitmap bitmap, String inFileName, Point point) {
        UploadPointTask uploadTask = new UploadPointTask(bitmap, inFileName, point);
        uploadTask.execute();
    }

    class UploadPointTask extends AsyncTask<Void, Void, Void> {
        private WeakReference<Bitmap> bitmapReference;
        private Point point;
        private String fileName;

        public UploadPointTask(Bitmap bitmap, String inFileName, Point point) {
            this.bitmapReference = new WeakReference<Bitmap>(bitmap);
            this.point = point;
            this.fileName = inFileName;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(Void... params) {
            Bitmap fullSize = bitmapReference.get();
            if (fullSize == null) {
                return null;
            }
            FirebaseManager manager = FirebaseManager.getInstance(mContext);
            final Map<String, String> result = new HashMap<String, String>();

            manager.uploadPointImage(fullSize, fileName, point, result);
            onUploadFinished(result.get("error"));
            manager.destroy();
            // TODO: Refactor these insanely nested callbacks.
            return null;
        }
    }
    
    /***
     * constructor.
     *
     * @param context the application context
     */
    NewPointTaskService(final Context context) {
        this.mContext = context;
    }

    /**
     * starts pictures capturing process.
     * @param pictureData picture capturing listener
     * @param listener listener for callbacks
     */
    public abstract void uploadImage(final byte[] pictureData, String inFileName, Point point, final NewPointListener listener);

    public abstract void onUploadFinished(String error);

}
