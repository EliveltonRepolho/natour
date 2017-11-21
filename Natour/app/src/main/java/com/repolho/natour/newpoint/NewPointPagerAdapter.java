package com.repolho.natour.newpoint;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.repolho.natour.ViewModelHolder;
import com.repolho.natour.util.ActivityUtils;

import java.lang.ref.WeakReference;


/**
 * Created by repolho on 02/10/16.
 */
public class NewPointPagerAdapter extends FragmentStatePagerAdapter {

    public static final String TAG_PHOTO_FRAGMENT = "newCatchPhotoFragment";
    public static final String TAG_DETAIL_FRAGMENT = "newCatchDetailFragment";
    private Activity mActivity;
    private Context mContext;
    private FragmentManager mFragmentManager;

    private final SparseArray<WeakReference<Fragment>> instantiatedFragments = new SparseArray<>();

    public NewPointPagerAdapter(FragmentManager fm, Context context, Activity activity) {
        super(fm);
        mFragmentManager = fm;
        mContext = context;
        mActivity = activity;
    }

    @Override
    public Fragment getItem(int position) {
        NewPointViewModel viewModel = findOrCreateViewModel();
        switch (position) {
            case 0:
                NewPointPhotoFragment photoFragment = NewPointPhotoFragment.newInstance();
                photoFragment.setViewModel(viewModel);
                return photoFragment;
            case 1:
                NewPointDetailFragment detailFragment = NewPointDetailFragment.newInstance();
                detailFragment.setViewModel(viewModel);
                return detailFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Foto";
            case 1:
                return "Detalhe";
            default:
                return "";
        }
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        final Fragment fragment = (Fragment) super.instantiateItem(container, position);
        instantiatedFragments.put(position, new WeakReference<>(fragment));
        return fragment;
    }

    @Override
    public void destroyItem(final ViewGroup container, final int position, final Object object) {
        instantiatedFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    @Nullable
    public Fragment getFragment(final int position) {
        final WeakReference<Fragment> wr = instantiatedFragments.get(position);
        if (wr != null) {
            return wr.get();
        } else {
            return null;
        }
    }

    private NewPointViewModel findOrCreateViewModel() {
        // In a configuration change we might have a ViewModel present. It's retained using the
        // Fragment Manager.
        @SuppressWarnings("unchecked")
        ViewModelHolder<NewPointViewModel> retainedViewModel =
                (ViewModelHolder<NewPointViewModel>) mFragmentManager
                        .findFragmentByTag(NewPointActivity.NEW_POINT_VIEWMODEL_TAG);

        if (retainedViewModel != null && retainedViewModel.getViewmodel() != null) {
            // If the model was retained, return it.
            return retainedViewModel.getViewmodel();
        } else {
            // There is no ViewModel yet, create it.
            NewPointViewModel viewModel = new NewPointViewModel(mContext, mActivity);

            // and bind it to this Activity's lifecycle using the Fragment Manager.
            ActivityUtils.addFragmentToActivity(
                    mFragmentManager,
                    ViewModelHolder.createContainer(viewModel),
                    NewPointActivity.NEW_POINT_VIEWMODEL_TAG);
            return viewModel;
        }
    }
}