package com.lh.updateapk

import android.Manifest
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent
import android.content.BroadcastReceiver
import android.content.Context
import android.view.View
import com.lh.updateapk.utils.InfoUtils
import com.lh.updateapk.utils.UpdateCheckTask
import com.lh.updateapk.service.DownloadService
import com.lh.updateapk.Constants.SERVICE_RECEIVER
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Color
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.widget.Toast

/**
 * Created by liuhe on 18/06/19.
 */
class MainActivity : AppCompatActivity(), UpdateCheckTask.OnCheckListener {

    private lateinit var mUpdateInfo: UpdateCheckTask.UpdateInfo
    private var mDownloadIntent: Intent? = null
    private var mReceiver: ProgressReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        txt_main_version.text =
                "versionName：${InfoUtils.getVersionName(this)} " +
                "versionCode：${InfoUtils.getVersionCode(this)}"
        txt_main_version.setBackgroundColor(Color.GREEN)
        btn_main_update_message.setOnClickListener {
            UpdateCheckTask(this@MainActivity, this).execute()
        }
        btn_main_update.setOnClickListener {
            update()
        }
        mReceiver = ProgressReceiver()
        val intentFilter = IntentFilter()
        intentFilter.addAction(SERVICE_RECEIVER)
        registerReceiver(mReceiver, intentFilter)
    }

    private val REQUESTPERMISSION = 110

    private fun update() {
        mDownloadIntent = Intent(this, DownloadService::class.java)
        mDownloadIntent?.putExtra(Constants.APK_DOWNLOAD_URL, mUpdateInfo?.mUrl)
        mDownloadIntent?.putExtra(Constants.APK_MD5, mUpdateInfo?.mMD5)
        mDownloadIntent?.putExtra(Constants.APK_DIFF_UPDATE, mUpdateInfo?.mDiffUpdate)
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, listOf(Manifest.permission.WRITE_EXTERNAL_STORAGE).toTypedArray(),REQUESTPERMISSION)
        } else {
            startService(mDownloadIntent)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUESTPERMISSION) {
            if (permissions[0] == Manifest.permission.WRITE_EXTERNAL_STORAGE) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (mDownloadIntent != null)
                        startService(mDownloadIntent)
                } else {
                    //提示没有权限，安装不了咯
                }
            }
        }
    }

    override fun preCheck() {
    }

    override fun onSuccess(info: UpdateCheckTask.UpdateInfo?) {
        mUpdateInfo = info!!
        txt_main_update_message.text =
                "升级信息：${info?.mMessage}\n " +
                "versionName：${info?.mVersionName}\n" +
                "versionCode：${info?.mVersionCode}"

        btn_main_update.visibility = if (InfoUtils.getVersionCode(this) >= info?.mVersionCode!!) View.GONE else View.VISIBLE
    }

    override fun onFailed() {
        txt_main_update_message.text = "升级信息：暂无"
    }

    override fun onDestroy() {
        if (mDownloadIntent != null) {
            stopService(mDownloadIntent)
        }
        unregisterReceiver(mReceiver)
        super.onDestroy()
    }

    /**
     * 下载进度
     */
    inner class ProgressReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val progress = intent.getIntExtra(Constants.UPDATE_DOWNLOAD_PROGRESS, 0)
            progress_main_update.progress = progress
        }
    }
}
