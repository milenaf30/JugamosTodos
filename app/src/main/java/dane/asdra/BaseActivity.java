package dane.asdra;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;

/**
 * Created by blaja on 08/04/2015.
 */
public class BaseActivity extends FragmentActivity{



    /**
     * Starts and Activity set it up in the intent adding the page animation to the transition.
     * @param clase    Class of the activity to start.
     */
    public void nextScreen(Class clase){
        Intent intent = new Intent(this, clase);
        startActivity(intent);
        overridePendingTransition(R.anim.vertical_open_main, R.anim.vertical_close_next);
    }


    public void nextScreen(Class clase,String juego,int dificultad,String photo){
        Intent intent = new Intent(this, clase);

        if(photo!=null){

            intent.putExtra("photo", photo);

        }
        intent.putExtra("juego",juego);
        intent.putExtra("dificultad",dificultad);

        startActivity(intent);
        overridePendingTransition(R.anim.vertical_open_main, R.anim.vertical_close_next);
    }


    public void nextScreen(Class clase, boolean LSA){
        Intent intent = new Intent(this, clase);

        intent.putExtra("LSA",LSA);

        startActivity(intent);
        overridePendingTransition(R.anim.vertical_open_main, R.anim.vertical_close_next);
    }

    /**
     * Redefine to add page effect to the back transition.
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.vertical_open_main, R.anim.vertical_close_next);
    }
}
