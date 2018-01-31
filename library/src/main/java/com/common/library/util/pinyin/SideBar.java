package com.common.library.util.pinyin;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.common.library.R;

import java.util.ArrayList;
import java.util.List;


/**
 * 右侧的字母索引View
 *
 * @author
 */

public class SideBar extends View {

    //触摸事件
    private OnTouchingLetterChangedListener onTouchingLetterChangedListener;

    // 26个字母
    public static String[] b = {"A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z", "#"};

    public static List<String> firstLetterList = new ArrayList<String>();

    //选中
    private int choose = -1;

    private Paint paint = new Paint();

    private TextView mTextDialog;

    /**
     * 为SideBar显示字母的TextView
     *
     * @param mTextDialog
     */
    public void setTextView(TextView mTextDialog) {
        this.mTextDialog = mTextDialog;
    }


    public SideBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SideBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SideBar(Context context) {
        super(context);
    }

    /**
     * 重写的onDraw的方法
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int height = getHeight();//获取对应的高度
        int width = getWidth();//获取对应的宽度
        int singleHeight = height / b.length;//获取每一个字母的高度
        int headOffset = (b.length - firstLetterList.size()) / 2;
        if (firstLetterList.size() == 27) {
            for (int i = 0; i < b.length; i++) {
                paint.setColor(Color.parseColor("#1f1f1f"));
                paint.setTypeface(Typeface.DEFAULT_BOLD);
                paint.setAntiAlias(true);
                paint.setTextSize(24);
                //选中的状态
                if (i == choose) {
                    paint.setColor(Color.parseColor("#03a9f4"));
                    paint.setFakeBoldText(true);//设置是否为粗体文字
                }
                //x坐标等于=中间-字符串宽度的一般
                float xPos = width / 2 - paint.measureText(b[i]) / 2;
                float yPos = singleHeight * i + singleHeight;
                canvas.drawText(b[i], xPos, yPos, paint);
                paint.reset();//重置画笔
            }
        } else {
            for (int i = 0; i < firstLetterList.size(); i++) {
                paint.setColor(Color.parseColor("#1f1f1f"));
                paint.setTypeface(Typeface.DEFAULT_BOLD);
                paint.setAntiAlias(true);
                paint.setTextSize(24);
                //选中的状态
                if (i + headOffset + 1 == choose) {
                    paint.setColor(Color.parseColor("#3399ff"));
                    paint.setFakeBoldText(true);//设置是否为粗体文字
                }
                //x坐标等于=中间-字符串宽度的一般
                float xPos = width / 2 - paint.measureText(firstLetterList.get(i)) / 2;
                float yPos = singleHeight * (i + headOffset) + singleHeight;
                canvas.drawText(firstLetterList.get(i), xPos, yPos, paint);
                paint.reset();//重置画笔
            }
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        int action = event.getAction();
        float y = event.getY();//点击y坐标
        int oldChoose = choose;
        boolean isAll = b.length == firstLetterList.size();
        OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
        int headOffset = (b.length - firstLetterList.size()) / 2;
        int c = (int) (y / getHeight() * b.length);//点击y坐标所占高度的比例*b数组的长度就等于点击b中的个数
        switch (action) {
            case MotionEvent.ACTION_UP:
                setBackgroundDrawable(new ColorDrawable(0x00000000));//设置背景颜色
                choose = -1;
                invalidate();
                if (mTextDialog != null) {
                    mTextDialog.setVisibility(View.INVISIBLE);
                }
                break;

            default:
                setBackgroundResource(R.drawable.sidebar_background);
                if (oldChoose != c) {
                    if (isAll) {
                        if (c >= 0 && c < b.length) {
                            if (listener != null) {
                                listener.onTouchingLetterChanged(b[c]);
                            }
                            if (mTextDialog != null) {
                                mTextDialog.setText(b[c]);
                                mTextDialog.setVisibility(View.VISIBLE);
                            }
                            choose = c;
                            invalidate();
                        }
                    } else {
                        if (c >= headOffset + 1 && c < headOffset + firstLetterList.size() + 1) {
                            if (listener != null) {
                                listener.onTouchingLetterChanged(firstLetterList.get(c - headOffset - 1));
                            }
                            if (mTextDialog != null) {
                                mTextDialog.setText(firstLetterList.get(c - headOffset - 1));
                                mTextDialog.setVisibility(View.VISIBLE);
                            }
                            choose = c;
                            invalidate();
                        }
                    }

                }
                break;
        }
        return true;
    }

    /**
     * 向外松开的方法
     *
     * @param onTouchingLetterChangedListener
     */
    public void setOnTouchingLetterChangedListener(
            OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
        this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
    }

    /**
     * 接口
     *
     * @author
     */
    public interface OnTouchingLetterChangedListener {
        void onTouchingLetterChanged(String s);
    }

}
