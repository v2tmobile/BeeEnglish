package com.ahiho.apps.beeenglish.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.ahiho.apps.beeenglish.R;
import com.ahiho.apps.beeenglish.util.UtilActivity;
import com.ahiho.apps.beeenglish.util.UtilString;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class MainActivity extends BaseActivity {

    private TextView tvForgotPassword;
    private EditText etEmail, etPassword;
    private Button btSignIn, btSignUp;
    private LoginButton btSignInFacebook;
    private CallbackManager callbackManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        FacebookSdk.sdkInitialize(MainActivity.this);
        setContentView(R.layout.activity_main);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btSignIn = (Button) findViewById(R.id.btSignIn);
        btSignUp = (Button) findViewById(R.id.btSignUp);
        btSignInFacebook = (LoginButton) findViewById(R.id.btSignInFacebook);
        tvForgotPassword = (TextView) findViewById(R.id.tvForgotPassword);

        setViewShowSnackBar(etEmail);

        btSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        btSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,SignUpActivity.class));
            }
        });

        callbackManager = CallbackManager.Factory.create();
        btSignInFacebook.setReadPermissions("public_profile", "user_birthday");
        btSignInFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
//                    "User ID: "
//                            + loginResult.getAccessToken().getUserId()
//                            + "\n" +
//                            "Auth Token: "
//                            + loginResult.getAccessToken().getToken()
                handleSignInFacebook(loginResult.getAccessToken(), loginResult.getAccessToken().getUserId());

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

    }

    private void signIn() {
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        if (!email.contains(" ")) {
            if (password.length() > 4) {
                actionSignIn(email,password);
            } else {
                etPassword.setError(getString(R.string.err_password_short));
                etPassword.requestFocus();
            }
        } else {
            etEmail.setError(getString(R.string.err_username_invalid));
            etEmail.requestFocus();
        }
    }









    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (callbackManager != null)
            callbackManager.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);

    }

    private void handleSignInFacebook(AccessToken accessToken, final String socialId) {
        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        // Application code
                        String name = "";
                        String link = "";
                        String gender = "";
                        try {
                            name = object.getString("name");
                            link = "http://graph.facebook.com/" + socialId + "/picture?width=100&height=100";
                            gender = object.getString("gender");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        String birthday = "";

                        try {
                            birthday = formatDateFacebook(object.getString("birthday"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        int sex = 0;
                        if (gender.equals("female") || gender.equals("nữ")) {
                            sex = 1;
                        }
                        String socialIdNew = socialId;

                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,link,gender,birthday,age_range,picture.width(150).height(150)");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private String formatDateFacebook(String birthday) {
        DateFormat inputFormat = new SimpleDateFormat("MM/dd/yyyy");
        inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date parsed = new Date();
        try {
            parsed = inputFormat.parse(birthday);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String outputText = outputFormat.format(parsed);
        return outputText;
    }


}
