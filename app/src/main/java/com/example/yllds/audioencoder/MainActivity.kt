package com.example.yllds.audioencoder

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val pcmFilePath = "/mnt/sdcard/vocal.pcm"
    private val aacFilePath = "/mnt/sdcard/vocal_fdk.aac"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        soft_coding.setOnClickListener {
            val startTimeMills = System.currentTimeMillis()
            val audioEncoder = SoftAudioEncoder()
            audioEncoder.encode(pcmFilePath, 2, 128 * 1024, 44100, aacFilePath)
            val wasteTimeMills = (System.currentTimeMillis() - startTimeMills).toInt()
            Log.i("success", "wasteTimeMills is : $wasteTimeMills")
        }

    }



}
