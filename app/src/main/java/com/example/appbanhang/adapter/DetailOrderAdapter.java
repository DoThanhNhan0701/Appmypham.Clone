package com.example.appbanhang.adapter;

import android.annotation.SuppressLint;
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
import com.example.appbanhang.model.ProductOrder;

import java.text.DecimalFormat;
import java.util.List;

public class DetailOrderAdapter extends RecyclerView.Adapter<DetailOrderAdapter.MyViewHolder> {
    Context context;
    List<ProductOrder> detailOrderList;

    public DetailOrderAdapter(Context context, List<ProductOrder> detailOrderList) {
        this.context = context;
        this.detailOrderList = detailOrderList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_detail_order, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ProductOrder productOrder = detailOrderList.get(position);
        String nameDetailOrder = productOrder.getName();
        if(nameDetailOrder.length() <= 23){
            holder.txtNameOrder.setText(nameDetailOrder);
        }else{
            holder.txtNameOrder.setText(nameDetailOrder.substring(0, 24));
        }
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.txtPriceOrder.setText("Giá: " +decimalFormat.format(Double.parseDouble(productOrder.getPrice() )) + "đ");
        holder.txtPriceSoLuong.setText(String.valueOf(productOrder.getSoluong()));
        Glide.with(context).load(productOrder.getImages()).into(holder.imageViewOrder);
    }

    @Override
    public int getItemCount() {
        return detailOrderList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewOrder;
        TextView txtNameOrder, txtPriceOrder, txtPriceSoLuong;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewOrder = (ImageView) itemView.findViewById(R.id.imageViewDetailOrder);
            txtNameOrder = (TextView) itemView.findViewById(R.id.txtNameDetailOrder);
            txtPriceOrder = (TextView) itemView.findViewById(R.id.txtNameDetailPrice);
            txtPriceSoLuong = (TextView) itemView.findViewById(R.id.txtNameDetailSoLuong);

        }
    }
}
