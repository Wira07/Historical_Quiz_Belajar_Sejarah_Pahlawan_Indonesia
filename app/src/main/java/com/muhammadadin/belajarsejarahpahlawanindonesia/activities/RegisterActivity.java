package com.muhammadadin.belajarsejarahpahlawanindonesia.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.muhammadadin.belajarsejarahpahlawanindonesia.databinding.ActivityRegisterBinding;
import com.muhammadadin.belajarsejarahpahlawanindonesia.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String[] roles = {"Siswa", "Admin"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        setupSpinner();
        setupClickListeners();
    }

    private void setupSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, roles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerRole.setAdapter(adapter);
    }

    private void setupClickListeners() {
        binding.btnRegister.setOnClickListener(v -> registerUser());
        binding.btnBack.setOnClickListener(v -> finish());
    }

    private void registerUser() {
        String name = binding.etName.getText().toString().trim();
        String email = binding.etEmail.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();
        String selectedRole = roles[binding.spinnerRole.getSelectedItemPosition()];
        String role = selectedRole.equals("Admin") ? "admin" : "student";

        if (name.isEmpty()) {
            binding.etName.setError("Nama tidak boleh kosong");
            return;
        }

        if (email.isEmpty()) {
            binding.etEmail.setError("Email tidak boleh kosong");
            return;
        }

        if (password.isEmpty()) {
            binding.etPassword.setError("Password tidak boleh kosong");
            return;
        }

        if (password.length() < 6) {
            binding.etPassword.setError("Password minimal 6 karakter");
            return;
        }

        showLoading(true);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        String uid = mAuth.getCurrentUser().getUid();
                        User user = new User(uid, email, name, role);

                        saveUserToFirestore(user);
                    } else {
                        showLoading(false);
                        Toast.makeText(this, "Registrasi gagal: " +
                                task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveUserToFirestore(User user) {
        db.collection("users").document(user.getUid())
                .set(user)
                .addOnSuccessListener(aVoid -> {
                    showLoading(false);
                    Toast.makeText(this, "Registrasi berhasil!", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    showLoading(false);
                    Toast.makeText(this, "Gagal menyimpan data: " +
                            e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void showLoading(boolean show) {
        binding.progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        binding.btnRegister.setEnabled(!show);
        binding.btnBack.setEnabled(!show);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
