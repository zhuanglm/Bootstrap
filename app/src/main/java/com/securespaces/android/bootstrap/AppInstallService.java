package com.securespaces.android.bootstrap;

import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.securespaces.android.ssm.SpaceIntent;
import com.securespaces.android.ssm.SpacesManager;
import com.securespaces.android.ssm.UserUtils;

import java.io.File;
import java.util.HashSet;

/**
 * Handles the downloading and installation of recommended apps selected by the user.
 */
public class AppInstallService extends Service {
    public static final String EXTRA_DOWNLOAD_URL = "extra_download_url";
    private static final String MIME_APK = "application/vnd.android.package-archive";

    private HashSet<Long> mDownloadIds;
    private DownloadManager mDownloadManager;

    BroadcastReceiver mDownloadReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
            if (mDownloadIds.contains(downloadId)) {
                onDownloadComplete(downloadId);
            }
        }
    };

    @Override
    public void onCreate() {
        mDownloadIds = new HashSet<>();
        mDownloadManager = (DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);

        IntentFilter intentFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(mDownloadReceiver, intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mDownloadReceiver);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) {
            return START_STICKY;
        }
        String url = intent.getStringExtra(EXTRA_DOWNLOAD_URL);
        if (url == null) {
            return START_STICKY;
        }
        Uri source = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(source);
        request.setMimeType(MIME_APK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }
        String[] segments = source.getPath().split("/");
        String fileName = segments[segments.length-1];

        request.setTitle(fileName.replaceAll(".apk",""));
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
        mDownloadIds.add(mDownloadManager.enqueue(request));

        return START_STICKY;
    }

    private void onDownloadComplete(long downloadId) {
        Uri downloadUri = null;
        // get the path of the downloaded file
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(downloadId);
        Cursor cursor = mDownloadManager.query(query);
        if (cursor == null) {
            //abort, something has gone terribly wrong
            return;
        }

        try {
            if (!cursor.moveToFirst()) {
                //abort, something has gone terribly wrong
                return;
            }
            int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
            if (DownloadManager.STATUS_SUCCESSFUL == cursor.getInt(columnIndex)) {
                String uriString = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                Uri uri = Uri.parse(uriString);
                downloadUri = uri;
            } else {
                return;
            }
        } finally {
            cursor.close();
        }

        File file = new File(downloadUri.getPath());

        // in sdk16 or higher, we can use SpacesManager to install the app without asking the user.
        String requiredSdkVersion = "com.securespaces.android.sdk16";
        if (getPackageManager().hasSystemFeature(requiredSdkVersion)) {
            SpacesManager spacesManager = new SpacesManager(this);
            spacesManager.installApp(downloadUri, UserUtils.myUserHandle());
        } else {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(file), MIME_APK);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
