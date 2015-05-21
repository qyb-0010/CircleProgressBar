package cn.qyb.circle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import org.apache.http.HeaderIterator;

/**
 * Created by qiaoyubo on 2015/5/18.
 */
public class WaveProgressBar extends View {

    private static final int WAVE_SPEED = 14;
    private static final int STRETCH_FACTOR_A = 20;
    private float[] mYPosition;
    private float[] mDyPosition;
    private float mFactor;

    private Paint mPaint;
    private int mWaveColor = Color.rgb(66, 145, 241);

    private int mCurrentPos = 0;

    private int mWidth;
    private int mHeight;

    private int mMaxProgress = 100;
    private int mProgress = 0;


    public WaveProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaveProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG|Paint.DITHER_FLAG);
        mPaint.setColor(mWaveColor);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mWidth = w;
        mHeight = h;
        mFactor = (float) (Math.PI * 2 / w);
        mYPosition = new float[w];
        mDyPosition = new float[w];

        for (int i = 0; i <  mWidth; i++) {
            mYPosition[i] = (float) (STRETCH_FACTOR_A * Math.sin(mFactor * i));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (getProgress() == 0) {
            return;
        }
        calcYPosition();
        for (int i = 0; i < mWidth; i++) {
            canvas.drawLine(i, mHeight - mDyPosition[i] - ((float)getProgress() / getMaxProgress()) * mHeight, i, mHeight, mPaint);
        }

        mCurrentPos += WAVE_SPEED;
        if (mCurrentPos >= mWidth) {
            mCurrentPos = 0;
        }
        postInvalidate();
    }

    private void calcYPosition() {
        int offset = mYPosition.length - mCurrentPos;
        System.arraycopy(mYPosition, mCurrentPos, mDyPosition, 0, offset);
        System.arraycopy(mYPosition, 0, mDyPosition, offset, mCurrentPos);
    }

    public void setProgress(int progress) {
        if (progress < 0 || progress > mMaxProgress) {
            return;
        }
        this.mProgress = progress;
        invalidate();
    }

    public int getProgress() {
        return mProgress;
    }

    public void setMaxProgress(int maxProgress) {
        if (maxProgress > 0) {
            mMaxProgress = maxProgress;
            invalidate();
        }
    }

    public int getMaxProgress() {
        return mMaxProgress;
    }
}
