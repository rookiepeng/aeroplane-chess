package com.rookiedev.aeroplanechess.app.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.rookiedev.aeroplanechess.app.constants.Constants;

/**
 * plane    focusPlane  Color
 * 0        1           red
 * 1        2           red
 * 2        3           red
 * 3        4           red
 * 4        5           yellow
 * 5        6           yellow
 * 6        7           yellow
 * 7        8           yellow
 * 8        9           blue
 * 9        10          blue
 * 10       11          blue
 * 11       12          blue
 * 12       13          green
 * 13       14          green
 * 14       15          green
 * 15       16          green
 */
public class PlayView extends ViewGroup {
    /**
     * the size of the board
     */
    public static int circlePosX[], circlePosY[];
    public static int redHangarX[], redHangarY[], yellowHangarX[], yellowHangarY[], blueHangarX[], blueHangarY[], greenHangarX[], greenHangarY[];// hangars
    public static int redStripX[], redStripY[], yellowStripX[], yellowStripY[], blueStripX[], blueStripY[], greenStripX[], greenStripY[];// 0 start position, 1~6 flight strip
    public static int radius;// the radius of planes
    private int width, height, blank;
    private int minWidth;
    /**
     * Context
     */
    private Context mContext;
    /**
     * views
     */
    private DiceView dice;
    private BoardView boardView;
    private RankView rankView;
    private Plane[] plane;
    /**
     * resume from a previous game?
     */
    private boolean isResume = false;

    private int diceNumber;
    private int stepForward;// plane step forward
    private int stepAnimatorTimes;// how many steps for a plane to fly
    /**
     * Flags
     */
    private boolean stepFlag;
    private boolean fly18KickFlag;
    /**
     * leaderboard
     */
    private boolean isRedDown = false, isYellowDown = false, isBlueDown = false, isGreenDown = false;
    private int rank1 = -1, rank2 = -1, rank3 = -1, rank4 = -1;
    /**
     * Animators
     */
    private ObjectAnimator transXAnimator, transYAnimator, diceAnimator;
    private Animator.AnimatorListener listener;
    private int aniDuration = 350, diceDuration = 300;

    private int playType, turn;
    private int focusPlane = 18;

    private int previousStep, currentStep;

    private boolean isPremium = false;

    private int countSix = 0, firstSix = 0, secondSix = 0;

    public PlayView(Context context, int playtype, int playturn, boolean resume) {
        super(context);
        mContext = context;
        playType = playtype;
        turn = playturn;
        isResume = resume;
        if (isResume) {
            readPref();
        }
        init(context, null, 0);
    }

    public PlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(context, attrs, 0);
    }

    public PlayView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        init(context, attrs, defStyle);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int planeRadius = minWidth / 2;
        boardView.layout(l, t, r, b);
        plane[0].layout(redHangarX[0] - planeRadius, redHangarY[0] - planeRadius + blank, redHangarX[0] + planeRadius, redHangarY[0] + planeRadius + blank);
        plane[1].layout(redHangarX[1] - planeRadius, redHangarY[1] - planeRadius + blank, redHangarX[1] + planeRadius, redHangarY[1] + planeRadius + blank);
        plane[2].layout(redHangarX[2] - planeRadius, redHangarY[2] - planeRadius + blank, redHangarX[2] + planeRadius, redHangarY[2] + planeRadius + blank);
        plane[3].layout(redHangarX[3] - planeRadius, redHangarY[3] - planeRadius + blank, redHangarX[3] + planeRadius, redHangarY[3] + planeRadius + blank);
        plane[4].layout(yellowHangarX[0] - planeRadius, yellowHangarY[0] - planeRadius + blank, yellowHangarX[0] + planeRadius, yellowHangarY[0] + planeRadius + blank);
        plane[5].layout(yellowHangarX[1] - planeRadius, yellowHangarY[1] - planeRadius + blank, yellowHangarX[1] + planeRadius, yellowHangarY[1] + planeRadius + blank);
        plane[6].layout(yellowHangarX[2] - planeRadius, yellowHangarY[2] - planeRadius + blank, yellowHangarX[2] + planeRadius, yellowHangarY[2] + planeRadius + blank);
        plane[7].layout(yellowHangarX[3] - planeRadius, yellowHangarY[3] - planeRadius + blank, yellowHangarX[3] + planeRadius, yellowHangarY[3] + planeRadius + blank);
        plane[8].layout(blueHangarX[0] - planeRadius, blueHangarY[0] - planeRadius + blank, blueHangarX[0] + planeRadius, blueHangarY[0] + planeRadius + blank);
        plane[9].layout(blueHangarX[1] - planeRadius, blueHangarY[1] - planeRadius + blank, blueHangarX[1] + planeRadius, blueHangarY[1] + planeRadius + blank);
        plane[10].layout(blueHangarX[2] - planeRadius, blueHangarY[2] - planeRadius + blank, blueHangarX[2] + planeRadius, blueHangarY[2] + planeRadius + blank);
        plane[11].layout(blueHangarX[3] - planeRadius, blueHangarY[3] - planeRadius + blank, blueHangarX[3] + planeRadius, blueHangarY[3] + planeRadius + blank);
        plane[12].layout(greenHangarX[0] - planeRadius, greenHangarY[0] - planeRadius + blank, greenHangarX[0] + planeRadius, greenHangarY[0] + planeRadius + blank);
        plane[13].layout(greenHangarX[1] - planeRadius, greenHangarY[1] - planeRadius + blank, greenHangarX[1] + planeRadius, greenHangarY[1] + planeRadius + blank);
        plane[14].layout(greenHangarX[2] - planeRadius, greenHangarY[2] - planeRadius + blank, greenHangarX[2] + planeRadius, greenHangarY[2] + planeRadius + blank);
        plane[15].layout(greenHangarX[3] - planeRadius, greenHangarY[3] - planeRadius + blank, greenHangarX[3] + planeRadius, greenHangarY[3] + planeRadius + blank);
        rankView.layout(l, -b, r, t);
        dice.layout(0, 0, blank, blank);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        setChildrenDrawingOrderEnabled(true);// enable the Children Drawing Order
        /** game board */
        boardView = new BoardView(mContext);
        boardView.setClickable(false);
        addView(boardView);

        plane = new Plane[16];
        /** red planes */
        plane[0] = new Plane(0, Constants.RED, mContext);
        plane[1] = new Plane(1, Constants.RED, mContext);
        plane[2] = new Plane(2, Constants.RED, mContext);
        plane[3] = new Plane(3, Constants.RED, mContext);
        /** yellow planes */
        plane[4] = new Plane(0, Constants.YELLOW, mContext);
        plane[5] = new Plane(1, Constants.YELLOW, mContext);
        plane[6] = new Plane(2, Constants.YELLOW, mContext);
        plane[7] = new Plane(3, Constants.YELLOW, mContext);
        /** blue planes */
        plane[8] = new Plane(0, Constants.BLUE, mContext);
        plane[9] = new Plane(1, Constants.BLUE, mContext);
        plane[10] = new Plane(2, Constants.BLUE, mContext);
        plane[11] = new Plane(3, Constants.BLUE, mContext);
        /** green planes */
        plane[12] = new Plane(0, Constants.GREEN, mContext);
        plane[13] = new Plane(1, Constants.GREEN, mContext);
        plane[14] = new Plane(2, Constants.GREEN, mContext);
        plane[15] = new Plane(3, Constants.GREEN, mContext);

        plane[0].setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                focusPlane = 1;// move the plane to the top of the view layer
                invalidate();
                if (diceNumber == 6 && countSix == 1) {
                    firstSix = focusPlane - 1;
                } else if (diceNumber == 6 && countSix == 2) {
                    secondSix = focusPlane - 1;
                }
                plane[0].setClickable(false);
                plane[1].setClickable(false);
                plane[2].setClickable(false);
                plane[3].setClickable(false);
                clickPlane();
            }
        });

        plane[1].setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                focusPlane = 2;
                invalidate();
                if (diceNumber == 6 && countSix == 1) {
                    firstSix = focusPlane - 1;
                } else if (diceNumber == 6 && countSix == 2) {
                    secondSix = focusPlane - 1;
                }
                plane[0].setClickable(false);
                plane[1].setClickable(false);
                plane[2].setClickable(false);
                plane[3].setClickable(false);
                clickPlane();
            }
        });

        plane[2].setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                focusPlane = 3;
                invalidate();
                if (diceNumber == 6 && countSix == 1) {
                    firstSix = focusPlane - 1;
                } else if (diceNumber == 6 && countSix == 2) {
                    secondSix = focusPlane - 1;
                }
                plane[0].setClickable(false);
                plane[1].setClickable(false);
                plane[2].setClickable(false);
                plane[3].setClickable(false);
                clickPlane();
            }
        });

        plane[3].setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                focusPlane = 4;
                invalidate();
                if (diceNumber == 6 && countSix == 1) {
                    firstSix = focusPlane - 1;
                } else if (diceNumber == 6 && countSix == 2) {
                    secondSix = focusPlane - 1;
                }
                plane[0].setClickable(false);
                plane[1].setClickable(false);
                plane[2].setClickable(false);
                plane[3].setClickable(false);
                clickPlane();
            }
        });

        plane[4].setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                focusPlane = 5;
                invalidate();
                if (diceNumber == 6 && countSix == 1) {
                    firstSix = focusPlane - 1;
                } else if (diceNumber == 6 && countSix == 2) {
                    secondSix = focusPlane - 1;
                }
                plane[4].setClickable(false);
                plane[5].setClickable(false);
                plane[6].setClickable(false);
                plane[7].setClickable(false);
                clickPlane();
            }
        });

        plane[5].setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                focusPlane = 6;
                invalidate();
                if (diceNumber == 6 && countSix == 1) {
                    firstSix = focusPlane - 1;
                } else if (diceNumber == 6 && countSix == 2) {
                    secondSix = focusPlane - 1;
                }
                plane[4].setClickable(false);
                plane[5].setClickable(false);
                plane[6].setClickable(false);
                plane[7].setClickable(false);
                clickPlane();
            }
        });

        plane[6].setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                focusPlane = 7;
                invalidate();
                if (diceNumber == 6 && countSix == 1) {
                    firstSix = focusPlane - 1;
                } else if (diceNumber == 6 && countSix == 2) {
                    secondSix = focusPlane - 1;
                }
                plane[4].setClickable(false);
                plane[5].setClickable(false);
                plane[6].setClickable(false);
                plane[7].setClickable(false);
                clickPlane();
            }
        });

        plane[7].setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                focusPlane = 8;
                invalidate();
                if (diceNumber == 6 && countSix == 1) {
                    firstSix = focusPlane - 1;
                } else if (diceNumber == 6 && countSix == 2) {
                    secondSix = focusPlane - 1;
                }
                plane[4].setClickable(false);
                plane[5].setClickable(false);
                plane[6].setClickable(false);
                plane[7].setClickable(false);
                clickPlane();
            }
        });

        plane[8].setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                focusPlane = 9;
                invalidate();
                if (diceNumber == 6 && countSix == 1) {
                    firstSix = focusPlane - 1;
                } else if (diceNumber == 6 && countSix == 2) {
                    secondSix = focusPlane - 1;
                }
                plane[8].setClickable(false);
                plane[9].setClickable(false);
                plane[10].setClickable(false);
                plane[11].setClickable(false);
                clickPlane();
            }
        });

        plane[9].setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                focusPlane = 10;
                invalidate();
                if (diceNumber == 6 && countSix == 1) {
                    firstSix = focusPlane - 1;
                } else if (diceNumber == 6 && countSix == 2) {
                    secondSix = focusPlane - 1;
                }
                plane[8].setClickable(false);
                plane[9].setClickable(false);
                plane[10].setClickable(false);
                plane[11].setClickable(false);
                clickPlane();
            }
        });

        plane[10].setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                focusPlane = 11;
                invalidate();
                if (diceNumber == 6 && countSix == 1) {
                    firstSix = focusPlane - 1;
                } else if (diceNumber == 6 && countSix == 2) {
                    secondSix = focusPlane - 1;
                }
                plane[8].setClickable(false);
                plane[9].setClickable(false);
                plane[10].setClickable(false);
                plane[11].setClickable(false);
                clickPlane();
            }
        });

        plane[11].setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                focusPlane = 12;
                invalidate();
                if (diceNumber == 6 && countSix == 1) {
                    firstSix = focusPlane - 1;
                } else if (diceNumber == 6 && countSix == 2) {
                    secondSix = focusPlane - 1;
                }
                plane[8].setClickable(false);
                plane[9].setClickable(false);
                plane[10].setClickable(false);
                plane[11].setClickable(false);
                clickPlane();
            }
        });

        plane[12].setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                focusPlane = 13;
                invalidate();
                if (diceNumber == 6 && countSix == 1) {
                    firstSix = focusPlane - 1;
                } else if (diceNumber == 6 && countSix == 2) {
                    secondSix = focusPlane - 1;
                }
                plane[12].setClickable(false);
                plane[13].setClickable(false);
                plane[14].setClickable(false);
                plane[15].setClickable(false);
                clickPlane();
            }
        });

        plane[13].setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                focusPlane = 14;
                invalidate();
                if (diceNumber == 6 && countSix == 1) {
                    firstSix = focusPlane - 1;
                } else if (diceNumber == 6 && countSix == 2) {
                    secondSix = focusPlane - 1;
                }
                plane[12].setClickable(false);
                plane[13].setClickable(false);
                plane[14].setClickable(false);
                plane[15].setClickable(false);
                clickPlane();
            }
        });

        plane[14].setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                focusPlane = 15;
                invalidate();
                if (diceNumber == 6 && countSix == 1) {
                    firstSix = focusPlane - 1;
                } else if (diceNumber == 6 && countSix == 2) {
                    secondSix = focusPlane - 1;
                }
                plane[12].setClickable(false);
                plane[13].setClickable(false);
                plane[14].setClickable(false);
                plane[15].setClickable(false);
                clickPlane();
            }
        });

        plane[15].setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                focusPlane = 16;
                invalidate();
                if (diceNumber == 6 && countSix == 1) {
                    firstSix = focusPlane - 1;
                } else if (diceNumber == 6 && countSix == 2) {
                    secondSix = focusPlane - 1;
                }
                plane[12].setClickable(false);
                plane[13].setClickable(false);
                plane[14].setClickable(false);
                plane[15].setClickable(false);
                clickPlane();
            }
        });

        for (int i = 0; i < 16; i++) {
            addView(plane[i]);
            plane[i].setClickable(false);
        }

        switch (playType) {
            case Constants.REDvsBLUE:
                plane[4].setVisibility(View.INVISIBLE);
                plane[5].setVisibility(View.INVISIBLE);
                plane[6].setVisibility(View.INVISIBLE);
                plane[7].setVisibility(View.INVISIBLE);
                plane[12].setVisibility(View.INVISIBLE);
                plane[13].setVisibility(View.INVISIBLE);
                plane[14].setVisibility(View.INVISIBLE);
                plane[15].setVisibility(View.INVISIBLE);
                break;
            case Constants.YELLOWvsGREEN:
                plane[0].setVisibility(View.INVISIBLE);
                plane[1].setVisibility(View.INVISIBLE);
                plane[2].setVisibility(View.INVISIBLE);
                plane[3].setVisibility(View.INVISIBLE);
                plane[8].setVisibility(View.INVISIBLE);
                plane[9].setVisibility(View.INVISIBLE);
                plane[10].setVisibility(View.INVISIBLE);
                plane[11].setVisibility(View.INVISIBLE);
                break;
            case Constants.FOURPLAYER:
                break;
        }

        dice = new DiceView(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                diceNumber = dice.getNumber();
                if (diceNumber == 6) {
                    countSix = countSix + 1;
                } else {
                    countSix = 0;
                }
                if (countSix == 3) {
                    switch (turn) {
                        case Constants.RED:
                            plane[0].setClickable(false);
                            plane[1].setClickable(false);
                            plane[2].setClickable(false);
                            plane[3].setClickable(false);
                            if (playType != Constants.FOURPLAYER) {
                                turn = (turn + 2) % 4;
                            } else if (playType == Constants.FOURPLAYER && !isYellowDown) {
                                turn = (turn + 1) % 4;
                            } else if (playType == Constants.FOURPLAYER && isYellowDown && !isBlueDown) {
                                turn = (turn + 2) % 4;
                            } else if (playType == Constants.FOURPLAYER && isYellowDown && isBlueDown) {
                                turn = (turn + 3) % 4;
                            }
                            threeSix();
                            break;
                        case Constants.YELLOW:
                            plane[4].setClickable(false);
                            plane[5].setClickable(false);
                            plane[6].setClickable(false);
                            plane[7].setClickable(false);
                            if (playType != Constants.FOURPLAYER) {
                                turn = (turn + 2) % 4;
                            } else if (playType == Constants.FOURPLAYER && !isBlueDown) {
                                turn = (turn + 1) % 4;
                            } else if (playType == Constants.FOURPLAYER && isBlueDown && !isGreenDown) {
                                turn = (turn + 2) % 4;
                            } else if (playType == Constants.FOURPLAYER && isBlueDown && isGreenDown) {
                                turn = (turn + 3) % 4;
                            }
                            threeSix();
                            break;
                        case Constants.BLUE:
                            plane[8].setClickable(false);
                            plane[9].setClickable(false);
                            plane[10].setClickable(false);
                            plane[11].setClickable(false);
                            if (playType != Constants.FOURPLAYER) {
                                turn = (turn + 2) % 4;
                            } else if (playType == Constants.FOURPLAYER && !isGreenDown) {
                                turn = (turn + 1) % 4;
                            } else if (playType == Constants.FOURPLAYER && isGreenDown && !isRedDown) {
                                turn = (turn + 2) % 4;
                            } else if (playType == Constants.FOURPLAYER && isGreenDown && isRedDown) {
                                turn = (turn + 3) % 4;
                            }
                            threeSix();
                            break;
                        case Constants.GREEN:
                            plane[12].setClickable(false);
                            plane[13].setClickable(false);
                            plane[14].setClickable(false);
                            plane[15].setClickable(false);
                            if (playType != Constants.FOURPLAYER) {
                                turn = (turn + 2) % 4;
                            } else if (playType == Constants.FOURPLAYER && !isRedDown) {
                                turn = (turn + 1) % 4;
                            } else if (playType == Constants.FOURPLAYER && isRedDown && !isYellowDown) {
                                turn = (turn + 2) % 4;
                            } else if (playType == Constants.FOURPLAYER && isRedDown && isYellowDown) {
                                turn = (turn + 3) % 4;
                            }
                            threeSix();
                            break;
                    }
                } else {
                    switch (turn) {
                        case Constants.RED:
                            plane[0].setClickable((plane[0].getStep() != -1 || diceNumber % 2 == 0) && plane[0].getStep() != 57);
                            plane[1].setClickable((plane[1].getStep() != -1 || diceNumber % 2 == 0) && plane[1].getStep() != 57);
                            plane[2].setClickable((plane[2].getStep() != -1 || diceNumber % 2 == 0) && plane[2].getStep() != 57);
                            plane[3].setClickable((plane[3].getStep() != -1 || diceNumber % 2 == 0) && plane[3].getStep() != 57);
                            if (diceNumber != 6) {
                                if (playType != Constants.FOURPLAYER) {
                                    turn = (turn + 2) % 4;
                                } else if (playType == Constants.FOURPLAYER && !isYellowDown) {
                                    turn = (turn + 1) % 4;
                                } else if (playType == Constants.FOURPLAYER && isYellowDown && !isBlueDown) {
                                    turn = (turn + 2) % 4;
                                } else if (playType == Constants.FOURPLAYER && isYellowDown && isBlueDown) {
                                    turn = (turn + 3) % 4;
                                }
                            }
                            if (!plane[0].isClickable() && !plane[1].isClickable() && !plane[2].isClickable() && !plane[3].isClickable()) {
                                changeDice();
                            }
                            break;
                        case Constants.YELLOW:
                            plane[4].setClickable((plane[4].getStep() != -1 || diceNumber % 2 == 0) && plane[4].getStep() != 57);
                            plane[5].setClickable((plane[5].getStep() != -1 || diceNumber % 2 == 0) && plane[5].getStep() != 57);
                            plane[6].setClickable((plane[6].getStep() != -1 || diceNumber % 2 == 0) && plane[6].getStep() != 57);
                            plane[7].setClickable((plane[7].getStep() != -1 || diceNumber % 2 == 0) && plane[7].getStep() != 57);
                            if (diceNumber != 6) {
                                if (playType != Constants.FOURPLAYER) {
                                    turn = (turn + 2) % 4;
                                } else if (playType == Constants.FOURPLAYER && !isBlueDown) {
                                    turn = (turn + 1) % 4;
                                } else if (playType == Constants.FOURPLAYER && isBlueDown && !isGreenDown) {
                                    turn = (turn + 2) % 4;
                                } else if (playType == Constants.FOURPLAYER && isBlueDown && isGreenDown) {
                                    turn = (turn + 3) % 4;
                                }
                            }
                            if (!plane[4].isClickable() && !plane[5].isClickable() && !plane[6].isClickable() && !plane[7].isClickable()) {
                                changeDice();
                            }
                            break;
                        case Constants.BLUE:
                            plane[8].setClickable((plane[8].getStep() != -1 || diceNumber % 2 == 0) && plane[8].getStep() != 57);
                            plane[9].setClickable((plane[9].getStep() != -1 || diceNumber % 2 == 0) && plane[9].getStep() != 57);
                            plane[10].setClickable((plane[10].getStep() != -1 || diceNumber % 2 == 0) && plane[10].getStep() != 57);
                            plane[11].setClickable((plane[11].getStep() != -1 || diceNumber % 2 == 0) && plane[11].getStep() != 57);
                            if (diceNumber != 6) {
                                if (playType != Constants.FOURPLAYER) {
                                    turn = (turn + 2) % 4;
                                } else if (playType == Constants.FOURPLAYER && !isGreenDown) {
                                    turn = (turn + 1) % 4;
                                } else if (playType == Constants.FOURPLAYER && isGreenDown && !isRedDown) {
                                    turn = (turn + 2) % 4;
                                } else if (playType == Constants.FOURPLAYER && isGreenDown && isRedDown) {
                                    turn = (turn + 3) % 4;
                                }
                            }
                            if (!plane[8].isClickable() && !plane[9].isClickable() && !plane[10].isClickable() && !plane[11].isClickable()) {
                                changeDice();
                            }
                            break;
                        case Constants.GREEN:
                            plane[12].setClickable((plane[12].getStep() != -1 || diceNumber % 2 == 0) && plane[12].getStep() != 57);
                            plane[13].setClickable((plane[13].getStep() != -1 || diceNumber % 2 == 0) && plane[13].getStep() != 57);
                            plane[14].setClickable((plane[14].getStep() != -1 || diceNumber % 2 == 0) && plane[14].getStep() != 57);
                            plane[15].setClickable((plane[15].getStep() != -1 || diceNumber % 2 == 0) && plane[15].getStep() != 57);
                            if (diceNumber != 6) {
                                if (playType != Constants.FOURPLAYER) {
                                    turn = (turn + 2) % 4;
                                } else if (playType == Constants.FOURPLAYER && !isRedDown) {
                                    turn = (turn + 1) % 4;
                                } else if (playType == Constants.FOURPLAYER && isRedDown && !isYellowDown) {
                                    turn = (turn + 2) % 4;
                                } else if (playType == Constants.FOURPLAYER && isRedDown && isYellowDown) {
                                    turn = (turn + 3) % 4;
                                }
                            }
                            if (!plane[12].isClickable() && !plane[13].isClickable() && !plane[14].isClickable() && !plane[15].isClickable()) {
                                changeDice();
                            }
                            break;
                    }
                }
            }
        }, mContext);
        addView(dice);
        dice.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dice.setClickable(false);
                dice.rollDice();
            }
        });

        /** the leaderboard view */
        rankView = new RankView(mContext);
        addView(rankView);
        rankView.setVisibility(INVISIBLE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        blank = (height - width) / 2;
        minWidth = width / 17;
        radius = minWidth * 3 / 8;
        int fourWidth = 4 * minWidth;
        int twoWidth = 2 * minWidth;
        int offset = (width - minWidth * 17) / 2;
        circlePosX = new int[52];
        circlePosY = new int[52];
        circlePosX[0] = minWidth + minWidth / 2;
        circlePosX[1] = minWidth * 2 + minWidth / 2;
        circlePosX[2] = minWidth * 3 + minWidth / 2;
        circlePosX[3] = minWidth * 4 + minWidth / 2;
        circlePosX[4] = minWidth * 5 + minWidth / 2;
        circlePosX[5] = minWidth * 5;
        circlePosX[6] = minWidth * 5;
        circlePosX[7] = minWidth * 5 + minWidth / 2;
        circlePosX[8] = minWidth * 6 + minWidth / 2;
        circlePosX[9] = minWidth * 7 + minWidth / 2;
        circlePosX[10] = minWidth * 8 + minWidth / 2;
        circlePosX[11] = minWidth * 9 + minWidth / 2;
        circlePosX[12] = minWidth * 10 + minWidth / 2;
        circlePosX[13] = minWidth * 11 + minWidth / 2;
        circlePosX[14] = minWidth * 12;
        circlePosX[15] = minWidth * 12;
        circlePosX[16] = minWidth * 11 + minWidth / 2;
        circlePosX[17] = minWidth * 12 + minWidth / 2;
        circlePosX[18] = minWidth * 13 + minWidth / 2;
        circlePosX[19] = minWidth * 14 + minWidth / 2;
        circlePosX[20] = minWidth * 15 + minWidth / 2;
        circlePosX[21] = minWidth * 16;
        circlePosX[22] = minWidth * 16;
        circlePosX[23] = minWidth * 16;
        circlePosX[24] = minWidth * 16;
        circlePosX[25] = minWidth * 16;
        circlePosX[26] = minWidth * 15 + minWidth / 2;
        circlePosX[27] = minWidth * 14 + minWidth / 2;
        circlePosX[28] = minWidth * 13 + minWidth / 2;
        circlePosX[29] = minWidth * 12 + minWidth / 2;
        circlePosX[30] = minWidth * 11 + minWidth / 2;
        circlePosX[31] = minWidth * 12;
        circlePosX[32] = minWidth * 12;
        circlePosX[33] = minWidth * 11 + minWidth / 2;
        circlePosX[34] = minWidth * 10 + minWidth / 2;
        circlePosX[35] = minWidth * 9 + minWidth / 2;
        circlePosX[36] = minWidth * 8 + minWidth / 2;
        circlePosX[37] = minWidth * 7 + minWidth / 2;
        circlePosX[38] = minWidth * 6 + minWidth / 2;
        circlePosX[39] = minWidth * 5 + minWidth / 2;
        circlePosX[40] = minWidth * 5;
        circlePosX[41] = minWidth * 5;
        circlePosX[42] = minWidth * 5 + minWidth / 2;
        circlePosX[43] = minWidth * 4 + minWidth / 2;
        circlePosX[44] = minWidth * 3 + minWidth / 2;
        circlePosX[45] = minWidth * 2 + minWidth / 2;
        circlePosX[46] = minWidth + minWidth / 2;
        circlePosX[47] = minWidth;
        circlePosX[48] = minWidth;
        circlePosX[49] = minWidth;
        circlePosX[50] = minWidth;
        circlePosX[51] = minWidth;
        /** add the offset to make the board locate in the middle of the view */
        for (int i = 0; i <= 51; i++) {
            circlePosX[i] = circlePosX[i] + offset;
        }
        circlePosY[0] = fourWidth + minWidth / 2;
        circlePosY[0] = fourWidth + minWidth + minWidth / 2;
        circlePosY[1] = fourWidth + minWidth;
        circlePosY[2] = fourWidth + minWidth;
        circlePosY[3] = fourWidth + minWidth + minWidth / 2;
        circlePosY[4] = fourWidth + minWidth / 2;
        circlePosY[5] = minWidth * 3 + minWidth / 2;
        circlePosY[6] = minWidth * 2 + minWidth / 2;
        circlePosY[7] = minWidth + minWidth / 2;
        circlePosY[8] = minWidth;
        circlePosY[9] = minWidth;
        circlePosY[10] = minWidth;
        circlePosY[11] = minWidth;
        circlePosY[12] = minWidth;
        circlePosY[13] = minWidth + minWidth / 2;
        circlePosY[14] = minWidth * 2 + minWidth / 2;
        circlePosY[15] = minWidth * 3 + minWidth / 2;
        circlePosY[16] = fourWidth + minWidth / 2;
        circlePosY[17] = fourWidth + minWidth + minWidth / 2;
        circlePosY[18] = fourWidth + minWidth;
        circlePosY[19] = fourWidth + minWidth;
        circlePosY[20] = fourWidth + minWidth + minWidth / 2;
        circlePosY[21] = fourWidth + minWidth * 2 + minWidth / 2;
        circlePosY[22] = fourWidth + minWidth * 3 + minWidth / 2;
        circlePosY[23] = fourWidth + minWidth * 4 + minWidth / 2;
        circlePosY[24] = fourWidth + minWidth * 5 + minWidth / 2;
        circlePosY[25] = fourWidth + minWidth * 6 + minWidth / 2;
        circlePosY[26] = fourWidth + minWidth * 7 + minWidth / 2;
        circlePosY[27] = fourWidth + minWidth * 8;
        circlePosY[28] = fourWidth + minWidth * 8;
        circlePosY[29] = fourWidth + minWidth * 7 + minWidth / 2;
        circlePosY[30] = fourWidth + minWidth * 8 + minWidth / 2;
        circlePosY[31] = fourWidth + minWidth * 9 + minWidth / 2;
        circlePosY[32] = fourWidth + minWidth * 10 + minWidth / 2;
        circlePosY[33] = fourWidth + minWidth * 11 + minWidth / 2;
        circlePosY[34] = fourWidth + minWidth * 12;
        circlePosY[35] = fourWidth + minWidth * 12;
        circlePosY[36] = fourWidth + minWidth * 12;
        circlePosY[37] = fourWidth + minWidth * 12;
        circlePosY[38] = fourWidth + minWidth * 12;
        circlePosY[39] = fourWidth + minWidth * 11 + minWidth / 2;
        circlePosY[40] = fourWidth + minWidth * 10 + minWidth / 2;
        circlePosY[41] = fourWidth + minWidth * 9 + minWidth / 2;
        circlePosY[42] = fourWidth + minWidth * 8 + minWidth / 2;
        circlePosY[43] = fourWidth + minWidth * 7 + minWidth / 2;
        circlePosY[44] = fourWidth + minWidth * 8;
        circlePosY[45] = fourWidth + minWidth * 8;
        circlePosY[46] = fourWidth + minWidth * 7 + minWidth / 2;
        circlePosY[47] = fourWidth + minWidth * 6 + minWidth / 2;
        circlePosY[48] = fourWidth + minWidth * 5 + minWidth / 2;
        circlePosY[49] = fourWidth + minWidth * 4 + minWidth / 2;
        circlePosY[50] = fourWidth + minWidth * 3 + minWidth / 2;
        circlePosY[51] = fourWidth + minWidth * 2 + minWidth / 2;

        redHangarX = new int[4];
        redHangarY = new int[4];
        redHangarX[0] = minWidth + offset;
        redHangarX[1] = minWidth + twoWidth + offset;
        redHangarX[2] = minWidth + offset;
        redHangarX[3] = minWidth + twoWidth + offset;
        redHangarY[0] = minWidth;
        redHangarY[1] = minWidth;
        redHangarY[2] = minWidth + twoWidth;
        redHangarY[3] = minWidth + twoWidth;
        yellowHangarX = new int[4];
        yellowHangarY = new int[4];
        yellowHangarY = redHangarY;
        for (int i = 0; i <= 3; i++) {
            yellowHangarX[i] = redHangarX[i] + minWidth * 13 + offset;
        }
        blueHangarX = new int[4];
        blueHangarY = new int[4];
        blueHangarX = yellowHangarX;
        for (int i = 0; i <= 3; i++) {
            blueHangarY[i] = yellowHangarY[i] + minWidth * 13;
        }
        greenHangarX = new int[4];
        greenHangarY = new int[4];
        greenHangarX = redHangarX;
        for (int i = 0; i <= 3; i++) {
            greenHangarY[i] = redHangarY[i] + minWidth * 13;
        }
        redStripX = new int[7];
        redStripY = new int[7];
        yellowStripX = new int[7];
        yellowStripY = new int[7];
        blueStripX = new int[7];
        blueStripY = new int[7];
        greenStripX = new int[7];
        greenStripY = new int[7];
        redStripX[0] = minWidth / 2 + offset;
        redStripY[0] = fourWidth + minWidth / 2;
        yellowStripX[0] = minWidth * 12 + minWidth / 2 + offset;
        yellowStripY[0] = minWidth / 2;
        blueStripX[0] = minWidth * 16 + minWidth / 2 + offset;
        blueStripY[0] = minWidth * 12 + minWidth / 2;
        greenStripX[0] = minWidth * 4 + minWidth / 2 + offset;
        greenStripY[0] = minWidth * 16 + minWidth / 2;
        for (int i = 1; i <= 6; i++) {
            redStripX[i] = minWidth * (i + 1) + minWidth / 2 + offset;
            redStripY[i] = minWidth * 8 + minWidth / 2;
            yellowStripX[i] = minWidth * 8 + minWidth / 2 + offset;
            yellowStripY[i] = minWidth * (i + 1) + minWidth / 2;
            blueStripX[7 - i] = minWidth * (8 + i) + minWidth / 2 + offset;
            blueStripY[i] = minWidth * 8 + minWidth / 2;
            greenStripX[i] = minWidth * 8 + minWidth / 2 + offset;
            greenStripY[7 - i] = minWidth * (8 + i) + minWidth / 2;
        }

        /** set the parameters of the board */
        boardView.setParameter(circlePosX, circlePosY, redHangarX, redHangarY, yellowHangarX,
                yellowHangarY, blueHangarX, blueHangarY, greenHangarX, greenHangarY,
                redStripX, redStripY, yellowStripX, yellowStripY, blueStripX, blueStripY,
                greenStripX, greenStripY, playType);

        if (isResume) {
            setPlaneDiceFromCache();
        } else {
            /** set the direction of the planes */
            for (int i = 0; i < 16; i++) {
                plane[i].setStep(-1);
                plane[i].setAngle((i - (i % 4)) / 4 * 90 + 90);
            }
            /** set the position of dice when start */
            switch (turn) {
                case Constants.RED:
                    dice.setTranslationX(0);
                    dice.setTranslationY(0);
                    break;
                case Constants.YELLOW:
                    dice.setTranslationX(width - blank);
                    dice.setTranslationY(0);
                    break;
                case Constants.BLUE:
                    dice.setTranslationX(width - blank);
                    dice.setTranslationY(width + blank);
                    break;
                case Constants.GREEN:
                    dice.setTranslationX(0);
                    dice.setTranslationY(width + blank);
                    break;
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    /**
     * change the position and clickablity according to *turn*
     */
    private void changeDice() {
        switch (turn) {
            case Constants.RED:
                if (dice.getTranslationX() != 0 || dice.getTranslationY() != 0) {
                    diceAnimator = ObjectAnimator.ofFloat(dice, "alpha", 1f, 0f);
                    diceAnimator.setDuration(diceDuration);
                    diceAnimator.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            dice.setTranslationX(0);
                            dice.setTranslationY(0);
                            dice.updateNumber(0);
                            diceAnimator = ObjectAnimator.ofFloat(dice, "alpha", 0f, 1f);
                            diceAnimator.setDuration(diceDuration);
                            diceAnimator.start();
                            dice.setClickable(true);
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });
                    diceAnimator.start();
                } else {
                    dice.setClickable(true);
                }
                break;
            case Constants.YELLOW:
                if (dice.getTranslationX() != (width - blank) || dice.getTranslationY() != 0) {
                    diceAnimator = ObjectAnimator.ofFloat(dice, "alpha", 1f, 0f);
                    diceAnimator.setDuration(diceDuration);
                    diceAnimator.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            dice.setTranslationX(width - blank);
                            dice.setTranslationY(0);
                            dice.updateNumber(0);
                            diceAnimator = ObjectAnimator.ofFloat(dice, "alpha", 0f, 1f);
                            diceAnimator.setDuration(diceDuration);
                            diceAnimator.start();
                            dice.setClickable(true);
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });
                    diceAnimator.start();
                } else {
                    dice.setClickable(true);
                }
                break;
            case Constants.BLUE:
                if (dice.getTranslationX() != (width - blank) || dice.getTranslationY() != (width + blank)) {
                    diceAnimator = ObjectAnimator.ofFloat(dice, "alpha", 1f, 0f);
                    diceAnimator.setDuration(diceDuration);
                    diceAnimator.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            dice.setTranslationX(width - blank);
                            dice.setTranslationY(width + blank);
                            dice.updateNumber(0);
                            diceAnimator = ObjectAnimator.ofFloat(dice, "alpha", 0f, 1f);
                            diceAnimator.setDuration(diceDuration);
                            diceAnimator.start();
                            dice.setClickable(true);
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });
                    diceAnimator.start();
                } else {
                    dice.setClickable(true);
                }
                break;
            case Constants.GREEN:
                if (dice.getTranslationX() != 0 || dice.getTranslationY() != (width + blank)) {
                    diceAnimator = ObjectAnimator.ofFloat(dice, "alpha", 1f, 0f);
                    diceAnimator.setDuration(diceDuration);
                    diceAnimator.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            dice.setTranslationX(0);
                            dice.setTranslationY(width + blank);
                            dice.updateNumber(0);
                            diceAnimator = ObjectAnimator.ofFloat(dice, "alpha", 0f, 1f);
                            diceAnimator.setDuration(diceDuration);
                            diceAnimator.start();
                            dice.setClickable(true);
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });
                    diceAnimator.start();
                } else {
                    dice.setClickable(true);
                }
                break;
        }
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        if (i == focusPlane) {
            return childCount - 1;
        } else if (i == (childCount - 1)) {
            return focusPlane;
        } else {
            return i;
        }
    }

    private boolean kickBack() {
        boolean kickBackFlag = false;
        if ((focusPlane - 1) <= 3) {
            for (int i = 4; i <= 7; i++) {
                if (plane[focusPlane - 1].getStep() == ((plane[i].getStep() + 13) % 52) && plane[i].getStep() > 0 && plane[i].getStep() <= 50 && plane[focusPlane - 1].getStep() > 0 && plane[focusPlane - 1].getStep() <= 50) {
                    setTransAnimator(i, -1);
                    plane[i].setOverlap(1);
                    AnimatorSet kickBackAnimatorSet = new AnimatorSet();
                    kickBackAnimatorSet.playTogether(transXAnimator, transYAnimator);
                    kickBackAnimatorSet.setDuration(aniDuration);
                    kickBackAnimatorSet.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            if (!dice.isClickable()) {
                                changeDice();
                            }
                            airPortDir();
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });
                    kickBackAnimatorSet.start();
                    kickBackFlag = true;
                }
            }
            for (int i = 8; i <= 11; i++) {
                if (plane[focusPlane - 1].getStep() == ((plane[i].getStep() + 26) % 52) && plane[i].getStep() > 0 && plane[i].getStep() <= 50 && plane[focusPlane - 1].getStep() > 0 && plane[focusPlane - 1].getStep() <= 50) {
                    setTransAnimator(i, -1);
                    plane[i].setOverlap(1);
                    AnimatorSet kickBackAnimatorSet = new AnimatorSet();
                    kickBackAnimatorSet.playTogether(transXAnimator, transYAnimator);
                    kickBackAnimatorSet.setDuration(aniDuration);
                    kickBackAnimatorSet.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            if (!dice.isClickable()) {
                                changeDice();
                            }
                            airPortDir();
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });
                    kickBackAnimatorSet.start();
                    kickBackFlag = true;
                }
            }
            for (int i = 12; i <= 15; i++) {
                if (plane[focusPlane - 1].getStep() == ((plane[i].getStep() + 39) % 52) && plane[i].getStep() > 0 && plane[i].getStep() <= 50 && plane[focusPlane - 1].getStep() > 0 && plane[focusPlane - 1].getStep() <= 50) {
                    setTransAnimator(i, -1);
                    plane[i].setOverlap(1);
                    AnimatorSet kickBackAnimatorSet = new AnimatorSet();
                    kickBackAnimatorSet.playTogether(transXAnimator, transYAnimator);
                    kickBackAnimatorSet.setDuration(aniDuration);
                    kickBackAnimatorSet.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            if (!dice.isClickable()) {
                                changeDice();
                            }
                            airPortDir();
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });
                    kickBackAnimatorSet.start();
                    kickBackFlag = true;
                }
            }
        } else if ((focusPlane - 1) >= 4 && (focusPlane - 1) <= 7) {
            for (int i = 0; i <= 3; i++) {
                if (((plane[focusPlane - 1].getStep() + 13) % 52) == plane[i].getStep() && plane[i].getStep() > 0 && plane[i].getStep() <= 50 && plane[focusPlane - 1].getStep() > 0 && plane[focusPlane - 1].getStep() <= 50) {
                    setTransAnimator(i, -1);
                    plane[i].setOverlap(1);
                    AnimatorSet kickBackAnimatorSet = new AnimatorSet();
                    kickBackAnimatorSet.playTogether(transXAnimator, transYAnimator);
                    kickBackAnimatorSet.setDuration(aniDuration);
                    kickBackAnimatorSet.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            if (!dice.isClickable()) {
                                changeDice();
                            }
                            airPortDir();
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });
                    kickBackAnimatorSet.start();
                    kickBackFlag = true;
                }
            }
            for (int i = 8; i <= 11; i++) {
                if (plane[focusPlane - 1].getStep() == ((plane[i].getStep() + 13) % 52) && plane[i].getStep() > 0 && plane[i].getStep() <= 50 && plane[focusPlane - 1].getStep() > 0 && plane[focusPlane - 1].getStep() <= 50) {
                    setTransAnimator(i, -1);
                    plane[i].setOverlap(1);
                    AnimatorSet kickBackAnimatorSet = new AnimatorSet();
                    kickBackAnimatorSet.playTogether(transXAnimator, transYAnimator);
                    kickBackAnimatorSet.setDuration(aniDuration);
                    kickBackAnimatorSet.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            if (!dice.isClickable()) {
                                changeDice();
                            }
                            airPortDir();
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });
                    kickBackAnimatorSet.start();
                    kickBackFlag = true;
                }
            }
            for (int i = 12; i <= 15; i++) {
                if (plane[focusPlane - 1].getStep() == ((plane[i].getStep() + 26) % 52) && plane[i].getStep() > 0 && plane[i].getStep() <= 50 && plane[focusPlane - 1].getStep() > 0 && plane[focusPlane - 1].getStep() <= 50) {
                    setTransAnimator(i, -1);
                    plane[i].setOverlap(1);
                    AnimatorSet kickBackAnimatorSet = new AnimatorSet();
                    kickBackAnimatorSet.playTogether(transXAnimator, transYAnimator);
                    kickBackAnimatorSet.setDuration(aniDuration);
                    kickBackAnimatorSet.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            if (!dice.isClickable()) {
                                changeDice();
                            }
                            airPortDir();
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });
                    kickBackAnimatorSet.start();
                    kickBackFlag = true;
                }
            }
        } else if ((focusPlane - 1) >= 8 && (focusPlane - 1) <= 11) {
            for (int i = 0; i <= 3; i++) {
                if (((plane[focusPlane - 1].getStep() + 26) % 52) == plane[i].getStep() && plane[i].getStep() > 0 && plane[i].getStep() <= 50 && plane[focusPlane - 1].getStep() > 0 && plane[focusPlane - 1].getStep() <= 50) {
                    setTransAnimator(i, -1);
                    plane[i].setOverlap(1);
                    AnimatorSet kickBackAnimatorSet = new AnimatorSet();
                    kickBackAnimatorSet.playTogether(transXAnimator, transYAnimator);
                    kickBackAnimatorSet.setDuration(aniDuration);
                    kickBackAnimatorSet.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            if (!dice.isClickable()) {
                                changeDice();
                            }
                            airPortDir();
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });
                    kickBackAnimatorSet.start();
                    kickBackFlag = true;
                }
            }
            for (int i = 4; i <= 7; i++) {
                if (((plane[focusPlane - 1].getStep() + 13) % 52) == plane[i].getStep() && plane[i].getStep() > 0 && plane[i].getStep() <= 50 && plane[focusPlane - 1].getStep() > 0 && plane[focusPlane - 1].getStep() <= 50) {
                    setTransAnimator(i, -1);
                    plane[i].setOverlap(1);
                    AnimatorSet kickBackAnimatorSet = new AnimatorSet();
                    kickBackAnimatorSet.playTogether(transXAnimator, transYAnimator);
                    kickBackAnimatorSet.setDuration(aniDuration);
                    kickBackAnimatorSet.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            if (!dice.isClickable()) {
                                changeDice();
                            }
                            airPortDir();
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });
                    kickBackAnimatorSet.start();
                    kickBackFlag = true;
                }
            }
            for (int i = 12; i <= 15; i++) {
                if (plane[focusPlane - 1].getStep() == ((plane[i].getStep() + 13) % 52) && plane[i].getStep() > 0 && plane[i].getStep() <= 50 && plane[focusPlane - 1].getStep() > 0 && plane[focusPlane - 1].getStep() <= 50) {
                    setTransAnimator(i, -1);
                    plane[i].setOverlap(1);
                    AnimatorSet kickBackAnimatorSet = new AnimatorSet();
                    kickBackAnimatorSet.playTogether(transXAnimator, transYAnimator);
                    kickBackAnimatorSet.setDuration(aniDuration);
                    kickBackAnimatorSet.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            if (!dice.isClickable()) {
                                changeDice();
                            }
                            airPortDir();
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });
                    kickBackAnimatorSet.start();
                    kickBackFlag = true;
                }
            }
        } else if ((focusPlane - 1) >= 12 && (focusPlane - 1) <= 15) {
            for (int i = 0; i <= 3; i++) {
                if (((plane[focusPlane - 1].getStep() + 39) % 52) == plane[i].getStep() && plane[i].getStep() > 0 && plane[i].getStep() <= 50 && plane[focusPlane - 1].getStep() > 0 && plane[focusPlane - 1].getStep() <= 50) {
                    setTransAnimator(i, -1);
                    plane[i].setOverlap(1);
                    AnimatorSet kickBackAnimatorSet = new AnimatorSet();
                    kickBackAnimatorSet.playTogether(transXAnimator, transYAnimator);
                    kickBackAnimatorSet.setDuration(aniDuration);
                    kickBackAnimatorSet.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            if (!dice.isClickable()) {
                                changeDice();
                            }
                            airPortDir();
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });
                    kickBackAnimatorSet.start();
                    kickBackFlag = true;
                }
            }
            for (int i = 4; i <= 7; i++) {
                if (((plane[focusPlane - 1].getStep() + 26) % 52) == plane[i].getStep() && plane[i].getStep() > 0 && plane[i].getStep() <= 50 && plane[focusPlane - 1].getStep() > 0 && plane[focusPlane - 1].getStep() <= 50) {
                    setTransAnimator(i, -1);
                    plane[i].setOverlap(1);
                    AnimatorSet kickBackAnimatorSet = new AnimatorSet();
                    kickBackAnimatorSet.playTogether(transXAnimator, transYAnimator);
                    kickBackAnimatorSet.setDuration(aniDuration);
                    kickBackAnimatorSet.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            if (!dice.isClickable()) {
                                changeDice();
                            }
                            airPortDir();
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });
                    kickBackAnimatorSet.start();
                    kickBackFlag = true;
                }
            }
            for (int i = 8; i <= 11; i++) {
                if (((plane[focusPlane - 1].getStep() + 13) % 52) == plane[i].getStep() && plane[i].getStep() > 0 && plane[i].getStep() <= 50 && plane[focusPlane - 1].getStep() > 0 && plane[focusPlane - 1].getStep() <= 50) {
                    setTransAnimator(i, -1);
                    plane[i].setOverlap(1);
                    AnimatorSet kickBackAnimatorSet = new AnimatorSet();
                    kickBackAnimatorSet.playTogether(transXAnimator, transYAnimator);
                    kickBackAnimatorSet.setDuration(aniDuration);
                    kickBackAnimatorSet.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            if (!dice.isClickable()) {
                                changeDice();
                            }
                            airPortDir();
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });
                    kickBackAnimatorSet.start();
                    kickBackFlag = true;
                }
            }
        }
        if (plane[focusPlane - 1].getStep() != -1 && plane[focusPlane - 1].getStep() != 57) {
            plane[focusPlane - 1].setNextStep(plane[focusPlane - 1].getStep() + 1);
        }
        return kickBackFlag;
    }

    private void setTransAnimator(int num, int nextStep) {
        float tempX, tempY;
        tempX = plane[num].getPosX();
        tempY = plane[num].getPosY();
        plane[num].setNextStep(nextStep);
        plane[num].setStep(nextStep);
        transXAnimator = ObjectAnimator.ofFloat(plane[num], "translationX", plane[num].getTranslationX(), plane[num].getTranslationX() + plane[num].getPosX() - tempX);
        //transXAnimator.setDuration(aniDuration);
        transYAnimator = ObjectAnimator.ofFloat(plane[num], "translationY", plane[num].getTranslationY(), plane[num].getTranslationY() + plane[num].getPosY() - tempY);
        //transYAnimator.setDuration(aniDuration);
    }

    private void airPortDir() {
        for (int i = 0; i < 16; i++) {
            if (plane[i].getStep() == -1) {
                plane[i].setAngle((i - (i % 4)) / 4 * 90 + 90);
            }
        }
    }

    private void clickPlane() {
        if (plane[focusPlane - 1].getStep() == -1) {
            if ((focusPlane - 1) <= 3) {
                transXAnimator = ObjectAnimator.ofFloat(plane[focusPlane - 1], "translationX", plane[focusPlane - 1].getTranslationX(), plane[focusPlane - 1].getTranslationX() + redStripX[0] - plane[focusPlane - 1].getPosX());
                transYAnimator = ObjectAnimator.ofFloat(plane[focusPlane - 1], "translationY", plane[focusPlane - 1].getTranslationY(), plane[focusPlane - 1].getTranslationY() + redStripY[0] - plane[focusPlane - 1].getPosY());
            } else if ((focusPlane - 1) > 3 && (focusPlane - 1) <= 7) {
                transXAnimator = ObjectAnimator.ofFloat(plane[focusPlane - 1], "translationX", plane[focusPlane - 1].getTranslationX(), plane[focusPlane - 1].getTranslationX() + yellowStripX[0] - plane[focusPlane - 1].getPosX());
                transYAnimator = ObjectAnimator.ofFloat(plane[focusPlane - 1], "translationY", plane[focusPlane - 1].getTranslationY(), plane[focusPlane - 1].getTranslationY() + yellowStripY[0] - plane[focusPlane - 1].getPosY());
            } else if ((focusPlane - 1) > 7 && (focusPlane - 1) <= 11) {
                transXAnimator = ObjectAnimator.ofFloat(plane[focusPlane - 1], "translationX", plane[focusPlane - 1].getTranslationX(), plane[focusPlane - 1].getTranslationX() + blueStripX[0] - plane[focusPlane - 1].getPosX());
                transYAnimator = ObjectAnimator.ofFloat(plane[focusPlane - 1], "translationY", plane[focusPlane - 1].getTranslationY(), plane[focusPlane - 1].getTranslationY() + blueStripY[0] - plane[focusPlane - 1].getPosY());
            } else if ((focusPlane - 1) > 11 && (focusPlane - 1) <= 15) {
                transXAnimator = ObjectAnimator.ofFloat(plane[focusPlane - 1], "translationX", plane[focusPlane - 1].getTranslationX(), plane[focusPlane - 1].getTranslationX() + greenStripX[0] - plane[focusPlane - 1].getPosX());
                transYAnimator = ObjectAnimator.ofFloat(plane[focusPlane - 1], "translationY", plane[focusPlane - 1].getTranslationY(), plane[focusPlane - 1].getTranslationY() + greenStripY[0] - plane[focusPlane - 1].getPosY());
            }
            AnimatorSet startAnimatorSet = new AnimatorSet();
            startAnimatorSet.setDuration(aniDuration);
            startAnimatorSet.playTogether(transXAnimator, transYAnimator);
            startAnimatorSet.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                    plane[focusPlane - 1].setNextStep(0);
                    plane[focusPlane - 1].setStep(0);
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    /** check the overlap on the start position */
                    int overlapCount = 0;
                    if ((focusPlane - 1) <= 3) {
                        for (int i = 0; i <= 3; i++) {
                            if (plane[i].getStep() == 0) {
                                overlapCount++;
                            }
                        }
                        for (int i = 0; i <= 3; i++) {
                            if (plane[i].getStep() == 0 && overlapCount > 1) {
                                plane[i].setOverlap(overlapCount);
                            }
                        }
                    } else if ((focusPlane - 1) > 3 && (focusPlane - 1) <= 7) {
                        for (int i = 4; i <= 7; i++) {
                            if (plane[i].getStep() == 0) {
                                overlapCount++;
                            }
                        }
                        for (int i = 4; i <= 7; i++) {
                            if (plane[i].getStep() == 0 && overlapCount > 1) {
                                plane[i].setOverlap(overlapCount);
                            }
                        }
                    } else if ((focusPlane - 1) > 7 && (focusPlane - 1) <= 11) {
                        for (int i = 8; i <= 11; i++) {
                            if (plane[i].getStep() == 0) {
                                overlapCount++;
                            }
                        }
                        for (int i = 8; i <= 11; i++) {
                            if (plane[i].getStep() == 0 && overlapCount > 1) {
                                plane[i].setOverlap(overlapCount);
                            }
                        }
                    } else if ((focusPlane - 1) > 11 && (focusPlane - 1) <= 15) {
                        for (int i = 12; i <= 15; i++) {
                            if (plane[i].getStep() == 0) {
                                overlapCount++;
                            }
                        }
                        for (int i = 12; i <= 15; i++) {
                            if (plane[i].getStep() == 0 && overlapCount > 1) {
                                plane[i].setOverlap(overlapCount);
                            }
                        }
                    }
                    /** change the direction of the planes */
                    if (plane[focusPlane - 1].getStep() != -1 && plane[focusPlane - 1].getStep() != 57) {
                        plane[focusPlane - 1].setNextStep(plane[focusPlane - 1].getStep() + 1);
                    }
                    changeDice();
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            startAnimatorSet.start();
        } else {
            /** check if the plane need to fly backward */
            stepAnimatorTimes = 0;
            if (plane[focusPlane - 1].getStep() + diceNumber > 56) {
                stepForward = 56 - plane[focusPlane - 1].getStep();
            } else {
                stepForward = diceNumber;
            }
            previousStep = plane[focusPlane - 1].getStep();/** save the step of the plane to check the overlap */
            setTransAnimator(focusPlane - 1, plane[focusPlane - 1].getStep() + 1);
            AnimatorSet stepAnimator = new AnimatorSet();
            stepAnimator.playTogether(transXAnimator, transYAnimator);
            stepAnimator.setDuration(aniDuration);
            listener = new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                    stepAnimatorTimes = stepAnimatorTimes + 1;
                    stepFlag = stepAnimatorTimes < diceNumber;
                    /** check if we need to change the overlap */
                    if ((focusPlane - 1) <= 3) {
                        for (int i = 0; i <= 3; i++) {
                            if ((focusPlane - 1) == i) {
                                plane[focusPlane - 1].setOverlap(1);
                            } else if (plane[i].getStep() == previousStep) {
                                plane[i].setOverlap(plane[i].getOverlap() - 1);
                            }
                        }
                    } else if ((focusPlane - 1) > 3 && (focusPlane - 1) <= 7) {
                        for (int i = 4; i <= 7; i++) {
                            if ((focusPlane - 1) == i) {
                                plane[focusPlane - 1].setOverlap(1);
                            } else if (plane[i].getStep() == previousStep) {
                                plane[i].setOverlap(plane[i].getOverlap() - 1);
                            }
                        }
                    } else if ((focusPlane - 1) > 7 && (focusPlane - 1) <= 11) {
                        for (int i = 8; i <= 11; i++) {
                            if ((focusPlane - 1) == i) {
                                plane[focusPlane - 1].setOverlap(1);
                            } else if (plane[i].getStep() == previousStep) {
                                plane[i].setOverlap(plane[i].getOverlap() - 1);
                            }
                        }
                    } else if ((focusPlane - 1) > 11 && (focusPlane - 1) <= 15) {
                        for (int i = 12; i <= 15; i++) {
                            if ((focusPlane - 1) == i) {
                                plane[focusPlane - 1].setOverlap(1);
                            } else if (plane[i].getStep() == previousStep) {
                                plane[i].setOverlap(plane[i].getOverlap() - 1);
                            }
                        }
                    }
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    previousStep = -2;  /** set previous step to -2 to stop the decreasing of the overlap */
                    if (stepFlag) {
                        float tempX, tempY;
                        tempX = plane[focusPlane - 1].getPosX();
                        tempY = plane[focusPlane - 1].getPosY();
                        if (stepAnimatorTimes < stepForward) { /** fly forward */
                            plane[focusPlane - 1].setNextStep(plane[focusPlane - 1].getStep() + 1);
                            plane[focusPlane - 1].setStep(plane[focusPlane - 1].getStep() + 1);
                        } else {    /** fly backward */
                            plane[focusPlane - 1].setNextStep(plane[focusPlane - 1].getStep() - 1);
                            plane[focusPlane - 1].setStep(plane[focusPlane - 1].getStep() - 1);
                        }
                        transXAnimator = ObjectAnimator.ofFloat(plane[focusPlane - 1], "translationX", plane[focusPlane - 1].getTranslationX(), plane[focusPlane - 1].getTranslationX() + plane[focusPlane - 1].getPosX() - tempX);
                        transYAnimator = ObjectAnimator.ofFloat(plane[focusPlane - 1], "translationY", plane[focusPlane - 1].getTranslationY(), plane[focusPlane - 1].getTranslationY() + plane[focusPlane - 1].getPosY() - tempY);
                        AnimatorSet stepAnimator = new AnimatorSet();
                        stepAnimator.playTogether(transXAnimator, transYAnimator);
                        stepAnimator.setDuration(aniDuration);
                        stepAnimator.addListener(listener);
                        stepAnimator.start();
                    } else if (plane[focusPlane - 1].getStep() == 14) { /** stop on position 14, jump and fly */
                        if (!kickBack()) {  /** if kick other plane, stop jump */
                            setTransAnimator(focusPlane - 1, plane[focusPlane - 1].getStep() + 4);
                            AnimatorSet jump14AnimatorSet = new AnimatorSet();
                            jump14AnimatorSet.playTogether(transXAnimator, transYAnimator);
                            jump14AnimatorSet.setDuration(aniDuration);
                            jump14AnimatorSet.addListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animator) {

                                }

                                @Override
                                public void onAnimationEnd(Animator animator) {
                                    if (!kickBack()) {  /** if kick other plane after jump, stop fly */
                                        float tempX, tempY;
                                        tempX = plane[focusPlane - 1].getPosX();
                                        tempY = plane[focusPlane - 1].getPosY();
                                        plane[focusPlane - 1].setNextStep(30);
                                        plane[focusPlane - 1].setStep(30);
                                        transXAnimator = ObjectAnimator.ofFloat(plane[focusPlane - 1], "translationX", plane[focusPlane - 1].getTranslationX(), plane[focusPlane - 1].getTranslationX() + plane[focusPlane - 1].getPosX() - tempX);
                                        //transXAnimator.setDuration(aniDuration * 2);
                                        transYAnimator = ObjectAnimator.ofFloat(plane[focusPlane - 1], "translationY", plane[focusPlane - 1].getTranslationY(), plane[focusPlane - 1].getTranslationY() + plane[focusPlane - 1].getPosY() - tempY);
                                        //transYAnimator.setDuration(aniDuration * 2);
                                        AnimatorSet fly14AnimatorSet = new AnimatorSet();
                                        fly14AnimatorSet.playTogether(transXAnimator, transYAnimator);
                                        fly14AnimatorSet.setDuration(aniDuration);
                                        fly14AnimatorSet.addListener(new Animator.AnimatorListener() {
                                            @Override
                                            public void onAnimationStart(Animator animator) {
                                                if ((focusPlane - 1) <= 3) {
                                                    for (int i = 8; i <= 11; i++) {
                                                        if (plane[i].getStep() == 53) {
                                                            float tempX, tempY;
                                                            tempX = plane[i].getPosX();
                                                            tempY = plane[i].getPosY();
                                                            plane[i].setStep(-1);
                                                            transXAnimator = ObjectAnimator.ofFloat(plane[i], "translationX", plane[i].getTranslationX(), plane[i].getTranslationX() + plane[i].getPosX() - tempX);
                                                            //transXAnimator.setDuration(aniDuration);
                                                            transYAnimator = ObjectAnimator.ofFloat(plane[i], "translationY", plane[i].getTranslationY(), plane[i].getTranslationY() + plane[i].getPosY() - tempY);
                                                            //transYAnimator.setDuration(aniDuration);
                                                            AnimatorSet kickBackAnimatorSet = new AnimatorSet();
                                                            kickBackAnimatorSet.playTogether(transXAnimator, transYAnimator);
                                                            kickBackAnimatorSet.setDuration(aniDuration / 2);
                                                            kickBackAnimatorSet.setStartDelay(aniDuration / 2);
                                                            kickBackAnimatorSet.addListener(new Animator.AnimatorListener() {
                                                                @Override
                                                                public void onAnimationStart(Animator animator) {
                                                                    for (int i = 8; i <= 11; i++) {
                                                                        if (plane[i].getStep() == -1) {
                                                                            plane[i].setOverlap(1);
                                                                        }
                                                                    }
                                                                }

                                                                @Override
                                                                public void onAnimationEnd(Animator animator) {
                                                                    //airPortDir();
                                                                }

                                                                @Override
                                                                public void onAnimationCancel(Animator animator) {

                                                                }

                                                                @Override
                                                                public void onAnimationRepeat(Animator animator) {

                                                                }
                                                            });
                                                            kickBackAnimatorSet.start();
                                                        }
                                                    }
                                                } else if ((focusPlane - 1) >= 4 && (focusPlane - 1) <= 7) {
                                                    for (int i = 12; i <= 15; i++) {
                                                        if (plane[i].getStep() == 53) {
                                                            float tempX, tempY;
                                                            tempX = plane[i].getPosX();
                                                            tempY = plane[i].getPosY();
                                                            //plane[i].setNextStep(nextStep);
                                                            plane[i].setStep(-1);
                                                            //plane[i].setOverlap(1);
                                                            transXAnimator = ObjectAnimator.ofFloat(plane[i], "translationX", plane[i].getTranslationX(), plane[i].getTranslationX() + plane[i].getPosX() - tempX);
                                                            //transXAnimator.setDuration(aniDuration);
                                                            transYAnimator = ObjectAnimator.ofFloat(plane[i], "translationY", plane[i].getTranslationY(), plane[i].getTranslationY() + plane[i].getPosY() - tempY);
                                                            //transYAnimator.setDuration(aniDuration);
                                                            AnimatorSet kickBackAnimatorSet = new AnimatorSet();
                                                            kickBackAnimatorSet.playTogether(transXAnimator, transYAnimator);
                                                            kickBackAnimatorSet.setDuration(aniDuration / 2);
                                                            kickBackAnimatorSet.setStartDelay(aniDuration / 2);
                                                            kickBackAnimatorSet.addListener(new Animator.AnimatorListener() {
                                                                @Override
                                                                public void onAnimationStart(Animator animator) {
                                                                    for (int i = 12; i <= 15; i++) {
                                                                        if (plane[i].getStep() == -1) {
                                                                            plane[i].setOverlap(1);
                                                                        }
                                                                    }
                                                                }

                                                                @Override
                                                                public void onAnimationEnd(Animator animator) {
                                                                    //airPortDir();
                                                                }

                                                                @Override
                                                                public void onAnimationCancel(Animator animator) {

                                                                }

                                                                @Override
                                                                public void onAnimationRepeat(Animator animator) {

                                                                }
                                                            });
                                                            kickBackAnimatorSet.start();
                                                        }
                                                    }
                                                } else if ((focusPlane - 1) >= 8 && (focusPlane - 1) <= 11) {
                                                    for (int i = 0; i <= 3; i++) {
                                                        if (plane[i].getStep() == 53) {
                                                            float tempX, tempY;
                                                            tempX = plane[i].getPosX();
                                                            tempY = plane[i].getPosY();
                                                            //plane[i].setNextStep(nextStep);
                                                            plane[i].setStep(-1);
                                                            //plane[i].setOverlap(1);
                                                            transXAnimator = ObjectAnimator.ofFloat(plane[i], "translationX", plane[i].getTranslationX(), plane[i].getTranslationX() + plane[i].getPosX() - tempX);
                                                            //transXAnimator.setDuration(aniDuration);
                                                            transYAnimator = ObjectAnimator.ofFloat(plane[i], "translationY", plane[i].getTranslationY(), plane[i].getTranslationY() + plane[i].getPosY() - tempY);
                                                            //transYAnimator.setDuration(aniDuration);
                                                            AnimatorSet kickBackAnimatorSet = new AnimatorSet();
                                                            kickBackAnimatorSet.playTogether(transXAnimator, transYAnimator);
                                                            kickBackAnimatorSet.setDuration(aniDuration / 2);
                                                            kickBackAnimatorSet.setStartDelay(aniDuration / 2);
                                                            kickBackAnimatorSet.addListener(new Animator.AnimatorListener() {
                                                                @Override
                                                                public void onAnimationStart(Animator animator) {
                                                                    for (int i = 0; i <= 3; i++) {
                                                                        if (plane[i].getStep() == -1) {
                                                                            plane[i].setOverlap(1);
                                                                        }
                                                                    }
                                                                }

                                                                @Override
                                                                public void onAnimationEnd(Animator animator) {
                                                                    //airPortDir();
                                                                }

                                                                @Override
                                                                public void onAnimationCancel(Animator animator) {

                                                                }

                                                                @Override
                                                                public void onAnimationRepeat(Animator animator) {

                                                                }
                                                            });
                                                            kickBackAnimatorSet.start();
                                                        }
                                                    }
                                                } else if ((focusPlane - 1) >= 12 && (focusPlane - 1) <= 15) {
                                                    for (int i = 4; i <= 7; i++) {
                                                        if (plane[i].getStep() == 53) {
                                                            float tempX, tempY;
                                                            tempX = plane[i].getPosX();
                                                            tempY = plane[i].getPosY();
                                                            //plane[i].setNextStep(nextStep);
                                                            plane[i].setStep(-1);
                                                            //plane[i].setOverlap(1);
                                                            transXAnimator = ObjectAnimator.ofFloat(plane[i], "translationX", plane[i].getTranslationX(), plane[i].getTranslationX() + plane[i].getPosX() - tempX);
                                                            //transXAnimator.setDuration(aniDuration);
                                                            transYAnimator = ObjectAnimator.ofFloat(plane[i], "translationY", plane[i].getTranslationY(), plane[i].getTranslationY() + plane[i].getPosY() - tempY);
                                                            //transYAnimator.setDuration(aniDuration);
                                                            AnimatorSet kickBackAnimatorSet = new AnimatorSet();
                                                            kickBackAnimatorSet.playTogether(transXAnimator, transYAnimator);
                                                            kickBackAnimatorSet.setDuration(aniDuration / 2);
                                                            kickBackAnimatorSet.setStartDelay(aniDuration / 2);
                                                            kickBackAnimatorSet.addListener(new Animator.AnimatorListener() {
                                                                @Override
                                                                public void onAnimationStart(Animator animator) {
                                                                    for (int i = 4; i <= 7; i++) {
                                                                        if (plane[i].getStep() == -1) {
                                                                            plane[i].setOverlap(1);
                                                                        }
                                                                    }
                                                                }

                                                                @Override
                                                                public void onAnimationEnd(Animator animator) {
                                                                    //airPortDir();
                                                                }

                                                                @Override
                                                                public void onAnimationCancel(Animator animator) {

                                                                }

                                                                @Override
                                                                public void onAnimationRepeat(Animator animator) {

                                                                }
                                                            });
                                                            kickBackAnimatorSet.start();
                                                        }
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onAnimationEnd(Animator animator) {
                                                if (!kickBack()) {
                                                    /** change direction of the plane */
                                                    if (plane[focusPlane - 1].getStep() != -1 && plane[focusPlane - 1].getStep() != 57) {
                                                        plane[focusPlane - 1].setNextStep(plane[focusPlane - 1].getStep() + 1);
                                                    }
                                                    /** check overlap */
                                                    currentStep = plane[focusPlane - 1].getStep();
                                                    int count = 0;
                                                    if ((focusPlane - 1) <= 3) {
                                                        for (int i = 0; i <= 3; i++) {
                                                            if ((focusPlane - 1) != i && plane[i].getStep() == currentStep) {
                                                                plane[i].setOverlap(plane[i].getOverlap() + 1);
                                                                count = plane[i].getOverlap();
                                                            }
                                                        }
                                                        plane[focusPlane - 1].setOverlap(count);
                                                    } else if ((focusPlane - 1) > 3 && (focusPlane - 1) <= 7) {
                                                        for (int i = 4; i <= 7; i++) {
                                                            if ((focusPlane - 1) != i && plane[i].getStep() == currentStep) {
                                                                plane[i].setOverlap(plane[i].getOverlap() + 1);
                                                                count = plane[i].getOverlap();
                                                            }
                                                        }
                                                        plane[focusPlane - 1].setOverlap(count);
                                                    } else if ((focusPlane - 1) > 7 && (focusPlane - 1) <= 11) {
                                                        for (int i = 8; i <= 11; i++) {
                                                            if ((focusPlane - 1) != i && plane[i].getStep() == currentStep) {
                                                                plane[i].setOverlap(plane[i].getOverlap() + 1);
                                                                count = plane[i].getOverlap();
                                                            }
                                                        }
                                                        plane[focusPlane - 1].setOverlap(count);
                                                    } else if ((focusPlane - 1) > 11 && (focusPlane - 1) <= 15) {
                                                        for (int i = 12; i <= 15; i++) {
                                                            if ((focusPlane - 1) != i && plane[i].getStep() == currentStep) {
                                                                plane[i].setOverlap(plane[i].getOverlap() + 1);
                                                                count = plane[i].getOverlap();
                                                            }
                                                        }
                                                        plane[focusPlane - 1].setOverlap(count);
                                                    }
                                                    changeDice();
                                                }
                                            }

                                            @Override
                                            public void onAnimationCancel(Animator animator) {

                                            }

                                            @Override
                                            public void onAnimationRepeat(Animator animator) {

                                            }
                                        });
                                        fly14AnimatorSet.start();
                                    }
                                }

                                @Override
                                public void onAnimationCancel(Animator animator) {

                                }

                                @Override
                                public void onAnimationRepeat(Animator animator) {

                                }
                            });
                            jump14AnimatorSet.start();
                        }
                    } else if (plane[focusPlane - 1].getStep() == 18) { /** on position 18, fly and jump */
                        if (!kickBack()) { /** if kick other plane, stop fly */
                            float tempX, tempY;
                            tempX = plane[focusPlane - 1].getPosX();
                            tempY = plane[focusPlane - 1].getPosY();
                            plane[focusPlane - 1].setNextStep(30);
                            plane[focusPlane - 1].setStep(30);
                            transXAnimator = ObjectAnimator.ofFloat(plane[focusPlane - 1], "translationX", plane[focusPlane - 1].getTranslationX(), plane[focusPlane - 1].getTranslationX() + plane[focusPlane - 1].getPosX() - tempX);
                            //transXAnimator.setDuration(aniDuration * 2);
                            transYAnimator = ObjectAnimator.ofFloat(plane[focusPlane - 1], "translationY", plane[focusPlane - 1].getTranslationY(), plane[focusPlane - 1].getTranslationY() + plane[focusPlane - 1].getPosY() - tempY);
                            //transYAnimator.setDuration(aniDuration * 2);
                            AnimatorSet fly18AnimatorSet = new AnimatorSet();
                            fly18AnimatorSet.playTogether(transXAnimator, transYAnimator);
                            fly18AnimatorSet.setDuration(aniDuration);
                            fly18AnimatorSet.addListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animator) {
                                    fly18KickFlag = false;/** check if kick other plane */
                                    if ((focusPlane - 1) <= 3) {
                                        for (int i = 8; i <= 11; i++) {
                                            if (plane[i].getStep() == 53) {
                                                fly18KickFlag = true;
                                                float tempX, tempY;
                                                tempX = plane[i].getPosX();
                                                tempY = plane[i].getPosY();
                                                //plane[i].setNextStep(nextStep);
                                                plane[i].setStep(-1);
                                                //plane[i].setOverlap(1);
                                                transXAnimator = ObjectAnimator.ofFloat(plane[i], "translationX", plane[i].getTranslationX(), plane[i].getTranslationX() + plane[i].getPosX() - tempX);
                                                //transXAnimator.setDuration(aniDuration);
                                                transYAnimator = ObjectAnimator.ofFloat(plane[i], "translationY", plane[i].getTranslationY(), plane[i].getTranslationY() + plane[i].getPosY() - tempY);
                                                //transYAnimator.setDuration(aniDuration);
                                                AnimatorSet kickBackAnimatorSet = new AnimatorSet();
                                                kickBackAnimatorSet.playTogether(transXAnimator, transYAnimator);
                                                kickBackAnimatorSet.setDuration(aniDuration / 2);
                                                kickBackAnimatorSet.setStartDelay(aniDuration / 2);
                                                kickBackAnimatorSet.addListener(new Animator.AnimatorListener() {
                                                    @Override
                                                    public void onAnimationStart(Animator animator) {
                                                        for (int i = 8; i <= 11; i++) {
                                                            if (plane[i].getStep() == -1) {
                                                                plane[i].setOverlap(1);
                                                            }
                                                        }
                                                    }

                                                    @Override
                                                    public void onAnimationEnd(Animator animator) {
                                                        //airPortDir();
                                                    }

                                                    @Override
                                                    public void onAnimationCancel(Animator animator) {

                                                    }

                                                    @Override
                                                    public void onAnimationRepeat(Animator animator) {

                                                    }
                                                });
                                                kickBackAnimatorSet.start();
                                            }
                                        }
                                    } else if ((focusPlane - 1) >= 4 && (focusPlane - 1) <= 7) {
                                        for (int i = 12; i <= 15; i++) {
                                            if (plane[i].getStep() == 53) {
                                                fly18KickFlag = true;
                                                float tempX, tempY;
                                                tempX = plane[i].getPosX();
                                                tempY = plane[i].getPosY();
                                                //plane[i].setNextStep(nextStep);
                                                plane[i].setStep(-1);
                                                //plane[i].setOverlap(1);
                                                transXAnimator = ObjectAnimator.ofFloat(plane[i], "translationX", plane[i].getTranslationX(), plane[i].getTranslationX() + plane[i].getPosX() - tempX);
                                                //transXAnimator.setDuration(aniDuration);
                                                transYAnimator = ObjectAnimator.ofFloat(plane[i], "translationY", plane[i].getTranslationY(), plane[i].getTranslationY() + plane[i].getPosY() - tempY);
                                                //transYAnimator.setDuration(aniDuration);
                                                AnimatorSet kickBackAnimatorSet = new AnimatorSet();
                                                kickBackAnimatorSet.playTogether(transXAnimator, transYAnimator);
                                                kickBackAnimatorSet.setDuration(aniDuration / 2);
                                                kickBackAnimatorSet.setStartDelay(aniDuration / 2);
                                                kickBackAnimatorSet.addListener(new Animator.AnimatorListener() {
                                                    @Override
                                                    public void onAnimationStart(Animator animator) {
                                                        for (int i = 12; i <= 15; i++) {
                                                            if (plane[i].getStep() == -1) {
                                                                plane[i].setOverlap(1);
                                                            }
                                                        }
                                                    }

                                                    @Override
                                                    public void onAnimationEnd(Animator animator) {
                                                        //airPortDir();
                                                    }

                                                    @Override
                                                    public void onAnimationCancel(Animator animator) {

                                                    }

                                                    @Override
                                                    public void onAnimationRepeat(Animator animator) {

                                                    }
                                                });
                                                kickBackAnimatorSet.start();
                                            }
                                        }
                                    } else if ((focusPlane - 1) >= 8 && (focusPlane - 1) <= 11) {
                                        for (int i = 0; i <= 3; i++) {
                                            if (plane[i].getStep() == 53) {
                                                fly18KickFlag = true;
                                                float tempX, tempY;
                                                tempX = plane[i].getPosX();
                                                tempY = plane[i].getPosY();
                                                //plane[i].setNextStep(nextStep);
                                                plane[i].setStep(-1);
                                                //plane[i].setOverlap(1);
                                                transXAnimator = ObjectAnimator.ofFloat(plane[i], "translationX", plane[i].getTranslationX(), plane[i].getTranslationX() + plane[i].getPosX() - tempX);
                                                //transXAnimator.setDuration(aniDuration);
                                                transYAnimator = ObjectAnimator.ofFloat(plane[i], "translationY", plane[i].getTranslationY(), plane[i].getTranslationY() + plane[i].getPosY() - tempY);
                                                //transYAnimator.setDuration(aniDuration);
                                                AnimatorSet kickBackAnimatorSet = new AnimatorSet();
                                                kickBackAnimatorSet.playTogether(transXAnimator, transYAnimator);
                                                kickBackAnimatorSet.setDuration(aniDuration / 2);
                                                kickBackAnimatorSet.setStartDelay(aniDuration / 2);
                                                kickBackAnimatorSet.addListener(new Animator.AnimatorListener() {
                                                    @Override
                                                    public void onAnimationStart(Animator animator) {
                                                        for (int i = 0; i <= 3; i++) {
                                                            if (plane[i].getStep() == -1) {
                                                                plane[i].setOverlap(1);
                                                            }
                                                        }
                                                    }

                                                    @Override
                                                    public void onAnimationEnd(Animator animator) {
                                                        //airPortDir();
                                                    }

                                                    @Override
                                                    public void onAnimationCancel(Animator animator) {

                                                    }

                                                    @Override
                                                    public void onAnimationRepeat(Animator animator) {

                                                    }
                                                });
                                                kickBackAnimatorSet.start();
                                            }
                                        }
                                    } else if ((focusPlane - 1) >= 12 && (focusPlane - 1) <= 15) {
                                        for (int i = 4; i <= 7; i++) {
                                            if (plane[i].getStep() == 53) {
                                                fly18KickFlag = true;
                                                float tempX, tempY;
                                                tempX = plane[i].getPosX();
                                                tempY = plane[i].getPosY();
                                                //plane[i].setNextStep(nextStep);
                                                plane[i].setStep(-1);
                                                //plane[i].setOverlap(1);
                                                transXAnimator = ObjectAnimator.ofFloat(plane[i], "translationX", plane[i].getTranslationX(), plane[i].getTranslationX() + plane[i].getPosX() - tempX);
                                                //transXAnimator.setDuration(aniDuration);
                                                transYAnimator = ObjectAnimator.ofFloat(plane[i], "translationY", plane[i].getTranslationY(), plane[i].getTranslationY() + plane[i].getPosY() - tempY);
                                                //transYAnimator.setDuration(aniDuration);
                                                AnimatorSet kickBackAnimatorSet = new AnimatorSet();
                                                kickBackAnimatorSet.playTogether(transXAnimator, transYAnimator);
                                                kickBackAnimatorSet.setDuration(aniDuration / 2);
                                                kickBackAnimatorSet.setStartDelay(aniDuration / 2);
                                                kickBackAnimatorSet.addListener(new Animator.AnimatorListener() {
                                                    @Override
                                                    public void onAnimationStart(Animator animator) {
                                                        for (int i = 4; i <= 7; i++) {
                                                            if (plane[i].getStep() == -1) {
                                                                plane[i].setOverlap(1);
                                                            }
                                                        }
                                                    }

                                                    @Override
                                                    public void onAnimationEnd(Animator animator) {
                                                        //airPortDir();
                                                    }

                                                    @Override
                                                    public void onAnimationCancel(Animator animator) {

                                                    }

                                                    @Override
                                                    public void onAnimationRepeat(Animator animator) {

                                                    }
                                                });
                                                kickBackAnimatorSet.start();
                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onAnimationEnd(Animator animator) {
                                    boolean tempKickBackflag = kickBack();
                                    if (!tempKickBackflag && !fly18KickFlag) { /** jump after fly */
                                        float tempX, tempY;
                                        tempX = plane[focusPlane - 1].getPosX();
                                        tempY = plane[focusPlane - 1].getPosY();
                                        plane[focusPlane - 1].setNextStep(34);
                                        plane[focusPlane - 1].setStep(34);
                                        transXAnimator = ObjectAnimator.ofFloat(plane[focusPlane - 1], "translationX", plane[focusPlane - 1].getTranslationX(), plane[focusPlane - 1].getTranslationX() + plane[focusPlane - 1].getPosX() - tempX);
                                        //transXAnimator.setDuration(aniDuration);
                                        transYAnimator = ObjectAnimator.ofFloat(plane[focusPlane - 1], "translationY", plane[focusPlane - 1].getTranslationY(), plane[focusPlane - 1].getTranslationY() + plane[focusPlane - 1].getPosY() - tempY);
                                        //transYAnimator.setDuration(aniDuration);
                                        AnimatorSet jump18AnimatorSet = new AnimatorSet();
                                        jump18AnimatorSet.playTogether(transXAnimator, transYAnimator);
                                        jump18AnimatorSet.setDuration(aniDuration);
                                        jump18AnimatorSet.addListener(new Animator.AnimatorListener() {
                                            @Override
                                            public void onAnimationStart(Animator animator) {

                                            }

                                            @Override
                                            public void onAnimationEnd(Animator animator) {
                                                if (!kickBack()) {
                                                    /** change direction */
                                                    if (plane[focusPlane - 1].getStep() != -1 && plane[focusPlane - 1].getStep() != 57) {
                                                        plane[focusPlane - 1].setNextStep(plane[focusPlane - 1].getStep() + 1);
                                                    }
                                                    /** change overlap */
                                                    currentStep = plane[focusPlane - 1].getStep();
                                                    int count = 0;
                                                    if ((focusPlane - 1) <= 3) {
                                                        for (int i = 0; i <= 3; i++) {
                                                            if ((focusPlane - 1) != i && plane[i].getStep() == currentStep) {
                                                                plane[i].setOverlap(plane[i].getOverlap() + 1);
                                                                count = plane[i].getOverlap();
                                                            }
                                                        }
                                                        plane[focusPlane - 1].setOverlap(count);
                                                    } else if ((focusPlane - 1) > 3 && (focusPlane - 1) <= 7) {
                                                        for (int i = 4; i <= 7; i++) {
                                                            if ((focusPlane - 1) != i && plane[i].getStep() == currentStep) {
                                                                plane[i].setOverlap(plane[i].getOverlap() + 1);
                                                                count = plane[i].getOverlap();
                                                            }
                                                        }
                                                        plane[focusPlane - 1].setOverlap(count);
                                                    } else if ((focusPlane - 1) > 7 && (focusPlane - 1) <= 11) {
                                                        for (int i = 8; i <= 11; i++) {
                                                            if ((focusPlane - 1) != i && plane[i].getStep() == currentStep) {
                                                                plane[i].setOverlap(plane[i].getOverlap() + 1);
                                                                count = plane[i].getOverlap();
                                                            }
                                                        }
                                                        plane[focusPlane - 1].setOverlap(count);
                                                    } else if ((focusPlane - 1) > 11 && (focusPlane - 1) <= 15) {
                                                        for (int i = 12; i <= 15; i++) {
                                                            if ((focusPlane - 1) != i && plane[i].getStep() == currentStep) {
                                                                plane[i].setOverlap(plane[i].getOverlap() + 1);
                                                                count = plane[i].getOverlap();
                                                            }
                                                        }
                                                        plane[focusPlane - 1].setOverlap(count);
                                                    }
                                                    changeDice();
                                                }
                                            }

                                            @Override
                                            public void onAnimationCancel(Animator animator) {

                                            }

                                            @Override
                                            public void onAnimationRepeat(Animator animator) {

                                            }
                                        });
                                        jump18AnimatorSet.start();
                                    } else if (!tempKickBackflag) {
                                        // check overlap
                                        currentStep = plane[focusPlane - 1].getStep();
                                        int count = 0;
                                        if ((focusPlane - 1) <= 3) {
                                            for (int i = 0; i <= 3; i++) {
                                                if ((focusPlane - 1) != i && plane[i].getStep() == currentStep) {
                                                    plane[i].setOverlap(plane[i].getOverlap() + 1);
                                                    count = plane[i].getOverlap();
                                                }
                                            }
                                            plane[focusPlane - 1].setOverlap(count);
                                        } else if ((focusPlane - 1) > 3 && (focusPlane - 1) <= 7) {
                                            for (int i = 4; i <= 7; i++) {
                                                if ((focusPlane - 1) != i && plane[i].getStep() == currentStep) {
                                                    plane[i].setOverlap(plane[i].getOverlap() + 1);
                                                    count = plane[i].getOverlap();
                                                }
                                            }
                                            plane[focusPlane - 1].setOverlap(count);
                                        } else if ((focusPlane - 1) > 7 && (focusPlane - 1) <= 11) {
                                            for (int i = 8; i <= 11; i++) {
                                                if ((focusPlane - 1) != i && plane[i].getStep() == currentStep) {
                                                    plane[i].setOverlap(plane[i].getOverlap() + 1);
                                                    count = plane[i].getOverlap();
                                                }
                                            }
                                            plane[focusPlane - 1].setOverlap(count);
                                        } else if ((focusPlane - 1) > 11 && (focusPlane - 1) <= 15) {
                                            for (int i = 12; i <= 15; i++) {
                                                if ((focusPlane - 1) != i && plane[i].getStep() == currentStep) {
                                                    plane[i].setOverlap(plane[i].getOverlap() + 1);
                                                    count = plane[i].getOverlap();
                                                }
                                            }
                                            plane[focusPlane - 1].setOverlap(count);
                                        }
                                        changeDice();
                                    }
                                }

                                @Override
                                public void onAnimationCancel(Animator animator) {
                                }

                                @Override
                                public void onAnimationRepeat(Animator animator) {
                                }
                            });
                            fly18AnimatorSet.start();
                        }
                    } else if (plane[focusPlane - 1].getStep() % 4 == 2 && plane[focusPlane - 1].getStep() < 50 && plane[focusPlane - 1].getStep() != 14 && plane[focusPlane - 1].getStep() != 18) {// fly 4 steps
                        if (!kickBack()) { /** jump */
                            float tempX, tempY;
                            tempX = plane[focusPlane - 1].getPosX();
                            tempY = plane[focusPlane - 1].getPosY();
                            plane[focusPlane - 1].setNextStep(plane[focusPlane - 1].getStep() + 4);
                            plane[focusPlane - 1].setStep(plane[focusPlane - 1].getStep() + 4);
                            transXAnimator = ObjectAnimator.ofFloat(plane[focusPlane - 1], "translationX", plane[focusPlane - 1].getTranslationX(), plane[focusPlane - 1].getTranslationX() + plane[focusPlane - 1].getPosX() - tempX);
                            //transXAnimator.setDuration(aniDuration);
                            transYAnimator = ObjectAnimator.ofFloat(plane[focusPlane - 1], "translationY", plane[focusPlane - 1].getTranslationY(), plane[focusPlane - 1].getTranslationY() + plane[focusPlane - 1].getPosY() - tempY);
                            //transYAnimator.setDuration(aniDuration);
                            AnimatorSet jumpAnimatorSet = new AnimatorSet();
                            jumpAnimatorSet.playTogether(transXAnimator, transYAnimator);
                            jumpAnimatorSet.setDuration(aniDuration);
                            jumpAnimatorSet.addListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animator) {

                                }

                                @Override
                                public void onAnimationEnd(Animator animator) {
                                    if (!kickBack()) {
                                        if (plane[focusPlane - 1].getStep() != -1 && plane[focusPlane - 1].getStep() != 57) {
                                            plane[focusPlane - 1].setNextStep(plane[focusPlane - 1].getStep() + 1);
                                        }
                                        /** change overlap */
                                        currentStep = plane[focusPlane - 1].getStep();
                                        int count = 0;
                                        if ((focusPlane - 1) <= 3) {
                                            for (int i = 0; i <= 3; i++) {
                                                if ((focusPlane - 1) != i && plane[i].getStep() == currentStep) {
                                                    plane[i].setOverlap(plane[i].getOverlap() + 1);
                                                    count = plane[i].getOverlap();
                                                }
                                            }
                                            plane[focusPlane - 1].setOverlap(count);
                                        } else if ((focusPlane - 1) > 3 && (focusPlane - 1) <= 7) {
                                            for (int i = 4; i <= 7; i++) {
                                                if ((focusPlane - 1) != i && plane[i].getStep() == currentStep) {
                                                    plane[i].setOverlap(plane[i].getOverlap() + 1);
                                                    count = plane[i].getOverlap();
                                                }
                                            }
                                            plane[focusPlane - 1].setOverlap(count);
                                        } else if ((focusPlane - 1) > 7 && (focusPlane - 1) <= 11) {
                                            for (int i = 8; i <= 11; i++) {
                                                if ((focusPlane - 1) != i && plane[i].getStep() == currentStep) {
                                                    plane[i].setOverlap(plane[i].getOverlap() + 1);
                                                    count = plane[i].getOverlap();
                                                }
                                            }
                                            plane[focusPlane - 1].setOverlap(count);
                                        } else if ((focusPlane - 1) > 11 && (focusPlane - 1) <= 15) {
                                            for (int i = 12; i <= 15; i++) {
                                                if ((focusPlane - 1) != i && plane[i].getStep() == currentStep) {
                                                    plane[i].setOverlap(plane[i].getOverlap() + 1);
                                                    count = plane[i].getOverlap();
                                                }
                                            }
                                            plane[focusPlane - 1].setOverlap(count);
                                        }
                                        changeDice();
                                    }
                                }

                                @Override
                                public void onAnimationCancel(Animator animator) {

                                }

                                @Override
                                public void onAnimationRepeat(Animator animator) {

                                }
                            });
                            jumpAnimatorSet.start();
                        }
                    } else if (plane[focusPlane - 1].getStep() == 56) { /** back to hangar */
                        setTransAnimator(focusPlane - 1, 57);
                        AnimatorSet landAnimatorSet = new AnimatorSet();
                        landAnimatorSet.playTogether(transXAnimator, transYAnimator);
                        landAnimatorSet.setDuration(aniDuration);
                        landAnimatorSet.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animator) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animator) {
                                plane[focusPlane - 1].setDonw();
                                if ((focusPlane - 1) <= 3) {
                                    plane[focusPlane - 1].setAngle(90);
                                    if (plane[0].isDown() && plane[1].isDown() && plane[2].isDown() && plane[3].isDown()) {
                                        isRedDown = true;
                                        if (rank1 == -1) {
                                            rank1 = Constants.RED;
                                            if (playType != Constants.FOURPLAYER) {
                                                focusPlane = 18;
                                                invalidate();
                                                rank2 = Constants.BLUE;
                                                rankView.setRank(rank1, rank2, rank3, rank4);
                                                rankView.setVisibility(VISIBLE);
                                                showRank();
                                            } else {
                                                changeDice();
                                            }
                                        } else if (rank2 == -1 && playType == Constants.FOURPLAYER) {
                                            rank2 = Constants.RED;
                                            changeDice();
                                        } else if (rank3 == -1 && playType == Constants.FOURPLAYER) {
                                            rank3 = Constants.RED;
                                            focusPlane = 18;
                                            invalidate();
                                            if (!isYellowDown) {
                                                rank4 = Constants.YELLOW;
                                            } else if (!isBlueDown) {
                                                rank4 = Constants.BLUE;
                                            } else if (!isGreenDown) {
                                                rank4 = Constants.GREEN;
                                            }
                                            rankView.setRank(rank1, rank2, rank3, rank4);
                                            rankView.setVisibility(VISIBLE);
                                            showRank();
                                        } else {
                                            changeDice();
                                        }
                                    } else {
                                        changeDice();
                                    }
                                } else if ((focusPlane - 1) > 3 && (focusPlane - 1) <= 7) {
                                    plane[focusPlane - 1].setAngle(180);
                                    if (plane[4].isDown() && plane[5].isDown() && plane[6].isDown() && plane[7].isDown()) {
                                        isYellowDown = true;
                                        if (rank1 == -1) {
                                            rank1 = Constants.YELLOW;
                                            if (playType != Constants.FOURPLAYER) {
                                                focusPlane = 18;
                                                invalidate();
                                                rank2 = Constants.GREEN;
                                                rankView.setRank(rank1, rank2, rank3, rank4);
                                                rankView.setVisibility(VISIBLE);
                                                showRank();
                                            } else {
                                                changeDice();
                                            }
                                        } else if (rank2 == -1 && playType == Constants.FOURPLAYER) {
                                            rank2 = Constants.YELLOW;
                                            changeDice();
                                        } else if (rank3 == -1 && playType == Constants.FOURPLAYER) {
                                            rank3 = Constants.YELLOW;
                                            focusPlane = 18;
                                            invalidate();
                                            if (!isRedDown) {
                                                rank4 = Constants.RED;
                                            } else if (!isBlueDown) {
                                                rank4 = Constants.BLUE;
                                            } else if (!isGreenDown) {
                                                rank4 = Constants.GREEN;
                                            }
                                            rankView.setRank(rank1, rank2, rank3, rank4);
                                            rankView.setVisibility(VISIBLE);
                                            showRank();
                                        } else {
                                            changeDice();
                                        }
                                    } else {
                                        changeDice();
                                    }
                                } else if ((focusPlane - 1) > 7 && (focusPlane - 1) <= 11) {
                                    plane[focusPlane - 1].setAngle(270);
                                    if (plane[8].isDown() && plane[9].isDown() && plane[10].isDown() && plane[11].isDown()) {
                                        isBlueDown = true;
                                        if (rank1 == -1) {
                                            rank1 = Constants.BLUE;
                                            if (playType != Constants.FOURPLAYER) {
                                                focusPlane = 18;
                                                invalidate();
                                                rank2 = Constants.RED;
                                                rankView.setRank(rank1, rank2, rank3, rank4);
                                                rankView.setVisibility(VISIBLE);
                                                showRank();
                                            } else {
                                                changeDice();
                                            }
                                        } else if (rank2 == -1 && playType == Constants.FOURPLAYER) {
                                            rank2 = Constants.BLUE;
                                            changeDice();
                                        } else if (rank3 == -1 && playType == Constants.FOURPLAYER) {
                                            rank3 = Constants.BLUE;
                                            focusPlane = 18;
                                            invalidate();
                                            if (!isRedDown) {
                                                rank4 = Constants.RED;
                                            } else if (!isYellowDown) {
                                                rank4 = Constants.YELLOW;
                                            } else if (!isGreenDown) {
                                                rank4 = Constants.GREEN;
                                            }
                                            rankView.setRank(rank1, rank2, rank3, rank4);
                                            rankView.setVisibility(VISIBLE);
                                            showRank();
                                        } else {
                                            changeDice();
                                        }
                                    } else {
                                        changeDice();
                                    }
                                } else if ((focusPlane - 1) > 11 && (focusPlane - 1) <= 15) {
                                    plane[focusPlane - 1].setAngle(0);
                                    if (plane[12].isDown() && plane[13].isDown() && plane[14].isDown() && plane[15].isDown()) {
                                        isGreenDown = true;
                                        if (rank1 == -1) {
                                            rank1 = Constants.GREEN;
                                            if (playType != Constants.FOURPLAYER) {
                                                focusPlane = 18;
                                                invalidate();
                                                rank2 = Constants.YELLOW;
                                                rankView.setRank(rank1, rank2, rank3, rank4);
                                                rankView.setVisibility(VISIBLE);
                                                showRank();
                                            } else {
                                                changeDice();
                                            }
                                        } else if (rank2 == -1 && playType == Constants.FOURPLAYER) {
                                            rank2 = Constants.GREEN;
                                            changeDice();
                                        } else if (rank3 == -1 && playType == Constants.FOURPLAYER) {
                                            rank3 = Constants.GREEN;
                                            focusPlane = 18;
                                            invalidate();
                                            if (!isRedDown) {
                                                rank4 = Constants.RED;
                                            } else if (!isYellowDown) {
                                                rank4 = Constants.YELLOW;
                                            } else if (!isBlueDown) {
                                                rank4 = Constants.BLUE;
                                            }
                                            rankView.setRank(rank1, rank2, rank3, rank4);
                                            rankView.setVisibility(VISIBLE);
                                            showRank();
                                        } else {
                                            changeDice();
                                        }
                                    } else {
                                        changeDice();
                                    }
                                }
                            }

                            @Override
                            public void onAnimationCancel(Animator animator) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animator) {

                            }
                        });
                        landAnimatorSet.start();
                    } else {
                        if (!kickBack()) {
                            if (plane[focusPlane - 1].getStep() != -1 && plane[focusPlane - 1].getStep() != 57) {
                                plane[focusPlane - 1].setNextStep(plane[focusPlane - 1].getStep() + 1);
                            }
                            /** change overlap */
                            currentStep = plane[focusPlane - 1].getStep();
                            int count = 0;
                            if ((focusPlane - 1) <= 3) {
                                for (int i = 0; i <= 3; i++) {
                                    if ((focusPlane - 1) != i && plane[i].getStep() == currentStep) {
                                        plane[i].setOverlap(plane[i].getOverlap() + 1);
                                        count = plane[i].getOverlap();
                                    }
                                }
                                plane[focusPlane - 1].setOverlap(count);
                            } else if ((focusPlane - 1) > 3 && (focusPlane - 1) <= 7) {
                                for (int i = 4; i <= 7; i++) {
                                    if ((focusPlane - 1) != i && plane[i].getStep() == currentStep) {
                                        plane[i].setOverlap(plane[i].getOverlap() + 1);
                                        count = plane[i].getOverlap();
                                    }
                                }
                                plane[focusPlane - 1].setOverlap(count);
                            } else if ((focusPlane - 1) > 7 && (focusPlane - 1) <= 11) {
                                for (int i = 8; i <= 11; i++) {
                                    if ((focusPlane - 1) != i && plane[i].getStep() == currentStep) {
                                        plane[i].setOverlap(plane[i].getOverlap() + 1);
                                        count = plane[i].getOverlap();
                                    }
                                }
                                plane[focusPlane - 1].setOverlap(count);
                            } else if ((focusPlane - 1) > 11 && (focusPlane - 1) <= 15) {
                                for (int i = 12; i <= 15; i++) {
                                    if ((focusPlane - 1) != i && plane[i].getStep() == currentStep) {
                                        plane[i].setOverlap(plane[i].getOverlap() + 1);
                                        count = plane[i].getOverlap();
                                    }
                                }
                                plane[focusPlane - 1].setOverlap(count);
                            }
                            changeDice();
                        }
                    }
                }

                @Override
                public void onAnimationCancel(Animator animator) {
                }

                @Override
                public void onAnimationRepeat(Animator animator) {
                }
            };
            stepAnimator.addListener(listener);
            stepAnimator.start();
            //transYAnimator.addListener(listener);
            //transAnimatorStart();
        }
    }

    public void setParameter(int p, int t) {
        playType = p;
        turn = t;

        switch (playType) {
            case Constants.REDvsBLUE:
                plane[4].setVisibility(View.INVISIBLE);
                plane[5].setVisibility(View.INVISIBLE);
                plane[6].setVisibility(View.INVISIBLE);
                plane[7].setVisibility(View.INVISIBLE);
                plane[12].setVisibility(View.INVISIBLE);
                plane[13].setVisibility(View.INVISIBLE);
                plane[14].setVisibility(View.INVISIBLE);
                plane[15].setVisibility(View.INVISIBLE);
                break;
            case Constants.YELLOWvsGREEN:
                plane[0].setVisibility(View.INVISIBLE);
                plane[1].setVisibility(View.INVISIBLE);
                plane[2].setVisibility(View.INVISIBLE);
                plane[3].setVisibility(View.INVISIBLE);
                plane[8].setVisibility(View.INVISIBLE);
                plane[9].setVisibility(View.INVISIBLE);
                plane[10].setVisibility(View.INVISIBLE);
                plane[11].setVisibility(View.INVISIBLE);
                break;
            case Constants.FOURPLAYER:
                break;
        }

        dice = new DiceView(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                diceNumber = dice.getNumber();
                if (diceNumber == 6) {
                    countSix = countSix + 1;
                } else {
                    countSix = 0;
                }
                if (countSix == 3) {
                    switch (turn) {
                        case Constants.RED:
                            plane[0].setClickable(false);
                            plane[1].setClickable(false);
                            plane[2].setClickable(false);
                            plane[3].setClickable(false);
                            if (playType != Constants.FOURPLAYER) {
                                turn = (turn + 2) % 4;
                            } else if (playType == Constants.FOURPLAYER && !isYellowDown) {
                                turn = (turn + 1) % 4;
                            } else if (playType == Constants.FOURPLAYER && isYellowDown && !isBlueDown) {
                                turn = (turn + 2) % 4;
                            } else if (playType == Constants.FOURPLAYER && isYellowDown && isBlueDown) {
                                turn = (turn + 3) % 4;
                            }
                            threeSix();
                            break;
                        case Constants.YELLOW:
                            plane[4].setClickable(false);
                            plane[5].setClickable(false);
                            plane[6].setClickable(false);
                            plane[7].setClickable(false);
                            if (playType != Constants.FOURPLAYER) {
                                turn = (turn + 2) % 4;
                            } else if (playType == Constants.FOURPLAYER && !isBlueDown) {
                                turn = (turn + 1) % 4;
                            } else if (playType == Constants.FOURPLAYER && isBlueDown && !isGreenDown) {
                                turn = (turn + 2) % 4;
                            } else if (playType == Constants.FOURPLAYER && isBlueDown && isGreenDown) {
                                turn = (turn + 3) % 4;
                            }
                            threeSix();
                            break;
                        case Constants.BLUE:
                            plane[8].setClickable(false);
                            plane[9].setClickable(false);
                            plane[10].setClickable(false);
                            plane[11].setClickable(false);
                            if (playType != Constants.FOURPLAYER) {
                                turn = (turn + 2) % 4;
                            } else if (playType == Constants.FOURPLAYER && !isGreenDown) {
                                turn = (turn + 1) % 4;
                            } else if (playType == Constants.FOURPLAYER && isGreenDown && !isRedDown) {
                                turn = (turn + 2) % 4;
                            } else if (playType == Constants.FOURPLAYER && isGreenDown && isRedDown) {
                                turn = (turn + 3) % 4;
                            }
                            threeSix();
                            break;
                        case Constants.GREEN:
                            plane[12].setClickable(false);
                            plane[13].setClickable(false);
                            plane[14].setClickable(false);
                            plane[15].setClickable(false);
                            if (playType != Constants.FOURPLAYER) {
                                turn = (turn + 2) % 4;
                            } else if (playType == Constants.FOURPLAYER && !isRedDown) {
                                turn = (turn + 1) % 4;
                            } else if (playType == Constants.FOURPLAYER && isRedDown && !isYellowDown) {
                                turn = (turn + 2) % 4;
                            } else if (playType == Constants.FOURPLAYER && isRedDown && isYellowDown) {
                                turn = (turn + 3) % 4;
                            }
                            threeSix();
                            break;
                    }
                } else {
                    switch (turn) {
                        case Constants.RED:
                            plane[0].setClickable((plane[0].getStep() != -1 || diceNumber % 2 == 0) && plane[0].getStep() != 57);
                            plane[1].setClickable((plane[1].getStep() != -1 || diceNumber % 2 == 0) && plane[1].getStep() != 57);
                            plane[2].setClickable((plane[2].getStep() != -1 || diceNumber % 2 == 0) && plane[2].getStep() != 57);
                            plane[3].setClickable((plane[3].getStep() != -1 || diceNumber % 2 == 0) && plane[3].getStep() != 57);
                            if (diceNumber != 6) {
                                if (playType != Constants.FOURPLAYER) {
                                    turn = (turn + 2) % 4;
                                } else if (playType == Constants.FOURPLAYER && !isYellowDown) {
                                    turn = (turn + 1) % 4;
                                } else if (playType == Constants.FOURPLAYER && isYellowDown && !isBlueDown) {
                                    turn = (turn + 2) % 4;
                                } else if (playType == Constants.FOURPLAYER && isYellowDown && isBlueDown) {
                                    turn = (turn + 3) % 4;
                                }
                            }
                            if (!plane[0].isClickable() && !plane[1].isClickable() && !plane[2].isClickable() && !plane[3].isClickable()) {
                                changeDice();
                            }
                            break;
                        case Constants.YELLOW:
                            plane[4].setClickable((plane[4].getStep() != -1 || diceNumber % 2 == 0) && plane[4].getStep() != 57);
                            plane[5].setClickable((plane[5].getStep() != -1 || diceNumber % 2 == 0) && plane[5].getStep() != 57);
                            plane[6].setClickable((plane[6].getStep() != -1 || diceNumber % 2 == 0) && plane[6].getStep() != 57);
                            plane[7].setClickable((plane[7].getStep() != -1 || diceNumber % 2 == 0) && plane[7].getStep() != 57);
                            if (diceNumber != 6) {
                                if (playType != Constants.FOURPLAYER) {
                                    turn = (turn + 2) % 4;
                                } else if (playType == Constants.FOURPLAYER && !isBlueDown) {
                                    turn = (turn + 1) % 4;
                                } else if (playType == Constants.FOURPLAYER && isBlueDown && !isGreenDown) {
                                    turn = (turn + 2) % 4;
                                } else if (playType == Constants.FOURPLAYER && isBlueDown && isGreenDown) {
                                    turn = (turn + 3) % 4;
                                }
                            }
                            if (!plane[4].isClickable() && !plane[5].isClickable() && !plane[6].isClickable() && !plane[7].isClickable()) {
                                changeDice();
                            }
                            break;
                        case Constants.BLUE:
                            plane[8].setClickable((plane[8].getStep() != -1 || diceNumber % 2 == 0) && plane[8].getStep() != 57);
                            plane[9].setClickable((plane[9].getStep() != -1 || diceNumber % 2 == 0) && plane[9].getStep() != 57);
                            plane[10].setClickable((plane[10].getStep() != -1 || diceNumber % 2 == 0) && plane[10].getStep() != 57);
                            plane[11].setClickable((plane[11].getStep() != -1 || diceNumber % 2 == 0) && plane[11].getStep() != 57);
                            if (diceNumber != 6) {
                                if (playType != Constants.FOURPLAYER) {
                                    turn = (turn + 2) % 4;
                                } else if (playType == Constants.FOURPLAYER && !isGreenDown) {
                                    turn = (turn + 1) % 4;
                                } else if (playType == Constants.FOURPLAYER && isGreenDown && !isRedDown) {
                                    turn = (turn + 2) % 4;
                                } else if (playType == Constants.FOURPLAYER && isGreenDown && isRedDown) {
                                    turn = (turn + 3) % 4;
                                }
                            }
                            if (!plane[8].isClickable() && !plane[9].isClickable() && !plane[10].isClickable() && !plane[11].isClickable()) {
                                changeDice();
                            }
                            break;
                        case Constants.GREEN:
                            plane[12].setClickable((plane[12].getStep() != -1 || diceNumber % 2 == 0) && plane[12].getStep() != 57);
                            plane[13].setClickable((plane[13].getStep() != -1 || diceNumber % 2 == 0) && plane[13].getStep() != 57);
                            plane[14].setClickable((plane[14].getStep() != -1 || diceNumber % 2 == 0) && plane[14].getStep() != 57);
                            plane[15].setClickable((plane[15].getStep() != -1 || diceNumber % 2 == 0) && plane[15].getStep() != 57);
                            if (diceNumber != 6) {
                                if (playType != Constants.FOURPLAYER) {
                                    turn = (turn + 2) % 4;
                                } else if (playType == Constants.FOURPLAYER && !isRedDown) {
                                    turn = (turn + 1) % 4;
                                } else if (playType == Constants.FOURPLAYER && isRedDown && !isYellowDown) {
                                    turn = (turn + 2) % 4;
                                } else if (playType == Constants.FOURPLAYER && isRedDown && isYellowDown) {
                                    turn = (turn + 3) % 4;
                                }
                            }
                            if (!plane[12].isClickable() && !plane[13].isClickable() && !plane[14].isClickable() && !plane[15].isClickable()) {
                                changeDice();
                            }
                            break;
                    }
                }
            }
        }, mContext);
        addView(dice);
        dice.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dice.setClickable(false);
                dice.rollDice();
            }
        });
    }

    public void savePref() {
        SharedPreferences prefs = mContext.getSharedPreferences(Constants.SHARED_PREFS_NAME,
                AppCompatActivity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        if (rankView.getVisibility() == INVISIBLE) {
            editor.putString(Constants.ISCACHED_PREF, "true");
        } else {
            editor.putString(Constants.ISCACHED_PREF, "false");
        }
        editor.putString(Constants.PLAYTYPE_PREF, String.valueOf(playType));
        editor.putString(Constants.TURN_PREF, String.valueOf(turn));
        editor.putString(Constants.RANK1_PREF, String.valueOf(rank1));
        editor.putString(Constants.RANK2_PREF, String.valueOf(rank2));
        editor.putString(Constants.RANK3_PREF, String.valueOf(rank3));
        if (isRedDown) {
            editor.putString(Constants.ISREDDOWN_PREF, "true");
        } else {
            editor.putString(Constants.ISREDDOWN_PREF, "false");
        }
        if (isYellowDown) {
            editor.putString(Constants.ISYELLOWDOWN_PREF, "true");
        } else {
            editor.putString(Constants.ISYELLOWDOWN_PREF, "false");
        }
        if (isBlueDown) {
            editor.putString(Constants.ISBLUEDOWN_PREF, "true");
        } else {
            editor.putString(Constants.ISBLUEDOWN_PREF, "false");
        }
        if (isGreenDown) {
            editor.putString(Constants.ISGREENDOWN_PREF, "true");
        } else {
            editor.putString(Constants.ISGREENDOWN_PREF, "false");
        }
        for (int i = 0; i <= 15; i++) {
            if (plane[i].isClickable()) {
                editor.putString("PLANE" + String.valueOf(i) + "C", "true");
            } else {
                editor.putString("PLANE" + String.valueOf(i) + "C", "false");
            }
            editor.putString("PLANE" + String.valueOf(i) + "S", String.valueOf(plane[i].getStep()));
            editor.putString("PLANE" + String.valueOf(i) + "X", String.valueOf(plane[i].getTranslationX()));
            editor.putString("PLANE" + String.valueOf(i) + "Y", String.valueOf(plane[i].getTranslationY()));
            editor.putString("PLANE" + String.valueOf(i) + "O", String.valueOf(plane[i].getOverlap()));
        }
        if (dice.isClickable()) {
            editor.putString(Constants.DICE_C_PREF, "true");
        } else {
            editor.putString(Constants.DICE_C_PREF, "false");
        }
        editor.putString(Constants.DICE_NUMBER_PREF, String.valueOf(dice.getNumber()));
        editor.putString(Constants.DICE_X_PREF, String.valueOf(dice.getTranslationX()));
        editor.putString(Constants.DICE_Y_PREF, String.valueOf(dice.getTranslationY()));
        editor.putString(Constants.COUNTSIX_PREF, String.valueOf(countSix));
        editor.putString(Constants.FIRSTSIX_PREF, String.valueOf(firstSix));
        editor.putString(Constants.SECONDSIX_PREF, String.valueOf(secondSix));
        editor.apply();
    }

    private void readPref() {
        SharedPreferences prefs = mContext.getSharedPreferences(Constants.SHARED_PREFS_NAME,
                AppCompatActivity.MODE_PRIVATE);// get the parameters from the Shared
        // read values from the shared preferences
        //player = Integer.parseInt(prefs.getString(HomeActivity.PLAYTYPE_PREF, String.valueOf(PlayView.FOURPLAYER)));
        //turn = Integer.parseInt(prefs.getString(HomeActivity.TURN_PREF, String.valueOf(PlayView.RED)));
        //setParameter(player, turn);
        // read values from the shared preferences
        playType = Integer.parseInt(prefs.getString(Constants.PLAYTYPE_PREF, String.valueOf(Constants.FOURPLAYER)));
        turn = Integer.parseInt(prefs.getString(Constants.TURN_PREF, String.valueOf(Constants.RED)));
        //setParameter(playType, turn);

        diceNumber = Integer.parseInt(prefs.getString(Constants.DICE_NUMBER_PREF, "0"));
        rank1 = Integer.parseInt(prefs.getString(Constants.RANK1_PREF, "-1"));
        rank2 = Integer.parseInt(prefs.getString(Constants.RANK2_PREF, "-1"));
        rank3 = Integer.parseInt(prefs.getString(Constants.RANK3_PREF, "-1"));
        isRedDown = prefs.getString(Constants.ISREDDOWN_PREF, "false").equals("true");
        isYellowDown = prefs.getString(Constants.ISYELLOWDOWN_PREF, "false").equals("true");
        isBlueDown = prefs.getString(Constants.ISBLUEDOWN_PREF, "false").equals("true");
        isBlueDown = prefs.getString(Constants.ISGREENDOWN_PREF, "false").equals("true");

        countSix = Integer.parseInt(prefs.getString(Constants.COUNTSIX_PREF, "0"));
        firstSix = Integer.parseInt(prefs.getString(Constants.FIRSTSIX_PREF, "0"));
        secondSix = Integer.parseInt(prefs.getString(Constants.SECONDSIX_PREF, "0"));
    }

    private void setPlaneDiceFromCache() {
        SharedPreferences prefs = mContext.getSharedPreferences(Constants.SHARED_PREFS_NAME,
                AppCompatActivity.MODE_PRIVATE);// get the parameters from the Shared
        for (int i = 0; i <= 15; i++) {
            int stepTemp = Integer.parseInt(prefs.getString("PLANE" + String.valueOf(i) + "S", "-1"));
            plane[i].setStep(stepTemp);
            plane[i].setTranslationX(Float.parseFloat(prefs.getString("PLANE" + String.valueOf(i) + "X", String.valueOf(plane[i].getPosX()))));
            plane[i].setTranslationY(Float.parseFloat(prefs.getString("PLANE" + String.valueOf(i) + "Y", String.valueOf(plane[i].getPosY()))));
            plane[i].setOverlap(Integer.parseInt(prefs.getString("PLANE" + String.valueOf(i) + "O", "1")));
            plane[i].setClickable(prefs.getString("PLANE" + String.valueOf(i) + "C", "false").equals("true"));
            if (stepTemp == 57 || stepTemp == -1) {
                plane[i].setAngle((i - (i % 4)) / 4 * 90 + 90);
                if (stepTemp == 57) {
                    plane[i].setDonw();
                }
            } else {
                plane[i].setNextStep(plane[i].getStep() + 1);
            }
        }
        dice.setClickable(prefs.getString(Constants.DICE_C_PREF, "false").equals("true"));
        dice.setNumber(Integer.parseInt(prefs.getString(Constants.DICE_NUMBER_PREF, "0")));
        //dice.setClickable(prefs.getString(HomeActivity.DICE_C_PREF, "false").equals("true"));
        dice.setTranslationX(Float.parseFloat(prefs.getString(Constants.DICE_X_PREF, "0")));
        dice.setTranslationY(Float.parseFloat(prefs.getString(Constants.DICE_Y_PREF, "0")));
        //dice.setNumber(Integer.parseInt(prefs.getString(HomeActivity.DICE_NUMBER_PREF, "0")));
    }

    public void setResume() {
        //isResume = true;
        SharedPreferences prefs = mContext.getSharedPreferences(Constants.SHARED_PREFS_NAME,
                AppCompatActivity.MODE_PRIVATE);// get the parameters from the Shared

    }

    private void showRank() {
        ObjectAnimator rankAnimator;
        rankAnimator = transXAnimator = ObjectAnimator.ofFloat(rankView, "translationY", 0, height);
        rankAnimator.setDuration(aniDuration);
        rankAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

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
        rankAnimator.start();
    }

    private void threeSix() {
        currentStep = plane[firstSix].getStep();
        plane[firstSix].setOverlap(1);
        setTransAnimator(firstSix, -1);
        AnimatorSet backHangerAnimatorSet = new AnimatorSet();
        backHangerAnimatorSet.playTogether(transXAnimator, transYAnimator);
        backHangerAnimatorSet.setDuration(aniDuration);
        backHangerAnimatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                /** change overlap */
                if (firstSix <= 3) {
                    for (int i = 0; i <= 3; i++) {
                        if (firstSix != i && plane[i].getStep() == currentStep) {
                            plane[i].setOverlap(plane[i].getOverlap() - 1);
                        }
                    }
                } else if (firstSix > 3 && firstSix <= 7) {
                    for (int i = 4; i <= 7; i++) {
                        if (firstSix != i && plane[i].getStep() == currentStep) {
                            plane[i].setOverlap(plane[i].getOverlap() - 1);
                        }
                    }
                } else if (firstSix > 7 && firstSix <= 11) {
                    for (int i = 8; i <= 11; i++) {
                        if (firstSix != i && plane[i].getStep() == currentStep) {
                            plane[i].setOverlap(plane[i].getOverlap() - 1);
                        }
                    }
                } else if (firstSix > 11 && firstSix <= 15) {
                    for (int i = 12; i <= 15; i++) {
                        if (firstSix != i && plane[i].getStep() == currentStep) {
                            plane[i].setOverlap(plane[i].getOverlap() - 1);
                        }
                    }
                }
                if (secondSix != firstSix) {
                    currentStep = plane[secondSix].getStep();
                    plane[secondSix].setOverlap(1);
                    setTransAnimator(secondSix, -1);
                    AnimatorSet backHangerAnimatorSet = new AnimatorSet();
                    backHangerAnimatorSet.playTogether(transXAnimator, transYAnimator);
                    backHangerAnimatorSet.setDuration(aniDuration);
                    backHangerAnimatorSet.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {
                            /** change overlap */
                            if (secondSix <= 3) {
                                for (int i = 0; i <= 3; i++) {
                                    if (secondSix != i && plane[i].getStep() == currentStep) {
                                        plane[i].setOverlap(plane[i].getOverlap() - 1);
                                    }
                                }
                            } else if (secondSix > 3 && secondSix <= 7) {
                                for (int i = 4; i <= 7; i++) {
                                    if (secondSix != i && plane[i].getStep() == currentStep) {
                                        plane[i].setOverlap(plane[i].getOverlap() - 1);
                                    }
                                }
                            } else if (secondSix > 7 && secondSix <= 11) {
                                for (int i = 8; i <= 11; i++) {
                                    if (secondSix != i && plane[i].getStep() == currentStep) {
                                        plane[i].setOverlap(plane[i].getOverlap() - 1);
                                    }
                                }
                            } else if (secondSix > 11 && secondSix <= 15) {
                                for (int i = 12; i <= 15; i++) {
                                    if (secondSix != i && plane[i].getStep() == currentStep) {
                                        plane[i].setOverlap(plane[i].getOverlap() - 1);
                                    }
                                }
                            }
                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            airPortDir();
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });
                    backHangerAnimatorSet.start();
                }
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                airPortDir();
                countSix = 0;
                changeDice();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        backHangerAnimatorSet.start();
    }
}
