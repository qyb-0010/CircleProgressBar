package cn.qyb.circleprogress;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Timer;
import java.util.TimerTask;

import cn.qyb.circle.CircleProgressBar;
import cn.qyb.circle.loading.SpotsDialog;


public class MainActivity extends Activity implements View.OnClickListener {

    private CircleProgressBar mCP;
    private int mProgress = 0;

    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews() {
        mCP = (CircleProgressBar) findViewById(R.id.circle_progress);
        btn = (Button) findViewById(R.id.dialog);
        btn.setOnClickListener(this);
//        Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        mCP.setProgress(mProgress);
//                        mProgress ++;
//                    }
//                });
//            }
//        }, 1000, 100);
    }

    @Override
    public void onClick(View v) {
        if (v == btn) {
            SpotsDialog dialog = new SpotsDialog(this);
            dialog.withSpotNumber(3)
                    .withTitle("updating")
                    .show();
        }
    }
}
