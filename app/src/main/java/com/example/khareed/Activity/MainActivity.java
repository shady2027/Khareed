package com.example.khareed.Activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;

import com.example.khareed.Adapter.CategoryAdapter;
import com.example.khareed.Adapter.PopularAdapter;
import com.example.khareed.Adapter.SliderAdapter;
import com.example.khareed.Domain.CategoryDomain;
import com.example.khareed.Domain.ItemsDomain;
import com.example.khareed.Domain.SliderItems;

import com.example.khareed.R;
import com.example.khareed.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {
    private ActivityMainBinding binding;
    private int lastSelectedItemId = R.id.action_explore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initBanner();
        initCategory();
        initPopular();
        bottomNavigation();

    }


    private void bottomNavigation() {
        binding.bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                lastSelectedItemId = item.getItemId();
                if (item.getItemId() == R.id.action_explore) {
                    return true;
                } else if (item.getItemId() == R.id.action_cart) {
                    // Navigate to CartActivity
                    Intent cartIntent = new Intent(MainActivity.this, CartActivity.class);
                    startActivity(cartIntent);
                    return true;
                } else if (item.getItemId() == R.id.action_profile) {
                    // Navigate to ProfileActivity
                    Intent profileIntent = new Intent(MainActivity.this, ProfileActivity.class);
                    startActivity(profileIntent);
                    return true;
                }
                return false;
            }
        });
        // binding.cartBtn.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, CartActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.bottomNavigationView.setSelectedItemId(R.id.action_explore);
    }


    private void initPopular() {
        DatabaseReference myref=database.getReference("Items");
        binding.progressBarPopular.setVisibility(View.VISIBLE);
        ArrayList<ItemsDomain> items=new ArrayList<>();

        myref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        items.add(issue.getValue(ItemsDomain.class));
                    }
                    if(!items.isEmpty()){
                        binding.recyclerviewPopular.setLayoutManager(new GridLayoutManager(MainActivity.this,2));
                        binding.recyclerviewPopular.setAdapter(new PopularAdapter(items));
                    }
                    binding.progressBarPopular.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initCategory() {
        DatabaseReference myref=database.getReference("Category");
        binding.progressBarOffical.setVisibility(View.VISIBLE);
        ArrayList<CategoryDomain> items=new ArrayList<>();
        myref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        items.add(issue.getValue(CategoryDomain.class));
                    }
                    if(!items.isEmpty()){
                        binding.recyclerViewOfficial.setLayoutManager(new LinearLayoutManager(MainActivity.this,
                            LinearLayoutManager.HORIZONTAL,false));
                        binding.recyclerViewOfficial.setAdapter(new CategoryAdapter(items));

                    }
                    binding.progressBarOffical.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initBanner() {
        DatabaseReference myRef = database.getReference("Banner");
        binding.progressBarBanner.setVisibility(View.VISIBLE);
        ArrayList<SliderItems> items = new ArrayList<>();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot issue : snapshot.getChildren()) {
                        items.add(issue.getValue(SliderItems.class));
                    }
                    banners(items);
                    binding.progressBarBanner.setVisibility(View.GONE);
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void banners(ArrayList<SliderItems> items) {

        binding.viewpagerSlider.setAdapter(new SliderAdapter(items, binding.viewpagerSlider));
        binding.viewpagerSlider.setClipToPadding(false);
        binding.viewpagerSlider.setClipChildren(false);
        binding.viewpagerSlider.setOffscreenPageLimit(3);
        binding.viewpagerSlider.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));

        binding.viewpagerSlider.setPageTransformer(compositePageTransformer);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}