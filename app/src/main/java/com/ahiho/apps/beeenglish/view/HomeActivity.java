package com.ahiho.apps.beeenglish.view;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ahiho.apps.beeenglish.R;
import com.ahiho.apps.beeenglish.util.CircleTransform;
import com.ahiho.apps.beeenglish.util.UtilSharedPreferences;
import com.ahiho.apps.beeenglish.util.UtilString;
import com.ahiho.apps.beeenglish.view.communication.CommunicationActivity;
import com.ahiho.apps.beeenglish.view.dialog.ActiveDialog;
import com.ahiho.apps.beeenglish.view.dialog.FirstDownloadDialog;
import com.ahiho.apps.beeenglish.view.sample.SampleActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.realm.Realm;
import io.realm.RealmResults;

public class HomeActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private NavigationView mNavigationView;
    private DrawerLayout mDrawerLayout;
    private RelativeLayout llHeader;
    private ImageView ivSignOut;
    private ImageView ivAvatar;
    private TextView tvDisplayName, tvTime;
    private TextView tvEmail;
    //    private RecyclerView rvUsually, rvRecent;
    private UtilSharedPreferences mUtilSharedPreferences;
    private Dialog dialogTrial;
    private final int REQ_UPDATE = 100;
    private Button btDictionary, btBook, btTest, btSample, btSkill, btGrammar, btCommunicate, btVocabulary;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        init();
        grantPermissionReadWriteFile();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        mUtilSharedPreferences = UtilSharedPreferences.getInstanceSharedPreferences(HomeActivity.this);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);
        llHeader = (RelativeLayout) mNavigationView.getHeaderView(0).findViewById(R.id.llHeader);
        ivSignOut = (ImageView) mNavigationView.getHeaderView(0).findViewById(R.id.ivSignOut);
        ivAvatar = (ImageView) mNavigationView.getHeaderView(0).findViewById(R.id.ivAvatar);
        tvDisplayName = (TextView) mNavigationView.getHeaderView(0).findViewById(R.id.tvDisplayName);
        tvEmail = (TextView) mNavigationView.getHeaderView(0).findViewById(R.id.tvEmail);

//        btDictionary,btBook,btTest,btSample,btSkill,btGrammar,btCommunicate,btVocabulary
        btDictionary = (Button) findViewById(R.id.btDictionary);
        btBook = (Button) findViewById(R.id.btBook);
        btTest = (Button) findViewById(R.id.btTest);
        btSample = (Button) findViewById(R.id.btSample);
        btSkill = (Button) findViewById(R.id.btSkill);
        btGrammar = (Button) findViewById(R.id.btGrammar);
        btCommunicate = (Button) findViewById(R.id.btCommunicate);
        btVocabulary = (Button) findViewById(R.id.btVocabulary);

        initOnClick();

        loadDataUser();
        if (!mUtilSharedPreferences.isUpdateFirst()) {
            Intent intent = new Intent(HomeActivity.this, FirstDownloadDialog.class);
            startActivityForResult(intent, REQ_UPDATE);
        } else {
            showDialogTrial();
        }
    }

    private void initOnClick() {
        btDictionary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidUser()) {
                    Intent intent = new Intent(HomeActivity.this, DictionaryActivity.class);
                    startActivity(intent);
                } else {
                    showInvalidError();
                }
            }
        });
        btBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidUser()) {
                    Intent intent = new Intent(HomeActivity.this, BooksActivity.class);
                    startActivity(intent);
                } else {
                    showInvalidError();
                }
            }
        });
        btTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidUser()) {
//                    Intent intent = new Intent(HomeActivity.this, DictionaryActivity.class);
//                    startActivity(intent);
                } else {
                    showInvalidError();
                }
            }
        });
        btSample.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidUser()) {
                    Intent intent = new Intent(HomeActivity.this, SampleActivity.class);
                    startActivity(intent);
                } else {
                    showInvalidError();
                }
            }
        });
        btSkill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidUser()) {
//                    Intent intent = new Intent(HomeActivity.this, DictionaryActivity.class);
//                    startActivity(intent);
                } else {
                    showInvalidError();
                }
            }
        });
        btGrammar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidUser()) {
                    Intent intent = new Intent(HomeActivity.this, GrammarActivity.class);
                    startActivity(intent);
                } else {
                    showInvalidError();
                }
            }
        });
        btCommunicate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidUser()) {
                    Intent intent = new Intent(HomeActivity.this, CommunicationActivity.class);
                    startActivity(intent);
                } else {
                    showInvalidError();
                }
            }
        });
        btVocabulary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidUser()) {
                    Intent intent = new Intent(HomeActivity.this, VocabularyActivity.class);
                    startActivity(intent);
                } else {
                    showInvalidError();
                }
            }
        });

    }

    private void showInvalidError() {
    }

    private boolean isValidUser() {
        long time = mUtilSharedPreferences.getTrialTimeExpired() - System.currentTimeMillis();
        if (time > 0) {
            return true;
        } else {
            Toast.makeText(this, R.string.err_trial_time_action, Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void loadDataUser() {
        try {
            JSONObject jsonObject = new JSONObject(mUtilSharedPreferences.getUserData());

            tvDisplayName.setText(jsonObject.getString("email"));
            String uri = jsonObject.getString("avatar");
            if (uri != null && !uri.isEmpty() && !uri.equals("null")) {
                Picasso.with(HomeActivity.this).load(uri).transform(new CircleTransform()).error(R.drawable.default_avatar).into(ivAvatar);
            } else {
                Picasso.with(HomeActivity.this).load(R.drawable.default_avatar).transform(new CircleTransform()).into(ivAvatar);
            }
            ivSignOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mUtilSharedPreferences.signOut();
                    startActivity(new Intent(HomeActivity.this, MainActivity.class));
                    finish();
                }
            });
        } catch (JSONException e) {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_UPDATE) {
            showDialogTrial();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_active:
                startActivity(new Intent(HomeActivity.this, ScanQRActivity.class));//ActiveDialog
                break;
            case R.id.nav_backup:
                break;
            case R.id.nav_restore:
                break;
        }
        mDrawerLayout.closeDrawers();
        return true;
    }


    private void showDialogTrial() {
        String activeInfo= mUtilSharedPreferences.getActiveData();
        if (!activeInfo.isEmpty()) {
            //start
            return;
        }
        if (dialogTrial != null && dialogTrial.isShowing())
            return;

        //demo
        if (mUtilSharedPreferences.getTrialTimeExpired() <= 0) {
            mUtilSharedPreferences.setTrialTimeExpired(System.currentTimeMillis() + 3600000);
        }

        dialogTrial = new Dialog(HomeActivity.this);
        dialogTrial.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogTrial.setContentView(R.layout.dialog_trial);
        dialogTrial.setCanceledOnTouchOutside(true);
        tvTime = (TextView) dialogTrial.findViewById(R.id.tvTime);
        Button btOk = (Button) dialogTrial.findViewById(R.id.btOk);
        Button btCancel = (Button) dialogTrial.findViewById(R.id.btCancel);
        btOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, ScanQRActivity.class));
                dialogTrial.dismiss();
            }
        });

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogTrial.dismiss();
            }
        });
        long trialTime = mUtilSharedPreferences.getTrialTimeExpired();
        long time = trialTime - System.currentTimeMillis();
        if (time > 0) {
            final Timer myTimer = new Timer();
            myTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            long time = mUtilSharedPreferences.getTrialTimeExpired() - System.currentTimeMillis();
                            if (time > 0) {
                                tvTime.setText(UtilString.convertTime(time));
                            } else {
                                setTextExpired(tvTime);
                                myTimer.cancel();
                            }
                        }
                    });

                }

            }, 0, 1000);
            dialogTrial.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    if (myTimer != null)
                        myTimer.cancel();
                }
            });
        } else {
            setTextExpired(tvTime);
        }

        dialogTrial.show();
    }

    private void setTextExpired(TextView tvTime) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            tvTime.setTextColor(getColor(R.color.colorError));
        } else {
            tvTime.setTextColor(Color.parseColor("#cc0000"));
        }
        tvTime.setText(getString(R.string.err_trial_time));

    }
}
