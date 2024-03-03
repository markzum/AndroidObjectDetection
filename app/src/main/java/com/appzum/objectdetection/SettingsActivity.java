package com.appzum.objectdetection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;

public class SettingsActivity extends AppCompatActivity {

    String[] yoloModels = {"n", "s"};
    String[] resolutionsStrings = {"342", "480", "640", "1024", "1920"};
    int[] resolutions = {342, 480, 640, 1024, 1920};
    private final String TAG = "markzum";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        Spinner chooseYoloModelSpinner = findViewById(R.id.chooseYoloModelSpinner);
        ArrayAdapter<String> adapterYoloModel = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, yoloModels);
        adapterYoloModel.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chooseYoloModelSpinner.setAdapter(adapterYoloModel);
        chooseYoloModelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // set to shared preferences
                SharedPreferences sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("yoloModel", position);
                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });


        Spinner chooseResolutionSpinner = findViewById(R.id.chooseResolutionSpinner);
        ArrayAdapter<String> adapterResolution = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, resolutionsStrings);
        adapterResolution.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chooseResolutionSpinner.setAdapter(adapterResolution);
        chooseResolutionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // set to shared preferences
                SharedPreferences sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("targetSize", resolutions[position]);
                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });


        CheckBox gpuCheckBox = findViewById(R.id.gpuCheckBox);
        gpuCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // set to shared preferences
            SharedPreferences sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isGPU", isChecked);
            editor.apply();
        });


        // Set from shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE);
        int yoloModel = sharedPreferences.getInt("yoloModel", 0);
        boolean isGPU = sharedPreferences.getBoolean("isGPU", false);
        int targetSize = sharedPreferences.getInt("targetSize", 640);
        chooseYoloModelSpinner.setSelection(yoloModel);
        gpuCheckBox.setChecked(isGPU);
        for (int i = 0; i < resolutions.length; i++) {
            if (resolutions[i] == targetSize) {
                chooseResolutionSpinner.setSelection(i);
            }
        }


        ImageView backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> finish());
    }
}