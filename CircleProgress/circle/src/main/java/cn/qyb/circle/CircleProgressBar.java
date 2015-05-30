package cn.qyb.circle;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

public class CircleProgressBar extends View {

    private static final int MIN_SIZE = 100;
    private static final int LEFT = 1;
    private static final int RIGHT = 2;
    private static final int TOP = 3;
    private static final int BOTTOM = 4;

    private float mOffset;

    private Paint mReachPaint;
    private Paint mUnreachPaint;
    private Paint mTextPaint;

    private String mText = "0";
    private String mUnit = "%";
    private float mTextSize;

    private int mTextColor;
    private int mReachColor;
    private int mUnReachColor;

    private float mReachBarH;
    private float mUnReachBarH;

    private static final int default_text_color = Color.rgb(66, 145, 241);
    private static final int default_reached_color = Color.rgb(66, 145, 241);
    private static final int default_unreached_color = Color.rgb(204, 204, 204);

    private int mMaxProgress = 100;
    private int mProgress = 0;

    private RectF mRect = new RectF();

    private int mOrientation;
    private boolean mTextVisible;

    private static final String INSTANCE_STATE = "saved_instance";
    private static final String INSTANCE_TEXT_COLOR = "instance_text_color";
    private static final String INSTANCE_TEXT_SIZE = "instance_text_size";
    private static final String INSTANCE_REACH_WIDTH = "instance_reach_width";
    private static final String INSTANCE_REACH_COLOR = "instance_reach_color";
    private static final String INSTANCE_UNREACH_WIDTH = "instance_unreach_width";
    private static final String INSTANCE_UNREACH_COLOR = "instance_unreach_color";
    private static final String INSTANCE_MAX = "instance_max";
    private static final String INSTANCE_PROGRESS = "instance_progress";
    private static final String INSTANCE_TEXT = "instance_text";
    private static final String INSTANCE_UNIT = "instance_unit";

    public CircleProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressBar, defStyleAttr, 0);
        mTextColor = a.getColor(R.styleable.CircleProgressBar_textColor, default_text_color);
        mReachColor = a.getColor(R.styleable.CircleProgressBar_reach_bar_color, default_reached_color);
        mUnReachColor = a.getColor(R.styleable.CircleProgressBar_unreach_bar_color, default_unreached_color);

        mTextSize = a.getDimension(R.styleable.CircleProgressBar_textSize, 40);
        mUnit = a.getString(R.styleable.CircleProgressBar_unit);
        mReachBarH = a.getDimension(R.styleable.CircleProgressBar_reach_bar_height, 10);
        mUnReachBarH = a.getDimension(R.styleable.CircleProgressBar_unreach_bar_height, 6);

        mMaxProgress = a.getInt(R.styleable.CircleProgressBar_max, 100);
        mProgress = a.getInt(R.styleable.CircleProgressBar_progress, 0);

        mOrientation = a.getInt(R.styleable.CircleProgressBar_startPos, BOTTOM);
        mTextVisible = a.getInt(R.styleable.CircleProgressBar_textVisible, 1) == 1;

        a.recycle();
        init();
    }

    private void init() {
        mReachPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mUnreachPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mReachPaint.setColor(mReachColor);
        mUnreachPaint.setColor(mUnReachColor);
        mTextPaint.setColor(default_text_color);

        mReachPaint.setStyle(Paint.Style.STROKE);
        mUnreachPaint.setStyle(Paint.Style.STROKE);
        mReachPaint.setStrokeWidth(mReachBarH);
        mUnreachPaint.setStrokeWidth(mUnReachBarH);

        mOffset = Math.max(mReachPaint.getStrokeWidth(), mUnreachPaint.getStrokeWidth()) / 2;
        if (mUnit == null) {
            mUnit = "%";
        }

        initPaint();
    }

    private void initPaint() {
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float angle = getProgress() / (1.0f * getMaxProgress()) * 360;
        float startAngle = 90;
        if (mOrientation == BOTTOM) {
            startAngle = 90;
        } else if (mOrientation == RIGHT) {
            startAngle = 0;
        } else if (mOrientation == TOP) {
            startAngle = 270;
        } else if (mOrientation == LEFT) {
            startAngle = 180;
        }
        canvas.drawArc(mRect, 0, 360, false, mUnreachPaint);
        canvas.drawArc(mRect, startAngle, angle, false, mReachPaint);

        if (!TextUtils.isEmpty(mText) && mTextVisible) {
            float height = mTextPaint.descent() - mTextPaint.ascent();
            mTextPaint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(getProgress() + mUnit, getWidth() / 2.0f, (getHeight() + height) / 2, mTextPaint);
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
        int height;
        int width;
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
        Bundle bundle = new Bundle();
        bundle.putParcelable(INSTANCE_STATE, super.onSaveInstanceState());
        bundle.putInt(INSTANCE_TEXT_COLOR, mTextColor);
        bundle.putFloat(INSTANCE_TEXT_SIZE, mTextSize);
        bundle.putInt(INSTANCE_REACH_COLOR, mReachColor);
        bundle.putInt(INSTANCE_UNREACH_COLOR, mUnReachColor);
        bundle.putFloat(INSTANCE_REACH_WIDTH, mReachBarH);
        bundle.putFloat(INSTANCE_UNREACH_WIDTH, mUnReachBarH);
        bundle.putInt(INSTANCE_MAX, getMaxProgress());
        bundle.putInt(INSTANCE_PROGRESS, getProgress());
        bundle.putString(INSTANCE_TEXT, mText);
        bundle.putString(INSTANCE_UNIT, mUnit);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            mTextColor = bundle.getInt(INSTANCE_TEXT_COLOR);
            mTextSize = bundle.getFloat(INSTANCE_TEXT_SIZE);
            mReachColor = bundle.getInt(INSTANCE_REACH_COLOR);
            mUnReachColor = bundle.getInt(INSTANCE_UNREACH_COLOR);
            mReachBarH = bundle.getFloat(INSTANCE_REACH_WIDTH);
            mUnReachBarH = bundle.getFloat(INSTANCE_UNREACH_WIDTH);
            mText = bundle.getString(INSTANCE_TEXT);
            mUnit = bundle.getString(INSTANCE_UNIT);
            mMaxProgress = bundle.getInt(INSTANCE_MAX);
            mProgress = bundle.getInt(INSTANCE_PROGRESS);
            initPaint();
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE_STATE));
            return;
        }
        super.onRestoreInstanceState(state);
    }

}
