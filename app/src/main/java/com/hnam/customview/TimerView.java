package com.hnam.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by nampham on 1/21/19.
 */
public class TimerView extends View {

    private Paint backgroudPaint;
    private TextPaint textPaint;

    //for programmatically
    public TimerView(Context context) {
        super(context);
        init();
    }

    //for drawing from xml
    public TimerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        backgroudPaint = new Paint();
        backgroudPaint.setColor(Color.parseColor("#880e4f"));

        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(ContextCompat.getColor(getContext(), android.R.color.white));
        textPaint.setTextSize(64f * getResources().getDisplayMetrics().scaledDensity);
    }

    private long startTime = 0;
    public void startTimer(){
        startTime = System.currentTimeMillis();
        updateTimer();
    }

    public void stopTimer(){
        startTime = 0;
        removeCallbacks(updateRunnable);
    }

    private void updateTimer(){
        invalidate();

        postDelayed(updateRunnable, 200);
    }

    private Runnable updateRunnable = new Runnable() {
        @Override
        public void run() {
            updateTimer();
        }
    };

    @Override
    protected void onDraw(Canvas canvas) {
        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();

        float centerX = canvasWidth / 2;
        float centerY = canvasHeight / 2;

        float radius = (canvasWidth < canvasHeight ? canvasWidth : canvasHeight) *  0.5f;

        String number = String.valueOf((long)((System.currentTimeMillis() - startTime) * 0.001));
        float textOffsetX = textPaint.measureText(number) * 0.5f;
        float textOffsetY = textPaint.getFontMetrics().ascent * -0.4f;

        canvas.drawCircle(centerX, centerY, radius, backgroudPaint);
        canvas.drawText(number, centerX - textOffsetX, centerY + textOffsetY, textPaint);
    }
}
