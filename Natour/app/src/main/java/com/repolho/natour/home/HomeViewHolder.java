package com.repolho.natour.home;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.repolho.natour.R;
import com.repolho.natour.model.Image;
import com.repolho.natour.util.GlideUtil;

/**
 * Created by repolho on 04/10/16.
 */
public class HomeViewHolder
    extends RecyclerView.ViewHolder {

        public interface HomeClickListener {
            void onLongClick(View view);
        }

        private final View mView;
        private HomeClickListener mListener;
        public DatabaseReference mLocalRef;
        public ValueEventListener mHomeListener;

        private static final int POST_TEXT_MAX_LINES = 2;
        private ImageView mImageView;
        private ImageView mIconView;
        private TextView mAuthorView;
        private TextView mHomeTextView;
        private TextView mTimestampView;
        private TextView mDescGeolocation;

        public HomeViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            mImageView = itemView.findViewById(R.id.home_image);
            mIconView = mView.findViewById(R.id.home_author_icon);
            mAuthorView = mView.findViewById(R.id.home_author_name);
            mHomeTextView = itemView.findViewById(R.id.home_text);
            mTimestampView = itemView.findViewById(R.id.home_timestamp);
            mDescGeolocation = itemView.findViewById(R.id.home_desc_geolocation);

            mImageView.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View view) {
                    mListener.onLongClick(view);
                    return false;
                }
            });
        }

    public void setPhoto(Image image) {
        String url = null;
        if(image != null ) url = image.getFullUrl();

        GlideUtil.loadImage(url, mImageView);
    }

    public void setIcon(String url, final String authorId) {
        GlideUtil.loadProfileIcon(url, mIconView);
    }

    public void setAuthor(String author, final String authorId) {
        if (author == null || author.isEmpty()) {
            author = "Desconhecido";//mView.getResources().getString(R.string.user_info_no_name);
        }
        mAuthorView.setText(author);
    }


    public void setText(final String text) {
        if (text == null || text.isEmpty()) {
            mHomeTextView.setVisibility(View.GONE);
            return;
        } else {
            mHomeTextView.setVisibility(View.VISIBLE);
            mHomeTextView.setText(text);
            mHomeTextView.setMaxLines(POST_TEXT_MAX_LINES);
            mHomeTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mHomeTextView.getMaxLines() == POST_TEXT_MAX_LINES) {
                        mHomeTextView.setMaxLines(Integer.MAX_VALUE);
                    } else {
                        mHomeTextView.setMaxLines(POST_TEXT_MAX_LINES);
                    }
                }
            });
        }
    }


    public void setDescGeolocation(final String text) {
        if (text == null || text.isEmpty()) {
            mDescGeolocation.setVisibility(View.GONE);
            return;
        } else {
            mDescGeolocation.setVisibility(View.VISIBLE);
            mDescGeolocation.setText(text);
        }
    }

    public void setTimestamp(String timestamp) {
        mTimestampView.setText(timestamp);
    }

    public void setPostClickListener(HomeClickListener listener) {
        mListener = listener;
    }
}
