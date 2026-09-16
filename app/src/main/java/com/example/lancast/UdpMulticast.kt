package com.blessedyoung.lancast

import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.MulticastSocket

const val MULTICAST_ADDRESS="239.1.1.1"
const val MULTICAST_PORT=5000

class UdpMulticastSender {
 private val socket=DatagramSocket(); private val group=InetAddress.getByName(MULTICAST_ADDRESS)
 fun send(data:ByteArray){socket.send(DatagramPacket(data,data.size,group,MULTICAST_PORT))}
 fun close(){socket.close()}
}

class UdpMulticastReceiver {
 private var socket:MulticastSocket?=null
 fun start(onPacket:(RtpPacket)->Unit){val s=MulticastSocket(MULTICAST_PORT);socket=s;val group=InetAddress.getByName(MULTICAST_ADDRESS);s.joinGroup(group);Thread{val buf=ByteArray(1500);while(!s.isClosed){try{val p=DatagramPacket(buf,buf.size);s.receive(p);RtpPacket.parse(p.data,p.length)?.let(onPacket)}catch(_:Exception){}}}}.start()}
 fun stop(){socket?.let{try{it.leaveGroup(InetAddress.getByName(MULTICAST_ADDRESS))}catch(_:Exception){};it.close()};socket=null}
}
