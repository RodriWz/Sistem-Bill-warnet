package com.example.sistembillwarnet;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;

public class PembayaranActivity extends AppCompatActivity {

    private EditText inputNama;
    private Spinner spinnerPaket, spinnerMetode, spinnerMakanan, spinnerMinuman;
    private TextView countdownTimer, summaryInfo;
    private Button btnKonfirmasi;
    private BottomNavigationView bottomNav;
    private CountDownTimer timer;

    private final String[] paketOptions = {
            "PAKET 1 - 1 JAM - RP5.000",
            "PAKET 2 - 2 JAM - RP9.000",
            "PAKET 3 - 3 JAM - RP14.000",
            "PAKET 4 - 5 JAM - RP20.000",
            "PAKET HARIAN - 7 JAM - RP25.000",
            "PAKET ZEUS - 24 JAM - RP50.000",
            "PAKET REGULER - 10 JAM - RP60.000",
            "PAKET VIP - 10 JAM VIP - RP70.000"
    };
    private final String[] metodeOptions = {"Tunai", "Dana", "OVO", "Gopay"};
    private final String[] makananOptions = {"Indomie Goreng", "Indomie Coto", "Indomie + Telur", "Nasi Goreng", "Ayam Geprek"};
    private final String[] minumanOptions = {"Teh Pucuk", "Teh Kotak", "Freshtea", "Kopikap", "Teh Gelas"};

    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pembayaran);

        db = FirebaseFirestore.getInstance();

        initViews();
        setupSpinners();
        setupBottomNavigation();

        SharedPreferences pref = getSharedPreferences("user_pref", MODE_PRIVATE);
        String savedName = pref.getString("nama", null);
        if (savedName != null) {
            inputNama.setText(savedName);
            listenStatusUpdate(savedName);
        }

        btnKonfirmasi.setOnClickListener(v -> handleKonfirmasi());
    }

    private void initViews() {
        inputNama = findViewById(R.id.inputNama);
        spinnerPaket = findViewById(R.id.spinnerPaket);
        spinnerMetode = findViewById(R.id.spinnerMetode);
        spinnerMakanan = findViewById(R.id.spinnerMakanan);
        spinnerMinuman = findViewById(R.id.spinnerMinuman);
        countdownTimer = findViewById(R.id.countdownTimer);
        summaryInfo = findViewById(R.id.summaryInfo);
        btnKonfirmasi = findViewById(R.id.btnKonfirmasi);
        bottomNav = findViewById(R.id.bottom_nav);
    }

    private void setupSpinners() {
        spinnerPaket.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, paketOptions));
        spinnerMetode.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, metodeOptions));
        spinnerMakanan.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, makananOptions));
        spinnerMinuman.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, minumanOptions));
    }
    private void setupBottomNavigation() {
        bottomNav.setSelectedItemId(R.id.menu_pembayaran);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.menu_paket) {
                startActivity(new Intent(this, MainActivity.class));
                return true;
            } else if (id == R.id.menu_pembayaran) {
                return true;
            } else if (id == R.id.menu_makanan) {
                startActivity(new Intent(this, Makanan.class));
                return true;
            } else if (id == R.id.menu_logout) {
                getSharedPreferences("user_pref", MODE_PRIVATE).edit().clear().apply();
                Intent i = new Intent(this, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                return true;
            } else {
                return false;
            }
        });
    }


    private void handleKonfirmasi() {
        String nama = inputNama.getText().toString().trim().toLowerCase();
        if (nama.isEmpty()) {
            Toast.makeText(this, "Nama tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences.Editor editor = getSharedPreferences("user_pref", MODE_PRIVATE).edit();
        editor.putString("nama", nama);
        editor.apply();

        String paket = spinnerPaket.getSelectedItem().toString();
        String metode = spinnerMetode.getSelectedItem().toString();
        String makanan = spinnerMakanan.getSelectedItem().toString();
        String minuman = spinnerMinuman.getSelectedItem().toString();

        summaryInfo.setText("🧾 " + nama + " memilih\n" + paket + "\nMetode: " + metode + "\nMakanan: " + makanan + "\nMinuman: " + minuman);

        Map<String, Object> data = new HashMap<>();
        data.put("nama", nama);
        data.put("paket", paket);
        data.put("metode", metode);
        data.put("makanan", makanan);
        data.put("minuman", minuman);
        data.put("status", "pending");
        data.put("timestamp", System.currentTimeMillis());

        db.collection("struk")
                .add(data)
                .addOnSuccessListener(docRef -> {
                    Toast.makeText(this, "Struk dikirim. Menunggu aktivasi admin...", Toast.LENGTH_SHORT).show();
                    listenStatusUpdate(nama);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Gagal mengirim: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void listenStatusUpdate(String nama) {
        db.collection("struk")
                .whereEqualTo("nama", nama)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(1)
                .addSnapshotListener((snapshots, e) -> {
                    if (e != null || snapshots == null || snapshots.isEmpty()) return;

                    DocumentSnapshot doc = snapshots.getDocuments().get(0);
                    String status = doc.getString("status");
                    String paket = doc.getString("paket");

                    if ("aktif".equalsIgnoreCase(status)) {
                        int jam = getDurasiFromPaket(paket);
                        startCountdown(jam);
                    }
                });
    }

    private void startCountdown(int jam) {
        long millis = jam * 60L * 60L * 1000L;

        if (timer != null) timer.cancel();

        btnKonfirmasi.setEnabled(false);
        inputNama.setEnabled(false);
        spinnerPaket.setEnabled(false);
        spinnerMetode.setEnabled(false);
        spinnerMakanan.setEnabled(false);
        spinnerMinuman.setEnabled(false);

        timer = new CountDownTimer(millis, 1000) {
            public void onTick(long millisUntilFinished) {
                long s = (millisUntilFinished / 1000) % 60;
                long m = (millisUntilFinished / (1000 * 60)) % 60;
                long h = (millisUntilFinished / (1000 * 60 * 60));
                countdownTimer.setText(String.format("%02d:%02d:%02d", h, m, s));
            }

            public void onFinish() {
                countdownTimer.setText("⛔ Sesi selesai.");
                Toast.makeText(PembayaranActivity.this, "Waktu habis. Silakan input ulang.", Toast.LENGTH_LONG).show();
                resetForm();
            }
        };
        timer.start();
    }

    private void resetForm() {
        getSharedPreferences("user_pref", MODE_PRIVATE).edit().clear().apply();
        inputNama.setText("");
        summaryInfo.setText("🧾 Ringkasan: Belum dipilih");
        countdownTimer.setText("");
        btnKonfirmasi.setEnabled(true);
        inputNama.setEnabled(true);
        spinnerPaket.setEnabled(true);
        spinnerMetode.setEnabled(true);
        spinnerMakanan.setEnabled(true);
        spinnerMinuman.setEnabled(true);
    }

    private int getDurasiFromPaket(String paket) {
        if (paket == null) return 1;
        paket = paket.toUpperCase();

        if (paket.contains("PAKET 1")) return 1;
        if (paket.contains("PAKET 2")) return 2;
        if (paket.contains("PAKET 3")) return 3;
        if (paket.contains("PAKET 4")) return 5;
        if (paket.contains("HARIAN")) return 7;
        if (paket.contains("ZEUS")) return 24;
        if (paket.contains("REGULER")) return 10;
        if (paket.contains("VIP")) return 10;

        return 1;
    }
}
