package dane.asdra;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.media.session.MediaController;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;


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
    boolean lsa;
    Vibrator vibrator;
    private int screenHeight;
    private int screenWidth;
    private TextView textDrag;
    private TextView textDrag2;
    private TextView textDrag3;
    private TextView resp1;
    private TextView resp2;
    private TextView resp3;
    private ImageView image;

    private Map<String, Float> txLocation = new HashMap<>();
    private Map<String, Float> tx2Location = new HashMap<>();
    private Map<String, Float> tx3Location = new HashMap<>();

    private final int POTHO_DISPLAY_LENGTH = 3000;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gamebase);
        juego = getIntent().getStringExtra("juego");
        dificultad = getIntent().getIntExtra("dificultad", 1);
        lsa = getIntent().getBooleanExtra("LSA", false);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        setupScreenDimensions();
        setPlayerBar();
        initGameViews();
        arrayDeResultados = getInstancia(juego , dificultad);




        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if(lsa) {
                    nextScreen(VideoInfoActivity.class, arrayDeResultados.get(0).animal.idVideo);
                    initVideoButton();
                }
            }
        }, POTHO_DISPLAY_LENGTH);

        firstAnimation(image);
        findViewById(R.id.next).setOnClickListener(new NextLevelUp());


        View.OnTouchListener mListener = new View.OnTouchListener() {
            float newX, newY;
            int lastAction = 0;
            private float dX;
            private float dY;
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:

                        dX = view.getX() - motionEvent.getRawX();
                        dY = view.getY() - motionEvent.getRawY();

                        break;

                    case MotionEvent.ACTION_MOVE:

                        newX = motionEvent.getRawX() + dX;
                        newY = motionEvent.getRawY() + dY;

                        // check if the view out of screen
                        if ((newX <= 0 || newX >= screenWidth-view.getWidth()) || (newY <= 0 || newY >= screenHeight-view.getHeight()))
                        {
                            lastAction = MotionEvent.ACTION_MOVE;
                            break;
                        }

                        view.setX(newX);
                        view.setY(newY);

                        lastAction = MotionEvent.ACTION_MOVE;

                        break;

                    case MotionEvent.ACTION_UP:
                         if(isViewInDropzone(view,resp1)){
                         evaluateResults(view,resp1);
                         }
                        if(isViewInDropzone(view,resp2)){
                            evaluateResults(view,resp2);
                        }
                        if(isViewInDropzone(view,resp3)){
                            evaluateResults(view,resp3);
                        }

                        break;

                    default:
                        return false;
                }
                return true;
            }

        };

        textDrag.setOnTouchListener(mListener);
        textDrag2.setOnTouchListener(mListener);
        textDrag3.setOnTouchListener(mListener);

    }

    private void initGameViews() {

        textDrag = (TextView) findViewById(R.id.textoDrag);
        textDrag2 = (TextView)findViewById(R.id.textoDrag2);
        textDrag3 = (TextView)findViewById(R.id.textoDrag3);

        resp1 = (TextView) findViewById(R.id.resp1);
        resp2 = (TextView)findViewById(R.id.resp2);
        resp3 = (TextView)findViewById(R.id.resp3);

        image = (ImageView)findViewById(R.id.imagenPrincipal);
    }

    private void setPlayerBar() {
        View menuBarLayout = findViewById(R.id.brown_bar);
        userPhotoView = (ImageView) menuBarLayout.findViewById(R.id.user_default);
//        userPhotoView.setImageDrawable(Drawable.createFromPath(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
//                + "/" + getBaseContext().getString(R.string.JuguemosTodosPerfiles) + "/" + photo));

        String titleString = "Juego " + decodeGameNumber(juego) + " - Nivel " + dificultad;
        TextView titulo = (TextView) findViewById(R.id.titulo);
        titulo.setText(titleString);
        ImageView backButton = (ImageView) menuBarLayout.findViewById(R.id.back_arrow);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
    }

    private int decodeGameNumber(String juego) {
        if (juego.equals(getString(R.string.juegoDePalabras)))
            return 1;

        return 2;
    }

    private void initVideoButton(){
        findViewById(R.id.user_default).setVisibility(View.VISIBLE);
        findViewById(R.id.user_default).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                nextScreen(VideoInfoActivity.class, arrayDeResultados.get(0).animal.idVideo);

            }
        });
    }

    private void setDatosAlJuego() {
        findViewById(R.id.resp1).setVisibility(View.VISIBLE);
        findViewById(R.id.imagenPrincipal).setBackgroundResource(0);
        findViewById(R.id.imagenPrincipal).setBackgroundResource(arrayDeResultados.get(0).animal.idResource);
        findViewById(R.id.imagenPrincipal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               if(findViewById(R.id.finalDeJuego).getVisibility() == View.VISIBLE){
                   back();
               }
               else{
                   nextScreen(VideoInfoActivity.class, arrayDeResultados.get(0).animal.idVideo);
               }
            }
        });

        if (juego.contains(getString(R.string.juegoDePalabras))) {
            if(generateRandomNumberWithRestriction(99) < 7 )  {
                ((TextView)findViewById(R.id.textoDrag)).setText(arrayDeResultados.get(0).animal.animal);
                ((TextView)findViewById(R.id.textoDrag2)).setText(arrayDeResultados.get(0).respuestaIncorrecta);
            }
            else {
                ((TextView) findViewById(R.id.textoDrag)).setText(arrayDeResultados.get(0).respuestaIncorrecta);
                ((TextView) findViewById(R.id.textoDrag2)).setText(arrayDeResultados.get(0).animal.animal);
            }
            ((TextView)findViewById(R.id.resp1)).setText(arrayDeResultados.get(0).animal.animal);
        }
        //sino
        if (juego.contains(getString(R.string.juegoDeSilabas))) {
            findViewById(R.id.resp2).setVisibility(View.VISIBLE);
            findViewById(R.id.resp3).setVisibility(View.GONE);
            findViewById(R.id.separador1).setVisibility(View.VISIBLE);
            List<String> randomList = new ArrayList<String>();
            randomList.add(arrayDeResultados.get(0).animal.silaba1);
            randomList.add(arrayDeResultados.get(0).animal.silaba2);

            if(dificultad == 1 )  {
                Collections.shuffle(randomList);
            }
            else    /* dificultad == 2 */
            {
                resp3.setVisibility(View.VISIBLE);
                textDrag3.setVisibility(View.VISIBLE);
                findViewById(R.id.separador2).setVisibility(View.VISIBLE);

                randomList.add(arrayDeResultados.get(0).animal.silaba3);
                Collections.shuffle(randomList);

                textDrag3.setText(randomList.get(2));
            }
                textDrag.setText(randomList.get(0));
                textDrag2.setText(randomList.get(1));

            resp1.setText(arrayDeResultados.get(0).animal.silaba1);
            resp2.setText(arrayDeResultados.get(0).animal.silaba2);
            resp3.setText(arrayDeResultados.get(0).animal.silaba3);
        }

        txLocation.put("X",textDrag.getX());
        txLocation.put("Y",textDrag.getY());

        tx2Location.put("X",textDrag2.getX());
        tx2Location.put("Y",textDrag2.getY());

        tx3Location.put("X",textDrag3.getX());
        tx3Location.put("Y",textDrag3.getY());
    }


 //GAME LOGIC
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
                        sendCorrectVibration();
                        mp3 = MediaPlayer.create(getBaseContext(), R.raw.aplausos);
                        mp3.start();
                        findViewById(R.id.user_default).setVisibility(View.INVISIBLE);
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
                                sendCorrectVibration();
                            } else{
                                sendErrorVibration();
                            }
                            break;
                        case R.id.resp2:
                            if (respuesta.contentEquals(arrayDeResultados.get(0).animal.silaba2)){
                                ((TextView) v).setText(respuesta);
                                respuestaDrag.setVisibility(View.INVISIBLE);
                                findViewById(R.id.resp2).setBackgroundResource(0) ;
                                sendCorrectVibration();
                            } else{
                                sendErrorVibration();
                            }
                            break;
                        case R.id.resp3:
                            if (respuesta.contentEquals(arrayDeResultados.get(0).animal.silaba3)){
                                ((TextView) v).setText(respuesta);
                                respuestaDrag.setVisibility(View.INVISIBLE);
                                findViewById(R.id.resp3).setBackgroundResource(0) ;
                                sendCorrectVibration();
                            } else{
                                sendErrorVibration();
                            }
                            break;
                    }
                    if ((((TextView)findViewById(R.id.resp1)).getText().toString().contains(arrayDeResultados.get(0).animal.silaba1))&&
                            (((TextView)findViewById(R.id.resp2)).getText().toString().contains(arrayDeResultados.get(0).animal.silaba2))&&
                            (((TextView)findViewById(R.id.resp3)).getText().toString().contains(arrayDeResultados.get(0).animal.silaba3)) ){
                        // TODO: mostrar toda la palabra.
                        ((TextView) findViewById(R.id.resp2)).setText(arrayDeResultados.get(0).animal.animal);
                        findViewById(R.id.resp1).setVisibility(View.GONE);
                        findViewById(R.id.separador1).setVisibility(View.INVISIBLE);
                        findViewById(R.id.separador2).setVisibility(View.INVISIBLE);
                        findViewById(R.id.resp3).setVisibility(View.GONE);
                        findViewById(R.id.good).setVisibility(View.VISIBLE);
                        findViewById(R.id.next).setVisibility(View.VISIBLE);
                        mp3 = MediaPlayer.create(getBaseContext(), R.raw.aplausos);
                        mp3.start();
                        findViewById(R.id.user_default).setVisibility(View.INVISIBLE);
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

        if(lsa) {

            translateAnimation.setStartOffset(5000);
            translateAnimation.setDuration(1000);
            translateAnimation.setFillAfter(true);

            ScaleAnimation scaleAnimation = new ScaleAnimation(1, 0.8f, 1, 0.8f);
            scaleAnimation.setStartOffset(5000);
            scaleAnimation.setDuration(1000);
            scaleAnimation.setFillAfter(true);

            RotateAnimation rotateAnimation = new RotateAnimation(0, -360);
            rotateAnimation.setStartOffset(5000);
            rotateAnimation.setDuration(1000);
            rotateAnimation.setFillAfter(true);

            AnimationSet animationSet = new AnimationSet(true);
            animationSet.addAnimation(translateAnimation);
            animationSet.addAnimation(rotateAnimation);
            animationSet.addAnimation(scaleAnimation);
            animationSet.setFillAfter(true);

            animationSet.setAnimationListener(new CreateSetAnimation());
            view.setAnimation(animationSet);
            view.startAnimation(animationSet);

            findViewById(R.id.user_default).setVisibility(View.VISIBLE);
        }


        else{

            translateAnimation.setStartOffset(3000);
            translateAnimation.setDuration(1000);
            translateAnimation.setFillAfter(true);

            ScaleAnimation scaleAnimation = new ScaleAnimation(1, 0.8f, 1, 0.8f);
            scaleAnimation.setStartOffset(2000);
            scaleAnimation.setDuration(1000);
            scaleAnimation.setFillAfter(true);

            RotateAnimation rotateAnimation = new RotateAnimation(0, -360);
            rotateAnimation.setStartOffset(2000);
            rotateAnimation.setDuration(1000);
            rotateAnimation.setFillAfter(true);

            AnimationSet animationSet = new AnimationSet(true);
            animationSet.addAnimation(translateAnimation);
            animationSet.addAnimation(rotateAnimation);
            animationSet.addAnimation(scaleAnimation);
            animationSet.setFillAfter(true);

            animationSet.setAnimationListener(new CreateSetAnimation());
            view.setAnimation(animationSet);
            view.startAnimation(animationSet);

        }
    }

    private void back(){
        super.onBackPressed();
    }


    private void sendErrorVibration(){
        // Vibrate for 600 milliseconds
        vibrator.vibrate(600);
    }

    private void sendCorrectVibration(){
        // pattern: delay 0, vibrate 100, delay 100, vibrate 100.
        long[] pattern = { 0, 100, 100, 100};
        vibrator.vibrate(pattern, -1);

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
            textDrag.setVisibility(View.INVISIBLE);
            textDrag2.setVisibility(View.INVISIBLE);
            textDrag3.setVisibility(View.INVISIBLE);
            resp1.setBackgroundResource(0);
            resp2.setBackgroundResource(0);
            resp3.setBackgroundResource(0);
            findViewById(R.id.good).setVisibility(View.INVISIBLE);
            findViewById(R.id.next).setVisibility(View.INVISIBLE);

            if (arrayDeResultados.get(0).animal.idSound != 0){
                mp3 = MediaPlayer.create(getBaseContext(), arrayDeResultados.get(0).animal.idSound);
                if (mp3 != null) {
                    mp3.start();
                }
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {}
    }

    private class NextLevelUp implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            arrayDeResultados.remove(0);
            ((TextView)findViewById(R.id.resp1)).setText("");

            resetInitialTextPositions();

            if (arrayDeResultados.size() > 0) {
                mp3.stop();

                new Handler().postDelayed(new Runnable(){
                    @Override
                    public void run() {

                        if(lsa) {
                            nextScreen(VideoInfoActivity.class, arrayDeResultados.get(0).animal.idVideo);
                            initVideoButton();
                        }
                    }
                }, POTHO_DISPLAY_LENGTH);

                firstAnimation(findViewById(R.id.imagenPrincipal));
            }else{
                textDrag.setVisibility(View.INVISIBLE);
                textDrag2.setVisibility(View.INVISIBLE);
                textDrag3.setVisibility(View.INVISIBLE);

                resp1.setBackgroundResource(0) ;
                resp2.setBackgroundResource(0) ;
                resp3.setBackgroundResource(0) ;
                findViewById(R.id.good).setVisibility(View.INVISIBLE);
                findViewById(R.id.next).setVisibility(View.INVISIBLE);
                image.setBackgroundResource(0);
                image.setVisibility(View.INVISIBLE);
                findViewById(R.id.barraDeAbajo).setVisibility(View.GONE);
                findViewById(R.id.finalDeJuego).setVisibility(View.VISIBLE);
                resp1.setText("");
                mp3.stop();
                mp3 = MediaPlayer.create(getBaseContext(), R.raw.tututututu);
                mp3.start();
            }
        }
    }

    private void resetInitialTextPositions() {
        textDrag.setX(txLocation.get("X"));
        textDrag.setY(txLocation.get("Y"));

        textDrag2.setX(tx2Location.get("X"));
        textDrag2.setY(tx2Location.get("Y"));

        textDrag3.setX(tx3Location.get("X"));
        textDrag3.setY(tx3Location.get("Y"));
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

            arrayDeAnimales.add(new Animal(R.drawable.burro,R.raw.burro,R.raw.video_burro,"BURRO","BU","RRO",""));
            arrayDeAnimales.add(new Animal(R.drawable.cabra,R.raw.cabra,R.raw.video_cabra,"CABRA","CA","BRA",""));
            arrayDeAnimales.add(new Animal(R.drawable.chancho,R.raw.chancho,R.raw.video_chancho,"CHANCHO","CHAN","CHO",""));
            arrayDeAnimales.add(new Animal(R.drawable.foca,R.raw.foca,R.raw.video_foca,"FOCA","FO","CA",""));
            arrayDeAnimales.add(new Animal(R.drawable.gato,R.raw.gato,R.raw.video_gato, "GATO","GA","TO",""));
            arrayDeAnimales.add(new Animal(R.drawable.leon,R.raw.leon,R.raw.video_leon, "LEON","LE","ON",""));
            arrayDeAnimales.add(new Animal(R.drawable.llama,0,R.raw.video_llama, "LLAMA","LLA","MA",""));
            arrayDeAnimales.add(new Animal(R.drawable.loro,R.raw.loro,R.raw.video_loro,"LORO","LO","RO",""));
            arrayDeAnimales.add(new Animal(R.drawable.mono,R.raw.mono,R.raw.video_mono, "MONO","MO","NO",""));
            arrayDeAnimales.add(new Animal(R.drawable.oso,R.raw.oso,R.raw.video_oso,"OSO","O","SO",""));
            arrayDeAnimales.add(new Animal(R.drawable.pato,R.raw.pato,R.raw.video_pato, "PATO","PA","TO",""));
            arrayDeAnimales.add(new Animal(R.drawable.perro,R.raw.perro,R.raw.video_perro, "PERRO","PE","RRO",""));
            arrayDeAnimales.add(new Animal(R.drawable.raton,R.raw.raton,R.raw.video_raton, "RATON","RA","TON",""));
            arrayDeAnimales.add(new Animal(R.drawable.sapo,0,R.raw.video_sapo, "SAPO","SA","PO",""));
            arrayDeAnimales.add(new Animal(R.drawable.vaca,R.raw.vaca,R.raw.video_vaca, "VACA","VA","CA",""));


        }
        else /* dificultad == 2 */
        {
            arrayDeAnimales.add(new Animal(R.drawable.arana,0,R.raw.video_arana,"ARAÑA","A","RA","ÑA"));
            arrayDeAnimales.add(new Animal(R.drawable.ballena,R.raw.ballena,R.raw.video_ballena,"BALLENA","BA","LLE","NA"));
            arrayDeAnimales.add(new Animal(R.drawable.camello,R.raw.camello,R.raw.video_camello,"CAMELLO","CA","ME","LLO"));
            arrayDeAnimales.add(new Animal(R.drawable.cangrejo,0,R.raw.video_cangrejo,"CANGREJO","CAN","GRE","JO"));
            arrayDeAnimales.add(new Animal(R.drawable.canguro,R.raw.canguro,R.raw.video_canguro,"CANGURO","CAN","GU","RO"));
            arrayDeAnimales.add(new Animal(R.drawable.conejo,R.raw.conejo,R.raw.video_conejo,"CONEJO","CO","NE","JO"));
            arrayDeAnimales.add(new Animal(R.drawable.gallina,R.raw.gallina,R.raw.video_gallina,"GALLINA","GA","LLI","NA"));
            arrayDeAnimales.add(new Animal(R.drawable.jirafa,R.raw.jirafa,R.raw.video_jirafa,"JIRAFA","JI","RA","FA"));
            arrayDeAnimales.add(new Animal(R.drawable.mosquito,R.raw.mosquito,R.raw.video_mosquito,"MOSQUITO","MOS","QUI","TO"));
            arrayDeAnimales.add(new Animal(R.drawable.oveja,R.raw.oveja,R.raw.video_oveja,"OVEJA","O","VE","JA"));
            arrayDeAnimales.add(new Animal(R.drawable.pajaro,R.raw.pajaro,R.raw.video_pajaro,"PAJARO","PA","JA","RO"));
            arrayDeAnimales.add(new Animal(R.drawable.pinguino,R.raw.pinguino,R.raw.video_pinguino,"PINGÜINO","PIN","GÜI","NO"));
            arrayDeAnimales.add(new Animal(R.drawable.tortuga,0,R.raw.video_tortuga,"TORTUGA","TOR","TU","GA"));
            arrayDeAnimales.add(new Animal(R.drawable.vibora,R.raw.serpiente,R.raw.video_vibora,"VIBORA","VI","BO","RA"));

        }

        Collections.shuffle(arrayDeAnimales);
        return arrayDeAnimales;
    }

    private void setupScreenDimensions() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        screenHeight = displaymetrics.heightPixels;
        screenWidth = displaymetrics.widthPixels;
    }

    private boolean isViewInDropzone (View firstView,View secondView){
        int[] firstPosition = new int[2];
        int[] secondPosition = new int[2];

        firstView.getLocationOnScreen(firstPosition);
        secondView.getLocationOnScreen(secondPosition);

        // Rect constructor parameters: left, top, right, bottom
        Rect rectFirstView = new Rect(firstPosition[0], firstPosition[1],
                firstPosition[0] + firstView.getMeasuredWidth(), firstPosition[1] + firstView.getMeasuredHeight());
        Rect rectSecondView = new Rect(secondPosition[0], secondPosition[1],
                secondPosition[0] + secondView.getMeasuredWidth(), secondPosition[1] + secondView.getMeasuredHeight());
        return rectFirstView.intersect(rectSecondView);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mp3.stop();
    }

    public class Animal {
        int idResource;
        int idSound;
        int idVideo;
        String animal;
        String silaba1;
        String silaba2;
        String silaba3;
        /* contructor */

        public Animal(int idResource, int idSound, int idVideo, String animal,String silaba1,String silaba2,String silaba3){
            this.idResource=idResource;
            this.idSound=idSound;
            this.idVideo=idVideo;
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
        public Resultados(Animal animal, String animalFalso) {
            this.animal = animal;
            this.respuestaIncorrecta = animalFalso;
        }
    }

    public void evaluateResults(View draggedView,View dropZone){

        if (juego.contains(getString(R.string.juegoDePalabras))) {
            String respuetaCorrecta = arrayDeResultados.get(0).animal.animal;


            TextView respuestaDrag = (TextView) draggedView;
            String respuesta = respuestaDrag.getText().toString();
            if (respuesta.contentEquals(respuetaCorrecta)) {
                ((TextView)dropZone).setText(respuetaCorrecta);
                textDrag.setVisibility(View.INVISIBLE);
                textDrag2.setVisibility(View.INVISIBLE);

                findViewById(R.id.good).setVisibility(View.VISIBLE);
                findViewById(R.id.next).setVisibility(View.VISIBLE);
                sendCorrectVibration();
                mp3 = MediaPlayer.create(getBaseContext(), R.raw.aplausos);
                mp3.start();
                findViewById(R.id.user_default).setVisibility(View.INVISIBLE);
            } else{
                sendErrorVibration();
            }
        }

        if (juego.contains(getString(R.string.juegoDeSilabas))) {
            TextView respuestaDrag = (TextView) draggedView;
            String respuesta = respuestaDrag.getText().toString();
            switch (dropZone.getId()){
                case R.id.resp1:
                    if (respuesta.contentEquals(arrayDeResultados.get(0).animal.silaba1)){
                        ((TextView) dropZone).setText(respuesta);
                        respuestaDrag.setVisibility(View.INVISIBLE);
                        resp1.setBackgroundResource(0) ;
                        sendCorrectVibration();
                    } else{
                        sendErrorVibration();
                    }
                    break;
                case R.id.resp2:
                    if (respuesta.contentEquals(arrayDeResultados.get(0).animal.silaba2)){
                        ((TextView) dropZone).setText(respuesta);
                        respuestaDrag.setVisibility(View.INVISIBLE);
                        resp2.setBackgroundResource(0) ;
                        sendCorrectVibration();
                    } else{
                        sendErrorVibration();
                    }
                    break;
                case R.id.resp3:
                    if (respuesta.contentEquals(arrayDeResultados.get(0).animal.silaba3)){
                        ((TextView) dropZone).setText(respuesta);
                        respuestaDrag.setVisibility(View.INVISIBLE);
                        resp3.setBackgroundResource(0) ;
                        sendCorrectVibration();
                    } else{
                        sendErrorVibration();
                    }
                    break;
            }
            if (resp1.getText().toString().contains(arrayDeResultados.get(0).animal.silaba1)&&
                    resp2.getText().toString().contains(arrayDeResultados.get(0).animal.silaba2)&&
                    resp3.getText().toString().contains(arrayDeResultados.get(0).animal.silaba3)){

                resp2.setText(arrayDeResultados.get(0).animal.animal);
               resp1.setVisibility(View.INVISIBLE);
                resp3.setVisibility(View.INVISIBLE);

                findViewById(R.id.separador1).setVisibility(View.INVISIBLE);
                findViewById(R.id.separador2).setVisibility(View.INVISIBLE);
                findViewById(R.id.good).setVisibility(View.VISIBLE);
                findViewById(R.id.next).setVisibility(View.VISIBLE);

                mp3 = MediaPlayer.create(getBaseContext(), R.raw.aplausos);
                mp3.start();
                findViewById(R.id.user_default).setVisibility(View.INVISIBLE);
            }
        }

    }

}
