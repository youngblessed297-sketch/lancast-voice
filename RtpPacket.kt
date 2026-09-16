package com.blessedyoung.lancast

import java.nio.ByteBuffer
import java.nio.ByteOrder

data class RtpPacket(val sequence:Int,val timestamp:Long,val ssrc:Long,val payload:ByteArray){
 fun toBytes():ByteArray{ val b=ByteBuffer.allocate(12+payload.size).order(ByteOrder.BIG_ENDIAN); b.put(0x80.toByte()); b.put(111.toByte()); b.putShort(sequence.toShort()); b.putInt(timestamp.toInt()); b.putInt(ssrc.toInt()); b.put(payload); return b.array() }
 companion object { fun parse(data:ByteArray,length:Int):RtpPacket?{ if(length<12)return null; val b=ByteBuffer.wrap(data,0,length).order(ByteOrder.BIG_ENDIAN); val v=b.get().toInt() and 255; val pt=b.get().toInt() and 255; if(v shr 6 !=2 || (pt and 127)!=111)return null; val seq=b.short.toInt() and 65535; val ts=b.int.toLong() and 0xffffffffL; val ssrc=b.int.toLong() and 0xffffffffL; val p=ByteArray(length-12); b.get(p); return RtpPacket(seq,ts,ssrc,p) } }
}
