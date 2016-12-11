package com.ahiho.apps.beeenglish.view.dialog;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.ahiho.apps.beeenglish.R;
import com.ahiho.apps.beeenglish.util.Identity;

public class StatusDialog extends FragmentActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_status);
        init();
    }

    public void init() {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int screenWidth = (int) (metrics.widthPixels * 0.85);
        getWindow().setLayout(screenWidth, LinearLayout.LayoutParams.WRAP_CONTENT);
        Intent intent = getIntent();
        String title = intent.getStringExtra(Identity.EXTRA_STATUS_TITLE);
        String text = intent.getStringExtra(Identity.EXTRA_STATUS_TEXT);
        boolean status = intent.getBooleanExtra(Identity.EXTRA_STATUS_BOOLEAN, false);
        Button btOk = (Button) findViewById(R.id.btOk);
        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        TextView tvText = (TextView) findViewById(R.id.tvText);

        if(status){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                tvTitle.setTextColor(getColor(R.color.colorSuccess));
            }else{
                tvTitle.setTextColor(Color.parseColor("#4caf50"));
            }
        }
        tvTitle.setText(title);
        tvText.setText(text);
        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


}
