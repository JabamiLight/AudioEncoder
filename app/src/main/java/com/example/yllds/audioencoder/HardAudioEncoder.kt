package com.example.yllds.audioencoder

import android.media.MediaCodec
import android.media.MediaCodecInfo
import android.media.MediaFormat
import android.util.Log
import java.io.IOException
import java.nio.ByteBuffer


/*
* Created by TY on 2018/6/23.
*      
*
*      
*          ┌─┐       ┌─┐
*       ┌──┘ ┴───────┘ ┴──┐
*       │                 │
*       │       ───       │
*       │  ─┬┘       └┬─  │
*       │                 │
*       │       ─┴─       │
*       │                 │
*       └───┐         ┌───┘
*           │         │
*           │         │
*           │         │
*           │         └──────────────┐
*           │                        │
*           │                        ├─┐
*           │                        ┌─┘    
*           │                        │
*           └─┐  ┐  ┌───────┬──┐  ┌──┘         
*             │ ─┤ ─┤       │ ─┤ ─┤         
*             └──┴──┘       └──┴──┘ 
*                 神兽保佑 
*                 代码无BUG! 
*/

class HardAudioEncoder @Throws(IOException::class) constructor(outputAACDelegate: OutputAACDelegate, sampleRate: Int, channels: Int, bitRate: Int) {
    private var mediaCodec: MediaCodec? = null
    private var inputBuffers: Array<ByteBuffer>? = null
    private var outputBuffers: Array<ByteBuffer>? = null
    private var outputAACDelegate: OutputAACDelegate? = outputAACDelegate

    companion object {
        val MINE_TYPE = "audio/mp4a-latm"
    }


    fun fireAudio(data: ByteArray, len: Int) {
        mediaCodec?.run {
            val inputBufferIndex = dequeueInputBuffer(-1)
            if (inputBufferIndex > 0) {
                val inputBuffer = this@HardAudioEncoder.inputBuffers!![inputBufferIndex]
                inputBuffer.clear()
                inputBuffer.put(data)
                queueInputBuffer(inputBufferIndex, 0, len, System.nanoTime(), 0)
            }
            val bufferInfo = MediaCodec.BufferInfo()
            var outputBufferIndex = dequeueOutputBuffer(bufferInfo, 0)

            while (outputBufferIndex > 0) {
                val outputBuffer =this@HardAudioEncoder.outputBuffers!![outputBufferIndex]
                outputAACDelegate?.run {
                    val outPacketSize = bufferInfo.size + 7
                    outputBuffer.position(bufferInfo.offset)
                    outputBuffer.limit(bufferInfo.offset + bufferInfo.size)
                    val outData = ByteArray(outPacketSize)
                    addADTStoPacket(outData, outPacketSize);//添加ADTS 代码后面会贴上
                    outputBuffer.get(outData,7,bufferInfo.size)
                    //读取完成postion回归
                    outputBuffer.position(bufferInfo.offset)
                    outputAACDelegate?.outputAACPacket(outData);
                }
                releaseOutputBuffer(outputBufferIndex,false)
                outputBufferIndex=dequeueOutputBuffer(bufferInfo,0)
            }
        }


    }

    private fun addADTStoPacket(packet: ByteArray, packetLen: Int) {
        val profile = 2 // AAC LC
        val freqIdx = 4 // 44.1KHz
        val chanCfg = 2// CPE
        packet[0] = 0xFF.toByte()
        packet[1] = 0xF9.toByte()
        packet[2] = ((profile - 1 shl 6) + (freqIdx shl 2) + (chanCfg shr 2)).toByte()
        packet[3] = ((chanCfg and 3 shl 6) + (packetLen shr 11)).toByte()
        packet[4] = (packetLen and 0x7FF shr 3).toByte()
        packet[5] = ((packetLen and 7 shl 5) + 0x1F) .toByte()
        packet[6] = 0xFC.toByte()
    }

     fun stop(){
         mediaCodec?.run {
             stop()
             release()
         }
    }

    init {
        val encodeFormat: MediaFormat = MediaFormat.createAudioFormat(MINE_TYPE, sampleRate, channels)
        encodeFormat.setInteger(MediaFormat.KEY_BIT_RATE, bitRate)
        encodeFormat.setInteger(MediaFormat.KEY_AAC_PROFILE, MediaCodecInfo.CodecProfileLevel.AACObjectLC)
        encodeFormat.setInteger(MediaFormat.KEY_MAX_INPUT_SIZE, 10 * 1024)
        mediaCodec = MediaCodec.createEncoderByType(MINE_TYPE)
        mediaCodec?.run {
            configure(encodeFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
            start()
            this@HardAudioEncoder.inputBuffers = inputBuffers
            this@HardAudioEncoder.outputBuffers = outputBuffers
        }.ifNull {
            Log.e("problem", "create mediaEncode failed");
            return@ifNull
        }
    }
}