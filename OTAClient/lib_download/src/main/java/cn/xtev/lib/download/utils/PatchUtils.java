package cn.xtev.lib.download.utils;

/**
 * APK Patch工具类
 *
 * @author liuhe
 * @date 2018-06-23
 */
public class PatchUtils {
    static {
        System.loadLibrary("bspatch");
    }

    /**
     * native方法 使用路径为oldApkPath的apk与路径为patchPath的补丁包，合成新的apk，并存储于newApkPath
     * <p>
     * 返回：0，说明操作成功
     *
     * @param oldApk 示例:/sdcard/old.apk
     * @param newApk 示例:/sdcard/new.apk
     * @param patch  示例:/sdcard/xx.patch
     * @return
     */
    public static native int bspatch(String oldApk, String newApk, String patch);
}