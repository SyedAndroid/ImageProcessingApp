package com.idt.syed.imageapplication;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import cz.msebera.android.httpclient.Header;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 */
public class NetworkIntentService extends IntentService {

    public static final String LOG_TAG = "NetworkIntentService";
    public static final String INTENT_URL = "INTENT_URL";


    private final AsyncHttpClient aClient = new SyncHttpClient();
    // we use a synchronous call so that the service waits for the download to finish. In case of Async call the service will finish with an error without
    //waiting for async call back



    public NetworkIntentService() {
        super("NetworkIntentService");
    }

    @Override
    public void onStart(Intent intent, int startId) {
        Log.d(LOG_TAG, "onStart()");
        super.onStart(intent, startId);
    }


    @Override
    protected void onHandleIntent(final Intent intent) {
        if (intent != null  && intent.hasExtra(INTENT_URL)) {

            aClient.get(this, intent.getStringExtra(INTENT_URL), new AsyncHttpResponseHandler() {

                @Override
                public void onStart() {
                    // called before request is started
                    Log.d(LOG_TAG, "onStart");

                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                    ResultReceiver resultReceiver = intent.getParcelableExtra("receiver");
                    Bundle bundle = new Bundle();
                    bundle.putByteArray("resultValue",responseBody);
                    resultReceiver.send(Activity.RESULT_OK,bundle);// when we get the results back we notify the reciever and get the data back in our main activity
                    Log.d(LOG_TAG, "onSuccess");

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    ResultReceiver resultReceiver = intent.getParcelableExtra("receiver");
                    Bundle bundle = new Bundle();
                    bundle.putByteArray("resultValue",responseBody);
                    resultReceiver.send(statusCode,bundle);
                    Log.d(LOG_TAG, "failure");
                }
                @Override
                public void onRetry(int retryNo) {
                    // called when request is retried

                }
            });

        }
    }



}
