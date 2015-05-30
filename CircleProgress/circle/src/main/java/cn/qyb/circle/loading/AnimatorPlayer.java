package cn.qyb.circle.loading;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.AnimatorSet;

public class AnimatorPlayer extends AnimatorListenerAdapter{

    private Animator[] mAnimators;
    private boolean isInterrupt = false;

    public AnimatorPlayer (Animator[] animators) {
        mAnimators = animators;
    }

    @Override
    public void onAnimationEnd(Animator animation) {
        if (!isInterrupt) {
            animate();
        }
    }

    public void play() {
        animate();
    }

    public void stop() {
        isInterrupt = true;
    }

    public boolean isPlaying() {
        return !isInterrupt;
    }

    private void animate() {
        AnimatorSet set = new AnimatorSet();
        set.addListener(this);
        set.playTogether(mAnimators);
        set.start();
    }
}
