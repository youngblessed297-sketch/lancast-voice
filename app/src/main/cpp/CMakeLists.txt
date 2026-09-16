cmake_minimum_required(VERSION 3.22.1)
project(lancastopus)

add_library(lancastopus SHARED opus_jni.cpp)

# Point this at a vendored/build output of libopus for each Android ABI.
# Example expected layout:
#   app/src/main/jniLibs/arm64-v8a/libopus.so
#   app/src/main/jniLibs/armeabi-v7a/libopus.so
#   app/src/main/jniLibs/x86_64/libopus.so

add_library(opus SHARED IMPORTED)
set_target_properties(opus PROPERTIES
    IMPORTED_LOCATION "${CMAKE_SOURCE_DIR}/../jniLibs/${ANDROID_ABI}/libopus.so"
    INTERFACE_INCLUDE_DIRECTORIES "${CMAKE_SOURCE_DIR}/opus/include"
)

target_link_libraries(lancastopus opus log)
