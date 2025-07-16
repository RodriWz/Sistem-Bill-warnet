package com.example.sistembillwarnet;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.packetactivity); // layout utama kamu

        // Inisialisasi BottomNavigationView
        bottomNav = findViewById(R.id.bottom_nav);

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.menu_paket) {
                Toast.makeText(this, "Kamu sudah di halaman Paket", Toast.LENGTH_SHORT).show();
                return true;
            } else if (id == R.id.menu_pembayaran) {
                startActivity(new Intent(MainActivity.this, PembayaranActivity.class));
                return true;
            } else if (id == R.id.menu_makanan) {
                startActivity(new Intent(MainActivity.this, Makanan.class));
                return true;
            } else if (id == R.id.menu_logout) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(this, "Berhasil Logout", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                return true;
            }

            return false;
        });
    }
}
