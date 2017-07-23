package com.rookiedev.aeroplanechess.app.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.rookiedev.aeroplanechess.app.MenuActivity;
import com.rookiedev.aeroplanechess.app.PlayActivity;
import com.rookiedev.aeroplanechess.app.R;
import com.rookiedev.aeroplanechess.app.constants.Constants;

/**
 * TODO: document your custom view class.
 */
public class HomeView extends ViewGroup {
    private Context mContext;
    private ItemView newGame, resumeGame, twoPlayers, fourPlayers, redVSblue, yellowVSgreen;
    private float density;
    private int itemHeight, width;
    private Paint titlePaint;

    private ObjectAnimator outTransAnimator1, outTransAnimator2, inTransAnimator1, inTransAnimator2;
    private ObjectAnimator outAlphaAnimator1, outAlphaAnimator2, inAlphaAnimator1, inAlphaAnimator2;
    private int aniDuration = 200;

    public static int page = 0;
    private boolean aniOnGoing = false;

    private int paddingX, paddingY;

    private boolean isResume = false;

    public HomeView(Context context) {
        super(context);
        mContext = context;
        init(null, 0);
    }

    public HomeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(attrs, 0);
    }

    public HomeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        init(attrs, defStyle);
    }

    @Override
    protected void onLayout(boolean x, int l, int t, int r, int b) {
        resumeGame.layout(l + paddingX, t + paddingY * 2 + itemHeight, r - paddingX, t + paddingY * 2 + itemHeight + itemHeight);
        newGame.layout(l + paddingX, t + paddingY * 2 + itemHeight, r - paddingX, t + paddingY * 2 + itemHeight + itemHeight);

        twoPlayers.layout(l + paddingX, t + paddingY * 2 + itemHeight * 2, r - paddingX, t + paddingY * 2 + itemHeight * 3);
        fourPlayers.layout(l + paddingX, t + paddingY * 3 + itemHeight * 3, r - paddingX, t + paddingY * 3 + itemHeight * 4);

        redVSblue.layout(l + paddingX, t + paddingY * 2 + itemHeight * 2, r - paddingX, t + paddingY * 2 + itemHeight * 3);
        yellowVSgreen.layout(l + paddingX, t + paddingY * 3 + itemHeight * 3, r - paddingX, t + paddingY * 3 + itemHeight * 4);
    }

    private void init(AttributeSet attrs, int defStyle) {
        titlePaint = new Paint();
        titlePaint.setColor(mContext.getResources().getColor(R.color.grayDark));
        titlePaint.setStyle(Paint.Style.FILL);
        titlePaint.setStrokeWidth(0);
        titlePaint.setAntiAlias(true);
        titlePaint.setTextAlign(Paint.Align.CENTER);

        resumeGame = new ItemView(mContext);
        resumeGame.setStyle(ItemView.RESUME);
        addView(resumeGame);

        newGame = new ItemView(mContext);
        newGame.setStyle(ItemView.NEW);
        addView(newGame);

        twoPlayers = new ItemView(mContext);
        twoPlayers.setStyle(ItemView.TWOPLAYERS);
        addView(twoPlayers);
        twoPlayers.setAlpha(0);

        fourPlayers = new ItemView(mContext);
        fourPlayers.setStyle(ItemView.FOURPLAYERS);
        addView(fourPlayers);
        fourPlayers.setAlpha(0);

        redVSblue = new ItemView(mContext);
        redVSblue.setStyle(ItemView.REDBLUE);
        addView(redVSblue);
        redVSblue.setAlpha(0);

        yellowVSgreen = new ItemView(mContext);
        yellowVSgreen.setStyle(ItemView.YELLOWGREEN);
        addView(yellowVSgreen);
        yellowVSgreen.setAlpha(0);

        resumeGame.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(mContext, PlayActivity.class);
                intent.putExtra(Constants.ISCACHED_PREF, "true");
                intent.putExtra(Constants.PLAYTYPE_PREF, String.valueOf(Constants.FOURPLAYER));
                mContext.startActivity(intent);
            }
        });

        newGame.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                resumeGame.setClickable(false);
                newGame.setClickable(false);

                outTransAnimator1 = ObjectAnimator.ofFloat(resumeGame, "translationX", resumeGame.getTranslationX(), resumeGame.getTranslationX() + itemHeight * 2);
                outTransAnimator1.setDuration(aniDuration);
                outAlphaAnimator1 = ObjectAnimator.ofFloat(resumeGame, "alpha", 1f, 0f);
                outAlphaAnimator1.setDuration(aniDuration);

                outTransAnimator2 = ObjectAnimator.ofFloat(newGame, "translationX", newGame.getTranslationX(), newGame.getTranslationX() + itemHeight * 2);
                outTransAnimator2.setDuration(aniDuration);
                outAlphaAnimator2 = ObjectAnimator.ofFloat(newGame, "alpha", 1f, 0f);
                outAlphaAnimator2.setDuration(aniDuration);

                inTransAnimator1 = ObjectAnimator.ofFloat(twoPlayers, "translationY", twoPlayers.getTranslationY(), twoPlayers.getTranslationY() - itemHeight);
                inTransAnimator1.setDuration(aniDuration);
                inTransAnimator1.setStartDelay(aniDuration / 2);
                inAlphaAnimator1 = ObjectAnimator.ofFloat(twoPlayers, "alpha", 0f, 1f);
                inAlphaAnimator1.setDuration(aniDuration);
                inAlphaAnimator1.setStartDelay(aniDuration / 2);

                inTransAnimator2 = ObjectAnimator.ofFloat(fourPlayers, "translationY", fourPlayers.getTranslationY(), fourPlayers.getTranslationY() - itemHeight);
                inTransAnimator2.setDuration(aniDuration);
                inTransAnimator2.setStartDelay(aniDuration / 2);
                inAlphaAnimator2 = ObjectAnimator.ofFloat(fourPlayers, "alpha", 0f, 1f);
                inAlphaAnimator2.setDuration(aniDuration);
                inAlphaAnimator2.setStartDelay(aniDuration / 2);

                outTransAnimator1.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {
                        aniOnGoing = true;
                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {

                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });

                inAlphaAnimator2.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        twoPlayers.setClickable(true);
                        fourPlayers.setClickable(true);
                        page = 1;
                        aniOnGoing = false;
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });

                outTransAnimator1.start();
                outAlphaAnimator1.start();
                outTransAnimator2.start();
                outAlphaAnimator2.start();

                inTransAnimator1.start();
                inAlphaAnimator1.start();
                inTransAnimator2.start();
                inAlphaAnimator2.start();
            }
        });

        twoPlayers.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                twoPlayers.setClickable(false);
                fourPlayers.setClickable(false);

                outTransAnimator1 = ObjectAnimator.ofFloat(twoPlayers, "translationX", twoPlayers.getTranslationX(), twoPlayers.getTranslationX() + itemHeight * 2);
                outTransAnimator1.setDuration(aniDuration);
                outAlphaAnimator1 = ObjectAnimator.ofFloat(twoPlayers, "alpha", 1f, 0f);
                outAlphaAnimator1.setDuration(aniDuration);

                outTransAnimator2 = ObjectAnimator.ofFloat(fourPlayers, "translationX", fourPlayers.getTranslationX(), fourPlayers.getTranslationX() + itemHeight * 2);
                outTransAnimator2.setDuration(aniDuration);
                outAlphaAnimator2 = ObjectAnimator.ofFloat(fourPlayers, "alpha", 1f, 0f);
                outAlphaAnimator2.setDuration(aniDuration);

                inTransAnimator1 = ObjectAnimator.ofFloat(redVSblue, "translationY", redVSblue.getTranslationY(), redVSblue.getTranslationY() - itemHeight);
                inTransAnimator1.setDuration(aniDuration);
                inTransAnimator1.setStartDelay(aniDuration / 2);
                inAlphaAnimator1 = ObjectAnimator.ofFloat(redVSblue, "alpha", 0f, 1f);
                inAlphaAnimator1.setDuration(aniDuration);
                inAlphaAnimator1.setStartDelay(aniDuration / 2);

                inTransAnimator2 = ObjectAnimator.ofFloat(yellowVSgreen, "translationY", yellowVSgreen.getTranslationY(), yellowVSgreen.getTranslationY() - itemHeight);
                inTransAnimator2.setDuration(aniDuration);
                inTransAnimator2.setStartDelay(aniDuration / 2);
                inAlphaAnimator2 = ObjectAnimator.ofFloat(yellowVSgreen, "alpha", 0f, 1f);
                inAlphaAnimator2.setDuration(aniDuration);
                inAlphaAnimator2.setStartDelay(aniDuration / 2);

                outAlphaAnimator1.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {
                        aniOnGoing = true;
                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {

                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });

                inAlphaAnimator2.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        redVSblue.setClickable(true);
                        yellowVSgreen.setClickable(true);
                        page = 2;
                        aniOnGoing = false;
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });

                outTransAnimator1.start();
                outAlphaAnimator1.start();
                outTransAnimator2.start();
                outAlphaAnimator2.start();

                inTransAnimator1.start();
                inAlphaAnimator1.start();
                inTransAnimator2.start();
                inAlphaAnimator2.start();
            }
        });

        fourPlayers.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                MenuActivity.isPlayed = true;
                Intent intent = new Intent();
                intent.setClass(mContext, PlayActivity.class);
                intent.putExtra(Constants.ISCACHED_PREF, "false");
                intent.putExtra(Constants.PLAYTYPE_PREF, String.valueOf(Constants.FOURPLAYER));
                mContext.startActivity(intent);
            }
        });

        redVSblue.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                MenuActivity.isPlayed = true;
                Intent intent = new Intent();
                intent.setClass(mContext, PlayActivity.class);
                intent.putExtra(Constants.ISCACHED_PREF, "false");
                intent.putExtra(Constants.PLAYTYPE_PREF, String.valueOf(Constants.REDvsBLUE));
                mContext.startActivity(intent);
            }
        });

        yellowVSgreen.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                MenuActivity.isPlayed = true;
                Intent intent = new Intent();
                intent.setClass(mContext, PlayActivity.class);
                intent.putExtra(Constants.ISCACHED_PREF, "false");
                intent.putExtra(Constants.PLAYTYPE_PREF, String.valueOf(Constants.YELLOWvsGREEN));
                mContext.startActivity(intent);
            }
        });
        twoPlayers.setClickable(false);
        fourPlayers.setClickable(false);
        redVSblue.setClickable(false);
        yellowVSgreen.setClickable(false);
        resumeGame.setClickable(false);
        resumeGame.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        titlePaint.setTextSize(itemHeight * 4 / 5);
        Paint.FontMetricsInt fontMetrics = titlePaint.getFontMetricsInt();
        //targetRect.top + (targetRect.bottom - targetRect.top) / 2 - (FontMetrics.bottom - FontMetrics.top) / 2 - FontMetrics.top
        int baseline = (paddingY * 2 + itemHeight - 0 - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
        canvas.drawText(mContext.getString(R.string.home_title), width / 2, baseline, titlePaint);
        //canvas.drawRect(0,0,100,100,titlePaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        width = getWidth();
        density = dm.density;      // 屏幕密度（像素比例：0.75/1.0/1.5/2.0）
        //int densityDPI = dm.densityDpi;     // 屏幕密度（每寸像素：120/160/240/320）
        paddingY = width / 10;
        paddingX = width / 10;
        //itemHeight = (height - paddingY * 5) / 4;
        itemHeight = (width - paddingX * 2) / 3;

        if (isResume) {
            newGame.setTranslationY(itemHeight + paddingY);
            resumeGame.setVisibility(View.VISIBLE);
            resumeGame.setClickable(true);
        } else {
            newGame.setTranslationY(0);
            resumeGame.setVisibility(View.INVISIBLE);
            resumeGame.setClickable(false);
        }
    }

    public void backToPage(int p) {
        if (p == 1) {
            redVSblue.setClickable(false);
            yellowVSgreen.setClickable(false);

            outTransAnimator1 = ObjectAnimator.ofFloat(twoPlayers, "translationX", twoPlayers.getTranslationX(), twoPlayers.getTranslationX() - itemHeight * 2);
            outTransAnimator1.setDuration(aniDuration);
            outTransAnimator1.setStartDelay(aniDuration / 2);
            outAlphaAnimator1 = ObjectAnimator.ofFloat(twoPlayers, "alpha", 0f, 1f);
            outAlphaAnimator1.setDuration(aniDuration);
            outAlphaAnimator1.setStartDelay(aniDuration / 2);

            outTransAnimator2 = ObjectAnimator.ofFloat(fourPlayers, "translationX", fourPlayers.getTranslationX(), fourPlayers.getTranslationX() - itemHeight * 2);
            outTransAnimator2.setDuration(aniDuration);
            outTransAnimator2.setStartDelay(aniDuration / 2);
            outAlphaAnimator2 = ObjectAnimator.ofFloat(fourPlayers, "alpha", 0f, 1f);
            outAlphaAnimator2.setDuration(aniDuration);
            outAlphaAnimator2.setStartDelay(aniDuration / 2);

            inTransAnimator1 = ObjectAnimator.ofFloat(redVSblue, "translationY", redVSblue.getTranslationY(), redVSblue.getTranslationY() + itemHeight);
            inTransAnimator1.setDuration(aniDuration);
            inAlphaAnimator1 = ObjectAnimator.ofFloat(redVSblue, "alpha", 1f, 0f);
            inAlphaAnimator1.setDuration(aniDuration);

            inTransAnimator2 = ObjectAnimator.ofFloat(yellowVSgreen, "translationY", yellowVSgreen.getTranslationY(), yellowVSgreen.getTranslationY() + itemHeight);
            inTransAnimator2.setDuration(aniDuration);
            inAlphaAnimator2 = ObjectAnimator.ofFloat(yellowVSgreen, "alpha", 1f, 0f);
            inAlphaAnimator2.setDuration(aniDuration);

            inTransAnimator1.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                    aniOnGoing = true;
                }

                @Override
                public void onAnimationEnd(Animator animator) {

                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });

            outTransAnimator2.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    twoPlayers.setClickable(true);
                    fourPlayers.setClickable(true);
                    page = 1;
                    aniOnGoing = false;
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });

            outTransAnimator1.start();
            outAlphaAnimator1.start();
            outTransAnimator2.start();
            outAlphaAnimator2.start();

            inTransAnimator1.start();
            inAlphaAnimator1.start();
            inTransAnimator2.start();
            inAlphaAnimator2.start();
        } else if (p == 0) {
            twoPlayers.setClickable(false);
            fourPlayers.setClickable(false);

            outTransAnimator1 = ObjectAnimator.ofFloat(resumeGame, "translationX", resumeGame.getTranslationX(), resumeGame.getTranslationX() - itemHeight * 2);
            outTransAnimator1.setDuration(aniDuration);
            outTransAnimator1.setStartDelay(aniDuration / 2);
            outAlphaAnimator1 = ObjectAnimator.ofFloat(resumeGame, "alpha", 0f, 1f);
            outAlphaAnimator1.setDuration(aniDuration);
            outAlphaAnimator1.setStartDelay(aniDuration / 2);

            outTransAnimator2 = ObjectAnimator.ofFloat(newGame, "translationX", newGame.getTranslationX(), newGame.getTranslationX() - itemHeight * 2);
            outTransAnimator2.setDuration(aniDuration);
            outTransAnimator2.setStartDelay(aniDuration / 2);
            outAlphaAnimator2 = ObjectAnimator.ofFloat(newGame, "alpha", 0f, 1f);
            outAlphaAnimator2.setDuration(aniDuration);
            outAlphaAnimator2.setStartDelay(aniDuration / 2);

            inTransAnimator1 = ObjectAnimator.ofFloat(twoPlayers, "translationY", twoPlayers.getTranslationY(), twoPlayers.getTranslationY() + itemHeight);
            inTransAnimator1.setDuration(aniDuration);
            inAlphaAnimator1 = ObjectAnimator.ofFloat(twoPlayers, "alpha", 1f, 0f);
            inAlphaAnimator1.setDuration(aniDuration);

            inTransAnimator2 = ObjectAnimator.ofFloat(fourPlayers, "translationY", fourPlayers.getTranslationY(), fourPlayers.getTranslationY() + itemHeight);
            inTransAnimator2.setDuration(aniDuration);
            inAlphaAnimator2 = ObjectAnimator.ofFloat(fourPlayers, "alpha", 1f, 0f);
            inAlphaAnimator2.setDuration(aniDuration);

            inTransAnimator1.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                    aniOnGoing = true;
                }

                @Override
                public void onAnimationEnd(Animator animator) {

                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });

            outTransAnimator2.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    resumeGame.setClickable(true);
                    newGame.setClickable(true);
                    page = 0;
                    aniOnGoing = false;
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });

            outTransAnimator1.start();
            outAlphaAnimator1.start();
            outTransAnimator2.start();
            outAlphaAnimator2.start();

            inTransAnimator1.start();
            inAlphaAnimator1.start();
            inTransAnimator2.start();
            inAlphaAnimator2.start();
        }
    }

    public void resetView() {
        page = 0;
        aniOnGoing = false;
        twoPlayers.setAlpha(0);
        fourPlayers.setAlpha(0);
        redVSblue.setAlpha(0);
        yellowVSgreen.setAlpha(0);

        twoPlayers.setTranslationX(0);
        twoPlayers.setTranslationY(0);
        fourPlayers.setTranslationX(0);
        fourPlayers.setTranslationY(0);
        redVSblue.setTranslationX(0);
        redVSblue.setTranslationY(0);
        yellowVSgreen.setTranslationX(0);
        yellowVSgreen.setTranslationY(0);

        twoPlayers.setClickable(false);
        fourPlayers.setClickable(false);
        redVSblue.setClickable(false);
        yellowVSgreen.setClickable(false);

        resumeGame.setAlpha(1);
        newGame.setAlpha(1);
        resumeGame.setTranslationX(0);
        resumeGame.setTranslationY(0);
        newGame.setTranslationX(0);
        newGame.setTranslationY(0);
        resumeGame.setClickable(true);
        newGame.setClickable(true);
    }

    public boolean isAniOnGoing() {
        return aniOnGoing;
    }

    public int getPage() {
        return page;
    }

    public void setResume(boolean i) {
        isResume = i;
        if (isResume) {
            newGame.setTranslationY(itemHeight + paddingY);
            resumeGame.setVisibility(View.VISIBLE);
            resumeGame.setClickable(true);
        } else {
            newGame.setTranslationY(0);
            resumeGame.setVisibility(View.INVISIBLE);
            resumeGame.setClickable(false);
        }
    }
}
