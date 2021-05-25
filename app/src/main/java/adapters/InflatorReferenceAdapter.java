package edu.education.androiddevelopmentcontest.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import java.util.ArrayList;

import edu.education.androiddevelopmentcontest.R;
import edu.education.androiddevelopmentcontest.classes.Config;
import edu.education.androiddevelopmentcontest.classes.GetReference;

public class InflatorReferenceAdapter extends RecyclerView.Adapter<InflatorReferenceAdapter.ReferenceHolder> {

    public Context context;
    public ArrayList<GetReference> details;
    public OnItemClickListener listener;

    public interface  OnItemClickListener {
        void onIemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public InflatorReferenceAdapter(Context context,ArrayList<GetReference> details) {
        this.context = context;
        this.details =  details;
    }

    @NonNull
    @Override
    public ReferenceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.formulareference,parent,false);
        return new ReferenceHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReferenceHolder holder, int position) {
        final GetReference  getReference = details.get(position);

        holder.textView.setText(getReference.getTitle());
        //holder.tubeThumbnailView.initialize(Config.YOUTUBE_API_KEY, (YouTubeThumbnailView.OnInitializedListener) context.getApplicationContext());
        final YouTubeThumbnailLoader.OnThumbnailLoadedListener onThumbnailLoadedListener = new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
            @Override
            public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                youTubeThumbnailView.setVisibility(View.VISIBLE);

            }

            @Override
            public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {

            }
        };

        holder.tubeThumbnailView.initialize(Config.YOUTUBE_API_KEY, new YouTubeThumbnailView.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {
                youTubeThumbnailLoader.setVideo(getReference.getVideoid());
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

    public class ReferenceHolder extends RecyclerView.ViewHolder {

        public YouTubeThumbnailView tubeThumbnailView;
        public TextView textView;

        public ReferenceHolder(@NonNull View itemView) {
            super(itemView);

            tubeThumbnailView = itemView.findViewById(R.id.videoplayer);
            textView = itemView.findViewById(R.id.title);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onIemClick(position);
                        }
                    }
                }
            });
        }
    }
}
