#include <jni.h>
#include <string>
#include "SystemInfo.h"

extern "C"
JNIEXPORT jstring JNICALL Java_com_theikchan_screencloudwebplaybridge_NativeStore_getDeviceInfo(JNIEnv* env, jobject /* this */) {
    SystemInfo systemInfo;
    std::string json = systemInfo.getDeviceInfoJson();
    return env->NewStringUTF(json.c_str());
}