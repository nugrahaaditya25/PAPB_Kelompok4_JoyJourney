package com.example.joyjourney.user;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.joyjourney.R;

import java.util.List;

public class FacilitiesAdapter extends ArrayAdapter<String> {
    private List<String> facilities;

    public FacilitiesAdapter(Context context, List<String> facilities) {
        super(context, 0, facilities);
        this.facilities = facilities;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.facilities_item_layout, parent, false);
        }

        String facility = facilities.get(position);

        ImageView itemImage = convertView.findViewById(R.id.facilitiyIcon);
        TextView itemTitle = convertView.findViewById(R.id.facilityName);
        itemTitle.setText(facility);

        return convertView;
    }
}
