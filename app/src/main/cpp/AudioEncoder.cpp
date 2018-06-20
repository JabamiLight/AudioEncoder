//
// Created by 唐宇 on 2018/6/20.
//

#include "AudioEncoder.h"

int AudioEncoder::init(int bitRate, int channels, int sampleRate, int bitsPerSample,
                       const char *aacFilePath, const char *codec_name) {
    av_register_all();

    return 0;
}

AudioEncoder::AudioEncoder() {
    av_register_all();
}
