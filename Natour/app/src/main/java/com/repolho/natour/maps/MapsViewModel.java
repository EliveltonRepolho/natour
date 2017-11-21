package com.repolho.natour.maps;

import android.content.Context;
import android.databinding.ObservableField;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.repolho.natour.BaseViewModel;

public class MapsViewModel extends BaseViewModel {
    private static String TAG = "UserViewModel";

    private GoogleMap mMap;

    public final ObservableField<String> userNameLabel = new ObservableField<>();

    public MapsViewModel(Context context) {
        super(context);
    }

    void onActivityDestroyed() {
        // Clear references to avoid potential memory leaks.
        mMap = null;
    }

    public void handleMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
