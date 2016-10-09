package com.example.liam.atlas;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by Ryan on 10/8/2016.
 */

public class MapItemAdapter implements GoogleMap.InfoWindowAdapter {

    ImageView tv;

    public MapItemAdapter(Context context) {
        tv = new ImageView(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        tv.setLayoutParams(lp);
        tv.setMinimumWidth(900);
        tv.setMinimumHeight(900);
        //tv.setMaxWidth(500);
        //tv.setMaxHeight(500);
        //tv.setForegroundGravity(Gravity.CENTER);
    }


    @Override
    public View getInfoWindow(Marker marker) {
        tv.setImageBitmap((Bitmap) marker.getTag());
        return tv;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
