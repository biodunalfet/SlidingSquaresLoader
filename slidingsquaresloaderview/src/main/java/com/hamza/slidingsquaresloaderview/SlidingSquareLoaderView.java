package com.hamza.slidingsquaresloaderview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.FrameLayout;

import java.util.Random;

/**
 * Created by Hamza Fetuga on 2/4/2017.
 */

public class SlidingSquareLoaderView extends FrameLayout {

    private final String TAG = "SSLV";
    /**
     * Direction trackers for the view
     * By default, it starts from the top right
     */
    boolean moveRight = false;
    boolean moveUp = false;

    /**
     * All possible XY positions of squares
     */
    Point[][] squareCoordinates;

    /**
     * Square to move
     */
    Square mover;

    /**
     * All squares and their XY positions
     */
    Square[][] squares;

    /**
     * The current column and row number of the moving square
     */
    int colNumber = 3;
    int rowNumber = 0;

    /**
     * Duration of translation of each square
     */
    int duration = 350;

    /**
     * Delay period before each translation
     */
    int delay = 25;

    /**
     * Default color of squares
     */
    int default_color;

    /**
     * Flag to track whether to start animtion or not
     */
    private boolean start;

    /**
     * Coordinates of center of view
     */
    private Point center;

    /**
     * Dimensions of squares
     */
    private int margin = 10; // margin between squares
    private int height = 50; // height of squares
    private int width = 50; // width of square;

    /**
     * Flag that stores the state of the view
     */
    private boolean isAnimating = false;

    /**
     * Flag to determine whether to redraw on requestLayout() invocation
     */
    private boolean redraw = false;

    public SlidingSquareLoaderView(Context context) {
        this(context, null);
    }

    public SlidingSquareLoaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingSquareLoaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.SlidingSquareLoaderView);
        start = typedArray.getBoolean(R.styleable.SlidingSquareLoaderView_sslv_start, true);
        default_color = typedArray.getColor(R.styleable.SlidingSquareLoaderView_sslv_color, getResources().getColor(R.color.sslv_color));

        int sslv_duration = typedArray.getInteger(R.styleable.SlidingSquareLoaderView_sslv_duration, duration);
        int sslv_delay = typedArray.getInteger(R.styleable.SlidingSquareLoaderView_sslv_duration, delay);
        int dimen = (int) typedArray
                .getDimension(R.styleable.SlidingSquareLoaderView_sslv_square_length, height);
        int gap = (int) typedArray
                .getDimension(R.styleable.SlidingSquareLoaderView_sslv_gap, margin);

        setDuration(sslv_duration);
        setDelay(sslv_delay);
        setSquareDimen(dimen);
        setSquareGap(gap);

        typedArray.recycle();

        if (start){
            start();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        Log.d(TAG, "onMeasure: ");

        setMeasuredDimension(calculateWidth(widthMeasureSpec),
                calculateHeight(heightMeasureSpec));
    }

    private int calculateHeight(int measureSpec) {
        int preferred = (3 * height) + (margin * 2);
        return getMeasurement(preferred, measureSpec);
    }

    private int calculateWidth(int measureSpec) {
        int preferred = (4 * width) + (3 * margin);
        return getMeasurement(preferred, measureSpec);
    }

    private int getMeasurement(int preferred, int measureSpec) {

        int specSize = MeasureSpec.getSize(measureSpec);
        int measurement;

        switch (MeasureSpec.getMode(measureSpec)) {
            case MeasureSpec.EXACTLY:
                // This means the dimension of this view has been given.
                measurement = specSize;
                break;
            case MeasureSpec.AT_MOST:
                // Take the minimum of the preferred size and what
                // we were told to be.
                measurement = Math.min(preferred, specSize);
                break;
            default:
                measurement = preferred;
                break;
        }

        return measurement;

    }

    private void drawGrid() {

        squares = new Square[3][4];
        Context context = getContext();
        for (int j = 0; j < 4; j++) {
            addSquare(1, j, context);
        }

        mover = addSquare(0, 3, context);

//        for (int i = 0; i < 3; i++){
//            for (int j = 0; j < 4; j++){
//                addSquare(i, j, context);
//            }
//        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

        Log.d(TAG, "onLayout: " + getChildCount());

        if (changed || redraw) {

            redraw = false;
            getOrigin();
            calculateSquareCoordinates();
            drawGrid();

            int moverTag = 4 * colNumber + rowNumber;

            for (int i = 0; i < getChildCount(); i++) {

                View child = getChildAt(i);
                Log.d(TAG, "onLayout: " + child.getTag());
                int tag = (int) child.getTag();


                int row = tag / 4;
                int col = tag % 4;
                int l = squareCoordinates[row][col].x;
                int t = squareCoordinates[row][col].y;
                int r = l + width;

                squares[row][col] = (Square) child;

                int b = t + height;

                if (4 * row + col == moverTag) {
                    mover = (Square) child;
                }

                child.layout(l, t, r, b);
                Log.d(TAG, "onLayout: L" + l);
                Log.d(TAG, "onLayout: T" + t);
                Log.d(TAG, "onLayout: R" + r);
                Log.d(TAG, "onLayout: B" + b);

            }

            if (start) {
                isAnimating = true;
                moveHoriz();
            }

        }

    }

    private Square addSquare(int row, int col, Context c) {

        Square currentSquare = new Square(c);
        Point point = squareCoordinates[row][col];
        LayoutParams params = new FrameLayout.LayoutParams(width, height);
        currentSquare.setLayoutParams(params);
//        currentSquare.setX(point.x);
//        currentSquare.setY(point.y);
        currentSquare.setTag(4 * row + col);
        currentSquare.setColor(default_color);


        addView(currentSquare, width,
                height);
        return currentSquare;
    }

    private void calculateSquareCoordinates() {

        int x = center.x;
        int y = center.y;
        int p = margin;
        int w = width;
        int h = height;

        int ex = x - p / 2 - 2 * w - p;
        int ey = y - h / 2;
        int fx = x - p / 2 - w;
        int fy = ey;
        int gx = x + p / 2;
        int gy = ey;
        int hx = x + p / 2 + w + p;
        int hy = ey;

        int ax = ex;
        int ay = y - h / 2 - p - h;
        int bx = fx;
        int by = ay;
        int cx = gx;
        int cy = ay;
        int dx = hx;
        int dy = ay;

        int ix = ex;
        int iy = y + h / 2 + p;
        int jx = fx;
        int jy = iy;
        int kx = gx;
        int ky = iy;
        int lx = hx;
        int ly = iy;

        squareCoordinates = new Point[][]{
                {new Point(ax, ay), new Point(bx, by), new Point(cx, cy), new Point(dx, dy)},
                {new Point(ex, ey), new Point(fx, fy), new Point(gx, gy), new Point(hx, hy)},
                {new Point(ix, iy), new Point(jx, jy), new Point(kx, ky), new Point(lx, ly)}
        };


    }

    private void getOrigin() {
        center = new Point(getMeasuredWidth() / 2, getMeasuredHeight() / 2);
    }

    private int generateRandomColor(){
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }

    public void stop() {

        if (isAnimating) {

            for (int i = 0; i < getChildCount(); i++) {
                getChildAt(i).animate().cancel();
            }
        }

        start = false;
        redraw = false;
        colNumber = 3;
        rowNumber = 0;
        isAnimating = false;
        moveRight = false;
        moveUp = false;

    }

    public boolean isAnimating() {
        return isAnimating;
    }

    public void setColor(int color){
        default_color = color;
    }

    public void start() {

        start = true;
        removeAllViews();
        redraw = true;
        requestLayout();

    }

    public void setDuration(int duration){
        this.duration = Math.max(0, duration);
    }

    public void setDelay(int delay){
        this.delay = Math.max(0, delay);
    }

    public int getDelay(){
        return delay;
    }

    public int getSpeed(){
        return duration;
    }

    public void setSquareDimen(int dimen){
        dimen = Math.max(0, dimen);
        width = dimen;
        height = dimen;
    }

    public void setSquareGap(int gap){
        gap = Math.max(0, gap);
        margin = gap;
    }

    private void moveVert() {

        if (moveUp) {
//            Log.d(TAG, "moveVert: up");
            ViewPropertyAnimator animatorMover =
                    mover.animate()
                            .y(squareCoordinates[1][colNumber].y)
                            .setDuration(duration)
                            .setStartDelay(delay);

            final Square middle = squares[1][colNumber];

            final ViewPropertyAnimator animatorOther =
                    middle.animate()
                            .y(squareCoordinates[0][colNumber].y)
                            .setDuration(duration)
                            .setStartDelay(delay);

            animatorOther.setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    rowNumber = 0;

                    squares[0][colNumber] = middle;
                    squares[1][colNumber] = mover;
                    squares[2][colNumber] = null;
                    animatorOther.setListener(null);
                    mover = squares[0][colNumber];
                    moveUp = false;
                    if (isAnimating){moveHoriz();}
//                    moveHoriz();
                }
            });
        } else {

//            Log.d(TAG, "moveVert: down");

            final Square middle = squares[1][colNumber];

            final ViewPropertyAnimator animatorOther =
                    middle.animate()
                            .y(squareCoordinates[2][colNumber].y)
                            .setDuration(duration)
                            .setStartDelay(delay);


            ViewPropertyAnimator animatorMover =
                    mover.animate()
                            .y(squareCoordinates[1][colNumber].y)
                            .setDuration(duration)
                            .setStartDelay(delay);

            animatorOther.setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    rowNumber = 2;

                    squares[1][colNumber] = mover;
                    squares[0][colNumber] = null;
                    squares[2][colNumber] = middle;
                    animatorOther.setListener(null);
                    mover = squares[2][colNumber];
                    moveUp = true;
                    if (isAnimating){moveHoriz();}
//                    moveHoriz();
                }
            });

        }

    }

    private void moveHoriz() {

        if (colNumber == 3) {
            moveRight = false;
        }

        if (colNumber == 0) {
            moveRight = true;
        }

        if (moveRight) {

//            Log.d(TAG, "moveHoriz: right");

            final ViewPropertyAnimator animator =
                    mover.animate()
                            .x(squares[1][colNumber + 1].getX())
                            .setDuration(duration)
                            .setStartDelay(delay);


            animator.setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    int preCol = colNumber;
                    colNumber = Math.min(3, colNumber + 1);
                    squares[rowNumber][colNumber] = mover;
                    squares[rowNumber][preCol] = null;
                    animator.setListener(null);
                    if (isAnimating){moveVert();}
//                    moveVert();
                }
            });

        } else {
//            Log.d(TAG, "moveHoriz: left");
            final ViewPropertyAnimator animator =
                    mover.animate()
                            .x(squares[1][colNumber - 1].getX())
                            .setDuration(duration)
                            .setStartDelay(delay);


            animator.setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
//                    mover.setX(squares[1][colNumber - 1].getX());
                    int preCol = colNumber;
                    colNumber = Math.max(0, colNumber - 1);
                    squares[rowNumber][colNumber] = mover;
                    squares[rowNumber][preCol] = null;
                    animator.setListener(null);
                    if (isAnimating){moveVert();}
//                    moveVert();
                }
            });
        }
    }

}
