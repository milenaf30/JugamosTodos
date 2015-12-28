package dane.asdra;

import android.os.Bundle;
import android.os.Handler;

/**
 * Created by blaja on 09/05/2015.
 */
public class SplashActivity extends BaseActivity {
    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 3000;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        setContentView(R.layout.splash_screen);
        /* New Handler to start the Main-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {

                nextScreen(HomeActivity.class);
                SplashActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}