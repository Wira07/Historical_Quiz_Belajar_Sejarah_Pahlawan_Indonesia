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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            // Add multiple sample facts instead of just one
            addSampleFacts();
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

    private void addSampleFacts() {
        Log.d(TAG, "Adding sample facts...");

        // Fact 1: Tentang Soekarno
        addFactToFirestore(
                "Fakta Menarik Soekarno",
                "Soekarno adalah orang pertama yang mencetuskan ide Pancasila sebagai dasar negara Indonesia. Beliau juga dikenal sebagai orator ulung yang mampu membakar semangat rakyat Indonesia untuk merdeka.",
                "soekarno_hero_id"
        );

        // Fact 2: Tentang Cut Nyak Dien
        addFactToFirestore(
                "Perjuangan Cut Nyak Dien",
                "Cut Nyak Dien memimpin perlawanan terhadap Belanda selama lebih dari 25 tahun di Aceh. Meskipun kehilangan suami dan anaknya dalam perang, ia tetap melanjutkan perjuangan hingga akhir hayatnya.",
                "cut_nyak_dien_hero_id"
        );

        // Fact 3: Tentang Ki Hajar Dewantara
        addFactToFirestore(
                "Dedikasi Ki Hajar Dewantara",
                "Ki Hajar Dewantara mendirikan Perguruan Taman Siswa dengan prinsip 'Ing Ngarso Sung Tuladha, Ing Madya Mangun Karsa, Tut Wuri Handayani' yang kini menjadi semboyan pendidikan Indonesia.",
                "ki_hajar_dewantara_hero_id"
        );

        // Fact 4: Tentang Sultan Hasanuddin
        addFactToFirestore(
                "Keberanian Sultan Hasanuddin",
                "Sultan Hasanuddin dijuluki 'Ayam Jantan dari Timur' oleh Belanda karena keberaniannya dalam melawan kolonialisme. Beliau berhasil mempertahankan kemerdekaan Kerajaan Gowa selama bertahun-tahun.",
                "sultan_hasanuddin_hero_id"
        );

        // Fact 5: Tentang RA Kartini
        addFactToFirestore(
                "Pemikiran Maju RA Kartini",
                "RA Kartini tidak hanya memperjuangkan pendidikan untuk perempuan, tetapi juga menulis surat-surat yang berisi kritik terhadap sistem feodal dan kolonialisme. Surat-suratnya kemudian dibukukan dengan judul 'Habis Gelap Terbitlah Terang'.",
                "ra_kartini_hero_id"
        );

        Toast.makeText(this, "Adding 5 different facts about Indonesian heroes...", Toast.LENGTH_SHORT).show();
    }

    private void addSampleFact() {
        // Sample fact for testing
        addFactToFirestore("Sample Fact", "This is a sample fact content for testing purposes.", "");
    }

    private void addFactToFirestore(String title, String content) {
        addFactToFirestore(title, content, ""); // Call overloaded method with empty heroId
    }

    private void addFactToFirestore(String title, String content, String heroId) {
        Log.d(TAG, "Adding fact to Firestore: " + title);

        // FIXED: Create fact using Map to ensure all fields are properly set
        Map<String, Object> factData = new HashMap<>();
        factData.put("title", title);
        factData.put("content", content);
        factData.put("heroId", heroId != null ? heroId : ""); // Set heroId
        factData.put("isDiscovered", true); // CRITICAL: Set to true so it appears in student view
        factData.put("timestamp", System.currentTimeMillis()); // Add timestamp for ordering

        Log.d(TAG, "Fact data to be saved: " + factData.toString());

        // Add fact to Firestore
        db.collection("facts").add(factData)
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