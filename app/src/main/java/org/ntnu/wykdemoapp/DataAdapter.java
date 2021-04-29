package org.ntnu.wykdemoapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DataAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private int data_type;
    private Context context;

    String[] id, time, result;

    /*String[] id = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", };
    String[] time = {"5653457453", "4284535423", "3463846284", "5653457453", "4284535423",
                     "3463846284", "5653457453", "4284535423", "3463846284", "5653457453",
                     "4284535423", "3463846284"};
    String[] result = {"SUCCESS", "SUCCESS", "SUCCESS", "SUCCESS", "SUCCESS", "SUCCESS",
                       "SUCCESS", "SUCCESS", "SUCCESS", "SUCCESS", "SUCCESS", "SUCCESS" };*/

    public DataAdapter(Context context, String[] id, String[] time, String[] status, int type){
        this.context = context;
        this.data_type = type;
        this.id = id;
        this.time = time;
        this.result = status;
        inflater = (LayoutInflater.from(context));
    }

    @Override
    public int getCount(){
        return time.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup){
        view = inflater.inflate(R.layout.listview_main, null);

        TextView log_id, log_time, log_result;
        log_id = (TextView) view.findViewById(R.id.id);
        log_time = (TextView) view.findViewById(R.id.time);

        log_id.setText(this.id[i]);
        log_time.setText(this.time[i]);

        if ( this.data_type == Constants.DATA_TYPE.AUTH_LOG ) {
            log_result = (TextView) view.findViewById(R.id.result);
            log_result.setText(this.result[i]);
        }

        return view;
    }
}
