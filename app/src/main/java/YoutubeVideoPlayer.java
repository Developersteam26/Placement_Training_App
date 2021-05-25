package edu.education.androiddevelopmentcontest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import edu.education.androiddevelopmentcontest.classes.Config;

public class YoutubeVideoPlayer extends YouTubeBaseActivity {

    private YouTubePlayerView videoPlayer;

    private String VIDEO_ID;

    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_video_player);

        videoPlayer = findViewById(R.id.player);

        bundle = getIntent().getExtras();
        VIDEO_ID = bundle.getString("videoid");

        //Toast.makeText(getApplicationContext(),VIDEO_ID,Toast.LENGTH_LONG).show();

        videoPlayer.initialize(Config.YOUTUBE_API_KEY, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.loadVideo(VIDEO_ID);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        });
    }
}
