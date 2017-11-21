package com.repolho.natour.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.repolho.natour.R;
import com.repolho.natour.ViewModelHolder;
import com.repolho.natour.firebase.FirebaseManager;
import com.repolho.natour.login.LoginActivity;
import com.repolho.natour.maps.MapsFragment;
import com.repolho.natour.newpoint.NewPointActivity;
import com.repolho.natour.user.UserFragment;
import com.repolho.natour.util.ActivityUtils;


public class HomeActivity extends AppCompatActivity implements HomeItemNavigator, HomeNavigator {

    private DrawerLayout mDrawerLayout;

    public static final String TASKS_VIEWMODEL_TAG = "HOME_VIEWMODEL_TAG";

    private HomeViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkUserAuthenticated();

        setContentView(R.layout.home_act);

        HomeFragment homeFragment = findOrCreateHomeFragment();

        mViewModel = findOrCreateViewModel();
        mViewModel.setNavigator(this);

        // Link View and ViewModel
        homeFragment.setViewModel(mViewModel);

        setupBottomBar();
        showFragment(homeFragment);

        Toolbar toolbarTop = (Toolbar) findViewById(R.id.toolbarMainTop);
        TextView toolbarTopTitle  = (TextView)findViewById(R.id.tv_title_toolbar);
        toolbarTopTitle.setText(getString(R.string.app_name));
        setSupportActionBar(toolbarTop);
    }

    @Override
    protected void onDestroy() {
        mViewModel.onActivityDestroyed();
        super.onDestroy();
    }

    private HomeViewModel findOrCreateViewModel() {
        // In a configuration change we might have a ViewModel present. It's retained using the
        // Fragment Manager.
        @SuppressWarnings("unchecked")
        ViewModelHolder<HomeViewModel> retainedViewModel =
                (ViewModelHolder<HomeViewModel>) getSupportFragmentManager()
                        .findFragmentByTag(TASKS_VIEWMODEL_TAG);

        if (retainedViewModel != null && retainedViewModel.getViewmodel() != null) {
            // If the model was retained, return it.
            return retainedViewModel.getViewmodel();
        } else {
            // There is no ViewModel yet, create it.
            HomeViewModel viewModel = new HomeViewModel(getApplicationContext());
            // and bind it to this Activity's lifecycle using the Fragment Manager.
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(),
                    ViewModelHolder.createContainer(viewModel),
                    TASKS_VIEWMODEL_TAG);
            return viewModel;
        }
    }

    @NonNull
    private HomeFragment findOrCreateHomeFragment() {
        HomeFragment homeFragment =
                (HomeFragment) getSupportFragmentManager().findFragmentByTag(HomeFragment.TAG);
        if (homeFragment == null) {
            // Create the fragment
            homeFragment = HomeFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), homeFragment, HomeFragment.TAG);
        }
        return homeFragment;
    }

    @NonNull
    private UserFragment findOrCreateUserFragment() {
        UserFragment homeFragment =
                (UserFragment) getSupportFragmentManager().findFragmentByTag(UserFragment.TAG);
        if (homeFragment == null) {
            // Create the fragment
            homeFragment = UserFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), homeFragment, UserFragment.TAG);
        }
        return homeFragment;
    }

    @NonNull
    private MapsFragment findOrCreateMapsFragment() {
        MapsFragment homeFragment =
                (MapsFragment) getSupportFragmentManager().findFragmentByTag(MapsFragment.TAG);
        if (homeFragment == null) {
            // Create the fragment
            homeFragment = MapsFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), homeFragment, MapsFragment.TAG);
        }
        return homeFragment;
    }

    @Override
    public void openPointDetails(String taskId) {
        /*Intent intent = new Intent(this, PointDetailActivity.class);
        intent.putExtra(PointDetailActivity.EXTRA_TASK_ID, taskId);
        startActivityForResult(intent, AddEditPointActivity.REQUEST_CODE);*/
        Toast.makeText(this, "openPointDetails", Toast.LENGTH_LONG);

    }

    @Override
    public void addNewPoint() {
        startActivity(new Intent(this, NewPointActivity.class));
    }

    private void setupBottomBar() {

        findViewById(R.id.toolbarButtonHome).setOnClickListener(v -> showFragment(findOrCreateHomeFragment()));

        findViewById(R.id.toolbarButtonNewCatch).setOnClickListener(v -> addNewPoint());

        findViewById(R.id.toolbarButtonMap).setOnClickListener(v -> showFragment(findOrCreateMapsFragment()));

        findViewById(R.id.toolbarButtonUser).setOnClickListener(v -> showFragment(findOrCreateUserFragment()));
    }

    private void checkUserAuthenticated() {
        if (FirebaseManager.getCurrentUserId() == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    private void showFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.containerHomeFragment, fragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }
}
