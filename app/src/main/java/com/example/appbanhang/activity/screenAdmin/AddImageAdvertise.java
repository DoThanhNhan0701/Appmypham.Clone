package com.example.appbanhang.activity.screenAdmin;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.example.appbanhang.retrofit.ApiSell;
import com.example.appbanhang.retrofit.RetrofitCliend;
import com.example.appbanhang.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@SuppressWarnings("unchecked")
public class AddImageAdvertise extends AppCompatActivity {
    ImageView imageUploadImages;
    Button btnChonImages;
    Toolbar toolbar;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiSell apiSell;
    // Upload Images
    private static final int IMAGE_REQUEST = 1;
    public static final String TAG = "Upload ###";
    private Uri imagePath;
    Map config = new HashMap();
    String urlDataImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_image_advertise);
        apiSell = RetrofitCliend.getInstance(Utils.BASE_URL).create(ApiSell.class);

        mapping();
        actionToolbar();
        initConfig();
        upLoadDataCloudinary();
    }

    private void actionToolbar() {
        toolbar.setNavigationIcon(R.drawable.icon_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AdminAdvertiseActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void upLoadDataCloudinary() {
        imageUploadImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPermission();
            }
        });
        btnChonImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imagePath == null){
                    Toast.makeText(getApplicationContext(), "Bạn chưa chọn ảnh", Toast.LENGTH_SHORT).show();
                }
                else{
                    uploadImagesCloudinary();
                }
            }
        });
    }

    private void uploadImagesCloudinary() {
        String dispatch = MediaManager.get().upload(imagePath).callback(new UploadCallback() {
            @Override
            public void onStart(String requestId) {
                Log.d(TAG, "onStart: " + "Start");
            }
            @Override
            public void onProgress(String requestId, long bytes, long totalBytes) {
                Log.d(TAG, "onProgress: " + "uploading");
            }

            @Override
            public void onSuccess(String requestId, Map resultData) {
                Log.d(TAG, "onSuccess: " + "Success");
                urlDataImages = Objects.requireNonNull(resultData.get("url")).toString();
                insertDataServer();
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

    private void insertDataServer() {
        compositeDisposable.add(apiSell.addAdvertise(urlDataImages)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        advertisementModel -> {
                            if(advertisementModel.isSuccess()){
                                Toast.makeText(getApplicationContext(), "Thêm ảnh quảng cáo thành công", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), AdminAdvertiseActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            else{
                                Toast.makeText(getApplicationContext(), advertisementModel.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                )
        );
    }


    private void initConfig() {
        try {
            config.put("cloud_name", "dgmyonazl");
            config.put("api_key", "993952827512817");
            config.put("api_secret", "gR-5rzbkJgWTIWnnz0Om0mpI6nE");
            MediaManager.init(this, config);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void requestPermission() {
        if(ContextCompat.checkSelfPermission(AddImageAdvertise.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            selectImages();
        }
        else{
            ActivityCompat.requestPermissions(AddImageAdvertise.this, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE
            }, IMAGE_REQUEST);
        }
    }

    private void selectImages() {
        Intent intentSelect = new Intent();
        intentSelect.setType("image/*");
        intentSelect.setAction(Intent.ACTION_GET_CONTENT);
        someActivityResultLauncher.launch(intentSelect);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == IMAGE_REQUEST && requestCode == Activity.RESULT_OK && data != null && data.getData()!=null){
            imagePath = data.getData();
            Picasso.get().load(imagePath).into(imageUploadImages);
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
                        Picasso.get().load(imagePath).into(imageUploadImages);
                    }
                }
            });

    private void mapping() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_qc);
        imageUploadImages = (ImageView) findViewById(R.id.themanhqc);
        btnChonImages = (Button) findViewById(R.id.buttonAddImagesQc);
    }

}