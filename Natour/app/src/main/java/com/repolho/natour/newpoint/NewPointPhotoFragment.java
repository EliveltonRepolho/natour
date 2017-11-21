package com.repolho.natour.newpoint;

import android.databinding.Observable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.repolho.natour.R;
import com.repolho.natour.databinding.NewPointPhotoFragBinding;
import com.repolho.natour.util.SnackbarUtils;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by repolho on 02/10/16.
 */
public class NewPointPhotoFragment extends Fragment {

    protected static final String TAG = "newCatchDetailFragment";
    private NewPointViewModel mViewModel;
    private NewPointPhotoFragBinding mViewDataBinding;

    private Observable.OnPropertyChangedCallback mSnackbarCallback;

    public NewPointPhotoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ThirdFragment.
     */
    public static NewPointPhotoFragment newInstance() {
        return new NewPointPhotoFragment();
    }

    public void setViewModel(@NonNull NewPointViewModel viewModel) {
        mViewModel = checkNotNull(viewModel);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.new_point_photo_frag, container, false);
        if (mViewDataBinding == null) {
            mViewDataBinding = NewPointPhotoFragBinding.bind(root);
        }
        setHasOptionsMenu(true);
        mViewDataBinding.setViewmodel(mViewModel);
        return mViewDataBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupSnackbar();
    }

    @Override
    public void onResume() {
        super.onResume();
        mViewModel.start(mViewDataBinding.textureImage);
    }

    @Override
    public void onPause() {
        super.onPause();
        mViewModel.pause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mSnackbarCallback != null) {
            mViewModel.snackbarText.removeOnPropertyChangedCallback(mSnackbarCallback);
        }
    }

    private void setupSnackbar() {
        mSnackbarCallback = new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                SnackbarUtils.showSnackbar(getView(), mViewModel.getSnackbarText());
            }
        };
        mViewModel.snackbarText.addOnPropertyChangedCallback(mSnackbarCallback);
    }

}