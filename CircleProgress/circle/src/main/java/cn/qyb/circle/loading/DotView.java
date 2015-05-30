package cn.qyb.circle.loading;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import com.nineoldandroids.view.ViewHelper;

public class DotView extends View {

    private int mTarget;
    private Paint mPaint;

    public DotView(Context context) {
        super(context);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setColor(Color.BLUE);
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

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(12, 12, 12, mPaint);
    }
}
