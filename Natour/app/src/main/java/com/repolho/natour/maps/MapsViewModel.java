package com.repolho.natour.maps;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableField;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.GeoPoint;
import com.repolho.natour.BaseViewModel;
import com.repolho.natour.firebase.FirebaseManager;
import com.repolho.natour.home.FirebaseHomeQueryAdapter;
import com.repolho.natour.home.HomeNavigator;
import com.repolho.natour.home.HomeViewHolder;
import com.repolho.natour.location.LocationHelper;
import com.repolho.natour.model.Point;

import java.util.ArrayList;
import java.util.List;
/**
 * Exposes the data to be used in the Maps screen.
 */
public class MapsViewModel extends BaseViewModel implements GoogleMap.OnMarkerClickListener {
    private static String TAG = "MapsViewModel";

    private GoogleMap mMap;

    private MapsNavigator mNavigator;
    private LocationHelper locationHelper;

    public MapsViewModel(Context context, Activity activity) {
        super(context);

        locationHelper = new LocationHelper(activity);
    }

    void onActivityDestroyed() {
        // Clear references to avoid potential memory leaks.
        mMap = null;
        locationHelper = null;
    }

    public void handleMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        //mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        // Set a listener for marker click.
        mMap.setOnMarkerClickListener(this);

        setupFirebaseListener();

        updateMapUIZoom();
    }

    public void updateMapUIZoom() {
        Location location = locationHelper.getLocation();
        if(location != null){
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 13));
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the point of the map to location user
                    .zoom(15)                   // Sets the zoom
                    .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

    void setNavigator(MapsNavigator navigator) {
        mNavigator = navigator;
    }

    private void setupFirebaseListener() {

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Point point = snapshot.getValue(Point.class);
                    if(point != null && point.getLatitude() != 0 && point.getLatitude() != 0 ){
                        isFirst.set(false);
                        LatLng latLng = new LatLng(point.getLatitude(), point.getLongitude());
                        mMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(point.getText())
                                .snippet(point.getDescCoordinates()));
                    }
                }
                updateMapUIZoom();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }

        };
        FirebaseManager.getPointsRef().addValueEventListener(postListener);

    }

    public void startLocationApi(){
        locationHelper.buildGoogleApiClient();
    }

    /**
     * User click in the marker.
     */
    @Override
    public boolean onMarkerClick(final Marker marker) {

        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }

    /**
     * Called by the Data Binding library and the FAB's click listener.
     */
    public void addNewPoint() {
        if (mNavigator != null) {
            mNavigator.addNewPoint();
        }
    }

}
