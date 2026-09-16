package com.blessedyoung.lancast
import android.media.*

class AudioEngine {
 companion object {const val RATE=48000;const val FRAME=960}
 private var record:AudioRecord?=null;private var track:AudioTrack?=null;@Volatile private var running=false
 fun capture(onFrame:(ShortArray)->Unit){val min=AudioRecord.getMinBufferSize(RATE,AudioFormat.CHANNEL_IN_MONO,AudioFormat.ENCODING_PCM_16BIT);record=AudioRecord(MediaRecorder.AudioSource.VOICE_COMMUNICATION,RATE,AudioFormat.CHANNEL_IN_MONO,AudioFormat.ENCODING_PCM_16BIT,maxOf(min,FRAME*8));running=true;record!!.startRecording();Thread{val f=ShortArray(FRAME);while(running){var o=0;while(o<FRAME&&running){val n=record!!.read(f,o,FRAME-o);if(n>0)o+=n else break};if(o==FRAME)onFrame(f.copyOf())}}.start()}
 fun stopCapture(){running=false;record?.stop();record?.release();record=null}
 fun play(pcm:ShortArray){if(track==null){track=AudioTrack.Builder().setAudioAttributes(AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_VOICE_COMMUNICATION).setContentType(AudioAttributes.CONTENT_TYPE_SPEECH).build()).setAudioFormat(AudioFormat.Builder().setSampleRate(RATE).setEncoding(AudioFormat.ENCODING_PCM_16BIT).setChannelMask(AudioFormat.CHANNEL_OUT_MONO).build()).setBufferSizeInBytes(FRAME*8).build();track!!.play()};track!!.write(pcm,0,pcm.size)}
 fun stopPlayback(){track?.stop();track?.release();track=null}
}
