package com.example.appbanhang.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
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
import com.example.appbanhang.model.Product;

import java.text.SimpleDateFormat;
import java.util.List;

public class AdminProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<Product> productList;
    public static final int VIEW_TYPE_DATA = 0;
    public static final int VIEW_TYPE_LOADING = 1;

    public AdminProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }
    public static class LoadingViewHolder extends RecyclerView.ViewHolder{
        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)  {
        if(viewType == VIEW_TYPE_DATA){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_items_product, parent, false);
            return new MyViewHolder(view);
        }else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_loading, parent, false);
            return new LoadingViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof MyViewHolder){
            MyViewHolder myViewHolder = (MyViewHolder) holder;
            Product product = productList.get(position);
            myViewHolder.textViewName.setText(product.getName());
            myViewHolder.textViewPrice_old.setText(String.valueOf(product.getPrice_old()));
            myViewHolder.textViewPrice_new.setText(String.valueOf(product.getPrice_new()));
            myViewHolder.txtDescriptionProduct.setText(product.getDescription());

            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String dateProduct = simpleDateFormat.format(product.getCreate_date());
            myViewHolder.txtDateProduct.setText(dateProduct);

            Glide.with(context).load(product.getImages()).into(myViewHolder.imageViewProduct);
        }
        else {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return productList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_DATA;
    }


    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textViewName;
        TextView textViewPrice_new;
        TextView textViewPrice_old;
        ImageView imageViewProduct;
        TextView txtDateProduct;
        TextView txtDescriptionProduct;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = (TextView) itemView.findViewById(R.id.textNameProductAdmin);
            textViewPrice_new = (TextView) itemView.findViewById(R.id.textPriceProductNewAdmin);
            textViewPrice_old = (TextView) itemView.findViewById(R.id.textPriceProductOldAdmin);
            imageViewProduct = (ImageView) itemView.findViewById(R.id.imagesProductAdmin);
            txtDateProduct = (TextView) itemView.findViewById(R.id.date_product);
            txtDescriptionProduct = (TextView) itemView.findViewById(R.id.descripton_product);

        }
    }
}
