package com.securespaces.wizard;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.webkit.WebResourceResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class UrlCache {

    private static final String TAG = UrlCache.class.getSimpleName();

    public static final long ONE_SECOND = 1000L;
    public static final long ONE_MINUTE = 60L * ONE_SECOND;
    public static final long ONE_HOUR = 60L * ONE_MINUTE;
    public static final long ONE_DAY = 24 * ONE_HOUR;
    public static final long MAX_AGE = ONE_DAY;

    protected Activity activity = null;
    protected File rootDir = null;

    public UrlCache(Activity activity) {
        this.activity = activity;
        this.rootDir = this.activity.getFilesDir();
    }

    public UrlCache(Activity activity, File rootDir) {
        this.activity = activity;
        this.rootDir = rootDir;
    }

    public WebResourceResponse load(String url) {
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        File cachedFile = new File(this.rootDir.getPath() + File.separator + url.hashCode());

        if (cachedFile.exists()) {
            long cacheEntryAge = System.currentTimeMillis() - cachedFile.lastModified();
            if (cacheEntryAge > MAX_AGE && networkInfo!=null && networkInfo.isConnected()) {
                //Cache is too old, and internet is available to get new data
                cachedFile.delete();

                //cached file deleted, call load() again.
                Log.d(TAG, "Deleting from cache: " + url);
                return load(url);
            }

            //cached file exists and is not too old OR no internet. Return file.
            Log.d(TAG, "Loading from cache: " + url);
            try {
                String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(url));
                return new WebResourceResponse(
                        mimeType, "UTF-8", new FileInputStream(cachedFile));
            } catch (FileNotFoundException e) {
                Log.d(TAG, "Error loading cached file: " + cachedFile.getPath() + " : "
                        + e.getMessage(), e);
            }

        } else if (networkInfo != null && networkInfo.isConnected()) {
            //if there is network and no up to date cache exists, download page in background
            new AsyncTask<String, Void, Void>() {
                @Override
                protected Void doInBackground(String... url) {
                    try {
                        downloadAndStore(url[0]);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, url);
            return null;
        }
        //No data in the cache and no internet, there's nothing that can be done.
        return null;
    }


    public void downloadAndStore(String url)
            throws IOException {
        URL urlObj = new URL(url);
        URLConnection urlConnection = urlObj.openConnection();
        InputStream urlInput = urlConnection.getInputStream();

        FileOutputStream fileOutputStream =
                this.activity.openFileOutput(""+url.hashCode(), Context.MODE_PRIVATE);

        int data = urlInput.read();
        while (data != -1) {
            fileOutputStream.write(data);

            data = urlInput.read();
        }

        urlInput.close();
        fileOutputStream.close();
        Log.d(TAG, "Cache url: " +url+" stored. ");
    }
}