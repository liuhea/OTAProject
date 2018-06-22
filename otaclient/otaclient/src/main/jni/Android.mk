LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)
LOCAL_MODULE:= bspatch # 指定了生成的动态链接库的名字
LOCAL_SRC_FILES:= bspatch.c  # 指定了C的源文件叫什么名字
APP_ALLOW_MISSING_DEPS := true
include $(BUILD_SHARED_LIBRARY) # 指定要生成动态链接库