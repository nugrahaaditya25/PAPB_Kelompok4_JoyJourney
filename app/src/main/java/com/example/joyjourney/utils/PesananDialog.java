package com.example.joyjourney.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.joyjourney.R;
import com.example.joyjourney.model.Pesanan;

public class PesananDialog {
    private Activity activity;
    private AlertDialog dialog;
    private Pesanan pesanan;

    public PesananDialog(Activity activity, Pesanan pesanan){
        this.activity = activity;
        this.pesanan = pesanan;
    }
    public interface DialogDismissListener {
        void onDismiss();
    }
    private DialogDismissListener dismissListener;
    public void setDismissListener(DialogDismissListener listener) {
        this.dismissListener = listener;
    }

    public void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.pesanan_dialog, null);
        TextView buyername = view.findViewById(R.id.popup_history_buyername);
        TextView gender = view.findViewById(R.id.popup_history_gender);
        TextView phone = view.findViewById(R.id.popup_history_phone);
        TextView email = view.findViewById(R.id.popup_history_email);
        TextView wahanaName = view.findViewById(R.id.popup_history_wahanaName);
        TextView date = view.findViewById(R.id.popup_history_wahanaDate);
        TextView time = view.findViewById(R.id.popup_history_time);
        TextView visitors = view.findViewById(R.id.popup_history_visitors);
        TextView paymentMethod = view.findViewById(R.id.popup_history_paymentmethod);
        TextView nominal = view.findViewById(R.id.popup_history_totalprice);
        TextView status = view.findViewById(R.id.popup_history_status);
        TextView refundable = view.findViewById(R.id.popup_history_refund);

        buyername.setText("Nama Lengkap: "+pesanan.getName());
        gender.setText("Jenis Kelamin: "+pesanan.getGender());
        phone.setText("No Hp: "+pesanan.getPhoneNumber());
        email.setText("Email: "+pesanan.getEmail());
        visitors.setText("Total Pengunjung: "+pesanan.getVisitors()+" orang");
        wahanaName.setText("Nama Paket: "+pesanan.getWahanaName());
        date.setText("Tanggal: "+pesanan.getDate());
        time.setText("Jam: "+pesanan.getReservationHour());
        paymentMethod.setText("Metode Pembayaran: "+pesanan.getPaymentMethod());
        nominal.setText("Nominal: "+pesanan.getTotalPrice()+"");
        status.setText("Status Pembayaran: Lunas");
        refundable.setText("Status Refund: "+(pesanan.getIsRefundable()?"Iya":"Tidak"));

        builder.setView(view);
        builder.setCancelable(true);
        dialog = builder.create();

        dialog.setOnCancelListener(dialog1 ->{
            if(dismissListener!=null){
                dismissListener.onDismiss();
            }
        });
        dialog.show();
    }
}
