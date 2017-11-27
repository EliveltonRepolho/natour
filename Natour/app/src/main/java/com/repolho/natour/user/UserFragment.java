package com.repolho.natour.user;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.repolho.natour.R;
import com.repolho.natour.ViewModelHolder;
import com.repolho.natour.databinding.UserFragBinding;
import com.repolho.natour.intro.IntroActivity;
import com.repolho.natour.login.LoginActivity;
import com.repolho.natour.util.ActivityUtils;

public class UserFragment extends Fragment implements UserNavigator {
    public static final String TAG = "UserFragment";
    public static final String USER_VIEWMODEL_TAG = "USER_VIEWMODEL_TAG";

    private UserViewModel mViewModel;
    private UserFragBinding mUserFragBinding;

    public UserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ThirdFragment.
     */
    public static UserFragment newInstance() {
        return new UserFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = findOrCreateViewModel();
        mViewModel.setNavigator(this);
    }

    public void setViewModel(UserViewModel viewModel) {
        mViewModel = viewModel;
    }

    @NonNull
    private UserViewModel findOrCreateViewModel() {
        // In a configuration change we might have a ViewModel present. It's retained using the
        // Fragment Manager.
        ViewModelHolder<UserViewModel> retainedViewModel =
                (ViewModelHolder<UserViewModel>) getActivity().getSupportFragmentManager()
                        .findFragmentByTag(USER_VIEWMODEL_TAG);

        if (retainedViewModel != null && retainedViewModel.getViewmodel() != null) {
            // If the model was retained, return it.
            return retainedViewModel.getViewmodel();
        } else {
            // There is no ViewModel yet, create it.
            UserViewModel viewModel = new UserViewModel(getActivity().getApplicationContext());

            // and bind it to this Activity's lifecycle using the Fragment Manager.
            ActivityUtils.addFragmentToActivity(
                    getActivity().getSupportFragmentManager(),
                    ViewModelHolder.createContainer(viewModel),
                    USER_VIEWMODEL_TAG);
            return viewModel;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mUserFragBinding = DataBindingUtil.inflate(
                inflater, R.layout.user_frag, container, false);
        return mUserFragBinding.getRoot();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mUserFragBinding.setViewmodel(mViewModel);
    }
    @Override
    public void onResume() {
        super.onResume();
        mViewModel.start(mUserFragBinding.profileUserPhoto);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mViewModel.onActivityDestroyed();
    }

    @Override
    public void onLogout() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(mViewModel.getContext(), LoginActivity.class));
        getActivity().finish();
    }

    @Override
    public void onRestartIntro() {
        startActivity(new Intent(mViewModel.getContext(), IntroActivity.class));
    }

    @Override
    public void answerOnlineForm(String url) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }


}
