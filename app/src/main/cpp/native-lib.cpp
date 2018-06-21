#include <jni.h>
#include <string>
#include "AudioEncoder.h"

AudioEncoder *audioEncoder;
extern "C" JNIEXPORT jstring

JNICALL
Java_com_example_yllds_audioencoder_SoftAudioEncoder_encode(
        JNIEnv * env, jobject obj, jstring pcmPathParam, jint channels, jint bitRate,
        jint sampleRate, jstring aacPathParam) {
    std::string hello = "Hello from C++";
    audioEncoder=new AudioEncoder();
    audioEncoder->init(bitRate, channels, sampleRate, 1024, "/mnt/sdcard/vocal_fdk.aac", "");
    return env->NewStringUTF(hello.c_str());
}
