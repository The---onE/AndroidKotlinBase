package com.xmx.androidkotlinbase.common.net

import android.annotation.SuppressLint
import android.os.AsyncTask

import com.xmx.androidkotlinbase.utils.StringUtil

import java.util.ArrayList

import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody

/**
 * Created by The_onE on 2017/5/26.
 * 用于管理HTTP请求，使用OkHttp插件
 */

object HttpManager {

    private val client = OkHttpClient()
    private val JSON = MediaType.parse("application/json; charset=utf-8")

    fun makeGetRequest(url: String, params: Map<String, String>): String {
        val list = ArrayList<String>()
        for ((key, value) in params) {

            list.add(key + "=" + value)
        }
        return url + "?" + StringUtil.join(list, "&")
    }

    // TODO
    @SuppressLint("StaticFieldLeak")
    operator fun get(url: String, params: Map<String, String>?,
                     success: (String) -> Unit,
                     fail: (Exception) -> Unit) {
        // 开启新线程
        object : AsyncTask<String, Exception, String>() {
            override fun doInBackground(vararg strings: String): String? {
                try {
                    var re = url
                    if (params != null) {
                        re = makeGetRequest(url, params)
                    }
                    // 生成Get请求
                    val request = Request.Builder()
                            .url(re)
                            .build()
                    // 获取Get响应
                    val response = client.newCall(request).execute()
                    // 返回响应结果
                    return response.body()!!.string()
                } catch (e: Exception) {
                    publishProgress(e)
                    return null
                }

            }

            override fun onProgressUpdate(vararg values: Exception) {
                super.onProgressUpdate(*values)
                // 请求过程出现异常，请求失败
                fail(values[0])
            }

            override fun onPostExecute(s: String?) {
                super.onPostExecute(s)
                if (s != null) {
                    // 请求成功，返回响应结果
                    success(s)
                }
            }
        }.execute()
    }

    // TODO
    @SuppressLint("StaticFieldLeak")
    fun post(url: String, json: String,
             success: (String) -> Unit,
             fail: (Exception) -> Unit) {
        // 开启新线程
        object : AsyncTask<String, Exception, String>() {
            override fun doInBackground(vararg strings: String): String? {
                return try {
                    // 生成Post请求
                    val body = RequestBody.create(JSON, json)
                    val request = Request.Builder()
                            .url(url)
                            .post(body)
                            .build()
                    // 获取Post响应
                    val response = client.newCall(request).execute()
                    // 返回响应结果
                    response.body()!!.string()
                } catch (e: Exception) {
                    publishProgress(e)
                    null
                }

            }

            override fun onProgressUpdate(vararg values: Exception) {
                super.onProgressUpdate(*values)
                // 请求过程出现异常，请求失败
                fail(values[0])
            }

            override fun onPostExecute(s: String?) {
                super.onPostExecute(s)
                if (s != null) {
                    // 请求成功，返回响应结果
                    success(s)
                }
            }
        }.execute()
    }
}
