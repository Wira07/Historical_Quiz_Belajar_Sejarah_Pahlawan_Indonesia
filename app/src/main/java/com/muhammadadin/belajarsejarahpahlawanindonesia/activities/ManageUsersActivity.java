package com.muhammadadin.belajarsejarahpahlawanindonesia.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.muhammadadin.belajarsejarahpahlawanindonesia.R;
import com.muhammadadin.belajarsejarahpahlawanindonesia.adapters.UserAdapter;
import com.muhammadadin.belajarsejarahpahlawanindonesia.databinding.ActivityManageUsersBinding;
import com.muhammadadin.belajarsejarahpahlawanindonesia.models.User;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ManageUsersActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private ActivityManageUsersBinding binding;
    private UserAdapter userAdapter;
    private List<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize ViewBinding
        binding = ActivityManageUsersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();

        // Initialize the list and the adapter
        userList = new ArrayList<>();
        userAdapter = new UserAdapter(userList);

        // Set the adapter to the RecyclerView
        binding.recyclerViewUsers.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewUsers.setAdapter(userAdapter);

        // Load users data
        loadUsersData();
    }

    private void loadUsersData() {
        db.collection("users")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (DocumentSnapshot document : queryDocumentSnapshots) {
                            User user = document.toObject(User.class);
                            userList.add(user);
                        }
                        // Notify the adapter that data has changed
                        userAdapter.notifyDataSetChanged();
                        Toast.makeText(this, "Loaded Users Data", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "No users found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error loading users data", Toast.LENGTH_SHORT).show());
    }
}
