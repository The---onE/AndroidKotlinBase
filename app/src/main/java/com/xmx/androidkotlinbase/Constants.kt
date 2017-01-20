package com.xmx.androidkotlinbase

/**
 * Created by The_onE on 2017/1/16.
 */

object Constants {
    public val EXCEPTION_DEBUG = false

    public val APP_ID = "vBb01A8F2LI63zJBqkQWiuWc-gzGzoHsz"
    public val APP_KEY = "tfMtJ1uRTmRre40QhxT46yVl"

    public val USER_INFO_TABLE = "UserInfo"
    public val USER_DATA_TABLE = "UserData"
    public val LOGIN_LOG_TABLE = "LoginLog"

    public val FILE_DIR = "/Framework"
    public val DATABASE_DIR = FILE_DIR + "/Database"
    public val DATABASE_FILE = DATABASE_DIR + "/database.db"

    public val LONGEST_EXIT_TIME: Long = 2000
    public val SPLASH_TIME: Long = 2000
    public val LONGEST_SPLASH_TIME: Long = 6000

    public val CHOOSE_ALBUM = 1
    public val CHOOSE_PHOTO = 2
    public val TAKE_PHOTO = 3

    public val SECOND_TIME: Long = 1000
    public val MINUTE_TIME = 60 * SECOND_TIME
    public val HOUR_TIME = 60 * MINUTE_TIME
    public val DAY_TIME = 24 * HOUR_TIME

    public val DAYS_OF_MONTH = intArrayOf(31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)

    public val DAY_OF_WEEK = arrayOf("星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六")

    public val STATUS_WAITING = 0
    public val STATUS_CANCELED = -1
    public val STATUS_DELETED = -2
    public val STATUS_COMPLETE = 1
}
