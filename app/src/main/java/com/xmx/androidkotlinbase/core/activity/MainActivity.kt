package com.xmx.androidkotlinbase.core.activity

import android.Manifest
import android.app.Activity
import android.app.AppOpsManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.view.MenuItem
import com.xmx.androidkotlinbase.core.CoreConstants
import com.xmx.androidkotlinbase.R
import com.xmx.androidkotlinbase.base.activity.BaseActivity
import com.xmx.androidkotlinbase.common.user.*
import com.xmx.androidkotlinbase.core.HomePagerAdapter
import com.xmx.androidkotlinbase.core.fragment.*
import com.xmx.androidkotlinbase.module.user.LoginActivity
import com.xmx.androidkotlinbase.utils.ExceptionUtil
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.tool_bar.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.util.*

/**
 * Created by The_onE on 2017/2/15.
 * 主Activity，利用Fragment展示所有程序内容
 */
class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var um: IUserManager = UserManager // 用户管理器

    private val writeSdRequest = 1
    // 侧滑菜单登录菜单项
    private var loginItem: MenuItem? = null
    // 是否已成功登录
    private var loginFlag = false

    override fun initView(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_main)

        // 初始化工具栏
        setSupportActionBar(toolbar)

        // 初始化侧滑菜单
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)

        // 各标签页Fragment
        val fragments = ArrayList<Fragment>()
        fragments.add(HomeFragment())
        fragments.add(DataFragment())
        fragments.add(NetFragment())
        fragments.add(IMFragment())
        fragments.add(NotificationFragment())

        // 各标签页标题
        val titles = ArrayList<String>()
        titles.add("首页")
        titles.add("数据")
        titles.add("网络")
        titles.add("IM")
        titles.add("通知")

        // 设置ViewPager中的标签页
        viewPager.adapter = HomePagerAdapter(supportFragmentManager, fragments, titles)
        // 设置标签页底部选项卡
        tabLayout.setupWithViewPager(viewPager)

        tabLayout.getTabAt(0)!!.setIcon(R.mipmap.ic_launcher)
        tabLayout.getTabAt(1)!!.setIcon(R.mipmap.ic_launcher)
        tabLayout.getTabAt(2)!!.setIcon(R.mipmap.ic_launcher)
        tabLayout.getTabAt(3)!!.setIcon(R.mipmap.ic_launcher)
        tabLayout.getTabAt(4)!!.setIcon(R.mipmap.ic_launcher)

        EventBus.getDefault().register(this)
    }

    override fun setListener() {
    }

    // 登录成功的处理
    private val loginSuccess = {
        data: UserData ->
        loginItem?.title = "${data.nickname} 点击注销"
        loginFlag = true
    }

    // 登录失败的处理
    private val loginError = {
        e: Int ->
        when (e) {
            UserConstants.NOT_LOGGED_IN -> showToast(R.string.not_loggedin)
            UserConstants.USERNAME_ERROR -> showToast(R.string.username_error)
            UserConstants.CHECKSUM_ERROR -> showToast(R.string.not_loggedin)
            UserConstants.CANNOT_CHECK_LOGIN -> showToast(R.string.cannot_check_login)
        }
    }

    override fun processLogic(savedInstanceState: Bundle?) {
        checkLocalPhonePermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, writeSdRequest)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkOpsPermission(AppOpsManager.OPSTR_WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE, writeSdRequest)
        }

        // 设置侧滑菜单
        val menu = nav_view.menu
        loginItem = menu.findItem(R.id.nav_user)
        // 在SplashActivity中自动登录，在此校验登录
        if (um.isLoggedIn()) {
            checkLogin()
        }
    }

    private fun checkLogin() {
        um.checkLogin(
                success = loginSuccess,
                error = loginError,
                cloudError = {
                    e ->
                    showToast(R.string.network_error)
                    ExceptionUtil.normalException(e, baseContext)
                })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // 在登录页登录成功或注册页注册成功
        if (requestCode == UserConstants.LOGIN_REQUEST_CODE
                || requestCode == UserConstants.REGISTER_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                checkLogin()
            }
        }
    }

    // 侧滑菜单项点击事件
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // 显示选择的选项卡
        when (item.itemId) {
            R.id.nav_home -> viewPager.currentItem = 0
            R.id.nav_data -> viewPager.currentItem = 1
            R.id.nav_net -> viewPager.currentItem = 2
            R.id.nav_im -> viewPager.currentItem = 3
            R.id.nav_notification -> viewPager.currentItem = 4
            R.id.nav_user -> {
                val intent = Intent(this, LoginActivity::class.java)
                if (um.isLoggedIn()) {
                    // 注销
                    val builder = AlertDialog.Builder(this)
                    builder.setMessage("确定要注销吗？")
                    builder.setTitle("提示")
                    builder.setNeutralButton("取消") { dialogInterface, _ -> dialogInterface.dismiss() }
                    builder.setPositiveButton("确定") { _, _ ->
                        // 确认注销
                        um.logout {
                            //SyncEntityManager.getInstance().getSQLManager().clearDatabase();
                        }
                        showToast("注销成功")
                        loginItem?.title = "登录"
//                            startActivityForResult(intent, UserConstants.LOGIN_REQUEST_CODE);
                    }
                    builder.show()
                } else {
                    // 登录
                    startActivityForResult(intent, UserConstants.LOGIN_REQUEST_CODE)
                }
            }
        }
        // 关闭侧边栏
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    // 点击返回键时添加二次确认
    private var mExitTime: Long = 0 // 上次按键的时间

    override fun onBackPressed() {
        // 如果侧边栏已打开，则关闭
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
            return
        }
        // 如果是第一次按键或距离上次按键时间过长，则重新计时
        if (System.currentTimeMillis() - mExitTime > CoreConstants.LONGEST_EXIT_TIME) {
            showToast(R.string.confirm_exit)
            mExitTime = System.currentTimeMillis()
        } else {
            super.onBackPressed()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            writeSdRequest -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    showToast("您拒绝了读写手机存储的权限，某些功能会导致程序出错，请手动允许该权限！")
                }
            }
        }
    }

    /**
     * 订阅登录成功事件，在SplashActivity自动登录成功时触发
     * @param[event] 操作日志变动事件
     */
    @Subscribe
    fun onEvent(event: LoginEvent) {
        checkLogin()
    }
}
