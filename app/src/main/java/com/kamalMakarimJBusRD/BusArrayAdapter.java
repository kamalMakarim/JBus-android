package com.kamalMakarimJBusRD;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.kamalMakarimJBusRD.model.Bus;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class BusArrayAdapter extends ArrayAdapter<Bus>{
    public int resourceLayout;
    public ImageView calendar;
    public BusArrayAdapter(Context context, List<Bus> objects, int resourceLayout) {
        super(context, 0, objects);
        this.resourceLayout = resourceLayout;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Bus bus = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.bus_view, parent, false);
        }

        TextView busName = convertView.findViewById(R.id.bus_view_name);
        TextView busPrice = convertView.findViewById(R.id.bus_view_price);
        TextView capacity = convertView.findViewById(R.id.bus_view_capacity);
        if(this.resourceLayout == R.layout.bus_view_manage) {
            this.calendar = convertView.findViewById(R.id.calendar_image);
        }
        assert bus != null;
        busName.setText(bus.name);
        int priceInIDR = (int) bus.price.price;
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        String formattedPrice = currencyFormat.format(priceInIDR);
        busPrice.setText(formattedPrice);
        String capacityText = "Capacity :" + String.valueOf(bus.capacity);
        capacity.setText(capacityText);

        return convertView;
    }

}
