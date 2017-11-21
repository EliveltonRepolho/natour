package com.repolho.natour.camera2;

/**
 * Picture capturing listener
 *
 */
public interface PictureCapturingListener {

    /**
     * a callback called when we've done taking a picture from a single camera
     * (use this method if you don't want to wait for ALL taken pictures to be ready @see onDoneCapturingAllPhotos)
     *
     * @param pictureData taken picture's data as a byte array
     */
    void onCaptureDone(byte[] pictureData);

    void onError(String error);

}
