package com.example.sistembillwarnet;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.*;

public class AdminActivity extends AppCompatActivity {

    RecyclerView recyclerStruk;
    StrukAdapter adapter;
    List<Struk> strukList;
    List<String> docIdList;
    FirebaseFirestore db;

    private Struk selectedStruk;
    private String selectedDocId;

    String[] paketList = {
            "PAKET 1 - 1 JAM - RP5.000",
            "PAKET 2 - 2 JAM - RP9.000",
            "PAKET 3 - 3 JAM - RP14.000",
            "PAKET 4 - 5 JAM - RP20.000",
            "PAKET HARIAN - 7 JAM - RP25.000",
            "PAKET ZEUS - 24 JAM - RP50.000",
            "PAKET REGULER - 10 JAM - RP60.000",
            "PAKET VIP - 10 JAM VIP - RP70.000"
    };

    String[] metodeList = {"Tunai", "Dana", "OVO", "Gopay"};
    String[] makananList = {"Indomie Goreng", "Indomie Coto", "Indomie + Telur", "Nasi Goreng", "Ayam Geprek"};
    String[] minumanList = {"Teh Pucuk", "Teh Kotak", "Freshtea", "Kopikap", "Teh Gelas"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_activity);

        Toolbar toolbar = findViewById(R.id.adminToolbar);
        setSupportActionBar(toolbar);

        recyclerStruk = findViewById(R.id.recyclerStruk);
        recyclerStruk.setLayoutManager(new LinearLayoutManager(this));

        strukList = new ArrayList<>();
        docIdList = new ArrayList<>();

        adapter = new StrukAdapter(strukList, docIdList, (struk, docId) -> {
            selectedStruk = struk;
            selectedDocId = docId;
            Toast.makeText(this, "Dipilih: " + struk.getNama(), Toast.LENGTH_SHORT).show();
        });

        recyclerStruk.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();

        db.collection("struk")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener((value, error) -> {
                    if (error != null || value == null) return;

                    strukList.clear();
                    docIdList.clear();
                    for (DocumentSnapshot doc : value.getDocuments()) {
                        Struk s = doc.toObject(Struk.class);
                        strukList.add(s);
                        docIdList.add(doc.getId());
                    }
                    adapter.notifyDataSetChanged();
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admin_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return true;
        } else if (id == R.id.menu_edit) {
            showEditDialog();
            return true;
        } else if (id == R.id.menu_delete) {
            if (selectedDocId == null) {
                Toast.makeText(this, "Pilih item dahulu", Toast.LENGTH_SHORT).show();
            } else {
                db.collection("struk").document(selectedDocId)
                        .delete()
                        .addOnSuccessListener(unused -> Toast.makeText(this, "Struk dihapus", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e -> Toast.makeText(this, "Gagal hapus: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showEditDialog() {
        if (selectedStruk == null || selectedDocId == null) {
            Toast.makeText(this, "Pilih item dahulu", Toast.LENGTH_SHORT).show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Struk");

        EditText inputNama = new EditText(this);
        inputNama.setHint("Nama");
        inputNama.setText(selectedStruk.getNama());

        Spinner spinnerPaket = new Spinner(this);
        Spinner spinnerMetode = new Spinner(this);
        Spinner spinnerMakanan = new Spinner(this);
        Spinner spinnerMinuman = new Spinner(this);

        spinnerPaket.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, paketList));
        spinnerMetode.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, metodeList));
        spinnerMakanan.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, makananList));
        spinnerMinuman.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, minumanList));

        // Set default pilihan
        setSpinnerSelection(spinnerPaket, selectedStruk.getPaket());
        setSpinnerSelection(spinnerMetode, selectedStruk.getMetode());
        setSpinnerSelection(spinnerMakanan, selectedStruk.getMakanan());
        setSpinnerSelection(spinnerMinuman, selectedStruk.getMinuman());

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        int pad = (int) getResources().getDimension(android.R.dimen.app_icon_size) / 2;
        layout.setPadding(pad, pad, pad, pad);
        layout.addView(inputNama);
        layout.addView(spinnerPaket);
        layout.addView(spinnerMetode);
        layout.addView(spinnerMakanan);
        layout.addView(spinnerMinuman);

        builder.setView(layout);

        builder.setPositiveButton("Simpan", (dialog, which) -> {
            String nama = inputNama.getText().toString().trim();
            String paket = spinnerPaket.getSelectedItem().toString();
            String metode = spinnerMetode.getSelectedItem().toString();
            String makanan = spinnerMakanan.getSelectedItem().toString();
            String minuman = spinnerMinuman.getSelectedItem().toString();

            if (nama.isEmpty()) {
                Toast.makeText(this, "Nama tidak boleh kosong", Toast.LENGTH_SHORT).show();
                return;
            }

            Map<String, Object> update = new HashMap<>();
            update.put("nama", nama);
            update.put("paket", paket);
            update.put("metode", metode);
            update.put("makanan", makanan);
            update.put("minuman", minuman);

            db.collection("struk").document(selectedDocId)
                    .update(update)
                    .addOnSuccessListener(unused -> Toast.makeText(this, "Struk berhasil diedit", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(this, "Gagal edit: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        });

        builder.setNegativeButton("Batal", (dialog, which) -> dialog.dismiss());

        builder.setNeutralButton("Aktifkan", (dialog, which) -> {
            db.collection("struk").document(selectedDocId)
                    .update("status", "aktif")
                    .addOnSuccessListener(unused -> Toast.makeText(this, "Paket telah diaktifkan!", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(this, "Gagal mengaktifkan: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        });

        builder.show();
    }

    private void setSpinnerSelection(Spinner spinner, String value) {
        ArrayAdapter adapter = (ArrayAdapter) spinner.getAdapter();
        for (int i = 0; i < adapter.getCount(); i++) {
            if (adapter.getItem(i).toString().equalsIgnoreCase(value)) {
                spinner.setSelection(i);
                break;
            }
        }
    }
}
