package cn.qyb.circleprogress;

import android.app.Activity;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

import cn.qyb.circle.CircleProgressBar;


public class MainActivity extends Activity {

    private CircleProgressBar mCP;
    private int mProgress = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews() {
        mCP = (CircleProgressBar) findViewById(R.id.circle_progress);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mCP.setProgress(mProgress);
                        mProgress ++;
                    }
                });
            }
        }, 1000, 100);
    }

}
