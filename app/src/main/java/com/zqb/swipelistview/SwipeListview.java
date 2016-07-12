package com.zqb.swipelistview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;

/**
 * Created by zqb on 2016/7/12.
 */
public class SwipeListview extends ListView {
    private int mScreenWidth; //屏幕宽度

    //通过mDownX和mDownY获取是哪个item
    private int mDownX;       //按下点的X值
    private int mDownY;       //按下点的Y值

    private int mDeleteBtnWidth;  //删除按钮宽度
    private boolean isDeleteShow;//当前按钮是否正在显示
    private ViewGroup mPointChild; //当前处理的item
    private LinearLayout.LayoutParams mLayoutParams;

    public SwipeListview(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public SwipeListview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //获取屏幕宽度
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm=new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        mScreenWidth=dm.widthPixels;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction())
        {
            case MotionEvent.ACTION_DOWN:
            {
                performActionDown(ev);
                break;
            }
            case MotionEvent.ACTION_MOVE:
            {
                performActionMove(ev);
                break;
            }
            case MotionEvent.ACTION_UP:
            {
                performActionUp();
                break;
            }
            default:break;
        }
        return super.onTouchEvent(ev);
    }
    private void performActionDown(MotionEvent ev)
    {
        if(isDeleteShow)//若当前正在显示删除按钮，则先将其关闭
        {
            turnToNormal();
        }
        mDownX= (int) ev.getX();
        mDownY= (int) ev.getY();

        //获取当前点的item
        mPointChild= (ViewGroup) getChildAt(pointToPosition(mDownX,mDownY));

        // 获取删除按钮的宽度
        mDeleteBtnWidth=mPointChild.getChildAt(1).getLayoutParams().width;//1表示获取mPointChild的第二个元素
        /*
            为什么要多此一举重新设置为屏幕宽度呢？
            答案是：当前一个View的layout_width为match_parent时，
            ViewGroup就不去理会剩下的View了，
            也就是删除的那个按钮根本没有绘制出来！
         */

        mLayoutParams= (LinearLayout.LayoutParams) mPointChild.getChildAt(0).getLayoutParams();
        mLayoutParams.width=mScreenWidth;
        mPointChild.getChildAt(0).setLayoutParams(mLayoutParams);
    }

    /*
    *    performActionMove是有返回值的，而且我们在onTouchEvent中return了它的返回值，
    *   有返回值的目的就是让横向滑动的时候在我们的onTouchEvent中消费了事件，
    *   竖屏滑动的时候交由ListView的onTouchEvent处理事件，
    *   从而避免屏蔽掉了ListView的上下滑动机制。
     */
    private boolean performActionMove(MotionEvent ev)
    {
        int nowX= (int) ev.getX();
        int nowY= (int) ev.getY();
        if(Math.abs(nowX-mDownX)>Math.abs(nowY-mDownY))//判断是滑动侧边还是上下滑动
        {
            if(nowX<mDownX)//向左滑动
            {
                // 计算要偏移的距离
                int scroll=(nowX-mDownX)/2;
                // 如果大于了删除按钮的宽度， 则最大为删除按钮的宽度
                if(-scroll>=mDeleteBtnWidth)
                {
                    scroll=-mDeleteBtnWidth;
                }
                // 重新设置leftMargin
                mLayoutParams.leftMargin=scroll;
                mPointChild.getChildAt(0).setLayoutParams(mLayoutParams);
            }
            return true;
        }
        return super.onTouchEvent(ev);
    }

    private void performActionUp()
    {
        // 偏移量大于button的一半，则显示button
        // 否则恢复默认
        if(-mLayoutParams.leftMargin>=mDeleteBtnWidth/2)
        {
            mLayoutParams.leftMargin=-mDeleteBtnWidth;
            isDeleteShow=true;
        }
        else
        {
            turnToNormal();
        }
        mPointChild.getChildAt(0).setLayoutParams(mLayoutParams);
    }
    protected void turnToNormal()
    {
        mLayoutParams.leftMargin=0;
        mPointChild.getChildAt(0).setLayoutParams(mLayoutParams);
        isDeleteShow=false;
    }
    protected boolean canClick()
    {
        return !isDeleteShow;
    }
}
