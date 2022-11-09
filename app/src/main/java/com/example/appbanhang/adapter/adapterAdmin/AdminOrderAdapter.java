package com.example.appbanhang.adapter.adapterAdmin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbanhang.R;
import com.example.appbanhang.adapter.adpterUser.DetailOrderAdapter;
import com.example.appbanhang.interFace.ItemClickListener;
import com.example.appbanhang.model.ViewOrder;
import com.example.appbanhang.utils.eventbus.OrderEventBus;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class AdminOrderAdapter extends RecyclerView.Adapter<AdminOrderAdapter.MyViewHolder> {
    private final RecyclerView.RecycledViewPool recycledViewPool = new RecyclerView.RecycledViewPool();
    Context context;
    List<ViewOrder> viewOrderList;

    public AdminOrderAdapter(Context context, List<ViewOrder> viewOrderList) {
        this.context = context;
        this.viewOrderList = viewOrderList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_order_admin, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint({"SetTextI18n"})
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ViewOrder order = viewOrderList.get(position);
        holder.userIdOder.setText("Đơn hàng: "+ order.getId());
        holder.userDateOrder.setText("Ngày đặt: " + order.getCreate_date());
        String addressUser = order.getDiachi() + "_" + order.getQuan() + "_" + order.getPhuong() + "_" + order.getThanhpho();
        holder.addressUserOrder.setText("Địa chỉ: " + addressUser);
        holder.phoneUserOrder.setText("Phone: " + order.getSodienthoai());
        holder.statusUserOrder.setText(status(order.getStatus()));
        holder.nameUserOder.setText("Tên khách hàng: " + order.getIduser());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(holder.detailRecyclerView.getContext(), LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setInitialPrefetchItemCount(order.getProductorder().size());
        //
        DetailOrderAdapter detailOrderAdapter = new DetailOrderAdapter(context, order.getProductorder());
        holder.detailRecyclerView.setLayoutManager(linearLayoutManager);
        holder.detailRecyclerView.setAdapter(detailOrderAdapter);
        holder.detailRecyclerView.setRecycledViewPool(recycledViewPool);
        holder.imageStatusClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().postSticky(new OrderEventBus(order));
            }
        });
    }

    private String status(int status){
        String resuilt = "";
        switch (status){
            case 0:
                resuilt = "Đơn hàng đã đặt";
                break;
            case 1:
                resuilt = "Đơn hàng đang được xử lí !";
                break;
            case 2:
                resuilt = "Đơn hàng đang giao đến đơn vị vận chuyển";
                break;
            case 3:
                resuilt = "Đơn hàng đã giao thành công";
                break;
            case 4:
                resuilt = "Đơn hàng đã hủy";
                break;
            default:
                break;
        }

        return resuilt;
    }

    @Override
    public int getItemCount() {
        return viewOrderList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView userIdOder, userDateOrder;
        TextView nameUserOder, addressUserOrder, phoneUserOrder, statusUserOrder;
        RecyclerView detailRecyclerView;
        ImageView imageStatusClick;
        private ItemClickListener itemClickListener;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            userIdOder = (TextView) itemView.findViewById(R.id.userIdOrder);
            userDateOrder = (TextView) itemView.findViewById(R.id.dateOrderAdmin);
            nameUserOder = (TextView) itemView.findViewById(R.id.nameUserOrder);
            addressUserOrder = (TextView) itemView.findViewById(R.id.addressUserOrder);
            phoneUserOrder = (TextView) itemView.findViewById(R.id.phoneUserOrder);
            statusUserOrder = (TextView) itemView.findViewById(R.id.statusUserOrder);
            detailRecyclerView = (RecyclerView) itemView.findViewById(R.id.recyclerUserOrderAdmin);
            imageStatusClick = (ImageView) itemView.findViewById(R.id.statusOrderClick);
        }
    }
}
