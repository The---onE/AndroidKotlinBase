package com.xmx.androidkotlinbase.Tools

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

/**
 * Created by The_onE on 2016/3/3.
 * 首页ViewPager适配器，管理标签页Fragment和对应标题
 */
class MyPagerAdapter(fm: FragmentManager,
                     internal var mFragments: List<Fragment>,
                     internal var mTitles: List<String>)
    : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return mFragments[position]
    }

    override fun getCount(): Int {
        return mFragments.size
    }

    override fun getPageTitle(position: Int): CharSequence {
        return mTitles[position]
    }
}
