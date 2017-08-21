package com.DVLA.testapp.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Edem on 16-Aug-17.
 */

class ListAdapter extends ArrayAdapter<vehRecord> {

    public ListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public ListAdapter(Context context, int resource, List<vehRecord> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.rowtextview, null);
        }

        vehRecord p = getItem(position);

        if (p != null) {
            TextView tt1 = (TextView) v.findViewById(R.id.LicensePlate);
            TextView tt2 = (TextView) v.findViewById(R.id.ValidApk);
            TextView tt3 = (TextView) v.findViewById(R.id.ApkInvalidDate);

            if (tt1 != null) {
                tt1.setText(p.getLicensePlate());
            }

            if (tt2 != null) {
                if (p.getValidApk()){
                    tt2.setText("Goedgekeurd");
                }else{
                    tt2.setText("Verlopen");
                }
            }

            if (tt3 != null) {
                String output = p.getApkInvalidDate().toString("dd/MM/yyyy");
                tt3.setText(output);
            }
        }

        return v;
    }

}
