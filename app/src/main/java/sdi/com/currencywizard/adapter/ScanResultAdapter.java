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
import sdi.com.currencywizard.model.ScanResultList;

/**
 * Created by twilightuser on 2/8/16.
 */
public class ScanResultAdapter extends BaseAdapter {


    private List<ScanResultList> scanResultsList;
    private Activity activity;
    private LayoutInflater inflater;


    public ScanResultAdapter(Activity activity, List<ScanResultList> scanResults) {
        this.activity = activity;
        this.scanResultsList = scanResults;
    }
    @Override
    public int getCount() {

        return scanResultsList.size();
    }

    @Override
    public Object getItem(int position) {
        return scanResultsList.get(position);
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
            convertView = inflater.inflate(R.layout.scan_result_list_item, null);

        TextView scan_result = (TextView) convertView.findViewById(R.id.scan_result);

        ScanResultList list = scanResultsList.get(position);

        scan_result.setText(list.getScanResult());

        return convertView;

    }
}
