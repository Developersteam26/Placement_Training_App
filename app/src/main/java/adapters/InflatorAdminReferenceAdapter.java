package edu.education.androiddevelopmentcontest.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import java.util.ArrayList;

import edu.education.androiddevelopmentcontest.classes.AdminReference;
import edu.education.androiddevelopmentcontest.R;
import edu.education.androiddevelopmentcontest.classes.Config;

public class InflatorAdminReferenceAdapter extends RecyclerView.Adapter<InflatorAdminReferenceAdapter.AdminReferenceHolder> {

    public Context context;
    public ArrayList<AdminReference> details;
    public onItemClickListener listener;

    public interface onItemClickListener {
        public void onDeleteVideo(int position);
    }

    public void setOnItemClickListener(onItemClickListener listener) {
        this.listener = listener;
    }

    public InflatorAdminReferenceAdapter(Context context, ArrayList<AdminReference> details) {
        this.context = context;
        this.details = details;
    }

    @NonNull
    @Override
    public AdminReferenceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adminreference,parent,false);
        return new AdminReferenceHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminReferenceHolder holder, int position) {
        final AdminReference reference = details.get(position);

        holder.title.setText(reference.getTitle().toString());

        final YouTubeThumbnailLoader.OnThumbnailLoadedListener onThumbnailLoadedListener = new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
            @Override
            public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                youTubeThumbnailView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {

            }
        };

        holder.thumbnailView.initialize(Config.YOUTUBE_API_KEY, new YouTubeThumbnailView.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {
                youTubeThumbnailLoader.setVideo(reference.getVideoid());
                youTubeThumbnailLoader.setOnThumbnailLoadedListener(onThumbnailLoadedListener);
            }

            @Override
            public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return details.size();
    }

    public class AdminReferenceHolder extends RecyclerView.ViewHolder {

        private YouTubeThumbnailView thumbnailView;
        private TextView title;
        private LinearLayout deleteVideo;

        public AdminReferenceHolder(@NonNull View itemView) {
            super(itemView);

            thumbnailView = itemView.findViewById(R.id.videoplayer);
            title = itemView.findViewById(R.id.title);
            deleteVideo = itemView.findViewById(R.id.deleteVideo);

            deleteVideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onDeleteVideo(getAdapterPosition());
                }
            });
        }
    }
}
