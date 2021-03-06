package com.repolho.natour.firebase.points;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.repolho.natour.model.Point;


/**
 * The aim of this service is to secretly take pictures (without preview or opening device's camera app)
 * from all available cameras using Android Camera 2 API
 *
 */
public class NewPointTaskServiceImpl extends NewPointTaskService {

    private static final String TAG = "NewPointTaskServiceImpl";

    private NewPointListener mListener;

    /***
     * private constructor, meant to force the use of {@link #getInstance}  method
     */
    private NewPointTaskServiceImpl(final Context context) {
        super(context);
    }

    @Override
    public void uploadImage(byte[] pictureData, String inFileName, Point point, NewPointListener listener) {
        this.mListener = listener;
        final Bitmap bitmap = BitmapFactory.decodeByteArray(pictureData, 0, pictureData.length);
        uploadPoint(bitmap, point.getText(), point);
    }

    @Override
    public void onUploadFinished(String error) {
        mListener.onUploadoDone(error);
    }

    /**
     * @param context the activity used to get the app's context and the display manager
     * @return a new instance
     */
    public static NewPointTaskService getInstance(final Context context) {
        return new NewPointTaskServiceImpl(context);
    }



}
