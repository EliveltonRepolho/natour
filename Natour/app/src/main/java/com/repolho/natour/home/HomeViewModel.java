package com.repolho.natour.home;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableBoolean;

import com.repolho.natour.BaseViewModel;

/**
 * Exposes the data to be used in the task list screen.
 * <p>
 * {@link BaseObservable} implements a listener registration mechanism which is notified when a
 * property changes. This is done by assigning a {@link Bindable} annotation to the property's
 * getter method.
 */
public class HomeViewModel extends BaseViewModel {

    private HomeNavigator mNavigator;

    public HomeViewModel(Context context) {
        super(context);
    }

    void setNavigator(HomeNavigator navigator) {
        mNavigator = navigator;
    }

    void onActivityDestroyed() {
        // Clear references to avoid potential memory leaks.
        mNavigator = null;
    }

    public void start() {
        loadTasks(false);
    }

    public void loadTasks(boolean forceUpdate) {

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
