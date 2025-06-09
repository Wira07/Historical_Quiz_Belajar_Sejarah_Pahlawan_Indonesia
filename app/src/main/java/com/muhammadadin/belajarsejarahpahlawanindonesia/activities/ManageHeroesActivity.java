package com.muhammadadin.belajarsejarahpahlawanindonesia.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.muhammadadin.belajarsejarahpahlawanindonesia.R;
import com.muhammadadin.belajarsejarahpahlawanindonesia.adapters.HeroAdapter;
import com.muhammadadin.belajarsejarahpahlawanindonesia.databinding.ActivityManageHeroesBinding;
import com.muhammadadin.belajarsejarahpahlawanindonesia.models.Hero;

import java.util.ArrayList;
import java.util.List;

public class ManageHeroesActivity extends AppCompatActivity {

    private static final String TAG = "ManageHeroesActivity";
    private FirebaseFirestore db;
    private ActivityManageHeroesBinding binding;
    private HeroAdapter heroAdapter;
    private List<Hero> heroList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            binding = ActivityManageHeroesBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());

            // Initialize Firestore
            db = FirebaseFirestore.getInstance();

            // Setup RecyclerView
            setupRecyclerView();

            // Setup button listeners
            setupButtonListeners();

            // Load heroes data from Firestore
            loadHeroesData();

        } catch (Exception e) {
            Log.e(TAG, "Error in onCreate: ", e);
            Toast.makeText(this, "Error initializing activity", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupRecyclerView() {
        try {
            heroAdapter = new HeroAdapter(heroList, new HeroAdapter.OnHeroClickListener() {
                @Override
                public void onHeroClick(Hero hero) {
                    // Handle hero item click
                    Toast.makeText(ManageHeroesActivity.this, "Hero clicked: " + hero.getName(), Toast.LENGTH_SHORT).show();
                    // You can add edit functionality here
                }
            });

            binding.recyclerViewHeroes.setLayoutManager(new LinearLayoutManager(this));
            binding.recyclerViewHeroes.setAdapter(heroAdapter);
        } catch (Exception e) {
            Log.e(TAG, "Error setting up RecyclerView: ", e);
        }
    }

    private void setupButtonListeners() {
        binding.btnAddHero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to AddHeroActivity or show dialog
                openAddHeroDialog();
            }
        });
    }

    private void loadHeroesData() {
        try {
            // Show loading state
            binding.btnAddHero.setEnabled(false);

            db.collection("heroes")
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        try {
                            heroList.clear();

                            if (!queryDocumentSnapshots.isEmpty()) {
                                for (DocumentSnapshot document : queryDocumentSnapshots) {
                                    Hero hero = document.toObject(Hero.class);
                                    if (hero != null) {
                                        hero.setId(document.getId()); // Set document ID
                                        heroList.add(hero);
                                    }
                                }
                                heroAdapter.notifyDataSetChanged();
                                Log.d(TAG, "Loaded " + heroList.size() + " heroes");
                            } else {
                                Toast.makeText(this, "No heroes found in database", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "Error processing heroes data: ", e);
                            Toast.makeText(this, "Error processing heroes data", Toast.LENGTH_SHORT).show();
                        } finally {
                            binding.btnAddHero.setEnabled(true);
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Error loading heroes data: ", e);
                        Toast.makeText(this, "Error loading heroes data: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        binding.btnAddHero.setEnabled(true);
                    });
        } catch (Exception e) {
            Log.e(TAG, "Error in loadHeroesData: ", e);
            Toast.makeText(this, "Error loading data", Toast.LENGTH_SHORT).show();
            binding.btnAddHero.setEnabled(true);
        }
    }

    private void openAddHeroDialog() {
        // You can implement a dialog here or navigate to a new activity
        // For now, let's add a sample hero
        addSampleHero();
    }

    private void addSampleHero() {
        // Example of adding a hero with proper error handling
        String heroName = "Sample Hero " + System.currentTimeMillis();
        String heroDescription = "This is a sample hero description.";
        String heroImageUrl = "https://via.placeholder.com/150"; // Sample image URL

        addHeroToFirestore(heroName, heroDescription, heroImageUrl);
    }

    private void addHeroToFirestore(String heroName, String heroDescription, String heroImageUrl) {
        try {
            // Validate input
            if (heroName == null || heroName.trim().isEmpty()) {
                Toast.makeText(this, "Hero name cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create new Hero instance
            Hero hero = new Hero(heroName.trim(), heroDescription, heroImageUrl);

            // Disable button during operation
            binding.btnAddHero.setEnabled(false);

            // Add hero to Firestore
            db.collection("heroes")
                    .add(hero)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(this, "Hero added successfully!", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "Hero added with ID: " + documentReference.getId());
                        loadHeroesData(); // Reload heroes after adding
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Error adding hero: ", e);
                        Toast.makeText(this, "Error adding hero: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        binding.btnAddHero.setEnabled(true);
                    });
        } catch (Exception e) {
            Log.e(TAG, "Error in addHeroToFirestore: ", e);
            Toast.makeText(this, "Error adding hero", Toast.LENGTH_SHORT).show();
            binding.btnAddHero.setEnabled(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Clean up binding to avoid memory leaks
        if (binding != null) {
            binding = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh data when returning to this activity
        if (heroList != null && heroAdapter != null) {
            loadHeroesData();
        }
    }
}