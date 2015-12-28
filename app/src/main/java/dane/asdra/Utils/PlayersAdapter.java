package dane.asdra.Utils;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ca.laplanete.mobile.pageddragdropgrid.PagedDragDropGridAdapter;
import dane.asdra.R;
import dane.asdra.model.Player;

/**
 * Created by blaja on 09/04/2015.
 */
public class PlayersAdapter implements PagedDragDropGridAdapter {



    private Context context;

    private ArrayList<Player> gridValues;

    List<Page> pages = null;
    private int items_per_page;
    private String playerString;


    public PlayersAdapter(Context context,
                                   ArrayList<Player> gridValues) {
        super();
        this.context = context;
        this.gridValues = gridValues;
        this.playerString = context.getResources().getString(R.string.player_tile_number);
        items_per_page =  context.getResources().getInteger(R.integer.items_per_page);
        calculatePages();

    }

    public void calculatePages() {

        pages = new ArrayList<Page>();

        for (int i=0; i < gridValues.size(); i+=items_per_page) {

            Page page = new Page();
            List<Player> items = new ArrayList<Player>();

            for (int j=i; j<gridValues.size() && j < i+items_per_page ; j++) {

                items.add(gridValues.get(j));
                page.setItems(items);
            }

            pages.add(page);
        }
    }

    @Override
    public int pageCount() {
        return pages.size();
    }

    private List<Player> itemsInPage(int page) {
        if (pages.size() > page) {
            return pages.get(page).getItems();
        }
        return Collections.emptyList();
    }

    @Override
    public View view(int page, int index) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View grid_item;

        if(index==0 && page == 0){
            grid_item = inflater.inflate( R.layout.button_take_photo ,null);
        }else{
            grid_item = inflater.inflate( R.layout.player_tile ,null);
        Player item = getItem(page,index);

       //calculate player number
        int n = index;
        if(page>0) {
           n= page*items_per_page;
            if(index>0){
                n=n+index;
            }
        }
        //LayoutInflator to call external grid_item.xml file


        // get layout from grid_item.xml ( Defined Below )

        ImageView imageView = (ImageView) grid_item
                .findViewById(R.id.user_photo);

        TextView textView = (TextView) grid_item
                .findViewById(R.id.user_name);

        TextView textViewNumber = (TextView) grid_item
                .findViewById(R.id.user_number);

        textView.setText(item.getName());
        textViewNumber.setText(playerString + " " + n );

        imageView.setImageDrawable(Drawable.createFromPath(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                + "/" + context.getString(R.string.JuguemosTodosPerfiles) + "/" + item.getPhoto()));
        imageView.setTag(item.getPhoto());
        }

        int width = (int) context.getResources().getDimension(R.dimen.player_tile_width);
        int height = (int) context.getResources().getDimension(R.dimen.player_tile_height);
        if(isTablet(context)){

            grid_item.setLayoutParams(new ViewGroup.LayoutParams(width,height));
        }else{
            grid_item.setLayoutParams(new ViewGroup.LayoutParams(220,280));
        }

        return grid_item;
    }

    public boolean isTablet(Context context) {
        boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE);
        boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (xlarge || large);
    }

    private Player getItem(int page, int index) {
        List<Player> items = itemsInPage(page);
        return items.get(index);
    }

    @Override
    public int rowCount() {
        return AUTOMATIC;
    }

    @Override
    public int columnCount() {
        return AUTOMATIC;
    }

    @Override
    public int itemCountInPage(int page) {
        return itemsInPage(page).size();
    }

    @Override
    public void printLayout() {

    }

    private Page getPage(int pageIndex) {
        return pages.get(pageIndex);
    }

    @Override
    public void swapItems(int pageIndex, int itemIndexA, int itemIndexB) {
        getPage(pageIndex).swapItems(itemIndexA, itemIndexB);
    }

    @Override
    public void moveItemToPreviousPage(int pageIndex, int itemIndex) {
        int leftPageIndex = pageIndex-1;
        if (leftPageIndex >= 0) {
            Page startPage = getPage(pageIndex);
            Page landingPage = getPage(leftPageIndex);

            Player item = startPage.removeItem(itemIndex);
            landingPage.addItem(item);
        }
    }

    @Override
    public void moveItemToNextPage(int pageIndex, int itemIndex) {
        int rightPageIndex = pageIndex+1;
        if (rightPageIndex < pageCount()) {
            Page startPage = getPage(pageIndex);
            Page landingPage = getPage(rightPageIndex);

            Player item = startPage.removeItem(itemIndex);
            landingPage.addItem(item);
        }
    }

    @Override
    public void deleteItem(int pageIndex, int itemIndex) {

        getItem(pageIndex,itemIndex);
        if(itemIndex==0 && pageIndex == 0){
           return;

        }else {
            Player itemToDelete = getPage(pageIndex).deleteItem(itemIndex);
            gridValues.remove(itemToDelete);
            erasePhotoFromStorage(itemToDelete.getPhoto());
            calculatePages();
        }

    }

    private void erasePhotoFromStorage(String photo) {

        File sdDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), context.getString(R.string.JuguemosTodosPerfiles));
        if (sdDir.exists()) {
            File[] sdDirFiles = sdDir.listFiles();
            for (File singleFile : sdDirFiles) {
                if(singleFile.getName().equals(photo)){
                singleFile.delete();
                break;
                }
            }
        }
    }

    @Override
    public int deleteDropZoneLocation() {
        return BOTTOM;
    }

    @Override
    public boolean showRemoveDropZone() {
        return true;
    }

    @Override
    public int getPageWidth(int page) {
        return 0;
    }

    @Override
    public Object getItemAt(int page, int index) {
        return getPage(page).getItems().get(index);
    }

    @Override
    public boolean disableZoomAnimationsOnChangePage() {
        return false;
    }

    public String getPlayerString() {
        return playerString;
    }

    public void setPlayerString(String playerString) {
        this.playerString = playerString;
    }
}
