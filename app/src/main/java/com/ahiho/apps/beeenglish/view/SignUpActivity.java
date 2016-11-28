package com.ahiho.apps.beeenglish.view;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ahiho.apps.beeenglish.R;
import com.ahiho.apps.beeenglish.util.MyConnection;
import com.ahiho.apps.beeenglish.util.UtilActivity;

import static com.ahiho.apps.beeenglish.util.MyConnection.noConnectInternet;
import static com.ahiho.apps.beeenglish.util.MyConnection.openConnectWifi;

public class SignUpActivity extends BaseActivity {

    private ImageButton btBack, btHelp;
    private EditText etUserName, etEmail, etPassword;
    private Button btSignUp;
    private TextView tvAlreadyAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        init();
    }

    private void init() {
        btBack = (ImageButton) findViewById(R.id.btBack);
        btHelp = (ImageButton) findViewById(R.id.btHelp);
        etUserName = (EditText) findViewById(R.id.etUserName);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        tvAlreadyAccount = (TextView) findViewById(R.id.tvAlreadyAccount);
        btSignUp = (Button) findViewById(R.id.btSignUp);

        setViewShowSnackBar(etEmail);

        btSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = etUserName.getText().toString();
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                if (!userName.contains(" ")) {
                    if (password.length() > 4) {
                        actionSignUp(userName,email,password);
                    } else {
                        etPassword.setError(getString(R.string.err_password_short));
                        etPassword.requestFocus();
                    }
                } else {
                    etUserName.setError(getString(R.string.err_username_invalid));
                    etUserName.requestFocus();
                }
            }
        });

        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvAlreadyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }



}
