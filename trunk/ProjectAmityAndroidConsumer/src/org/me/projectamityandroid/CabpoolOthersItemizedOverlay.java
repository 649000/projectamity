package org.me.projectamityandroid;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.drawable.Drawable;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;
import java.util.ArrayList;

public class CabpoolOthersItemizedOverlay extends ItemizedOverlay
{

    // Stores each of the OverlayItem objects you want on the map
    private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
    private Context mContext;
    private MobileHome top;
    private OverlayItem currentItem;

    public CabpoolOthersItemizedOverlay(Drawable defaultMarker)
    {
        super(boundCenterBottom(defaultMarker));
    }

    public CabpoolOthersItemizedOverlay(Drawable defaultMarker, Context context)
    {
        super(boundCenterBottom(defaultMarker));
        mContext = context;
    }

    public CabpoolOthersItemizedOverlay(Drawable defaultMarker, Context context, MobileHome top)
    {
        super(boundCenterBottom(defaultMarker));
        mContext = context;
        this.top = top;
    }

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
        currentItem = mOverlays.get(index);
        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
        dialog.setTitle(currentItem.getTitle());
        dialog.setMessage(currentItem.getSnippet());
        dialog.setPositiveButton("Send Message", new OnClickListener()
                                        {
                                            final OverlayItem item = currentItem;
                                            @Override
                                            public void onClick(DialogInterface dialog, int which)
                                            {
                                                top.setPotentialCabpooler( item.getTitle() );
                                                top.setPm(true);
                                                top.pmCabpoool();
                                                dialog.dismiss();
                                            }
                                        });
        dialog.setNegativeButton("Cancel", new OnClickListener()
                                        {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which)
                                            {
                                                dialog.dismiss();
                                            }
                                        });
        dialog.show();
        return true;
    }

}