package com.hnam.customview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    TimerView timerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        timerView = findViewById(R.id.timerView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        timerView.stopTimer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        timerView.startTimer();

    }
}
