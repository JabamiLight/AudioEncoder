package com.example.yllds.audioencoder

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        soft_coding.setOnClickListener {
            SoftAudioEncoder().encode("",1,1,1,"1")
        }

    }



}
