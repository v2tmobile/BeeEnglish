package com.ahiho.apps.beeenglish.view.dialog;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.UnderlineSpan;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ahiho.apps.beeenglish.R;
import com.ahiho.apps.beeenglish.util.UtilString;
import com.ahiho.apps.beeenglish.view.BaseActivity;
import com.ahiho.apps.beeenglish.view.ScanQRActivity;

public class ActiveDialog extends BaseActivity {

    private TextView tvScanQR;
    private EditText etCode;
    private ImageButton btCancel;
    private LinearLayout llScanQR;
    private Button btBuy,btOk;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_active);
        init();
    }

    @Override
    public void finish() {
        super.finish();
    }

    public void init() {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int screenWidth = (int) (metrics.widthPixels * 0.9);
        getWindow().setLayout(screenWidth, LinearLayout.LayoutParams.WRAP_CONTENT);

        llScanQR = (LinearLayout) findViewById(R.id.llScanQR);
        tvScanQR = (TextView) findViewById(R.id.tvScanQR);
        etCode = (EditText) findViewById(R.id.etCode);
        btOk = (Button) findViewById(R.id.btOk);
        btCancel = (ImageButton) findViewById(R.id.btCancel);
        SpannableString content = new SpannableString(getString(R.string.active_scan_qr));
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        tvScanQR.setText(content);

        etCode.addTextChangedListener(new TextWatcher() {
            boolean isEdiging;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(isEdiging) return;
                isEdiging = true;
                // removing old dashes
                StringBuilder sb = new StringBuilder();
                sb.append(s.toString().replace("-", ""));

                if (sb.length()> 3)
                    sb.insert(3, "-");
                if (sb.length()> 8)
                    sb.insert(8, "-");
                if (sb.length()> 13)
                    sb.insert(13, "-");
                s.replace(0, s.length(), sb.toString().toUpperCase());
                isEdiging = false;
            }

        });


        llScanQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ActiveDialog.this, ScanQRActivity.class));
            }
        });

        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }









}

