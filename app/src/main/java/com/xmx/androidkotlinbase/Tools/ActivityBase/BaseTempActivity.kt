package com.xmx.androidkotlinbase.Tools.ActivityBase

import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.GestureDetector
import android.view.MotionEvent
import com.xmx.androidkotlinbase.R

/**
 * Created by The_onE on 2016/1/1.
 * 临时Activity基类，增加右滑关闭功能，工具栏带有返回按钮
 * 布局文件中需添加 <include layout="@layout/tool_bar" />
 * AndroidManifest.xml中对应的Activity需添加 android:theme="@style/AppTheme.NoActionBar"
 */
abstract class BaseTempActivity : BaseActivity() {
    // 手势识别
    private var mGestureDetector: GestureDetector? = null

    // 自定义手势识别
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return mGestureDetector?.onTouchEvent(event) ?: false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 初始化工具栏
        // 布局中必须要有 <include layout="@layout/tool_bar" />
        val toolbar = findViewById(R.id.toolbar) as Toolbar?
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        // 工具栏添加返回按钮
        toolbar?.setNavigationOnClickListener { onBackPressed() }

        mGestureDetector = GestureDetector(this,
                // 添加右滑关闭功能
                object : GestureDetector.SimpleOnGestureListener() {
                    override fun onFling(e1: MotionEvent, e2: MotionEvent,
                                         velocityX: Float, velocityY: Float): Boolean {
                        if (velocityX > Math.abs(velocityY)) {
                            onBackPressed()
                        }
                        return super.onFling(e1, e2, velocityX, velocityY)
                    }
                })
    }
}
