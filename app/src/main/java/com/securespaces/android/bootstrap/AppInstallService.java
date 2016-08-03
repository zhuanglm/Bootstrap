package com.securespaces.android.bootstrap;

import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;
import java.util.HashSet;

/**
 * Created by eric on 03/08/16.
 */
public class AppInstallService extends Service {
    public static final String EXTRA_DOWNLOAD_URL = "extra_download_url";

    private HashSet<Long> mDownloadIds;
    private DownloadManager mDownloadManager;

    BroadcastReceiver mDownloadReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
            Log.d("Eric","downloadComplete, id is " + downloadId);
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
        // Make a new request pointing to the .apk url
        DownloadManager.Request request = new DownloadManager.Request(source);
        // appears the same in Notification bar while downloading
        request.setDescription("Description for the DownloadManager Bar");
        request.setTitle("YourApp.apk");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }
        // save the file in the "Downloads" folder of SDCARD
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "SmartPigs.apk");
        // get download service and enqueue file
        mDownloadIds.add(mDownloadManager.enqueue(request));


        return START_STICKY;
    }

    private void onDownloadComplete(long downloadId) {
        Uri downloadUri = mDownloadManager.getUriForDownloadedFile(downloadId);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(downloadUri.getPath())), "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        /*
        @Override
        public void onReceive(Context context, Intent intent) {
            // Prevents the occasional unintentional call. I needed this.
            if (mDownloadedFileID == -1)
                return;
            Intent fileIntent = new Intent(Intent.ACTION_VIEW);

            // Grabs the Uri for the file that was downloaded.
            Uri mostRecentDownload =
                    mDownloadManager.getUriForDownloadedFile(mDownloadedFileID);
            // DownloadManager stores the Mime Type. Makes it really easy for us.
            String mimeType =
                    mDownloadManager.getMimeTypeForDownloadedFile(mDownloadedFileID);
            fileIntent.setDataAndType(mostRecentDownload, mimeType);
            fileIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try {
                mContext.startActivity(fileIntent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(mContext, "No handler for this type of file.",
                        Toast.LENGTH_LONG).show();
            }
            // Sets up the prevention of an unintentional call. I found it necessary. Maybe not for others.
            mDownloadedFileID = -1;
        }
        */
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
