package com.swapnil.lostnfound.Views.CustomViews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class LFProgressView extends RelativeLayout {

    // --Commented out by Inspection (8/21/2016 1:21 PM):final static String MATERIALDESIGNXML = "http://schemas.android.com/apk/res-auto";
    // --Commented out by Inspection (8/21/2016 1:21 PM):final static String ANDROIDXML = "http://schemas.android.com/apk/res/android";

    final int disabledBackgroundColor = Color.parseColor("#E2E2E2");
    public int beforeBackground;

    // --Commented out by Inspection (8/21/2016 1:21 PM):public boolean isLastTouch = false;

    public LFProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if(enabled)
            setBackgroundColor(beforeBackground);
        else
            setBackgroundColor(disabledBackgroundColor);
        invalidate();
    }

    boolean animation = false;

    @Override
    protected void onAnimationStart() {
        super.onAnimationStart();
        animation = true;
    }

    @Override
    protected void onAnimationEnd() {
        super.onAnimationEnd();
        animation = false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(animation)
            invalidate();
    }
}