# Build notes

1. Vendor the official Xiph Opus source under `app/src/main/cpp/opus`.
2. Build libopus for each Android ABI with the Android NDK.
3. Place the resulting shared libraries at:
   `app/src/main/jniLibs/<ABI>/libopus.so`
4. Keep the Opus headers under `app/src/main/cpp/opus/include`.
5. Add this module's CMake file to the Android app's `externalNativeBuild` configuration.
6. Build the APK.

Recommended codec settings for LANCAST voice:
- 48000 Hz
- mono
- OPUS_APPLICATION_VOIP
- 24000 bps CBR
- 20 ms frames

The official Opus implementation supports interactive speech and is designed for real-time transmission. See the upstream project for its license and build instructions.
