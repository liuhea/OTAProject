package com.lh.updateapk.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.annotations.SerializedName;
import com.lh.updateapk.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by liuhe on 18/06/19.
 */
public class UpdateCheckTask extends AsyncTask<Void, Void, String> {

    private final String TAG = "UpdateCheckTask";
    Context mContext;
    OnCheckListener mListener = null;

    public UpdateCheckTask(Context context, OnCheckListener listener) {
        this.mContext = context;
        this.mListener = listener;
    }

    @Override
    protected void onPreExecute() {
        Log.i(TAG, "onPreExecute()");
        if (mListener != null) {
            mListener.preCheck();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        UpdateInfo info = GsonUtils.jsonToBean(result, UpdateInfo.class);
        if (this.mListener != null) {
            this.mListener.onSuccess(info);
        }
    }

    @Override
    protected String doInBackground(Void... voids) {
        HttpURLConnection uRLConnection = null;
        InputStream is = null;
        BufferedReader buffer = null;
        String result = null;
        String urlStr = Constants.UPDATE_REQUEST_URL + "?" + Constants.APK_VERSION_CODE+ "=" + InfoUtils.INSTANCE.getVersionCode(this.mContext);

        try {
            URL url = new URL(urlStr);
            uRLConnection = (HttpURLConnection) url.openConnection();
            uRLConnection.setRequestMethod("GET");

            is = uRLConnection.getInputStream();
            buffer = new BufferedReader(new InputStreamReader(is));
            StringBuilder strBuilder = new StringBuilder();
            String line;
            while ((line = buffer.readLine()) != null) {
                strBuilder.append(line);
            }
            result = strBuilder.toString();
        } catch (Exception e) {
            Log.e(TAG, "http post error");
        } finally {
            if (buffer != null) {
                try {
                    buffer.close();
                } catch (IOException ignored) {
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ignored) {
                }
            }
            if (uRLConnection != null) {
                uRLConnection.disconnect();
            }
        }

        if (result != null) {
            Log.i(TAG, result);
        }

        return result;
    }

    public interface OnCheckListener {
        void preCheck();

        void onSuccess(UpdateInfo info);

        void onFailed();
    }

    public class UpdateInfo {
        @SerializedName("updateMessage")
        public String mMessage;
        @SerializedName("url")
        public String mUrl;
        @SerializedName("versionName")
        public String mVersionName;
        @SerializedName("versionCode")
        public int mVersionCode;
        @SerializedName("md5")
        public String mMD5;

        public boolean mDiffUpdate;
    }
}
