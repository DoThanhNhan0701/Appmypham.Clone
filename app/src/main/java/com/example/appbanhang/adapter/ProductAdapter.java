package com.example.appbanhang.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appbanhang.R;
import com.example.appbanhang.activity.TitleProductActivity;
import com.example.appbanhang.interFace.ItemClickListener;
import com.example.appbanhang.model.Product;

import java.text.DecimalFormat;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {
    Context context;
    List<Product> listItemsProduct;

    public ProductAdapter(Context context, List<Product> listItemsProduct) {
        this.context = context;
        this.listItemsProduct = listItemsProduct;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textViewName;
        TextView textViewPrice_new;
        TextView textViewPrice_old;
        ImageView imageViewProduct;
        private ItemClickListener itemClickListener;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            textViewName = (TextView) itemView.findViewById(R.id.textNameProduct);
            textViewPrice_new = (TextView) itemView.findViewById(R.id.textPriceProduct);
            textViewPrice_old = (TextView) itemView.findViewById(R.id.textPriceProductOld);
            imageViewProduct = (ImageView) itemView.findViewById(R.id.imagesProduct);
            itemView.setOnClickListener(this);

        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view, getAdapterPosition(), false);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_products, parent, false);

        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Product product = listItemsProduct.get(position);
        holder.textViewName.setText(product.getName());

        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.textViewPrice_new.setText(decimalFormat.format(Double
                .parseDouble(String.valueOf(product.getPrice_new()))) + "đ");
        holder.textViewPrice_old.setText(decimalFormat.format(Double
                .parseDouble(String.valueOf(product.getPrice_old()))) + "đ");
        holder.textViewPrice_old.setPaintFlags(holder.textViewPrice_old
                .getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);

        Glide.with(context).load(product.getImages()).into(holder.imageViewProduct);
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if(!isLongClick){
                    Intent intentTitle = new Intent(context, TitleProductActivity.class);
                    intentTitle.putExtra("title", product);
                    intentTitle.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intentTitle);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listItemsProduct.size();
    }
}
