package com.muhammadadin.belajarsejarahpahlawanindonesia.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.google.firebase.firestore.DocumentSnapshot;
import com.muhammadadin.belajarsejarahpahlawanindonesia.R;
import com.muhammadadin.belajarsejarahpahlawanindonesia.models.Fact;
import com.muhammadadin.belajarsejarahpahlawanindonesia.adapters.FactAdapter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.muhammadadin.belajarsejarahpahlawanindonesia.databinding.ActivityManageFactsBinding;

import java.util.ArrayList;
import java.util.List;

public class ManageFactsActivity extends AppCompatActivity {

    private static final String TAG = "ManageFactsActivity";
    private FirebaseFirestore db;
    private ActivityManageFactsBinding binding;
    private FactAdapter factAdapter;
    private List<Fact> factList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityManageFactsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();

        // Initialize fact list and adapter
        factList = new ArrayList<>();
        factAdapter = new FactAdapter(factList);

        // Setup RecyclerView
        binding.recyclerViewFacts.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewFacts.setAdapter(factAdapter);

        // Setup Add New Fact button
        binding.btnAddFact.setOnClickListener(v -> {
            // For now, add a sample fact - you can replace this with a dialog or new activity
            addSampleFact();
        });

        // Load facts data
        loadFactsData();
    }

    private void loadFactsData() {
        Log.d(TAG, "Loading facts data...");

        // Fetching facts from Firestore
        db.collection("facts")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    Log.d(TAG, "Query successful. Documents count: " + queryDocumentSnapshots.size());

                    factList.clear(); // Clear existing data

                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (DocumentSnapshot document : queryDocumentSnapshots) {
                            try {
                                Fact fact = document.toObject(Fact.class);
                                if (fact != null) {
                                    // Set the document ID
                                    fact.setId(document.getId());
                                    factList.add(fact);
                                    Log.d(TAG, "Added fact: " + fact.getTitle());
                                }
                            } catch (Exception e) {
                                Log.e(TAG, "Error converting document to Fact: " + e.getMessage());
                            }
                        }

                        // Notify adapter that data has changed
                        factAdapter.notifyDataSetChanged();
                        Toast.makeText(this, "Loaded " + factList.size() + " facts", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "No facts found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error loading facts data: " + e.getMessage(), e);
                    Toast.makeText(this, "Error loading facts data: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    private void addSampleFact() {
        // Sample fact for testing
        addFactToFirestore("Sample Fact", "This is a sample fact content for testing purposes.");
    }

    private void addFactToFirestore(String title, String content) {
        Log.d(TAG, "Adding fact to Firestore: " + title);

        // Create new Fact instance
        Fact fact = new Fact(title, content);

        // Add fact to Firestore
        db.collection("facts").add(fact)
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "Fact added successfully with ID: " + documentReference.getId());
                    Toast.makeText(this, "Fact added successfully!", Toast.LENGTH_SHORT).show();

                    // Reload facts to update the list
                    loadFactsData();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error adding fact: " + e.getMessage(), e);
                    Toast.makeText(this, "Error adding fact: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null; // Avoid memory leak
    }
}