package com.repolho.natour.firebase.points;

/**
 * Picture uploading listener
 *
 */
public interface NewPointListener {

    /**
     * a callback called when we've done taking a picture from a single camera
     * (use this method if you don't want to wait for ALL taken pictures to be ready @see onDoneCapturingAllPhotos)
     *
     * @param error if has erros
     */
    void onUploadoDone(String error);

}
