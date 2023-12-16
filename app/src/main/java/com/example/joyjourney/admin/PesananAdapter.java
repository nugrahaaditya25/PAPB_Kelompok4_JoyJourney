package com.example.joyjourney.admin;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.joyjourney.R;
import com.example.joyjourney.model.Pesanan;
import com.example.joyjourney.model.Wahana;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PesananAdapter extends RecyclerView.Adapter<PesananAdapter.Holder> {
    public enum type{
        Admin,User
    }
    List<Pesanan> listPesanan;
    int resource;

    public PesananAdapter(PesananAdapter.type type,List<Pesanan> listPesanan, Context context, OnItemClickListener onItemClickListener) {
        if(type == PesananAdapter.type.Admin){
            this.resource = R.layout.pesanan_item_layout;
        }else{
            this.resource = R.layout.user_pesanan_item_layout;
        }
        this.listPesanan = listPesanan;
        this.context = context;
        this.onItemClickListener = onItemClickListener;
    }

    Context context;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(Pesanan wahana);
    }

    public class Holder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView wahanaName, visitor, dateAndTime, buyer;
        public Holder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.cardImageIV);
            wahanaName = itemView.findViewById(R.id.wahanaNameTV);
            buyer = itemView.findViewById(R.id.buyerNameTV);
            dateAndTime = itemView.findViewById(R.id.dateAndTimeTV);
            visitor = itemView.findViewById(R.id.visitorNameTV);
        }
    }


    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(resource, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Pesanan pesanan = listPesanan.get(position);
        holder.itemView.setOnClickListener(v->{
            if(onItemClickListener!=null){
                onItemClickListener.onItemClick(pesanan);
            }
        });
        holder.visitor.setText("Total Pengunjung: "+pesanan.getVisitors()+" orang");
        holder.wahanaName.setText(pesanan.getWahanaName());
        holder.buyer.setText("Pembeli: " + pesanan.getName());
        holder.dateAndTime.setText("Tanggal & Jam: "+ pesanan.getDate()+" - "+pesanan.getReservationHour());
        Picasso.get().load(pesanan.getWahanaImage()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return listPesanan.size();
    }
}
