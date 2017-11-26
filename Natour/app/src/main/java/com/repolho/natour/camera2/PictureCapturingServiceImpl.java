package com.repolho.natour.camera2;

import android.annotation.TargetApi;
import android.app.Activity;
import android.media.Image;
import android.media.ImageReader;
import android.os.Build;
import android.view.TextureView;

import java.nio.ByteBuffer;


/**
 * The aim of this service is to take pictures with a preview
 * from available camera using Android Camera 2 API
 *
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP) //NOTE: camera 2 api was added in API level 21
public class PictureCapturingServiceImpl extends APictureCapturingService {

    private static final String TAG = "PictureCapturingServiceImpl";

    private PictureCapturingListener capturingListener;

    /***
     * private constructor, meant to force the use of {@link #getInstance}  method
     */
    private PictureCapturingServiceImpl(final Activity activity) {
        super(activity);
    }

    /**
     * @param activity the activity used to get the app's context and the display manager
     * @return a new instance
     */
    public static APictureCapturingService getInstance(final Activity activity) {
        return new PictureCapturingServiceImpl(activity);
    }

    @Override
    public void takePicture(PictureCapturingListener listener) {
        this.capturingListener = listener;
        lockFocus();
    }

    @Override
    public void pictureTaken(ImageReader imageReader) {
        final Image image = imageReader.acquireLatestImage();
        final ByteBuffer buffer = image.getPlanes()[0].getBuffer();
        final byte[] bytes = new byte[buffer.capacity()];
        buffer.get(bytes);
        image.close();
        capturingListener.onCaptureDone(bytes);
    }

    @Override
    public void showError(String errorMessage) {
        capturingListener.onError(errorMessage);
    }

}
