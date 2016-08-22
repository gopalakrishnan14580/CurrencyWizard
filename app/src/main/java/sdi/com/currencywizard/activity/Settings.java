package sdi.com.currencywizard.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import sdi.com.currencywizard.R;
import sdi.com.currencywizard.adapter.CountryCurrencyListAdapter;
import sdi.com.currencywizard.model.Country;

public class Settings extends Activity implements Comparator<Country> {

    ImageButton setting_currency;
    ImageView camera,settings_back;
    TextView currency_name,terms_and_conditions,privacy_policy;
    public static final String MY_PREFS_NAME = "MyPrefsFile";

    //The "x" and "y" position of the "setting_currency" on screen.
    Point p;

    private ListView countryListView;

    private CountryCurrencyListAdapter adapter;

    private List<Country> allCountriesList;

    private List<Country> selectedCountriesList;

    RelativeLayout rate_app;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        context=this;

        setting_currency=(ImageButton) findViewById(R.id.setting_currency);
        settings_back=(ImageView) findViewById(R.id.settings_back);
        camera=(ImageView) findViewById(R.id.camera);
        currency_name=(TextView) findViewById(R.id.currency_name);
        terms_and_conditions=(TextView) findViewById(R.id.terms_and_conditions);
        privacy_policy=(TextView) findViewById(R.id.privacy_policy);
        rate_app=(RelativeLayout) findViewById(R.id.rate_app);

        camera.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

                finish();Intent activityChangeIntent = new Intent(Settings.this, CaptureImage.class);
                startActivity(activityChangeIntent);
                finish();
            }
        });

        settings_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

                finish();
            }
        });

        rate_app.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

                rateApp();
               // rateThisAppAlert();
            }
        });

        //Terms and Conditions
        terms_and_conditions.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

                Intent ChangeIntent = new Intent(Settings.this, TermsAndConditions.class);

                startActivity(ChangeIntent);
            }
        });

        //Privacy Policy
        privacy_policy.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

                Intent ChangeIntent = new Intent(Settings.this, PrivacyPolicy.class);

                startActivity(ChangeIntent);
            }
        });

        //default currency settings
        setting_currency.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

                if (p != null)
                    showPopup(Settings.this, p);

            }
        });

        currency_name.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

                if (p != null)
                    showPopup(Settings.this, p);

            }
        });


        //retrieve shared preferences value

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String to_code = prefs.getString("to_code", null);
        if (to_code != null) {

            System.out.println("Code :"+to_code);

            String drawableName = "flag_"
                    + to_code.substring(0, 2).toLowerCase(Locale.ENGLISH);

            if(drawableName.equals("flag_eu")) drawableName = "euro";

            setting_currency.setBackgroundResource(getResId(drawableName));

            currency_name.setText(to_code);

        }

    }

    private void rateThisAppAlert() {
        // TODO Auto-generated method stub

        final Dialog dialog = new Dialog(context);
        // Set Dialog Title
        dialog.setTitle("Rate Currency Wizard");
        dialog.setCancelable(false);
        LinearLayout ll = new LinearLayout(context);
        ll.setOrientation(LinearLayout.VERTICAL);

        TextView tv = new TextView(context);
        tv.setText("If you enjoy using Currency Wizard App, would you mind taking a moment to rate it? It won't take more than a minute. Thanks for your support!");
        tv.setTextSize(18);
        tv.setGravity(Gravity.CENTER);
        // tv.setLayoutParams(new LayoutParams(500, 500));
        tv.setPadding(10, 10, 10, 10);

        ll.addView(tv);

        // First Button
        Button b1 = new Button(context);
        b1.setText("Rate this App");
        b1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                System.out.println("Package Name : "+context.getPackageName());

                Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    context.startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(context, "Couldn't launch the Play Store.", Toast.LENGTH_LONG);
                }

                dialog.dismiss();

            }
        });
        ll.addView(b1);

        // Second Button
        Button b2 = new Button(context);
        b2.setText("No, Thanks");
        b2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        ll.addView(b2);

        dialog.setContentView(ll);

        // Show Dialog
        dialog.show();

    }

    public void rateApp() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=" + context.getPackageName()));
        startActivity(intent);
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

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        int[] location = new int[2];
        ImageButton button = (ImageButton) findViewById(R.id.setting_currency);

        // Get the x, y location and store it in the location[] array
        // location[0] = x, location[1] = y.
        button.getLocationOnScreen(location);

        //Initialize the Point with x, and y positions
        p = new Point();
        p.x = location[0];
        p.y = location[1];
    }

    // The method that displays the popup.
    private void showPopup(final Activity context, Point p) {
        int popupWidth = 450;
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
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String to_symbol=selectedCountriesList.get(position).getSymbol();

                String to_code= selectedCountriesList.get(position).getCode();

                String to_name =selectedCountriesList.get(position).getName();

                String to_symbol_native=selectedCountriesList.get(position).getSymbol_native();

                String to_decimal_digits=selectedCountriesList.get(position).getDecimal_digits();

                String to_rounding=selectedCountriesList.get(position).getRounding();

                String to_name_plural=selectedCountriesList.get(position).getName_plural();


                String drawableName = "flag_"
                        + to_code.substring(0, 2).toLowerCase(Locale.ENGLISH);

                if(drawableName.equals("flag_eu")) drawableName = "euro";

                setting_currency.setBackgroundResource(getResId(drawableName));

                currency_name.setText(to_code);

                //store shared preferences value

                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                editor.putString("to_symbol", to_symbol);
                editor.putString("to_name", to_name);
                editor.putString("to_symbol_native", to_symbol_native);
                editor.putString("to_decimal_digits", to_decimal_digits);
                editor.putString("to_rounding", to_rounding);
                editor.putString("to_code", to_code);
                editor.putString("to_name_plural", to_name_plural);
                editor.commit();

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

                String allCountriesCode = readEncodedJsonString(Settings.this);

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


}
