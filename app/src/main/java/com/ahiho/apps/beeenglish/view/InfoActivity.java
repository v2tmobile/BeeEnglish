package com.ahiho.apps.beeenglish.view;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ahiho.apps.beeenglish.R;
import com.ahiho.apps.beeenglish.model.ResponseData;
import com.ahiho.apps.beeenglish.my_interface.OnCallbackSnackBar;
import com.ahiho.apps.beeenglish.util.Identity;
import com.ahiho.apps.beeenglish.util.MyConnection;
import com.ahiho.apps.beeenglish.util.MyFile;
import com.ahiho.apps.beeenglish.util.UtilSharedPreferences;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class InfoActivity extends BaseActivity {

    private TextView tvBarDisplayName, tvBarPhone;
    private ImageView ivDisplayName, ivPhone;
    private CircleImageView ivAvatar;
    private ImageButton btEditDisplayName, btEditPhone, btBack;
    private Button btUpdate;
    private EditText etDisplayName, etPhone;
    private final int DEFAULT_ID = -1;
    private int currentEdit = DEFAULT_ID;
    private final int RESULT_SELECT_IMAGE = 21;
    private UtilSharedPreferences mUtilSharedPreferences;
    private Uri selectedImage = null;
    private String mUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUtilSharedPreferences = UtilSharedPreferences.getInstanceSharedPreferences(InfoActivity.this);
        init();
    }

    private void init() {
        setContentView(R.layout.activity_info);
        ivAvatar = (CircleImageView) findViewById(R.id.ivAvatar);
        tvBarDisplayName = (TextView) findViewById(R.id.tvBarDisplayName);
        ivDisplayName = (ImageView) findViewById(R.id.ivDisplayName);
        btBack = (ImageButton) findViewById(R.id.btBack);
        btUpdate = (Button) findViewById(R.id.btUpdate);
        btEditDisplayName = (ImageButton) findViewById(R.id.btEditDisplayName);
        etDisplayName = (EditText) findViewById(R.id.etDisplayName);
        tvBarPhone = (TextView) findViewById(R.id.tvBarPhone);
        ivPhone = (ImageView) findViewById(R.id.ivPhone);
        btEditPhone = (ImageButton) findViewById(R.id.btEditPhone);
        etPhone = (EditText) findViewById(R.id.etPhone);
        try {
            JSONObject jsonObject = new JSONObject(mUtilSharedPreferences.getUserData());
            mUserId = jsonObject.getString("id");
            Picasso.with(InfoActivity.this).load(jsonObject.getString("avatar")).error(R.drawable.default_avatar).into(ivAvatar);
            String name = "";
            String firstName = jsonObject.getString("first_name");
            String lastName = jsonObject.getString("last_name");
            if (firstName != null && !firstName.isEmpty() && !firstName.equals("null"))
                name += firstName;
            if (lastName != null && !lastName.isEmpty() && !lastName.equals("null"))
                name += lastName;
            if (!name.isEmpty())
                etDisplayName.setText(name);
            else {
                etDisplayName.setText(jsonObject.getString("email"));
            }
            String phone = jsonObject.getString("mobile");
            if (!phone.isEmpty() && !phone.equals("null")) {
                etPhone.setText(phone);
            } else {

            }
        } catch (JSONException e) {

        }

        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_SELECT_IMAGE);
            }
        });

        btEditDisplayName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editDisplayName();
            }
        });

        btEditPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editPhone();
            }
        });

        etDisplayName.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    editDisplayName();
                    return true;
                }
                return false;
            }
        });
        etPhone.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    editPhone();
                    return true;
                }
                return false;
            }
        });

        btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyConnection.isOnline(InfoActivity.this)) {
                    String name = etDisplayName.getText().toString();
                    String phone = etPhone.getText().toString();
                    if (name.isEmpty()) {
                        etDisplayName.setError(getString(R.string.err_edit_text_empty));
                    } else {
                        if (phone.isEmpty()) {
                            etPhone.setError(getString(R.string.err_edit_text_empty));
                        } else {
                            new UpdateInfo(name, phone).execute();
                        }
                    }
                } else {
                    mSnackbar.showTextAction(R.string.err_connection_fail, R.string.bt_try_connection, new OnCallbackSnackBar() {
                        @Override
                        public void onAction() {
                            ActivityCompat.requestPermissions(InfoActivity.this,
                                    new String[]{Manifest.permission.CHANGE_WIFI_STATE}, Identity.REQUEST_PERMISSION_CHANGE_WIFI_STATE);
                        }
                    });
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RESULT_SELECT_IMAGE:

                if (resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
                    try {
                        selectedImage = data.getData();
//                        String[] filePathColumn = {MediaStore.Images.Media.DATA };
//                        Cursor cursor = getContentResolver().query(selectedImage,
//                                filePathColumn, null, null, null);
//                        cursor.moveToFirst();
//                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                        String picturePath = cursor.getString(columnIndex);
//                        cursor.close();

                        Picasso.with(InfoActivity.this).load(selectedImage).error(R.drawable.default_avatar).into(ivAvatar);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Intent returnFromGalleryIntent = new Intent();
                        setResult(RESULT_CANCELED, returnFromGalleryIntent);
                        finish();
                    }
                }
                break;
        }
    }

    private void editPhone() {
        if (currentEdit != R.id.btEditPhone) {
            currentEdit = R.id.btEditPhone;
            defaultDisplayName();
            tvBarPhone.setVisibility(View.VISIBLE);
            etPhone.setEnabled(true);
            focusEditText(etPhone);

            btEditPhone.setImageResource(R.drawable.ic_check_circle_white_48dp);
        } else {
            currentEdit = DEFAULT_ID;
            defaultPhone();
        }
    }

    private void editDisplayName() {
        if (currentEdit != R.id.btEditDisplayName) {
            currentEdit = R.id.btEditDisplayName;
            defaultPhone();
            tvBarDisplayName.setVisibility(View.VISIBLE);
            etDisplayName.setEnabled(true);
            focusEditText(etDisplayName);
            btEditDisplayName.setImageResource(R.drawable.ic_check_circle_white_48dp);
        } else {
            currentEdit = DEFAULT_ID;
            defaultDisplayName();
        }
    }

    private void focusEditText(EditText editText) {
        if(editText.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void defaultDisplayName() {
        tvBarDisplayName.setVisibility(View.INVISIBLE);
        etDisplayName.setEnabled(false);
        btEditDisplayName.setImageResource(R.drawable.ic_edit_white_24dp);
    }

    private void defaultPhone() {
        tvBarPhone.setVisibility(View.INVISIBLE);
        etPhone.setEnabled(false);
        btEditPhone.setImageResource(R.drawable.ic_edit_white_24dp);
    }

    class UpdateInfo extends AsyncTask<Void, Void, ResponseData> {
        private int reqWidth, reqHeight, MAX_IMAGE_SIZE;
        private String name, phone;

        public UpdateInfo(String name, String phone) {
            this.name = name;
            this.phone = phone;
            reqWidth = ivAvatar.getWidth();
            reqHeight = ivAvatar.getHeight();
            MAX_IMAGE_SIZE = reqWidth;
            if (reqHeight > reqWidth)
                MAX_IMAGE_SIZE = reqHeight;
        }

        @Override
        protected void onPreExecute() {
            showDialogLoading();
        }

        @Override
        protected ResponseData doInBackground(Void... params) {
            String avatar = "";
//            An chua thuc hien update avatar
//            try {
//                if (selectedImage != null) {
//                    BitmapFactory.Options options = new BitmapFactory.Options();
//                    options.inJustDecodeBounds = false;
//                    options.inPreferredConfig = Bitmap.Config.RGB_565;
//                    options.inDither = true;
//                    BitmapFactory.decodeFile(MyFile.convertUri2FileUri(selectedImage.toString()), options);
//                    options.inSampleSize = MyFile.calculateInSampleSize(options, reqWidth, reqHeight);
//                    options.inJustDecodeBounds = false;
//                    Bitmap bm = null;
//
//                    bm = BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, options);
//                    bm = MyFile.scaleDown(bm, MAX_IMAGE_SIZE, true);
//                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                    bm.compress(Bitmap.CompressFormat.JPEG, 40, baos); //bm is the bitmap object
//                    byte[] byteArrayImage = baos.toByteArray();
//                    avatar = "data:image/jpeg;base64,"+ com.ahiho.apps.beeenglish.util.Base64.encodeBytes(byteArrayImage, Base64.DEFAULT)+"=";
//                }
//            } catch (Exception e) {
//            }
            return MyConnection.getInstanceMyConnection(InfoActivity.this).updateInfo(mUserId, avatar, name, phone);
        }

        @Override
        protected void onPostExecute(ResponseData responseData) {
            dismissDialogLoading();
            try {
                if (responseData.isResponseState()) {
                    JSONObject jsonObject = new JSONObject(responseData.getResponseData());
                    if (jsonObject.getBoolean("success")) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        JSONObject userData= new JSONObject(mUtilSharedPreferences.getUserData());
                        userData.put("first_name",data.getString("first_name"));
                        userData.put("last_name",data.getString("last_name"));
                        userData.put("avatar",data.getString("avatar"));
                        mUtilSharedPreferences.setUserData(userData.toString());
                        showToast(R.string.success_update_info,Toast.LENGTH_LONG);
                        finish();
                    } else {
                        loadFail(responseData.getResponseData());
                    }
                } else {
                    loadFail(responseData.getResponseData());
                }
            } catch (JSONException e) {

                loadFail(getString(R.string.err_json_exception));
            }
            super.onPostExecute(responseData);
        }
    }

    private void loadFail(String text) {
        finish();
        showToast(text, Toast.LENGTH_LONG);
    }
}
