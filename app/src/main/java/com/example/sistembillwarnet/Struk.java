package com.example.sistembillwarnet;

public class Struk {
    private String nama;
    private String paket;
    private String metode;
    private String makanan;
    private String minuman;
    private String status;
    private long timestamp;

    public Struk() {
        // Diperlukan oleh Firestore
    }

    public Struk(String nama, String paket, String metode, String makanan, String minuman, String status, long timestamp) {
        this.nama = nama;
        this.paket = paket;
        this.metode = metode;
        this.makanan = makanan;
        this.minuman = minuman;
        this.status = status;
        this.timestamp = timestamp;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getPaket() {
        return paket;
    }

    public void setPaket(String paket) {
        this.paket = paket;
    }

    public String getMetode() {
        return metode;
    }

    public void setMetode(String metode) {
        this.metode = metode;
    }

    public String getMakanan() {
        return makanan;
    }

    public void setMakanan(String makanan) {
        this.makanan = makanan;
    }

    public String getMinuman() {
        return minuman;
    }

    public void setMinuman(String minuman) {
        this.minuman = minuman;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
