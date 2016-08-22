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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.util.Attributes;

import java.util.List;

import sdi.com.currencywizard.R;
import sdi.com.currencywizard.adapter.StoresAdapter;
import sdi.com.currencywizard.database.DatabaseHandler;
import sdi.com.currencywizard.model.StoresList;

public class Stores extends Activity implements StoresAdapter.StoresListAdapterDelegate {


    ListView store_listView;
    private List<StoresList> storesLists;
    private StoresAdapter adapter;
    DatabaseHandler db;
    String basket_id,basket_title;
    ImageView store_back_btn,settings,camera;
    TextView store_title;
    private Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stores_list);

        store_listView=(ListView) findViewById(R.id.store_listView);
        store_back_btn=(ImageView) findViewById(R.id.store_back_btn);
        store_title=(TextView) findViewById(R.id.store_title);
        settings=(ImageView) findViewById(R.id.settings);
        camera=(ImageView) findViewById(R.id.camera);
        TextView emptyText = (TextView)findViewById(android.R.id.empty);
        store_listView.setEmptyView(emptyText);

        store_back_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(store_back_btn.getWindowToken(),
                        InputMethodManager.RESULT_UNCHANGED_SHOWN);
                finish();
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

                Intent activityChangeIntent = new Intent(Stores.this, Settings.class);

                startActivity(activityChangeIntent);
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

                Intent activityChangeIntent = new Intent(Stores.this, CaptureImage.class);
                startActivity(activityChangeIntent);
                finish();
            }
        });

        store_load();

    }

    private  void store_load()
    {
        try {
            db = new DatabaseHandler(this);

            basket_id= getIntent().getExtras().getString("basket_id");
            basket_title= getIntent().getExtras().getString("basket_title");

            store_title.setText(basket_title);

            storesLists = db.getStore(basket_id);

            adapter = new StoresAdapter(mContext, storesLists,this);
            store_listView.setAdapter(adapter);
            adapter.setMode(Attributes.Mode.Single);

        }
        catch (Exception e)
        {
            Toast.makeText(Stores.this.getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onDeleteStores(final int position) {

        System.out.println("delete position "+position);

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

        txtContent.setVisibility(View.GONE);
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

                int stores_id = storesLists.get(position).getId();

                DatabaseHandler  db = new DatabaseHandler(mContext);
                StoresList storesList =new StoresList();

                storesList.setId(stores_id);

                db.deleteStore1(storesList);

                store_load();

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
