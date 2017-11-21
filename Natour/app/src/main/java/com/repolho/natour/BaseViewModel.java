package com.repolho.natour;

import android.content.Context;
import android.databinding.BaseObservable;


/**
 * Abstract class for View Models.
 */
public abstract class BaseViewModel extends BaseObservable {

    private final Context mContext;

    public BaseViewModel(Context context) {
        mContext = context.getApplicationContext(); // Force use of Application Context.
    }

    public Context getContext() {
        return mContext;
    }
}
