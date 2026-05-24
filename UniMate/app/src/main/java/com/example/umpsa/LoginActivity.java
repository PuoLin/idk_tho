package com.example.umpsa;

import android.app.Activity;
import android.content.Intent;
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

public class LoginActivity extends Activity {

    EditText editEmail, editPassword;
    RadioButton radioStudent, radioTeacher;
    TextView btnLogin, txtSignup;

    private final String STUDENT_LOGIN_URL = "http://103.40.207.48/api/student_login.php";
    private final String LECTURER_LOGIN_URL = "http://103.40.207.48/api/lecturer_login.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editEmail = findViewById(R.id.editEmail);
        editPassword = findViewById(R.id.editPassword);
        radioStudent = findViewById(R.id.radioStudent);
        radioTeacher = findViewById(R.id.radioTeacher);
        btnLogin = findViewById(R.id.btnLogin);
        txtSignup = findViewById(R.id.txtSignup);

        txtSignup.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        btnLogin.setOnClickListener(v -> {
            String email = editEmail.getText().toString().trim();
            String password = editPassword.getText().toString().trim();

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

            if (!radioStudent.isChecked() && !radioTeacher.isChecked()) {
                Toast.makeText(this, "Please choose Student or Teacher", Toast.LENGTH_SHORT).show();
                return;
            }

            if (radioStudent.isChecked()) {
                loginUser(email, password, STUDENT_LOGIN_URL);
            } else {
                loginUser(email, password, LECTURER_LOGIN_URL);
            }
        });
    }

    private void loginUser(String email, String password, String loginUrl) {
        new Thread(() -> {
            try {
                URL url = new URL(loginUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setConnectTimeout(10000);
                connection.setReadTimeout(10000);

                String postData = "email=" + URLEncoder.encode(email, "UTF-8")
                        + "&password=" + URLEncoder.encode(password, "UTF-8");

                OutputStream outputStream = connection.getOutputStream();
                outputStream.write(postData.getBytes());
                outputStream.flush();
                outputStream.close();

                int responseCode = connection.getResponseCode();

                BufferedReader reader;

                if (responseCode >= 200 && responseCode < 300) {
                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                } else {
                    reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                }

                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                reader.close();
                connection.disconnect();

                JSONObject jsonObject = new JSONObject(response.toString());
                String status = jsonObject.getString("status");

                runOnUiThread(() -> {
                    try {
                        if (status.equals("success")) {
                            String role = jsonObject.getString("role");
                            String name = jsonObject.getString("name");

                            Toast.makeText(LoginActivity.this, "Welcome " + name, Toast.LENGTH_SHORT).show();

                            Intent intent;

                            if (role.equals("lecturer")) {
                                intent = new Intent(LoginActivity.this, LecturerActivity.class);
                            } else {
                                intent = new Intent(LoginActivity.this, MainActivity.class);
                            }

                            startActivity(intent);
                            finish();

                        } else {
                            Toast.makeText(LoginActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        Toast.makeText(LoginActivity.this, "Login response error", Toast.LENGTH_SHORT).show();
                    }
                });

            } catch (Exception e) {
                runOnUiThread(() ->
                        Toast.makeText(LoginActivity.this, "Server connection failed", Toast.LENGTH_SHORT).show()
                );
            }
        }).start();
    }
}