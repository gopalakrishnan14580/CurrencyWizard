package sdi.com.currencywizard.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import sdi.com.currencywizard.R;
import sdi.com.currencywizard.adapter.BasketAdapter;
import sdi.com.currencywizard.database.DatabaseHandler;
import sdi.com.currencywizard.model.BasketList;

public class ExistingBasket extends Activity {

    ListView basket_listView;
    private List<BasketList> basketList;
    private BasketAdapter adapter;
    DatabaseHandler db;
    ImageView backbtn;

    String from_con_dollar_rate,from_dollar_name,to_con_dollar_rate,to_conversion_dollar,
            from_conversion_symbol,to_conversion_symbol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basket_list);

        basket_listView=(ListView) findViewById(R.id.basket_listView);
        backbtn=(ImageView) findViewById(R.id.backbtn);
        TextView emptyText = (TextView)findViewById(android.R.id.empty);
        basket_listView.setEmptyView(emptyText);

        from_con_dollar_rate= getIntent().getExtras().getString("from_con_dollar_rate");
        from_dollar_name= getIntent().getExtras().getString("from_dollar_name");
        from_conversion_symbol= getIntent().getExtras().getString("from_conversion_symbol");
        to_con_dollar_rate= getIntent().getExtras().getString("to_con_dollar_rate");
        to_conversion_dollar= getIntent().getExtras().getString("to_conversion_dollar");
        to_conversion_symbol= getIntent().getExtras().getString("to_conversion_symbol");

        backbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(backbtn.getWindowToken(),
                        InputMethodManager.RESULT_UNCHANGED_SHOWN);
                finish();
            }
        });

        try {

            db = new DatabaseHandler(this);

            basketList = db.getAllBasket();

            adapter = new BasketAdapter(this, basketList);
            basket_listView.setAdapter(adapter);


            basket_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    int basket_id = basketList.get(position).getId();
                    String basket_title =basketList.get(position).getBasket_title();

                    Intent intent = new Intent(ExistingBasket.this, AddToExistingBasket.class);
                    intent.putExtra("basket_id", String.valueOf(basket_id));
                    intent.putExtra("basket_title", basket_title);
                    intent.putExtra("from_con_dollar_rate", from_con_dollar_rate);
                    intent.putExtra("from_dollar_name", from_dollar_name);
                    intent.putExtra("from_conversion_symbol", from_conversion_symbol);
                    intent.putExtra("to_con_dollar_rate", to_con_dollar_rate);
                    intent.putExtra("to_conversion_dollar", to_conversion_dollar);
                    intent.putExtra("to_conversion_symbol", to_conversion_symbol);
                    startActivity(intent);
                    finish();
                }
            });

        }
        catch (Exception e)
        {
            Toast.makeText(ExistingBasket.this.getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
