package cn.qyb.circle.loading;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;

import cn.qyb.circle.R;
import cn.qyb.circle.utils.ScreenUtils;

public class SpotsDialog extends Dialog {

    private FrameLayout mProgress;
    private TextView mTitle;

    private DotView[] mDots;

    private int mCount = 5;
    private AnimatorPlayer mPlayer;
    private int mDialogWidth;
    private int mSpotRadius = 0;

    private int[] mColors;

    public SpotsDialog(Context context) {
        this(context, 0);
    }

    public SpotsDialog(Context context, int theme) {
        super(context, theme);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog);
        initViews(context);
    }

    private void initViews(Context context) {
        mProgress = (FrameLayout) findViewById(R.id.progress);
        mTitle = (TextView) findViewById(R.id.dialog_title);
        mColors = new int[]{Color.BLUE};
        mDialogWidth = ScreenUtils.getScreenWidth(context);
    }

    private void init() {
        mDots = new DotView[mCount];
        for (int i = 0; i < mDots.length; i++) {
            DotView view = new DotView(getContext());
            view.setTarget(mDialogWidth);
            view.setXFactor(-1f);
            view.setColor(mColors[i % mColors.length]);
            if (mSpotRadius > 0) {
                view.setRadius(mSpotRadius);
            }
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

    public SpotsDialog withSpotColors(int... colors) {
        mColors = colors;
        return this;
    }

    public SpotsDialog withSpotRadius(int radius) {
        mSpotRadius = radius;
        return this;
    }

    @Override
    public void show() {
        init();
        super.show();
    }

    @Override
    protected void onStop() {
        stopAnimation();
        super.onStop();
    }
}
