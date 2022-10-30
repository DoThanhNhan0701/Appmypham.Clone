package com.example.appbanhang.activity.screenAdmin;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.example.appbanhang.R;
import com.example.appbanhang.model.Category;
import com.example.appbanhang.retrofit.ApiSell;
import com.example.appbanhang.retrofit.RetrofitCliend;
import com.example.appbanhang.utils.Utils;
import com.example.appbanhang.utils.cloudinary.ConfigCloudinary;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AdminProduct extends AppCompatActivity {
    Toolbar toolbar;
    Spinner spinner;
    ApiSell apiSell;
    Button btnAddproduct;
    Button btnSeclectImages;
    TextView txtHienthi;

    TextInputEditText textInputEditTextName;
    TextInputEditText textInputDiscount;
    TextInputEditText textInputEditTextOld;
    TextInputEditText textInputEditTextSl;
    TextInputEditText textInputEditTextDate;
    TextInputEditText textInputEditTextDescription;

    List<Category> categoryList;
    List<String> stringList;
    String name;
    int categories = 0;
    CompositeDisposable compositeDisposable = new CompositeDisposable();

    private static final int IMAGE_REQUEST = 1;
    public static final String TAG = "Upload ###";
    private Uri imagePath;
    Map config = new HashMap();
    String urlDataImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_add_product);
        apiSell = RetrofitCliend.getInstance(Utils.BASE_URL).create(ApiSell.class);

        mapping();
        initConfig();
        setActionData();
        setActioToolbar();
        upLoadDataCloudinary();
        upLoadDataCloudinaryServer();
    }

    private void initConfig() {
        try {
            config.put("cloud_name", ConfigCloudinary.CLOUD_NAME);
            config.put("api_key", ConfigCloudinary.API_KEY);
            config.put("api_secret", ConfigCloudinary.API_SECRET);
            MediaManager.init(this, config);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void upLoadDataCloudinary() {
        btnSeclectImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPermission();
            }
        });
    }

    private void requestPermission() {
        if(ContextCompat.checkSelfPermission(AdminProduct.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            selectImages();
        }
        else{
            ActivityCompat.requestPermissions(AdminProduct.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, IMAGE_REQUEST);
        }
    }

    private void selectImages() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        someActivityResultLauncher.launch(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == IMAGE_REQUEST && requestCode == Activity.RESULT_OK && data != null && data.getData() != null){
            imagePath = data.getData();
        }
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        assert data != null;
                        imagePath = data.getData();
                        String fileName = String.valueOf(imagePath);
                        Cursor returnCursor = getContentResolver().query(imagePath, null, null, null, null);
                        try {
                            int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                            returnCursor.moveToFirst();
                            fileName = returnCursor.getString(nameIndex);
                            txtHienthi.setText(fileName);
                        }catch (Exception e){
                            Log.d(TAG, "onActivityResult: " + e);
                        } finally {
                            returnCursor.close();
                        }
                    }
                }
            });

    private void upLoadDataCloudinaryServer() {
        btnAddproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imagePath == null){
                    Toast.makeText(getApplicationContext(), "Bạn chưa chọn hình ảnh", Toast.LENGTH_SHORT).show();
                }
                else {
                    uploadDataNow();
                }
            }
        });
    }

    private void uploadDataNow() {
        MediaManager.get().upload(imagePath).callback(new UploadCallback() {
            @Override
            public void onStart(String requestId) {
                Log.d(TAG, "onStart: " + "Đang chuẩn bị tải");
            }

            @Override
            public void onProgress(String requestId, long bytes, long totalBytes) {
                Log.d(TAG, "onProgress: " + "onProgress");
            }

            @Override
            public void onSuccess(String requestId, Map resultData) {
                Log.d(TAG, "onSuccess: " + "onSuccess");
                urlDataImages = Objects.requireNonNull(resultData.get("url").toString());
                addProduct();
            }

            @Override
            public void onError(String requestId, ErrorInfo error) {
                Log.d(TAG, "onError: " + error);
            }

            @Override
            public void onReschedule(String requestId, ErrorInfo error) {
                Log.d(TAG, "onReschedule: " + error);
            }
        }).dispatch();
    }

    private void showMessage(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void addProduct() {
        String name = Objects.requireNonNull(textInputEditTextName.getText()).toString().trim();
        String images = urlDataImages;
        String priceOld = Objects.requireNonNull(textInputEditTextOld.getText()).toString().trim();
        String discount = Objects.requireNonNull(textInputDiscount.getText().toString().trim());
        String soluong = Objects.requireNonNull(textInputEditTextSl.getText()).toString().trim();
        String createDate = Objects.requireNonNull(textInputEditTextDate.getText()).toString().trim();
        String description = Objects.requireNonNull(textInputEditTextDescription.getText()).toString().trim();
        if (TextUtils.isEmpty(name)){
            showMessage("Bạn chưa nhập tên sản phẩm");
        }
        else if(TextUtils.isEmpty(images)){
            showMessage("Bạn chưa chọn hình ảnh sản phẩm");
        }
        else if(priceOld.equals("")){
            showMessage("Bạn chưa nhập giá sản phẩm");
        }
        else if(discount.equals("")){
            showMessage("Bạn chưa nhập giảm giá");
        }
        else if(soluong.equals("")){
            showMessage("Bạn chưa nhập số lượng");
        }
        else if(TextUtils.isEmpty(description)){
            showMessage("Bạn chưa nhập chi tiết sản phẩm");
        }
        else if(createDate.equals("")){
            showMessage("Bạn chưa nhập ngày xuất bản sản phẩm");
        }
        else{
            compositeDisposable.add(apiSell.addProduct(categories, name, images, Integer.parseInt(priceOld), Integer.parseInt(discount), Integer.parseInt(soluong), createDate, description)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            addProductModel -> {
                                if(addProductModel.isSuccess()){
                                    Toast.makeText(getApplicationContext(), "Đã thêm sản phẩm thành công !", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), AdminProductActivity.class);
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
    }

    private void setActioToolbar() {
        toolbar.setNavigationIcon(R.drawable.icon_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AdminProductActivity.class);
                startActivity(intent);
            }
        });
    }


    private void mapping() {
        categoryList = new ArrayList<>();
        stringList = new ArrayList<>();

        toolbar = (Toolbar) findViewById(R.id.toolbarthemsanpham);
        spinner = (Spinner) findViewById(R.id.inputCategories);
        btnAddproduct = (Button) findViewById(R.id.btnAddProduct);
        btnSeclectImages = (Button) findViewById(R.id.buttonSelectImages);
        txtHienthi = (TextView) findViewById(R.id.hienthinameimages);

        textInputEditTextName = (TextInputEditText) findViewById(R.id.inputNameA);
        textInputDiscount = (TextInputEditText) findViewById(R.id.inputDiscount);
        textInputEditTextOld = (TextInputEditText) findViewById(R.id.inputPriceOldA);
        textInputEditTextSl = (TextInputEditText) findViewById(R.id.inputSoluongA);
        textInputEditTextDate = (TextInputEditText) findViewById(R.id.inputNgaytaoSpA);
        textInputEditTextDescription= (TextInputEditText) findViewById(R.id.inputMotaSanphamA);

    }
}