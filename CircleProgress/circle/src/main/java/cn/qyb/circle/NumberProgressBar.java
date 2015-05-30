package cn.qyb.circle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;

public class NumberProgressBar extends View {

    private int mMaxProgress = 100;
    private int mCurProress = 0;

    private Paint mProgressPaint;
    private Paint mBackPaint;
    private Paint mTextPaint;

    private Context mContext;

    private int mReachBarColor;
    private int mUnReachBarColor;

    private RectF mReachRect = new RectF(0, 0, 0, 0);
    private RectF mUnreachRect = new RectF(0, 0, 0, 0);

    private final int default_text_color = Color.rgb(66, 145, 241);
    private final int default_reached_color = Color.rgb(66, 145, 241);
    private final int default_unreached_color = Color.rgb(204, 204, 204);

    private int mReachBarHeight;
    private int mUnreachBarHeight;

    private boolean needToDrawText = true;
    private float mTextStartPos;
    private float mTextEndPos;

    private String mText;
    private int mTextOffset = 10;

    public NumberProgressBar(Context context) {
        super(context);
        init(context);
    }

    public NumberProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        mProgressPaint = new Paint(Paint.ANTI_ALIAS_FLAG|Paint.DITHER_FLAG);
        mBackPaint = new Paint(Paint.ANTI_ALIAS_FLAG|Paint.DITHER_FLAG);
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mProgressPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mBackPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mProgressPaint.setColor(default_reached_color);
        mBackPaint.setColor(default_unreached_color);
        mTextPaint.setColor(default_text_color);

        mReachBarHeight = (int) dp2px(2.0f);
        mUnreachBarHeight = (int) dp2px(2.0f);

        setProgress(0);
    }

    public void setProgress(int progress) {
        if (progress <=100 && progress >= 0) {
            mCurProress = progress;
            invalidate();
        }
    }

    public void setReachBarHeight(int height) {
        mReachBarHeight = height;
    }

    public void setUnreachBarHeight(int height) {
        mUnreachBarHeight = height;
    }

    private float dp2px(float dp) {
        float density = getResources().getDisplayMetrics().density;
        return density * dp;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (needToDrawText) {
            calcDrawRectF();
        }

        canvas.drawRect(mReachRect, mProgressPaint);
        canvas.drawRect(mUnreachRect, mBackPaint);
        canvas.drawText(mText, mTextStartPos, mTextEndPos, mTextPaint);
    }

    private void calcDrawRectF() {
        mText = getProgress() + "%";
        float textWidth = mTextPaint.measureText(mText);
        if (getProgress() == 0) {
            mTextStartPos = getPaddingLeft();
        } else {
            mReachRect.left = getPaddingLeft();
            mReachRect.top = getHeight() / 2 - mReachBarHeight / 2;
            mReachRect.right = (getWidth() - getPaddingRight() - getPaddingLeft() - textWidth) / (getMaxProgress()*1.0f) * getProgress() - mTextOffset + getPaddingLeft();
            mReachRect.bottom = mReachRect.top + mReachBarHeight;
            mTextStartPos = mReachRect.right + mTextOffset;
        }

        // baseline
        mTextEndPos = (int) ((getHeight() / 2.0f) - ((mTextPaint.descent() + mTextPaint.ascent()) / 2.0f));

        if ((mTextStartPos + textWidth) >= getWidth() - getPaddingRight() ) {

        }

        float unReachBarStart = mTextStartPos + textWidth + mTextOffset;
        if (unReachBarStart < getWidth() - getPaddingRight()) {
            mUnreachRect.left = unReachBarStart;
            mUnreachRect.right = getWidth() - getPaddingRight();
            mUnreachRect.top = getHeight() / 2.0f - mUnreachBarHeight / 2.0f;
            mUnreachRect.bottom = mUnreachRect.top + mUnreachBarHeight;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(doMeasure(widthMeasureSpec, true), doMeasure(heightMeasureSpec, false));
    }

    private int doMeasure(int measureSpec, boolean isWidth) {
        int result;
        int size = MeasureSpec.getSize(measureSpec);
        int mode = MeasureSpec.getMode(measureSpec);
        int padding = isWidth ? getPaddingLeft() + getPaddingRight() : getPaddingBottom() + getPaddingTop();
        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            result = isWidth ? getSuggestedMinimumWidth() : getSuggestedMinimumHeight();
            result += padding;
            if (mode == MeasureSpec.AT_MOST) {
                if (isWidth) {
                    result = Math.max(result, size);
                } else {
                    result = Math.min(result, size);
                }
            }
        }
        return result;
    }

    public int getProgress() {
        return mCurProress;
    }

    public int getMaxProgress() {
        return mMaxProgress;
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        return super.onSaveInstanceState();
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
    }
}
