package com.example.sistembillwarnet;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class StrukAdapter extends RecyclerView.Adapter<StrukAdapter.ViewHolder> {

    private List<Struk> strukList;
    private List<String> docIdList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Struk struk, String docId);
    }

    public StrukAdapter(List<Struk> strukList, List<String> docIdList, OnItemClickListener listener) {
        this.strukList = strukList;
        this.docIdList = docIdList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public StrukAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_struk, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StrukAdapter.ViewHolder holder, int position) {
        Struk s = strukList.get(position);
        String docId = docIdList.get(position);

        holder.nama.setText("Nama: " + s.getNama());
        holder.paket.setText("Paket: " + s.getPaket());
        holder.metode.setText("Metode: " + s.getMetode());
        holder.makanan.setText("Makanan: " + s.getMakanan());
        holder.minuman.setText("Minuman: " + s.getMinuman());

        holder.btnAktifkan.setOnClickListener(v -> {
            FirebaseFirestore.getInstance()
                    .collection("struk")
                    .document(docId)
                    .update("status", "aktif")
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(holder.itemView.getContext(),
                                "✅ Paket " + s.getNama() + " telah diaktifkan",
                                Toast.LENGTH_SHORT).show();
                        holder.btnAktifkan.setEnabled(false);
                    })
                    .addOnFailureListener(e -> Toast.makeText(holder.itemView.getContext(),
                            "❌ Gagal mengaktifkan: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show());
        });

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(s, docId);
            }
        });
    }

    @Override
    public int getItemCount() {
        return strukList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nama, paket, metode, makanan, minuman;
        Button btnAktifkan;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nama = itemView.findViewById(R.id.txtNama);
            paket = itemView.findViewById(R.id.txtPaket);
            metode = itemView.findViewById(R.id.txtMetode);
            makanan = itemView.findViewById(R.id.txtMakanan);
            minuman = itemView.findViewById(R.id.txtMinuman);
            btnAktifkan = itemView.findViewById(R.id.btnAktifkan);
        }
    }
}
