package com.example.appbanhang.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.appbanhang.R;
import com.example.appbanhang.activity.IntroActivity;
import com.example.appbanhang.activity.MainActivity;


public class SlideAdapter extends PagerAdapter {
    Context context;

    public SlideAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }


    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int i) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.activity_intro, container,  false);


        ImageView logoView = view.findViewById(R.id.logo);
        ImageView imageView1 = view.findViewById(R.id.ind1);
        ImageView imageView2 = view.findViewById(R.id.ind2);
        ImageView imageView3 = view.findViewById(R.id.ind3);
        TextView title = view.findViewById(R.id.title);
        TextView description = view.findViewById(R.id.desc);
        ImageView next = view.findViewById(R.id.next);
        ImageView back = view.findViewById(R.id.back);
        Button buttonStart = view.findViewById(R.id.btnGetStarted);

        switch (i){
            case 0:
                imageView1.setImageResource(R.drawable.seleted);
                imageView2.setImageResource(R.drawable.unselected);
                imageView3.setImageResource(R.drawable.unselected);
                logoView.setImageResource(R.drawable.introl0);
                title.setText("Mua sắm mõi lúc, mọi nơi");
                description.setText("Thời gian miễn phí, nhưng nó vô giá. Bạn không thể sở hữu nó, nhưng bạn có thể sử dụng nói");
                back.setVisibility(View.GONE);
                next.setVisibility(View.VISIBLE);
                break;
            case 1:
                imageView1.setImageResource(R.drawable.unselected);
                imageView2.setImageResource(R.drawable.seleted);
                imageView3.setImageResource(R.drawable.unselected);
                logoView.setImageResource(R.drawable.intro1);
                title.setText("Mua sắm mõi lúc, mọi nơi");
                description.setText("Thời gian miễn phí, nhưng nó vô giá. Bạn không thể sở hữu nó, nhưng bạn có thể sử dụng nói");
                back.setVisibility(View.VISIBLE);
                next.setVisibility(View.VISIBLE);
                break;
            case 2:
                imageView1.setImageResource(R.drawable.unselected);
                imageView2.setImageResource(R.drawable.unselected);
                imageView3.setImageResource(R.drawable.seleted);
                logoView.setImageResource(R.drawable.introl2);

                title.setText("Mua sắm mõi lúc, mọi nơi");
                description.setText("Thời gian miễn phí, nhưng nó vô giá. Bạn không thể sở hữu nó, nhưng bạn có thể sử dụng nói");
                back.setVisibility(View.VISIBLE);
                next.setVisibility(View.GONE);
                break;
        }
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentStart = new Intent(context, MainActivity.class);
                intentStart.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intentStart);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntroActivity.viewPager.setCurrentItem(i - 1);
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntroActivity.viewPager.setCurrentItem(i + 1);
            }
        });
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
