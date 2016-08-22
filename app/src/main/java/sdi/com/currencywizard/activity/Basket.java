package sdi.com.currencywizard.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.util.Attributes;
import com.mixpanel.android.mpmetrics.MixpanelAPI;

import java.util.List;

import sdi.com.currencywizard.R;
import sdi.com.currencywizard.adapter.BasketListAdapter;
import sdi.com.currencywizard.database.DatabaseHandler;
import sdi.com.currencywizard.model.BasketList;
import sdi.com.currencywizard.model.StoresList;

public class Basket extends Activity implements BasketListAdapter.BasketListAdapterDelegate {

    ListView basket_listView;
    List<BasketList> basketList;
    BasketListAdapter adapter;
    DatabaseHandler db;
    ImageView backbtn,camera,settings;
    private Context mContext = this;
    private EditText userInput;

    String projectToken = "30dec2ca702b71422f765ef4a2e05001";

    MixpanelAPI mixpanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basket_list);

        //  Add project token in the getInstance() method.
        mixpanel = MixpanelAPI.getInstance(this, projectToken);

        basket_listView=(ListView) findViewById(R.id.basket_listView);
        backbtn=(ImageView) findViewById(R.id.backbtn);
        settings=(ImageView) findViewById(R.id.settings);
        camera=(ImageView) findViewById(R.id.camera);
        TextView emptyText = (TextView)findViewById(android.R.id.empty);
        basket_listView.setEmptyView(emptyText);

        backbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(backbtn.getWindowToken(),
                        InputMethodManager.RESULT_UNCHANGED_SHOWN);
                finish();
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

                Intent activityChangeIntent = new Intent(Basket.this, Settings.class);

                startActivity(activityChangeIntent);
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

                Intent activityChangeIntent = new Intent(Basket.this, CaptureImage.class);
                startActivity(activityChangeIntent);
                finish();
            }
        });

        //basket list load
        basket_load();

    }

    private  void basket_load()
    {

        try {
            db = new DatabaseHandler(this);

            basketList = db.getAllBasket();


                adapter = new BasketListAdapter(mContext, basketList, this);
                basket_listView.setAdapter(adapter);
                adapter.setMode(Attributes.Mode.Single);

                basket_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        int basket_id = basketList.get(position).getId();
                        String basket_title = basketList.get(position).getBasket_title();

                        Intent intent = new Intent(Basket.this, Stores.class);
                        intent.putExtra("basket_id", String.valueOf(basket_id));
                        intent.putExtra("basket_title", basket_title);
                        startActivity(intent);
                        //Toast.makeText(getBaseContext(),String.valueOf(basket_id),Toast.LENGTH_SHORT).show();
                    }
                });

        }
        catch (Exception e)
        {
            Toast.makeText(Basket.this.getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void onEditBasket(final int position) {

        System.out.println("Edit"+position);

        final Dialog alertDialog = new Dialog(mContext);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = (View) inflater.inflate(R.layout.custom_basket_edit, null);
        alertDialog.setContentView(convertView);
        alertDialog.setCanceledOnTouchOutside(false);
        userInput = (EditText) convertView.findViewById(R.id.editTextDialogUserInput);

        userInput.setText(basketList.get(position).getBasket_title());

        Button cancel = (Button) convertView.findViewById(R.id.cancel);

        Button update = (Button) convertView.findViewById(R.id.update);

        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // cancel has been clicked

                alertDialog.dismiss();

            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // update has been clicked
                alertDialog.dismiss();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.RESULT_HIDDEN, 0);

                String userInput1=userInput.getText().toString().trim();
                int basket_id = basketList.get(position).getId();

                DatabaseHandler  db = new DatabaseHandler(mContext);
                BasketList basketList =new BasketList();

                basketList.setBasket_title(userInput1);
                basketList.setId(basket_id);

                db.updateBasket(basketList);

                mixpanel.track("Update Basket");

                basket_load();


            }
        });


        alertDialog.show();

        userInput.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = alertDialog.getWindow();
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
    }

    @Override
    public void onDeleteBasket(final int position) {
        System.out.println("Delete"+position);


        final Dialog alertDialog = new Dialog(mContext);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = (View) inflater.inflate(R.layout.alert, null);
        alertDialog.setContentView(convertView);
        alertDialog.setCanceledOnTouchOutside(false);
        TextView txtHeader = (TextView) convertView.findViewById(R.id.txtHeader);
        TextView txtContent = (TextView) convertView.findViewById(R.id.txtContent);
        Button btnCancel = (Button) convertView.findViewById(R.id.btnCancel);
        Button btnOk = (Button) convertView.findViewById(R.id.btnOk);
        View view = (View) convertView.findViewById(R.id.viewSep);
        btnCancel.setVisibility(View.VISIBLE);
        view.setVisibility(View.VISIBLE);

        txtContent.setText(basketList.get(position).getBasket_title());
        txtHeader.setText("Do you want to delete");

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

                int basket_id = basketList.get(position).getId();

                DatabaseHandler  db = new DatabaseHandler(mContext);

                BasketList basketList =new BasketList();

                basketList.setId(basket_id);

                db.deleteBasket(basketList);

                StoresList storesList =new StoresList();

                storesList.setBasket_id(String.valueOf(basket_id));

                db.deleteStore(storesList);

                basket_load();

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

}
