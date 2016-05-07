package com.example.administrator.gobang;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * User: Administrator
 * Time: 2016/5/6 21:00 00
 * Annotation:
 */
public class GobangPanel extends View {

    private int mPanelWidth;
    private float mLineHeight;
    private int MAX_LINE = 10;

    public boolean mIsGameOver;
    public boolean mIsWhiteWinner;

    private Paint mPaint = new Paint ();

    private Bitmap mWhitePiece;
    private Bitmap mBlackPiece;
    private float radioPieceOfLineHeight = 3 * 1.0f / 4;//控制棋子的大小的比例

    private boolean mIsWhite = true;//判定黑白棋那一个该下，白棋先手
    private ArrayList<Point> mWhiteArray = new ArrayList<> ();//储存白棋坐标
    private ArrayList<Point> mBlackArray = new ArrayList<> ();//储存黑棋坐标

    private OnGameOverListener mOnGameOverListener;

    public void setOnGameOverListener(OnGameOverListener mOnGameOverListener)
    {
        this.mOnGameOverListener = mOnGameOverListener;
    }

    public GobangPanel (Context context, AttributeSet attrs) {
        super (context, attrs);
        init ();
    }

    public void init () {
        mPaint.setColor (0x88000000);//灰色，半透明
        mPaint.setAntiAlias (true);//抗锯齿
        mPaint.setDither (true);//??
        mPaint.setStyle (Paint.Style.STROKE);//画线

        mWhitePiece = BitmapFactory.decodeResource (getResources (), R.drawable.stone_w2);
        mBlackPiece = BitmapFactory.decodeResource (getResources (), R.drawable.stone_b1);
    }


    //捕获用户手势
    @Override
    public boolean onTouchEvent (MotionEvent event) {
        if (mIsGameOver) {
            return false;
        }
        int action = event.getAction ();
        if(action == MotionEvent.ACTION_UP) {
            int x = (int) event.getX ();
            int y = (int) event.getY ();

            Point p = getValidPoint(x, y);
            //判断当前点是否已经有棋子了
            if (mWhiteArray.contains (p) || mBlackArray.contains (p)) {
                return false;
            }
            if(mIsWhite) {
                mWhiteArray.add (p);
            }else {
                mBlackArray.add (p);
            }

            invalidate ();//请求重绘，
            mIsWhite = !mIsWhite;
        }
        return true;
    }

    //让棋子落在精确的坐标上
    public Point getValidPoint (int x, int y) {

        return new Point ((int)(x / mLineHeight), (int)(y / mLineHeight));
    }

    @Override
    protected void onMeasure (int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode (widthMeasureSpec);

        int heightSize = MeasureSpec.getSize (heightMeasureSpec);
        int heightMode = MeasureSpec.getMode (heightMeasureSpec);

        int width = Math.min (widthSize, heightSize);

        //当width等于0时，为了防止视图不显示，所以改变最小值
        if(widthMode == MeasureSpec.UNSPECIFIED) {
            width = heightSize;
        }else if (heightMode == MeasureSpec.UNSPECIFIED){
            width = widthSize;
        }

        setMeasuredDimension (width, width);
    }

    //当宽高确定后，值被改变时，调用
    //关于尺寸的编写
    @Override
    protected void onSizeChanged (int w, int h, int oldw, int oldh) {
        super.onSizeChanged (w, h, oldw, oldh);

        mPanelWidth = w;
        mLineHeight = mPanelWidth * 1.0f / MAX_LINE;

        int pieceWidth = (int) (mLineHeight * radioPieceOfLineHeight);
        mWhitePiece = Bitmap.createScaledBitmap (mWhitePiece, pieceWidth, pieceWidth, false);
        mBlackPiece = Bitmap.createScaledBitmap (mBlackPiece, pieceWidth, pieceWidth, false);
    }

    //绘制棋盘
    @Override
    protected void onDraw (Canvas canvas) {
        super.onDraw (canvas);

        drawBoard (canvas);

        drawPieces (canvas);

        checkGameOver ();

        checkTie();
    }

    private void checkTie () {
        if(mBlackArray.size () + mWhiteArray.size () >= MAX_LINE * MAX_LINE) {
            if (mOnGameOverListener != null) {
                mOnGameOverListener.onGameTie ();
            }
        }

    }

    private  void checkGameOver () {

        boolean whiteWin = GobangJudge.checkFiveInLine (mWhiteArray);
        boolean blackWin = GobangJudge.checkFiveInLine (mBlackArray);

        if(whiteWin || blackWin) {
            mIsGameOver = true;
            mIsWhiteWinner = whiteWin;


            if(mOnGameOverListener != null) {
                mOnGameOverListener.onGameWin (mIsWhiteWinner);
            }
            String text = mIsWhiteWinner ? "白棋胜" : "黑棋胜" ;
        }
    }

    public void drawPieces (Canvas canvas) {
        for(int i = 0, n = mWhiteArray.size (); i < n; i++) {
            Point whitePoint = mWhiteArray.get (i);
            canvas.drawBitmap (mWhitePiece,
                    (whitePoint.x + (1 - radioPieceOfLineHeight) / 2) * mLineHeight,
                    (whitePoint.y + (1 - radioPieceOfLineHeight) / 2) * mLineHeight,
                    null);
        }
        for(int i = 0, n = mBlackArray.size (); i < n; i++) {
            Point blackPoint = mBlackArray.get (i);
            canvas.drawBitmap (mBlackPiece,
                    (blackPoint.x + (1 - radioPieceOfLineHeight) / 2) * mLineHeight,
                    (blackPoint.y + (1 - radioPieceOfLineHeight) / 2) * mLineHeight,
                    null);
        }
    }

    public void drawBoard (Canvas canvas) {
        int w = mPanelWidth;
        float lineHeight = mLineHeight;

        for(int i = 0; i < MAX_LINE; i++) {
            //画棋盘的横线
            int startX = (int)(lineHeight / 2);
            int endX = (int)(w - lineHeight / 2);
            int y = (int) ((0.5 + i) * lineHeight);
            canvas.drawLine (startX, y, endX, y, mPaint);

            //画棋盘的纵线
            int startY = (int)(lineHeight / 2);
            int endY = (int)(w - lineHeight / 2);
            int x = (int) ((0.5 + i) * lineHeight);
            canvas.drawLine (x, startY, x, endY, mPaint);
        }
    }

    public void restart() {
        mWhiteArray.clear ();
        mBlackArray.clear ();
        mIsGameOver = false;
        mIsWhiteWinner = false;
        invalidate ();
    }

    public static final String INSTANCE = "instance";
    public static final String INSTANCE_GAME_OVER = "instance_game_over";
    public static final String INSTANCE_WHITE_ARRAY = "instance_white_array";
    public static final String INSTANCE_BLACK_ARRAY = "instance_black_array";


    @Override
    protected Parcelable onSaveInstanceState () {
        Bundle bundle = new Bundle ();
        bundle.putParcelable (INSTANCE, super.onSaveInstanceState ());
        bundle.putBoolean (INSTANCE_GAME_OVER, mIsGameOver);
        bundle.putParcelableArrayList (INSTANCE_WHITE_ARRAY, mWhiteArray);
        bundle.putParcelableArrayList (INSTANCE_BLACK_ARRAY, mBlackArray);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState (Parcelable state) {

        if(state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            mIsGameOver = bundle.getBoolean (INSTANCE_GAME_OVER);
            mWhiteArray = bundle.getParcelableArrayList (INSTANCE_WHITE_ARRAY);
            mBlackArray = bundle.getParcelableArrayList (INSTANCE_BLACK_ARRAY);
            super.onRestoreInstanceState (bundle.getParcelable (INSTANCE));
            return ;
        }
        super.onRestoreInstanceState (state);
    }

    /**
     * 悔棋功能
     */
    public void regret()
    {
        if(mIsWhiteWinner)
        {
            if(mBlackArray.size() > 0)
            {
                mBlackArray.remove(mBlackArray.size() - 1);
                mIsWhiteWinner = !mIsWhiteWinner;
            }
        }
        else
        {
            if(mWhiteArray.size() > 0)
            {
                mWhiteArray.remove(mWhiteArray.size() - 1);
                mIsWhiteWinner = !mIsWhiteWinner;
            }
        }
        invalidate ();
    }

}
