package com.example.sistembillwarnet;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseFirestore db;
    EditText email, password;
    Button btnLogin, btnToRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btnLogin = findViewById(R.id.btnLogin);
        btnToRegister = findViewById(R.id.btnToRegister);
        TextView title = findViewById(R.id.titleText);
        Animation blink = AnimationUtils.loadAnimation(this, R.anim.blink);
        title.startAnimation(blink);

        btnLogin.setOnClickListener(v -> {
            String em = email.getText().toString().trim();
            String pw = password.getText().toString().trim();

            if (em.isEmpty() || pw.isEmpty()) {
                Toast.makeText(this, "Email dan password wajib diisi", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.signInWithEmailAndPassword(em, pw)
                    .addOnSuccessListener(authResult -> {
                        FirebaseUser currentUser = mAuth.getCurrentUser();
                        if (currentUser != null) {
                            String userId = currentUser.getUid();

                            db.collection("users").document(userId).get()
                                    .addOnSuccessListener(documentSnapshot -> {
                                        if (documentSnapshot.exists()) {
                                            String role = documentSnapshot.getString("role");

                                            if (role == null) {
                                                Toast.makeText(this, "Role tidak ditemukan di database", Toast.LENGTH_SHORT).show();
                                                return;
                                            }

                                            if (role.equals("admin")) {
                                                startActivity(new Intent(this, AdminActivity.class));
                                            } else if (role.equals("user")) {
                                                // Simpan nama pengguna ke SharedPreferences
                                                String namaUser = em.split("@")[0].toLowerCase();
                                                SharedPreferences pref = getSharedPreferences("user_pref", MODE_PRIVATE);
                                                pref.edit().putString("nama", namaUser).apply();

                                                startActivity(new Intent(this, MainActivity.class));
                                            } else {
                                                Toast.makeText(this, "Role tidak dikenali: " + role, Toast.LENGTH_SHORT).show();
                                            }

                                            finish();
                                        } else {
                                            Toast.makeText(this, "Dokumen user tidak ditemukan", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(this, "Gagal mengambil role: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    });
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Login gagal: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });

        btnToRegister.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });
    }
}
