package com.muhammadadin.belajarsejarahpahlawanindonesia.activities;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.DocumentSnapshot;
import com.muhammadadin.belajarsejarahpahlawanindonesia.R;
import com.muhammadadin.belajarsejarahpahlawanindonesia.models.Fact;
import com.google.firebase.firestore.FirebaseFirestore;
import com.muhammadadin.belajarsejarahpahlawanindonesia.databinding.ActivityManageFactsBinding; // Import the generated binding class

public class ManageFactsActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private ActivityManageFactsBinding binding; // ViewBinding reference

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityManageFactsBinding.inflate(getLayoutInflater()); // Inflate the binding
        setContentView(binding.getRoot()); // Set the root view from the binding

        db = FirebaseFirestore.getInstance();

        // Load facts data
        loadFactsData();
    }

    private void loadFactsData() {
        // Fetching facts from Firestore
        db.collection("facts")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (DocumentSnapshot document : queryDocumentSnapshots) {
                            Fact fact = document.toObject(Fact.class);
                            // Update UI with fact data (e.g., populate RecyclerView)
                            Toast.makeText(this, "Loaded Facts Data", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "No facts found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error loading facts data", Toast.LENGTH_SHORT).show());
    }

    private void addFactToFirestore(String title, String content) {
        // Create new Fact instance
        Fact fact = new Fact(title, content); // Use the constructor with title and content

        // Add fact to Firestore
        db.collection("facts").add(fact)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Fact added successfully!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error adding fact", Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null; // Avoid memory leak
    }
}
