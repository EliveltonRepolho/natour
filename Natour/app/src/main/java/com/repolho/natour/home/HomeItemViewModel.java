package com.repolho.natour.home;

import android.content.Context;
import android.support.annotation.Nullable;

import com.repolho.natour.BaseViewModel;

import java.lang.ref.WeakReference;


/**
 * Listens to user actions from the list item in ({@link HomeFragment}) and redirects them to the
 * Fragment's actions listener.
 */
public class HomeItemViewModel extends BaseViewModel {

    // This navigator is s wrapped in a WeakReference to avoid leaks because it has references to an
    // activity. There's no straightforward way to clear it for each item in a list adapter.
    @Nullable
    private WeakReference<HomeItemNavigator> mNavigator;

    public HomeItemViewModel(Context context) {
        super(context);
    }

    public void setNavigator(HomeItemNavigator navigator) {
        mNavigator = new WeakReference<>(navigator);
    }

    /**
     * Called by the Data Binding library when the row is clicked.
     */
    public void taskClicked() {
        String taskId = null;
        if (taskId == null) {
            // Click happened before task was loaded, no-op.
            return;
        }
        if (mNavigator != null && mNavigator.get() != null) {
            mNavigator.get().openPointDetails(taskId);
        }
    }
}
