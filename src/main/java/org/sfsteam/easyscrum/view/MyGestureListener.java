package org.sfsteam.easyscrum.view;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ArrayAdapter;

import org.sfsteam.easyscrum.data.DeckDT;

/**
 * Created by warmount on 09.04.2014.
 */
public class MyGestureListener extends GestureDetector.SimpleOnGestureListener{

    private final FlyOutContainer root;
    private final ArrayAdapter arrayAdapter;
    private boolean isNewOpen;

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

    public MyGestureListener(boolean isNewOpen, FlyOutContainer root, ArrayAdapter arrayAdapter){
        super();
        this.isNewOpen = isNewOpen;
        this.root = root;
        this.arrayAdapter = arrayAdapter;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        try {
            if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                //right-to-left swipe
                if (!isNewOpen){
                    isNewOpen=false;
                    root.toggleMenuClose();
                    return true;
                }
            }  else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                // left-to-right swipe
                if (!isNewOpen){
                    arrayAdapter.notifyDataSetChanged();
                    root.toggleMenuOpen();

                    return true;
                } else {
                    isNewOpen=false;
                    root.toggleMenuClose();
                    return true;
                }
            }
        } catch (Exception e) {
            // nothing
        }
        return false;
    }
}