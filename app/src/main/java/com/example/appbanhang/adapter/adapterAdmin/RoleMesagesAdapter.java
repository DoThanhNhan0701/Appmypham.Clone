package com.example.appbanhang.adapter.adapterAdmin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbanhang.R;
import com.example.appbanhang.activity.screenAdmin.SendMessagesRole;
import com.example.appbanhang.interFace.ItemClickListener;
import com.example.appbanhang.model.User;
import com.example.appbanhang.utils.Utils;

import java.util.List;


public class RoleMesagesAdapter extends RecyclerView.Adapter<RoleMesagesAdapter.MyViewHolder> {
    Context context;
    List<User> userList;

    public RoleMesagesAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.items_role_messages, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        User user = userList.get(position);
        holder.txtName.setText(user.getFirst_name() + user.getLast_name());
        holder.txtPhone.setText(String.valueOf(user.getPhone()));
        holder.txtId.setText(String.valueOf(user.getId()));

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if(user.getId() == Utils.userCurrent.getId()){
                    showMessages("Bạn không thể chat với chính mình được !!!");
                }else{
                    if(!isLongClick){
                        Intent intent = new Intent(context, SendMessagesRole.class);
                        intent.putExtra("idRole", user);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    private void showMessages(String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtName;
        TextView txtId;
        TextView txtPhone;
        private ItemClickListener itemClickListener;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtId = itemView.findViewById(R.id.idRoleMessage);
            txtName = itemView.findViewById(R.id.nameRoleMessage);
            txtPhone = itemView.findViewById(R.id.idRolePhone);
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
}
