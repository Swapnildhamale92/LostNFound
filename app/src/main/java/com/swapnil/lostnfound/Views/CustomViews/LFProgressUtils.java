package com.swapnil.lostnfound.Views.CustomViews;

import android.content.res.Resources;
import android.util.TypedValue;

public class LFProgressUtils {

    public static int dpToPx(float dp, Resources resources){
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
        return (int) px;
    }
}

