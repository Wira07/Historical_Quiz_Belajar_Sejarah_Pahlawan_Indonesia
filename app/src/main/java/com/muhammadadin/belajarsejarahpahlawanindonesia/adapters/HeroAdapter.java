package com.muhammadadin.belajarsejarahpahlawanindonesia.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.muhammadadin.belajarsejarahpahlawanindonesia.R;
import com.muhammadadin.belajarsejarahpahlawanindonesia.models.Hero;

import java.util.List;

public class HeroAdapter extends RecyclerView.Adapter<HeroAdapter.HeroViewHolder> {
    private List<Hero> heroList;
    private OnHeroClickListener listener;

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hero, parent, false);
        return new HeroViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HeroViewHolder holder, int position) {
        Hero hero = heroList.get(position);
        holder.bind(hero);
    }

    @Override
    public int getItemCount() {
        return heroList.size();
    }

    class HeroViewHolder extends RecyclerView.ViewHolder {
        private TextView tvHeroName;

        public HeroViewHolder(View itemView) {
            super(itemView);
            tvHeroName = itemView.findViewById(R.id.tvHeroName);
        }

        public void bind(Hero hero) {
            tvHeroName.setText(hero.getName());
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onHeroClick(hero);
                }
            });
        }
    }
}
