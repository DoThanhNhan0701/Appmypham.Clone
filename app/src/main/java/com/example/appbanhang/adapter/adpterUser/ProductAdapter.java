package com.example.appbanhang.adapter.adpterUser;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appbanhang.R;
import com.example.appbanhang.activity.screenUser.DetailProductActivity;
import com.example.appbanhang.interFace.ItemClickListener;
import com.example.appbanhang.model.Product;

import java.text.DecimalFormat;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<Product> listItemsProduct;
    public static final int VIEW_TYPE_DATA = 0;
    public static final int VIEW_TYPE_LOADING = 1;


    public ProductAdapter(Context context, List<Product> listItemsProduct) {
        this.context = context;
        this.listItemsProduct = listItemsProduct;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
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
    public static class LoadingViewHolder extends RecyclerView.ViewHolder{
        ProgressBar progressBar;
        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBarMain);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_DATA){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_products, parent, false);
            return new MyViewHolder(view);
        }else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.loading_product_main, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof MyViewHolder){
            MyViewHolder myViewHolder = (MyViewHolder) holder;
            Product product = listItemsProduct.get(position);
            String nameProduct = product.getName();
            if(nameProduct.length() <= 15){
                myViewHolder.textViewName.setText(nameProduct);
            }
            else{
                myViewHolder.textViewName.setText(nameProduct.substring(0, 16) +"...");
            }

            DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
            myViewHolder.textViewPrice_new.setText(decimalFormat.format(Double
                    .parseDouble(String.valueOf(product.getPrice_new()))) + "đ");
            myViewHolder.textViewPrice_old.setText(decimalFormat.format(Double
                    .parseDouble(String.valueOf(product.getPrice_old()))) + "đ");
            myViewHolder.textViewPrice_old.setPaintFlags(myViewHolder.textViewPrice_old
                    .getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
            Glide.with(context).load(product.getImages()).into(myViewHolder.imageViewProduct);

            myViewHolder.setItemClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, int position, boolean isLongClick) {
                    if(!isLongClick){
                        Intent intentTitle = new Intent(context, DetailProductActivity.class);
                        intentTitle.putExtra("title", product);
                        intentTitle.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intentTitle);
                    }
                }
            });
        }else{

            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return listItemsProduct.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_DATA;
    }

    @Override
    public int getItemCount() {
        return listItemsProduct.size();
    }
}