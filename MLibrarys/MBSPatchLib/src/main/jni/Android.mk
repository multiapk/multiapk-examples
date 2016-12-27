LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

APP_PLATFORM := android-14
TARGET_PLATFORM := android-23

LOCAL_MODULE    := bspatch
LOCAL_SRC_FILES := bspatch.c

include $(PREBUILT_SHARED_LIBRARY)
include $(CLEAR_VARS)

LOCAL_LDLIBS    := -llog

include $(BUILD_SHARED_LIBRARY)