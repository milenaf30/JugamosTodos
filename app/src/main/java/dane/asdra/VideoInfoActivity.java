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

    MediaPlayer mp;
    boolean lsa;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_info);

        lsa = getIntent().getBooleanExtra("LSA", true);

        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        VideoView videoHolder = new VideoView(this);
        //if you want the controls to appear
        videoHolder.setMediaController(new MediaController(this));
        Uri video = Uri.parse("android.resource://" + getPackageName() + "/"+ R.raw.info);
        videoHolder.setVideoURI(video);
        setContentView(videoHolder);
        videoHolder.start();
        videoHolder.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                nextScreen(HomeActivity.class, lsa);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        mp = MediaPlayer.create(getBaseContext(), R.raw.toys_on_the_run);
        mp.start();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mp.stop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mp.stop();
    }
}
