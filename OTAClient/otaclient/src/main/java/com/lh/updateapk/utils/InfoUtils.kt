package com.lh.updateapk.utils

import android.content.Context
import android.content.pm.PackageManager
/**
 * Created by liuhe on 18/06/19.
 */
object InfoUtils {
    fun getVersionCode(context: Context?): Int {
        if (context != null) {
            try {
                return context.packageManager.getPackageInfo(context.packageName, 0).versionCode
            } catch (ignored: PackageManager.NameNotFoundException) {
            }
        }
        return 0
    }

    fun getVersionName(context: Context?): String {
        if (context != null) {
            try {
                return context.packageManager.getPackageInfo(context.packageName, 0).versionName
            } catch (ignored: PackageManager.NameNotFoundException) {
            }
        }
        return ""
    }

    fun getBaseApkPath(context: Context): String? {
        val pkName = context.packageName
        try {
            val appInfo = context.packageManager.getApplicationInfo(pkName, 0)
            return appInfo.sourceDir
        } catch (e: PackageManager.NameNotFoundException) {
        }

        return null
    }
}
