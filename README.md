# LANCAST Native Opus JNI Pack

Native JNI bridge for libopus, intended for the LANCAST Android project.

- 48 kHz mono
- VOIP application mode
- 24 kbps target bitrate
- 20 ms frames (960 samples)
- Encoder and decoder JNI functions
- CMake/NDK build integration

The Opus implementation should be vendored from the official Xiph.Org Opus project and built with the Android NDK. The bridge does not include prebuilt proprietary binaries.

Official source: https://github.com/xiph/opus
