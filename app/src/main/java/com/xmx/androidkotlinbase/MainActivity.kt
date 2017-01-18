package com.xmx.androidkotlinbase

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import com.xmx.androidkotlinbase.Fragments.HomeFragment
import com.xmx.androidkotlinbase.Fragments.TestFragment
import com.xmx.androidkotlinbase.Tools.ActivityBase.BaseActivity
import com.xmx.androidkotlinbase.Tools.MyPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.tool_bar.*
import java.util.*

class MainActivity : BaseActivity() {
    // 初始化View
    override fun initView(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_main)

        // 初始化工具栏
        setSupportActionBar(toolbar)

        // 各标签页Fragment
        val fragments = ArrayList<Fragment>()
        fragments.add(HomeFragment())
        fragments.add(TestFragment())

        // 各标签页标题
        val titles = ArrayList<String>()
        titles.add("首页")
        titles.add("测试")

        // 设置ViewPager中的标签页
        viewPager.adapter = MyPagerAdapter(supportFragmentManager, fragments, titles)
        // 设置标签页底部选项卡
        tabLayout.setupWithViewPager(viewPager)
    }

    // 声明事件监听
    override fun setListener() {
    }

    // 处理业务逻辑
    override fun processLogic(savedInstanceState: Bundle?) {
    }

}
