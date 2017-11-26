package com.repolho.natour;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.ObservableBoolean;


/**
 * Abstract class for View Models.
 */
public abstract class BaseViewModel extends BaseObservable {

    private final Context mContext;
    public final ObservableBoolean isFirst = new ObservableBoolean( true);

    public BaseViewModel(Context context) {
        mContext = context.getApplicationContext(); // Force use of Application Context.
    }

    public Context getContext() {
        return mContext;
    }
}
