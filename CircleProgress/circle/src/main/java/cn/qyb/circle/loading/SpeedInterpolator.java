package cn.qyb.circle.loading;

import android.view.animation.Interpolator;

public class SpeedInterpolator implements Interpolator {


    @Override
    public float getInterpolation(float input) {
        double POW = 1.0/2.0;
        return input < 0.5
                ? (float) Math.pow(input * 2, POW) * 0.5f
                : (float) Math.pow((1 - input) * 2, POW) * -0.5f + 1;
    }
}
