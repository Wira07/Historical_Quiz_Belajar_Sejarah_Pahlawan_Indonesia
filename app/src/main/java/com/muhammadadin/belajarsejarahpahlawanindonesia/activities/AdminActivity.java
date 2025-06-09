package com.muhammadadin.belajarsejarahpahlawanindonesia.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.muhammadadin.belajarsejarahpahlawanindonesia.R;
import com.muhammadadin.belajarsejarahpahlawanindonesia.databinding.ActivityAdminBinding;

public class AdminActivity extends AppCompatActivity {

    private ActivityAdminBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        // Setup click listeners for each admin button
        setupClickListeners();
        setupLogout();
    }

    private void setupClickListeners() {
        binding.cardManageHeroes.setOnClickListener(v -> manageHeroes());
        binding.cardManageFacts.setOnClickListener(v -> manageFacts());
        binding.cardManageQuiz.setOnClickListener(v -> manageQuizzes());
        binding.cardViewUsers.setOnClickListener(v -> manageUsers());
    }

    private void setupLogout() {
        // Logout ketika tombol logout ditekan
        binding.btnLogout.setOnClickListener(v -> logout());
    }

    private void manageHeroes() {
        Toast.makeText(this, "Mengelola Pahlawan", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(AdminActivity.this, ManageHeroesActivity.class));
    }

    private void manageFacts() {
        Toast.makeText(this, "Mengelola Fakta", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(AdminActivity.this, ManageFactsActivity.class));
    }

    private void manageQuizzes() {
        Toast.makeText(this, "Mengelola Kuis", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(AdminActivity.this, ManageQuizzesActivity.class));
    }

    private void manageUsers() {
        Toast.makeText(this, "Mengelola Pengguna", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(AdminActivity.this, ManageUsersActivity.class));
    }

    private void logout() {
        mAuth.signOut(); // Sign out dari Firebase
        Toast.makeText(this, "Berhasil logout", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(AdminActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish(); // Tutup AdminActivity
    }
}
