package com.example.azadljy.simulateturnplate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.azadljy.simulateturnplate.TurnPlate.TurnPlateSurfaceView;

public class MainActivity extends AppCompatActivity {
    TurnPlateSurfaceView view;
    boolean isStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        view = (TurnPlateSurfaceView) findViewById(R.id.view);
    }

    public void start(View v) {
        view.changeState(TurnPlateSurfaceView.STATE_SPEEDUP);
//        isStart = true;
//        v.setEnabled(!isStart);
    }

    public void stop(View v) {
        view.changeState(TurnPlateSurfaceView.STATE_SPEEDCUT);
//        isStart = false;
//        v.setEnabled(isStart);
    }
}
