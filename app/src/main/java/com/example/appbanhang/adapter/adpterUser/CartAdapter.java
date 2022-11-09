package com.example.appbanhang.adapter.adpterUser;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appbanhang.R;
import com.example.appbanhang.interFace.ImageViewOnClickListener;
import com.example.appbanhang.model.Cart;
import com.example.appbanhang.utils.eventbus.TotalEventBus;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {
    Context context;
    List<Cart> listCart;

    public CartAdapter(Context context, List<Cart> listCart) {
        this.context = context;
        this.listCart = listCart;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_cart, parent, false);

        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Cart cart = listCart.get(position);
        String nameCart = cart.getName();
        if(nameCart.length() <= 15){
            holder.textViewNameCartItems.setText(nameCart);
        }else{
            holder.textViewNameCartItems.setText(nameCart.substring(0,16) +"...");
        }

        holder.soluongCartItems.setText(String.valueOf(cart.getAmount_cart()));
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.textViewPriceCartItems.setText(decimalFormat.format(Double.parseDouble(String.valueOf(cart.getPrice()))) + "đ");


        long priceItemCartAll = (long) cart.getAmount_cart() * Long.parseLong(cart.getPrice());
        holder.textViewItemsPriceAllCart.setText(decimalFormat.format(Long.parseLong(String.valueOf(priceItemCartAll))) + "đ");

        Log.d("So luong", "onBindViewHolder: " +cart.getAmount_cart());
        Glide.with(context).load(cart.getImages()).into(holder.imageViewItemCart);

        holder.setImageViewOnClickListener(new ImageViewOnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClickImage(View view, int position, int dem) {
                if(dem == 2){
                    if(listCart.get(position).getAmount_cart() > 1){  // > 1 plus
                        int amount_new = listCart.get(position).getAmount_cart() - 1;
                        listCart.get(position).setAmount_cart(amount_new);
                    }
                    holder.soluongCartItems.setText(String.valueOf(listCart.get(position).getAmount_cart()));
                    long priceItemNew = Long.parseLong(listCart.get(position).getPrice()) * listCart.get(position).getAmount_cart();
                    DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                    holder.textViewItemsPriceAllCart.setText(decimalFormat.format(priceItemNew));
                    EventBus.getDefault().postSticky(new TotalEventBus());
                }
                else if(dem == 1){
                    if(listCart.get(position).getAmount_cart() < 11){
                        int amount_new = listCart.get(position).getAmount_cart() + 1;
                        listCart.get(position).setAmount_cart(amount_new);
                    }
                    holder.soluongCartItems.setText(String.valueOf(listCart.get(position).getAmount_cart()));
                    long priceItemNew = Long.parseLong(listCart.get(position).getPrice()) * listCart.get(position).getAmount_cart();
                    DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                    holder.textViewItemsPriceAllCart.setText(decimalFormat.format(priceItemNew));
                    EventBus.getDefault().postSticky(new TotalEventBus());
                }

            }
        });
    }


    @Override
    public int getItemCount() {
        return listCart.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textViewNameCartItems, textViewPriceCartItems, soluongCartItems, textViewItemsPriceAllCart;
        ImageView imageViewItemCart;
        ImageView imageViewCartPlus;
        ImageView imageViewCartMinus;
        ImageViewOnClickListener imageViewOnClickListener;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNameCartItems = itemView.findViewById(R.id.textViewItemNameCart);
            textViewPriceCartItems = itemView.findViewById(R.id.textViewItemPriceCart);
            soluongCartItems = itemView.findViewById(R.id.CartItemPlusMinus);
            textViewItemsPriceAllCart = itemView.findViewById(R.id.textViewItemPrice);
            imageViewItemCart = itemView.findViewById(R.id.imageViewItemCart);
            imageViewCartPlus = itemView.findViewById(R.id.imageViewPlusCart);
            imageViewCartMinus = itemView.findViewById(R.id.imageViewMinusCart);
            // Click
            imageViewCartPlus.setOnClickListener(this);
            imageViewCartMinus.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(view == imageViewCartPlus){
                imageViewOnClickListener.onClickImage(view, getAdapterPosition(), 2);

            }else if(view == imageViewCartMinus){
                imageViewOnClickListener.onClickImage(view, getAdapterPosition(), 1);
            }
        }

        public void setImageViewOnClickListener(ImageViewOnClickListener imageViewOnClickListener) {
            this.imageViewOnClickListener = imageViewOnClickListener;
        }
    }
}
