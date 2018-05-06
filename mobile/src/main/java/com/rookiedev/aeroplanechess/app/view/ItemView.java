package com.rookiedev.aeroplanechess.app.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.rookiedev.aeroplanechess.app.R;

/**
 * TODO: document your custom view class.
 */
public class ItemView extends View {
    public final static int RESUME = 0, NEW = 1, TWOPLAYERS = 2, FOURPLAYERS = 3, REDBLUE = 4, YELLOWGREEN = 5;
    private int height;
    private Paint mPaint, mTextPaint;
    private Context mContext;
    private String itemString;
    private int itemStyle = 0;
    private Path mPath;

    public ItemView(Context context) {
        super(context);
        mContext = context;
        init(null, 0);
    }

    public ItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(attrs, 0);
    }

    public ItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(0);
        mPaint.setAntiAlias(true);

        mTextPaint = new Paint();
        //overlapTextPaint.setColor(mContext.getResources().getColor(R.color.redDarkHolo));
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setStrokeWidth(0);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextAlign(Paint.Align.LEFT);

        mPath = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        height = getHeight();
        mPaint.setColor(Color.WHITE);
        canvas.drawRect(0, 0, getWidth(), height, mPaint);

        switch (itemStyle) {
            case RESUME:
                mPaint.setColor(mContext.getResources().getColor(R.color.orangeLightHolo));
                canvas.drawRect(0, 0, height, height, mPaint);
                mPath.reset();
                mPath.moveTo(height / 4, height / 3);
                mPath.lineTo(height / 4, height * 2 / 3);
                mPath.lineTo(height / 4 + height / 12, height * 2 / 3);
                mPath.lineTo(height / 4 + height / 12, height / 3);
                mPath.close();
                mPath.moveTo(height / 4 + height / 6, height / 3);
                mPath.lineTo(height / 4 + height / 6, height * 2 / 3);
                mPath.lineTo(height / 4 + height / 6 + height / 3, height / 2);
                mPath.close();
                mPaint.setColor(Color.WHITE);
                canvas.drawPath(mPath, mPaint);
                break;
            case NEW:
                mPaint.setColor(mContext.getResources().getColor(R.color.blueLightHolo));
                canvas.drawRect(0, 0, height, height, mPaint);
                mPath.reset();
                mPath.moveTo(height / 3, height / 3);
                mPath.lineTo(height / 3, height * 2 / 3);
                mPath.lineTo(height * 2 / 3, height / 2);
                mPath.close();
                mPaint.setColor(Color.WHITE);
                canvas.drawPath(mPath, mPaint);
                break;
            case TWOPLAYERS:
                mPaint.setColor(mContext.getResources().getColor(R.color.gray));
                canvas.drawRect(0, 0, height, height, mPaint);
                mPaint.setColor(mContext.getResources().getColor(R.color.redLightHolo));
                canvas.drawRect(0, 0, height * 2 / 3, height * 2 / 3, mPaint);
                mPaint.setColor(mContext.getResources().getColor(R.color.blueLightHolo));
                canvas.drawRect(height * 2 / 3, height * 2 / 3, height, height, mPaint);
                break;
            case FOURPLAYERS:
                mPaint.setColor(mContext.getResources().getColor(R.color.gray));
                canvas.drawRect(0, 0, height, height, mPaint);
                mPaint.setColor(mContext.getResources().getColor(R.color.redLightHolo));
                canvas.drawRect(0, 0, height / 3, height / 3, mPaint);
                mPaint.setColor(mContext.getResources().getColor(R.color.orangeLightHolo));
                canvas.drawRect(height * 2 / 3, 0, height, height / 3, mPaint);
                mPaint.setColor(mContext.getResources().getColor(R.color.blueLightHolo));
                canvas.drawRect(height * 2 / 3, height * 2 / 3, height, height, mPaint);
                mPaint.setColor(mContext.getResources().getColor(R.color.greenLightHolo));
                canvas.drawRect(0, height * 2 / 3, height / 3, height, mPaint);
                break;
            case REDBLUE:
                mPaint.setColor(mContext.getResources().getColor(R.color.gray));
                canvas.drawRect(0, 0, height, height, mPaint);
                mPaint.setColor(mContext.getResources().getColor(R.color.redLightHolo));
                canvas.drawRect(0, 0, height / 2, height / 2, mPaint);
                mPaint.setColor(mContext.getResources().getColor(R.color.blueLightHolo));
                canvas.drawRect(height / 2, height / 2, height, height, mPaint);
                break;
            case YELLOWGREEN:
                mPaint.setColor(mContext.getResources().getColor(R.color.gray));
                canvas.drawRect(0, 0, height, height, mPaint);
                mPaint.setColor(mContext.getResources().getColor(R.color.orangeLightHolo));
                canvas.drawRect(height / 2, 0, height, height / 2, mPaint);
                mPaint.setColor(mContext.getResources().getColor(R.color.greenLightHolo));
                canvas.drawRect(0, height / 2, height / 2, height, mPaint);
                break;
        }

        mTextPaint.setTextSize(height / 4);
        //mTextPaint.setColor(mContext.getResources().getColor(R.color.grayDark));
        Paint.FontMetricsInt fontMetrics = mTextPaint.getFontMetricsInt();
        //targetRect.top + (targetRect.bottom - targetRect.top) / 2 - (FontMetrics.bottom - FontMetrics.top) / 2 - FontMetrics.top
        int baseline = (height - 0 - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
        canvas.drawText(itemString, height + height / 10, baseline, mTextPaint);
    }

    public void setStyle(int style) {
        itemStyle = style;
        switch (itemStyle) {
            case RESUME:
                itemString = mContext.getString(R.string.resume_game);
                break;
            case NEW:
                itemString = mContext.getString(R.string.new_game);
                break;
            case TWOPLAYERS:
                itemString = mContext.getString(R.string.two_players);
                break;
            case FOURPLAYERS:
                itemString = mContext.getString(R.string.four_players);
                break;
            case REDBLUE:
                itemString = mContext.getString(R.string.red_vs_blue);
                break;
            case YELLOWGREEN:
                itemString = mContext.getString(R.string.yellow_vs_green);
                break;
        }
    }
}
