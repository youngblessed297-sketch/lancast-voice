package com.blessedyoung.lancast

class OpusEncoder {
 private var handle=0L
 fun start(){handle=nativeCreateEncoder(48000,1,24000); check(handle!=0L){"Opus native encoder unavailable"}}
 fun encode(pcm:ShortArray):ByteArray{val out=ByteArray(400); val n=nativeEncode(handle,pcm,pcm.size,out); check(n>0){"Opus encode failed"}; return out.copyOf(n)}
 fun stop(){if(handle!=0L){nativeDestroyEncoder(handle);handle=0}}
 private external fun nativeCreateEncoder(rate:Int,channels:Int,bitrate:Int):Long
 private external fun nativeEncode(handle:Long,pcm:ShortArray,samples:Int,out:ByteArray):Int
 private external fun nativeDestroyEncoder(handle:Long)
 companion object { init { try{System.loadLibrary("lancastopus")}catch(_:UnsatisfiedLinkError){}} }
}

class OpusDecoder {
 private var handle=0L
 fun start(){handle=nativeCreateDecoder(48000,1);check(handle!=0L){"Opus native decoder unavailable"}}
 fun decode(packet:ByteArray):ShortArray{val out=ShortArray(960);val n=nativeDecode(handle,packet,packet.size,out);check(n>0){"Opus decode failed"};return out.copyOf(n)}
 fun stop(){if(handle!=0L){nativeDestroyDecoder(handle);handle=0}}
 private external fun nativeCreateDecoder(rate:Int,channels:Int):Long
 private external fun nativeDecode(handle:Long,packet:ByteArray,length:Int,out:ShortArray):Int
 private external fun nativeDestroyDecoder(handle:Long)
 companion object { init { try{System.loadLibrary("lancastopus")}catch(_:UnsatisfiedLinkError){}} }
}
