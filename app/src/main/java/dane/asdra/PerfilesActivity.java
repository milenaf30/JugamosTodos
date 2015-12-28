package dane.asdra;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import ca.laplanete.mobile.pageddragdropgrid.PagedDragDropGrid;
import dane.asdra.Utils.EditNameDialog;
import dane.asdra.Utils.PlayersAdapter;
import dane.asdra.model.Player;

/**
 * Created with IntelliJ IDEA.
 * User: sergio
 * Date: 01/08/13
 * Time: 14:35
 * To change this template use File | Settings | File Templates.
 */

public class PerfilesActivity extends BaseActivity implements EditNameDialog.EditNameDialogListener {

    private static final int CAMERA_REQUEST = 1888;
    private ImageView imageView;
    private PlayersAdapter playersAdapter;
    private Player actualPlayer;
    private PagedDragDropGrid gridview;
    private static final String  TITULO ="Seleccionar Jugador";
   // private Vector<ImageView> mySDCardImages;
   ArrayList<Player> players = new ArrayList<Player>();
    String juego;
    int dificultad;
    Context context;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perfiles);

        context = getBaseContext();
        View menuBarLayout = findViewById(R.id.brown_bar);
        imageView = (ImageView) menuBarLayout.findViewById(R.id.user_default);
        ImageView  backButton =  (ImageView) menuBarLayout.findViewById(R.id.back_arrow);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    back();
            }
        });


        TextView titulo = (TextView) findViewById(R.id.titulo);
        titulo.setText(TITULO);
        juego = getIntent().getStringExtra("juego");
        dificultad = getIntent().getIntExtra("dificultad",1);

        initPlayersGallery();
    }

    private void initPlayersGallery(){

        players.clear();

        Player player_default = new Player("default_player","default");
        players.add(player_default);
        //mySDCardImages = new Vector<ImageView>();
        File sdDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), getString(R.string.JuguemosTodosPerfiles));
        if (sdDir.exists()) {
            File[] sdDirFiles = sdDir.listFiles();
            for (File singleFile : sdDirFiles) {

                Player player = new Player(singleFile.getName(),decodePlayerNameFromFile(singleFile.getName()));
                players.add(player);

            }
        }


        gridview = (PagedDragDropGrid) findViewById(R.id.gridview);

        playersAdapter = new PlayersAdapter(this,players);

        gridview.setAdapter(playersAdapter);

        gridview.setClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imageView=(ImageView)v.findViewById(R.id.user_photo);


                 if(imageView == null){
                     takePhoto(v);
                 } else {
                     String photo = (String) imageView.getTag();
                     nextScreen(GameOneActivity.class, juego, dificultad, photo);
                 }
            }
        });


    }

    public void takePhoto(View v) {
        actualPlayer = new Player();
        showEditDialog();

    }



    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try{
            if (requestCode == CAMERA_REQUEST) {

                Bitmap photo = (Bitmap) data.getExtras().get("data");


                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = actualPlayer.getName() + "_" + timeStamp + ".jpg";
                File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), getString(R.string.JuguemosTodosPerfiles));
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 80, bytes);

                File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/"+getString(R.string.JuguemosTodosPerfiles)+"/" + imageFileName);
                f.createNewFile();
                //write the bytes in file
                FileOutputStream  fo = new FileOutputStream(f.getAbsoluteFile());
                fo.write(bytes.toByteArray());

                addImageView(f);
            }
        }
        catch (Exception e)
        {
            Log.w("PERFILES_ACTIVITY", " " + e.getMessage());
        }
    }

    private void addImageView(File singleFile) {

        actualPlayer.setPhoto(singleFile.getName());
        players.add(actualPlayer);
        playersAdapter.calculatePages();

        restartActivity();

    }

    private String decodePlayerNameFromFile(String fileName){
        String[] parts = fileName.split("\\_");
        String name = parts[0];
        return name;
    }

    private void restartActivity(){
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    private void back(){
        super.onBackPressed();
    }

    private void showEditDialog() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(new EditNameDialog(), "dlg_edit_name");
       // ft.addToBackStack(null);
        ft.commit();
    }


    @Override
    public void onFinishEditDialog(String inputText) {
        FragmentManager fm = getSupportFragmentManager();
        fm.popBackStack();
        actualPlayer.setName(inputText);
        try{
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        }
        catch (Exception e)
        {

        }
    }



}
