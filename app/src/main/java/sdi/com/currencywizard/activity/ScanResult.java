package sdi.com.currencywizard.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

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
import sdi.com.currencywizard.adapter.ScanResultAdapter;
import sdi.com.currencywizard.model.Country;
import sdi.com.currencywizard.model.CurrencyApplication;
import sdi.com.currencywizard.model.ScanResultList;

/**
 * Created by twilightuser on 2/8/16.
 */
public class ScanResult extends Activity implements Comparator<Country> {

    ListView scan_listView;
    ImageButton show_flag;
    ImageView     scan_back;
    TextView show_help_overlay,show_currency_code;

    Point p;

    private ListView countryListView;

    private CountryCurrencyListAdapter adapter;

    private List<Country> allCountriesList;

    private List<Country> selectedCountriesList;

    public static final String MY_PREFS_NAME = "MyPrefsFile";


    List<ScanResultList> scanResultLists;
    ScanResultAdapter scan_adapter;

    private static int SHOW_HELF_LINE =0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_result_list);


        scan_listView=(ListView) findViewById(R.id.scan_listView);

        show_flag=(ImageButton) findViewById(R.id.show_flag);
        scan_back=(ImageView) findViewById(R.id.scan_back);

        show_help_overlay=(TextView) findViewById(R.id.show_help_overlay);
        show_currency_code=(TextView) findViewById(R.id.show_currency_code);

        scan_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

                Intent activityChangeIntent = new Intent(ScanResult.this, CurrencyWizard.class);
                startActivity(activityChangeIntent);
                finish();
            }
        });

try {

    SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

    int show_help = prefs.getInt("show_help", 0);

    System.out.println("show_help ----------------->"+show_help);

    //if(show_help)

    if (SHOW_HELF_LINE == 0 && show_help == 0 ) {
        show_help_overlay.setVisibility(View.VISIBLE);

        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putInt("show_help", 1);
        editor.commit();
    } else {

        show_help_overlay.setVisibility(View.GONE);

        SHOW_HELF_LINE = 1;

    }

    }catch (Exception e)
    {
        System.out.println("Exception : "+e.getMessage());
    }



        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String from_code = prefs.getString("from_code", null);
        if (from_code != null) {

            //System.out.println("Code :"+code);

            String drawableName = "flag_"
                    + from_code.substring(0, 2).toLowerCase(Locale.ENGLISH);

            if (drawableName.equals("flag_eu")) drawableName = "euro";

            show_flag.setBackgroundResource(getResId(drawableName));

            show_currency_code.setText(prefs.getString("from_code",null));

        }

            try{

            final CurrencyApplication globalVariable = (CurrencyApplication) getApplicationContext();

            scanResultLists=globalVariable.getScanList();

            System.out.println("Size :"+scanResultLists.size());

        }
        catch (Exception e)
        {
            System.out.println("Exception : "+e.getMessage());
        }


        show_currency_code.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                try{
                    show_help_overlay.setVisibility(View.GONE);
                    SHOW_HELF_LINE =1;
                }
                catch (Exception e)
                {
                    System.out.println("Exception : "+e.getMessage());
                }

                // Perform action on click
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(show_flag.getWindowToken(),
                        InputMethodManager.RESULT_UNCHANGED_SHOWN);
                //Open popup window
                if (p != null)

                    fromShowPopup(ScanResult.this, p);

            }
        });

        show_flag.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                try{
                    show_help_overlay.setVisibility(View.GONE);
                    SHOW_HELF_LINE =1;
                }
                catch (Exception e)
                {
                    System.out.println("Exception : "+e.getMessage());
                }


                // Perform action on click
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(show_flag.getWindowToken(),
                        InputMethodManager.RESULT_UNCHANGED_SHOWN);
                //Open popup window
                if (p != null)

                    fromShowPopup(ScanResult.this, p);

            }
        });


        scan_adapter = new ScanResultAdapter(this, scanResultLists);
        scan_listView.setAdapter(scan_adapter);

        scan_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String scan_result = scanResultLists.get(position).getScanResult();

                final CurrencyApplication globalVariable = (CurrencyApplication) getApplicationContext();

                globalVariable.setCamera_currency(scan_result);

                Intent intent = new Intent(ScanResult.this, CurrencyWizard.class);
                startActivity(intent);
                finish();

            }
        });


    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        int[] location = new int[2];

        ImageView show_flag=(ImageView) findViewById(R.id.show_flag);

        // Get the x, y location and store it in the location[] array
        // location[0] = x, location[1] = y.
        show_flag.getLocationOnScreen(location);


        //Initialize the Point with x, and y positions
        p = new Point();
        p.x = location[0];
        p.y = location[1];

    }

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

                String from_drawableName = "flag_" + from_code.substring(0, 2).toLowerCase(Locale.ENGLISH);

                if (from_drawableName.equals("flag_eu")) from_drawableName = "euro";

                //to flag

                show_flag.setBackgroundResource(getResId(from_drawableName));

                show_currency_code.setText(from_code);


                SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                editor.putString("from_symbol", from_symbol);
                editor.putString("from_name", from_name);
                editor.putString("from_symbol_native", from_symbol_native);
                editor.putString("from_decimal_digits", from_decimal_digits);
                editor.putString("from_rounding", from_rounding);
                editor.putString("from_code", from_code);
                editor.putString("from_name_plural", from_name_plural);
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
    private List<Country> getAllCountries() {
        if (allCountriesList == null) {
            try {
                allCountriesList = new ArrayList<Country>();

                String allCountriesCode = readEncodedJsonString(ScanResult.this);

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

                Collections.sort(allCountriesList, ScanResult.this);

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
    public void onResume() {
        super.onResume();

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String from_code = prefs.getString("from_code", null);
        if (from_code != null) {

            //System.out.println("Code :"+code);

            String drawableName = "flag_"
                    + from_code.substring(0, 2).toLowerCase(Locale.ENGLISH);

            if (drawableName.equals("flag_eu")) drawableName = "euro";

            show_flag.setBackgroundResource(getResId(drawableName));

        }
    }

}
