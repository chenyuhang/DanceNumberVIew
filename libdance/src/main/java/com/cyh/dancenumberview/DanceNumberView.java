package com.cyh.dancenumberview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.cyh.lib.dancenumberview.R;

import java.util.ArrayList;
import java.util.List;

public class DanceNumberView extends LinearLayout {

    private String text="";
    private int textColor;
    private int textSize;
    private int textPaddingHorizontal;  //用于计算单个文字的宽高
    private int textPaddingVertical;  //用于计算单个文字的宽高
    private int subViewPadding;//每个文字之间的空隙
    private String separationString;//分隔符
    private int separationSize;//分隔符大小
    private int separationColor;//分隔符颜色
    private int textBackground;//单个文字的背景
    private int scrollType = Constants.SCROLLTYPE_FROMZEOR;//滚动方式
    char[] chars_old;
    private List<SingleDanceView> list = new ArrayList<>();


    public DanceNumberView(Context context) {
        this(context, null);
    }

    public DanceNumberView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DanceNumberView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.scrollNumber);
        initAttributesData(typedArray);
        this.setOrientation(HORIZONTAL);
        this.setGravity(Gravity.CENTER);
    }

    /*
     *
     * 根据当前的text进行展示
     * */
    public void createView() {
        if (TextUtils.isEmpty(text)) return;
        char[] chars = text.toCharArray();
        list.clear();
        removeAllViews();
        int chars_old_index = -1;
        if (chars_old != null) {
            chars_old_index = chars_old.length - 1;
        }
        //倒序插入保证了数据对齐
        for (int i = chars.length - 1; i >= 0; i--) {
            //判断是否是数字
            if (charIsNumber(chars[i])) {
                SingleDanceView scrollNumber = new SingleDanceView(getContext()).setBackGroundRes(textBackground);
                scrollNumber.setTextPaddingHorizontal(textPaddingHorizontal)
                        .setTextPaddingVertical(textPaddingVertical).setTextColor(textColor).setTextSize(textSize)
                        .setTargerText(String.valueOf(chars[i]));
                if (chars_old != null && chars_old_index >= 0) {
                    scrollNumber.setText(String.valueOf(chars_old[chars_old_index]));
                    chars_old_index--;
                } else {
                    if(scrollType == Constants.SCROLLTYPE_FROMCURRENT)
                    {
                        scrollNumber.setText(String.valueOf(chars[i]));
                    }
                    else
                    {
                        scrollNumber.setText("0");
                    }
                 }
                addView(scrollNumber, 0);
                list.add(scrollNumber);
            } else {
                if (separationString != null && !separationString.equals("")) {
                    TextView tv = new TextView(getContext());
                    tv.setText(separationString);
                    tv.setTextSize(TypedValue.COMPLEX_UNIT_PX,separationSize);
                    tv.setTextColor(separationColor);
//                    //逗号
//                    if (chars[i] == 44) {
//                        tv.setText(",");
//                    }
//                    //小数点
//                    if (chars[i] == 46) {
//                        tv.setText(".");
//                    }
                    addView(tv, 0);
                }
            }
            chars_old_index--;
        }
        if (list != null && list.size() > 0) {
            for (int i = 0; i < getChildCount(); i++) {
                LayoutParams p = (LinearLayout.LayoutParams) getChildAt(i).getLayoutParams();
                p.leftMargin = subViewPadding;
                getChildAt(i).setLayoutParams(p);
            }
        }

    }

    public void start() {
        if (list == null || list.size() == 0) return;
        for (SingleDanceView view : list) {
            view.start();
        }
    }

    private void initAttributesData(TypedArray typedArray) {
        textColor = typedArray.getColor(R.styleable.scrollNumber_textColor, Color.BLACK);
        textSize = (int) typedArray.getDimension(R.styleable.scrollNumber_textSize, Constants.DEFAULT_TEXTSIZE);
        text = typedArray.getString(R.styleable.scrollNumber_text);
        textPaddingHorizontal = (int) typedArray.getDimension(R.styleable.scrollNumber_textPaddingHorizontal, Constants.DEAFULT_TEXTPADDING_HORIZONTAL);
        textPaddingVertical = (int) typedArray.getDimension(R.styleable.scrollNumber_textPaddingVertical, Constants.DEAFULT_TEXTPADDING_VERTICAL);
        subViewPadding = (int) typedArray.getDimension(R.styleable.scrollNumber_subViewPadding, Constants.DEAFULT_TEXTPADDING_VERTICAL);
        separationString = typedArray.getString(R.styleable.scrollNumber_separationString);
        separationSize = (int) typedArray.getDimension(R.styleable.scrollNumber_separationSize, Constants.DEFAULT_TEXTSIZE);
        separationColor = typedArray.getColor(R.styleable.scrollNumber_separationColor, Color.BLACK);
        textBackground =  typedArray.getResourceId(R.styleable.scrollNumber_subTextBackground, android.R.color.transparent);
        scrollType=typedArray.getInt(R.styleable.scrollNumber_scrollType, Constants.SCROLLTYPE_FROMZEOR);
    }

    public void setText(String text) {
        if(this.text!= null)
        {
            chars_old = this.text.toCharArray();
        }
        this.text = text;
        createView();
    }

    public String getText() {
        return text;
    }

    /*
     *
     * 判断下是否是数字范围来确定下滚动次数
     *
     * */
    private boolean charIsNumber(char a) {
        if (a <= 57 && a >= 48) {
            return true;
        }
        return false;
    }


}
