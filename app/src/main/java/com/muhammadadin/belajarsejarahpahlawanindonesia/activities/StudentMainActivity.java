package com.muhammadadin.belajarsejarahpahlawanindonesia.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.muhammadadin.belajarsejarahpahlawanindonesia.adapters.FactAdapter;
import com.muhammadadin.belajarsejarahpahlawanindonesia.adapters.HeroAdapter;
import com.muhammadadin.belajarsejarahpahlawanindonesia.databinding.ActivityStudentMainBinding;
import com.muhammadadin.belajarsejarahpahlawanindonesia.models.Fact;
import com.muhammadadin.belajarsejarahpahlawanindonesia.models.Hero;
import com.muhammadadin.belajarsejarahpahlawanindonesia.models.Quiz;
import com.muhammadadin.belajarsejarahpahlawanindonesia.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StudentMainActivity extends AppCompatActivity {
    private ActivityStudentMainBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private HeroAdapter heroAdapter;
    private FactAdapter factAdapter;
    private List<Hero> heroList;
    private List<Fact> factList;
    private List<Quiz> quizList;
    private User currentUser;
    private Quiz currentQuiz;
    private int currentTab = 0; // 0: Gallery, 1: Quiz, 2: Facts

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStudentMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        initializeData();
        setupRecyclerViews();
        setupClickListeners();
        loadUserData();
        loadHeroes();
        loadFacts();
        loadQuizzes();
    }

    private void initializeData() {
        heroList = new ArrayList<>();
        factList = new ArrayList<>();
        quizList = new ArrayList<>();
    }

    private void setupRecyclerViews() {
        // Heroes RecyclerView - CHANGED: Using LinearLayoutManager for single column
        heroAdapter = new HeroAdapter(heroList, hero -> {
            if (!hero.isUnlocked()) {
                unlockHero(hero);
            } else {
                Toast.makeText(this, "Pahlawan sudah terbuka!", Toast.LENGTH_SHORT).show();
            }
        });
        // FIXED: Changed from GridLayoutManager(this, 2) to LinearLayoutManager(this)
        binding.recyclerViewHeroes.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewHeroes.setAdapter(heroAdapter);

        // Facts RecyclerView
        factAdapter = new FactAdapter(factList);
        binding.recyclerViewFacts.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewFacts.setAdapter(factAdapter);
    }

    private void setupClickListeners() {
        binding.btnLogout.setOnClickListener(v -> logout());

        binding.btnTabGallery.setOnClickListener(v -> switchTab(0));
        binding.btnTabQuiz.setOnClickListener(v -> switchTab(1));
        binding.btnTabFacts.setOnClickListener(v -> switchTab(2));

        binding.btnSubmitAnswer.setOnClickListener(v -> submitAnswer());
    }

    private void switchTab(int tab) {
        currentTab = tab;

        // Reset tab colors - Find TextView children in LinearLayouts
        resetTabColors();

        // Hide all views
        binding.recyclerViewHeroes.setVisibility(View.GONE);
        binding.layoutQuiz.setVisibility(View.GONE);
        binding.recyclerViewFacts.setVisibility(View.GONE);

        switch (tab) {
            case 0: // Gallery
                setTabActive(binding.btnTabGallery);
                binding.recyclerViewHeroes.setVisibility(View.VISIBLE);
                break;
            case 1: // Quiz
                setTabActive(binding.btnTabQuiz);
                binding.layoutQuiz.setVisibility(View.VISIBLE);
                loadRandomQuiz();
                break;
            case 2: // Facts
                setTabActive(binding.btnTabFacts);
                binding.recyclerViewFacts.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void resetTabColors() {
        setTabInactive(binding.btnTabGallery);
        setTabInactive(binding.btnTabQuiz);
        setTabInactive(binding.btnTabFacts);
    }

    private void setTabActive(View tabLayout) {
        // Find TextView in LinearLayout and set active color
        if (tabLayout instanceof android.widget.LinearLayout) {
            android.widget.LinearLayout layout = (android.widget.LinearLayout) tabLayout;
            for (int i = 0; i < layout.getChildCount(); i++) {
                View child = layout.getChildAt(i);
                if (child instanceof TextView) {
                    ((TextView) child).setTextColor(getColor(com.google.android.material.R.color.design_default_color_primary));
                }
            }
        }
    }

    private void setTabInactive(View tabLayout) {
        // Find TextView in LinearLayout and set inactive color
        if (tabLayout instanceof android.widget.LinearLayout) {
            android.widget.LinearLayout layout = (android.widget.LinearLayout) tabLayout;
            for (int i = 0; i < layout.getChildCount(); i++) {
                View child = layout.getChildAt(i);
                if (child instanceof TextView) {
                    ((TextView) child).setTextColor(getColor(android.R.color.darker_gray));
                }
            }
        }
    }

    private void loadUserData() {
        if (mAuth.getCurrentUser() != null) {
            db.collection("users").document(mAuth.getCurrentUser().getUid())
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            currentUser = documentSnapshot.toObject(User.class);
                            updateUI();
                        }
                    });
        }
    }

    private void updateUI() {
        if (currentUser != null) {
            binding.tvUserName.setText(currentUser.getName());
            binding.tvPoints.setText("Points: " + currentUser.getPoints());
        }
    }

    private void loadHeroes() {
        db.collection("heroes")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    heroList.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Hero hero = document.toObject(Hero.class);
                        hero.setId(document.getId());
                        heroList.add(hero);
                    }
                    heroAdapter.notifyDataSetChanged();
                });
    }

    private void loadFacts() {
        db.collection("facts")
                .whereEqualTo("isDiscovered", true)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    factList.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Fact fact = document.toObject(Fact.class);
                        fact.setId(document.getId());
                        factList.add(fact);
                    }
                    factAdapter.notifyDataSetChanged();
                });
    }

    private void loadQuizzes() {
        db.collection("quizzes")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    quizList.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Quiz quiz = document.toObject(Quiz.class);
                        quiz.setId(document.getId());
                        quizList.add(quiz);
                    }
                });
    }

    private void unlockHero(Hero hero) {
        // Add points for tapping
        if (currentUser != null) {
            currentUser.setPoints(currentUser.getPoints() + 10);

            // Update user points in Firebase
            db.collection("users").document(currentUser.getUid())
                    .update("points", currentUser.getPoints())
                    .addOnSuccessListener(aVoid -> updateUI());

            // Unlock hero
            hero.setUnlocked(true);
            heroAdapter.notifyDataSetChanged();

            // Discover related facts
            discoverFactsForHero(hero.getId());

            Toast.makeText(this, "Pahlawan terbuka! +10 poin", Toast.LENGTH_SHORT).show();
        }
    }

    private void discoverFactsForHero(String heroId) {
        db.collection("facts")
                .whereEqualTo("heroId", heroId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        db.collection("facts").document(document.getId())
                                .update("isDiscovered", true);
                    }
                    loadFacts(); // Reload facts to show newly discovered ones
                });
    }

    private void loadRandomQuiz() {
        if (!quizList.isEmpty()) {
            Random random = new Random();
            currentQuiz = quizList.get(random.nextInt(quizList.size()));
            displayQuiz(currentQuiz);
        } else {
            binding.tvQuizQuestion.setText("Belum ada kuis tersedia");
        }
    }

    private void displayQuiz(Quiz quiz) {
        binding.tvQuizQuestion.setText(quiz.getQuestion());

        // UPDATED: Use List<String> instead of String[]
        List<String> options = quiz.getOptions();
        if (options.size() >= 4) {
            binding.radioOption1.setText(options.get(0));
            binding.radioOption2.setText(options.get(1));
            binding.radioOption3.setText(options.get(2));
            binding.radioOption4.setText(options.get(3));
        } else {
            // Handle case where there are fewer than 4 options
            binding.radioOption1.setText(options.size() > 0 ? options.get(0) : "");
            binding.radioOption2.setText(options.size() > 1 ? options.get(1) : "");
            binding.radioOption3.setText(options.size() > 2 ? options.get(2) : "");
            binding.radioOption4.setText(options.size() > 3 ? options.get(3) : "");
        }

        // Clear radio group selection - need to find the RadioGroup
        RadioGroup radioGroup = findRadioGroup();
        if (radioGroup != null) {
            radioGroup.clearCheck();
        }
    }

    private RadioGroup findRadioGroup() {
        // You'll need to replace this with the actual RadioGroup from your binding
        // For example: return binding.radioGroup; or binding.rgOptions;
        // This is a placeholder - adjust according to your actual layout
        try {
            // Try to find RadioGroup in your layout
            return (RadioGroup) findViewById(android.R.id.list); // Replace with actual ID
        } catch (Exception e) {
            return null;
        }
    }

    private void submitAnswer() {
        if (currentQuiz == null) return;

        RadioGroup radioGroup = findRadioGroup();
        if (radioGroup == null) {
            Toast.makeText(this, "Radio group tidak ditemukan!", Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedId = radioGroup.getCheckedRadioButtonId();
        if (selectedId == -1) {
            Toast.makeText(this, "Pilih jawaban terlebih dahulu!", Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedAnswer = -1;
        if (selectedId == binding.radioOption1.getId()) selectedAnswer = 0;
        else if (selectedId == binding.radioOption2.getId()) selectedAnswer = 1;
        else if (selectedId == binding.radioOption3.getId()) selectedAnswer = 2;
        else if (selectedId == binding.radioOption4.getId()) selectedAnswer = 3;

        if (selectedAnswer == currentQuiz.getCorrectAnswer()) {
            // Correct answer
            if (currentUser != null) {
                currentUser.setPoints(currentUser.getPoints() + 50);
                db.collection("users").document(currentUser.getUid())
                        .update("points", currentUser.getPoints())
                        .addOnSuccessListener(aVoid -> updateUI());
            }
            Toast.makeText(this, "Benar! +50 poin", Toast.LENGTH_LONG).show();
        } else {
            // UPDATED: Use List.get() instead of array indexing
            List<String> options = currentQuiz.getOptions();
            String correctAnswerText = "Invalid";
            if (currentQuiz.getCorrectAnswer() >= 0 && currentQuiz.getCorrectAnswer() < options.size()) {
                correctAnswerText = options.get(currentQuiz.getCorrectAnswer());
            }
            Toast.makeText(this, "Salah! Jawaban yang benar: " + correctAnswerText, Toast.LENGTH_LONG).show();
        }

        // Load next quiz
        loadRandomQuiz();
    }

    private void logout() {
        mAuth.signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}