package io.github.xzhang1234.flickrbrowser;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by xiaoyun on 7/5/17.
 */

class FlickrRecyclerViewAdapter extends RecyclerView.Adapter<FlickrRecyclerViewAdapter.FlickrImageViewHolder> {
    private static final String TAG = "FlickrRecyclerViewAdt";
    private List<Photo> photoList;
    private Context context;

    public FlickrRecyclerViewAdapter(List<Photo> photoList, Context context) {
        this.photoList = photoList;
        this.context = context;
    }

    @Override
    public FlickrImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: new view requested");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.browser, parent, false);
        return new FlickrImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FlickrImageViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: starts");
        if (photoList == null || photoList.isEmpty()) {
            holder.thumbnail.setImageResource(R.drawable.ic_add_a_photo_black_48dp);
            holder.title.setText("No photo match your search, please try another keyword");
        } else {
            Photo photo = getPhoto(position);
            Picasso.with(context).load(photo.getImage())
                    .error(R.drawable.ic_add_a_photo_black_48dp)
                    .placeholder(R.drawable.ic_add_a_photo_black_48dp)
                    .into(holder.thumbnail);

            holder.title.setText(photo.getTitle());
        }
    }

    @Override
    public int getItemCount() {
        return ((photoList != null) && (photoList.size() != 0) ? photoList.size() : 1);
    }

    void loadNewData(List<Photo> newPhotos) {
        photoList = newPhotos;
        notifyDataSetChanged();
    }

    public Photo getPhoto(int position) {
        return ((photoList != null) && (photoList.size() != 0) ? photoList.get(position) : null );
    }

    static class FlickrImageViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "FlickrImageViewHolder";
        ImageView thumbnail = null;
        TextView title = null;

        public FlickrImageViewHolder(View itemView) {
            super(itemView);
            Log.d(TAG, "FlickrImageViewHolder: starts");
            this.thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            this.title = (TextView) itemView.findViewById(R.id.title);
        }
    }
}
