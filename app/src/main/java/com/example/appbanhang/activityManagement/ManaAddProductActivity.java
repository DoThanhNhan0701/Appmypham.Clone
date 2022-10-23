package com.example.appbanhang.activityManagement;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appbanhang.R;
import com.example.appbanhang.model.Category;
import com.example.appbanhang.retrofit.ApiSell;
import com.example.appbanhang.retrofit.RetrofitCliend;
import com.example.appbanhang.utils.Utils;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ManaAddProductActivity extends AppCompatActivity {
    Toolbar toolbar;
    Spinner spinner;
    ApiSell apiSell;
    Button btnAddproduct;

    TextInputEditText textInputEditTextName;
    TextInputEditText textInputEditTextImages;
    TextInputEditText textInputEditTextNew;
    TextInputEditText textInputEditTextOld;
    TextInputEditText textInputEditTextSl;
    TextInputEditText textInputEditTextDate;
    TextInputEditText textInputEditTextDescription;

    List<Category> categoryList;
    List<String> stringList;
    String name;
    int categories = 0;
    CompositeDisposable compositeDisposable = new CompositeDisposable();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mana_add_product);
        apiSell = RetrofitCliend.getInstance(Utils.BASE_URL).create(ApiSell.class);
        mapping();
        setActionData();
        setActioToolbar();
    }

    private void addProduct() {
        btnAddproduct.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SimpleDateFormat")
            @Override
            public void onClick(View view) {
                String name = Objects.requireNonNull(textInputEditTextName.getText()).toString().trim();
                String images = Objects.requireNonNull(textInputEditTextImages.getText()).toString().trim();
                String priceNew = Objects.requireNonNull(textInputEditTextNew.getText()).toString().trim();
                String priceOld = Objects.requireNonNull(textInputEditTextOld.getText()).toString().trim();
                String soluong = Objects.requireNonNull(textInputEditTextSl.getText()).toString().trim();
                String createDate = Objects.requireNonNull(textInputEditTextDate.getText()).toString().trim();
                String description = Objects.requireNonNull(textInputEditTextDescription.getText()).toString().trim();
                if (TextUtils.isEmpty(name)){
                    Toast.makeText(getApplicationContext(), "Bạn chưa nhập tên sản phẩm", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(images)){
                    Toast.makeText(getApplicationContext(), "Bạn chưa chọn ảnh sản phẩm", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(description)){
                    Toast.makeText(getApplicationContext(), "Bạn chưa nhập mô tả sản phẩm", Toast.LENGTH_SHORT).show();
                }
                else if(priceNew.equals("")){
                    Toast.makeText(getApplicationContext(), "Bạn chưa nhập giá mới sản phẩm", Toast.LENGTH_SHORT).show();
                }
                else if(priceOld.equals("")){
                    Toast.makeText(getApplicationContext(), "Bạn chưa nhập giá cũ sản phẩm", Toast.LENGTH_SHORT).show();
                }
                else if(soluong.equals("")){
                    Toast.makeText(getApplicationContext(), "Bạn chưa nhập số lượng sản phẩm", Toast.LENGTH_SHORT).show();
                }
                else if(createDate.equals("")){
                    Toast.makeText(getApplicationContext(), "Bạn chưa nhập ngày tọa sản phẩm", Toast.LENGTH_SHORT).show();
                }
                else{
                    compositeDisposable.add(apiSell.addProduct(categories, name, images, Integer.parseInt(priceNew), Integer.parseInt(priceOld), Integer.parseInt(soluong), createDate, description)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    addProductModel -> {
                                        if(addProductModel.isSuccess()){
                                            Toast.makeText(getApplicationContext(), "Đã thêm sản phẩm thành công !", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(getApplicationContext(), ManagementProductActivity.class);
                                            startActivity(intent);
                                        }
                                        else {
                                            Toast.makeText(getApplicationContext(), addProductModel.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    },
                                    throwable -> {
                                        Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                            )
                    );

                }
            }
        });

    }

    private void setActioToolbar() {
        toolbar.setNavigationIcon(R.drawable.icon_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ManagementProductActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setActionData() {
        compositeDisposable.add(apiSell.getCategory()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        categoryModel -> {
                            if(categoryModel.isSuccess()){
                                categoryList = categoryModel.getResult();
                                for(int i = 0; i < categoryList.size(); i++){
                                    name = categoryModel.getResult().get(i).getName();
                                    stringList.add(name);
                                    Log.d("String list", "setActionData: " + stringList);
                                }
                            }
                            ArrayAdapter<String> arraySol = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, stringList);
                            spinner.setAdapter(arraySol);
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                )

        );
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                categories = i;
                categories += 1;
                Log.d("Vị trí", "onItemSelected: " + categories);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        addProduct();
    }

    private void mapping() {
        categoryList = new ArrayList<>();
        stringList = new ArrayList<>();

        toolbar = (Toolbar) findViewById(R.id.toolbarthemsanpham);
        spinner = (Spinner) findViewById(R.id.inputCategories);
        btnAddproduct = (Button) findViewById(R.id.btnAddProduct);

        textInputEditTextName = (TextInputEditText) findViewById(R.id.inputNameA);
        textInputEditTextImages = (TextInputEditText) findViewById(R.id.inputImagesA);
        textInputEditTextNew = (TextInputEditText) findViewById(R.id.inputPriceNewA);
        textInputEditTextOld = (TextInputEditText) findViewById(R.id.inputPriceOldA);
        textInputEditTextSl = (TextInputEditText) findViewById(R.id.inputSoluongA);
        textInputEditTextDate = (TextInputEditText) findViewById(R.id.inputNgaytaoSpA);
        textInputEditTextDescription= (TextInputEditText) findViewById(R.id.inputMotaSanphamA);

    }
}