package com.example.liam.atlas;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by Liam on 10/8/2016.
 */

public class MapItemAdapter implements GoogleMap.InfoWindowAdapter {

    TextView tv;

    public MapItemAdapter(Context context) {
        tv = new TextView(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        tv.setLayoutParams(lp);
        tv.setGravity(Gravity.CENTER);
    }


    @Override
    public View getInfoWindow(Marker marker) {
        tv.setText(marker.getTitle());
        return tv;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
