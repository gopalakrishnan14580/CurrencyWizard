package sdi.com.currencywizard.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import sdi.com.currencywizard.R;
import sdi.com.currencywizard.adapter.CountryCurrencyListAdapter;
import sdi.com.currencywizard.database.DatabaseHandler;
import sdi.com.currencywizard.model.Country;
import sdi.com.currencywizard.model.CurrencyApplication;
@SuppressWarnings("deprecation")
public class CurrencyWizard extends Activity implements Comparator<Country> {

    ImageView basket_icon,camera,settings,currency_exchange;
    TextView from_dollar_name_txt,to_dollar_name_txt,from_dollar_txt, from_dollar_name_con_txt,
            to_dollar_txt,to_dollar_name_con_txt,from_conversion_dollar_txt, to_conversion_dollar_txt,
            from_conversion_symbol_txt,to_conversion_symbol_txt,add_to_card;
    EditText from_con_dollar_rate_et,to_con_dollar_rate_et;
    LinearLayout existing_basket,new_basket,country,currency_con,to_linear;

    ImageButton from_flag_name_img,to_flag_name_img;
    RelativeLayout currency_window,mapview;

    String from_con_dollar_rate,to_con_dollar_rate,to_conversion_dollar,from_dollar_name,
    from_dollar_value="USD",to_dollar_value="EUR",from_conversion_symbol,to_conversion_symbol,
            from_dollar_name_con,to_dollar_name_con;

    public static final String MY_PREFS_NAME = "MyPrefsFile";


    //The "x" and "y" position of the "from_flag_name_img" on screen.
    Point p;
    //The "x" and "y" position of the "to_flag_name_img" on screen.
    Point p1;

    private ListView countryListView;

    private CountryCurrencyListAdapter adapter;

    private List<Country> allCountriesList;

    private List<Country> selectedCountriesList;

    RelativeLayout currency_wizard;

    Context context;

    ProgressDialog pd;

    DatabaseHandler db;

    int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.currencywizard);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //progress dialog
        context = this;

        basket_icon=(ImageView) findViewById(R.id.basket_icon);
        from_flag_name_img=(ImageButton) findViewById(R.id.from_flag_name_img);
        to_flag_name_img=(ImageButton) findViewById(R.id.to_flag_name_img);
        camera=(ImageView) findViewById(R.id.camera);
        settings=(ImageView) findViewById(R.id.settings);
        currency_exchange=(ImageView) findViewById(R.id.currency_exchange);

        from_dollar_name_txt=(TextView) findViewById(R.id.from_dollar_name_txt);
        to_dollar_name_txt=(TextView) findViewById(R.id.to_dollar_name_txt);
        from_dollar_txt=(TextView) findViewById(R.id.from_dollar_txt);
        from_dollar_name_con_txt=(TextView) findViewById(R.id.from_dollar_name_con_txt);
        to_dollar_txt=(TextView) findViewById(R.id.to_dollar_txt);
        to_dollar_name_con_txt=(TextView) findViewById(R.id.to_dollar_name_con_txt);
        from_conversion_dollar_txt=(TextView) findViewById(R.id.from_conversion_dollar_txt);
        to_conversion_dollar_txt=(TextView) findViewById(R.id.to_conversion_dollar_txt);
        from_conversion_symbol_txt=(TextView) findViewById(R.id.from_conversion_symbol_txt);
        to_conversion_symbol_txt=(TextView) findViewById(R.id.to_conversion_symbol_txt);

        add_to_card=(TextView) findViewById(R.id.add_to_card);


        existing_basket=(LinearLayout) findViewById(R.id.existing_basket);
        new_basket=(LinearLayout) findViewById(R.id.new_basket);
        country=(LinearLayout) findViewById(R.id.country);
        currency_con=(LinearLayout) findViewById(R.id.currency_con);
        to_linear=(LinearLayout) findViewById(R.id.to_linear);

        from_con_dollar_rate_et=(EditText) findViewById(R.id.from_con_dollar_rate_et);
        to_con_dollar_rate_et=(EditText) findViewById(R.id.to_con_dollar_rate_et);
        currency_window=(RelativeLayout) findViewById(R.id.currency_window);
        mapview=(RelativeLayout) findViewById(R.id.mapview);
        currency_wizard=(RelativeLayout) findViewById(R.id.currency_wizard);

        from_con_dollar_rate_et.setCursorVisible(false);
        to_con_dollar_rate_et.setCursorVisible(false);


        //add to card

       try {
           db = new DatabaseHandler(CurrencyWizard.this);

           count = db.getBasketCount();

           System.out.println("Basket Count :" + count);

           add_to_card.setText(String.valueOf(count));
       }catch (Exception e)
       {
           System.out.println("Exception:"+e.getMessage());
       }


        currency_wizard.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

                to_linear.setBackgroundResource(R.color.white);
                currency_con.setBackgroundResource(R.color.white);
                add_to_card.setVisibility(View.VISIBLE);
                basket_icon.setVisibility(View.VISIBLE);
                from_con_dollar_rate_et.setCursorVisible(false);
                to_con_dollar_rate_et.setCursorVisible(false);
                //hide soft keyboard

                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(currency_wizard.getWindowToken(),
                        InputMethodManager.RESULT_UNCHANGED_SHOWN);

            }
        });


        currency_window.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

                to_linear.setBackgroundResource(R.color.white);
                currency_con.setBackgroundResource(R.color.white);
                add_to_card.setVisibility(View.VISIBLE);
                basket_icon.setVisibility(View.VISIBLE);
                from_con_dollar_rate_et.setCursorVisible(false);
                to_con_dollar_rate_et.setCursorVisible(false);
                //hide soft keyboard

                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(currency_window.getWindowToken(),
                        InputMethodManager.RESULT_UNCHANGED_SHOWN);

            }
        });

        mapview.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

                to_linear.setBackgroundResource(R.color.white);
                currency_con.setBackgroundResource(R.color.white);
                basket_icon.setVisibility(View.VISIBLE);
                from_con_dollar_rate_et.setCursorVisible(false);
                to_con_dollar_rate_et.setCursorVisible(false);

                //hide soft keyboard
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mapview.getWindowToken(),
                        InputMethodManager.RESULT_UNCHANGED_SHOWN);


            }
        });


        camera.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

                Intent activityChangeIntent = new Intent(CurrencyWizard.this, CaptureImage.class);
                startActivity(activityChangeIntent);
                finish();

            }
        });


        try{

            final CurrencyApplication globalVariable = (CurrencyApplication) getApplicationContext();

            final String name  = globalVariable.getCamera_currency();

            if(name!=null)

            from_con_dollar_rate_et.setText(name);
            else
                from_con_dollar_rate_et.setText("1");

        }
        catch (Exception e)
        {
            System.out.println("Exception : "+e.getMessage());
        }


        //retrieve shared preferences value

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        String to_code = prefs.getString("to_code", null);

        String from_code = prefs.getString("from_code", null);

        if (to_code != null && from_code != null) {


            //from flag

            String from_drawableName = "flag_" + from_code.substring(0, 2).toLowerCase(Locale.ENGLISH);

            if (from_drawableName.equals("flag_eu")) from_drawableName = "euro";

            //to flag

            String to_drawableName = "flag_" + to_code.substring(0, 2).toLowerCase(Locale.ENGLISH);

            if (to_drawableName.equals("flag_eu")) to_drawableName = "euro";


            to_flag_name_img.setBackgroundResource(getResId(to_drawableName));

            from_flag_name_img.setBackgroundResource(getResId(from_drawableName));


            from_dollar_name_txt.setText(from_code);

            to_dollar_name_txt.setText(to_code);


            from_conversion_dollar_txt.setText(from_code);

            to_conversion_dollar_txt.setText(to_code);

            to_dollar_name_con_txt.setText(prefs.getString("to_name", null));

            from_dollar_name_con_txt.setText(prefs.getString("from_name", null));


            to_conversion_symbol_txt.setText(prefs.getString("to_symbol_native", null));

            from_conversion_symbol_txt.setText(prefs.getString("from_symbol_native", null));


            to_dollar_value = prefs.getString("to_code", null);

            from_dollar_value = prefs.getString("from_code", null);


            SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
            editor.putString("from_symbol", prefs.getString("from_symbol", null));
            editor.putString("to_symbol", prefs.getString("to_symbol", null));
            editor.putString("from_name", prefs.getString("from_name", null));
            editor.putString("to_name", prefs.getString("to_name", null));
            editor.putString("to_symbol_native", prefs.getString("to_symbol_native", null));
            editor.putString("from_symbol_native", prefs.getString("from_symbol_native", null));
            editor.putString("to_code", to_code);
            editor.putString("from_code", from_code);
            editor.commit();
        }

        currency_exchange.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

                //Toast.makeText(getApplication(), "Currency Exchanged.", Toast.LENGTH_LONG).show();

                pd = new ProgressDialog(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                pd.setMessage("Loading...");
                pd.setCancelable(false);
                pd.show();


                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

                String to_code = prefs.getString("to_code", null);

                String from_code = prefs.getString("from_code", null);

                if (to_code != null && from_code != null) {


                    //from flag

                    String from_drawableName = "flag_"+ from_code.substring(0, 2).toLowerCase(Locale.ENGLISH);

                    if(from_drawableName.equals("flag_eu")) from_drawableName = "euro";

                    //to flag

                    String to_drawableName = "flag_"+ to_code.substring(0, 2).toLowerCase(Locale.ENGLISH);

                    if(to_drawableName.equals("flag_eu")) to_drawableName = "euro";


                    to_flag_name_img.setBackgroundResource(getResId(from_drawableName));

                    from_flag_name_img.setBackgroundResource(getResId(to_drawableName));



                    from_dollar_name_txt.setText(to_code);

                    to_dollar_name_txt.setText(from_code);


                    from_conversion_dollar_txt.setText(to_code);

                    to_conversion_dollar_txt.setText(from_code);

                    to_dollar_name_con_txt.setText(prefs.getString("from_name", null));

                    from_dollar_name_con_txt.setText(prefs.getString("to_name", null));


                    to_conversion_symbol_txt.setText(prefs.getString("from_symbol_native", null));

                    from_conversion_symbol_txt.setText(prefs.getString("to_symbol_native", null));



                    to_dollar_value=prefs.getString("from_code", null);

                    from_dollar_value=prefs.getString("to_code", null);


                    SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                    editor.putString("from_symbol", prefs.getString("to_symbol", null));
                    editor.putString("to_symbol", prefs.getString("from_symbol", null));
                    editor.putString("from_name", prefs.getString("to_name", null));
                    editor.putString("to_name", prefs.getString("from_name", null));
                    editor.putString("to_symbol_native", prefs.getString("from_symbol_native", null));
                    editor.putString("from_symbol_native", prefs.getString("to_symbol_native", null));
                    editor.putString("to_code", from_code);
                    editor.putString("from_code", to_code);
                    editor.commit();

                    closeProgress();

                    if(!isNetworkConnected(getApplicationContext())){

                        Toast.makeText(getApplication(), "Please check your internet connection.", Toast.LENGTH_LONG).show();
                    }

                    else {

                        getCurrencyValue(from_dollar_value,to_dollar_value);
                    }

                }

            }
        });


        from_con_dollar_rate_et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {

                    from_con_dollar_rate_et.setCursorVisible(false);
                    to_linear.setBackgroundResource(R.color.white);
                    currency_con.setBackgroundResource(R.color.white);
                    add_to_card.setVisibility(View.VISIBLE);
                    basket_icon.setVisibility(View.VISIBLE);

                    System.out.println("From Value : ---------> "+from_con_dollar_rate_et.getText().toString());


                    if(!isNetworkConnected(getApplicationContext())){

                        Toast.makeText(getApplication(), "Please check your internet connection.", Toast.LENGTH_LONG).show();
                    }

                    else {

                        getCurrencyValue(from_dollar_value,to_dollar_value);
                    }

                }
                return false;
            }
        });


        to_con_dollar_rate_et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {

                    to_con_dollar_rate_et.setCursorVisible(false);
                    to_linear.setBackgroundResource(R.color.white);
                    currency_con.setBackgroundResource(R.color.white);
                    add_to_card.setVisibility(View.VISIBLE);
                    basket_icon.setVisibility(View.VISIBLE);

                    System.out.println("To Value : ---------> "+to_con_dollar_rate_et.getText().toString());

                    if(!isNetworkConnected(getApplicationContext())){

                        Toast.makeText(getApplication(), "Please check your internet connection.", Toast.LENGTH_LONG).show();
                    }

                    else {

                        getFromCurrencyValue(to_dollar_value,from_dollar_value);
                    }

                }
                return false;
            }
        });

        /*-----------------------------------------------------------------*/
        //store shared preferences value on load activity

        from_conversion_symbol=from_conversion_symbol_txt.getText().toString().trim();
        to_conversion_symbol=to_conversion_symbol_txt.getText().toString().trim();

        from_dollar_name_con=from_dollar_name_con_txt.getText().toString().trim();
        to_dollar_name_con=to_dollar_name_con_txt.getText().toString().trim();


        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString("from_name", from_dollar_name_con);
        editor.putString("to_name", to_dollar_name_con);
        editor.putString("to_symbol_native", to_conversion_symbol);
        editor.putString("from_symbol_native", from_conversion_symbol);
        editor.putString("to_code", to_dollar_value);
        editor.putString("from_code", from_dollar_value);
        editor.commit();


        if(!isNetworkConnected(this)){

            Toast.makeText(getApplication(), "Please check your internet connection.", Toast.LENGTH_LONG).show();
        }

        else {

            getCurrencyValue(from_dollar_value,to_dollar_value);
        }

        /*-------------------------------------------------------*/
        from_flag_name_img.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(from_flag_name_img.getWindowToken(),
                        InputMethodManager.RESULT_UNCHANGED_SHOWN);
                //Open popup window
                if (p != null)
                    fromShowPopup(CurrencyWizard.this, p);

            }
        });


        to_flag_name_img.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(from_flag_name_img.getWindowToken(),
                        InputMethodManager.RESULT_UNCHANGED_SHOWN);
                //Open popup window
                if (p != null)
                    toShowPopup(CurrencyWizard.this, p1);

            }
        });

        //currency rate on change

        from_con_dollar_rate_et.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                to_linear.setBackgroundResource(R.drawable.grey);
                currency_con.setBackgroundResource(R.drawable.grey);
                add_to_card.setVisibility(View.GONE);
                basket_icon.setVisibility(View.GONE);
                to_con_dollar_rate_et.setCursorVisible(true);
                from_con_dollar_rate_et.setCursorVisible(true);

            }
        });



        to_con_dollar_rate_et.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                to_linear.setBackgroundResource(R.drawable.grey);
                currency_con.setBackgroundResource(R.drawable.grey);
                add_to_card.setVisibility(View.GONE);
                basket_icon.setVisibility(View.GONE);
                to_con_dollar_rate_et.setCursorVisible(true);
                from_con_dollar_rate_et.setCursorVisible(true);

            }
        });



        //settings
        settings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

                Intent activityChangeIntent = new Intent(CurrencyWizard.this, Settings.class);

                startActivity(activityChangeIntent);
            }
        });


        //add to existing basket
        existing_basket.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click


                if(count ==0)
                {
                    final Dialog alertDialog = new Dialog(context);
                    alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View convertView = (View) inflater.inflate(R.layout.basket_alert, null);
                    alertDialog.setContentView(convertView);
                    alertDialog.setCanceledOnTouchOutside(false);
                    TextView txtHeader = (TextView) convertView.findViewById(R.id.txtHeader);
                    TextView txtContent = (TextView) convertView.findViewById(R.id.txtContent);
                    Button btnCancel = (Button) convertView.findViewById(R.id.btnCancel);
                    Button btnOk = (Button) convertView.findViewById(R.id.btnOk);
                    View view = (View) convertView.findViewById(R.id.viewSep);
                    btnCancel.setVisibility(View.VISIBLE);
                    view.setVisibility(View.VISIBLE);
                    txtHeader.setVisibility(View.VISIBLE);
                    txtContent.setVisibility(View.VISIBLE);

                    txtHeader.setText("CurrencyWizard");
                    txtContent.setText("Your Basket is empty. Do you want to create a new basket");
                    btnOk.setText("Yes");
                    btnCancel.setText("No");

                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            alertDialog.dismiss();
                        }
                    });

                    btnOk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();

                            from_con_dollar_rate=from_con_dollar_rate_et.getText().toString().trim();
                            to_con_dollar_rate=to_con_dollar_rate_et.getText().toString().trim();
                            to_conversion_dollar=to_conversion_dollar_txt.getText().toString().trim();
                            from_dollar_name=from_dollar_name_txt.getText().toString().trim();
                            from_conversion_symbol=from_conversion_symbol_txt.getText().toString().trim();
                            to_conversion_symbol=to_conversion_symbol_txt.getText().toString().trim();


                            if (from_con_dollar_rate.length() ==0 )
                            {
                                from_con_dollar_rate_et.setError("from dollar");
                            }
                            else if (to_con_dollar_rate.length() ==0)
                            {
                                to_con_dollar_rate_et.setError("to dollar");
                            }
                            else
                            {

                                Intent activityChangeIntent = new Intent(CurrencyWizard.this, AddToNewBasket.class);
                                activityChangeIntent.putExtra("from_con_dollar_rate", from_con_dollar_rate);
                                activityChangeIntent.putExtra("from_dollar_name", from_dollar_name);
                                activityChangeIntent.putExtra("from_conversion_symbol", from_conversion_symbol);
                                activityChangeIntent.putExtra("to_con_dollar_rate", to_con_dollar_rate);
                                activityChangeIntent.putExtra("to_conversion_dollar", to_conversion_dollar);
                                activityChangeIntent.putExtra("to_conversion_symbol", to_conversion_symbol);
                                startActivity(activityChangeIntent);

                            }

                        }
                    });

                    alertDialog.show();

                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                    Window window = alertDialog.getWindow();
                    lp.copyFrom(window.getAttributes());
                    //This makes the dialog take up the full width
                    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                    lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                    window.setAttributes(lp);


                }
                else{

                    from_con_dollar_rate=from_con_dollar_rate_et.getText().toString().trim();
                    to_con_dollar_rate=to_con_dollar_rate_et.getText().toString().trim();
                    to_conversion_dollar=to_conversion_dollar_txt.getText().toString().trim();
                    from_dollar_name=from_dollar_name_txt.getText().toString().trim();
                    from_conversion_symbol=from_conversion_symbol_txt.getText().toString().trim();
                    to_conversion_symbol=to_conversion_symbol_txt.getText().toString().trim();



                    if (from_con_dollar_rate.length() ==0 )
                    {
                        from_con_dollar_rate_et.setError("from dollar");
                    }
                    else if (to_con_dollar_rate.length() ==0)
                    {
                        to_con_dollar_rate_et.setError("to dollar");
                    }
                    else
                    {

                        Intent activityChangeIntent = new Intent(CurrencyWizard.this, ExistingBasket.class);
                        activityChangeIntent.putExtra("from_con_dollar_rate", from_con_dollar_rate);
                        activityChangeIntent.putExtra("from_dollar_name", from_dollar_name);
                        activityChangeIntent.putExtra("from_conversion_symbol", from_conversion_symbol);
                        activityChangeIntent.putExtra("to_con_dollar_rate", to_con_dollar_rate);
                        activityChangeIntent.putExtra("to_conversion_dollar", to_conversion_dollar);
                        activityChangeIntent.putExtra("to_conversion_symbol", to_conversion_symbol);
                        startActivity(activityChangeIntent);

                    }
                }

            }
        });

        //add to new basket
        new_basket.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

                from_con_dollar_rate=from_con_dollar_rate_et.getText().toString().trim();
                to_con_dollar_rate=to_con_dollar_rate_et.getText().toString().trim();
                to_conversion_dollar=to_conversion_dollar_txt.getText().toString().trim();
                from_dollar_name=from_dollar_name_txt.getText().toString().trim();
                from_conversion_symbol=from_conversion_symbol_txt.getText().toString().trim();
                to_conversion_symbol=to_conversion_symbol_txt.getText().toString().trim();


                if (from_con_dollar_rate.length() ==0 )
                {
                    from_con_dollar_rate_et.setError("from dollar");
                }
                else if (to_con_dollar_rate.length() ==0)
                {
                    to_con_dollar_rate_et.setError("to dollar");
                }
                else
                {

                    Intent activityChangeIntent = new Intent(CurrencyWizard.this, AddToNewBasket.class);
                    activityChangeIntent.putExtra("from_con_dollar_rate", from_con_dollar_rate);
                    activityChangeIntent.putExtra("from_dollar_name", from_dollar_name);
                    activityChangeIntent.putExtra("from_conversion_symbol", from_conversion_symbol);
                    activityChangeIntent.putExtra("to_con_dollar_rate", to_con_dollar_rate);
                    activityChangeIntent.putExtra("to_conversion_dollar", to_conversion_dollar);
                    activityChangeIntent.putExtra("to_conversion_symbol", to_conversion_symbol);
                    startActivity(activityChangeIntent);

                }

            }
        });

        //basket icon
        basket_icon.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

                Intent activityChangeIntent = new Intent(CurrencyWizard.this, Basket.class);

                startActivity(activityChangeIntent);
            }
        });
    }

    //get drawable id
    private int getResId(String drawableName) {

        try {
            Class<R.drawable> res = R.drawable.class;
            Field field = res.getField(drawableName);
            int drawableId = field.getInt(null);
            return drawableId;
        } catch (Exception e) {
            Log.e("CountryCodePicker", "Failure to get drawable id.", e);
        }
        return -1;

    }

    //getCurrencyValue
        public  void getCurrencyValue(String from_dollar_value , String to_dollar_value){

            pd = new ProgressDialog(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
            pd.setMessage("Loading...");
            pd.setCancelable(false);
            pd.show();
            //System.out.println("From Currency value : "+from_dollar_value);
            //System.out.println("TO Currency value : "+to_dollar_value);


            if(from_dollar_value.equals(to_dollar_value) )
            {
                closeProgress();

                to_dollar_txt.setText("1.00");
                //from_con_dollar_rate_et.setText();
                String f_value = from_con_dollar_rate_et.getText().toString().trim();

                to_con_dollar_rate_et.setText(f_value);

            }

            else {


                Document doc = Jsoup.parse(readURL("https://www.google.com/finance/converter?a=1&from=" + from_dollar_value + "&to=" + to_dollar_value));
                Elements p_tags = doc.select("span");
                for (Element p : p_tags) {

                    closeProgress();

                    System.out.println("To Currency value is " + p.text().substring(0, 5));

                    to_dollar_txt.setText(p.text().substring(0, 5));

                    try {
                        String f_value = from_con_dollar_rate_et.getText().toString().trim();

                        String rate = to_dollar_txt.getText().toString().trim();

                       String t2 = String.valueOf(Float.valueOf(f_value) * Float.valueOf(rate));

                        to_con_dollar_rate_et.setText(new DecimalFormat("##.##").format(Double.parseDouble(t2)));

                    }catch (NumberFormatException e) {

                        //System.out.println("Exception :" +e.getMessage());

                        String numberOnly= e.getMessage().replaceAll("[^0-9]", "");

                        to_con_dollar_rate_et.setText(new DecimalFormat("##.##").format(Double.parseDouble(numberOnly)));

                        //System.out.println("Exception Value : "+new DecimalFormat("##.##").format(Double.parseDouble(numberOnly)));

                    }
                }
            }
        }


    public  void getFromCurrencyValue(String to_dollar_value , String from_dollar_value){


        pd = new ProgressDialog(context, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        pd.setMessage("Loading...");
        pd.setCancelable(false);
        pd.show();
        //System.out.println("From Currency value : "+from_dollar_value);
        //System.out.println("TO Currency value : "+to_dollar_value);


        if(from_dollar_value.equals(to_dollar_value) )
        {
            closeProgress();

            to_dollar_txt.setText("1.00");
            //from_con_dollar_rate_et.setText();
            String f_value = to_con_dollar_rate_et.getText().toString().trim();

            from_con_dollar_rate_et.setText(f_value);

        }

        else {


            Document doc = Jsoup.parse(readURL("https://www.google.com/finance/converter?a=1&from=" + to_dollar_value + "&to=" + from_dollar_value));
            Elements p_tags = doc.select("span");
            for (Element p : p_tags) {

                closeProgress();

                System.out.println("From Currency value is " + p.text().substring(0, 5));

                //to_dollar_txt.setText(p.text().substring(0, 5));

                try {
                    String f_value = to_con_dollar_rate_et.getText().toString().trim();

                    String rate = p.text().substring(0, 5).trim();

                    String t2 = String.valueOf(Float.valueOf(f_value) * Float.valueOf(rate));

                    from_con_dollar_rate_et.setText(new DecimalFormat("##.##").format(Double.parseDouble(t2)));

                }catch (NumberFormatException e) {

                    //System.out.println("Exception :" +e.getMessage());

                    String numberOnly= e.getMessage().replaceAll("[^0-9]", "");

                    from_con_dollar_rate_et.setText(new DecimalFormat("##.##").format(Double.parseDouble(numberOnly)));

                    //System.out.println("Exception Value : "+new DecimalFormat("##.##").format(Double.parseDouble(numberOnly)));

                }
            }
        }
    }

        public static String readURL(String url) {

            String fileContents = "";
            String currentLine = "";

            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
                fileContents = reader.readLine();
                while (currentLine != null) {
                    currentLine = reader.readLine();
                    fileContents += "\n" + currentLine;
                }
                reader.close();
                reader = null;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return fileContents;
        }


    //check internet connection
    public static boolean isNetworkConnected(final Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        int[] location = new int[2];
        int[] location1 = new int[2];
        ImageButton button = (ImageButton) findViewById(R.id.from_flag_name_img);
        ImageButton to_flag = (ImageButton) findViewById(R.id.to_flag_name_img);

        // Get the x, y location and store it in the location[] array
        // location[0] = x, location[1] = y.
        button.getLocationOnScreen(location);

        to_flag.getLocationOnScreen(location1);

        //Initialize the Point with x, and y positions
        p = new Point();
        p.x = location[0];
        p.y = location[1];

        p1 = new Point();
        p1.x = location1[0];
        p1.y = location1[1];
    }

    // The method that displays the popup.
    private void fromShowPopup(final Activity context, Point p) {
        int popupWidth = 470;
        int popupHeight = 520;

        // Inflate the popup_layout.xml
        LinearLayout viewGroup = (LinearLayout) context.findViewById(R.id.popup);
        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.popup_layout, viewGroup);

        final PopupWindow popup = new PopupWindow(context);

        getAllCountries();

        countryListView = (ListView) layout
                .findViewById(R.id.country_code_picker_listview);

        adapter = new CountryCurrencyListAdapter(this, selectedCountriesList);
        countryListView.setAdapter(adapter);

        countryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {


                String from_symbol=selectedCountriesList.get(position).getSymbol();

                String from_code= selectedCountriesList.get(position).getCode();

                String from_name =selectedCountriesList.get(position).getName();

                String from_symbol_native=selectedCountriesList.get(position).getSymbol_native();

                String from_decimal_digits=selectedCountriesList.get(position).getDecimal_digits();

                String from_rounding=selectedCountriesList.get(position).getRounding();

                String from_name_plural=selectedCountriesList.get(position).getName_plural();


                String drawableName = "flag_"+ from_code.substring(0, 2).toLowerCase(Locale.ENGLISH);

                if(drawableName.equals("flag_eu")) drawableName = "euro";

                from_flag_name_img.setBackgroundResource(getResId(drawableName));

                from_dollar_name_txt.setText(from_code);

                from_conversion_dollar_txt.setText(from_code);

                from_dollar_name_con_txt.setText(from_name);

                from_conversion_symbol_txt.setText(from_symbol_native);

                from_dollar_value=from_code;


                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                editor.putString("from_symbol", from_symbol);
                editor.putString("from_name", from_name);
                editor.putString("from_symbol_native", from_symbol_native);
                editor.putString("from_decimal_digits", from_decimal_digits);
                editor.putString("from_rounding", from_rounding);
                editor.putString("from_code", from_code);
                editor.putString("from_name_plural", from_name_plural);
                editor.commit();

                if(!isNetworkConnected(context)){

                    Toast.makeText(getApplication(), "Please check your internet connection.", Toast.LENGTH_LONG).show();
                }

                else {

                    getCurrencyValue(from_dollar_value, to_dollar_value);
                }

                popup.dismiss();

            }
        });

        // Creating the PopupWindow

        popup.setContentView(layout);
        popup.setWidth(popupWidth);
        popup.setHeight(popupHeight);
        popup.setFocusable(true);

        // Some offset to align the popup a bit to the right, and a bit down, relative to button's position.
        int OFFSET_X = -40;
        int OFFSET_Y = 60;

        // Clear the default translucent background
        popup.setBackgroundDrawable(new BitmapDrawable());

        // Displaying the popup at the specified location, + offsets.
        popup.showAtLocation(layout, Gravity.NO_GRAVITY, p.x + OFFSET_X, p.y + OFFSET_Y);


    }


    private void toShowPopup(final Activity context, Point p) {
        int popupWidth = 470;
        int popupHeight = 520;

        // Inflate the popup_layout.xml
        LinearLayout viewGroup = (LinearLayout) context.findViewById(R.id.popup);
        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.popup_layout, viewGroup);

        final PopupWindow popup = new PopupWindow(context);

        getAllCountries();

        countryListView = (ListView) layout
                .findViewById(R.id.country_code_picker_listview);

        adapter = new CountryCurrencyListAdapter(this, selectedCountriesList);
        countryListView.setAdapter(adapter);

        countryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {



                String to_symbol=selectedCountriesList.get(position).getSymbol();

                String to_code= selectedCountriesList.get(position).getCode();

                String to_name =selectedCountriesList.get(position).getName();

                String to_symbol_native=selectedCountriesList.get(position).getSymbol_native();

                String to_decimal_digits=selectedCountriesList.get(position).getDecimal_digits();

                String to_rounding=selectedCountriesList.get(position).getRounding();

                String to_name_plural=selectedCountriesList.get(position).getName_plural();


                String drawableName = "flag_"+ to_code.substring(0, 2).toLowerCase(Locale.ENGLISH);

                if(drawableName.equals("flag_eu")) drawableName = "euro";

                to_flag_name_img.setBackgroundResource(getResId(drawableName));

                to_dollar_name_txt.setText(to_code);

                to_conversion_dollar_txt.setText(to_code);

                to_dollar_name_con_txt.setText(to_name);

                to_conversion_symbol_txt.setText(to_symbol_native);

                to_dollar_value=to_code;


                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                editor.putString("to_symbol", to_symbol);
                editor.putString("to_name", to_name);
                editor.putString("to_symbol_native", to_symbol_native);
                editor.putString("to_decimal_digits", to_decimal_digits);
                editor.putString("to_rounding", to_rounding);
                editor.putString("to_code", to_code);
                editor.putString("to_name_plural", to_name_plural);
                editor.commit();


                if(!isNetworkConnected(context)){

                    Toast.makeText(getApplication(), "Please check your internet connection.", Toast.LENGTH_LONG).show();
                }

                else {

                    getCurrencyValue(from_dollar_value, to_dollar_value);
                }

                popup.dismiss();

            }
        });

        // Creating the PopupWindow

        popup.setContentView(layout);
        popup.setWidth(popupWidth);
        popup.setHeight(popupHeight);
        popup.setFocusable(true);

        // Some offset to align the popup a bit to the right, and a bit down, relative to button's position.
        int OFFSET_X = -40;
        int OFFSET_Y = 60;

        // Clear the default translucent background
        popup.setBackgroundDrawable(new BitmapDrawable());

        // Displaying the popup at the specified location, + offsets.
        popup.showAtLocation(layout, Gravity.NO_GRAVITY, p.x + OFFSET_X, p.y + OFFSET_Y);


    }


    //get all countries list
    private List<Country> getAllCountries() {
        if (allCountriesList == null) {
            try {
                allCountriesList = new ArrayList<Country>();

                String allCountriesCode = readEncodedJsonString(CurrencyWizard.this);

                JSONArray countrArray = new JSONArray(allCountriesCode);

                for (int i = 0; i < countrArray.length(); i++) {
                    JSONObject jsonObject = countrArray.getJSONObject(i);
                    String countrySymbol = jsonObject.getString("symbol");
                    String countryName = jsonObject.getString("name");
                    String countrySymbolNative = jsonObject.getString("symbol_native");
                    String countryDecimalDigits = jsonObject.getString("decimal_digits");
                    String countryRounding = jsonObject.getString("rounding");
                    String countryCode = jsonObject.getString("code");
                    String countryNamePlural = jsonObject.getString("name_plural");

                    Country country = new Country();
                    country.setSymbol(countrySymbol);
                    country.setName(countryName);
                    country.setSymbol_native(countrySymbolNative);
                    country.setDecimal_digits(countryDecimalDigits);
                    country.setRounding(countryRounding);
                    country.setCode(countryCode);
                    country.setName_plural(countryNamePlural);
                    allCountriesList.add(country);
                }

                Collections.sort(allCountriesList, this);

                selectedCountriesList = new ArrayList<Country>();
                selectedCountriesList.addAll(allCountriesList);

                // Return
                return allCountriesList;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    //encode json string
    private static String readEncodedJsonString(Context context)
            throws java.io.IOException {

        String base64 = context.getResources().getString(R.string.currency_code);

        byte[] data = Base64.decode(base64, Base64.DEFAULT);

        return new String(data, "UTF-8");
    }

    @Override
    public int compare(Country lhs, Country rhs) {
        return lhs.getName().compareTo(rhs.getName());
    }


//OnResume method
    @Override
    public void onResume(){
        super.onResume();

        try {
            db = new DatabaseHandler(CurrencyWizard.this);

            count = db.getBasketCount();

            System.out.println("Basket Count :" + count);

            add_to_card.setText(String.valueOf(count));
        }catch (Exception e)
        {
            System.out.println("Exception:"+e.getMessage());
        }


        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String to_code = prefs.getString("to_code", null);
        if (to_code != null) {

            //System.out.println("Code :"+code);

            String drawableName = "flag_"
                    + to_code.substring(0, 2).toLowerCase(Locale.ENGLISH);

            if(drawableName.equals("flag_eu")) drawableName = "euro";

            to_flag_name_img.setBackgroundResource(getResId(drawableName));

            to_dollar_name_txt.setText(to_code);

            to_dollar_name_con_txt.setText(prefs.getString("to_name", null));

            to_conversion_dollar_txt.setText(prefs.getString("to_code", null));

            to_conversion_symbol_txt.setText(prefs.getString("to_symbol_native", null));

            to_dollar_value=prefs.getString("to_code", null);


            if(!isNetworkConnected(this)){

                Toast.makeText(getApplication(), "Please check your internet connection.", Toast.LENGTH_LONG).show();
            }

            else {

                getCurrencyValue(from_dollar_value,to_dollar_value);
            }


            try {

                String f_value= from_con_dollar_rate_et.getText().toString().trim();

                //System.out.println("Values"+f_value);

                if (f_value.isEmpty())

                {
                    String ff_value="0";

                    String rate=to_dollar_txt.getText().toString().trim();

                    String t2 = String.valueOf(Float.valueOf(ff_value) * Float.valueOf(rate));

                    to_con_dollar_rate_et.setText(new DecimalFormat("##.##").format(Double.parseDouble(t2)));
                }
                else {

                    String rate=to_dollar_txt.getText().toString().trim();

                    String t2 = String.valueOf(Float.valueOf(f_value) * Float.valueOf(rate));

                    to_con_dollar_rate_et.setText(new DecimalFormat("##.##").format(Double.parseDouble(t2)));
                }

            }
            catch (NumberFormatException e)
            {
                e.getMessage();
            }

        }

        String from_code = prefs.getString("from_code", null);
        if (from_code != null) {

            //System.out.println("Code :"+code);

            String drawableName = "flag_"
                    + from_code.substring(0, 2).toLowerCase(Locale.ENGLISH);

            if(drawableName.equals("flag_eu")) drawableName = "euro";

            from_flag_name_img.setBackgroundResource(getResId(drawableName));

            from_dollar_name_txt.setText(from_code);

            from_dollar_name_con_txt.setText(prefs.getString("from_name", null));

            from_conversion_dollar_txt.setText(prefs.getString("from_code", null));

            from_conversion_symbol_txt.setText(prefs.getString("from_symbol_native", null));

            from_dollar_value=prefs.getString("from_code", null);



            if(!isNetworkConnected(this)){

                Toast.makeText(getApplication(), "Please check your internet connection.", Toast.LENGTH_LONG).show();
            }

            else {



                getCurrencyValue(from_dollar_value,to_dollar_value);
            }


            try {

                String f_value= from_con_dollar_rate_et.getText().toString().trim();

                //System.out.println("Values"+f_value);

                if (f_value.isEmpty())

                {
                    String ff_value="0";

                    String rate=to_dollar_txt.getText().toString().trim();

                    String t2 = String.valueOf(Float.valueOf(ff_value) * Float.valueOf(rate));

                    to_con_dollar_rate_et.setText(new DecimalFormat("##.##").format(Double.parseDouble(t2)));
                }
                else {

                    String rate=to_dollar_txt.getText().toString().trim();

                    String t2 = String.valueOf(Float.valueOf(f_value) * Float.valueOf(rate));

                    to_con_dollar_rate_et.setText(new DecimalFormat("##.##").format(Double.parseDouble(t2)));
                }

            }
            catch (NumberFormatException e)
            {
                e.getMessage();
            }

        }

    }

    private void closeProgress() {
        // TODO Auto-generated method stub
        if (pd.isShowing())
            pd.cancel();
    }
}
