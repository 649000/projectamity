/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.me.projectamityandroidofficer;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.Toast;
import com.google.android.maps.OverlayItem;
import java.util.ArrayList;

/**
 *
 * @author student
 */
public class ItemizedOverlay extends com.google.android.maps.ItemizedOverlay {

    private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
    private Context mContext;

    public ItemizedOverlay(Drawable defaultMarker) {
        super(boundCenterBottom(defaultMarker));
    }

    public void addOverlay(OverlayItem overlay) {
        mOverlays.add(overlay);
        populate();

    }

    @Override
    public int size() {
        return mOverlays.size();
    }

    public ItemizedOverlay(Drawable defaultMarker, Context context) {
      //  super(defaultMarker);
        super(boundCenterBottom(defaultMarker));
        mContext = context;
    }

    @Override
    protected boolean onTap(int index) {
        OverlayItem item = mOverlays.get(index);
//        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
//        dialog.setTitle(item.getTitle());
//        dialog.setMessage(item.getSnippet());
//        dialog.setNeutralButton("Ok", null);
//        dialog.show();
         Toast.makeText(mContext, item.getSnippet(), Toast.LENGTH_LONG).show();
        return true;
    }

    @Override
    protected OverlayItem createItem(int i) {
        return mOverlays.get(i);
    }
}
