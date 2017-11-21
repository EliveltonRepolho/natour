package com.repolho.natour.login;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.repolho.natour.R;
import com.repolho.natour.databinding.LoginFragBinding;


/**
 * Main UI for the login screen.
 */
public class LoginFragment extends Fragment {

    private LoginFragBinding mViewDataBinding;

    private LoginViewModel mLoginViewModel;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mViewDataBinding = DataBindingUtil.inflate(
                inflater, R.layout.login_frag, container, false);
        return mViewDataBinding.getRoot();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewDataBinding.setViewmodel(mLoginViewModel);
        mViewDataBinding.signInButton.setOnClickListener((view) -> mLoginViewModel.loginClicked());
    }

    public void setViewModel(LoginViewModel loginViewModel) {
        mLoginViewModel = loginViewModel;
    }

    public boolean isActive() {
        return isAdded();
    }
}
