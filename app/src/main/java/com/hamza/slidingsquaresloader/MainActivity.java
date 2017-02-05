package com.hamza.slidingsquaresloader;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.HandlerThread;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.Button;
import android.widget.RelativeLayout;

import java.util.Random;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class MainActivity extends AppCompatActivity {

    private SlidingSquareLoaderView slidingview;
    private SlidingSquareLoaderView slidingview2;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.slidingview = (SlidingSquareLoaderView) findViewById(R.id.sliding_view);
        this.slidingview2 = (SlidingSquareLoaderView) findViewById(R.id.sliding_view2);
        this.button = (Button) findViewById(R.id.button);

        slidingview.start();

        slidingview2.start();
        slidingview2.setDuration(200);
        slidingview2.setDelay(15);
        slidingview2.setColor(Color.parseColor("#2196F3"));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (slidingview2.isAnimating()){
                    slidingview2.stop();
                }
                else {
                    slidingview2.start();
                }
            }
        });

    }
}
