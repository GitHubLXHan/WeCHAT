package com.example.hany.wechat;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
/**
 * @author 6小h
 * @e-mail 1026310040@qq.com
 * @date 2018/11/10 16:27
 * @filName EditTextView
 * @describe ...
 */
public class EditTextView extends LinearLayout{

    private TextView mTxt;
    private EditText mEdt;
    private Paint mPaint = new Paint();
    private int mLineColor;
//    Drawable drawableRight;

    public EditTextView(Context context) {
        super(context);
    }

    public EditTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOrientation(HORIZONTAL);
        setWillNotDraw(false); // 语序onDraw()函数运行
        LayoutInflater.from(context).inflate(R.layout.view_edt, this, true);

        initView();
        setAttrs(context, attrs);


//        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(972, 150);
//        if (layoutParams == null) {
//            layoutParams.height = 222;
//        }
//        this.setLayoutParams(layoutParams);

    }



    /**
     * 初始化文本控件和输入框控件
     */
    private void initView() {
        mTxt = findViewById(R.id.txt_edtview);
        mTxt.measure(0,0);
        Log.d("高度",String.valueOf(mTxt.getMeasuredHeight()));
//        LinearLayout linearLayout =
//        mTxt.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 4.0f));
        mEdt = findViewById(R.id.edt_edtview);
//        mEdt.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1.0f));

        // 输入框焦点事件
        // 获得焦点时下方的横线呈绿色，失去焦点时呈黑色
        mEdt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) { // 获得焦点时处理逻辑
                    mLineColor = 0xFF62F45A;
                    invalidate(); // 重新绘制
                }else { // 失去焦点时处理逻辑
                    mLineColor = Color.BLACK;
                    invalidate(); // 重新绘制
                }
            }
        });

    }

    /**
     * 设置属性值
     * @param context
     * @param attrs
     */
    private void setAttrs(Context context, AttributeSet attrs) {
        // 获取自定义属性，返回一个数组
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.EditTextView);
        int lenght = typedArray.getIndexCount();
        // 逐个设置属性
        for (int i = 0; i < lenght; i++) {
            int index = typedArray.getIndex(i);
            switch (index) {
                case R.styleable.EditTextView_bottomLine:
                    mLineColor = typedArray.getColor(index, Color.BLACK);
                    break;
                case R.styleable.EditTextView_edtHint:
                    String hint = typedArray.getString(index);
                    mEdt.setHint(hint);
                    break;
                case R.styleable.EditTextView_leftText:
                    String text = typedArray.getString(index);
                    mTxt.setText(text);

            }
        }
    }


    /**
     * 画出输入框下方的横线
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 设置画笔样式
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setColor(mLineColor);
        mPaint.setStrokeWidth(2);
        // 进行绘制操作
        canvas.drawLine(0, getHeight() - 10, getWidth(), getHeight() - 10, mPaint);
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        // 左：0；上：1；右：2；下：3；
//
//        float x = event.getX(); // 点击处的x坐标
//        switch (event.getAction()){
//            case MotionEvent.ACTION_UP:
//                if (x >= getWidth() - drawableRight.getBounds().width() - drawableRight.getIntrinsicWidth()) {
//                    Toast.makeText(this.getContext(), "点击到了", Toast.LENGTH_SHORT).show();
//                    mEdt.setFocusable(true);
//                    mEdt.setFocusableInTouchMode(true);
//                    String TAG = "点击到了";
//                    Log.d(TAG, "getWidth(): " + getWidth());
//                    Log.d(TAG, "drawableRight.getBounds().width(): " + drawableRight.getBounds().width());
//                    Log.d(TAG, "mEdt.getPaddingRight(): " + mEdt.getPaddingRight());
//                } else {
////                    mEdt.setFocusable(true);
////                    mEdt.setFocusableInTouchMode(true);
////                    mEdt.requestFocus();
//                }
//                break;
//
//        }
//
//        if (drawableRight != null) {
//            String TAG = "aaaa";
//            Log.d(TAG, "onTouchEvent: ");
//            Log.d(TAG, "getWidth(): " + getWidth());
//            Log.d(TAG, "drawableRight.getBounds().width(): " + drawableRight.getBounds().width());
//            Log.d(TAG, "X: " + x);
//            Log.d(TAG, "drawableRight.getIntrinsicWidth(): " + drawableRight.getIntrinsicWidth());
//        }
//
//
//
//        return true;
//    }




}
