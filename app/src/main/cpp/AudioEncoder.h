//
// Created by 唐宇 on 2018/6/20.
//

#ifndef AUDIOENCODER_AUDIOENCODER_H
#define AUDIOENCODER_AUDIOENCODER_H

extern "C" {
#include "libavcodec/avcodec.h"
#include "libavformat/avformat.h"
#include "libavutil/avutil.h"
#include "libswresample/swresample.h"
#include "libavutil/samplefmt.h"
#include "libavutil/common.h"
#include "libavutil/channel_layout.h"
#include "libavutil/opt.h"
#include "libavutil/imgutils.h"
#include "libavutil/mathematics.h"
};

#include "./CommonTools.h"
class AudioEncoder {


public:
    AudioEncoder();

private:
    int init(int bitRate, int channels, int sampleRate, int bitsPerSample, const char* aacFilePath, const char * codec_name);

};


#endif //AUDIOENCODER_AUDIOENCODER_H
