package com.example.joyjourney.admin.crud;

import static com.example.joyjourney.utils.Utils.isNotEmpty;
import static com.example.joyjourney.utils.Utils.timePicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.example.joyjourney.R;
import com.example.joyjourney.databinding.ActivityCrudBinding;
import com.example.joyjourney.model.Wahana;
import com.example.joyjourney.utils.NotifyDialog;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class CrudActivity extends AppCompatActivity {
    ActivityCrudBinding binding;
    NotifyDialog dialog;
    Wahana wahana;

    boolean isUpdate = false;

    Uri imageUri;
    CrudViewModel viewModel;
    private static final int PICK_IMAGE_REQUEST = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCrudBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(CrudActivity.this).get(CrudViewModel.class);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.refund_array,
                R.layout.dropdown_item
        );

        binding.refundSpinner.setAdapter(adapter);

        binding.openHour.setOnClickListener(v->{
            timePicker(v, CrudActivity.this, 0);
        });

        binding.closedHour.setOnClickListener(v->{
            timePicker(v,CrudActivity.this, 0);
        });

        binding.imageEdit.setOnClickListener(v->{
            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, PICK_IMAGE_REQUEST);
        });

        binding.chipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {

        });

        binding.uploadKatalog.setOnClickListener(v->{
            if(wahana==null){
                wahana = new Wahana();
            }
            List<String> facilities = new LinkedList<>();
            Wahana uploadWahana = new Wahana();
            String namaWahana = binding.nameEditText.getText().toString().trim();
            String lokasi = binding.lokasiEditText.getText().toString().trim();
            String price = binding.hargaEditText.getText().toString().trim();
            String linkGmaps = binding.gmapsLinkEditText.getText().toString().trim();
            String desc = binding.descEditText.getText().toString().trim();
            ChipGroup chipGroup = binding.chipGroup;
            for(int i = 0; i<chipGroup.getChildCount(); i++){
                Chip chip = (Chip) chipGroup.getChildAt(i);

                if(chip.isChecked()){
                    facilities.add(chip.getText().toString());
                }
            }
            String openHour = binding.openHour.getText().toString().trim();
            String closeHour = binding.closedHour.getText().toString().trim();
            boolean isRefundable = binding.refundSpinner.getText().toString().trim().equals("Tidak")?false:true;

            if (isNotEmpty(namaWahana) && isNotEmpty(lokasi) && isNotEmpty(linkGmaps)
                    && isNotEmpty(desc) && isNotEmpty(openHour) && isNotEmpty(closeHour)
                    && !facilities.isEmpty() && isNotEmpty(price)
              ) {
                Toast.makeText(this, "Redi" + namaWahana, Toast.LENGTH_SHORT).show();
                wahana.setName(namaWahana);
                wahana.setRating(0.0);
                wahana.setFacilities(facilities);
                wahana.setAddress(lokasi);
                wahana.setPrice(Integer.parseInt(price));
                wahana.setMapsUrl(linkGmaps);
                wahana.setDescription(desc);
                wahana.setOpenDate(Integer.parseInt(getHour(openHour)));
                wahana.setClosedDate(Integer.parseInt(getHour(closeHour)));
                wahana.setRefundable(isRefundable);
                if(!isUpdate){
                    wahana.setId(UUID.randomUUID().toString());
                }
                if (imageUri != null) {
                    if(isUpdate){
                        viewModel.updateWahana(wahana, imageUri);
                    }else{
                        viewModel.addWahana(wahana, imageUri);
                    }

                } else {
                    if(isUpdate){
                        viewModel.updateWahana(wahana, imageUri);
                    }else{
                        Toast.makeText(this, "Image is Not Proviided", Toast.LENGTH_SHORT).show();
                    }

                }

            } else {
                Toast.makeText(this, "Make sure all field is filled", Toast.LENGTH_SHORT).show();
            }

        });
        Intent intent = getIntent();
        Wahana wahanaTemp = (Wahana) intent.getSerializableExtra("wahana");
        if(wahanaTemp!=null){
            wahana = (Wahana) intent.getSerializableExtra("wahana");
            binding.nameEditText.setText(wahana.getName());
            binding.openHour.setText(wahana.getOpenDate()+":00");
            binding.closedHour.setText(wahana.getClosedDate()+":00");
            binding.lokasiEditText.setText(wahana.getAddress());
            binding.gmapsLinkEditText.setText(wahana.getMapsUrl());
            binding.descEditText.setText(wahana.getDescription());
            binding.hargaEditText.setText(wahana.getPrice()+"");
            binding.refundSpinner.setText(wahana.isRefundable()?"Iya":"Tidak");
            for(String facility: wahana.getFacilities()){
                switch (facility){
                    case "Wifi":
                        binding.chipWifi.setChecked(true);
                        break;
                    case  "Kantin":
                        binding.chipKantin.setChecked(true);
                        break;
                    case "Kolam Renang":
                        binding.chipKolam.setChecked(true);
                        break;
                    case "Musholla":
                        binding.chipMusholla.setChecked(true);
                        break;
                    case "Parkir Gratis":
                        binding.chipParkir.setChecked(true);
                        break;
                }
            }
        }

        if(wahana==null){
            binding.deleteKatalog.setVisibility(View.GONE);

        }else{
            binding.uploadKatalog.setText("Update Katalog");
            isUpdate=true;
        }

        binding.deleteKatalog.setOnClickListener(v->{
            viewModel.deleteWahana(wahana);
        });
        binding.backButton.setOnClickListener(v->{
            finish();
        });
        viewModel.getError().observe(this,errorMessage -> {
            if (errorMessage != null) {
                Toast.makeText(CrudActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
        
        viewModel.getIsSuccess().observe(this, success->{
            if(success){
                NotifyDialog.DialogType type = viewModel.getRequestType().getValue();
                dialog = new NotifyDialog(CrudActivity.this,type);
                dialog.setDismissListener(new NotifyDialog.DialogDismissListener() {
                    @Override
                    public void onDismiss() {
                        Intent resultIntent = new Intent();

                        resultIntent.putExtra("refresh", true);

                        setResult(Activity.RESULT_OK, resultIntent);

                        finish();
                    }
                });
                dialog.showDialog();
            }
        });

    }

    private String getHour(String time){
        return time.split(":")[0];
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            // Dapatkan URI gambar yang dipilih
            Uri selectedImageUri = data.getData();
            imageUri = selectedImageUri;
            binding.imageEdit.setText("imageSelected");
        }

    }
}