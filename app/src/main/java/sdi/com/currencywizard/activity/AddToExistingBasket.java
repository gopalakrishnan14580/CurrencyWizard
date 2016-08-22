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
import android.widget.TextView;
import android.widget.Toast;

import sdi.com.currencywizard.R;
import sdi.com.currencywizard.database.DatabaseHandler;
import sdi.com.currencywizard.model.StoresList;


public class AddToExistingBasket extends Activity {

    EditText add_a_note_et;
    Button update_btn;
    ImageView btnback;
    TextView existing_basket_title;
    String basket_id,basket_title,from_con_dollar_rate,from_dollar_name,to_con_dollar_rate,
            from_conversion_symbol,to_conversion_symbol, to_conversion_dollar,add_a_note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_to_existing_basket);

        add_a_note_et=(EditText) findViewById(R.id.add_a_note);
        existing_basket_title=(TextView) findViewById(R.id.existing_basket_title);
        update_btn=(Button) findViewById(R.id.update_btn);
        btnback=(ImageView) findViewById(R.id.btnback);


        //getting the putExtra value for currency wizard activity

        basket_id= getIntent().getExtras().getString("basket_id");
        basket_title= getIntent().getExtras().getString("basket_title");
        from_con_dollar_rate= getIntent().getExtras().getString("from_con_dollar_rate");
        from_dollar_name= getIntent().getExtras().getString("from_dollar_name");
        from_conversion_symbol= getIntent().getExtras().getString("from_conversion_symbol");
        to_con_dollar_rate= getIntent().getExtras().getString("to_con_dollar_rate");
        to_conversion_dollar= getIntent().getExtras().getString("to_conversion_dollar");
        to_conversion_symbol= getIntent().getExtras().getString("to_conversion_symbol");

        existing_basket_title.setText(basket_title);


        //back button
        btnback.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

                //hide soft keyboard
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(btnback.getWindowToken(),
                        InputMethodManager.RESULT_UNCHANGED_SHOWN);
                finish();
            }
        });

        //update basket button
        update_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

                add_a_note=add_a_note_et.getText().toString().trim();

                 if(add_a_note.length() == 0)
                {
                    add_a_note_et.setError("add note");
                }
                else
                {
                    try{
                        //update basket value
                        DatabaseHandler db = new DatabaseHandler(AddToExistingBasket.this);

                        StoresList storesList = new StoresList();
                        storesList.setBasket_id(String.valueOf(basket_id));
                        storesList.setNote(add_a_note);
                        storesList.setFrom_amt(from_con_dollar_rate);
                        storesList.setFrom_code(from_dollar_name);
                        storesList.setFrom_sym(from_conversion_symbol);
                        storesList.setTo_amt(to_con_dollar_rate);
                        storesList.setTo_code(to_conversion_dollar);
                        storesList.setTo_sym(to_conversion_symbol);
                        db.addStore(storesList);
                        //update basket value to database
                        Intent activityChangeIntent = new Intent(AddToExistingBasket.this, Basket.class);
                        startActivity(activityChangeIntent);
                        finish();

                    } catch (Exception e)

                    {
                        Toast.makeText(AddToExistingBasket.this.getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

            }
        });


    }
}
