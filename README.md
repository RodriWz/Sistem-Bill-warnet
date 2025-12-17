# Sistem Bill Warnet

## Deskripsi
Sistem Bill Warnet merupakan aplikasi berbasis **Java** yang dibuat untuk memenuhi **tugas kuliah**.  
Aplikasi ini digunakan untuk menghitung durasi pemakaian komputer di warnet dan menentukan total biaya yang harus dibayarkan oleh pelanggan berdasarkan waktu penggunaan.

Project ini bertujuan untuk melatih pemahaman dasar pemrograman Java, logika perhitungan waktu, serta penggunaan build system **Gradle**.

## Tujuan Pembuatan
- Memenuhi tugas mata kuliah pemrograman
- Melatih kemampuan pemrograman Java
- Menerapkan logika perhitungan waktu dan biaya
- Memahami struktur project Java menggunakan Gradle

## Fitur Aplikasi
- Input waktu mulai penggunaan komputer
- Input waktu selesai penggunaan komputer
- Perhitungan durasi pemakaian
- Perhitungan total biaya warnet
- Sistem billing sederhana berbasis console

## Teknologi yang Digunakan
- **Bahasa Pemrograman** : Java  
- **Build Tool** : Gradle  
- **IDE** : IntelliJ IDEA

## Alur Program
Alur kerja aplikasi Sistem Bill Warnet adalah sebagai berikut:

1. Program dijalankan melalui class utama (`Main`)
2. Sistem meminta pengguna untuk memasukkan:
   - Waktu mulai penggunaan komputer
   - Waktu selesai penggunaan komputer
3. Program menghitung durasi pemakaian berdasarkan selisih waktu
4. Sistem menghitung total biaya dengan mengalikan durasi pemakaian dan tarif yang ditentukan
5. Hasil perhitungan ditampilkan ke layar
6. Program selesai


---

## 🔹 Cara Menjalankan Program (Penjelasan Praktis)

### 🔸 Jika Pakai IntelliJ IDEA
1. Buka **IntelliJ IDEA**
2. Pilih **Open**
3. Arahkan ke folder `Sistem-Bill-warnet`
4. Tunggu Gradle loading (pojok kanan bawah)
5. Buka folder:
6. Klik kanan file **Main.java**
7. Pilih **Run 'Main'**

---

### 🔸 Jika Program Tidak Jalan
Cek ini:
- ✔ Java sudah terinstall (JDK)
- ✔ Gradle selesai load
- ✔ File `Main.java` punya `public static void main(String[] args)`

---

## Struktur Project
```text
Sistem-Bill-warnet/
├── app/
│   └── src/
│       └── main/
│           └── java/
│               └── (kode sumber aplikasi)
├── gradle/
├── build.gradle.kts
├── settings.gradle.kts
├── gradlew
├── gradlew.bat
└── README.md

