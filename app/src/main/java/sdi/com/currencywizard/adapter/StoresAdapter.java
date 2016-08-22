package sdi.com.currencywizard.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Locale;

import sdi.com.currencywizard.R;
import sdi.com.currencywizard.model.StoresList;

/**
 * Created by twilightuser on 6/7/16.
 */
public class StoresAdapter extends BaseSwipeAdapter {

    private List<StoresList> storesLists;
    private Activity activity;
    private LayoutInflater inflater;
    private Context mContext;
    StoresListAdapterDelegate delegate;

    public interface StoresListAdapterDelegate
    {
        void onDeleteStores(int position);
    }
    public StoresAdapter(Context mContext, List<StoresList> stores, StoresListAdapterDelegate delegate) {
        this.mContext = mContext;
        this.storesLists = stores;
        this.delegate=delegate;

        System.out.println("selected adapter page" + storesLists.size());
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe_layout1;
    }


    @Override
    public View generateView(final int position, ViewGroup parent) {

        View v = LayoutInflater.from(mContext).inflate(R.layout.stores_list_item, null);

        SwipeLayout swipeLayout = (SwipeLayout)v.findViewById(getSwipeLayoutResourceId(position));

        swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);


        v.findViewById(R.id.swipe_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(mContext, "click delete"+position, Toast.LENGTH_SHORT).show();

                delegate.onDeleteStores(position);

            }
        });

        return v;
    }

    @Override
    public void fillValues(int position, View convertView) {

        ImageView to_flag_name=(ImageView)convertView.findViewById(R.id.to_flag_name);
        TextView to_dollar_rate = (TextView) convertView.findViewById(R.id.to_dollar_rate);
        TextView to_dollar_name = (TextView) convertView.findViewById(R.id.to_dollar_name);

        ImageView from_flag_name=(ImageView)convertView.findViewById(R.id.from_flag_name);
        TextView from_dollar_rate = (TextView) convertView.findViewById(R.id.from_dollar_rate);
        TextView from_dollar_name = (TextView) convertView.findViewById(R.id.from_dollar_name);

        TextView note = (TextView) convertView.findViewById(R.id.note);
        TextView note_details = (TextView) convertView.findViewById(R.id.note_details);

        StoresList list = storesLists.get(position);

        String from_symbol=list.getFrom_sym()+""+list.getFrom_amt();

        String to_symbol=list.getTo_sym()+""+list.getTo_amt();


        to_dollar_rate.setText(to_symbol);
        to_dollar_name.setText(list.getTo_code());
        from_dollar_rate.setText(from_symbol);
        from_dollar_name.setText(list.getFrom_code());
        note_details.setText(list.getNote());


        String drawableName = "flag_"
                + list.getFrom_code().substring(0, 2).toLowerCase(Locale.ENGLISH);

        if(drawableName.equals("flag_eu")) drawableName = "euro";

        from_flag_name.setBackgroundResource(getResId(drawableName));



        String drawableName1 = "flag_"
                + list.getTo_code().substring(0, 2).toLowerCase(Locale.ENGLISH);

        if(drawableName1.equals("flag_eu")) drawableName1 = "euro";

        to_flag_name.setBackgroundResource(getResId(drawableName1));

    }

    @Override
    public int getCount() {
        return storesLists.size();
    }

    @Override
    public Object getItem(int position) {

        return storesLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /*@Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.stores_list_item, null);

        ImageView to_flag_name=(ImageView)convertView.findViewById(R.id.to_flag_name);
        TextView to_dollar_rate = (TextView) convertView.findViewById(R.id.to_dollar_rate);
        TextView to_dollar_name = (TextView) convertView.findViewById(R.id.to_dollar_name);

        ImageView from_flag_name=(ImageView)convertView.findViewById(R.id.from_flag_name);
        TextView from_dollar_rate = (TextView) convertView.findViewById(R.id.from_dollar_rate);
        TextView from_dollar_name = (TextView) convertView.findViewById(R.id.from_dollar_name);

        TextView note = (TextView) convertView.findViewById(R.id.note);
        TextView note_details = (TextView) convertView.findViewById(R.id.note_details);

        StoresList list = storesLists.get(position);



        to_dollar_rate.setText(list.getTo_amt());
        to_dollar_name.setText(list.getTo_code());
        from_dollar_rate.setText(list.getFrom_amt());
        from_dollar_name.setText(list.getFrom_code());
        note_details.setText(list.getNote());


        String from_symbol=list.getFrom_code();

        String to_symbol=list.getTo_code();

        String drawableName = "flag_"
                + list.getFrom_code().substring(0, 2).toLowerCase(Locale.ENGLISH);

        if(drawableName.equals("flag_eu")) drawableName = "euro";

        from_flag_name.setBackgroundResource(getResId(drawableName));



        String drawableName1 = "flag_"
                + list.getTo_code().substring(0, 2).toLowerCase(Locale.ENGLISH);

        if(drawableName1.equals("flag_eu")) drawableName1 = "euro";

        to_flag_name.setBackgroundResource(getResId(drawableName1));

        return convertView;

    }*/

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
}
