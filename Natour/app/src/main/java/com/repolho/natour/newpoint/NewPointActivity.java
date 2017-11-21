package com.repolho.natour.newpoint;

import android.Manifest;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.repolho.natour.BaseActivity;
import com.repolho.natour.R;
import com.repolho.natour.ViewModelHolder;
import com.repolho.natour.databinding.NewPointActBinding;
import com.repolho.natour.util.ActivityUtils;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;


public class NewPointActivity extends BaseActivity implements NewPointNavigator, EasyPermissions.PermissionCallbacks{

    public static final String TAG = "newCatchActivity";
    public static final String NEW_POINT_VIEWMODEL_TAG = "NEW_POINT_VIEWMODEL_TAG";

    private NewPointPagerAdapter mNewPointPagerAdapter;

    private NewPointViewModel mViewModel;
    private NewPointActBinding mViewDataBinding;

    private static final int RC_PERMISSIONS = 102;
    private static final String[] neededPerms = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    @Override
    public void onPointSaved() {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.new_point_act);
        mViewDataBinding = DataBindingUtil.setContentView(this, R.layout.new_point_act);

        mViewModel = findOrCreateViewModel();
        mViewModel.setNavigator(this);

        setupPager();

        setupToolbar();

        checkPermissions();

        // Building the GoogleApi client
        mViewModel.startLocationApi();
        mViewDataBinding.btnEnableEditDescCoordenate.setOnClickListener(v -> setupAddress());
        setupAddress();
    }

    private void setupAddress() {
        mViewDataBinding.txtDescCoordenate.setText(mViewModel.getLocation());
    }

    @AfterPermissionGranted(RC_PERMISSIONS)
    private void checkPermissions() {
        // Check for camera permissions
        if (!EasyPermissions.hasPermissions(this, neededPerms)) {
            EasyPermissions.requestPermissions(this,
                    "This sample will upload a picture from your Camera",
                    RC_PERMISSIONS, neededPerms);
            return;
        }
    }

    private void setupPager() {
        mNewPointPagerAdapter = new NewPointPagerAdapter(getSupportFragmentManager(), getApplicationContext(), this);
        mViewDataBinding.viewPagerNewCatch.setAdapter(mNewPointPagerAdapter);

        mViewDataBinding.viewPagerTabLayout.setupWithViewPager(mViewDataBinding.viewPagerNewCatch);

        RelativeLayout tabOne = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.new_point_photo_tab, null);
        mViewDataBinding.viewPagerTabLayout.getTabAt(0).setCustomView(tabOne);

        RelativeLayout tabTwo = (RelativeLayout) LayoutInflater.from(this).inflate(R.layout.new_point_detail_tab, null);
        mViewDataBinding.viewPagerTabLayout.getTabAt(1).setCustomView(tabTwo);
    }

    private void setupToolbar() {
        Toolbar toolbarTop = (Toolbar) mViewDataBinding.toolbarMainTop;
        setSupportActionBar(toolbarTop);
        if (getSupportActionBar() != null) {
            TextView toolbarTopTitle = (TextView) findViewById(R.id.tv_title_toolbar);
            toolbarTopTitle.setText("Adicionar novo local");
            toolbarTopTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            getSupportActionBar().setTitle(null);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private NewPointViewModel findOrCreateViewModel() {
        // In a configuration change we might have a ViewModel present. It's retained using the
        // Fragment Manager.
        @SuppressWarnings("unchecked")
        ViewModelHolder<NewPointViewModel> retainedViewModel =
                (ViewModelHolder<NewPointViewModel>) getSupportFragmentManager()
                        .findFragmentByTag(NEW_POINT_VIEWMODEL_TAG);

        if (retainedViewModel != null && retainedViewModel.getViewmodel() != null) {
            // If the model was retained, return it.
            return retainedViewModel.getViewmodel();
        } else {
            // There is no ViewModel yet, create it.
            NewPointViewModel viewModel = new NewPointViewModel(getApplicationContext(), this);

            // and bind it to this Activity's lifecycle using the Fragment Manager.
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(),
                    ViewModelHolder.createContainer(viewModel),
                    NEW_POINT_VIEWMODEL_TAG);
            return viewModel;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mViewModel.onActivityDestroyed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.menu_new_catch_done:
                mViewModel.addNewPoint();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_new_catch, menu);  //  menu for photospec.
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {}

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {}

}
