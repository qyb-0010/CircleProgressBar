package cn.qyb.circle.loading;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;

import cn.qyb.circle.R;

public class SpotsDialog extends Dialog {

    private FrameLayout mProgress;
    private TextView mTitle;

    private DotView[] mDots;

    private int mCount = 5;
    private AnimatorPlayer mPlayer;
    private int mDialogWidth;

    public SpotsDialog(Context context) {
        super(context);
        setContentView(R.layout.dialog);
        initViews(context);
    }

    private void initViews(Context context) {
        mProgress = (FrameLayout) findViewById(R.id.progress);
        mTitle = (TextView) findViewById(R.id.dialog_title);
        //TODO change to dialog width
        mDialogWidth = getScreenWidth(context);
    }

    private void init() {
        mDots = new DotView[mCount];
        for (int i = 0; i < mDots.length; i++) {
            DotView view = new DotView(getContext());
            view.setTarget(mDialogWidth);
            view.setXFactor(-1f);
            mProgress.addView(view);
            mDots[i] = view;
        }
        mPlayer = new AnimatorPlayer(createAnimations());
        mPlayer.play();
    }

    private Animator[] createAnimations() {
        Animator[] animators = new Animator[mCount];
        for (int i = 0; i < mDots.length; i++) {
            Animator move = ObjectAnimator.ofFloat(mDots[i], "xFactor", 0, 1);
            move.setDuration(1500);
            move.setInterpolator(new SpeedInterpolator());
            move.setStartDelay(100 * i);
            animators[i] = move;
        }
        return animators;
    }

    @Override
    public void dismiss() {
        stopAnimation();
        super.dismiss();
    }

    @Override
    public void cancel() {
        stopAnimation();
        super.cancel();
    }

    private void stopAnimation() {
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.stop();
        }
    }

    public SpotsDialog withTitle(String msg) {
        mTitle.setText(msg);
        return this;
    }

    public SpotsDialog withTitle(int resId) {
        mTitle.setText(resId);
        return this;
    }

    public SpotsDialog withSpotNumber(int number) {
        mCount = number;
        return this;
    }

    public int getScreenWidth(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }

    @Override
    public void show() {
        init();
        super.show();
    }
}
