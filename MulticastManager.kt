package com.blessedyoung.lancast
import android.content.Context
import android.net.wifi.WifiManager
class MulticastManager(context:Context){private val wifi=context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager;private var lock:WifiManager.MulticastLock?=null
 fun acquire(){if(lock==null)lock=wifi.createMulticastLock("lancast").apply{setReferenceCounted(true);acquire()}}
 fun release(){lock?.let{if(it.isHeld)it.release()};lock=null}}
