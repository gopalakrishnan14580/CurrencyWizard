package sdi.com.currencywizard.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.mixpanel.android.mpmetrics.MixpanelAPI;

import java.text.SimpleDateFormat;
import java.util.Date;

import sdi.com.currencywizard.R;
import sdi.com.currencywizard.database.DatabaseHandler;
import sdi.com.currencywizard.model.BasketList;
import sdi.com.currencywizard.model.StoresList;

public class AddToNewBasket extends Activity {

    EditText add_a_note_et,title_et;
    ImageView new_basket_back;
    Button create_btn;
    String add_note,title,from_con_dollar_rate,from_dollar_name,to_con_dollar_rate,to_conversion_dollar,to_conversion_symbol,from_conversion_symbol;

    String projectToken = "30dec2ca702b71422f765ef4a2e05001";

    MixpanelAPI mixpanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_to_new_basket);


        //  Add project token in the getInstance() method.
        mixpanel = MixpanelAPI.getInstance(this, projectToken);

        add_a_note_et=(EditText) findViewById(R.id.add_a_note_et);
        title_et=(EditText) findViewById(R.id.title_et);
        new_basket_back=(ImageView) findViewById(R.id.new_basket_back);
        create_btn=(Button) findViewById(R.id.create_btn);

        //getting the putExtra value for currency wizard activity

        from_con_dollar_rate= getIntent().getExtras().getString("from_con_dollar_rate");
        from_dollar_name= getIntent().getExtras().getString("from_dollar_name");
        from_conversion_symbol= getIntent().getExtras().getString("from_conversion_symbol");
        to_con_dollar_rate= getIntent().getExtras().getString("to_con_dollar_rate");
        to_conversion_dollar= getIntent().getExtras().getString("to_conversion_dollar");
        to_conversion_symbol= getIntent().getExtras().getString("to_conversion_symbol");

        //new basket back button

        new_basket_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                //hide soft keyboard
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(new_basket_back.getWindowToken(),
                        InputMethodManager.RESULT_UNCHANGED_SHOWN);
                finish();
            }
        });

        //create basket button
        create_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                title=title_et.getText().toString().trim();
                add_note=add_a_note_et.getText().toString().trim();

                if (title.length()==0)
                {
                    title_et.setError("title");
                }
                else if(add_note.length() == 0)
                {
                    add_a_note_et.setError("add note");
                }
                else
                {
                    //add basket value
                    try{

                    Date dNow = new Date( );
                    SimpleDateFormat ft =new SimpleDateFormat(" MMMM d, y 'at' hh:mm a ");

                    DatabaseHandler db = new DatabaseHandler(AddToNewBasket.this);

                    BasketList basketList=new BasketList();

                    basketList.setBasket_title(title);
                    basketList.setCreate_date_time(String.valueOf(ft.format(dNow)));

                    int id = db.addBasket(basketList);


                        StoresList storesList = new StoresList();
                        storesList.setBasket_id(String.valueOf(id));
                        storesList.setNote(add_note);
                        storesList.setFrom_amt(from_con_dollar_rate);
                        storesList.setFrom_code(from_dollar_name);
                        storesList.setFrom_sym(from_conversion_symbol);
                        storesList.setTo_amt(to_con_dollar_rate);
                        storesList.setTo_code(to_conversion_dollar);
                        storesList.setTo_sym(to_conversion_symbol);
                        db.addStore(storesList);
                        //add basket value to database

                        //mixpanel tracking
                        mixpanel.track("Create New Basket");


                        Intent activityChangeIntent = new Intent(AddToNewBasket.this, Basket.class);
                        startActivity(activityChangeIntent);
                        finish();

                    } catch (Exception e)

                    {
                        Toast.makeText(AddToNewBasket.this.getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

    }
}
