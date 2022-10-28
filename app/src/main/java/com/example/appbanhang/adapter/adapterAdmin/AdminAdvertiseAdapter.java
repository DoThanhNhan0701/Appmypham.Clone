package com.example.appbanhang.adapter.adapterAdmin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.appbanhang.R;
import com.example.appbanhang.interFace.ItemClickListener;
import com.example.appbanhang.model.Advertisement;
import com.example.appbanhang.utils.eventbus.UpdateDeleteEventBus;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class AdminAdvertiseAdapter extends RecyclerView.Adapter<AdminAdvertiseAdapter.MyViewHolder> {
    Context context;
    List<Advertisement> advertisementList;

    public AdminAdvertiseAdapter(Context context, List<Advertisement> advertisementList) {
        this.context = context;
        this.advertisementList = advertisementList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_admin_advertise, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Advertisement advertisement = advertisementList.get(position);
        holder.txtIdAvertise.setText("Quãng cáo: " + advertisement.getId());

        Glide.with(context).load(advertisement.getImages()).into(holder.imageViewQc);

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if(isLongClick){
                    EventBus.getDefault().postSticky(new UpdateDeleteEventBus(advertisement));
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return advertisementList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener, View.OnClickListener, View.OnLongClickListener {
        TextView txtIdAvertise;
        ImageView imageViewQc;
        private ItemClickListener itemClickListener;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtIdAvertise = (TextView) itemView.findViewById(R.id.idAdvertise);
            imageViewQc = (ImageView) itemView.findViewById(R.id.imagesAdvertiseAdmin);
            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.add(0, 0, getAdapterPosition(), "Sửa");
            contextMenu.add(0, 1, getAdapterPosition(), "Xóa");

        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view, getAdapterPosition(), false);
        }

        @Override
        public boolean onLongClick(View view) {
            itemClickListener.onClick(view, getAdapterPosition(), true);
            return false;
        }
    }

}
