package net.kamradtfamily.myapplication;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

public class NetworkActivity extends FragmentActivity implements DownloadCallback {

    private static final String LogTag = "NetworkActivity";

    // Keep a reference to the NetworkFragment, which owns the AsyncTask object
    // that is used to execute network ops.
    private NetworkFragment mNetworkFragment;

    // Boolean telling us whether a download is in progress, so we don't trigger overlapping
    // downloads with consecutive button clicks.
    private boolean mDownloading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network);

        Log.i(LogTag, "in onCreate");
        mNetworkFragment = NetworkFragment.getInstance(this, "https://www.google.com");
    }

    /** Called when the user taps the Send button */
    public void sendMessage(View view) {
        if (!mDownloading && mNetworkFragment != null) {
            // Execute the async download.
            mNetworkFragment.startDownload();
            mDownloading = true;
        }
    }
    private void startDownload() {
    }

    @Override
    public void updateFromDownload(Object result) {

    }

    @Override
    public NetworkInfo getActiveNetworkInfo() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo;
    }

    @Override
    public void onProgressUpdate(int progressCode, int percentComplete) {
        switch(progressCode) {
            // You can add UI behavior for progress updates here.
            case Progress.ERROR:
                Log.i(LogTag, "in onProgressUpdate ERROR");
                break;
            case Progress.CONNECT_SUCCESS:
                Log.i(LogTag, "in onProgressUpdate CONNECT_SUCCESS");
                break;
            case Progress.GET_INPUT_STREAM_SUCCESS:
                Log.i(LogTag, "in onProgressUpdate GET_INPUT_STREAM_SUCCESS");
                break;
            case Progress.PROCESS_INPUT_STREAM_IN_PROGRESS:
                Log.i(LogTag, "in onProgressUpdate PROCESS_INPUT_STREAM_IN_PROGRESS");
                break;
            case Progress.PROCESS_INPUT_STREAM_SUCCESS:
                Log.i(LogTag, "in onProgressUpdate PROCESS_INPUT_STREAM_SUCCESS");
                break;
        }
    }

    @Override
    public void finishDownloading() {
        mDownloading = false;
        if (mNetworkFragment != null) {
            mNetworkFragment.cancelDownload();
        }
    }
}
