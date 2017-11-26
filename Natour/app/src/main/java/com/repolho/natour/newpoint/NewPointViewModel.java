package com.repolho.natour.newpoint;

import android.app.Activity;
import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableField;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Location;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.repolho.natour.BaseViewModel;
import com.repolho.natour.R;
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

    public final ObservableField<Integer> accessDifficulty = new ObservableField<>();
    public final ObservableField<String> accessDifficultyText = new ObservableField<>();
    public final ObservableField<Drawable> accessDifficultyRes = new ObservableField<>();

    public final ObservableField<Integer> wheelchairDifficulty = new ObservableField<>();
    public final ObservableField<String> wheelchairDifficultyText = new ObservableField<>();
    public final ObservableField<Drawable> wheelchairDifficultyRes = new ObservableField<>();

    public final ObservableField<Integer> dangerousness = new ObservableField<>();
    public final ObservableField<String> dangerousnessText = new ObservableField<>();
    public final ObservableField<Drawable> dangerousnessRes = new ObservableField<>();

    public final ObservableField<String> walkingAverage = new ObservableField<>();

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

        //Defaults
        wheelchairDifficultyRes.set(null);
        wheelchairDifficultyText.set(getContext().getString(R.string.text_wheelchair_difficulty_no_selection));
        accessDifficultyRes.set(null);
        accessDifficultyText.set(getContext().getString(R.string.text_access_difficulty_no_selection));
        dangerousnessRes.set(null);
        dangerousnessText.set(getContext().getString(R.string.text_dangerousness_no_selection));
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
        Detail detail = new Detail(dangerousness.get(), walkingAverage.get(), accessDifficulty.get(), wheelchairDifficulty.get());
        Point point = new Point(title.get(), locationHelper.getLastLocation().getLatitude(), locationHelper.getLastLocation().getLongitude() , getLocation(), detail);
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

    public void onWeelchairInfoChanged(SeekBar seekBar, int pos, boolean fromUser) {
        wheelchairDifficulty.set(pos);
        switch (pos){
            case 0:
                wheelchairDifficultyRes.set(null);
                wheelchairDifficultyText.set(getContext().getString(R.string.text_wheelchair_difficulty_no_selection));
                break;
            case 1:
                wheelchairDifficultyRes.set(getContext().getDrawable(R.drawable.ic_not_interested_black_24dp));
                wheelchairDifficultyText.set(getContext().getString(R.string.text_wheelchair_difficulty_no_access));
                break;
            case 2:
                wheelchairDifficultyRes.set(getContext().getDrawable(R.drawable.ic_directions_walk_black_24dp));
                wheelchairDifficultyText.set(getContext().getString(R.string.text_wheelchair_difficulty_with_help));
                break;
            case 3:
                wheelchairDifficultyRes.set(getContext().getDrawable(R.drawable.ic_accessible_black_24dp));
                wheelchairDifficultyText.set(getContext().getString(R.string.text_wheelchair_difficulty_good));
                break;
        }
    }

    public void onAccessDifficultyInfoChanged(SeekBar seekBar, int pos, boolean fromUser) {
        accessDifficulty.set(pos);
        switch (pos){
            case 0:
                accessDifficultyRes.set(null);
                accessDifficultyText.set(getContext().getString(R.string.text_access_difficulty_no_selection));
                break;
            case 1:
                accessDifficultyRes.set(getContext().getDrawable(R.drawable.ic_priority_high_black_24dp));
                accessDifficultyText.set(getContext().getString(R.string.text_access_difficulty_no_access));
                break;
            case 2:
                accessDifficultyRes.set(getContext().getDrawable(R.drawable.ic_favorite_border_black_24dp));
                accessDifficultyText.set(getContext().getString(R.string.text_access_difficulty_with_help));
                break;
            case 3:
                accessDifficultyRes.set(getContext().getDrawable(R.drawable.ic_favorite_black_24dp));
                accessDifficultyText.set(getContext().getString(R.string.text_access_difficulty_good));
                break;
        }
    }

    public void onDangerousnessInfoChanged(SeekBar seekBar, int pos, boolean fromUser) {
        dangerousness.set(pos);
        switch (pos){
            case 0:
                dangerousnessRes.set(null);
                dangerousnessText.set(getContext().getString(R.string.text_dangerousness_no_selection));
                break;
            case 1:
                dangerousnessRes.set(getContext().getDrawable(R.drawable.ic_report_problem_black_24dp));
                dangerousnessText.set(getContext().getString(R.string.text_dangerousness_no_access));
                break;
            case 2:
                dangerousnessRes.set(getContext().getDrawable(R.drawable.ic_group_work_black_24dp));
                dangerousnessText.set(getContext().getString(R.string.text_dangerousness_with_help));
                break;
            case 3:
                dangerousnessRes.set(getContext().getDrawable(R.drawable.ic_thumb_up_black_24dp));
                dangerousnessText.set(getContext().getString(R.string.text_dangerousness_good));
                break;
        }

    }
}
