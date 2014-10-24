package com.rookiedev.aeroplanechess.app.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.rookiedev.aeroplanechess.app.R;
import com.rookiedev.aeroplanechess.app.constants.Constants;

/**
 * TODO: document your custom view class.
 */
public class Plane extends View {
    private Context mContext;
    private Paint mPaint;
    private Paint overlapTextPaint, overlapOutline, overlapBack;
    private int mColor;
    private int Step = -1, nextStep;
    private int planeNumber;
    private int posX, posY, nextposX, nextposY;
    private Path mPath1, mPath2;
    private int overlap = 1;
    private RectF overlapRect;
    private int angle = 0;

    public Plane(int planenumber, int color, Context context) {
        super(context);
        mColor = color;
        planeNumber = planenumber;
        init(context, null, 0);
    }

    public Plane(int planenumber, int color, Context context, AttributeSet attrs) {
        super(context, attrs);
        mColor = color;
        planeNumber = planenumber;
        init(context, attrs, 0);
    }

    public Plane(int planenumber, int color, Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mColor = color;
        planeNumber = planenumber;
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        // Load attributes
        mContext = context;
        mPath1 = new Path();
        mPath2 = new Path();

        mPaint = new Paint();
        mPaint.setColor(mContext.getResources().getColor(R.color.redDarkHolo));
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(0);
        mPaint.setAntiAlias(true);

        overlapRect = new RectF();
        overlapTextPaint = new Paint();
        //overlapTextPaint.setColor(mContext.getResources().getColor(R.color.redDarkHolo));
        overlapTextPaint.setStyle(Paint.Style.FILL);
        overlapTextPaint.setStrokeWidth(0);
        overlapTextPaint.setAntiAlias(true);
        overlapTextPaint.setTextAlign(Paint.Align.CENTER);

        overlapOutline = new Paint();
        //overlapOutline.setColor(mContext.getResources().getColor(R.color.redDarkHolo));
        overlapOutline.setStyle(Paint.Style.STROKE);
        //overlapOutline.setStrokeWidth(0);
        overlapOutline.setAntiAlias(true);

        overlapBack = new Paint();
        overlapBack.setColor(Color.WHITE);
        overlapBack.setAlpha(200);
        overlapBack.setStyle(Paint.Style.FILL);
        overlapBack.setStrokeWidth(0);
        overlapBack.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int center = getWidth() / 2;
        int radius = getWidth() / 2 - 1;
        mPath1.moveTo(center, center / 3);
        mPath1.lineTo(center, center + center / 3);
        mPath1.lineTo((float) (center - center * 2 / 3 * Math.cos(40)), (float) (center + center * 2 / 3 * Math.sin(40)));
        mPath1.close();

        mPath2.moveTo(center, center / 3);
        mPath2.lineTo(center, center + center / 3);
        mPath2.lineTo((float) (center + center * 2 / 3 * Math.cos(40)), (float) (center + center * 2 / 3 * Math.sin(40)));
        mPath2.close();
        mPaint.setColor(Color.WHITE);
        canvas.drawCircle(center, center, radius, mPaint);

        canvas.save();
        canvas.rotate(angle, center, center);
        switch (mColor) {
            case Constants.RED:
                mPaint.setColor(mContext.getResources().getColor(R.color.redDarkHolo));
                canvas.drawCircle(center, center, radius - 1, mPaint);

                mPaint.setColor(Color.WHITE);
                canvas.drawCircle(center, center, radius * 4 / 5, mPaint);

                mPaint.setColor(mContext.getResources().getColor(R.color.redDarkHolo));
                canvas.drawPath(mPath1, mPaint);
                mPaint.setColor(mContext.getResources().getColor(R.color.redLightHolo));
                canvas.drawPath(mPath2, mPaint);
                overlapTextPaint.setColor(mContext.getResources().getColor(R.color.redDarkHolo));
                overlapOutline.setColor(mContext.getResources().getColor(R.color.redDarkHolo));
                break;
            case Constants.YELLOW:
                mPaint.setColor(mContext.getResources().getColor(R.color.orangeDarkHolo));
                canvas.drawCircle(center, center, radius - 1, mPaint);

                mPaint.setColor(Color.WHITE);
                canvas.drawCircle(center, center, radius * 4 / 5, mPaint);

                mPaint.setColor(mContext.getResources().getColor(R.color.orangeDarkHolo));
                canvas.drawPath(mPath1, mPaint);
                mPaint.setColor(mContext.getResources().getColor(R.color.orangeLightHolo));
                canvas.drawPath(mPath2, mPaint);
                overlapTextPaint.setColor(mContext.getResources().getColor(R.color.orangeDarkHolo));
                overlapOutline.setColor(mContext.getResources().getColor(R.color.orangeDarkHolo));
                break;
            case Constants.BLUE:
                mPaint.setColor(mContext.getResources().getColor(R.color.blueDarkHolo));
                canvas.drawCircle(center, center, radius - 1, mPaint);

                mPaint.setColor(Color.WHITE);
                canvas.drawCircle(center, center, radius * 4 / 5, mPaint);

                mPaint.setColor(mContext.getResources().getColor(R.color.blueDarkHolo));
                canvas.drawPath(mPath1, mPaint);
                mPaint.setColor(mContext.getResources().getColor(R.color.blueLightHolo));
                canvas.drawPath(mPath2, mPaint);
                overlapTextPaint.setColor(mContext.getResources().getColor(R.color.blueDarkHolo));
                overlapOutline.setColor(mContext.getResources().getColor(R.color.blueDarkHolo));
                break;
            case Constants.GREEN:
                mPaint.setColor(mContext.getResources().getColor(R.color.greenDarkHolo));
                canvas.drawCircle(center, center, radius - 1, mPaint);

                mPaint.setColor(Color.WHITE);
                canvas.drawCircle(center, center, radius * 4 / 5, mPaint);

                mPaint.setColor(mContext.getResources().getColor(R.color.greenDarkHolo));
                canvas.drawPath(mPath1, mPaint);
                mPaint.setColor(mContext.getResources().getColor(R.color.greenLightHolo));
                canvas.drawPath(mPath2, mPaint);
                overlapTextPaint.setColor(mContext.getResources().getColor(R.color.greenDarkHolo));
                overlapOutline.setColor(mContext.getResources().getColor(R.color.greenDarkHolo));
                break;
            case Constants.DOWN:
                mPaint.setColor(mContext.getResources().getColor(R.color.grayDark));
                canvas.drawCircle(center, center, radius - 1, mPaint);

                mPaint.setColor(Color.WHITE);
                canvas.drawCircle(center, center, radius * 4 / 5, mPaint);

                mPaint.setColor(mContext.getResources().getColor(R.color.grayDark));
                canvas.drawPath(mPath1, mPaint);
                mPaint.setColor(mContext.getResources().getColor(R.color.grayLight));
                canvas.drawPath(mPath2, mPaint);
                break;
        }
        canvas.restore();
        if (overlap > 1) {
            overlapRect.set(1, 1, center * 3 / 4, center * 3 / 4);
            canvas.drawRoundRect(overlapRect, radius / 6, radius / 6, overlapBack);

            overlapOutline.setStrokeWidth(1);
            overlapRect.set(1, 1, center * 3 / 4, center * 3 / 4);
            canvas.drawRoundRect(overlapRect, radius / 6, radius / 6, overlapOutline);

            overlapTextPaint.setTextSize(radius * 3 / 4);
            Paint.FontMetricsInt fontMetrics = overlapTextPaint.getFontMetricsInt();
            int baseline = (radius * 3 / 4 - 1 - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;

            //canvas.save();
            //canvas.rotate(-90, center*3/8, center*3/8);
            canvas.drawText(String.valueOf(overlap), radius * 3 / 8, baseline + 1, overlapTextPaint);
            //canvas.restore();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //height = View.MeasureSpec.getSize(heightMeasureSpec);
        //width = View.MeasureSpec.getSize(widthMeasureSpec);
        //setMeasuredDimension((int)(BoardView.radius*2)+2,(int) (BoardView.radius*2)+2);
    }

    public void setStep(int step) {
        Step = step;
        if (Step == -1) {
            switch (mColor) {
                case Constants.RED:
                    posX = PlayView.redHangarX[planeNumber];
                    posY = PlayView.redHangarY[planeNumber];
                    //setAngle(90);
                    break;
                case Constants.YELLOW:
                    posX = PlayView.yellowHangarX[planeNumber];
                    posY = PlayView.yellowHangarY[planeNumber];
                    //setAngle(180);
                    break;
                case Constants.BLUE:
                    posX = PlayView.blueHangarX[planeNumber];
                    posY = PlayView.blueHangarY[planeNumber];
                    //setAngle(270);
                    break;
                case Constants.GREEN:
                    posX = PlayView.greenHangarX[planeNumber];
                    posY = PlayView.greenHangarY[planeNumber];
                    //setAngle(0);
                    break;
            }
        } else if (Step == 0) {
            switch (mColor) {
                case Constants.RED:
                    posX = PlayView.redStripX[0];
                    posY = PlayView.redStripY[0];
                    break;
                case Constants.YELLOW:
                    posX = PlayView.yellowStripX[0];
                    posY = PlayView.yellowStripY[0];
                    break;
                case Constants.BLUE:
                    posX = PlayView.blueStripX[0];
                    posY = PlayView.blueStripY[0];
                    break;
                case Constants.GREEN:
                    posX = PlayView.greenStripX[0];
                    posY = PlayView.greenStripY[0];
                    break;
            }
        } else if (Step >= 1 && Step <= 50) {
            switch (mColor) {
                case Constants.RED:
                    posX = PlayView.circlePosX[Step - 1];
                    posY = PlayView.circlePosY[Step - 1];
                    break;
                case Constants.YELLOW:
                    posX = PlayView.circlePosX[(Step + 12) % 52];
                    posY = PlayView.circlePosY[(Step + 12) % 52];
                    break;
                case Constants.BLUE:
                    posX = PlayView.circlePosX[(Step + 25) % 52];
                    posY = PlayView.circlePosY[(Step + 25) % 52];
                    break;
                case Constants.GREEN:
                    posX = PlayView.circlePosX[(Step + 38) % 52];
                    posY = PlayView.circlePosY[(Step + 38) % 52];
                    break;
            }
        } else if (Step > 50 && Step <= 56) {
            switch (mColor) {
                case Constants.RED:
                    posX = PlayView.redStripX[Step - 50];
                    posY = PlayView.redStripY[Step - 50];
                    break;
                case Constants.YELLOW:
                    posX = PlayView.yellowStripX[Step - 50];
                    posY = PlayView.yellowStripY[Step - 50];
                    break;
                case Constants.BLUE:
                    posX = PlayView.blueStripX[Step - 50];
                    posY = PlayView.blueStripY[Step - 50];
                    break;
                case Constants.GREEN:
                    posX = PlayView.greenStripX[Step - 50];
                    posY = PlayView.greenStripY[Step - 50];
                    break;
            }
        } else if (Step == 57) {
            switch (mColor) {
                case Constants.RED:
                    posX = PlayView.redHangarX[planeNumber];
                    posY = PlayView.redHangarY[planeNumber];
                    break;
                case Constants.YELLOW:
                    posX = PlayView.yellowHangarX[planeNumber];
                    posY = PlayView.yellowHangarY[planeNumber];
                    break;
                case Constants.BLUE:
                    posX = PlayView.blueHangarX[planeNumber];
                    posY = PlayView.blueHangarY[planeNumber];
                    break;
                case Constants.GREEN:
                    posX = PlayView.greenHangarX[planeNumber];
                    posY = PlayView.greenHangarY[planeNumber];
                    break;
            }
        }
    }

    public void setNextStep(int nextstep) {
        nextStep = nextstep;
        if (nextStep == -1) {
            switch (mColor) {
                case Constants.RED:
                    nextposX = PlayView.redHangarX[planeNumber];
                    nextposY = PlayView.redHangarY[planeNumber];
                    break;
                case Constants.YELLOW:
                    nextposX = PlayView.yellowHangarX[planeNumber];
                    nextposY = PlayView.yellowHangarY[planeNumber];
                    break;
                case Constants.BLUE:
                    nextposX = PlayView.blueHangarX[planeNumber];
                    nextposY = PlayView.blueHangarY[planeNumber];
                    break;
                case Constants.GREEN:
                    nextposX = PlayView.greenHangarX[planeNumber];
                    nextposY = PlayView.greenHangarY[planeNumber];
                    break;
            }
        } else if (nextStep == 0) {
            switch (mColor) {
                case Constants.RED:
                    nextposX = PlayView.redStripX[0];
                    nextposY = PlayView.redStripY[0];
                    break;
                case Constants.YELLOW:
                    nextposX = PlayView.yellowStripX[0];
                    nextposY = PlayView.yellowStripY[0];
                    break;
                case Constants.BLUE:
                    nextposX = PlayView.blueStripX[0];
                    nextposY = PlayView.blueStripY[0];
                    break;
                case Constants.GREEN:
                    nextposX = PlayView.greenStripX[0];
                    nextposY = PlayView.greenStripY[0];
                    break;
            }
        } else if (nextStep >= 1 && nextStep <= 50) {
            switch (mColor) {
                case Constants.RED:
                    nextposX = PlayView.circlePosX[nextStep - 1];
                    nextposY = PlayView.circlePosY[nextStep - 1];
                    break;
                case Constants.YELLOW:
                    nextposX = PlayView.circlePosX[(nextStep + 12) % 52];
                    nextposY = PlayView.circlePosY[(nextStep + 12) % 52];
                    break;
                case Constants.BLUE:
                    nextposX = PlayView.circlePosX[(nextStep + 25) % 52];
                    nextposY = PlayView.circlePosY[(nextStep + 25) % 52];
                    break;
                case Constants.GREEN:
                    nextposX = PlayView.circlePosX[(nextStep + 38) % 52];
                    nextposY = PlayView.circlePosY[(nextStep + 38) % 52];
                    break;
            }
        } else if (nextStep > 50 && nextStep <= 56) {
            switch (mColor) {
                case Constants.RED:
                    nextposX = PlayView.redStripX[nextStep - 50];
                    nextposY = PlayView.redStripY[nextStep - 50];
                    break;
                case Constants.YELLOW:
                    nextposX = PlayView.yellowStripX[nextStep - 50];
                    nextposY = PlayView.yellowStripY[nextStep - 50];
                    break;
                case Constants.BLUE:
                    nextposX = PlayView.blueStripX[nextStep - 50];
                    nextposY = PlayView.blueStripY[nextStep - 50];
                    break;
                case Constants.GREEN:
                    nextposX = PlayView.greenStripX[nextStep - 50];
                    nextposY = PlayView.greenStripY[nextStep - 50];
                    break;
            }
        } else if (nextStep == 57) {
            switch (mColor) {
                case Constants.RED:
                    nextposX = PlayView.redHangarX[planeNumber];
                    nextposY = PlayView.redHangarY[planeNumber];
                    //setAngle(90);
                    break;
                case Constants.YELLOW:
                    nextposX = PlayView.yellowHangarX[planeNumber];
                    nextposY = PlayView.yellowHangarY[planeNumber];
                    //setAngle(180);
                    break;
                case Constants.BLUE:
                    nextposX = PlayView.blueHangarX[planeNumber];
                    nextposY = PlayView.blueHangarY[planeNumber];
                    //setAngle(270);
                    break;
                case Constants.GREEN:
                    nextposX = PlayView.greenHangarX[planeNumber];
                    nextposY = PlayView.greenHangarY[planeNumber];
                    //setAngle(0);
                    break;
            }
        }
        setAngle(90 + (int) (Math.atan2((nextposY - posY), (nextposX - posX)) / Math.PI * 180));
    }

    public int getStep() {
        return Step;
    }

    public float getPosX() {
        return posX;
    }

    public float getPosY() {
        return posY;
    }

    public void setOverlap(int o) {
        if (o < 1) {
            overlap = 1;
        } else {
            overlap = o;
        }
        invalidate();
    }

    public int getOverlap() {
        return overlap;
    }

    public void setAngle(int a) {
        angle = a;
        invalidate();
    }

    public void setDonw() {
        mColor = Constants.DOWN;
        invalidate();
    }

    public boolean isDown(){
        return mColor==Constants.DOWN;
    }
}
