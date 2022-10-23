package com.example.appbanhang.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.appbanhang.fragment.DeliveryFragment;
import com.example.appbanhang.fragment.PurchaseApplicationFragment;

public class OrderTagAdapter extends FragmentStateAdapter {


    public OrderTagAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public OrderTagAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    public OrderTagAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch(position){
            case 0:
                return new PurchaseApplicationFragment();
            case 2:
                return new DeliveryFragment();
            default:
                return new DeliveryFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
