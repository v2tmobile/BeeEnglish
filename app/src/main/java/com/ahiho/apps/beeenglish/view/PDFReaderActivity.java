package com.ahiho.apps.beeenglish.view;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.ahiho.apps.beeenglish.R;
import com.ahiho.apps.beeenglish.util.Identity;
import com.ahiho.apps.beeenglish.util.UtilSharedPreferences;
import com.joanzapata.pdfview.PDFView;
import com.joanzapata.pdfview.listener.OnPageChangeListener;

import java.io.File;

public class PDFReaderActivity extends Activity {
    private PDFView pdfView;
    private UtilSharedPreferences mUtilSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfreader);
        pdfView = (PDFView) findViewById(R.id.pdfview);
        mUtilSharedPreferences = UtilSharedPreferences.getInstanceSharedPreferences(PDFReaderActivity.this);
        final String uri = getIntent().getStringExtra(Identity.EXTRA_PDF_FILE_NAME);
        File file = new File(uri);
        if (file.exists()) {

            pdfView.fromFile(file)
//                    .pages(0, 2, 1, 3, 3, 3)
                    .defaultPage(mUtilSharedPreferences.getBookLastPosition(uri))
                    .showMinimap(false)
                    .enableSwipe(true)
//                .onDraw(onDrawListener)
//                .onLoad(onLoadCompleteListener)
                    .onPageChange(new OnPageChangeListener() {
                        @Override
                        public void onPageChanged(int page, int pageCount) {
                            mUtilSharedPreferences.setBookLastPosition(uri, page);
                        }
                    })
                    .load();
        } else {
            Toast.makeText(this, R.string.not_download_file, Toast.LENGTH_LONG).show();
            finish();
        }


    }
}
