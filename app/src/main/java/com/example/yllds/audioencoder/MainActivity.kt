package com.example.yllds.audioencoder

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import java.io.FileInputStream
import java.io.FileOutputStream


class MainActivity : AppCompatActivity(), OutputAACDelegate {
    override fun outputAACPacket(data: ByteArray) {
        outputStream?.write(data)
    }

    private var outputStream: FileOutputStream? = null
    private var inputStream: FileInputStream? = null
    private val aacHardFilePath = "/mnt/sdcard/vocal_mediacodec.aac"
    private val pcmFilePath = "/mnt/sdcard/vocal.pcm"
    private val aacFilePath = "/mnt/sdcard/vocal_fdk.aac"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        soft_coding.setOnClickListener {
            val startTimeMills = System.currentTimeMillis()
            val audioEncoder = SoftAudioEncoder()
            //128Kbps 比特率
            audioEncoder.encode(pcmFilePath, 2, 128 * 1024, 44100, aacFilePath);
            val wasteTimeMills = (System.currentTimeMillis() - startTimeMills).toInt()
            Log.i("success", "wasteTimeMills is : $wasteTimeMills")
        }
        hard_coding.setOnClickListener {
            encodeHard()
        }
    }

    private fun encodeHard() {
        val startTimeMills = System.currentTimeMillis()
        val audioEncoder: HardAudioEncoder? = HardAudioEncoder(this, 44100, 2, 128 * 1024);
        inputStream = FileInputStream(pcmFilePath)
        outputStream = FileOutputStream(aacHardFilePath)
        inputStream?.use {
            outputStream?.use {
                val bufferSize = 1024 * 256
                val buffer = ByteArray(bufferSize)
                val encodeBufferSize = 1024 * 10
                val encodeBuffer = ByteArray(encodeBufferSize)
                var len = -1
                //执行fun的形式
                while ({ len = inputStream!!.read(buffer);len }() > 0) {
                    var offset = 0

                    while (offset < len) {
                        val encodeBufferLenth = Math.min(len - offset, encodeBufferSize)
                        System.arraycopy(buffer, offset, encodeBuffer, 0, encodeBufferLenth)
                        audioEncoder?.fireAudio(encodeBuffer, encodeBufferLenth)
                        offset += encodeBufferLenth
                    }

                }


            }
        }
        audioEncoder?.stop()
        val wasteTimeMills = (System.currentTimeMillis() - startTimeMills).toInt()
        Log.i("success", "wasteTimeMills is : $wasteTimeMills")
    }

}
