#include <jni.h>
#include <string>
#include "AudioEncoder.h"

extern "C" JNIEXPORT jstring

JNICALL
Java_com_example_yllds_audioencoder_SoftAudioEncoder_encode(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    new AudioEncoder();
    return env->NewStringUTF(hello.c_str());
}
