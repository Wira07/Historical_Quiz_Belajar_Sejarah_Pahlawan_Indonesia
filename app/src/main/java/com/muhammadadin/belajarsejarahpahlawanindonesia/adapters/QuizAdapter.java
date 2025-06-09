package com.muhammadadin.belajarsejarahpahlawanindonesia.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.muhammadadin.belajarsejarahpahlawanindonesia.R;
import com.muhammadadin.belajarsejarahpahlawanindonesia.models.Quiz;

import java.util.List;

public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.QuizViewHolder> {

    private static final String TAG = "QuizAdapter";
    private List<Quiz> quizList;
    private OnQuizClickListener listener;
    private Context context;

    public interface OnQuizClickListener {
        void onQuizClick(Quiz quiz);
    }

    public QuizAdapter(List<Quiz> quizList, OnQuizClickListener listener) {
        this.quizList = quizList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public QuizViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_quiz, parent, false);
        return new QuizViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizViewHolder holder, int position) {
        try {
            Quiz quiz = quizList.get(position);
            holder.bind(quiz);
        } catch (Exception e) {
            Log.e(TAG, "Error binding quiz at position " + position, e);
        }
    }

    @Override
    public int getItemCount() {
        return quizList != null ? quizList.size() : 0;
    }

    public void updateQuizList(List<Quiz> newQuizList) {
        this.quizList = newQuizList;
        notifyDataSetChanged();
    }

    class QuizViewHolder extends RecyclerView.ViewHolder {
        private TextView tvQuestion;
        private TextView tvOptions;
        private TextView tvCorrectAnswer;

        public QuizViewHolder(@NonNull View itemView) {
            super(itemView);
            try {
                tvQuestion = itemView.findViewById(R.id.tvQuestion);
                tvOptions = itemView.findViewById(R.id.tvOptions);
                tvCorrectAnswer = itemView.findViewById(R.id.tvCorrectAnswer);
            } catch (Exception e) {
                Log.e(TAG, "Error initializing ViewHolder", e);
            }
        }

        public void bind(Quiz quiz) {
            try {
                if (quiz == null) {
                    Log.w(TAG, "Quiz is null, skipping bind");
                    return;
                }

                // Set question
                if (tvQuestion != null) {
                    tvQuestion.setText(quiz.getQuestion() != null ? quiz.getQuestion() : "No question available");
                }

                // Set options
                if (tvOptions != null && quiz.getOptions() != null) {
                    StringBuilder optionsText = new StringBuilder();
                    String[] options = quiz.getOptions();

                    for (int i = 0; i < options.length; i++) {
                        if (i > 0) optionsText.append("\n");
                        optionsText.append((char)('A' + i)).append(". ").append(options[i]);
                    }

                    tvOptions.setText(optionsText.toString());
                }

                // Set correct answer
                if (tvCorrectAnswer != null && quiz.getOptions() != null) {
                    int correctIndex = quiz.getCorrectAnswer();
                    if (correctIndex >= 0 && correctIndex < quiz.getOptions().length) {
                        String correctAnswerText = "Correct: " + (char)('A' + correctIndex) + ". " + quiz.getOptions()[correctIndex];
                        tvCorrectAnswer.setText(correctAnswerText);
                    } else {
                        tvCorrectAnswer.setText("Correct answer: Invalid");
                    }
                }

                // Set click listener
                itemView.setOnClickListener(v -> {
                    if (listener != null) {
                        listener.onQuizClick(quiz);
                    }
                });

            } catch (Exception e) {
                Log.e(TAG, "Error in bind method", e);
            }
        }
    }
}