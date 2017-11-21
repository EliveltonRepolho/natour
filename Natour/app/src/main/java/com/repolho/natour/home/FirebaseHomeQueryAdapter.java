package com.repolho.natour.home;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.repolho.natour.R;
import com.repolho.natour.firebase.FirebaseManager;
import com.repolho.natour.model.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by repolho on 04/10/16.
 */
public class FirebaseHomeQueryAdapter extends RecyclerView.Adapter<HomeViewHolder> {
    private final String TAG = "FirebaseHomeQueryAdapte";
    private List<String> mHomePaths;
    private OnSetupViewListener mOnSetupViewListener;

    public FirebaseHomeQueryAdapter(List<String> paths, OnSetupViewListener onSetupViewListener) {
        if (paths == null || paths.isEmpty()) {
            mHomePaths = new ArrayList<>();
        } else {
            mHomePaths = paths;
        }
        mOnSetupViewListener = onSetupViewListener;
    }

    @Override
    public HomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_item, parent, false);
        return new HomeViewHolder(v);
    }

    public void setPaths(List<String> postPaths) {
        mHomePaths = postPaths;
        notifyDataSetChanged();
    }

    public void addItem(String path) {
        if(mHomePaths.contains(path))
            return;

        mHomePaths.add(path);
        notifyItemInserted(mHomePaths.size());
    }

    @Override
    public void onBindViewHolder(final HomeViewHolder holder, int position) {
        DatabaseReference ref = FirebaseManager.getPointsRef().child(mHomePaths.get(position));
        // TODO: Fix this so async event won't bind the wrong view post recycle.
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Point point = dataSnapshot.getValue(Point.class);
                Log.d(TAG, "local key: " + dataSnapshot.getKey());
                mOnSetupViewListener.onSetupView(holder, point, holder.getAdapterPosition(),
                        dataSnapshot.getKey());
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {
                Log.e(TAG, "Error occurred: " + firebaseError.getMessage());
            }
        };
        ref.addValueEventListener(postListener);
        holder.mLocalRef = ref;
        holder.mHomeListener = postListener;
    }

    @Override
    public void onViewRecycled(HomeViewHolder holder) {
        super.onViewRecycled(holder);
        holder.mLocalRef.removeEventListener(holder.mHomeListener);
    }

    @Override
    public int getItemCount() {
        return mHomePaths.size();
    }

    public interface OnSetupViewListener {
        void onSetupView(HomeViewHolder holder, Point local, int position, String postKey);
    }
}
