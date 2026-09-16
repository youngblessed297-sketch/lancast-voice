# LANCAST Voice v0.2 — Opus + RTP + UDP Multicast

This version adds the real transport architecture:

**Microphone → 48 kHz mono PCM → Opus JNI → RTP → UDP multicast → LAN**

Default group: `239.1.1.1:5000`
Frame: 20 ms / 960 samples
Target Opus bitrate: 24 kbps

## Important build note
Android does not expose a simple built-in API for this exact low-latency Opus encode/decode pipeline. `OpusCodec.kt` therefore defines a JNI boundary for a native `libopus` implementation. The transport and RTP code is ready, but the native `libopus` + JNI bridge still needs to be supplied under `app/src/main/jniLibs/<ABI>/` before real Opus encoding works.

This is intentional: it prevents shipping a project that claims to have Opus while silently using a different codec.

## LAN / hotspot
Internet is not required. Devices need to be on the same LAN. Some Android hotspots/Wi-Fi networks block multicast; in that case a future unicast fallback should be added.
