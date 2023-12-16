package com.example.joyjourney.utils;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.util.Patterns;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.joyjourney.admin.crud.CrudActivity;
import com.google.android.material.textfield.TextInputEditText;

import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;

public class Utils {
    public static boolean isValidEmail(CharSequence email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    public static boolean isNotEmpty(String value) {
        return value != null && !value.isEmpty();
    }
    public static void timePicker(View view, Context context, int openHour){
        Calendar c = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                context,
                (v, hourOfDay, minute) -> {
                    String selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
                    Toast.makeText(context, selectedTime, Toast.LENGTH_SHORT).show();
                    ((EditText)view).setText(selectedTime);
                },
                openHour,
                0,
                true
        );
        timePickerDialog.show();
    }

    public static String formatRupiah(int amount) {
        // Create a NumberFormat for the specified locale (Indonesia in this case)
        NumberFormat rupiahFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

        // Format the amount as currency
        return rupiahFormat.format(amount);
    }
}
