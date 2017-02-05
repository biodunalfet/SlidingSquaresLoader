package com.hamza.slidingsquaresloader;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Hamza Fetuga on 2/3/2017.
 */

public class Square extends View {

    boolean isMover = false;

    public boolean isMover() {
        return isMover;
    }

    public void setMover(boolean mover) {
        isMover = mover;
    }

    public Square(Context context) {
        super(context);
        setBackgroundResource(R.drawable.square);
    }

    public Square(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Square(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public Square(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setColor(int color){
        getBackground().setColorFilter(color, PorterDuff.Mode.SRC_IN);
    }



}
