package org.me.projectamityandroid;

import android.graphics.drawable.Drawable;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;
import java.util.ArrayList;

public class CabpoolYouItemizedOverlay extends ItemizedOverlay
{

    // Stores each of the OverlayItem objects you want on the map
    private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
    // private Context mContext;

    public CabpoolYouItemizedOverlay(Drawable defaultMarker)
    {
        super(boundCenterBottom(defaultMarker));
    }

//    public CabpoolYouItemizedOverlay(Drawable defaultMarker, Context context)
//    {
//        super(boundCenterBottom(defaultMarker));
//        mContext = context;
//    }

    public void addOverlay(OverlayItem overlay)
    {
        mOverlays.add(overlay);
        populate();
    }

    public void removeOverlay(int index)
    {
        mOverlays.remove(index);
        populate();
    }

    public void removeAllOverlays()
    {
        mOverlays = new ArrayList<OverlayItem>();
    }

    @Override
    protected OverlayItem createItem(int i)
    {
        return mOverlays.get(i);
    }

    @Override
    public int size()
    {
      return mOverlays.size();
    }

    @Override
    protected boolean onTap(int index)
    {
//        OverlayItem item = mOverlays.get(index);
//        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
//        dialog.setTitle(item.getTitle());
//        dialog.setMessage(item.getSnippet());
//        dialog.setPositiveButton("Yes", new OnClickListener()
//                                        {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which)
//                                            {
//                                                dialog.dismiss();
//                                            }
//                                        });
//        dialog.show();
        return true;
    }

}
