package com.slider;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * Created by administrator on 2017/9/19.
 */

public class Slider extends View {
    /**
     * 背景
    * */
    private Paint paint;

    private Paint bgpaint;


    private Paint sliderpaint;


    private float f;

    /**
    *视图圆角半径
     */
    private float defaultr=30f;
    private float cornersRadius;


    /**
    * 文字
    * */
    private Paint textpaint;
    private String text;
    private int textSize;
    private int DefaultSize=10;

    /**
    * 视图的中心
    * */
    private float halfw;
    private float halfh;
    /**
    * 滑动按钮
    * */
    private Paint btnpaint;
    private float btnRadius;
    private float DefaultbtnRadius=36;
    private float btnX;


    private Handler sliderhandler=new Handler();
    public Slider(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        f=context.getResources().getDisplayMetrics().density;
        init();
        initattr(attrs,defStyleAttr);
    }

    public Slider(Context context, @Nullable AttributeSet attrs) {
        this(context,attrs,0);
    }

    public Slider(Context context) {
        this(context,null);
    }
    private void init(){
        paint=new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);

        bgpaint=new Paint(paint);
        textpaint=new Paint(paint);
        btnpaint=new Paint(paint);
        sliderpaint=new Paint(paint);


        bgpaint.setColor(Color.YELLOW);
        textpaint.setColor(Color.BLACK);
        btnpaint.setColor(Color.WHITE);
        sliderpaint.setColor(Color.GREEN);


    }
    private void initattr(AttributeSet set,int defStyleAttr){
        TypedArray t= getContext().getTheme().obtainStyledAttributes(set,R.styleable.Slider,defStyleAttr,0);
        if(t!=null){
            int n=t.getIndexCount();
            for(int i=0;i<n;i++){
                int attr=t.getIndex(i);
                switch (attr){
                    case R.styleable.Slider_cornersRadius:
                        cornersRadius=t.getDimension(attr,defaultr);
                        btnX=cornersRadius;
                        break;
                    case R.styleable.Slider_text:
                        text=t.getString(attr);
                        if(TextUtils.isEmpty(text))
                            text="";
                        break;
                    case R.styleable.Slider_textSize:
                        textSize=t.getDimensionPixelSize(attr,DefaultSize);
                        break;
                    case R.styleable.Slider_btnRadius:
                        btnRadius=t.getDimension(attr,DefaultbtnRadius);

                        break;
                }
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.i("Round 106","onsizechanged");
        halfh=h/2;
        halfw=w/2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawbg(canvas);
        drawtext(canvas);
        drawslider(canvas);
        drawbtn(canvas,btnX);
    }
    private void drawbg(Canvas canvas){
        RectF rectF=new RectF();
        rectF.top=0;
        rectF.left=btnRadius/2;
        rectF.bottom=getMeasuredHeight();
        rectF.right=getMeasuredWidth()-btnRadius/2;
        canvas.drawRoundRect(rectF,cornersRadius,cornersRadius,bgpaint);
    }
    private void drawtext(Canvas canvas){
        Rect r=new Rect();
        textpaint.setTextSize(textSize);
        textpaint.getTextBounds(text,0,text.length(),r);
        Paint.FontMetrics fm = textpaint.getFontMetrics();

//假设已经计算出文字上下居中后Y轴的坐标为 ---> y;
        float textY = halfh+fm.descent-r.height()/2+ (fm.descent - fm.ascent) / 2;
        canvas.drawText(text,halfw-r.width()/2,textY,textpaint);
    }
    private void drawslider(Canvas canvas){
        RectF rectF=new RectF();
        rectF.top=0;
        rectF.left=btnRadius/2;
        rectF.bottom=getMeasuredHeight();
        rectF.right=btnX;
        canvas.drawRoundRect(rectF,cornersRadius,cornersRadius,sliderpaint);
    }
    private void drawbtn(Canvas canvas,float position){
        canvas.drawCircle(position,halfh,btnRadius,btnpaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float positionX=event.getX();
        switch (event.getAction()){
            case MotionEvent.ACTION_UP:
                if(positionX<halfw*2-cornersRadius)
                    sliderhandler.post(r);
                else
                    Toast.makeText(getContext(),"解锁",Toast.LENGTH_SHORT).show();
                break;
            case MotionEvent.ACTION_DOWN:
                Log.i("Rounded_rectangle 86",String.valueOf(textSize));
                break;
            case MotionEvent.ACTION_MOVE:
                btnX=positionX;
                if(btnX<cornersRadius)
                    btnX=cornersRadius;
                if(btnX>halfw*2-cornersRadius) {
                    btnX = halfw * 2 - cornersRadius;
                }
                invalidate();
                break;
            default:
                break;
        }
        return true;
    }
    private Runnable r=new Runnable() {
        @Override
        public void run() {
            btnX=btnX-cornersRadius/10;
            if(btnX<cornersRadius){
                btnX=cornersRadius;
                sliderhandler.removeCallbacks(r);
            }else
                sliderhandler.post(r);
            invalidate();
        }
    };
}
