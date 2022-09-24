//package com.example.appbanhang.adapter;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.bumptech.glide.Glide;
//import com.example.appbanhang.R;
//import com.example.appbanhang.model.Product;
//
//import java.text.DecimalFormat;
//import java.util.List;
//
//public class SonmoiAdapter extends RecyclerView.Adapter<SonmoiAdapter.MyViewHolder> {
//    Context context;
//    List<Product> listSonmoiAdapter;
//
//    public SonmoiAdapter(Context context, List<Product> listSonmoiAdapter) {
//        this.context = context;
//        this.listSonmoiAdapter = listSonmoiAdapter;
//    }
//
//    public class MyViewHolder extends RecyclerView.ViewHolder{
//        TextView textViewName;
//        TextView textViewPrice;
//        TextView textViewCreate_date;
//        ImageView imageViewProduct;
//
//
//        public MyViewHolder(@NonNull View itemView) {
//            super(itemView);
//            textViewName = (TextView) itemView.findViewById(R.id.textViewName);
//            textViewPrice = (TextView) itemView.findViewById(R.id.textViewPrice);
//            textViewCreate_date = (TextView) itemView.findViewById(R.id.textViewCreatedate);
//            imageViewProduct = (ImageView) itemView.findViewById(R.id.imageViewProduct);
//        }
//    }
//    @NonNull
//    @Override
//    public SonmoiAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_category_product, parent, false);
//        return new MyViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull SonmoiAdapter.MyViewHolder holder, int position) {
//        Product product = listSonmoiAdapter.get(position);
//        holder.textViewName.setText(product.getName());
//        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
//        holder.textViewPrice.setText(decimalFormat.format(Double.parseDouble(String.valueOf(product.getPrice()))));
//        holder.textViewCreate_date.setText((CharSequence) product.getCreate_date());
//        Glide.with(context).load(product.getImages()).into(holder.imageViewProduct);
//    }
//
//    @Override
//    public int getItemCount() {
//        return listSonmoiAdapter.size();
//    }
//}
