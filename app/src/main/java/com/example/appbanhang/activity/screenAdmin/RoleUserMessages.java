package com.example.appbanhang.activity.screenAdmin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appbanhang.R;
import com.example.appbanhang.adapter.adapterAdmin.RoleMesagesAdapter;
import com.example.appbanhang.databinding.ActivityRoleUserMessagesBinding;
import com.example.appbanhang.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class RoleUserMessages extends AppCompatActivity {
    RecyclerView.LayoutManager layoutManager;
    FirebaseFirestore firestore;
    RoleMesagesAdapter mesagesAdapter;
    List<User> list;
    private ActivityRoleUserMessagesBinding messagesBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        messagesBinding = ActivityRoleUserMessagesBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(messagesBinding.getRoot());
        setUpLayout();
        setToolbar();
        setDataRoleUser();
    }

    private void setUpLayout() {
        list = new ArrayList<>();
        layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        messagesBinding.recycleRoleMessages.setHasFixedSize(true);
        messagesBinding.recycleRoleMessages.setLayoutManager(layoutManager);
    }

    private void setDataRoleUser() {
        firestore = FirebaseFirestore.getInstance();
        firestore.collection("roleUser").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot documentSnapshot: task.getResult()){
                                User u = new User();
                                u.setId(documentSnapshot.getLong("id").intValue());
                                u.setFirst_name(documentSnapshot.getString("userName"));
                                list.add(u);
                                Log.d("####", "onComplete: " + list);
                            }
                            if(list.size() > 0){
                                mesagesAdapter = new RoleMesagesAdapter(getApplicationContext(), list);
                                messagesBinding.recycleRoleMessages.setAdapter(mesagesAdapter);
                                Log.d("####1", "onComplete: " + list.size());
                            }
                        }
                    }
                });
    }

    private void setToolbar() {
        messagesBinding.toolbarRoleMess.setNavigationIcon(R.drawable.icon_back);
        messagesBinding.toolbarRoleMess.setNavigationOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MainActivityAdmin.class);
            startActivity(intent);
            finish();
        });
    }
}