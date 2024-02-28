package com.example.infrastructurecosts;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.example.infrastructurecosts.InfrastructureCalculator;

public class MainActivity extends AppCompatActivity {

    private ConstraintLayout mainLayout;
    private TextView titleText;
    private TextView subtitleText;
    private Button computeButton;
    private EditText mileageEditText;
    private boolean isStreetSelected = false;

    private PopupWindow popupWindow;
    private TextView breakdownTextView;

    private InfrastructureCalculator calculator;
    private boolean isComputing = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainLayout = findViewById(R.id.main_layout);
        titleText = findViewById(R.id.title_text);
        subtitleText = findViewById(R.id.subtitle_text);
        computeButton = findViewById(R.id.computeButton);
        mileageEditText = findViewById(R.id.mileageEditText);

        CustomToggle customToggle = findViewById(R.id.custom_toggle);
        customToggle.setOnToggleListener(isOn -> {
            isStreetSelected = isOn;
            calculator.setSelectedMode(isStreetSelected ? "UAM" : "Street");
            updateUIBasedOnTheme();
        });

        customToggle.setOnClickListener(v -> {
            isStreetSelected = !isStreetSelected;
            calculator.setSelectedMode(isStreetSelected ? "UAM" : "Street");
            updateUIBasedOnTheme();
        });

        calculator = new InfrastructureCalculator(0.0); // Initialize with mileage only
        setupPopupWindow();

        computeButton.setOnClickListener(v -> {
            if (!isComputing) {
                // Ensure mode is correctly set before computing
                calculator.setSelectedMode(isStreetSelected ? "UAM" : "Street");
                computeCosts();
            }
        });

        updateUIBasedOnTheme();
    }

    private void setupPopupWindow() {
        ConstraintLayout costLayout = (ConstraintLayout) LayoutInflater.from(this).inflate(R.layout.layout_costs, null);
        ImageView closeButton = costLayout.findViewById(R.id.closeButton);
        TextView totalCostTextView = costLayout.findViewById(R.id.totalCostTextView);
        breakdownTextView = costLayout.findViewById(R.id.breakdownTextView);

        popupWindow = new PopupWindow(costLayout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(getDrawable(android.R.color.transparent));
        popupWindow.setElevation(8);

        closeButton.setOnClickListener(v -> popupWindow.dismiss());
    }

    private void updateUIBasedOnTheme() {
        setThemeColors();
        updateComputeButtonAppearance();
        updateEditTextAppearance();
        updateLogoVisibility();
    }

    private void updateLogoVisibility() {
        ImageView themeIcon1 = findViewById(R.id.theme_icon1);
        ImageView themeIcon2 = findViewById(R.id.theme_icon2);

        if (isStreetSelected) {
            themeIcon1.setVisibility(View.VISIBLE);
            themeIcon2.setVisibility(View.INVISIBLE);
        } else {
            themeIcon1.setVisibility(View.INVISIBLE);
            themeIcon2.setVisibility(View.VISIBLE);
        }
    }



    private void setThemeColors() {
        int backgroundColor = isStreetSelected ? R.color.dark_background : R.color.light_background;
        int textColor = isStreetSelected ? R.color.text_color_light : R.color.text_color_dark;

        mainLayout.setBackgroundColor(ContextCompat.getColor(this, backgroundColor));
        titleText.setTextColor(ContextCompat.getColor(this, textColor));
        subtitleText.setTextColor(ContextCompat.getColor(this, textColor));
    }

    private void updateComputeButtonAppearance() {
        if (isStreetSelected) {
            computeButton.setBackground(ContextCompat.getDrawable(this, R.drawable.dark_compute_button_background));
            computeButton.setTextColor(ContextCompat.getColor(this, R.color.text_color_light));
        } else {
            computeButton.setBackground(ContextCompat.getDrawable(this, R.drawable.light_compute_button_background));
            computeButton.setTextColor(ContextCompat.getColor(this, R.color.text_color_dark));
        }
    }

    private void updateEditTextAppearance() {
        int editTextColor = isStreetSelected ? R.color.edittext_color_uam : R.color.edittext_color_street;
        int colorRes = ContextCompat.getColor(this, editTextColor);

        mileageEditText.setTextColor(colorRes);
        mileageEditText.setHintTextColor(colorRes);
    }

    private void computeCosts() {
        try {
            double mileage = Double.parseDouble(mileageEditText.getText().toString());
            calculator.setMileage(mileage);
            startComputing();
            simulateComputing();
        } catch (NumberFormatException e) {
            Toast.makeText(MainActivity.this, "Invalid mileage input. Please enter a valid number.", Toast.LENGTH_SHORT).show();
        }
    }

    private void simulateComputing() {
        new Handler().postDelayed(() -> {
            calculator.computeCosts();
            stopComputing();
            displayResults();
            showCostLayout();
        }, 0); // Simulated delay
    }

    private void showCostLayout() {

        breakdownTextView.setText(calculator.getBreakdown());
        popupWindow.showAtLocation(mainLayout, Gravity.BOTTOM, 0, 0);
    }

    private void startComputing() {
        isComputing = true;
        computeButton.setEnabled(false);
    }

    private void stopComputing() {
        isComputing = false;
        computeButton.setEnabled(true);
    }

    private void displayResults() {
        Log.d("Results", "Total Cost: " + calculator.getTotalCost() + ", Breakdown: " + calculator.getBreakdown());

    }

}
