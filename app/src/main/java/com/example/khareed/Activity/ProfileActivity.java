package com.example.khareed.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.khareed.Authentication.LoginActivity;
import com.example.khareed.R;
import com.example.khareed.databinding.ActivityMainBinding;
import com.example.khareed.databinding.ActivityProfileBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileActivity extends AppCompatActivity {
    private ActivityProfileBinding binding;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();

        binding.reportIssue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                report();
            }
        });
        binding.viewCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cart();
            }
        });
        binding.rateCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rateApp();
            }
        });
        binding.shareApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareApp();
            }
        });
        binding.aboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aboutUs();
            }
        });


        binding.backBtn.setOnClickListener(v -> finish());

        binding.loginOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                Intent i = new Intent(ProfileActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });

    }

    ///////////////////////////////
    private void rateApp() {
        String appPackageName = getPackageName();
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    private void shareApp() {
        String appPackageName = getPackageName();
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out this awesome app on the Play Store: https://play.google.com/store/apps/details?id=" + appPackageName);
        startActivity(Intent.createChooser(shareIntent, "Share via"));
    }

    private void aboutUs() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_about_us, null);

        ImageView cancelButton = view.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();
    }

    private void report() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_report, null);
        ImageView cancelBtn = view.findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();
    }

    private void cart(){
        // Navigate to CartActivity
        Intent cartIntent = new Intent(ProfileActivity.this, CartActivity.class);
        startActivity(cartIntent);
    }

}