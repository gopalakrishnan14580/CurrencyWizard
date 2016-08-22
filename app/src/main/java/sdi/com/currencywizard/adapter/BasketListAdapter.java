package sdi.com.currencywizard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;

import java.util.List;

import sdi.com.currencywizard.R;
import sdi.com.currencywizard.model.BasketList;

/**
 * Created by twilightuser on 6/7/16.
 */
public class BasketListAdapter extends BaseSwipeAdapter {

    private Context mContext;
    private List<BasketList> basketList;
    BasketListAdapterDelegate delegate;

    public interface BasketListAdapterDelegate
    {
        void onEditBasket(int position);
        void onDeleteBasket(int position);
    }

    public BasketListAdapter(Context mContext, List<BasketList> baskets, BasketListAdapterDelegate delegate) {
        this.mContext = mContext;
        this.basketList = baskets;
        this.delegate=delegate;

        System.out.println("selected adapter page" + basketList.size());
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe_layout;
    }

    @Override
    public View generateView(final int position, ViewGroup parent) {

        View v = LayoutInflater.from(mContext).inflate(R.layout.basket_list_item, null);

        SwipeLayout swipeLayout = (SwipeLayout)v.findViewById(getSwipeLayoutResourceId(position));

        swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);

        v.findViewById(R.id.edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(mContext, "click Edit"+position, Toast.LENGTH_SHORT).show();

                delegate.onEditBasket(position);
            }
        });

        v.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(mContext, "click delete"+position, Toast.LENGTH_SHORT).show();

                delegate.onDeleteBasket(position);

            }
        });

        return v;
    }

    @Override
    public void fillValues(int position, View convertView) {

        TextView tvbasketname = (TextView) convertView.findViewById(R.id.basket_name);
        TextView tvbasketcreate = (TextView) convertView.findViewById(R.id.basket_create);
        TextView tvbasketdate = (TextView) convertView.findViewById(R.id.basket_date);

        BasketList list = basketList.get(position);

        tvbasketname.setText(list.getBasket_title());
        tvbasketdate.setText(list.getCreate_date_time());
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
}
