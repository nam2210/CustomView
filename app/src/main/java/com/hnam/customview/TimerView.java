package com.hnam.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by nampham on 1/21/19.
 */
public class TimerView extends View {
    private static final String TAG = TimerView.class.getSimpleName();

    private static final long MAX_VALUE = 99999;

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

    //định ra cái view đó sẽ có chiều dài chiều rộng như thế nào
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        int maxTextWidth = (int) Math.ceil(textPaint.measureText(String.valueOf(MAX_VALUE)));
        int maxTextHeight = (int) Math.ceil(-fontMetrics.top + fontMetrics.bottom);

        //add padding
        int contentWidth = maxTextWidth + getPaddingLeft() + getPaddingRight();
        int contentHeight = maxTextHeight + getPaddingBottom() + getPaddingTop();

        int contentSize = Math.max(contentWidth, contentHeight);


        int mode = MeasureSpec.getMode(widthMeasureSpec);
        switch (mode){
            case MeasureSpec.AT_MOST:
                Log.e(TAG, "++++++++++++++++++++");
                break;
            case MeasureSpec.EXACTLY:
                Log.e(TAG, "EXACTLY");
                break;
            case MeasureSpec.UNSPECIFIED:
                Log.e(TAG, "UNSPECIFIED");
                break;
        }

        //tính toán size của view chọn content size hay là chọn size tối đa của parent
        int measuredWidth = resolveSize(contentSize, widthMeasureSpec);
        int measuredHeight = resolveSize(contentSize, heightMeasureSpec);
        Log.e(TAG, String.valueOf(measuredWidth));
        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();

        float centerX = canvasWidth / 2;
        float centerY = canvasHeight / 2;

        float radius = (canvasWidth < canvasHeight ? canvasWidth : canvasHeight) *  0.5f;

        long seconds = MAX_VALUE; //(long)((System.currentTimeMillis() - startTime) * 0.001)
        String number = String.valueOf(seconds);
        float textOffsetX = textPaint.measureText(number) * 0.5f;
        float textOffsetY = textPaint.getFontMetrics().ascent * -0.4f;

        canvas.drawCircle(centerX, centerY, radius, backgroudPaint);
        canvas.drawText(number, centerX - textOffsetX, centerY + textOffsetY, textPaint);
    }
}
