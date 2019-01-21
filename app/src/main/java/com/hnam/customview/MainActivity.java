package com.hnam.customview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    TimerView timerView;
    TimerView timerView1;
    TimerView timerView2;
    TimerView timerView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_1);
        timerView = findViewById(R.id.timerView);
        timerView1 = findViewById(R.id.timerView1);
        timerView2 = findViewById(R.id.timerView2);
        timerView3 = findViewById(R.id.timerView3);
    }

    @Override
    protected void onPause() {
        super.onPause();
        timerView.stopTimer();
        timerView1.stopTimer();
        timerView2.stopTimer();
        timerView3.stopTimer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        timerView.startTimer();
        timerView1.startTimer();
        timerView2.startTimer();
        timerView3.startTimer();

    }
}
