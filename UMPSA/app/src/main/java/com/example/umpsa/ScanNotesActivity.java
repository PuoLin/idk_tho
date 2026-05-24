package com.example.umpsa;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ScanNotesActivity extends Activity {

    private static final int REQUEST_IMAGE_CAPTURE = 100;

    TextView btnBack;
    TextView btnTakePicture;
    TextView txtScanStatus;
    TextView txtCameraIcon;
    ImageView imagePreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_notes);

        btnBack = findViewById(R.id.btnBack);
        btnTakePicture = findViewById(R.id.btnTakePicture);
        txtScanStatus = findViewById(R.id.txtScanStatus);
        txtCameraIcon = findViewById(R.id.txtCameraIcon);
        imagePreview = findViewById(R.id.imagePreview);

        btnBack.setOnClickListener(v -> finish());

        btnTakePicture.setOnClickListener(v -> openCamera());
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
        } else {
            Toast.makeText(this, "Camera app not found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null) {
            Bundle extras = data.getExtras();

            if (extras != null) {
                Bitmap imageBitmap = (Bitmap) extras.get("data");

                if (imageBitmap != null) {
                    imagePreview.setImageBitmap(imageBitmap);
                    imagePreview.setVisibility(ImageView.VISIBLE);
                    txtCameraIcon.setVisibility(TextView.GONE);
                    txtScanStatus.setText("Photo captured. OCR processing can be added later.");
                }
            }
        }
    }
}