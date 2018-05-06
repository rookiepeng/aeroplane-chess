package com.rookiedev.aeroplanechess.app.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import com.rookiedev.aeroplanechess.app.R;

import java.util.Random;

/**
 * TODO: document your custom view class.
 */
public class DiceView extends View {
    private Context mContext;
    private int length, center;
    private Paint mPaintBack1, mPaintBack2;
    private Paint mPaintBlack, mPaintRed;
    private Paint mPaintHolderDark, mPaintHolderLight;
    private RectF outerRect;

    private Handler mHandler, animationHandler;
    private final int rollAnimations = 10;
    private Random random;

    private int mColor;
    //private int diceNumber = 0;

    private int Number = 0;

    public DiceView(Handler handler, Context context) {
        super(context);
        //mColor = color;
        init(handler, context, null, 0);
    }

    public DiceView(Handler handler, Context context, AttributeSet attrs) {
        super(context, attrs);
        //mColor = color;
        init(handler, context, attrs, 0);
    }

    public DiceView(Handler handler, Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        //mColor = color;
        init(handler, context, attrs, defStyle);
    }

    private void init(Handler handler, Context context, AttributeSet attrs, int defStyle) {
        // Load attributes
        mContext = context;
        mHandler = handler;
        random = new Random();

        mPaintHolderDark = new Paint();
        mPaintHolderDark.setColor(context.getResources().getColor(R.color.redDarkHolo));
        mPaintHolderDark.setStyle(Paint.Style.FILL);
        mPaintHolderDark.setStrokeWidth(0);
        mPaintHolderDark.setAntiAlias(true);

        mPaintHolderLight = new Paint();
        mPaintHolderLight.setColor(context.getResources().getColor(R.color.redLightHolo));
        mPaintHolderLight.setStyle(Paint.Style.FILL);
        mPaintHolderLight.setStrokeWidth(0);
        mPaintHolderLight.setAntiAlias(true);

        mPaintBack1 = new Paint();
        mPaintBack1.setColor(context.getResources().getColor(R.color.grayDark));
        mPaintBack1.setStyle(Paint.Style.FILL);
        mPaintBack1.setStrokeWidth(0);
        mPaintBack1.setAntiAlias(true);

        mPaintBack2 = new Paint();
        mPaintBack2.setColor(context.getResources().getColor(R.color.grayLight));
        mPaintBack2.setStyle(Paint.Style.FILL);
        mPaintBack2.setStrokeWidth(0);
        mPaintBack2.setAntiAlias(true);

        mPaintRed = new Paint();
        mPaintRed.setColor(Color.RED);
        mPaintRed.setStyle(Paint.Style.FILL);
        mPaintRed.setStrokeWidth(0);
        mPaintRed.setAntiAlias(true);

        mPaintBlack = new Paint();
        mPaintBlack.setColor(Color.BLACK);
        mPaintBlack.setStyle(Paint.Style.FILL);
        mPaintBlack.setStrokeWidth(0);
        mPaintBlack.setAntiAlias(true);
        //mPaintBlack.setTextSize(dip2px(mContext, 40));

        outerRect = new RectF();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        length = canvas.getWidth();
        center = length / 2;

        mPaintBlack.setTextSize(length / 5);
        /*switch (mColor) {
            case PlayView.RED:
                mPaintHolderDark.setColor(mContext.getResources().getColor(R.color.redDarkHolo));
                mPaintHolderLight.setColor(mContext.getResources().getColor(R.color.redLightHolo));
                break;
            case PlayView.YELLOW:
                mPaintHolderDark.setColor(mContext.getResources().getColor(R.color.orangeDarkHolo));
                mPaintHolderLight.setColor(mContext.getResources().getColor(R.color.orangeLightHolo));
                break;
            case PlayView.BLUE:
                mPaintHolderDark.setColor(mContext.getResources().getColor(R.color.blueDarkHolo));
                mPaintHolderLight.setColor(mContext.getResources().getColor(R.color.blueLightHolo));
                break;
            case PlayView.GREEN:
                mPaintHolderDark.setColor(mContext.getResources().getColor(R.color.greenDarkHolo));
                mPaintHolderLight.setColor(mContext.getResources().getColor(R.color.greenLightHolo));
                break;
        }

        canvas.drawRect(length / 20, length / 20, length - length / 20, length - length / 20, mPaintHolderLight);
        canvas.drawRect(length / 10, length / 10, length - length / 10, length - length / 10, mPaintHolderDark);*/

        outerRect.set(length * 3 / 20, length * 3 / 20, length - length * 3 / 20, length - length * 3 / 20);
        canvas.drawRoundRect(outerRect, length / 10, length / 10, mPaintBack1);
        canvas.drawRoundRect(outerRect, length * 4 / 20, length * 4 / 20, mPaintBack2);

        switch (Number) {
            case 0:
                drawInit(canvas);
                break;
            case 1:
                drawOne(canvas);
                break;
            case 2:
                drawTwo(canvas);
                break;
            case 3:
                drawThree(canvas);
                break;
            case 4:
                drawFour(canvas);
                break;
            case 5:
                drawFive(canvas);
                break;
            case 6:
                drawSix(canvas);
                break;
        }
    }

    public void updateNumber(int number) {
        Number = number;
        invalidate();
    }

    public void setColor(int color) {
        mColor = color;
        invalidate();
    }

    private void drawInit(Canvas canvas) {
        //canvas.save();
        //canvas.rotate(180, length / 2, length / 2);
        Paint.FontMetricsInt fontMetrics = mPaintBlack.getFontMetricsInt();
        int baseline = (length - 0 - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
        mPaintBlack.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("Roll", length / 2, baseline, mPaintBlack);
        //canvas.restore();
    }

    private void drawOne(Canvas canvas) {
        canvas.drawCircle(center, center, length / 10, mPaintRed);
    }

    private void drawTwo(Canvas canvas) {
        canvas.drawCircle(center - length * 3 / 20, center, length * 3 / 40, mPaintBlack);
        canvas.drawCircle(center + length * 3 / 20, center, length * 3 / 40, mPaintBlack);
    }

    private void drawThree(Canvas canvas) {
        canvas.drawCircle(center - length * 3 / 20, center - length * 3 / 20, length * 3 / 40, mPaintBlack);
        canvas.drawCircle(center, center, length * 3 / 40, mPaintBlack);
        canvas.drawCircle(center + length * 3 / 20, center + length * 3 / 20, length * 3 / 40, mPaintBlack);
    }

    private void drawFour(Canvas canvas) {
        canvas.drawCircle(center - length * 3 / 20, center - length * 3 / 20, length * 3 / 40, mPaintRed);
        canvas.drawCircle(center + length * 3 / 20, center - length * 3 / 20, length * 3 / 40, mPaintRed);
        canvas.drawCircle(center - length * 3 / 20, center + length * 3 / 20, length * 3 / 40, mPaintRed);
        canvas.drawCircle(center + length * 3 / 20, center + length * 3 / 20, length * 3 / 40, mPaintRed);
    }

    private void drawFive(Canvas canvas) {
        canvas.drawCircle(center - length * 3 / 20, center - length * 3 / 20, length * 3 / 40, mPaintBlack);
        canvas.drawCircle(center - length * 3 / 20, center + length * 3 / 20, length * 3 / 40, mPaintBlack);
        canvas.drawCircle(center + length * 3 / 20, center - length * 3 / 20, length * 3 / 40, mPaintBlack);
        canvas.drawCircle(center + length * 3 / 20, center + length * 3 / 20, length * 3 / 40, mPaintBlack);
        canvas.drawCircle(center, center, length * 3 / 40, mPaintBlack);
    }

    private void drawSix(Canvas canvas) {
        canvas.drawCircle(center - length * 3 / 20, center - length * 3 / 20 - length / 40, length * 3 / 40, mPaintBlack);
        canvas.drawCircle(center - length * 3 / 20, center + length * 3 / 20 + length / 40, length * 3 / 40, mPaintBlack);
        canvas.drawCircle(center + length * 3 / 20, center - length * 3 / 20 - length / 40, length * 3 / 40, mPaintBlack);
        canvas.drawCircle(center + length * 3 / 20, center + length * 3 / 20 + length / 40, length * 3 / 40, mPaintBlack);
        canvas.drawCircle(center - length * 3 / 20, center, length * 3 / 40, mPaintBlack);
        canvas.drawCircle(center + length * 3 / 20, center, length * 3 / 40, mPaintBlack);
    }

    public int getNumber() {
        return Number;
    }

    public void rollDice() {
        //if (paused) return;
        animationHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                invalidate();
                //dice1.updateNumber(diceNumber);
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < rollAnimations; i++) {
                    doRoll();
                }
                mHandler.sendEmptyMessage(0);
            }
        }).start();
    }

    private void doRoll() { // only does a single roll
        Number = random.nextInt(6) + 1;
        //Log.v("TAG",String.valueOf(Number));
        //Message msg = mHandler.obtainMessage();
        // msg.what = 1;
        //msg.sendToTarget();

        synchronized (this) {
            animationHandler.sendEmptyMessage(0);
        }
        try { // delay to alloy for smooth animation
            int delayTime = 15;
            Thread.sleep(delayTime);
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }
    }

    public int getColor() {
        return mColor;
    }

    public void setNumber(int n) {
        Number = n;
        invalidate();
    }
}
