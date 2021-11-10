package com.cyh.dancenumberview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * @author cyh at 2020/02/19
 * 滚动数字的单个view
 */
public class SingleDanceView extends View {
    //当前显示的文字
    private String mText = "0";
    //文字最终显示
    private String mtargerText = "";
    private int mTextColor = Color.BLACK;
    private int mTextSize = 1;
    private int textPaddingHorizontal = 0;
    private int textPaddingVertical = 0;
    private int backgroundRes = 0;

    private Paint mPaint;
    private Context mContext;
    private static String BASE_NUMBER = "0123456789";
    //总时间
    private int total_seconds = 15;
    //文字高
    private int textOutHeight;
    //文字宽
    private int textOutWidth;
    private int textInnerWidth;
    private int scrollY = 0;
    private int scale = 1;

    public SingleDanceView(Context context) {
        this(context, null);
    }

    public SingleDanceView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SingleDanceView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint();
        //设置抗锯齿
        mPaint.setAntiAlias(true);
        //设置字体和颜色
        mPaint.setTextSize(mTextSize * scale);
        //设置样式
//        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/din_alternate.otf");
//        mPaint.setTypeface(typeface);
        mPaint.setColor(mTextColor);
//        setPadding(0, 0, 0, 0);

//        setBackgroundResource(R.drawable.magic_square_bg_corner_scrollnumber);
    }

    public SingleDanceView setText(String text) {
        this.mText = text;
        return this;
    }

    public SingleDanceView setTextColor(int textColor) {
        this.mTextColor = textColor;
        mPaint.setColor(mTextColor);
        return this;
    }

    public SingleDanceView setTextSize(int textSize) {
        this.mTextSize = textSize;
        mPaint.setTextSize(mTextSize * scale);
        return this;
    }


    public SingleDanceView setTextPaddingHorizontal(int textPaddingHorizontal) {
        this.textPaddingHorizontal = textPaddingHorizontal;
        return this;
    }


    public SingleDanceView setTextPaddingVertical(int textPaddingVertical) {
        this.textPaddingVertical = textPaddingVertical;
        return this;
    }
    public SingleDanceView setBackGroundRes(int backGroundRes)
    {
        this.backgroundRes = backGroundRes;
        setBackgroundResource(backgroundRes);
        return this;
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode == MeasureSpec.AT_MOST) {
            Rect bounds = new Rect();
            mPaint.getTextBounds("0", 0, "0".length(), bounds);
            textOutWidth = bounds.width();
            width = textOutWidth +textPaddingHorizontal*2;

        }
        if (heightMode == MeasureSpec.AT_MOST) {
            Rect bounds = new Rect();
            mPaint.getTextBounds("0", 0, "0".length(), bounds);
            textOutHeight = bounds.height();
            height = textOutHeight + textPaddingVertical*2;
        }
        //设置宽高
        setMeasuredDimension(width,height);
//        textOutHeight = height * 2;
        textOutHeight = height;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (TextUtils.isEmpty(mText)) return;
        if (mText.length() > 1) return;
        if (!BASE_NUMBER.contains(mText)) return;
        //保持垂直方向居中
        //getPaddingLeft() + (textOutWidth - textInnerWidth) / 2 保持水平方向居中
        mPaint.setTextSize(mTextSize);
        Paint.FontMetricsInt fontMetrics = mPaint.getFontMetricsInt();
        int dy = (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;
        int baseLine = getHeight() / 2 + dy;
        mPaint.setTextAlign(Paint.Align.CENTER);
        int length = getCurrentToTargerLength(Integer.parseInt(mText), Integer.parseInt(mtargerText));
        for (int i = Integer.valueOf(mText); i <= Integer.parseInt(mText) + length; i++) {
            mPaint.setTextSize(mTextSize);
            Rect innerBounds = new Rect();
            mPaint.getTextBounds(mText, 0, mText.length(), innerBounds);
            textInnerWidth = innerBounds.width();
            String drawNum = String.valueOf(i);
            if (i > 9) {
                drawNum = String.valueOf(i - 10);
            }
            canvas.drawText(drawNum, getWidth() / 2, baseLine + (i - Integer.valueOf(mText)) * textOutHeight - scrollY, mPaint);
        }
    }


    private void animateView(int step) {
        this.postDelayed(new Runnable() {
            @Override
            public void run() {
                int length = 0;
                try {
                    length = getCurrentToTargerLength(Integer.parseInt(mText), Integer.parseInt(mtargerText));

                } catch (Exception e) {
                }
                if (scrollY >= textOutHeight * length) {
                    scrollY = textOutHeight * length;
                    SingleDanceView.this.postInvalidate();
                    return;
                }
                //设置一个缓速
                if (scrollY > textOutHeight * (length - 1)) {
                    scrollY += textOutHeight / 10;
                } else {
                    scrollY += textOutHeight / 4;
                }

                SingleDanceView.this.postInvalidate();
                animateView(step);
            }
        }, step);
    }

    public void start() {
        scrollY = 0;
        if (!StringIsNumber(mText)) {
            mText = "0";
        }

        int length = getCurrentToTargerLength(Integer.parseInt(mText), Integer.parseInt(mtargerText));
        length = length == 0 ? 1 : length;
        animateView(total_seconds / length);
    }

    private boolean StringIsNumber(String str) {
        for (int i = str.length(); --i >= 0; ) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public String getTargerText() {
        return mtargerText;
    }

    public void setTargerText(String targerText) {
        this.mtargerText = targerText;
    }

    //获取当前数字与目标数字相差多少
    private int getCurrentToTargerLength(int current, int target) {


        if (target >= current) {
            return target - current;
        } else {
            return target + 10 - current;
        }

    }
}
