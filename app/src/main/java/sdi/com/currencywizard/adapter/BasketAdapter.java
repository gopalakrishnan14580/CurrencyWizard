package sdi.com.currencywizard.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import sdi.com.currencywizard.R;
import sdi.com.currencywizard.model.BasketList;

/**
 * Created by twilightuser on 6/7/16.
 */
public class BasketAdapter extends BaseAdapter {

    private List<BasketList> basketList;
    private Activity activity;
    private LayoutInflater inflater;


    public BasketAdapter(Activity activity, List<BasketList> baskets) {
        this.activity = activity;
        this.basketList = baskets;
    }
    @Override
    public int getCount() {
        return basketList.size();
    }

    @Override
    public Object getItem(int position) {

        return basketList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.basket_list_item, null);

        TextView tvbasketname = (TextView) convertView.findViewById(R.id.basket_name);
        TextView tvbasketcreate = (TextView) convertView.findViewById(R.id.basket_create);
        TextView tvbasketdate = (TextView) convertView.findViewById(R.id.basket_date);

        BasketList list = basketList.get(position);

        tvbasketname.setText(list.getBasket_title());
        tvbasketdate.setText(list.getCreate_date_time());

        return convertView;

    }
}
