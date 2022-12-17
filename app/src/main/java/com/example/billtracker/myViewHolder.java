package com.example.billtracker;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class myViewHolder extends RecyclerView.ViewHolder {
    TextView category, bill;
    ImageView imageView;

    public myViewHolder(@NonNull View itemView) {
        super(itemView);
        category = itemView.findViewById(R.id.name);
        bill = itemView.findViewById(R.id.billView);
        imageView = itemView.findViewById(R.id.imageView);
    }
}
