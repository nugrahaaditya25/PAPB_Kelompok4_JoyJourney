package com.example.joyjourney.admin;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.joyjourney.R;
import com.example.joyjourney.admin.crud.CrudActivity;
import com.example.joyjourney.model.Wahana;
import com.example.joyjourney.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class WahanaAdapter extends RecyclerView.Adapter<WahanaAdapter.Holder> {
    List<Wahana> wahanaList;
    Context context;
    @Nullable
    int resource;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(Wahana wahana);
    }

    public WahanaAdapter(List<Wahana> wahanaList, Context context, OnItemClickListener onItemClickListener) {
        this.wahanaList = wahanaList;
        this.context = context;
        this.onItemClickListener = onItemClickListener;
        resource = R.layout.katalog_item_layout;
    }

    public WahanaAdapter(List<Wahana> wahanaList, Context context, int resource, OnItemClickListener onItemClickListener) {
        this.wahanaList = wahanaList;
        this.context = context;
        this.onItemClickListener = onItemClickListener;
        this.resource = resource;
    }


    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(resource, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Wahana wahana =wahanaList.get(position);
        holder.itemView.setOnClickListener(v->{
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(wahana);
            }
        });

        holder.title.setText(wahana.getName());
        holder.desc.setText(wahana.getDescription());
        holder.price.setText(Utils.formatRupiah(wahana.getPrice()));
        Picasso.get().load(wahana.getImageUrl()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return wahanaList.size();
    }

    public class Holder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView title, desc, price;
        public Holder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.wahanaImage);
            title = itemView.findViewById(R.id.wahanaTitle);
            desc = itemView.findViewById(R.id.wahanaDesc);
            price = itemView.findViewById(R.id.wahanaPrice);
        }
    }
}
