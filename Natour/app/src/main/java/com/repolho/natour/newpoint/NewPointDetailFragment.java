package com.repolho.natour.newpoint;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.repolho.natour.R;
import com.repolho.natour.databinding.NewPointDetailFragBinding;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by repolho on 02/10/16.
 */
public class NewPointDetailFragment extends Fragment {

    protected static final String TAG = "NewPointDetailFragment";

    private NewPointViewModel mViewModel;
    private NewPointDetailFragBinding mViewDataBinding;

    public NewPointDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ThirdFragment.
     */
    public static NewPointDetailFragment newInstance() {
        return new NewPointDetailFragment();
    }

    public void setViewModel(@NonNull NewPointViewModel viewModel) {
        mViewModel = checkNotNull(viewModel);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.new_point_detail_frag, container, false);
        if (mViewDataBinding == null) {
            mViewDataBinding = NewPointDetailFragBinding.bind(root);
        }
        setHasOptionsMenu(true);
        mViewDataBinding.setViewmodel(mViewModel);
        return mViewDataBinding.getRoot();
    }

}
