package com.muhammadadin.belajarsejarahpahlawanindonesia.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.muhammadadin.belajarsejarahpahlawanindonesia.R;
import com.muhammadadin.belajarsejarahpahlawanindonesia.databinding.ItemUserBinding;
import com.muhammadadin.belajarsejarahpahlawanindonesia.models.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private List<User> userList;

    public UserAdapter(List<User> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Use the generated View Binding class
        ItemUserBinding binding = ItemUserBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new UserViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        private ItemUserBinding binding;

        public UserViewHolder(@NonNull ItemUserBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(User user) {
            // Use View Binding to set the data
            binding.tvUserName.setText(user.getName());
            binding.tvUserRole.setText(user.getRole());
        }
    }
}
