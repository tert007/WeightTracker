package com.greenkey.weighttracker.app;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.greenkey.weighttracker.R;

public class CircularProgressBar extends View {

    private int viewWidth;
    private int viewHeight;

    private final float startAngle = 135;      // Always start from top (0 is: "3 o'clock on a watch, -90 is : 12 o'clock on a watch)

    private float sweepAngle = 0;              // How long to sweep from startAngle
    private float maxSweepAngle = 270;         // Max degrees to sweep; 360 = full circle

    private int strokeWidth = 10;              // Width of outline

    private int animationDuration = 1000;       // Animation duration for progress change

    private float progress = 0f;
    private float maxProgress = 100f;             // Max progress to use

    private boolean mDrawPrimaryText = false;           // Set to true if progress text should be drawn
    private boolean mDrawSecondaryText = false;           // Set to true if progress text should be drawn

    private boolean mRoundedCorners = true;     // Set to true if rounded corners should be applied to outline ends

    private final int defaultArcColor = ContextCompat.getColor(getContext(), R.color.light_grey); //light grey
    private int progressArcColor = Color.BLACK;                                                   // Outline color

    private int mPrimaryTextColor = Color.BLACK;       //main text color
    private int mSecondaryTextColor = Color.BLACK;

    private String mPrimaryText;
    private String mSecondaryText;

    private int primaryTextSize = 50;
    private int secondaryTextSize = 24;

    private final Paint mPaint;                 // Allocate paint outside onDraw to avoid unnecessary object creation

    public CircularProgressBar(Context context) {
        this(context, null);
    }

    public CircularProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircularProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    public String getPrimaryText() {
        return mPrimaryText;
    }

    public void setPrimaryText(String mPrimaryText) {
        this.mPrimaryText = mPrimaryText;
    }

    public String getSecondaryText() {
        return mSecondaryText;
    }

    public void setSecondaryText(String mSecondaryText) {
        this.mSecondaryText = mSecondaryText;
    }

    public int getPrimaryTextSize() {
        return primaryTextSize;
    }

    public void setPrimaryTextSize(int primaryTextSize) {
        this.primaryTextSize = primaryTextSize;
    }

    public int getSecondaryTextSize() {
        return secondaryTextSize;
    }

    public void setSecondaryTextSize(int secondaryTextSize) {
        this.secondaryTextSize = secondaryTextSize;
    }

    public float getMaxProgress() {
        return maxProgress;
    }

    public void setMaxProgress(float maxProgress) {
        this.maxProgress = maxProgress;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        initMeasurements();

        drawDefaultArc(canvas);
        drawArc(canvas);

        if (mDrawPrimaryText)
            drawPrimaryText(canvas);

        if (mDrawSecondaryText)
            drawSecondaryText(canvas);
    }

    private void initMeasurements() {
        viewWidth = getWidth();
        viewHeight = getHeight();
    }

    private void drawDefaultArc(Canvas canvas) {
        final int diameter = Math.min(viewWidth, viewHeight);

        final RectF outerOval = new RectF(strokeWidth, strokeWidth, diameter - strokeWidth, diameter - strokeWidth);

        mPaint.setColor(defaultArcColor);
        mPaint.setStrokeWidth(strokeWidth);
        mPaint.setAntiAlias(true);

        mPaint.setStrokeCap(mRoundedCorners ? Paint.Cap.ROUND : Paint.Cap.BUTT);
        mPaint.setStyle(Paint.Style.STROKE);

        canvas.drawArc(outerOval, startAngle, maxSweepAngle, false, mPaint);
    }

    private void drawArc(Canvas canvas) {
        final int diameter = Math.min(viewWidth, viewHeight); //- (strokeWidth * 2);

        final RectF outerOval = new RectF(strokeWidth, strokeWidth, diameter - strokeWidth, diameter - strokeWidth);

        mPaint.setColor(progressArcColor);
        mPaint.setStrokeWidth(strokeWidth);
        mPaint.setAntiAlias(true);

        mPaint.setStrokeCap(mRoundedCorners ? Paint.Cap.ROUND : Paint.Cap.BUTT);
        mPaint.setStyle(Paint.Style.STROKE);

        canvas.drawArc(outerOval, startAngle, sweepAngle, false, mPaint);
    }

    private void drawPrimaryText(Canvas canvas) {
        if (mPrimaryText == null )
            return;

        final float scale = getContext().getResources().getDisplayMetrics().density;
        int textSize = (int) (primaryTextSize * scale + 0.5f);

        mPaint.setTextSize(textSize);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setColor(mPrimaryTextColor);
        mPaint.setStrokeWidth(0);

        // Center text
        int xPos = (canvas.getWidth() / 2) - strokeWidth /2;
        int yPos = (int) ((canvas.getHeight() / 2) - ((mPaint.descent() + mPaint.ascent()) / 2));

        canvas.drawText(mPrimaryText, xPos, yPos, mPaint);
    }

    private void drawSecondaryText(Canvas canvas) {
        if (mSecondaryText == null )
            return;

        final float scale = getContext().getResources().getDisplayMetrics().density;
        int textSize = (int) (secondaryTextSize * scale + 0.5f);

        mPaint.setTextSize(textSize);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setColor(mSecondaryTextColor);
        mPaint.setStrokeWidth(0);

        // Center text
        int xPos = (canvas.getWidth() / 2) - strokeWidth / 2;
        int yPos = canvas.getHeight() - textSize;

        canvas.drawText(mSecondaryText, xPos, yPos, mPaint);
    }

    private float calcSweepAngleFromProgress(float progress) {
        if (progress == 0f)
            return 1f; // Littlest progress

        return (maxSweepAngle / maxProgress) * progress;
    }

    /*
    private int calcProgressFromSweepAngle(float sweepAngle) {
        return (int) ((sweepAngle * maxProgress) / maxSweepAngle);
    }*/

    /**
     * Set progress of the circular progress bar.
     */
    public void setProgress(float progress) {
        if (progress > maxProgress) {
            this.progress = maxProgress;
        } else if(progress < 0) {
            this.progress = 0;
        } else {
            this.progress = progress;
        }

        ValueAnimator animator = ValueAnimator.ofFloat(sweepAngle, calcSweepAngleFromProgress(this.progress));
        animator.setInterpolator(new DecelerateInterpolator());
        animator.setDuration(animationDuration);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                sweepAngle = (float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });

        animator.start();
    }

    public float getProgress() {
        return progress;
    }

    public void setProgressColor(int color) {
        progressArcColor = color;
        invalidate();
    }

    public void setProgressWidth(int width) {
        strokeWidth = width;
        invalidate();
    }

    public void setPrimaryTextColor(int color) {
        mPrimaryTextColor = color;
        invalidate();
    }

    public void SecondaryTextColor(int color) {
        mSecondaryTextColor = color;
        invalidate();
    }

    public void showPrimaryText(boolean show) {
        mDrawPrimaryText = show;
        invalidate();
    }

    public void showSecondaryText(boolean show) {
        mDrawSecondaryText = show;
        invalidate();
    }

    /**
     * Toggle this if you don't want rounded corners on progress bar.
     * Default is true.
     * @param roundedCorners true if you want rounded corners of false otherwise.
     */
    public void useRoundedCorners(boolean roundedCorners) {
        mRoundedCorners = roundedCorners;
        invalidate();
    }
}