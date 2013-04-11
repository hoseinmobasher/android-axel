#
# Compile axel
#
LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
APP_SUBDIRS := intl/src axel
LOCAL_MODULE    := axel
LOCAL_SRC_FILES := $(foreach F, $(APP_SUBDIRS), $(addprefix $(F)/,$(notdir $(wildcard $(LOCAL_PATH)/$(F)/*.c)))) test-axel.c
LOCAL_C_INCLUDES := $(LOCAL_PATH)/axel
LOCAL_LDLIBS 	:= -llog
LOCAL_CFLAGS := -I$(LOCAL_PATH)/axel -DBUILDING_LIBINTL -DBUILDING_DLL \
                -DIN_LIBINTL -DENABLE_RELOCATABLE=1 -DIN_LIBRARY \
                -Dset_relocation_prefix=libintl_set_relocation_prefix \
                -Drelocate=libintl_relocate -DDEPENDS_ON_LIBICONV=1 \
                -DNO_XMALLOC -DHAVE_CONFIG_H \
                -DLOCALEDIR=\"/usr/local/share/locale\" \
                -DLIBDIR=\"/usr/local/lib\" \
                -DLOCALE_ALIAS_PATH=\"/usr/local/share/locale\" \
                -DINSTALLDIR=\"/usr/local/lib\"
include $(BUILD_SHARED_LIBRARY)

