package com.muhammadadin.belajarsejarahpahlawanindonesia.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.muhammadadin.belajarsejarahpahlawanindonesia.R;
import com.muhammadadin.belajarsejarahpahlawanindonesia.adapters.QuizAdapter;
import com.muhammadadin.belajarsejarahpahlawanindonesia.databinding.ActivityManageQuizzesBinding;
import com.muhammadadin.belajarsejarahpahlawanindonesia.models.Quiz;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManageQuizzesActivity extends AppCompatActivity {

    private static final String TAG = "ManageQuizzesActivity";
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private ActivityManageQuizzesBinding binding;
    private QuizAdapter quizAdapter;
    private List<Quiz> quizList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            binding = ActivityManageQuizzesBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());

            // Initialize Firebase
            db = FirebaseFirestore.getInstance();
            mAuth = FirebaseAuth.getInstance();

            // Check if user is authenticated
            checkUserAuthentication();

            // Setup RecyclerView
            setupRecyclerView();

            // Setup button listeners
            setupButtonListeners();

            // Load quizzes data from Firestore
            loadQuizzesData();

        } catch (Exception e) {
            Log.e(TAG, "Error in onCreate: ", e);
            Toast.makeText(this, "Error initializing activity: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void checkUserAuthentication() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Log.d(TAG, "User is authenticated: " + currentUser.getUid());
            Log.d(TAG, "User email: " + currentUser.getEmail());
        } else {
            Log.w(TAG, "User is NOT authenticated");
            Toast.makeText(this, "Please login first", Toast.LENGTH_LONG).show();
            // You might want to redirect to login activity here
        }
    }

    private void setupRecyclerView() {
        try {
            quizAdapter = new QuizAdapter(quizList, new QuizAdapter.OnQuizClickListener() {
                @Override
                public void onQuizClick(Quiz quiz) {
                    // Handle quiz item click
                    Toast.makeText(ManageQuizzesActivity.this, "Quiz clicked: " + quiz.getQuestion(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Quiz clicked: " + quiz.toString());
                }
            });

            binding.recyclerViewQuizzes.setLayoutManager(new LinearLayoutManager(this));
            binding.recyclerViewQuizzes.setAdapter(quizAdapter);
            Log.d(TAG, "RecyclerView setup completed");
        } catch (Exception e) {
            Log.e(TAG, "Error setting up RecyclerView: ", e);
        }
    }

    private void setupButtonListeners() {
        binding.btnAddQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Add Quiz button clicked");
                // Navigate to AddQuizActivity or show dialog
                openAddQuizDialog();
            }
        });
    }

    private void loadQuizzesData() {
        try {
            Log.d(TAG, "Starting to load quizzes data...");

            // Check authentication first
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser == null) {
                Log.e(TAG, "User not authenticated, cannot load quizzes");
                Toast.makeText(this, "Please login to view quizzes", Toast.LENGTH_LONG).show();
                return;
            }

            // Show loading state
            binding.btnAddQuiz.setEnabled(false);

            db.collection("quizzes")
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        try {
                            Log.d(TAG, "Successfully fetched quizzes from Firestore");
                            quizList.clear();

                            if (!queryDocumentSnapshots.isEmpty()) {
                                Log.d(TAG, "Found " + queryDocumentSnapshots.size() + " quiz documents");

                                for (DocumentSnapshot document : queryDocumentSnapshots) {
                                    try {
                                        Log.d(TAG, "Processing document: " + document.getId());
                                        Log.d(TAG, "Document data: " + document.getData());

                                        // Manual parsing to handle Firestore data properly
                                        Quiz quiz = parseQuizFromDocument(document);
                                        if (quiz != null) {
                                            quiz.setId(document.getId()); // Set document ID
                                            quizList.add(quiz);
                                            Log.d(TAG, "Added quiz: " + quiz.getQuestion());
                                        } else {
                                            Log.w(TAG, "Failed to parse quiz from document: " + document.getId());
                                        }
                                    } catch (Exception e) {
                                        Log.e(TAG, "Error parsing individual document: " + document.getId(), e);
                                    }
                                }
                                quizAdapter.notifyDataSetChanged();
                                Log.d(TAG, "Loaded " + quizList.size() + " quizzes successfully");
                                Toast.makeText(this, "Loaded " + quizList.size() + " quizzes", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.d(TAG, "No quiz documents found");
                                Toast.makeText(this, "No quizzes found in database", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "Error processing quizzes data: ", e);
                            Toast.makeText(this, "Error processing quizzes data: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        } finally {
                            binding.btnAddQuiz.setEnabled(true);
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Error loading quizzes data: ", e);
                        Log.e(TAG, "Error type: " + e.getClass().getSimpleName());
                        Log.e(TAG, "Error message: " + e.getMessage());

                        String errorMessage = "Error loading quizzes: ";
                        if (e.getMessage() != null) {
                            errorMessage += e.getMessage();
                        } else {
                            errorMessage += "Unknown error";
                        }

                        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
                        binding.btnAddQuiz.setEnabled(true);
                    });
        } catch (Exception e) {
            Log.e(TAG, "Exception in loadQuizzesData: ", e);
            Toast.makeText(this, "Error loading data: " + e.getMessage(), Toast.LENGTH_LONG).show();
            binding.btnAddQuiz.setEnabled(true);
        }
    }

    // Helper method to manually parse Quiz from Firestore document
    private Quiz parseQuizFromDocument(DocumentSnapshot document) {
        try {
            Map<String, Object> data = document.getData();
            if (data == null) return null;

            Quiz quiz = new Quiz();

            // Parse question
            if (data.containsKey("question")) {
                quiz.setQuestion((String) data.get("question"));
            }

            // Parse options - handle both List and Array formats
            if (data.containsKey("options")) {
                Object optionsObj = data.get("options");
                List<String> optionsList = null;

                if (optionsObj instanceof List) {
                    // If it's already a List, cast it directly
                    List<?> rawList = (List<?>) optionsObj;
                    optionsList = new ArrayList<>();
                    for (Object item : rawList) {
                        optionsList.add(String.valueOf(item));
                    }
                } else if (optionsObj instanceof String[]) {
                    // If it's a String array, convert to List
                    String[] optionsArray = (String[]) optionsObj;
                    optionsList = Arrays.asList(optionsArray);
                }

                // Set the options as List<String>
                if (optionsList != null) {
                    quiz.setOptions(optionsList);
                }
            }

            // Parse correctAnswer
            if (data.containsKey("correctAnswer")) {
                Object correctAnswerObj = data.get("correctAnswer");
                if (correctAnswerObj instanceof Long) {
                    quiz.setCorrectAnswer(((Long) correctAnswerObj).intValue());
                } else if (correctAnswerObj instanceof Integer) {
                    quiz.setCorrectAnswer((Integer) correctAnswerObj);
                }
            }

            // Parse heroId
            if (data.containsKey("heroId")) {
                quiz.setHeroId((String) data.get("heroId"));
            }

            // Parse timestamp
            if (data.containsKey("timestamp")) {
                Object timestampObj = data.get("timestamp");
                if (timestampObj instanceof Long) {
                    quiz.setTimestamp((Long) timestampObj);
                }
            }

            return quiz;
        } catch (Exception e) {
            Log.e(TAG, "Error parsing quiz from document", e);
            return null;
        }
    }

    private void openAddQuizDialog() {
        Log.d(TAG, "Opening add quiz dialog...");
        // You can implement a dialog here or navigate to a new activity
        // For now, let's add a sample quiz
        addSampleQuiz();
    }

    private void addSampleQuiz() {
        Log.d(TAG, "Adding sample quizzes...");

        // Quiz 1: Tentang Proklamator
        String question1 = "Siapa yang dikenal sebagai Bapak Proklamator Indonesia?";
        String[] options1 = {"Soekarno", "Mohammad Hatta", "Sutan Sjahrir", "Ki Hajar Dewantara"};
        int correctAnswer1 = 0; // Index 0 = "Soekarno"
        addQuizToFirestore(question1, options1, correctAnswer1);

        // Quiz 2: Tentang Pahlawan Wanita
        String question2 = "Siapa pahlawan wanita yang dikenal sebagai 'Ibu Kartini dari Sumatera'?";
        String[] options2 = {"Cut Nyak Dien", "Rohana Kudus", "Martha Christina Tiahahu", "Dewi Sartika"};
        int correctAnswer2 = 1; // Index 1 = "Rohana Kudus"
        addQuizToFirestore(question2, options2, correctAnswer2);

        // Quiz 3: Tentang Perjuangan Kemerdekaan
        String question3 = "Kapan Indonesia memproklamasikan kemerdekaannya?";
        String[] options3 = {"16 Agustus 1945", "17 Agustus 1945", "18 Agustus 1945", "19 Agustus 1945"};
        int correctAnswer3 = 1; // Index 1 = "17 Agustus 1945"
        addQuizToFirestore(question3, options3, correctAnswer3);

        // Quiz 4: Tentang Pahlawan Nasional
        String question4 = "Siapa yang dijuluki 'Si Singamangaraja XII' dan merupakan pahlawan dari Sumatera Utara?";
        String[] options4 = {"Tuanku Imam Bonjol", "Sultan Hasanuddin", "Sisingamangaraja XII", "Pangeran Diponegoro"};
        int correctAnswer4 = 2; // Index 2 = "Sisingamangaraja XII"
        addQuizToFirestore(question4, options4, correctAnswer4);

        // Quiz 5: Tentang Pendidikan
        String question5 = "Siapa yang dikenal sebagai 'Bapak Pendidikan Nasional Indonesia'?";
        String[] options5 = {"Ki Hajar Dewantara", "Dr. Wahidin Sudirohusodo", "Mohammad Sjafei", "Ki Hadjar Dewantoro"};
        int correctAnswer5 = 0; // Index 0 = "Ki Hajar Dewantara"
        addQuizToFirestore(question5, options5, correctAnswer5);

        Toast.makeText(this, "Adding 5 different quiz questions...", Toast.LENGTH_SHORT).show();
    }

    private void addQuizToFirestore(String question, String[] options, int correctAnswer) {
        try {
            Log.d(TAG, "Starting addQuizToFirestore...");
            Log.d(TAG, "Question: " + question);
            Log.d(TAG, "Options count: " + (options != null ? options.length : 0));
            Log.d(TAG, "Correct answer index: " + correctAnswer);

            // Check authentication first
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser == null) {
                Log.e(TAG, "User not authenticated, cannot add quiz");
                Toast.makeText(this, "Please login to add quizzes", Toast.LENGTH_LONG).show();
                return;
            }

            // Validate input
            if (question == null || question.trim().isEmpty()) {
                Log.e(TAG, "Question is empty");
                Toast.makeText(this, "Question cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            if (options == null || options.length < 2) {
                Log.e(TAG, "Not enough options provided");
                Toast.makeText(this, "At least 2 options are required", Toast.LENGTH_SHORT).show();
                return;
            }

            if (correctAnswer < 0 || correctAnswer >= options.length) {
                Log.e(TAG, "Invalid correct answer index");
                Toast.makeText(this, "Invalid correct answer index", Toast.LENGTH_SHORT).show();
                return;
            }

            // Log options
            for (int i = 0; i < options.length; i++) {
                Log.d(TAG, "Option " + i + ": " + options[i]);
            }

            // FIXED: Create new Quiz using Map and convert array to List for Firestore compatibility
            Map<String, Object> quizData = new HashMap<>();
            quizData.put("question", question.trim());

            // Convert String[] to List<String> for Firestore compatibility
            List<String> optionsList = Arrays.asList(options);
            quizData.put("options", optionsList);

            quizData.put("correctAnswer", correctAnswer);
            quizData.put("heroId", ""); // Add empty heroId if needed
            quizData.put("timestamp", System.currentTimeMillis()); // Add timestamp for ordering

            Log.d(TAG, "Quiz data to be saved: " + quizData.toString());

            // Disable button during operation
            binding.btnAddQuiz.setEnabled(false);
            binding.btnAddQuiz.setText("Adding...");

            // Add quiz to Firestore
            db.collection("quizzes")
                    .add(quizData)
                    .addOnSuccessListener(documentReference -> {
                        Log.d(TAG, "Quiz added successfully with ID: " + documentReference.getId());
                        Toast.makeText(this, "Quiz added successfully!", Toast.LENGTH_SHORT).show();

                        // Reset button
                        binding.btnAddQuiz.setText("Add New Quiz");

                        // Reload quizzes after adding
                        loadQuizzesData();
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Error adding quiz: ", e);
                        Log.e(TAG, "Error type: " + e.getClass().getSimpleName());
                        Log.e(TAG, "Error message: " + e.getMessage());

                        String errorMessage = "Error adding quiz: ";
                        if (e.getMessage() != null) {
                            errorMessage += e.getMessage();
                        } else {
                            errorMessage += "Unknown error";
                        }

                        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();

                        // Reset button
                        binding.btnAddQuiz.setEnabled(true);
                        binding.btnAddQuiz.setText("Add New Quiz");
                    });

        } catch (Exception e) {
            Log.e(TAG, "Exception in addQuizToFirestore: ", e);
            Toast.makeText(this, "Error adding quiz: " + e.getMessage(), Toast.LENGTH_LONG).show();

            // Reset button
            binding.btnAddQuiz.setEnabled(true);
            binding.btnAddQuiz.setText("Add New Quiz");
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
        Log.d(TAG, "onResume called");
        // Refresh data when returning to this activity
        if (quizList != null && quizAdapter != null) {
            loadQuizzesData();
        }
    }
}