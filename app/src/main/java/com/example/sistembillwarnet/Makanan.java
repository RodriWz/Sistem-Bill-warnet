package com.example.sistembillwarnet;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class Makanan extends AppCompatActivity {

    BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.makanan); // layout yang kamu gunakan

        bottomNav = findViewById(R.id.bottom_nav);

        // Set menu yang aktif
        bottomNav.setSelectedItemId(R.id.menu_makanan);

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.menu_paket) {
                startActivity(new Intent(Makanan.this, MainActivity.class));
                return true;
            } else if (id == R.id.menu_pembayaran) {
                startActivity(new Intent(Makanan.this, PembayaranActivity.class));
                return true;
            } else if (id == R.id.menu_makanan) {
                Toast.makeText(this, "Kamu sudah di halaman Makanan", Toast.LENGTH_SHORT).show();
                return true;
            } else if (id == R.id.menu_logout) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(Makanan.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                return true;
            }

            return false;
        });
    }
}
