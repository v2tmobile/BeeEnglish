package com.ahiho.apps.beeenglish.view;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ahiho.apps.beeenglish.R;

public class AboutActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        init();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void init() {
        TextView tvContent = (TextView) findViewById(R.id.tvContent);
        TextView tvTerm = (TextView) findViewById(R.id.tvTerm);
        String text =getString(R.string.app_name)+"\u2122"
                +"\n\n" +getString(R.string.copy_right_of)
                +"\n\n" +getString(R.string.about_content);

        tvContent.setText(text);
        SpannableString content = new SpannableString(getString(R.string.term_text));
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        tvTerm.setText(content);
        tvTerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Button btOk = (Button) findViewById(R.id.btOk);
        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
