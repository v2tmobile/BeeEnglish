package com.ahiho.apps.beeenglish.view;

import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ahiho.apps.beeenglish.R;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanQRActivity extends BaseActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr);
        mScannerView = new ZXingScannerView(this);
        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.content_frame);
        contentFrame.addView(mScannerView);
        Log.e("RESPONSE", SystemClock.elapsedRealtime()+"");
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        String data =  "Contents = " + rawResult.getText() +
                "\n Format = " + rawResult.getBarcodeFormat().toString();
        Toast.makeText(this,data, Toast.LENGTH_SHORT).show();
        // Note:
        // * Wait 2 seconds to resume the preview.
        // * On older devices continuously stopping and resuming camera preview can result in freezing the app.
        // * I don't know why this is the case but I don't have the time to figure out.
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mScannerView.resumeCameraPreview(ScanQRActivity.this);
            }
        }, 2000);
    }
}
