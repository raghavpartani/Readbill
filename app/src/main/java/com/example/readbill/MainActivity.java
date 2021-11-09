package com.example.readbill;

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
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;


public class MainActivity extends AppCompatActivity {
    ImageView iv;
    Button btnGallery,btnCamera,btnNewApp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv=findViewById(R.id.imageView);
        btnCamera=findViewById(R.id.btnCamera);
        btnGallery=findViewById(R.id.btnGal);


        btnGallery.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "gd", Toast.LENGTH_SHORT).show();
                if (ActivityCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) !=
                        PermissionChecker.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new
                            String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 112);
                }
                else
                {
                    Intent in=new Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(in,112);
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
        if(requestCode==112 && data!=null)
        {
            Uri uri= data.getData();
            iv.setImageURI(uri);
        }
        if(requestCode==113 && data !=null)
        {
            Bitmap bmp=(Bitmap)data.getExtras().get("data");
            iv.setImageBitmap(bmp);
            TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();

            Frame imageFrame = new Frame.Builder()

                    .setBitmap(bmp)                 // your image bitmap
                    .build();

            String imageText = "";


            SparseArray<TextBlock> textBlocks = textRecognizer.detect(imageFrame);

            for (int i = 0; i < textBlocks.size(); i++) {
                TextBlock textBlock = textBlocks.get(textBlocks.keyAt(i));
                imageText = textBlock.getValue();                   // return string
            }
            Toast.makeText(this, ""+imageText, Toast.LENGTH_SHORT).show();
        }
    }
}