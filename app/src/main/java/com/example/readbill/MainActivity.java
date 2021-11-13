package com.example.readbill;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {
    ImageView iv;
    Button btnGallery, btnCamera, btnNewApp;
    TextView tv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv = findViewById(R.id.imageView);
        btnCamera = findViewById(R.id.btnCamera);
        btnGallery = findViewById(R.id.btnGal);
        tv1 = findViewById(R.id.text);


        btnGallery.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "gd", Toast.LENGTH_SHORT).show();
                if (ActivityCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) !=
                        PermissionChecker.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new
                            String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 112);
                } else {
                    Intent in = new Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(in, 112);
                }
            }
        });

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (ActivityCompat.checkSelfPermission(MainActivity.this,
//                        Manifest.permission.CAMERA) !=
//                        PermissionChecker.PERMISSION_GRANTED) {
//                    ActivityCompat.requestPermissions(MainActivity.this, new
//                            String[]{Manifest.permission.CAMERA}, 11);
//                }
//                else {
                Intent in =
                        new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(in, 113);
            }
            //}
        });

    }

    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 112 && data != null) {
            Uri uri = data.getData();
            try {
                Bitmap bmp = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bmp);
                FirebaseVisionTextRecognizer textRecognizer = FirebaseVision.getInstance()
                        .getOnDeviceTextRecognizer();

                Task<FirebaseVisionText> result =
                        textRecognizer.processImage(image)
                                .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                                    @Override
                                    public void onSuccess(FirebaseVisionText firebaseVisionText) {

                                        tv1.setText(firebaseVisionText.getText());
                                        Toast.makeText(MainActivity.this, ""+firebaseVisionText.getText(), Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(
                                        new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                //process failure
                                            }
                                        });

            } catch (IOException e) {
                e.printStackTrace();
            }
            iv.setImageURI(uri);
        }
        if (requestCode == 113 && data != null) {
            Bitmap bmp = (Bitmap) data.getExtras().get("data");
            iv.setImageBitmap(bmp);
            FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bmp);
            FirebaseVisionTextRecognizer textRecognizer = FirebaseVision.getInstance()
                    .getOnDeviceTextRecognizer();

            Task<FirebaseVisionText> result =
                    textRecognizer.processImage(image)
                            .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                                @Override
                                public void onSuccess(FirebaseVisionText firebaseVisionText) {

                                    tv1.setText(firebaseVisionText.getText());
                                    Toast.makeText(MainActivity.this, ""+firebaseVisionText.getText(), Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(
                                    new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            //process failure
                                        }
                                    });
        }
    }
}