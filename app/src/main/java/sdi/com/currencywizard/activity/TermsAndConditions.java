package sdi.com.currencywizard.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import sdi.com.currencywizard.R;

public class TermsAndConditions extends Activity {

    ImageView condition_back;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.terms_and_conditions);


        condition_back=(ImageView) findViewById(R.id.condition_back);

        webView = (WebView) findViewById(R.id.terms_conditions);
        webView.loadData(readTextFromResource(R.raw.terms_and_conditions), "text/html", "utf-8");

        condition_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(condition_back.getWindowToken(),
                        InputMethodManager.RESULT_UNCHANGED_SHOWN);
                finish();
            }
        });

    }

    private String readTextFromResource(int resourceID) {
        InputStream raw = getResources().openRawResource(resourceID);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        int i;
        try {
            i = raw.read();

            while (i != -1)

            {
                stream.write(i);

                i = raw.read();

            }

            raw.close();
        } catch (IOException e)

        {
            e.printStackTrace();
        }
        return stream.toString();
    }

}
