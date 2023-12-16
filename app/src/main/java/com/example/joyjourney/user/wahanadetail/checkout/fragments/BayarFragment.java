package com.example.joyjourney.user.wahanadetail.checkout.fragments;

import static android.app.Activity.RESULT_OK;
import static com.example.joyjourney.utils.Utils.isNotEmpty;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.joyjourney.R;
import com.example.joyjourney.databinding.FragmentBayarBinding;
import com.example.joyjourney.databinding.FragmentIsiDataBinding;
import com.example.joyjourney.model.Pesanan;
import com.example.joyjourney.model.Wahana;
import com.example.joyjourney.user.wahanadetail.checkout.CheckoutViewModel;
import com.example.joyjourney.user.wahanadetail.checkout.payment.MetodePembayaran;
import com.example.joyjourney.utils.Utils;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class BayarFragment extends Fragment {
    FragmentBayarBinding binding;
    CheckoutViewModel vm;
    Wahana wahana;
    int price;
    Pesanan pesanan;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBayarBinding.inflate(getLayoutInflater(), container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        vm = new ViewModelProvider(requireActivity()).get(CheckoutViewModel.class);
        wahana = vm.getWahana();
        price = wahana.getPrice();
        pesanan = vm.getPesanan().getValue();
        Picasso.get().load(wahana.getImageUrl()).into(binding.wisataImage);
        binding.titleTV.setText(wahana.getName());
        binding.adressTV.setText(wahana.getAddress());

        if(wahana.isRefundable()){
            binding.refundTitleTV.setText("Tidak Bisa Refund");
            binding.refundDescTV.setText("Anda dapat melakukan refund pembayaran Anda ketika Anda membatalkan.");
        }

        binding.timeButton.setOnClickListener(v->{
            Utils.timePicker(v,getActivity(), wahana.getOpenDate());
        });

        binding.dateButton.setOnClickListener(v->{
            showDatePickerDialog(v, getActivity());
        });

        binding.visitorsTV.setText("1");
        binding.increase.setOnClickListener(v->{
            vm.increase();
        });
        binding.decrease.setOnClickListener(v->{
            vm.decrease();
        });
        binding.paymentMethodTV.setText("");
        binding.paymentMethodContainer.setOnClickListener(v->{
            Intent intent = new Intent(getActivity(), MetodePembayaran.class);
            startActivityForResult(intent, 1);
        });
        vm.getVisitors().observe(getViewLifecycleOwner(), integer -> {
            binding.visitorsTV.setText(integer+"");
            price = integer*wahana.getPrice();
            String rupiah = Utils.formatRupiah(price);
            binding.subtotalTV.setText(rupiah);
            binding.subtotalTV2.setText(rupiah);
            binding.grandTotalTV.setText(rupiah);
        });

        binding.bayarButton.setOnClickListener(v->{
            String date = binding.dateButton.getText().toString();
            String time = binding.timeButton.getText().toString();
            int visitors = Integer.parseInt(binding.visitorsTV.getText().toString());
            String paymentMethod = binding.paymentMethodTV.getText().toString();
            int totalPayment = price;
            Log.e("konfirmasi", "total harga"+totalPayment);

            if(isNotEmpty(date) && isNotEmpty(time) && isNotEmpty(paymentMethod) && visitors>=1 && totalPayment!=0){
                pesanan.setDate(date);
                pesanan.setReservationHour(time);
                pesanan.setVisitors(visitors);
                pesanan.setPaymentMethod(paymentMethod);
                pesanan.setTotalPrice(totalPayment);
                vm.setPesananChanged();
                vm.setCurrentTab(2);
            }
        });


    }

    public static void showDatePickerDialog(View view, Context context) {
        // Set the locale to Indonesian
        Locale localeID = new Locale("id", "ID");
        Locale.setDefault(localeID);

        // Get the current date
        Calendar calendar = Calendar.getInstance(localeID);
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        // Create and show the DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                context,
                (DatePicker view1, int year, int month, int dayOfMonth) -> {
                    Calendar selectedCalendar = new GregorianCalendar(localeID);
                    selectedCalendar.set(Calendar.YEAR, year);
                    selectedCalendar.set(Calendar.MONTH, month);
                    selectedCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    DateFormatSymbols symbols = new DateFormatSymbols(localeID);
                    String[] monthNames = symbols.getMonths();
                    String[] dayNames = symbols.getWeekdays();

                    String selectedDate = dayNames[selectedCalendar.get(Calendar.DAY_OF_WEEK)] +
                            ", " + dayOfMonth +
                            " " + monthNames[selectedCalendar.get(Calendar.MONTH)] +
                            " " + year;

                    ((EditText) view).setText(selectedDate);
                },
                currentYear, currentMonth, currentDay // Set initial date to the current date
        );

        // Set the locale of the DatePickerDialog
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());

        datePickerDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode ==1 && resultCode == RESULT_OK && data != null) {
            String paymentMethod =data.getStringExtra("result");
            if(paymentMethod!=null){
                binding.paymentMethodTV.setText(paymentMethod);
            }
        }
    }
}