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
import android.widget.TextView;

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
        mNetworkFragment = NetworkFragment.getInstance(getFragmentManager(), "https://api.rlksr.com/");
    }

    /** Called when the user taps the Send button */
    public void sendMessage(View view) {
        if (!mDownloading && mNetworkFragment != null) {
            // Execute the async download.
            mNetworkFragment.startDownload();
            mDownloading = true;
        }
    }

    @Override
    public void updateFromDownload(Object obj) {
        Log.i(LogTag, "in updateFromDownload");
        NetworkFragment.DownloadTask.Result result = (NetworkFragment.DownloadTask.Result) obj;
        TextView textView = findViewById(R.id.textView2);
        if(result.mException != null) {
            Log.w(LogTag, "in updateFromDownload exception = " + result.mException);
            textView.setText(result.mException.getMessage());
        } else {
            Log.i(LogTag, "in updateFromDownload result = " + result.mResultValue);
            textView.setText(result.mResultValue);
        }

    }

    @Override
    public NetworkInfo getActiveNetworkInfo() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        Log.i(LogTag, "in getActiveNetworkInfo networkInfo = " + networkInfo);
        return networkInfo;
    }

    @Override
    public void onProgressUpdate(int progressCode, int percentComplete) {
        TextView textView = findViewById(R.id.textView2);
        Log.i(LogTag, "in onProgressUpdate progressCode = " + progressCode + " percentComplete = " + percentComplete);

        switch(progressCode) {
            // You can add UI behavior for progress updates here.
            case Progress.ERROR:
                textView.setText("in onProgressUpdate ERROR");
                break;
            case Progress.CONNECT_SUCCESS:
                textView.setText("in onProgressUpdate CONNECT_SUCCESS");
                break;
            case Progress.GET_INPUT_STREAM_SUCCESS:
                textView.setText("in onProgressUpdate GET_INPUT_STREAM_SUCCESS");
                break;
            case Progress.PROCESS_INPUT_STREAM_IN_PROGRESS:
                textView.setText("in onProgressUpdate PROCESS_INPUT_STREAM_IN_PROGRESS");
                break;
            case Progress.PROCESS_INPUT_STREAM_SUCCESS:
                textView.setText("in onProgressUpdate PROCESS_INPUT_STREAM_SUCCESS");
                break;
        }
    }

    @Override
    public void finishDownloading() {
        Log.i(LogTag, "in finishDownloading mNetworkFragment = " + mNetworkFragment);
        mDownloading = false;
        if (mNetworkFragment != null) {
            mNetworkFragment.cancelDownload();
        }
    }
}
