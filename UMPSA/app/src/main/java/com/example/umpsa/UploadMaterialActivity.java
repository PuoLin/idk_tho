package com.example.umpsa;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.widget.TextView;
import android.widget.Toast;

public class UploadMaterialActivity extends Activity {

    private static final int REQUEST_PICK_FILE = 200;

    TextView btnBack;
    TextView btnUploadFile;
    TextView txtSelectedFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_material);

        btnBack = findViewById(R.id.btnBack);
        btnUploadFile = findViewById(R.id.btnUploadFile);
        txtSelectedFile = findViewById(R.id.txtSelectedFile);

        btnBack.setOnClickListener(v -> finish());

        btnUploadFile.setOnClickListener(v -> openFilePicker());
    }

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        intent.setType("*/*");

        String[] mimeTypes = {
                "application/pdf",
                "application/vnd.ms-powerpoint",
                "application/vnd.openxmlformats-officedocument.presentationml.presentation",
                "image/jpeg",
                "image/png"
        };

        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);

        startActivityForResult(intent, REQUEST_PICK_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_PICK_FILE && resultCode == RESULT_OK && data != null) {
            Uri fileUri = data.getData();

            if (fileUri != null) {
                String fileName = getFileName(fileUri);
                txtSelectedFile.setText(fileName);
                Toast.makeText(this, "File selected. AI extraction can be added later.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String getFileName(Uri uri) {
        String result = "Selected file";

        Cursor cursor = getContentResolver().query(uri, null, null, null, null);

        if (cursor != null) {
            int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);

            if (nameIndex >= 0 && cursor.moveToFirst()) {
                result = cursor.getString(nameIndex);
            }

            cursor.close();
        }

        return result;
    }
}