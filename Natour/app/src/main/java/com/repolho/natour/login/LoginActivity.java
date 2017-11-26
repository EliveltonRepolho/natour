package com.repolho.natour.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.crash.FirebaseCrash;
import com.repolho.natour.BaseActivity;
import com.repolho.natour.R;
import com.repolho.natour.ViewModelHolder;
import com.repolho.natour.intro.IntroActivity;
import com.repolho.natour.util.ActivityUtils;

public class LoginActivity extends BaseActivity implements LoginNavigator, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "LoginActivity";
    public static final String LOGIN_VIEWMODEL_TAG = "LOGIN_VIEWMODEL_TAG";

    private static final int RC_SIGN_IN = 103;

    private GoogleApiClient mGoogleApiClient;

    private LoginViewModel mLoginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.repolho.natour.R.layout.login_act);

        LoginFragment loginFragment = findOrCreateViewFragment();

        mLoginViewModel = findOrCreateViewModel();
        mLoginViewModel.setNavigator(this);
        // Link View and ViewModel
        loginFragment.setViewModel(mLoginViewModel);

        setupGoogleListeners();

    }

    @NonNull
    private LoginViewModel findOrCreateViewModel() {
        // In a configuration change we might have a ViewModel present. It's retained using the
        // Fragment Manager.
        ViewModelHolder<LoginViewModel> retainedViewModel =
                (ViewModelHolder<LoginViewModel>) getSupportFragmentManager()
                        .findFragmentByTag(LOGIN_VIEWMODEL_TAG);

        if (retainedViewModel != null && retainedViewModel.getViewmodel() != null) {
            // If the model was retained, return it.
            return retainedViewModel.getViewmodel();
        } else {
            // There is no ViewModel yet, create it.
            LoginViewModel viewModel = new LoginViewModel(getApplicationContext());

            // and bind it to this Activity's lifecycle using the Fragment Manager.
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(),
                    ViewModelHolder.createContainer(viewModel),
                    LOGIN_VIEWMODEL_TAG);
            return viewModel;
        }
    }

    @NonNull
    private LoginFragment findOrCreateViewFragment() {
        LoginFragment statisticsFragment = (LoginFragment) getSupportFragmentManager()
                .findFragmentById(R.id.content_login_frame);
        if (statisticsFragment == null) {
            statisticsFragment = LoginFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    statisticsFragment, R.id.content_login_frame);
        }
        return statisticsFragment;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleGoogleSignInResult(result);
        }
    }

    private void handleGoogleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.getStatus());
        if (result.isSuccess()) {
            // Successful Google sign in, authenticate with Firebase.
            GoogleSignInAccount acct = result.getSignInAccount();
            firebaseAuthWithGoogle(acct);
        } else {
            // Unsuccessful Google Sign In, show signed-out UI
            Log.d(TAG, "Google Sign-In failed.");
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGooogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        showProgressDialog("Logando com conta do google selecionada");
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult result) {
                        handleFirebaseAuthResult(result);
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        FirebaseCrash.logcat(Log.ERROR, TAG, "auth:onFailure:" + e.getMessage());
                        handleFirebaseAuthResult(null);
                    }
                });
    }

    private void handleFirebaseAuthResult(AuthResult result) {
        // TODO: This auth callback isn't being called after orientation change. Investigate.
        dismissProgressDialog();
        if (result != null) {
            Log.d(TAG, "handleFirebaseAuthResult:SUCCESS");

            startActivity(new Intent(LoginActivity.this, IntroActivity.class));
            finish();
        } else {
            Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        mLoginViewModel.onActivityDestroyed();
        super.onDestroy();
    }

    @Override
    public void makeLoginWithGoogle() {
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(intent, RC_SIGN_IN);
    }

    private void setupGoogleListeners() {
        // GoogleApiClient with Sign In
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,
                        new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                                .requestEmail()
                                .requestIdToken(this.getString(R.string.default_web_client_id))
                                .build())
                .build();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        mLoginViewModel.error.set(true);
        Log.w(TAG, "onConnectionFailed:" + connectionResult);
    }
}
