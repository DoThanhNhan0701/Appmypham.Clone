package com.example.appbanhang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appbanhang.R;
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

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textViewName;
        TextView textViewPrice;
        ImageView imageViewProduct;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            textViewName = (TextView) itemView.findViewById(R.id.textNameProduct);
            textViewPrice = (TextView) itemView.findViewById(R.id.textPriceProduct);
            imageViewProduct = (ImageView) itemView.findViewById(R.id.imagesProduct);

        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_products, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Product product = listItemsProduct.get(position);
        holder.textViewName.setText(product.getName());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.textViewPrice.setText(decimalFormat.format(Double.parseDouble(String.valueOf(product.getPrice()))));
        Glide.with(context).load(product.getImages()).into(holder.imageViewProduct);
    }

    @Override
    public int getItemCount() {
        return listItemsProduct.size();
    }
}
