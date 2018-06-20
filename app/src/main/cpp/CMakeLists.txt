
cmake_minimum_required(VERSION 3.4.1)

set(PATH_TO_NATIVE ${PATH_TO_MEDIACORE}/src/main/cpp)
set(PATH_TO_PRE_BUILT ${PATH_TO_MEDIACORE}/libs/${ANDROID_ABI})
include_directories(${PATH_TO_MEDIACORE}/libs/ffmpeg/include)# 包含.h文件include目录

file(GLOB  FFMPEG_ENCODE_SOURCE "*.cpp")#包含当前目录的cpp文件

add_library( native-lib
             SHARED
             ${FFMPEG_ENCODE_SOURCE})



target_link_libraries( # Specifies the target library.
                       native-lib
                        log
                        z
                        ${PATH_TO_PRE_BUILT}/libaudioencoder.so
                       )





