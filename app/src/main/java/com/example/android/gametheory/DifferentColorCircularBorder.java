package com.example.android.gametheory;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

public class DifferentColorCircularBorder {
    private RelativeLayout parentLayout;

    public DifferentColorCircularBorder(RelativeLayout parentLayout) {
        this.parentLayout = parentLayout;
    }

    public void addBorderPortion(Context context, int color, int startDegree, int endDegree) {
        ProgressBar portion = getBorderPortion(context, color, startDegree, endDegree);
        parentLayout.addView(portion);
    }

    private ProgressBar getBorderPortion(Context context, int color, int startDegree, int endDegree) {
        LayoutInflater inflater = LayoutInflater.from(context);

        ProgressBar portion = (ProgressBar) inflater.inflate(R.layout.progress_timer, parentLayout, false);
        portion.setRotation(startDegree);
        portion.setProgress(endDegree - startDegree);

        portion.getProgressDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) portion.getLayoutParams();
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        portion.setLayoutParams(params);

        return portion;
    }
}
