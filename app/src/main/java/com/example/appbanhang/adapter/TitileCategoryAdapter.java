package com.example.appbanhang.adapter;

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
import com.example.appbanhang.activity.DetailProductActivity;
import com.example.appbanhang.interFace.ItemClickListener;
import com.example.appbanhang.model.Product;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class TitileCategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<Product> listTitleCategoryAdapter;
    public static final int VIEW_TYPE_DATA = 0;
    public static final int VIEW_TYPE_LOADING = 1;

    public TitileCategoryAdapter(Context context, List<Product> listTitleCategoryAdapter) {
        this.context = context;
        this.listTitleCategoryAdapter = listTitleCategoryAdapter;
    }
    public class LoadingViewHolder extends RecyclerView.ViewHolder{
        ProgressBar progressBar;
        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
        }
    }
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textViewName;
        TextView textViewPrice_new;
        TextView textViewPrice_old;
        TextView textViewSl;
        TextView textViewCreate_date;
        ImageView imageViewProduct;
        private ItemClickListener itemClickListener;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = (TextView) itemView.findViewById(R.id.textViewNameTitle);
            textViewPrice_new = (TextView) itemView.findViewById(R.id.textViewPrice_new);
            textViewPrice_old = (TextView) itemView.findViewById(R.id.textViewPrice_old);
            textViewSl = (TextView) itemView.findViewById(R.id.textViewSl);
            textViewCreate_date = (TextView) itemView.findViewById(R.id.textViewCreatedate);
            imageViewProduct = (ImageView) itemView.findViewById(R.id.imageViewProduct);
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
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_DATA){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_category_product, parent, false);
            return new MyViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_loading, parent, false);
            return new LoadingViewHolder(view);
        }

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof MyViewHolder){
            MyViewHolder myViewHolder = (MyViewHolder) holder;
            Product product = listTitleCategoryAdapter.get(position);

            String categoryProduct = product.getName();
            if(categoryProduct.length() <= 19){
                myViewHolder.textViewName.setText(categoryProduct);
            }else{
                myViewHolder.textViewName.setText(categoryProduct.substring(0, 20) +"...");
            }

            DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
            myViewHolder.textViewPrice_new.setText(decimalFormat.format(Double
                    .parseDouble(String.valueOf(product.getPrice_new()))) + "đ");
            myViewHolder.textViewPrice_old.setText(decimalFormat.format(Double
                    .parseDouble(String.valueOf(product.getPrice_old()))) + "đ");
            myViewHolder.textViewPrice_old.setPaintFlags(myViewHolder.textViewPrice_old
                    .getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);

            myViewHolder.textViewSl.setText(String.valueOf(product.getAmount()));

            DateFormat dateFormat = null;
            dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            myViewHolder.textViewCreate_date.setText(dateFormat.format(product.getCreate_date()));
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

        }else {
            LoadingViewHolder loadingView = (LoadingViewHolder) holder;
            loadingView.progressBar.setIndeterminate(true);
        }

    }

    @Override
    public int getItemViewType(int position) {
        return listTitleCategoryAdapter.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_DATA;
    }

    @Override
    public int getItemCount() {
        return listTitleCategoryAdapter.size();
    }
}
