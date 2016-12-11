package com.ahiho.apps.beeenglish.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ahiho.apps.beeenglish.R;
import com.ahiho.apps.beeenglish.model.ResponseData;
import com.ahiho.apps.beeenglish.util.Identity;
import com.ahiho.apps.beeenglish.util.MyConnection;

import org.json.JSONException;
import org.json.JSONObject;

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


        btSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = etUserName.getText().toString();
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                if (!userName.contains(" ")) {
                    if (password.length() > 4) {
                        if(isOnline())
                            new SignUp(userName, email, password).execute();
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


    class SignUp extends AsyncTask<Void, Void, ResponseData> {

        private String mUserName, mEmail, mPassword;

        public SignUp(String... params) {
            mUserName = params[0];
            mEmail = params[1];
            mPassword = params[2];
        }

        @Override
        protected void onPreExecute() {
            showDialogLoading();

        }

        @Override
        protected ResponseData doInBackground(Void... params) {
            return MyConnection.getInstanceMyConnection(SignUpActivity.this).signUp(mUserName, mEmail, mPassword);
        }

        @Override
        protected void onPostExecute(ResponseData responseData) {
            dismissDialog();
            if (responseData.isResponseState()) {
                        postSignUp(responseData,mUserName,mPassword);
            } else {
                showSnackBar(responseData.getResponseData());
            }
            super.onPostExecute(responseData);
        }
    }

    private void postSignUp(ResponseData responseData,String userName,String password) {
        try {
            JSONObject jsonObject = new JSONObject(responseData.getResponseData());
            if (jsonObject.getBoolean("success")) {
                showSnackBar(R.string.success_sign_up);
                Intent returnIntent = new Intent();
                returnIntent.putExtra(Identity.EXTRA_USER_NAME,userName);
                returnIntent.putExtra(Identity.EXTRA_PASSWORD,password);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        } catch (JSONException e) {
            showSnackBar(R.string.err_json_exception);
        }
    }

}
