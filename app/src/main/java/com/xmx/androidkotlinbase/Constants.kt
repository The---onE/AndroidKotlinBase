package com.xmx.androidkotlinbase

/**
 * Created by The_onE on 2017/1/16.
 */

object Constants {
    val EXCEPTION_DEBUG = true

    val APP_ID = "vBb01A8F2LI63zJBqkQWiuWc-gzGzoHsz"
    val APP_KEY = "tfMtJ1uRTmRre40QhxT46yVl"

    val USER_INFO_TABLE = "UserInfo"
    val USER_DATA_TABLE = "UserData"
    val LOGIN_LOG_TABLE = "LoginLog"

    val USER_SHARED_PREFERENCE = "USER"

    val FILE_DIR = "/Framework"
    val DATABASE_DIR = FILE_DIR + "/Database"
    val DATABASE_FILE = DATABASE_DIR + "/database.db"

    val LONGEST_EXIT_TIME: Long = 2000
    val SPLASH_TIME: Long = 2000
    val LONGEST_SPLASH_TIME: Long = 6000

    val CHOOSE_ALBUM = 1
    val CHOOSE_PHOTO = 2
    val TAKE_PHOTO = 3

    val SECOND_TIME: Long = 1000
    val MINUTE_TIME = 60 * SECOND_TIME
    val HOUR_TIME = 60 * MINUTE_TIME
    val DAY_TIME = 24 * HOUR_TIME

    val DAYS_OF_MONTH = intArrayOf(31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)

    val DAY_OF_WEEK = arrayOf("星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六")

    val STATUS_WAITING = 0
    val STATUS_CANCELED = -1
    val STATUS_DELETED = -2
    val STATUS_COMPLETE = 1
}
