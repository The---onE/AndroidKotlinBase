package com.xmx.androidkotlinbase.module.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.xmx.androidkotlinbase.R
import com.xmx.androidkotlinbase.base.dialog.BaseDialog

/**
 * Created by The_onE on 2017/5/22.
 * 测试
 */

class TestDialog : BaseDialog() {
    override fun getContentView(inflater: LayoutInflater, container: ViewGroup?): View {
        return inflater.inflate(R.layout.dialog_test, container)
    }

    override fun initView(view: View, savedInstanceState: Bundle?) {

    }

    override fun setListener(view: View) {
        view.findViewById(R.id.btnOK).setOnClickListener {
            showToast(R.string.confirm)
            dismiss()
        }

        view.findViewById(R.id.btnCancel).setOnClickListener {
            showToast(R.string.cancel)
            dismiss()
        }
    }

    override fun processLogic(view: View, savedInstanceState: Bundle?) {

    }
}
