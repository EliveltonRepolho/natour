package com.repolho.natour.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.repolho.natour.databinding.HomeFragBinding;
import com.repolho.natour.firebase.FirebaseManager;
import com.repolho.natour.model.Author;
import com.repolho.natour.model.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Display a grid of {@link com.repolho.natour.model.Point}s. User can choose to view all, active or completed tasks.
 */
public class HomeFragment extends Fragment {

    protected static final String TAG = "HomeFragment";
    private static final String KEY_LAYOUT_POSITION = "layoutPosition";

    private HomeViewModel mViewModel;

    private HomeFragBinding mViewDataBinding;

    private int mRecyclerViewPosition = 0;
    private FirebaseHomeQueryAdapter mAdapter;

    public HomeFragment() {
        // Requires empty public constructor
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        mViewModel.start();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mViewDataBinding = HomeFragBinding.inflate(inflater, container, false);

        mViewDataBinding.setView(this);

        mViewDataBinding.setViewmodel(mViewModel);

        setHasOptionsMenu(true);

        View root = mViewDataBinding.getRoot();

        return root;
    }

    public void setViewModel(HomeViewModel viewModel) {
        mViewModel = viewModel;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupListLayout(savedInstanceState);
        setupRecyclerView();
        Log.d(TAG, "Restoring recycler view position (following): " + mRecyclerViewPosition);
    }

    private void setupRecyclerView() {
        FirebaseManager.getPointsRef()
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        final List<String> postPaths = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Log.d(TAG, "adding local key: " + snapshot.getKey());
                            postPaths.add(snapshot.getKey());
                            if(mAdapter != null)
                                mAdapter.addItem(snapshot.getKey());
                        }

                        if (postPaths.size() == 0)
                            mViewModel.isFirst.set(true);
                        else
                            mViewModel.isFirst.set(false);

                        if(mAdapter == null){
                            mAdapter = new FirebaseHomeQueryAdapter(postPaths,
                                    new FirebaseHomeQueryAdapter.OnSetupViewListener() {
                                        @Override
                                        public void onSetupView(HomeViewHolder holder, Point point, int position, String postKey) {
                                            setupHome(holder, point, position, postKey);
                                        }
                                    });
                            mViewDataBinding.recyclerViewHome.setAdapter(mAdapter);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError firebaseError) {

                    }
                });

        mViewDataBinding.recyclerViewHome.setAdapter(mAdapter);
    }

    private void setupListLayout(@Nullable Bundle savedInstanceState) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        mViewDataBinding.recyclerViewHome.setLayoutManager(linearLayoutManager);

        if (savedInstanceState != null) {
            // Restore saved layout manager type.
            mRecyclerViewPosition = (int) savedInstanceState
                    .getSerializable(KEY_LAYOUT_POSITION);
            mViewDataBinding.recyclerViewHome.scrollToPosition(mRecyclerViewPosition);
            // TODO: RecyclerView only restores position properly for some tabs.
        }
    }

    private void setupHome(final HomeViewHolder postViewHolder, final Point point, final int position, final String inHomeKey) {
        postViewHolder.setPhoto(point.getImage());
        postViewHolder.setText(point.getText());
        postViewHolder.setDescGeolocation(point.getDescCoordinates());
        postViewHolder.setTimestamp(DateUtils.getRelativeTimeSpanString((long) point.getTimestamp()).toString());

        Author author = point.getAuthor();
        postViewHolder.setAuthor(author.getFullName(), author.getUid());
        postViewHolder.setIcon(author.getProfilePicture(), author.getUid());

        postViewHolder.setPostClickListener(new HomeViewHolder.HomeClickListener() {

            @Override
            public void onLongClick(View view) {
                Log.d(TAG, "LongClick position: " + position);
            }
        });
    }

}
