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
        TextView name;
        ImageView picture;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if(view == null){
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.items_products, null);

            viewHolder.name = view.findViewById(R.id.nameCategrory);
            viewHolder.picture = view.findViewById(R.id.imagesCategory);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.name.setText(listCategory.get(i).getName());
        Glide.with(context).load(listCategory.get(i).getPicture()).into(viewHolder.picture);
        return view;
    }
}
