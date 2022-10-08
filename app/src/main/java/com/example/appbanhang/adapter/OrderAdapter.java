package com.example.appbanhang.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbanhang.R;
import com.example.appbanhang.model.ViewOrder;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {
    private RecyclerView.RecycledViewPool recycledViewPool = new RecyclerView.RecycledViewPool();
    Context context;
    List<ViewOrder> viewOrderList;

    public OrderAdapter(Context context, List<ViewOrder> viewOrderList) {
        this.context = context;
        this.viewOrderList = viewOrderList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_order, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint({"SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ViewOrder donhang = viewOrderList.get(position);
        holder.idOrder.setText("Đơn hàng: "+ donhang.getId());

//        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//        holder.txtDateOrder.setText(dateFormat.format(donhang.getDateOrder()));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                holder.detailRecyclerView.getContext(), LinearLayoutManager.VERTICAL, false
        );
        linearLayoutManager.setInitialPrefetchItemCount(donhang.getProductorder().size());
        // DeitailOrderAdapter ReCyclerView nam trong ReCyclerView
        DetailOrderAdapter detailOrderAdapter = new DetailOrderAdapter(context, donhang.getProductorder());
        holder.detailRecyclerView.setLayoutManager(linearLayoutManager);
        holder.detailRecyclerView.setAdapter(detailOrderAdapter);
        holder.detailRecyclerView.setRecycledViewPool(recycledViewPool);
    }

    @Override
    public int getItemCount() {
        return viewOrderList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView idOrder, txtDateOrder;
        RecyclerView detailRecyclerView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            idOrder = (TextView) itemView.findViewById(R.id.txtIdOrder);
            txtDateOrder = (TextView) itemView.findViewById(R.id.txtDateOder);
            detailRecyclerView = (RecyclerView) itemView.findViewById(R.id.recyclerItemOrder);
        }
    }
}
