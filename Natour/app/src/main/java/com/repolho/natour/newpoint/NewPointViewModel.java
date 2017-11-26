package com.repolho.natour.newpoint;

import android.app.Activity;
import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableField;
import android.location.Address;
import android.location.Location;
import android.support.annotation.Nullable;

import com.repolho.natour.BR;
import com.repolho.natour.BaseViewModel;
import com.repolho.natour.camera2.APictureCapturingService;
import com.repolho.natour.camera2.AutoFitTextureView;
import com.repolho.natour.camera2.PictureCapturingListener;
import com.repolho.natour.camera2.PictureCapturingServiceImpl;
import com.repolho.natour.firebase.points.NewPointListener;
import com.repolho.natour.firebase.points.NewPointTaskService;
import com.repolho.natour.firebase.points.NewPointTaskServiceImpl;
import com.repolho.natour.location.LocationHelper;
import com.repolho.natour.model.Detail;
import com.repolho.natour.model.Point;

/**
 * Exposes the data to be used in the task list screen.
 * <p>
 * {@link BaseObservable} implements a listener registration mechanism which is notified when a
 * property changes. This is done by assigning a {@link Bindable} annotation to the property's
 * getter method.
 */
public class NewPointViewModel extends BaseViewModel
        implements PictureCapturingListener, NewPointListener {

    private NewPointNavigator mNavigator;

    public final ObservableField<String> title = new ObservableField<>();
    public final ObservableField<String> coordenatesDesc = new ObservableField<>();
    public final ObservableField<String> detail = new ObservableField<>();

    public final ObservableField<String> snackbarText = new ObservableField<>();

    private APictureCapturingService mPictureService;
    private NewPointTaskService mNewPointTaskService;
    private LocationHelper locationHelper;

    public NewPointViewModel(Context context, Activity activity) {
        super(context);
        //getting instance of the Service from PictureCapturingServiceImpl
        mPictureService = PictureCapturingServiceImpl.getInstance(activity);
        mNewPointTaskService = NewPointTaskServiceImpl.getInstance(context);
        locationHelper = new LocationHelper(activity);
    }

    void setNavigator(NewPointNavigator navigator) {
        mNavigator = navigator;
    }

    void onActivityDestroyed() {
        // Clear references to avoid potential memory leaks.
        mNavigator = null;
        mPictureService = null;
        mNewPointTaskService = null;
        locationHelper = null;
    }

    @Nullable
    public String getSnackbarText() {
        return snackbarText.get();
    }

    public void start(AutoFitTextureView textureView) {
        mPictureService.start(textureView);
    }

    public void pause() {
        mPictureService.pause();
    }

    /**
     * Called by the Data Binding library and the FAB's click listener.
     */
    public void addNewPoint() {
        mPictureService.takePicture(this);
        //TAKE PICTURE
        //VALIDATE FORM
    }

    /**
     * Displaying the pictures taken.
     */
    @Override
    public void onCaptureDone(byte[] pictureData) {
        Detail detailObj = new Detail(detail.get());
        Point point = new Point(title.get(), locationHelper.getLastLocation().getLatitude(), locationHelper.getLastLocation().getLongitude() , getLocation(), detailObj);
        mNewPointTaskService.uploadImage(pictureData, title.get(), point, this);
    }

    @Override
    public void onError(String error) {
        snackbarText.set(error);
    }

    @Override
    public void onUploadoDone(String error) {
        if(error != null){
            snackbarText.set(error);
            return;
        }
        if (mNavigator != null) {
            mNavigator.onPointSaved();
        }
    }

    public void startLocationApi(){
        locationHelper.buildGoogleApiClient();
        getLocation();
    }

    public String getLocation() {
        Location location = locationHelper.getLocation();
        Address address = locationHelper.getAddress(location);
        String fullAddress = locationHelper.buildAddress(address);
        coordenatesDesc.set(fullAddress);
        return fullAddress;
    }

}
