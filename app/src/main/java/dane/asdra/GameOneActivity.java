package dane.asdra;

import android.content.ClipData;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: sergio
 * Date: 10/07/13
 * Time: 18:04
 * To change this template use File | Settings | File Templates.
 */
public class GameOneActivity extends BaseActivity {

    List<Resultados> arrayDeResultados = null;
    String juego;
    int dificultad;
    MediaPlayer mp3;
    ImageView userPhotoView;
    String photo;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gamebase);
        juego = getIntent().getStringExtra("juego");
        dificultad = getIntent().getIntExtra("dificultad", 1);
        photo=getIntent().getStringExtra("photo");

        setPlayerBar();

        arrayDeResultados = getInstancia(juego , dificultad);

        firstAnimation(findViewById(R.id.imagenPrincipal));

        findViewById(R.id.textoDrag).setOnTouchListener(new DragTouchListener());
        findViewById(R.id.textoDrag2).setOnTouchListener(new DragTouchListener());
        findViewById(R.id.textoDrag3).setOnTouchListener(new DragTouchListener());


        findViewById(R.id.resp1).setOnDragListener(new DropTouchListener());
        findViewById(R.id.resp2).setOnDragListener(new DropTouchListener());
        findViewById(R.id.resp3).setOnDragListener(new DropTouchListener());
        findViewById(R.id.next).setOnClickListener(new NextLevelUp());
    }

    private void setPlayerBar() {
        View menuBarLayout = findViewById(R.id.brown_bar);
        userPhotoView = (ImageView) menuBarLayout.findViewById(R.id.user_default);


        userPhotoView.setImageDrawable(Drawable.createFromPath(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                + "/" + getBaseContext().getString(R.string.JuguemosTodosPerfiles) + "/" + photo));

        String titleString = "Juego " + decodeGameNumber(juego) + " - Nivel " + dificultad;
        TextView titulo = (TextView) findViewById(R.id.titulo);
        titulo.setText(titleString);
        ImageView  backButton =  (ImageView) menuBarLayout.findViewById(R.id.back_arrow);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
    }

    private int decodeGameNumber(String juego){
         if(juego.equals(getString(R.string.juegoDePalabras)))
            return 1;

        return 2;
    }

    private void setDatosAlJuego() {

        findViewById(R.id.imagenPrincipal).setBackgroundResource(arrayDeResultados.get(0).animal.idResource);
        if (juego.contains(getString(R.string.juegoDePalabras))) {
            if(generateRandomNumberWithRestriction(99) < 7 )  {
                ((TextView)findViewById(R.id.textoDrag)).setText(arrayDeResultados.get(0).animal.animal);
                ((TextView)findViewById(R.id.textoDrag2)).setText(arrayDeResultados.get(0).respuestaIncorrecta);
            }
            else
            {
                ((TextView)findViewById(R.id.textoDrag)).setText(arrayDeResultados.get(0).respuestaIncorrecta);
                ((TextView)findViewById(R.id.textoDrag2)).setText(arrayDeResultados.get(0).animal.animal);
            }
            ((TextView)findViewById(R.id.resp1)).setText(arrayDeResultados.get(0).animal.animal);
        }
        //sino
        if (juego.contains(getString(R.string.juegoDeSilabas))) {
            findViewById(R.id.resp2).setVisibility(View.VISIBLE);
            findViewById(R.id.separador1).setVisibility(View.VISIBLE);
            List<String> randomList = new ArrayList<String>();
            randomList.add(arrayDeResultados.get(0).animal.silaba1);
            randomList.add(arrayDeResultados.get(0).animal.silaba2);

            if(dificultad == 1 )  {
                Collections.shuffle(randomList);
            }
            else    /* dificultad == 2 */
            {
                findViewById(R.id.resp3).setVisibility(View.VISIBLE);
                findViewById(R.id.textoDrag3).setVisibility(View.VISIBLE);
                findViewById(R.id.separador2).setVisibility(View.VISIBLE);

                randomList.add(arrayDeResultados.get(0).animal.silaba3);
                Collections.shuffle(randomList);

                ((TextView)findViewById(R.id.textoDrag3)).setText(randomList.get(2));
            }
            ((TextView)findViewById(R.id.textoDrag)).setText(randomList.get(0));
            ((TextView)findViewById(R.id.textoDrag2)).setText(randomList.get(1));

            ((TextView)findViewById(R.id.resp1)).setText(arrayDeResultados.get(0).animal.silaba1);
            ((TextView)findViewById(R.id.resp2)).setText(arrayDeResultados.get(0).animal.silaba2);
            ((TextView)findViewById(R.id.resp3)).setText(arrayDeResultados.get(0).animal.silaba3);
        }
    }


    private final class DragTouchListener implements View.OnTouchListener {
        public boolean onTouch(View view, MotionEvent motionEvent) {

            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                view.startDrag(data, shadowBuilder, view, 0);


                //view.setVisibility(View.INVISIBLE);
                return true;
            } else {
                //view.setVisibility(View.VISIBLE);
                return false;
            }
        }
    }


/*  private final class DragTouchListener implements View.OnTouchListener {

PRINCIPIO DE IMPLEMENTACION PARADRAG AND DROP EN CUALQUIER PARTE DE LA PANTALLA

        public boolean onTouch(View view, MotionEvent event) {
            int X = (int) event.getRawX();
            int Y = (int) event.getRawY();
            switch (event.getAction() & MotionEvent.ACTION_MASK) {

                case MotionEvent.ACTION_DOWN:


                    RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                 _xDelta = X - lParams.leftMargin;
                    _yDelta = Y - lParams.topMargin;

                    break;
                case MotionEvent.ACTION_UP:
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    break;
                case MotionEvent.ACTION_MOVE:
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                    layoutParams.leftMargin = X - _xDelta;
                    layoutParams.topMargin = Y - _yDelta;
                    layoutParams.rightMargin = -250;
                    layoutParams.bottomMargin = -250;
                    view.setLayoutParams(layoutParams);
                    break;
            }
            _root.invalidate();
            return true;
        }

    }*/

    private final class DropTouchListener implements View.OnDragListener {

        @Override
        public boolean onDrag(View v, DragEvent event) {
            if (event.getAction() == DragEvent.ACTION_DROP) {
                if (juego.contains(getString(R.string.juegoDePalabras))) {
                    String respuetaCorrecta = arrayDeResultados.get(0).animal.animal;
                    TextView respuestaDrag = (TextView) event.getLocalState();
                    String respuesta = respuestaDrag.getText().toString();
                    if (respuesta.contentEquals(respuetaCorrecta)) {
                        ((TextView) v).setText(respuetaCorrecta);
                        findViewById(R.id.textoDrag).setVisibility(View.INVISIBLE);
                        findViewById(R.id.textoDrag2).setVisibility(View.INVISIBLE);

                        findViewById(R.id.good).setVisibility(View.VISIBLE);
                        findViewById(R.id.next).setVisibility(View.VISIBLE);
                        mp3 = MediaPlayer.create(getBaseContext(), R.raw.aplausos);
                        mp3.start();
                    } else{
                        sendErrorVibration();
                    }
                    //v.invalidate();
                }

                if (juego.contains(getString(R.string.juegoDeSilabas))) {
                    TextView respuestaDrag = (TextView) event.getLocalState();
                    String respuesta = respuestaDrag.getText().toString();
                    switch (v.getId()){
                        case R.id.resp1:
                            if (respuesta.contentEquals(arrayDeResultados.get(0).animal.silaba1)){
                                ((TextView) v).setText(respuesta);
                                respuestaDrag.setVisibility(View.INVISIBLE);
                                findViewById(R.id.resp1).setBackgroundResource(0) ;

                            } else{
                                sendErrorVibration();
                            }
                            break;
                        case R.id.resp2:
                            if (respuesta.contentEquals(arrayDeResultados.get(0).animal.silaba2)){
                                ((TextView) v).setText(respuesta);
                                respuestaDrag.setVisibility(View.INVISIBLE);
                                findViewById(R.id.resp2).setBackgroundResource(0) ;

                            } else{
                                sendErrorVibration();
                            }
                            break;
                        case R.id.resp3:
                            if (respuesta.contentEquals(arrayDeResultados.get(0).animal.silaba3)){
                                ((TextView) v).setText(respuesta);
                                respuestaDrag.setVisibility(View.INVISIBLE);
                                findViewById(R.id.resp3).setBackgroundResource(0) ;

                            } else{
                                sendErrorVibration();
                            }
                            break;
                    }
                    if ((((TextView)findViewById(R.id.resp1)).getText().toString().contains(arrayDeResultados.get(0).animal.silaba1))&&
                        (((TextView)findViewById(R.id.resp2)).getText().toString().contains(arrayDeResultados.get(0).animal.silaba2))&&
                        (((TextView)findViewById(R.id.resp3)).getText().toString().contains(arrayDeResultados.get(0).animal.silaba3)) ){
                        findViewById(R.id.good).setVisibility(View.VISIBLE);
                        findViewById(R.id.next).setVisibility(View.VISIBLE);
                        mp3 = MediaPlayer.create(getBaseContext(), R.raw.aplausos);
                        mp3.start();
                    }
                }
            }

            v.invalidate();
            return true;
        }
    }

    private void firstAnimation(View view) {
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        //int xDest = dm.widthPixels/2-(view.getMeasuredWidth()/2);
        //int yDest = dm.heightPixels/2- (view.getMeasuredHeight()/2);
        int xDest = dm.widthPixels/2;
        int yDest = dm.heightPixels/2;
        TranslateAnimation translateAnimation = new TranslateAnimation( 0, -xDest/2 , 0,  -yDest/8);
        translateAnimation.setStartOffset(2000);
        translateAnimation.setDuration(2000);
        translateAnimation.setFillAfter(true);

        ScaleAnimation scaleAnimation = new ScaleAnimation(1, 0.8f , 1,  0.8f);
        scaleAnimation.setStartOffset(2200);
        scaleAnimation.setDuration(1000);
        scaleAnimation.setFillAfter(true);

        RotateAnimation rotateAnimation = new RotateAnimation(0,-360);
        rotateAnimation.setStartOffset(2200);
        rotateAnimation.setDuration(1000);
        rotateAnimation.setFillAfter(true);

        AnimationSet animationSet  = new AnimationSet(true);
        animationSet.addAnimation(translateAnimation);
        animationSet.addAnimation(rotateAnimation);
        animationSet.addAnimation(scaleAnimation);
        animationSet.setFillAfter(true);

        animationSet.setAnimationListener(new CreateSetAnimation());
        view.setAnimation(animationSet);
        view.startAnimation(animationSet);
    }

    private void back(){
        super.onBackPressed();
    }


    private void sendErrorVibration(){
        Vibrator v = (Vibrator) getSystemService(this.getBaseContext().VIBRATOR_SERVICE);

// Vibrate for 300 milliseconds
        v.vibrate(300);

    }

    public class CreateSetAnimation implements Animation.AnimationListener {

        @Override
        public void onAnimationEnd(Animation animation) {

            findViewById(R.id.textoDrag).setVisibility(View.VISIBLE);
            findViewById(R.id.textoDrag2).setVisibility(View.VISIBLE);
            if ((juego.contains(getString(R.string.juegoDeSilabas)))&&(dificultad==2)) {
                findViewById(R.id.textoDrag3).setVisibility(View.VISIBLE);
            }
            findViewById(R.id.resp1).setBackgroundResource(R.drawable.border_bottom) ;
            findViewById(R.id.resp2).setBackgroundResource(R.drawable.border_bottom) ;
            findViewById(R.id.resp3).setBackgroundResource(R.drawable.border_bottom) ;
            findViewById(R.id.textoDrag).setBackgroundResource(R.drawable.shadow_drag);
            findViewById(R.id.textoDrag2).setBackgroundResource(R.drawable.shadow_drag);
            findViewById(R.id.textoDrag3).setBackgroundResource(R.drawable.shadow_drag);
            ((TextView)findViewById(R.id.resp1)).setText("");
            ((TextView)findViewById(R.id.resp2)).setText("");
            ((TextView)findViewById(R.id.resp3)).setText("");
        }

        @Override
        public void onAnimationStart(Animation animation) {
            setDatosAlJuego();
            findViewById(R.id.textoDrag).setVisibility(View.INVISIBLE);
            findViewById(R.id.textoDrag2).setVisibility(View.INVISIBLE);
            findViewById(R.id.textoDrag3).setVisibility(View.INVISIBLE);
            findViewById(R.id.resp1).setBackgroundResource(0) ;
            findViewById(R.id.resp2).setBackgroundResource(0) ;
            findViewById(R.id.resp3).setBackgroundResource(0) ;
            findViewById(R.id.good).setVisibility(View.INVISIBLE);
            findViewById(R.id.next).setVisibility(View.INVISIBLE);

            if (arrayDeResultados.get(0).animal.idSound != 0){
                mp3 = MediaPlayer.create(getBaseContext(), arrayDeResultados.get(0).animal.idSound);
                mp3.start();
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {}
    }

    private class NextLevelUp implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            arrayDeResultados.remove(0);
            if (arrayDeResultados.size() > 0)
            {
                mp3.stop();
                firstAnimation(findViewById(R.id.imagenPrincipal));
            }
            else
            {
                findViewById(R.id.textoDrag).setVisibility(View.INVISIBLE);
                findViewById(R.id.textoDrag2).setVisibility(View.INVISIBLE);
                findViewById(R.id.textoDrag3).setVisibility(View.INVISIBLE);
                findViewById(R.id.resp1).setBackgroundResource(0) ;
                findViewById(R.id.resp2).setBackgroundResource(0) ;
                findViewById(R.id.resp3).setBackgroundResource(0) ;
                findViewById(R.id.good).setVisibility(View.INVISIBLE);
                findViewById(R.id.next).setVisibility(View.INVISIBLE);
                findViewById(R.id.imagenPrincipal).setBackgroundResource(0);
                findViewById(R.id.imagenPrincipal).setVisibility(View.INVISIBLE);
                findViewById(R.id.barraDeAbajo).setVisibility(View.GONE);
                findViewById(R.id.finalDeJuego).setVisibility(View.VISIBLE);
                ((TextView)findViewById(R.id.resp1)).setText("");
                mp3.stop();
                mp3 = MediaPlayer.create(getBaseContext(), R.raw.tututututu);
                mp3.start();
            }
        }
    }

    private List<Resultados> getInstancia(String juego, int dificultad) {
        if (arrayDeResultados == null) {
            // create a instance
            arrayDeResultados = generarResultados(juego, dificultad);
        }
        // return the instance of the singletonClass
        return arrayDeResultados;
    }

    private List<Resultados> generarResultados(String juego, int dificultad) {

        List<Resultados> vectorDeResultados = new ArrayList<Resultados>();
        List<Animal> arrayDeAnimales = getAnimalsSegunDificultad(dificultad);
        if (juego.contains(getString(R.string.juegoDePalabras))){
            for(int x = 0; x <= 9; x++) {
                int y = generateRandomNumberWithRestriction(x);
                vectorDeResultados.add(new Resultados(arrayDeAnimales.get(x) , arrayDeAnimales.get(y).animal));
            }
        }
        else  /* juego == 2 */
        {
            for(int x = 0; x <= 9; x++) {
                vectorDeResultados.add(new Resultados(arrayDeAnimales.get(x) , ""));
            }
        }
        return vectorDeResultados;
    }

    private int generateRandomNumberWithRestriction(int x) {
        Random r = new Random();
        int random = r.nextInt(14); // returns random number between 0 and 14
        if (random != x)
            return random;
        else
            return generateRandomNumberWithRestriction(x);
    }

    private List<Animal> getAnimalsSegunDificultad(int dificultad) {
        List<Animal> arrayDeAnimales = new ArrayList<Animal>();

        if (dificultad == 1){
            arrayDeAnimales.add(new Animal(R.drawable.foca,R.raw.foca,"FOCA","FO","CA",""));
            arrayDeAnimales.add(new Animal(R.drawable.oso,R.raw.oso,"OSO","O","SO",""));
            arrayDeAnimales.add(new Animal(R.drawable.loro,R.raw.loro,"LORO","LO","RO",""));
            arrayDeAnimales.add(new Animal(R.drawable.gallo,R.raw.gallo,"GALLO","GA","LLO",""));
            arrayDeAnimales.add(new Animal(R.drawable.gato,R.raw.gato,"GATO","GA","TO",""));
            arrayDeAnimales.add(new Animal(R.drawable.mono,R.raw.mono,"MONO","MO","NO",""));
            arrayDeAnimales.add(new Animal(R.drawable.pato,R.raw.pato,"PATO","PA","TO",""));
            arrayDeAnimales.add(new Animal(R.drawable.perro,R.raw.perro,"PERRO","PE","RRO",""));
            arrayDeAnimales.add(new Animal(R.drawable.puma,R.raw.puma,"PUMA","PU","MA",""));
            arrayDeAnimales.add(new Animal(R.drawable.vaca,R.raw.vaca,"VACA","VA","CA",""));
            arrayDeAnimales.add(new Animal(R.drawable.zorro,R.raw.zorro,"ZORRO","ZO","RRO",""));
            arrayDeAnimales.add(new Animal(R.drawable.lobo,R.raw.lobo,"LOBO","LO","BO",""));
            arrayDeAnimales.add(new Animal(R.drawable.leon,R.raw.leon,"LEON","LE","ON",""));
            arrayDeAnimales.add(new Animal(R.drawable.raton,R.raw.raton,"RATON","RA","TON",""));
            arrayDeAnimales.add(new Animal(R.drawable.tucan,R.raw.tucan,"TUCAN","TU","CAN",""));
        }
        else /* dificultad == 2 */
        {
            arrayDeAnimales.add(new Animal(R.drawable.ballena,R.raw.ballena,"BALLENA","BA","LLE","NA"));
            arrayDeAnimales.add(new Animal(R.drawable.caballo,R.raw.caballo,"CABALLO","CA","BA","LLO"));
            arrayDeAnimales.add(new Animal(R.drawable.camello,R.raw.camello,"CAMELLO","CA","ME","LLO"));
            arrayDeAnimales.add(new Animal(R.drawable.canguro,R.raw.canguro,"CANGURO","CAN","GU","RO"));
            arrayDeAnimales.add(new Animal(R.drawable.conejo,R.raw.conejo,"CONEJO","CO","NE","JO"));
            arrayDeAnimales.add(new Animal(R.drawable.gallina,R.raw.gallina,"GALLINA","GA","LLI","NA"));
            arrayDeAnimales.add(new Animal(R.drawable.jirafa,R.raw.jirafa,"JIRAFA","JI","RA","FA"));
            arrayDeAnimales.add(new Animal(R.drawable.oveja,R.raw.oveja,"OVEJA","O","VE","JA"));
            arrayDeAnimales.add(new Animal(R.drawable.pajaro,R.raw.pajaro,"PAJARO","PA","JA","RO"));
            arrayDeAnimales.add(new Animal(R.drawable.paloma,R.raw.paloma,"PALOMA","PA","LO","MA"));
            arrayDeAnimales.add(new Animal(R.drawable.serpiente,R.raw.serpiente,"SERPIENTE","SER","PIEN","TE"));
            arrayDeAnimales.add(new Animal(R.drawable.pinguino,R.raw.pinguino,"PINGÜINO","PIN","GÜI","NO"));
            arrayDeAnimales.add(new Animal(R.drawable.pantera,R.raw.pantera,"PANTERA","PAN","TE","RA"));
            arrayDeAnimales.add(new Animal(R.drawable.langosta,R.raw.langosta,"LANGOSTA","LAN","GOS","TA"));
            arrayDeAnimales.add(new Animal(R.drawable.albatro,R.raw.albatro,"ALBATRO","AL","BA","TRO"));
        }

        Collections.shuffle(arrayDeAnimales);
        return arrayDeAnimales;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mp3.stop();
    }

    public class Animal {
        int idResource;
        int idSound;
        String animal;
        String silaba1;
        String silaba2;
        String silaba3;
        /* contructor */
        public Animal(int idResource, int idSound, String animal,String silaba1,String silaba2,String silaba3){
            this.idResource=idResource;
            this.idSound=idSound;
            this.animal=animal;
            this.silaba1=silaba1;
            this.silaba2=silaba2;
            this.silaba3=silaba3;
        }
    }

    public class Resultados {
        Animal animal;
        String respuestaIncorrecta;
        /* contructor */
        public Resultados(Animal animal,String animalFalso){
            this.animal=animal;
            this.respuestaIncorrecta=animalFalso;
        }
    }
}
