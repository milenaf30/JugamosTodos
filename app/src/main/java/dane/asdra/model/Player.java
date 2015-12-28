package dane.asdra.model;

import android.net.Uri;

/**
 * Created by blaja on 09/04/2015.
 */
public class Player {

    private String photo;

    private String name;

    private  String juego;

    private  int dificultad;

    private Uri uri;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player)) return false;

        Player player = (Player) o;

        if (name != null ? !name.equals(player.name) : player.name != null) return false;
        if (photo != null ? !photo.equals(player.photo) : player.photo != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = photo != null ? photo.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    public Player(String photo, String name) {

        this.photo = photo;
        this.name = name;
    }

    public Player() {
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJuego() {
        return juego;
    }

    public void setJuego(String juego) {
        this.juego = juego;
    }

    public int getDificultad() {
        return dificultad;
    }

    public void setDificultad(int dificultad) {
        this.dificultad = dificultad;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }
}
