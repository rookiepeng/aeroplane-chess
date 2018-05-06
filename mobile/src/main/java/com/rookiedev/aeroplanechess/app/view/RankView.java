package com.rookiedev.aeroplanechess.app.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.rookiedev.aeroplanechess.app.R;
import com.rookiedev.aeroplanechess.app.constants.Constants;

/**
 * TODO: document your custom view class.
 */
public class RankView extends View {
    private Context mContext;
    private Paint mPaint, starPaint;
    private int width, height, padding, itemHeight;
    private int rank1X, rank1Y, rank2X, rank2Y, rank3X, rank3Y, rank4X, rank4Y;
    private int rank1 = -1, rank2 = -1, rank3 = -1, rank4 = -1;
    private Path mPath1, mPath2;

    public RankView(Context context) {
        super(context);
        mContext = context;
        init(null, 0);
    }

    public RankView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(attrs, 0);
    }

    public RankView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(0);
        mPaint.setAntiAlias(true);

        starPaint = new Paint();
        starPaint.setStyle(Paint.Style.FILL);
        starPaint.setStrokeWidth(0);
        starPaint.setAntiAlias(true);

        mPath1 = new Path();
        mPath2 = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        width = getWidth();
        height = getHeight();
        padding = width / 10;
        itemHeight = (height - padding * 2) / 5;
        rank1X = padding;
        rank1Y = padding + itemHeight;
        rank2X = rank1X;
        rank2Y = rank1Y + itemHeight;
        rank3X = rank1X;
        rank3Y = rank2Y + itemHeight;
        rank4X = rank1X;
        rank4Y = rank3Y + itemHeight;

        mPaint.setColor(Color.WHITE);
        mPaint.setAlpha(230);
        canvas.drawRect(0, 0, width, height, mPaint);

        if (rank3 == -1) {
            drawPlane(canvas, rank1, rank1X, rank1Y, 4);
            drawPlane(canvas, rank2, rank2X, rank2Y, 1);
            //drawPlane(canvas, rank3, rank3X, rank3Y, 2);
            //drawPlane(canvas, rank4, rank4X, rank4Y, 1);
        } else {
            drawPlane(canvas, rank1, rank1X, rank1Y, 4);
            drawPlane(canvas, rank2, rank2X, rank2Y, 3);
            drawPlane(canvas, rank3, rank3X, rank3Y, 2);
            drawPlane(canvas, rank4, rank4X, rank4Y, 1);
        }
    }

    private void drawPlane(Canvas canvas, int color, int posX, int posY, int star) {
        String string;
        mPaint.setAlpha(255);
        mPath1.reset();
        mPath1.moveTo(posX + itemHeight / 2, posY + itemHeight / 2 / 3);
        mPath1.lineTo(posX + itemHeight / 2, posY + itemHeight / 2 + itemHeight / 2 / 3);
        mPath1.lineTo((float) (posX + itemHeight / 2 - itemHeight / 3 * Math.cos(40)), (float) (posY + itemHeight / 2 + itemHeight / 3 * Math.sin(40)));
        mPath1.close();

        mPath2.reset();
        mPath2.moveTo(posX + itemHeight / 2, posY + itemHeight / 2 / 3);
        mPath2.lineTo(posX + itemHeight / 2, posY + itemHeight / 2 + itemHeight / 2 / 3);
        mPath2.lineTo((float) (posX + itemHeight / 2 + itemHeight / 3 * Math.cos(40)), (float) (posY + itemHeight / 2 + itemHeight / 3 * Math.sin(40)));
        mPath2.close();

        switch (color) {
            case Constants.RED:
                mPaint.setColor(mContext.getResources().getColor(R.color.redDarkHolo));
                canvas.drawPath(mPath1, mPaint);
                mPaint.setColor(mContext.getResources().getColor(R.color.redLightHolo));
                canvas.drawPath(mPath2, mPaint);
                break;
            case Constants.YELLOW:
                mPaint.setColor(mContext.getResources().getColor(R.color.orangeDarkHolo));
                canvas.drawPath(mPath1, mPaint);
                mPaint.setColor(mContext.getResources().getColor(R.color.orangeLightHolo));
                canvas.drawPath(mPath2, mPaint);
                break;
            case Constants.BLUE:
                mPaint.setColor(mContext.getResources().getColor(R.color.blueDarkHolo));
                canvas.drawPath(mPath1, mPaint);
                mPaint.setColor(mContext.getResources().getColor(R.color.blueLightHolo));
                canvas.drawPath(mPath2, mPaint);
                break;
            case Constants.GREEN:
                mPaint.setColor(mContext.getResources().getColor(R.color.greenDarkHolo));
                canvas.drawPath(mPath1, mPaint);
                mPaint.setColor(mContext.getResources().getColor(R.color.greenLightHolo));
                canvas.drawPath(mPath2, mPaint);
                break;
        }

        switch (star) {
            case 4:
                string = mContext.getString(R.string.four_star);
                break;
            case 3:
                string = mContext.getString(R.string.three_star);
                break;
            case 2:
                string = mContext.getString(R.string.two_star);
                break;
            case 1:
                string = mContext.getString(R.string.one_star);
                break;
            default:
                string = mContext.getString(R.string.one_star);
                break;
        }

        starPaint.setTextSize(itemHeight / 2);
        starPaint.setColor(mContext.getResources().getColor(R.color.grayDark));
        starPaint.setTextAlign(Paint.Align.CENTER);
        Paint.FontMetricsInt fontMetrics = starPaint.getFontMetricsInt();
        //targetRect.top + (targetRect.bottom - targetRect.top) / 2 - (FontMetrics.bottom - FontMetrics.top) / 2 - FontMetrics.top
        int baseline = padding + (padding + itemHeight - padding - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
        canvas.drawText(mContext.getString(R.string.leaderboard), width / 2, baseline, starPaint);

        starPaint.setTextSize(itemHeight / 3);
        starPaint.setColor(mContext.getResources().getColor(R.color.orangeLightHolo));
        starPaint.setTextAlign(Paint.Align.LEFT);
        fontMetrics = starPaint.getFontMetricsInt();
        //targetRect.top + (targetRect.bottom - targetRect.top) / 2 - (FontMetrics.bottom - FontMetrics.top) / 2 - FontMetrics.top
        baseline = posY + (posY + itemHeight - posY - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
        canvas.drawText(string, posX + itemHeight, baseline, starPaint);
    }

    public void setRank(int r1, int r2, int r3, int r4) {
        rank1 = r1;
        rank2 = r2;
        rank3 = r3;
        rank4 = r4;
        invalidate();
    }
}
