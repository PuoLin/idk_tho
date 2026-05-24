package com.example.umpsa;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class RegisterActivity extends Activity {

    EditText editRegisterName, editRegisterEmail, editRegisterPassword;
    RadioButton radioRegisterStudent, radioRegisterLecturer;
    TextView btnRegister, btnBackToLogin;

    private final String REGISTER_URL = "http://103.40.207.48/api/register.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editRegisterName = findViewById(R.id.editRegisterName);
        editRegisterEmail = findViewById(R.id.editRegisterEmail);
        editRegisterPassword = findViewById(R.id.editRegisterPassword);

        radioRegisterStudent = findViewById(R.id.radioRegisterStudent);
        radioRegisterLecturer = findViewById(R.id.radioRegisterLecturer);

        btnRegister = findViewById(R.id.btnRegister);
        btnBackToLogin = findViewById(R.id.btnBackToLogin);

        btnBackToLogin.setOnClickListener(v -> finish());

        btnRegister.setOnClickListener(v -> {
            String name = editRegisterName.getText().toString().trim();
            String email = editRegisterEmail.getText().toString().trim();
            String password = editRegisterPassword.getText().toString().trim();

            if (name.isEmpty()) {
                Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show();
                return;
            }

            if (email.isEmpty()) {
                Toast.makeText(this, "Please enter education email", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!email.contains(".edu")) {
                Toast.makeText(this, "Please use an education email", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.isEmpty()) {
                Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
                return;
            }

            String role;

            if (radioRegisterStudent.isChecked()) {
                role = "student";
            } else {
                role = "lecturer";
            }

            registerUser(name, email, password, role);
        });
    }

    private void registerUser(String name, String email, String password, String role) {
        new Thread(() -> {
            try {
                URL url = new URL(REGISTER_URL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setConnectTimeout(10000);
                connection.setReadTimeout(10000);

                String postData =
                        "name=" + URLEncoder.encode(name, "UTF-8") +
                                "&email=" + URLEncoder.encode(email, "UTF-8") +
                                "&password=" + URLEncoder.encode(password, "UTF-8") +
                                "&role=" + URLEncoder.encode(role, "UTF-8");

                OutputStream outputStream = connection.getOutputStream();
                outputStream.write(postData.getBytes());
                outputStream.flush();
                outputStream.close();

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream())
                );

                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                reader.close();
                connection.disconnect();

                JSONObject jsonObject = new JSONObject(response.toString());
                String status = jsonObject.getString("status");
                String message = jsonObject.getString("message");

                runOnUiThread(() -> {
                    if (status.equals("success")) {
                        Toast.makeText(RegisterActivity.this, "Account created successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                });

            } catch (Exception e) {
                runOnUiThread(() ->
                        Toast.makeText(RegisterActivity.this, "Server connection failed", Toast.LENGTH_SHORT).show()
                );
            }
        }).start();
    }
}