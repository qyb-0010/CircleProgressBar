package cn.qyb.circle.loading;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import com.nineoldandroids.view.ViewHelper;

public class DotView extends View {

    private static final int DEFAULT_RADIUS = 10;

    private int mTarget;
    private Paint mPaint;
    private int mRadius;

    public DotView(Context context) {
        super(context);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setColor(Color.BLUE);

        mRadius = DEFAULT_RADIUS;
    }

    public void setColor(int color) {
        mPaint.setColor(color);
        invalidate();
    }

    public int getTarget() {
        return mTarget;
    }

    public void setTarget(int target) {
        this.mTarget = target;
    }

    public void setXFactor(float factor) {
        ViewHelper.setX(this, mTarget * factor);
    }

    public float getXFactor() {
        return ViewHelper.getX(this);
    }

    public int getRadius() {
        return mRadius;
    }

    public void setRadius(int radius) {
        this.mRadius = radius;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(mRadius, mRadius, mRadius, mPaint);
    }
}
