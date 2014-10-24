package com.rookiedev.aeroplanechess.app.view;

import android.content.Context;
import android.graphics.Canvas;
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
public class BoardView extends View {
    private Context mContext;
    private Paint mPaintRed, mPaintGreen, mPaintYellow, mPaintBlue, mPaintPos;
    private Path path = new Path();
    private int circlePosX[], circlePosY[];
    private int redAirPortX[], redAirPortY[], yellowAirPortX[], yellowAirPortY[], blueAirPortX[], blueAirPortY[], greenAirPortX[], greenAirPortY[];// Airports
    private int redPosX[], redPosY[], yellowPosX[], yellowPosY[], bluePosX[], bluePosY[], greenPosX[], greenPosY[];// 0 start position, 1~6 finial positions
    private int twoWidth, minWidth;

    private int playType;

    RectF redDiceHolderOut, redDiceHolderIn, yellowDiceHolderOut, yellowDiceHolderIn, blueDiceHolderOut, blueDiceHolderIn, greenDiceHolderOut, greenDiceHolderIn;

    public BoardView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public BoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public BoardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        mContext = context;
        // Load attributes
        mPaintRed = new Paint();
        mPaintRed.setColor(mContext.getResources().getColor(R.color.redLightHolo));
        mPaintRed.setStyle(Paint.Style.FILL);
        mPaintRed.setStrokeWidth(0);
        mPaintRed.setAntiAlias(true);
        mPaintYellow = new Paint();
        mPaintYellow.setColor(mContext.getResources().getColor(R.color.orangeLightHolo));
        mPaintYellow.setStyle(Paint.Style.FILL);
        mPaintYellow.setStrokeWidth(0);
        mPaintYellow.setAntiAlias(true);
        mPaintGreen = new Paint();
        mPaintGreen.setColor(mContext.getResources().getColor(R.color.greenLightHolo));
        mPaintGreen.setStyle(Paint.Style.FILL);
        mPaintGreen.setStrokeWidth(0);
        mPaintGreen.setAntiAlias(true);
        mPaintBlue = new Paint();
        mPaintBlue.setColor(mContext.getResources().getColor(R.color.blueLightHolo));
        mPaintBlue.setStyle(Paint.Style.FILL);
        mPaintBlue.setStrokeWidth(0);
        mPaintBlue.setAntiAlias(true);
        mPaintPos = new Paint();
        mPaintPos.setColor(mContext.getResources().getColor(R.color.grayLight));
        mPaintPos.setStyle(Paint.Style.FILL);
        mPaintPos.setStrokeWidth(dip2px(mContext, 4));
        mPaintPos.setAntiAlias(true);

        path = new Path();

        redDiceHolderOut = new RectF();
        redDiceHolderIn = new RectF();
        yellowDiceHolderOut = new RectF();
        yellowDiceHolderIn = new RectF();
        blueDiceHolderOut = new RectF();
        blueDiceHolderIn = new RectF();
        greenDiceHolderOut = new RectF();
        greenDiceHolderIn = new RectF();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int gap = dip2px(mContext, 3);
        int width = getWidth();
        int blank = (getHeight() - getWidth()) / 2;
        //int height = width;
        minWidth = width / 17;
        int offset = (width - minWidth * 17) / 2;
        int fourWidth = 4 * minWidth;
        twoWidth = 2 * minWidth;
        int radius = minWidth * 3 / 8;

        canvas.drawRect(gap + offset, gap + blank, fourWidth - gap + offset, fourWidth - gap + blank, mPaintRed);
        //canvas.drawRect(0, blank, fourWidth, fourWidth + blank, mPaintPos);
        canvas.drawRect(minWidth * 13 + gap + offset, gap + blank, width - gap + offset, fourWidth - gap + blank, mPaintYellow);
        //canvas.drawRect(minWidth * 13, blank, width, fourWidth + blank, mPaintPos);
        canvas.drawRect(gap + offset, minWidth * 13 + gap + blank, fourWidth - gap + offset, width - gap + blank, mPaintGreen);
        //canvas.drawRect(0, minWidth * 13 + blank, fourWidth, height - blank, mPaintPos);
        canvas.drawRect(minWidth * 13 + gap + offset, minWidth * 13 + gap + blank, width - gap + offset, width - gap + blank, mPaintBlue);
        //canvas.drawRect(minWidth * 13, minWidth * 13 + blank, width, height - blank, mPaintPos);

        path.moveTo(offset, minWidth * 6 + blank);
        path.lineTo(minWidth * 2 + offset, minWidth * 4 + blank);
        path.lineTo(minWidth * 2 + offset, minWidth * 6 + blank);
        //path.lineTo(0, minWidth * 6 + blank);
        path.close();
        path.moveTo(fourWidth + offset, fourWidth + blank);
        path.lineTo(fourWidth + twoWidth + offset, fourWidth + blank);
        path.lineTo(fourWidth + twoWidth + offset, fourWidth + twoWidth + blank);
        //path.lineTo(fourWidth, fourWidth + blank);
        path.close();
        path.moveTo(minWidth * 11 + offset, fourWidth + blank);
        path.lineTo(minWidth * 11 + offset, fourWidth + twoWidth + blank);
        path.lineTo(minWidth * 13 + offset, fourWidth + blank);
        //path.lineTo(width - fourWidth - twoWidth, fourWidth + blank);
        path.close();
        path.moveTo(minWidth * 15 + offset, fourWidth + blank);
        path.lineTo(minWidth * 15 + offset, fourWidth + twoWidth + blank);
        path.lineTo(minWidth * 17 + offset, fourWidth + twoWidth + blank);
        //path.lineTo(width - twoWidth, fourWidth + blank);
        path.close();
        path.moveTo(minWidth * 8 + offset, minWidth * 17 + blank);
        path.lineTo(minWidth * 8 + offset, minWidth * 10 + blank);
        path.lineTo(minWidth * 7 + offset, minWidth * 10 + blank);
        path.lineTo(minWidth * 8 + minWidth / 2 + offset, minWidth * 8 + minWidth / 2 + blank);
        path.lineTo(minWidth * 10 + offset, minWidth * 10 + blank);
        path.lineTo(minWidth * 9 + offset, minWidth * 10 + blank);
        path.lineTo(minWidth * 9 + offset, minWidth * 17 + blank);
        //path.lineTo(minWidth*8,minWidth*17+blank);
        path.close();
        path.moveTo(minWidth * 6 + minWidth / 2 + offset, minWidth * 4 + minWidth / 2 - radius / 2 + blank);
        path.lineTo(minWidth * 7 + offset, minWidth * 4 + minWidth / 2 - radius / 2 + blank);
        path.lineTo(minWidth * 7 + offset, minWidth * 4 + minWidth / 2 - radius + blank);
        path.lineTo(minWidth * 7 + minWidth / 2 + offset, minWidth * 4 + minWidth / 2 + blank);
        path.lineTo(minWidth * 7 + offset, minWidth * 4 + minWidth / 2 + radius + blank);
        path.lineTo(minWidth * 7 + offset, minWidth * 4 + minWidth / 2 + radius / 2 + blank);
        path.lineTo(minWidth * 6 + minWidth / 2 + offset, minWidth * 4 + minWidth / 2 + radius / 2 + blank);
        path.close();
        path.moveTo(minWidth * 9 + minWidth / 2 + offset, minWidth * 4 + minWidth / 2 - radius / 2 + blank);
        path.lineTo(minWidth * 10 + offset, minWidth * 4 + minWidth / 2 - radius / 2 + blank);
        path.lineTo(minWidth * 10 + offset, minWidth * 4 + minWidth / 2 - radius + blank);
        path.lineTo(minWidth * 10 + minWidth / 2 + offset, minWidth * 4 + minWidth / 2 + blank);
        path.lineTo(minWidth * 10 + offset, minWidth * 4 + minWidth / 2 + radius + blank);
        path.lineTo(minWidth * 10 + offset, minWidth * 4 + minWidth / 2 + radius / 2 + blank);
        path.lineTo(minWidth * 9 + minWidth / 2 + offset, minWidth * 4 + minWidth / 2 + radius / 2 + blank);
        path.close();
        canvas.drawPath(path, mPaintGreen);

        path.reset();
        path.moveTo(minWidth * 11 + offset, 0 + blank);
        path.lineTo(minWidth * 11 + offset, twoWidth + blank);
        path.lineTo(minWidth * 13 + offset, twoWidth + blank);
        //path.lineTo(minWidth*11,blank);
        path.close();
        path.moveTo(minWidth * 11 + offset, fourWidth + twoWidth + blank);
        path.lineTo(minWidth * 13 + offset, fourWidth + twoWidth + blank);
        path.lineTo(minWidth * 13 + offset, fourWidth + blank);
        //path.lineTo(minWidth*11,fourWidth+twoWidth+blank);
        path.close();
        path.moveTo(minWidth * 11 + offset, minWidth * 11 + blank);
        path.lineTo(minWidth * 13 + offset, minWidth * 11 + blank);
        path.lineTo(minWidth * 13 + offset, minWidth * 13 + blank);
        //path.lineTo(minWidth*11,minWidth*11+blank);
        path.close();
        path.moveTo(minWidth * 11 + offset, minWidth * 15 + blank);
        path.lineTo(minWidth * 13 + offset, minWidth * 15 + blank);
        path.lineTo(minWidth * 11 + offset, minWidth * 17 + blank);
        //path.lineTo(minWidth*11,minWidth*15+blank);
        path.close();
        path.moveTo(offset, minWidth * 8 + blank);
        path.lineTo(minWidth * 7 + offset, minWidth * 8 + blank);
        path.lineTo(minWidth * 7 + offset, minWidth * 7 + blank);
        path.lineTo(minWidth * 8 + minWidth / 2 + offset, minWidth * 8 + minWidth / 2 + blank);
        path.lineTo(minWidth * 7 + offset, minWidth * 10 + blank);
        path.lineTo(minWidth * 7 + offset, minWidth * 9 + blank);
        path.lineTo(offset, minWidth * 9 + blank);
        path.close();
        path.moveTo(minWidth * 12 + minWidth / 2 + offset - radius / 2, minWidth * 6 + minWidth / 2 + blank);
        path.lineTo(minWidth * 12 + minWidth / 2 + offset - radius / 2, minWidth * 7 + blank);
        path.lineTo(minWidth * 12 + minWidth / 2 + offset - radius, minWidth * 7 + blank);
        path.lineTo(minWidth * 12 + minWidth / 2 + offset, minWidth * 7 + minWidth / 2 + blank);
        path.lineTo(minWidth * 12 + minWidth / 2 + offset + radius, minWidth * 7 + blank);
        path.lineTo(minWidth * 12 + minWidth / 2 + offset + radius / 2, minWidth * 7 + blank);
        path.lineTo(minWidth * 12 + minWidth / 2 + offset + radius / 2, minWidth * 6 + minWidth / 2 + blank);
        path.close();
        path.moveTo(minWidth * 12 + minWidth / 2 + offset - radius / 2, minWidth * 9 + minWidth / 2 + blank);
        path.lineTo(minWidth * 12 + minWidth / 2 + offset - radius / 2, minWidth * 10 + blank);
        path.lineTo(minWidth * 12 + minWidth / 2 + offset - radius, minWidth * 10 + blank);
        path.lineTo(minWidth * 12 + minWidth / 2 + offset, minWidth * 10 + minWidth / 2 + blank);
        path.lineTo(minWidth * 12 + minWidth / 2 + offset + radius, minWidth * 10 + blank);
        path.lineTo(minWidth * 12 + minWidth / 2 + offset + radius / 2, minWidth * 10 + blank);
        path.lineTo(minWidth * 12 + minWidth / 2 + offset + radius / 2, minWidth * 9 + minWidth / 2 + blank);
        path.close();
        canvas.drawPath(path, mPaintRed);

        path.reset();
        path.moveTo(minWidth * 15 + offset, minWidth * 11 + blank);
        path.lineTo(minWidth * 17 + offset, minWidth * 11 + blank);
        path.lineTo(minWidth * 15 + offset, minWidth * 13 + blank);
        //path.lineTo(width-twoWidth,minWidth*11+blank);
        path.close();
        path.moveTo(minWidth * 11 + offset, minWidth * 11 + blank);
        path.lineTo(minWidth * 11 + offset, minWidth * 13 + blank);
        path.lineTo(minWidth * 13 + offset, minWidth * 13 + blank);
        //path.lineTo(minWidth*11,minWidth*11+blank);
        path.close();
        path.moveTo(fourWidth + twoWidth + offset, minWidth * 11 + blank);
        path.lineTo(fourWidth + twoWidth + offset, minWidth * 13 + blank);
        path.lineTo(fourWidth + offset, minWidth * 13 + blank);
        //path.lineTo(fourWidth+twoWidth,minWidth*11+blank);
        path.close();
        path.moveTo(offset, minWidth * 11 + blank);
        path.lineTo(twoWidth + offset, minWidth * 11 + blank);
        path.lineTo(twoWidth + offset, minWidth * 13 + blank);
        //path.lineTo(0,minWidth*11+blank);
        path.close();
        path.moveTo(minWidth * 8 + offset, 0 + blank);
        path.lineTo(minWidth * 8 + offset, minWidth * 7 + blank);
        path.lineTo(minWidth * 7 + offset, minWidth * 7 + blank);
        path.lineTo(minWidth * 8 + minWidth / 2 + offset, minWidth * 8 + minWidth / 2 + blank);
        path.lineTo(minWidth * 10 + offset, minWidth * 7 + blank);
        path.lineTo(minWidth * 9 + offset, minWidth * 7 + blank);
        path.lineTo(minWidth * 9 + offset, 0 + blank);
        path.close();
        path.moveTo(minWidth * 7 + minWidth / 2 + offset, minWidth * 12 + minWidth / 2 - radius / 2 + blank);
        path.lineTo(minWidth * 7 + offset, minWidth * 12 + minWidth / 2 - radius / 2 + blank);
        path.lineTo(minWidth * 7 + offset, minWidth * 12 + minWidth / 2 - radius + blank);
        path.lineTo(minWidth * 6 + minWidth / 2 + offset, minWidth * 12 + minWidth / 2 + blank);
        path.lineTo(minWidth * 7 + offset, minWidth * 12 + minWidth / 2 + radius + blank);
        path.lineTo(minWidth * 7 + offset, minWidth * 12 + minWidth / 2 + radius / 2 + blank);
        path.lineTo(minWidth * 7 + minWidth / 2 + offset, minWidth * 12 + minWidth / 2 + radius / 2 + blank);
        path.close();
        path.moveTo(minWidth * 10 + minWidth / 2 + offset, minWidth * 12 + minWidth / 2 - radius / 2 + blank);
        path.lineTo(minWidth * 10 + offset, minWidth * 12 + minWidth / 2 - radius / 2 + blank);
        path.lineTo(minWidth * 10 + offset, minWidth * 12 + minWidth / 2 - radius + blank);
        path.lineTo(minWidth * 9 + minWidth / 2 + offset, minWidth * 12 + minWidth / 2 + blank);
        path.lineTo(minWidth * 10 + offset, minWidth * 12 + minWidth / 2 + radius + blank);
        path.lineTo(minWidth * 10 + offset, minWidth * 12 + minWidth / 2 + radius / 2 + blank);
        path.lineTo(minWidth * 10 + minWidth / 2 + offset, minWidth * 12 + minWidth / 2 + radius / 2 + blank);
        path.close();
        canvas.drawPath(path, mPaintYellow);

        path.reset();
        path.moveTo(fourWidth + offset, twoWidth + blank);
        path.lineTo(fourWidth + twoWidth + offset, twoWidth + blank);
        path.lineTo(fourWidth + twoWidth + offset, 0 + blank);
        //path.lineTo(fourWidth,twoWidth+blank);
        path.close();
        path.moveTo(fourWidth + offset, fourWidth + blank);
        path.lineTo(fourWidth + offset, fourWidth + twoWidth + blank);
        path.lineTo(fourWidth + twoWidth + offset, fourWidth + twoWidth + blank);
        //path.lineTo(fourWidth,fourWidth+blank);
        path.close();
        path.moveTo(fourWidth + offset, minWidth * 11 + blank);
        path.lineTo(fourWidth + twoWidth + offset, minWidth * 11 + blank);
        path.lineTo(fourWidth + offset, minWidth * 13 + blank);
        //path.lineTo(fourWidth,minWidth*11+blank);
        path.close();
        path.moveTo(fourWidth + offset, minWidth * 15 + blank);
        path.lineTo(fourWidth + twoWidth + offset, minWidth * 17 + blank);
        path.lineTo(fourWidth + twoWidth + offset, minWidth * 15 + blank);
        //path.lineTo(fourWidth,minWidth*15+blank);
        path.close();
        path.moveTo(minWidth * 17 + offset, minWidth * 8 + blank);
        path.lineTo(minWidth * 10 + offset, minWidth * 8 + blank);
        path.lineTo(minWidth * 10 + offset, minWidth * 7 + blank);
        path.lineTo(minWidth * 8 + minWidth / 2 + offset, minWidth * 8 + minWidth / 2 + blank);
        path.lineTo(minWidth * 10 + offset, minWidth * 10 + blank);
        path.lineTo(minWidth * 10 + offset, minWidth * 9 + blank);
        path.lineTo(minWidth * 17 + offset, minWidth * 9 + blank);
        path.close();
        path.moveTo(minWidth * 4 + minWidth / 2 + offset - radius / 2, minWidth * 7 + minWidth / 2 + blank);
        path.lineTo(minWidth * 4 + minWidth / 2 + offset - radius / 2, minWidth * 7 + blank);
        path.lineTo(minWidth * 4 + minWidth / 2 + offset - radius, minWidth * 7 + blank);
        path.lineTo(minWidth * 4 + minWidth / 2 + offset, minWidth * 6 + minWidth / 2 + blank);
        path.lineTo(minWidth * 4 + minWidth / 2 + offset + radius, minWidth * 7 + blank);
        path.lineTo(minWidth * 4 + minWidth / 2 + offset + radius / 2, minWidth * 7 + blank);
        path.lineTo(minWidth * 4 + minWidth / 2 + offset + radius / 2, minWidth * 7 + minWidth / 2 + blank);
        path.close();
        path.moveTo(minWidth * 4 + minWidth / 2 + offset - radius / 2, minWidth * 10 + minWidth / 2 + blank);
        path.lineTo(minWidth * 4 + minWidth / 2 + offset - radius / 2, minWidth * 10 + blank);
        path.lineTo(minWidth * 4 + minWidth / 2 + offset - radius, minWidth * 10 + blank);
        path.lineTo(minWidth * 4 + minWidth / 2 + offset, minWidth * 9 + minWidth / 2 + blank);
        path.lineTo(minWidth * 4 + minWidth / 2 + offset + radius, minWidth * 10 + blank);
        path.lineTo(minWidth * 4 + minWidth / 2 + offset + radius / 2, minWidth * 10 + blank);
        path.lineTo(minWidth * 4 + minWidth / 2 + offset + radius / 2, minWidth * 10 + minWidth / 2 + blank);
        path.close();
        canvas.drawPath(path, mPaintBlue);
        path.reset();

        rectV(canvas, twoWidth + offset, fourWidth + blank, mPaintRed);
        rectV(canvas, twoWidth + minWidth + offset, fourWidth + blank, mPaintYellow);

        rectH(canvas, fourWidth + offset, minWidth * 3 + blank, mPaintRed);
        rectH(canvas, fourWidth + offset, twoWidth + blank, mPaintYellow);

        rectV(canvas, fourWidth + twoWidth + offset, 0 + blank, mPaintGreen);
        rectV(canvas, fourWidth + twoWidth + minWidth + offset, 0 + blank, mPaintRed);
        rectV(canvas, fourWidth + twoWidth + minWidth * 3 + offset, 0 + blank, mPaintBlue);
        rectV(canvas, fourWidth + twoWidth + minWidth * 4 + offset, 0 + blank, mPaintGreen);

        rectH(canvas, minWidth * 11 + offset, twoWidth + blank, mPaintYellow);
        rectH(canvas, minWidth * 11 + offset, twoWidth + minWidth + blank, mPaintBlue);

        rectV(canvas, minWidth * 13 + offset, fourWidth + blank, mPaintYellow);
        rectV(canvas, minWidth * 14 + offset, fourWidth + blank, mPaintBlue);

        rectH(canvas, minWidth * 15 + offset, fourWidth + twoWidth + blank, mPaintRed);
        rectH(canvas, minWidth * 15 + offset, fourWidth + twoWidth + minWidth + blank, mPaintYellow);
        rectH(canvas, minWidth * 15 + offset, fourWidth + twoWidth + 3 * minWidth + blank, mPaintGreen);
        rectH(canvas, minWidth * 15 + offset, fourWidth + twoWidth + 4 * minWidth + blank, mPaintRed);

        rectV(canvas, minWidth * 14 + offset, minWidth * 11 + blank, mPaintBlue);
        rectV(canvas, minWidth * 13 + offset, minWidth * 11 + blank, mPaintGreen);

        rectH(canvas, minWidth * 11 + offset, minWidth * 13 + blank, mPaintBlue);
        rectH(canvas, minWidth * 11 + offset, minWidth * 14 + blank, mPaintGreen);

        rectV(canvas, minWidth * 10 + offset, minWidth * 15 + blank, mPaintYellow);
        rectV(canvas, minWidth * 9 + offset, minWidth * 15 + blank, mPaintBlue);
        rectV(canvas, minWidth * 7 + offset, minWidth * 15 + blank, mPaintRed);
        rectV(canvas, minWidth * 6 + offset, minWidth * 15 + blank, mPaintYellow);

        rectH(canvas, fourWidth + offset, minWidth * 14 + blank, mPaintGreen);
        rectH(canvas, fourWidth + offset, minWidth * 13 + blank, mPaintRed);

        rectV(canvas, minWidth * 3 + offset, minWidth * 11 + blank, mPaintGreen);
        rectV(canvas, twoWidth + offset, minWidth * 11 + blank, mPaintRed);

        rectH(canvas, offset, minWidth * 10 + blank, mPaintBlue);
        rectH(canvas, offset, minWidth * 9 + blank, mPaintGreen);
        rectH(canvas, offset, minWidth * 7 + blank, mPaintYellow);
        rectH(canvas, offset, minWidth * 6 + blank, mPaintBlue);

        for (int i = 0; i <= 51; i++) {
            canvas.drawCircle(circlePosX[i], circlePosY[i] + blank, radius, mPaintPos);
        }

        for (int i = 0; i <= 3; i++) {
            canvas.drawCircle(redAirPortX[i], redAirPortY[i] + blank, radius, mPaintPos);
            canvas.drawCircle(yellowAirPortX[i], yellowAirPortY[i] + blank, radius, mPaintPos);
            canvas.drawCircle(blueAirPortX[i], blueAirPortY[i] + blank, radius, mPaintPos);
            canvas.drawCircle(greenAirPortX[i], greenAirPortY[i] + blank, radius, mPaintPos);
        }

        for (int i = 0; i <= 6; i++) {
            canvas.drawCircle(redPosX[i], redPosY[i] + blank, radius, mPaintPos);
            canvas.drawCircle(yellowPosX[i], yellowPosY[i] + blank, radius, mPaintPos);
            canvas.drawCircle(greenPosX[i], greenPosY[i] + blank, radius, mPaintPos);
            canvas.drawCircle(bluePosX[i], bluePosY[i] + blank, radius, mPaintPos);
        }

        if (playType != Constants.YELLOWvsGREEN) {
            redDiceHolderOut.set(blank / 20, blank / 20, blank - blank / 20, blank - blank / 20);
            mPaintRed.setColor(mContext.getResources().getColor(R.color.redDarkHolo));
            canvas.drawRoundRect(redDiceHolderOut, blank / 20, blank / 20, mPaintRed);
            redDiceHolderIn.set(blank / 10, blank / 10, blank - blank / 10, blank - blank / 10);
            mPaintRed.setColor(mContext.getResources().getColor(R.color.redLightHolo));
            canvas.drawRoundRect(redDiceHolderIn, blank / 30, blank / 30, mPaintRed);
        }

        if (playType != Constants.REDvsBLUE) {
            yellowDiceHolderOut.set(blank / 20 + width - blank, blank / 20, blank - blank / 20 + width - blank, blank - blank / 20);
            mPaintYellow.setColor(mContext.getResources().getColor(R.color.orangeDarkHolo));
            canvas.drawRoundRect(yellowDiceHolderOut, blank / 20, blank / 20, mPaintYellow);
            yellowDiceHolderIn.set(blank / 10 + width - blank, blank / 10, blank - blank / 10 + width - blank, blank - blank / 10);
            mPaintYellow.setColor(mContext.getResources().getColor(R.color.orangeLightHolo));
            canvas.drawRoundRect(yellowDiceHolderIn, blank / 30, blank / 30, mPaintYellow);
        }

        if (playType != Constants.YELLOWvsGREEN) {
            blueDiceHolderOut.set(blank / 20 + width - blank, blank / 20 + width + blank, blank - blank / 20 + width - blank, blank - blank / 20 + width + blank);
            mPaintBlue.setColor(mContext.getResources().getColor(R.color.blueDarkHolo));
            canvas.drawRoundRect(blueDiceHolderOut, blank / 20, blank / 20, mPaintBlue);
            blueDiceHolderIn.set(blank / 10 + width - blank, blank / 10 + width + blank, blank - blank / 10 + width - blank, blank - blank / 10 + width + blank);
            mPaintBlue.setColor(mContext.getResources().getColor(R.color.blueLightHolo));
            canvas.drawRoundRect(blueDiceHolderIn, blank / 30, blank / 30, mPaintBlue);
        }

        if (playType != Constants.REDvsBLUE) {
            greenDiceHolderOut.set(blank / 20, blank / 20 + width + blank, blank - blank / 20, blank - blank / 20 + width + blank);
            mPaintGreen.setColor(mContext.getResources().getColor(R.color.greenDarkHolo));
            canvas.drawRoundRect(greenDiceHolderOut, blank / 20, blank / 20, mPaintGreen);
            greenDiceHolderIn.set(blank / 10, blank / 10 + width + blank, blank - blank / 10, blank - blank / 10 + width + blank);
            mPaintGreen.setColor(mContext.getResources().getColor(R.color.greenLightHolo));
            canvas.drawRoundRect(greenDiceHolderIn, blank / 30, blank / 30, mPaintGreen);
        }
    }

    public void setParameter(int circleposx[], int circleposy[], int redairportx[], int redairporty[], int yellowairportx[],
                             int yellowairporty[], int blueairportx[], int blueairporty[], int greenairportx[], int greenairporty[],
                             int redposx[], int redposy[], int yellowposx[], int yellowposy[], int blueposx[], int blueposy[],
                             int greenposx[], int greenposy[], int type) {
        circlePosX = circleposx;
        circlePosY = circleposy;
        redAirPortX = redairportx;
        redAirPortY = redairporty;
        yellowAirPortX = yellowairportx;
        yellowAirPortY = yellowairporty;
        blueAirPortX = blueairportx;
        blueAirPortY = blueairporty;
        greenAirPortX = greenairportx;
        greenAirPortY = greenairporty;
        redPosX = redposx;
        redPosY = redposy;
        yellowPosX = yellowposx;
        yellowPosY = yellowposy;
        bluePosX = blueposx;
        bluePosY = blueposy;
        greenPosX = greenposx;
        greenPosY = greenposy;
        playType = type;
    }

    private void rectH(Canvas canvas, float x, float y, Paint mPaint) {
        canvas.drawRect(x, y, x + twoWidth, y + minWidth, mPaint);
    }

    private void rectV(Canvas canvas, float x, float y, Paint mPaint) {
        canvas.drawRect(x, y, x + minWidth, y + twoWidth, mPaint);
    }

    private int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
