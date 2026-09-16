#include <jni.h>
#include <opus/opus.h>
#include <cstdint>
#include <new>

extern "C" JNIEXPORT jlong JNICALL
Java_com_blessedyoung_lancast_OpusEncoder_nativeCreateEncoder(
        JNIEnv*, jobject, jint rate, jint channels, jint bitrate) {
    int err = OPUS_OK;
    OpusEncoder* enc = opus_encoder_create(rate, channels, OPUS_APPLICATION_VOIP, &err);
    if (!enc || err != OPUS_OK) return 0;
    opus_encoder_ctl(enc, OPUS_SET_BITRATE(bitrate));
    opus_encoder_ctl(enc, OPUS_SET_VBR(0));
    return reinterpret_cast<jlong>(enc);
}

extern "C" JNIEXPORT jint JNICALL
Java_com_blessedyoung_lancast_OpusEncoder_nativeEncode(
        JNIEnv* env, jobject, jlong handle, jshortArray pcm, jint samples, jbyteArray output) {
    auto* enc = reinterpret_cast<OpusEncoder*>(handle);
    if (!enc) return OPUS_BAD_ARG;
    jshort* in = env->GetShortArrayElements(pcm, nullptr);
    jbyte* out = env->GetByteArrayElements(output, nullptr);
    const jsize cap = env->GetArrayLength(output);
    int n = opus_encode(enc, reinterpret_cast<const opus_int16*>(in), samples,
                        reinterpret_cast<unsigned char*>(out), cap);
    env->ReleaseShortArrayElements(pcm, in, JNI_ABORT);
    env->ReleaseByteArrayElements(output, out, 0);
    return n;
}

extern "C" JNIEXPORT void JNICALL
Java_com_blessedyoung_lancast_OpusEncoder_nativeDestroyEncoder(
        JNIEnv*, jobject, jlong handle) {
    if (handle) opus_encoder_destroy(reinterpret_cast<OpusEncoder*>(handle));
}

extern "C" JNIEXPORT jlong JNICALL
Java_com_blessedyoung_lancast_OpusDecoder_nativeCreateDecoder(
        JNIEnv*, jobject, jint rate, jint channels) {
    int err = OPUS_OK;
    OpusDecoder* dec = opus_decoder_create(rate, channels, &err);
    return (dec && err == OPUS_OK) ? reinterpret_cast<jlong>(dec) : 0;
}

extern "C" JNIEXPORT jint JNICALL
Java_com_blessedyoung_lancast_OpusDecoder_nativeDecode(
        JNIEnv* env, jobject, jlong handle, jbyteArray packet, jint length, jshortArray output) {
    auto* dec = reinterpret_cast<OpusDecoder*>(handle);
    if (!dec) return OPUS_BAD_ARG;
    jbyte* in = env->GetByteArrayElements(packet, nullptr);
    jshort* out = env->GetShortArrayElements(output, nullptr);
    const int maxSamples = env->GetArrayLength(output);
    int n = opus_decode(dec, reinterpret_cast<const unsigned char*>(in), length,
                        reinterpret_cast<opus_int16*>(out), maxSamples, 0);
    env->ReleaseByteArrayElements(packet, in, JNI_ABORT);
    env->ReleaseShortArrayElements(output, out, 0);
    return n;
}

extern "C" JNIEXPORT void JNICALL
Java_com_blessedyoung_lancast_OpusDecoder_nativeDestroyDecoder(
        JNIEnv*, jobject, jlong handle) {
    if (handle) opus_decoder_destroy(reinterpret_cast<OpusDecoder*>(handle));
}
