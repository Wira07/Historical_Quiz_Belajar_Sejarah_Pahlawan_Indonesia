package com.muhammadadin.belajarsejarahpahlawanindonesia.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.muhammadadin.belajarsejarahpahlawanindonesia.R;
import com.muhammadadin.belajarsejarahpahlawanindonesia.models.Hero;

import java.util.List;

public class HeroAdapter extends RecyclerView.Adapter<HeroAdapter.HeroViewHolder> {

    private static final String TAG = "HeroAdapter";
    private List<Hero> heroList;
    private OnHeroClickListener listener;
    private Context context;

    public interface OnHeroClickListener {
        void onHeroClick(Hero hero);
    }

    public HeroAdapter(List<Hero> heroList, OnHeroClickListener listener) {
        this.heroList = heroList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public HeroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_hero, parent, false);
        return new HeroViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HeroViewHolder holder, int position) {
        try {
            Hero hero = heroList.get(position);
            holder.bind(hero);
        } catch (Exception e) {
            Log.e(TAG, "Error binding hero at position " + position, e);
        }
    }

    @Override
    public int getItemCount() {
        return heroList != null ? heroList.size() : 0;
    }

    public void updateHeroList(List<Hero> newHeroList) {
        this.heroList = newHeroList;
        notifyDataSetChanged();
    }

    class HeroViewHolder extends RecyclerView.ViewHolder {
        private TextView tvHeroName;
        private TextView tvHeroDescription;
        private ImageView ivHeroImage;

        public HeroViewHolder(@NonNull View itemView) {
            super(itemView);
            try {
                tvHeroName = itemView.findViewById(R.id.tvHeroName);
                tvHeroDescription = itemView.findViewById(R.id.tvHeroDescription);
                ivHeroImage = itemView.findViewById(R.id.ivHeroImage);
            } catch (Exception e) {
                Log.e(TAG, "Error initializing ViewHolder", e);
            }
        }

        public void bind(Hero hero) {
            try {
                if (hero == null) {
                    Log.w(TAG, "Hero is null, skipping bind");
                    return;
                }

                // Set hero name
                if (tvHeroName != null) {
                    tvHeroName.setText(hero.getName() != null ? hero.getName() : "Unknown Hero");
                }

                // Set hero description
                if (tvHeroDescription != null) {
                    tvHeroDescription.setText(hero.getDescription() != null ? hero.getDescription() : "No description available");
                }

                // Load hero image with Glide
                if (ivHeroImage != null && context != null) {
                    String imageUrl = hero.getImageUrl();

                    Glide.with(context)
                            .load(imageUrl)
                            .placeholder(R.drawable.ic_person) // Placeholder while loading
                            .error(R.drawable.ic_person) // Error image if loading fails
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .centerCrop()
                            .into(ivHeroImage);
                }

                // Set click listener
                itemView.setOnClickListener(v -> {
                    if (listener != null) {
                        listener.onHeroClick(hero);
                    }
                });

            } catch (Exception e) {
                Log.e(TAG, "Error in bind method", e);
            }
        }
    }
}