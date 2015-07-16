package com.example.mummyding.displaylyricsondesktop;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import static android.graphics.Color.WHITE;


public class MainActivity extends Activity implements View.OnTouchListener{
    MyView myView;
    WindowManager wm;
    WindowManager.LayoutParams layoutParams;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        //设置TextView的属性
        layoutParams = new WindowManager.LayoutParams();
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //这里是关键，使控件始终在最上方
        layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT | WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        //这个Gravity也不能少，不然的话，下面"移动歌词"的时候就会出问题了～ 可以试试[官网文档有说明]
        layoutParams.gravity = Gravity.LEFT|Gravity.TOP;

        //创建自定义的TextView
        myView = new MyView(this);
        myView.setText("Test Touch");
        myView.setTextColor(Color.BLACK);
        myView.setBackgroundColor(Color.WHITE);
        //监听 OnTouch 事件 为了实现"移动歌词"功能
        myView.setOnTouchListener(this);

        wm.addView(myView, layoutParams);

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
         switch (event.getAction()){
            case MotionEvent.ACTION_UP:
                //getRawX/Y 是获取相对于Device的坐标位置 注意区别getX/Y[相对于View]
                layoutParams.x = (int) event.getRawX();
                layoutParams.y = (int) event.getRawY();
                //更新"桌面歌词"的位置
                wm.updateViewLayout(myView,layoutParams);
                //下面的removeView 可以去掉"桌面歌词"
                //wm.removeView(myView);
                break;
            case MotionEvent.ACTION_MOVE:
                layoutParams.x = (int) event.getRawX();
                layoutParams.y = (int) event.getRawY();
                wm.updateViewLayout(myView,layoutParams);
                break;
        }
        return false;
    }
    //继承 TextView  好吧，貌似有点多此一举，其实直接用TextView就好
    public class MyView extends TextView{
        public MyView(Context context) {
            super(context);
        }
    }
}
