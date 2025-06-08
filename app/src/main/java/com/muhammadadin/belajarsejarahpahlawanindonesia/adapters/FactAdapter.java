package com.muhammadadin.belajarsejarahpahlawanindonesia.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.muhammadadin.belajarsejarahpahlawanindonesia.databinding.ItemFactBinding;
import com.muhammadadin.belajarsejarahpahlawanindonesia.models.Fact;

import java.util.List;

public class FactAdapter extends RecyclerView.Adapter<FactAdapter.FactViewHolder> {
    private List<Fact> factList;

    public FactAdapter(List<Fact> factList) {
        this.factList = factList;
    }

    @NonNull
    @Override
    public FactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item_fact layout using ViewBinding
        ItemFactBinding binding = ItemFactBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new FactViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FactViewHolder holder, int position) {
        Fact fact = factList.get(position);
        holder.bind(fact);
    }

    @Override
    public int getItemCount() {
        return factList.size();
    }

    class FactViewHolder extends RecyclerView.ViewHolder {
        private ItemFactBinding binding;

        public FactViewHolder(ItemFactBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Fact fact) {
            binding.tvFactTitle.setText(fact.getTitle());
            binding.tvFactContent.setText(fact.getContent());
        }
    }
}
