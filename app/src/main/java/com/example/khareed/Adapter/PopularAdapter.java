package com.example.khareed.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.khareed.Activity.DetailActivity;
import com.example.khareed.Domain.ItemsDomain;
import com.example.khareed.databinding.ViewholderPopListBinding;

import java.util.ArrayList;

public class PopularAdapter extends RecyclerView.Adapter<PopularAdapter.Viewholder> {
    ArrayList<ItemsDomain> items;
    Context context;

    public PopularAdapter(ArrayList<ItemsDomain> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        ViewholderPopListBinding binding = ViewholderPopListBinding.inflate(LayoutInflater.from(context), parent, false);
        return new Viewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        holder.binding.title.setText(items.get(position).getTitle());
        holder.binding.reviewTxt.setText("" + items.get(position).getReview());
        holder.binding.priceTxt.setText("\u20B9" + items.get(position).getPrice());
        holder.binding.ratingTxt.setText("(" + items.get(position).getRating() + ")");
        holder.binding.oldPriceTxt.setText("\u20B9" + items.get(position).getOldPrice());
        holder.binding.oldPriceTxt.setPaintFlags(holder.binding.oldPriceTxt.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.binding.ratingBar.setRating((float) items.get(position).getRating());

        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transform(new CenterCrop());

        Glide.with(context)
                .load(items.get(position).getPicUrl().get(0))
                .apply(requestOptions)
                .into(holder.binding.pic);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("object", items.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        ViewholderPopListBinding binding;

        public Viewholder(ViewholderPopListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
