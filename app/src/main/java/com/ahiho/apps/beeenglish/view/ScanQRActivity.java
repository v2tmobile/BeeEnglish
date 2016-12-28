package com.ahiho.apps.beeenglish.view;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ahiho.apps.beeenglish.R;
import com.ahiho.apps.beeenglish.model.ResponseData;
import com.ahiho.apps.beeenglish.util.MyConnection;
import com.ahiho.apps.beeenglish.util.UtilSharedPreferences;
import com.google.zxing.Result;

import org.json.JSONException;
import org.json.JSONObject;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanQRActivity extends BaseActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;
    private MyConnection mMyConnection;
    private UtilSharedPreferences mUtilSharedPreferences;
    private Dialog dialogActive;
    private TextView tvContent;
    private ImageView ivSuccess;
    private RelativeLayout rlLoading;
    private LinearLayout llButton;
    private Button btTryAgain,btCancel;
    private boolean isActive=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr);
        mScannerView = new ZXingScannerView(this);
        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.content_frame);
        contentFrame.addView(mScannerView);
        mMyConnection = MyConnection.getInstanceMyConnection(ScanQRActivity.this);
        mUtilSharedPreferences = UtilSharedPreferences.getInstanceSharedPreferences(ScanQRActivity.this);
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
//        String data =  "Contents = " + rawResult.getText() +
//                "\n Format = " + rawResult.getBarcodeFormat().toString();
//        Toast.makeText(this,data, Toast.LENGTH_SHORT).show();
//        // Note:
//        // * Wait 2 seconds to resume the preview.
//        // * On older devices continuously stopping and resuming camera preview can result in freezing the app.
//        // * I don't know why this is the case but I don't have the time to figure out.

        showDialogActive(rawResult.getText());

    }

    private void showDialogActive(String data) {
        if (!mUtilSharedPreferences.getActiveData().isEmpty()) {
            finish();
            return;
        }
        if (dialogActive != null && dialogActive.isShowing())
            return;


        //demo
        if (mUtilSharedPreferences.getTrialTimeExpired() <= 0) {
            mUtilSharedPreferences.setTrialTimeExpired(System.currentTimeMillis() + 3600000);
        }

        dialogActive = new Dialog(ScanQRActivity.this);
        dialogActive.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogActive.setCanceledOnTouchOutside(true);
        dialogActive.setContentView(R.layout.dialog_active_loading);
        dialogActive.setCanceledOnTouchOutside(true);
        ivSuccess = (ImageView) dialogActive.findViewById(R.id.ivSuccess);
        tvContent = (TextView) dialogActive.findViewById(R.id.tvContent);
        rlLoading= (RelativeLayout) dialogActive.findViewById(R.id.rlLoading);
        llButton= (LinearLayout) dialogActive.findViewById(R.id.llButton);
        btTryAgain = (Button) dialogActive.findViewById(R.id.btTryAgain);
        btCancel= (Button) dialogActive.findViewById(R.id.btCancel);


        btTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogActive.dismiss();
            }
        });

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogActive.dismiss();
                finish();
            }
        });
        dialogActive.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if(isActive){
                    finish();
                }else{
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mScannerView.resumeCameraPreview(ScanQRActivity.this);
                        }
                    }, 500);
                }
            }
        });
        dialogActive.show();
        try {
            JSONObject jsonObject = new JSONObject(data);
            new ActiveKey(jsonObject.getString("key"),jsonObject.getString("agency_id")).execute();
        } catch (JSONException e) {
            invalidCode(getString(R.string.err_qr_not_match));
        }
    }


    private void invalidCode(String errorData){
        rlLoading.setVisibility(View.GONE);
        llButton.setVisibility(View.VISIBLE);
        tvContent.setVisibility(View.VISIBLE);
        ivSuccess.setVisibility(View.VISIBLE);
        tvContent.setText(errorData);
        ivSuccess.setImageResource(R.drawable.ic_warning_white_48dp);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            tvContent.setTextColor(getColor(R.color.colorError));
            ivSuccess.setColorFilter(getColor(R.color.colorError));
        } else {
            tvContent.setTextColor(Color.parseColor("#cc0000"));
            ivSuccess.setColorFilter(Color.parseColor("#cc0000"));
        }
        btTryAgain.setVisibility(View.VISIBLE);
    }
    private void validCode(){
        rlLoading.setVisibility(View.GONE);
        llButton.setVisibility(View.VISIBLE);
        tvContent.setVisibility(View.VISIBLE);
        ivSuccess.setVisibility(View.VISIBLE);
    }

    class ActiveKey extends AsyncTask<Void, Void, ResponseData> {
        private String activeKey,agencyId;
        public ActiveKey(String activeKey, String agencyId){
            this.activeKey=activeKey;
            this.agencyId=agencyId;
        }
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected ResponseData doInBackground(Void... params) {
            try {
                JSONObject jsonObject = new JSONObject(mUtilSharedPreferences.getUserData());
                String userName =jsonObject.getString("username");
                Log.e("RESPONSE",userName+"_"+activeKey+"_"+agencyId);
                return mMyConnection.activeKey(userName,activeKey,agencyId);
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(ResponseData responseData) {
            if (responseData != null) {
                if (responseData.isResponseState()) {
                    try {
                        JSONObject jsonObject = new JSONObject(responseData.getResponseData());
                        if (jsonObject.getBoolean("success")) {
                            isActive=true;
                            validCode();
//                            mUtilSharedPreferences.setActiveData(jsonObject.getString("data"));
                        } else {
                            invalidCode(responseData.getResponseData());
                        }
                    } catch (JSONException e) {
                        showToast(R.string.err_json_exception, Toast.LENGTH_LONG);
                        finish();
                    }
                } else {
                    invalidCode(responseData.getResponseData());
                }
            }
            super.onPostExecute(responseData);
        }
    }

}
