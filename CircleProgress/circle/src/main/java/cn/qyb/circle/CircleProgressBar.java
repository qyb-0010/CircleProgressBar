package cn.qyb.circle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class CircleProgressBar extends View {

    private static final int MIN_SIZE = 100;

    private float mOffset;

    private Paint mReachPaint;
    private Paint mUnreachPaint;
    private Paint mTextPaint;

    private String mText = "0%";
    private float mTextSize = 40;
    private int mTextColor;

    private static final int default_text_color = Color.rgb(66, 145, 241);
    private static final int default_reached_color = Color.rgb(66, 145, 241);
    private static final int default_unreached_color = Color.rgb(204, 204, 204);

    private int mMaxProgress = 100;
    private int mProgress = 0;

    private RectF mRect = new RectF();

    public CircleProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircleProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        mReachPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mUnreachPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mReachPaint.setColor(default_reached_color);
        mUnreachPaint.setColor(default_unreached_color);
        mTextPaint.setColor(default_text_color);

        mReachPaint.setStyle(Paint.Style.STROKE);
        mUnreachPaint.setStyle(Paint.Style.STROKE);
        mReachPaint.setStrokeWidth(10); // TODO
        mUnreachPaint.setStrokeWidth(5); // TODO

        mOffset = Math.max(mReachPaint.getStrokeWidth(), mUnreachPaint.getStrokeWidth()) / 2;

        initPaint();
    }

    private void initPaint() {
        mTextPaint.setColor(default_text_color);
        mTextPaint.setTextSize(mTextSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.GREEN);
        float angle = getProgress() / (1.0f * getMaxProgress()) * 360;
        float startAngle = 90;
        canvas.drawArc(mRect, 0, 360, false, mUnreachPaint);
        canvas.drawArc(mRect, startAngle, angle, false, mReachPaint);

        // TODO draw text
        if (!TextUtils.isEmpty(mText)) {
            float height = mTextPaint.descent() - mTextPaint.ascent();
            mTextPaint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(getProgress() + "%", getWidth() / 2.0f, (getHeight() + height) / 2, mTextPaint);
        }
    }

    public void setProgress(int progress) {
        if (progress >= 0 && progress <= mMaxProgress) {
            mProgress = progress;
            invalidate();
        }
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

    public void setText(String text) {
        mText = text;
        invalidate();
    }

    public void setText(int resId) {
        mText = getResources().getString(resId);
        invalidate();
    }

    public void setTextColor(int color) {
        mTextColor = color;
        invalidate();
    }

    public void setTextSize(float size) {
        mTextSize = size;
        invalidate();
    }

    @Override
    public void invalidate() {
        initPaint();
        super.invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height = 0;
        int width = 0;
        int w = MeasureSpec.getSize(widthMeasureSpec);
        int h = MeasureSpec.getSize(heightMeasureSpec);
        int wMode = MeasureSpec.getMode(widthMeasureSpec);
        int hMode = MeasureSpec.getMode(heightMeasureSpec);
        if (wMode == MeasureSpec.EXACTLY) {
            width = w;
        } else {
            width = MIN_SIZE;
            if (wMode == MeasureSpec.AT_MOST) {
                width = Math.min(width, w);
            }
        }

        if (hMode == MeasureSpec.EXACTLY) {
            height = h;
        } else {
            height = MIN_SIZE;
            if (hMode == MeasureSpec.AT_MOST) {
                height = Math.min(height, h);
            }
        }
        if (width > height) {
            mRect.set((width - height) / 2 + mOffset, mOffset, (width + height) / 2 - mOffset, height - mOffset);
        } else {
            mRect.set(mOffset, (height - width) / 2 + mOffset, width - mOffset, (height + width) / 2 - mOffset);
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        //TODO
        return super.onSaveInstanceState();
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        //TODO
        super.onRestoreInstanceState(state);
    }
}
