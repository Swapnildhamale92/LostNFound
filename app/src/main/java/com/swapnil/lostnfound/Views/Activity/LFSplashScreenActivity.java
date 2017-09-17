package com.swapnil.lostnfound.Views.Activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;


import com.swapnil.lostnfound.R;


public class LFSplashScreenActivity extends AppCompatActivity {

    private Thread mSplashThread;
    private TextView tvSplash;
    private ImageView imSplash;
    Typeface face;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        tvSplash = (TextView)findViewById(R.id.splash_text);
        imSplash = (ImageView)findViewById(R.id.splash_logo);
        imSplash.setAnimation(inFromTopAnimation(1000));
        tvSplash.setAnimation(inFromBottomAnimation(1000));
        /*Animation a = AnimationUtils.loadAnimation(this, R.anim.in_from_bottom);
        a.reset();
        tvSplash.clearAnimation();
        tvSplash.startAnimation(a);*/
        face = Typeface.createFromAsset(getApplicationContext().getAssets(),
                "font/RobotoCondensed-Regular.ttf");
        tvSplash.setTypeface(face);
        mSplashThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    synchronized (this) {
                        wait(4000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Intent i = new Intent(LFSplashScreenActivity.this,LFBaseActivity.class);
                startActivity(i);
                finish();

            }
        });
        mSplashThread.start();

    }

    public Animation inFromTopAnimation(long durationMillis) {
        Animation inFromTop = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT,  0.0f, Animation.RELATIVE_TO_PARENT,  0.0f,
                Animation.RELATIVE_TO_PARENT,  -1.0f, Animation.RELATIVE_TO_PARENT,   0.0f
        );
        inFromTop.setDuration(durationMillis);
        return inFromTop;
    }

    public Animation inFromBottomAnimation(long durationMillis) {
        Animation inFromBottom = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT,  -1.0f, Animation.RELATIVE_TO_PARENT,  0.0f,
                Animation.RELATIVE_TO_PARENT,  0.0f, Animation.RELATIVE_TO_PARENT,   0.0f
        );
        inFromBottom.setDuration(durationMillis);
        return inFromBottom;
    }
}
