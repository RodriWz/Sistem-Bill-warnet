package com.example.sistembillwarnet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;

public class Makanan extends AppCompatActivity {

    ImageButton btnFood, btnComputer, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.makanan);

        btnFood = findViewById(R.id.btnFood);
        btnComputer = findViewById(R.id.btnComputer);
        btnLogout = findViewById(R.id.btnLogout);

        // Jika tombol makanan diklik (misalnya tetap di halaman makanan)
        btnFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Contoh: bisa buat toast atau reload halaman makanan
                // Atau bisa ditambahkan aksi lain sesuai kebutuhan
            }
        });

        // Navigasi ke PaketActivity
        btnComputer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Makanan.this, PaketActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Logout, contoh kembali ke LoginActivity (sesuaikan dengan nama kelas login kamu)
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Makanan.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // clear stack
                startActivity(intent);
                finish();
            }
        });
    }
}
