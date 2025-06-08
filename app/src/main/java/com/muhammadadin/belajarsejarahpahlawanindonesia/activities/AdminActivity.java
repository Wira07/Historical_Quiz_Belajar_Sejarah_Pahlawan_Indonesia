package com.muhammadadin.belajarsejarahpahlawanindonesia.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.muhammadadin.belajarsejarahpahlawanindonesia.R;
import com.muhammadadin.belajarsejarahpahlawanindonesia.databinding.ActivityAdminBinding;
import com.google.firebase.auth.FirebaseAuth;

public class AdminActivity extends AppCompatActivity {

    private ActivityAdminBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        // Apply click listeners for each admin button
        setupClickListeners();

        // Load admin data
        loadAdminData();
    }


    private void setupClickListeners() {
        // Handle logout click
        binding.btnLogout.setOnClickListener(v -> logout());

        // Handle other admin menu clicks
        binding.cardManageHeroes.setOnClickListener(v -> manageHeroes());
        binding.cardManageFacts.setOnClickListener(v -> manageFacts());
        binding.cardManageQuiz.setOnClickListener(v -> manageQuizzes());
        binding.cardViewUsers.setOnClickListener(v -> manageUsers());
    }

    private void loadAdminData() {
        // Retrieve admin data to display in the Admin Panel
        // You can load data here if needed, e.g., admin's name or role.
    }

    private void manageHeroes() {
        Toast.makeText(this, "Mengelola Pahlawan", Toast.LENGTH_SHORT).show();
    }

    private void manageFacts() {
        Toast.makeText(this, "Mengelola Fakta", Toast.LENGTH_SHORT).show();
    }

    private void manageQuizzes() {
        Toast.makeText(this, "Mengelola Kuis", Toast.LENGTH_SHORT).show();
    }

    private void manageUsers() {
        Toast.makeText(this, "Mengelola Pengguna", Toast.LENGTH_SHORT).show();
    }

    // Logout method
    private void logout() {
        mAuth.signOut(); // Sign out from Firebase
        Intent intent = new Intent(AdminActivity.this, LoginActivity.class); // Navigate to LoginActivity
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear previous activities from the stack
        startActivity(intent);
        finish(); // Close AdminActivity
    }

}
