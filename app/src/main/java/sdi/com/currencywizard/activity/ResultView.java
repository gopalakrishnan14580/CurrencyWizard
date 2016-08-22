package sdi.com.currencywizard.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import sdi.com.currencywizard.R;


public class ResultView extends Activity {

    TextView result;

    String result_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_view);

        result_view= getIntent().getExtras().getString("result");

        result=(TextView) findViewById(R.id.result);

        result.setText(result_view);


    }
}
