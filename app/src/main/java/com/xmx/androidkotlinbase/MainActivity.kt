package com.xmx.androidkotlinbase

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById(R.id.btn_test)!!.setOnClickListener {
            Toast.makeText(this, "Hello Kotlin", Toast.LENGTH_SHORT).show();
        }
    }
}
