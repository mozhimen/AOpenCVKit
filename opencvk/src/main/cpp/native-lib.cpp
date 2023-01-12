#include <jni.h>
#include <string>

#include "MD5.h"

extern "C" JNIEXPORT jstring JNICALL
Java_com_mozhimen_opencvk_OpenCVK_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {

    std::string hello = "Hello from C++";

    return env->NewStringUTF(hello.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_mozhimen_opencvk_libs_OpenCVKLib_stringFromJNI(JNIEnv *env, jclass clazz) {
    std::string hello = "Hello OpenCVK from C++";

    return env->NewStringUTF(hello.c_str());
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_mozhimen_opencvk_libs_OpenCVKLib_getSignKeyFromJNI(JNIEnv *env, jclass clazz,
                                                            jstring origin) {
    const char *originStr;
    //将jstring转化成char *类型
    originStr = env->GetStringUTFChars(origin, 0);
    MD5 md5 = MD5(originStr);
    std::string md5Result = md5.hexdigest();
    //将char *类型转化成jstring返回给Java层
    return env->NewStringUTF(md5Result.c_str());
}