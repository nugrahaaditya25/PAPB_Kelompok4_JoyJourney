package com.example.joyjourney.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.joyjourney.R;

public class NotifyDialog {
    private Activity activity;
    private AlertDialog dialog;
    private DialogType dialogType;

    public NotifyDialog(Activity activity, DialogType type){
        this.activity = activity;
        this.dialogType = type;
    }
    public interface DialogDismissListener {
        void onDismiss();
    }
    private DialogDismissListener dismissListener;
    public void setDismissListener(DialogDismissListener listener) {
        this.dismissListener = listener;
    }

    public void showDialog(){
        String titleString = "Katalog Berhasil ";

        String descString = "Selamat, katalog paket wisata anda berhasil ";
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.custom_dialog, null);
        TextView title = view.findViewById(R.id.dialogTitle);
        TextView desc = view.findViewById(R.id.dialogDesc);
        TextView textButton = view.findViewById(R.id.dialogTextButton);

        if(dialogType == DialogType.Upload){
            titleString +="Masuk";
            descString +="masuk";
        }else if(dialogType == DialogType.Update){
            titleString +="Diperbarui";
            descString +="diperbarui";
        }else{
            titleString +="Dihapus";
            descString +="dihapus";
        }

        title.setText(titleString);
        desc.setText(descString);
        textButton.setOnClickListener(v->{
            dialog.dismiss();
            if(dismissListener!=null){
                dismissListener.onDismiss();
            }
        });

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
    public enum DialogType{
        Upload, Delete, Update
    }
}
