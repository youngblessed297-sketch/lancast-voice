package com.blessedyoung.lancast

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.MotionEvent
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.concurrent.atomic.AtomicInteger

class MainActivity:AppCompatActivity(){
 private lateinit var status:TextView
 private val audio=AudioEngine(); private lateinit var lock:MulticastManager; private lateinit var receiver:UdpMulticastReceiver
 private var sender:UdpMulticastSender?=null;private var encoder:OpusEncoder?=null
 private val seq=AtomicInteger(0);private var timestamp=0L;private val ssrc=System.nanoTime() and 0xffffffffL
 override fun onCreate(b:Bundle?){super.onCreate(b);setContentView(R.layout.activity_main);status=findViewById(R.id.status);val talk=findViewById<Button>(R.id.talkButton);lock=MulticastManager(this);receiver=UdpMulticastReceiver();lock.acquire()
 if(ContextCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO)!=PackageManager.PERMISSION_GRANTED)ActivityCompat.requestPermissions(this,arrayOf(Manifest.permission.RECORD_AUDIO),10)
 receiver.start{rtp->if(rtp.ssrc!=ssrc)runOnUiThread{status.text="RTP received • seq ${rtp.sequence}"}}
 talk.setOnTouchListener{_,e->when(e.action){MotionEvent.ACTION_DOWN->{startTalk();true};MotionEvent.ACTION_UP,MotionEvent.ACTION_CANCEL->{stopTalk();true};else->true}}
 }
 private fun startTalk(){if(sender!=null)return;try{val e=OpusEncoder();e.start();encoder=e;sender=UdpMulticastSender()}catch(_:Exception){status.text="Install native libopus to enable voice";return};status.text="TRANSMITTING"
 audio.capture{pcm->try{val opus=encoder?.encode(pcm)?:return@capture;val p=RtpPacket(seq.getAndIncrement() and 65535,timestamp,ssrc,opus);timestamp=(timestamp+AudioEngine.FRAME) and 0xffffffffL;sender?.send(p.toBytes())}catch(_:Exception){}}}
 private fun stopTalk(){audio.stopCapture();encoder?.stop();encoder=null;sender?.close();sender=null;status.text="Ready — LAN only"}
 override fun onDestroy(){stopTalk();receiver.stop();lock.release();audio.stopPlayback();super.onDestroy()}
}
