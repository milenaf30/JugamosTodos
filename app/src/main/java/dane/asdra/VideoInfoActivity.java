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
        final VideoView videoHolder = new VideoView(this);
        //if you want the controls to appear
        final MediaController mediaController = new MediaController(this);
        videoHolder.setMediaController(mediaController);
        Uri video = Uri.parse("android.resource://" + getPackageName() + "/"+ getIntent().getIntExtra("videoId",0));
        videoHolder.setVideoURI(video);
        setContentView(videoHolder);
        mediaController.requestFocus();

        //show the mediacontroller , until video ends//
        videoHolder.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp){
                videoHolder.start();
                mediaController.show(0);
            }

        });

        //when the video ends, turn down the video controller//
        videoHolder.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                onBackPressed();
                mediaController.hide();
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
