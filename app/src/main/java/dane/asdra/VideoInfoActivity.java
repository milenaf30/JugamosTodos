package dane.asdra;

import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

/**
 * Created by jasmina on 05/10/16.
 */
public class VideoInfoActivity extends BaseActivity {


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_info);

        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        VideoView videoHolder = new VideoView(this);
        //if you want the controls to appear
        videoHolder.setMediaController(new MediaController(this));
        Uri video = Uri.parse("android.resource://" + getPackageName() + "/"+ getIntent().getIntExtra("videoId",0));
        videoHolder.setVideoURI(video);
        setContentView(videoHolder);
        videoHolder.start();
        videoHolder.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                onBackPressed();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
