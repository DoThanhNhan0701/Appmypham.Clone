package com.example.appbanhang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.appbanhang.R;
import com.example.appbanhang.model.Category;

import java.util.List;

public class CategoryAdapter extends BaseAdapter {
    List<Category> listCategory;
    Context context;

    public CategoryAdapter(Context context, List<Category> categoryList) {
        this.listCategory = categoryList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return listCategory.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
    public class ViewHolder{
        TextView decription;
        TextView name;
        ImageView Images;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if(view == null){
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.items_category, null);

            viewHolder.name = view.findViewById(R.id.nameCategrory);
            viewHolder.decription = view.findViewById(R.id.decription);
            viewHolder.Images = view.findViewById(R.id.imagesCategory);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.name.setText(listCategory.get(i).getName());
        viewHolder.decription.setText(listCategory.get(i).getDecription());
        Glide.with(context).load(listCategory.get(i).getImage()).into(viewHolder.Images);
        return view;
    }
}