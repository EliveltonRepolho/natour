package com.repolho.natour.maps;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.repolho.natour.R;
import com.repolho.natour.ViewModelHolder;
import com.repolho.natour.databinding.MapsFragBinding;
import com.repolho.natour.home.HomeNavigator;
import com.repolho.natour.newpoint.NewPointActivity;
import com.repolho.natour.util.ActivityUtils;

public class MapsFragment extends Fragment implements OnMapReadyCallback, MapsNavigator {
    public static final String TAG = "MapsFragment";
    public static final String MAPS_VIEWMODEL_TAG = "MAPS_VIEWMODEL_TAG";

    private MapsViewModel mViewModel;
    private MapsFragBinding mFragBinding;

    public MapsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MapsFragment.
     */
    public static MapsFragment newInstance() {
        return new MapsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = findOrCreateViewModel();
        mViewModel.setNavigator(this);
        mViewModel.startLocationApi();
    }

    @NonNull
    private MapsViewModel findOrCreateViewModel() {
        // In a configuration change we might have a ViewModel present. It's retained using the
        // Fragment Manager.
        ViewModelHolder<MapsViewModel> retainedViewModel =
                (ViewModelHolder<MapsViewModel>) getActivity().getSupportFragmentManager()
                        .findFragmentByTag(MAPS_VIEWMODEL_TAG);

        if (retainedViewModel != null && retainedViewModel.getViewmodel() != null) {
            // If the model was retained, return it.
            return retainedViewModel.getViewmodel();
        } else {
            // There is no ViewModel yet, create it.
            MapsViewModel viewModel = new MapsViewModel(getActivity().getApplicationContext(), getActivity());

            // and bind it to this Activity's lifecycle using the Fragment Manager.
            ActivityUtils.addFragmentToActivity(
                    getActivity().getSupportFragmentManager(),
                    ViewModelHolder.createContainer(viewModel),
                    MAPS_VIEWMODEL_TAG);
            return viewModel;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragBinding = DataBindingUtil.inflate(inflater, R.layout.maps_frag, container, false);
        loadMap();
        return mFragBinding.getRoot();
    }

    private void loadMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.mapPoints);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mViewModel.updateMapUIZoom();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel.updateMapUIZoom();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mViewModel.onActivityDestroyed();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mViewModel.handleMapReady(googleMap);
    }


    @Override
    public void addNewPoint() {
        startActivity(new Intent(getContext(), NewPointActivity.class));
    }
}
